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

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.NO;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.YES;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isEqualIgnoreCase;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isListNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isMapNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isNotEqualIgnoreCase;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isObjectNotNull;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isObjectNull;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isSetNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.parseCommaDelimitedValues;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.ASSOCIATED_DSMT_LINK_QUERY;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.ASSOCIATED_DSMT_LINK_BASE_ID_QUERY;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.AUDITABLE_ENTITY_TYPE_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.AVAILABLE_DSMT_LINK_UNDER_ISSUE_QUERY;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.BASE_DSMT_LINK_IS_NOT_ACTIVE_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.BASE_DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.BASE_ID_STR;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_ACTIVE_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_AE_ID_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_BASE_ID_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_IS_NOT_ACTIVE_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_LVNAME_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_MGNAME_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_MSNAME_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_SCOPE_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_STATUS_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.DSMT_LINK_TYPE_FIELD;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.HELPER_UI_DISPLAY_FIELDS_INFO;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.HELPER_UI_DISPLAY_FIELDS_INFO_EXISTING;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.HELPER_UI_DISPLAY_FIELDS_INFO_SEARCH;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.IN;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.INVALID_DSMT_LINK_ASSOCIATED_CAP_BASE_QUERY;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.OUT;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.ORIGINAL_DSMT_LINK_NOT_FOUND_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.ORIGINAL_DSMT_LINK_UNDER_ISSUE_QUERY;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.PRIMARY_PARENT_NOT_FOUND_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.CorrectiveActionPlanDSMTLinkConstants.PRIMARY_PARENT_NOT_ISSUE_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.CAP_DSMT_LINK_SERVICE_LOG_FILE_NAME;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_BASE_SETTING;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_DISPLAY_NAME;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_OBJECT_DISPLAY_INFO;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_TITLE;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.INVALID_DSMT_EXISTS_FIELD;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.ISSUE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.openpages.api.application.CopyConflictBehavior;
import com.ibm.openpages.api.application.CopyObjectOptions;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IApplicationService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;
import com.ibm.openpages.ext.tss.helpers.service.IHelperService;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.GRCObjectSearchUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.DSMTLinkDisableInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkObjectAssociationInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkObjectInfo;
import com.ibm.openpages.ext.ui.bean.DSMTObjectGenericDetails;
import com.ibm.openpages.ext.ui.bean.DataGridHeaderColumnInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;
import com.ibm.openpages.ext.ui.bean.ExistingDSMTLinkInfo;
import com.ibm.openpages.ext.ui.bean.GRCObjectDetailsInfo;
import com.ibm.openpages.ext.ui.bean.UpdateDSMTLinkInfo;
import com.ibm.openpages.ext.ui.service.ICorrectiveActionPlanDSMTLinkService;
import com.ibm.openpages.ext.ui.util.DSMTLinkHelperUtil;

@Service("capDSMTLinkServiceImpl")
public class CorrectiveActionPlanDSMTLinkServiceImpl implements ICorrectiveActionPlanDSMTLinkService {

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
    DSMTLinkServiceUtilImpl dsmtLinkServiceUtilImpl;

    @Autowired
    DSMTLinkHelperUtil dsmtLinkHelperUtil;

    @Autowired
    IServiceFactoryProxy serviceFactoryProxy;

