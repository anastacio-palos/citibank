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

package com.ibm.openpages.ext.ui.util;

import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IncludeAssociations;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.ext.tss.service.*;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.DataGridHeaderColumnInfo;
import com.ibm.openpages.ext.ui.bean.ExistingDSMTLinkInfo;
import com.ibm.openpages.ext.ui.bean.GRCObjectDetailsInfo;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.*;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.APPLICATION_URL_HOST;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.APPLICATION_URL_PORT;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.APPLICATION_URL_PROTOCOL;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.*;

/**
 * <p>
 * This utility class provides utility methods for the DSMT Link helper.
 * </P>
 *
 * @author : Praveen Ravi <BR>
 * email : raviprav@us.ibm.com <BR>
 * company : IBM OpenPages
 * @version : OpenPages 8.2.0
 * @custom.date : 04-23-2021
 * @custom.feature : Helper Services
 * @custom.category : Helper
 */
@Service("dsmtLinkHelperUtil")
public class DSMTLinkHelperUtil {

    @Autowired
    IFieldUtil fieldUtil;
    @Autowired
    ILoggerUtil loggerUtil;
    @Autowired
    IApplicationUtil applicationUtil;
    @Autowired
    IGRCObjectUtil grcObjectUtil;
    @Autowired
    IGRCObjectTypeUtil grcObjectTypeUtil;
    @Autowired
    IGRCObjectSearchUtil grcObjectSearchUtil;
    @Autowired
    IServiceFactoryProxy serviceFactoryProxy;
    // Class Level Variables
    private Log logger;

