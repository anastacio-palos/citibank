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

package com.ibm.openpages.ext.ui.constant;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class EmployeeSelectorConstants {

    // Helper Base Setting to get Header Information and other Helper related information
    public static final String EMPLOYEE_SELECTOR_APP_BASE_SETTING = "/OpenPages/Custom Deliverables/Helper App/Employee Selector App/";
    public static final String EMPLOYEE_SELECTOR_APP_OBJECT_DISPLAY_INFO = "/Object Field Diplay Info";
    public static final String EMPLOYEE_SELECTOR_APP_TITLE = "/Header Info/App Title";
    public static final String EMPLOYEE_SELECTOR_APP_DISPLAY_NAME = "/Header Info/Display Name";
    public static final String MAX_EMPLOYEE_SEARCH_RESULT = "/OpenPages/Custom Deliverables/Helper App/Employee Selector App/Search/Employee Search Max Records";
    public static final String CRC_OMU_DATA_WEBHOOK_URL_REG = "/OpenPages/Custom Deliverables/Helper App/Employee Selector App/Issue/Citi Risk and Controls OMU Service SOE Id WebHook";
    public static final String ISSUE_RESP_EXEC = "/OpenPages/Custom Deliverables/Helper App/Employee Selector App/Issue/Issue Responsible Executive Status";
    public static final String ISSUE_SCORECARD_EXEC_REG = "/OpenPages/Custom Deliverables/Helper App/Employee Selector App/Issue/Issue Scorecard Responsible Executive Status";

    public static final Map<String, String> EMPLOYEE_HEADER_MAP = new LinkedHashMap<String, String>() {

        {
            put("soeId", "SOE Id");
            put("firstName", "First Name");
            put("lastName", "Last Name");
            put("gocId", "GOC Id");
            put("emailId", "Email Address");
        }
    };

    public static final Map<String, String> EMPLOYEE_HR_DSMT_HEADER_MAP = new LinkedHashMap<String, String>() {

        {
            put("firstName", "First Name");
            put("lastName", "Last Name");
            put("soeId", "SOE Id");
            put("emailId", "Email Address");
            put("gocId", "GOC Number");
            put("gocName", "GOC Name");
            put("managedSegmentId", "Managed Segment ID");
            put("managedSegmentName", "Managed Segment Name");
            put("managedSegmentPath", "Managed Segment Path");
            put("managedGeographyId", "Managed Geography ID");
            put("managedGeographyName", "Managed Geography Name");
            put("managedGeographyPath", "Managed Geography Path");
            put("legalEntityId", "Legal Entity ID");
            put("legalEntityName", "Legal Entity Name");
            put("legalEntityPath", "Legal Entity Path");
            put("gocStatus", "Status");
            put("mappingStatus", "Mapping Status");
            put("creationDate", "Creation Date");
            put("updatedDate", "Updated Date");
        }
    };

    public static final Map<String, String> OMU_DSMT_LINK_HEADER_MAP = new LinkedHashMap<String, String>() {

        {
            put("activeInd", "OMU Status");
            put("employeeStatus", "Employee Status");
            put("manSegmentId", "Managed Segment Id");
            put("manGeoId", "Managed Geography Id");
            put("soeId", "SOE Id");
        }
    };

    // Field Name
    public static final String DSMT_LINK_SCOPE_FIELD = "Citi-DL:Scp";
    public static final String DSMT_LINK_STATUS_FIELD = "Citi-DL:Status";
    public static final String DSMT_LINK_TYPE_FIELD = "Citi-DSMT:Type";
    public static final String DSMT_LINK_BASE_ID_FIELD = "Citi-DL:BaseID";
    public static final String DSMT_LINK_ACTIVE_FIELD = "Citi-DL:Active";
    public static final String DSMT_LINK_SELECT_OMU_FIELD = "Citi-DL:Select OMU";
    public static final String DSMT_LINK_MSNAME_FIELD = "Citi-DSMT:MSName";
    public static final String DSMT_LINK_LVNAME_FIELD = "Citi-DSMT:LVName";
    public static final String DSMT_LINK_INVALID_OMU_EXEC_FIELD = "Citi-DL:InvalidOMUExec";
    public static final String DSMT_LINK_IS_OMU_FIELD = "Citi-DSMT:IsOMU";
    public static final String DSMT_LINK_SOEID_FIELD = "Citi-DSMT:OMUExec";

    public static final String DSMT_LINK_MANAGED_SEGMENT_ID_FIELD = "Citi-DSMT:MSID";
    public static final String DSMT_LINK_MANAGED_SEGMENT_NAME_FIELD = "Citi-DSMT:MSName";
    public static final String DSMT_LINK_MANAGED_GEOGRAPHY_ID_FIELD = "Citi-DSMT:MGID";
    public static final String DSMT_LINK_MANAGED_GEOGRAPHY_NAME_FIELD = "Citi-DSMT:MGName";
//    public static final String DSMT_LINK_MANAGED_SEGMENT_PATH_FIELD = "Citi-DL:MSPath";
//    public static final String DSMT_LINK_MANAGED_GEOGRAPHY_PATH_FIELD = "Citi-DL:MGPath";
    public static final String DSMT_LINK_MANAGED_SEGMENT_GOC_NUMBER_FIELD = "Citi-DL:MngdSegmentGOCNum";
    public static final String DSMT_LINK_MANAGED_GEOGRAPHY_NODE_NUMBER_FIELD = "Citi-DL:MngdGeogNodeNum";
    public static final String DSMT_LINK_MANAGED_SEGMENT_HIERARCHIES_FIELD = "Citi-DSMT:ManagedSegHier";

    public static final String ISSUE_GOC_NUMBER_FIELD = "Citi-Iss:GOCNum";
    public static final String ISSUE_GOC_NAME_FIELD = "Citi-Iss:GOCName";
    public static final String ISSUE_MANAGED_SEGMENT_ID_FIELD = "Citi-Iss:MgdSegId";
    public static final String ISSUE_MANAGED_SEGMENT_NAME_FIELD = "Citi-Iss:MgdSegName";
    public static final String ISSUE_MANAGED_SEGMENT_PATH_FIELD = "Citi-Iss:MgdSegPath";
    public static final String ISSUE_MANAGED_GEOGRAPHY_ID_FIELD = "Citi-Iss:MgdGeoID";
    public static final String ISSUE_MANAGED_GEOGRAPHY_NAME_FIELD = "Citi-Iss:MgdGeoName";
    public static final String ISSUE_MANAGED_GEOGRAPHY_PATH_FIELD = "Citi-Iss:MgdGeoPath";
    public static final String ISSUE_LEGAL_ENTITY_ID_FIELD = "Citi-Iss:LegEntID";
    public static final String ISSUE_LEGAL_ENTITY_NAME_FIELD = "Citi-Iss:LegEntName";
    public static final String ISSUE_RESP_EXEC_NAME_FIELD = "Citi-Iss:RespExcName";
    public static final String ISSUE_RESP_EXEC_TITLE_FIELD = "Citi-Iss:RespExcTitle";
    public static final String ISSUE_SCORECARD_EXEC_NAME_FIELD = "Citi-Iss:ScrcrdRespExcName";
    public static final String ISSUE_SCORECARD_EXEC_TITLE_FIELD = "Citi-Iss:ScrcrdRespExcTitle";
    public static final String ISSUE_HR_DSMT_OMU_FIELD = "Citi-Iss:HR DSMT-OMU";
    public static final String ISSUE_VALIDATION_FIELD = "Citi-Iss:IssVal";
    public static final String ISSUE_STATUS_FIELD = "Citi-Iss:Status";

    public static final String CAP_ACTION_PLAN_OWNER_FIELD = "Citi-CAP:Assignee";
    public static final String CAP_STATUS_FIELD = "Citi-CAP:Status";
    public static final String CAP_VALIDATION_FIELD = "Citi-CAP:CAPVal";

    public static final String EMPLOYEE_SELECTOR_CONTROLLER_LOG_FILE_NAME = "employee_selector_helper_controller.log";
    public static final String EMPLOYEE_SELECTOR_LOG_FILE_NAME = "employee_selector_helper.log";
    public static final String EMPLOYEE_SELECTOR_UTIL_LOG_FILE_NAME = "employee_selector_helper_util.log";
    public static final String EMPLOYEE_SELECTOR_HELPER_INFORMATION = "Employee_Selector_Helper_Info";
    public static final String DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE = "DSMT Link status is not null or empty.";
    public static final String DSMT_LINK_IS_NOT_ACTIVE_MESSAGE = "DSMT Link is not Active.";
    public static final String DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE = "DSMT Link is out of scope.";
    public static final String BASE_DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE = "Base DSMT Link status is not null or empty.";
    public static final String BASE_DSMT_LINK_IS_NOT_ACTIVE_MESSAGE = "Base DSMT Link is not Active.";
    public static final String BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE = "Base DSMT Link is out of scope.";
    public static final String DSMT_LINK_IS_INVALID_MESSAGE = "DSMT Link is invalid.";
    public static final String OMU_IS_INACTIVE_MESSAGE = "OMU is inactive.";

    public static final String HR_DSMT_VALUE = "HR DSMT";
    public static final String OMU_DSMT_VALUE = "OMU";
    public static final String SCOPE_IN_STATUS = "In";
    public static final String SCOPE_OUT_STATUS = "Out";
    public static final String ISSUE_EXECUTION = "issueExecution";
    public static final String SCORE_CARD_EXECUTION = "scorecardExecution";
    public static final String OPEN_BRACKET = "(";
    public static final String CLOSE_BRACKET = ")";
    public static final String CITI_ACTIVE_STATUS = "A";
    public static final String CAP_INACTIVE_ACTION_OWNER = "Inactive Action Owner";
    public static final String CAP_ACTION_OWNER_HR_UPDATE = "Action Owner HR Update";
    public static final String ISSUE_INVALID_OMU_EXEC = "Invalid OMU Exec";
    public static final String ISSUE_INACTIVE_OMU = "Inactive OMU";
    public static final String ISSUE_OMU_UPDATE = "OMU Update";
    public static final String ISSUE_OMU_EXEC_UPDATE = "OMU Exec Update";
    public static final String ISSUE_SCORECARD_HR_UPDATE = "Scorecard HR Update";
    public static final String ISSUE_INACTIVE_USER = "Inactive User";
    public static final String ISSUE_RESPONSIBLE_EXECUTIVE = "Issue Responsible Executive";
    public static final String SCORECARD_EXECUTIVE = "Scorecard Executive";

    public static final String ISSUE = "Citi_Iss";
    public static final String CORRECTIVE_ACITON_PLAN = "Citi_CAP";
    public static final String DSMT = "Citi_DSMT";
    public static final String DSMT_LINK = "Citi_DSMT_Link";

    public static final String APPLICATION_URL_HOST = "/OpenPages/Custom Deliverables/Object URL Generator/Host";
    public static final String APPLICATION_URL_PORT = "/OpenPages/Custom Deliverables/Object URL Generator/Port";
    public static final String APPLICATION_URL_PROTOCOL = "/OpenPages/Custom Deliverables/Object URL Generator/Protocol";
    public static final String APPLICATION_TASK_VIEW_URL_DETAIL_PAGE = "/OpenPages/Custom Deliverables/Object URL Generator/Task View Page";
    public static final String MODAL_NEW_WINDOW_INFO = "target=\"_blank\" rel=\"noreferrer\"";

    public static final String EMPLOYEE_DATA_ACCEES_CONFIG_LOG_FILE_NAME = "employee_data_access_config.log";
    public static final String EMPLOYEE_DATA_HELPER_APP_BASE_LOG_FILE_NAME = "EmployeeSelectorAppBaseDAOImpl.log";
    public static final String EMPLOYEE_DB_JDBC_CONNECTION_STRING = "jdbc:oracle:thin:@//";

    public static final String EMPLOYEE_DB_DATABASE_FILE_PATH = "/OpenPages/Custom Deliverables/Interfaces/DSMT Interface/DSMT DB/DB Properties File Path and Name";

    public static final String EXISTING_DSMT_LINK_FIELDS_INFO = "Name,Citi-DSMT:Type,Citi-DL:Status,Citi-DL:Scp,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName";

    public static final String EXISTING_DSMT_LINK_FIELDS_FOR_DISPLAY = "Resource ID, Name, Description";

    public static final String GENERIC_EXCEPTION_MESSAGE = "A system error occured. Please contact your System Administrator.";
    public static final String HELPER_ACCESS_MESSAGE = "You are not an Authorized user to run this helper.";
    public static final String HELPER_ERROR_MESSAGE = "Issue Status is not valid to run either Issue Responsible Executive or Scorecard Responsible Executive helper.";

    public static final String BASE_ID_STR = "<Base_ID>";
    public static final String SOEID_STR = "<SOEID>";
    public static final String RESOURCE_ID_STR = "<Resource_ID>";
    public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss zzz";

    public static final String SELECT_BY_SOE_ID_SQL = "SELECT SOE_ID, DELTA, ACTIVE, SYNC_STATUS, ERROR_DESC, FAIL_COUNT FROM OP_EMPLOYEE WHERE SOE_ID = ?";
    public static final String INSERT_SOE_ID_SQL = "INSERT INTO OP_EMPLOYEE (SOE_ID, DELTA, ACTIVE, SYNC_STATUS) VALUES (?, 'N', 'Y', 'SUCCESS')";

    // Query
    public static final String DSMT_LINK_BASE_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Citi-DSMT:OMUExec], [Citi_DSMT_Link].[Citi-DSMT:MSID], [Citi_DSMT_Link].[Citi-DSMT:MGID], [Citi_DSMT_Link].[Citi-DSMT:IsOMU], [Citi_DSMT_Link].[Citi-DL:Active], [Citi_DSMT_Link].[Citi-DL:Scp], [Citi_DSMT_Link].[Citi-DL:BaseID] \n"
            + "FROM [Citi_Iss] \n"
            + "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n"
            + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
            + "AND [Citi_DSMT_Link].[Citi-DSMT:IsOMU] IN ('Yes') \n"
            + "AND [Citi_DSMT_Link].[Citi-DSMT:OMUExec] = '<SOEID>' \n"
            + "AND [Citi_Iss].[Resource ID] = <Resource_ID>";

    public static final String DESCOPE_DSMT_LINK_BASE_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Citi-DSMT:OMUExec], [Citi_DSMT_Link].[Citi-DSMT:MSID], [Citi_DSMT_Link].[Citi-DSMT:MGID], [Citi_DSMT_Link].[Citi-DSMT:IsOMU], [Citi_DSMT_Link].[Citi-DL:Active], [Citi_DSMT_Link].[Citi-DL:Scp], [Citi_DSMT_Link].[Citi-DL:BaseID] \n"
            + "FROM [Citi_Iss] \n"
            + "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n"
            + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
            + "AND [Citi_DSMT_Link].[Citi-DSMT:IsOMU] IN ('Yes') \n"
            + "AND [Citi_DSMT_Link].[Citi-DSMT:OMUExec] <> '<SOEID>' \n"
            + "AND [Citi_Iss].[Resource ID] = <Resource_ID>";

    // Employee Search Query
    public static final String EMPLOYEE_SEARCH_QUERY = "SELECT CGH_SOE_ID, \n"
            + " FIRST_NAME,\n"
            + " LAST_NAME,\n"
            + " CGH_GOC,\n"
            + " EMAIL_ADDR\n"
            + " FROM TABLE(GDW_EMPLOYEE_PKG.GET_GDW_EMP_SEARCH_FUN(?,?,?))";

    // Employee Query
    public static final String EMPLOYEE_QUERY = "SELECT CGH_SOE_ID, \n"
            + " FIRST_NAME,\n"
            + " LAST_NAME,\n"
            + " CGH_GOC,\n"
            + " EMAIL_ADDR\n"
            + " FROM TABLE(GDW_EMPLOYEE_PKG.GET_GDW_EMP_SEARCH_FUN(?))";

    // HR DSMT GOC Search Query
    public static final String HR_DSMT_GOC_SEARCH_QUERY = "SELECT FIRST_NAME, \n"
            + " LAST_NAME,\n"
            + " SOE_ID,\n"
            + " EMAIL_ADDRESS,\n"
            + " GOC_ID,\n"
            + " GOC_NAME,\n"
            + " MANAGED_SEGMENT_ID,\n"
            + " MANAGE_SEGMENT_NAME,\n"
            + " MANAGED_SEGMENT_PATH,\n"
            + " MANAGED_GEOGRAPHY_ID,\n"
            + " MANAGE_GEOGRAPHY_NAME,\n"
            + " MANAGED_GEOGRAPHY_PATH,\n"
            + " LEGAL_ENTITY_ID,\n"
            + " LEGAL_ENTITY_NAME,\n"
            + " LEGAL_ENTITY_PATH,\n"
            + " GOC_STATUS,\n"
            + " USER_HR_MAP,\n"
            + " CREATION_DATE,\n"
            + " UPDATED_DATE\n"
            + " FROM TABLE(HR_DSMT_PKG.GET_HR_DSMT(?))";

}
