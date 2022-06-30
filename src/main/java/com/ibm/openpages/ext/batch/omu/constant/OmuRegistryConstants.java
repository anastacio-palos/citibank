package com.ibm.openpages.ext.batch.omu.constant;


public class OmuRegistryConstants {
    public static final String INTERFACES_PATH = "/OpenPages/Custom Deliverables/Interfaces";
    public static final String OMU_INTERFACE_LOGGER = "omu_interface.log";
    public static final String OMU_INTERFACES_QUERY_PATH_DAO = INTERFACES_PATH + "/EmployeeBatch/DAO/";
    public static final String OMU_INTERFACES_QUERY_PATH = INTERFACES_PATH + "/EmployeeBatch/";

    public static final String URI = OMU_INTERFACES_QUERY_PATH + "uri";
    public static final String URI_RESPONSE_SOE_ID = OMU_INTERFACES_QUERY_PATH + "uri_response_soeid";
    public static final String FAIL_COUNT = OMU_INTERFACES_QUERY_PATH + "fail_count";
    public static final String OP_QUERY_TO_RETRIEVE_ISSUES = OMU_INTERFACES_QUERY_PATH + "OP Query to Retrieve Issues";
    public static final String OP_QUERY_TO_RETRIEVE_OMUS = OMU_INTERFACES_QUERY_PATH + "OP Query to Retrieve OMUs";
    public static final String OP_QUERY_TO_RETRIEVE_CAPS = OMU_INTERFACES_QUERY_PATH + "OP Query to Retrieve CAPs";
    public static final String LIST_ISSUE_STATUS = OMU_INTERFACES_QUERY_PATH + "issue_status";
    public static final String LIST_CAP_STATUS = OMU_INTERFACES_QUERY_PATH + "cap_status";
    public static final String SQL_SELECT_BY_SOE_ID = OMU_INTERFACES_QUERY_PATH_DAO + "sql_select_by_soe_id";
    public static final String SQL_SELECT_DELTA_OLD_NEW_STATUS = OMU_INTERFACES_QUERY_PATH_DAO + "sql_select_delta_old_new_status";
    public static final String SQL_UPDATE_DELTA_BY_SOE_ID = OMU_INTERFACES_QUERY_PATH_DAO + "sql_update_delta_by_soe_id";
    public static final String SQL_UPDATE_OP_EMPLOYEE_ERROR = OMU_INTERFACES_QUERY_PATH_DAO + "sql_update_op_employee_error";
    public static final String SQL_SELECT_GDW_EMPLOYEE_BYE_SOE_ID = OMU_INTERFACES_QUERY_PATH_DAO + "sql_select_gdw_employee_bye_soe_id";
    public static final String SQL_SELECT_VALUE_OMU_CONFIG = OMU_INTERFACES_QUERY_PATH_DAO + "sql_last_run_date";
    public static final String SQL_UPDATE_VALUE_OMU_CONFIG = OMU_INTERFACES_QUERY_PATH_DAO + "query_update_last_run_date";


}
