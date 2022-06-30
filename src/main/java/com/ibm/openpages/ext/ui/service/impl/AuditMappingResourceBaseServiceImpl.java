package com.ibm.openpages.ext.ui.service.impl;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isListNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isObjectNotNull;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.ACTIVE_YES;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.DSMT_LINK_ACTIVE_FIELD;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.DSMT_LINK_SCOPE_FIELD;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.GET_ISSUES_ASSOCIATED_TO_AMR;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.GET_LIST_OF_SCOPEDIN_DSMTS_BASEID_QUERY;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.GET_LIST_OF_SCOPEDOUT_DSMTS_INFO_QUERY;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.GET_PARENT_OBJECTS_ID_OF_TYPE_AUDITABLE_ENTITY;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.RESOURCE_ID_STR;
import static com.ibm.openpages.ext.ui.constant.AuditMappingReportDSMTLinkConstants.SCOPE_IN;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_BASE_SETTING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;
import com.ibm.openpages.ext.tss.service.beans.IGRCObjectAssociationInformation;
import com.ibm.openpages.ext.ui.bean.AuditMappingReportInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkObjectAssociationInfo;
import com.ibm.openpages.ext.ui.bean.DSMTObjectGenericDetails;

//TODO format the code, add the licensing at the top.
@Component
public class AuditMappingResourceBaseServiceImpl extends DSMTLinkBaseServiceImpl {

    /**
     * <p>
     * This method retrieves the Helpers Header Information from the Registry settings and sets the appropriate values
     * in the {@link } object. This will be used to display the content in the UI Header.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the helper header information set
     * @throws Exception a generi exception
     */
    public DSMTLinkHelperAppInfo getHelperHeaderInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getHelperHeaderInfo() Start");

        // Method Level Variables.
        CarbonHeaderInfo headerInfo;

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
     * @throws Exception a generic exception
     */
    public DSMTLinkHelperAppInfo getLandingPageInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getLandingPageInfo() Start");

        // Get the Basic Object information of the current object under execution
        getBasicObjectInfoForDisplay(dsmtLinkHelperInfo);

