package com.ibm.openpages.ext.interfaces.query.constant;

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
public class QueryRegistryConstants extends RegistryConstants {
    public static final String ENGINE_METADATA_INTERFACES_QUERY_PATH = ENGINE_METADATA_INTERFACES_PATH + "/query/";

    public static final String ENABLE_DEBUG = ENGINE_METADATA_INTERFACES_QUERY_PATH + "enable_debug_mode";
    public static final String LOG_FILE_PATH = ENGINE_METADATA_INTERFACES_QUERY_PATH + "log_file_path";
    public static final String LOG_FILE_SIZE = ENGINE_METADATA_INTERFACES_QUERY_PATH + "log_file_size";
    public static final String LOG_LEVEL = ENGINE_METADATA_INTERFACES_QUERY_PATH + "log_level";

    private static final Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger(QueryRegistryConstants.class);

    public void init() {
        logger.debug("***** QueryRegistryConstants - init START *****");
        if(registryConstants == null)
            registryConstants = new ArrayList<>();
        logger.debug("registryConstants=" + registryConstants);
        logger.debug("***** QueryRegistryConstants - init END *****");
    }

    public List<String> getRegistryConstants() {
        if (registryConstants == null) {
            init();
        }
        return registryConstants;
    }
}
