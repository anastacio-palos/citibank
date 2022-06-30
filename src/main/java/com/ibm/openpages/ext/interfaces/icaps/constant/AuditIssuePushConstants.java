package com.ibm.openpages.ext.interfaces.icaps.constant;

import com.ibm.openpages.api.logging.LoggerFactory;
import com.ibm.openpages.ext.interfaces.common.constant.RegistryConstants;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AuditIssuePushConstants extends RegistryConstants {


    private static final Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger(AuditIssuePushConstants.class);

    public static final String METADATA_PATH = "/OpenPages/Custom Deliverables/Interfaces/iCAPS/outbound/Audit Issues";
    public static final String ENABLE_DEBUG = METADATA_PATH + "/logs/enable_debug_mode";
    public static final String LOG_FILE_PATH = METADATA_PATH + "/logs/log_file_path";
    public static final String LOG_FILE_SIZE = METADATA_PATH + "/logs/log_file_size";
    public static final String LOG_LEVEL = METADATA_PATH + "/logs/log_level";
    public static final String REMOTE_SERVICE_ENDPOINT = METADATA_PATH + "/Remote Service Endpoint";
    public static final String REQUEST_HEADERS = METADATA_PATH + "/Request Headers";
    public static final String REQUEST_MEDIA_TYPE = METADATA_PATH + "/Media Type";
    public static final String TIMEOUT = METADATA_PATH + "/Service Timeout";
    public static final String REQUEST_ID_HEADER = METADATA_PATH + "/Request Id Header";

    public static final String METADATA_AUDIT_ISSUE_JSON_FILE = METADATA_PATH + "/metadata_issue.json";
    public static final String METADATA_AUDIT_ISSUE_FIRST_JSON_FILE = METADATA_PATH + "/metadata_issue_config_1.json";
    public static final String METADATA_AUDIT_ISSUE_SECOND_JSON_FILE = METADATA_PATH + "/metadata_issue_config_2.json";
    public static final String METADATA_AUDIT_CAP_JSON_FILE = METADATA_PATH + "/metadata_cap_config.json";
    public static final String METADATA_AUDIT_REQUEST_JSON_FILE = METADATA_PATH + "/metadata_request_config.json";


    public static final String IS_UPDATE_CAPS_ACTIVE_REGISTRY_SETTING = METADATA_PATH + "/Update CAPS";

    public static final String METADATA_AUDIT_MAP_STATUS_ISSUE = METADATA_PATH + "/map_status_issue";
    public static final String METADATA_AUDIT_MAP_STATUS_DETAILED_ISSUE = METADATA_PATH + "/map_status_detailed_issue";
    public static final String METADATA_AUDIT_MAP_STATUS_CAP = METADATA_PATH + "/map_status_cap";
    public static final String METADATA_AUDIT_MAP_STATUS_DETAILED_CAP = METADATA_PATH + "/map_status_detailed_cap";
    public static final String METADATA_AUDIT_MAP_IA_VALIDATION_DAYS_ISSUE = METADATA_PATH + "/map_ia_validation_days_issue";

    public static final String TRIGGER_IGNORED_STATUS_LIST = METADATA_PATH + "/trigger/Ignored Status";
    public static final String TRIGGER_IGNORED_CAP_STATUS_LIST = METADATA_PATH + "/trigger/Ignored Status CAP";
    public static final String CAP_CHANGE_COUNTER_LIMIT = METADATA_PATH + "/trigger/CAP Change Counter Limit";


    public static final String DSMT_ATTRIBUTE = "dsmtAttribute";
    public static final String IMPACTED_DSMT_ATTRIBUTES = "impactedDsmtAttributes";
    public static final String ISSUE_IDENTIFICATION_DATE = "issueIdentificationDate";
    public static final String ISSUE_REPORT_NUMBER = "reportNumber";
    public static final String ISSUE_ACTIVATION_DATE = "issueActivationDate";
    public static final String ISSUE_REQUEST = "issueRequest";

    @Override
    public List<String> getRegistryConstants() {
        if (registryConstants == null) {
            init();
        }
        return registryConstants;
    }

    @Override
    public void init() {
        logger.debug("***** IssueRegistryConstants - init START *****");
        if (registryConstants == null)
            registryConstants = new ArrayList<>();
        registryConstants.add(METADATA_AUDIT_CAP_JSON_FILE);
        registryConstants.add(METADATA_AUDIT_REQUEST_JSON_FILE);

        logger.debug("registryConstants=" + registryConstants);
        logger.debug("***** IssueRegistryConstants - init END *****");
    }
}
