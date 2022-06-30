/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * © Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.util;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.COLON;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.FORWARD_SLASH;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.HTML_ANCHOR_CLOSE_TAG;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.HTML_ANCHOR_TAG;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.HTML_TAG_END_CLOSE;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.SINGLE_SPACE;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.ui.constant.EmployeeSelectorConstants.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IMultiEnumField;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.IMultiEnumFieldUtil;
import com.ibm.openpages.ext.tss.service.impl.GRCObjectSearchUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.Employee;
import com.ibm.openpages.ext.ui.bean.EmployeeSelectorGRCObjectDetailsInfo;
import com.ibm.openpages.ext.ui.bean.Validation;

/**
 * <P>
 * This utility class provides utility methods for the Employee Selector helper.
 * </P>
 * 
 * @version : OpenPages 8.2.0
 * @author : Praveen Ravi <BR>
 *         email : raviprav@us.ibm.com <BR>
 *         company : IBM OpenPages
 * 
 * @custom.date : 06-19-2021
 * @custom.feature : Helper Services
 * @custom.category : Helper
 */
@Service("employeeSelectorHelperUtil")
public class EmployeeSelectorHelperUtil {

    // Class Level Variables
    private Log logger;

    @Autowired
    IFieldUtil fieldUtil;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IApplicationUtil applicationUtil;

    @Autowired
    GRCObjectSearchUtil grcObjectSearchUtil;

    @Autowired
    IGRCObjectUtil grcObjectUtil;

    @Autowired
    IMultiEnumFieldUtil multiEnumFieldUtil;

    @Autowired
    IServiceFactoryProxy serviceFactoryProxy;

