package com.ibm.openpages.ext.ui.util;

import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.dao.DBConfig;
import com.openpages.aurora.common.logging.ConsoleLogger;
import org.apache.commons.logging.Log;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DSMTDatasourceConfigReaderTest {

    Log log = new ConsoleLogger("test");
    IGRCObjectUtil grcObjectUtil = Mockito.mock(IGRCObjectUtil.class);
    ILoggerUtil iLoggerUtil = Mockito.mock(ILoggerUtil.class);

    IApplicationUtil applicationUtil = Mockito.mock(IApplicationUtil.class);

    private IServiceFactory serviceFactory = Mockito.mock(IServiceFactory.class);

    @Test
    public void testRetrieveDBConfig() throws Exception{
        Mockito.when(iLoggerUtil.getExtLogger(Mockito.any())).thenReturn(log);
        Mockito.when(applicationUtil.getRegistrySetting(Mockito.any())).thenReturn("/Users/ashish.tiwari/Documents/Ashish/01Documents/Citi/Workspace/Citibank/OPWebApps/src/main/resources/dsmtdb.properties");

        DSMTDatasourceConfigReader underTest = new DSMTDatasourceConfigReader(iLoggerUtil, applicationUtil, serviceFactory);

        DBConfig response = underTest.retrieveDBConfig();

        assert response != null;

        log.info("response=" + response.getPassword());


    }
}