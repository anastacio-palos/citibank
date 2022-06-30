/*
 IBM Confidential OCO Source Materials
 5725-D51, 5725-D52, 5725-D53, 5725-D54
 © Copyright IBM Corporation 2021
 The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 has been deposited with the U.S. Copyright Office.
 */

package com.ibm.openpages.ext.ui.service.impl;

import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;
import com.ibm.openpages.ext.tss.helpers.service.IHelperService;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectSearchUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.service.IContextHelperService;
import com.ibm.openpages.ext.ui.util.DSMTLinkHelperUtil;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.COMMA_SEPERATED_DELIMITER;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.COLON;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.COLON_SEPERATED_DELIMITER;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.PIPE_SEPERATED_DELIMITER;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EQUAL_TO;
import static com.ibm.openpages.ext.ui.constant.ContextHelperConstants.*;

@Service("contextServiceImpl")
public class ContextServiceImpl implements IContextHelperService {

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

    @PostConstruct
    public void initController() {

        logger = loggerUtil.getExtLogger(CONTEXT_SERVICE_IMPLEMENTATION_LOG_FILE_NAME);
    }

    /**
     * <p>
     * This method retrieves the Helper Landing Page Content Section. The Landing Page Title, Landing Page Content and
     * the fields and the values of the fields from the object from which the helper was launched are obtained. On
     * conditional basis pre existing associated object information is also retrieved. The Landing page information is
     * set in an instance of the {@link DSMTObjectGenericDetails}. The link object association information is set in an
     * instance of the {@link DSMTLinkObjectAssociationInfo}
     * </P>
     *
     * @param contextHelperAppInfo - an instance of the {@link ContextHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception - Generic Exception
     */
    @Override
    public ContextHelperAppInfo getHeaderAndLandingPageInfo(ContextHelperAppInfo contextHelperAppInfo, String objectId) throws Exception {

        logger.info("getLandingPageInfo() START");

        setupHelperObjectInfo(contextHelperAppInfo, objectId);

        // Get the Basic Object information of the current object under execution
        getHelperHeaderInfo(contextHelperAppInfo);
        getBasicObjectInfoForDisplay(contextHelperAppInfo);

        logger.info("contextHelperAppInfo : " + contextHelperAppInfo);
        logger.info("getLandingPageInfo() END");
        return contextHelperAppInfo;
    }

