/*********************************************************************************************************************
 IBM Confidential OCO Source Materials

 5725-D51, 5725-D52, 5725-D53, 5725-D54

 © Copyright IBM Corporation 2021

 The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 has been deposited with the U.S. Copyright Office.
 */

package com.ibm.openpages.ext.ui.service.impl;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isEqual;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isEqualIgnoreCase;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isListNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isListNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isMapNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isObjectNotNull;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isSetNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.parseCommaDelimitedValues;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IIntegerField;
import com.ibm.openpages.ext.tss.service.IGRCObjectTypeUtil;
import com.ibm.openpages.ext.tss.service.beans.IGRCObjectAssociationInformation;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.AncestorIssueInfo;
import com.ibm.openpages.ext.ui.bean.AuditMappingReportInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkDisableInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkUpdateInfo;
import com.ibm.openpages.ext.ui.bean.DataGridHeaderColumnInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;
import com.ibm.openpages.ext.ui.bean.ExistingDSMTLinkInfo;
import com.ibm.openpages.ext.ui.bean.UpdateDSMTLinkInfo;
import com.ibm.openpages.ext.ui.service.IAuditMappingReportDSMTLinkService;

//TODO format the code, add the licensing at the top.
@Service("auditNonIerAmrServiceImpl")
public class AuditNonIERAMRServiceImpl extends AuditMappingResourceBaseServiceImpl
		implements IAuditMappingReportDSMTLinkService {

	@Autowired
	IServiceFactoryProxy serviceFactoryProxy;

	@Autowired
	IGRCObjectTypeUtil grcObjectTypeUtil;

	@PostConstruct
	public void initServiceImpl() {

		// use ILoggerUtil service in projects
		logger = loggerUtil.getExtLogger(AMR_NON_IER_LOG_FILE_NAME);
	}

	/**
	 * <p>
	 * This method returns a DataGrid with all existing DSMT links grouped by their respective AE-ID
	 * The DSMT's will be the ones associated (CHILD) to the current AMR
	 * </p>
	 */

	@Override
	public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

		logger.info("AMR NON-IER getExistingDSMTLinks() Start");

		// Local Variables
		String amrObjectId= dsmtLinkHelperInfo.getObjectID();
		IGRCObject amrObject = grcObjectUtil.getObjectFromId(amrObjectId);

		// Table Header Variable
		String headerFields = TABLE_HEADER_EXISTING_DSMTS;
		List<String> fieldNamesList = parseCommaDelimitedValues(headerFields);
		List<DataGridHeaderColumnInfo> gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);
		logger.info("Existing DSMT links table header fields : " + gridHeaderInfoList);

		// Query Variable 1 : Get DSMTS for given AMR
		String dsmtForGivenAMRIDQuery = GET_DSMT_LINKS_QUERY.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
		logger.info("Query 1 : Get DSMTS for given AMR : " + dsmtForGivenAMRIDQuery);

		// Query Output Variables
		Map<String, List<ExistingDSMTLinkInfo>> groupedDSMTSMap = new LinkedHashMap<String, List<ExistingDSMTLinkInfo>>();
		List<ExistingDSMTLinkInfo> existingDMSTLinkInfoList;
		
		// Return Variable
		DataGridInfo existingDSMTLinks = new DataGridInfo();

		// Run query 1
		ITabularResultSet resultSet = grcObjectSearchUtil.executeQuery(dsmtForGivenAMRIDQuery, true);

		// Group the result set based on AE ID ( MAP : key - AE ID, Value - list of DSMT's)
		groupedDSMTSMap = groupExistingDSMT(dsmtLinkHelperInfo, resultSet, new ArrayList<>(), amrObject);

		// Format MAP for UI display
		existingDMSTLinkInfoList = formatMapForUIDiasplay(groupedDSMTSMap, amrObjectId);

		// Add table header and row information
		existingDSMTLinks.setHeaders(gridHeaderInfoList);
		existingDSMTLinks.setRows(existingDMSTLinkInfoList);

		logger.info("AMR NON-IER getExistingDSMTLinks() Start");

		return existingDSMTLinks;
	}

	/**
	 * <p>
	 * This method returns all DSMT links grouped by their respective AE-ID and available to be associated to the AMR
	 * This will contain all DSMT links that are associated to ancestor audit, is Active, in Scope, and Status is blank
	 * </p>
	 */

	@Override
	public DataGridInfo getAvailableDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

		logger.info("AMR NON-IER getAvailableDSMTLinks() Start");

		// Local Variables
		String amrObjectId = dsmtLinkHelperInfo.getObjectID();
		logger.info("Audit Mapping Report ID : " + amrObjectId);
		
		// Table Header Variables
		String headerFields = TABLE_HEADER_FIELDS_AVAILABLE_DSMTS;
		List<String> fieldNamesList = parseCommaDelimitedValues(headerFields);
		List<DataGridHeaderColumnInfo> gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);
		logger.info("Available DSMT links table header fields : " + gridHeaderInfoList);

		// Query String Variables
		String ancestorAuditIdQuery = GET_ANCESTOR_AUDIT_IDS_QUERY.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
		String existinDSMTLinkBaseIdQuery = GET_BASE_ID_OF_EXISTING_DSMT_LINKS_QUERY.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
		String associatedAuditableEntityIdQuery = GET_ASSOCIATED_AUDITABLE_ENTITY_ID.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
		String availableDSMTQuery;
		logger.info("Query 1 : Get ancestor audit id's : " + ancestorAuditIdQuery);
		logger.info("Query 3 : Get base id's of existing DSMT's with scope as IN : " + existinDSMTLinkBaseIdQuery);
		logger.info("Query 4 : Get associated auditable entity id : " + associatedAuditableEntityIdQuery);

		// Query Output Variables
		List<String> ancestorAuditIdList = new ArrayList<String>();
		List<String> existinDSMTLinkBaseIdsList;
		Set<String> associatedAuditableEntityIdSet  = new HashSet<String>();
		
		// Processing Output Variables
		Map<String, List<ExistingDSMTLinkInfo>> groupedDSMTSMap = new LinkedHashMap<String, List<ExistingDSMTLinkInfo>>();
		List<ExistingDSMTLinkInfo> existingDMSTLinkInfoList;
		AuditMappingReportInfo auditMappingReportInfo = dsmtLinkHelperInfo.getAuditMappingReportInfo();
		
		// Return Variable
		DataGridInfo availableDSMTLinks = new DataGridInfo();

		// Run query 1
		ancestorAuditIdList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(ancestorAuditIdQuery, true);
		logger.info("Query 1 : Output : " + ancestorAuditIdList);

		// Run query 3
		existinDSMTLinkBaseIdsList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(existinDSMTLinkBaseIdQuery, true);
		logger.info("Query 3 : Output : " + existinDSMTLinkBaseIdsList);

		// Run query 4
		associatedAuditableEntityIdSet.addAll(grcObjectSearchUtil.getMultipleValuesFromQueryAsList(associatedAuditableEntityIdQuery, false));
		logger.info("Query 4 : Output : " + associatedAuditableEntityIdSet);

		if (isListNotNullOrEmpty(ancestorAuditIdList)) {

			for (String auditId : ancestorAuditIdList) {

				availableDSMTQuery = GET_DSMTS_FOR_NON_IER_FOR_GIVEN_ANCESTOR_ID_QUERY.replaceAll(RESOURCE_ID_STR, auditId);
				logger.info("Query 2 : Get available DSMT's associated to ancestor (Audit) : " + availableDSMTQuery);

				// Run query 2
				ITabularResultSet resultSet = grcObjectSearchUtil.executeQuery(availableDSMTQuery, true);

				// Group the result set based on AE ID ( MAP : key - AE ID, Value - list of DSMT's)
				groupedDSMTSMap = groupAvailableDSMT(resultSet, existinDSMTLinkBaseIdsList, associatedAuditableEntityIdSet);

			}

		}

		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo : new AuditMappingReportInfo();
		
		// Set the ancestor audit id's to the list in the bean
		auditMappingReportInfo.setAncestorAuditIdList(ancestorAuditIdList);
		// Set the AMR info bean back to the dsmtHelperInfo bean
		dsmtLinkHelperInfo.setAuditMappingReportInfo(auditMappingReportInfo);

		// Format MAP for UI display
		existingDMSTLinkInfoList = formatMapForUIDiasplay(groupedDSMTSMap, amrObjectId);

		// Add table header and row information
		availableDSMTLinks.setHeaders(gridHeaderInfoList);
		availableDSMTLinks.setRows(existingDMSTLinkInfoList);

		logger.info("getAvailableDSMTLinks() End");

		return availableDSMTLinks;
	}

	/**
	 * <p>
	 * This method returns a DataGrid with issues that are available to be associated to the AMR.
	 * 
	 * Ancestor Audit Id's Query : Gets resource id's of all ancestor audits associated to current AMR.
	 * Base id's Of DSMT'S Associated To AMR Query : Gets base id's of all DSMT'S associated to AMR.
	 * Issues Directly Associated To AMR Query : Gets resource id's of all issues directly associated to AMR.
	 * Issues Associated To Ancestor Audit Query : Gets all info about Issues and DSMT's associated to each ancestor audit id,
	 * from running the very first query.
	 * 
	 * Once we have all the above query results, we call the processIssueList() method.
	 * The arguments that we pass to this method are (resultSet, baseidOfDSMTSAssociatedToAMRList,issuesDirectlyAssociatedToAMRList, finalMap)
	 * The processIssueList method will then return a map with all the issues that are available to be associated with the current AMR and also 
	 * add's the currently associated issues to the end of the end in the map.
	 * 
	 * We get the issues with status = dissociated from the map and add it to a new list, and then get the issues with status = associated from
	 * the same map and add it to the end of the new list.
	 * 
	 * This new list is then added to the data-grid and returned back.
	 * </p>
	 */
	@Override
	public DataGridInfo getAncestorIssues(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

		DataGridInfo issueDataGridInfo = new DataGridInfo();

		logger.info("getAvailableIssueInfo() Start");

		// Local Variables
		int rowCount = 1;

		// Table Header Variables
		String issueTableHeaderString = TABLE_HEADER_FIELDS_FOR_ISSUES;
		List<String> TableHeaderList = parseCommaDelimitedValues(issueTableHeaderString);
		List<DataGridHeaderColumnInfo> gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(TableHeaderList);
		logger.info("Issues Table Header List : " + gridHeaderInfoList);

		// Query String Variables
		String ancestorAuditIdsQuery = GET_ANCESTOR_AUDIT_IDS_QUERY.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
		String baseidOfDSMTSAssociatedToAMRQuery = GET_BASEID_OF_DSMTS_ASSOCIATED_TO_AMR.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
		String issuesDirectlyAssociatedToAMRQuery = GET_ISSUES_DIRECTLY_ASSOCIATED_TO_AMR.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
		String issuesAssociatedToAuditQuery;
		logger.info("Query 1 : Get ancestor audit's : " + ancestorAuditIdsQuery);
		logger.info("Query 3 : Get DSMT's associated to AMR : " + baseidOfDSMTSAssociatedToAMRQuery);
		logger.info("Query 4 : Get issues directly associated to AMR : " + issuesDirectlyAssociatedToAMRQuery);

		// Query Output Variables
		List<String> auditIdList = new ArrayList<String>();
		List<String> baseidOfDSMTSAssociatedToAMRList;
		List<String> issuesDirectlyAssociatedToAMRList;
		List<AncestorIssueInfo> issueInfoList = new ArrayList<AncestorIssueInfo>();
		Map<String, List<AncestorIssueInfo>> finalMap = new HashMap<String, List<AncestorIssueInfo>>();

		// Run Query 1 : Returns a list of ancestor audit id's
		auditIdList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(ancestorAuditIdsQuery, false);
		logger.info("Query 1 : Output: " + auditIdList);

		// Run Query 3 : Returns base id's of DSMT's associated to AMR
		baseidOfDSMTSAssociatedToAMRList = grcObjectSearchUtil
				.getMultipleValuesFromQueryAsList(baseidOfDSMTSAssociatedToAMRQuery, false);
		logger.info("Query 3 : Output: " + baseidOfDSMTSAssociatedToAMRList);

		// Run Query 4 : Returns all issues directly associated to AMR
		issuesDirectlyAssociatedToAMRList = grcObjectSearchUtil
				.getMultipleValuesFromQueryAsList(issuesDirectlyAssociatedToAMRQuery, false);
		logger.info("Query 4 : Output: " + issuesDirectlyAssociatedToAMRList);

		if (isListNotNullOrEmpty(auditIdList)) {

			for (String auditID : auditIdList) {

				issuesAssociatedToAuditQuery = GET_ISSUES_ASSOCITED_TO_AUDIT_QUERY.replaceAll(RESOURCE_ID_STR, auditID);
				logger.info("Query 2 : Get Issues associated to Audit: " + issuesAssociatedToAuditQuery);

				// Run Query 2 : Returns all the issues associated with ancestor audit
				ITabularResultSet resultSet = grcObjectSearchUtil.executeQuery(issuesAssociatedToAuditQuery, true);

				// This method will process the issues and set it to the finalMap and give it back
				finalMap = processIssueList(resultSet, baseidOfDSMTSAssociatedToAMRList,
						issuesDirectlyAssociatedToAMRList, finalMap);

			}

			// Get the issues with status as dissociated and add it to the issueInfo List
			if (isListNotNullOrEmpty(finalMap.get(STATUS_DISSOCIATED)))
				issueInfoList.addAll(finalMap.get(STATUS_DISSOCIATED));

			// Get the issues with status as associated and add it to the issueInfo List
			if (isListNotNullOrEmpty(finalMap.get(STATUS_ASSOCIATED)))
				issueInfoList.addAll(finalMap.get(STATUS_ASSOCIATED));
		}

		// For each item in the list we set a row-count, which is used in the UI to display the issues in the order of the row-count
		for (AncestorIssueInfo eachObject : issueInfoList) {

			eachObject.setId("row-" + rowCount);
			rowCount++;
		}

		logger.info("getAvailableIssueInfo() End");

		issueDataGridInfo.setHeaders(gridHeaderInfoList);
		issueDataGridInfo.setRows(issueInfoList);

		return issueDataGridInfo;

	}

	/**
	 * <p>
	 * This method will create an auto named DSMT Link object, as a child of the AMR and associate the AE's (AE's under which the DSMT was selected) 
	 * to the AMR. If an AE is being associated to the AMR (AE is not already associated) then, Get the grandparent Audit of the AMR 
	 * (AMR -> AuditPhase ->Audit:) check for certain conditions and update the respective counter fields. 
	 * The counterUpdate() method takes care of this task.
	 * </p>
	 */
	@Override
	public DSMTLinkHelperAppInfo addDSMTLinksToAMR(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

		logger.info("addDSMTLinksToAMR() Start");

		// Local Variables
		AuditMappingReportInfo auditMappingReportInfo = dsmtLinkHelperInfo.getAuditMappingReportInfo();
		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo : new AuditMappingReportInfo();
		IGRCObjectAssociationInformation objectAssociationInformation = new IGRCObjectAssociationInformation();
		List<String> ancestorAuditIdList = dsmtLinkHelperInfo.getAuditMappingReportInfo().getAncestorAuditIdList();
		IGRCObject amrObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
		List<Id> auditableEntityToAssociate;

		// Will set the list of scoped in dsmt's and map of scoped out dsmt's to DSMTLinkHelperAppInfo bean.
		dsmtLinkHelperInfo = getDSMTBaseIdInfoForAMR(dsmtLinkHelperInfo);
		
		// Will check if a DSMT has to be copied or updated
		dsmtLinkHelperInfo = processDSMTsToCopyOrUpdate(dsmtLinkHelperInfo);

		// will look for DSMT's that has to be copied, and copies it and associates it to the AMR
		processAddNewDSMTLinksToAMR(dsmtLinkHelperInfo);
		
		// will look for DSMT's that are already associated to the AMR and updates its scope value 
		processUpdateExistingDSMTLinksForAMR(dsmtLinkHelperInfo, SCOPE_IN);

		auditableEntityToAssociate = auditMappingReportInfo.getAuditableEntityToAssociate();

		logger.info("Final List of Auditable Entities to associate: " + auditableEntityToAssociate);
		if (isListNotNullOrEmpty(auditableEntityToAssociate)) {

			objectAssociationInformation.setSecondaryParentAssociationList(auditableEntityToAssociate);
			counterUpdate(FLAG_ASSOCIATE, ancestorAuditIdList, auditableEntityToAssociate);
			grcObjectUtil.associateParentAndChildrentToAnObject(amrObject, objectAssociationInformation);
		}

		logger.info("addDSMTLinksToAMR() End");

		return dsmtLinkHelperInfo;
	}

	/**
	 * This method will update the scope of a DSMT to OUT and also
	 * check every AE associated to the AMR, and it should have at least one HOME DSMT that has Scope as IN and Active as YES and Status as BLANK.
	 * If no such DSMT is present in the AE then the AE has to be dissociated from the AMR.
	 */
	@Override
	public DSMTLinkHelperAppInfo descopeDSMTLinksFromAMR(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception {

		logger.info("descopeDSMTLinksFromAMR() START");
		
		// AMR Object Variable
		IGRCObject amrObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());
		logger.info("AMR Object Resource Id : " + dsmtLinkHelperAppInfo.getObjectID());
		
		// List of DSMT's to De-scope Variable
		List<DSMTLinkUpdateInfo> dsmtLinksToDescopeList = dsmtLinkHelperAppInfo.getDsmtLinkUpdateInfo();
		logger.info("List of DSMT's to DeScope : " + dsmtLinksToDescopeList);
		
		// Secondary Parent Association Variables
		IGRCObjectAssociationInformation objectAssociationInfo;
		List<Id> disassociateParentIdList;
		List<String> ancestorAuditIdList = dsmtLinkHelperAppInfo.getAuditMappingReportInfo().getAncestorAuditIdList();

		// Check if the dsmtLinksToDescopeList is not null or empty
		if (isListNotNullOrEmpty(dsmtLinksToDescopeList)) {

			// Iterate through the list of De-scoping items. The items are grouped by the AE Id.
			for (DSMTLinkUpdateInfo descopeDSMTInfo : dsmtLinksToDescopeList) {

				// Will set the appropriate value to the scope field in the object
				descopeDSMTs(descopeDSMTInfo);

				// TODO: check with suruchi if the extra child association ifcheck has to be added or not.

				// If there is a AE that has to be dissociated then it will be done here
				if (descopeDSMTInfo.isDescopeParentAE()) {

					logger.info("Auditable entity has to be dissociated : " + descopeDSMTInfo.isDescopeParentAE());
					logger.info("Auditable entity ID : " + descopeDSMTInfo.getAuditableEntityId());

					disassociateParentIdList = new ArrayList<>();
					objectAssociationInfo = new IGRCObjectAssociationInformation();
					disassociateParentIdList.add(new Id(descopeDSMTInfo.getAuditableEntityId()));
					objectAssociationInfo.setSecondaryParentAssociationList(disassociateParentIdList);

					logger.info("Disassociating the issue from AE: " + objectAssociationInfo);
					counterUpdate(FLAG_DISSOCIATE, ancestorAuditIdList, disassociateParentIdList);
					grcObjectUtil.disassociateParentAndChildrentFromAnObject(amrObject, objectAssociationInfo);
					grcObjectUtil.saveResource(amrObject);
				}
			}
		}

		logger.info("descopeDSMTLinksFromAMR() END");
		return dsmtLinkHelperAppInfo;
	}

	/**
	 * <p>
	 * This method associates or dissociates issues from the current AMR
	 * First  : Get a list of issues directly associated to current AMR
	 * Second : Get list of issue id's that has to be updated from DSMTLinkHelperAppInfo
	 * Third  : Iterate through the above list, and for each item, check 
	 * 
	 * If status is Dissociate and issuesDirectlyAssociatedToAMR list contains the same issue : the we dissociate that issue from AMR
	 * If status is Associate and issuesDirectlyAssociatedToAMR list contains the same issue : the we associate that issue from AMR
	 * Finally save all changes we made.
	 * </p>
	 * 
	 */
	public DSMTLinkHelperAppInfo processScopeIssuesFromAMR(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
			throws Exception {

		logger.info("processScopeIssuesFromAMR() START");

		// Local Variables
		List<DSMTLinkUpdateInfo> dsmtLinkUpdateInfo = dsmtLinkHelperAppInfo.getDsmtLinkUpdateInfo();
		List<ExistingDSMTLinkInfo> issueIdsToUpdateList = new ArrayList<ExistingDSMTLinkInfo>();

		// Query Variable
		String issuesDirectlyAssociatedToAMRQuery = GET_ISSUES_DIRECTLY_ASSOCIATED_TO_AMR.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperAppInfo.getObjectID());
		logger.info("Query : Issues directly associated to current AMR : " + issuesDirectlyAssociatedToAMRQuery);
		
		// Query Output Variable
		List<String> issuesDirectlyAssociatedToAMR = new ArrayList<String>();
		
		// Secondary Parent Association Variables
		List<Id> issueIdList;
		IGRCObjectAssociationInformation objectAssociationInfo;
		IGRCObject amrObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());

		// Run Query
		issuesDirectlyAssociatedToAMR = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(issuesDirectlyAssociatedToAMRQuery, false);
		logger.info("Query Output: Issues directly associated to current AMR : " + issuesDirectlyAssociatedToAMR);

		
		// Iterate through the dsmtLinkUpdateInfo, get the list of Issue id's that needs to be updated
		for (DSMTLinkUpdateInfo eachItem : dsmtLinkUpdateInfo) {

			issueIdsToUpdateList = eachItem.getDsmtLinkIdsToUpdate();
			logger.info("List of issues to update : " + issueIdsToUpdateList);

		}

		
		// Iterate through issueIdsToUpdateList and get the issues to update
		for (ExistingDSMTLinkInfo eachItem : issueIdsToUpdateList) {

			logger.info("ID of the issue, to be updated : " + eachItem.getResourceId());
			logger.info("Scope value of the issue, to be updated : " + eachItem.getScope());

			issueIdList = new ArrayList<>();
			objectAssociationInfo = new IGRCObjectAssociationInformation();
			issueIdList.add(new Id(eachItem.getResourceId()));
			objectAssociationInfo.setSecondaryParentAssociationList(issueIdList);

			logger.info("Disassociating the issue from AE: " + objectAssociationInfo);

			if (isEqual(eachItem.getScope(), STATUS_DISSOCIATED)
					&& issuesDirectlyAssociatedToAMR.contains(eachItem.getResourceId()))
				
				grcObjectUtil.disassociateParentAndChildrentFromAnObject(amrObject, objectAssociationInfo);

			if (isEqual(eachItem.getScope(), STATUS_ASSOCIATED)
					&& !issuesDirectlyAssociatedToAMR.contains(eachItem.getResourceId()))
				
				grcObjectUtil.associateParentAndChildrentToAnObject(amrObject, objectAssociationInfo);

			grcObjectUtil.saveResource(amrObject);

		}

		logger.info("processScopeIssuesFromAMR() END");

		return dsmtLinkHelperAppInfo;

	}
	
	/**
	 * This method has not been implemented in this case
	 */
	@Override
	public DataGridInfo searchDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

	private Map<String, List<ExistingDSMTLinkInfo>> groupAvailableDSMT(ITabularResultSet resultSet,
			List<String> existinDSMTLinkBaseIdsList, Set<String> associatedAuditableEntityIdSet) throws Exception {

		logger.info("groupAvailableDSMT() Start");

		// Local Variable
		ExistingDSMTLinkInfo existingDSMTLinkInfo = new ExistingDSMTLinkInfo();
		Map<String, List<ExistingDSMTLinkInfo>> groupedDSMTMap = new LinkedHashMap<String, List<ExistingDSMTLinkInfo>>();
		List<ExistingDSMTLinkInfo> dsmtList;

		// Initialize variables
		existingDSMTLinkInfo = null;
		dsmtList = null;

		for (IResultSetRow row : resultSet) {

			String auditableEntityId = fieldUtil.getFieldValueAsString(row.getField(0));
			String dsmtBaseId = fieldUtil.getFieldValueAsString(row.getField(7));

			logger.info("Auditable Entity ID : " + auditableEntityId);
			logger.info("DSMT Base ID : " + dsmtBaseId);
			logger.info("Base id is null or empty : " + isNullOrEmpty(dsmtBaseId));
			logger.info("Existing dsmt link base id List is null or empty : "
					+ isListNullOrEmpty(existinDSMTLinkBaseIdsList));
			logger.info("Is list not null or empty && not contain dsmt base id : "
					+ (isListNotNullOrEmpty(existinDSMTLinkBaseIdsList)
							&& !existinDSMTLinkBaseIdsList.contains(dsmtBaseId)));
			logger.info("Is set not null or empty && not contain AE id : : "
					+ (isSetNotNullOrEmpty(associatedAuditableEntityIdSet)
							&& !associatedAuditableEntityIdSet.contains(auditableEntityId)));
			logger.info("AE id is not null or empty : " + isNotNullOrEmpty(auditableEntityId));

			if ((isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(existinDSMTLinkBaseIdsList)
					|| ((isListNotNullOrEmpty(existinDSMTLinkBaseIdsList)
							&& !existinDSMTLinkBaseIdsList.contains(dsmtBaseId))
							|| (isObjectNotNull(associatedAuditableEntityIdSet)
									&& !associatedAuditableEntityIdSet.contains(auditableEntityId))))
					&& isNotNullOrEmpty(auditableEntityId)) {

				dsmtList = groupedDSMTMap.get(auditableEntityId);

				dsmtList = (isListNullOrEmpty(dsmtList)) ? new ArrayList<ExistingDSMTLinkInfo>() : dsmtList;

				existingDSMTLinkInfo = new ExistingDSMTLinkInfo();

				existingDSMTLinkInfo.setParentResourceId(auditableEntityId);
				existingDSMTLinkInfo.setResourceId(fieldUtil.getFieldValueAsString(row.getField(1)));
				existingDSMTLinkInfo.setName(fieldUtil.getFieldValueAsString(row.getField(2)));
				existingDSMTLinkInfo.setType(fieldUtil.getFieldValueAsString(row.getField(3)));
				existingDSMTLinkInfo.setManagedsegmentname(fieldUtil.getFieldValueAsString(row.getField(4)));
				existingDSMTLinkInfo.setManagedgeographyname(fieldUtil.getFieldValueAsString(row.getField(5)));
				existingDSMTLinkInfo.setLegalvehiclename(fieldUtil.getFieldValueAsString(row.getField(6)));
				existingDSMTLinkInfo.setBaseId(fieldUtil.getFieldValueAsString(row.getField(7)));

				dsmtList.add(existingDSMTLinkInfo);

				groupedDSMTMap.put(auditableEntityId, dsmtList);

			}

		}

		logger.info("groupAvailableDSMT() End");
		return groupedDSMTMap;

	}
	

	private Map<String, List<ExistingDSMTLinkInfo>> groupExistingDSMT(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
			ITabularResultSet resultSet, List<String> existinDSMTLinkBaseIdsList, IGRCObject amrObject)
			throws Exception {

		logger.info("groupByAuditibleEntityForExistingDSMT() Start");

		// Local Variable
		DSMTLinkDisableInfo dsmtLinkDisableInfo;
		ExistingDSMTLinkInfo existingDSMTLinkInfo = new ExistingDSMTLinkInfo();
		Map<String, List<ExistingDSMTLinkInfo>> groupedDSMTMap = new LinkedHashMap<String, List<ExistingDSMTLinkInfo>>();
		List<ExistingDSMTLinkInfo> dsmtList;
		List<String> parentAEIdList;

		// Initialize variables
		existingDSMTLinkInfo = null;
		dsmtList = null;

		parentAEIdList = getParentObjectsIdsOfTypeAuditableEntity(amrObject, AUDITABLE_ENTITY);

		for (IResultSetRow row : resultSet) {

			String auditableEntityId = fieldUtil.getFieldValueAsString(row.getField(0));
			String dsmtBaseId = fieldUtil.getFieldValueAsString(row.getField(7));

			logger.info("Auditable Entity ID : " + auditableEntityId);
			logger.info("DSMT Base ID : " + dsmtBaseId);
			logger.info("Is AE id in parent AE id list ? " + parentAEIdList.contains(auditableEntityId));
			logger.info(
					"If check returns : " + ((isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(existinDSMTLinkBaseIdsList)
							|| (isListNotNullOrEmpty(existinDSMTLinkBaseIdsList)
									&& !existinDSMTLinkBaseIdsList.contains(dsmtBaseId)))
							&& isNotNullOrEmpty(auditableEntityId)));

			// we are checking if the AE resource id is in the list of parent AE Id
			if (parentAEIdList.contains(auditableEntityId)) {

				if ((isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(existinDSMTLinkBaseIdsList)
						|| (isListNotNullOrEmpty(existinDSMTLinkBaseIdsList)
								&& !existinDSMTLinkBaseIdsList.contains(dsmtBaseId)))
						&& isNotNullOrEmpty(auditableEntityId)) {

					dsmtList = groupedDSMTMap.get(auditableEntityId);

					dsmtList = (isListNullOrEmpty(dsmtList)) ? new ArrayList<ExistingDSMTLinkInfo>() : dsmtList;

					existingDSMTLinkInfo = new ExistingDSMTLinkInfo();

					existingDSMTLinkInfo.setParentResourceId(auditableEntityId);
					existingDSMTLinkInfo.setResourceId(fieldUtil.getFieldValueAsString(row.getField(1)));
					existingDSMTLinkInfo.setName(fieldUtil.getFieldValueAsString(row.getField(2)));
					existingDSMTLinkInfo.setType(fieldUtil.getFieldValueAsString(row.getField(3)));
					existingDSMTLinkInfo.setManagedsegmentname(fieldUtil.getFieldValueAsString(row.getField(4)));
					existingDSMTLinkInfo.setManagedgeographyname(fieldUtil.getFieldValueAsString(row.getField(5)));
					existingDSMTLinkInfo.setLegalvehiclename(fieldUtil.getFieldValueAsString(row.getField(6)));
					existingDSMTLinkInfo.setBaseId(fieldUtil.getFieldValueAsString(row.getField(7)));
					existingDSMTLinkInfo.setDescription(fieldUtil.getFieldValueAsString(row.getField(8)));
					existingDSMTLinkInfo.setScope(fieldUtil.getFieldValueAsString(row.getField(9)));
					existingDSMTLinkInfo.setActive(fieldUtil.getFieldValueAsString(row.getField(10)));

					logger.info("Scope Value : " + existingDSMTLinkInfo.getScope());
					logger.info("Active / Valid : " + existingDSMTLinkInfo.getActive());
					logger.info("Resource Id : " + existingDSMTLinkInfo.getResourceId());

					existingDSMTLinkInfo = dsmtLinkServiceUtil.processValidDSMTLink(existingDSMTLinkInfo);

					// DO the check here
					processHigherOrderDependency(dsmtLinkHelperInfo, existingDSMTLinkInfo);

					dsmtLinkDisableInfo = existingDSMTLinkInfo.getDsmtLinkDisableInfo();
					dsmtLinkDisableInfo = isObjectNotNull(dsmtLinkDisableInfo) ? dsmtLinkDisableInfo
							: new DSMTLinkDisableInfo();

					existingDSMTLinkInfo.setDisabled(isSetNotNullOrEmpty(dsmtLinkDisableInfo.getDisableInfoList())
							|| isSetNotNullOrEmpty(dsmtLinkDisableInfo.getLowerLevelDepObjectsList()));

					dsmtList.add(existingDSMTLinkInfo);

					groupedDSMTMap.put(auditableEntityId, dsmtList);

				}

			}

		}

		logger.info("groupByAuditibleEntityForExistingDSMT() End");
		return groupedDSMTMap;

	}

	private List<ExistingDSMTLinkInfo> formatMapForUIDiasplay(Map<String, List<ExistingDSMTLinkInfo>> groupedByIssueId,
			String amrObjectId) throws Exception {

		logger.info("formatMapForUIDiasplay() Start");

		// Local variables
		int childCount;
		int rowCount = 1;
		List<ExistingDSMTLinkInfo> childDMSTLinkInfoList = null;
		List<ExistingDSMTLinkInfo> finalIssuesAndDsmtsList = null;
		List<String> hierarchyList;
		IGRCObject auditEntityObject;
		String auditEntityName;
		String auditEntityDesc;

		ExistingDSMTLinkInfo existingDSMTLinkInfo = new ExistingDSMTLinkInfo();

		// check if the map is not null or empty
		if (isMapNotNullOrEmpty(groupedByIssueId)) {

			logger.info("Is the map not null or empty : " + isMapNotNullOrEmpty(groupedByIssueId));

			String getResourceIdOfIssueWithDsmtAsChildQuery;
			List<String> issuesAssociatedToAmrList;
			List<String> resourceIdOfIssueWithDsmtAsChildList;
			List<String> issueObjectName;

			issuesAssociatedToAmrList = getResourceIdOfParentIssuesAssociatedToAMR(amrObjectId);

			finalIssuesAndDsmtsList = isObjectNotNull(finalIssuesAndDsmtsList) ? finalIssuesAndDsmtsList
					: new ArrayList<>();

			// Iterate through the map, and set the ID and Hierarchy
			for (String auditEntityID : groupedByIssueId.keySet()) {

				logger.info("AuditEntity ID: : " + auditEntityID);

				childCount = 1;
				hierarchyList = new ArrayList<>();
				existingDSMTLinkInfo = new ExistingDSMTLinkInfo();
				auditEntityObject = grcObjectUtil.getObjectFromId(auditEntityID);
				childDMSTLinkInfoList = groupedByIssueId.get(auditEntityID);
				auditEntityName = auditEntityObject.getName();
				auditEntityDesc = auditEntityObject.getDescription();

				logger.info("AuditEntity Name: : " + auditEntityName);
				logger.info("AuditEntity Description: : " + auditEntityDesc);

				hierarchyList.add(auditEntityName);
				existingDSMTLinkInfo.setId("row-" + rowCount);
				existingDSMTLinkInfo.setResourceId(auditEntityID);
				existingDSMTLinkInfo.setDescription(auditEntityDesc);
				existingDSMTLinkInfo.setName(auditEntityName + " - " + auditEntityDesc);
				existingDSMTLinkInfo.setHasChildren(isListNotNullOrEmpty(childDMSTLinkInfoList));
				existingDSMTLinkInfo.setHierarchy(hierarchyList);

				if (isListNotNullOrEmpty(childDMSTLinkInfoList)) {

					logger.info("Is list not null or empty : " + isListNotNullOrEmpty(childDMSTLinkInfoList));

					for (ExistingDSMTLinkInfo childDMSTLinkInfo : childDMSTLinkInfoList) {

						hierarchyList = new ArrayList<>();
						issueObjectName = new ArrayList<>();
						hierarchyList.add(auditEntityName);
						hierarchyList.add(childDMSTLinkInfo.getName());
						childDMSTLinkInfo.setId("row-" + rowCount + "-child-" + childCount);
						childDMSTLinkInfo.setHierarchy(hierarchyList);

						if (!isNullOrEmpty(childDMSTLinkInfo.getBaseId())) {

							// Query Setup :
							getResourceIdOfIssueWithDsmtAsChildQuery = GET_RESOURCE_ID_OF_ISSUE_WITH_DSMT_AS_CHILD
									.replaceAll(RESOURCE_ID_STR, childDMSTLinkInfo.getBaseId());
							logger.info("Get resource id of issue with DSMT as child : Query : "
									+ getResourceIdOfIssueWithDsmtAsChildQuery);

							// Adding the ancestorIssues information to the bean.
							// Run Query : Returns the resource id of the issue
							resourceIdOfIssueWithDsmtAsChildList = grcObjectSearchUtil
									.getMultipleValuesFromQueryAsList(getResourceIdOfIssueWithDsmtAsChildQuery, false);
							logger.info("Query Output : " + resourceIdOfIssueWithDsmtAsChildList);

							if (isListNotNullOrEmpty(resourceIdOfIssueWithDsmtAsChildList)) {

								// For each resource id in the list check if it is in issuesAssociatedToAmrList
								for (String resourceId : resourceIdOfIssueWithDsmtAsChildList) {

									logger.info("Resource Id of issues with DSMT as child : " + resourceId);
									logger.info("Is Resource Id in, issues associated to AMR list? : "
											+ issuesAssociatedToAmrList.contains(resourceId));

									if (issuesAssociatedToAmrList.contains(resourceId)) {

										issueObjectName.add(grcObjectUtil.getObjectFromId(resourceId).getName());

									}
								}

							}

							logger.info("Issue Object Name list : " + issueObjectName);
							childDMSTLinkInfo.setAncestorIssues(issueObjectName);

						}

						childCount++;
						finalIssuesAndDsmtsList.add(childDMSTLinkInfo);
					}
				}

				rowCount++;
				finalIssuesAndDsmtsList.add(existingDSMTLinkInfo);

				logger.info("Hierarchy List : " + hierarchyList);
				logger.info("Final issue and DSMT List : " + finalIssuesAndDsmtsList);

			}

		}

		logger.info("formatMapForUIDiasplay() END");

		return finalIssuesAndDsmtsList;

	}

	/**
	 * <p>
	 * This method will decide which DSMT' has to be copied and which has to be updated, set the information to dsmtLinkHelperAppInfo and return it.
	 * 
	 * From the DSMTLinkHelperAppInfo bean get auditMappingReportInfo bean, and from auditMappingReportInfo bean we get the following
	 * 1 : get scoped in DSMT's List
	 * 2 : get scoped out DSMT's Map (from the map get only the baseId's)
	 * 3 : get parentAEIdList by calling another method (getParentObjectsIdsOfTypeAuditableEntity)
	 * 4 : get udpateDsmtLinkInfo List
	 * 
	 * First  : For each item in the udpateDsmtLinkInfo List, get the AE id and check if it is not in the parentAEIdList, if it is not then we add that AE ID
	 * 			to auditableEntityToAssociate List.
	 * 
	 * Second : get DsmtLinkIdsToUpdate list from udpateDsmtLinkInfo, for each ID in it, get the respective object with the ID and get the 
	 * 			BaseId value from its field.
	 * 
	 * Third  : if the BaseId we get from the above step is NOT in scoped in DSMT's List and NOT in scoped out DSMT's List, 
	 * 			then that id will be added to dsmtLinksToCopy List.
	 * 
	 * Fourth : if the BaseId we get from the above step is in the scoped out DSMT's List then that id will be added to dsmtLinksToUpdate List
	 * 
	 * @param dsmtLinkHelperAppInfo
	 * @return
	 * @throws Exception
	 */
	private DSMTLinkHelperAppInfo processDSMTsToCopyOrUpdate(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
			throws Exception {

		logger.info("processDSMTsToCopyOrUpdate() Start");

		// Local Variables
		IGRCObject amrObject;
		AuditMappingReportInfo auditMappingReportInfo;

		Map<String, String> scopedOutDSMTBaseIdInfo;
		Set<String> scopedInDSMTBaseIdList;
		Set<String> scopedOutDSMTBaseIdList;
		List<String> parentAEIdList;

		List<Id> auditableEntityToAssociate;
		List<Id> dsmtLinksToCopy;
		List<Id> dsmtLinksToUpdate;

		// Initialize Variables
		auditMappingReportInfo = dsmtLinkHelperAppInfo.getAuditMappingReportInfo();
		amrObject = null;

		auditableEntityToAssociate = new ArrayList<>();
		dsmtLinksToCopy = new ArrayList<>();
		dsmtLinksToUpdate = new ArrayList<>();

		// Check if the AMRInfo bean is null or not
		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo
				: new AuditMappingReportInfo();

		// Get scoped in DSMT's Base Id list from the AMRInfo bean
		scopedInDSMTBaseIdList = auditMappingReportInfo.getScopedInDSMTBaseIdList();
		scopedInDSMTBaseIdList = isSetNotNullOrEmpty(scopedInDSMTBaseIdList) ? scopedInDSMTBaseIdList : new HashSet<>();

		// Get scoped out DSMT's info Map from the AMRInfo bean, and add the key set of
		// the map to a new list
		scopedOutDSMTBaseIdInfo = auditMappingReportInfo.getScopedOutDSMTInfoMap();
		scopedOutDSMTBaseIdInfo = isMapNotNullOrEmpty(scopedOutDSMTBaseIdInfo) ? scopedOutDSMTBaseIdInfo
				: new HashMap<>();
		scopedOutDSMTBaseIdList = scopedOutDSMTBaseIdInfo.keySet();

		// Get the AMR Object using Id from the helper app info and set it here
		amrObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());


		parentAEIdList = getParentObjectsIdsOfTypeAuditableEntity(amrObject, AUDITABLE_ENTITY);

		parentAEIdList = isListNotNullOrEmpty(parentAEIdList) ? parentAEIdList : new ArrayList<>();
		logger.info("Parent Auditable Entities: " + parentAEIdList);

		// Get Update Dsmt LinkInfo List and iterate through each item in the list
		for (UpdateDSMTLinkInfo udpateDsmtLinkInfo : dsmtLinkHelperAppInfo.getUpdateDsmtLinkInfo()) {

			logger.info("Parent Auditable Entity Not already associated: "
					+ (!parentAEIdList.contains(udpateDsmtLinkInfo.getAuditableEntityId())));

			// if the AE ID from udpateDsmtLinkInfo is not in parentAEIdList, then add the AE ID to auditableEntityToAssociate List
			if (!parentAEIdList.contains(udpateDsmtLinkInfo.getAuditableEntityId())) {
				
				auditableEntityToAssociate.add(new Id(udpateDsmtLinkInfo.getAuditableEntityId()));
			}

			if (isListNotNullOrEmpty(udpateDsmtLinkInfo.getDsmtLinkIdsToUpdate())) {

				// For each DSMT link ID in the list
				for (String dsmtLinkId : udpateDsmtLinkInfo.getDsmtLinkIdsToUpdate()) {

					// Get the respective object using the ID
					IGRCObject dsmtLinkObjectToCopy = grcObjectUtil.getObjectFromId(dsmtLinkId);

					// From the object get its Base Id
					String baseId = fieldUtil.getFieldValueAsString(dsmtLinkObjectToCopy, DSMT_LINK_BASE_ID_FIELD);
					logger.info("Base Id: " + baseId + " For DSMT Link id: " + dsmtLinkId);

					if (isNotNullOrEmpty(baseId)) {

						logger.info("Is DSMT Link with Base Id not present: " + (!scopedInDSMTBaseIdList.contains(baseId)
								&& !scopedOutDSMTBaseIdList.contains(baseId)));

						logger.info("Is DSMT Link with Base Id present but inactive: " + (scopedOutDSMTBaseIdList.contains(baseId)));

						// If the base Id is not in Scoped-In or Scoped-Out List then we add the DSMT ID to dsmtLinksToCopy list
						if (!scopedInDSMTBaseIdList.contains(baseId) && !scopedOutDSMTBaseIdList.contains(baseId)) {

							dsmtLinksToCopy.add(new Id(dsmtLinkId));
						}
						
						// If the base Id is in Scoped-Out List then we add the DSMT ID to dsmtLinksToUpdate list
						else if (scopedOutDSMTBaseIdList.contains(baseId)) {

							dsmtLinksToUpdate.add(new Id(scopedOutDSMTBaseIdInfo.get(baseId)));
						}
					}
				}
			}

		}

		auditMappingReportInfo.setDsmtLinksToCopy(dsmtLinksToCopy);
		auditMappingReportInfo.setDsmtLinksToUpdate(dsmtLinksToUpdate);
		auditMappingReportInfo.setAuditableEntityToAssociate(auditableEntityToAssociate);
		dsmtLinkHelperAppInfo.setAuditMappingReportInfo(auditMappingReportInfo);

		logger.info("processDSMTsToCopyOrUpdate() End");

		return dsmtLinkHelperAppInfo;

	}	

	private Map<String, List<AncestorIssueInfo>> processIssueList(ITabularResultSet resultSet,
			List<String> baseidOfDSMTSAssociatedToAMRList, List<String> issuesDirectlyAssociatedToAMRList,
			Map<String, List<AncestorIssueInfo>> finalMap) throws Exception {

		logger.info("processIssueList() Start");
		logger.info("Final Map: " + finalMap);

		// Local variables
		AncestorIssueInfo ancestorIssueInfo;
		List<String> duplicateIssueList;
		List<AncestorIssueInfo> associatedIssuesList;
		List<AncestorIssueInfo> disassociatedIssuesList;

		// Initialize Variables
		ancestorIssueInfo = null;
		duplicateIssueList = new ArrayList<String>();
		associatedIssuesList = finalMap.get(STATUS_ASSOCIATED);
		disassociatedIssuesList = finalMap.get(STATUS_DISSOCIATED);

		associatedIssuesList = (isListNotNullOrEmpty(associatedIssuesList)) ? associatedIssuesList
				: new ArrayList<AncestorIssueInfo>();

		disassociatedIssuesList = (isListNotNullOrEmpty(disassociatedIssuesList)) ? disassociatedIssuesList
				: new ArrayList<AncestorIssueInfo>();

		// Log all initialized variables
		logger.info("List of issues associated to AMR : " + issuesDirectlyAssociatedToAMRList);
		logger.info("List 1: Associated Issues List : " + associatedIssuesList);
		logger.info("List 2: Disassociated Issues List : " + disassociatedIssuesList);

		for (IResultSetRow row : resultSet) {

			logger.info("Processing Issues List : ");
			logger.info("Checking if the resultSet of Query 2 contains the current base id : "
					+ (baseidOfDSMTSAssociatedToAMRList.contains(fieldUtil.getFieldValueAsString(row.getField(5)))));
			logger.info("Checking if the issue resource id already exist : "
					+ (duplicateIssueList.contains(fieldUtil.getFieldValueAsString(row.getField(0)))));

			// Check if the DSMT's base id from the resultSet is also in List of based id's
			if (baseidOfDSMTSAssociatedToAMRList.contains(fieldUtil.getFieldValueAsString(row.getField(5)))
					&& !duplicateIssueList.contains(fieldUtil.getFieldValueAsString(row.getField(0)))) {

				ancestorIssueInfo = new AncestorIssueInfo();
				duplicateIssueList.add(fieldUtil.getFieldValueAsString(row.getField(0)));
				ancestorIssueInfo.setResourceId(fieldUtil.getFieldValueAsString(row.getField(0)));
				ancestorIssueInfo.setName(fieldUtil.getFieldValueAsString(row.getField(1)));
				ancestorIssueInfo.setTitle(fieldUtil.getFieldValueAsString(row.getField(2)));
				ancestorIssueInfo.setIssuelevel(fieldUtil.getFieldValueAsString(row.getField(3)));

				logger.info("Is issue " + ancestorIssueInfo.getResourceId() + " already associated to AMR: "
						+ issuesDirectlyAssociatedToAMRList.contains(fieldUtil.getFieldValueAsString(row.getField(0))));

				if (issuesDirectlyAssociatedToAMRList.contains(fieldUtil.getFieldValueAsString(row.getField(0)))) {

					ancestorIssueInfo.setAssociationstatus(STATUS_ASSOCIATED);
					associatedIssuesList.add(ancestorIssueInfo);
					logger.info("Adding to associated issues list");
					logger.info("Status value from the bean : " + ancestorIssueInfo.getAssociationstatus());

				} else {

					ancestorIssueInfo.setAssociationstatus(STATUS_DISSOCIATED);
					disassociatedIssuesList.add(ancestorIssueInfo);
					logger.info("Adding to disassociated issues list");
					logger.info("Status value from the bean : " + ancestorIssueInfo.getAssociationstatus());

				}

			}

		}

		finalMap.put(STATUS_ASSOCIATED, associatedIssuesList);
		finalMap.put(STATUS_DISSOCIATED, disassociatedIssuesList);

		logger.info("Processed Issue Map : " + finalMap);
		logger.info("processIssueList() End");

		return finalMap;
	}

	private void descopeDSMTs(DSMTLinkUpdateInfo descopeDSMTInfo) throws Exception {

		logger.info("descopeDSMTs() Start");

		IGRCObject dsmtLinkObject;
		Map<String, String> dsmtFieldsUpdateInfoMap;

		logger.info("Is DSMT linkes available to update: "
				+ isListNotNullOrEmpty(descopeDSMTInfo.getDsmtLinkIdsToUpdate()));

		if (isListNotNullOrEmpty(descopeDSMTInfo.getDsmtLinkIdsToUpdate())) {

			logger.info("Going to iterate over total dsmts: " + descopeDSMTInfo.getDsmtLinkIdsToUpdate().size());

			// Iterate through the list of DSMT's to descope
			for (ExistingDSMTLinkInfo descopeDSMT : descopeDSMTInfo.getDsmtLinkIdsToUpdate()) {

				/*
				<p>
				This list will have all the DSMT's that has to be dissociated, also the AE id in it.
				We will have to filter out only the DSMT's and for that we will have to check if each row item has a
				parentRResourceID or not. if parentResourceID is null then it is an AE, and we should exclude
				it from the list.
				 */

				logger.info("Resource id of DSMT: " + descopeDSMT.getResourceId());
				logger.info("Parent resource id of DSMT: " + descopeDSMT.getParentResourceId());
				logger.info("If parent resource id is NULL then it is an AE");

				if(isNotNullOrEmpty(descopeDSMT.getParentResourceId())) {

					dsmtFieldsUpdateInfoMap = new HashMap<>();
					dsmtFieldsUpdateInfoMap.put(DSMT_LINK_SCOPE_FIELD, descopeDSMT.getScope());
					dsmtLinkObject = grcObjectUtil.getObjectFromId(descopeDSMT.getResourceId());
					grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, dsmtFieldsUpdateInfoMap);

				}
			}
		}

		logger.info("descopeDSMTs() End");
	}
	

	private ExistingDSMTLinkInfo processHigherOrderDependency(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
			ExistingDSMTLinkInfo dsmtLinkObjectInfo) throws Exception {

		logger.info("processHigherLevelDependencyForDSMTLink() Start");

		// Method Level Variables.
		boolean isDSMTLinkOutOfScope;
		boolean isHigherLevelDep = false;

		String baseId;
		Set<String> parentObjectIdSet = new HashSet<String>();
		List<String> temp;

		isDSMTLinkOutOfScope = isEqualIgnoreCase(SCOPE_OUT, dsmtLinkObjectInfo.getScope());
		baseId = dsmtLinkObjectInfo.getBaseId();

		logger.info("Is DSMT Link Out of Scope : " + isDSMTLinkOutOfScope);
		logger.info("DSMT Base Id : " + baseId);

		/*
		 * Check if the DSMT Link is active and if the DSMT Link is out of scope only
		 * then proceed
		 */
		if (isDSMTLinkOutOfScope) {

			temp = dsmtLinkHelperInfo.getAuditMappingReportInfo().getAncestorAuditIdList();
			if(isListNotNullOrEmpty(temp))
			parentObjectIdSet.addAll(temp);

			logger.info("Ancestor Audit ID List : " + temp);
			logger.info("Parent Object Id Set: " + baseId);

			isHigherLevelDep = dsmtLinkServiceUtil.isHigherLevelDependencyPresent(parentObjectIdSet,
					ASSOCIATED_INVALID_DSMTS_IN_CONTROL_QUERY, baseId);

			/*
			 * If a higher level dependency is found then go on to fill out the necessary
			 * information in the current DSMT Link object.
			 */
			logger.info("isHigherLevelDep: " + isHigherLevelDep);
			if (isHigherLevelDep) {

				dsmtLinkObjectInfo = dsmtLinkServiceUtil.populateHigherLevelDependentResults(dsmtLinkObjectInfo);

			}

		}

		/* Log and return */
		logger.info("processHigherLevelDependencyForDSMTLink() End");

		return dsmtLinkObjectInfo;
	}
	
	/**
	 * This method will update the respective counter field in the AEApp object based on the following conditions :
	 * 
	 * First  : If Audit Type is "Risk-Based", then get the intermediary AEApp object, parent of the Audit, where AEApp is the child of AE 
	 * 			being associated to the AMR, increment the AEApp AMR Count field by 1.
	 * 
	 * Second : If Audit Type is "Other" And Audit Subtype is not "Continuous Auditing", then get the intermediary AEApp object, parent of the Audit, 
	 * 			where AEApp is the child of AE being associated to the AMR, increment the AEApp AMR Count field by 1.
	 * 
	 * Third  : If Audit Type is "Other" And Audit Subtype is "Continuous Auditing", then get the intermediary AEApp object, parent of the Audit, 
	 * 			where AEApp is the child of AE being associated to the AMR, increment the AEApp Quarterly AMR Count field by 1.
	 * 
	 * @param operationFlag
	 * @param ancestorAuditIdList
	 * @param auditableEntityIdList
	 * @throws Exception
	 */

	private void counterUpdate(String operationFlag, List<String> ancestorAuditIdList, List<Id> auditableEntityIdList) throws Exception {

		logger.info("counterUpdate() Start");

		logger.info("Operational Flag Value : " + operationFlag);
		logger.info("Ancestor Audit Id List : " + ancestorAuditIdList);
		logger.info("Auditable Entity Id List : " + auditableEntityIdList);

		if (isListNotNullOrEmpty(ancestorAuditIdList) && isListNotNullOrEmpty(auditableEntityIdList)) {

			// Local Variables
			String ancestorAuditID;
			IGRCObject ancestorAuditObject;
			String getParentAeAppQuery;
			String aeResourceIdQuery;
			List<String> getParentAeAppList;
			String auditType;
			String auditSubType;
			String fieldToUpdateInAEAPP = null;

			
			// Initialize Variables
			getParentAeAppList = new ArrayList<String>();
			ancestorAuditID = ancestorAuditIdList.get(0);
			ancestorAuditObject = grcObjectUtil.getObjectFromId(ancestorAuditID);
			auditType = fieldUtil.getFieldValueAsString(ancestorAuditObject, AUDIT_TYPE_FIELD);
			auditSubType = fieldUtil.getFieldValueAsString(ancestorAuditObject, AUDIT_SUB_TYPE_FIELD);

			logger.info("Ancestor Audit ID : " + ancestorAuditID);
			logger.info("Ancestor Audit Object Name : " + ancestorAuditObject.getName());
			logger.info("Audit Type : " + auditType);
			logger.info("Audit Sub Type : " + auditSubType);

			// Based on the audit type, we update separate fields in the AEApp object
			if (isEqual(auditType, AUDIT_TYPE_IS_RISK_BASED) || (isEqual(auditType, AUDIT_TYPE_IS_OTHER)
					&& !isEqual(auditSubType, AUDIT_SUB_TYPE_IS_CONTINUOUS_AUDITING))) {

				// update AMR COUNT in AeAPP
				fieldToUpdateInAEAPP = AEAPP_AMRCOUNT_FIELD;

			} else if (isEqual(auditType, AUDIT_TYPE_IS_OTHER)
					&& isEqual(auditSubType, AUDIT_SUB_TYPE_IS_CONTINUOUS_AUDITING)) {

				// update QTLY AMR COUNT in AeAPP
				fieldToUpdateInAEAPP = AEAPP_QTLYAMRCOUNT_FIELD;
			}
			
			logger.info("Field To Update In AEAPP Is Not Null? : " + isNotNullOrEmpty(fieldToUpdateInAEAPP));
			logger.info("Field To Update In AEAPP : " + fieldToUpdateInAEAPP);
			
			
			if(isNotNullOrEmpty(fieldToUpdateInAEAPP)) {
				
				// Get parent AE APP for the given ancestor audit id
				getParentAeAppQuery = GET_PARENT_AEAPPS_QUERY.replaceAll(RESOURCE_ID_STR, ancestorAuditID);
				logger.info("Get Parent AE APP's Query : " + getParentAeAppQuery);

				getParentAeAppList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(getParentAeAppQuery, false);
				logger.info("Parent AE APP List : " + getParentAeAppList);

				// Get AE name for each AE_ID and each aeAppId
				for (Id aeID : auditableEntityIdList) {

					for (String aeAppId : getParentAeAppList) {

						aeResourceIdQuery = AE_RESOURCE_IDS_QUERY.replaceAll(AE_RESOURCE_ID_STR, aeID.toString())
								.replaceAll(AEAPP_RESOURCE_STR, aeAppId);
						logger.info("Get AE resource id and name : " + aeResourceIdQuery);

						// we just want to know if the resultSet has value in it, hence we will pass the
						// above query to the
						// getTotalNumberOfRecordsReturned method, and it will return an int value, we
						// can use that value
						// to determine if the resultSet have value or not

						if (grcObjectSearchUtil.getTotalNumberOfRecordsReturned(aeResourceIdQuery) > 0) {
							
							IGRCObject aeAppObject;
							
							aeAppObject = grcObjectUtil.getObjectFromId(aeAppId);
							IIntegerField iIntegerField = (IIntegerField) aeAppObject.getField(fieldToUpdateInAEAPP);
							int currentFieldValue = (isObjectNotNull(iIntegerField.getValue()) ? iIntegerField.getValue().intValue() : 0);
							
							logger.info(fieldToUpdateInAEAPP + " - Value" + " : " + currentFieldValue);
							
							if (isEqual(FLAG_ASSOCIATE, operationFlag)) {
								
								currentFieldValue = currentFieldValue + 1;
								iIntegerField.setValue(new Integer(currentFieldValue));
								
							} else if (isEqual(FLAG_DISSOCIATE, operationFlag) && currentFieldValue > 0) {
								
								currentFieldValue = currentFieldValue - 1;
								iIntegerField.setValue(new Integer(currentFieldValue));
								
							}
							
							grcObjectUtil.saveResource(aeAppObject);

						}

					}
				}
			}			
		}

		logger.info("counterUpdate() End");

	}

}
