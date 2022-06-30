package com.ibm.openpages.ext.ui.util;

import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.util.CommonUtil;
import com.ibm.openpages.ext.ui.dao.DBConfig;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.ibm.openpages.ext.ui.constant.DSMTDBConfigurationConstants.DSMT_DB_DATABASE_FILE_PATH;
import static com.ibm.openpages.ext.ui.constant.DSMTDBConfigurationConstants.DSMT_DB_JDBC_CONNECTION_STRING;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_HELPER_UTIL_LOG_FILE_NAME;

@Component
public class DSMTDatasourceConfigReader {

    private Log log;
    private IApplicationUtil applicationUtil;
    private IServiceFactory serviceFactory;

    public DSMTDatasourceConfigReader(ILoggerUtil loggerUtil, IApplicationUtil applicationUtil,
            IServiceFactory serviceFactory) {

        this.log = loggerUtil.getExtLogger(DSMT_LINK_HELPER_UTIL_LOG_FILE_NAME);
        this.applicationUtil = applicationUtil;
        this.serviceFactory = serviceFactory;

    }

    public DBConfig retrieveDBConfig() {

        DBConfig dbConfig = null;
        try {
            String dbFilePath = retrieveDBFilePath();

            log.info("DBFilePath=" + dbFilePath);

            return retrieveConfigFromFile(dbFilePath);
        }
        catch (Exception ex) {

            log.error("Error reading connection", ex);
            throw new RuntimeException("Error reading db connection params", ex);
        }

    }

    private String retrieveDBFilePath() throws Exception {

        if (this.applicationUtil != null) {
            return applicationUtil.getRegistrySetting(DSMT_DB_DATABASE_FILE_PATH);
        }
        else {
            return getRegistrySetting(DSMT_DB_DATABASE_FILE_PATH);

        }

    }

    public String getRegistrySetting(String settingsPath) throws Exception {

        IConfigurationService configrServ = this.serviceFactory.createConfigurationService();
        String registryValue = configrServ.getConfigProperties().getProperty(settingsPath);
        return registryValue;
    }

    private DBConfig retrieveConfigFromFile(String fileName) {

        log.info("DBConfig file name = " + fileName);

        DBConfig dbConfig = null;

        try {
            dbConfig = new DBConfig();

            String path = fileName.substring(0, fileName.lastIndexOf("/"));
            String propFile = Optional.ofNullable(fileName.substring(fileName.lastIndexOf("/") + 1))
                    .filter(v -> v.contains(".properties")).map(v -> v.replace(".properties", ""))
                    .orElseGet(() -> fileName.substring(fileName.lastIndexOf("/") + 1));

            log.info(String.format("FileName=%s, FilePath=%s", path, propFile));

            File file = new File(path);
            URL[] urls = { file.toURI().toURL() };
            ClassLoader loader = new URLClassLoader(urls);
            ResourceBundle rb = ResourceBundle.getBundle(propFile, Locale.getDefault(), loader);

            log.info("ResourceBundle=" + rb);

            dbConfig.setUserName(rb.getString("USERNAME"));
            dbConfig.setPort(Integer.valueOf(rb.getString("PORT")));
            dbConfig.setName(rb.getString("DBNAME"));
            dbConfig.setHost(rb.getString("HOST"));
            dbConfig.setPassword(CommonUtil.decryptString(rb.getString("PASSWORD")));
            dbConfig.setConnString(DSMT_DB_JDBC_CONNECTION_STRING);

            log.info("DB_CONFIG_FILE = " + dbConfig);
            return dbConfig;

        }
        catch (Exception ex) {
            log.error("Error reading connection params from registry", ex);
            throw new RuntimeException("Error reading db connection params", ex);
        }

    }
}
