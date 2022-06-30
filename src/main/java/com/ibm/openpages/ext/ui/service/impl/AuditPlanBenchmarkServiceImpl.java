/*
 IBM Confidential OCO Source Materials
 5725-D51, 5725-D52, 5725-D53, 5725-D54
 © Copyright IBM Corporation 2021
 The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 has been deposited with the U.S. Copyright Office.
 */

package com.ibm.openpages.ext.ui.service.impl;


import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;
import com.ibm.openpages.ext.tss.helpers.service.IHelperService;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectSearchUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.IUserAccessUtil;
import com.ibm.openpages.ext.tss.service.IUserUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.service.IAuditPlanBenchmarkHelperService;
import com.ibm.openpages.ext.ui.util.DSMTLinkHelperUtil;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.tss.service.util.NumericUtil.isNumeric;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.COLON;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.ui.constant.AuditPlanBenchmarkHelperConstants.*;

@Service("auditPlanBenchmarkServiceImpl")
public class AuditPlanBenchmarkServiceImpl implements IAuditPlanBenchmarkHelperService
{

    // Class Level Variables
    protected Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IGRCObjectUtil grcObjectUtil;

    @Autowired
    IHelperService helperService;

    @Autowired
    IApplicationUtil applicationUtil;

    @Autowired
    IGRCObjectSearchUtil grcObjectSearchUtil;

    @Autowired
    IFieldUtil fieldUtil;

    @Autowired
    IServiceFactoryProxy serviceFactoryProxy;

    @Autowired
    DSMTLinkHelperUtil dsmtLinkHelperUtil;

    @Autowired
    IUserUtil userUtil;

    @Autowired
    IUserAccessUtil userAccessUtil;

    @PostConstruct
    public void initServiceImpl()
    {

        logger = loggerUtil.getExtLogger(AUDIT_PLAN_BENCHMARK_SRVICE_LOG_FILE_NAME);
    }

    /**
     * <p>
     * This method retrieves the Helper Landing Page Content Section. The
     * Landing Page Title, Landing Page Content and the fields and the values of
     * the fields from the object from which the helper was launched are
     * obtained.
     * </P>
     *
     * @param AuditPlanBenchmarkHelperAppInfo
     *            - an instance of the {@link auditPlanBenchmarkHelperAppInfo} a
     *            master bean that has all the details for the current session
     * @return - the AuditPlanBenchmarkHelperAppInfo with the objects basic
     *         information and the association information set
     * @throws Exception
     *             - Generic Exception
     */
    @Override
    public AuditPlanBenchmarkHelperAppInfo getHeaderAndLandingPageInfo(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo,
            String objectId) throws Exception
    {

        logger.info("getLandingPageInfo() START");

        setupHelperObjectInfo(auditPlanBenchmarkHelperAppInfo, objectId);

        // Get the Basic Object information of the current object under
        // execution
        getHelperHeaderInfo(auditPlanBenchmarkHelperAppInfo);
        getBasicObjectInfoForDisplay(auditPlanBenchmarkHelperAppInfo);

        logger.info("auditPlanBenchmarkHelperAppInfo : " + auditPlanBenchmarkHelperAppInfo);
        logger.info("getLandingPageInfo() END");
        return auditPlanBenchmarkHelperAppInfo;
    }

    /**
     * <p>
     * This method retrieves the Helpers Header Information from the Registry
     * settings and sets the appropriate values in the {@link } object. This
     * will be used to display the content in the UI Header.
     * </P>
     *
     * @param auditPlanBenchmarkHelperAppInfo
     *            - an instance of the {@link auditPlanBenchmarkHelperAppInfo} a
     *            master bean that has all the details for the current session
     * @throws Exception
     *             - Generic Exception
     */
    private void getHelperHeaderInfo(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo) throws Exception
    {

        logger.info("getHelperHeaderInfo() START");

        // Method Level Variables.
        CarbonHeaderInfo headerInfo;

        /* Initialize Variables */
        // Get the helper header information by passing the Helper Base Setting
        headerInfo = helperService.getTFUIHelperAppHeaderInfo(ROOT_SETTING);

        // Set the bean to be returned
        auditPlanBenchmarkHelperAppInfo.setHeaderInfo(headerInfo);

        logger.info("getHelperHeaderInfo() END");

        // return auditPlanBenchmarkHelperAppInfo;
    }

