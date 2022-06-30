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

import static com.ibm.openpages.ext.citi.workflow.constant.NewDSMTLinkConstants.*;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.COMMA_SEPERATED_DELIMITER;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isListNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isSetNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.parseDelimitedValuesIgnoringNullOrSpace;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.unParseCommaDelimitedValues;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.tss.service.logging.EXTLoggerFactoryImpl;
import com.ibm.openpages.ext.tss.workflow.action.BaseWorkflowCustomAction;

/**
 * <p>
 * The class implements the code for the custom action to:
 * a) get all DSMT Links (Citi_DSMT_Link) under AE (AuditableEntity) where DSMT Link Status (Citi-DL:Status) = Pending Addition – these will be the DSMTs that the approval is for
 * b) get the based ids of all of these DSMT Links Citi_DSMT_Link:Citi-DL:BaseID (e.g. if there were 3 DSMT Links where status = pending addition, then base ids would be 1234, 6544, 7574)
 * c) get all AEApps (Citi_AEApp) directly under AE (AuditableEntity) where AE App Scope (Citi-AEApp:Scp) = In
 * d) get all Audits (AuditProgram) directly under the AEApps (Citi_AEApp) from the list above where Audit status (OPSS-Aud:Status) != Completed or Cancelled
 * e) Update all the Audits from the list above to set:
 *      AuditProgram:Citi-Aud:DSMTFlag == Yes
 *      AuditProgram:Citi-Aud:DSMTList==Comma separated list of base ID's of the new DSMT's --> list from b above. Note: This should be appended to the existing list.
 * </P>
 *
 * @custom.feature : Workflow Custom Actions
 * @custom.category : Custom Actions
 */
public class NewDSMTLinkCustomAction extends BaseWorkflowCustomAction {

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
    public NewDSMTLinkCustomAction(IWFOperationContext context, List<IWFCustomProperty> properties) {

        super(context, properties);
    }