    /**
     * Construct any information once the bean is created.
     */
    @PostConstruct
    public void initServiceImpl() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(CAP_DSMT_LINK_SERVICE_LOG_FILE_NAME);
    }

    /**
     * <P>
     * This method retrieves the Helpers Header Information from the Registry settings and sets the appropriate values
     * in the {@link DSMTLinkHelperAppHeaderInfo} object. This will be used to display the content in the UI Header.
     * </P>
     *
     * @param  dsmtLinkHelperInfo
     *                                an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return                    the dsmtLinkHelperInfo with the helper header information set
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
     * @param  dsmtLinkHelperInfo
     *                                an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return                    - the dsmtLinkHelperInfo with the objects basic information and the association
     *                            information set
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

    /**
     * <P>
     * This method retrieves the existing DSMT Links under the CAP.
     * </P>
     *
     * @param  dsmtLinkHelperInfo
     *                                an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return                    a JSON representation of the DataGridInfo
     * @throws Exception
     */
    @Override
    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getExistingDSMTLinks() Start");

        String query = EMPTY_STRING;
        DataGridInfo existingQuadsInfo = null;

        // Construct query to get all valid DSMT Links under CAP object
        query = ASSOCIATED_DSMT_LINK_QUERY + dsmtLinkHelperInfo.getObjectID() + " \n";
        logger.info("Associated DSMT Link Query: \n" + query);

        // Set Existing DSMTLinkObject into DSMTLinkHelperAppInfo
        existingQuadsInfo = getExistingDSMTLinkObject(dsmtLinkHelperInfo, query);

        logger.info("getExistingDSMTLinks() End");
        return existingQuadsInfo;
    }

    /**
     * <P>
     * This method descopes the DSMT Link objects that were selected in the UI.
     * </P>
     * 
     * @param  dsmtLinkHelperInfo
     *                                an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return                    - the list of failed {@link DSMTLinkObjectInfo} if any
     * @throws Exception
     */
    @Override
    public List<DSMTLinkObjectInfo> descopeDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("descopeDSMTLinks() Start");

        // Method Level Variables.
        IGRCObject dsmtLinkObject = null;
        Map<String, String> fieldsToUpdateInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkUpdateFailInfo = null;

        logger.info("DSMT Link Descoping List : " + dsmtLinkHelperInfo.getUpdateDsmtLinkInfo());

        // Make sure the Descoping information is present to continue
        if (isListNotNullOrEmpty(dsmtLinkHelperInfo.getUpdateDsmtLinkInfo()))
        {

            // Iterate through the list of Descoping items.
            for (UpdateDSMTLinkInfo descopeDSMTInfo : dsmtLinkHelperInfo.getUpdateDsmtLinkInfo())
            {

                // Iterate through each DSMT Link Ids and descope it individually
                logger.info("DSMT Link List Ids for descoping : " + descopeDSMTInfo.getDsmtLinkIdsToUpdate());
                if (isListNotNullOrEmpty(descopeDSMTInfo.getDsmtLinkIdsToUpdate()))
                {

                    // Iterate through the list of DSMT links to be descoped.
                    for (String dsmtLinkId : descopeDSMTInfo.getDsmtLinkIdsToUpdate())
                    {

                        dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);

                        // Make sure the Id passed corresponds to an object in the system
                        if (isObjectNotNull(dsmtLinkObject))
                        {

                            logger.info("DSMT Link Object name [" + dsmtLinkObject.getName() + "] and Id ["
                                    + dsmtLinkObject.getId() + "]");

                            // when we are descoping the DSMT, then mark scope out in the DSMT Link Obj
                            fieldsToUpdateInfo = new HashMap<String, String>();
                            fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, OUT);

                            grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                            logger.info("DSMT Link Obj scope field updated to : " + OUT);
                        }
                    }

                }
            }
        }

        dsmtLinkServiceUtilImpl.updateInvalidDMSTLinkField(dsmtLinkHelperInfo.getObjectID(),
                INVALID_DSMT_LINK_ASSOCIATED_CAP_BASE_QUERY, INVALID_DSMT_EXISTS_FIELD, false);

        // Return the DSMT Link Descoping failure information
        logger.info("descopeDSMTLinks() End");
        return dsmtLinkUpdateFailInfo;
    }

    /**
     * <P>
     * This method scopes the DSMT Link objects that were selected in the UI.
     * </P>
     * 
     * @param  dsmtLinkHelperInfo
     *                                an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return                    - the list of failed {@link DSMTLinkObjectInfo} if any
     * @throws Exception
     */
    @Override
    public List<DSMTLinkObjectInfo> associateDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("associateDSMTLinks() Start");

        // Method Level Variables.
        IGRCObject dsmtLinkObject = null;
        IGRCObject capObject = null;
        List<DSMTLinkObjectInfo> dsmtLinkUpdateFailInfo = null;

        IServiceFactory serviceFactory = null;
        IApplicationService applicationService = null;
        CopyObjectOptions copyOptions = null;

        /* Initialize Variables */
        capObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        serviceFactory = serviceFactoryProxy.getServiceFactory();
        applicationService = serviceFactory.createApplicationService();

        logger.info("Associate DSMT Link List : " + dsmtLinkHelperInfo.getUpdateDsmtLinkInfo());

        // Make sure the Associating DSMT Ids are present to continue
        if (isListNotNullOrEmpty(dsmtLinkHelperInfo.getUpdateDsmtLinkInfo()))
        {

            // Iterate through the list of Associating items. The items are grouped by the Auditable Entities.
            for (UpdateDSMTLinkInfo associateDSMTInfo : dsmtLinkHelperInfo.getUpdateDsmtLinkInfo())
            {

                logger.info("AE Id : " + associateDSMTInfo.getAuditableEntityId());
                // Make sure that Auditable Entity available for scoping.
                if (isNotNullOrEmpty(associateDSMTInfo.getAuditableEntityId()))
                {

                    logger.info("DSMT Link List for update : " + associateDSMTInfo.getDsmtLinkIdsToUpdate());
                    // Make sure there are DSMT Links under the Auditable Entity available to be scoped.
                    if (isListNotNullOrEmpty(associateDSMTInfo.getDsmtLinkIdsToUpdate()))
                    {

                        // Iterate through the list of DSMT links to be scoped.
                        for (String dsmtLinkId : associateDSMTInfo.getDsmtLinkIdsToUpdate())
                        {

                            // Get the DSMT Link object from the ID passed from the UI
                            dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);

                            logger.info("Base DSMT Link Object name [" + dsmtLinkObject.getName() + "] and Id ["
                                    + dsmtLinkObject.getId() + "]");

                            // Create a copy object of DSMT Link for association under audit
                            copyOptions = new CopyObjectOptions();
                            copyOptions.setConflictBehavior(CopyConflictBehavior.OVERWRITE);
                            copyOptions.setIncludeChildren(false);

                            List<Id> dsmtLinkList = new ArrayList<Id>();
                            dsmtLinkList.add(dsmtLinkObject.getId());
                            List<IGRCObject> copiedObjList = applicationService.copyToParent(capObject.getId(),
                                    dsmtLinkList, copyOptions);

                            for (IGRCObject newDSMTObj : copiedObjList)
                            {

                                logger.info("New DSMT Link Object name [" + newDSMTObj.getName() + "] and Id ["
                                        + newDSMTObj.getId() + "]");
                            }
                        }
                    }
                }
            }
        }

        dsmtLinkServiceUtilImpl.updateInvalidDMSTLinkField(dsmtLinkHelperInfo.getObjectID(),
                INVALID_DSMT_LINK_ASSOCIATED_CAP_BASE_QUERY, INVALID_DSMT_EXISTS_FIELD, false);

        // Return the DSMT Link Associating failure information
        logger.info("associateDSMTLinks() End");
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
                            fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, IN);

                            grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                            logger.info("DSMT Link Obj scope field updated to : " + IN);
                        }
                    }

                }
            }
        }

        dsmtLinkServiceUtilImpl.updateInvalidDMSTLinkField(dsmtLinkHelperInfo.getObjectID(),
                INVALID_DSMT_LINK_ASSOCIATED_CAP_BASE_QUERY, INVALID_DSMT_EXISTS_FIELD, false);

        // Return the DSMT Link re-scoping failure information
        logger.info("rescopeDSMTLinks() End");
        return dsmtLinkUpdateFailInfo;
    }

    /**
     * <P>
     * This method retrieves the Helper Landing Page Content Section. The Landing Page Title, Landing Page Content and
     * the fields and the values of the fields from the object from which the helper was launched are obtained. The
     * Landing page information is set in an instance of the {@link DSMTObjectGenericDetails}.
     * </P>
     *
     * @param  dsmtLinkHelperInfo
     *                                an instance of the {@link DSMTLinkHelperAppInfo}
     *
     * @return                    - the dsmtLinkHelperInfo with the objects basic information set
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
     * This method retrieves the available DSMT Links that are associated under the ancestor Audit.
     * </P>
     *
     * @param  dsmtLinkHelperInfo
     *                                an instance of the {@link DSMTLinkHelperAppInfo}
     * @return                    a JSON representation of the DataGridInfo
     * @throws Exception
     */
    @Override
    public DataGridInfo getAvailableDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getAvailableDSMTLinks() Start");

        // Method Level Variables.
        String issueDSMTLinkQuery = EMPTY_STRING;
        String capDMSTLinkBaseIdQuery = EMPTY_STRING;

        /* Initialize Variables */
        Set<String> issueDMSTLinkSet = new HashSet<String>();
        Set<String> capDSMTLinkBaseIdSet = new HashSet<String>();

        DataGridInfo availableQuadsInfo = null;

        IGRCObject capObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        logger.info("CAP Object name [" + capObject.getName() + "] and Id [" + capObject.getId() + "]");

        if (isObjectNull(capObject.getPrimaryParent()))
        {

            logger.info("Primary Parent not found!");
            logger.info("getAvailableDSMTLinks() End");
            return availableQuadsInfo;
        }

        IGRCObject issueObject = grcObjectUtil.getObjectFromId(capObject.getPrimaryParent());
        logger.info("Parent Object name [" + issueObject.getName() + "], Id [" + issueObject.getId() + "]"
                + ", Object Type[" + issueObject.getType().getName() + "]");

        if (isNotEqualIgnoreCase(issueObject.getType().getName(), ISSUE))
        {

            logger.info("Primary Parent is not Issue!");
            logger.info("getAvailableDSMTLinks() End");
            return availableQuadsInfo;
        }

        // Construct Issue DSMT Link query to get all the DSMT Links under parent Issue
        issueDSMTLinkQuery = AVAILABLE_DSMT_LINK_UNDER_ISSUE_QUERY + issueObject.getId();
        logger.info("issueDSMTLinkQuery : " + issueDSMTLinkQuery);

        // get all the DSMT Links under parent Issue
        issueDMSTLinkSet = dsmtLinkServiceUtilImpl.returnQueryResultsSet(issueDSMTLinkQuery);
        logger.info("Issue DSMT Link Set : " + issueDMSTLinkSet);

        // Construct DSMT link query to get all the DSMT Links under CAP Object so that
        // we can compare with Issue DSMT Link query and remove DSMT Links that are already Associated with CAP.
        capDMSTLinkBaseIdQuery = ASSOCIATED_DSMT_LINK_BASE_ID_QUERY + dsmtLinkHelperInfo.getObjectID();
        logger.info("capDMSTLinkBaseIdQuery : " + capDMSTLinkBaseIdQuery);

        // get all the CAP DSMT Link base id set
        capDSMTLinkBaseIdSet = dsmtLinkServiceUtilImpl.returnQueryResultsSet(capDMSTLinkBaseIdQuery);
        logger.info("CAP DSMT Link Base Id Set : " + capDSMTLinkBaseIdSet);

        // Populate Available DSMTLinkObject
        availableQuadsInfo = populateAvailableDSMTLink(dsmtLinkHelperInfo, issueDMSTLinkSet, capDSMTLinkBaseIdSet);
        logger.info("availableQuadsInfo : " + availableQuadsInfo);

        logger.info("getAvailableDSMTLinks() End");
        return availableQuadsInfo;
    }

    /**
     * <P>
     * This method returns the existing DSMT Links associated to the current object grouped by the Auditable Entity ID
     * present in the DSMT link object.
     * </P>
     * 
     * @param  dsmtLinkHelperInfo
     *                                an instance of the {@link DSMTLinkHelperAppInfo}
     * @param  query
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

        DSMTLinkDisableInfo dsmtLinkDisableInfo = null;
        Set<String> disableInfoList = null;

        ITabularResultSet resultSet = null;
        DataGridInfo carbonDataGridInfo = null;
        Map<String, List<ExistingDSMTLinkInfo>> associatedDSMTLinksInfoMap = null;

        ExistingDSMTLinkInfo dsmtLinkObjectInfo = null;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList = null;

        associatedDSMTLinksInfoMap = new TreeMap<String, List<ExistingDSMTLinkInfo>>();
        resultSet = grcObjectSearchUtil.executeQuery(query);

        for (IResultSetRow row : resultSet)
        {

            dsmtLinkDisableInfo = new DSMTLinkDisableInfo();
            disableInfoList = new HashSet<String>();
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

            dsmtLinkObjectInfo.setLink(dsmtLinkHelperUtil.getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(),
                    dsmtLinkObjectInfo.getName()));
            dsmtLinkObjectInfo
                    .setLegalvehiclename(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_LVNAME_FIELD));
            dsmtLinkObjectInfo
                    .setManagedsegmentname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MSNAME_FIELD));
            dsmtLinkObjectInfo
                    .setManagedgeographyname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MGNAME_FIELD));
            dsmtLinkObjectInfo.setParentResourceId(auditableEntityId);

            if (isEqualIgnoreCase(active, YES) &&  isEqualIgnoreCase(scope, OUT))
            {

                IGRCObject capObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
                logger.info("CAP Object name [" + capObject.getName() + "] and Id [" + capObject.getId() + "]");

                if (isObjectNull(capObject.getPrimaryParent()))
                {

                    disableInfoList.add(PRIMARY_PARENT_NOT_FOUND_MESSAGE);
                    dsmtLinkObjectInfo.setDisabled(true);

                } else {

                    IGRCObject issueObject = grcObjectUtil.getObjectFromId(capObject.getPrimaryParent());
                    logger.info("Parent Object name [" + issueObject.getName() + "], Id [" + issueObject.getId() + "]"
                            + ", Object Type[" + issueObject.getType().getName() + "]");

                    if (isNotEqualIgnoreCase(issueObject.getType().getName(), ISSUE))
                    {

                        disableInfoList.add(PRIMARY_PARENT_NOT_ISSUE_MESSAGE);
                        dsmtLinkObjectInfo.setDisabled(true);
                    } else {

                        // Construct Original DSMT query to get the DSMT Id
                        String orgDSMTLInkIdQuery = ORIGINAL_DSMT_LINK_UNDER_ISSUE_QUERY
                                .replace(BASE_ID_STR, baseId) + issueObject.getId().toString() + " \n";
                        logger.info("Original DSMT Link Query: " + orgDSMTLInkIdQuery);

                        Set<String> orginalDSMTLinkSet = new HashSet<String>();
                        orginalDSMTLinkSet = dsmtLinkServiceUtilImpl.returnQueryResultsSet(orgDSMTLInkIdQuery);
                        logger.info("orginalDSMTLinkSet : " + orginalDSMTLinkSet);

                        // If original DSMT Link exist
                        if (isSetNotNullOrEmpty(orginalDSMTLinkSet)) {

                            baseDSMTLinkObj = grcObjectUtil.getObjectFromId(orginalDSMTLinkSet.iterator().next());
                            logger.info("Original DMST Link Object name [" + baseDSMTLinkObj.getName() + "] and Id [" + baseDSMTLinkObj.getId() + "]");

                            baseActive = fieldUtil.getFieldValueAsString(baseDSMTLinkObj, DSMT_LINK_ACTIVE_FIELD);
                            baseStatus = fieldUtil.getFieldValueAsString(baseDSMTLinkObj, DSMT_LINK_STATUS_FIELD);
                            baseScope = fieldUtil.getFieldValueAsString(baseDSMTLinkObj, DSMT_LINK_SCOPE_FIELD);

                            if (isNotEqualIgnoreCase(baseScope, IN))
                            {
                                disableInfoList.add(BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE);
                                dsmtLinkObjectInfo.setDisabled(true);
                            }

                            if (isNotEqualIgnoreCase(baseActive, YES))
                            {
                                disableInfoList.add(BASE_DSMT_LINK_IS_NOT_ACTIVE_MESSAGE);
                                dsmtLinkObjectInfo.setDisabled(true);
                            }

                            if (isNotNullOrEmpty(baseStatus))
                            {
                                disableInfoList.add(BASE_DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE);
                                dsmtLinkObjectInfo.setDisabled(true);
                            }

                        } else {

                            disableInfoList.add(ORIGINAL_DSMT_LINK_NOT_FOUND_MESSAGE);
                            dsmtLinkObjectInfo.setDisabled(true);
                        }
                    }
                }
            }

            if (isNotNullOrEmpty(status))
            {
                disableInfoList.add(DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE);
                dsmtLinkObjectInfo.setDisabled(true);
            }

            if (isEqualIgnoreCase(active, NO) && isEqualIgnoreCase(scope, OUT))
            {
                disableInfoList.add(DSMT_LINK_IS_NOT_ACTIVE_MESSAGE);
                dsmtLinkObjectInfo.setDisabled(true);
            }

            dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
            dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);

            dsmtLinkObjectList = associatedDSMTLinksInfoMap.get(auditableEntityId);
            dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ? dsmtLinkObjectList
                    : new ArrayList<ExistingDSMTLinkInfo>();
            dsmtLinkObjectList.add(dsmtLinkObjectInfo);
            associatedDSMTLinksInfoMap.put(auditableEntityId, dsmtLinkObjectList);
        }

        dsmtLinkServiceUtilImpl.updateInvalidDMSTLinkField(dsmtLinkHelperInfo.getObjectID(),
                INVALID_DSMT_LINK_ASSOCIATED_CAP_BASE_QUERY, INVALID_DSMT_EXISTS_FIELD, false);

        carbonDataGridInfo = processDataForDisplay(associatedDSMTLinksInfoMap, false, true, dsmtLinkHelperInfo);

        logger.info("getExistingDSMTLinkObject() End");
        return carbonDataGridInfo;
    }

    private DataGridInfo processDataForDisplay(Map<String, List<ExistingDSMTLinkInfo>> existingDSMTLinksInfoMap,
            boolean isSearch, boolean isExistingDSMTLink, DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("processDataForDisplay() Start");

        int rowCount = 1;
        int childCount = 1;

        IGRCObject audEntityObj = null;
        DataGridInfo carbonDataGridInfo = null;
        ExistingDSMTLinkInfo existingDSMTLinkInfo = null;

        List<String> fieldNamesList = null;
        List<String> hierarchyList = null;
        List<ExistingDSMTLinkInfo> childDMSTLinkInfoList = null;
        List<DataGridHeaderColumnInfo> gridHeaderInfoList = null;
        List<ExistingDSMTLinkInfo> existingDMSTLinkInfoList = null;

        carbonDataGridInfo = new DataGridInfo();
        existingDMSTLinkInfoList = new LinkedList<ExistingDSMTLinkInfo>();

        if (isSearch)
        {
            fieldNamesList = parseCommaDelimitedValues(HELPER_UI_DISPLAY_FIELDS_INFO_SEARCH);
        }
        else if (isExistingDSMTLink) {
            fieldNamesList = parseCommaDelimitedValues(HELPER_UI_DISPLAY_FIELDS_INFO_EXISTING);
        }
        else
        {
            fieldNamesList = parseCommaDelimitedValues(HELPER_UI_DISPLAY_FIELDS_INFO);
        }

        gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);

        if (isMapNotNullOrEmpty(existingDSMTLinksInfoMap))
        {

            for (String audEntityId : existingDSMTLinksInfoMap.keySet())
            {

                existingDSMTLinkInfo = new ExistingDSMTLinkInfo();
                hierarchyList = new ArrayList<String>();

                audEntityObj = grcObjectUtil.getObjectFromId(audEntityId);
                childDMSTLinkInfoList = existingDSMTLinksInfoMap.get(audEntityId);
                hierarchyList.add(audEntityObj.getName());

                existingDSMTLinkInfo.setId("row-" + rowCount);
                existingDSMTLinkInfo.setName(audEntityObj.getName() + " - " + audEntityObj.getDescription());
                existingDSMTLinkInfo.setDescription(audEntityObj.getDescription());
                existingDSMTLinkInfo.setResourceId(audEntityId);
                existingDSMTLinkInfo
                        .setType(fieldUtil.getFieldValueAsString(audEntityObj, AUDITABLE_ENTITY_TYPE_FIELD));
                existingDSMTLinkInfo.setHasChildren(isListNotNullOrEmpty(childDMSTLinkInfoList));
                existingDSMTLinkInfo.setHierarchy(hierarchyList);
                existingDMSTLinkInfoList.add(existingDSMTLinkInfo);

                existingDSMTLinkInfo.setLink(dsmtLinkHelperUtil
                        .getObjectDetailViewLink(existingDSMTLinkInfo.getResourceId(), audEntityObj.getName()));

                if (isListNotNullOrEmpty(childDMSTLinkInfoList))
                {

                    childCount = 1;

                    for (ExistingDSMTLinkInfo childDSMTLinkInfo : childDMSTLinkInfoList)
                    {

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
            }
        }

        carbonDataGridInfo.setHeaders(gridHeaderInfoList);
        carbonDataGridInfo.setRows(existingDMSTLinkInfoList);

        logger.info("processDataForDisplay() End");
        return carbonDataGridInfo;
    }

    /**
     * <P>
     * This method populates the available DSMT Link objects to the DSMTLinkObjectInfo.
     * </P>
     * 
     * @param  dsmtLinkHelperInfo
     * @param  issueDMSTLinkSet
     * @param  capDSMTLinkBaseIdSet
     * @return                      a JSON representation of the DataGridInfo
     * @throws Exception
     */
    private DataGridInfo populateAvailableDSMTLink(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            Set<String> issueDMSTLinkSet, Set<String> capDSMTLinkBaseIdSet) throws Exception {

        logger.info("populateAvailableDSMTLink() Start");

        // Method Level Variables.
        ExistingDSMTLinkInfo dsmtLinkObjectInfo = null;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList = null;
        TreeMap<String, List<ExistingDSMTLinkInfo>> availableDSMTLinksInfoMap = null;
        DataGridInfo carbonDataGridInfo = null;

        IGRCObject dsmtLinkObj = null;

        String active = null;
        String status = null;
        String baseId = null;
        String scope = null;
        String auditableEntityId = null;

        /* Initialize Variables */
        availableDSMTLinksInfoMap = new TreeMap<String, List<ExistingDSMTLinkInfo>>();

        IGRCObject capObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        logger.info("CAP Object name [" + capObject.getName() + "] and Id [" + capObject.getId() + "]");

        if (isObjectNull(capObject.getPrimaryParent()))
        {

            logger.info("Primary Parent not found!");
            logger.info("populateAvailableDSMTLink() End");
            return carbonDataGridInfo;
        }

        IGRCObject issueObject = grcObjectUtil.getObjectFromId(capObject.getPrimaryParent());
        logger.info("Parent Object name [" + issueObject.getName() + "], Id [" + issueObject.getId() + "]"
                + ", Object Type[" + issueObject.getType().getName() + "]");

        if (isNotEqualIgnoreCase(issueObject.getType().getName(), ISSUE))
        {

            logger.info("Primary Parent is not Issue!");
            logger.info("populateAvailableDSMTLink() End");
            return carbonDataGridInfo;
        }

        // loop through all DSMT Links under Audit and populate the available DMST Link tree
        for (String dsmtLinkId : issueDMSTLinkSet)
        {

            // Get the DSMT Link object from the DSMT Link Id
            dsmtLinkObj = grcObjectUtil.getObjectFromId(dsmtLinkId);
            logger.info("DSMT Link Object name [" + dsmtLinkObj.getName() + "] and Id [" + dsmtLinkObj.getId() + "]");

            baseId = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_BASE_ID_FIELD);

            if (isSetNotNullOrEmpty(capDSMTLinkBaseIdSet) && capDSMTLinkBaseIdSet.contains(baseId))
            {
                logger.info("DSMT Link Object already exist!");
                continue;
            }

            dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();

            dsmtLinkObjectInfo.setName(dsmtLinkObj.getName());
            dsmtLinkObjectInfo.setResourceId(dsmtLinkObj.getId().toString());
            dsmtLinkObjectInfo.setDescription(dsmtLinkObj.getDescription());
            dsmtLinkObjectInfo.setType(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_TYPE_FIELD));

            scope = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_SCOPE_FIELD);
            dsmtLinkObjectInfo.setScope(scope);

            status = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_STATUS_FIELD);
            dsmtLinkObjectInfo.setStatus(status);

            auditableEntityId = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_AE_ID_FIELD);

            dsmtLinkObjectInfo.setBaseId(baseId);

            active = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_ACTIVE_FIELD);
            dsmtLinkObjectInfo.setActive(active);

            dsmtLinkObjectInfo.setLink(dsmtLinkHelperUtil.getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(),
                    dsmtLinkObjectInfo.getName()));
            dsmtLinkObjectInfo
                    .setLegalvehiclename(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_LVNAME_FIELD));
            dsmtLinkObjectInfo
                    .setManagedsegmentname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MSNAME_FIELD));
            dsmtLinkObjectInfo
                    .setManagedgeographyname(fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MGNAME_FIELD));
            dsmtLinkObjectInfo.setParentResourceId(auditableEntityId);

            dsmtLinkObjectList = availableDSMTLinksInfoMap.get(auditableEntityId);
            dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ? dsmtLinkObjectList
                    : new ArrayList<ExistingDSMTLinkInfo>();
            dsmtLinkObjectList.add(dsmtLinkObjectInfo);
            availableDSMTLinksInfoMap.put(auditableEntityId, dsmtLinkObjectList);
        }

        carbonDataGridInfo = processDataForDisplay(availableDSMTLinksInfoMap, true, false, dsmtLinkHelperInfo);
        logger.info("populateAvailableDSMTLink.carbonDataGridInfo : " + carbonDataGridInfo);

        logger.info("populateAvailableDSMTLink() End");
        return carbonDataGridInfo;
    }

}
