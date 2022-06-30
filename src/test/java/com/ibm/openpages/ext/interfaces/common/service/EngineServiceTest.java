package com.ibm.openpages.ext.interfaces.common.service;

import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.resource.IDateField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IStringField;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import com.ibm.openpages.ext.interfaces.common.service.impl.EngineServiceImpl;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueRegistryConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.RegulatoryIssuePushConstants;
import com.ibm.openpages.ext.interfaces.icaps.util.IssueEngineRegistry;
import com.ibm.openpages.ext.interfaces.icaps.util.PushIssueRegistry;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Map;

public class EngineServiceTest {
    String testData;
    String issueMetadata;
    String capMetadata;
    String regIssueMetadata;
    String regIssueHeadingMetadata;
    //String capMetadataConfig;


    private static Logger logger = null;

    @BeforeClass
    public static void setLogger() throws MalformedURLException
    {
        logger = LogManager.getLogger();
    }

    @BeforeClass
    public void validateAndProcessPayloadTestInit() throws IOException {
        testData = IOUtils.toString(this.getClass().getResourceAsStream("/iCAPS_Request_full.json"));
        issueMetadata = IOUtils.toString(this.getClass().getResourceAsStream("/iCAPS_Issue_Configuration_full.json"));
        capMetadata = IOUtils.toString(this.getClass().getResourceAsStream("/iCAPS_Cap_Configuration_full.json"));
        regIssueMetadata = IOUtils.toString(this.getClass().getResourceAsStream("/Push_RegIssue_Configuration.json"));
        regIssueHeadingMetadata = IOUtils.toString(this.getClass().getResourceAsStream("/Push_RegIssue_Heading_Configuration.json"));
        //capMetadataConfig = IOUtils.toString(this.getClass().getResourceAsStream("/cap_configuration.json"));

    }

    @Test
    public void extractAndGeneratePayloadTest(){
        logger.debug("Start Testing");

        RegulatoryIssuePushConstants pushConstants = new RegulatoryIssuePushConstants();

        IServiceFactory apiFactory = Mockito.mock(IServiceFactory.class);
        IConfigurationService configService = Mockito.mock(IConfigurationService.class);
        IConfigProperties configProps = Mockito.mock(IConfigProperties.class);
        //Mockito.when(configProps.getProperty(IssueRegistryConstants.ENGINE_METADATA_ISSUE_JSON_FILE)).thenReturn(pushMetadata);
        //Mockito.when(configProps.getProperty(IssueRegistryConstants.ENGINE_METADATA_CAP_JSON_FILE)).thenReturn(capMetadata);
        Mockito.when(configService.getConfigProperties()).thenReturn(configProps);
        Mockito.when(apiFactory.createConfigurationService()).thenReturn(configService);
        PushIssueRegistry pushIssueRegistry = new PushIssueRegistry();
        Mockito.when(configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.METADATA_REG_ISSUE_JSON_FILE)).thenReturn(regIssueMetadata);
        Mockito.when(configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.METADATA_REG_ISSUE_HEADING_JSON_FILE)).thenReturn(regIssueHeadingMetadata);
        //Mockito.when(configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.METADATA_REG_ISSUE_HEADING_JSON_FILE)).thenReturn(capMetadataConfig);

        pushIssueRegistry.init(apiFactory, pushConstants.getRegistryConstants(), logger);
        String[] metadataProperties = {
                RegulatoryIssuePushConstants.METADATA_REG_ISSUE_JSON_FILE,
                RegulatoryIssuePushConstants.METADATA_REG_ISSUE_HEADING_JSON_FILE,
                RegulatoryIssuePushConstants.METADATA_CAP_JSON_FILE
        };
        EngineService underTest = new EngineServiceImpl();
        IGRCObject igrcObject = Mockito.mock(IGRCObject.class);
        Mockito.when(igrcObject.getName()).thenReturn("Issue Name");
        Mockito.when(igrcObject.getDescription()).thenReturn("Issue Description");
        IStringField stringField = Mockito.mock(IStringField.class);
        Mockito.when(stringField.getValue()).thenReturn("Field Text");
        //Mockito.when(igrcObject.getField(Mockito.anyString())).thenReturn(stringField);

        IDateField dateField = Mockito.mock(IDateField.class);
        Mockito.when(dateField.getValue()).thenReturn(Calendar.getInstance().getTime());
        Mockito.when(igrcObject.getField(Mockito.anyString())).thenReturn(dateField);

        Map<String, Object> json = underTest.extractAndGeneratePayload(igrcObject, pushIssueRegistry,
                RegulatoryIssuePushConstants.METADATA_REG_ISSUE_JSON_FILE, metadataProperties,
                apiFactory, logger);
        logger.debug("json = " + json);
    }

    @Test(groups = {"engine"})
    public void validateAndProcessPayloadTest() {
        logger.debug("Start Testing");
        logger.debug("issueMetadata=" + issueMetadata);
        logger.debug("capMetadata=" + capMetadata);
        logger.debug("testData=" + testData);


        IssueRegistryConstants issueRegistryConstants = new IssueRegistryConstants();
        IServiceFactory apiFactory = Mockito.mock(IServiceFactory.class);
        IConfigurationService configService = Mockito.mock(IConfigurationService.class);
        IConfigProperties configProps = Mockito.mock(IConfigProperties.class);
        Mockito.when(configProps.getProperty(IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE)).thenReturn(issueMetadata);
        Mockito.when(configProps.getProperty(IssueRegistryConstants.ENGINE_CAP_METADATA_JSON_FILE)).thenReturn(capMetadata);
        Mockito.when(configService.getConfigProperties()).thenReturn(configProps);
        Mockito.when(apiFactory.createConfigurationService()).thenReturn(configService);
        IssueEngineRegistry issueEngineRegistry = new IssueEngineRegistry();
        Mockito.when(configService.getConfigProperties().getProperty(IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE)).thenReturn(issueMetadata);
        Mockito.when(configService.getConfigProperties().getProperty(IssueRegistryConstants.ENGINE_CAP_METADATA_JSON_FILE)).thenReturn(capMetadata);


        issueEngineRegistry.init(apiFactory, issueRegistryConstants.getRegistryConstants(), logger);
        String[] metadataProperties = {
                IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE,
                IssueRegistryConstants.ENGINE_CAP_METADATA_JSON_FILE
        };
        EngineService underTest = new EngineServiceImpl();
        EngineResultsBean dataEngineBean = underTest.validateAndProcessPayload(testData, issueEngineRegistry,
                IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE, metadataProperties,
                apiFactory, logger);
        //EngineResultsBean dataEngineBean = underTest.validateAndProcessPayload(testData, issueEngineRegistry, issueRegistryConstants.ENGINE_METADATA_ISSUE_JSON_FILE, apiFactory, logger);
        logger.debug("dataEngineBean = " + dataEngineBean.toString());
    }

}
