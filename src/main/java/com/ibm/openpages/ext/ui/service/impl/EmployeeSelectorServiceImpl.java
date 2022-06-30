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

package com.ibm.openpages.ext.ui.service.impl;

import com.ibm.openpages.ext.service.CRCService;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;
import com.ibm.openpages.ext.tss.helpers.service.IHelperService;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.HYPHEN;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.SINGLE_SPACE;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.YES;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.tss.service.util.NumericUtil.*;
import static com.ibm.openpages.ext.ui.constant.EmployeeSelectorConstants.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.IUserAccessUtil;
import com.ibm.openpages.ext.tss.service.beans.IGRCObjectCreateInformation;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import com.ibm.openpages.ext.ui.bean.Data;
import com.ibm.openpages.ext.ui.bean.Employee;
import com.ibm.openpages.ext.ui.bean.EmployeeHRDSMTInfo;
import com.ibm.openpages.ext.ui.bean.EmployeeSelectorGRCObjectDetailsInfo;
import com.ibm.openpages.ext.ui.bean.EmployeeSelectorHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.EmployeeSelectorObjectGenericDetails;
import com.ibm.openpages.ext.ui.bean.OMUDsmtLinkDisplayData;
import com.ibm.openpages.ext.ui.bean.GridData;
import com.ibm.openpages.ext.ui.bean.GridDataHeaderColumnInfo;
import com.ibm.openpages.ext.ui.bean.Node;
import com.ibm.openpages.ext.ui.bean.OMUDsmtRoot;
import com.ibm.openpages.ext.ui.bean.Validation;
import com.ibm.openpages.ext.ui.dao.IDSMTHelperAppBaseDAO;
import com.ibm.openpages.ext.ui.service.IEmployeeSelectorService;
import com.ibm.openpages.ext.ui.util.EmployeeSelectorHelperUtil;

@Service("employeeSelectorServiceImpl")
public class EmployeeSelectorServiceImpl implements IEmployeeSelectorService {

    private Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IHelperService helperService;

    @Autowired
    IApplicationUtil applicationUtil;

    @Autowired
    IGRCObjectUtil grcObjectUtil;

    @Autowired
    IFieldUtil fieldUtil;

    @Autowired
    IUserAccessUtil userAccessUtil;

    @Autowired
    EmployeeSelectorHelperUtil employeeSelectorHelperUtil;

    @Autowired
    IDSMTHelperAppBaseDAO dsmtHelperAppBaseDAO;

