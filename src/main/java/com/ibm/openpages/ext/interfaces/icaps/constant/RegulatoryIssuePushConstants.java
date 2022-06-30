package com.ibm.openpages.ext.interfaces.icaps.constant;

import com.ibm.openpages.api.logging.LoggerFactory;
import com.ibm.openpages.ext.interfaces.common.constant.RegistryConstants;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RegulatoryIssuePushConstants extends RegistryConstants {
    public static final String METADATA_PATH = "/OpenPages/Custom Deliverables/Interfaces/iCAPS/outbound/Regulatory Issues";
    public static final String ENABLE_DEBUG = METADATA_PATH + "/logs/enable_debug_mode";
    public static final String LOG_FILE_PATH = METADATA_PATH + "/logs/log_file_path";
    public static final String LOG_FILE_SIZE = METADATA_PATH + "/logs/log_file_size";
    public static final String LOG_LEVEL = METADATA_PATH + "/logs/log_level";
    public static final String REMOTE_SERVICE_ENDPOINT = METADATA_PATH + "/Remote Service Endpoint";
    public static final String REQUEST_HEADERS = METADATA_PATH + "/Request Headers";
    public static final String TIMEOUT = METADATA_PATH + "/Service Timeout";
    public static final String REQUEST_ID_HEADER = METADATA_PATH + "/Request Id Header";
    public static final String PUSH_FIELDS_MAP = METADATA_PATH + "/push_fields_map";
    public static final String REQUEST_MEDIA_TYPE = METADATA_PATH + "/Media Type";
    public static final String METADATA_REG_ISSUE_JSON_FILE = METADATA_PATH + "/metadata_config.json";
    public static final String METADATA_REG_ISSUE_HEADING_JSON_FILE = METADATA_PATH + "/metadata_heading_config.json";
    public static final String METADATA_CAP_JSON_FILE = METADATA_PATH + "/metadata_cap_config.json";
    public static final String METADATA_REG_ISSUE_AIMS_DETAILS = METADATA_PATH + "/metadata_aims-details_config.json";
    public static final String METADATA_REG_ISSUE_CAP = METADATA_PATH + "/metadata_cap_config.json";
    public static final String METADATA_REG_ISSUE_COMMON = METADATA_PATH + "/metadata_common-validations_config.json";
    public static final String METADATA_REG_ISSUE_REQUEST = METADATA_PATH + "/metadata_request_config.json";
    public static final String METADATA_REG_ISSUE_RET = METADATA_PATH + "/metadata_ret-issue_config.json";
    public static final String IS_UPDATE_CAPS_ACTIVE_REGISTRY_SETTING = METADATA_PATH + "/Update CAPS";
    private static final Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger(RegulatoryIssuePushConstants.class);

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
        if (registryConstants == null) registryConstants = new ArrayList<>();
        registryConstants.add(METADATA_REG_ISSUE_JSON_FILE);
        registryConstants.add(METADATA_REG_ISSUE_HEADING_JSON_FILE);
        registryConstants.add(METADATA_CAP_JSON_FILE);
        logger.debug("registryConstants=" + registryConstants);
        logger.debug("***** IssueRegistryConstants - init END *****");
    }
}