    /**
     * <p>
     * This method retrieves the Helper Landing Page Content Section. The
     * Landing Page Title, Landing Page Content and the fields and the values of
     * the fields from the object from which the helper was launched are
     * obtained. The Landing page information is set in an instance of the
     * {@link DSMTObjectGenericDetails}.
     * </P>
     *
     * @param auditPlanBenchmarkHelperAppInfo
     *            - an instance of the {@link DSMTLinkHelperAppInfo} a master
     *            bean that has all the details for the current session
     * @throws Exception
     *             - Generic Exception
     */
    private void getBasicObjectInfoForDisplay(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo) throws Exception
    {

        logger.info("getBasicObjectInfoForDisplay() Start");

        // Method Level Variables.
        String contentBody;
        String contentHeader;

        DSMTObjectGenericDetails helperObjectContentInfo;

        /* Initialize Variables */
        helperObjectContentInfo = new DSMTObjectGenericDetails();

        /*
         * Obtain the object under execution, Get the fields and its values that
         * needs to be displayed in the UI
         */
        logger.info("objectId : " + auditPlanBenchmarkHelperAppInfo.getObjectID());

        /*
         * Get the Helper page content header and body to be displayed in the UI
         */
        contentHeader = applicationUtil.getRegistrySetting(ROOT_SETTING + APP_TITLE,
                EMPTY_STRING);
        contentBody = applicationUtil
                .getRegistrySetting(ROOT_SETTING + APP_DISPLAY_NAME, EMPTY_STRING);

        /* Set all the obtained values in the bean to be returned */
        helperObjectContentInfo.setContentBody(contentBody);
        helperObjectContentInfo.setContentHeader(contentHeader);

        /* Log information for debugging */
        logger.info("Helper Object Content Info: " + helperObjectContentInfo);

        auditPlanBenchmarkHelperAppInfo.setHelperObjectContentInfo(helperObjectContentInfo);
        logger.info("getBasicObjectInfoForDisplay() End");
    }

