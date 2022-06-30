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

package com.ibm.openpages.ext.ui.controller;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.*;
import static com.ibm.openpages.ext.ui.constant.EmployeeSelectorConstants.*;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectTypeUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.bean.Employee;
import com.ibm.openpages.ext.ui.bean.EmployeeHRDSMTInfo;
import com.ibm.openpages.ext.ui.bean.EmployeeSelectorHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.GridData;
import com.ibm.openpages.ext.ui.bean.Validation;
import com.ibm.openpages.ext.ui.service.IEmployeeSelectorService;
import com.ibm.openpages.ext.ui.util.EmployeeSelectorHelperUtil;
import com.openpages.apps.common.SimpleException;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "jspview/employeeSelectorApp")
public class EmployeeSelectorController {

    private Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IGRCObjectTypeUtil objectTypeUtil;

    @Autowired
    IGRCObjectUtil grcObjectUtil;

    @Autowired
    IFieldUtil fieldUtil;

    @Autowired
    IEmployeeSelectorService employeeSelectorService;

    @Autowired
    EmployeeSelectorHelperUtil employeeSelectorHelperUtil;

    /**
     * Initialize any required service post the object construction.
     */
    @PostConstruct
    public void initController() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(EMPLOYEE_SELECTOR_CONTROLLER_LOG_FILE_NAME);
    }

    /**
     * <P>
     * This method handles the initialization of the helper. The method fetches all necessary data for the landing page
     * of the helper. The helpers header information any application text strings and any associated object information
     * is sent back to the UI for the landing page display.
     * </P>
     *
     * @param resourceId
     *            - String resourceId
     * @param model
     *            - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the EmployeeSelectorHelperAppInfo
     * @throws Exception
     */
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public @ResponseBody EmployeeSelectorHelperAppInfo getInitialPageForApp(@RequestParam("resourceId") String objectId,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.info("Employee Selector Helper Init Start");

        // Method Level Variables.
        String grcObjectType = EMPTY_STRING;
        String scorecardExeInfo = EMPTY_STRING;
        String issueStatus = EMPTY_STRING;

        IGRCObject grcObject = null;
        Validation validation = new Validation();
        Employee employee = null;;
        EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo = null;

        try {

            /* Initialize Variables */
            logger.info("Resource Id: " + objectId);
            grcObject = grcObjectUtil.getObjectFromId(objectId);
            grcObjectType = grcObject.getType().getName();
            employeeSelectorHelperAppInfo = new EmployeeSelectorHelperAppInfo();

            // Update the Object information
            if (isEqualIgnoreCase(grcObjectType, ISSUE)) {

                issueStatus = fieldUtil.getFieldValueAsString(grcObject, ISSUE_STATUS_FIELD);
                scorecardExeInfo = fieldUtil.getFieldValueAsString(grcObject, ISSUE_SCORECARD_EXEC_NAME_FIELD);
                logger.info("scorecardExeInfo: " + scorecardExeInfo);
                employeeSelectorHelperAppInfo.setObjectStatus(issueStatus);
                validation = employeeSelectorHelperUtil.getSelectedValidation(objectId, ISSUE_VALIDATION_FIELD);

                employee = employeeSelectorHelperUtil.getEmployee(objectId, ISSUE_SCORECARD_EXEC_NAME_FIELD);
                logger.info("employee: " + employee);

                if(isObjectNull(employee) || isNullOrEmpty(employee.getSoeId())) {
                    logger.info("Resetting Scorecard Executive Info because unable to determine Soe Id!");
                    scorecardExeInfo = EMPTY_STRING;
                } else {
                    scorecardExeInfo = employee.getSoeId();
                }

                if(validation.isInactiveUser()) {
                    logger.info("Resetting Scorecard Executive Info because user is inactive : " + validation.isInactiveUser());
                    scorecardExeInfo = EMPTY_STRING;
                }

                if(validation.isOmuExecUpdate()) {
                    logger.info("Resetting Scorecard Responsible Executive’s HR DSMT has become invalid because HR DSMT is invalid : " + validation.isOmuExecUpdate());
                    scorecardExeInfo = EMPTY_STRING;
                }

            }
            else if (isEqualIgnoreCase(grcObjectType, CORRECTIVE_ACITON_PLAN)) {

                employeeSelectorHelperAppInfo
                        .setObjectStatus(fieldUtil.getFieldValueAsString(grcObject, CAP_STATUS_FIELD));
                validation = employeeSelectorHelperUtil.getSelectedValidation(objectId, CAP_VALIDATION_FIELD);
            }

            employeeSelectorHelperAppInfo.setObjectName(grcObject.getName());
            employeeSelectorHelperAppInfo.setObjectDescription(grcObject.getDescription());
            employeeSelectorHelperAppInfo.setObjectID(objectId);
            employeeSelectorHelperAppInfo.setObjectType(grcObjectType);
            employeeSelectorHelperAppInfo.setObjectTypeLabel(grcObject.getType().getLocalizedLabel());
            employeeSelectorHelperAppInfo.setScorecardExeInfo(scorecardExeInfo);
            employeeSelectorHelperAppInfo.setValidation(validation);
            employeeSelectorHelperAppInfo.setObjRegistrySetting(EMPLOYEE_SELECTOR_APP_BASE_SETTING + grcObject.getType().getLocalizedLabel());

            /* Log information for debugging. */
            logger.info("Object Id: " + objectId);
            logger.info("Scorecard Exe Info: " + scorecardExeInfo);

            grcObject = grcObjectUtil.getObjectFromId(objectId);
            logger.info("Object Name : " + grcObject.getName());

            logger.info("GRC Object Type for the DSMT Link : " + grcObjectType);

            employeeSelectorService.getHelperHeaderInfo(employeeSelectorHelperAppInfo);
            employeeSelectorService.getLandingPageInfo(employeeSelectorHelperAppInfo);
        }
        catch (Exception ex) {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getInitialPageForApp()" + getStackTrace(ex));
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        logger.info(
                "Employee Selector Helper Information before setting it in session: " + employeeSelectorHelperAppInfo);
        session.setAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION, employeeSelectorHelperAppInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("Employee Selector Helper Init End");
        return employeeSelectorHelperAppInfo;
    }

    /**
     * <P>
     * This method get the existing OMU DSMT for selected Employee
     * </P>
     *
     * @param employee
     *            - Employee employee
     * @param model
     *            - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception
     */
    @RequestMapping(value = "/getExistingOMU", method = RequestMethod.GET)
    public @ResponseBody GridData getExistingOMU(@RequestParam("soeId") String soeId, Model model, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("getExistingOMU() Start");

        GridData existingOMUList = null;
        EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo = null;

        try {

            /* Initialize Variables */
            employeeSelectorHelperAppInfo = (EmployeeSelectorHelperAppInfo) session
                    .getAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION);

            existingOMUList = employeeSelectorService.getExistingOMU(employeeSelectorHelperAppInfo, soeId, session);
        }
        catch (Exception ex) {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getExistingOMU()" + getStackTrace(ex));
            throw opException;
        }

        /* Log for debugging. */
        logger.info("employeeSelectorHelperAppInfo: " + employeeSelectorHelperAppInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("getExistingOMU() End");
        return existingOMUList;
    }

    /**
     * <P>
     * This method searches employee based on soeId or first name or last name.
     * </P>
     *
     * @param soeId
     *            - String soeId
     * @param firstName
     *            - String firstName
     * @param lastName
     *            - String lastName
     * @param model
     *            - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DataGridInfo
     * @throws Exception
     */
    @RequestMapping(value = "/searchEmployee", method = RequestMethod.GET)
    public @ResponseBody GridData searchEmployee(@RequestParam("soeId") String soeId,
            @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, Model model,
            HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("searchEmployee() Start");

        // Method Level Variables.
        GridData employeeList = null;
        EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo = null;

        try {

            /* Initialize Variables */
            logger.info("Employee soeId: " + soeId);
            logger.info("Employee firstName: " + firstName);
            logger.info("Employee lastName: " + lastName);
            employeeSelectorHelperAppInfo = (EmployeeSelectorHelperAppInfo) session
                    .getAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION);
            employeeList = employeeSelectorService.searchEmployee(soeId, firstName, lastName);
            logger.info("employeeList : " + employeeList);
        }
        catch (Exception ex) {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "searchEmployee()" + getStackTrace(ex));
            throw opException;
        }

        /* Log for debugging. */
        logger.info("employeeSelectorHelperAppInfo : " + employeeSelectorHelperAppInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("searchEmployee() End");
        return employeeList;
    }

    /**
     * <P>
     * This method searches employee based on soeId or first name or last name.
     * </P>
     *
     * @param soeId
     *            - String soeId
     * @param gocId
     *            - String gocId
     * @param model
     *            - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DataGridInfo
     * @throws Exception
     */
    @RequestMapping(value = "/getEmployeeHRDSMTInfo", method = RequestMethod.GET)
    public @ResponseBody GridData getEmployeeHRDSMTInfo(@RequestParam("soeId") String soeId, Model model,
            HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("getEmployeeHRDSMTInfo() Start");

        // Method Level Variables.
        GridData gridData = null;
        EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo = null;
        List<EmployeeHRDSMTInfo> employeeHRDSMTInfoList = null;
        Employee employee = new Employee();

        try {

            /* Initialize Variables */
            logger.info("Employee soeId: " + soeId);
            employeeSelectorHelperAppInfo = (EmployeeSelectorHelperAppInfo) session
                    .getAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION);

            employee.setSoeId(soeId);
            employee.setGocId(EMPTY_STRING);
            employee.setFirstName(EMPTY_STRING);
            employee.setLastName(EMPTY_STRING);
            employeeSelectorHelperAppInfo.setEmployee(employee);

            gridData = employeeSelectorService.getEmployeeHRDSMTInfo(soeId);
            employeeHRDSMTInfoList = (List<EmployeeHRDSMTInfo>) gridData.getRows();

            if (isListNotNullOrEmpty(employeeHRDSMTInfoList)) {
                logger.info("EmployeeHRDSMTInfo : " + employeeHRDSMTInfoList.get(0));
                employeeSelectorHelperAppInfo.setEmployeeHRDSMTInfo(employeeHRDSMTInfoList.get(0));
            }
        }
        catch (Exception ex) {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getEmployeeHRDSMTInfo()" + getStackTrace(ex));
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        session.setAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION, employeeSelectorHelperAppInfo);

        /* Log for debugging. */
        logger.info("employeeSelectorHelperAppInfo : " + employeeSelectorHelperAppInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("getEmployeeHRDSMTInfo() End");
        return gridData;
    }

    /**
     * <P>
     * This method handles the process Employee Selector (Issue) Helper Logic.
     * </P>
     *
     * @param selectedOMUList
     *            - List<OMUDsmtRoot> selectedOMUList
     * @param executionType
     *            - String executionType
     * @param model
     *            - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception
     */
    @RequestMapping(value = "/Issue/save", method = RequestMethod.POST)
    public @ResponseBody EmployeeSelectorHelperAppInfo processIssueHelperLogic(@RequestBody GridData gridData,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.info("processIssueHelperLogic() Start");
        logger.info("GridData : " + gridData);

        // Method Level Variables.
        EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo = null;

        try {

            /* Initialize Variables */
            employeeSelectorHelperAppInfo = (EmployeeSelectorHelperAppInfo) session
                    .getAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION);

            employeeSelectorHelperAppInfo = employeeSelectorService
                    .processIssueHelperLogic(employeeSelectorHelperAppInfo, gridData);
        }
        catch (Exception ex) {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "processIssueHelperLogic()" + getStackTrace(ex));
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        session.setAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION, employeeSelectorHelperAppInfo);

        /* Log for debugging. */
        logger.info("employeeSelectorHelperAppInfo : " + employeeSelectorHelperAppInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("processIssueHelperLogic() End");
        return employeeSelectorHelperAppInfo;
    }

    /**
     * <P>
     * This method handles the process Employee Selector (CAP) Helper Logic.
     * </P>
     *
     * @param model
     *            - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception
     */
    @RequestMapping(value = "/CAP/save", method = RequestMethod.POST)
    public @ResponseBody EmployeeSelectorHelperAppInfo processCAPHelperLogic(@RequestBody Employee employee,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.info("processCAPHelperLogic() Start");
        logger.info("Employee Info : " + employee);

        // Method Level Variables.
        EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo = null;

        try {

            /* Initialize Variables */
            employeeSelectorHelperAppInfo = (EmployeeSelectorHelperAppInfo) session
                    .getAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION);

            if(isObjectNotNull(employee)) {
                employeeSelectorHelperAppInfo.setEmployee(employee);
            } else {

                employee = employeeSelectorHelperAppInfo.getEmployee();
                logger.info("Current session employee Info : " + employee);
            }

            employeeSelectorHelperAppInfo = employeeSelectorService
                    .processCAPHelperLogic(employeeSelectorHelperAppInfo);
        }
        catch (Exception ex) {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "processCAPHelperLogic()" + getStackTrace(ex));
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        session.setAttribute(EMPLOYEE_SELECTOR_HELPER_INFORMATION, employeeSelectorHelperAppInfo);

        /* Log for debugging. */
        logger.info("employeeSelectorHelperAppInfo : " + employeeSelectorHelperAppInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("processCAPHelperLogic() End");
        return employeeSelectorHelperAppInfo;
    }

}
