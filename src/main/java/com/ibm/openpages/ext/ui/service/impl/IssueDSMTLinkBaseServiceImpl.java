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

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;
import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.constant.IssueStatus;
import com.ibm.openpages.ext.ui.constant.IssueSubType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.*;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.*;

/**
 * <p>
 * This class works as the Controller to branch the request coming into the Issue DSMT Link helper controller.
 * All requests are handled by their corresponding methods marked by the request mapping.
 * </P>
 *
 * @author : Praveen Ravi <BR>
 * email : raviprav@us.ibm.com <BR>
 * company : IBM OpenPages
 * @version : OpenPages 8.2.0
 * @custom.date : 06-28-2021
 * @custom.feature : Helper Controller
 * @custom.category : Helper App
 */
@Component
public class IssueDSMTLinkBaseServiceImpl extends DSMTLinkBaseServiceImpl {

    /**
     * <p>
     * This method retrieves the Helpers Header Information from the Registry settings and sets the appropriate values
     * in the {@link CarbonHeaderInfo} object. This will be used to display the content in the UI Header.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the helper header information set
     * @throws Exception - Generic Exception
     */
    public DSMTLinkHelperAppInfo getHelperHeaderInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getHelperHeaderInfo() Start");

        // Method Level Variables.
        CarbonHeaderInfo headerInfo;

        /* Initialize Variables */
        // Get the helper header information by passing the Helper Base Setting + the object type label
        headerInfo = helperService.getTFUIHelperAppHeaderInfo(
                DSMT_LINK_APP_BASE_SETTING + dsmtLinkHelperInfo.getObjectTypeLabel());

        // Set the bean to be returned
        dsmtLinkHelperInfo.setHeaderInfo(headerInfo);
        logger.info("getHelperHeaderInfo() End");
        return dsmtLinkHelperInfo;
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
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception - Generic Exception
     */
    public DSMTLinkHelperAppInfo getLandingPageInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getLandingPageInfo() Start");

        Set<String> ancestorAudits;
        CitiIssueInfo citiIssueInfo;
        List<AncestorControlsInfo> ancestorControlsInfoList;


        // Get the Basic Object information of the current object under execution
        getBasicObjectInfoForDisplay(dsmtLinkHelperInfo);
        ancestorAudits = getAncestorAuditsofIssue(dsmtLinkHelperInfo);
        ancestorControlsInfoList = getAncestorControlObjectsInfo(dsmtLinkHelperInfo.getObjectID());


        citiIssueInfo = dsmtLinkHelperInfo.getCitiIssueInfo();
        citiIssueInfo = isObjectNotNull(citiIssueInfo) ? citiIssueInfo : new CitiIssueInfo();
        citiIssueInfo.setAncestorControlsInfoList(ancestorControlsInfoList);
        citiIssueInfo.setAncestorAuditsList(ancestorAudits);
        dsmtLinkHelperInfo.setCitiIssueInfo(citiIssueInfo);