    /**
     * Initialize any required service post the object construction.
     */
    @PostConstruct
    public void initController() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(DSMT_LINK_HELPER_UTIL_LOG_FILE_NAME);
    }

    /**
     * <P>
     *     This method returns a list resource ids of the parent object for the given object. The given parent type
     *     will be matched with the parents for the given object.
     * </P>
     * @param object           - an instance of the IGRCObject whose parent Object of a specific type will be retrieved
     * @param parentObjectType - the type of parent object that needs to be identified for the given object
     * @return a List of String that represents the resource id's of the parent object identified by the parent
     * object type
     * @throws Exception - Generic Exception
     */
    public List<String> getParentObjectsOfSpecificType(IGRCObject object, String parentObjectType) throws Exception {

        logger.info("getParentObjectsOfSpecificType() Start");
        logger.info("Object Name: " + object.getName());
        logger.info("Parent Object Type: " + parentObjectType);

        GRCObjectFilter filter;
        IGRCObject currentObject;
        List<String> parentObjectsList;
        ITypeDefinition parentObjectTypeDef;
        List<IAssociationNode> parentObjectsNodes;
        IConfigurationService configurationService;

        parentObjectsList = new ArrayList<>();
        configurationService = serviceFactoryProxy.getServiceFactory().createConfigurationService();
        parentObjectTypeDef = grcObjectTypeUtil.getObjectTypeDefinition(parentObjectType);

        filter = new GRCObjectFilter(configurationService.getCurrentReportingPeriod());
        filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.PARENT);
        filter.getAssociationFilter().setTypeFilters(parentObjectTypeDef);

        currentObject = grcObjectUtil.getObjectFromId(object.getId(), filter);
        parentObjectsNodes = currentObject.getParents();

        logger.info("Filtered Parent Object List is not null: " + parentObjectsNodes);
        logger.info("Filtered Parent Object List is not null: " + isListNotNullOrEmpty(parentObjectsNodes));

        if (isListNotNullOrEmpty(parentObjectsNodes)) {

            for (IAssociationNode parentObjectNode : parentObjectsNodes) {

                parentObjectsList.add(parentObjectNode.getId().toString());
            }
        }

        logger.info("Parent Objects List: " + parentObjectsList);
        logger.info("getParentObjectsOfSpecificType() Start");
        return parentObjectsList;
    }

    /**
     * @param object
     * @param hierarchyPath
     * @return
     * @throws Exception
     */
    public Set<String> getAncestorObjectsOfSpecificType(IGRCObject object, String hierarchyPath, String searchCriteria)
            throws Exception {

        logger.info("getAncestorObjectsOfSpecificType() Start");

        List<String> fieldsList;
        StringBuilder query;
        List<String> hierarchyList;
        List<String> hierarchyPathList;
        Set<String> ancestorObjectsList;

        fieldsList = new ArrayList<>();
        fieldsList.add("Resource ID");
        ancestorObjectsList = new HashSet<>();
        hierarchyPathList = parsePipeDelimitedValues(hierarchyPath);
        logger.info("Hierarchy Path List: " + hierarchyPathList);

        if (isListNotNullOrEmpty(hierarchyPathList)) {

            for (String hierarchy : hierarchyPathList) {

                hierarchyList = parseCommaDelimitedValues(hierarchy);
                logger.info("Hierarchy List: " + hierarchyList);

                query = grcObjectSearchUtil.getQueryForMultipleParentAssociation(fieldsList, hierarchyList);
                query.append("[Resource ID] = '");
                query.append(object.getId());
                query.append("'");
                query.append(searchCriteria);

                ancestorObjectsList.addAll(
                        grcObjectSearchUtil.getMultipleValuesFromQueryAsList(query.toString(), false));
                logger.info("Query to execute: " + query.toString());
            }
        }

        logger.info("getAncestorObjectsOfSpecificType() End");
        return ancestorObjectsList;
    }

    /**
     * <p>
     * This method returns the object information requested in the form of a map. For the given IGRCObject and the
     * provided names of fields whose values are needed a map is constructed with the label of the field as the key and
     * the value the value of the field in the given object in the form of a String.
     * </P>
     *
     * @param object         an instance of the {@link IGRCObject} whose field values needs to be retrieved
     * @param fieldNamesInfo a comma separated String representation of fields
     * @return
     * @throws Exception
     */
    public List<GRCObjectDetailsInfo> getBasicObjectInformationForDisplay(IGRCObject object, String fieldNamesInfo)
            throws Exception {

        logger.info("getFieldValuesOfObjectAsMap() Start");

        // Method Level Variables.
        String fieldValue = EMPTY_STRING;

        List<String> fieldNamesList = null;
        GRCObjectDetailsInfo objectDetailsInfo = null;
        List<GRCObjectDetailsInfo> objectFieldsInfoList = null;

        /* Initialize */
        // Parse the comma separated string of fields into a list
        objectFieldsInfoList = new ArrayList<GRCObjectDetailsInfo>();
        fieldNamesList = parseCommaDelimitedValues(fieldNamesInfo);

        /* Iterate through the list of fields */
        for (String fieldName : fieldNamesList) {

            objectDetailsInfo = new GRCObjectDetailsInfo();

            /*
             * Get the value of the field in the given object and populate the map with the localized label as the key
             * and the value of the field as the value
             */
            if (isEqual("Name", fieldName)) {

                fieldValue = object.getName();
                objectDetailsInfo.setLink(getObjectDetailTaskViewURL(object.getId().toString()));
            }
            else {

                fieldValue = fieldUtil.getFieldValueAsString(object, fieldName);
            }

            /* Set the information in the Object Details information and add it to the list */
            objectDetailsInfo.setFieldName(getFieldLableForDisplay(fieldName));
            objectDetailsInfo.setFieldValue(fieldValue);
            objectFieldsInfoList.add(objectDetailsInfo);
        }

        /* Log the object fields and return */
        logger.info("Basic Object Field Labels: " + objectFieldsInfoList);
        logger.info("getFieldValuesOfObjectAsMap() End");
        return objectFieldsInfoList;
    }

    /**
     * <p>
     * This method prepares the format for the data grid header. This method is used if the field names are fields in an
     * object in the OpenPages system,
     * <p>
     * For the given field names the localized name of the field in the OpenPages system is obtained to be displayed as
     * the Header name and the field is constructed and set in their corresponding object and added to the list and
     * finally returned.
     * <p>
     * The field is nothing but a small case version of the Header name, any spaces present is removed.
     * </P>
     *
     * @param fieldNamesList - a List of String that represents the name of the header column.
     * @return - a list of {@link DataGridHeaderColumnInfo} each representing a column header
     * @throws Exception
     */
    public List<DataGridHeaderColumnInfo> getHeaderForDataGrid(List<String> fieldNamesList) throws Exception {

        logger.info("getHeaderForDataGrid() End");
        logger.info("Field Names List: " + fieldNamesList);

        // Method Level Variables.
        String key = EMPTY_STRING;
        String fieldLabel = EMPTY_STRING;
        DataGridHeaderColumnInfo availableDSMTLinkHeader = null;
        List<DataGridHeaderColumnInfo> availableDSMTLinkHeaderList = null;

        // Method Implementation
        /* Initialize */
        availableDSMTLinkHeaderList = new ArrayList<DataGridHeaderColumnInfo>();
        availableDSMTLinkHeader = new DataGridHeaderColumnInfo();

        /* Set the id field, this will not be part of the list */
        availableDSMTLinkHeader.setField("id");
        availableDSMTLinkHeader.setHide(true);
        availableDSMTLinkHeaderList.add(availableDSMTLinkHeader);

        /* Iterate through each item in the header column names */
        for (String fieldName : fieldNamesList) {

            /* We leave the name as is */
            if (isNotEqual("Name", fieldName)) {

                /* Get the Localized label for the field in the OpenPages system */
                if (fieldName.contains(COLON)) {
                    fieldLabel = getFieldLableForDisplay(fieldName);
                }
                else {
                    fieldLabel = fieldName;
                }

                /* Prepare a new column header info and set the name and field */
                availableDSMTLinkHeader = new DataGridHeaderColumnInfo();
                availableDSMTLinkHeader.setHeaderName(fieldLabel);

                key = fieldLabel.toLowerCase().replaceAll(" ", "");

                if (isEqualIgnoreCase(key, VALID)) {
                    availableDSMTLinkHeader.setField(ACTIVE);
                }
                else {
                    availableDSMTLinkHeader.setField(key);
                }

                /* Add it to the list */
                availableDSMTLinkHeaderList.add(availableDSMTLinkHeader);
            }
        }

        logger.info("Available DSMT link header: " + availableDSMTLinkHeaderList);
        logger.info("getHeaderForDataGrid() End");
        return availableDSMTLinkHeaderList;
    }

    /**
     * <p>
     * This method gets the localized label for a given field from the OpenPages system.
     * </P>
     *
     * @param fieldName - the field information of the field whose localized label needs to be obtained. Field information is
     *                  in the format FieldGroupName:FieldName
     * @return - the localized label of a field in the OpenPages system in String format
     * @throws Exception
     */
    public String getFieldLableForDisplay(String fieldName) throws Exception {

        logger.info("getFieldValuesOfObjectAsMap() Start");

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

    /**
     * <p>
     * The method returns a HTML Anchor tag enclosing the URL to the given objects detail View. The URL is constructed
     * from the Object URL generator in the Registry Setting. If the Object Id or the Object Name is null or empty then
     * an EMPTY_String is returned.
     * </P>
     *
     * @param objectId   The Object Id for which the HTML Link needs to be generated
     * @param objectName The Object Name for which the HTML Link needs to be generated
     * @return a HTML Anchor tag containing the objects detail view link
     * @throws Exception any runtime exception
     */
    public String getObjectDetailViewLink(String objectId, String objectName) throws Exception {

        return (isNotNullOrEmpty(objectId) && isNotNullOrEmpty(objectName)) ?
                HTML_ANCHOR_TAG + getObjectDetailTaskViewURL(
                        objectId) + HTML_TAG_END_CLOSE + objectName + HTML_ANCHOR_CLOSE_TAG :
                EMPTY_STRING;
    }

    /**
     * <p>
     * The method returns a HTML Anchor tag enclosing the URL to the given objects detail View and link open in a new
     * window by default. The URL is constructed from the Object URL generator in the Registry Setting. If the Object Id
     * or the Object Name is null or empty then an EMPTY_String is returned.
     * </P>
     *
     * @param objectId   The Object Id for which the HTML Link needs to be generated
     * @param objectName The Object Name for which the HTML Link needs to be generated
     * @return a HTML Anchor tag containing the objects detail view link
     * @throws Exception any runtime exception
     */
    public String getObjectDetailViewLinkForModal(String objectId, String objectName) throws Exception {

        return (isNotNullOrEmpty(objectId) && isNotNullOrEmpty(objectName)) ?
                HTML_ANCHOR_TAG + getObjectDetailTaskViewURL(
                        objectId) + "\"" + SINGLE_SPACE + MODAL_NEW_WINDOW_INFO + HTML_TAG_END_CLOSE + objectName + HTML_ANCHOR_CLOSE_TAG :
                EMPTY_STRING;
    }

    /**
     * <p>
     * The method returns the detail view URL (plain URL not the HTML formatted anchor tag) for the given Object Id. The
     * URL is constructed from the Object URL generator in the Registry Setting.
     * </P>
     *
     * @param objectId The Object Id for which the detail view URL needs to be generated
     * @return the Objects detail view URL. The URL is not formatted as HTML Anchor tag
     * @throws Exception any runtime exception
     */
    public String getObjectDetailTaskViewURL(String objectId) throws Exception {

        return applicationUtil.getRegistrySetting(
                APPLICATION_URL_PROTOCOL) + COLON + FORWARD_SLASH + applicationUtil.getRegistrySetting(
                APPLICATION_URL_HOST) + COLON + applicationUtil.getRegistrySetting(
                APPLICATION_URL_PORT) + applicationUtil.getRegistrySetting(
                APPLICATION_TASK_VIEW_URL_DETAIL_PAGE) + objectId;
    }

    /**
     * <p>
     * This method processes the Actual DSMT links in the system and the Updated DSMT links sent from the UI for update.
     * For each item in the Actual DSMT link, we check for each updated DSMT link if their resource Id's match, If the
     * resource Id matches then we check if the scope is different. Only if the scope is different we add it to the the
     * items that needs to be updated. This has to be done because all the items in the existing dsmt links table is
     * sent to the back end regardless of the fact that its changed or not. We identify the change based on the scope as
     * the user can only update the scope of the DSMT.
     * </P>
     *
     * @param actualDSMTLinksList  - A List of {@link ExistingDSMTLinkInfo} representing each DSMT Link associated to the object under
     *                             execution
     * @param updatedDSMTLinksList - A List of {@link ExistingDSMTLinkInfo} representing each DSMT Link present in the Existing DSMT
     *                             Links table in the objects helper.
     * @return - A List of {@link ExistingDSMTLinkInfo} representing a concise list of the updatedDSMTLinksList where
     * any DSMT that was not updated is removed.
     * @throws Exception
     */
    public List<ExistingDSMTLinkInfo> processDSMTLinksForUpdate(List<ExistingDSMTLinkInfo> actualDSMTLinksList,
            List<ExistingDSMTLinkInfo> updatedDSMTLinksList) throws Exception {

        logger.info("processDSMTLinksForUpdate() Start");

        // Method Level Variables.
        List<ExistingDSMTLinkInfo> dsmtLinksToUpdateList = null;

        // Method Implementation
        /* Initialize Variables */
        dsmtLinksToUpdateList = new ArrayList<ExistingDSMTLinkInfo>();

        /* Log information for debug */
        logger.info("Does the Actual List has values: " + isListNotNullOrEmpty(actualDSMTLinksList));
        logger.info("Does updated list has values: " + isListNotNullOrEmpty(updatedDSMTLinksList));

        /* Make sure both the list has values */
        if (isListNotNullOrEmpty(actualDSMTLinksList) && isListNotNullOrEmpty(updatedDSMTLinksList)) {

            /* Iterate through the Actual DSMT Link children present in the system for the object */
            for (ExistingDSMTLinkInfo actualDSMTLink : actualDSMTLinksList) {

                /* Iterate through the DSMT Link objects sent from the UI as part of update */
                for (ExistingDSMTLinkInfo updatedDSMTLink : updatedDSMTLinksList) {

                    /* Make sure we match the Actual DSMT and the one in the Updated DSMT */
                    if (isEqual(actualDSMTLink.getResourceId(), updatedDSMTLink.getResourceId())) {

                        logger.info(
                                "Object Id: " + actualDSMTLink.getResourceId() + ", Is scope changed: " + isNotEqual(
                                        actualDSMTLink.getScope(), updatedDSMTLink.getScope()));

                        /*
                         * Only if the scope is different from the scope in the Actual DSMT we considered that the DSMT
                         * was updated
                         */
                        if (isNotEqual(actualDSMTLink.getScope(), updatedDSMTLink.getScope())) {

                            /* Add it to the list and move on to the next DSMT in the Actual DSMT list */
                            dsmtLinksToUpdateList.add(updatedDSMTLink);
                            break;
                        }
                    }
                }
            }

            /* Log the updated list and return */
            logger.info("Filtered out list for update: " + dsmtLinksToUpdateList);
            logger.info("processDSMTLinksForUpdate() End");
        }

        return dsmtLinksToUpdateList;
    }

    public List<?> returnGoodList(List<?> list) {

        return isListNotNullOrEmpty(list) ? list : new ArrayList<>();
    }
}