    /**
     * Default process method for the workflow to implement the increment logic.
     */
    @Override
    protected void process() throws Exception {

        // Method Level Variables
        String dsmtLinkStatusValue = null;
        String aeAppScopeValue = null;
        String auditStatusIgnoreValue = null;
        String auditDSMTLinkFlagValue = null;
        List<String> currentAuditDSMTLinkList = new ArrayList<String>();

        IGRCObject auditableEntityObj = null;
        IGRCObject auditObj = null;
        IWFOperationContext context = null;

        try {

            /* Initialize required services */
            initFieldUtilServices();
            initGRCObjectUtilServices();

            // Initialize
            logger = new EXTLoggerFactoryImpl().getInstance(LOG_FILE_NAME);
            logger.debug("\nStarting NewDSMTLinkCustomAction...");

            /* Get the workflow context object, log it for information */
            context = getContext();
            auditableEntityObj = context.getResource();
            logger.info("Object Name [" + auditableEntityObj.getName() + "], Id [" + auditableEntityObj.getId()+ "] and Object Type [" + auditableEntityObj.getType().getName() + "]");

            serviceFactory = context.getServiceFactory();
            queryService = serviceFactory.createQueryService();


            //Get the New DSMTLink Custom Action properties
            dsmtLinkStatusValue = getPropertyValue(DSMT_LINK_STATUS_VALUE_PROPERTY);
            aeAppScopeValue = getPropertyValue(AE_APP_SCOPE_VALUE_PROPERTY);
            auditStatusIgnoreValue = getPropertyValue(AUDIT_STATUS_IGNORE_VALUE_PROPERTY);
            auditDSMTLinkFlagValue = getPropertyValue(AUDIT_DSMT_LINK_FLAG_VALUE_PROPERTY);

            List<String> validDsmtLinkStatusList = parseDelimitedValuesIgnoringNullOrSpace(dsmtLinkStatusValue, COMMA_SEPERATED_DELIMITER);
            String validDsmtLinkStatus = parseInputValue(validDsmtLinkStatusList);
            String dsmtLinkQuery = ALL_DSMT_LINKS_IN_AUDITABLE_ENTITY_QUERY.replace(QUERY_VALUE, validDsmtLinkStatus) + auditableEntityObj.getId(); 
            logger.info("DSMT Link Query: " + dsmtLinkQuery);

            Set<String> dsmtLinkSet = new HashSet<String>();
            dsmtLinkSet = returnQueryResultsSet(dsmtLinkQuery);
            logger.info("dsmtLinkSet : " + dsmtLinkSet);

            if (isSetNullOrEmpty(dsmtLinkSet)) {

                logger.info("DSMT Link Set is empty: " + dsmtLinkSet);
                return;
            }

            List<String> validAeAppScopeList = parseDelimitedValuesIgnoringNullOrSpace(aeAppScopeValue, COMMA_SEPERATED_DELIMITER);
            String validAeAppScopes = parseInputValue(validAeAppScopeList);

            List<String> auditStatusIgnoreList = parseDelimitedValuesIgnoringNullOrSpace(auditStatusIgnoreValue, COMMA_SEPERATED_DELIMITER);
            String auditStatusIgnore = parseInputValue(auditStatusIgnoreList);

            String auditQuery = ALL_AUDIT_IN_AUDITABLE_ENTITY_QUERY.replace(QUERY_VALUE, validAeAppScopes).replace(QUERY_IGNORE_VALUE, auditStatusIgnore) + auditableEntityObj.getId(); 
            logger.info("Audit Query: " + auditQuery);

            Set<String> auditSet = new HashSet<String>();
            auditSet = returnQueryResultsSet(auditQuery);
            logger.info("auditSet : " + auditSet);

            for (String auditId : auditSet) {

                auditObj = serviceFactory.createResourceService().getGRCObject(new Id(auditId));
                logger.info("Audit Name [" + auditObj.getName() + "], Id [" + auditObj.getId()+ "]");

                String auditNewDSMTLinkListFld = fieldUtil.getFieldValueAsString(auditObj, AUDIT_NEW_DSMT_LINK_LIST_FIELD);
                currentAuditDSMTLinkList = parseDelimitedValuesIgnoringNullOrSpace(auditNewDSMTLinkListFld, COMMA_SEPERATED_DELIMITER);
                currentAuditDSMTLinkList.addAll(dsmtLinkSet);
                currentAuditDSMTLinkList = new ArrayList<String>(new LinkedHashSet<String>(currentAuditDSMTLinkList));
                logger.info("New Audit DSMT Link Values: " + currentAuditDSMTLinkList);
                String allValidIds = unParseCommaDelimitedValues(currentAuditDSMTLinkList);
                logger.info("allValidIds: " + allValidIds);
                logger.info("auditDSMTLinkFlagValue: " + auditDSMTLinkFlagValue);

                fieldUtil.setFieldValue(auditObj, AUDIT_NEW_DSMT_LINK_LIST_FIELD, allValidIds);
                fieldUtil.setFieldValue(auditObj, AUDIT_NEW_DSMT_LINK_FLAG_FIELD, auditDSMTLinkFlagValue);
                serviceFactory.createResourceService().saveResource(auditObj);
                logger.info("[" + auditObj.getName() + "] updated!");
            }
        }
        catch (Exception ex) {

            logger.error(
                    "EXCEPTION!!!!!!!!!!! An exception occurred while running the NewDSMTLinkCustomAction : " + getStackTrace(ex));
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

        logger.info("returnQueryResultsSet() Start");
        IQuery query = null;

        Set<String> returnSet = new HashSet<String>();

        query = queryService.buildQuery(query_string);

        ITabularResultSet resultSet = query.fetchRows(0);

        for (IResultSetRow row : resultSet) {
            returnSet.add(fieldUtil.getFieldValueAsString(row.getField(1)));
        }

        logger.info("returnQueryResultsSet() End");
        return returnSet;
    }

    private String parseInputValue(List<String> list) {

        int length = 1;
        StringBuilder unParsedValue = null;

        unParsedValue = new StringBuilder();

        if ( isListNotNullOrEmpty(list)) {
            
            for (String val : list) {
                
                if( isNotNullOrEmpty(val)) {

                    unParsedValue.append("'").append(val).append("'");
                    if(length < list.size()) {
                        unParsedValue.append(",");
                    }
                }
                length ++;
            }
        }

        return unParsedValue.toString();
    }

}