        logger.info("getLandingPageInfo() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * <p>
     * This method looks for any higher order dependencies for the current DSMT object that is being processed.
     * The higher level dependencies are figured out based on the Sub-Type of the issue object and the status of
     * the issue object. Based on the sub-type and the status we either look for dependencies in the ancestor
     * controls or the ancestor audits.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @param dsmtLinkObjectInfo - an instance of the {@link ExistingDSMTLinkInfo} that has the details of the DSMT
     *                           object for which the higher level dependency needs to be checked
     * @return - the dsmtLinkObjectInfo with the higher level dependency information populated in it
     * @throws Exception - Generic Exception
     */
    public ExistingDSMTLinkInfo processHigherLevelDependencyCheck(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            ExistingDSMTLinkInfo dsmtLinkObjectInfo) throws Exception {

        logger.info("processHigherLevelDependencyForDSMTLink() Start");

        // Method Level Variables.
        boolean isDEAOETIssue;
        boolean isGenericIssue;
        boolean isDSMTLinkActive;
        boolean isPublishedIssue;
        boolean isDSMTLinkOutOfScope;
        boolean isDraftOrPendingIssue;
        boolean isHigherLevelDep = false;

        String baseId;
        IGRCObject dsmtObject;
        Set<String> parentObjectIdList;
        List<IssueSubType> issueSubTypeList;

        isDSMTLinkActive = isEqualIgnoreCase(YES, dsmtLinkObjectInfo.getActive());
        isDSMTLinkOutOfScope = isEqualIgnoreCase(OUT_STR, dsmtLinkObjectInfo.getScope());

        /* Check if the DSMT Link is active and if the DSMT Link is out of scope only then proceed */
        if (isDSMTLinkActive && isDSMTLinkOutOfScope) {

            /* Get the required information from the DSMT object */
            dsmtObject = grcObjectUtil.getObjectFromId(new Id(dsmtLinkObjectInfo.getResourceId()));
            issueSubTypeList = dsmtLinkHelperInfo.getCitiIssueInfo().getIssueSubTypeList();
            baseId = fieldUtil.getFieldValueAsString(dsmtObject, BASE_ID_FLD);

            /* Check what is the type of the object based on sub-type and status */
            isDEAOETIssue = isListNotNullOrEmpty(
                    issueSubTypeList) && issueSubTypeList.size() == 1 && issueSubTypeList.contains(
                    IssueSubType.DEA_OET);
            isGenericIssue = isListNotNullOrEmpty(issueSubTypeList) && issueSubTypeList.contains(IssueSubType.GENERIC);
            isDraftOrPendingIssue = dsmtLinkHelperInfo.getCitiIssueInfo().getIssueStatus()
                    .equals(IssueStatus.DRAFT) || dsmtLinkHelperInfo.getCitiIssueInfo().getIssueStatus()
                    .equals(IssueStatus.PENDING);
            isPublishedIssue = dsmtLinkHelperInfo.getCitiIssueInfo().getIssueStatus().equals(IssueStatus.PUBLISHED);

            logger.info("IS DEA OET Issue: " + isDEAOETIssue);
            logger.info("Is Generic Issue: " + isGenericIssue);
            logger.info("Is Draft or Published Issue: " + isPublishedIssue);
            logger.info("Is Draft or Pending Issue: " + isDraftOrPendingIssue);

            /* For DEA OET issues that are in Status Draft of Pending then check for higher level dependency on the
            Control parent hierarchy. */
            if (isDEAOETIssue && isDraftOrPendingIssue) {

                logger.info("Checking for Controls for higher level ");
                parentObjectIdList = dsmtLinkHelperInfo.getCitiIssueInfo().getAncestorControlsList();
                isHigherLevelDep = isHigherLevelDependencyPresent(parentObjectIdList,
                                                                  ASSOCIATED_INVALID_DSMTS_IN_CONTROL_QUERY, baseId);
            }
            /* For DEA OET issue that is published or if it's a Generic Issue then check for higher level dependency
            on the Audit parent hierarchy. */
            else if (isGenericIssue || isPublishedIssue) {

                logger.info("Checking for Audits for higher level ");
                parentObjectIdList = dsmtLinkHelperInfo.getCitiIssueInfo().getAncestorAuditsList();
                isHigherLevelDep = isHigherLevelDependencyPresent(parentObjectIdList,
                                                                  ASSOCIATED_INVALID_DSMTS_IN_AUDIT_QUERY, baseId);
            }
        }

        /* If a higher level dependency is found then go on to fill out the necessary information in the current
        DSMT Link object. */
        if (isHigherLevelDep) {

            dsmtLinkObjectInfo = populateHigherLevelDependentResults(dsmtLinkObjectInfo);
        }

        /* Log and return */
        logger.info("processHigherLevelDependencyForDSMTLink() End");
        return dsmtLinkObjectInfo;
    }

    /**
     * <p>
     * This method looks for any lower level dependencies for the current DSMT object that is being processed.
     * There are a series of hierarchical paths that needs to be checked for any lower level dependencies, the lower
     * level dependencies are checked based on the base id of the DSMT Link object.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @param dsmtLinkObjectInfo - an instance of the {@link ExistingDSMTLinkInfo} that has the details of the DSMT
     *                           object for which the lower order dependency needs to be checked
     * @return - the dsmtLinkObjectInfo with the higher order dependency information populated in it
     * @throws Exception - Generic Exception
     */
    public ExistingDSMTLinkInfo processLowerLevelDependencyCheck(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            ExistingDSMTLinkInfo dsmtLinkObjectInfo) throws Exception {

        logger.info("processLowerLevelDependencyForDSMTLink() Start");

        // Method Level Variables.
        String baseQuery;
        String constraint;
        StringBuilder query;

        IGRCObject dsmtLinkObject;
        List<String> objectHierarchy;
        List<String> objHierarchyAndConstraint;
        List<String> objHierarchyAndConstraintInfoList;

        // Method Implementation.
        /* Initialize Variables */
        // Get the GRCObject from the DSMTLink Resource ID
        dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkObjectInfo.getResourceId());
        objHierarchyAndConstraintInfoList = parseCommaDelimitedValues(AUDIT_ISSUE_LOWER_LEVEL_DEPENDENCY_CHECK_INFO);
        logger.info("Object Id: " + dsmtLinkObjectInfo.getResourceId());
        logger.info("DSMT Link Object: " + dsmtLinkObject.getName());

        // Iterate through each lower level dependency check
        for (String objHierarchyAndConstraintInfo : objHierarchyAndConstraintInfoList) {

            // Construct a new query, parse the pipe delimited value containing the object hierarchy and the constraint
            query = new StringBuilder();
            objHierarchyAndConstraint = parsePipeDelimitedValues(objHierarchyAndConstraintInfo);

            // Parse the colon delimited object hierarchy information
            objectHierarchy = parseDelimitedValues(objHierarchyAndConstraint.get(0), ":");
            baseQuery = AUDIT_ISSUE_LOWER_LEVEL_DEPENDENCY_BASE_QUERY + objHierarchyAndConstraint.get(1);
            logger.info("Object hierarchy: " + objectHierarchy);
            logger.info("Base Query: " + baseQuery);

            // Append the object hierarchy information to the base query
            query.append(
                    dsmtLinkServiceUtil.appendBaseQueryToParentAssociationQuery(dsmtLinkHelperInfo, objectHierarchy,
                                                                                baseQuery));
            logger.info("Base Query With Parent Association: " + query);

            query.append(SINGLE_SPACE);

            // Append the constraint, if the constraint has the Base ID add the base id value to the query
            constraint = objHierarchyAndConstraint.get(2);
            query.append(constraint);

            // If the constraint contains the BASE ID field, then append the BASE ID Field value in the DSMT Link object
            // to the query
            if (constraint.contains(DSMT_LINK_BASE_ID_FIELD)) {

                query.append("'");
                query.append(fieldUtil.getFieldValueAsString(dsmtLinkObject, DSMT_LINK_BASE_ID_FIELD));
                query.append("'");
            }

            logger.info("Base Query With Association and Constraints: " + query);
            dsmtLinkObjectInfo = populateLowerLevelDependentResults(query.toString(), dsmtLinkObjectInfo);
        }

        logger.info("processLowerLevelDependencyForDSMTLink() End");
        return dsmtLinkObjectInfo;
    }