        logger.info("getLandingPageInfo() End");
        return dsmtLinkHelperInfo;
    }
    
	public List<String> getParentObjectsIdsOfTypeAuditableEntity(IGRCObject object, String parentObjectType)
			throws Exception {

		logger.info("getParentObjectsIdsOfTypeAuditableEntity() Start");

		logger.info("Object Name: " + object.getName());
		logger.info("Parent Object Type: " + parentObjectType);

		// Local Variables
		List<String> parentObjectsList;
		String query;

		// Initialize Variables
		parentObjectsList = null;
		query = null;

		// Construct Query
		query = GET_PARENT_OBJECTS_ID_OF_TYPE_AUDITABLE_ENTITY.replaceAll(RESOURCE_ID_STR, object.getId().toString());
		logger.info("Query : " + query);

		// Run Query
		parentObjectsList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, false);
		logger.info("Parent Objects List: " + parentObjectsList);

		logger.info("getParentObjectsIdsOfTypeAuditableEntity() End");

		return parentObjectsList;

	}
	
	public List<String> getResourceIdOfParentIssuesAssociatedToAMR(String amrObjectId) throws Exception {

		logger.info("getResourceIdOfParentIssuesAssociatedToAMR() Start");

		String getIssueAssociatedToAMRQuery;
		List<String> issuesAssociatedToAmrList;

		getIssueAssociatedToAMRQuery = GET_ISSUES_ASSOCIATED_TO_AMR;
		issuesAssociatedToAmrList = new ArrayList<String>();

		// Query Setup : Set the AMR object id to the query
		getIssueAssociatedToAMRQuery = getIssueAssociatedToAMRQuery.replaceAll(RESOURCE_ID_STR, amrObjectId);
		logger.info("Query 2 : Get Resource id's of Parent Issues of AMR : " + getIssueAssociatedToAMRQuery);

		// Run Query : Returns all the parent issues for the given AMR id
		issuesAssociatedToAmrList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(getIssueAssociatedToAMRQuery,
				false);
		logger.info("Query 2 : Issues associated to AMR List : " + issuesAssociatedToAmrList);

		logger.info("getResourceIdOfParentIssuesAssociatedToAMR() End");

		return issuesAssociatedToAmrList;

	}
	
	/**
	 * <p>
	 * This method will return updated DSMTLinkHelperAppInfo bean with the following information in it.
	 * 
	 * First  : get list of scoped in DSMT's BaseID's for current AMR, and add the list  to auditMappingReportInfo bean.
	 * Second : get a map (key: BaseId's and Value: ResourceId's) of scoped out DSMT's for the current AMR, and add it to auditMappingReportInfo bean.
	 * Finally : set the auditMappingReportInfo bean to DSMTLinkHelperAppInfo bean and return DSMTLinkHelperAppInfo.
	 * 
	 * @param dsmtLinkHelperAppInfo
	 * @return
	 * @throws Exception
	 */
	public DSMTLinkHelperAppInfo getDSMTBaseIdInfoForAMR(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
			throws Exception {

		logger.info("getDSMTBaseIdInfoForAMR() Start");

		// Local variables
		AuditMappingReportInfo auditMappingReportInfo;
		ITabularResultSet resultSet;
		Set<String> scopedInDSMTBaseIdList;
		Map<String, String> scopedOutDSMTBaseIdInfo;
		String scopedinDSMTBaseIDQuery;
		String scopedoutDSMTInfoQuery;

		// Initialize Variables
		auditMappingReportInfo = dsmtLinkHelperAppInfo.getAuditMappingReportInfo();
		scopedInDSMTBaseIdList = new HashSet<>();
		scopedOutDSMTBaseIdInfo = new HashMap<>();
		scopedinDSMTBaseIDQuery = null;
		scopedoutDSMTInfoQuery = null;

		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo
				: new AuditMappingReportInfo();

		// Construct Query 1 : Get list of Scoped-In DSMT BaseId
		scopedinDSMTBaseIDQuery = GET_LIST_OF_SCOPEDIN_DSMTS_BASEID_QUERY.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperAppInfo.getObjectID());
		logger.info("Construct Query : Get Scoped-In DSMT BaseId List : " + scopedinDSMTBaseIDQuery);

		// Run Query 1 : Get the DMST BaseId (Scoped in) and add it to the SET
		scopedInDSMTBaseIdList.addAll(grcObjectSearchUtil.getMultipleValuesFromQueryAsList(scopedinDSMTBaseIDQuery, true));

		// Set the output to the bean
		auditMappingReportInfo.setScopedInDSMTBaseIdList(scopedInDSMTBaseIdList);

		// Construct Query 2 : Get list of Scoped-Out DSMT Info (BaseId and ResourceId)
		scopedoutDSMTInfoQuery = GET_LIST_OF_SCOPEDOUT_DSMTS_INFO_QUERY.replaceAll(RESOURCE_ID_STR, dsmtLinkHelperAppInfo.getObjectID());
		logger.info("Construct Query : Get Scoped-Out DSMT Info Map : " + scopedoutDSMTInfoQuery);

		// Run Query 2 : Get the DMST Info (Scoped out) and add it to the MAP
		resultSet = grcObjectSearchUtil.executeQuery(scopedoutDSMTInfoQuery, true);

		// Put the output to the MAP
		for (IResultSetRow row : resultSet) {

			logger.info(
					"Can add row information: " + (isNotNullOrEmpty(fieldUtil.getFieldValueAsString(row.getField(0)))
							&& isNotNullOrEmpty(fieldUtil.getFieldValueAsString(row.getField(1)))));

			if (isNotNullOrEmpty(fieldUtil.getFieldValueAsString(row.getField(0)))
					&& isNotNullOrEmpty(fieldUtil.getFieldValueAsString(row.getField(1)))) {

				scopedOutDSMTBaseIdInfo.put(fieldUtil.getFieldValueAsString(row.getField(1)),
						fieldUtil.getFieldValueAsString(row.getField(0)));
			}
		}

		// Set the output to the bean
		auditMappingReportInfo.setScopedOutDSMTInfoMap(scopedOutDSMTBaseIdInfo);

		dsmtLinkHelperAppInfo.setAuditMappingReportInfo(auditMappingReportInfo);

		logger.info("getDSMTBaseIdInfoForAMR() End");

		return dsmtLinkHelperAppInfo;

	}
	
	public void processAddNewDSMTLinksToAMR(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception {

		logger.info("processAddNewDSMTLinksToAMR() Start");

		IGRCObject amrObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());
		AuditMappingReportInfo auditMappingReportInfo = dsmtLinkHelperAppInfo.getAuditMappingReportInfo();
		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo : new AuditMappingReportInfo();
		List<Id> dsmtLinksToCopy = auditMappingReportInfo.getDsmtLinksToCopy();
		logger.info("ID's Of DSMT's that has to be copied : " + dsmtLinksToCopy);

		if (isListNotNullOrEmpty(dsmtLinksToCopy)) {

			dsmtLinkServiceUtil.copyListOfObjectsUnderParent(amrObject, dsmtLinksToCopy);
		}

		logger.info("processAddNewDSMTLinksToAMR() End");

	}
	
	public void processAssociateParents(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception {

		logger.info("processAssociateParents() Start");

		List<Id> auditableEntityToAssociate = null;
		IGRCObjectAssociationInformation objectAssociationInformation = null;
		IGRCObject amrObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());
		AuditMappingReportInfo auditMappingReportInfo = dsmtLinkHelperAppInfo.getAuditMappingReportInfo();
		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo : new AuditMappingReportInfo();
		
		
		auditableEntityToAssociate = auditMappingReportInfo.getAuditableEntityToAssociate();

		logger.info("Final list of Issues and Auditable Entities to associate: " + auditableEntityToAssociate);
		if (isListNotNullOrEmpty(auditableEntityToAssociate)) {

			objectAssociationInformation = new IGRCObjectAssociationInformation();
			objectAssociationInformation.setSecondaryParentAssociationList(auditableEntityToAssociate);
			grcObjectUtil.associateParentAndChildrentToAnObject(amrObject, objectAssociationInformation);
		}

		logger.info("processAssociateParents() End");

	}

	public void processDissociateParents(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception {

		logger.info("processDissociateParents() Start");

		List<Id> auditableEntityToDissociate = null;
		IGRCObjectAssociationInformation objectAssociationInformation = null;
		IGRCObject amrObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());
		AuditMappingReportInfo auditMappingReportInfo = dsmtLinkHelperAppInfo.getAuditMappingReportInfo();
		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo : new AuditMappingReportInfo();


		auditableEntityToDissociate = auditMappingReportInfo.getAuditableEntityToAssociate();

		logger.info("Final list of Issues and Auditable Entities to dissociate: " + auditableEntityToDissociate);
		if (isListNotNullOrEmpty(auditableEntityToDissociate)) {

			objectAssociationInformation = new IGRCObjectAssociationInformation();
			objectAssociationInformation.setSecondaryParentAssociationList(auditableEntityToDissociate);
			grcObjectUtil.disassociateParentAndChildrentFromAnObject(amrObject, objectAssociationInformation);
		}

		logger.info("processDissociateParents() End");

	}
	

	public void processUpdateExistingDSMTLinksForAMR(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo, String scope) throws Exception {

		logger.info("processUpdateExistingDSMTLinksForAMR() Start");

		AuditMappingReportInfo auditMappingReportInfo = dsmtLinkHelperAppInfo.getAuditMappingReportInfo();
		auditMappingReportInfo = isObjectNotNull(auditMappingReportInfo) ? auditMappingReportInfo : new AuditMappingReportInfo();
		List<Id> dsmtLinksToUpdate = auditMappingReportInfo.getDsmtLinksToUpdate();
		logger.info("ID's of DSMT's that has to be updated : " + dsmtLinksToUpdate);

		if (isListNotNullOrEmpty(dsmtLinksToUpdate)) {

			for (Id dsmtLinkId : dsmtLinksToUpdate) {
				
				IGRCObject dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);
				
				fieldUtil.setFieldValue(dsmtLinkObject, DSMT_LINK_SCOPE_FIELD, scope);
				grcObjectUtil.saveResource(dsmtLinkObject);
			}
		}

		logger.info("processUpdateExistingDSMTLinksForAMR() End");

	}
}
