package com.ibm.openpages.ext.batch.dsmt;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.dao.DBConfig;
import com.ibm.openpages.ext.ui.util.DSMTDatasourceConfigReader;
import com.openpages.apps.common.util.AppEnv;
import com.openpages.aurora.common.KeyConstants;
import com.openpages.aurora.common.logging.ConsoleLogger;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static org.testng.Assert.*;

public class DSMTInterfaceJobTest {

    Log log = new ConsoleLogger("test");
    IGRCObjectUtil grcObjectUtil = Mockito.mock(IGRCObjectUtil.class);
    ILoggerUtil iLoggerUtil = Mockito.mock(ILoggerUtil.class);

    IApplicationUtil applicationUtil = Mockito.mock(IApplicationUtil.class);
    IServiceFactory sf = Mockito.mock(IServiceFactory.class);

    Context mockCtx = Mockito.mock(Context.class);

    DSMTInterfaceJob underTest = null;

    public void init() throws Exception{

        underTest = new DSMTInterfaceJob(-11111);

        Context ctx = underTest.getApplicationContext();

        ctx = mockCtx;


        Mockito.when(this.mockCtx.get(ILoggerUtil.class.getName())).thenReturn(iLoggerUtil);

        DataSource ds = dataSource();
        Mockito.when(this.mockCtx.get(DataSource.class.getName())).thenReturn(ds);
        Mockito.when(iLoggerUtil.getExtLogger(Mockito.any())).thenReturn(log);

        underTest.init(mockCtx);

    }


    public DataSource dataSource() {


        BasicDataSource dataSource = null;

        try {
            Mockito.when(iLoggerUtil.getExtLogger(Mockito.any())).thenReturn(log);
            Mockito.when(applicationUtil.getRegistrySetting(Mockito.any())).thenReturn("/Users/ashish.tiwari/Documents/Ashish/01Documents/Citi/Workspace/Citibank/OPWebApps/src/main/resources/dsmtdb.properties");

            DSMTDatasourceConfigReader underTest = new DSMTDatasourceConfigReader(iLoggerUtil, applicationUtil, sf);

            DBConfig dbConfig = underTest.retrieveDBConfig();

            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
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
    public void testInit() throws Exception{

        init();

        underTest.execute();

    }

}