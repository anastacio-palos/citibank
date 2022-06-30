/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * � Copyright IBM Corporation 2018
 *
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.dao;

import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.util.DSMTDatasourceConfigReader;
import com.openpages.apps.common.util.AppEnv;
import com.openpages.aurora.common.KeyConstants;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.ui.constant.DSMTDBConfigurationConstants.DSMT_DATA_ACCEES_CONFIG_LOG_FILE_NAME;

import java.util.concurrent.Executors;

/**
 * <p>
 * The Data Access Configuration instantiates the Data Source Bean in the Spring context. The DataSource is constructed
 * by getting the Database URL, UserId and Password from the application environment.
 * </P>
 *
 * @author : Praveen Ravi <BR>
 * email : raviprav@us.ibm.com <BR>
 * company : IBM OpenPages
 * @version : OpenPages 7.4.0
 * @custom.date : 10-19-2016
 * @custom.feature : Base DAO Implementation
 * @custom.category :Data Access
 * @since : OpenPages 7.2.0
 */
@Configuration
@EnableTransactionManagement
public class DSMTDataAccessConfig {

    private Log logger;

    private ILoggerUtil loggerUtil;

    private DSMTDatasourceConfigReader dsmtDatasourceConfigReader;

    public DSMTDataAccessConfig(ILoggerUtil loggerUtil, DSMTDatasourceConfigReader dsmtDatasourceConfigReader) {

        this.loggerUtil = loggerUtil;
        logger = loggerUtil.getExtLogger(DSMT_DATA_ACCEES_CONFIG_LOG_FILE_NAME);
        this.dsmtDatasourceConfigReader = dsmtDatasourceConfigReader;
    }

    /**
     * <p>
     * A spring managed bean that constructs and returns a {@link DataSource} object.
     *
     * @return an instance of the java SQL data source to create database connections
     * @throws Exception the runtime Exception
     */
    @Bean(name = "dsmt-db")
    @Lazy(true)
    public DataSource dataSource() throws Exception {

        BasicDataSource dataSource = null;

        try {
            DBConfig dbConfig = dsmtDatasourceConfigReader.retrieveDBConfig();

            logger.info("DBConfig=" + dbConfig);

            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(AppEnv.getProperty(KeyConstants.DB_DRIVER));
            dataSource.setUrl(dbConfig.getUrl());
            dataSource.setUsername(dbConfig.getUserName());
            dataSource.setPassword(dbConfig.getPassword());

        }
        catch (Exception ioex) {

            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "dataSource()" + getStackTrace(ioex));
            throw ioex;
        }

        /* Return the data store. */
        logger.info("Datastore creation successful returning the datastore");
        return dataSource;
    }
}
