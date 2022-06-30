package com.ibm.openpages.ext.batch.dsmt;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.batch.dsmt.model.DSMTData;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.dao.DBConfig;
import com.ibm.openpages.ext.ui.util.DSMTDatasourceConfigReader;
import com.openpages.aurora.common.logging.ConsoleLogger;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import java.util.List;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;

public class DSMTUpdateProcessorTest {




//    public void init(){
//
//        this.dataSource  = dataSource();
//
//    }
//
//    public DataSource dataSource() {
//
//
//        BasicDataSource dataSource = null;
//
//        try {
//            Mockito.when(iLoggerUtil.getExtLogger(Mockito.any())).thenReturn(log);
//            Mockito.when(applicationUtil.getRegistrySetting(Mockito.any())).thenReturn("/Users/ashish.tiwari/Documents/Ashish/01Documents/Citi/Workspace/Citibank/OPWebApps/src/main/resources/dsmtdb.properties");
//
//            DSMTDatasourceConfigReader underTest = new DSMTDatasourceConfigReader(iLoggerUtil, applicationUtil, serviceFactory);
//
//            DBConfig dbConfig = underTest.retrieveDBConfig();
//
//            dataSource = new BasicDataSource();
//            dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//            dataSource.setUrl(dbConfig.getUrl());
//            dataSource.setUsername(dbConfig.getUserName());
//            dataSource.setPassword(dbConfig.getPassword());
//
//        }
//        catch (Exception ioex) {
//
//            log.error("EXCEPTION!!!!!!!!!!!!!!! " + "dataSource()" + getStackTrace(ioex));
//            return Mockito.mock(DataSource.class);
//        }
//
//        /* Return the data store. */
//        log.info("Datastore creation successful returning the datastore");
//        return dataSource;
//    }


}