    @Override
    public DataGridInfo getBenchmarkPlans(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo) throws Exception
    {

        logger.info("getBenchmarkPlans() Start");
        List<DataGridHeaderColumnInfo> gridHeaderInfoList = null;
        List<String> fieldNamesList = null;
        DataGridInfo carbonDataGridInfo = null;
        IGRCObject grcObject = null;
        ITabularResultSet resultSet = null;
        AuditPlanBenchmarkHelperDisplayInfo helperDisplayInfo = null;

        List<AuditPlanBenchmarkHelperDisplayInfo> helperDisplayInfoList = new ArrayList<AuditPlanBenchmarkHelperDisplayInfo>();
        int rowCount = 1;

        String query = EMPTY_STRING;
        carbonDataGridInfo = new DataGridInfo();

        fieldNamesList = parseCommaDelimitedValues(AUDIT_PLAN_BENCHMARK_HELPER_DISPLAY_FIELDS);
        logger.info("fieldNamesList : " + fieldNamesList);

        gridHeaderInfoList = getHeaderForDataGrid(fieldNamesList);
        logger.info("getBenchmarkPlans gridHeaderInfoList : " + gridHeaderInfoList);

        grcObject = grcObjectUtil.getObjectFromId(auditPlanBenchmarkHelperAppInfo.getObjectID());
        logger.info("Helper Object name [" + grcObject.getName() + "], Id [" + grcObject.getId() + "], and Object type ["
                + grcObject.getType().getName() + "]");

        query = AUDIT_PLAN_BASE_QUERY.replaceAll(AUDIT_PLAN_PATH_STR, grcObject.getName());
        logger.info("get Benchmark Plans Query : " + query);
        if (isNotNullOrEmpty(query))
        {
            resultSet = grcObjectSearchUtil.executeQuery(query);

            for (IResultSetRow row : resultSet)
            {

                helperDisplayInfo = new AuditPlanBenchmarkHelperDisplayInfo();
                helperDisplayInfo.setId("row-" + rowCount);

                // Get the object from the query
                grcObject = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(1)));
                logger.info("Object name [" + grcObject.getName() + "], Id [" + grcObject.getId() + "], and Object type ["
                        + grcObject.getType().getName() + "]");

                helperDisplayInfo.setResourceid(grcObject.getId().toString());
                helperDisplayInfo.setObjectid(grcObject.getName());
                helperDisplayInfo.setTitle(grcObject.getDescription());
                helperDisplayInfo.setLocation(fieldUtil.getFieldValueAsString(row.getField(2)));
                helperDisplayInfo.setLeadproductteam(fieldUtil.getFieldValueAsString(row.getField(3)));
                helperDisplayInfo.setBenchmark(fieldUtil.getFieldValueAsString(row.getField(4)));
                helperDisplayInfo.setObjectType(grcObject.getType().getName());

                logger.info("helperDisplayInfo : " + helperDisplayInfo);
                helperDisplayInfoList.add(helperDisplayInfo);
                rowCount++;
            }
        }

        carbonDataGridInfo.setHeaders(gridHeaderInfoList);
        carbonDataGridInfo.setRows(helperDisplayInfoList);
        logger.info("carbonDataGridInfo : " + carbonDataGridInfo);
        logger.info("getBenchmarkPlans() END");

        carbonDataGridInfo.setHeaders(gridHeaderInfoList);
        return carbonDataGridInfo;
    }

    @Override
    public DataGridInfo auditPlanConfirm(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo) throws Exception
    {

        logger.info("auditPlanConfirm() Start");

        IGRCObject auditPlanObject = null;
        IGRCObject auditObject = null;
        IGRCObject entityObject = null;
        IGRCObject atrObject = null;
        List<String> auditIdList = null;
        ITabularResultSet resultSet = null;
        String query = EMPTY_STRING;
        String auditType = EMPTY_STRING;
        Map<String, String> fieldsToUpdateInfo = null;
        DataGridInfo carbonDataGridInfo = null;


        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();

        entityObject = grcObjectUtil.getObjectFromId(auditPlanBenchmarkHelperAppInfo.getObjectID());
        logger.info("Entity Object name [" + entityObject.getName() + "], Id [" + entityObject.getId() + "], and Object type ["
                + entityObject.getType().getName() + "]");

        List<String> benchMarkYearList = parseCommaDelimitedValues(auditPlanBenchmarkHelperAppInfo.getBenchmarkYear());
        logger.info("benchMarkYearList : " + benchMarkYearList);

        StringBuffer sb = new StringBuffer("(");
        for (String year : benchMarkYearList)
        {
            sb.append("'" + year + "',");
        }

        String benchMarkYear = sb.toString();
        benchMarkYear = benchMarkYear.substring(0, benchMarkYear.lastIndexOf(',')) + ")";
        logger.info("benchMarkYear : " + benchMarkYear);

        query = AUDIT_PLAN_CONFIRM_QUERY.replaceAll(AUDIT_PLAN_PATH_STR, entityObject.getName()).replaceAll(BENCHMARK_YEARS_STR,
                benchMarkYear);
        logger.info("Audit Plan Confirm Query : " + query);

        if (isNotNullOrEmpty(query))
        {
            resultSet = grcObjectSearchUtil.executeQuery(query);

            for (IResultSetRow row : resultSet)
            {

                auditPlanObject = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(2)));
                logger.info("Audit Plan Object name [" + auditPlanObject.getName() + "], Id [" + auditPlanObject.getId()
                        + "], and Object type [" + auditPlanObject.getType().getName() + "]");

                auditObject = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(5)));
                logger.info("Audit Object name [" + auditObject.getName() + "], Id [" + auditObject.getId() + "], and Object type ["
                        + auditObject.getType().getName() + "]");

                if (resultMap.containsKey(auditPlanObject.getId().toString()))
                {

                    auditIdList = resultMap.get(auditPlanObject.getId().toString());
                    auditIdList.add(auditObject.getId().toString());
                    resultMap.put(auditPlanObject.getId().toString(), auditIdList);
                } else {
                    auditIdList = new ArrayList<String>();
                    auditIdList.add(auditObject.getId().toString());
                    resultMap.put(auditPlanObject.getId().toString(), auditIdList);
                }

                auditType = fieldUtil.getFieldValueAsString(auditObject, AUDIT_TYPE_FIELD);
                logger.info("auditType : " + auditType);

                List<String> atrObjList = grcObjectSearchUtil.getAllChildObjectIdsOfGivenObject(auditObject, AUDIT_TEAM_RESOURCE, true);
                logger.info("atrObjList.size() : " + atrObjList.size());

                for (String auditTeamResNode : atrObjList)
                {

                    atrObject = grcObjectUtil.getObjectFromId(auditTeamResNode);
                    logger.info("ATR Object name [" + atrObject.getName() + "], Id [" + atrObject.getId() + "], and Object type ["
                            + atrObject.getType().getName() + "]");

                    fieldsToUpdateInfo = new HashMap<String, String>();
                    if (isEqualIgnoreCase(auditType, GRAM))
                    {

                        fieldsToUpdateInfo.put(ATR_PLANHRSQ1_FIELD,
                                fieldUtil.getFieldValueAsString(atrObject, ATR_ADJHRSQ1_FIELD));
                        fieldsToUpdateInfo.put(ATR_PLANHRSQ2_FIELD,
                                fieldUtil.getFieldValueAsString(atrObject, ATR_ADJHRSQ2_FIELD));
                        fieldsToUpdateInfo.put(ATR_PLANHRSQ3_FIELD,
                                fieldUtil.getFieldValueAsString(atrObject, ATR_ADJHRSQ3_FIELD));
                        fieldsToUpdateInfo.put(ATR_PLANHRSQ4_FIELD,
                                fieldUtil.getFieldValueAsString(atrObject, ATR_ADJHRSQ4_FIELD));
                    }
                    else
                    {
                        fieldsToUpdateInfo.put(ATR_PLANNED_HOURS_FIELD,
                                fieldUtil.getFieldValueAsString(atrObject, ATR_ADJUSTTED_HOURS_FIELD));
                    }

                    grcObjectUtil.updateFieldsInObjectAndSave(atrObject, fieldsToUpdateInfo);
                }

                fieldsToUpdateInfo = new HashMap<String, String>();
                fieldsToUpdateInfo.put(AUDIT_PLANNED_PLANNING_START_DATE_FIELD, fieldUtil.getFieldValueAsString(auditObject, AUDIT_ADJUSTED_PLANNING_START_DATE_FIELD));
                fieldsToUpdateInfo.put(AUDIT_PLANNED_FIELDWORK_START_DATE_FIELD, fieldUtil.getFieldValueAsString(auditObject, AUDIT_ADJUSTED_FIELDWORK_START_DATE_FIELD));
                fieldsToUpdateInfo.put(AUDIT_PLANNED_FIELDWORK_END_DATE_FIELD, fieldUtil.getFieldValueAsString(auditObject, AUDIT_ADJUSTED_FIELDWORK_END_DATE_FIELD));
                fieldsToUpdateInfo.put(AUDIT_PLANNED_REPORT_PUBLICATION_DATE_FIELD, fieldUtil.getFieldValueAsString(auditObject, AUDIT_ADJUSTED_REPORT_PUBLICATION_DATE_FIELD));
                
                //IDateField BenchmarkDate = (IDateField) auditObject.getField(BENCHMARK_DATE_FIELD);
                Date date = new Date();
                //BenchmarkDate.setValue(date);

                String loggedUserName = userUtil.getLoggedInUser().getName();
                logger.info("loggedUserName : " + loggedUserName);

                fieldsToUpdateInfo.put(AUDIT_USER_LAST_BENCHMARK_FIELD, loggedUserName);
                fieldsToUpdateInfo.put(AUDIT_BENCHMARK_DATE_FIELD, date.toString());

                if (isEqualIgnoreCase(auditType, GRAM))
                {
                    String auditStatus = fieldUtil.getFieldValueAsString(auditObject, AUDIT_STATUS_FIELD);
                    if (auditStatus.equalsIgnoreCase(AUDIT_DRAFT_STATUS))
                    {
                        logger.info("Updating audit status to : " + AUDIT_NOT_STARTED_STATUS);
                        fieldsToUpdateInfo.put(AUDIT_STATUS_FIELD, AUDIT_NOT_STARTED_STATUS);
                    }
                }
                logger.info("Updating " + AUDIT_IS_BENCHMARK_DONE_FIELD + " : " + YES_STATUS);
                fieldsToUpdateInfo.put(AUDIT_IS_BENCHMARK_DONE_FIELD, YES_STATUS);

                grcObjectUtil.updateFieldsInObjectAndSave(auditObject, fieldsToUpdateInfo);
                logger.info("Successfully saved : " + AUDIT_NOT_STARTED_STATUS);
            }
        }

        carbonDataGridInfo = processResultForDisplay(resultMap);
        logger.info("carbonDataGridInfo : " + carbonDataGridInfo);
        logger.info("auditPlanConfirm() End");
        return carbonDataGridInfo;
    }

    private DataGridInfo processResultForDisplay(Map<String, List<String>> resultMap) throws Exception {

        logger.info("processResultForDisplay() Start");

        int rowCount = 1;
        int childCount = 1;

        IGRCObject auditObject = null;
        IGRCObject auditPlanObject = null;
        DataGridInfo carbonDataGridInfo = null;
        GRCObjectInfo grcObjectInfo = null;
        GRCObjectInfo childObjectInfo = null;
        DataGridHeaderColumnInfo availableHeader = null;

        List<String> hierarchyList = null;
        List<String> childObjectIdList = null;
        List<DataGridHeaderColumnInfo> gridHeaderInfoList = null;
        List<GRCObjectInfo> grcObjectInfoList = null;


        carbonDataGridInfo = new DataGridInfo();
        grcObjectInfoList = new LinkedList<GRCObjectInfo>();

        gridHeaderInfoList = new ArrayList<DataGridHeaderColumnInfo>();
        availableHeader = new DataGridHeaderColumnInfo();

        availableHeader.setField("id");
        availableHeader.setHide(true);
        gridHeaderInfoList.add(availableHeader);

        availableHeader = new DataGridHeaderColumnInfo();
        availableHeader.setField("objectName");
        availableHeader.setHide(false);
        availableHeader.setHeaderName("Name");
        gridHeaderInfoList.add(availableHeader);

        logger.info("processResultForDisplay gridHeaderInfoList : " + gridHeaderInfoList);


        if (isMapNotNullOrEmpty(resultMap)) {

            for (String auditPlanId : resultMap.keySet()) {

                logger.info("Looping Audit Plan Id : " + auditPlanId);
                grcObjectInfo = new GRCObjectInfo();
                hierarchyList = new ArrayList<String>();


                if (!isNumeric(auditPlanId)) {
                    logger.info("Audit Plan Id is not numeric : " + auditPlanId);
                    continue;
                }

                try {
                    auditPlanObject = grcObjectUtil.getObjectFromId(auditPlanId);
                    logger.info("Object name [" + auditPlanObject.getName() + "], Id [" + auditPlanObject.getId() + "], and Object type ["
                            + auditPlanObject.getType().getName() + "]");
                }
                catch (Exception e) {
                    logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "processResultForDisplay() : " + getStackTrace(e));
                    continue;
                }

                childObjectIdList = resultMap.get(auditPlanId);
                hierarchyList.add(auditPlanObject.getName());

                grcObjectInfo.setId("row-" + rowCount);
                grcObjectInfo.setObjectId(auditPlanId);
                grcObjectInfo.setObjectName(auditPlanObject.getName());
                grcObjectInfo.setObjectType(auditPlanObject.getType().getName());
                grcObjectInfo.setHasChildren(isListNotNullOrEmpty(childObjectIdList));
                grcObjectInfo.setHierarchy(hierarchyList);
                grcObjectInfo.setObjectLink(dsmtLinkHelperUtil.getObjectDetailViewLink(auditPlanId, auditPlanObject.getName()));

                grcObjectInfoList.add(grcObjectInfo);

                if (isListNotNullOrEmpty(childObjectIdList)) {

                    childCount = 1;

                    for (String childObjectId : childObjectIdList) {

                        auditObject = grcObjectUtil.getObjectFromId(childObjectId);
                        logger.info("Object name [" + auditObject.getName() + "], Id [" + auditObject.getId() + "], and Object type ["
                                + auditObject.getType().getName() + "]");

                        childObjectInfo = new GRCObjectInfo();
                        hierarchyList = new ArrayList<String>();
                        hierarchyList.add(auditPlanObject.getName());
                        hierarchyList.add(auditObject.getName());

                        childObjectInfo.setId("row-" + rowCount + "-child-" + childCount);
                        childObjectInfo.setHierarchy(hierarchyList);

                        childObjectInfo.setObjectId(childObjectId);
                        childObjectInfo.setParentResourceId(auditPlanId);
                        childObjectInfo.setObjectName(auditObject.getName());
                        childObjectInfo.setObjectType(auditObject.getType().getName());
                        childObjectInfo.setHasChildren(isListNotNullOrEmpty(childObjectIdList));
                        childObjectInfo.setHierarchy(hierarchyList);

                        childObjectInfo.setObjectLink(dsmtLinkHelperUtil.getObjectDetailViewLink(childObjectId, auditObject.getName()));
                        grcObjectInfoList.add(childObjectInfo);
                        childCount++;
                    }
                }

                rowCount++;
            }
        }

        carbonDataGridInfo.setHeaders(gridHeaderInfoList);
        carbonDataGridInfo.setRows(grcObjectInfoList);
        //carbonDataGridInfo.setWarningMessage("Success");
        logger.info("processResultForDisplay() End");
        return carbonDataGridInfo;
    }

    private void setupHelperObjectInfo(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo, String objectId) throws Exception
    {

        logger.info("setupHelperObjectInfo() START");

        // Method Level Variables.
        IGRCObject grcObject;
        String grcObjectType;
        boolean isHelperDeskMember = false;
        boolean isMIMember = false;
        String benchmarkYear = EMPTY_STRING;

        /* Initialize Variables */
        grcObject = grcObjectUtil.getObjectFromId(objectId);
        grcObjectType = grcObject.getType().getName();
        logger.info("Object name [" + grcObject.getName() + "], Id [" + grcObject.getId() + "], and Object type [" + grcObjectType + "]");

        auditPlanBenchmarkHelperAppInfo.setObjectID(objectId);
        auditPlanBenchmarkHelperAppInfo.setObjectType(grcObjectType);
        auditPlanBenchmarkHelperAppInfo.setLocalizedLabel(grcObject.getType().getLocalizedLabel());

        benchmarkYear = fieldUtil.getFieldValueAsString(grcObject, ENTITY_BENCHMARK_YEAR_FIELD);
        logger.info("Benchmark Year : " + benchmarkYear);
        auditPlanBenchmarkHelperAppInfo.setBenchmarkYear(benchmarkYear);

        isHelperDeskMember = userUtil.isLoggedInUserMemberOfAGroup(HELP_DESK_GRP);
        isMIMember = userUtil.isLoggedInUserMemberOfAGroup(MI_GRP);
        logger.info("isHelperDeskMember [" + isHelperDeskMember + "], and isMIMember [" + isMIMember + "]");

        if(isNullOrEmpty(benchmarkYear)) {

            logger.info("Benchmark Year field is null or empty : [" + benchmarkYear + "]");
            auditPlanBenchmarkHelperAppInfo.setBenchmarkYearAvailable(false);
            auditPlanBenchmarkHelperAppInfo.setMessage(BENCHMARK_YEAR_FIELD_EMPTY_MESSAGE);
        } else {
            logger.info("Benchmark Year field is valid : [" + benchmarkYear + "]");
            auditPlanBenchmarkHelperAppInfo.setBenchmarkYearAvailable(true);
        }

        if(isHelperDeskMember || isMIMember) {

            logger.info("User have access to run this helper.");
            auditPlanBenchmarkHelperAppInfo.setHasAccess(true);
        } else {
            logger.info("User is not a member of [" + HELP_DESK_GRP + "] or [" + MI_GRP + "]");
            auditPlanBenchmarkHelperAppInfo.setHasAccess(false);
            auditPlanBenchmarkHelperAppInfo.setMessage(HELPER_ACCESS_MESSAGE);
        }

        logger.info("setupHelperObjectInfo() END");
    }

    private List<DataGridHeaderColumnInfo> getHeaderForDataGrid(List<String> fieldNamesList) throws Exception {

        logger.info("getHeaderForDataGrid() End");
        logger.info("Field Names List: " + fieldNamesList);

        // Method Level Variables.
        String key = EMPTY_STRING;
        String fieldLabel = EMPTY_STRING;
        DataGridHeaderColumnInfo availableHeader = null;
        List<DataGridHeaderColumnInfo> availableHeaderList = null;

        // Method Implementation
        /* Initialize */
        availableHeaderList = new ArrayList<DataGridHeaderColumnInfo>();
        availableHeader = new DataGridHeaderColumnInfo();

        /* Set the id field, this will not be part of the list */
        availableHeader.setField("id");
        availableHeader.setHide(true);
        availableHeaderList.add(availableHeader);

        /* Iterate through each item in the header column names */
        for (String fieldName : fieldNamesList) {


            /* Get the Localized label for the field in the OpenPages system */
            if (fieldName.contains(COLON)) {
                fieldLabel = getFieldLableForDisplay(fieldName);
            }
            else {
                fieldLabel = fieldName;
            }

            /* Prepare a new column header info and set the name and field */
            availableHeader = new DataGridHeaderColumnInfo();
            availableHeader.setHeaderName(fieldLabel);

            if(isEqual(fieldLabel, RESOURCE_ID)) {
                availableHeader.setHide(true);
            }
            key = fieldLabel.toLowerCase().replaceAll(" ", "").replaceAll("-", "");

            availableHeader.setField(key);

            /* Add it to the list */
            availableHeaderList.add(availableHeader);
        }

        logger.info("getHeaderForDataGrid() End");
        return availableHeaderList;
    }

    private String getFieldLableForDisplay(String fieldName) throws Exception {

        //logger.info("getFieldValuesOfObjectAsMap() Start");

        // Method Level Variables.
        String fieldLabel = EMPTY_STRING;
        IFieldDefinition fieldDefinition = null;
        IMetaDataService metadataService = null;

        // Method Implementation
        /* Initialize Variables */
        metadataService = serviceFactoryProxy.getServiceFactory().createMetaDataService();

        // From the metadata service get the field definition and then get the localized label
        fieldDefinition = metadataService.getField(fieldName);
        fieldLabel = fieldDefinition.getLocalizedLabel();

        return fieldLabel;
    }

}