    /**
     * <p>
     * This method populates the lower level dependency if any to the DSMTLinkObjectInfo. If the query returns the
     * results the values of the lower level dependency is updated in the DSMTLinkObjectInfo object and returned.
     * </P>
     *
     * @param query              - the query to execute that will return data if lower level dependencies are present
     * @param dsmtLinkObjectInfo - an instance of the {@link ExistingDSMTLinkInfo} that has the details of the DSMT
     *                           object for which the lower order dependency needs to be checked
     * @return - the dsmtLinkObjectInfo with the higher order dependency information populated in it
     * @throws Exception - Generic Exception
     */
    public ExistingDSMTLinkInfo populateLowerLevelDependentResults(String query,
            ExistingDSMTLinkInfo dsmtLinkObjectInfo) throws Exception {

        logger.info("populateLowerLevelDependentResults() Start");

        // Method Level Variables.
        IGRCObject parentObj;
        IGRCObject lowerDSMTLinkObj;
        List<String> matchingDSMTList;
        DSMTLinkDisableInfo dsmtLinkDisableInfo;
        Set<String> lowerLevelDependentObjetSet;

        // Method Implementation.
        // determine the dsmtLinkObjectInfo. If its already present reuse else create a new instance
        dsmtLinkObjectInfo = isObjectNotNull(dsmtLinkObjectInfo) ? dsmtLinkObjectInfo : new ExistingDSMTLinkInfo();
        dsmtLinkDisableInfo = dsmtLinkObjectInfo.getDsmtLinkDisableInfo();
        dsmtLinkDisableInfo = isObjectNotNull(dsmtLinkDisableInfo) ? dsmtLinkDisableInfo : new DSMTLinkDisableInfo();

        // Execute the query
        matchingDSMTList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, false);
        logger.info("Matching DSMT Links: " + matchingDSMTList);

        // If the results are present then a lower level dependency is present
        if (isListNotNullOrEmpty(matchingDSMTList)) {

            lowerLevelDependentObjetSet = dsmtLinkDisableInfo.getLowerLevelDepObjectsList();
            lowerLevelDependentObjetSet = isSetNotNullOrEmpty(lowerLevelDependentObjetSet) ?
                    lowerLevelDependentObjetSet :
                    new HashSet<>();

            // Iterate through the results to add the information
            for (String lowerDSMTLinkId : matchingDSMTList) {

                // Construct the information of the lower dependency object
                lowerDSMTLinkObj = grcObjectUtil.getObjectFromId(lowerDSMTLinkId);
                parentObj = grcObjectUtil.getObjectFromId(lowerDSMTLinkObj.getPrimaryParent());
                lowerLevelDependentObjetSet.add(
                        dsmtLinkHelperUtil.getObjectDetailViewLinkForModal(parentObj.getId().toString(),
                                                                           parentObj.getName()));
            }
            dsmtLinkDisableInfo.setLowerLevelDepObjectsList(lowerLevelDependentObjetSet);
            dsmtLinkObjectInfo.setDsmtLinkDisableInfo(dsmtLinkDisableInfo);
        }