    @Override
    public DataGridInfo getAvailableObjects(ContextHelperAppInfo contextHelperAppInfo) throws Exception {

        logger.info("getAvailableObjects() START");
        logger.info("contextHelperAppInfo : " + contextHelperAppInfo);

        // Local Variables
        IGRCObject grcObject;
        IGRCObject helperObject;
        String query = EMPTY_STRING;
        String helperObjPrimaryId = EMPTY_STRING;
        ITabularResultSet resultSet = null;
        ContextHelperDisplayInfo contextHelperDisplayInfo = null;
        List<DataGridHeaderColumnInfo> gridHeaderInfoList = null;
        List<String> existingObjectIdList = null;
        List<IAssociationNode> parentAssociationNodes = null;
        List<String> fieldNamesList = null;

        DataGridInfo carbonDataGridInfo = new DataGridInfo();
        List<ContextHelperDisplayInfo> contextHelperDisplayInfoList = new ArrayList<ContextHelperDisplayInfo>();
        int rowCount = 1;
        existingObjectIdList = new ArrayList<String>();


        helperObject = grcObjectUtil.getObjectFromId(contextHelperAppInfo.getObjectID());
        logger.info("Helper Object name [" + helperObject.getName() + "], Id [" + helperObject.getId() + "], and Object type [" + helperObject.getType().getName() + "]");
        helperObjPrimaryId = helperObject.getPrimaryParent().toString();

        // get the query
        if(isEqual(helperObject.getType().getName(), APPLICATION)) {
            query = processObjectPathFromRootQuery(contextHelperAppInfo);
            logger.info("Application Query : " + query);
        } else {
            query = processObjectPathFromRootQuery(contextHelperAppInfo);
            logger.info("Query : " + query);
        }

        fieldNamesList = parseCommaDelimitedValues(contextHelperAppInfo.getColumnFieldName());
        logger.info("fieldNamesList : " + fieldNamesList);
        gridHeaderInfoList = getHeaderForDataGrid(fieldNamesList);
        logger.info("gridHeaderInfoList : " + gridHeaderInfoList);

        if(isEqual(helperObject.getType().getName(), ISSUE)) {

            logger.info("Getting all parent Exception objects list of " + helperObject.getType().getName());
            parentAssociationNodes = grcObjectSearchUtil.getAllParentsOfGivenType(helperObject.getId(), EXCEPTION, true);

            for (IAssociationNode parentNode : parentAssociationNodes) {

                existingObjectIdList.add(parentNode.getId().toString());
            }
            logger.info("Exception objects list : " + existingObjectIdList);
        }
        else if(isEqual(helperObject.getType().getName(), EXCEPTION)) {

            logger.info("Getting all child Issue objects list of " + helperObject.getType().getName());
            existingObjectIdList = grcObjectSearchUtil.getAllChildObjectIdsOfGivenObject(helperObject, ISSUE, true);
            logger.info("Issue objects list : " + existingObjectIdList);
        }

        if(isNotNullOrEmpty(query)) {
            resultSet = grcObjectSearchUtil.executeQuery(query, false);

            for (IResultSetRow row : resultSet) {

                contextHelperDisplayInfo = new ContextHelperDisplayInfo();
                contextHelperDisplayInfo.setId("row-" + rowCount);

                // Get the object from the query
                grcObject = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(0)));
                logger.info("Object name [" + grcObject.getName() + "], Id [" + grcObject.getId() + "], and Object type [" + grcObject.getType().getName() + "]");

                contextHelperDisplayInfo.setId("row-" + rowCount);
                contextHelperDisplayInfo.setResourceid(fieldUtil.getFieldValueAsString(row.getField(0)));
                contextHelperDisplayInfo.setObjectid(fieldUtil.getFieldValueAsString(row.getField(1)));
                contextHelperDisplayInfo.setTitle(fieldUtil.getFieldValueAsString(row.getField(2)));
                contextHelperDisplayInfo.setObjectType(grcObject.getType().getName());

                if(isListNotNullOrEmpty(existingObjectIdList) && existingObjectIdList.contains(grcObject.getId().toString())) {
                    logger.info("Association already exist : " + grcObject.getName());
                    contextHelperDisplayInfo.setSelected(true);
                } else {
                    contextHelperDisplayInfo.setSelected(false);
                }

                if(isEqual(grcObject.getType().getName(), AUDITABLE_ENTITY)) {

                    logger.info("Object Type is : " + grcObject.getType().getName());
                    contextHelperDisplayInfo.setAuditableentitystatus(fieldUtil.getFieldValueAsString(row.getField(3)));
                    contextHelperDisplayInfo.setAuditableentitytype(fieldUtil.getFieldValueAsString(row.getField(4)));
                    contextHelperDisplayInfo.setAuditableentitysubtype(fieldUtil.getFieldValueAsString(row.getField(5)));

                    logger.info("Helper Object Primary Id : " + helperObjPrimaryId);
                    if(isEqual(helperObjPrimaryId, grcObject.getId().toString())) {
                        contextHelperDisplayInfo.setSelected(true);
                    }
                    contextHelperDisplayInfo.setDisposition(null);
                    contextHelperDisplayInfo.setIssuestatus(null);
                    contextHelperDisplayInfo.setExceptionsource(null);

                } else if(isEqual(grcObject.getType().getName(), EXCEPTION)) {

                    logger.info("Object Type is : " + grcObject.getType().getName());
                    contextHelperDisplayInfo.setDisposition(fieldUtil.getFieldValueAsString(row.getField(3)));
                    contextHelperDisplayInfo.setExceptionsource(fieldUtil.getFieldValueAsString(row.getField(4)));

                    contextHelperDisplayInfo.setIssuestatus(null);
                    contextHelperDisplayInfo.setAuditableentitystatus(null);
                    contextHelperDisplayInfo.setAuditableentitytype(null);
                    contextHelperDisplayInfo.setAuditableentitysubtype(null);

                }else if(isEqual(grcObject.getType().getName(), ISSUE)) {

                    logger.info("Object Type is : " + grcObject.getType().getName());
                    contextHelperDisplayInfo.setIssuestatus(fieldUtil.getFieldValueAsString(row.getField(3)));

                    contextHelperDisplayInfo.setDisposition(null);
                    contextHelperDisplayInfo.setExceptionsource(null);
                    contextHelperDisplayInfo.setAuditableentitystatus(null);
                    contextHelperDisplayInfo.setAuditableentitytype(null);
                    contextHelperDisplayInfo.setAuditableentitysubtype(null);
                }

                logger.info("contextHelperDisplayInfo : " + contextHelperDisplayInfo);
                contextHelperDisplayInfoList.add(contextHelperDisplayInfo);
                rowCount++;
            }
        }

        carbonDataGridInfo.setHeaders(gridHeaderInfoList);
        carbonDataGridInfo.setRows(contextHelperDisplayInfoList);
        logger.info("carbonDataGridInfo : " + carbonDataGridInfo);
        logger.info("getAvailableObjects() END");
        return carbonDataGridInfo;

    }

    @Override
    public String associateObjects(ContextHelperAppInfo contextHelperAppInfo, List<String> resourceIdlist) throws Exception {

        logger.info("associateObjects() START");
        logger.info("contextHelperAppInfo : " + contextHelperAppInfo);
        logger.info("Resource Id list : " + resourceIdlist);

        // Method Level Variables.
        IGRCObject grcObject = null;
        IServiceFactory serviceFactory = null;
        IResourceService resourceService = null;

        /* Initialize Variables */
        serviceFactory = serviceFactoryProxy.getServiceFactory();
        resourceService = serviceFactory.createResourceService();
        List<Id> associationsIdList = new ArrayList<Id>();

        for(String id : resourceIdlist) {

            associationsIdList.add(new Id(id));
        }
        grcObject = grcObjectUtil.getObjectFromId(contextHelperAppInfo.getObjectID());
        logger.info("Object name [" + grcObject.getName() + "], Id [" + grcObject.getId() + "], and Object type [" + grcObject.getType().getName() + "]");

        if(isEqual(grcObject.getType().getName(), ISSUE)) {

            logger.info("Associating parent Exception objects to : " + grcObject.getName());
            resourceService.associate(grcObject.getId(), associationsIdList, new ArrayList<Id>());
        }
        else if(isEqual(grcObject.getType().getName(), EXCEPTION)) {

            logger.info("Associating child Issue objects to : " + grcObject.getName());
            resourceService.associate(grcObject.getId(), new ArrayList<Id>(), associationsIdList);
        }
        else if(isEqual(grcObject.getType().getName(), APPLICATION)) {

            logger.info("Associating primary Auditable Entity to : " + grcObject.getName());
            grcObject.setPrimaryParent(associationsIdList.get(0));
        }

        logger.info("associateObjects() END");
        return "Success";
    }

    /**
     * @param objectId :
     * @throws Exception :
     */
    private void setupHelperObjectInfo(ContextHelperAppInfo contextHelperAppInfo, String objectId) throws Exception {

        logger.info("setupHelperObjectInfo() START");

        // Method Level Variables.
        IGRCObject grcObject;
        IGRCObject rootObj;
        String grcObjectType;


        /* Initialize Variables */
        grcObject = grcObjectUtil.getObjectFromId(objectId);
        grcObjectType = grcObject.getType().getName();
        logger.info("Object name [" + grcObject.getName() + "], Id [" + grcObject.getId() + "], and Object type [" + grcObjectType + "]");

        contextHelperAppInfo.setObjectID(objectId);
        contextHelperAppInfo.setObjectType(grcObjectType);
        contextHelperAppInfo.setLocalizedLabel(grcObject.getType().getLocalizedLabel());

        // Get all registry settings
//        logger.info(ROOT_OBJECT  + " : " + applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + ROOT_OBJECT));
//        logger.info(ROOT_OBJECT_PATH  + " : " + applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + ROOT_OBJECT_PATH));
//        logger.info(OBJECT_TO_DISPLAY + " : " + applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + OBJECT_TO_DISPLAY));
//        logger.info(OBJECT_PATH_FROM_ROOT + " : " + applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + OBJECT_PATH_FROM_ROOT));
//        logger.info(COLUMN_HEADERS + " : " + applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + COLUMN_HEADERS));
//        logger.info(COLUMN_FIELD_NAME + " : " + applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + COLUMN_FIELD_NAME));
//        logger.info(FILTER_FIELDS + " : " + applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + FILTER_FIELDS));

        // Update the Object information

        contextHelperAppInfo.setRootObject(applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + ROOT_OBJECT));
        contextHelperAppInfo.setRootObjectPath(applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + ROOT_OBJECT_PATH));
        contextHelperAppInfo.setObjectToDisplay(applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + OBJECT_TO_DISPLAY));
        contextHelperAppInfo.setObjectPathFromRoot(applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + OBJECT_PATH_FROM_ROOT));
        contextHelperAppInfo.setColumnFieldName(applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + COLUMN_FIELD_NAME));
        contextHelperAppInfo.setFilterFields(applicationUtil.getRegistrySetting(contextHelperAppInfo.getObjectRegistrySetting() + FILTER_FIELDS));

        String rootObjResourceId = getRootObjResourceId(contextHelperAppInfo);
        rootObj = grcObjectUtil.getObjectFromId(rootObjResourceId);
        logger.info("Root Object name [" + rootObj.getName() + "], Id [" + rootObj.getId() + "], and Object type [" + rootObj.getType().getName() + "]");

        contextHelperAppInfo.setRootObjName(rootObj.getName());
        contextHelperAppInfo.setRootObjContextPath(rootObj.getParentFolder().getName());
        contextHelperAppInfo.setRootObjResourceId(rootObjResourceId);

        //logger.info("contextHelperAppInfo : " + contextHelperAppInfo);
        logger.info("setupHelperObjectInfo() END");
    }

    /**
     * <p>
     * This method retrieves the Helpers Header Information from the Registry settings and sets the appropriate values
     * in the {@link } object. This will be used to display the content in the UI Header.
     * </P>
     *
     * @param contextHelperAppInfo - an instance of the {@link ContextHelperAppInfo} a master bean that has all the details for the current session
     * @throws Exception - Generic Exception
     */
    private void getHelperHeaderInfo(ContextHelperAppInfo contextHelperAppInfo) throws Exception {

        logger.info("getHelperHeaderInfo() START");

        // Method Level Variables.
        CarbonHeaderInfo headerInfo;

        /* Initialize Variables */
        // Get the helper header information by passing the Helper Base Setting
        headerInfo = helperService.getTFUIHelperAppHeaderInfo(ROOT_SETTING);

        // Set the bean to be returned
        contextHelperAppInfo.setHeaderInfo(headerInfo);

        logger.info("getHelperHeaderInfo() END");

        // return contextHelperAppInfo;
    }

    /**
     * <p>
     * This method retrieves the Helper Landing Page Content Section. The Landing Page Title, Landing Page Content and
     * the fields and the values of the fields from the object from which the helper was launched are obtained. The
     * Landing page information is set in an instance of the {@link DSMTObjectGenericDetails}.
     * </P>
     *
     * @param contextHelperAppInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @throws Exception - Generic Exception
     */
    private void getBasicObjectInfoForDisplay(ContextHelperAppInfo contextHelperAppInfo) throws Exception {

        logger.info("getBasicObjectInfoForDisplay() Start");

        // Method Level Variables.
        String contentBody;
        String contentHeader;
        String registrySetting;
        String grcObjFieldRegValue;

        IGRCObject grcObject;
        List<GRCObjectDetailsInfo> objFieldsInfo;
        DSMTObjectGenericDetails helperObjectContentInfo;

        /* Initialize Variables */
        helperObjectContentInfo = new DSMTObjectGenericDetails();

        /*
         * Prepare the registry setting path based on the base registry setting and the object type and obtain the list
         * of fields that needs to be displayed in the UI from the registry setting.
         */
        registrySetting = contextHelperAppInfo.getObjectRegistrySetting() + DSMT_LINK_APP_OBJECT_DISPLAY_INFO;
        grcObjFieldRegValue = applicationUtil.getRegistrySetting(registrySetting);

        /* Obtain the object under execution, Get the fields and its values that needs to be displayed in the UI */
        logger.info("objectId : " + contextHelperAppInfo.getObjectID());
        grcObject = grcObjectUtil.getObjectFromId(contextHelperAppInfo.getObjectID());
        objFieldsInfo = dsmtLinkHelperUtil.getBasicObjectInformationForDisplay(grcObject, grcObjFieldRegValue);

        /* Get the Helper page content header and body to be displayed in the UI */
        contentHeader = applicationUtil.getRegistrySetting(
                contextHelperAppInfo.getObjectRegistrySetting() + DSMT_LINK_APP_TITLE, EMPTY_STRING);
        contentBody = applicationUtil.getRegistrySetting(
                contextHelperAppInfo.getObjectRegistrySetting() + DSMT_LINK_APP_DISPLAY_NAME, EMPTY_STRING);

        /* Set all the obtained values in the bean to be returned */
        helperObjectContentInfo.setContentBody(contentBody);
        helperObjectContentInfo.setContentHeader(contentHeader);
        helperObjectContentInfo.setGeneralDetails(objFieldsInfo);

        /* Log information for debugging */
        logger.info("Helper Object Content Info: " + helperObjectContentInfo);

        contextHelperAppInfo.setHelperObjectContentInfo(helperObjectContentInfo);
        logger.info("getBasicObjectInfoForDisplay() End");
    }

    /**
     * <p>
     * This method constructs the base query to fetch values from a parent association. The query construction is based
     * on the query API format.
     * </p>
     *
     * @param objectHierarchyList the objects hierarchy list containing the hierarchy path pointing to the parent association
     * @return a string builder containing the base query
     * @throws Exception any runtime exception
     */
    private static StringBuilder getBaseQueryForMultipleParentAssociation(List<String> objectHierarchyList) throws Exception {

        // Method Level Variables.
        int count = INT_ZERO;
        StringBuilder queryString = null;
        String parentObjectType = EMPTY_STRING;

        // Method Implementation.
        if (isListNotNullOrEmpty(objectHierarchyList)) {

            parentObjectType = objectHierarchyList.get(INT_ZERO);
            queryString = new StringBuilder();
            queryString.append("SELECT [");
            queryString.append(parentObjectType);
            queryString.append("].[Resource ID] FROM [");
            queryString.append(parentObjectType);
            queryString.append("] ");

            for (String childObjectTypeName : objectHierarchyList) {

                if (count != INT_ZERO) {

                    queryString.append("JOIN [");
                    queryString.append(childObjectTypeName);
                    queryString.append("] ON PARENT([");
                    queryString.append(parentObjectType);
                    queryString.append("]) ");

                    parentObjectType = childObjectTypeName;
                }
                count++;
            }
        }

        queryString.append("WHERE [");
        queryString.append(parentObjectType);
        queryString.append("].");

        return queryString;
    }

    /**
     * <p>
     * This method gets the value of a field in a {@link IGRCObject} represented by the objects Id.The object type and
     * the field information determines the field value that needs to be fetched, along with the child association in
     * object hierarchy list.
     * </p>
     *
     * @param objectToDisplay    the object type of the object id from which the field value needs to be fetched
     * @param fieldInfoList the field name from which the value needs to be fetched
     * @param objectHierarchyList the objects hierarchy list containing the hierarchy path pointing to the child association
     * @return a String representation of the value of the field
     * @throws Exception any runtime exception
     */
    private StringBuilder getBaseQueryToGetFieldValuesOfMultipleChildAssociation(String objectToDisplay, List<String> fieldInfoList, List<String> objectHierarchyList, String filterFields, boolean isApplication) throws Exception {

        // Method Level Variables.
        int fieldInfoCount = INIT_ONE;
        int count = INT_ZERO;
        boolean firstConstraint = true;
        StringBuilder queryString = null;
        String parentObjectType = EMPTY_STRING;
        Map<String, String> constraintMap = parseDelimitedValuesAsMap(filterFields, PIPE_SEPERATED_DELIMITER, EQUAL_TO);
        //List<String> objHeirarchyAndConstraint = null;

        // Method Implementation.
        queryString = new StringBuilder();

        if (isListNotNullOrEmpty(fieldInfoList)) {

            queryString.append("SELECT [");

            for (String fieldInfo : fieldInfoList) {

                queryString.append(objectToDisplay);
                queryString.append("].[");

                fieldInfo = (fieldInfo.indexOf(SYSTEM_FIELD_NAME) != -1) ?
                        fieldInfo.replace(SYSTEM_FIELD_NAME, EMPTY_STRING) :
                        fieldInfo;

                queryString.append(fieldInfo);

                if (fieldInfoCount == fieldInfoList.size()) {
                    queryString.append("] ");
                }
                else {
                    queryString.append("], [");
                }

                fieldInfoCount++;
            }

            queryString.append(" FROM [");
            parentObjectType = objectHierarchyList.get(INT_ZERO);
            queryString.append(parentObjectType);
            queryString.append("]");

            for (String childObjectTypeName : objectHierarchyList) {

                if (count != INT_ZERO) {

                    queryString.append(" JOIN [");
                    queryString.append(childObjectTypeName);
                    queryString.append("] ON PARENT([");
                    queryString.append(parentObjectType);
                    queryString.append("]) ");
                }
                parentObjectType = childObjectTypeName;
                count++;
            }
        }

        if(isMapNotNullOrEmpty(constraintMap)) {

            for (Map.Entry<String,String> entry : constraintMap.entrySet()) {
                if(firstConstraint) {
                    queryString.append(" WHERE [");
                    firstConstraint = false;
                } else {
                    queryString.append(" AND [");
                }
                queryString.append(objectToDisplay);
                queryString.append("].[");
                queryString.append(entry.getKey());
                queryString.append("] IN ");
                queryString.append(getQueryValue(entry.getValue() + " "));
            }
            queryString.append(" AND [");
            if(isApplication && objectHierarchyList.size() > 1) {
                queryString.append(objectHierarchyList.get(1));
            } else {
                queryString.append(objectHierarchyList.get(0));
            }
            queryString.append("].[Resource ID] = ");
        } else {
            queryString.append(" WHERE [");
            if(isApplication && objectHierarchyList.size() > 1) {
                queryString.append(objectHierarchyList.get(1));
            } else {
                queryString.append(objectHierarchyList.get(0));
            }
            queryString.append("].[Resource ID] = ");
        }

        return queryString;
    }

    private static String getQueryValue(String value) {

        String[] valueArray = null;
        StringBuilder queryValue = null;
        int length = 1;

        queryValue = new StringBuilder("(");

        if (isNotNullOrEmpty(value)) {

            valueArray = value.split(COMMA_SEPERATED_DELIMITER);
            for (String val : valueArray) {

                if(isNotNullOrEmpty(val)) {

                    queryValue.append("'").append(val.trim()).append("'");

                    if(length < valueArray.length) {
                        queryValue.append(COMMA_SEPERATED_DELIMITER);
                    }
                }
                length ++;
            }
        }
        queryValue.append(")");
        return queryValue.toString();
    }
    /**
     * <p>
     *     Dynamically construct the query to identify if there are any AuditPrograms.
     *     The Finding object resourceID is appended to the query as well.
     * </p>
     * @param contextHelperAppInfo
     * @return: This method will return a List with dynamically constructed queries.
     */
    private List<String> processRootObjectPathQuery(ContextHelperAppInfo contextHelperAppInfo) throws Exception {

        logger.info("processRootObjectPathQuery() START");

        // Local Variables
        List<String> pipeDelimitedValues;
        List<String> objectHierarchyList;
        StringBuilder partiallyConstructedQuery;
        List<String> finalQuery;
        String rootObjectPath;
        String resourceID;

        // Initialize Variables
        rootObjectPath = contextHelperAppInfo.getRootObjectPath();
        logger.info("rootObjectPath : " + rootObjectPath);
        pipeDelimitedValues = parsePipeDelimitedValues(rootObjectPath);
        logger.info("pipeDelimitedValues : " + pipeDelimitedValues);
        partiallyConstructedQuery = null;
        finalQuery = new ArrayList<String>();
        resourceID = contextHelperAppInfo.getObjectID();
        logger.info("resourceID : " + resourceID);

        // Log the initialized variables
        logger.info("Root object path: " + rootObjectPath);
        logger.info("Pipe delimited values: " + pipeDelimitedValues);

        // Check if the list is not null or empty
        if(isListNotNullOrEmpty(pipeDelimitedValues)) {
            for(String eachItem : pipeDelimitedValues) {

                logger.info("Each delimited value: " + eachItem);
                objectHierarchyList = parseDelimitedValues(eachItem, COLON_SEPERATED_DELIMITER);
                logger.info("Object Hierarchy List: " + objectHierarchyList);

                // Check if the delimited list is not null or empty? ConstructDynamicQuery : Do Nothing
                if(isListNotNullOrEmpty(objectHierarchyList)) {
                    partiallyConstructedQuery = getBaseQueryForMultipleParentAssociation(objectHierarchyList);
                }

                logger.info("Partially constructed query( to get auditPrograms ): " + partiallyConstructedQuery);

                // Construct the full query : append ([Resource ID] = 16101) to partiallyConstructedQuery
                finalQuery.add((partiallyConstructedQuery.append("[Resource ID] = " + resourceID)).toString());
            }
        }
        logger.info("The final root object path query list: " + finalQuery);

        logger.info("processRootObjectPathQuery() END");

        return finalQuery;
    }

    /**
     * <p>
     *     Dynamically construct query to fetch all issues under a given parent object.
     *     The filter field values used to construct the query comes from registry settings.
     * </p>
     * @param auditProgramResourceID
     * @param contextHelperAppInfo
     * @return : A string ( final query string)
     * @throws Exception
     */
    private String processObjectPathFromRootQuery(ContextHelperAppInfo contextHelperAppInfo) throws Exception {

        logger.info("processObjectPathFromRoot() START");

        // Local Variables
        String objectToDisplay;
        String columnFieldName;
        String objectPathFromRoot;
        List<String> fieldInfoList;
        List<String> objectHierarchyList;
        List<String> pipeDelimitedValues;
        StringBuilder partialQuery;
        String finalQuery = "";
        String filterFields;
        ITabularResultSet resultSet;


        // Initialize Variables
        objectToDisplay = contextHelperAppInfo.getObjectToDisplay();
        columnFieldName = contextHelperAppInfo.getColumnFieldName();
        objectPathFromRoot = contextHelperAppInfo.getObjectPathFromRoot();
        pipeDelimitedValues = parsePipeDelimitedValues(objectPathFromRoot);
        fieldInfoList = parseCommaDelimitedValues(columnFieldName);
        filterFields = contextHelperAppInfo.getFilterFields();

        // Log Initialized Variables
        logger.info("Object to display : " + objectToDisplay);
        logger.info("Column field name : " + columnFieldName);
        logger.info("Object path from root : " + objectPathFromRoot);
        logger.info("Object Path list : " + pipeDelimitedValues);
        logger.info("Field information list : " + fieldInfoList);
        logger.info("Filter Fields : " + filterFields);
        logger.info("Root Object Resource Id : " + contextHelperAppInfo.getRootObjResourceId());

        // Check if the list is not null or empty
        if(isListNotNullOrEmpty(pipeDelimitedValues)) {
            for(String eachObjectPath : pipeDelimitedValues) {

                logger.info("Delimited Object Path: " + eachObjectPath);
                objectHierarchyList = parseDelimitedValues(eachObjectPath, COLON_SEPERATED_DELIMITER);
                logger.info("Object Hierarchy List: " + objectHierarchyList);

                // Check if the delimited list is not null or empty? ConstructDynamicQuery : Do Nothing
                if(isListNotNullOrEmpty(objectHierarchyList)) {

                    if(isEqual(contextHelperAppInfo.getObjectType(), APPLICATION)) {
                        partialQuery = getBaseQueryToGetFieldValuesOfMultipleChildAssociation(objectToDisplay,fieldInfoList, objectHierarchyList,filterFields, true);
                        logger.info("Application Partial Query: " + partialQuery);
                        finalQuery = partialQuery.append(contextHelperAppInfo.getObjectID()).toString();
                        logger.info("Application Final Query: " + finalQuery);
                	} else {
                        partialQuery = getBaseQueryToGetFieldValuesOfMultipleChildAssociation(objectToDisplay,fieldInfoList, objectHierarchyList,filterFields, false);
                        logger.info("Partial Query: " + partialQuery);
                        finalQuery = partialQuery.append(contextHelperAppInfo.getRootObjResourceId()).toString();
                        logger.info("Final Query: " + finalQuery);
                    }

                    if(isNotNullOrEmpty(finalQuery)) {
                        resultSet = grcObjectSearchUtil.executeQuery(finalQuery, false);
                        logger.info("resultSet : " + resultSet);
                        if (isObjectNotNull(resultSet) && resultSet.iterator().hasNext()) {
                            logger.info(" return resultSet : " + resultSet.iterator().hasNext());
                            return finalQuery;
                        } else {
                            logger.info("else resultSet : " + resultSet.iterator().hasNext());
                        }
                    }
                }
            }
        }

        logger.info("Return Query: " + finalQuery);

        logger.info("processObjectPathFromRoot() END");
        return finalQuery;
    }

    private String getRootObjResourceId(ContextHelperAppInfo contextHelperAppInfo) throws Exception {

        logger.info("getRootObjResourceId() START");

        // Local Variables
        List<String> rootObjectpathQueryList;
        String singleValueFromQuery = EMPTY_STRING;

        // Initialize Variables
        rootObjectpathQueryList = processRootObjectPathQuery(contextHelperAppInfo);
        logger.info("rootObjectpathQueryList : " + rootObjectpathQueryList);

        if(isListNotNullOrEmpty(rootObjectpathQueryList)) {
            for(String eachItem : rootObjectpathQueryList) {

                logger.info("Query : " + eachItem);
                singleValueFromQuery = grcObjectSearchUtil.getSingleValueFromQuery(eachItem);
                logger.info("singleValueFromQuery : " + singleValueFromQuery);
                if(isNotNullOrEmpty(singleValueFromQuery)) {
                    logger.info("getRootObjResourceId() END!");
                    return singleValueFromQuery;
                }
            }
        }

        logger.info("getRootObjResourceId() END");
        return singleValueFromQuery;
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