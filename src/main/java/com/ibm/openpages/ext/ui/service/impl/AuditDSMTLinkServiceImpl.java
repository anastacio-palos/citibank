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


package com.ibm.openpages.ext.ui.service.impl;

import com.ibm.openpages.api.application.CopyConflictBehavior;
import com.ibm.openpages.api.application.CopyObjectOptions;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IApplicationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;
import com.ibm.openpages.ext.tss.helpers.service.IHelperService;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.GRCObjectSearchUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.service.IAuditDSMTLinkService;
import com.ibm.openpages.ext.ui.util.DSMTLinkHelperUtil;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.*;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.tss.service.util.NumericUtil.getIntValue;
import static com.ibm.openpages.ext.tss.service.util.NumericUtil.isNumeric;
import static com.ibm.openpages.ext.ui.constant.AuditDSMTLinkConstants.*;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.*;

@Service("auditDSMTLinkServiceImpl")
public class AuditDSMTLinkServiceImpl implements IAuditDSMTLinkService {

    private Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IHelperService helperService;

    @Autowired
    IHelperService commonHelperService;

    @Autowired
    IApplicationUtil applicationUtil;

    @Autowired
    IGRCObjectUtil grcObjectUtil;

    @Autowired
    GRCObjectSearchUtil grcObjectSearchUtil;

    @Autowired
    IFieldUtil fieldUtil;

    @Autowired
    DSMTLinkServiceUtilImpl dsmtLinkServiceUtil;

    @Autowired
    DSMTLinkHelperUtil dsmtLinkHelperUtil;

    // @Autowired
    // DSMTLinkHelperUtil dsmtLinkHelperUtil;

    @Autowired
    IServiceFactoryProxy serviceFactoryProxy;

