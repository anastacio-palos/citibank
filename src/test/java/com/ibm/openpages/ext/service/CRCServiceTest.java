package com.ibm.openpages.ext.service;

import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.openpages.aurora.common.logging.ConsoleLogger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class CRCServiceTest {

    private Log log = new ConsoleLogger("test");
    private ILoggerUtil iLoggerUtil = Mockito.mock(ILoggerUtil.class);
    private RestTemplate mockTemplate = Mockito.mock(RestTemplate.class);


    @BeforeClass
    public void init(){


        Mockito.when(iLoggerUtil.getExtLogger(Mockito.any())).thenReturn(log);
    }


    @Test
    public void testCRCOMUService() throws Exception{

        String omuURL = "https://dev.dsmt-ref-service.apps.namoseswd20d.ecs.dyn.nsroot" +
                        ".net/dsmt-ref-service/swagger-ui.html#!/omu45service45controller/getOMUDetailsBySOEIDUsingGET/omu/<date of the last successfull run>";

        String response = IOUtils.toString(this.getClass().getResourceAsStream("/OMU_Response.json"));

        log.info("mock response="+ response);

        ResponseEntity mockEntity = Mockito.mock(ResponseEntity.class);
        Mockito.when(mockEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        Mockito.when(mockEntity.getBody()).thenReturn(response);

        Mockito.when(mockTemplate.getForEntity(Mockito.any(String.class), Mockito.any())).thenReturn(mockEntity);

        CRCService underTest = new CRCService(mockTemplate, iLoggerUtil);
        Object serviceResponse = underTest.invoke(omuURL, null);

        log.info("Response=" + serviceResponse);

        assert serviceResponse != null;
        assert serviceResponse.equals(response);

    }

    @Test
    public void testCRCInitialisation() throws Exception{

        String omuURL = "https://dev.dsmt-ref-service.apps.namoseswd20d.ecs.dyn.nsroot.net/dsmt-ref-service/omu/soeId?soeId=<soeid>";
        String response = IOUtils.toString(this.getClass().getResourceAsStream("/OMU_Response_SOEID.json"));

        log.info("mock response="+ response);

        ResponseEntity mockEntity = Mockito.mock(ResponseEntity.class);
        Mockito.when(mockEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        Mockito.when(mockEntity.getBody()).thenReturn(response);

        Mockito.when(mockTemplate.getForEntity(Mockito.any(String.class), Mockito.any())).thenReturn(mockEntity);

        CRCService underTest = new CRCService(mockTemplate, iLoggerUtil);
        Object serviceResponse = underTest.invoke(omuURL, null);

        log.info("Response=" + serviceResponse);

        assert serviceResponse != null;
        assert serviceResponse.equals(response);

    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testCRCException() throws Exception{

        String omuURL = "https://dev.dsmt-ref-service.apps.namoseswd20d.ecs.dyn.nsroot.net/dsmt-ref-service/omu/soeId?soeId=<soeid>";

        ResponseEntity mockEntity = Mockito.mock(ResponseEntity.class);
        Mockito.when(mockEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        Mockito.when(mockEntity.getBody()).thenReturn("Bad Request");

        Mockito.when(mockTemplate.getForEntity(Mockito.any(String.class), Mockito.any())).thenReturn(mockEntity);
        CRCService underTest = new CRCService(mockTemplate, iLoggerUtil);
        Object serviceResponse = underTest.invoke(omuURL, null);


    }

}