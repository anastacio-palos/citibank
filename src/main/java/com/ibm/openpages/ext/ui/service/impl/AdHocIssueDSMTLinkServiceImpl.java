package com.ibm.openpages.ext.ui.service.impl;

import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.service.IIssueDSMTLinkService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isListNotNullOrEmpty;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.*;

@Service("adHocIssueDSMTLinkServiceImpl")
public class AdHocIssueDSMTLinkServiceImpl  extends IssueDSMTLinkBaseServiceImpl implements IIssueDSMTLinkService {

    /**
     * <p>
     * This method retrieves a part of the Helper Landing Page Content Section. On conditional basis pre existing
     * associated object information is retrieved. The link object association information is set in an instance of the
     * {@link DataGridInfo}
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects association information set
     * @throws Exception
     */
    @Override
    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getAssociatedObjectInformation() Start");

        String query;
        ITabularResultSet resultSet;
        List<String> fieldNamesList;
        DataGridInfo existingQuadsInfo;
        List<DataGridHeaderColumnInfo> gridHeaderInfoList;
        List<ExistingDSMTLinkInfo> existingDMSTLinkInfoList;
        Map<String, List<ExistingDSMTLinkInfo>> associatedDSMTLinksInfoMap;

        existingQuadsInfo = new DataGridInfo();
        associatedDSMTLinksInfoMap = new LinkedHashMap<>();
        fieldNamesList = parseCommaDelimitedValues(EXISTING_DSMT_LINK_TABLE_HEADER_FIELDS_INFO);
        gridHeaderInfoList = dsmtLinkHelperUtil.getHeaderForDataGrid(fieldNamesList);

        query = ASSOCIATED_DSMT_LINK_BASE_QUERY + dsmtLinkHelperInfo.getObjectID();
        logger.info("Associated DSMT Link Query: \n" + query);
        resultSet = grcObjectSearchUtil.executeQuery(query);
        associatedDSMTLinksInfoMap = groupDSMTLinksToParentAuditableEntity(resultSet, associatedDSMTLinksInfoMap,
                                                                           new ArrayList<>());
        existingDMSTLinkInfoList = processDSMTLinkInfoMapForDisplay(associatedDSMTLinksInfoMap, new ArrayList<>());
        existingQuadsInfo.setHeaders(gridHeaderInfoList);
        existingQuadsInfo.setRows(existingDMSTLinkInfoList);
        logger.info("getAssociatedObjectInformation() End");
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

        String query;
        CitiIssueInfo citiIssueInfo;
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

        existingDMSTLinkInfoList = processSearchForAuditIssueDraftStatus(dsmtLinkHelperInfo,
                                                                         existinDSMTLinkBaseIdsList);

        existingQuadsInfo.setHeaders(gridHeaderInfoList);
        existingQuadsInfo.setRows(existingDMSTLinkInfoList);
        logger.info("getAvailableDSMTLinks() End");
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
     * @return
     * @throws Exception
     */
    @Override
    public DSMTLinkHelperAppInfo addDSMTLinksToIssue(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        return null;
    }

