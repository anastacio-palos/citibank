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
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.service.IApplicationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.beans.SuperUserInformation;
import com.ibm.openpages.ext.tss.service.impl.GRCObjectSearchUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.util.DSMTLinkHelperUtil;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.*;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.ui.constant.AuditableEntityDSMTLinkConstants.*;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.*;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.BASE_ID_STR;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.DSMT_LINK_NOT_ACTIVE_EXCEPTION;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.RESOURCE_ID_STR;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.SCOPE_OUT;

/**
 * <P>
 * This utility class provides utility methods for the DSMT Link helper.
 * </P>
 * 
 * @version : OpenPages 8.2.0
 * @author : Praveen Ravi <BR>
 *         email : raviprav@us.ibm.com <BR>
 *         company : IBM OpenPages
 * 
 * @custom.date : 02-25-2021
 * @custom.feature : Helper Services
 * @custom.category : Helper
 */
@Service("dsmtLinkServiceUtilImpl")
public class DSMTLinkServiceUtilImpl {

    private Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IFieldUtil fieldUtil;

    @Autowired
    IGRCObjectUtil grcObjectUtil;

    @Autowired
    GRCObjectSearchUtil grcObjectSearchUtil;

    @Autowired
    IApplicationUtil applicationUtil;

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
        logger = loggerUtil.getExtLogger(DSMT_LINK_SERVICE_UTIL_LOG_FILE_NAME);
    }

    /**
     * <P>
     * This methods builds the query to get the existing DSMT Link objects for the current object from which the helper
     * was launched. The query is dynamically built based on the configured registry settings that has the object
     * hierarchy and the fields that needs to be retrieved for the associated DSMT link objects.
     * </P>
     * 
     * @param dsmtLinkHelperInfo-
     *            an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *            current session
     * 
     * @return the constructed query to get the Existing DSMT Link objects for a given object in string format
     * @throws Exception
     */
    public String buildExistingDSMTLinksQuery(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("buildExistingDSMTLinksQuery() Start");

        // Method Level Variables.
        String query = EMPTY_STRING;
        String baseQuery = EMPTY_STRING;
        String parentObjHeirarchy = EMPTY_STRING;
        String parentObjHeirarchyRegSetting = EMPTY_STRING;
        String existingDsmtLinkFields = EMPTY_STRING;

        List<String> parentObjHeirarchyList = null;
        List<String> existingDsmtLinkFieldsList = null;

        /* Initialize Variables */
        // Get the parent object hierarchy from the registry setting and parse it into a list
        parentObjHeirarchyRegSetting = dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LINK_APP_PARENT_HIERARCHY;
        parentObjHeirarchy = applicationUtil.getRegistrySetting(parentObjHeirarchyRegSetting);
        parentObjHeirarchyList = parseCommaDelimitedValues(parentObjHeirarchy);

        /* Log for debugging */
        logger.info("Parent Object hierarchy reg setting: " + parentObjHeirarchyRegSetting);
        logger.info("Parent Object hierarchy: " + parentObjHeirarchy);
        logger.info("Parent Object hierarchy list: " + parentObjHeirarchyList);

        /* Get the fields whose values needs to be obtained from the registry setting and parse it into a list */
        existingDsmtLinkFieldsList = parseCommaDelimitedValues(EXISTING_DSMT_LINK_FIELDS_INFO);

        /* Log for debugging */
        logger.info("Existing fields to display: " + existingDsmtLinkFields);
        logger.info("Existing fields to display list: " + existingDsmtLinkFieldsList);

        /* Construct the base query with the field values that needs to be retrieved */
        baseQuery = getBaseQueryToGetFieldValues(parentObjHeirarchyList, existingDsmtLinkFieldsList, true, true);
        logger.info("Base Query: " + baseQuery);

        /* Append the base query with the Child Association based on the object hierarchy */
        query = appendBaseQueryToChildAssociationQuery(dsmtLinkHelperInfo, parentObjHeirarchyList, baseQuery);

        /* Log the query information and return */
        logger.info("Base Query with association: " + query);
        logger.info("buildExistingDSMTLinksQuery() End");
        return query;
    }

    /**
     * <p>
     * This method returns a boolean value stating if there were higher level dependencies found for the given
     * DSMT Link object.
     * </P>
     *
     * @param parentObjectIdList - a list of parent object id's under which dependencies need to be checked
     * @param baseQuery          - The query that needs to be executed to get the dependency object
     * @param baseId             - The based ID that needs to be matched to confirm dependency
     * @return - a boolean value stating if a higher level dependency was found or not.
     * @throws Exception - Generic Exception
     */
    public boolean isHigherLevelDependencyPresent(Set<String> parentObjectIdList, String baseQuery, String baseId)
            throws Exception {

        logger.info("isHigherLevelDependencyPresent() Start");

        // Method Level Variables.
        boolean isHigherLevelDep = false;
        List<String> invalidDSMTList;

        // Method Implementation.
        // Make sure the parent objects list has information
        logger.info("Parent Object list: " + parentObjectIdList);
        if (isSetNotNullOrEmpty(parentObjectIdList)) {

            // Iterate through the list
            for (String parentObjectId : parentObjectIdList) {

                // Modify the query given with the variables
                baseQuery = baseQuery.replaceAll(BASE_ID_STR, baseId);
                baseQuery = baseQuery.replaceAll(RESOURCE_ID_STR, parentObjectId);
                logger.info("Is Higher Level Dependency base query: " + baseQuery);

                invalidDSMTList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(baseQuery, true);
                logger.info("Higher Level Dependency Present: " + isListNotNullOrEmpty(invalidDSMTList));

                // If present then return true and exit
                if (isListNotNullOrEmpty(invalidDSMTList)) {

                    isHigherLevelDep = true;
                    break;
                }
            }
        }

        logger.info("isHigherLevelDependencyPresent() End");
        return isHigherLevelDep;
    }

    /**
     * <p>
     * This method populates the lower level dependency if any to the DSMTLinkObjectInfo. If the query returns the
     * results the values of the lower level dependency is updated in the DSMTLinkObjectInfo object and returned.
     * </P>
     *
     * @param dsmtLinkObjectInfo - the instance of the DSMT Link object that needs to be populated with the higher
     *                           level dependency information
     * @return - the DSMT Link object instance with the higher level dependency information
     */
    public ExistingDSMTLinkInfo populateHigherLevelDependentResults(ExistingDSMTLinkInfo dsmtLinkObjectInfo) {

        logger.info("populateHigherLevelDependentResults() Start");

        // Method Level Variables.
        DSMTLinkDisableInfo dsmtLinkDisableInfo;
        Set<String> higherLevelDependentObjetSet;

        // Method Implementation.
        /* Populate the required information with the higher level dependency information */
        dsmtLinkDisableInfo = dsmtLinkObjectInfo.getDsmtLinkDisableInfo();
        dsmtLinkDisableInfo = isObjectNotNull(dsmtLinkDisableInfo) ? dsmtLinkDisableInfo : new DSMTLinkDisableInfo();
        higherLevelDependentObjetSet = dsmtLinkDisableInfo.getDisableInfoList();
        higherLevelDependentObjetSet = isSetNotNullOrEmpty(higherLevelDependentObjetSet) ?
                higherLevelDependentObjetSet :
                new HashSet<>();
        higherLevelDependentObjetSet.add(BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE);
        dsmtLinkDisableInfo.setDisableInfoList(higherLevelDependentObjetSet);
        dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);

        logger.info("populateHigherLevelDependentResults() End");
        return dsmtLinkObjectInfo;
    }

    /**
     * 
     * @param dsmtLinkHelperInfo
     * @param dsmtLinkObjectInfo
     * @return
     * @throws Exception
     */
    public ExistingDSMTLinkInfo processLowerLevelDependencyForDSMTLink(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            ExistingDSMTLinkInfo dsmtLinkObjectInfo) throws Exception {

        logger.info("processLowerLevelDependencyForDSMTLink() Start");

        // Method Level Variables.
        StringBuilder query = null;
        String constraint = EMPTY_STRING;
        String baseQuery = EMPTY_STRING;

        IGRCObject dsmtLinkObject = null;
        List<String> objectHeirarchy = null;
        List<String> objHeirarchyAndConstraint = null;
        List<String> existingDsmtLinkFieldsList = null;
        List<String> objHeirarchyAndConstraintInfoList = null;

        // Method Implementation.
        /* Initialize Variables */
        // Get the GRCObject from the DSMTLink Resource ID
        // Parse the comma delimited lower level dependency check information
        dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkObjectInfo.getResourceId());
        logger.info("Object Id: " + dsmtLinkObjectInfo.getResourceId());
        logger.info("DSMT Link Object: " + dsmtLinkObject.getName());
        existingDsmtLinkFieldsList = parseCommaDelimitedValues(EXISTING_DSMT_LINK_FIELDS_FOR_DISPLAY);
        objHeirarchyAndConstraintInfoList = parseCommaDelimitedValues(AUD_ENTITY_LOWER_LEVEL_DEPENDENCY_CHECK_INFO);

        // Iterate through each lower level dependency check
        for (String objHeirarchyAndConstraintInfo : objHeirarchyAndConstraintInfoList) {

            // Construct a new query, parse the pipe delimited value containing the object hierarchy and the constraint
            query = new StringBuilder();
            objHeirarchyAndConstraint = parsePipeDelimitedValues(objHeirarchyAndConstraintInfo);

            // Parse the colon delimited object hierarchy information
            objectHeirarchy = parseDelimitedValues(objHeirarchyAndConstraint.get(0), ":");
            logger.info("Object hierarchy: " + objectHeirarchy);

            // Construct the base query with the fields to retrieve
            baseQuery = getBaseQueryToGetFieldValues(objectHeirarchy, existingDsmtLinkFieldsList, false, true);
            logger.info("Base Query: " + baseQuery);

            // Append the object hierarchy information to the base query
            query.append(appendBaseQueryToParentAssociationQuery(dsmtLinkHelperInfo, objectHeirarchy, baseQuery));
            logger.info("Base Query With Parent Association: " + query.toString());

            query.append(SINGLE_SPACE);

            // Append the constraint, if the constraint has the Base ID add the base id value to the query
            constraint = objHeirarchyAndConstraint.get(1);
            query.append(constraint);

            // If the constraint contains the BASE ID field, then append the BASE ID Field value in the DSMT Link object
            // to the query
            if (constraint.contains(DSMT_LINK_BASE_ID_FIELD)) {

                query.append("'");
                query.append(fieldUtil.getFieldValueAsString(dsmtLinkObject, DSMT_LINK_BASE_ID_FIELD));
                query.append("'");
            }

            logger.info("Base Query With Association and Constraints: " + query.toString());
            dsmtLinkObjectInfo = populateLowerLevelDependentResults(query.toString(), dsmtLinkObjectInfo);

        }

        logger.info("processLowerLevelDependencyForDSMTLink() End");
        return dsmtLinkObjectInfo;
    }

    /**
     * <P>
     * This method populates the lower level dependency if any to the DSMTLinkObjectInfo. If the query returns the
     * results the values of the lower level dependency is updated in the DSMTLinkObjectInfo object and returned.
     * </P>
     * 
     * @param query
     * @param dsmtLinkObjectInfo
     * @return
     * @throws Exception
     */
    public ExistingDSMTLinkInfo populateLowerLevelDependentResults(String query, ExistingDSMTLinkInfo dsmtLinkObjectInfo)
            throws Exception {

        logger.info("populateLowerLevelDevependentyQueryResults() Start");

        // Method Level Variables.
        IGRCObject parentObj = null;
        IGRCObject lowerDSMTLinkObj = null;
        ITabularResultSet resultSet = null;
        DSMTLinkDisableInfo dsmtLinkDisableInfo = null;
        Set<String> lowerLevelDependentObjetSet = null;

        // Method Implementation.
        // Execute the query
        resultSet = grcObjectSearchUtil.executeQuery(query);

        // If the results are present
        if (isObjectNotNull(resultSet)) {

            // determine the dsmtLinkObjectInfo. If its already present reuse else create a new instance
            dsmtLinkObjectInfo = isObjectNotNull(dsmtLinkObjectInfo) ? dsmtLinkObjectInfo : new ExistingDSMTLinkInfo();
            dsmtLinkDisableInfo = dsmtLinkObjectInfo.getDsmtLinkDisableInfo();
            dsmtLinkDisableInfo = isObjectNotNull(dsmtLinkDisableInfo) ? dsmtLinkDisableInfo
                    : new DSMTLinkDisableInfo();

            lowerLevelDependentObjetSet = dsmtLinkDisableInfo.getLowerLevelDepObjectsList();
            lowerLevelDependentObjetSet = isSetNotNullOrEmpty(lowerLevelDependentObjetSet) ? lowerLevelDependentObjetSet
                    : new HashSet<String>();

            // Iterate through the results
            for (IResultSetRow row : resultSet) {

                // Construct the information of the lower dependency object
                lowerDSMTLinkObj = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(0)));
                parentObj = grcObjectUtil.getObjectFromId(lowerDSMTLinkObj.getPrimaryParent());
                lowerLevelDependentObjetSet.add(dsmtLinkHelperUtil
                        .getObjectDetailViewLinkForModal(parentObj.getId().toString(), parentObj.getName()));
            }
            dsmtLinkDisableInfo.setLowerLevelDepObjectsList(lowerLevelDependentObjetSet);
            dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);
        }

        logger.info("populateLowerLevelDevependentyQueryResults() End");
        return dsmtLinkObjectInfo;
    }

    public ExistingDSMTLinkInfo processValidDSMTLink(ExistingDSMTLinkInfo dsmtLinkObjectInfo) throws Exception {

        boolean isScopeOut;
        boolean isNotActive;
        Set<String> disableInfoList;
        DSMTLinkDisableInfo dsmtLinkDisableInfo;

        isScopeOut = isEqualIgnoreCase(SCOPE_OUT, dsmtLinkObjectInfo.getScope());
        isNotActive = isEqualIgnoreCase(NO, dsmtLinkObjectInfo.getActive());

        if (isScopeOut && isNotActive) {

            dsmtLinkDisableInfo = dsmtLinkObjectInfo.getDsmtLinkDisableInfo();
            dsmtLinkDisableInfo = isObjectNotNull(dsmtLinkDisableInfo) ? dsmtLinkDisableInfo :
                    new DSMTLinkDisableInfo();

            disableInfoList = dsmtLinkDisableInfo.getDisableInfoList();
            disableInfoList = isSetNotNullOrEmpty(disableInfoList) ? disableInfoList : new HashSet<>();
            disableInfoList.add(DSMT_LINK_NOT_ACTIVE_EXCEPTION);

            dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
            dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);
        }

        return dsmtLinkObjectInfo;
    }

    public ExistingDSMTLinkInfo processValidStatusForDSMTLink(ExistingDSMTLinkInfo dsmtLinkObjectInfo) throws Exception {

        logger.info("processValidStatusForDSMTLink() Start");

        Set<String> disableInfoList;
        DSMTLinkDisableInfo dsmtLinkDisableInfo;

        /*
         * If the status is empty then the item has to be disabled for select in the UI. Also build the
         * information to be displayed so user knows the reason why its disabled
         */
        if (isNotNullOrEmpty(dsmtLinkObjectInfo.getStatus())) {

            logger.info("Status is not Empty");

            dsmtLinkDisableInfo = dsmtLinkObjectInfo.getDsmtLinkDisableInfo();
            dsmtLinkDisableInfo = isObjectNotNull(dsmtLinkDisableInfo) ? dsmtLinkDisableInfo :
                    new DSMTLinkDisableInfo();

            disableInfoList = dsmtLinkDisableInfo.getDisableInfoList();
            disableInfoList = isSetNotNullOrEmpty(disableInfoList) ? disableInfoList : new HashSet<>();
            disableInfoList.add(DSMT_STATUS_IS_NOT_NULL_MESSAGE);

            dsmtLinkDisableInfo.setDisableInfoList(disableInfoList);
            dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);
        }

        logger.info("processValidStatusForDSMTLink() End");
        return dsmtLinkObjectInfo;
    }

    /**
     * <P>
     * This method populates the existing DSMT Links associated to the current object grouped by the Auditable Entity ID
     * present in the DSMT link object.
     * </P>
     * 
     * @param query
     * @return
     * @throws Exception
     */
    public Map<String, DSMTLinkObjectAssociationInfo> populateQueryResults(String query) throws Exception {

        logger.info("populateQueryResults() Start");

        // Method Level Variables.
        String auditableEntityId = null;

        ITabularResultSet resultSet = null;
        IGRCObject auditableEntityObj = null;
        DSMTLinkObjectInfo dsmtLinkObjectInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkObjectList = null;
        DSMTLinkObjectAssociationInfo dsmtLinkObjectAssociationInfo = null;
        Map<String, DSMTLinkObjectAssociationInfo> associatedDSMTLinksInfo = null;

        // Method Implementation.
        /* Initialize Variables */
        associatedDSMTLinksInfo = new LinkedHashMap<String, DSMTLinkObjectAssociationInfo>();

        // Execute the query
        logger.info("query: " + query);
        resultSet = grcObjectSearchUtil.executeQuery(query);

        // Iterate through the results
        for (IResultSetRow row : resultSet) {

            // Populate the retrieved values of the DSMT link object
            dsmtLinkObjectInfo = new DSMTLinkObjectInfo();
            dsmtLinkObjectInfo.setScope(fieldUtil.getFieldValueAsString(row.getField(2)));
            dsmtLinkObjectInfo.setStatus(fieldUtil.getFieldValueAsString(row.getField(0)));
            dsmtLinkObjectInfo.setHomeImpacted(fieldUtil.getFieldValueAsString(row.getField(1)));

            // Get the Auditable Entity ID value in the retrieved DSMT Link object
            auditableEntityId = fieldUtil.getFieldValueAsString(row.getField(3));
            auditableEntityObj = grcObjectUtil.getObjectFromId(auditableEntityId);
            dsmtLinkObjectInfo.setAuditableEntityName(auditableEntityObj.getName());

            // Check if the Auditable entity information already present in the Map if present use the
            // dsmtLinkObjectAssociationInfo Else instantiate a new dsmtLinkObjectAssociationInfo
            dsmtLinkObjectAssociationInfo = associatedDSMTLinksInfo.get(auditableEntityId);
            dsmtLinkObjectAssociationInfo = isObjectNotNull(dsmtLinkObjectAssociationInfo)
                    ? dsmtLinkObjectAssociationInfo
                    : new DSMTLinkObjectAssociationInfo();

            // Get the Child DSMT link objects list from the dsmtLinkObjectAssociationInfo. If the list is null
            // create a new list and add the dsmtLinkObjectInfo that has the details of the DSMT Link retrieved.
            dsmtLinkObjectList = dsmtLinkObjectAssociationInfo.getChildDSMTLinkObjectsList();
            dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ? dsmtLinkObjectList
                    : new ArrayList<DSMTLinkObjectInfo>();

            // Finally add the list to the dsmtLinkObjectAssociationInfo and set the dsmtLinkObjectAssociationInfo in
            // the Map. Repeat the above for each DSMT Link object retrieved.
            dsmtLinkObjectList.add(dsmtLinkObjectInfo);
            dsmtLinkObjectAssociationInfo.setChildDSMTLinkObjectsList(dsmtLinkObjectList);
            associatedDSMTLinksInfo.put(auditableEntityId, dsmtLinkObjectAssociationInfo);
        }

        logger.info("populateQueryResults() End");
        return associatedDSMTLinksInfo;
    }

    /**
     * <P>
     * This method creates and returns a base query string that has the Select and the From statement built dynamically.
     * The fields list provided is used to create the list of field information that will be used in Select statement
     * and the hierarchy is used to get the object from which the information will be retrieved.
     * </P>
     * 
     * @param objectHeirarchyList
     * @param fieldsList
     * @param isFieldsFromParent
     * @param isStartFromParent
     * @return
     * @throws Exception
     */
    public String getBaseQueryToGetFieldValues(List<String> objectHeirarchyList, List<String> fieldsList,
            boolean isFieldsFromParent, boolean isStartFromParent) throws Exception {

        logger.info("getBaseQueryToGetFieldValues() Start");

        // Method Level Variables.
        int count = 1;
        String objectType = EMPTY_STRING;
        String parentObjectType = EMPTY_STRING;
        StringBuilder fieldValuesQuery = null;

        // Method Implementation.
        // Make sure both the lists have values to proceed
        if (isListNotNullOrEmpty(objectHeirarchyList) && isListNotNullOrEmpty(fieldsList)) {

            // Get the Object type based on the isStartFromParent flag. If the flag is true
            // get the First object in the object hierarchy. If false get the last object in
            // the object hierarchy.
            objectType = isStartFromParent ? objectHeirarchyList.get(0)
                    : objectHeirarchyList.get(objectHeirarchyList.size() - 1);

            // Get the parent object type based on the isFieldsFromParent flag. If the flag is
            // true get the First object in the object hierarchy. If false get the last object in
            // the object hierarchy.
            parentObjectType = isFieldsFromParent ? objectHeirarchyList.get(0)
                    : objectHeirarchyList.get(objectHeirarchyList.size() - 1);

            // Make sure the fields list exists
            if (isListNotNullOrEmpty(fieldsList)) {

                // Instantiate and construct the query
                fieldValuesQuery = new StringBuilder();
                fieldValuesQuery.append("SELECT [");

                // Iterate through the fields and append it to the Select statement
                for (String fieldName : fieldsList) {

                    fieldValuesQuery.append(parentObjectType);
                    fieldValuesQuery.append("].[");
                    fieldValuesQuery.append(fieldName);

                    if (count == fieldsList.size())
                        fieldValuesQuery.append("] ");
                    else
                        fieldValuesQuery.append("], [");

                    count++;
                }

                // Finally add the From statement to the query
                fieldValuesQuery.append("FROM [");
                fieldValuesQuery.append(objectType);
                fieldValuesQuery.append("] ");
            }
        }

        logger.info("getBaseQueryToGetFieldValues() End");
        return fieldValuesQuery.toString();
    }

    /**
     * <P>
     * This method appends the JOIN construct to the base query provided. The JOIN is used to get to the Child object
     * from which information will be retrieved based on the child object hierarchy provided.
     * </P>
     * 
     * @param dsmtLinkHelperInfo
     * @param baseQuery
     * @return
     * @throws Exception
     */
    public String appendBaseQueryToChildAssociationQuery(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            List<String> childObjHeirarchyList, String baseQuery) throws Exception {

        logger.info("appendBaseQueryToChildAssociationQuery() Start");

        // Method Level Variables.
        int count = INT_ZERO;
        String parentObjectType = EMPTY_STRING;

        StringBuilder queryString = null;

        // Method Implementation.
        /* Initialize Variables */
        queryString = new StringBuilder();

        if (isListNotNullOrEmpty(childObjHeirarchyList)) {

            queryString.append(baseQuery);
            parentObjectType = childObjHeirarchyList.get(INT_ZERO);

            // Iterate through the hierarchy and append the JOIN statement for the CHILD object
            for (String childObjectTypeName : childObjHeirarchyList) {

                if (count != INT_ZERO) {

                    queryString.append("JOIN [");
                    queryString.append(childObjectTypeName);
                    queryString.append("] ON CHILD ([");
                    queryString.append(parentObjectType);
                    queryString.append("]) ");

                    parentObjectType = childObjectTypeName;
                }
                count++;
            }

            // Finally add the where clause to include the resource ID of the object from which the helper was launched.
            queryString.append("WHERE [");
            queryString.append(dsmtLinkHelperInfo.getObjectType());
            queryString.append("].[Resource ID] = ");
            queryString.append(dsmtLinkHelperInfo.getObjectID());

        }

        logger.info("appendBaseQueryToChildAssociationQuery() End");
        return queryString.toString();

    }

    /**
     * <P>
     * This method appends the parent association information to the given base query. The parent information is
     * obtained from the hierarchy
     * </P>
     * 
     * @param dsmtLinkHelperInfo
     * @param parentObjHeirarchyList
     * @param baseQuery
     * @return
     * @throws Exception
     */
    public String appendBaseQueryToParentAssociationQuery(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            List<String> parentObjHeirarchyList, String baseQuery) throws Exception {

        logger.info("appendQueryWithParentAssociation() Start");

        // Method Level Variables.
        int count = INT_ZERO;
        String parentObjectType = EMPTY_STRING;

        StringBuilder queryString = null;

        // Method Implementation.
        /* Initialize Variables */
        // Parse the comma separated object hierarchy to reach the Child object into a list
        queryString = new StringBuilder();

        if (isListNotNullOrEmpty(parentObjHeirarchyList)) {

            queryString.append(baseQuery);
            parentObjectType = parentObjHeirarchyList.get(INT_ZERO);

            // Iterate through the hierarchy and append the JOIN statement for the PARENT object
            for (String childObjectTypeName : parentObjHeirarchyList) {

                if (count != INT_ZERO) {

                    queryString.append(" JOIN [");
                    queryString.append(childObjectTypeName);
                    queryString.append("] ON PARENT ([");
                    queryString.append(parentObjectType);
                    queryString.append("]) ");

                    parentObjectType = childObjectTypeName;
                }
                count++;
            }

            // Finally add the where clause to include the resource ID of the object from which the helper was launched.
            queryString.append("WHERE [");
            queryString.append(dsmtLinkHelperInfo.getObjectType());
            queryString.append("].[Resource ID] = ");
            queryString.append(dsmtLinkHelperInfo.getObjectID());

        }
        logger.info("appendQueryWithParentAssociation() End");

        return queryString.toString();

    }

    /**
     * <P>
     * 
     * </P>
     * 
     * @param object
     * @throws Exception
     */
    public void deleteDSMTLinkAsSuperUser(IGRCObject object) throws Exception {

        IServiceFactory serviceFactory = null;
        IResourceService resourceService = null;
        SuperUserInformation superUserInformation = null;

        superUserInformation = new SuperUserInformation();
        superUserInformation.setSuperUserInfoInConfigFile(true);
        superUserInformation.setConfigFilePath(applicationUtil.getRegistrySetting(DSMT_LINK_DELETE_SUPER_USER_SETTING));

        serviceFactory = applicationUtil.createServiceFactoryForUser(superUserInformation);
        resourceService = serviceFactory.createResourceService();

        resourceService.deleteResource(object.getId());
    }

    /**
     * <P>
     * This method returns the query results in set
     * </P>
     *
     * @param  query
     *
     * @return      - the Set of resource Id from query
     * @throws Exception
     */
    public Set<String> returnQueryResultsSet(String query) throws Exception
    {

        logger.info("returnQueryResultsSet() Start");

        Set<String> returnSet = new HashSet<String>();
        // logger.info("query: " + query);
        ITabularResultSet resultSet = grcObjectSearchUtil.executeQuery(query);

        for (IResultSetRow row : resultSet)
        {
            returnSet.add(fieldUtil.getFieldValueAsString(row.getField(1)));
        }

        logger.info("returnQueryResultsSet() End");
        return returnSet;
    }


    public List<IGRCObject> copyListOfObjectsUnderParent(IGRCObject parentObject, List<Id> childObjectsToCopyList) throws Exception {

        logger.info("copyListOfObjectsUnderParent() Start");

        CopyObjectOptions copyOptions;
        List<IGRCObject> copiedObjList;
        IApplicationService applicationService;

        copyOptions = new CopyObjectOptions();
        copyOptions.setConflictBehavior(CopyConflictBehavior.OVERWRITE);
        copyOptions.setIncludeChildren(false);

        applicationService = serviceFactoryProxy.getServiceFactory().createApplicationService();
        logger.info("Parent Object Name: " + parentObject.getName());

        copiedObjList = applicationService.copyToParent(parentObject.getId(),
                                                                         childObjectsToCopyList, copyOptions);

        logger.info("copyListOfObjectsUnderParent() End");

        return copiedObjList;
    }


    public void updateInvalidDMSTLinkField(String resourceId, String baseQuery, String field, boolean value)
            throws Exception {

        logger.info("updateInvalidDMSTLinkField() Start");

        // Method Level Variables.
        IGRCObject helperObject = null;
        Set<String> queryResultSet = new HashSet<String>();

        logger.info("resourceId : " + resourceId);
        logger.info("Base query : \n" + baseQuery);
        logger.info("\nfield : " + field);
        logger.info("value : " + value);

        helperObject = grcObjectUtil.getObjectFromId(resourceId);
        logger.info("Helper Object name [" + helperObject.getName() + "], Id [" + helperObject.getId() + "]"
                + ", Object Type[" + helperObject.getType().getName() + "]");

        queryResultSet = returnQueryResultsSet(baseQuery + resourceId);
        logger.info("Invalid DSMT Link result set : " + queryResultSet);

        if (isSetNullOrEmpty(queryResultSet))
        {

            logger.info("Invalid DSMT Links were not found in : " + helperObject.getName());
            ResourceUtil.setFieldValue(helperObject.getField(field), value);
            grcObjectUtil.saveResource(helperObject);
            logger.info("Invalid DSMT exists field updated to : " + value);
        }

        logger.info("updateInvalidDMSTLinkField() End");
    }

}
