/*********************************************************************************************************************
 IBM Confidential OCO Source Materials

 5725-D51, 5725-D52, 5725-D53, 5725-D54

 © Copyright IBM Corporation 2021

 The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 has been deposited with the U.S. Copyright Office.
 */

package com.ibm.openpages.ext.ui.service.impl;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.ui.constant.BMQSIERAMRConstants.*;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.service.IGRCObjectTypeUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.openpages.ext.ui.service.IAuditMappingReportDSMTLinkService;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("bmqsIerAmrServiceImpl")
public class BMQSIERAMRServiceImpl extends AuditMappingResourceBaseServiceImpl implements IAuditMappingReportDSMTLinkService {

	@Autowired
	IServiceFactoryProxy serviceFactoryProxy;

	@Autowired
	IGRCObjectTypeUtil grcObjectTypeUtil;

	@PostConstruct
	public void initServiceImpl() {

		// use ILoggerUtil service in projects
		logger = loggerUtil.getExtLogger(BMQS_IER_LOG_FILE_NAME);

	}

	@Override
	public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGridInfo getAvailableDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGridInfo searchDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DSMTLinkHelperAppInfo addDSMTLinksToAMR(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DSMTLinkHelperAppInfo descopeDSMTLinksFromAMR(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 *     This method returns a DataGrid with issues that are available to be associated to the AMR.
	 * </p>
	 * <p>
	 *     Ancestor BMQS Query : This query will return all the ancestor BMQS associated to the current AMR
	 *     Issues directly associated to AMR : This query will return all the issues that are directly associated to the current AMR.
	 *
	 *    Once we have the above two information, we pass those as an argument to processAncestorIssuesList() method.
	 *    This method will make use of those information to process a new list, which will have all the issues that has to be displayed in the UI.
	 * </p>
	 *
	 * @param dsmtLinkHelperInfo
	 * @return
	 * @throws Exception
	 */
	@Override
	public DataGridInfo getAncestorIssues(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

		DataGridInfo issueDataGridInfo = new DataGridInfo();

		logger.info("BMQS IER getAncestorIssues() Start");

		// Local Variables
		int rowCount = 1;
		String amrResourceId = dsmtLinkHelperInfo.getObjectID();

		// Table Header Variables
		String issueTableHeaderString = TABLE_HEADER_FIELDS_FOR_ISSUES;
		List<String> TableHeaderList = parseCommaDelimitedValues(issueTableHeaderString);
		List<DataGridHeaderColumnInfo> gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(TableHeaderList);
		logger.info("Issues Table Header List : " + gridHeaderInfoList);

		// Query String Variables
		String ancestorBMQSAssociatedToCurrentAMRQuery = GET_ALL_ANCESTOR_BMQS_ASSOCIATED_TO_CURRENT_AMR_QUERY.replaceAll(RESOURCE_ID, amrResourceId);
		String issuesDirectlyAssociatedToAMRQuery = GET_ALL_ISSUES_DIRECTLY_ASSOCIATED_TO_CURRENT_AMR_QUERY.replaceAll(RESOURCE_ID, amrResourceId);

		logger.info("Ancestor BMQS associated to current AMR query: " + ancestorBMQSAssociatedToCurrentAMRQuery);
		logger.info("Issues directly associated to AMR query: " + issuesDirectlyAssociatedToAMRQuery);

		// Run Queries
		List<String> ancestorBMQSAssociatedToCurrentAMRList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(ancestorBMQSAssociatedToCurrentAMRQuery, false);
		List<String> issuesDirectlyAssociatedToAMRList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(issuesDirectlyAssociatedToAMRQuery, false);

		logger.info("Ancestor BMQS associated to current AMR list: " + ancestorBMQSAssociatedToCurrentAMRList);
		logger.info("Issues directly associated to AMR list: " + issuesDirectlyAssociatedToAMRList);

		// Process the final list of issues to be displayed in the UI
		List<AncestorIssueInfo> finalListOfIssues = processAncestorIssuesList(ancestorBMQSAssociatedToCurrentAMRList, issuesDirectlyAssociatedToAMRList);

		// For each item in the list we set a row-count, which is used in the UI to display the issues in the order of the row-count
		for (AncestorIssueInfo eachObject : finalListOfIssues) {

			eachObject.setId("row-" + rowCount);
			rowCount++;
		}

		logger.info("Final list of issue:  " + finalListOfIssues);

		issueDataGridInfo.setHeaders(gridHeaderInfoList);
		issueDataGridInfo.setRows(finalListOfIssues);

		logger.info("BMQS IER getAncestorIssues() End");

		return issueDataGridInfo;
	}

	/**
	 * <p>
	 *     This method will associate or dissociate the given list of issues.
	 *     It Iterates through List<ExistingDSMTLinkInfo> and get the issue information (get issue resourceID and Scope)
	 *          1. If the Scope = Associated, add the resourceID to IssuesToBeAssociatedList
	 *          2. If the Scope = Dissociated, add the resourceID to  IssuesToBeDissociatedList
	 *     Once the IssuesToBeAssociatedList and IssuesToBeDissociatedList are prepared, it calls the respective methods to
	 *     associate and dissociate the issues.
	 * </p>
	 *
	 * @param dsmtLinkHelperAppInfo
	 * @return
	 * @throws Exception
	 */
	@Override
	public DSMTLinkHelperAppInfo processScopeIssuesFromAMR(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception {

		logger.info("BMQS IER processScopeIssuesFromAMR() START");

		// Local Variables
		List<DSMTLinkUpdateInfo> dsmtLinkUpdateInfo = dsmtLinkHelperAppInfo.getDsmtLinkUpdateInfo();
		List<ExistingDSMTLinkInfo> existingDSMTLinkInfo = new ArrayList<ExistingDSMTLinkInfo>();
		List<String> IssuesToBeAssociatedList = new ArrayList<String>();
		List<String> IssuesToBeDissociatedList = new ArrayList<String>();

		// Iterate through List<DSMTLinkUpdateInfo>, get the List<ExistingDSMTLinkInfo>
		if (isListNotNullOrEmpty(dsmtLinkUpdateInfo))
			for (DSMTLinkUpdateInfo eachItem : dsmtLinkUpdateInfo) {
				existingDSMTLinkInfo = eachItem.getDsmtLinkIdsToUpdate();
			}

		/*
            Iterate through List<ExistingDSMTLinkInfo> and get the issue information (get issue resourceID and Scope)
            1. If the Scope = Associated, add the resourceID to IssuesToBeAssociatedList
            2. If the Scope = Dissociated, add the resourceID to  IssuesToBeDissociatedList
		*/
		if (isListNotNullOrEmpty(existingDSMTLinkInfo))
			for (ExistingDSMTLinkInfo eachItem : existingDSMTLinkInfo) {

				String resourceID = eachItem.getResourceId();
				logger.info("Resource ID of the issue  : " + resourceID);
				String scope = eachItem.getScope();
				logger.info("Scope value of the issue  : " + scope);

				if (isEqual(scope, STATUS_ASSOCIATED))
					IssuesToBeAssociatedList.add(resourceID);

				if (isEqual(scope, STATUS_DISSOCIATED))
					IssuesToBeDissociatedList.add(resourceID);
			}

		logger.info("List of issues (resourceID) to be associated  : " + IssuesToBeAssociatedList);
		logger.info("List of issues (resourceID) to be dissociated : " + IssuesToBeDissociatedList);

		// Associate Issues
		if(isListNotNullOrEmpty(IssuesToBeAssociatedList))
			associateIssues(dsmtLinkHelperAppInfo, IssuesToBeAssociatedList);

		// Dissociate Issues
		if(isListNotNullOrEmpty(IssuesToBeDissociatedList))
			dissociateIssues(dsmtLinkHelperAppInfo, IssuesToBeDissociatedList);

		logger.info("BMQS IER processScopeIssuesFromAMR() END");

		return dsmtLinkHelperAppInfo;
	}

	/**
	 * <p>
	 *     For each ancestor BMQS resource id in ancestorBMQSAssociatedToCurrentAMRList: run the query that will give us all the issues associated to each of the BMQS.
	 *     Iterate through the query output, for each row : get the  issue_ResourceID, issue_Name, issue_TitleOrDescription, issue_Level and association_Status.
	 *     Create a new instance of the ancestorIssueInfo ean and set the above values to it.
	 * </p>
	 * <p>
	 *     Conditions to set association_Status in the bean
	 *     Condition 1 : if the list(issuesDirectlyAssociatedToAMRList) is not null or empty and contains the current issue resource id : then association status will be ASSOCIATED
	 *     Condition 2 : for all other cases  : the association status will be DISSOCIATED
	 * </p>
	 * <p>
	 *     Add the list of dissociated issues to the finalListOfIssues list
	 *     Add the list of associated issues to the finalListOfIssues list
	 *     return the finalListOfIssues list
	 * </p>
	 * @param ancestorBMQSAssociatedToCurrentAMRList
	 * @param issuesDirectlyAssociatedToAMRList
	 * @return
	 * @throws Exception
	 */
	private List<AncestorIssueInfo> processAncestorIssuesList(List<String> ancestorBMQSAssociatedToCurrentAMRList, List<String> issuesDirectlyAssociatedToAMRList) throws Exception {

		logger.info("BMQS IER processAncestorIssuesList() START");

		// Local Variables
		AncestorIssueInfo ancestorIssueInfo = null;
		Set<String> issuesAlreadyProcessedSet = new HashSet<>();
		List<AncestorIssueInfo> issuesToBeAssociatedList = new ArrayList<>();
		List<AncestorIssueInfo> issuesToBeDisassociatedList = new ArrayList<>();
		List<AncestorIssueInfo> finalListOfIssues = new ArrayList<>();

		if(isListNotNullOrEmpty(ancestorBMQSAssociatedToCurrentAMRList))
			for(String ancestorBMQSResourceId : ancestorBMQSAssociatedToCurrentAMRList) {

				// Run Query
				logger.info("Get all issues associated to each ancestor BMQS query: " + GET_ALL_ISSUES_ASSOCIATED_TO_EACH_ANCESTOR_BMQS_QUERY.replaceAll(RESOURCE_ID, ancestorBMQSResourceId));
				ITabularResultSet resultSet = grcObjectSearchUtil.executeQuery(GET_ALL_ISSUES_ASSOCIATED_TO_EACH_ANCESTOR_BMQS_QUERY.replaceAll(RESOURCE_ID, ancestorBMQSResourceId), true);

				for (IResultSetRow row : resultSet) {

					String issue_ResourceID = fieldUtil.getFieldValueAsString(row.getField(0));
					String issue_Name = fieldUtil.getFieldValueAsString(row.getField(1));
					String issue_TitleOrDescription = fieldUtil.getFieldValueAsString(row.getField(2));
					String issue_Level = fieldUtil.getFieldValueAsString(row.getField(3));
					//String issue_Status = fieldUtil.getFieldValueAsString(row.getField(4));
					//String DSMT_BaseID = fieldUtil.getFieldValueAsString(row.getField(5));
					//String DSMT_Scope = fieldUtil.getFieldValueAsString(row.getField(4));

					ancestorIssueInfo = new AncestorIssueInfo();
					ancestorIssueInfo.setResourceId(issue_ResourceID);
					ancestorIssueInfo.setName(issue_Name);
					ancestorIssueInfo.setTitle(issue_TitleOrDescription);
					ancestorIssueInfo.setIssuelevel(issue_Level);

					if(!issuesAlreadyProcessedSet.contains(issue_ResourceID)){
						if (isListNotNullOrEmpty(issuesDirectlyAssociatedToAMRList) && issuesDirectlyAssociatedToAMRList.contains(issue_ResourceID)) {
							ancestorIssueInfo.setAssociationstatus(STATUS_ASSOCIATED);
							issuesToBeAssociatedList.add(ancestorIssueInfo);
						} else {
							ancestorIssueInfo.setAssociationstatus(STATUS_DISSOCIATED);
							issuesToBeDisassociatedList.add(ancestorIssueInfo);
						}
					}

					issuesAlreadyProcessedSet.add(issue_ResourceID);

				}
			}

		if(isListNotNullOrEmpty(issuesToBeDisassociatedList))
			finalListOfIssues.addAll(issuesToBeDisassociatedList);

		if(isListNotNullOrEmpty(issuesToBeAssociatedList))
			finalListOfIssues.addAll(issuesToBeAssociatedList);

		logger.info("issues to be associated list: " + issuesToBeAssociatedList);
		logger.info("issues to be disassociated list: " + issuesToBeDisassociatedList);

		logger.info("BMQS IER processAncestorIssuesList() END");

		return finalListOfIssues;
	}

	/**
	 * <p>
	 * This method will do the following
	 * 1. Associate the given list of issues to current AMR
	 * 2. Associate DSMT's to current AMR
	 *      The helper will get all the DSMT's, based on the unique key: BaseID, and copy them under the AMR.
	 *      If a DSMT is shared among multiple issues, it will be copied only once under the AMR.
	 *      There will be only one copy of DSMT with the same BaseID under the AMR.
	 * 3. Associate AuditEntity to current AMR
	 *      Associate the AE listed on the DSMT AEID field, to the AMR, where DSMT Link is active (Active = Yes),
	 *      in scope (Scope - In) and status is blank (Status = Blank).
	 * </p>
	 *
	 * @param dsmtLinkHelperAppInfo
	 * @param IssuesToBeAssociatedList
	 * @throws Exception
	 */
	private DSMTLinkHelperAppInfo associateIssues(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo, List<String> IssuesToBeAssociatedList) throws Exception {

		logger.info("BMQS IER associateIssues() START");

		// Set the scopedIn and scopedOut DSMT(S) to the bean, so we can use it later.
		dsmtLinkHelperAppInfo = getDSMTBaseIdInfoForAMR(dsmtLinkHelperAppInfo);

		if (isListNotNullOrEmpty(IssuesToBeAssociatedList)) {

			// STEP 1
			logger.info("\n\n");
			logger.info("STEP 1: Associate issue(s) : START");

			// Local Variables
			AuditMappingReportInfo auditMappingReportInfo = dsmtLinkHelperAppInfo.getAuditMappingReportInfo();
			auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo : new AuditMappingReportInfo();
			IssuesToBeAssociatedList = isListNotNullOrEmpty(IssuesToBeAssociatedList) ? IssuesToBeAssociatedList : new ArrayList<String>();
			List<Id> issuesAndAEToBeAssociatedList = new ArrayList<Id>();
			List<Id> dsmtsToBeCopiedList = new ArrayList<>();
			List<Id> dsmtsToBeUpdatedList = new ArrayList<>();

			// Query Variables
			String issuesDirectlyAssociatedToAMRQuery = GET_ALL_ISSUES_DIRECTLY_ASSOCIATED_TO_CURRENT_AMR_QUERY.replaceAll(RESOURCE_ID, dsmtLinkHelperAppInfo.getObjectID());
			logger.info("issuesDirectlyAssociatedToAMRQuery: " + issuesDirectlyAssociatedToAMRQuery);

			// Run Query : Returns all issues directly associated to AMR
			List<String> issuesDirectlyAssociatedToAMRList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(issuesDirectlyAssociatedToAMRQuery, false);
			logger.info("List of issues (resource ID's) directly associated to AMR: " + issuesDirectlyAssociatedToAMRList);
			logger.info("List of issues (resource ID's) to be associated to AMR: " + IssuesToBeAssociatedList);

			/*
                Iterate through the IssuesToBeAssociatedLists
                Check if the issue resource ID is not in the list of issues directly associated to AMR?
                If it is not, then add it to final list of issues to be associated.
			 */
			for (String issueResourceID : IssuesToBeAssociatedList) {
				if (!issuesDirectlyAssociatedToAMRList.contains(issueResourceID))
					issuesAndAEToBeAssociatedList.add(new Id(issueResourceID));
			}

			logger.info("Final list of issues (resource ID's) to be associated: " + issuesAndAEToBeAssociatedList);
			logger.info("STEP 1: Associate issue(s) : END");
			logger.info("\n\n");

			// STEP 2
			logger.info("\n\n");
			logger.info("STEP 2: Associate DSMT(s) : START");

			// Local Variables
			Map<String, List<String>> dsmtAssociatedToEachIssueMap = new HashMap<String, List<String>>();
			Set<String> scopedInDSMTSet = auditMappingReportInfo.getScopedInDSMTBaseIdList();
			scopedInDSMTSet = isSetNotNullOrEmpty(scopedInDSMTSet) ? scopedInDSMTSet : new HashSet<>();
			Map<String, String> scopedOutDSMTMap = auditMappingReportInfo.getScopedOutDSMTInfoMap();
			scopedOutDSMTMap = isMapNotNullOrEmpty(scopedOutDSMTMap) ? scopedOutDSMTMap : new HashMap<>();
			Set<String> scopedOutDSMTSet = scopedOutDSMTMap.keySet();

			/*
			Iterate through finalIssuesToBeAssociatedList, for each issue resourceId in the list
			Get info about the DSMT'S (baseId, ResourceId, and AEID) associated to the issue.
			Add the output of the query to dsmtAssociatedToEachIssueMap with baseID as key and value = ResourceId, and AEID.
			If a dsmt is shared between multiple issues, then it will be added to the map only once.
			 */
			for (Id issueResourceID : issuesAndAEToBeAssociatedList) {
				String dmtAssociatedToEachIssueQuery = GET_ALL_DSMT_ASSOCIATED_TO_EACH_ISSUE_QUERY.replaceAll(RESOURCE_ID, issueResourceID.toString());
				logger.info("dmtAssociatedToEachIssueQuery: " + dmtAssociatedToEachIssueQuery);
				dsmtAssociatedToEachIssueMap.putAll(grcObjectSearchUtil.getMultipleValuesFromQueryAsMap(dmtAssociatedToEachIssueQuery, false));
			}

			/*
			Iterate through the dsmtAssociatedToEachIssueMap, for each DSMT baseID in the map, get the resourceID of the DSMT.
			If the baseId is not in Scoped-In or Scoped-Out List then we add the DSMT'S resourceID to dsmtsToBeCopiedList list
			If the baseId is in Scoped-Out List then we add the DSMT'S resourceID to dsmtsToBeUpdatedList list
			 */
			if (isMapNotNullOrEmpty(dsmtAssociatedToEachIssueMap))
				for (String baseIDKey : dsmtAssociatedToEachIssueMap.keySet()) {

					String resourceID = dsmtAssociatedToEachIssueMap.get(baseIDKey).get(0);
					logger.info("DSMT Resource ID : " + resourceID);

					if (!scopedInDSMTSet.contains(baseIDKey) && !scopedOutDSMTSet.contains(baseIDKey)) {
						dsmtsToBeCopiedList.add(new Id(resourceID));
					} else if (scopedOutDSMTSet.contains(baseIDKey)) {
						dsmtsToBeUpdatedList.add(new Id(scopedOutDSMTMap.get(baseIDKey)));
					}
				}

			logger.info("Map with DSMT'S info Associated to each Issue" + dsmtAssociatedToEachIssueMap);
			logger.info("Final list of DSMT Links To Copy : " + dsmtsToBeCopiedList);
			logger.info("Final list of DSMT Links To Update : " + dsmtsToBeUpdatedList);
			logger.info("STEP 2: Associate DSMT(s) : END");
			logger.info("\n\n");

			// STEP 3
			logger.info("\n\n");
			logger.info("STEP 3: Associate AE(s) : START");

			// Local Variables
			String amrObjectID = dsmtLinkHelperAppInfo.getObjectID();
			IGRCObject amrObject = grcObjectUtil.getObjectFromId(amrObjectID);
			List<String> parentAEIdList = getParentObjectsIdsOfTypeAuditableEntity(amrObject, AUDITABLEENTITY);
			parentAEIdList = isListNotNullOrEmpty(parentAEIdList) ? parentAEIdList : new ArrayList<>();

			for (String baseIDKey : dsmtAssociatedToEachIssueMap.keySet()) {

				// Get the AE ID from the map
				String aeID = dsmtAssociatedToEachIssueMap.get(baseIDKey).get(1);

				// if the AE ID is not in parentAEIdList, then add the AE ID to issuesAndAEToBeAssociatedList List
				if (!parentAEIdList.contains(aeID)) {
					issuesAndAEToBeAssociatedList.add(new Id(aeID));
				}

			}

			logger.info("Parent Auditable Entity ID List : " + parentAEIdList);
			logger.info("Final List of Issues and Auditable Entities To be associated : " + issuesAndAEToBeAssociatedList);
			logger.info("STEP 3: Associate AE(s) : END");
			logger.info("\n\n");

			// Set all the information to dsmtLinkHelperAppInfo bean
			auditMappingReportInfo.setDsmtLinksToCopy(dsmtsToBeCopiedList);
			auditMappingReportInfo.setDsmtLinksToUpdate(dsmtsToBeUpdatedList);
			auditMappingReportInfo.setAuditableEntityToAssociate(issuesAndAEToBeAssociatedList);
			dsmtLinkHelperAppInfo.setAuditMappingReportInfo(auditMappingReportInfo);

			// Perform Association
			processAddNewDSMTLinksToAMR(dsmtLinkHelperAppInfo);
			processUpdateExistingDSMTLinksForAMR(dsmtLinkHelperAppInfo, SCOPE_IN);
			processAssociateParents(dsmtLinkHelperAppInfo);
		} else {
			logger.info("There are no issues that has to be associated to the AMR.");
		}

		logger.info("BMQS IER associateIssues() END");

		return dsmtLinkHelperAppInfo;
	}

	/**
	 * <p>
	 * This method would do the following
	 * 1. Dissociate the issue from the current AMR
	 *
	 * 2. Dissociate the DSMT's from the current AMR
	 *      a. Update the Scope to Out for any AMR DSMT's that share the base ID with the issue DSMT's for the issue being disassociated.
	 *         And that DSMT's BaseID is not shared by any other issues associated to the AMR.
	 *      b. If any DSMT BaseID is shared among issues being disassociated and issue is still associated to the AMR,
	 *         then the shared DSMT's will not be marked out of Scope.
	 *
	 * 3. Dissociate the AuditEntity from the current AMR
	 *      Also check that for every AE associated to the AMR, there should be at least one DSMT that is active (Active = Yes),
	 *      in scope (Scope - In) and status is blank (Status = Blank). if there is no such DSMT, the AE should be disassociated from the AMR.
	 * </p>
	 *
	 * @param dsmtLinkHelperAppInfo
	 * @param IssuesToBeDissociatedList
	 * @return
	 * @throws Exception
	 */
	private DSMTLinkHelperAppInfo dissociateIssues(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo, List<String> IssuesToBeDissociatedList) throws Exception {

		logger.info("BMQS IER dissociateIssues() START");

		// Local Variables
		AuditMappingReportInfo auditMappingReportInfo = dsmtLinkHelperAppInfo.getAuditMappingReportInfo();
		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo : new AuditMappingReportInfo();
		List<Id> issuesAndAEToBeAssociatedList = new ArrayList<Id>();
		List<String> remainingIssuesAssociatedToAMRList = new ArrayList<>();
		List<Id> dsmtsToBeUpdatedList = new ArrayList<>();

		// STEP 1
		logger.info("\n\n");
		logger.info("STEP 1: Dissociate issue(s) : START");

		/*
			<p>
			The below query will return all issues directly associated to AMR.
			Iterate through the output of the query and check if eachIssue is not in the list of issues to be dissociated?
			If it is not then add that issue resourceID to remainingIssuesAssociatedToAMRList.
			</p>
		 */
		String issuesDirectlyAssociatedToAMRQuery = GET_ALL_ISSUES_DIRECTLY_ASSOCIATED_TO_CURRENT_AMR_QUERY.replaceAll(RESOURCE_ID, dsmtLinkHelperAppInfo.getObjectID());
		logger.info("Issues directly associated to AMR : QUERY : " + issuesDirectlyAssociatedToAMRQuery);
		List<String> issuesDirectlyAssociatedToAMRList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(issuesDirectlyAssociatedToAMRQuery, false);
		logger.info("Issues directly associated to AMR : LIST  :" + issuesDirectlyAssociatedToAMRList);
		logger.info("Issues to be dissociated from AMR : LIST  :" + IssuesToBeDissociatedList);

		for (String eachIssue : issuesDirectlyAssociatedToAMRList)
			if (!IssuesToBeDissociatedList.contains(eachIssue))
				remainingIssuesAssociatedToAMRList.add(eachIssue);

		logger.info("Remaining issues associated to AMR : LIST : " + remainingIssuesAssociatedToAMRList);

		/*
			<p>
			Iterate through IssuesToBeDissociatedList, for each resourceID of the issues in the list
			add them to the issuesAndAEToBeAssociatedList list.
			</p>
		 */
		for(String issueResourceID : IssuesToBeDissociatedList)
			issuesAndAEToBeAssociatedList.add(new Id(issueResourceID));

		logger.info("Final list of issues (resource ID's) to be dissociated: " + issuesAndAEToBeAssociatedList);
		logger.info("STEP 1: Dissociate issue(s) : END");
		logger.info("\n\n");

		// STEP 2
		logger.info("\n\n");
		logger.info("STEP 2: Dissociate DSMT(s) : START");

		// Local Variables
		Map<String, List<String>> dsmtAssociatedToEachIssueMap = new HashMap<>();
		String dsmtSharedByMultipleIssuesQuery;

		/*
			<p>
			STEP 2.1 : Iterate through IssuesToBeDissociatedList, for each issue resourceId in the list.
			Get info about the DSMT'S (baseId, ResourceId, and AEID) associated to the issue by running the query.
			Add the output of the query to Map with baseID as key and value = ResourceId, and AEID.
			</p>
		*/
		for (String issueResourceID : IssuesToBeDissociatedList) {
			String dmtAssociatedToEachIssueQuery = GET_ALL_DSMT_ASSOCIATED_TO_EACH_ISSUE_QUERY.replaceAll(RESOURCE_ID, issueResourceID);
			logger.info("DSMT associated to each issue : QUERY :" + dmtAssociatedToEachIssueQuery);
			dsmtAssociatedToEachIssueMap.putAll(grcObjectSearchUtil.getMultipleValuesFromQueryAsMap(dmtAssociatedToEachIssueQuery, false));
		}
		logger.info("DSMT associated to each issue : MAP :" + dsmtAssociatedToEachIssueMap);

		/*
			<p>
			STEP 2.2 : Iterate through the Map, for each baseID in the map,
			Prepare the query by adding the baseID to it.
			Iterate through the remainingIssuesAssociatedToAMRList list, for each issue resourceID in it,
			Prepare the query by adding the resourceID to it.
			</p>
			<p>
			Once we have the baseID and the resourceID set to the query we run the query to check if the given baseID of DSMT
			is shared by another issue associated to the AMR, if it is shared then we just set the boolean to TRUE and break,
			if it is not then we add the resourceID of those DSMT's to the dsmtsToBeUpdatedList list to set the scope as OUT.
			</p>
		*/
		if (isMapNotNullOrEmpty(dsmtAssociatedToEachIssueMap)) {
			for (String baseIDKey : dsmtAssociatedToEachIssueMap.keySet()) {

				boolean isDSMTFound = false;

				dsmtSharedByMultipleIssuesQuery = GET_ALL_DSMT_SHARED_BY_MULTIPLE_ISSUES_QUERY.replaceAll(BASE_ID, baseIDKey);

				for (String eachRemainingIssue : remainingIssuesAssociatedToAMRList) {

					dsmtSharedByMultipleIssuesQuery = dsmtSharedByMultipleIssuesQuery.replaceAll(RESOURCE_ID, eachRemainingIssue);
					logger.info("Check if the baseID of DSMT is shared by another issue associated to the AMR : QUERY :" + dsmtSharedByMultipleIssuesQuery);
					List<String> dsmtSharedByMultipleIssuesList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(dsmtSharedByMultipleIssuesQuery, false);
					logger.info("BaseID of DSMT's shared by multiple issues associated to the AMR : LIST  :" + dsmtSharedByMultipleIssuesList);

					if (dsmtSharedByMultipleIssuesList.size() > 0) {
						isDSMTFound = true;
						break;
					}
				}

				if (!isDSMTFound) {

					// Query to get the dsmt under AMR with THE above base id
					String resourceIdOfDSMTassociatedToAMRQuery = GET_RESOURCEID_OF_DSMTS_ASSOCIATED_TO_AMR_FOR_GIVEN_BASEID_QUERY.replaceAll(BASE_ID, baseIDKey);
					logger.info("Get resource id of DSMT associated to AMR for given base id : QUERY :" + resourceIdOfDSMTassociatedToAMRQuery);
					String resourceIDOfDSMT = grcObjectSearchUtil.getSingleValueFromQuery(resourceIdOfDSMTassociatedToAMRQuery, false);
					logger.info("Resource id of DSMT associated to AMR for given base id: " + resourceIdOfDSMTassociatedToAMRQuery);

					if (isNotNullOrEmpty(resourceIDOfDSMT))
						dsmtsToBeUpdatedList.add(new Id(resourceIDOfDSMT));

				}

			}
		}
		logger.info("DSMTs for which the scope has to be updated: " + dsmtsToBeUpdatedList);

		// Set the information to dsmtLinkHelperAppInfo bean, and process dissociation of DSMT's
		auditMappingReportInfo.setDsmtLinksToUpdate(dsmtsToBeUpdatedList);
		dsmtLinkHelperAppInfo.setAuditMappingReportInfo(auditMappingReportInfo);
		processUpdateExistingDSMTLinksForAMR(dsmtLinkHelperAppInfo, SCOPE_OUT);

		logger.info("STEP 2: Dissociate DSMT(s) : END");
		logger.info("\n\n");

		// STEP 3
		logger.info("\n\n");
		logger.info("STEP 3: Dissociate AE(s) : START");

		// Local Variables
		List<String> aeIDAssoociatedToAMRList = new ArrayList<>();

		if (isMapNotNullOrEmpty(dsmtAssociatedToEachIssueMap)) {
			for (String baseIDKey : dsmtAssociatedToEachIssueMap.keySet()) {
				List<String> resourceIDandAEIDList = dsmtAssociatedToEachIssueMap.get(baseIDKey);
				aeIDAssoociatedToAMRList.add(resourceIDandAEIDList.get(1));
			}
		}
		logger.info("aeID Assoociated To AMR List : " + aeIDAssoociatedToAMRList);

		/*
			<p>
			iterate the list, for each AE
			check if there is a dsmt under the AMR where the AEID =  the above AEID
			if the above returns a value, then ignore, else add the AEID to the list that has to be dissociated
			</p>
		*/
		if(isListNotNullOrEmpty(aeIDAssoociatedToAMRList)) {
			for(String aeID : aeIDAssoociatedToAMRList) {

				String getDSMTSAssociatedToAMRQuery = null;
				getDSMTSAssociatedToAMRQuery = GET_DSMTS_ASSOCIATED_TO_AMR_FOR_GIVEN_AEID_QUERY.replaceAll(RESOURCE_ID, dsmtLinkHelperAppInfo.getObjectID());
				getDSMTSAssociatedToAMRQuery = getDSMTSAssociatedToAMRQuery.replaceAll(AUDITABLE_ENTITY_ID, aeID);
				logger.info("get DSMTS Associated To AMR Query : " + getDSMTSAssociatedToAMRQuery);
				List<String>  dsmtsAssociatedToAMRList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(getDSMTSAssociatedToAMRQuery, false);
				logger.info("dsmts Associated To AMR List : " + dsmtsAssociatedToAMRList);

				if(isListNullOrEmpty(dsmtsAssociatedToAMRList)) {
					issuesAndAEToBeAssociatedList.add(new Id (aeID));
				}
			}
		}
		logger.info("Final List of Issues and Auditable Entities To be associated : " + issuesAndAEToBeAssociatedList);

		// Set all the information to dsmtLinkHelperAppInfo bean, and dissociate the issues and AE's
		auditMappingReportInfo.setAuditableEntityToAssociate(issuesAndAEToBeAssociatedList);
		dsmtLinkHelperAppInfo.setAuditMappingReportInfo(auditMappingReportInfo);
		processDissociateParents(dsmtLinkHelperAppInfo);

		logger.info("STEP 3: Dissociate AE(s) : END");
		logger.info("\n\n");

		logger.info("BMQS IER dissociateIssues() END");

		return dsmtLinkHelperAppInfo;
	}

}
