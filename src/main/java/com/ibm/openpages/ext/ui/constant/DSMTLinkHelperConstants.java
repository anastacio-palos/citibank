package com.ibm.openpages.ext.ui.constant;

import com.ibm.openpages.ext.tss.service.constants.CommonConstants;


public class DSMTLinkHelperConstants extends CommonConstants {

    // Helper Base Setting to get Header Information and other Helper related information
    public static final String DSMT_LINK_APP_BASE_SETTING = "/OpenPages/Custom Deliverables/Helper App/DSMT Link App/";
    public static final String DSMT_LINK_APP_OBJECT_DISPLAY_INFO = "/Object Field Diplay Info";
    public static final String DSMT_LINK_APP_TITLE = "/Header Info/App Title";
    public static final String DSMT_LINK_APP_DISPLAY_NAME = "/Header Info/Display Name";
    public static final String DSMT_LINK_APP_CONTENT_HEADER = "/Content Header";
    public static final String ASSOCIATED_DSMT_LINK_QUERY = "/Associated DSMT Link Query";
    public static final String AVAILABLE_DSMT_LINK_QUERY = "/Available DSMT Link Query";
    public static final String DSMT_LINK_APP_PARENT_HIERARCHY = "/DSMT Link Parent Heirarchy";
    public static final String EXISTING_DSMT_LINK_FIELDS_TO_DISPLAY = "/Existing DSMT Link Fields To Display";
    public static final String AUDITABLE_ENTITY_FOLDER_PATH = "/AE Folder Path";
    public static final String MAX_AE_SEARCH_RESULT = "/Search/DSMT Search Max Records";
    public static final String TOO_MANY_RESULTS_WARNING = "Too many results returned for the search criteria entered," +
            " Refine the search to narrow down the results.";

    // Log File Name Constants
    public static final String CAP_DSMT_LINK_SERVICE_LOG_FILE_NAME = "cap_dsmt_link_Service.log";
    public static final String AMR_DSMT_LINK_SERVICE_LOG_FILE_NAME = "amr_dsmt_link_Service.log";
    public static final String ISSUE_DSMT_LINK_SERVICE_LOG_FILE_NAME = "issue_dsmt_link_Service.log";
    public static final String BMQS_ISSUE_DSMT_LINK_SERVICE_LOG_FILE_NAME = "bmqs_issue_dsmt_link_Service.log";
    public static final String AUDIT_DSMT_LINK_SERVICE_LOG_FILE_NAME = "audit_dsmt_link_Service.log";
    public static final String DSMT_LINK_CONTROLLER_LOG_FILE_NAME = "dsmt_link_helper_controller.log";
    public static final String DSMT_LINK_AUDITABLE_ENTITY_CONTROLLER_LOG_FILE_NAME = "dsmt_link_auditable_entity_helper_controller.log";
    public static final String DSMT_LINK_CITI_ISSUE_CONTROLLER_LOG_FILE_NAME = "dsmt_link_issue_helper_controller.log";
    public static final String DSMT_LINK_CITI_AMR_CONTROLLER_LOG_FILE_NAME = "dsmt_link_amr_helper_controller.log";
    public static final String DSMT_LINK_HELPER_UTIL_LOG_FILE_NAME = "dsmt_link_helper_util.log";
    public static final String CONTROL_DSMT_LINK_SERVICE_LOG_FILE_NAME = "control_dsmt_link_Service.log";
    public static final String DSMT_LINK_SERVICE_UTIL_LOG_FILE_NAME = "dsmt_link_service_util.log";
    public static final String AUDITABLE_ENTITY_DSMT_LINK_SERVICE_LOG_FILE_NAME = "auditable_entity_dsmt_link_Service.log";

    public static final String OBJECT_ID = "resourceId";
    public static final String DSMT_LINK_APP_INFORMATION = "DSMT_Helper_Link_Info";
    public static final String DSMT_DISS_IDS = "dsmtDissIds";
    public static final String AE_DISS_IDS = "aeDissIds";
    public static final String DSMT_ASSO_IDS = "dsmtAssoMap";
    public static final String DSMT_SEARCH = "dsmtSearch";
    public static final String AE_SEARCH = "aeSearch";

    // Object Types supported by Helper App
    public static final String ISSUE = "Citi_Iss";
    public static final String AUDIT = "AuditProgram";
    public static final String CONTROL = "SOXControl";
    public static final String CORRECTIVE_ACITON_PLAN = "Citi_CAP";
    public static final String AUDIT_MAPPING_REPORT = "Citi_AudMpReport";
    public static final String AUDITABLE_ENTITY = "AuditableEntity";
    public static final String AUDITABLE_ENTITY_APP = "Citi_AEApp";
    public static final String DSMT_LINK = "Citi_DSMT_Link";
    
    // Object Type ( Citi_AudMpReport )
    public static final String TYPE_IS_AUDIT= "AuditPhase";
    public static final String TYPE_IS_BMQS= "Citi_AudEntityClusEval";
    public static final String AMR_AUDIT_REPORT_TYPE_FIELD_INFO = "Citi-AMR:Audit Report Type";
    public static final String AMR_AUDIT_REPORT_TYPE_IS_BMIER = "BM IER";
    public static final String AMR_AUDIT_REPORT_TYPE_IS_CAIER = "CA IER";
    public static final String NON_IER_VALUES = "Risk Based,CA Quarterly Report,Other-Non-CA";

    //Field Name
    public static final String DSMT_SCP_FLD = "Citi-DL:Scp";
    public static final String DSMT_STATUS_FLD = "Citi-DL:Status";
    public static final String AUDIT_STATUS_FLD = "OPSS-Aud:Status";
    public static final String AE_APP_SCP_FLD = "Citi-AEApp:Scp";
    public static final String AE_APP_STATUS_FLD = "Citi-AEApp:Status";
    public static final String AE_ID_FLD = "Citi-DL:AEID";
    public static final String BASE_ID_FLD = "Citi-DL:BaseID";
    public static final String INVALID_DSMT_EXISTS_FIELD = "Citi-Flg:DataSegFlag";

    //Field Value
    public static final String IN_STR = "In";
    public static final String OUT_STR = "Out";
    public static final String DRAFT_STR = "Draft";
    public static final String YES_STR = "Yes";
    public static final String PENDING_REM_STR = "Pending Removal";
    public static final String ACTIVE = "active";
    public static final String VALID = "valid";
    
    //
    public static final String DSMT_LINK_OBJECT_TYPE = "Citi_DSMT_Link";
    
    //
    public static final String DSMT_STATUS_IS_NOT_NULL_MESSAGE = "DSMT status is not null or empty." ;

    public static final String HELPER_ACCESS_MESSAGE = "You are not an Authorized user to run this helper.";
    //
    public static final String APPLICATION_TASK_VIEW_URL_DETAIL_PAGE = "/OpenPages/Custom Deliverables/Object URL Generator/Task View Page";
    public static final String MODAL_NEW_WINDOW_INFO = "target=\"_blank\" rel=\"noreferrer\"";
    
    // DSMT Link Delete Super User Registry setting 
    public static final String DSMT_LINK_DELETE_SUPER_USER_SETTING = "/OpenPages/Custom Deliverables/Helper App/DSMT Link App/DSMT Super User Info/Helper Admin Properties File Name";
    

}
