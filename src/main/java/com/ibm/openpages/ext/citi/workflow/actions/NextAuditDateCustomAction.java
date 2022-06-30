/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * � Copyright IBM Corporation 2021
 *
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.citi.workflow.actions;

import static com.ibm.openpages.ext.citi.workflow.constant.NextAuditDateConstants.*;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isSetNullOrEmpty;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IDateField;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.tss.service.beans.SuperUserInformation;
import com.ibm.openpages.ext.tss.service.logging.EXTLoggerFactoryImpl;
import com.ibm.openpages.ext.tss.workflow.action.BaseWorkflowCustomAction;

/**
 * 
    Calculate "Last Audit Date" on AE (AE:OPSS-AudEnt:Previous Audit Completed)
    
    1) If Audit Type (AuditProgram:OPSS-Aud:Type) = Risk Based, then find all Auditable entities where:
    
    AEApp Scope (Citi_AEApp:Citi-AEApp:Scope) = In (Path: Auditable Entity -> AEApp -> Audit) AND
    AE Type (AuditableEntity:OPSS-AudEnt:Type) = Risk Based AND AE Status (AuditableEntity:Citi-AE:EntStatus = Active
    If Audit Type is not Risk Based, skip
    
    2) For each of these AEs, find child Audit Mapping Report (Citi_AudMpReport ) where:
    
    AMR type (Citi_AudMpReport:Citi-AMR:Audit Report Type) =  Risk Based
    2.1: For each of these AMRs, find the AMR wth the latest Actual Report Publication Date  (Citi_AudMpReport:Citi-AMR:ARPubDate)
    
    If there is only 1 AMR, use the date on that AMR.
    
    2.2: set "Last Audit Date" on AE (AE:OPSS-AudEnt:Previous Audit Completed) = the latest date from 2.1
 *
 */
public class NextAuditDateCustomAction extends BaseWorkflowCustomAction {

    // Class Level Variables
    private Log logger = null;
    private IServiceFactory serviceFactory = null;
    private IQueryService queryService = null;

    /**
     * Constructor implementation for the AbstractCustomAction
     *
     * @param context
     * @param properties
     */
    public NextAuditDateCustomAction(IWFOperationContext context, List<IWFCustomProperty> properties) {

        super(context, properties);
    }

