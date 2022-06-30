package com.ibm.openpages.ext.dsmt;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.batch.dsmt.DSMTInterfaceJob;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.dao.DBConfig;
import com.ibm.openpages.ext.ui.util.DSMTDatasourceConfigReader;
import com.openpages.aurora.common.logging.ConsoleLogger;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import com.ibm.openpages.ext.model.DSMTTripletModel;

import javax.sql.DataSource;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static org.testng.Assert.*;


public class DSMTTripletDataProcessorTest {

    Log log = new ConsoleLogger("test");
    IGRCObjectUtil grcObjectUtil = Mockito.mock(IGRCObjectUtil.class);
    ILoggerUtil iLoggerUtil = Mockito.mock(ILoggerUtil.class);

    IApplicationUtil applicationUtil = Mockito.mock(IApplicationUtil.class);

    Context mockCtx = Mockito.mock(Context.class);

    DataSource dataSource;

    DSMTInterfaceJob underTest = null;

    private IServiceFactory serviceFactory = Mockito.mock(IServiceFactory.class);

    public DataSource dataSource() {


        BasicDataSource dataSource = null;

        try {
            Mockito.when(iLoggerUtil.getExtLogger(Mockito.any())).thenReturn(log);
            Mockito.when(applicationUtil.getRegistrySetting(Mockito.any())).thenReturn("/Users/ashish.tiwari/Documents/Ashish/01Documents/Citi/Workspace/Citibank/OPWebApps/src/main/resources/dsmtdb.properties");

            DSMTDatasourceConfigReader underTest = new DSMTDatasourceConfigReader(iLoggerUtil, applicationUtil, serviceFactory);

            DBConfig dbConfig = underTest.retrieveDBConfig();

            dataSource = new BasicDataSource();
//            dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
            dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
            dataSource.setUrl(dbConfig.getUrl());
            dataSource.setUsername(dbConfig.getUserName());
            dataSource.setPassword(dbConfig.getPassword());

        }
        catch (Exception ioex) {

            log.error("EXCEPTION!!!!!!!!!!!!!!! " + "dataSource()" + getStackTrace(ioex));
            return Mockito.mock(DataSource.class);
        }

        /* Return the data store. */
        log.info("Datastore creation successful returning the datastore");
        return dataSource;
    }

    @Test
    public void testCreateTriplet() {

        DSMTTripletDataProcessor underTest = new DSMTTripletDataProcessor( dataSource(), iLoggerUtil);

        DSMTTripletModel testData = new DSMTTripletModel();

        testData.setTripletID("44077");
        testData.setMgid("1001");
        testData.setLvid("08530");
        testData.setMsid("1276");
        testData.setStatus("I");

        underTest.createTriplet(testData);

    }
}