package com.ibm.openpages.ext.interfaces.icaps.constant;

import com.ibm.openpages.api.logging.LoggerFactory;
import com.ibm.openpages.ext.interfaces.common.constant.RegistryConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IssueRegistryConstants extends RegistryConstants {
    //Interface path
    public static final String INTERFACES_ICAPS_PATH = ENGINE_METADATA_INTERFACES_PATH + "/iCAPS/inbound/";
    //virtual setting
    public static final String ENGINE_ISSUE_METADATA_JSON_FILE = INTERFACES_ICAPS_PATH + "meta-validations-engine_Issue.json";
    //Registry settings
    public static final String ENGINE_ISSUE_METADATA_JSON_FILE_1 = INTERFACES_ICAPS_PATH + "meta-validations-engine_1.json";
    public static final String ENGINE_ISSUE_METADATA_JSON_FILE_2 = INTERFACES_ICAPS_PATH + "meta-validations-engine_2.json";
    public static final String ENGINE_ISSUE_METADATA_JSON_FILE_3 = INTERFACES_ICAPS_PATH + "meta-validations-engine_3.json";
    public static final String ENGINE_CAP_METADATA_JSON_FILE = INTERFACES_ICAPS_PATH + "meta-validations-engine_CAP.json";

    public static final String GET_ISSUE_QUERY = INTERFACES_ICAPS_PATH + "issue_query";
    public static final String GET_CAP_QUERY = INTERFACES_ICAPS_PATH + "cap_query";
    public static final String GET_ISSUE_TYPE = INTERFACES_ICAPS_PATH + "issue_type";
    public static final String GET_CAP_TYPE = INTERFACES_ICAPS_PATH + "cap_type";
    public static final String DSMT_MAP = INTERFACES_ICAPS_PATH + "dsmt_map";
    public static final String CAP_STATUS_MAP = INTERFACES_ICAPS_PATH + "cap_status_map";
    public static final String ISSUE_STATUS_MAP = INTERFACES_ICAPS_PATH + "issue_status_map";
    public static final String DSMT_NOT_FOUND = INTERFACES_ICAPS_PATH + "dsmt_not_found";
    public static final String PUSH_FIELDS_MAP = INTERFACES_ICAPS_PATH + "push_fields_map";
    //Log Registry Settings
    public static final String LOG_FILE_PATH_ICAPS = INTERFACES_ICAPS_PATH + "logs/log_file_path_icaps";
    public static final String LOG_FILE_SIZE = INTERFACES_ICAPS_PATH + "logs/log_file_size";
    public static final String ENABLE_DEBUG = INTERFACES_ICAPS_PATH + "logs/enable_debug_mode";
    public static final String LOG_LEVEL = INTERFACES_ICAPS_PATH + "logs/log_level";
    public static final String PLANNING_CAP_MAP = INTERFACES_ICAPS_PATH + "planning_map";

    private static final Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger(IssueRegistryConstants.class);

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
        registryConstants.add(GET_ISSUE_QUERY);
        //registryConstants.add(GET_ISSUE_TYPE);
        registryConstants.add(DSMT_MAP);
        registryConstants.add(CAP_STATUS_MAP);

        logger.debug("registryConstants=" + registryConstants);
        logger.debug("***** IssueRegistryConstants - init END *****");
    }
}