    /**
     * Default process method for the workflow to implement the increment logic.
     */
    @Override
    protected void process() throws Exception {

        // Method Level Variables
        String aeAppScopeValue = null;
        String aeTypeValue = null;
        String aeStatusValue = null;
        String amrTypeValue = null;

        IGRCObject auditableEntityObj = null;
        IGRCObject auditObj = null;
        IGRCObject amrObj = null;
        IWFOperationContext context = null;

        try {

            /* Initialize required services */
            initFieldUtilServices();
            initGRCObjectUtilServices();

            // Initialize
            logger = new EXTLoggerFactoryImpl().getInstance(LOG_FILE_NAME);
            logger.debug("\nStarting NextAuditDateCustomAction...");

            initApplicationUtilServices();


            /* Get the workflow context object, log it for information */
            context = getContext();
            auditObj = context.getResource();
            logger.info("Object Name [" + auditObj.getName() + "], Id [" + auditObj.getId() + "] and Object Type [" + auditObj.getType().getName() + "]");

            serviceFactory = context.getServiceFactory();
            queryService = serviceFactory.createQueryService();


            //Get the Next Audit Date Custom Action properties
            aeAppScopeValue = getPropertyValue(AE_APP_SCOPE_VALUE_PROPERTY);
            aeTypeValue = getPropertyValue(AUDIT_ENTITY_TYPE_VALUE_PROPERTY);
            aeStatusValue = getPropertyValue(AUDIT_ENTITY_STATUS_VALUE_PROPERTY);
            amrTypeValue = getPropertyValue(AMR_TYPE_VALUE_PROPERTY);
            logger.info("aeAppScopeValue : " + aeAppScopeValue);
            logger.info("aeTypeValue : " + aeTypeValue);
            logger.info("aeStatusValue : " + aeStatusValue);
            logger.info("amrTypeValue : " + amrTypeValue);

            String aeQuery = AUDITABLE_ENTITY_APP_QUERY.replace(AE_TYPE_VALUE, aeTypeValue).replace(AE_STATUS_VALUE, aeStatusValue).replace(AEAPP_SCOPE_VALUE, aeAppScopeValue).replace(AuditProgram_ID, auditObj.getId().toString());
            logger.info("Auditable Entity Query : " + aeQuery);

            Set<String> aeSet = new HashSet<String>();
            aeSet = returnQueryResultsSet(aeQuery);
            logger.info("Active and In-scope AEs for the Audit : " + aeSet);

            if (isSetNullOrEmpty(aeSet)) {

                logger.info("AE Set is empty or null!");
                return;
            }

            for (String aeId : aeSet) {

                auditableEntityObj = serviceFactory.createResourceService().getGRCObject(new Id(aeId));
                logger.info("Auditable Entity Name [" + auditableEntityObj.getName() + "], Id [" + auditableEntityObj.getId()+ "]");

                String amrQuery = AMR_QUERY.replace(AMR_TYPE_VALUE, amrTypeValue).replace(AuditableEntity_ID, aeId);
                logger.info("AMR Query : " + amrQuery);

                Set<String> amrSet = new HashSet<String>();
                amrSet = returnQueryResultsSet(amrQuery);
                logger.info("AMR Object Set for AE : " + amrSet);

                if (isSetNullOrEmpty(amrSet)) {

                    logger.info("AMR Set is null or empty.");
                    continue;
                }

                Date latestDate = null;
                Date currentDate = null;
                for (String amrId : amrSet) {

                    amrObj = serviceFactory.createResourceService().getGRCObject(new Id(amrId));
                    logger.info("AMR Name [" + amrObj.getName() + "], Id [" + amrObj.getId() + "]");

                    IField field = amrObj.getField(AMR_PUBLICATION_DATE_FIELD);
                    if (field instanceof IDateField)
                    {
                        IDateField dateField = (IDateField) field;
                        currentDate = isObjectNull(dateField) ? null : dateField.getValue();
                        logger.info("Current AMR's Actual Report Publication Date : " + currentDate);
                        logger.info(" Latest AMR's Actual Report Publication Date : " + latestDate);

                        if (!isObjectNull(currentDate) && isObjectNull(latestDate)) {
                            logger.info("Case 1");
                            latestDate = currentDate;
                        }else if (!isObjectNull(currentDate) && !isObjectNull(latestDate) && latestDate.before(currentDate)) {
                            latestDate = currentDate;
                            logger.info("Case 2");
                        }

                    } else {
                        logger.info(AMR_PUBLICATION_DATE_FIELD + " is not a date field!");
                        continue;
                    }

                    logger.info("AMR's Actual Report Publication Date : " + latestDate);
                }

                if (!isObjectNull(latestDate)) {
                    //fieldUtil.setFieldValue(auditableEntityObj, AUD_ENTITY_LAST_AUDIT_DATE_FIELD, latestDate.toString());
                    //serviceFactory.createResourceService().saveResource(auditableEntityObj);
                    //logger.info("[" + auditableEntityObj.getName() + "], Id [" + auditableEntityObj.getId() + "] updated with [" + latestDate + "]");
                    updateGRCObjectAsSuperUser(auditableEntityObj, latestDate);
                }else {
                    logger.info("AMR's Actual Report Publication Date is null!");
                }

            }
        }
        catch (Exception ex) {

            logger.error(
                    "EXCEPTION!!!!!!!!!!! An exception occurred while running the NextAuditDateCustomAction : " + getStackTrace(ex));
        }

    }

    /**
     * <P>
     * This method returns the query results in set
     * </P>
     *
     * @param query
     *
     * @return - the Set of resource Id from query
     * @throws Exception
     */
    private Set<String> returnQueryResultsSet(String query_string) throws Exception {

        //logger.info("returnQueryResultsSet() Start");
        IQuery query = null;

        Set<String> returnSet = new HashSet<String>();

        query = queryService.buildQuery(query_string);

        ITabularResultSet resultSet = query.fetchRows(0);

        for (IResultSetRow row : resultSet) {
            returnSet.add(fieldUtil.getFieldValueAsString(row.getField(1)));
        }

        //logger.info("returnQueryResultsSet() End");
        return returnSet;
    }

    private boolean isObjectNull(Object obj) {

        return (obj == null);
    }

    private void updateGRCObjectAsSuperUser(IGRCObject object, Date latestDate) throws Exception {

        logger.info("updateGRCObjectAsSuperUser() Start");
        IServiceFactory serviceFactory = null;
        IResourceService resourceService = null;
        SuperUserInformation superUserInformation = null;

        superUserInformation = new SuperUserInformation();
        superUserInformation.setSuperUserInfoInConfigFile(true);
        superUserInformation.setConfigFilePath(applicationUtil.getRegistrySetting(SUPER_USER_SETTING));
        logger.info("Super User Information : " + superUserInformation);

        serviceFactory = applicationUtil.createServiceFactoryForUser(superUserInformation);
        logger.info("Super User Name [" + serviceFactory.createSecurityService().getCurrentUser().getName()
                + "], and email [" + serviceFactory.createSecurityService().getCurrentUser().getEmailAddress() + "]");
        resourceService = serviceFactory.createResourceService();

        fieldUtil.setFieldValue(object, AUD_ENTITY_LAST_AUDIT_DATE_FIELD, latestDate.toString());
        resourceService.saveResource(object);
        logger.info("[" + object.getName() + "], Id [" + object.getId() + "] updated with [" + latestDate + "]");
        logger.info("updateGRCObjectAsSuperUser() End");
    }

}