    @Override
    public DSMTLinkHelperAppInfo descopeDSMTLinksFromIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo)
            throws Exception {

        return null;
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
    private Map<String, List<ExistingDSMTLinkInfo>> groupDSMTLinksToParentAuditableEntity(ITabularResultSet resultSet,
            Map<String, List<ExistingDSMTLinkInfo>> dsmtLinksGroupedByAEMap, List<String> existinDSMTLinkBaseIdsList)
            throws Exception {

        logger.info("populateDSMTLinkInfo() Start");

        String dsmtBaseId;
        String dsmtObjLink;
        String legalVehicleName;
        String auditableEntityId;
        String managedSegmentName;
        String managedGeographyName;
        IGRCObject dsmtLinkObj;
        ExistingDSMTLinkInfo dsmtLinkObjectInfo;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList;

        logger.info("Existing DSMT-Links Base Id list: " + existinDSMTLinkBaseIdsList);
        dsmtLinksGroupedByAEMap = isObjectNull(dsmtLinksGroupedByAEMap) ?
                new LinkedHashMap<>() :
                dsmtLinksGroupedByAEMap;

        for (IResultSetRow row : resultSet) {

            dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();
            dsmtBaseId = fieldUtil.getFieldValueAsString(row.getField(7));
            logger.info("Base Id for current DSMT Link: " + dsmtBaseId);
            logger.info("Duplicate Check returns: " + (isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(
                    existinDSMTLinkBaseIdsList) || (isListNotNullOrEmpty(
                    existinDSMTLinkBaseIdsList) && !existinDSMTLinkBaseIdsList.contains(dsmtBaseId))));

            if (isNullOrEmpty(dsmtBaseId) || isListNullOrEmpty(existinDSMTLinkBaseIdsList) || (isListNotNullOrEmpty(
                    existinDSMTLinkBaseIdsList) && !existinDSMTLinkBaseIdsList.contains(dsmtBaseId))) {

                dsmtLinkObj = grcObjectUtil.getObjectFromId(fieldUtil.getFieldValueAsString(row.getField(1)));
                auditableEntityId = fieldUtil.getFieldValueAsString(row.getField(6));
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
                dsmtLinkObjectInfo.setStatus(fieldUtil.getFieldValueAsString(row.getField(5)));
                dsmtLinkObjectInfo.setActive(fieldUtil.getFieldValueAsString(row.getField(8)));

                dsmtObjLink = dsmtLinkHelperUtil
                        .getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(), dsmtLinkObjectInfo.getName());

                dsmtLinkObjectInfo.setLink(dsmtObjLink);
                dsmtLinkObjectInfo.setLegalvehiclename(legalVehicleName);
                dsmtLinkObjectInfo.setManagedsegmentname(managedSegmentName);
                dsmtLinkObjectInfo.setManagedgeographyname(managedGeographyName);
                dsmtLinkObjectInfo.setParentResourceId(fieldUtil.getFieldValueAsString(row.getField(6)));

                dsmtLinkObjectList = dsmtLinksGroupedByAEMap.get(auditableEntityId);
                dsmtLinkObjectList = isListNotNullOrEmpty(dsmtLinkObjectList) ? dsmtLinkObjectList : new ArrayList<>();
                dsmtLinkObjectList.add(dsmtLinkObjectInfo);
                dsmtLinksGroupedByAEMap.put(auditableEntityId, dsmtLinkObjectList);
            }
        }

        logger.info("populateDSMTLinkInfo() End");
        return dsmtLinksGroupedByAEMap;
    }

    private List<ExistingDSMTLinkInfo> processDSMTLinkInfoMapForDisplay(
            Map<String, List<ExistingDSMTLinkInfo>> dsmtLinkInfoMap, List<ExistingDSMTLinkInfo> availableDSMTLinksList)
            throws Exception {

        int childCount;
        int rowCount = 1;
        String auditEntityType;
        String auditableEntityName;
        String auditableEntityDesc;

        List<String> hierarchyList;
        IGRCObject audEntityObject;
        ExistingDSMTLinkInfo existingDSMTLinkInfo;
        List<ExistingDSMTLinkInfo> childDMSTLinkInfoList;

        if (isMapNotNullOrEmpty(dsmtLinkInfoMap)) {

            availableDSMTLinksList = isObjectNotNull(availableDSMTLinksList) ?
                    availableDSMTLinksList : new ArrayList<>();

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
                    }
                }

                rowCount++;
                availableDSMTLinksList.add(existingDSMTLinkInfo);
            }
        }

        return availableDSMTLinksList;
    }

    private List<ExistingDSMTLinkInfo> processSearchForAuditIssueDraftStatus(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            List<String> existinDSMTLinkBaseIdsList) throws Exception {

        logger.info("searchDSMT() Start");

        String query;
        List<String> ancestorAudits;
        ITabularResultSet resultSet;
        List<ExistingDSMTLinkInfo> availableDSMTLinksList;
        Map<String, List<ExistingDSMTLinkInfo>> dsmtLinksGroupedByAEMap;

        availableDSMTLinksList = new ArrayList<>();
        dsmtLinksGroupedByAEMap = new LinkedHashMap<>();
        query = FETCH_ANCESTOR_AUDITS_QUERY + dsmtLinkHelperInfo.getObjectID();
        logger.info("Associated DSMT Link Query: \n" + query);
        ancestorAudits = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, false);

        if (isListNotNullOrEmpty(ancestorAudits)) {

            for (String auditId : ancestorAudits) {

                query = AVAILABLE_DSMT_LINK_FROM_ANCESTOR_AUDI_BASE_QUERY + auditId;
                resultSet = grcObjectSearchUtil.executeQuery(query);
                dsmtLinksGroupedByAEMap = groupDSMTLinksToParentAuditableEntity(resultSet, dsmtLinksGroupedByAEMap,
                                                                                existinDSMTLinkBaseIdsList);
            }
            availableDSMTLinksList = processDSMTLinkInfoMapForDisplay(dsmtLinksGroupedByAEMap, availableDSMTLinksList);
        }

        logger.info("searchDSMT() End");
        return availableDSMTLinksList;
    }

    private List<ExistingDSMTLinkInfo> processSearchForAuditIssueOtherStatus(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            List<String> existinDSMTLinkBaseIdsList) throws Exception {

        logger.info("processSearchForAuditIssueOtherStatus() Start");

        String query;
        List<String> ancestorAuditableEntities;
        ITabularResultSet resultSet;
        List<ExistingDSMTLinkInfo> availableDSMTLinksList;
        Map<String, List<ExistingDSMTLinkInfo>> dsmtLinksGroupedByAEMap;

        availableDSMTLinksList = new ArrayList<>();
        dsmtLinksGroupedByAEMap = new LinkedHashMap<>();
        query = FETCH_ALL_ANCESTOR_AUDITABLE_ENTITY_QUERY
                .replaceAll(AUDITABLE_ENTITY_STR, dsmtLinkHelperInfo.getObjectSearchText());
        logger.info("Ancestor Auditable Entity Query: \n" + query);
        ancestorAuditableEntities = grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query, false);
        logger.info("List of available ancestor Auditable Entities: \n" + ancestorAuditableEntities);

        if (isListNotNullOrEmpty(ancestorAuditableEntities)) {

            for (String ancestorAEId : ancestorAuditableEntities) {

                query = AVAILABLE_DSMT_LINK_FROM_ANCESTOR_AUDIABLE_ENTITY_QUERY + ancestorAEId;
                logger.info("Available DSMT Link from ancestory Auditable Entity Query: \n" + query);

                resultSet = grcObjectSearchUtil.executeQuery(query);
                dsmtLinksGroupedByAEMap = groupDSMTLinksToParentAuditableEntity(resultSet, dsmtLinksGroupedByAEMap,
                                                                                existinDSMTLinkBaseIdsList);
            }
            availableDSMTLinksList = processDSMTLinkInfoMapForDisplay(dsmtLinksGroupedByAEMap, availableDSMTLinksList);
        }

        logger.info("processSearchForAuditIssueOtherStatus() End");
        return availableDSMTLinksList;
    }
}