    @Autowired
    IServiceFactoryProxy serviceFactoryProxy;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Construct any information once the bean is created.
     */
    @PostConstruct
    public void initServiceImpl() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(EMPLOYEE_SELECTOR_LOG_FILE_NAME);
    }

    /**
     * <P>
     * This method retrieves the Helpers Header Information from the Registry settings and sets the appropriate values
     * in the {@link CarbonHeaderInfo} object. This will be used to display the content in the UI Header.
     * </P>
     *
     * @param employeeSelectorHelperAppInfo
     *            an instance of the {@link EmployeeSelectorHelperAppInfo}
     *
     * @return - the employeeSelectorHelperAppInfo with the helper header information set
     * @throws Exception
     */
    @Override
    public EmployeeSelectorHelperAppInfo getHelperHeaderInfo(
            EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo) throws Exception {

        // logger.info("getHelperHeaderInfo() Start");

        // Method Level Variables.
        CarbonHeaderInfo headerInfo = null;
        String issueStatus = EMPTY_STRING;
        IGRCObject grcObject;

        /* Initialize Variables */
        List<String> issueResponsibleList = new ArrayList<>();
        List<String> scorecardExecList = new ArrayList<>();

        grcObject = grcObjectUtil.getObjectFromId(employeeSelectorHelperAppInfo.getObjectID());
        logger.info("Object name [" + grcObject.getName() + "], Id [" + grcObject.getId() + "], and Object type [" + grcObject.getType().getName() + "]");

        // Get the helper header information by passing the Helper Base Setting + the object type label

        headerInfo = helperService.getTFUIHelperAppHeaderInfo(
                EMPLOYEE_SELECTOR_APP_BASE_SETTING + employeeSelectorHelperAppInfo.getObjectTypeLabel());

        // Set the bean to be returned
        employeeSelectorHelperAppInfo.setHeaderInfo(headerInfo);

        employeeSelectorHelperAppInfo.setHasAccess(true);
        if(userAccessUtil.canLoggedInUserWriteObject(grcObject)) {

            logger.info("User have write access to Object [" + grcObject.getName() + "] and Id [" + grcObject.getId() + "]");

            issueResponsibleList = parseCommaDelimitedValues(applicationUtil.getRegistrySetting(ISSUE_RESP_EXEC));
            logger.info("issueResponsibleList : " + issueResponsibleList);

            scorecardExecList = parseCommaDelimitedValues(applicationUtil.getRegistrySetting(ISSUE_SCORECARD_EXEC_REG));
            logger.info("scorecardExecList : " + scorecardExecList);

            issueStatus = fieldUtil.getFieldValueAsString(grcObject, ISSUE_STATUS_FIELD);
            logger.info("Issue Status : " + issueStatus);

            if (issueResponsibleList.contains(issueStatus)) {

                employeeSelectorHelperAppInfo.setExecutionType(ISSUE_RESPONSIBLE_EXECUTIVE);
            } else if(scorecardExecList.contains(issueStatus)) {

                employeeSelectorHelperAppInfo.setExecutionType(SCORECARD_EXECUTIVE);
            }
            else {
                employeeSelectorHelperAppInfo.setHasAccess(false);
                employeeSelectorHelperAppInfo.setMessage(HELPER_ERROR_MESSAGE);
            }

        } else {
            logger.info("User does not have write access to Object [" + grcObject.getName() + "] and Id [" + grcObject.getId() + "]");
            employeeSelectorHelperAppInfo.setHasAccess(false);
            employeeSelectorHelperAppInfo.setMessage(HELPER_ACCESS_MESSAGE);
        }

        logger.info("getHelperHeaderInfo() End");
        return employeeSelectorHelperAppInfo;
    }

    @Override
    public EmployeeSelectorHelperAppInfo getLandingPageInfo(EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo)
            throws Exception {

        // logger.info("getLandingPageInfo() Start");

        // Get the Basic Object information of the current object under execution
        employeeSelectorHelperAppInfo = getBasicObjectInfoForDisplay(employeeSelectorHelperAppInfo);

        logger.info("getLandingPageInfo() End");
        return employeeSelectorHelperAppInfo;
    }

    /**
     * <P>
     * This method retrieves the Helper Landing Page Content Section. The Landing Page Title, Landing Page Content and
     * the fields and the values of the fields from the object from which the helper was launched are obtained. The
     * Landing page information is set in an instance of the {@link EmployeeSelectorObjectGenericDetails}.
     * </P>
     *
     * @param employeeSelectorHelperAppInfo
     *            an instance of the {@link EmployeeSelectorHelperAppInfo}
     *
     * @return - the employeeSelectorHelperAppInfo with the objects basic information set
     * @throws Exception
     */
    private EmployeeSelectorHelperAppInfo getBasicObjectInfoForDisplay(
            EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo) throws Exception {

        // logger.info("getBasicObjectInfoForDisplay() Start");

        // Method Level Variables.
        String contentBody = EMPTY_STRING;
        String contentHeader = EMPTY_STRING;
        String registrySetting = EMPTY_STRING;
        String grcObjFieldRegValue = EMPTY_STRING;

        IGRCObject grcObject = null;
        List<EmployeeSelectorGRCObjectDetailsInfo> objFieldsInfo = null;
        EmployeeSelectorObjectGenericDetails helperObjectContentInfo = null;

        /* Initialize Variables */
        // objFieldsInfo = new HashMap<String, String>();
        helperObjectContentInfo = new EmployeeSelectorObjectGenericDetails();

        // Prepare the registry setting path based on the base registry setting and the object type
        // and obtain the list of fields that needs to be displayed in the UI from the registry setting.
        registrySetting = employeeSelectorHelperAppInfo.getObjRegistrySetting()
                + EMPLOYEE_SELECTOR_APP_OBJECT_DISPLAY_INFO;
        grcObjFieldRegValue = applicationUtil.getRegistrySetting(registrySetting);

        // Obtain the object under execution, Get the fields and its values that needs to be displayed in the UI
        // logger.info("objectId : " + employeeSelectorHelperAppInfo.getObjectID());
        grcObject = grcObjectUtil.getObjectFromId(employeeSelectorHelperAppInfo.getObjectID());
        objFieldsInfo = employeeSelectorHelperUtil.getBasicObjectInformationForDisplay(grcObject, grcObjFieldRegValue);

        // Get the Helper page content header and body to be displayed in the UI
        contentHeader = applicationUtil.getRegistrySetting(
                employeeSelectorHelperAppInfo.getObjRegistrySetting() + EMPLOYEE_SELECTOR_APP_TITLE, EMPTY_STRING);
        contentBody = applicationUtil.getRegistrySetting(
                employeeSelectorHelperAppInfo.getObjRegistrySetting() + EMPLOYEE_SELECTOR_APP_DISPLAY_NAME,
                EMPTY_STRING);

        // Set all the obtained values in the bean to be returned
        helperObjectContentInfo.setContentBody(contentBody);
        helperObjectContentInfo.setContentHeader(contentHeader);
        helperObjectContentInfo.setGeneralDetails(objFieldsInfo);

        employeeSelectorHelperAppInfo.setHelperObjectContentInfo(helperObjectContentInfo);
        logger.info("getBasicObjectInfoForDisplay() End");
        return employeeSelectorHelperAppInfo;
    }

    @Override
    public GridData searchEmployee(String soeId, String firstName, String lastName) throws Exception {

        logger.info("searchEmployee() Start");

        // Method Level Variables.
        int count = 1;

        GridData searchEmployeeGridInfo = null;
        Connection connection = null;
        ResultSet queryResults = null;
        PreparedStatement preparedStmt = null;
        List<Employee> employeeList;

        /* Initialize Variables */
        searchEmployeeGridInfo = new GridData();
        List<String> preparedStmtValues = new ArrayList<String>();

        try {

            /* Get a connection to the database and create the prepared statement using the query */
            // logger.info("dsmtHelperAppBaseDAO : " + dsmtHelperAppBaseDAO);
            connection = dsmtHelperAppBaseDAO.getConnection();
            logger.info("connection : " + connection);
            preparedStmt = connection.prepareStatement(EMPLOYEE_SEARCH_QUERY);
            logger.info("preparedStmt : " + preparedStmt);

            if (isNotNullOrEmpty(soeId)) {
                preparedStmtValues.add(soeId);
            }
            else {
                preparedStmtValues.add(null);
            }

            if (isNotNullOrEmpty(firstName)) {
                preparedStmtValues.add(firstName);
            }
            else {
                preparedStmtValues.add(null);
            }

            if (isNotNullOrEmpty(lastName)) {
                preparedStmtValues.add(lastName);
            }
            else {
                preparedStmtValues.add(null);
            }

            /* Log the information for debugging */
            logger.info("Employee Search Query : " + EMPLOYEE_SEARCH_QUERY);
            logger.info("Prepared Stmt Values: " + preparedStmtValues);

            /* Set the query params in the prepared statement created */
            for (String preparedStmtValue : preparedStmtValues) {

                preparedStmt.setString(count, preparedStmtValue);
                count++;
            }

            /* Execute the query and get the query results. */
            logger.info("Going to execute query with values: " + preparedStmt.toString());
            queryResults = preparedStmt.executeQuery();
            logger.info("queryResults: " + queryResults);

            /* Process the results obtained for display */
            employeeList = processEmployeeSearchResults(queryResults);
            logger.info("employeeList: " + employeeList);

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

        /* Set the table rows and header and return */
        searchEmployeeGridInfo.setRows(employeeList);

        logger.info("searchEmployee() End");
        return searchEmployeeGridInfo;
    }

    @Override
    public GridData getEmployeeHRDSMTInfo(String soeId) throws Exception {

        logger.info("getEmployeeHRDSMTInfo() Start");

        GridData employeeHRDSMTGridInfo = null;
        EmployeeHRDSMTInfo employeeHRDSMTInfo;
        List<EmployeeHRDSMTInfo> employeeHRDSMTInfoList;

        /* Initialize Variables */
        employeeHRDSMTGridInfo = new GridData();
        employeeHRDSMTInfoList = new ArrayList<EmployeeHRDSMTInfo>();

        /* Process the results obtained for display */
        employeeHRDSMTInfo = processEmployeeHRDSMTResults(soeId);
        logger.info("employeeHRDSMTInfo : " + employeeHRDSMTInfo);
        employeeHRDSMTInfoList.add(employeeHRDSMTInfo);

        /* Set the table rows and header and return */
        employeeHRDSMTGridInfo.setRows(employeeHRDSMTInfoList);

        logger.info("getEmployeeHRDSMTInfo() End");
        return employeeHRDSMTGridInfo;
    }

    @Override
    public EmployeeSelectorHelperAppInfo processIssueHelperLogic(
            EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo, GridData gridData) throws Exception {

        logger.info("processIssueHelperLogic() Start");
        logger.info("Execution Type : " + employeeSelectorHelperAppInfo.getExecutionType());

        // Method Level Variables.
        IGRCObject issueObject = null;
        String userInfo = EMPTY_STRING;
        Map<String, String> fieldsToUpdateInfo = null;
        Employee employee = null;
        EmployeeHRDSMTInfo employeeHRDSMTInfo = null;

        /* Initialize Variables */
        issueObject = grcObjectUtil.getObjectFromId(employeeSelectorHelperAppInfo.getObjectID());
        logger.info("Issue Object Name [" + issueObject.getName() + "] and Id [" + issueObject.getId() + "]");


        employeeHRDSMTInfo = processEmployeeHRDSMTResults(gridData.getSoeId());
        if(isObjectNull(employeeHRDSMTInfo)) {

            employeeHRDSMTInfo = employeeSelectorHelperAppInfo.getEmployeeHRDSMTInfo();
            logger.info("Employee HR DSMT Info from current session : " + employeeHRDSMTInfo);
        } else {

            logger.info("Latest Employee HR DSMT Info from DB : " + employeeHRDSMTInfo);
            employeeSelectorHelperAppInfo.setEmployeeHRDSMTInfo(employeeHRDSMTInfo);
        }

        employee = employeeSelectorHelperUtil.getEmployee(gridData.getSoeId(), gridData.getFirstName(),
                gridData.getLastName());
        logger.info("Selected Employee Info : " + employee);
        employeeSelectorHelperAppInfo.setEmployee(employee);

        if (isObjectNull(employee)) {
            userInfo = EMPTY_STRING;
        }
        else {
            userInfo = employeePrintFormat(employee);
        }

        logger.info("User Info : " + userInfo);

        // get the latest validation
        Validation validation = employeeSelectorHelperUtil
                .getSelectedValidation(employeeSelectorHelperAppInfo.getObjectID(), ISSUE_VALIDATION_FIELD);
        logger.info("validation : " + validation);

        if (validation.isInactiveUser()) {
            logger.info("Reset Scorecard Responsible Executive has become an Inactive User Validation..");
            issueObject = employeeSelectorHelperUtil.resetValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD, ISSUE_INACTIVE_USER);
            validation = employeeSelectorHelperUtil.getSelectedValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD);
            employeeSelectorHelperAppInfo.setValidation(validation);

            processOMUDMSTLink(employeeSelectorHelperAppInfo.getObjectID());
        }

        // Update Issue object with HR fields
        fieldsToUpdateInfo = new HashMap<String, String>();
        fieldsToUpdateInfo.put(ISSUE_GOC_NUMBER_FIELD, employeeHRDSMTInfo.getGocId());
        fieldsToUpdateInfo.put(ISSUE_GOC_NAME_FIELD, employeeHRDSMTInfo.getGocName());
        fieldsToUpdateInfo.put(ISSUE_MANAGED_SEGMENT_ID_FIELD, employeeHRDSMTInfo.getManagedSegmentId());
        fieldsToUpdateInfo.put(ISSUE_MANAGED_SEGMENT_NAME_FIELD, employeeHRDSMTInfo.getManagedSegmentName());
        fieldsToUpdateInfo.put(ISSUE_MANAGED_SEGMENT_PATH_FIELD, employeeHRDSMTInfo.getManagedSegmentPath());
        fieldsToUpdateInfo.put(ISSUE_MANAGED_GEOGRAPHY_ID_FIELD, employeeHRDSMTInfo.getManagedGeographyId());
        fieldsToUpdateInfo.put(ISSUE_MANAGED_GEOGRAPHY_NAME_FIELD, employeeHRDSMTInfo.getManagedGeographyName());
        fieldsToUpdateInfo.put(ISSUE_MANAGED_GEOGRAPHY_PATH_FIELD, employeeHRDSMTInfo.getManagedGeographyPath());
        fieldsToUpdateInfo.put(ISSUE_LEGAL_ENTITY_ID_FIELD, employeeHRDSMTInfo.getLegalEntityId());
        fieldsToUpdateInfo.put(ISSUE_LEGAL_ENTITY_NAME_FIELD, employeeHRDSMTInfo.getLegalEntityName());

        if (isEqualIgnoreCase(SCORECARD_EXECUTIVE, employeeSelectorHelperAppInfo.getExecutionType())) {

            logger.info("Scorecard Executive Case");
            fieldsToUpdateInfo.put(ISSUE_SCORECARD_EXEC_NAME_FIELD, userInfo);
            fieldsToUpdateInfo.put(ISSUE_SCORECARD_EXEC_TITLE_FIELD, gridData.getInputMessage());

        }
        else {
            logger.info("Issue Responsible Executive Case");
            fieldsToUpdateInfo.put(ISSUE_RESP_EXEC_NAME_FIELD, userInfo);
            fieldsToUpdateInfo.put(ISSUE_RESP_EXEC_TITLE_FIELD, gridData.getInputMessage());

            fieldsToUpdateInfo.put(ISSUE_SCORECARD_EXEC_NAME_FIELD, userInfo);
            fieldsToUpdateInfo.put(ISSUE_SCORECARD_EXEC_TITLE_FIELD, gridData.getInputMessage());

        }

        logger.info("Updating Issue Object Fields :\n" + fieldsToUpdateInfo);
        grcObjectUtil.updateFieldsInObjectAndSave(issueObject, fieldsToUpdateInfo);

        employeeSelectorHelperAppInfo.setOMU(false);
        logger.info("Creating or Updating DMST Links object...");
        employeeSelectorHelperAppInfo = saveDataForDSMTLinks(employeeSelectorHelperAppInfo, gridData);

        // Get the latest Copy of Issue Object
        issueObject = grcObjectUtil.getObjectFromId(employeeSelectorHelperAppInfo.getObjectID());

        // get the latest validation
        validation = employeeSelectorHelperUtil.getSelectedValidation(issueObject.getId().toString(),
                ISSUE_VALIDATION_FIELD);
        logger.info("latest validation : " + validation);

        if (validation.isInvalidOMUExec()) {
            logger.info(
                    "Reset Scorecard Responsible Executive no longer has responsibility for at least one OMU Validation..");
            issueObject = employeeSelectorHelperUtil.resetValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD, ISSUE_INVALID_OMU_EXEC);
            validation = employeeSelectorHelperUtil.getSelectedValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD);
            employeeSelectorHelperAppInfo.setValidation(validation);

            processHRDMSTLink(employeeSelectorHelperAppInfo.getObjectID());
        }

        if (validation.isInactiveOMU()) {
            logger.info("Reset Atleast one of the OMUs has become Invalid Validation..");
            issueObject = employeeSelectorHelperUtil.resetValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD, ISSUE_INACTIVE_OMU);
            validation = employeeSelectorHelperUtil.getSelectedValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD);
            employeeSelectorHelperAppInfo.setValidation(validation);

            processHRDMSTLink(employeeSelectorHelperAppInfo.getObjectID());
        }

        if (validation.isScorecardHRUpdate()) {
            logger.info("Reset Scorecard Responsible Executive’s HR DSMT has changed Validation..");
            issueObject = employeeSelectorHelperUtil.resetValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD, ISSUE_SCORECARD_HR_UPDATE);
            validation = employeeSelectorHelperUtil.getSelectedValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD);
            employeeSelectorHelperAppInfo.setValidation(validation);
        }

        if (validation.isOmuExecUpdate()) {
            logger.info("Reset Scorecard Responsible Executive’s HR DSMT has become invalid Validation..");
            issueObject = employeeSelectorHelperUtil.resetValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD, ISSUE_OMU_EXEC_UPDATE);
            validation = employeeSelectorHelperUtil.getSelectedValidation(issueObject.getId().toString(),
                    ISSUE_VALIDATION_FIELD);
            employeeSelectorHelperAppInfo.setValidation(validation);
        }

        if (isObjectNull(gridData) || isListNullOrEmpty(gridData.getRows())) {
            logger.info("No OMU found : " + gridData);
            // Update Issue object with HR flag
            fieldsToUpdateInfo = new HashMap<String, String>();
            fieldsToUpdateInfo.put(ISSUE_HR_DSMT_OMU_FIELD, HR_DSMT_VALUE);
            logger.info("Updating Issue Object HR DSMT OMU Field :\n" + fieldsToUpdateInfo);
            grcObjectUtil.updateFieldsInObjectAndSave(issueObject, fieldsToUpdateInfo);
            logger.info("Updated the Issue Object with HR flag for No OMU.");
        }

        if(!isSoeIdExist(gridData.getSoeId())) {
            logger.info("Soe Id doesn't exist in op employee table : " + gridData.getSoeId());
            insertSoeId(gridData.getSoeId());
        } else {
            logger.info("Soe Id exist in op employee table : " + gridData.getSoeId());
        }

        validation = employeeSelectorHelperUtil.getSelectedValidation(issueObject.getId().toString(),
                ISSUE_VALIDATION_FIELD);
        logger.info("Final Validation : " + validation);

        // Get the latest Copy of Issue Object
        issueObject = grcObjectUtil.getObjectFromId(employeeSelectorHelperAppInfo.getObjectID());
        if (employeeSelectorHelperAppInfo.isOMU()) {
            logger.info("OMU found...");

            logger.info("Update Issue Object with HR flag for OMU [" + issueObject.getName() + "] and Id ["
                    + issueObject.getId() + "]");

            fieldsToUpdateInfo = new HashMap<String, String>();
            fieldsToUpdateInfo.put(ISSUE_HR_DSMT_OMU_FIELD, OMU_DSMT_VALUE);
            logger.info("Updating Issue Object HR DSMT OMU Field :\n" + fieldsToUpdateInfo);
            grcObjectUtil.updateFieldsInObjectAndSave(issueObject, fieldsToUpdateInfo);
            logger.info("Updated the Issue Object with HR flag for OMU.");
        } else {

            logger.info("Update Issue Object with HR flag for No OMU [" + issueObject.getName() + "] and Id ["
                    + issueObject.getId() + "]");

            fieldsToUpdateInfo = new HashMap<String, String>();
            fieldsToUpdateInfo.put(ISSUE_HR_DSMT_OMU_FIELD, HR_DSMT_VALUE);
            logger.info("Updating Issue Object HR DSMT OMU Field :\n" + fieldsToUpdateInfo);
            grcObjectUtil.updateFieldsInObjectAndSave(issueObject, fieldsToUpdateInfo);
        }

        logger.info("processIssueHelperLogic() End");
        return employeeSelectorHelperAppInfo;
    }

    private void processHRDMSTLink(String issueObjectId) throws Exception {

        logger.info("processHRDMSTLink() Start");

        // Method Level Variables.
        Map<String, String> fieldsToUpdateInfo = null;
        Employee employee = null;

        IGRCObject grcObject = grcObjectUtil.getObjectFromId(issueObjectId);

        employee = employeeSelectorHelperUtil.getEmployee(issueObjectId, ISSUE_SCORECARD_EXEC_NAME_FIELD);
        logger.info("employee : " + employee);

        if (isObjectNull(employee) || isNullOrEmpty(employee.getSoeId())) {

            logger.info("processHRDMSTLink() End!");
            return;
        }

        // Construct DSMT Link query to get the DSMT Links Objects
        String dsmtLinkQuery = DSMT_LINK_BASE_QUERY.replace(SOEID_STR, employee.getSoeId()).replace(RESOURCE_ID_STR,
                issueObjectId);
        logger.info("HR DSMT Link Query: " + dsmtLinkQuery);

        Set<String> dsmtLinkSet = new HashSet<String>();
        dsmtLinkSet = employeeSelectorHelperUtil.getQueryResult(dsmtLinkQuery);
        logger.info("DSMT Link Resource Id List : " + dsmtLinkSet);

        if (isSetNullOrEmpty(dsmtLinkSet)) {

            logger.info("Update Issue Object with HR flag for No OMU [" + grcObject.getName() + "] and Id ["
                    + issueObjectId + "]");

            fieldsToUpdateInfo = new HashMap<String, String>();
            fieldsToUpdateInfo.put(ISSUE_HR_DSMT_OMU_FIELD, HR_DSMT_VALUE);
            logger.info("Updating Issue Object HR DSMT OMU Field :\n" + fieldsToUpdateInfo);
            grcObjectUtil.updateFieldsInObjectAndSave(grcObject, fieldsToUpdateInfo);
        }
        logger.info("processHRDMSTLink() End");
    }

    private void processOMUDMSTLink(String issueObjectId) throws Exception {

        logger.info("processOMUDMSTLink() Start");

        // Method Level Variables.
        Map<String, String> fieldsToUpdateInfo = null;
        IGRCObject dsmtLinkObject = null;
        Employee employee = null;

        employee = employeeSelectorHelperUtil.getEmployee(issueObjectId, ISSUE_SCORECARD_EXEC_NAME_FIELD);
        logger.info("employee : " + employee);

        if (isObjectNull(employee) || isNullOrEmpty(employee.getSoeId())) {

            logger.info("processOMUDMSTLink() End!");
            return;
        }

        // Construct DSMT Link query to get the DSMT Links Objects
        String dsmtLinkQuery = DSMT_LINK_BASE_QUERY.replace(SOEID_STR, employee.getSoeId()).replace(RESOURCE_ID_STR,
                issueObjectId);
        logger.info("Inactive user's DSMT Link Query: " + dsmtLinkQuery);

        Set<String> dsmtLinkSet = new HashSet<String>();
        dsmtLinkSet = employeeSelectorHelperUtil.getQueryResult(dsmtLinkQuery);
        logger.info("DSMT Link Resource Id List : " + dsmtLinkSet);

        if (isSetNotNullOrEmpty(dsmtLinkSet)) {

            for (String dsmtLinkId : dsmtLinkSet) {

                dsmtLinkObject = null;
                dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);
                logger.info("DSMT Link Object Name [" + dsmtLinkObject.getName() + "] and Id [" + dsmtLinkObject.getId()
                        + "]");

                fieldsToUpdateInfo = new HashMap<String, String>();
                fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, SCOPE_OUT_STATUS);
                logger.info("Updating DSMT Link Object Scope Field :\n" + fieldsToUpdateInfo);
                grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
            }
        }

        logger.info("processOMUDMSTLink() End");
    }

    @Override
    public GridData getExistingOMU(EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo, String soeId,
            HttpSession session) throws Exception {

        logger.info("getExistingOMU() Start");

        // Method Level Variables.
        GridData carbonGridDataInfo = null;
        OMUDsmtRoot omuDsmtRoot = null;
        CRCService crcService = null;
        String crcWebHook = EMPTY_STRING;

        EmployeeHRDSMTInfo employeeHRDSMTInfo = null;

        /* Initialize Variables */
        employeeHRDSMTInfo = processEmployeeHRDSMTResults(soeId);
        logger.info("employeeHRDSMTInfo : " + employeeHRDSMTInfo);
        employeeSelectorHelperAppInfo.setEmployeeHRDSMTInfo(employeeHRDSMTInfo);

        /* Set the Helper info bean in the session. */
        session.setAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION, employeeSelectorHelperAppInfo);
        logger.info("employeeSelectorHelperAppInfo : " + employeeSelectorHelperAppInfo);

        logger.info("Using Rest Template bean : " + restTemplate);
        crcService = new CRCService(restTemplate, loggerUtil);
        logger.info("crcService : " + crcService);
        crcWebHook = applicationUtil.getRegistrySetting(CRC_OMU_DATA_WEBHOOK_URL_REG);
        logger.info("crcWebHook : " + crcWebHook);

        String fullCRCWebHook = crcWebHook + soeId;
        logger.info("fullCRCWebHook : " + fullCRCWebHook);

        logger.info("getting exsting OMU and DSMT Link object for display...");

        // Temp code to check if we are running the this helper on IBM Host then get the data from JSON File and
        // otherwise get the data from CRC services
        String hostname = applicationUtil.getRegistrySetting(APPLICATION_URL_HOST);
        logger.info("hostname : " + hostname);
        if (isNotNullOrEmpty(hostname) && hostname.contains(".ibm.com")) {
            // get the OMU DSMT data from JSON File
            omuDsmtRoot = getOMUDsmtDataFromJSON(soeId);
        }
        else {
            // get the OMU DSMT data from CRC service
            omuDsmtRoot = (OMUDsmtRoot) crcService.invoke(fullCRCWebHook, OMUDsmtRoot.class);
        }

        // For now get the OMU DSMT data from CRC service
        // omuDsmtRoot = (OMUDsmtRoot)crcService.invoke(crcWebHook, OMUDsmtRoot.class);

        carbonGridDataInfo = processDataForDSMTLinks(employeeSelectorHelperAppInfo.getObjectID(), soeId, omuDsmtRoot,
                employeeHRDSMTInfo);

        logger.info("getExistingOMU() End");
        return carbonGridDataInfo;
    }

    @Override
    public EmployeeSelectorHelperAppInfo processCAPHelperLogic(
            EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo) throws Exception {

        logger.info("processCAPHelperLogic() Start");
        logger.info("employeeSelectorHelperAppInfo : " + employeeSelectorHelperAppInfo);
        logger.info("Object Id : " + employeeSelectorHelperAppInfo.getObjectID());

        // Method Level Variables.
        String userInfo = null;
        IGRCObject capObject = null;
        Map<String, String> fieldsToUpdateInfo = null;
        Employee employee;
        Validation validation = null;

        /* Initialize Variables */
        employee = employeeSelectorHelperAppInfo.getEmployee();
        logger.info("Selected Employee Info : " + employee);

        capObject = grcObjectUtil.getObjectFromId(employeeSelectorHelperAppInfo.getObjectID());
        logger.info("CAP Object Name : " + capObject.getName());
        logger.info("CAP Object Id : " + capObject.getId());

        if (isObjectNull(employee)) {
            userInfo = EMPTY_STRING;
        }
        else {
            userInfo = employeePrintFormat(employee);
        }

        logger.info("userInfo : " + userInfo);

        // Update CAP object with employee info
        fieldsToUpdateInfo = new HashMap<String, String>();
        fieldsToUpdateInfo.put(CAP_ACTION_PLAN_OWNER_FIELD, userInfo);

        logger.info("Updating CAP Object Owner Field :\n" + fieldsToUpdateInfo);
        grcObjectUtil.updateFieldsInObjectAndSave(capObject, fieldsToUpdateInfo);
        logger.info("Updated the CAP Object [" + capObject.getName() + "] with Action Plan Owner : " + userInfo);

        if (employeeSelectorHelperAppInfo.getValidation().isInactiveActionOwner()) {
            logger.info("Reset Inactive Action Owner..");
            capObject = employeeSelectorHelperUtil.resetValidation(capObject.getId().toString(), CAP_VALIDATION_FIELD,
                    CAP_INACTIVE_ACTION_OWNER);
            validation = employeeSelectorHelperUtil.getSelectedValidation(capObject.getId().toString(),
                    CAP_VALIDATION_FIELD);
            employeeSelectorHelperAppInfo.setValidation(validation);
        }

        if(!isSoeIdExist(employee.getSoeId())) {
            logger.info("Soe Id doesn't exist in op employee table : " + employee.getSoeId());
            insertSoeId(employee.getSoeId());
        } else {
            logger.info("Soe Id exist in op employee table : " + employee.getSoeId());
        }

        logger.info("processCAPHelperLogic() End");
        return employeeSelectorHelperAppInfo;
    }

    private GridData processDataForDSMTLinks(String objectId, String soeId, OMUDsmtRoot omuDsmtRoot,
            EmployeeHRDSMTInfo employeeHRDSMTInfo) throws Exception {

        logger.info("processDataForDSMTLinks() Start");
        int rowCount = 1;

        IGRCObject dsmtLinkObject = null;
        IGRCObject issueObject = null;
        String baseId = null;
        String scope = null;
        String active = null;
        String status = null;
        String invalidOMUExec = null;
        String managedSegmentId = null;
        String managedGeographyId = null;
        String dsmtLinkSoeId = null;
        GridData carbonGridDataInfo = null;
        OMUDsmtLinkDisplayData omuDsmtLinkDisplayData = null;
        Set<String> disableInfoSet = null;
        Node node = null;
        List<OMUDsmtLinkDisplayData> omuDsmtLinkDisplayDataList = null;

        carbonGridDataInfo = new GridData();
        omuDsmtLinkDisplayDataList = new LinkedList<OMUDsmtLinkDisplayData>();

        issueObject = grcObjectUtil.getObjectFromId(objectId);
        logger.info("Issue Object Name : " + issueObject.getName());
        logger.info("Issue Object Id : " + issueObject.getId());

        // Construct DSMT Link query to get the DSMT Links Objects
        String dsmtLinkQuery = DSMT_LINK_BASE_QUERY.replace(SOEID_STR, soeId).replace(RESOURCE_ID_STR, objectId);
        logger.info("Existing DSMT Link Query: " + dsmtLinkQuery);

        Set<String> dsmtLinkSet = new HashSet<String>();

        dsmtLinkSet = employeeSelectorHelperUtil.getQueryResult(dsmtLinkQuery);
        logger.info("In scope DSMT Link Resource Id List : " + dsmtLinkSet);

        if (isObjectNotNull(omuDsmtRoot) && isListNotNullOrEmpty(omuDsmtRoot.getData())) {

            for (Data data : omuDsmtRoot.getData()) {

                node = data.getNode();
                // logger.info("Node : " + node);
                if (isObjectNull(node)) {

                    logger.info("Node is null : " + node);
                    continue;
                }
                omuDsmtLinkDisplayData = new OMUDsmtLinkDisplayData();
                disableInfoSet = new HashSet<String>();

                omuDsmtLinkDisplayData.setId("row-" + rowCount);
                omuDsmtLinkDisplayData
                        .setName(node.getSoeId() + HYPHEN + node.getManSegmentId() + HYPHEN + node.getManGeoId());
                omuDsmtLinkDisplayData.setSelected(false);
                omuDsmtLinkDisplayData.setCheck(false);

                omuDsmtLinkDisplayData.setKeyId(node.getKeyId());
                omuDsmtLinkDisplayData.setMsOrgType(node.getMsOrgType());
                omuDsmtLinkDisplayData.setManSegmentId(node.getManSegmentId());
                omuDsmtLinkDisplayData.setManSegmentName(employeeHRDSMTInfo.getManagedSegmentName());
                omuDsmtLinkDisplayData.setManGeoName(employeeHRDSMTInfo.getManagedGeographyName());
                omuDsmtLinkDisplayData.setManGeoId(node.getManGeoId());
                omuDsmtLinkDisplayData.setResOrgMgrGeid(node.getResOrgMgrGeid());
                omuDsmtLinkDisplayData.setHrGenId(node.getHrGenId());
                omuDsmtLinkDisplayData.setPrimaryMgr(node.getPrimaryMgr());
                omuDsmtLinkDisplayData.setCoheadMgr(node.getCoheadMgr());
                omuDsmtLinkDisplayData.setOrgGrpMgr(node.getOrgGrpMgr());
                omuDsmtLinkDisplayData.setEffDt(node.getEffDt());
                omuDsmtLinkDisplayData.setEndDt(node.getEndDt());
                omuDsmtLinkDisplayData.setLastUpdoprId(node.getLastUpdoprId());
                omuDsmtLinkDisplayData.setLastUpdtDwEttm(node.getLastUpdtDwEttm());
                omuDsmtLinkDisplayData.setActiveInd(node.getActiveInd());
                omuDsmtLinkDisplayData.setCreatedBy(node.getCreatedBy());
                omuDsmtLinkDisplayData.setCreatedDate(node.getCreatedDate());
                omuDsmtLinkDisplayData.setLastEditedBy(node.getLastEditedBy());
                omuDsmtLinkDisplayData.setLastEditedDate(node.getLastEditedDate());
                omuDsmtLinkDisplayData.setSoeId(soeId);
                omuDsmtLinkDisplayData.setEmployeeStatus(node.getEmployeeStatus());
                omuDsmtLinkDisplayData.setUniqueKey(node.getUniqueKey());

                // If the DSMT Link object already exist
                if (isSetNotNullOrEmpty(dsmtLinkSet)) {

                    for (String dsmtLinkId : dsmtLinkSet) {

                        dsmtLinkObject = null;
                        baseId = null;
                        scope = null;
                        active = null;
                        status = null;
                        invalidOMUExec = null;

                        dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);
                        logger.info("DSMT Link Object Name [" + dsmtLinkObject.getName() + "] and Id ["
                                + dsmtLinkObject.getId() + "]");

                        managedSegmentId = fieldUtil.getFieldValueAsString(dsmtLinkObject,
                                DSMT_LINK_MANAGED_SEGMENT_ID_FIELD);
                        managedGeographyId = fieldUtil.getFieldValueAsString(dsmtLinkObject,
                                DSMT_LINK_MANAGED_GEOGRAPHY_ID_FIELD);
                        dsmtLinkSoeId = fieldUtil.getFieldValueAsString(dsmtLinkObject, DSMT_LINK_SOEID_FIELD);

                        logger.info("DSMT Link Managed Segment Id [" + managedSegmentId
                                + "], DSMT Link Managed Geography Id [" + managedGeographyId + "], DSMT Link SOE Id ["
                                + dsmtLinkSoeId + "]");
                        logger.info("CRC Managed Segment Id [" + node.getManSegmentId() + "], CRC Geography Id ["
                                + node.getManGeoId() + "], CRC SOE Id [" + soeId + "]");

                        if (isEqualIgnoreCase(managedSegmentId, node.getManSegmentId())
                                && isEqualIgnoreCase(managedGeographyId, node.getManGeoId())
                                && isEqualIgnoreCase(dsmtLinkSoeId, soeId)) {

                            logger.info("This DSMT Link Object is already associated : " + dsmtLinkObject.getName());

                            baseId = fieldUtil.getFieldValueAsString(dsmtLinkObject, DSMT_LINK_BASE_ID_FIELD);
                            scope = fieldUtil.getFieldValueAsString(dsmtLinkObject, DSMT_LINK_SCOPE_FIELD);
                            active = fieldUtil.getFieldValueAsString(dsmtLinkObject, DSMT_LINK_ACTIVE_FIELD);
                            status = fieldUtil.getFieldValueAsString(dsmtLinkObject, DSMT_LINK_STATUS_FIELD);
                            invalidOMUExec = fieldUtil.getFieldValueAsString(dsmtLinkObject,
                                    DSMT_LINK_INVALID_OMU_EXEC_FIELD);

                            omuDsmtLinkDisplayData.setLink(employeeSelectorHelperUtil.getObjectDetailViewLink(
                                    dsmtLinkObject.getId().toString(), dsmtLinkObject.getName()));
                            omuDsmtLinkDisplayData.setResourceId(dsmtLinkObject.getId().toString());
                            omuDsmtLinkDisplayData.setDescription(dsmtLinkObject.getDescription());
                            omuDsmtLinkDisplayData.setScope(scope);
                            omuDsmtLinkDisplayData.setStatus(status);
                            omuDsmtLinkDisplayData.setActive(active);
                            omuDsmtLinkDisplayData.setBaseId(baseId);
                            omuDsmtLinkDisplayData.setObjectType(dsmtLinkObject.getType().getName());
                            omuDsmtLinkDisplayData.setSoeId(node.getSoeId());

                            logger.info("Invalid OMU Exec [" + invalidOMUExec + "] and Active [" + active + "]");
                            if (isEqualIgnoreCase(YES, invalidOMUExec) || isNotEqualIgnoreCase(YES, active)) {
                                omuDsmtLinkDisplayData.setDisabled(true);

                                if (isEqualIgnoreCase(YES, invalidOMUExec)) {
                                    disableInfoSet.add(DSMT_LINK_IS_INVALID_MESSAGE);
                                }

                                if (isNotEqualIgnoreCase(YES, active)) {
                                    disableInfoSet.add(DSMT_LINK_IS_NOT_ACTIVE_MESSAGE);
                                }

                                omuDsmtLinkDisplayData.setDisableInfoSet(disableInfoSet);
                            }
                            else {
                                omuDsmtLinkDisplayData.setDisabled(false);
                            }

                            if(isEqualIgnoreCase(SCOPE_IN_STATUS, scope)) {
                                omuDsmtLinkDisplayData.setSelected(true);
                                omuDsmtLinkDisplayData.setCheck(true);
                            }
                            break;
                        }
                    }
                }

                if (isEqualIgnoreCase(node.getActiveInd(), CITI_ACTIVE_STATUS)) {
                    omuDsmtLinkDisplayData.setDeleted(false);
                }
                else {
                    omuDsmtLinkDisplayData.setDeleted(true);
                    omuDsmtLinkDisplayData.setDisabled(true);
                    disableInfoSet.add(OMU_IS_INACTIVE_MESSAGE);
                }

                if (omuDsmtLinkDisplayData.isDisabled() && isSetNotNullOrEmpty(disableInfoSet)) {
                    omuDsmtLinkDisplayData.setDisableInfoSet(disableInfoSet);
                }

                rowCount++;
                // logger.info("omuDsmtLinkDisplayData : " + omuDsmtLinkDisplayData);
                omuDsmtLinkDisplayDataList.add(omuDsmtLinkDisplayData);
            }
        }

        carbonGridDataInfo.setRows(omuDsmtLinkDisplayDataList);

        logger.info("carbonGridDataInfo : " + carbonGridDataInfo);
        logger.info("processDataForDSMTLinks() End");
        return carbonGridDataInfo;
    }

    private EmployeeSelectorHelperAppInfo saveDataForDSMTLinks(
            EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo, GridData gridData) throws Exception {

        logger.info("saveDataForDSMTLinks() Start");

        IGRCObject dsmtLinkObject = null;
        IGRCObject issueObject = null;
        boolean isAllOMUSelected = true;

        Map<String, String> fieldsToUpdateInfo = null;
        List<OMUDsmtLinkDisplayData> omuDsmtLinkProcessList = null;
        EmployeeHRDSMTInfo employeeHRDSMTInfo = null;

        /* Initialize Variables */
        employeeHRDSMTInfo = employeeSelectorHelperAppInfo.getEmployeeHRDSMTInfo();
        logger.info("employeeHRDSMTInfo : " + employeeHRDSMTInfo);

        // logger.info("gridData : " + gridData);
        if (isObjectNotNull(gridData) && isListNotNullOrEmpty(gridData.getRowData())) {

            // logger.info("gridData.getRowData() : " + gridData.getRowData());
            omuDsmtLinkProcessList = gridData.getRowData();
            // logger.info("omuDsmtLinkProcessList : " + omuDsmtLinkProcessList);
            for (OMUDsmtLinkDisplayData data : omuDsmtLinkProcessList) {

                logger.info("data : " + data);

                if (data.isDeleted() || data.isDisabled()) {

                    logger.info("data.isDeleted() : " + data.isDeleted());
                    logger.info("data.isDisabled() : " + data.isDisabled());
                    logger.info("DSMT Link Resource Id : " + data.getResourceId());

                    if (isNotNullOrEmpty(data.getResourceId())) {

                        dsmtLinkObject = grcObjectUtil.getObjectFromId(data.getResourceId());
                        logger.info("DSMT Link Object Name [" + dsmtLinkObject.getName() + "] and Id ["
                                + dsmtLinkObject.getId() + "]");

                        fieldsToUpdateInfo = new HashMap<String, String>();
                        fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, SCOPE_OUT_STATUS);
                        logger.info("Updating DSMT Link Object Scope Field :\n" + fieldsToUpdateInfo);
                        grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                    }
                } else if (data.isCheck()) {

                    logger.info("OMU is selected : " + data.isCheck());

                    if (isNotNullOrEmpty(data.getResourceId())) {

                        logger.info("DSMT Link already exist, Update it!");
                        updateDMSTLink(data.getResourceId(), employeeHRDSMTInfo, data,
                                employeeSelectorHelperAppInfo.getEmployee().getSoeId());
                    }
                    else {
                        logger.info("DSMT Link doesn't exist, create it!");
                        saveDMSTLink(employeeSelectorHelperAppInfo.getObjectID(), employeeHRDSMTInfo, data,
                                employeeSelectorHelperAppInfo.getEmployee().getSoeId());
                    }
                    logger.info("Set the falg for field : " + ISSUE_HR_DSMT_OMU_FIELD);
                    employeeSelectorHelperAppInfo.setOMU(true);
                }
                else if (!data.isCheck()) {

                    logger.info("Is OMU selected ? " + data.isCheck());
                    logger.info("DSMT Link Resource Id : " + data.getResourceId());

                    if (isNotNullOrEmpty(data.getResourceId())) {

                        dsmtLinkObject = grcObjectUtil.getObjectFromId(data.getResourceId());
                        logger.info("DSMT Link Object Name [" + dsmtLinkObject.getName() + "] and Id ["
                                + dsmtLinkObject.getId() + "]");

                        fieldsToUpdateInfo = new HashMap<String, String>();
                        fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, SCOPE_OUT_STATUS);
                        logger.info("Updating DSMT Link Object Scope Field :\n" + fieldsToUpdateInfo);
                        grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
                    }
                    logger.info("This valid OMU is not selected : " + data);
                    isAllOMUSelected = false;
                }
            }
            if (isAllOMUSelected) {
                logger.info("All valid OMU's are Selected : " + isAllOMUSelected);
                if (employeeSelectorHelperAppInfo.getValidation().isOmuUpdate()) {
                    logger.info(
                            "Reset Scorecard Responsible Executive has become responsible for one or more additional OMUs Validation..");
                    issueObject = grcObjectUtil.getObjectFromId(employeeSelectorHelperAppInfo.getObjectID());
                    issueObject = employeeSelectorHelperUtil.resetValidation(issueObject.getId().toString(),
                            ISSUE_VALIDATION_FIELD, ISSUE_OMU_UPDATE);
                    Validation validation = employeeSelectorHelperUtil
                            .getSelectedValidation(issueObject.getId().toString(), ISSUE_VALIDATION_FIELD);
                    employeeSelectorHelperAppInfo.setValidation(validation);

                }
            }
        }

        // Construct DSMT Link query to get the DSMT Links Objects
        String dsmtLinkQuery = DESCOPE_DSMT_LINK_BASE_QUERY.replace(SOEID_STR, gridData.getSoeId()).replace(RESOURCE_ID_STR, employeeSelectorHelperAppInfo.getObjectID());
        logger.info("De-Scope DSMT Link Query: " + dsmtLinkQuery);

        Set<String> dsmtLinkSet = new HashSet<String>();

        dsmtLinkSet = employeeSelectorHelperUtil.getQueryResult(dsmtLinkQuery);
        logger.info("In scope DSMT Link Resource Id List : " + dsmtLinkSet);

        for (String dsmtLinkId : dsmtLinkSet) {

            dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtLinkId);
            logger.info("De-Scope DSMT Link Object Name [" + dsmtLinkObject.getName() + "] and Id ["+ dsmtLinkObject.getId() + "]");

            fieldsToUpdateInfo = new HashMap<String, String>();
            fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, SCOPE_OUT_STATUS);
            logger.info("De-Scope DSMT Link Object :\n" + fieldsToUpdateInfo);
            grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
        }
        // logger.info("saveDataForDSMTLinks() End");
        return employeeSelectorHelperAppInfo;
    }

    private void updateDMSTLink(String dsmtId, EmployeeHRDSMTInfo employeeHRDSMTInfo, OMUDsmtLinkDisplayData data,
            String soeId) throws Exception {

        logger.info("updateDMSTLink() Start");

        // Method Level Variables.
        Map<String, String> fieldsToUpdateInfo = null;
        IGRCObject dsmtLinkObject = null;

        dsmtLinkObject = grcObjectUtil.getObjectFromId(dsmtId);
        logger.info("Updating Object Name [" + dsmtLinkObject.getName() + "], Id [" + dsmtLinkObject.getId()
                + "], and Object Type [" + dsmtLinkObject.getType().getName() + "]");

        // Update Issue object with HR fields
        fieldsToUpdateInfo = new HashMap<String, String>();

        fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, SCOPE_IN_STATUS);
        fieldsToUpdateInfo.put(DSMT_LINK_SELECT_OMU_FIELD, YES);
        fieldsToUpdateInfo.put(DSMT_LINK_ACTIVE_FIELD, YES);
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_GOC_NUMBER_FIELD, data.getManSegmentId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_ID_FIELD, data.getManSegmentId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_NAME_FIELD, employeeHRDSMTInfo.getManagedSegmentName());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_ID_FIELD, data.getManGeoId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_NODE_NUMBER_FIELD, data.getManGeoId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_NAME_FIELD, employeeHRDSMTInfo.getManagedGeographyName());
        //fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_PATH_FIELD, employeeHRDSMTInfo.getManagedSegmentPath());
        //fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_PATH_FIELD, employeeHRDSMTInfo.getManagedGeographyPath());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_HIERARCHIES_FIELD, employeeHRDSMTInfo.getManagedSegmentPath());
        fieldsToUpdateInfo.put(DSMT_LINK_BASE_ID_FIELD, dsmtLinkObject.getId().toString());
        fieldsToUpdateInfo.put(DSMT_LINK_IS_OMU_FIELD, YES);
        fieldsToUpdateInfo.put(DSMT_LINK_SOEID_FIELD, soeId);

        logger.info("Updating DSMT Link Object Fields :\n" + fieldsToUpdateInfo);
        grcObjectUtil.updateFieldsInObjectAndSave(dsmtLinkObject, fieldsToUpdateInfo);
        logger.info("DSMT Link object is updated : " + dsmtLinkObject.getName());

        logger.info("updateDMSTLink() End");
    }

    private void saveDMSTLink(String issueId, EmployeeHRDSMTInfo employeeHRDSMTInfo, OMUDsmtLinkDisplayData data,
            String soeId) throws Exception {

        logger.info("saveDMSTLink() Start");

        // Method Level Variables.
        Map<String, String> fieldsToUpdateInfo;
        IGRCObject newDSMTLinkObject;
        IGRCObject issueObject;
        IGRCObjectCreateInformation createDSMTLinkObjInfo = null;

        issueObject = grcObjectUtil.getObjectFromId(issueId);
        logger.info("Object Name [" + issueObject.getName() + "], Id [" + issueObject.getId() + "] and Type is ["
                + issueObject.getType().getName() + "]");
        createDSMTLinkObjInfo = new IGRCObjectCreateInformation();

        try {

            logger.info("DSMT Link object not present creating a new one");
            createDSMTLinkObjInfo.setSetPrimaryParent(true);
            createDSMTLinkObjInfo.setObjectTypeToCreate(DSMT_LINK);
            createDSMTLinkObjInfo.setPrimaryParentAssociationId(issueObject.getId());

            /* Create an Auto named object, and associate the parent to the Issue */
            logger.info("Going to create object : " + DSMT_LINK);
            newDSMTLinkObject = grcObjectUtil.createAutoNamedObject(createDSMTLinkObjInfo);
            logger.info("Object Created : " + newDSMTLinkObject.getName());
            logger.info("Object Id : " + newDSMTLinkObject.getId());

            /* Associate the newly created object to the given Issue Id as its primary parent. */
            newDSMTLinkObject = grcObjectUtil.associateParentAndChildrentToAnObject(newDSMTLinkObject,
                    createDSMTLinkObjInfo);
            logger.info("associated object : " + newDSMTLinkObject.getName());
            logger.info("associated Id : " + newDSMTLinkObject.getId());

            newDSMTLinkObject = grcObjectUtil.saveResource(newDSMTLinkObject);
            logger.info("New DSMT Link Object Name : " + newDSMTLinkObject.getName());
            logger.info("New DSMT Link Object Id : " + newDSMTLinkObject.getId());
        }
        catch (Exception ex) {

            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "saveDMSTLink() : " + ex.getMessage());
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "saveDMSTLink() : " + getStackTrace(ex));
            throw ex;
        }

        // Update DSMT Link object with HR fields
        fieldsToUpdateInfo = new HashMap<String, String>();

        fieldsToUpdateInfo.put(DSMT_LINK_SCOPE_FIELD, SCOPE_IN_STATUS);
        fieldsToUpdateInfo.put(DSMT_LINK_SELECT_OMU_FIELD, YES);
        fieldsToUpdateInfo.put(DSMT_LINK_ACTIVE_FIELD, YES);
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_GOC_NUMBER_FIELD, data.getManSegmentId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_ID_FIELD, data.getManSegmentId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_NAME_FIELD, employeeHRDSMTInfo.getManagedSegmentName());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_ID_FIELD, data.getManGeoId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_NODE_NUMBER_FIELD, data.getManGeoId());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_NAME_FIELD, employeeHRDSMTInfo.getManagedGeographyName());
        //fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_PATH_FIELD, employeeHRDSMTInfo.getManagedSegmentPath());
        //fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_GEOGRAPHY_PATH_FIELD, employeeHRDSMTInfo.getManagedGeographyPath());
        fieldsToUpdateInfo.put(DSMT_LINK_MANAGED_SEGMENT_HIERARCHIES_FIELD, employeeHRDSMTInfo.getManagedSegmentPath());
        fieldsToUpdateInfo.put(DSMT_LINK_BASE_ID_FIELD, newDSMTLinkObject.getId().toString());
        fieldsToUpdateInfo.put(DSMT_LINK_IS_OMU_FIELD, YES);
        fieldsToUpdateInfo.put(DSMT_LINK_SOEID_FIELD, soeId);

        logger.info("Updating New DSMT Link Object Fields :\n" + fieldsToUpdateInfo);
        grcObjectUtil.updateFieldsInObjectAndSave(newDSMTLinkObject, fieldsToUpdateInfo);
        logger.info("Newly created DSMT Link object is updated : " + newDSMTLinkObject.getName());

        logger.info("saveDMSTLink() End");
    }

    /**
     * <p>
     * This method process the employee search results
     * </P>
     *
     * @param employeeSelectorHelperAppInfo
     *            - an instance of the {@link EmployeeSelectorHelperAppInfo} a master bean that has all the details for
     *            the current session
     * @param queryResults-
     *            The employee search results
     * @return - a list of Employee based on the execute search query
     * @throws Exception
     *             - Generic Exception
     */
    private List<Employee> processEmployeeSearchResults(ResultSet queryResults) throws Exception {

        logger.info("processEmployeeSearchResults() Start");

        // Method Level Variables.
        int count = 1;
        int searchLimit;

        Employee employee;
        List<Employee> employeeList;

        /* Initialize Variables */
        employeeList = new ArrayList<Employee>();
        searchLimit = getIntValue(applicationUtil.getRegistrySetting(MAX_EMPLOYEE_SEARCH_RESULT));
        logger.info("Search Limit Val from reg: " + searchLimit);
        searchLimit = searchLimit <= 0 ? 200 : searchLimit;
        logger.info("Calculated Search Limit Val: " + searchLimit);

        /* Make sure the query results are present */
        if (isObjectNotNull(queryResults)) {

            /* Iterate through each item in the query results. */
            while (queryResults.next()) {

                /* On max count reach quit */
                if (count > searchLimit) {
                    break;
                }

                employee = new Employee();
                employee.setSoeId(queryResults.getString(1));
                employee.setFirstName(queryResults.getString(2));
                employee.setLastName(queryResults.getString(3));
                employee.setGocId(queryResults.getString(4));
                employee.setEmailId(queryResults.getString(5));

                employeeList.add(employee);
                logger.info("employee : " + employee);
                count++;
            }
        }

        logger.info("processEmployeeSearchResults() End");
        return employeeList;
    }

    /**
     * <p>
     * This method
     * </P>
     *
     * @param queryResults-
     *            The employee search results
     * @return - a list of Employee based on the execute search query
     * @throws Exception
     *             - Generic Exception
     */
    private EmployeeHRDSMTInfo processEmployeeHRDSMTResults(String soeId) throws Exception {

        logger.info("processEmployeeHRDSMTResults() Start");

        Connection connection = null;
        ResultSet queryResults = null;
        PreparedStatement preparedStmt = null;
        EmployeeHRDSMTInfo employeeHRDSMTInfo = null;

        /* Initialize Variables */
        int count = 1;
        List<String> preparedStmtValues = new ArrayList<String>();
        employeeHRDSMTInfo = new EmployeeHRDSMTInfo();

        try {

            /* Get a connection to the database and create the prepared statement using the query */
            connection = dsmtHelperAppBaseDAO.getConnection();
            preparedStmt = connection.prepareStatement(HR_DSMT_GOC_SEARCH_QUERY);

            if (isNotNullOrEmpty(soeId)) {
                preparedStmtValues.add(soeId);
            }
            else {
                preparedStmtValues.add(null);
            }

            /* Log the information for debugging */
            logger.info("HR DSMT Query to execute: " + HR_DSMT_GOC_SEARCH_QUERY);
            logger.info("Prepared Stmt Values: " + preparedStmtValues);

            /* Set the query params in the prepared statement created */
            for (String preparedStmtValue : preparedStmtValues) {

                preparedStmt.setString(count, preparedStmtValue);
                count++;
            }

            /* Execute the query and get the query results. */
            logger.info("Going to execute query with values: " + preparedStmt.toString());
            queryResults = preparedStmt.executeQuery();

            /* Make sure the query results are present */
            if (isObjectNotNull(queryResults)) {

                /* Iterate through each item in the query results. */
                while (queryResults.next()) {

                    employeeHRDSMTInfo.setFirstName(queryResults.getString(1));
                    employeeHRDSMTInfo.setLastName(queryResults.getString(2));
                    employeeHRDSMTInfo.setSoeId(queryResults.getString(3));
                    employeeHRDSMTInfo.setEmailId(queryResults.getString(4));
                    employeeHRDSMTInfo.setGocId(queryResults.getString(5));
                    employeeHRDSMTInfo.setGocName(queryResults.getString(6));
                    employeeHRDSMTInfo.setManagedSegmentId(queryResults.getString(7));
                    employeeHRDSMTInfo.setManagedSegmentName(queryResults.getString(8));
                    employeeHRDSMTInfo.setManagedSegmentPath(queryResults.getString(9));
                    employeeHRDSMTInfo.setManagedGeographyId(queryResults.getString(10));
                    employeeHRDSMTInfo.setManagedGeographyName(queryResults.getString(11));
                    employeeHRDSMTInfo.setManagedGeographyPath(queryResults.getString(12));
                    employeeHRDSMTInfo.setLegalEntityId(queryResults.getString(13));
                    employeeHRDSMTInfo.setLegalEntityName(queryResults.getString(14));
                    employeeHRDSMTInfo.setLegalEntityPath(queryResults.getString(15));
                    employeeHRDSMTInfo.setGocStatus(queryResults.getString(16));
                    employeeHRDSMTInfo.setMappingStatus(queryResults.getString(17));
                    employeeHRDSMTInfo.setCreationDate(queryResults.getString(18));
                    employeeHRDSMTInfo.setUpdatedDate(queryResults.getString(19));

                    logger.info("employeeHRDSMTInfo : " + employeeHRDSMTInfo);
                    break;
                }
            }

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

        logger.info("processEmployeeHRDSMTResults() End");
        return employeeHRDSMTInfo;
    }

    /**
     * <p>
     * The method processes the header names list to the header names for the AG Grid.
     * </P>
     *
     * @param fieldNamesList
     *            - A list of fields that will be used in the tabular header
     * @return - A list of DataGridHeaderColumnInfo that has the header representation of the Data Grid
     */
    private List<GridDataHeaderColumnInfo> getHeaderLabelList(Map<String, String> fieldNamesMap) {

        logger.info("getHeaderLabelList() Start");

        // Method Level Variables.
        GridDataHeaderColumnInfo searchHeader;
        List<GridDataHeaderColumnInfo> searchHeaderList;

        /* Initialize Variables */
        searchHeaderList = new ArrayList<>();
        fieldNamesMap = isMapNotNullOrEmpty(fieldNamesMap) ? fieldNamesMap : new HashMap<>();
        logger.info("Field Names Map: " + fieldNamesMap);

        /* Iterate through the map of header field names and labels */
        for (String headerName : fieldNamesMap.keySet()) {

            // Prepare a header item for each header fields
            searchHeader = new GridDataHeaderColumnInfo();

            // Set the header name and field name and add it to the list
            searchHeader.setHeaderName(headerName);
            searchHeader.setField(fieldNamesMap.get(headerName));
            searchHeaderList.add(searchHeader);
        }

        // Log and Return the header list
        // logger.info("Column Header list: " + searchHeaderList);
        logger.info("getHeaderLabelList() End");
        return searchHeaderList;
    }

    private String employeePrintFormat(Employee employee) {

        String firstName = (isNullOrEmpty(employee.getFirstName())) ? EMPTY_STRING : employee.getFirstName().trim();
        String lastName = (isNullOrEmpty(employee.getLastName())) ? EMPTY_STRING : employee.getLastName().trim();
        String soeId = (isNullOrEmpty(employee.getSoeId())) ? EMPTY_STRING : employee.getSoeId().trim();

        return firstName + SINGLE_SPACE + lastName + SINGLE_SPACE + OPEN_BRACKET + soeId + CLOSE_BRACKET;

    }

    private boolean isSoeIdExist(String soeId) throws Exception {

        logger.info("isSoeIdExist() Start");
        logger.debug("Select SOE Id SQL : " + SELECT_BY_SOE_ID_SQL);
        logger.info("soeId : " + soeId);
        boolean result = false;
        if (isNotNullOrEmpty(soeId)) {
            try (Connection connection = dsmtHelperAppBaseDAO.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(SELECT_BY_SOE_ID_SQL)) {
                preparedStmt.setString(1, soeId);
                ResultSet rs = preparedStmt.executeQuery();
                while (rs.next()) {
                    String resultSOEId = rs.getString(1);
                    logger.info("ResultSet Soe Id : " + resultSOEId);
                    if (isEqualIgnoreCase(soeId, resultSOEId)) {
                        logger.debug("SoeId already exist in the OP Employee table : " + resultSOEId);
                        result = true;
                    }
                }
            } catch (Exception e) {

                logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "isSoeIdExist()" + getStackTrace(e));
                throw e;
            }
        }
        logger.info("isSoeIdExist() End");
        return result;
    }

    private int insertSoeId(String soeId) throws Exception {

        logger.info("insertSoeId() Start");
        logger.debug("Insert SOE Id SQL : " + INSERT_SOE_ID_SQL);
        logger.info("soeId : " + soeId);
        int row = -1;
        if (isNotNullOrEmpty(soeId)) {
            try (Connection connection = dsmtHelperAppBaseDAO.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(INSERT_SOE_ID_SQL)) {
                preparedStmt.setString(1, soeId);
                row = preparedStmt.executeUpdate();
                logger.info("row : " + row);
            } catch (Exception e) {

                logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "insertSoeId()" + getStackTrace(e));
                throw e;
            }
        }
        logger.info("insertSoeId() End");
        return row;
    }

    private OMUDsmtRoot getOMUDsmtDataFromJSON(String soeId) throws Exception {

        logger.info("getOMUDsmtDataFromJSON() Start");
        String jsonFilePath = "/home/opuser/OP/OpenPages/omu_response.json";

        ObjectMapper objectMapper = new ObjectMapper();

        OMUDsmtRoot omuDsmtRoot = null;
        OMUDsmtRoot omuDsmtFilterData = new OMUDsmtRoot();
        List<Data> filterData = new ArrayList<Data>();
        Node node = null;

        File initialFile = new File(jsonFilePath);
        try {
            InputStream inputStream = new FileInputStream(initialFile);

            omuDsmtRoot = objectMapper.readValue(inputStream, OMUDsmtRoot.class);
            logger.info("omuDsmtRoot.getMessage() : " + omuDsmtRoot.getMessage());
            logger.info("omuDsmtRoot.isSuccess() : " + omuDsmtRoot.isSuccess());
            logger.info("Node size : " + omuDsmtRoot.getData().size());

            for (Data data : omuDsmtRoot.getData()) {

                node = data.getNode();
                if (isObjectNotNull(node)) {

                    String soeIdLowerCase = node.getSoeId().toLowerCase();

                    if (soeIdLowerCase.equals(soeId.toLowerCase())) {
                        filterData.add(data);
                    }
                }
                else {
                    logger.info("Node is null : " + node);
                }
            }
        }
        catch (Exception e) {
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getOMUDsmtDataFromJSON()" + getStackTrace(e));
            throw e;
        }
        omuDsmtFilterData.setData(filterData);
        logger.info("filterData : " + filterData);
        logger.info("filterData.size() : " + filterData.size());
        logger.info("getOMUDsmtDataFromJSON() End");
        return omuDsmtFilterData;
    }
}