        logger.info("populateLowerLevelDependentResults() End, " + dsmtLinkObjectInfo);
        return dsmtLinkObjectInfo;
    }

    /**
     * <p>
     * This method returns the ancestor Controls for the given Issue object based on the control hierarchy path and
     * the search criteria. This list of controls are used especially for the DEA_OET Issue.
     * </P>
     *
     * @param objectId - the issue object id for which the ancestor controls needs to be identified
     * @return - a list of {@link AncestorControlsInfo} that contains the Ancestor Control object information
     * @throws Exception - Generic Exception
     */
    private List<AncestorControlsInfo> getAncestorControlObjectsInfo(String objectId) throws Exception {

        logger.info("getAncestorObjectsOfSpecificType() Start");

        List<String> fieldsList;
        StringBuilder query;
        List<String> hierarchyList;
        ITabularResultSet resultSet;
        List<String> hierarchyPathList;
        AncestorControlsInfo controlInfo;
        List<AncestorControlsInfo> controlsInfoList;

        // Method Implementation.
        // Initialize Variables
        fieldsList = new ArrayList<>();
        fieldsList.add("Name");
        fieldsList.add("Resource ID");
        controlsInfoList = new ArrayList<>();
        hierarchyPathList = parsePipeDelimitedValues(CONTROL_ISSUE_HIERARCHY_PATH);
        logger.info("Hierarchy Path List: " + hierarchyPathList);

        // Make sure we have the hierarchy paths
        if (isListNotNullOrEmpty(hierarchyPathList)) {

            // Iterate over the hierarchy paths
            for (String hierarchy : hierarchyPathList) {

                // Construct the query using the hierarchy path and search criteria
                hierarchyList = parseCommaDelimitedValues(hierarchy);
                logger.info("Hierarchy List: " + hierarchyList);

                query = grcObjectSearchUtil.getQueryForMultipleParentAssociation(fieldsList, hierarchyList);
                query.append("[Resource ID] = '");
                query.append(objectId);
                query.append("'");
                query.append(CONTROL_ISSUE_SEARCH_CRITERIA);
                logger.info("Query to execute: " + query);

                resultSet = grcObjectSearchUtil.executeQuery(query.toString(), false);

                // Iterate over the results and populate the Control information as necessary and add to the list
                for (IResultSetRow row : resultSet) {

                    controlInfo = new AncestorControlsInfo();
                    controlInfo.setResourceId(fieldUtil.getFieldValueAsString(row.getField(1)));
                    controlInfo.setControlName(fieldUtil.getFieldValueAsString(row.getField(0)));

                    controlsInfoList.add(controlInfo);
                }
            }
        }

        /* Return the constructed list */
        logger.info("getAncestorObjectsOfSpecificType() End");
        return controlsInfoList;
    }

    private Set<String> getAncestorAuditsofIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception {

        logger.info("getAncestorAuditsofIssue() Start");

        String query;
        List<String> fieldsList;
        Set<String> ancestorAudits;
        List<String> auditHierarchyList;
        List<String> parentAuditHierarchyList;

        fieldsList = new ArrayList<>();
        ancestorAudits = new HashSet<>();

        parentAuditHierarchyList = parseCommaDelimitedValues(FETCH_ANCESTOR_AUDITS_HIERARCHY);

        if (isListNotNullOrEmpty(parentAuditHierarchyList)) {

            fieldsList.add("Resource ID");

            for (String parentAuditHierarchy : parentAuditHierarchyList) {

                auditHierarchyList = parseDelimitedValues(parentAuditHierarchy, COLON_SEPERATED_DELIMITER);
                logger.info("Hierarchy List: " + auditHierarchyList);

                if (isListNotNullOrEmpty(auditHierarchyList)) {

                    query = dsmtLinkServiceUtil.getBaseQueryToGetFieldValues(auditHierarchyList, fieldsList, true,
                                                                             true);

                    query = dsmtLinkServiceUtil.appendBaseQueryToParentAssociationQuery(dsmtLinkHelperAppInfo,
                                                                                        auditHierarchyList, query);

                    logger.info("Query to execute: " + query);

                    ancestorAudits.addAll(grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, false));
                }

            }
        }

        return ancestorAudits;
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
    private boolean isHigherLevelDependencyPresent(Set<String> parentObjectIdList, String baseQuery, String baseId)
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
}