    /**
     * Construct any information once the bean is created.
     */
    @PostConstruct
    public void initServiceImpl() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(AUDIT_DSMT_LINK_SERVICE_LOG_FILE_NAME);
    }

    /**
     * <P>
     * This method retrieves the Helpers Header Information from the Registry settings and sets the appropriate values
     * in the {@link CarbonHeaderInfo} object. This will be used to display the content in the UI Header.
     * </P>
     *
     * @param dsmtLinkHelperInfo
     *            an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return - the dsmtLinkHelperInfo with the helper header information set
     * @throws Exception
     */
    @Override
    public DSMTLinkHelperAppInfo getHelperHeaderInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getHelperHeaderInfo() Start");

        // Method Level Variables.
        CarbonHeaderInfo headerInfo = null;

        /* Initialize Variables */
        // Get the helper header information by passing the Helper Base Setting + the object type label
        headerInfo = helperService
                .getTFUIHelperAppHeaderInfo(DSMT_LINK_APP_BASE_SETTING + dsmtLinkHelperInfo.getObjectTypeLabel());

        // Set the bean to be returned
        dsmtLinkHelperInfo.setHeaderInfo(headerInfo);
        logger.info("getHelperHeaderInfo() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * <P>
     * This method retrieves the Helper Landing Page Content Section. The Landing Page Title, Landing Page Content and
     * the fields and the values of the fields from the object from which the helper was launched are obtained. On
     * conditional basis pre existing associated object information is also retrieved. The Landing page information is
     * set in an instance of the {@link DSMTObjectGenericDetails}. The link object association information is set in an
     * instance of the {@link DSMTLinkObjectAssociationInfo}
     * </P>
     *
     * @param dsmtLinkHelperInfo
     *            an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception
     */
    @Override
    public DSMTLinkHelperAppInfo getLandingPageInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getLandingPageInfo() Start");

        // Get the Basic Object information of the current object under execution
        dsmtLinkHelperInfo = getBasicObjectInfoForDisplay(dsmtLinkHelperInfo);

        logger.info("getLandingPageInfo() End");
        return dsmtLinkHelperInfo;
    }

    @Override
    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getAssociatedObjectInformation() Start");

        String query = EMPTY_STRING;
        DataGridInfo existingQuadsInfo = null;

        // Construct query to get all valid DSMT Links under Audit object
        query = ASSOCIATED_DSMT_LINK_BASE_QUERY + dsmtLinkHelperInfo.getObjectID() + " \n";
        logger.info("Associated DSMT Link Query: \n" + query);

        // Set Existing DSMTLinkObject into DSMTLinkHelperAppInfo
        existingQuadsInfo = getExistingDSMTLinkObject(dsmtLinkHelperInfo, query);

        logger.info("getAssociatedObjectInformation() End");
        return existingQuadsInfo;
    }

    /**
     * <P>
     * This method return the new DSMT Links available for association to the current object grouped by the Auditable
     * Entity ID present in the DSMT link object.
     * </P>
     * 
     * @param dsmtLinkHelperInfo
     * @return
     * @throws Exception
     */
    @Override
    public DataGridInfo getNewDSMTLinkObject(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getNewDSMTLinkObject() Start");

        // Method Level Variables.
        String auditableEntityId = null;
        String active = null;
        String status = null;
        String auditDSMTFlagVal = null;
        String aeAppStatus = null;
        String aeAppScope = null;
        ITabularResultSet resultSet = null;
        List<String> auditDSMTLisFieldList = null;

        DSMTLinkDisableInfo dsmtLinkDisableInfo = null;
        Set<String> disableInfoList = null;

        // IGRCObject auditableEntityObj = null;
        IGRCObject auditObj = null;
        IGRCObject dsmtLinkObj = null;
        IGRCObject aeAppObject = null;

        DataGridInfo carbonDataGridInfo = null;
        LinkedHashMap<String, List<ExistingDSMTLinkInfo>> newDSMTLinksInfoMap = null;

        ExistingDSMTLinkInfo dsmtLinkObjectInfo = null;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList = null;

        // Create Audit Object
        auditObj = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());

        // get New DSMT Link Flag and New DSMT Link list value from Audit Object
        auditDSMTFlagVal = fieldUtil.getFieldValueAsString(auditObj, AUDIT_NEW_DSMT_LINK_FLAG_FIELD);
        auditDSMTLisFieldList = parseDelimitedValuesIgnoringNullOrSpace(
                fieldUtil.getFieldValueAsString(auditObj, AUDIT_NEW_DSMT_LINK_LIST_FIELD), COMMA);

        logger.info("auditDSMTFlagVal : " + auditDSMTFlagVal);
        logger.info("auditDSMTLisFieldList : " + auditDSMTLisFieldList);
        logger.info("Audit Object Name : " + auditObj.getName());

        /* Initialize Variables */
        newDSMTLinksInfoMap = new LinkedHashMap<String, List<ExistingDSMTLinkInfo>>();
        String availableDSMTLinkQuery = EMPTY_STRING;

        // If the New DSMT Link Flag is not set to Yes on the Audit or New DSMT Link List field is empty, then return
        // null for New DSMT Link Object
        if (isNotEqualIgnoreCase(YES, auditDSMTFlagVal) || isListNullOrEmpty(auditDSMTLisFieldList)) {

            logger.info("New DSMT Link Flag is set to Yes or New DSMT Link List field is empty!");
            logger.info("getNewDSMTLinkObject() End!");

            newDSMTLinksInfoMap.put("", new ArrayList<ExistingDSMTLinkInfo>());
            carbonDataGridInfo = processDataForDisplay(newDSMTLinksInfoMap, false, true, false, new HashMap<String, String>(), null, null, dsmtLinkHelperInfo);
            return carbonDataGridInfo;
        }

        for (String baseId : auditDSMTLisFieldList) {

            logger.info("Base Id: " + baseId);
            
            dsmtLinkObj = grcObjectUtil.getObjectFromId(baseId);
            logger.info("DSMT Link Object Name : " + dsmtLinkObj.getName());

            dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();
            dsmtLinkDisableInfo = new DSMTLinkDisableInfo();
            disableInfoList = new HashSet<String>();

            dsmtLinkObjectInfo.setName(dsmtLinkObj.getName());
            dsmtLinkObjectInfo.setResourceId(baseId);
            dsmtLinkObjectInfo.setDescription(dsmtLinkObj.getDescription());
            dsmtLinkObjectInfo.setType(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_TYPE_FIELD));
            dsmtLinkObjectInfo.setScope(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_SCOPE_FIELD));

            status = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_STATUS_FIELD);
            dsmtLinkObjectInfo.setStatus(status);

            auditableEntityId = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_AE_ID_FIELD);

            // Construct a individual DSMT Link Query for each AE
            availableDSMTLinkQuery = AVAILABLE_DSMT_LINK_IN_AUDITABLE_ENTITY_BASE_QUERY + auditableEntityId;
            logger.info("availableDSMTQuery : " + availableDSMTLinkQuery);
            resultSet = grcObjectSearchUtil.executeQuery(availableDSMTLinkQuery);

            // If Audit status is not draft, check if AEApp already exist and if it approved or not.
            if (isNotEqual(AUDIT_DRAFT_STATUS, dsmtLinkHelperInfo.getObjectStatus())) {

                String aeAppQuery = AUDITABLE_ENTITY_APP_QUERY
                        .replace(AUDITPROGRAM_ID_STR, dsmtLinkHelperInfo.getObjectID())
                        .replace(AUDITABLE_ENTITY_ID_STR, auditableEntityId);
                logger.info("AE App Query: " + aeAppQuery);

                Set<String> aeAppSet = new HashSet<String>();
                aeAppSet = returnQueryResultsSet(aeAppQuery);
                logger.info("aeAppSet : " + aeAppSet);

                // AEApp exist, check the status of this AEApp
                if (isSetNotNullOrEmpty(aeAppSet)) {

                    aeAppObject = grcObjectUtil.getObjectFromId(aeAppSet.iterator().next());
                    logger.info("Existing AEApp Object name [" + aeAppObject.getName() + "] and Id ["
                            + aeAppObject.getId() + "]");

                    aeAppStatus = fieldUtil.getFieldValueAsString(aeAppObject, AUD_ENTITY_APP_STATUS_FIELD);
                    aeAppScope = fieldUtil.getFieldValueAsString(aeAppObject, AUD_ENTITY_APP_SCOPE_FIELD);
                    logger.info("AEApp Status [" + aeAppStatus + "] and Scope [" + aeAppScope + "]");

                    // If AE App already exists, and status != Blank, display the AE in disable mode and
                    // all the DSMT Link under this AE will also will automatically disabled
                    if (isNotNullOrEmpty(aeAppStatus)) {

                        logger.info("Add AE to disable list due to status != Blank : " + auditableEntityId);
                        disableInfoList.add(AUD_ENTITY_APP_STATUS_NOT_BLANK_MESSAGE);
                        dsmtLinkObjectInfo.setDisabled(true);
                    }

                    // If AE App already exists, and Scope == out, mark all the DSMT Link under this AE disabled
                    if (isEqualIgnoreCase(SCOPE_OUT_STATUS, aeAppScope)) {

                        logger.info("All the DSMT under this AE will be disabled due to Scope == out : "
                                + auditableEntityId);
                        disableInfoList.add(AUD_ENTITY_APP_SCOPE_OUT_MESSAGE);
                        dsmtLinkObjectInfo.setDisabled(true);
                    }
                }
                // AEApp doesn't exist, so disable DSMT Link under AE.
                else
                {

                    logger.info("AEApp doesn't exist, so All the DSMT under this AE will be disabled : "
                            + auditableEntityId);
                    disableInfoList.add(AUD_ENTITY_ASSOCIATION_DOES_NOT_EXIST_MESSAGE);
                    dsmtLinkObjectInfo.setDisabled(true);
                }
            }

            logger.info("Base Id : " + baseId);
            dsmtLinkObjectInfo.setBaseId(baseId);

            active = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_ACTIVE_FIELD);
            dsmtLinkObjectInfo.setActive(active);

            if (isNotNullOrEmpty(status)) {
                disableInfoList.add(DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE);
                dsmtLinkObjectInfo.setDisabled(true);
            }

            if (!DSMT_LINK_YES_STATUS.equalsIgnoreCase(active)) {
                disableInfoList.add(DSMT_LINK_IS_NOT_ACTIVE_MESSAGE);
                dsmtLinkObjectInfo.setDisabled(true);
            }

            dsmtLinkObjectInfo.setLink(dsmtLinkHelperUtil
                    .getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(), dsmtLinkObjectInfo.getName()));
            dsmtLinkObjectInfo.setLegalvehiclename(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_LVNAME_FIELD));
            dsmtLinkObjectInfo.setManagedsegmentname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MSNAME_FIELD));
            dsmtLinkObjectInfo.setManagedgeographyname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MGNAME_FIELD));
            dsmtLinkObjectInfo.setParentResourceId(auditableEntityId);

            dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
            dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);

            dsmtLinkObjectList = newDSMTLinksInfoMap.get(auditableEntityId);
            dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ? dsmtLinkObjectList
                    : new ArrayList<ExistingDSMTLinkInfo>();
            dsmtLinkObjectList.add(dsmtLinkObjectInfo);
            newDSMTLinksInfoMap.put(auditableEntityId, dsmtLinkObjectList);

        }

        carbonDataGridInfo = processDataForDisplay(newDSMTLinksInfoMap, false, true, false, new HashMap<String, String>(), null, null, dsmtLinkHelperInfo);
        logger.info("getNewDSMTLinkObject() End");
        return carbonDataGridInfo;
    }

    /**
     * <P>
     * This method descopes the DSMT Link objects that were selected in the UI. Before descoping it checks for the
     * status of the Audit from which the helper was run. 1. If the Status is Draft - the helper will update the AEApp
     * for the AE to be disassociated and set the AEApp:Scope to Out, and the helper will also delete all DSMT Links
     * that were scoped in for this AE. 2. If the Status is not Draft - the helper will update the AEApp for the AE to
     * be disassociated and set the AEApp:Status to Pending Removal. The helper will also update the status of all DSMT
     * Links for this AE to Pending Removal.
     * </P>
     * 
     * @param dsmtLinkHelperInfo
     * @return
     * @throws Exception
     */
    public List<DSMTLinkObjectInfo> descopeDSMTLinksFromAudit(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        logger.info("descopeDSMTLinksFromAudit() Start");

        // Method Level Variables.
        String auditStatus = EMPTY_STRING;

        IGRCObject dsmtLinkObject = null;
        IGRCObject aeAppObject = null;
        Map<String, String> fieldsToUpdateInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkUpdateFailInfo = null;

        /* Initialize Variables */
        auditStatus = dsmtLinkHelperInfo.getObjectStatus();

        logger.info("DSMT Link Descoping List : " + dsmtLinkHelperInfo.getUpdateDsmtLinkInfo());

        // Make sure the Descoping information is present to continue
        if (isListNotNullOrEmpty(dsmtLinkHelperInfo.getUpdateDsmtLinkInfo())) {

            // Iterate through the list of Descoping items. The items are grouped by the Auditable Entities.
            for (UpdateDSMTLinkInfo descopeDSMTInfo : dsmtLinkHelperInfo.getUpdateDsmtLinkInfo()) {

                logger.info("AE Id : " + descopeDSMTInfo.getAuditableEntityId());
                // Auditable Entities are selected by users to descope.
                // Association between Audit and Adutable Entity is maintain by AE App,
                if (isNotNullOrEmpty(descopeDSMTInfo.getAuditableEntityId())) {

                    String aeAppQuery = AUDITABLE_ENTITY_APP_QUERY
                            .replace(AUDITPROGRAM_ID_STR, dsmtLinkHelperInfo.getObjectID())
                            .replace(AUDITABLE_ENTITY_ID_STR, descopeDSMTInfo.getAuditableEntityId());
                    logger.info("AEApp Query: \n" + aeAppQuery);

                    Set<String> aeAppSet = new HashSet<String>();
                    aeAppSet = returnQueryResultsSet(aeAppQuery);
                    logger.info("aeAppSet : " + aeAppSet);

                    // Iterate through each and AE App and descope it first, before moving to descope DSMT LInks objects
                    for (String aeAppId : aeAppSet) {

                        aeAppObject = grcObjectUtil.getObjectFromId(aeAppId);
                        logger.info("AEApp Object name [" + aeAppObject.getName() + "] and Id [" + aeAppObject.getId() + "]");

                        // Instantiate the fields to update map, the map will hold the fields to update as the keys and
                        // the values with which the field will be updated as the values for the keys.
                        fieldsToUpdateInfo = new HashMap<String, String>();

                        // If status of Audit is Draft
                        if (isEqualIgnoreCase(AUDIT_DRAFT_STATUS, auditStatus)) {

                            fieldsToUpdateInfo.put(AUD_ENTITY_APP_SCOPE_FIELD, SCOPE_OUT_STATUS);
                            // TODO Need confirmation to blank the AE App status field
                            // fieldsToUpdateInfo.put(AUD_ENTITY_APP_STATUS_FIELD, EMPTY_STRING);
                            grcObjectUtil.updateFieldsInObjectAndSave(aeAppObject, fieldsToUpdateInfo);
                            logger.info("AEApp Obj scope updated to : " + SCOPE_OUT_STATUS);
                        }
                        // if the status of the Audit is not draft
                        else {
                            fieldsToUpdateInfo.put(AUD_ENTITY_APP_STATUS_FIELD, PENDING_REMOVAL_STATUS);
                            //fieldsToUpdateInfo.put(AUD_ENTITY_APP_RATIONALE_DISCOPING_FIELD, "Rationale is required when disassociating an AE");
                            grcObjectUtil.updateFieldsInObjectAndSave(aeAppObject, fieldsToUpdateInfo);
                            logger.info("AEApp Obj status updated to : " + PENDING_REMOVAL_STATUS);
                        }
                        // There should be only AE App association between Auditable Entity and Audit, so exit the for loop
                        break;
                    }

                    // Iterate through each DSMT Links objects and descope it
                    Set<String> dsmtLinkObjSet = new HashSet<String>();

                    String dsmtQuery = EXISTING_DSMT_LINK_QUERY
                            .replace(AUDITPROGRAM_ID_STR, dsmtLinkHelperInfo.getObjectID())
                            .replace(AUDITABLE_ENTITY_ID_STR, descopeDSMTInfo.getAuditableEntityId());
                    logger.info("DSMT Link Query: " + dsmtQuery);

                    dsmtLinkObjSet = returnQueryResultsSet(dsmtQuery);
                    logger.info("dsmtLinkObjSet : " + dsmtLinkObjSet);
                    for (String dsmtLinkId : dsmtLinkObjSet) {

                        dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);
                        logger.info("DSMT Link Object name [" + dsmtLinkObject.getName() + "] and Id [" + dsmtLinkObject.getId() + "]");

                        // Make sure the Id passed corresponds to an object in the system
                        if (isObjectNotNull(dsmtLinkObject)) {

                            // If status of Audit is Draft, delete the DSMT Link Obj
                            if (isEqualIgnoreCase(AUDIT_DRAFT_STATUS, auditStatus)) {

                                dsmtLinkServiceUtil.deleteDSMTLinkAsSuperUser(dsmtLinkObject);
                                logger.info("Deleted DSMT Link Obj : " + dsmtLinkObject.getName());
                            }
                            // if the status of the Audit is not draft, mark scope out in the DSMT Link Obj
                            else {
                                // Instantiate the fields to update map, the map will hold the fields to update as the
                                // keys and
                                // the values with which the field will be updated as the values for the keys.
                                fieldsToUpdateInfo = new HashMap<String, String>();
                                fieldsToUpdateInfo.put(DSMT_LINK_STATUS_FIELD, PENDING_REMOVAL_STATUS);
                                grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                                logger.info("DSMT Link Obj status updated to : " + PENDING_REMOVAL_STATUS);
                            }
                        }
                    }

                }
                else {
                    // This handles the use case where user have selected some DSMT Links Object under AE to descope,
                    // but not the AE.
                    // Iterate through each orphan DSMT Link Id's and descope it individually
                    logger.info(
                            "Orphan DSMT Link List case for descoping : " + descopeDSMTInfo.getDsmtLinkIdsToUpdate());
                    if (isListNotNullOrEmpty(descopeDSMTInfo.getDsmtLinkIdsToUpdate())) {

                        // Iterate through the list of DSMT links to be descoped.
                        for (String dsmtLinkId : descopeDSMTInfo.getDsmtLinkIdsToUpdate()) {

                            dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);

                            // Make sure the Id passed corresponds to an object in the system
                            if (isObjectNotNull(dsmtLinkObject)) {

                                logger.info("DSMT Link Object name [" + dsmtLinkObject.getName() + "] and Id [" + dsmtLinkObject.getId() + "]");

                                // for both draft and not draft, if we are only descoping the DSMT but not the AE,
                                // then mark scope out in the DSMT Link Obj
                                fieldsToUpdateInfo = new HashMap<String, String>();
                                fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, SCOPE_OUT_STATUS);

                                grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                                logger.info("DSMT Link Obj scope field updated to : " + SCOPE_OUT_STATUS);
                            }
                        }

                    }
                }
            }
        }

        dsmtLinkServiceUtil.updateInvalidDMSTLinkField(dsmtLinkHelperInfo.getObjectID(),
                INVALID_DSMT_LINK_ASSOCIATED_AUDIT_BASE_QUERY, INVALID_DSMT_EXISTS_FIELD, false);

        // Return the DSMT Link Descoping failure information
        logger.info("descopeDSMTLinksFromAudit() End");
        return dsmtLinkUpdateFailInfo;
    }

    /**
     * <P>
     * This method associate the DSMT Link objects that were selected in the UI. Before associating it checks for the
     * status of the Audit from which the helper was run. 1. If the Status is Draft - the helper will create the AEApp
     * (if it doesn't exist) for the AE to be associated and set the AEApp:Scope to In, and the helper will copy all
     * DSMT Links that were selected for this AE. 2. If the Status is not Draft - the helper will create the AEApp (if
     * it doesn't exist) for the AE to be associated and set AEApp Status = Pending Addition and Scope will be blank
     * 
     * If the AE association is approved, then the AE DSMT Links will be also copied under the Audit.
     * </P>
     * 
     * @param dsmtLinkHelperInfo
     * @return
     * @throws Exception
     */
    public List<DSMTLinkObjectInfo> associateDSMTLinksToAudit(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            boolean isNewDSMTLinkFlag) throws Exception {

        logger.info("associateDSMTLinksToAudit() Start");

        // Method Level Variables.
        IGRCObject dsmtLinkObject = null;
        IGRCObject auditObject = null;
        IGRCObject aeAppObject = null;
        IGRCObject aeObject = null;
        Map<String, String> fieldsToUpdateInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkUpdateFailInfo = null;

        IServiceFactory serviceFactory = null;
        IApplicationService applicationService = null;
        IResourceService resourceService = null;
        CopyObjectOptions copyOptions = null;
        ITypeDefinition iTypeDefinition = null;

        /* Initialize Variables */
        String auditStatus = EMPTY_STRING;
        String aeAppStatus = EMPTY_STRING;
        String aeAppScope = EMPTY_STRING;
        String aeAppObjectName = EMPTY_STRING;
        auditObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        auditStatus = dsmtLinkHelperInfo.getObjectStatus();
        serviceFactory = serviceFactoryProxy.getServiceFactory();
        applicationService = serviceFactory.createApplicationService();
        resourceService = serviceFactory.createResourceService();

        logger.info("Associate DSMT Link List : " + dsmtLinkHelperInfo.getUpdateDsmtLinkInfo());

        // Make sure the Associating AE information is present to continue
        if (isListNotNullOrEmpty(dsmtLinkHelperInfo.getUpdateDsmtLinkInfo())) {

            // Iterate through the list of Associating items. The items are grouped by the Auditable Entities.
            for (UpdateDSMTLinkInfo associateDSMTInfo : dsmtLinkHelperInfo.getUpdateDsmtLinkInfo()) {

                logger.info("AE Id : " + associateDSMTInfo.getAuditableEntityId());
                // Make sure that Auditable Entity available to for Scope.
                if (isNotNullOrEmpty(associateDSMTInfo.getAuditableEntityId())) {

                    // Construct Auditable Entity App query to check if association between AE and Audit is already
                    // approved or not
                    String aeAppQuery = AUDITABLE_ENTITY_APP_QUERY
                            .replace(AUDITPROGRAM_ID_STR, dsmtLinkHelperInfo.getObjectID())
                            .replace(AUDITABLE_ENTITY_ID_STR, associateDSMTInfo.getAuditableEntityId());
                    logger.info("AE App Query: " + aeAppQuery);

                    Set<String> aeAppSet = new HashSet<String>();
                    aeAppSet = returnQueryResultsSet(aeAppQuery);
                    logger.info("aeAppSet : " + aeAppSet);

                    aeObject = grcObjectUtil.getObjectFromId(associateDSMTInfo.getAuditableEntityId());
                    logger.info("Auditable Entity Object name [" + aeObject.getName() + "] and Id [" + aeObject.getId() + "]");

                    // AE App already exist
                    if (isSetNotNullOrEmpty(aeAppSet)) {
                        aeAppObject = grcObjectUtil.getObjectFromId(aeAppSet.iterator().next());
                        logger.info("Existing AE App Object name [" + aeAppObject.getName() + "] and Id [" + aeAppObject.getId() + "]");

                        //Copy the fields value from AE to AEApp
                        fieldsToUpdateInfo = new HashMap<String, String>();
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_ID_FIELD, aeObject.getName());
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_NAME_FIELD, aeObject.getDescription());
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_STATUS_FIELD, fieldUtil.getFieldValueAsString(aeObject, AUD_ENTITY_STATUS_FIELD));
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_TYPE_FIELD, fieldUtil.getFieldValueAsString(aeObject, AUD_ENTITY_TYPE_FIELD));
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_SUB_TYPE_FIELD, fieldUtil.getFieldValueAsString(aeObject, AUD_ENTITY_SUB_TYPE_FIELD));
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_WHEN_ADDED_FIELD, new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss").format(new Date()));
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_AUDIT_SCOPE_FIELD, auditObject.getName());
                        grcObjectUtil.updateFieldsInObjectAndSave(aeAppObject, fieldsToUpdateInfo);
                        logger.info("Updated the Existing AE App with latest info from AE.");
                    }
                    // AE App doesn't exist, create new AE App under AE and associate it with Audit.
                    else {

                        List<Id> childAssociationsList = new ArrayList<Id>();
                        childAssociationsList.add(auditObject.getId());

                        aeAppObjectName = aeObject.getName() + "-" + auditObject.getName();
                        iTypeDefinition = serviceFactory.createMetaDataService().getType(AUDITABLE_ENTITY_APP);
                        logger.info("AE App Object name : " + aeAppObjectName);
                        aeAppObject = resourceService.getResourceFactory().createGRCObject(aeAppObjectName, iTypeDefinition);

                        aeAppObject.setPrimaryParent(aeObject);
                        aeAppObject = grcObjectUtil.saveResource(aeAppObject);
                        logger.info("New AE App Object name [" + aeAppObject.getName() + "] and Id [" + aeAppObject.getId() + "]");
                        resourceService.associate(aeAppObject.getId(), new ArrayList<Id>(), childAssociationsList);

                        //Copy the fields value from AE to AEApp
                        fieldsToUpdateInfo = new HashMap<String, String>();
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_ID_FIELD, aeObject.getName());
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_NAME_FIELD, aeObject.getDescription());
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_STATUS_FIELD, fieldUtil.getFieldValueAsString(aeObject, AUD_ENTITY_STATUS_FIELD));
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_TYPE_FIELD, fieldUtil.getFieldValueAsString(aeObject, AUD_ENTITY_TYPE_FIELD));
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_ENTITY_SUB_TYPE_FIELD, fieldUtil.getFieldValueAsString(aeObject, AUD_ENTITY_SUB_TYPE_FIELD));
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_WHEN_ADDED_FIELD, new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss").format(new Date()));
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_AUDIT_SCOPE_FIELD, auditObject.getName());
                        grcObjectUtil.updateFieldsInObjectAndSave(aeAppObject, fieldsToUpdateInfo);
                        logger.info("Created New AE App object and copied the fields from AE.");
                    }

                    // get AE App fields to verify if association is already approved
                    aeAppStatus = fieldUtil.getFieldValueAsString(aeAppObject, AUD_ENTITY_APP_STATUS_FIELD);
                    aeAppScope = fieldUtil.getFieldValueAsString(aeAppObject, AUD_ENTITY_APP_SCOPE_FIELD);
                    logger.info("AEApp Status [" + aeAppStatus + "] and AEApp Scope [" + aeAppScope + "]");
                    logger.info("Audit Status :" + auditStatus);

                    // If status of Audit is Draft or AE App Association is already approved,
                    // copy the DSMT Links objects under Audit
                    if (isEqualIgnoreCase(AUDIT_DRAFT_STATUS, auditStatus)
                            || (isNullOrEmpty(aeAppStatus) && isEqualIgnoreCase(aeAppScope, SCOPE_IN_STATUS))) {

                        logger.info("Audit is in draft or it's Assocation to AE is already approved!");
                        // Instantiate the fields to update map, the map will hold the fields to update as the keys and
                        // the values with which the field will be updated as the values for the keys.
                        fieldsToUpdateInfo = new HashMap<String, String>();
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_STATUS_FIELD, EMPTY_STRING);
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_SCOPE_FIELD, SCOPE_IN_STATUS);
                        grcObjectUtil.updateFieldsInObjectAndSave(aeAppObject, fieldsToUpdateInfo);

                        logger.info("DSMT Link List for update : " + associateDSMTInfo.getDsmtLinkIdsToUpdate());
                        // Make sure there are DSMT Links under the Auditable Entity available to be scoped.
                        if (isListNotNullOrEmpty(associateDSMTInfo.getDsmtLinkIdsToUpdate())) {

                            // Iterate through the list of DSMT links to be scoped.
                            for (String dsmtLinkId : associateDSMTInfo.getDsmtLinkIdsToUpdate()) {

                                // Get the DSMT Link object from the ID passed from the UI
                                dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);

                                logger.info("Base DSMT Link Object name [" + dsmtLinkObject.getName() + "] and Id [" + dsmtLinkObject.getId() + "]");

                                // Create a copy object of DSMT Link for association under audit
                                copyOptions = new CopyObjectOptions();
                                copyOptions.setConflictBehavior(CopyConflictBehavior.OVERWRITE);
                                copyOptions.setIncludeChildren(false);

                                List<Id> dsmtLinkList = new ArrayList<Id>();
                                dsmtLinkList.add(dsmtLinkObject.getId());
                                List<IGRCObject> copiedObjList = applicationService.copyToParent(auditObject.getId(),
                                        dsmtLinkList, copyOptions);

                                for (IGRCObject newDSMTObj : copiedObjList) {

                                    logger.info("New DSMT Link Object name [" + newDSMTObj.getName() + "] and Id [" + newDSMTObj.getId() + "]");

                                    // In case of New DSMT Link Flag, remove the DSMT Links that we just scoped above
                                    // from New DSMT Link List field.
                                    // Get fresh copy of Audit Object before checking for New DSMT Link List field
                                    // value
                                    auditObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
                                    String auditDSMTLisFieldVal = fieldUtil.getFieldValueAsString(auditObject,
                                            AUDIT_NEW_DSMT_LINK_LIST_FIELD);
                                    List<String> newBaseIdList = parseDelimitedValuesIgnoringNullOrSpace(
                                            auditDSMTLisFieldVal, COMMA);

                                    if (isListNotNullOrEmpty(newBaseIdList) && newBaseIdList.contains(dsmtLinkId)) {

                                        newBaseIdList.removeAll(Collections.singleton(dsmtLinkId));

                                        // Instantiate the fields to update map, the map will hold the fields to
                                        // update as the keys and
                                        // the values with which the field will be updated as the values for the
                                        // keys.
                                        fieldsToUpdateInfo = new HashMap<String, String>();
                                        fieldsToUpdateInfo.put(AUDIT_NEW_DSMT_LINK_LIST_FIELD,
                                                unParseCommaDelimitedValues(newBaseIdList));
                                        grcObjectUtil.updateFieldsInObjectAndSave(auditObject, fieldsToUpdateInfo);
                                    }
                                }
                            }
                            // In case of New DSMT Link Flag, after scoping all DSMT Links, check if New DSMT Link List
                            // field is empty,
                            // and if it is empty then set the New DSMT Link Flag field to No and blank out the New DSMT
                            // Link List field.
                            // Get fresh copy of Audit Object before checking for New DSMT Link List field value
                            auditObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
                            String auditDSMTLisFieldVal = fieldUtil.getFieldValueAsString(auditObject,
                                    AUDIT_NEW_DSMT_LINK_LIST_FIELD);

                            if (isNullOrEmpty(auditDSMTLisFieldVal)
                                    || parseDelimitedValuesIgnoringNullOrSpace(auditDSMTLisFieldVal, COMMA)
                                            .isEmpty()) {

                                // Instantiate the fields to update map, the map will hold the fields to update as
                                // the keys and
                                // the values with which the field will be updated as the values for the keys.
                                fieldsToUpdateInfo = new HashMap<String, String>();
                                fieldsToUpdateInfo.put(AUDIT_NEW_DSMT_LINK_FLAG_FIELD, NO);
                                fieldsToUpdateInfo.put(AUDIT_NEW_DSMT_LINK_LIST_FIELD, EMPTY_STRING);
                                grcObjectUtil.updateFieldsInObjectAndSave(auditObject, fieldsToUpdateInfo);
                                logger.info("Updated the New DSMT Link List field!");
                            }
                        }
                    }
                    // if Audit is not draft and AE App's Association is not yet approved
                    // then update the AE App's status and scope fields
                    else {
                        logger.info("Audit not in draft or it's Assocation to AE is not yet approved!");
                        // Instantiate the fields to update map, the map will hold the fields to update as the keys and
                        // the values with which the field will be updated as the values for the keys.
                        fieldsToUpdateInfo = new HashMap<String, String>();
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_STATUS_FIELD, PENDING_ADDITION_STATUS);
                        fieldsToUpdateInfo.put(AUD_ENTITY_APP_SCOPE_FIELD, EMPTY_STRING);
                        grcObjectUtil.updateFieldsInObjectAndSave(aeAppObject, fieldsToUpdateInfo);
                        logger.info("Updated the AEApp[" + aeAppObject.getName() + "], Status to [" + PENDING_ADDITION_STATUS + "] and scope to [" + EMPTY_STRING + "]");
                    }
                }
            }
        }

        dsmtLinkServiceUtil.updateInvalidDMSTLinkField(dsmtLinkHelperInfo.getObjectID(),
                INVALID_DSMT_LINK_ASSOCIATED_AUDIT_BASE_QUERY, INVALID_DSMT_EXISTS_FIELD, false);

        // Return the DSMT Link Associating failure information
        logger.info("associateDSMTLinksToAudit() End");
        return dsmtLinkUpdateFailInfo;
    }

    /**
     * <P>
     * This method handles the re-scoping existing DSMT Links from existing table
     * </P>
     * 
     * @param  dsmtLinkUpdateInfo
     *                                - List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo
     * @param  model
     *                                - Model model
     * @param  session
     *                                - HttpSession session
     * @param  request
     *                                - HttpServletRequest request
     * @param  response
     *                                - HttpServletResponse response
     *
     * @return                    a JSON representation of the List<DSMTLinkObjectInfo>
     * @throws Exception
     */
    @Override
    public List<DSMTLinkObjectInfo> rescopeDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("rescopeDSMTLinks() Start");

        // Method Level Variables.
        IGRCObject dsmtLinkObject = null;
        Map<String, String> fieldsToUpdateInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkUpdateFailInfo = null;

        logger.info("DSMT Link re-scoping List : " + dsmtLinkHelperInfo.getUpdateDsmtLinkInfo());

        // Make sure the re-scoping information is present to continue
        if (isListNotNullOrEmpty(dsmtLinkHelperInfo.getUpdateDsmtLinkInfo())) {

            // Iterate through the list of re-scoping items. The items are grouped by the Auditable Entities.
            for (UpdateDSMTLinkInfo rescopeDSMTInfo : dsmtLinkHelperInfo.getUpdateDsmtLinkInfo()) {

                logger.info("AE Id : " + rescopeDSMTInfo.getAuditableEntityId());

                logger.info("DSMT Link List case for re-scoping : " + rescopeDSMTInfo.getDsmtLinkIdsToUpdate());
                if (isListNotNullOrEmpty(rescopeDSMTInfo.getDsmtLinkIdsToUpdate())) {

                    // Iterate through the list of DSMT links to be re-scoped.
                    for (String dsmtLinkId : rescopeDSMTInfo.getDsmtLinkIdsToUpdate()) {

                        dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);

                        // Make sure the Id passed corresponds to an object in the system
                        if (isObjectNotNull(dsmtLinkObject)) {

                            logger.info("DSMT Link Object name [" + dsmtLinkObject.getName() + "] and Id [" + dsmtLinkObject.getId() + "]");

                            fieldsToUpdateInfo = new HashMap<String, String>();
                            fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, SCOPE_IN_STATUS);

                            grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                            logger.info("DSMT Link Obj scope field updated to : " + SCOPE_IN_STATUS);
                        }
                    }

                }
            }
        }

        dsmtLinkServiceUtil.updateInvalidDMSTLinkField(dsmtLinkHelperInfo.getObjectID(),
                INVALID_DSMT_LINK_ASSOCIATED_AUDIT_BASE_QUERY, INVALID_DSMT_EXISTS_FIELD, false);

        // Return the DSMT Link re-scoping failure information
        logger.info("rescopeDSMTLinks() End");
        return dsmtLinkUpdateFailInfo;
    }

    /**
     * <P>
     * This method returns the existing DSMT Links associated to the current object grouped by the Auditable Entity ID
     * present in the DSMT link object.
     * </P>
     * 
     * @param query
     * @return
     * @throws Exception
     */
    private DataGridInfo getExistingDSMTLinkObject(DSMTLinkHelperAppInfo dsmtLinkHelperInfo, String query)
            throws Exception {

        logger.info("getExistingDSMTLinkObject() Start");

        String auditableEntityId = null;
        String active = null;
        String status = null;
        String baseId = null;
        String scope = null;
        String baseActive = null;
        String baseStatus = null;
        String baseScope = null;
        IGRCObject dsmtLinkObj = null;
        IGRCObject baseDSMTLinkObj = null;
        IGRCObject auditableEntityAppObject = null;
        Map<String, String> disabledAEMap = new HashMap<String, String>();
        Map<String, String> disabledAELowerDpenMap = new HashMap<String, String>();
        Map<String, Set<String>> lowerDpendentAEMap = new HashMap<String, Set<String>>();

        DSMTLinkDisableInfo dsmtLinkDisableInfo = null;
        Set<String> lowerLevelDepObjectsList = null;
        Set<String> lowerLevelDepObjectsDueToAEList = null;
        Set<String> disableInfoList = null;

        ITabularResultSet resultSet = null;
        DataGridInfo carbonDataGridInfo = null;
        Map<String, List<ExistingDSMTLinkInfo>> associatedDSMTLinksInfoMap = null;

        ExistingDSMTLinkInfo dsmtLinkObjectInfo = null;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList = null;

        associatedDSMTLinksInfoMap = new LinkedHashMap<String, List<ExistingDSMTLinkInfo>>();
        resultSet = grcObjectSearchUtil.executeQuery(query);

        for (IResultSetRow row : resultSet) {

            dsmtLinkDisableInfo = new DSMTLinkDisableInfo();
            disableInfoList = new HashSet<String>();
            lowerLevelDepObjectsList = new HashSet<String>();

            dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();

            // Get the DSMT Link object from the query
            dsmtLinkObj = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(1)));
            logger.info("DSMT Link Object name [" + dsmtLinkObj.getName() + "] and Id [" + dsmtLinkObj.getId() + "]");

            // dsmtLinkObjectInfo.setId("row-" + rowCount);
            dsmtLinkObjectInfo.setName(fieldUtil.getFieldValueAsString(row.getField(0)));
            dsmtLinkObjectInfo.setResourceId(fieldUtil.getFieldValueAsString(row.getField(1)));
            dsmtLinkObjectInfo.setDescription(fieldUtil.getFieldValueAsString(row.getField(2)));
            dsmtLinkObjectInfo.setType(fieldUtil.getFieldValueAsString(row.getField(3)));

            scope = fieldUtil.getFieldValueAsString(row.getField(4));
            dsmtLinkObjectInfo.setScope(scope);

            status = fieldUtil.getFieldValueAsString(row.getField(5));
            dsmtLinkObjectInfo.setStatus(status);

            auditableEntityId = fieldUtil.getFieldValueAsString(row.getField(6));

            baseId = fieldUtil.getFieldValueAsString(row.getField(7));
            dsmtLinkObjectInfo.setBaseId(baseId);

            active = fieldUtil.getFieldValueAsString(row.getField(8));
            dsmtLinkObjectInfo.setActive(active);

            dsmtLinkObjectInfo.setLink(dsmtLinkHelperUtil
                    .getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(), dsmtLinkObjectInfo.getName()));
            dsmtLinkObjectInfo.setLegalvehiclename(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_LVNAME_FIELD));
            dsmtLinkObjectInfo.setManagedsegmentname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MSNAME_FIELD));
            dsmtLinkObjectInfo.setManagedgeographyname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MGNAME_FIELD));
            dsmtLinkObjectInfo.setParentResourceId(auditableEntityId);

            //checking if the AEApp Status is not blank, then make the AE row and the DSMT Link rows under it, read only
            if(!disabledAEMap.containsKey(auditableEntityId)) {

                // Construct Auditable Entity App query to get association between AE and Audit
                String aeAppQuery = AUDITABLE_ENTITY_APP_QUERY
                        .replace(AUDITPROGRAM_ID_STR, dsmtLinkHelperInfo.getObjectID())
                        .replace(AUDITABLE_ENTITY_ID_STR, auditableEntityId);
                logger.info("Existing AE App Query: " + aeAppQuery);

                Set<String> aeAppSet = new HashSet<String>();
                aeAppSet = returnQueryResultsSet(aeAppQuery);
                logger.info("aeAppSet : " + aeAppSet);

                // AEApp should already exist
                if (isSetNotNullOrEmpty(aeAppSet)) {
                    auditableEntityAppObject = grcObjectUtil.getObjectFromId(aeAppSet.iterator().next());
                    logger.info("Current AEApp Object name [" + auditableEntityAppObject.getName() + "] and Id [" + auditableEntityAppObject.getId() + "]");

                    //get the AEApp Status and Scope
                    String aeAppStatus = fieldUtil.getFieldValueAsString(auditableEntityAppObject, AUD_ENTITY_APP_STATUS_FIELD);
                    String aeAppScope = fieldUtil.getFieldValueAsString(auditableEntityAppObject, AUD_ENTITY_APP_SCOPE_FIELD);
                    logger.info("AEApp Status [" + aeAppStatus + "] and Scope [" + aeAppScope + "]");

                    //If AEApp scope is out and status is blank, do not show the Auditable Entity in the existing table
                    if (isNullOrEmpty(aeAppStatus) && isEqualIgnoreCase(aeAppScope, SCOPE_OUT_STATUS)) {
                        logger.info("AEApp's status is blank and and scope is out, Skip adding Auditable Entity into the existing table : " + auditableEntityId);
                        logger.info("Skipped DSMT Link Object name [" + dsmtLinkObj.getName() + "] and Id [" + dsmtLinkObj.getId() + "]");
                        continue;
                    }

                   //If AEApp scope is blank and status is also blank, do not show the Auditable Entity in the existing table
                    if (isNullOrEmpty(aeAppScope) && isNullOrEmpty(aeAppStatus)) {
                        logger.info("AEApp's status and scope is blank, Skip adding Auditable Entity into the existing table : " + auditableEntityId);
                        logger.info("Skipped DSMT Link Object name [" + dsmtLinkObj.getName() + "] and Id [" + dsmtLinkObj.getId() + "]");
                        continue;
                    }

                    //If AEApp status is not blank, display the AE and DSMT Link in disable mode
                    if (isNotNullOrEmpty(aeAppStatus)) {
                        logger.info("Add to disable AE list : " + auditableEntityId);
                        disabledAEMap.put(auditableEntityId, AUD_ENTITY_APP_STATUS_NOT_BLANK_MESSAGE);
                        disableInfoList.add(AUD_ENTITY_APP_STATUS_NOT_BLANK_MESSAGE);
                        dsmtLinkObjectInfo.setDisabled(true);
                    }
                }
            }
            //AE is already disabled due to AEApp status not being blank, disable the DSMT Link under the AE also
            else {
                disableInfoList.add(AUD_ENTITY_APP_STATUS_NOT_BLANK_MESSAGE);
                dsmtLinkObjectInfo.setDisabled(true);
            }

            if (isEqualIgnoreCase(active, YES) && isEqualIgnoreCase(scope, SCOPE_OUT_STATUS)) {

                try {

                    // Get the DSMT Link object from the query
                    baseDSMTLinkObj = grcObjectUtil.getObjectFromId(baseId);

                    baseActive = fieldUtil.getFieldValueAsString(baseDSMTLinkObj, DSMT_LINK_ACTIVE_FIELD);
                    baseStatus = fieldUtil.getFieldValueAsString(baseDSMTLinkObj, DSMT_LINK_STATUS_FIELD);
                    baseScope = fieldUtil.getFieldValueAsString(baseDSMTLinkObj, DSMT_LINK_SCOPE_FIELD);

                    if(isNotEqualIgnoreCase(baseScope, SCOPE_IN_STATUS)) {
                        disableInfoList.add(BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE);
                        dsmtLinkObjectInfo.setDisabled(true);
                    }

                    if(isNotEqualIgnoreCase(baseActive, DSMT_LINK_YES_STATUS)) {
                        disableInfoList.add(BASE_DSMT_LINK_IS_NOT_ACTIVE_MESSAGE);
                        dsmtLinkObjectInfo.setDisabled(true);
                    }

                    if(isNotNullOrEmpty(baseStatus)) {
                        disableInfoList.add(BASE_DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE);
                        dsmtLinkObjectInfo.setDisabled(true);
                    }
                }
                catch (Exception e) {
                    disableInfoList.add(BASE_DSMT_LINK_NOT_FOUND_MESSAGE);
                    dsmtLinkObjectInfo.setDisabled(true);
                    logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getExistingDSMTLinkObject() : " + getStackTrace(e));
                }

            }

            if (isNotNullOrEmpty(status)) {
                disableInfoList.add(DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE);
                dsmtLinkObjectInfo.setDisabled(true);
            }

            if (isEqualIgnoreCase(active, NO) && isEqualIgnoreCase(scope, SCOPE_OUT_STATUS)) {
                disableInfoList.add(DSMT_LINK_IS_NOT_ACTIVE_MESSAGE);
                dsmtLinkObjectInfo.setDisabled(true);
            }

            if (isNotNullOrEmpty(baseId)) {
                logger.info("Base Id : " + baseId);
                lowerLevelDepObjectsList = getLowerLevelDependencyDSMTLink(dsmtLinkHelperInfo, baseId);
                logger.info("Lower Level Dependent Objects List : " + lowerLevelDepObjectsList);
                if (isSetNotNullOrEmpty(lowerLevelDepObjectsList)) {
                    dsmtLinkDisableInfo.setLowerLevelDepObjectsList(lowerLevelDepObjectsList);
                    dsmtLinkObjectInfo.setDisabled(true);
                    if(!disabledAELowerDpenMap.containsKey(auditableEntityId)) {
                        logger.info("add to disable AE list due to lower dependency :" + auditableEntityId);
                        disabledAELowerDpenMap.put(auditableEntityId, AUD_ENTITY_LOWER_DEPENDENCY_MESSAGE);
                    }
                }
            }

            lowerLevelDepObjectsDueToAEList = getLowerLevelDependencyDueToAuditableEntity(dsmtLinkHelperInfo.getObjectID(), auditableEntityId);
            logger.info("Lower Level Dependent Objects Due to AE List : " + lowerLevelDepObjectsDueToAEList);
            if (isSetNotNullOrEmpty(lowerLevelDepObjectsDueToAEList)) {
                if(!lowerDpendentAEMap.containsKey(auditableEntityId)) {
                    logger.info("Add to disable AE list due to lower dependency AE resource Id :" + auditableEntityId);
                    lowerDpendentAEMap.put(auditableEntityId, lowerLevelDepObjectsDueToAEList);
                } else {
                    logger.info("Append to disable AE list due to lower dependency AE resource Id :" + auditableEntityId);
                    Set<String> infoSet = lowerDpendentAEMap.get(auditableEntityId);
                    if (isSetNotNullOrEmpty(infoSet)) {
                        infoSet.addAll(lowerLevelDepObjectsDueToAEList);
                        lowerDpendentAEMap.put(auditableEntityId, infoSet);
                    } else {
                        lowerDpendentAEMap.put(auditableEntityId, lowerLevelDepObjectsDueToAEList);
                    }
                }
            }

            dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
            dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);

            dsmtLinkObjectList = associatedDSMTLinksInfoMap.get(auditableEntityId);
            dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ? dsmtLinkObjectList
                    : new ArrayList<ExistingDSMTLinkInfo>();
            dsmtLinkObjectList.add(dsmtLinkObjectInfo);
            associatedDSMTLinksInfoMap.put(auditableEntityId, dsmtLinkObjectList);
        }

        // Construct All Auditable Entity query to get association between AE and Audit
        String allAppQuery = ALL_AUDITABLE_ENTITY_APP_QUERY + dsmtLinkHelperInfo.getObjectID();
        logger.info("All AE Query: " + allAppQuery);
        Set<String> allAESet = new HashSet<String>();
        allAESet = returnQueryResultsSet(allAppQuery);
        logger.info("allAESet : " + allAESet);
        if (isSetNotNullOrEmpty(allAESet)) {
            logger.info("all AE Set is not null or empty : " + allAESet);
            for (String aeId : allAESet) {
                logger.info("AE Id : " + aeId);
                if (!associatedDSMTLinksInfoMap.containsKey(aeId)) {
                    logger.info("Add AE for display : " + aeId);
                    associatedDSMTLinksInfoMap.put(aeId, new ArrayList<ExistingDSMTLinkInfo>());

                    //Lower Level Dependent Objects Due to AE List check for AE without DSMT Links condition
                    lowerLevelDepObjectsDueToAEList = getLowerLevelDependencyDueToAuditableEntity(dsmtLinkHelperInfo.getObjectID(), aeId);
                    logger.info("Lower Level Dependent Objects Due to AE List (2) : " + lowerLevelDepObjectsDueToAEList);
                    if (isSetNotNullOrEmpty(lowerLevelDepObjectsDueToAEList)) {
                        if(!lowerDpendentAEMap.containsKey(aeId)) {
                            logger.info("Add to disable AE list due to lower dependency AE resource Id (2) :" + aeId);
                            lowerDpendentAEMap.put(aeId, lowerLevelDepObjectsDueToAEList);
                        } else {
                            logger.info("Append to disable AE list due to lower dependency AE resource Id (2) :" + aeId);
                            Set<String> infoSet = lowerDpendentAEMap.get(aeId);
                            if (isSetNotNullOrEmpty(infoSet)) {
                                infoSet.addAll(lowerLevelDepObjectsDueToAEList);
                                lowerDpendentAEMap.put(aeId, infoSet);
                            } else {
                                lowerDpendentAEMap.put(aeId, lowerLevelDepObjectsDueToAEList);
                            }
                        }
                    }
                }else {
                    logger.info("AE already exist : " + aeId);
                }
            }
        }

        //audit should not be updated from getExistingDSMTLinkObject methods
        //dsmtLinkServiceUtil.updateInvalidDMSTLinkField(dsmtLinkHelperInfo.getObjectID(), INVALID_DSMT_LINK_ASSOCIATED_AUDIT_BASE_QUERY, INVALID_DSMT_EXISTS_FIELD, false);

        carbonDataGridInfo = processDataForDisplay(associatedDSMTLinksInfoMap, false, false, true, disabledAEMap, lowerDpendentAEMap, disabledAELowerDpenMap, dsmtLinkHelperInfo);

        logger.info("getExistingDSMTLinkObject() End");
        return carbonDataGridInfo;
    }

    private DataGridInfo processDataForDisplay(Map<String, List<ExistingDSMTLinkInfo>> existingDSMTLinksInfoMap,
            boolean isSearch, boolean isNewDSMTLink, boolean isExistingDSMTLink, Map<String, String> disabledAEMap,
            Map<String, Set<String>> lowerDpendentAEMap, Map<String, String> disabledAELowerDpenMap,
            DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("processDataForDisplay() Start");

        int rowCount = 1;
        int childCount = 1;
        int count = 0;
        int maxAEToDisplay;

        IGRCObject audEntityObj = null;
        IGRCObject auditableEntityAppObject = null;
        DataGridInfo carbonDataGridInfo = null;
        ExistingDSMTLinkInfo existingDSMTLinkInfo = null;

        List<String> fieldNamesList = null;
        List<String> hierarchyList = null;
        List<ExistingDSMTLinkInfo> childDMSTLinkInfoList = null;
        List<DataGridHeaderColumnInfo> gridHeaderInfoList = null;
        List<ExistingDSMTLinkInfo> existingDMSTLinkInfoList = null;

        DSMTLinkDisableInfo dsmtLinkDisableInfo = null;
        Set<String> disableInfoList = null;

        carbonDataGridInfo = new DataGridInfo();
        existingDMSTLinkInfoList = new LinkedList<ExistingDSMTLinkInfo>();

        if (isSearch) {
            logger.info("isSearch : " + isSearch);
            fieldNamesList = parseCommaDelimitedValues(HELPER_UI_DISPLAY_FIELDS_INFO_SEARCH);
        }
        else if (isNewDSMTLink) {
            logger.info("isNewDSMTLink : " + isNewDSMTLink);
            fieldNamesList = parseCommaDelimitedValues(HELPER_UI_DISPLAY_FIELDS_INFO_NEW_DSMT_LINK);
        }
        else if (isExistingDSMTLink) {
            logger.info("isExistingDSMTLink : " + isExistingDSMTLink);
            fieldNamesList = parseCommaDelimitedValues(HELPER_UI_DISPLAY_FIELDS_INFO_EXISTING);
        }
        else {
            fieldNamesList = parseCommaDelimitedValues(HELPER_UI_DISPLAY_FIELDS_INFO);
        }

        gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);

        maxAEToDisplay = getIntValue(applicationUtil.getRegistrySetting(dsmtLinkHelperInfo.getObjRegistrySetting() + MAX_AE_SEARCH_RESULT));

        if (isMapNotNullOrEmpty(existingDSMTLinksInfoMap)) {

            for (String audEntityId : existingDSMTLinksInfoMap.keySet()) {

                logger.info("Looping AE Id : " + audEntityId);
                existingDSMTLinkInfo = new ExistingDSMTLinkInfo();
                hierarchyList = new ArrayList<String>();

                dsmtLinkDisableInfo = new DSMTLinkDisableInfo();
                disableInfoList = new HashSet<String>();

                if (!isNumeric(audEntityId)) {
                    logger.info("audEntityId is not numeric : " + audEntityId);
                    continue;
                }

                try {
                    audEntityObj = grcObjectUtil.getObjectFromId(audEntityId);
                }
                catch (Exception e) {
                    logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "processDataForDisplay() : " + getStackTrace(e));
                    continue;
                }

                childDMSTLinkInfoList = existingDSMTLinksInfoMap.get(audEntityId);
                hierarchyList.add(audEntityObj.getName());

                existingDSMTLinkInfo.setId("row-" + rowCount);
                existingDSMTLinkInfo.setName(audEntityObj.getName() + " - " + audEntityObj.getDescription());
                existingDSMTLinkInfo.setDescription(audEntityObj.getDescription());
                existingDSMTLinkInfo.setResourceId(audEntityId);
                existingDSMTLinkInfo.setType(fieldUtil.getFieldValueAsString(audEntityObj, AUD_ENTITY_TYPE_FIELD));
                existingDSMTLinkInfo.setHasChildren(isListNotNullOrEmpty(childDMSTLinkInfoList));
                existingDSMTLinkInfo.setHierarchy(hierarchyList);
                existingDMSTLinkInfoList.add(existingDSMTLinkInfo);

                existingDSMTLinkInfo.setLink(dsmtLinkHelperUtil
                        .getObjectDetailViewLink(existingDSMTLinkInfo.getResourceId(), audEntityObj.getName()));

                // Construct Auditable Entity App query to get association between AE and Audit
                String aeAppQuery = AUDITABLE_ENTITY_APP_QUERY
                        .replace(AUDITPROGRAM_ID_STR, dsmtLinkHelperInfo.getObjectID())
                        .replace(AUDITABLE_ENTITY_ID_STR, audEntityId);
                logger.info("Existing AE App Query: " + aeAppQuery);

                Set<String> aeAppSet = new HashSet<String>();
                aeAppSet = returnQueryResultsSet(aeAppQuery);
                logger.info("aeAppSet : " + aeAppSet);

                // If AE App exist, Use the AE App status and scope to set status and scope in the JSON
                if (isSetNotNullOrEmpty(aeAppSet)) {
                    auditableEntityAppObject = grcObjectUtil.getObjectFromId(aeAppSet.iterator().next());
                    logger.info("AE App Object name [" + auditableEntityAppObject.getName() + "] and Id [" + auditableEntityAppObject.getId() + "]");

                    //get the AEApp status
                    String aeAppStatus = fieldUtil.getFieldValueAsString(auditableEntityAppObject, AUD_ENTITY_APP_STATUS_FIELD);
                    existingDSMTLinkInfo.setStatus(aeAppStatus);

                    //get the AEApp scope
                    String aeAppScope = fieldUtil.getFieldValueAsString(auditableEntityAppObject, AUD_ENTITY_APP_SCOPE_FIELD);

                    logger.info("AEApp Status [" + aeAppStatus + "] and Scope [" + aeAppScope + "]");

                    //in case of existing table
                    //if status is not blank, then we display the AE in disabled mode
                    if (!isSearch && isNotNullOrEmpty(aeAppStatus)) {
                        existingDSMTLinkInfo.setDisabled(true);
                        disableInfoList.add(AUD_ENTITY_APP_STATUS_NOT_BLANK_MESSAGE);
                        dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
                        existingDSMTLinkInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);
                        logger.info("existingDSMTLinkInfo.getDsmtLinkDisableInfo() : " + existingDSMTLinkInfo.getDsmtLinkDisableInfo());
                    }


                } else {
                    existingDSMTLinkInfo.setStatus(EMPTY_STRING);
                }

                logger.info("Disabled Auditable Entity Map Due to AEApp status not being null : " + disabledAEMap);
                logger.info("Auditable Entity Id : " + audEntityId);
                if(isMapNotNullOrEmpty(disabledAEMap) && disabledAEMap.containsKey(audEntityId)) {

                    logger.info("Disabled Auditable Entity Map Due to AEApp status not being null : " + disabledAEMap);
                    existingDSMTLinkInfo.setDisabled(true);
                    disableInfoList.add(disabledAEMap.get(audEntityId));
                    dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
                    existingDSMTLinkInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);
                }

                logger.info("Disabled Auditable Entity Map Due to DSMT Link lower dependency : " + disabledAELowerDpenMap);
                logger.info("Auditable Entity Id : " + audEntityId);
                if(isMapNotNullOrEmpty(disabledAELowerDpenMap) && disabledAELowerDpenMap.containsKey(audEntityId)) {

                    logger.info("Disabled Auditable Entity Map Due to DSMT Link lower dependency : " + disabledAELowerDpenMap);
                    existingDSMTLinkInfo.setDisabled(true);
                    disableInfoList.add(disabledAELowerDpenMap.get(audEntityId));
                    dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
                    existingDSMTLinkInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);
                }

                logger.info("Disabled Auditable Entity Map Due to DSMT Link lower dependency(2) : " + lowerDpendentAEMap);
                logger.info("Auditable Entity Id : " + audEntityId);
                if(isMapNotNullOrEmpty(lowerDpendentAEMap) && lowerDpendentAEMap.containsKey(audEntityId) && isSetNotNullOrEmpty(lowerDpendentAEMap.get(audEntityId))) {

                    logger.info("Disabled Auditable Entity Map Due to DSMT Link lower dependency(2) : " + lowerDpendentAEMap);
                    existingDSMTLinkInfo.setDisabled(true);
                    dsmtLinkDisableInfo.setLowerLevelDepObjectsList(lowerDpendentAEMap.get(audEntityId));
                    existingDSMTLinkInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);

                }

                if (isListNotNullOrEmpty(childDMSTLinkInfoList)) {

                    childCount = 1;

                    for (ExistingDSMTLinkInfo childDSMTLinkInfo : childDMSTLinkInfoList) {

                        hierarchyList = new ArrayList<String>();
                        hierarchyList.add(audEntityObj.getName());
                        hierarchyList.add(childDSMTLinkInfo.getName());

                        childDSMTLinkInfo.setId("row-" + rowCount + "-child-" + childCount);
                        childDSMTLinkInfo.setHierarchy(hierarchyList);

                        existingDMSTLinkInfoList.add(childDSMTLinkInfo);
                        childCount++;
                    }
                }

                rowCount++;

                count ++;
                if (isSearch) {
                    logger.info("check max AE to display in case of search : " + isSearch);
                    logger.info("Max AE Ids : " + maxAEToDisplay);
                    logger.info("count : " + count);
                    if (count >= maxAEToDisplay) {
                        logger.info("Reached the maximum Auditable Entity limit : " + count);
                        logger.info("Total AE Id found : " + existingDSMTLinksInfoMap.keySet().size());
                        carbonDataGridInfo.setWarningMessage(TOO_MANY_RESULTS_WARNING);
                        break;
                    }
                }
            }
        }

        carbonDataGridInfo.setHeaders(gridHeaderInfoList);
        carbonDataGridInfo.setRows(existingDMSTLinkInfoList);

        logger.info("processDataForDisplay() End");
        return carbonDataGridInfo;
    }

    private Set<String> getLowerLevelDependencyDueToAuditableEntity(
            String auditId, String auditableEntityId) throws Exception
    {

        logger.info("getLowerLevelDependencyDueToAuditableEntity() Start");

        Set<String> lowerLevelDependentObjetSet = new HashSet<String>();

        String applicationQuery = APPLICATION_QUERY
                .replace(AUDITPROGRAM_ID_STR, auditId)
                .replace(AUDITABLE_ENTITY_ID_STR, auditableEntityId);
        logger.info("Lower Level Dependency (AE) AE Application Query: "
                + applicationQuery);

        // Method Implementation.
        // Execute the query and get the dependency object info
        Set<String> lowerDepenSetApplication = getLowerLevelDependentDueToAuditableEntityResults(
                applicationQuery, false);
        logger.info("lowerDepenSetApplication: " + lowerDepenSetApplication);
        if (isSetNotNullOrEmpty(lowerDepenSetApplication))
        {

            if (isSetNotNullOrEmpty(lowerLevelDependentObjetSet))
            {

                lowerLevelDependentObjetSet.addAll(lowerDepenSetApplication);
            }
            else
            {
                lowerLevelDependentObjetSet = lowerDepenSetApplication;
            }
        }

        String issueAppBaseQuery = ISSUE_APP_QUERY
                .replace(AUDITPROGRAM_ID_STR, auditId)
                .replace(AUDITABLE_ENTITY_ID_STR, auditableEntityId);
        logger.info("Lower Level Dependency (AE) AE Issue Query: "
                + issueAppBaseQuery);

        Set<String> lowerDepenSetIssueApp = getLowerLevelDependentDueToAuditableEntityResults(
                issueAppBaseQuery, true);
        logger.info("lowerDepenSetIssueApp: " + lowerDepenSetIssueApp);
        if (isSetNotNullOrEmpty(lowerDepenSetIssueApp))
        {

            if (isSetNotNullOrEmpty(lowerLevelDependentObjetSet))
            {

                lowerLevelDependentObjetSet.addAll(lowerDepenSetIssueApp);
            }
            else
            {
                lowerLevelDependentObjetSet = lowerDepenSetIssueApp;
            }
        }

        String processBaseQuery = PROCESS_QUERY
                .replace(AUDITPROGRAM_ID_STR, auditId)
                .replace(AUDITABLE_ENTITY_ID_STR, auditableEntityId);
        logger.info("Lower Level Dependency (AE) AE Process Query: "
                + processBaseQuery);

        Set<String> lowerDepenSetProcess = getLowerLevelDependentDueToAuditableEntityResults(
                processBaseQuery, false);
        logger.info("lowerDepenSetProcess: " + lowerDepenSetProcess);
        if (isSetNotNullOrEmpty(lowerDepenSetProcess))
        {

            if (isSetNotNullOrEmpty(lowerLevelDependentObjetSet))
            {

                lowerLevelDependentObjetSet.addAll(lowerDepenSetProcess);
            }
            else
            {
                lowerLevelDependentObjetSet = lowerDepenSetProcess;
            }
        }

        logger.info("getLowerLevelDependencyDueToAuditableEntity() End");
        return lowerLevelDependentObjetSet;
    }

    private Set<String> getLowerLevelDependencyDSMTLink(DSMTLinkHelperAppInfo dsmtLinkHelperInfo, String dsmtLinkResourceId)
            throws Exception {

        logger.info("getLowerLevelDependencyDSMTLink() Start");

        // Method Level Variables.
        StringBuilder query = null;
        String constraint = EMPTY_STRING;
        String baseQuery = EMPTY_STRING;
        Set<String> lowerLevelDependentObjetSet = null;

        List<String> objectHeirarchy = null;
        List<String> objHeirarchyAndConstraint = null;
        List<String> existingDsmtLinkFieldsList = null;
        List<String> objHeirarchyAndConstraintInfoList = null;

        // Method Implementation.
        /* Initialize Variables */
        // Get the GRCObject from the DSMTLink Resource ID
        // Parse the comma delimited lower level dependency check information
        // dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkResourceId);
        existingDsmtLinkFieldsList = parseCommaDelimitedValues(EXISTING_DSMT_LINK_FIELDS_FOR_DISPLAY);
        objHeirarchyAndConstraintInfoList = parseCommaDelimitedValues(AUDIT_LOWER_LEVEL_DEPENDENCY_CHECK_INFO);

        // Iterate through each lower level dependency check
        for (String objHeirarchyAndConstraintInfo : objHeirarchyAndConstraintInfoList) {

            // Construct a new query, parse the pipe delimited value containing the object hierarchy and the constraint
            query = new StringBuilder();
            objHeirarchyAndConstraint = parsePipeDelimitedValues(objHeirarchyAndConstraintInfo);

            // Parse the colon delimited object hierarchy information
            objectHeirarchy = parseDelimitedValues(objHeirarchyAndConstraint.get(0), ":");

            // Construct the base query with the fields to retrieve
            baseQuery = dsmtLinkServiceUtil.getBaseQueryToGetFieldValues(objectHeirarchy, existingDsmtLinkFieldsList,
                    false, true);
            // logger.info("Base Query: " + baseQuery);

            // Append the object hierarchy information to the base query
            query.append(dsmtLinkServiceUtil.appendBaseQueryToParentAssociationQuery(dsmtLinkHelperInfo,
                    objectHeirarchy, baseQuery));
            // logger.info("Base Query With Parent Association:\n" + query.toString());

            query.append(SINGLE_SPACE);

            // Append the constraint, if the constraint has the Base ID add the base id value to the query
            constraint = objHeirarchyAndConstraint.get(1);
            query.append(constraint);

            // If the constraint contains the BASE ID field, then append the BASE ID Field value in the DSMT Link object
            // to the query
            if (constraint.contains(DSMT_LINK_BASE_ID_FIELD)) {

                query.append(" ");
                query.append(dsmtLinkResourceId);
                query.append(" ");
            }

            //logger.info("Base Query With Association and Constraints: \n" + query.toString());
            Set<String> lowerDepenSet = getLowerLevelDependentResults(query.toString());
            if (isSetNotNullOrEmpty(lowerDepenSet)) {

                if (isSetNotNullOrEmpty(lowerLevelDependentObjetSet)) {

                    lowerLevelDependentObjetSet.addAll(lowerDepenSet);
                } else  {
                    lowerLevelDependentObjetSet = lowerDepenSet;
                }
            }

        }

        logger.info("processLowerLevelDependencyForDSMTLink() End");
        return lowerLevelDependentObjetSet;
    }

    /**
     * <P>
     * This method returns the lower level dependency if with AE id check
     * </P>
     * 
     * @param query
     * @param issueApp
     * @return
     * @throws Exception
     */
    private Set<String> getLowerLevelDependentDueToAuditableEntityResults(String query, boolean issueApp) throws Exception {

        logger.info("getLowerLevelDependentDueToAuditableEntityResults() Start");

        // Method Level Variables.
        ITabularResultSet resultSet = null;
        Set<String> lowerLevelDependentObjetSet = new HashSet<String>();
        IGRCObject lowerLevelDependentObj = null;
        IGRCObject issueObject = null;


        // Method Implementation.
        // Execute the query
        resultSet = grcObjectSearchUtil.executeQuery(query);

        // If the results are present
        if (isObjectNotNull(resultSet)) {

            // Iterate through the results
            for (IResultSetRow row : resultSet) {

                lowerLevelDependentObj = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(0)));

                if (issueApp) {
                    logger.info("Issue Applicability : " + issueApp);
                    String srcRefId = fieldUtil.getFieldValueAsString(lowerLevelDependentObj, ISSUE_APPLICABILITY_SRC_REF_ID_FIELD);
                    logger.info("srcRefId : " + srcRefId);
                    if(isNotNullOrEmpty(srcRefId)) {
                        issueObject = grcObjectUtil.getObjectFromId(srcRefId);
                        lowerLevelDependentObjetSet.add(dsmtLinkHelperUtil.getObjectDetailViewLinkForModal(issueObject.getId().toString(), issueObject.getName()));
                    } else {
                        logger.info("Source Reference field is empty : " + issueApp);
                        lowerLevelDependentObjetSet.add(dsmtLinkHelperUtil.getObjectDetailViewLinkForModal(lowerLevelDependentObj.getId().toString(), lowerLevelDependentObj.getName()));
                    }

                } else {
                    logger.info("issueApp : " + issueApp);
                    lowerLevelDependentObjetSet.add(dsmtLinkHelperUtil.getObjectDetailViewLinkForModal(lowerLevelDependentObj.getId().toString(), lowerLevelDependentObj.getName()));
                }

            }
        }
        logger.info("getLowerLevelDependentDueToAuditableEntityResults() End");
        return lowerLevelDependentObjetSet;
    }

    /**
     * <P>
     * This method returns the lower level dependency if any.
     * </P>
     * 
     * @param query
     * @return
     * @throws Exception
     */
    private Set<String> getLowerLevelDependentResults(String query) throws Exception {

        //logger.info("populateLowerLevelDevependentyQueryResults() Start");

        // Method Level Variables.
        ITabularResultSet resultSet = null;
        Set<String> lowerLevelDependentObjetSet = new HashSet<String>();
        IGRCObject lowerDSMTLinkObj = null;
        IGRCObject parentObj = null;


        // Method Implementation.
        // Execute the query
        resultSet = grcObjectSearchUtil.executeQuery(query);

        // If the results are present
        if (isObjectNotNull(resultSet)) {

            // Iterate through the results
            for (IResultSetRow row : resultSet) {

                lowerDSMTLinkObj = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(0)));
                parentObj = grcObjectUtil.getObjectFromId(lowerDSMTLinkObj.getPrimaryParent());
                lowerLevelDependentObjetSet.add(dsmtLinkHelperUtil.getObjectDetailViewLinkForModal(parentObj.getId().toString(), parentObj.getName()));
            }
        }
        //logger.info("lowerLevelDependentObjetSet : " + lowerLevelDependentObjetSet);
        //logger.info("populateLowerLevelDevependentyQueryResults() End");
        return lowerLevelDependentObjetSet;
    }

    /**
     * <P>
     * This method retrieves the Helper Landing Page Content Section. The Landing Page Title, Landing Page Content and
     * the fields and the values of the fields from the object from which the helper was launched are obtained. The
     * Landing page information is set in an instance of the {@link DSMTObjectGenericDetails}.
     * </P>
     *
     * @param dsmtLinkHelperInfo
     *            an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return - the dsmtLinkHelperInfo with the objects basic information set
     * @throws Exception
     */
    private DSMTLinkHelperAppInfo getBasicObjectInfoForDisplay(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        logger.info("getBasicObjectInfoForDisplay() Start");

        // Method Level Variables.
        String contentBody = EMPTY_STRING;
        String contentHeader = EMPTY_STRING;
        String registrySetting = EMPTY_STRING;
        String grcObjFieldRegValue = EMPTY_STRING;

        IGRCObject grcObject = null;
        List<GRCObjectDetailsInfo> objFieldsInfo = null;
        DSMTObjectGenericDetails helperObjectContentInfo = null;

        /* Initialize Variables */
        // objFieldsInfo = new HashMap<String, String>();
        helperObjectContentInfo = new DSMTObjectGenericDetails();

        // Prepare the registry setting path based on the base registry setting and the object type
        // and obtain the list of fields that needs to be displayed in the UI from the registry setting.
        registrySetting = dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LINK_APP_OBJECT_DISPLAY_INFO;
        grcObjFieldRegValue = applicationUtil.getRegistrySetting(registrySetting);

        // Obtain the object under execution, Get the fields and its values that needs to be displayed in the UI
        logger.info("objectId : " + dsmtLinkHelperInfo.getObjectID());
        grcObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        objFieldsInfo = dsmtLinkHelperUtil.getBasicObjectInformationForDisplay(grcObject, grcObjFieldRegValue);

        // Get the Helper page content header and body to be displayed in the UI
        contentHeader = applicationUtil
                .getRegistrySetting(dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LINK_APP_TITLE, EMPTY_STRING);
        contentBody = applicationUtil.getRegistrySetting(
                dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LINK_APP_DISPLAY_NAME, EMPTY_STRING);

        // Set all the obtained values in the bean to be returned
        helperObjectContentInfo.setContentBody(contentBody);
        helperObjectContentInfo.setContentHeader(contentHeader);
        helperObjectContentInfo.setGeneralDetails(objFieldsInfo);

        dsmtLinkHelperInfo.setHelperObjectContentInfo(helperObjectContentInfo);
        logger.info("getBasicObjectInfoForDisplay() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * <P>
     * This method retrieves the available DSMT Links objects under searched auditable entities.
     * </P>
     *
     * @param dsmtLinkHelperInfo
     *            an instance of the {@link DSMTLinkHelperAppInfo}
     * @param aeSearch
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception
     */
    public DataGridInfo getAvailableObjectInformation(DSMTLinkHelperAppInfo dsmtLinkHelperInfo, String aeSearch)
            throws Exception {

        logger.info("getAvailableDSMTLinkObjectInformation() Start");

        // Method Level Variables.
        String aeQuery = EMPTY_STRING;
        String dsmtQuery = EMPTY_STRING;
        String aeFolderRegSetting = EMPTY_STRING;
        String aeFolderRegValue = EMPTY_STRING;

        /* Initialize Variables */
        Set<String> aeSet = new HashSet<String>();
        Set<String> baseDSMTSet = new HashSet<String>();

        DataGridInfo availableQuadsInfo = null;
        // DataGridInfo newQuadsInfo = null;

        // get the AE folder registry setting path based on the base registry setting and the object type.
        aeFolderRegSetting = dsmtLinkHelperInfo.getObjRegistrySetting() + AUDITABLE_ENTITY_FOLDER_PATH;
        aeFolderRegValue = applicationUtil.getRegistrySetting(aeFolderRegSetting);
        logger.info("Auditable Entity Folder Path : " + aeFolderRegValue);

        if(isNullOrEmpty(aeFolderRegValue)) {
            logger.info("Auditable Entity Folder Path is null or empty : " + aeFolderRegValue);
            logger.info("getAvailableDSMTLinkObjectInformation() End!");
            return availableQuadsInfo;
        }

        // Construct Auditable Entity query to get all the DSMT Links under searched Auditable Entities
        aeQuery = AVAILABLE_AUDITABLE_ENTITY_BASE_QUERY.replaceAll(AUDITABLE_ENTITY_STR, aeSearch)
                .replaceAll(AUDITABLE_ENTITY_PATH_STR, aeFolderRegValue);
        logger.info("Auditable Entity Query : " + aeQuery);

        // Construct DSMT link query to get all the DSMT Links under Audit Object so that
        // we can compare with AE query and remove DSMT Links that are already Associated with Audit.
        dsmtQuery = AVAILABLE_DSMT_LINK_BASE_QUERY + dsmtLinkHelperInfo.getObjectID();
        logger.info("Available DSMT Link Query : " + dsmtQuery);

        // get all the DSMT Links under searched Auditable Entities
        aeSet = returnQueryResultsSet(aeQuery);
        logger.info("Auditable Entity Set : " + aeSet);

        // get all the Base DSMT Links under Audit
        baseDSMTSet = returnQueryResultsSet(dsmtQuery);
        logger.info("Base DSMT Set : " + baseDSMTSet);

        // Populate Available DSMTLinkObject
        availableQuadsInfo = populateAvailableDSMTLinkObject(dsmtLinkHelperInfo, aeSet, baseDSMTSet);
        //logger.info("availableQuadsInfo : " + availableQuadsInfo);

        logger.info("getAvailableDSMTLinkObjectInformation() End");
        return availableQuadsInfo;
    }

    /**
     * <P>
     * This method populates the available DSMT Link objects to the DSMTLinkObjectInfo.
     * </P>
     * 
     * @param dsmtLinkHelperInfo
     * @param aeSet
     * @param baseDSMTSet
     * @return
     * @throws Exception
     */
    private DataGridInfo populateAvailableDSMTLinkObject(DSMTLinkHelperAppInfo dsmtLinkHelperInfo, Set<String> aeSet,
            Set<String> baseDSMTSet) throws Exception {

        logger.info("populateAvailableDSMTLinkObject() Start");

        // Method Level Variables.
        String availableDSMTLinkQuery = EMPTY_STRING;
        ITabularResultSet resultSet = null;
        IGRCObject aeAppObject = null;
        ExistingDSMTLinkInfo dsmtLinkObjectInfo = null;
        DSMTLinkDisableInfo dsmtLinkDisableInfo = null;
        Set<String> disableInfoList = null;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList = null;
        LinkedHashMap<String, List<ExistingDSMTLinkInfo>> availableDSMTLinksInfoMap = null;
        DataGridInfo carbonDataGridInfo = null;

        String aeAppStatus = EMPTY_STRING;
        String aeAppScope = EMPTY_STRING;
        IGRCObject dsmtLinkObj = null;
        int aeAppAssociationReq = 0;
        int aeAppStatusNotBlank = 0;

        /* Initialize Variables */
        availableDSMTLinksInfoMap = new LinkedHashMap<String, List<ExistingDSMTLinkInfo>>();
        Map<String, String> disabledAEMap = new HashMap<String, String>();

        // loop through all DSMT Links under AE and populate the available DMST Link tree
        for (String aeId : aeSet) {

            // Construct a individual DSMT Link Query for each AE
            availableDSMTLinkQuery = AVAILABLE_DSMT_LINK_IN_AUDITABLE_ENTITY_BASE_QUERY + aeId;
            logger.info("availableDSMTQuery : " + availableDSMTLinkQuery);
            resultSet = grcObjectSearchUtil.executeQuery(availableDSMTLinkQuery);

            /* Initialize Variables */
            aeAppAssociationReq = 0;
            aeAppStatusNotBlank = 0;
            //dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();
            // dsmtLinkDisableInfo = new StringBuffer(EMPTY_STRING);

            // If Audit status is not draft, check if AEApp already exist and if it approved or not.
            if (isNotEqual(AUDIT_DRAFT_STATUS, dsmtLinkHelperInfo.getObjectStatus())) {
                String aeAppQuery = AUDITABLE_ENTITY_APP_QUERY
                        .replace(AUDITPROGRAM_ID_STR, dsmtLinkHelperInfo.getObjectID())
                        .replace(AUDITABLE_ENTITY_ID_STR, aeId);
                logger.info("AE App Query: " + aeAppQuery);

                Set<String> aeAppSet = new HashSet<String>();
                aeAppSet = returnQueryResultsSet(aeAppQuery);
                logger.info("aeAppSet : " + aeAppSet);

                // AEApp exist, check the status of this AEApp
                if (isSetNotNullOrEmpty(aeAppSet)) {
                    aeAppObject = grcObjectUtil.getObjectFromId(aeAppSet.iterator().next());
                    logger.info("Existing AEApp Object name [" + aeAppObject.getName() + "] and Id [" + aeAppObject.getId() + "]");

                    aeAppStatus = fieldUtil.getFieldValueAsString(aeAppObject, AUD_ENTITY_APP_STATUS_FIELD);
                    aeAppScope = fieldUtil.getFieldValueAsString(aeAppObject, AUD_ENTITY_APP_SCOPE_FIELD);
                    logger.info("AEApp Status [" + aeAppStatus + "] and Scope [" + aeAppScope + "]");

                    //If AE App already exists, and status != Blank, display the AE in disable mode and 
                    //all the DSMT Link under this AE will also will automatically disabled
                    if (isNotNullOrEmpty(aeAppStatus)) {
                        logger.info("Add AE to disable list due to status != Blank : " + aeId);
                        disabledAEMap.put(aeId, AUD_ENTITY_APP_STATUS_NOT_BLANK_MESSAGE);
                        aeAppStatusNotBlank = 1;
                    } 
                    //If AE App already exists, and status == Blank and Scope == out, display the AE in selectable mode 
                    // and mark all the DSMT Link under this AE disabled
                    else if (isNullOrEmpty(aeAppStatus) && isEqualIgnoreCase(SCOPE_OUT_STATUS, aeAppScope)) {
                        logger.info("All the DSMT under this AE will be disabled due to status == Blank and Scope == out : " + aeId);
                        aeAppAssociationReq = 1;
                    }
                }
                // AEApp doesn't exist, so disable DSMT Link under AE.
                else {

                    logger.info("AEApp doesn't exist, so All the DSMT under this AE will be disabled : " + aeId);
                    aeAppAssociationReq = -1;
                }
            }

            availableDSMTLinksInfoMap.put(aeId, new ArrayList<ExistingDSMTLinkInfo>());
            // Iterate through DSMT Link result set
            for (IResultSetRow row : resultSet) {

                dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();
                dsmtLinkDisableInfo = new DSMTLinkDisableInfo();
                disableInfoList = new HashSet<String>();
                if(aeAppAssociationReq > 0) {
                    disableInfoList.add(AUD_ENTITY_ASSOCIATION_NOT_APPROVED_MESSAGE);
                    dsmtLinkObjectInfo.setDisabled(true);
                }else if (aeAppAssociationReq < 0){
                    disableInfoList.add(AUD_ENTITY_ASSOCIATION_DOES_NOT_EXIST_MESSAGE);
                    dsmtLinkObjectInfo.setDisabled(true);
                }

                if(aeAppStatusNotBlank > 0) {
                    disableInfoList.add(AUD_ENTITY_APP_STATUS_NOT_BLANK_MESSAGE);
                    dsmtLinkObjectInfo.setDisabled(true);
                }

                dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
                dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);

                String dsmtLinkId = fieldUtil.getFieldValueAsString(row.getField(1));
                dsmtLinkObj = grcObjectUtil.getObjectFromId(dsmtLinkId);
                logger.info("DSMT Link Object name [" + dsmtLinkObj.getName() + "] and Id [" + dsmtLinkObj.getId() + "]");

                if (null != baseDSMTSet && baseDSMTSet.contains(dsmtLinkId)) {

                    logger.info("DSMT Link Object already exist!");
                    continue;
                }

                dsmtLinkObjectInfo.setName(fieldUtil.getFieldValueAsString(row.getField(0)));
                dsmtLinkObjectInfo.setResourceId(fieldUtil.getFieldValueAsString(row.getField(1)));
                dsmtLinkObjectInfo.setDescription(fieldUtil.getFieldValueAsString(row.getField(2)));
                dsmtLinkObjectInfo.setType(fieldUtil.getFieldValueAsString(row.getField(3)));
                dsmtLinkObjectInfo.setScope(fieldUtil.getFieldValueAsString(row.getField(4)));
                dsmtLinkObjectInfo.setStatus(fieldUtil.getFieldValueAsString(row.getField(5)));

                dsmtLinkObjectInfo.setBaseId(fieldUtil.getFieldValueAsString(row.getField(7)));
                dsmtLinkObjectInfo.setActive(fieldUtil.getFieldValueAsString(row.getField(8)));

                dsmtLinkObjectInfo.setLink(dsmtLinkHelperUtil
                        .getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(), dsmtLinkObjectInfo.getName()));
                dsmtLinkObjectInfo.setLegalvehiclename(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_LVNAME_FIELD));
                dsmtLinkObjectInfo.setManagedsegmentname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MSNAME_FIELD));
                dsmtLinkObjectInfo.setManagedgeographyname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MGNAME_FIELD));
                dsmtLinkObjectInfo.setParentResourceId(aeId);

                dsmtLinkObjectList = availableDSMTLinksInfoMap.get(aeId);
                dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ? dsmtLinkObjectList
                        : new ArrayList<ExistingDSMTLinkInfo>();
                dsmtLinkObjectList.add(dsmtLinkObjectInfo);
                availableDSMTLinksInfoMap.put(aeId, dsmtLinkObjectList);
            }
        }
        carbonDataGridInfo = processDataForDisplay(availableDSMTLinksInfoMap, true, false, false, disabledAEMap, null, null, dsmtLinkHelperInfo);
        //logger.info("populateAvailableDSMTLinkObject.carbonDataGridInfo : " + carbonDataGridInfo);
        logger.info("populateAvailableDSMTLinkObject() End");
        return carbonDataGridInfo;
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
    private Set<String> returnQueryResultsSet(String query) throws Exception {

        logger.info("returnQueryResultsSet() Start");

        Set<String> returnSet = new HashSet<String>();
        //logger.info("query: " + query);
        ITabularResultSet resultSet = grcObjectSearchUtil.executeQuery(query, false, true);

        for (IResultSetRow row : resultSet) {
            returnSet.add(fieldUtil.getFieldValueAsString(row.getField(1)));
        }

        logger.info("returnQueryResultsSet() End");
        return returnSet;
    }

    /**
     * <P>
     * This method process the ignores New DSMT Link objects.
     * </P>
     *
     * @param dsmtLinkHelperInfo
     *            an instance of the {@link DSMTLinkHelperAppInfo}
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception
     */
    @Override
    public List<DSMTLinkObjectInfo> ignoreNewDSMTLinksFromAudit(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        logger.info("ignoreNewDSMTLinksFromAudit() Start");
        logger.info("ignoreNewDSMTIdList : " + dsmtLinkHelperInfo.getUpdateDsmtLinkInfo());

        // Method Level Variables.
        IGRCObject auditObject = null;
        Map<String, String> fieldsToUpdateInfo = null;
        String auditDSMTLisFieldVal = EMPTY_STRING;
        List<UpdateDSMTLinkInfo> ignoreDSMTInfoList = null;
        List<DSMTLinkObjectInfo> dsmtLinkDescopingFailInfo = null;

        /* Initialize Variables */
        ignoreDSMTInfoList = dsmtLinkHelperInfo.getUpdateDsmtLinkInfo();
        auditObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());

        for (UpdateDSMTLinkInfo ignoreDSMTLinkInfo : ignoreDSMTInfoList) {

            // Iterate through DSMT Links selected by user and remove any matching Base Id from New DSMT Links List
            // fields
            // of Audit Object.
            for (String dsmtLinkId : ignoreDSMTLinkInfo.getDsmtLinkIdsToUpdate()) {

                auditDSMTLisFieldVal = fieldUtil.getFieldValueAsString(auditObject, AUDIT_NEW_DSMT_LINK_LIST_FIELD);
                List<String> newBaseIdList = parseDelimitedValuesIgnoringNullOrSpace(auditDSMTLisFieldVal, COMMA);
                if (newBaseIdList.contains(dsmtLinkId)) {
                    newBaseIdList.removeAll(Collections.singleton(dsmtLinkId));

                    // Instantiate the fields to update map, the map will hold the fields to update as the keys and
                    // the values with which the field will be updated as the values for the keys.
                    fieldsToUpdateInfo = new HashMap<String, String>();
                    fieldsToUpdateInfo.put(AUDIT_NEW_DSMT_LINK_LIST_FIELD, unParseCommaDelimitedValues(newBaseIdList));
                    grcObjectUtil.updateFieldsInObjectAndSave(auditObject, fieldsToUpdateInfo);
                }
            }
        }

        auditDSMTLisFieldVal = fieldUtil.getFieldValueAsString(auditObject, AUDIT_NEW_DSMT_LINK_LIST_FIELD);

        // After ignoring all DSMT Links, check if New DSMT Link List field is empty,
        // and if it is empty then set the New DSMT Link Flag field to No and blank out the New DSMT Link List field.
        if (isNullOrEmpty(auditDSMTLisFieldVal)
                || parseDelimitedValuesIgnoringNullOrSpace(auditDSMTLisFieldVal, COMMA).isEmpty()) {
            // Instantiate the fields to update map, the map will hold the fields to update as the keys and
            // the values with which the field will be updated as the values for the keys.
            fieldsToUpdateInfo = new HashMap<String, String>();
            fieldsToUpdateInfo.put(AUDIT_NEW_DSMT_LINK_FLAG_FIELD, NO);
            fieldsToUpdateInfo.put(AUDIT_NEW_DSMT_LINK_LIST_FIELD, EMPTY_STRING);
            grcObjectUtil.updateFieldsInObjectAndSave(auditObject, fieldsToUpdateInfo);
            logger.info("New DSMT Link List field is empty!");
        }

        logger.info("ignoreNewDSMTLinksFromAudit() End");
        return dsmtLinkDescopingFailInfo;
    }
}
