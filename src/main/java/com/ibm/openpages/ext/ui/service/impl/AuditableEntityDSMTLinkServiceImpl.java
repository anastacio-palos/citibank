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
import com.ibm.openpages.ext.model.DSMTTripletModel;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;
import com.ibm.openpages.ext.tss.service.beans.IGRCObjectCreateInformation;
import com.ibm.openpages.ext.tss.service.util.NumericUtil;
import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.dao.IDSMTHelperAppBaseDAO;
import com.ibm.openpages.ext.ui.service.IAuditableEntityDSMTLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.*;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.ui.constant.AuditableEntityDSMTLinkConstants.*;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.*;
import static com.ibm.openpages.ext.ui.constant.DSMTSearchQueryConstants.*;

/**
 * <p>
 * This class works as the Controller to branch the request coming into the AuditableEntity DSMTLink helper controller.
 * All requests are handled by their corresponding methods marked by the request mapping.
 * </P>
 *
 * @author : Praveen Ravi <BR>
 * email : raviprav@us.ibm.com <BR>
 * company : IBM OpenPages
 * @version : OpenPages 8.2.0
 * @custom.date : 02-15-2021
 * @custom.feature : Helper Service
 * @custom.category : Helper App
 */
@Service("auditableEntityDSMTLinkService")
public class AuditableEntityDSMTLinkServiceImpl extends DSMTLinkBaseServiceImpl implements IAuditableEntityDSMTLinkService {

    @Autowired
    IDSMTHelperAppBaseDAO dsmtHelperAppBaseDAO;

