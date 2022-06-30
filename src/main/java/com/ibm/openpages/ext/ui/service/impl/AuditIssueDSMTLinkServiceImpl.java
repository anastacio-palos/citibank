package com.ibm.openpages.ext.ui.service.impl;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.service.beans.IGRCObjectAssociationInformation;
import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.constant.IssueStatus;
import com.ibm.openpages.ext.ui.constant.IssueSubType;
import com.ibm.openpages.ext.ui.service.IIssueDSMTLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.FALSE;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.tss.service.util.NumericUtil.getIntValue;
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
@Service("auditIssueDSMTLinkServiceImpl")
public class AuditIssueDSMTLinkServiceImpl extends IssueDSMTLinkBaseServiceImpl implements IIssueDSMTLinkService {

    /**
     * Construct any information once the bean is created.
     */
    @PostConstruct
    public void initServiceImpl() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(ISSUE_DSMT_LINK_SERVICE_LOG_FILE_NAME);
    }

    @Override
    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getExistingDSMTLinks() Start");

        String query;
        boolean isDEAOETIssue;
        String fieldNames;
        ITabularResultSet resultSet;
        List<String> fieldNamesList;
        DataGridInfo existingQuadsInfo;
        List<IssueSubType> subTypesList;
        List<DataGridHeaderColumnInfo> gridHeaderInfoList;
        List<ExistingDSMTLinkInfo> existingDMSTLinkInfoList;
        Map<String, List<ExistingDSMTLinkInfo>> associatedDSMTLinksInfoMap;

        existingQuadsInfo = new DataGridInfo();
        associatedDSMTLinksInfoMap = new LinkedHashMap<>();
        subTypesList = dsmtLinkHelperInfo.getCitiIssueInfo().getIssueSubTypeList();
        isDEAOETIssue = isListNotNullOrEmpty(subTypesList) && subTypesList.contains(IssueSubType.DEA_OET);
        fieldNames = isDEAOETIssue ?
                EXISTING_DSMT_LINK_TABLE_DEA_OET_HEADER_FIELDS_INFO :
                EXISTING_DSMT_LINK_TABLE_HEADER_FIELDS_INFO;
        fieldNamesList = parseCommaDelimitedValues(fieldNames);
        logger.info("Header Field Names List: \n" + fieldNamesList);

        gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);
        logger.info("Grid Header Info: \n" + gridHeaderInfoList);

        query = ASSOCIATED_DSMT_LINK_BASE_QUERY + dsmtLinkHelperInfo.getObjectID();
        logger.info("Associated DSMT Link Query: \n" + query);
        resultSet = grcObjectSearchUtil.executeQuery(query);
        associatedDSMTLinksInfoMap = groupDSMTLinksToParentAuditableEntity(dsmtLinkHelperInfo, resultSet,
                                                                           associatedDSMTLinksInfoMap,
                                                                           new ArrayList<>());
        existingDMSTLinkInfoList = processDSMTLinkInfoMapForDisplay(associatedDSMTLinksInfoMap, new ArrayList<>());
        existingQuadsInfo.setHeaders(gridHeaderInfoList);
        existingQuadsInfo.setRows(existingDMSTLinkInfoList);
        logger.info("getExistingDSMTLinks() End");
        return existingQuadsInfo;
    }

    /**
     * <p>
     * This method retrieves the DSMT objects from the DSMT DB based on the search text. The search information is
     * present in the {@link DSMTLinkSearchInfo} in the given dsmtLinkHelperInfo. The information present in the
     * dsmtSearchInfo drives the query to get the search results back. Possible areas of search are - Managed Segment -
     * Managed Geography - Legal Vehicle The queries are constructed based on the Node selected and the search text in
     * the dsmtSearchInfo.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception a generic exception
     */
    public DataGridInfo getAvailableDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getAvailableDSMTLinks() Start");

        boolean isDEAOETIssueOnly;
        boolean isPublishedIssue;
        boolean isGenericAndDEAOETIssue;

        List<IssueSubType> issueSubTypeList;
        String headerFields;
        List<String> fieldNamesList;
        DataGridInfo existingQuadsInfo;
        List<DataGridHeaderColumnInfo> gridHeaderInfoList;
        List<ExistingDSMTLinkInfo> existingDMSTLinkInfoList;

        existingQuadsInfo = new DataGridInfo();
        gridHeaderInfoList = new ArrayList<>();
        existingDMSTLinkInfoList = new ArrayList<>();

        issueSubTypeList = dsmtLinkHelperInfo.getCitiIssueInfo().getIssueSubTypeList();
        isPublishedIssue = IssueStatus.PUBLISHED.equals(dsmtLinkHelperInfo.getCitiIssueInfo().getIssueStatus());
        isDEAOETIssueOnly = isListNotNullOrEmpty(
                issueSubTypeList) && issueSubTypeList.size() == 1 && issueSubTypeList.contains(IssueSubType.DEA_OET);
        isGenericAndDEAOETIssue = isListNotNullOrEmpty(issueSubTypeList) && issueSubTypeList.contains(
                IssueSubType.GENERIC);

        if (isDEAOETIssueOnly) {

            headerFields = isPublishedIssue ?
                    EXISTING_DSMT_LINK_FIELDS_INFO_SEARCH :
                    EXISTING_CONTROL_DSMT_LINK_TABLE_HEADER_FIELDS_INFO;
            fieldNamesList = parseCommaDelimitedValues(headerFields);
            gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);

            existingDMSTLinkInfoList = processSearchForDEAOETAuditIssue(dsmtLinkHelperInfo);

        }
        else if (isGenericAndDEAOETIssue) {

            headerFields = EXISTING_DSMT_LINK_FIELDS_INFO_SEARCH;
            fieldNamesList = parseCommaDelimitedValues(headerFields);
            gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);

            existingDMSTLinkInfoList = processSearchForGenericAuditIssue(dsmtLinkHelperInfo);

        }

        existingQuadsInfo.setHeaders(gridHeaderInfoList);
        existingQuadsInfo.setRows(existingDMSTLinkInfoList);
        logger.info("getAvailableDSMTLinks() End");
        return existingQuadsInfo;
    }

    private List<String> getBaseIdsOfExistingDSMTLinksOfIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
            throws Exception {

        String query;
        List<String> existinDSMTLinkBaseIdsList;

        query = FETCH_BASE_ID_OF_EXISTING_SCOPE_IN_DSMT_LINKS_QUERY + dsmtLinkHelperAppInfo.getObjectID();
        existinDSMTLinkBaseIdsList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, true);

        return existinDSMTLinkBaseIdsList;
    }

    /**
     * <p>
     * This method retrieves the DSMT objects from the DSMT DB based on the search text. The search information is
     * present in the {@link DSMTLinkSearchInfo} in the given dsmtLinkHelperInfo. The information present in the
     * dsmtSearchInfo drives the query to get the search results back. Possible areas of search are - Managed Segment -
     * Managed Geography - Legal Vehicle The queries are constructed based on the Node selected and the search text in
     * the dsmtSearchInfo.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception a generic exception
     */
    public DataGridInfo searchDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("searchDSMT() Start");

        String query;
        List<String> fieldNamesList;
        DataGridInfo existingQuadsInfo;
        List<String> existinDSMTLinkBaseIdsList;
        List<DataGridHeaderColumnInfo> gridHeaderInfoList;
        List<ExistingDSMTLinkInfo> existingDMSTLinkInfoList;

        existingQuadsInfo = new DataGridInfo();
        fieldNamesList = parseCommaDelimitedValues(EXISTING_DSMT_LINK_FIELDS_INFO_SEARCH);
        gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);
        query = FETCH_BASE_ID_OF_EXISTING_SCOPE_IN_DSMT_LINKS_QUERY + dsmtLinkHelperInfo.getObjectID();
        existinDSMTLinkBaseIdsList = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, true);

        existingDMSTLinkInfoList = processSearchForAuditIssueOtherStatus(dsmtLinkHelperInfo,
                                                                         existinDSMTLinkBaseIdsList);

        existingQuadsInfo.setHeaders(gridHeaderInfoList);
        existingQuadsInfo.setRows(existingDMSTLinkInfoList);
        existingQuadsInfo.setWarningMessage(dsmtLinkHelperInfo.getCitiIssueInfo().getAeSearchWarning());
        logger.info("searchDSMT() End");
        return existingQuadsInfo;
    }

    /**
     * <p>
     * This method creates a DSMT Link with the selected DSMT's, and associates to the AuditableEntity updates the
     * fields in the created DSMT Link. The values of the fields are determined by the status of the AudtitableEntity.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception - Generic Exception
     */
    @Override
    public DSMTLinkHelperAppInfo addDSMTLinksToIssue(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("addDSMTLinksToAuditableEntity() Start");

        // Method Level Variables.
        IGRCObject issueObject;
        CitiIssueInfo citiIssueInfo;

        List<Id> auditableEntityToAssociate;
        IGRCObjectAssociationInformation objectAssociationInformation;

        /* Initialize Variables */
        citiIssueInfo = dsmtLinkHelperInfo.getCitiIssueInfo();
        citiIssueInfo = isObjectNotNull(citiIssueInfo) ? citiIssueInfo : new CitiIssueInfo();
        objectAssociationInformation = new IGRCObjectAssociationInformation();

        /* Get the Issue object information based on the information in the session */
        issueObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        dsmtLinkHelperInfo = getDSMTBaseIdInfoForIssue(dsmtLinkHelperInfo);
        dsmtLinkHelperInfo = processDSMTsToCopyAndOrUpdate(dsmtLinkHelperInfo);

        processAddNewDSMTLinksToIssue(dsmtLinkHelperInfo);
        processUpdateExistingDSMTLinksForIssue(dsmtLinkHelperInfo);
        auditableEntityToAssociate = citiIssueInfo.getAuditableEntityToAssociate();

        logger.info("Final List of Auditable Entities to associate: " + auditableEntityToAssociate);
        if (isListNotNullOrEmpty(auditableEntityToAssociate)) {

            objectAssociationInformation.setSecondaryParentAssociationList(auditableEntityToAssociate);
            grcObjectUtil.associateParentAndChildrentToAnObject(issueObject, objectAssociationInformation);
        }

        logger.info("addDSMTLinksToAuditableEntity() End");
        return dsmtLinkHelperInfo;
    }

    private void processAddNewDSMTLinksToIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception {

        logger.info("processAddNewDSMTLinksToIssue() Start");

        IGRCObject issueObject;
        CitiIssueInfo citiIssueInfo;
        List<Id> dsmtLinksToCopy;
        List<IGRCObject> copiedDSMTLinkObjects;

        citiIssueInfo = dsmtLinkHelperAppInfo.getCitiIssueInfo();
        citiIssueInfo = isObjectNotNull(citiIssueInfo) ? citiIssueInfo : new CitiIssueInfo();
        dsmtLinksToCopy = citiIssueInfo.getDsmtLinksToCopy();
        issueObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());
        logger.info("dsmtLinksToCopy: " + dsmtLinksToCopy);

        if (isListNotNullOrEmpty(dsmtLinksToCopy)) {

            copiedDSMTLinkObjects = dsmtLinkServiceUtil.copyListOfObjectsUnderParent(issueObject, dsmtLinksToCopy);

            logger.info("Is Published Issue: " + citiIssueInfo.getIssueStatus().equals(IssueStatus.PUBLISHED));
            if (citiIssueInfo.getIssueStatus().equals(IssueStatus.PUBLISHED) && isListNotNullOrEmpty(
                    copiedDSMTLinkObjects)) {

                for (IGRCObject dsmtLinkObj : copiedDSMTLinkObjects) {

                    fieldUtil.setFieldValue(dsmtLinkObj, DSMT_LINK_WHEN_ADDED_FIELD,
                                            WHEN_ADDED_MONITORING_ISSUES_VALUE);
                    grcObjectUtil.saveResource(dsmtLinkObj);
                }
            }
        }

        logger.info("processAddNewDSMTLinksToIssue() End");
    }

    private void processUpdateExistingDSMTLinksForIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception {

        logger.info("processUpdateExistingDSMTLinksForIssue() Start");

        IGRCObject dsmtLinkObject;
        CitiIssueInfo citiIssueInfo;
        List<Id> dsmtLinksToUpdate;
        List<IGRCObject> copiedDSMTLinkObjects;

        citiIssueInfo = dsmtLinkHelperAppInfo.getCitiIssueInfo();
        citiIssueInfo = isObjectNotNull(citiIssueInfo) ? citiIssueInfo : new CitiIssueInfo();
        dsmtLinksToUpdate = citiIssueInfo.getDsmtLinksToUpdate();
        logger.info("dsmtLinksToUpdate: " + dsmtLinksToUpdate);

        if (isListNotNullOrEmpty(dsmtLinksToUpdate)) {

            for (Id dsmtLinkId : dsmtLinksToUpdate) {

                dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);
                fieldUtil.setFieldValue(dsmtLinkObject, DSMT_LINK_SCOPE_FIELD, SCOPE_IN);
                fieldUtil.setFieldValue(dsmtLinkObject, DSMT_LINK_ACTIVE_FIELD, ACTIVE_YES);
                grcObjectUtil.saveResource(dsmtLinkObject);
            }
        }

        logger.info("processUpdateExistingDSMTLinksForIssue() End");
    }

    private DSMTLinkHelperAppInfo getDSMTBaseIdInfoForIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
            throws Exception {

        logger.info("getDSMTBaseIdInfoForIssue() Start");

        String query;
        CitiIssueInfo citiIssueInfo;
        ITabularResultSet resultSet;
        Set<String> scopedInDSMTBaseIdList;
        Map<String, String> scopedOutDSMTBaseIdInfo;

        scopedInDSMTBaseIdList = new HashSet<>();
        scopedOutDSMTBaseIdInfo = new HashMap<>();
        citiIssueInfo = dsmtLinkHelperAppInfo.getCitiIssueInfo();
        citiIssueInfo = isObjectNotNull(citiIssueInfo) ? citiIssueInfo : new CitiIssueInfo();

        query = FETCH_BASE_ID_OF_EXISTING_SCOPE_IN_DSMT_LINKS_QUERY;
        query = query + dsmtLinkHelperAppInfo.getObjectID();

        logger.info("DSMT's Scoped In Query: " + query);
        scopedInDSMTBaseIdList.addAll(grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, true));
        citiIssueInfo.setScopedInDSMTBaseIdList(scopedInDSMTBaseIdList);

        query = FETCH_BASE_ID_OF_EXISTING_SCOPE_OUT_DSMT_LINKS_QUERY;
        query = query + dsmtLinkHelperAppInfo.getObjectID();

        logger.info("DSMT's Scoped In Query: " + query);
        resultSet = grcObjectSearchUtil.executeQuery(query, true);
        for (IResultSetRow row : resultSet) {

            logger.info("Can add row information: " + (isNotNullOrEmpty(
                    fieldUtil.getFieldValueAsString(row.getField(0))) && isNotNullOrEmpty(
                    fieldUtil.getFieldValueAsString(row.getField(1)))));

            if (isNotNullOrEmpty(fieldUtil.getFieldValueAsString(row.getField(0))) && isNotNullOrEmpty(
                    fieldUtil.getFieldValueAsString(row.getField(1)))) {

                scopedOutDSMTBaseIdInfo.put(fieldUtil.getFieldValueAsString(row.getField(1)),
                                            fieldUtil.getFieldValueAsString(row.getField(0)));
            }
        }

        citiIssueInfo.setScopedOutDSMTInfoMap(scopedOutDSMTBaseIdInfo);
        dsmtLinkHelperAppInfo.setCitiIssueInfo(citiIssueInfo);

        logger.info("getDSMTBaseIdInfoForIssue() End");
        return dsmtLinkHelperAppInfo;
    }

    private DSMTLinkHelperAppInfo processDSMTsToCopyAndOrUpdate(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
            throws Exception {

        IGRCObject issueObject;
        CitiIssueInfo citiIssueInfo;
        List<Id> dsmtLinksToCopy;
        List<Id> dsmtLinksToUpdate;
        List<Id> auditableEntityToAssociate;
        List<String> parentAuditableEntities;
        Set<String> scopedInDSMTBaseIdList;
        Set<String> scopedOutDSMTBaseIdList;
        Map<String, String> scopedOutDSMTBaseIdInfo;

        dsmtLinksToCopy = new ArrayList<>();
        dsmtLinksToUpdate = new ArrayList<>();
        auditableEntityToAssociate = new ArrayList<>();
        citiIssueInfo = dsmtLinkHelperAppInfo.getCitiIssueInfo();
        citiIssueInfo = isObjectNotNull(citiIssueInfo) ? citiIssueInfo : new CitiIssueInfo();
        scopedInDSMTBaseIdList = citiIssueInfo.getScopedInDSMTBaseIdList();
        scopedInDSMTBaseIdList = isSetNotNullOrEmpty(scopedInDSMTBaseIdList) ? scopedInDSMTBaseIdList : new HashSet<>();

        scopedOutDSMTBaseIdInfo = citiIssueInfo.getScopedOutDSMTInfoMap();
        scopedOutDSMTBaseIdInfo = isMapNotNullOrEmpty(scopedOutDSMTBaseIdInfo) ?
                scopedOutDSMTBaseIdInfo :
                new HashMap<>();
        scopedOutDSMTBaseIdList = scopedOutDSMTBaseIdInfo.keySet();

        issueObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());
        parentAuditableEntities = dsmtLinkHelperUtil.getParentObjectsOfSpecificType(issueObject, AUDITABLE_ENTITY);
        parentAuditableEntities = isListNotNullOrEmpty(parentAuditableEntities) ?
                parentAuditableEntities :
                new ArrayList<>();

        logger.info("Parent Auditable Entities: " + parentAuditableEntities);


        /* Iterate through the list of the DSMT's to add */
        for (UpdateDSMTLinkInfo udpateDSMDsmtLinkInfo : dsmtLinkHelperAppInfo.getUpdateDsmtLinkInfo()) {

            List<String> dsmtLinIdsList = udpateDSMDsmtLinkInfo.getDsmtLinkIdsToUpdate();

            logger.info("Parent Auditable Entity Not already associated: " + (!parentAuditableEntities.contains(
                    udpateDSMDsmtLinkInfo.getAuditableEntityId())));

            if (!parentAuditableEntities.contains(udpateDSMDsmtLinkInfo.getAuditableEntityId())) {
                auditableEntityToAssociate.add(new Id(udpateDSMDsmtLinkInfo.getAuditableEntityId()));
            }

            for (String dsmtLinkId : dsmtLinIdsList) {

                IGRCObject dsmtLinkObjectToCopy = grcObjectUtil.getObjectFromId(dsmtLinkId);
                String baseId = fieldUtil.getFieldValueAsString(dsmtLinkObjectToCopy, DSMT_LINK_BASE_ID_FIELD);

                logger.info("Base Id: " + baseId + " For DSMT Link id: " + dsmtLinkId);

                if (isNotNullOrEmpty(baseId)) {

                    logger.info("Is DSMT Link with Base Id present: " + (!scopedInDSMTBaseIdList.contains(
                            baseId) && !scopedOutDSMTBaseIdList.contains(baseId)));

                    logger.info("Is DSMT Link with Base Id present but inactive: " + (scopedOutDSMTBaseIdList.contains(
                            baseId)));

                    if (!scopedInDSMTBaseIdList.contains(baseId) && !scopedOutDSMTBaseIdList.contains(baseId)) {

                        dsmtLinksToCopy.add(new Id(dsmtLinkId));
                    }
                    else if (scopedOutDSMTBaseIdList.contains(baseId)) {

                        dsmtLinksToUpdate.add(new Id(scopedOutDSMTBaseIdInfo.get(baseId)));
                    }
                }
            }
        }

        citiIssueInfo.setDsmtLinksToCopy(dsmtLinksToCopy);
        citiIssueInfo.setDsmtLinksToUpdate(dsmtLinksToUpdate);
        citiIssueInfo.setAuditableEntityToAssociate(auditableEntityToAssociate);
        dsmtLinkHelperAppInfo.setCitiIssueInfo(citiIssueInfo);
        return dsmtLinkHelperAppInfo;
    }

    public DSMTLinkHelperAppInfo descopeDSMTLinksFromIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
            throws Exception {

        logger.info("descopeDSMTLinksFromIssue() Start");

        // Method Level Variables.
        String invalidDSMTLinkQuery;
        IGRCObject issueObject;
        List<String> invalidDSMTLinks;
        IGRCObjectAssociationInformation objectAssociationInfo;
        List<Id> disassociateParentIdList;

        /* Initialize Variables */
        issueObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperAppInfo.getObjectID());
        logger.info("DSMT Link Descoping List : " + dsmtLinkHelperAppInfo.getDsmtLinkUpdateInfo());

        // Make sure the Descoping information is present to continue
        if (isListNotNullOrEmpty(dsmtLinkHelperAppInfo.getDsmtLinkUpdateInfo())) {

            // Iterate through the list of Descoping items. The items are grouped by the Auditable Entities.
            for (DSMTLinkUpdateInfo descopeDSMTInfo : dsmtLinkHelperAppInfo.getDsmtLinkUpdateInfo()) {

                descopeDSMTs(descopeDSMTInfo);
                invalidDSMTLinkQuery = ASSOCIATED_INVALID_CHILD_DSMTS_WITH_ISSUE_QUERY.replaceAll(RESOURCE_ID_STR,
                                                                                                  dsmtLinkHelperAppInfo.getObjectID());
                logger.info("Is Descope Parent AE : " + descopeDSMTInfo.isDescopeParentAE());

                logger.info("Invalid child DSMT query: " + invalidDSMTLinkQuery);
                invalidDSMTLinks = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(invalidDSMTLinkQuery, true);

                logger.info("Invalid child DSMT's: " + invalidDSMTLinks);

                if (isListNotNullOrEmpty(invalidDSMTLinks)) {

                    fieldUtil.setFieldValue(issueObject, ISSUE_DATA_SEG_FLAG_FIELD, FALSE);
                    grcObjectUtil.saveResource(issueObject);
                }

                if (descopeDSMTInfo.isDescopeParentAE()) {

                    logger.info("Auditable entity id is only present so disassociating the Auditable entity and ");

                    disassociateParentIdList = new ArrayList<>();
                    objectAssociationInfo = new IGRCObjectAssociationInformation();
                    disassociateParentIdList.add(new Id(descopeDSMTInfo.getAuditableEntityId()));
                    objectAssociationInfo.setSecondaryParentAssociationList(disassociateParentIdList);

                    logger.info("Disassociating the issue from AE: " + objectAssociationInfo);
                    grcObjectUtil.disassociateParentAndChildrentFromAnObject(issueObject, objectAssociationInfo);
                    grcObjectUtil.saveResource(issueObject);
                }
            }
        }

        logger.info("descopeDSMTLinksFromIssue() End");
        return dsmtLinkHelperAppInfo;
    }

    private void descopeDSMTs(DSMTLinkUpdateInfo descopeDSMTInfo) throws Exception {

        logger.info("descopeDSMTs() Start");

        IGRCObject dsmtLinkObject;
        Map<String, String> dsmtFieldsUpdateInfoMap;

        logger.info("Is DSMT linkes available to update: " + isListNotNullOrEmpty(
                descopeDSMTInfo.getDsmtLinkIdsToUpdate()));

        if (isListNotNullOrEmpty(descopeDSMTInfo.getDsmtLinkIdsToUpdate())) {

            logger.info("Going to iterate over total dsmts: " + descopeDSMTInfo.getDsmtLinkIdsToUpdate().size());
            for (ExistingDSMTLinkInfo descopeDSMT : descopeDSMTInfo.getDsmtLinkIdsToUpdate()) {

                logger.info("Disassociating DSMT: " + descopeDSMT.getResourceId());
                dsmtFieldsUpdateInfoMap = new HashMap<>();
                dsmtFieldsUpdateInfoMap.put(DSMT_LINK_SCOPE_FIELD, descopeDSMT.getScope());
                dsmtLinkObject = grcObjectUtil.getObjectFromId(descopeDSMT.getResourceId());
                grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, dsmtFieldsUpdateInfoMap);
            }
        }

        logger.info("descopeDSMTs() End");
    }

    /**
     * <p>
     *
     * </P>
     *
     * @param resultSet - an instance of {@link ITabularResultSet} that contains the results of query executed
     * @return - a Map containing the auditable entity id as key and a list of ExistingDSMTLinkInfo containing the
     * DSMT Link object information
     * @throws Exception a generic exception
     */
    private Map<String, List<ExistingDSMTLinkInfo>> groupAvailableDSMTLinksToParentAuditableEntity(
            DSMTLinkHelperAppInfo dsmtLinkHelperInfo, ITabularResultSet resultSet,
            Map<String, List<ExistingDSMTLinkInfo>> dsmtLinksGroupedByAEMap, List<String> existinDSMTLinkBaseIdsList)
            throws Exception {

        logger.info("groupAvailableDSMTLinksToParentAuditableEntity() Start");

        boolean isDEAOETIssue;
        boolean isGenericIssue;
        boolean isPublishedIssue;
        String dsmtBaseId;
        String auditableEntityId;
        IGRCObject dsmtLinkObj;
        List<IssueSubType> subTypesList;
        ExistingDSMTLinkInfo dsmtLinkObjectInfo;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList;

        logger.info("Existing DSMT-Links Base Id list: " + existinDSMTLinkBaseIdsList);
        subTypesList = dsmtLinkHelperInfo.getCitiIssueInfo().getIssueSubTypeList();
        dsmtLinksGroupedByAEMap = isObjectNull(dsmtLinksGroupedByAEMap) ?
                new LinkedHashMap<>() :
                dsmtLinksGroupedByAEMap;

        for (IResultSetRow row : resultSet) {

            dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();
            dsmtBaseId = fieldUtil.getFieldValueAsString(row.getField(6));
            auditableEntityId = fieldUtil.getFieldValueAsString(row.getField(5));
            logger.info("Base Id for current DSMT Link: " + dsmtBaseId);
            logger.info("Duplicate Check returns: " + (isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(
                    existinDSMTLinkBaseIdsList) || (isListNotNullOrEmpty(
                    existinDSMTLinkBaseIdsList) && !existinDSMTLinkBaseIdsList.contains(dsmtBaseId))));

            if ((isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(existinDSMTLinkBaseIdsList) || (isListNotNullOrEmpty(
                    existinDSMTLinkBaseIdsList) && !existinDSMTLinkBaseIdsList.contains(
                    dsmtBaseId))) && isNotNullOrEmpty(auditableEntityId)) {

                dsmtLinkObj = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(1)));
                logger.info(
                        "DSMT Link Object name [" + dsmtLinkObj.getName() + "] and Id [" + dsmtLinkObj.getId() + "]");

                isDEAOETIssue = isListNotNullOrEmpty(subTypesList) && subTypesList.contains(IssueSubType.DEA_OET);
                isGenericIssue = isListNotNullOrEmpty(subTypesList) && subTypesList.contains(IssueSubType.GENERIC);
                isPublishedIssue = IssueStatus.PUBLISHED.equals(dsmtLinkHelperInfo.getCitiIssueInfo().getIssueStatus());

                logger.info("isDEAOETIssue: " + isDEAOETIssue);
                logger.info("isGenericIssue: " + isGenericIssue);

                if (!isPublishedIssue && isDEAOETIssue) {

                    getAncestorControlsForDSMTLink(dsmtLinkHelperInfo, dsmtLinkObjectInfo, dsmtBaseId);
                }

                logger.info("is groupAvailableDSMTs: " + (isPublishedIssue || (isDEAOETIssue && isListNotNullOrEmpty(
                        dsmtLinkObjectInfo.getControls())) || isGenericIssue));

                if (isPublishedIssue || (isDEAOETIssue && isListNotNullOrEmpty(
                        dsmtLinkObjectInfo.getControls())) || isGenericIssue) {

                    groupAvailableDSMTs(dsmtLinkObj, dsmtLinkObjectInfo, row);

                    dsmtLinkObjectList = dsmtLinksGroupedByAEMap.get(auditableEntityId);
                    dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ?
                            dsmtLinkObjectList :
                            new ArrayList<>();
                    dsmtLinkObjectList.add(dsmtLinkObjectInfo);
                    dsmtLinksGroupedByAEMap.put(auditableEntityId, dsmtLinkObjectList);
                }
            }
        }

        logger.info("groupAvailableDSMTLinksToParentAuditableEntity() End");
        return dsmtLinksGroupedByAEMap;
    }

    private void groupAvailableDSMTs(IGRCObject dsmtLinkObj, ExistingDSMTLinkInfo dsmtLinkObjectInfo,
            IResultSetRow dsmtRow) throws Exception {

        logger.info("groupAvailableDSMTs() Start");

        String dsmtBaseId;
        String dsmtObjLink;
        String legalVehicleName;
        String auditableEntityId;
        String managedSegmentName;
        String managedGeographyName;

        dsmtBaseId = fieldUtil.getFieldValueAsString(dsmtRow.getField(6));
        auditableEntityId = fieldUtil.getFieldValueAsString(dsmtRow.getField(5));

        legalVehicleName = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_LVNAME_FIELD);
        managedSegmentName = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MSNAME_FIELD);
        managedGeographyName = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MGNAME_FIELD);

        dsmtLinkObjectInfo.setBaseId(dsmtBaseId);
        dsmtLinkObjectInfo.setParentResourceId(auditableEntityId);
        dsmtLinkObjectInfo.setName(fieldUtil.getFieldValueAsString(dsmtRow.getField(0)));
        dsmtLinkObjectInfo.setResourceId(fieldUtil.getFieldValueAsString(dsmtRow.getField(1)));
        dsmtLinkObjectInfo.setDescription(fieldUtil.getFieldValueAsString(dsmtRow.getField(2)));
        dsmtLinkObjectInfo.setType(fieldUtil.getFieldValueAsString(dsmtRow.getField(3)));
        dsmtLinkObjectInfo.setStatus(fieldUtil.getFieldValueAsString(dsmtRow.getField(4)));
        dsmtLinkObjectInfo.setActive(fieldUtil.getFieldValueAsString(dsmtRow.getField(7)));

        dsmtObjLink = dsmtLinkHelperUtil.getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(),
                                                                 dsmtLinkObjectInfo.getName());

        dsmtLinkObjectInfo.setLink(dsmtObjLink);
        dsmtLinkObjectInfo.setLegalvehiclename(legalVehicleName);
        dsmtLinkObjectInfo.setManagedsegmentname(managedSegmentName);
        dsmtLinkObjectInfo.setManagedgeographyname(managedGeographyName);
        dsmtLinkObjectInfo.setParentResourceId(fieldUtil.getFieldValueAsString(dsmtRow.getField(5)));

        logger.info("groupAvailableDSMTs() End");
    }

    /**
     * <p>
     *
     * </P>
     *
     * @param resultSet - an instance of {@link ITabularResultSet} that contains the results of query executed
     * @return - a Map containing the auditable entity id as key and a list of ExistingDSMTLinkInfo containing the
     * DSMT Link object information
     * @throws Exception a generic exception
     */
    private Map<String, List<ExistingDSMTLinkInfo>> groupDSMTLinksToParentAuditableEntity(
            DSMTLinkHelperAppInfo dsmtLinkHelperInfo, ITabularResultSet resultSet,
            Map<String, List<ExistingDSMTLinkInfo>> dsmtLinksGroupedByAEMap, List<String> existinDSMTLinkBaseIdsList)
            throws Exception {

        logger.info("groupDSMTLinksToParentAuditableEntity() Start");

        String dsmtBaseId;
        String dsmtObjLink;
        String legalVehicleName;
        String auditableEntityId;
        String managedSegmentName;
        String managedGeographyName;
        IGRCObject dsmtLinkObj;
        IGRCObject issueObject;
        ExistingDSMTLinkInfo dsmtLinkObjectInfo;
        DSMTLinkDisableInfo dsmtLinkDisableInfo;
        List<String> parentAuditableEntities;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList;
        List<AncestorControlsInfo> ancestorControlsForIssueList = null;

        logger.info("Existing DSMT-Links Base Id list: " + existinDSMTLinkBaseIdsList);
        dsmtLinksGroupedByAEMap = isObjectNull(dsmtLinksGroupedByAEMap) ?
                new LinkedHashMap<>() :
                dsmtLinksGroupedByAEMap;

        issueObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        parentAuditableEntities = dsmtLinkHelperUtil.getParentObjectsOfSpecificType(issueObject, "AuditableEntity");
        parentAuditableEntities = isListNotNullOrEmpty(parentAuditableEntities) ?
                parentAuditableEntities :
                new ArrayList<>();

        logger.info("Parent Auditable Entities: " + parentAuditableEntities);

        if (isListNotNullOrEmpty(parentAuditableEntities)) {

            for (IResultSetRow row : resultSet) {

                dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();
                dsmtBaseId = fieldUtil.getFieldValueAsString(row.getField(6));
                auditableEntityId = fieldUtil.getFieldValueAsString(row.getField(5));

                logger.info(
                        "Is auditable Entity: " + auditableEntityId + " Present in available parents: " + parentAuditableEntities.contains(
                                auditableEntityId));

                if (parentAuditableEntities.contains(auditableEntityId)) {

                    logger.info("Base Id for current DSMT Link: " + dsmtBaseId);
                    logger.info("Duplicate Check returns: " + (isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(
                            existinDSMTLinkBaseIdsList) || (isListNotNullOrEmpty(
                            existinDSMTLinkBaseIdsList) && !existinDSMTLinkBaseIdsList.contains(dsmtBaseId))));

                    if ((isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(
                            existinDSMTLinkBaseIdsList) || (isListNotNullOrEmpty(
                            existinDSMTLinkBaseIdsList) && !existinDSMTLinkBaseIdsList.contains(
                            dsmtBaseId))) && isNotNullOrEmpty(auditableEntityId)) {

                        List<IssueSubType> subTypesList = dsmtLinkHelperInfo.getCitiIssueInfo().getIssueSubTypeList();
                        boolean isDEAOETIssue = isListNotNullOrEmpty(subTypesList) && subTypesList.contains(
                                IssueSubType.DEA_OET);
                        boolean isGenericIssue = isListNotNullOrEmpty(subTypesList) && subTypesList.contains(
                                IssueSubType.GENERIC);

                        if (isDEAOETIssue) {

                            getAncestorControlsForDSMTLink(dsmtLinkHelperInfo, dsmtLinkObjectInfo, dsmtBaseId);
                            ancestorControlsForIssueList = dsmtLinkObjectInfo.getControls();
                        }

                        logger.info("Is Issue qualify: " + (isGenericIssue || (isDEAOETIssue && isListNotNullOrEmpty(
                                ancestorControlsForIssueList)) || isListNullOrEmpty(subTypesList)));

                        dsmtLinkObj = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(1)));
                        logger.info(
                                "DSMT Link Object name [" + dsmtLinkObj.getName() + "] and Id [" + dsmtLinkObj.getId() + "]");

                        legalVehicleName = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_LVNAME_FIELD);
                        managedSegmentName = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MSNAME_FIELD);
                        managedGeographyName = fieldUtil.getFieldValueAsString(dsmtLinkObj, DSMT_LINK_MGNAME_FIELD);

                        dsmtLinkObjectInfo.setBaseId(dsmtBaseId);
                        dsmtLinkObjectInfo.setParentResourceId(auditableEntityId);
                        dsmtLinkObjectInfo.setName(fieldUtil.getFieldValueAsString(row.getField(0)));
                        dsmtLinkObjectInfo.setResourceId(fieldUtil.getFieldValueAsString(row.getField(1)));
                        dsmtLinkObjectInfo.setDescription(fieldUtil.getFieldValueAsString(row.getField(2)));
                        dsmtLinkObjectInfo.setType(fieldUtil.getFieldValueAsString(row.getField(3)));
                        dsmtLinkObjectInfo.setScope(fieldUtil.getFieldValueAsString(row.getField(4)));
                        dsmtLinkObjectInfo.setActive(fieldUtil.getFieldValueAsString(row.getField(7)));

                        dsmtObjLink = dsmtLinkHelperUtil.getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(),
                                                                                 dsmtLinkObjectInfo.getName());

                        dsmtLinkObjectInfo.setLink(dsmtObjLink);
                        dsmtLinkObjectInfo.setLegalvehiclename(legalVehicleName);
                        dsmtLinkObjectInfo.setManagedsegmentname(managedSegmentName);
                        dsmtLinkObjectInfo.setManagedgeographyname(managedGeographyName);
                        dsmtLinkObjectInfo.setParentResourceId(fieldUtil.getFieldValueAsString(row.getField(5)));

                        dsmtLinkObjectInfo = processHigherLevelDependencyCheck(dsmtLinkHelperInfo, dsmtLinkObjectInfo);

                        /*
                         * Check for lower level dependency if there is a lower level dependency then process to disable the row
                         * and also add the information to be shown in the screen.
                         */
                        logger.info("Check for lower level dependency");
                        dsmtLinkObjectInfo = processLowerLevelDependencyCheck(dsmtLinkHelperInfo, dsmtLinkObjectInfo);

                        dsmtLinkObjectInfo = dsmtLinkServiceUtil.processValidDSMTLink(dsmtLinkObjectInfo);
                        dsmtLinkDisableInfo = dsmtLinkObjectInfo.getDsmtLinkDisableInfo();
                        dsmtLinkDisableInfo = isObjectNotNull(dsmtLinkDisableInfo) ?
                                dsmtLinkDisableInfo :
                                new DSMTLinkDisableInfo();

                        dsmtLinkObjectInfo.setDisabled(
                                isSetNotNullOrEmpty(dsmtLinkDisableInfo.getDisableInfoList()) || isSetNotNullOrEmpty(
                                        dsmtLinkDisableInfo.getLowerLevelDepObjectsList()));

                        dsmtLinkObjectList = dsmtLinksGroupedByAEMap.get(auditableEntityId);
                        dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ?
                                dsmtLinkObjectList :
                                new ArrayList<>();
                        dsmtLinkObjectList.add(dsmtLinkObjectInfo);
                        dsmtLinksGroupedByAEMap.put(auditableEntityId, dsmtLinkObjectList);
                    }
                }
            }
        }

        logger.info("groupDSMTLinksToParentAuditableEntity() End");
        return dsmtLinksGroupedByAEMap;
    }

    private void getAncestorControlsForDSMTLink(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            ExistingDSMTLinkInfo dsmtLinkObjectInfo, String baseId) throws Exception {

        logger.info("getAncestorControlsForDSMTLink() Start");

        String controlName;
        String controlQuery;
        String controlObjLink;
        List<String> duplicateList;
        Set<String> ancestorControlsList;
        AncestorControlsInfo controlsInfo;
        List<AncestorControlsInfo> ancestorControlsForIssueList;

        duplicateList = new ArrayList<>();
        ancestorControlsForIssueList = new ArrayList<>();
        ancestorControlsList = dsmtLinkHelperInfo.getCitiIssueInfo().getAncestorControlsList();
        logger.info("List of Ancestor Controls: " + ancestorControlsList);

        if (isSetNotNullOrEmpty(ancestorControlsList)) {

            for (String ancestorControlId : ancestorControlsList) {

                controlsInfo = new AncestorControlsInfo();
                controlQuery = ASSOCIATED_DSMT_LINKS_CONTROL_ANCESTOR_CHECK_QUERY_1;
                controlQuery = controlQuery.replace(BASE_ID_STR, baseId);
                controlQuery = controlQuery.replace(CONTROL_RESOURCE_ID_STR, ancestorControlId);
                controlQuery = controlQuery.replace(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
                logger.info("Control Query 1 to execute: " + controlQuery);

                controlName = grcObjectSearchUtil.getSingleValueFromQuery(controlQuery);
                logger.info("Value Returned: " + controlName);
                logger.info("Duplicate List: " + duplicateList);

                if (isNotNullOrEmpty(controlName) && !duplicateList.contains(ancestorControlId)) {

                    duplicateList.add(ancestorControlId);
                    controlObjLink = dsmtLinkHelperUtil.getObjectDetailViewLinkForModal(ancestorControlId, controlName);

                    controlsInfo.setControlObjLink(controlObjLink);
                    controlsInfo.setResourceId(ancestorControlId);
                    controlsInfo.setControlName(controlName);
                    ancestorControlsForIssueList.add(controlsInfo);
                }

                controlQuery = ASSOCIATED_DSMT_LINKS_CONTROL_ANCESTOR_CHECK_QUERY_2;
                controlQuery = controlQuery.replace(BASE_ID_STR, baseId);
                controlQuery = controlQuery.replace(RESOURCE_ID_STR, dsmtLinkHelperInfo.getObjectID());
                controlQuery = controlQuery.replace(CONTROL_RESOURCE_ID_STR, ancestorControlId);
                logger.info("Control Query 2 to execute: " + controlQuery);

                controlName = grcObjectSearchUtil.getSingleValueFromQuery(controlQuery);
                logger.info("Value Returned: " + controlName);
                logger.info("Duplicate List: " + duplicateList);

                if (isNotNullOrEmpty(controlName) && !duplicateList.contains(ancestorControlId)) {

                    duplicateList.add(ancestorControlId);
                    controlObjLink = dsmtLinkHelperUtil.getObjectDetailViewLinkForModal(ancestorControlId, controlName);

                    controlsInfo.setControlObjLink(controlObjLink);
                    controlsInfo.setResourceId(ancestorControlId);
                    controlsInfo.setControlName(controlName);
                    ancestorControlsForIssueList.add(controlsInfo);
                }
            }
        }

        dsmtLinkObjectInfo.setControls(ancestorControlsForIssueList);
        logger.info("Ancestor Control Details for Base Id: " + baseId + " = " + ancestorControlsForIssueList);
        logger.info("getAncestorControlsForDSMTLink() End");
    }

    private List<ExistingDSMTLinkInfo> processDSMTLinkInfoMapForDisplay(
            Map<String, List<ExistingDSMTLinkInfo>> dsmtLinkInfoMap, List<ExistingDSMTLinkInfo> availableDSMTLinksList)
            throws Exception {

        logger.info("processDSMTLinkInfoMapForDisplay() Start");

        logger.info("DSMT Link Info Map: " + dsmtLinkInfoMap);

        int childCount;
        int rowCount = 1;
        boolean isChildDisableInfoPresent;
        boolean isParentDisableInfoPresent;

        String auditEntityType;
        String auditableEntityName;
        String auditableEntityDesc;

        List<String> hierarchyList;
        IGRCObject audEntityObject;
        ExistingDSMTLinkInfo existingDSMTLinkInfo;
        List<ExistingDSMTLinkInfo> childDMSTLinkInfoList;

        if (isMapNotNullOrEmpty(dsmtLinkInfoMap)) {

            availableDSMTLinksList = isObjectNotNull(availableDSMTLinksList) ?
                    availableDSMTLinksList :
                    new ArrayList<>();

            for (String auditableEntityId : dsmtLinkInfoMap.keySet()) {

                childCount = 1;
                hierarchyList = new ArrayList<>();
                existingDSMTLinkInfo = new ExistingDSMTLinkInfo();
                audEntityObject = grcObjectUtil.getObjectFromId(auditableEntityId);
                childDMSTLinkInfoList = dsmtLinkInfoMap.get(auditableEntityId);

                auditableEntityName = audEntityObject.getName();
                auditableEntityDesc = audEntityObject.getDescription();
                auditEntityType = fieldUtil.getFieldValueAsString(audEntityObject, AUDITABLE_ENTITY_TYPE_FIELD);

                hierarchyList.add(auditableEntityName);
                existingDSMTLinkInfo.setId("row-" + rowCount);
                existingDSMTLinkInfo.setType(auditEntityType);
                existingDSMTLinkInfo.setResourceId(auditableEntityId);
                existingDSMTLinkInfo.setDescription(auditableEntityDesc);
                existingDSMTLinkInfo.setName(auditableEntityName + " - " + auditableEntityDesc);
                existingDSMTLinkInfo.setHasChildren(isListNotNullOrEmpty(childDMSTLinkInfoList));
                existingDSMTLinkInfo.setHierarchy(hierarchyList);

                if (isListNotNullOrEmpty(childDMSTLinkInfoList)) {

                    for (ExistingDSMTLinkInfo childDMSTLinkInfo : childDMSTLinkInfoList) {
                        hierarchyList = new ArrayList<>();
                        hierarchyList.add(auditableEntityName);
                        hierarchyList.add(childDMSTLinkInfo.getName());
                        childDMSTLinkInfo.setId("row-" + rowCount + "-child-" + childCount);
                        childDMSTLinkInfo.setHierarchy(hierarchyList);
                        childCount++;
                        availableDSMTLinksList.add(childDMSTLinkInfo);

                        isParentDisableInfoPresent = isObjectNotNull(
                                existingDSMTLinkInfo.getDsmtLinkDisableInfo()) && (isSetNotNullOrEmpty(
                                existingDSMTLinkInfo.getDsmtLinkDisableInfo()
                                        .getDisableInfoList()) || isSetNotNullOrEmpty(
                                existingDSMTLinkInfo.getDsmtLinkDisableInfo().getLowerLevelDepObjectsList()));

                        isChildDisableInfoPresent = isObjectNotNull(
                                childDMSTLinkInfo.getDsmtLinkDisableInfo()) && (isSetNotNullOrEmpty(
                                childDMSTLinkInfo.getDsmtLinkDisableInfo().getLowerLevelDepObjectsList()));

                        logger.info("Is Parent Disable already processed: " + isParentDisableInfoPresent);
                        logger.info("Is Child has Disable Info: " + isChildDisableInfoPresent);

                        if (!isParentDisableInfoPresent && isChildDisableInfoPresent) {

                            DSMTLinkDisableInfo aeDisableInfo = new DSMTLinkDisableInfo();
                            Set<String> disableInfoList = new HashSet<>();
                            disableInfoList.add(AE_LOWER_DEPENDECY_VALIDATION_MESSAGE);
                            aeDisableInfo.setDisableInfoList(disableInfoList);
                            existingDSMTLinkInfo.setDisabled(true);
                            existingDSMTLinkInfo.setDsmtLinkDisableInfo(aeDisableInfo);
                        }
                    }
                }

                rowCount++;
                availableDSMTLinksList.add(existingDSMTLinkInfo);
            }
        }

        logger.info("processDSMTLinkInfoMapForDisplay() End");
        return availableDSMTLinksList;
    }

    private List<ExistingDSMTLinkInfo> processSearchForGenericAuditIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
            throws Exception {

        logger.info("processSearchForAuditIssueDraftStatus() Start");

        String query;
        Set<String> ancestorAudits;
        CitiIssueInfo citiIssueInfo;
        ITabularResultSet resultSet;
        List<String> existinDSMTLinkBaseIdsList;
        List<ExistingDSMTLinkInfo> availableDSMTLinksList;
        Map<String, List<ExistingDSMTLinkInfo>> dsmtLinksGroupedByAEMap;

        availableDSMTLinksList = new ArrayList<>();
        dsmtLinksGroupedByAEMap = new LinkedHashMap<>();
        ancestorAudits = isObjectNotNull(dsmtLinkHelperAppInfo.getCitiIssueInfo()) ?
                dsmtLinkHelperAppInfo.getCitiIssueInfo().getAncestorAuditsList() :
                null;
        logger.info("Ancestor Audits: \n" + ancestorAudits);

        if (isSetNotNullOrEmpty(ancestorAudits)) {

            citiIssueInfo = dsmtLinkHelperAppInfo.getCitiIssueInfo();
            citiIssueInfo = isObjectNotNull(citiIssueInfo) ? citiIssueInfo : new CitiIssueInfo();
            citiIssueInfo.setAncestorAuditsList(ancestorAudits);
            dsmtLinkHelperAppInfo.setCitiIssueInfo(citiIssueInfo);
            existinDSMTLinkBaseIdsList = getBaseIdsOfExistingDSMTLinksOfIssue(dsmtLinkHelperAppInfo);

            for (String auditId : ancestorAudits) {

                query = AVAILABLE_DSMT_LINK_FROM_ANCESTOR_AUDI_BASE_QUERY + auditId;
                logger.info("Existing Query executed: " + query);
                resultSet = grcObjectSearchUtil.executeQuery(query);
                dsmtLinksGroupedByAEMap = groupAvailableDSMTLinksToParentAuditableEntity(dsmtLinkHelperAppInfo,
                                                                                         resultSet,
                                                                                         dsmtLinksGroupedByAEMap,
                                                                                         existinDSMTLinkBaseIdsList);
            }
            availableDSMTLinksList = processDSMTLinkInfoMapForDisplay(dsmtLinksGroupedByAEMap, availableDSMTLinksList);
        }

        logger.info("processSearchForAuditIssueDraftStatus() End");
        return availableDSMTLinksList;
    }

    private List<ExistingDSMTLinkInfo> processSearchForDEAOETAuditIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
            throws Exception {

        logger.info("processSearchForDEAOETAuditIssue() Start");

        String query;
        ITabularResultSet resultSet;
        Set<String> ancestorControls;
        List<String> existinDSMTLinkBaseIdsList;
        List<ExistingDSMTLinkInfo> availableDSMTLinksList;
        Map<String, List<ExistingDSMTLinkInfo>> dsmtLinksGroupedByAEMap;

        availableDSMTLinksList = new ArrayList<>();
        dsmtLinksGroupedByAEMap = new LinkedHashMap<>();

        ancestorControls = dsmtLinkHelperAppInfo.getCitiIssueInfo().getAncestorControlsList();
        logger.info("Ancestor Controls: \n" + ancestorControls);

        if (isSetNotNullOrEmpty(ancestorControls)) {

            existinDSMTLinkBaseIdsList = getBaseIdsOfExistingDSMTLinksOfIssue(dsmtLinkHelperAppInfo);

            for (String controlId : ancestorControls) {

                query = AVAILABLE_DSMT_LINK_FROM_ANCESTOR_CONTROL_BASE_QUERY + controlId;
                logger.info("Existing Query executed: " + query);
                resultSet = grcObjectSearchUtil.executeQuery(query);
                dsmtLinksGroupedByAEMap = groupAvailableDSMTLinksToParentAuditableEntity(dsmtLinkHelperAppInfo,
                                                                                         resultSet,
                                                                                         dsmtLinksGroupedByAEMap,
                                                                                         existinDSMTLinkBaseIdsList);
            }

            availableDSMTLinksList = processDSMTLinkInfoMapForDisplay(dsmtLinksGroupedByAEMap, availableDSMTLinksList);
        }

        logger.info("processSearchForDEAOETAuditIssue() End");
        return availableDSMTLinksList;
    }

    private List<ExistingDSMTLinkInfo> processSearchForAuditIssueOtherStatus(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            List<String> existinDSMTLinkBaseIdsList) throws Exception {

        logger.info("processSearchForAuditIssueOtherStatus() Start");

        int count;
        int maxAEToDisplay;
        String query;
        String aeFolderPath;
        ITabularResultSet resultSet;
        List<String> ancestorAuditableEntities;
        List<ExistingDSMTLinkInfo> availableDSMTLinksList;
        Map<String, List<ExistingDSMTLinkInfo>> dsmtLinksGroupedByAEMap;

        availableDSMTLinksList = new ArrayList<>();
        dsmtLinksGroupedByAEMap = new LinkedHashMap<>();
        aeFolderPath = applicationUtil.getRegistrySetting(
                dsmtLinkHelperInfo.getObjRegistrySetting() + PARENT_AE_FOLDER_TO_SEARCH);
        query = FETCH_ALL_ANCESTOR_AUDITABLE_ENTITY_QUERY.replaceAll(AUDITABLE_ENTITY_STR,
                                                                     dsmtLinkHelperInfo.getObjectSearchText());
        query = query.replaceAll(AUDITABLE_ENTITY_PATH_STR, aeFolderPath);
        logger.info("Ancestor Auditable Entity Query: \n" + query);
        ancestorAuditableEntities = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, false, true);
        maxAEToDisplay = getIntValue(
                applicationUtil.getRegistrySetting(dsmtLinkHelperInfo.getObjRegistrySetting() + MAX_AE_SEARCH_RESULT));
        logger.info("Max Auditable Entities to display: \n" + maxAEToDisplay);
        logger.info("List of available ancestor Auditable Entities: \n" + ancestorAuditableEntities);

        if (isListNotNullOrEmpty(ancestorAuditableEntities)) {

            if (ancestorAuditableEntities.size() > maxAEToDisplay) {

                CitiIssueInfo citiIssueInfo = dsmtLinkHelperInfo.getCitiIssueInfo();
                citiIssueInfo = isObjectNotNull(citiIssueInfo) ? citiIssueInfo : new CitiIssueInfo();
                citiIssueInfo.setAeSearchWarning(TOO_MANY_RESULTS_WARNING);
            }

            for (String ancestorAEId : ancestorAuditableEntities) {

                query = AVAILABLE_DSMT_LINK_FROM_ANCESTOR_AUDIABLE_ENTITY_QUERY + ancestorAEId;
                logger.info("Available DSMT Link from ancestory Auditable Entity Query: \n" + query);

                resultSet = grcObjectSearchUtil.executeQuery(query);
                dsmtLinksGroupedByAEMap = groupAvailableDSMTLinksToParentAuditableEntity(dsmtLinkHelperInfo, resultSet,
                                                                                         dsmtLinksGroupedByAEMap,
                                                                                         existinDSMTLinkBaseIdsList);
                count = dsmtLinksGroupedByAEMap.size();
                if (count >= maxAEToDisplay) {
                    break;
                }
            }
            availableDSMTLinksList = processDSMTLinkInfoMapForDisplay(dsmtLinksGroupedByAEMap, availableDSMTLinksList);
        }

        logger.info("processSearchForAuditIssueOtherStatus() End");
        return availableDSMTLinksList;
    }
}