    /**
     * Initialize any required service post the object construction.
     */
    @PostConstruct
    public void initController() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(EMPLOYEE_SELECTOR_UTIL_LOG_FILE_NAME);
    }

    public Employee getEmployee(String objectId, String field) throws Exception {

        logger.info("getEmployee() Start");

        Employee employee = new Employee();

        IGRCObject grcObject = grcObjectUtil.getObjectFromId(objectId);
        logger.info("Object Name [" + grcObject.getName() + "] and Id [" + grcObject.getId() + "]");

        String employeeInfo = fieldUtil.getFieldValueAsString(grcObject, field);
        logger.info("employeeInfo [" + employeeInfo + "]");

        if (isNullOrEmpty(employeeInfo) || !employeeInfo.contains("(") || !employeeInfo.trim().endsWith(")")) {
            logger.info("getEmployee() End!");
            employee.setFirstName(EMPTY_STRING);
            employee.setLastName(EMPTY_STRING);
            employee.setSoeId(EMPTY_STRING);
            return employee;
        }

        String soeId = employeeInfo.substring(employeeInfo.indexOf("(") + 1, employeeInfo.length() - 1);
        logger.info("soeId : " + soeId);
        employee.setSoeId(soeId);

        String firstNameLastName = employeeInfo.substring(0, employeeInfo.indexOf("("));
        logger.info("firstNameLastName : " + firstNameLastName);

        if (isNullOrEmpty(firstNameLastName)) {
            employee.setFirstName(EMPTY_STRING);
            employee.setLastName(EMPTY_STRING);
        }
        else {

            String[] strArry = firstNameLastName.split("\\s+");

            if (isObjectNull(strArry) || strArry.length < 1) {

                employee.setFirstName(EMPTY_STRING);
                employee.setLastName(EMPTY_STRING);

            }
            else if (isObjectNull(strArry) || strArry.length >= 2) {

                if (isObjectNull(strArry[0]) || isNullOrEmpty(strArry[0])) {
                    employee.setFirstName(EMPTY_STRING);
                }
                else {
                    employee.setFirstName(strArry[0].trim());
                }

                if (isObjectNull(strArry[1]) || isNullOrEmpty(strArry[1])) {
                    employee.setLastName(EMPTY_STRING);
                }
                else {
                    employee.setLastName(strArry[1].trim());
                }
            }
            else if (isObjectNull(strArry) || strArry.length == 1) {

                if (isObjectNull(strArry[0]) || isNullOrEmpty(strArry[0])) {
                    employee.setFirstName(EMPTY_STRING);
                }
                else {
                    employee.setFirstName(strArry[0].trim());
                }

                employee.setLastName(EMPTY_STRING);

            }

        }

        logger.info("employee : " + employee);
        logger.info("getEmployee() End");
        return employee;
    }

    public Employee getEmployee(String soeId, String firstName, String lastName) throws Exception {

        logger.info("getEmployee() Start");

        Employee employee = new Employee();

        if(isNullOrEmpty(soeId)) {
            employee.setSoeId(EMPTY_STRING);
        } else {
            employee.setSoeId(soeId.trim());
        }

        if(isNullOrEmpty(firstName)) {
            employee.setFirstName(EMPTY_STRING);
        } else {
            employee.setFirstName(firstName.trim());
        }

        if(isNullOrEmpty(lastName)) {
            employee.setLastName(EMPTY_STRING);
        } else {
            employee.setLastName(lastName.trim());
        }

        logger.info("employee : " + employee);
        logger.info("getEmployee() End");
        return employee;
    }

    public Validation getSelectedValidation(String objectId, String field) throws Exception {

        logger.info("getSelectedValidation() Start");
        IGRCObject grcObject = grcObjectUtil.getObjectFromId(objectId);

        Validation validation = new Validation();
        List<String> selectedValueList = new ArrayList<String>();

        selectedValueList = multiEnumFieldUtil.getEnumFieldSelectedValue(grcObject, field);

        for(String selectedValue : selectedValueList) {

            switch (selectedValue) {
                case ISSUE_INVALID_OMU_EXEC: validation.setInvalidOMUExec(true);
                         break;
                case ISSUE_INACTIVE_OMU: validation.setInactiveOMU(true);
                         break;
                case ISSUE_OMU_UPDATE: validation.setOmuUpdate(true);
                         break;
                case ISSUE_OMU_EXEC_UPDATE: validation.setOmuExecUpdate(true);
                         break;
                case ISSUE_SCORECARD_HR_UPDATE: validation.setScorecardHRUpdate(true);
                         break;
                case ISSUE_INACTIVE_USER : validation.setInactiveUser(true);
                         break;
                case CAP_INACTIVE_ACTION_OWNER : validation.setInactiveActionOwner(true);
                         break;
                case CAP_ACTION_OWNER_HR_UPDATE: validation.setActionOwnerHRUpdate(true);
                         break;
            }
        }

        logger.info("getSelectedValidation() End");
        return validation;
    }

    public IGRCObject resetValidation(String objectId, String field, String resetKey) throws Exception {

        logger.info("resetValidation() Start");
        IGRCObject grcObject = grcObjectUtil.getObjectFromId(objectId);

        List<String> selectedValueList = multiEnumFieldUtil.getEnumFieldSelectedValue(grcObject, field);
        logger.info("selectedValueList : " + selectedValueList);
        logger.info("resetKey : " + resetKey);



        if(isListNotNullOrEmpty(selectedValueList) && selectedValueList.contains(resetKey)) {
            selectedValueList.remove(resetKey);
            if(isListNotNullOrEmpty(selectedValueList)) {
                multiEnumFieldUtil.setEnumFieldValue(grcObject, field, unParseCommaDelimitedValues(selectedValueList));
                logger.info("resetValidation() End");
                return grcObjectUtil.saveResource(grcObject);
            } else {
                IMultiEnumField enumField = (IMultiEnumField)grcObject.getField(field);
                enumField.setEnumValues(null);
                logger.info("resetValidation() End!!");
                return grcObjectUtil.saveResource(grcObject);
            }

        }else {
            logger.info("resetValidation() End!");
            return grcObject;
        }
    }


    /**
     * <P>
     * This method returns the query results in set
     * </P>
     *
     * @param query
     *
     * @return - the Set of resource Id from query
     * @throws Exception
     */
    public Set<String> getQueryResult(String query) throws Exception {

        logger.info("getQueryResult() Start");

        Set<String> queryResultSet = new HashSet<String>();
        // logger.info("query: " + query);
        ITabularResultSet resultSet = grcObjectSearchUtil.executeQuery(query);

        for (IResultSetRow row : resultSet) {
            queryResultSet.add(fieldUtil.getFieldValueAsString(row.getField(1)));
        }

        logger.info("getQueryResult() End");
        return queryResultSet;
    }

    /**
     * <P>
     * This method gets the localized label for a given field from the OpenPages system.
     * </P>
     * 
     * @param fieldName
     *            - the field information of the field whose localized label needs to be obtained. Field information is
     *            in the format FieldGroupName:FieldName
     * 
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

        // From the metadata service get the field definition and then get the localized
        // label
        fieldDefinition = metadataService.getField(fieldName);
        fieldLabel = fieldDefinition.getLocalizedLabel();

        return fieldLabel;
    }

    /**
     * <P>
     * The method returns a HTML Anchor tag enclosing the URL to the given objects detail View. The URL is constructed
     * from the Object URL generator in the Registry Setting. If the Object Id or the Object Name is null or empty then
     * an EMPTY_String is returned.
     * </P>
     * 
     * @param objectId
     *            The Object Id for which the HTML Link needs to be generated
     * @param objectName
     *            The Object Name for which the HTML Link needs to be generated
     * 
     * @return a HTML Anchor tag containing the objects detail view link
     * @throws Exception
     *             any runtime exception
     */
    public String getObjectDetailViewLink(String objectId, String objectName) throws Exception {

        return (isNotNullOrEmpty(objectId) && isNotNullOrEmpty(objectName)) ? HTML_ANCHOR_TAG
                + getObjectDetailTaskViewURL(objectId) + HTML_TAG_END_CLOSE + objectName + HTML_ANCHOR_CLOSE_TAG
                : EMPTY_STRING;
    }

    /**
     * <P>
     * The method returns a HTML Anchor tag enclosing the URL to the given objects detail View and link open in a new
     * window by default. The URL is constructed from the Object URL generator in the Registry Setting. If the Object Id
     * or the Object Name is null or empty then an EMPTY_String is returned.
     * </P>
     * 
     * @param objectId
     *            The Object Id for which the HTML Link needs to be generated
     * @param objectName
     *            The Object Name for which the HTML Link needs to be generated
     * 
     * @return a HTML Anchor tag containing the objects detail view link
     * @throws Exception
     *             any runtime exception
     */
    public String getObjectDetailViewLinkForModal(String objectId, String objectName) throws Exception {

        return (isNotNullOrEmpty(objectId) && isNotNullOrEmpty(objectName))
                ? HTML_ANCHOR_TAG + getObjectDetailTaskViewURL(objectId) + "\"" + SINGLE_SPACE + MODAL_NEW_WINDOW_INFO
                        + HTML_TAG_END_CLOSE + objectName + HTML_ANCHOR_CLOSE_TAG
                : EMPTY_STRING;
    }

    /**
     * <P>
     * The method returns the detail view URL (plain URL not the HTML formatted anchor tag) for the given Object Id. The
     * URL is constructed from the Object URL generator in the Registry Setting.
     * </P>
     * 
     * @param objectId
     *            The Object Id for which the detail view URL needs to be generated
     * 
     * @return the Objects detail view URL. The URL is not formatted as HTML Anchor tag
     * @throws Exception
     *             any runtime exception
     */
    public String getObjectDetailTaskViewURL(String objectId) throws Exception {

        return applicationUtil.getRegistrySetting(APPLICATION_URL_PROTOCOL) + COLON + FORWARD_SLASH
                + applicationUtil.getRegistrySetting(APPLICATION_URL_HOST) + COLON
                + applicationUtil.getRegistrySetting(APPLICATION_URL_PORT)
                + applicationUtil.getRegistrySetting(APPLICATION_TASK_VIEW_URL_DETAIL_PAGE) + objectId;
    }

    /**
     * <P>
     * This method returns the object information requested in the form of a map. For the given IGRCObject and the
     * provided names of fields whose values are needed a map is constructed with the label of the field as the key and
     * the value the value of the field in the given object in the form of a String.
     * </P>
     * 
     * @param object
     *            an instance of the {@link IGRCObject} whose field values needs to be retrieved
     * @param fieldNamesInfo
     *            a comma separated String representation of fields
     * @return
     * @throws Exception
     */
    public List<EmployeeSelectorGRCObjectDetailsInfo> getBasicObjectInformationForDisplay(IGRCObject object,
            String fieldNamesInfo) throws Exception {

        logger.info("getFieldValuesOfObjectAsMap() Start");

        // Method Level Variables.
        String fieldValue = EMPTY_STRING;

        List<String> fieldNamesList = null;
        EmployeeSelectorGRCObjectDetailsInfo objectDetailsInfo = null;
        List<EmployeeSelectorGRCObjectDetailsInfo> objectFieldsInfoList = null;

        /* Initialize */
        // Parse the comma separated string of fields into a list
        objectFieldsInfoList = new ArrayList<EmployeeSelectorGRCObjectDetailsInfo>();
        fieldNamesList = parseCommaDelimitedValues(fieldNamesInfo);

        /* Iterate through the list of fields */
        for (String fieldName : fieldNamesList) {

            objectDetailsInfo = new EmployeeSelectorGRCObjectDetailsInfo();

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

}