    /**
     * Construct any information once the bean is created.
     */
    @PostConstruct
    public void initServiceImpl() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(AUDITABLE_ENTITY_DSMT_LINK_SERVICE_LOG_FILE_NAME);
    }

    /**
     * <p>
     * This method retrieves the Helpers Header Information from the Registry settings and sets the appropriate values
     * in the {@link } object. This will be used to display the content in the UI Header.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the helper header information set
     * @throws Exception - Generic Exception
     */
    @Override
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
    @Override
    public DSMTLinkHelperAppInfo getLandingPageInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getLandingPageInfo() Start");

        // Method Level Variables.

        /* Initialize Variables */

        // Get the Basic Object information of the current object under execution
        getBasicObjectInfoForDisplay(dsmtLinkHelperInfo);

        logger.info("getLandingPageInfo() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * <p>
     * This method retrieves a part of the Helper Landing Page Content Section. On conditional basis pre existing
     * associated object information is retrieved. The link object association information is set in an instance of the
     * {@link DSMTLinkObjectAssociationInfo}
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects association information set
     * @throws Exception - Generic Exception
     */
    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getAssociatedObjectInformation() Start");

        /* Initialize Variables */
        StringBuilder query;

        List<String> headerFieldNamesList;
        DataGridInfo existingDSMTLinksGridInfo;
        List<ExistingDSMTLinkInfo> existingQuadsInfoList;
        List<DataGridHeaderColumnInfo> availableDSMTLinkHeaderList;

        /* Method Implementation */
        query = new StringBuilder();
        existingDSMTLinksGridInfo = new DataGridInfo();
        headerFieldNamesList = parseCommaDelimitedValues(EXISTING_DSMT_LINK_TABLE_HEADER_FIELDS_INFO);

        availableDSMTLinkHeaderList = dsmtLinkHelperUtil.getHeaderForDataGrid(headerFieldNamesList);
        logger.info("Available DSMT Link Header List: " + availableDSMTLinkHeaderList);

        /* Build the query using the information in the session */
        query.append(dsmtLinkServiceUtil.buildExistingDSMTLinksQuery(dsmtLinkHelperInfo));
        logger.info("Query Formed: " + query);

        /* Once the query is executed process the results to be displayed in the AG Grid */
        existingQuadsInfoList = populateQueryResults(dsmtLinkHelperInfo, query.toString());

        /* Set the table and header information in the Grid */
        existingDSMTLinksGridInfo.setHeaders(availableDSMTLinkHeaderList);
        existingDSMTLinksGridInfo.setRows(existingQuadsInfoList);

        /* Log the information and return the Grid */
        logger.info("Existing Quads Info Grid Information: " + existingDSMTLinksGridInfo);
        logger.info("getAssociatedObjectInformation() End");
        return existingDSMTLinksGridInfo;
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
     * @throws Exception - Generic Exception
     */
    @Override
    public DataGridInfo searchDSMT(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("searchDSMT() Start");

        // Method Level Variables.
        List<DSMTLinkSearchResultInfo> searchDSMTInfoList;
        List<DataGridHeaderColumnInfo> searchDSMTHeaderList;

        DSMTLinkSearchInfo dsmtSearchInfo;
        DataGridInfo searchDSMTLinksGridInfo;

        /* Initialize Variables */
        searchDSMTLinksGridInfo = new DataGridInfo();
        dsmtSearchInfo = dsmtLinkHelperInfo.getDsmtSearchInfo();

        /* Process and get the Search Details information based on the Node */
        getSearchDetailsInformation(dsmtSearchInfo);

        /*
         * Get the search results in the tabular format the UI requires and process the header information for the UI
         * table.
         */
        searchDSMTInfoList = getSearchResults(dsmtLinkHelperInfo, dsmtSearchInfo);
        searchDSMTHeaderList = processHeaderLabelList(dsmtSearchInfo.getHeaderFieldNamesList());

        /* Set the table rows and header and return */
        searchDSMTLinksGridInfo.setRows(searchDSMTInfoList);
        searchDSMTLinksGridInfo.setHeaders(searchDSMTHeaderList);

        logger.info("searchDSMT() Start");
        return searchDSMTLinksGridInfo;
    }

    /**
     * <p>
     * This method retrieves the child DSMT objects to display in the tree from the DSMT DB based on parent node
     * selected. The search information is present in the {@link DSMTLinkSearchInfo} in the given dsmtLinkHelperInfo.
     * The information present in the dsmtSearchInfo drives the query to get the search results back. Possible areas of
     * search are - Managed Segment - Managed Geography - Legal Vehicle The queries are constructed based on the Node
     * selected and the parent node id selected in the dsmtSearchInfo.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception - Generic Exception
     */
    @Override
    public DataGridInfo searchDSMTTree(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("searchDSMTTree() Start");

        // Method Level Variables.
        List<DSMTLinkSearchResultInfo> searchDSMTInfoList;
        List<DataGridHeaderColumnInfo> searchDSMTHeaderList;

        DSMTLinkSearchInfo dsmtSearchInfo;
        DataGridInfo searchDSMTLinksGridInfo;

        /* Initialize Variables */
        searchDSMTLinksGridInfo = new DataGridInfo();
        dsmtSearchInfo = dsmtLinkHelperInfo.getDsmtSearchInfo();
        searchDSMTHeaderList = new ArrayList<>();

        /* Process and get the Search Details information based on the Node */
        getSearchDetailsInformation(dsmtSearchInfo);
        dsmtSearchInfo.setParentNodeHierarchy(dsmtSearchInfo.getParentNodeHierarchy());

        /*
         * Get the search results in the tabular format the UI requires and process the header information for the UI
         * table.
         */
        searchDSMTInfoList = dsmtSearchInfo.isTopLevel() ?
                getTopLevelTreeSearchResults(dsmtSearchInfo) :
                getSearchResults(dsmtLinkHelperInfo, dsmtSearchInfo);

        /* Set the table rows and header and return s */
        searchDSMTLinksGridInfo.setRows(searchDSMTInfoList);
        searchDSMTLinksGridInfo.setHeaders(searchDSMTHeaderList);

        logger.info("searchDSMTTree() End");
        return searchDSMTLinksGridInfo;
    }

    /**
     * <p>
     * This method checks if each node in the selected DSMT triplet is within range. If any of the nodes are out of
     * range then the user will have to enter a rationale before he can request to create the DSMT Link. This will also
     * help us get the nodes that are out of range and updates the out of range field accordingly.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - a list of {@link CreateDSMTLinkForAudEntityInfo} that has the selected DSMT triplet information
     * @throws Exception - Generic Exception
     */
    @Override
    public List<CreateDSMTLinkForAudEntityInfo> processNodeRangeCheck(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        logger.info("processNodeRangeCheck() Start");

        // Method Level Variables.
        String mngdSegNodeLevels;
        String mngdGeoNodeLevels;
        String LegalVehicleNodeLevels;
        String mngdSegNodeLevlRegSetting;
        String mngdGeoNodeLevlRegSetting;
        String LegalVehicleNodeLevlRegSetting;

        List<String> outOfRangeNodes;
        List<String> mngdSegNodeLevelsList;
        List<String> mngdGeoNodeLevelsList;
        List<CreateDSMTLinkForAudEntityInfo> associateNewDSMTInfoList;

        /* Initialize Variables */
        associateNewDSMTInfoList = dsmtLinkHelperInfo.getAssociateNewDSMTInfoList();
        mngdSegNodeLevlRegSetting = dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_MANAGED_SEGMENT_NODE_LEVEL_RANGE_SETTING;
        mngdGeoNodeLevlRegSetting = dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_MANAGED_GEOGRAPHY_NODE_LEVEL_RANGE_SETTING;
        LegalVehicleNodeLevlRegSetting = dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LEGAL_VEHICLE_NODE_LEVEL_RANGE_SETTING;

        logger.info("Managed Segment Node Levels Setting: " + mngdSegNodeLevlRegSetting);
        logger.info("Managed Geography Node Levels Setting: " + mngdGeoNodeLevlRegSetting);
        logger.info("Legal Vehicle Node Levels Setting: " + LegalVehicleNodeLevlRegSetting);

        mngdSegNodeLevels = applicationUtil.getRegistrySetting(mngdSegNodeLevlRegSetting);
        mngdGeoNodeLevels = applicationUtil.getRegistrySetting(mngdGeoNodeLevlRegSetting);
        LegalVehicleNodeLevels = applicationUtil.getRegistrySetting(LegalVehicleNodeLevlRegSetting);

        logger.info("Managed Segment Node Levels: " + mngdSegNodeLevels);
        logger.info("Managed Geography Node Levels: " + mngdGeoNodeLevels);
        logger.info("Legal Vehicle Node Levels: " + LegalVehicleNodeLevels);

        mngdSegNodeLevelsList = parseCommaDelimitedValues(mngdSegNodeLevels);
        mngdGeoNodeLevelsList = parseCommaDelimitedValues(mngdGeoNodeLevels);
        // legalVehicleNodeLevelsList = parseCommaDelimitedValues(LegalVehicleNodeLevels);

        /* Iterate through the DSMT items selected to be created */
        for (CreateDSMTLinkForAudEntityInfo associateNewDSMTInfo : associateNewDSMTInfoList) {

            outOfRangeNodes = new ArrayList<>();
            // String legVehNodeLevel = associateNewDSMTInfo.getLegalVehicleLevel();
            String mgSegNodeLevel = associateNewDSMTInfo.getManagedSegmentLevel();
            String mgGeoNodeLevel = associateNewDSMTInfo.getManagedGeographyLevel();

            logger.info("Managed Segment Level: " + mgSegNodeLevel);
            logger.info("Managed Segment Levels List: " + (mngdSegNodeLevelsList));
            logger.info("Managed Segment Levels List null or empty: " + isListNullOrEmpty(mngdSegNodeLevelsList));
            logger.info(
                    "Managed Segment Levels List not null or empty: " + isListNotNullOrEmpty(mngdSegNodeLevelsList));
            logger.info("Managed Segment Node list does not contain the level: " + (!mngdSegNodeLevelsList.contains(
                    mgSegNodeLevel)));
            logger.info(
                    "Managed Segment Condition: " + (isListNullOrEmpty(mngdSegNodeLevelsList) || (isListNotNullOrEmpty(
                            mngdSegNodeLevelsList) && !mngdSegNodeLevelsList.contains(mgSegNodeLevel))));

            logger.info("Managed Segment Level: " + mgGeoNodeLevel);
            logger.info("Managed Segment Levels List: " + (mngdGeoNodeLevelsList));
            logger.info("Managed Segment Levels List null or empty: " + isListNullOrEmpty(mngdGeoNodeLevelsList));
            logger.info(
                    "Managed Segment Levels List not null or empty: " + isListNotNullOrEmpty(mngdGeoNodeLevelsList));
            logger.info("Managed Segment Node list does not contain the level: " + (!mngdGeoNodeLevelsList.contains(
                    mgGeoNodeLevel)));
            logger.info(
                    "Managed Segment Condition: " + (isListNullOrEmpty(mngdGeoNodeLevelsList) || (isListNotNullOrEmpty(
                            mngdGeoNodeLevelsList) && !mngdGeoNodeLevelsList.contains(mgGeoNodeLevel))));

            /*
             * Make sure the Managed Segment Node Level list is not null and if the managed segmeent node of the current
             * DSMT is present in the list, then add the node name to the list
             */
            if (isListNullOrEmpty(mngdSegNodeLevelsList) || (isListNotNullOrEmpty(
                    mngdSegNodeLevelsList) && !mngdSegNodeLevelsList.contains(mgSegNodeLevel))) {
                outOfRangeNodes.add(MANAGED_SEGMENT_NODE_NAME);
            }
            /*
             * Make sure the Managed Geography Node Level list is not null and if the managed geography node of the
             * current DSMT is present in the list, then add the node name to the list
             */
            if (isListNullOrEmpty(mngdGeoNodeLevelsList) || (isListNotNullOrEmpty(
                    mngdGeoNodeLevelsList) && !mngdGeoNodeLevelsList.contains(mgGeoNodeLevel))) {
                outOfRangeNodes.add(MANAGED_GEOGRAPHY_NODE_NAME);
            }
            /*
             * Make sure the Legal Vehicle Node Level list is not null and if the legal vehicle node of the current DSMT
             * is present in the list, then add the node name to the list
             *
             * if (isListNullOrEmpty(legalVehicleNodeLevelsList) || (isListNotNullOrEmpty(legalVehicleNodeLevelsList) &&
             * !legalVehicleNodeLevelsList.contains(legVehNodeLevel))) outOfRangeNodes.add(LEGAL_VEHICLE_NODE_NAME);
             */

            /* Finally set the value for the selected item and move to next */
            associateNewDSMTInfo.setOutOfRange(isListNotNullOrEmpty(outOfRangeNodes));
            associateNewDSMTInfo.setOutOfRangeNodes(outOfRangeNodes);
        }

        /* Log the information for debug and return */
        logger.info("Node Range check lists: " + associateNewDSMTInfoList);
        logger.info("processNodeRangeCheck() End");
        return associateNewDSMTInfoList;
    }

    /**
     * <p>
     * This method creates a DSMT Link with the selected DSMT's, and associates to the AuditableEntity updates the
     * fields in the created DSMT Link. The values of the fields are determined by the status of the AudtitableEntity.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - an instance of the DSMTLinkHelperAppInfo which has the information on creating new DSMT Links
     * @throws Exception - Generic Exception
     */
    @Override
    public DSMTLinkHelperAppInfo addDSMTLinksToAuditableEntity(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        logger.info("addDSMTLinksToAuditableEntity() Start");

        // Method Level Variables.
        Id auditableEntityId;
        String existingDSMTID;
        String audEntityStatus;

        IGRCObject dsmtLinkObject;
        IGRCObject audEntityObject;
        Map<String, String> fieldsToUpdateInfo;

        /* Initialize Variables */
        auditableEntityId = new Id(dsmtLinkHelperInfo.getObjectID());

        /* Get the AuditableEntity information based on the information in the session */
        audEntityObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        audEntityStatus = fieldUtil.getFieldValueAsString(audEntityObject, AUD_ENTITY_STATUS_FIELD_INFO);

        /* Log the information for debug */
        logger.info("DSMT Link object will be created under: " + dsmtLinkHelperInfo.getObjectID());
        logger.info("Auditable Entity Status: " + audEntityStatus);

        /*
         * Make sure if the list of dsmt's to associate is not null or empty and proceed only if the status of the
         * AuditableEntity is Draft or Active.
         */
        if (isListNotNullOrEmpty(dsmtLinkHelperInfo.getAssociateNewDSMTInfoList()) && ((isEqual(AUD_ENTITY_DRAFT_STATUS,
                                                                                                audEntityStatus)) || (isEqual(
                AUD_ENTITY_ACTIVE_STATUS, audEntityStatus)))) {

            /* Iterate through the list of the DSMT's to add */
            for (CreateDSMTLinkForAudEntityInfo createNewDSMTInfo : dsmtLinkHelperInfo.getAssociateNewDSMTInfoList()) {

                /* Prepare the map to update the newly created/ existing DSMTL Link object */
                fieldsToUpdateInfo = new HashMap<>();
                logger.info("Is DSMT Out of Range" + createNewDSMTInfo.isOutOfRange());
                logger.info("Is Draft Status: " + isEqual(AUD_ENTITY_DRAFT_STATUS, audEntityStatus));
                logger.info("Is Active Status: " + isEqual(AUD_ENTITY_ACTIVE_STATUS, audEntityStatus));

                /* Check if a DSMT Link object is already present with the DSMT Triplet information
                 * Process the create and get the DSMT Link object and update the created DSMT Link object id
                 * in the Create new DSMT information for further processing.
                 */
                existingDSMTID = checkExistingDSMTLinksWithSameTriplets(dsmtLinkHelperInfo, createNewDSMTInfo);
                dsmtLinkObject = processCreationOFDSMTLinkObject(createNewDSMTInfo, auditableEntityId, existingDSMTID);
                createNewDSMTInfo.setExistingDSMTResourceId(existingDSMTID);

                /*  Prepare the fields to update and set the Base Id and the Auditable Entity Id. */
                fieldsToUpdateInfo.put(DSMT_LINK_BASE_ID_FIELD, dsmtLinkObject.getId().toString());
                fieldsToUpdateInfo.put(DSMT_LINK_AUDITABLE_ENTITY_ID_FIELD, dsmtLinkHelperInfo.getObjectID());
                processCommonFieldsForAssociation(createNewDSMTInfo, fieldsToUpdateInfo);

                /*
                 * Based on the status and the out of range status update the created objects with the processed
                 * fields information.
                 */
                if (isEqual(AUD_ENTITY_DRAFT_STATUS, audEntityStatus)) {

                    if (createNewDSMTInfo.isOutOfRange()) {
                        processAssociationForAEDraftStatusOutOfRange(createNewDSMTInfo, fieldsToUpdateInfo);
                    }
                    else {
                        processAssociationForAEDraftStatusWithinRange(createNewDSMTInfo, fieldsToUpdateInfo);
                    }
                }
                else if (isEqual(AUD_ENTITY_ACTIVE_STATUS, audEntityStatus)) {

                    if (createNewDSMTInfo.isOutOfRange()) {
                        processAssociationForAEActiveStatusOutOfRange(createNewDSMTInfo, fieldsToUpdateInfo);
                    }
                    else {
                        processAssociationForAEActiveStatusWithinRange(createNewDSMTInfo, fieldsToUpdateInfo);
                    }
                }

                // Update the object and save
                logger.info("Final Set of Fields to update on object: " + fieldsToUpdateInfo);
                processFieldsToUpdateIfDSMTExists(fieldsToUpdateInfo, existingDSMTID);
                grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                logger.info("Object Updated");
            }
        }

        logger.info("addDSMTLinksToAuditableEntity() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * <p>
     * This method will determine if a new DSMT Link object should be created based on the existingDSMTLinkId. If
     * the existingDSMTLinkId is present the method returns the IGRCObject using the existingDSMTLinkId else it
     * creates a new DSMT Link object and associates it to the given auditableEntityId as a primary child and
     * returns the newly created IGRCObject.
     * </P>
     *
     * @param createNewDSMTInfo  - The parent Auditable Entity ID
     * @param auditableEntityId  - The parent Auditable Entity ID
     * @param existingDSMTLinkId - The Id of the Existing DSMT Link object
     * @return - an IGRCObject that is either a newly created object or the existing DSMT Link object
     * @throws Exception - Generic Exception
     */
    private IGRCObject processCreationOFDSMTLinkObject(CreateDSMTLinkForAudEntityInfo createNewDSMTInfo,
            Id auditableEntityId, String existingDSMTLinkId) throws Exception {

        logger.info("processCreationOFDSMTLinkObject() Start");

        // Method Level Variables.
        IGRCObject dsmtLinkObject;
        DSMTTripletModel dsmtTripletModel;
        IGRCObjectCreateInformation createDSMTLinkObjInfo;

        /* Method Implementation */
        /* Initialize Variables */
        dsmtTripletModel = new DSMTTripletModel();
        createDSMTLinkObjInfo = new IGRCObjectCreateInformation();

        /* If the an existing DSMT Object is not present. */
        if (isNullOrEmpty(existingDSMTLinkId)) {

            /* Set up all the necessary information to create a new DSMT link object */
            logger.info("DSMT Link object not present creating a new one");
            createDSMTLinkObjInfo.setSetPrimaryParent(true);
            createDSMTLinkObjInfo.setObjectTypeToCreate(DSMT_LINK_OBJECT_TYPE);
            createDSMTLinkObjInfo.setPrimaryParentAssociationId(auditableEntityId);


            /* Create an Auto named object, and associate the parent to the AuditableEntity */
            logger.info("Going to create object");
            dsmtLinkObject = grcObjectUtil.createAutoNamedObject(createDSMTLinkObjInfo);
            logger.info("Object Created");

            /* Associate the newly created object to the given auditableEntityId as its primary parent. */
            dsmtLinkObject = grcObjectUtil.associateParentAndChildrentToAnObject(dsmtLinkObject, createDSMTLinkObjInfo);
            logger.info("associated object");

            dsmtTripletModel.setStatus(DSMT_INSERT_STATUS);
            dsmtTripletModel.setLvid(createNewDSMTInfo.getLegalVehicleId());
            dsmtTripletModel.setTripletID(dsmtLinkObject.getId().toString());
            dsmtTripletModel.setMsid(createNewDSMTInfo.getManagedSegmentId());
            dsmtTripletModel.setMgid(createNewDSMTInfo.getManagedGeographyId());

            dsmtHelperAppBaseDAO.createDSMTTriplet(dsmtTripletModel);
        }
        /* A DSMT Link is already present, get it from the system */
        else {

            logger.info("DSMT Link object already present");
            dsmtLinkObject = grcObjectUtil.getObjectFromId(existingDSMTLinkId);
        }


        /* Log for debug and return the DSMT Link object */
        logger.info("Created new DSMT Link object: " + dsmtLinkObject.getName());
        logger.info("processCreationOFDSMTLinkObject() End");
        return dsmtLinkObject;
    }

    /**
     * <p>
     * This methods builds the query to get the existing DSMT Link objects for the current object from which the helper
     * was launched. The query is dynamically built based on the configured registry settings that has the object
     * hierarchy and the fields that needs to be retrieved for the associated DSMT link objects.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - a boolean value stating if the AuditableEntity has an existing DSMT link with the given DSMT triplet
     * combo
     * @throws Exception - Generic Exception
     */
    private String checkExistingDSMTLinksWithSameTriplets(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            CreateDSMTLinkForAudEntityInfo createDSMTInfo) throws Exception {

        logger.info("checkExistingDSMTLinksWithSameTriplets() Start");

        // Method Level Variables.
        String query;
        String baseQuery;
        String existingDSMTID;
        String parentObjHeirarchy;
        String parentObjHeirarchyRegSetting;

        StringBuilder checkQuery;
        List<String> parentObjHeirarchyList;
        List<String> existingDsmtLinkFieldsList;
        List<CreateDSMTLinkForAudEntityInfo> duplicateDSMTLinksOnCreate;

        /* Initialize Variables */
        // Get the parent object hierarchy from the registry setting and parse it into a list
        checkQuery = new StringBuilder();
        parentObjHeirarchyRegSetting = dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LINK_APP_PARENT_HIERARCHY;
        parentObjHeirarchy = applicationUtil.getRegistrySetting(parentObjHeirarchyRegSetting);
        parentObjHeirarchyList = parseCommaDelimitedValues(parentObjHeirarchy);

        /* Log for debugging */
        logger.info("Parent Object hierarchy reg setting: " + parentObjHeirarchyRegSetting);
        logger.info("Parent Object hierarchy: " + parentObjHeirarchy);
        logger.info("Parent Object hierarchy list: " + parentObjHeirarchyList);

        /* Get the fields whose values needs to be obtained from the registry setting and parse it into a list */
        existingDsmtLinkFieldsList = parseCommaDelimitedValues("Resource ID");

        /* Log for debugging */
        logger.info("Existing fields to display list: " + existingDsmtLinkFieldsList);

        /* Construct the base query with the field values that needs to be retrieved */
        baseQuery = dsmtLinkServiceUtil.getBaseQueryToGetFieldValues(parentObjHeirarchyList, existingDsmtLinkFieldsList,
                                                                     true, true);
        logger.info("Base Query: " + baseQuery);

        /* Append the base query with the Child Association based on the object hierarchy */
        query = dsmtLinkServiceUtil.appendBaseQueryToChildAssociationQuery(dsmtLinkHelperInfo, parentObjHeirarchyList,
                                                                           baseQuery);
        logger.info("Base Query with association: " + query);

        /* Append the DSMT triplet information */
        checkQuery.append(query);
        checkQuery.append(" ");
        checkQuery.append("AND [Citi_DSMT_Link].[Citi-DSMT:MSID] = '");
        checkQuery.append(createDSMTInfo.getManagedSegmentId());
        checkQuery.append("' ");
        checkQuery.append("AND [Citi_DSMT_Link].[Citi-DSMT:MGID] = '");
        checkQuery.append(createDSMTInfo.getManagedGeographyId());
        checkQuery.append("' ");
        checkQuery.append("AND [Citi_DSMT_Link].[Citi-DSMT:LVID] = '");
        checkQuery.append(createDSMTInfo.getLegalVehicleId());
        checkQuery.append("' ");
        checkQuery.append("AND [Citi_DSMT_Link].[Citi-DSMT:Type] = '");
        checkQuery.append(createDSMTInfo.getHomeOrImpacted());
        checkQuery.append("'");

        /* Execute the query and check the number of rows returned */
        logger.info("Finished Query: " + checkQuery);
        existingDSMTID = grcObjectSearchUtil.getSingleValueFromQuery(checkQuery.toString(), true);

        /*
         * If present preparer the duplicate DSMT information to display in the screen. This DSMT triplet wont be
         * created and user needs to be informed.
         */
        if (isNotNullOrEmpty(existingDSMTID)) {

            /*
             * Get the duplicate create DSMT info from the DataGridInfo, if no prior duplicate grid info found
             * initialize.
             */
            duplicateDSMTLinksOnCreate = dsmtLinkHelperInfo.getDuplicateDSMTLinksOnCreation();
            duplicateDSMTLinksOnCreate = isObjectNotNull(duplicateDSMTLinksOnCreate) ?
                    duplicateDSMTLinksOnCreate :
                    new ArrayList<>();

            /*
             * Add the duplicate entry to the list and add the list too the grid and the grid to the DSMTLinkHelperInfo.
             */
            duplicateDSMTLinksOnCreate.add(createDSMTInfo);
            dsmtLinkHelperInfo.setDuplicateDSMTLinksOnCreation(duplicateDSMTLinksOnCreate);
        }

        /* Log the information and return */
        logger.info("Existing DSMT ID: " + existingDSMTID);
        logger.info("checkExistingDSMTLinksWithSameTriplets() End");
        return existingDSMTID;
    }

    /**
     * <p>
     * This method process all the common fields that will be updated on Add new DSMT link to the AuditableEntity. Most
     * of these values are values entered in the screen or selected as part of the DSMT triplet in the UI.
     * <p>
     *
     * @param associateDSMTInfo  - an instance of the {@link CreateDSMTLinkForAudEntityInfo} that has all the info to create and
     *                           associate a DSMT Link to the AuditableEntity
     * @param fieldsToUpdateInfo - A Map that has the field to update as key and its value as the value of the map
     */
    private void processCommonFieldsForAssociation(CreateDSMTLinkForAudEntityInfo associateDSMTInfo,
            Map<String, String> fieldsToUpdateInfo) {

        logger.info("processCommonFieldsForAssocaition() Start");

        /* Initialize Variables */
        fieldsToUpdateInfo.put(DSMT_LINK_IS_OMU_FIELD, DSMT_LINK_FIELD_VALUE_NO);
        fieldsToUpdateInfo.put(DSMT_LINK_ACTIVE_FIELD, DSMT_LINK_FIELD_VALUE_YES);
        fieldsToUpdateInfo.put(DSMT_LINK_TYPE_FIELD, associateDSMTInfo.getHomeOrImpacted());

        fieldsToUpdateInfo.put(DSMT_LINK_REGION_FIELD, associateDSMTInfo.getDsmtRegion());
        fieldsToUpdateInfo.put(DSMT_LINK_COUNTRY_FIELD, associateDSMTInfo.getDsmtCountry());

        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_ID_FIELD, associateDSMTInfo.getManagedSegmentId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_NAME_FIELD, associateDSMTInfo.getManagedSegmentName());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_LEVEL_FIELD, associateDSMTInfo.getManagedSegmentLevel());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_HEIRARCHY, associateDSMTInfo.getManagedSegmentHierarcy());

        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_ID_FIELD, associateDSMTInfo.getManagedGeographyId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_NAME_FIELD, associateDSMTInfo.getManagedGeogprahyName());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_LEVEL_FIELD, associateDSMTInfo.getManagedGeographyLevel());

        fieldsToUpdateInfo.put(DSMT_LINK_LEGAL_VEHICLE_ID_FIELD, associateDSMTInfo.getLegalVehicleId());
        fieldsToUpdateInfo.put(DSMT_LINK_LEGAL_VEHICLE_NAME_FIELD, associateDSMTInfo.getLegalVehicleName());

        /* Log the information and return */
        logger.info("Common Fields For Association : " + fieldsToUpdateInfo);
        logger.info("processCommonFieldsForAssocaition() End");
    }

    /**
     * <p>
     * This method processes the fields for the Add DSMT Links to the AuditableEntity whose status is Draft and is
     * within range. The fields and their values are appended to the existing fields info map that is sent to the method
     * </P>
     *
     * @param associateDSMTInfo  - an instance of the {@link CreateDSMTLinkForAudEntityInfo} that has all the info to create
     *                           and associate a DSMT Link to the AuditableEntity
     * @param fieldsToUpdateInfo - A Map that has the field to update as key and its value as the value of the map
     */
    private void processAssociationForAEDraftStatusWithinRange(CreateDSMTLinkForAudEntityInfo associateDSMTInfo,
            Map<String, String> fieldsToUpdateInfo) throws Exception {

        logger.info("processAssociationForAEDraftStatusWihinRange() Start");

        /* Initialize Variables */
        String dsmtScope = EMPTY_STRING;
        IGRCObject existingDSMTLink;

        if (isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId())) {

            existingDSMTLink = grcObjectUtil.getObjectFromId(associateDSMTInfo.getExistingDSMTResourceId());
            dsmtScope = fieldUtil.getFieldValueAsString(existingDSMTLink, DSMT_LINK_SCOPE_FIELD);
        }

        logger.info("Existing Resource ID present: " + isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()));
        logger.info("DSMT Scope: " + dsmtScope);

        logger.info("Condtion 1: " + isNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()));
        logger.info("Condtion 2: " + (isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(
                dsmtScope, DSMT_LINK_FIELD_VALUE_IN)));

        logger.info("Total Condition: " + (isNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) || (isNotNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(dsmtScope, DSMT_LINK_FIELD_VALUE_IN))));

        if (isNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()) || (isNotNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(dsmtScope, DSMT_LINK_FIELD_VALUE_IN))) {

            fieldsToUpdateInfo.put(DSMT_LINK_STATUS_FIELD, null);
            fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, DSMT_LINK_FIELD_VALUE_IN);
        }

        logger.info("processAssociationForAEDraftStatusWihinRange() End");
    }

    /**
     * <p>
     * This method processes the fields for the Add DSMT Links to the AuditableEntity whose status is Draft and is out
     * of range. The fields and their values are appended to the existing fields info map that is sent to the method
     * </P>
     *
     * @param associateDSMTInfo  - an instance of the {@link CreateDSMTLinkForAudEntityInfo} that has all the info to create
     *                           and associate a DSMT Link to the AuditableEntity
     * @param fieldsToUpdateInfo - A Map that has the field to update as key and its value as the value of the map
     */
    private void processAssociationForAEDraftStatusOutOfRange(CreateDSMTLinkForAudEntityInfo associateDSMTInfo,
            Map<String, String> fieldsToUpdateInfo) throws Exception {

        logger.info("processAssociationForAEDraftStatusOutOfRange() Start");

        /* Initialize Variables */
        String dsmtScope = EMPTY_STRING;
        IGRCObject existingDSMTLink;

        if (isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId())) {

            existingDSMTLink = grcObjectUtil.getObjectFromId(associateDSMTInfo.getExistingDSMTResourceId());
            dsmtScope = fieldUtil.getFieldValueAsString(existingDSMTLink, DSMT_LINK_SCOPE_FIELD);
        }

        logger.info("Existing Resource ID present: " + isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()));
        logger.info("DSMT Scope: " + dsmtScope);

        logger.info("Condtion 1: " + isNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()));
        logger.info("Condtion 2: " + (isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(
                dsmtScope, DSMT_LINK_FIELD_VALUE_IN)));

        logger.info("Total Condition: " + (isNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) || (isNotNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(dsmtScope, DSMT_LINK_FIELD_VALUE_IN))));

        if (isNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()) || (isNotNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(dsmtScope, DSMT_LINK_FIELD_VALUE_IN))) {

            fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, null);
            fieldsToUpdateInfo.put(DSMT_LINK_STATUS_FIELD, DSMT_LINK_STATUS_VALUE_PENDING_ADDITION);

            /* If out of range and if the out of range names are present process the out of range field */
            if (associateDSMTInfo.isOutOfRange() && isListNotNullOrEmpty(associateDSMTInfo.getOutOfRangeNodes())) {

                fieldsToUpdateInfo.put(DSMT_LINK_RANGE_FIELD,
                                       unParseCommaDelimitedValues(associateDSMTInfo.getOutOfRangeNodes()));
            }
        }

        fieldsToUpdateInfo.put(DSMT_LINK_RATIONAL_FIELD, associateDSMTInfo.getRationale());

        logger.info("processAssociationForAEDraftStatusOutOfRange() End");
    }

    /**
     * <p>
     * This method processes the fields for the Add DSMT Links to the AuditableEntity whose status is Active and is
     * within range. The fields and their values are appended to the existing fields info map that is sent to the method
     * </P>
     *
     * @param associateDSMTInfo  - an instance of the {@link CreateDSMTLinkForAudEntityInfo} that has all the info to create
     *                           and associate a DSMT Link to the AuditableEntity
     * @param fieldsToUpdateInfo - A Map that has the field to update as key and its value as the value of the map
     * @throws Exception - Generic Exception
     */
    private void processAssociationForAEActiveStatusWithinRange(CreateDSMTLinkForAudEntityInfo associateDSMTInfo,
            Map<String, String> fieldsToUpdateInfo) throws Exception {

        logger.info("processAssociationForAEActiveStatusWihinRange() Start");

        String dsmtScope = EMPTY_STRING;
        IGRCObject existingDSMTLink;

        /* Method Implementation */
        /* Initialize Variables */
        if (isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId())) {

            existingDSMTLink = grcObjectUtil.getObjectFromId(associateDSMTInfo.getExistingDSMTResourceId());
            dsmtScope = fieldUtil.getFieldValueAsString(existingDSMTLink, DSMT_LINK_SCOPE_FIELD);
        }

        logger.info("Existing Resource ID present: " + isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()));
        logger.info("DSMT Scope: " + dsmtScope);

        logger.info("Condtion 1: " + isNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()));
        logger.info("Condtion 2: " + (isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(
                dsmtScope, DSMT_LINK_FIELD_VALUE_IN)));

        logger.info("Total Condition: " + (isNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) || (isNotNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(dsmtScope, DSMT_LINK_FIELD_VALUE_IN))));

        if (isNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()) || (isNotNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(dsmtScope, DSMT_LINK_FIELD_VALUE_IN))) {

            fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, null);
            fieldsToUpdateInfo.put(DSMT_LINK_STATUS_FIELD, DSMT_LINK_STATUS_VALUE_PENDING_ADDITION);
        }

        logger.info("processAssociationForAEActiveStatusWihinRange() End");
    }

    /**
     * <p>
     * This method processes the fields for the Add DSMT Links to the AuditableEntity whose status is Active and is out
     * of range. The fields and their values are appended to the existing fields info map that is sent to the method
     * </P>
     *
     * @param associateDSMTInfo  - an instance of the {@link CreateDSMTLinkForAudEntityInfo} that has all the info to create
     *                           and associate a DSMT Link to the AuditableEntity
     * @param fieldsToUpdateInfo - A Map that has the field to update as key and its value as the value of the map
     * @throws Exception - Generic Exception
     */
    private void processAssociationForAEActiveStatusOutOfRange(CreateDSMTLinkForAudEntityInfo associateDSMTInfo,
            Map<String, String> fieldsToUpdateInfo) throws Exception {

        logger.info("processAssociationForAEActiveStatusOutOfRange() Start");

        String dsmtScope = EMPTY_STRING;
        IGRCObject existingDSMTLink;

        /* Method Implementation */
        if (isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId())) {

            existingDSMTLink = grcObjectUtil.getObjectFromId(associateDSMTInfo.getExistingDSMTResourceId());
            dsmtScope = fieldUtil.getFieldValueAsString(existingDSMTLink, DSMT_LINK_SCOPE_FIELD);
        }

        logger.info("Existing Resource ID present: " + isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()));
        logger.info("DSMT Scope: " + dsmtScope);

        logger.info("Condtion 1: " + isNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()));
        logger.info("Condtion 2: " + (isNotNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(
                dsmtScope, DSMT_LINK_FIELD_VALUE_IN)));

        logger.info("Total Condition: " + (isNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) || (isNotNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(dsmtScope, DSMT_LINK_FIELD_VALUE_IN))));

        if (isNullOrEmpty(associateDSMTInfo.getExistingDSMTResourceId()) || (isNotNullOrEmpty(
                associateDSMTInfo.getExistingDSMTResourceId()) && isNotEqual(dsmtScope, DSMT_LINK_FIELD_VALUE_IN))) {

            fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, null);
            fieldsToUpdateInfo.put(DSMT_LINK_STATUS_FIELD, DSMT_LINK_STATUS_VALUE_PENDING_ADDITION);
        }

        fieldsToUpdateInfo.put(DSMT_LINK_RATIONAL_FIELD, associateDSMTInfo.getRationale());

        /* If out of range and if the out of range names are present process the out of range field */
        if (associateDSMTInfo.isOutOfRange() && isListNotNullOrEmpty(associateDSMTInfo.getOutOfRangeNodes())) {

            fieldsToUpdateInfo.put(DSMT_LINK_RANGE_FIELD,
                                   unParseCommaDelimitedValues(associateDSMTInfo.getOutOfRangeNodes()));
        }

        logger.info("processAssociationForAEActiveStatusOutOfRange() End");
    }

    /**
     * <p>
     * This method takes care of removing the fields that need not be updated, if an existing DSMT Link object is
     * present and we do not create a new DSMT Link object instead update the existing DSMT Link object with the
     * necessary values.
     * </P>
     *
     * @param fieldsToUpdateInfo - the map containing the fields that will be updated in the DSMT Link object
     * @param existingDSMTLinkId - the id of the existing DSMT Link object
     */
    private void processFieldsToUpdateIfDSMTExists(Map<String, String> fieldsToUpdateInfo, String existingDSMTLinkId) {

        logger.info("processFieldsToUpdateIfDSMTExists() Start");

        /* Method Implementation */
        /* Make sure the Map has values in it. */
        if (isMapNotNullOrEmpty(fieldsToUpdateInfo)) {

            /* Make sure the id is present */
            if (isNotNullOrEmpty(existingDSMTLinkId)) {

                /* The following fields need not be updated as they have existing values that doesn't change value */
                fieldsToUpdateInfo.remove(DSMT_LINK_IS_OMU_FIELD);
                fieldsToUpdateInfo.remove(DSMT_LINK_ACTIVE_FIELD);
                fieldsToUpdateInfo.remove(DSMT_LINK_TYPE_FIELD);
                fieldsToUpdateInfo.remove(DSMT_LINK_BASE_ID_FIELD);
                fieldsToUpdateInfo.remove(DSMT_LINK_RANGE_FIELD);
                fieldsToUpdateInfo.remove(DSMT_LINK_RANGE_FIELD);
            }
        }

        /* Log for debug */
        logger.info("Final list of fields to update: " + fieldsToUpdateInfo);
        logger.info("processFieldsToUpdateIfDSMTExists() End");
    }

    /**
     * <p>
     * This method de'scopes the DSMT Link objects that were selected in the UI. Before de'scoping it checks for the
     * status of the AuditableEntity from which the helper was run. 1. If the Status is Draft - the method sets the
     * values of the following fields in the selected DSMT Link objects and save the DSMT Link object i. Set Scope Field
     * value to Out 2. If the Status is Active - the method checks for each DSMT Link selected to be de'scoped if there
     * are any Lower Level Dependencies. The Lower level dependencies are evaluated by checking if for the
     * AuditableEntity there are any child Audit, Control, Issue, CAP or AMR object who has child/children DSMT Links
     * whose DSMT ID matches the DSMT ID of the selected DSMT Link object to be de'scoped. If So the DSMT Link object is
     * stopped from de'scoping and a detailed message on all the grand child DSMT Links with matching DSMT ID's are
     * collected and displayed to the user.
     * <p>
     * If there are no Lower Level Dependencies then the method sets the values of the following fields in the selected
     * DSMT Link OBjects and save the DSMT Link object i. Set Scope Field value to Out
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return a list of {@link DSMTLinkObjectInfo}
     * @throws Exception - Generic Exception
     */
    @Override
    @SuppressWarnings("unchecked")
    public DSMTLinkHelperAppInfo descopeDSMTLinksFromAuditableEntity(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        logger.info("descopeDSMTLinksFromAuditableEntity() Start");

        // Method Level Variables.
        String audEntityStatus;

        IGRCObject dsmtLinkObject;
        IGRCObject audEntityObject;
        DSMTTripletModel dsmtTripletModel;
        Map<String, String> fieldsToUpdateInfo;
        List<ExistingDSMTLinkInfo> actualDSMTLinksList;
        List<ExistingDSMTLinkInfo> dsmtLinksToUpdateList;

        /* Initialize Variables */
        audEntityObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        audEntityStatus = fieldUtil.getFieldValueAsString(audEntityObject, AUD_ENTITY_STATUS_FIELD_INFO);
        actualDSMTLinksList = (List<ExistingDSMTLinkInfo>) getExistingDSMTLinks(dsmtLinkHelperInfo).getRows();

        logger.info("DSMT Link Descoping List not null: " + isListNotNullOrEmpty(
                dsmtLinkHelperInfo.getDsmtLinkUpdateInfo()));

        // Make sure the De'scoping information is present to continue
        if (isListNotNullOrEmpty(dsmtLinkHelperInfo.getDsmtLinkUpdateInfo())) {

            /*
             * Iterate through the list of De'scoping items. The items are grouped by the AuditableEntities. In this
             * case there will be only one AuditableEntity as the De'scoping is performed on the AuditableEntity Level
             */
            for (DSMTLinkUpdateInfo descopeDSMTInfo : dsmtLinkHelperInfo.getDsmtLinkUpdateInfo()) {

                logger.info("DSMT Link ID To Descope List not null: " + isListNotNullOrEmpty(
                        descopeDSMTInfo.getDsmtLinkIdsToUpdate()));

                /* Make sure there are DSMT Links under the AuditableEntity available to be de'scoped. */
                if (isListNotNullOrEmpty(descopeDSMTInfo.getDsmtLinkIdsToUpdate())) {

                    dsmtLinksToUpdateList = dsmtLinkHelperUtil.processDSMTLinksForUpdate(actualDSMTLinksList,
                                                                                         descopeDSMTInfo.getDsmtLinkIdsToUpdate());
                    logger.info("DSMT Link to Descope: " + dsmtLinksToUpdateList);

                    /* Iterate through he list of DSMT links to be de'scoped. */
                    for (ExistingDSMTLinkInfo dsmtLinkInfo : dsmtLinksToUpdateList) {

                        logger.info("DSMT Link ID To Descope: " + dsmtLinkInfo);

                        /*
                         * Instantiate the fields to update map, the map will hold the fields to update as the keys and
                         * the values with which the field will be updated as the values for the keys. Get the DSMT Link
                         * object from the ID passed from the UI Update the map with the common fields to update
                         * irrespective of the Auditable Entity Status
                         */
                        fieldsToUpdateInfo = new HashMap<>();
                        dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkInfo.getResourceId());

                        logger.info("Type not null or empty: " + isNotNullOrEmpty(dsmtLinkInfo.getType()));

                        /* Make sure the Id passed corresponds to an object in the system */
                        if (isObjectNotNull(dsmtLinkObject)) {

                            String scopePreviousValue = fieldUtil.getFieldValueAsString(dsmtLinkObject,
                                                                                        DSMT_LINK_SCOPE_FIELD);
                            String currentValue = dsmtLinkInfo.getScope();

                            /* If status of AuditableEntity is Draft */
                            if (isEqual(AUD_ENTITY_DRAFT_STATUS, audEntityStatus)) {

                                logger.info(
                                        "Descoping With AUD Entity Draft Status will delete the " + dsmtLinkObject.getName() + " Object");

                                if (isEqual(scopePreviousValue, DSMT_LINK_FIELD_VALUE_IN) && isEqual(currentValue,
                                                                                                     DSMT_LINK_FIELD_VALUE_OUT)) {

                                    dsmtTripletModel = new DSMTTripletModel();
                                    dsmtTripletModel.setStatus(DSMT_DESCOPE_STATUS);
                                    dsmtTripletModel.setTripletID(
                                            fieldUtil.getFieldValueAsString(dsmtLinkObject, DSMT_LINK_BASE_ID_FIELD));
                                    dsmtTripletModel.setMsid(fieldUtil.getFieldValueAsString(dsmtLinkObject,
                                                                                             DSMT_LINK_MANAGED_SEGMENT_ID_FIELD));
                                    dsmtTripletModel.setMgid(fieldUtil.getFieldValueAsString(dsmtLinkObject,
                                                                                             DSMT_LINK_MANAGED_GEOGRAPHY_ID_FIELD));
                                    dsmtTripletModel.setLvid(fieldUtil.getFieldValueAsString(dsmtLinkObject,
                                                                                             DSMT_LINK_LEGAL_VEHICLE_ID_FIELD));

                                    logger.info(
                                            "Descoping With AUD Entity Draft Status Changed From In to Out: Deleting DSMT Link Obj : " + dsmtLinkObject.getName());
                                    dsmtLinkServiceUtil.deleteDSMTLinkAsSuperUser(dsmtLinkObject);
                                    logger.info("Deleted DSMT Link Obj : " + dsmtLinkObject.getName());

                                    logger.info("DSMT Triplet model info to delete : " + dsmtTripletModel);
                                    dsmtHelperAppBaseDAO.descopeDSMTTriplet(dsmtTripletModel);
                                }
                                else if (isEqual(scopePreviousValue, DSMT_LINK_FIELD_VALUE_OUT) && isEqual(currentValue,
                                                                                                           DSMT_LINK_FIELD_VALUE_IN)) {

                                    fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, currentValue);
                                    logger.info(
                                            "Descoping With AUD Entity Draft Status Changed From Out to In: " + fieldsToUpdateInfo);

                                    grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                                }

                            }
                            // Else if the status of the AuditableEntity is Active
                            else if (isEqual(AUD_ENTITY_ACTIVE_STATUS, audEntityStatus)) {

                                if (isEqual(scopePreviousValue, DSMT_LINK_FIELD_VALUE_IN) && isEqual(currentValue,
                                                                                                     DSMT_LINK_FIELD_VALUE_OUT)) {

                                    fieldsToUpdateInfo.put(DSMT_LINK_STATUS_FIELD,
                                                           DSMT_LINK_STATUS_VALUE_PENDING_REMOVAL);
                                    logger.info(
                                            "Descoping With AUD Entity Active Status Changed From In to Out: " + fieldsToUpdateInfo);

                                    grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                                }
                                else if (isEqual(scopePreviousValue, DSMT_LINK_FIELD_VALUE_OUT) && isEqual(currentValue,
                                                                                                           DSMT_LINK_FIELD_VALUE_IN)) {

                                    fieldsToUpdateInfo.put(DSMT_LINK_STATUS_FIELD,
                                                           DSMT_LINK_STATUS_VALUE_PENDING_ADDITION);
                                    logger.info(
                                            "Descoping With AUD Entity Active Status Changed From Out to In: " + fieldsToUpdateInfo);

                                    grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                                }

                            }
                        }
                    }
                }
            }
        }

        // Return the DSMT Link Descoping failure information
        logger.info("descopeDSMTLinksFromAuditableEntity() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * <p>
     * The method processes the header names list to the header names for the AG Grid.
     * </P>
     *
     * @param fieldNamesList - A list of fields that will be used in the tabular header
     * @return - A list of DataGridHeaderColumnInfo that has the header representaiton of the Data Grid
     */
    private List<DataGridHeaderColumnInfo> processHeaderLabelList(List<String> fieldNamesList) {

        logger.info("processHeaderLabelList() Start");

        // Method Level Variables.
        String headerField;
        DataGridHeaderColumnInfo searchDSMTHeader;
        List<DataGridHeaderColumnInfo> searchDSMTHeaderList;

        /* Initialize Variables */
        searchDSMTHeaderList = new ArrayList<>();
        fieldNamesList = isListNotNullOrEmpty(fieldNamesList) ? fieldNamesList : new ArrayList<>();
        logger.info("Field Names List: " + fieldNamesList);

        /* Iterate through the list of header field names */
        for (String headerName : fieldNamesList) {

            // Prepare a header item for each header fields
            searchDSMTHeader = new DataGridHeaderColumnInfo();

            // Ignoring the Name header process all other headers
            if (isNotEqual("Name", headerName)) {

                /*
                 * Construct the header Name 1. Convert the first letter of the header to lower case 2. In the rest of
                 * the string remove any spaces to make it a single word
                 */
                headerField = headerName.substring(0, 1).toLowerCase() + headerName.substring(1)
                        .replaceAll(SINGLE_SPACE, EMPTY_STRING);

                // Set the header name and field name and add it to the list
                searchDSMTHeader.setHeaderName(headerName);
                searchDSMTHeader.setField(headerField);
                searchDSMTHeaderList.add(searchDSMTHeader);
            }
        }

        // Log and Return the header list
        logger.info("Processed Search Header list: " + searchDSMTHeaderList);
        logger.info("processHeaderLabelList() End");
        return searchDSMTHeaderList;
    }

    /**
     * <p>
     * The method returns the Restricted Nodes list for a given node name. Based on the Node name we reach the root
     * setting for the node and then finds all child settings and combines them into a single list of restricted nodes
     * for the selected node.
     * </P>
     *
     * @param nodeName - the name of the node whose restricted list needs to be obtained
     * @return - A list of Strings that has the restricted nodes for the given node name
     * @throws Exception - Generic Exception
     */
    private List<String> getRestrictedNodesList(String nodeName) throws Exception {

        logger.info("getRestrictedNodesList() Start");

        // Method Level Variables.
        int count = 1;

        String settingValues;
        String restrictedNodeSetting;
        List<String> restrictedNodesList;

        /* Initialize Variables */
        restrictedNodesList = new ArrayList<>();
        restrictedNodeSetting = RESTRICTED_NODES_ROOT_SETTING + nodeName + RESTRRICTED_NODE_SETTING;
        logger.info("Restricted Node Setting: " + restrictedNodeSetting);

        /* Iterate until a registry setting is found s */
        while (!isEqualIgnoreCase(NO_SETTING_FOUND, applicationUtil.getRegistrySetting(restrictedNodeSetting + count,
                                                                                       NO_SETTING_FOUND))) {

            /* Get the registry setting value and combine it with the original list */
            settingValues = applicationUtil.getRegistrySetting(restrictedNodeSetting + count, EMPTY_STRING);
            logger.info("Restricted Node Setting Value: " + settingValues);

            restrictedNodesList.addAll(parseCommaDelimitedValues(settingValues));
            count++;
        }

        /* Log the information for debug and return */
        logger.info("Restricted Nodes list: " + restrictedNodesList);
        logger.info("getRestrictedNodesList() End");
        return restrictedNodesList;
    }

    /**
     * <p>
     * This method gets the Tree search / Regular search results. For the tree search the given Parent Node Id is used.
     * On expand of a node in the UI the node id is sent as the parent node id. This id is used to get all the children
     * of that node ONLY. Where as for the regular search the search text entered in the UI is used.
     * <p>
     * It establishes a connection to the DSMT DB using the Data Service and runs the prepared statement to get the
     * Result set. The results are then processed to be sent in the format required by the AG Grid table.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @param dsmtSearchInfo     - The DSMT Search information required to get the tree search values.
     * @return - a list of {@link DSMTLinkSearchResultInfo} containing the top level search information
     * @throws Exception - Generic Exception
     */
    private List<DSMTLinkSearchResultInfo> getSearchResults(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            DSMTLinkSearchInfo dsmtSearchInfo) throws Exception {

        logger.info("getSearchResults() Start");

        // Method Level Variables.
        int count = 1;

        Connection connection = null;
        ResultSet queryResults = null;
        PreparedStatement preparedStmt = null;
        List<DSMTLinkSearchResultInfo> searchDSMTInfoList;

        /* Initialize Variables */
        try {

            /* Get a connection to the database and create the prepared statement using the query */
            connection = dsmtHelperAppBaseDAO.getConnection();
            preparedStmt = connection.prepareStatement(dsmtSearchInfo.getPreparedStmtQuery());

            /* Log the information for debugging */
            logger.info("Query to execute: " + dsmtSearchInfo.getPreparedStmtQuery());
            logger.info("Prepared Stmt Values: " + dsmtSearchInfo.getPreparedStmtValues());

            /* Set the query params in the prepared statement created */
            for (String preparedStmtValue : dsmtSearchInfo.getPreparedStmtValues()) {

                preparedStmt.setString(count, preparedStmtValue);
                count++;
            }

            /* Execute the query and get the query results. */
            logger.info("Going to execute query with values: " + preparedStmt.toString());
            queryResults = preparedStmt.executeQuery();
            dsmtSearchInfo.setQueryResults(queryResults);

            /* Process the results obtained for display based on the search type s */
            searchDSMTInfoList = getQuerySearchResults(dsmtLinkHelperInfo, dsmtSearchInfo);
        }
        catch (Exception e) {

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getSearchResults()" + getStackTrace(e));
            throw e;
        }
        finally {

            /* Finally clear out all information */
            dsmtHelperAppBaseDAO.clearResultSet(queryResults);
            dsmtHelperAppBaseDAO.clearStatement(preparedStmt);
            dsmtHelperAppBaseDAO.closeConnection(connection);

        }

        logger.info("getSearchResults() End");
        return searchDSMTInfoList;
    }

    /**
     * <p>
     * This method constructs the {@link DSMTLinkSearchInfo} to run the search logic for both the search and the tree
     * search. The Node selected in the UI determines what query needs to be executed, the restricted nodes available
     * for that node, The prepared statement query to execute, The order of the prepared statement values. The above
     * values are also determined based on the fact that if the search is a regular search or a tree search.
     *
     * @param dsmtSearchInfo - an instance of the DSMTLinkSearchInfo bean that has the search information
     * @throws Exception - Generic Exception
     */
    private void getSearchDetailsInformation(DSMTLinkSearchInfo dsmtSearchInfo) throws Exception {

        logger.info("getSearchDetailsInformation() Start");

        // Method Level Variables.
        boolean isTreeSearch;
        String searchText;
        String node = EMPTY_STRING;
        String fieldNamesInfo = EMPTY_STRING;
        String preparedStmtQuery = EMPTY_STRING;

        List<String> fieldNamesList;
        List<String> preparedStmtValues;
        List<String> restrictedNodesList;

        /* Initialize Variables */
        preparedStmtValues = new ArrayList<>();
        isTreeSearch = dsmtSearchInfo.isTreeSearch();

        // If Managed Segment Node was selected
        if (isEqual(MANAGED_SEGMENT_NODE, dsmtSearchInfo.getNodeSelected())) {

            node = MANAGED_SEGMENT;
            fieldNamesInfo = isTreeSearch ? DSMT_SEARCH_TREE_HEADER_INFO : DSMT_SEARCH_TABLE_HEADER_INFO;
            searchText = isTreeSearch ? dsmtSearchInfo.getParentNodeId() : dsmtSearchInfo.getSearchText();
            preparedStmtQuery = isTreeSearch ? MANAGEMENT_SEGMENT_TREE_QUERY : MANAGEMENT_SEGMENT_SEARCH_QUERY;

            preparedStmtValues.add(searchText);
            preparedStmtValues.add(dsmtSearchInfo.getManageGeographyNodeId());
            preparedStmtValues.add(dsmtSearchInfo.getLegalVehicleNodeId());
        }
        // If Managed Geography Node was selected
        else if (isEqual(MANAGED_GEOGRAPHY_NODE, dsmtSearchInfo.getNodeSelected())) {

            node = MANAGED_GEOGRAPHY;
            dsmtSearchInfo.setManagedGeography(true);
            fieldNamesInfo = isTreeSearch ? DSMT_SEARCH_TREE_HEADER_INFO : DSMT_SEARCH_TABLE_HEADER_INFO;
            searchText = isTreeSearch ? dsmtSearchInfo.getParentNodeId() : dsmtSearchInfo.getSearchText();
            preparedStmtQuery = isTreeSearch ? MANAGEMENT_GEOGRAPHY_TREE_QUERY : MANAGEMENT_GEOGRAPHY_SEARCH_QUERY;

            preparedStmtValues.add(dsmtSearchInfo.getManageSegNodeId());
            preparedStmtValues.add(searchText);
            preparedStmtValues.add(dsmtSearchInfo.getLegalVehicleNodeId());

        }
        // If Legal Vehicle Node was selected
        else if (isEqual(LEGAL_VEHICLE_NODE, dsmtSearchInfo.getNodeSelected())) {

            node = LEGAL_VEHICLE;
            preparedStmtQuery = isTreeSearch ? LEGAL_VEHICLE_TREE_QUERY : LEGAL_VEHICLE_SEARCH_QUERY;
            fieldNamesInfo = isTreeSearch ? DSMT_SEARCH_TREE_HEADER_INFO : DSMT_SEARCH_TABLE_HEADER_INFO;
            searchText = isTreeSearch ? dsmtSearchInfo.getParentNodeId() : dsmtSearchInfo.getSearchText();

            preparedStmtValues.add(dsmtSearchInfo.getManageSegNodeId());
            preparedStmtValues.add(dsmtSearchInfo.getManageGeographyNodeId());
            preparedStmtValues.add(searchText);

        }

        /* process the restricted nodes and the header field names list based on the node */
        restrictedNodesList = getRestrictedNodesList(node);
        fieldNamesList = parseCommaDelimitedValues(fieldNamesInfo);

        // When all the values are determined set the values in their place holders
        dsmtSearchInfo.setPreparedStmtQuery(preparedStmtQuery);
        dsmtSearchInfo.setHeaderFieldNamesList(fieldNamesList);
        dsmtSearchInfo.setPreparedStmtValues(preparedStmtValues);
        dsmtSearchInfo.setRestrictedNodesList(restrictedNodesList);
        dsmtSearchInfo.setSelectedNode(TOP_LEVEL_NODE_ROOT_REGISTRY_SETTIING + node);

        /* Log the information for debug and return */
        logger.info("DSMT Search Details Information: " + dsmtSearchInfo);
        logger.info("getSearchDetailsInformation() End");
    }

    /**
     * <p>
     * The method gets the top level item in the tree. This is not retrieved from the back end instead its configured in
     * the registry setting. Based on the node selected the corresponding root registry setting is determined. Once the
     * root registry setting is determined the Node Name and the Node Id settings are calculated and their values are
     * obtained and constructed in the format required by the AG Grid Tree.
     * </P>
     *
     * @param dsmtSearchInfo - The DSMT Search information required to build the Top Level search for the tree
     * @return a list of {@link DSMTLinkSearchResultInfo} containing the top level search information
     * @throws Exception - Generic Exception
     */
    private List<DSMTLinkSearchResultInfo> getTopLevelTreeSearchResults(DSMTLinkSearchInfo dsmtSearchInfo)
            throws Exception {

        logger.info("getTopLevelTreeSearchResults() Start");

        // Method Level Variables.
        String rootIdValue;
        String rootNameValue;
        String rootLevelValue;
        String rootNodeSetting;

        List<String> hierarchylist;
        List<String> restrictedNodesList;
        DSMTLinkSearchResultInfo searchDSMTLinkInfo;
        List<DSMTLinkSearchResultInfo> searchDSMTLinkInfos;

        /* Initialize Variables */
        hierarchylist = new ArrayList<>();
        searchDSMTLinkInfos = new ArrayList<>();
        searchDSMTLinkInfo = new DSMTLinkSearchResultInfo();
        rootNodeSetting = dsmtSearchInfo.getSelectedNode();
        restrictedNodesList = dsmtSearchInfo.getRestrictedNodesList();

        /* Log for debug */
        logger.info("Root Node Setting: " + rootNodeSetting);
        logger.info("Root Node ID Setting: " + rootNodeSetting + TOP_LEVEL_NODE_ID_REGISTRY_SETTIING);
        logger.info("Root Node Name Setting: " + rootNodeSetting + TOP_LEVEL_NODE_NAME_REGISTRY_SETTIING);
        logger.info("Root Node Level Setting: " + rootNodeSetting + TOP_LEVEL_NODE_LEVEL_REGISTRY_SETTIING);

        /* Get the Root ID value, Root Name value, Root Level Value and construct the top level tree node. */
        rootIdValue = applicationUtil.getRegistrySetting(rootNodeSetting + TOP_LEVEL_NODE_ID_REGISTRY_SETTIING);
        rootNameValue = applicationUtil.getRegistrySetting(rootNodeSetting + TOP_LEVEL_NODE_NAME_REGISTRY_SETTIING);
        rootLevelValue = applicationUtil.getRegistrySetting(rootNodeSetting + TOP_LEVEL_NODE_LEVEL_REGISTRY_SETTIING);

        logger.info("Root Node ID Setting Value: " + rootIdValue);
        logger.info("Root Node Name Setting Value: " + rootNameValue);

        /* Set all necessary values */
        hierarchylist.add(rootNameValue);
        searchDSMTLinkInfo.setName(rootNameValue);
        searchDSMTLinkInfo.setNodeId(rootIdValue);
        searchDSMTLinkInfo.setLevel(rootLevelValue);
        searchDSMTLinkInfo.setHierarchy(hierarchylist);
        searchDSMTLinkInfo.setDisabled(restrictedNodesList.contains(rootIdValue));
        searchDSMTLinkInfo.setParentHierarchy(rootIdValue);
        searchDSMTLinkInfos.add(searchDSMTLinkInfo);

        if (isNotNullOrEmpty(dsmtSearchInfo.getManageGeographyNodeId()) || isNotNullOrEmpty(
                dsmtSearchInfo.getManageSegNodeId()) || isNotNullOrEmpty(dsmtSearchInfo.getLegalVehicleNodeId())) {

            dsmtSearchInfo.setParentNodeId(rootIdValue);
            dsmtSearchInfo.setParentNodeHierarchy(rootNameValue);
            getSearchDetailsInformation(dsmtSearchInfo);

            logger.info(
                    "Looks like we are in the second tab in the screen so we need the top level plus the second" + " level based on the context of the first level selected." + dsmtSearchInfo);
            //            searchDSMTLinkInfos.addAll(getSearchResults(dsmtLinkHelperInfo, dsmtSearchInfo));
        }

        /* Log top node items and return */
        logger.info("Top Level Node: " + searchDSMTLinkInfo);
        logger.info("getTopLevelTreeSearchResults() End");
        return searchDSMTLinkInfos;
    }

    /**
     * <p>
     * This method
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @param dsmtSearchInfo-    The DSMT Search information required to get the search results of a tree or regular search
     * @return - a list of DSMTLinkSearchResultInfo based on the execute search query
     * @throws Exception - Generic Exception
     */
    private List<DSMTLinkSearchResultInfo> getQuerySearchResults(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            DSMTLinkSearchInfo dsmtSearchInfo) throws Exception {

        logger.info("getQuerySearchResults() Start");

        // Method Level Variables.
        int count = 1;
        int searchLimit;
        ResultSet queryResults;
        String searchLimitSetting;

        List<String> hierarchyList;
        List<String> restrictedNodesList;
        List<String> parentHierarchyList;
        DSMTLinkSearchResultInfo searchDSMTLinkInfo;
        List<DSMTLinkSearchResultInfo> searchDSMTInfoList;

        /* Initialize Variables */
        queryResults = dsmtSearchInfo.getQueryResults();
        searchDSMTInfoList = new ArrayList<>();
        parentHierarchyList = parseCommaDelimitedValues(dsmtSearchInfo.getParentNodeHierarchy());
        restrictedNodesList = dsmtSearchInfo.getRestrictedNodesList();
        restrictedNodesList = isListNotNullOrEmpty(restrictedNodesList) ? restrictedNodesList : new ArrayList<>();
        searchLimitSetting = dsmtLinkHelperInfo.getObjRegistrySetting() + TOTAL_NUMBER_DSMT_RESULTS_RETURNED_SETTING;
        searchLimit = NumericUtil.getIntValue(applicationUtil.getRegistrySetting(searchLimitSetting));
        searchLimit = searchLimit == 0 ? 200 : searchLimit;

        /* Log information for debug */
        logger.info("Search Limit Setting Path: " + searchLimitSetting);
        logger.info("Search Limit Setting Value: " + searchLimit);
        logger.info("Restricted Node List: " + restrictedNodesList);
        logger.info("Search Limit Value: " + searchLimit);

        /* Make sure the query results are present */
        if (isObjectNotNull(queryResults)) {

            /* Iterate through each item in the qurey results. */
            while (queryResults.next()) {

                /* Log for debug */
                logger.info("Node Id: " + queryResults.getString(1));
                logger.info("Node Name: " + queryResults.getString(2));
                logger.info("Node Level: " + queryResults.getString(3));
                logger.info("Node Region: " + queryResults.getString(4));
                logger.info("Child Hierarchy: " + queryResults.getString(5));
                logger.info("Parent Hierarchy: " + queryResults.getString(6));

                logger.info("Is Item present in restricte Node List: " + (restrictedNodesList.contains(
                        queryResults.getString(1))));

                /*
                 * Create an row if its tree search OR if regular search and the item is not present in the restricted
                 * node list
                 */
                if (dsmtSearchInfo.isTreeSearch() || (!dsmtSearchInfo.isTreeSearch() && !restrictedNodesList.contains(
                        queryResults.getString(1)))) {

                    /*
                     * Create a new hierarchy list, this list should have the parent details of the parent node. In our
                     * case for a regular search we do not have a parent child relationship or a tree relationship so we
                     * retain the hierarchy list to have the name of the current node as AG Grid requires a value for
                     * the hierarchy.
                     *
                     * For a tree the parent hierarchy list is sent to us, we add that to the hierarchy list and add the
                     * current node at the end of the list
                     */
                    hierarchyList = new ArrayList<>();
                    searchDSMTLinkInfo = new DSMTLinkSearchResultInfo();

                    /* Set the values in the row details */
                    searchDSMTLinkInfo.setNodeId(queryResults.getString(1));
                    searchDSMTLinkInfo.setName(queryResults.getString(2));
                    searchDSMTLinkInfo.setLevel(queryResults.getString(3));
                    searchDSMTLinkInfo.setParentHierarchy(queryResults.getString(6));

                    /* For Managed Geography extra columns are present add them. */
                    if (dsmtSearchInfo.isManagedGeography()) {

                        logger.info("Region: " + queryResults.getString(8));
                        logger.info("Country: " + queryResults.getString(7));
                        searchDSMTLinkInfo.setRegion(queryResults.getString(8));
                        searchDSMTLinkInfo.setCountry(queryResults.getString(7));
                    }

                    /* For a tree search we do not ignore the restricted items but disable them for select in the UI */
                    if (dsmtSearchInfo.isTreeSearch()) {

                        // Add the parent hierarchy list sent from the UI
                        hierarchyList.addAll(parentHierarchyList);
                        searchDSMTLinkInfo.setDisabled(restrictedNodesList.contains(queryResults.getString(1)));
                    }

                    /* Set all values in the row element of the AG Grid table */
                    hierarchyList.add(searchDSMTLinkInfo.getName());
                    searchDSMTLinkInfo.setId("row-" + count);
                    searchDSMTLinkInfo.setHierarchy(hierarchyList);
                    searchDSMTInfoList.add(searchDSMTLinkInfo);

                    /* On max count reach quit */
                    if (count > searchLimit) {
                        break;
                    }
                    count++;
                }

            }
        }

        logger.info("getQuerySearchResults() End");
        return searchDSMTInfoList;
    }

    /**
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @param query              - The query that needs to be executed
     * @return - a list of the {@link ExistingDSMTLinkInfo} that has the results of the existing dsmt search in the
     * format required by the AG Grid
     * @throws Exception - Generic Exception
     */
    private List<ExistingDSMTLinkInfo> populateQueryResults(DSMTLinkHelperAppInfo dsmtLinkHelperInfo, String query)
            throws Exception {

        logger.info("populateQueryResults() Start");

        /* Initialize Variables */
        int count = 1;

        List<String> hierarchyList;
        ITabularResultSet resultSet;
        ExistingDSMTLinkInfo dsmtLinkObjectInfo;
        DSMTLinkDisableInfo dsmtLinkDisableInfo;
        List<ExistingDSMTLinkInfo> dsmtLinkObjectList;

        /* Method Implementation */
        /* Execute the query */
        logger.info("query: " + query);
        resultSet = grcObjectSearchUtil.executeQuery(query);
        dsmtLinkObjectList = new ArrayList<>();

        /* Making sure wee\ have the results */
        if (isObjectNotNull(resultSet)) {

            /* Iterate through the results */
            for (IResultSetRow row : resultSet) {

                hierarchyList = new ArrayList<>();
                dsmtLinkObjectInfo = new ExistingDSMTLinkInfo();

                logger.info("Status Null: " + isNotNullOrEmpty(fieldUtil.getFieldValueAsString(row.getField(3))));
                logger.info("Scope Null: " + isNotNullOrEmpty(fieldUtil.getFieldValueAsString(row.getField(4))));
                logger.info("Condition: " + (isNotNullOrEmpty(
                        fieldUtil.getFieldValueAsString(row.getField(3))) || isNotNullOrEmpty(
                        fieldUtil.getFieldValueAsString(row.getField(4)))));

                if (isNotNullOrEmpty(fieldUtil.getFieldValueAsString(row.getField(3))) || isNotNullOrEmpty(
                        fieldUtil.getFieldValueAsString(row.getField(4)))) {

                    /*
                     * Set the information to be displayed in the table and any extra information that will be sent back if
                     * the item is selected to be de'scoped
                     */
                    dsmtLinkObjectInfo.setId("row-" + count);
                    dsmtLinkObjectInfo.setName(fieldUtil.getFieldValueAsString(row.getField(1)));
                    dsmtLinkObjectInfo.setType(fieldUtil.getFieldValueAsString(row.getField(2)));
                    dsmtLinkObjectInfo.setScope(fieldUtil.getFieldValueAsString(row.getField(4)));
                    dsmtLinkObjectInfo.setStatus(fieldUtil.getFieldValueAsString(row.getField(3)));
                    dsmtLinkObjectInfo.setResourceId(fieldUtil.getFieldValueAsString(row.getField(0)));
                    dsmtLinkObjectInfo.setManagedsegmentname(fieldUtil.getFieldValueAsString(row.getField(5)));
                    dsmtLinkObjectInfo.setManagedgeographyname(fieldUtil.getFieldValueAsString(row.getField(6)));
                    dsmtLinkObjectInfo.setLegalvehiclename(fieldUtil.getFieldValueAsString(row.getField(7)));
                    dsmtLinkObjectInfo.setActive(fieldUtil.getFieldValueAsString(row.getField(8)));

                    /* The name has to be in the form of a link so build the TFUI link to be used in the UI */
                    dsmtLinkObjectInfo.setLink(
                            dsmtLinkHelperUtil.getObjectDetailViewLink(dsmtLinkObjectInfo.getResourceId(),
                                                                       dsmtLinkObjectInfo.getName()));
                    hierarchyList.add(dsmtLinkObjectInfo.getName());
                    dsmtLinkObjectInfo.setHierarchy(hierarchyList);

                    dsmtLinkObjectInfo = dsmtLinkServiceUtil.processValidStatusForDSMTLink(dsmtLinkObjectInfo);

                    /*
                     * Check for lower level dependency if there is a lower level dependency then process to disable the row
                     * and also add the information to be shown in the screen.
                     */
                    logger.info("Check for lower level dependency");
                    dsmtLinkObjectInfo = dsmtLinkServiceUtil.processLowerLevelDependencyForDSMTLink(dsmtLinkHelperInfo,
                                                                                                    dsmtLinkObjectInfo);

                    dsmtLinkObjectInfo = dsmtLinkServiceUtil.processValidDSMTLink(dsmtLinkObjectInfo);

                    dsmtLinkDisableInfo = dsmtLinkObjectInfo.getDsmtLinkDisableInfo();
                    dsmtLinkDisableInfo = isObjectNotNull(dsmtLinkDisableInfo) ?
                            dsmtLinkDisableInfo :
                            new DSMTLinkDisableInfo();
                    dsmtLinkObjectInfo.setDisabled(
                            isSetNotNullOrEmpty(dsmtLinkDisableInfo.getDisableInfoList()) || isSetNotNullOrEmpty(
                                    dsmtLinkDisableInfo.getLowerLevelDepObjectsList()));

                    count++;
                    dsmtLinkObjectList.add(dsmtLinkObjectInfo);
                }
            }
        }

        /* Log the existing DSMT's and return */
        logger.info("Existing DSMT objects: " + dsmtLinkObjectList);
        logger.info("populateQueryResults() End");
        return dsmtLinkObjectList;
    }
}