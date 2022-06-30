package com.ibm.openpages.ext.rest;

import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import com.ibm.openpages.ext.interfaces.common.service.EngineService;
import com.ibm.openpages.ext.interfaces.common.util.LoggerUtil;
import com.ibm.openpages.ext.interfaces.icaps.bean.IssueResponseBean;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueRegistryConstants;
import com.ibm.openpages.ext.interfaces.icaps.service.IssueService;
import com.ibm.openpages.ext.interfaces.icaps.util.IssueEngineRegistry;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/icaps")
public class IssueController extends BaseExtController {
    @Autowired
    EngineService engineService;
    @Autowired
    IssueService issueService;
    @Autowired
    IssueEngineRegistry issueEngineRegistry;
    @Autowired
    IssueRegistryConstants issueRegistryConstants;

    private Logger logger;

    @RequestMapping(value = "issue_and_caps", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IssueResponseBean createOrUpdateIssueAndCAP(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        IServiceFactory apiFactory = super.getServiceFactory(request);
        IConfigurationService configService = apiFactory.createConfigurationService();
        String enableDebug = configService.getConfigProperties().getProperty(IssueRegistryConstants.ENABLE_DEBUG);
        String logFilePath = configService.getConfigProperties().getProperty(IssueRegistryConstants.LOG_FILE_PATH_ICAPS);
        String logFileSize = configService.getConfigProperties().getProperty(IssueRegistryConstants.LOG_FILE_SIZE);
        String logLevel = configService.getConfigProperties().getProperty(IssueRegistryConstants.LOG_LEVEL);
        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug, logFileSize, logLevel);
        logger.debug("***** IssueController - createOrUpdateIssue START *****");
        IssueResponseBean issueResponse = null;
        List<String> messageList = new ArrayList<>();
        logger.trace("createEntitlement  >>>>   ***** Calling RegistryMap Init ***");
        issueEngineRegistry.init(apiFactory, issueRegistryConstants.getRegistryConstants(), logger);
        String json = issueEngineRegistry.getProperty(IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE_1);
        json += issueEngineRegistry.getProperty(IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE_2);
        issueEngineRegistry.putProperty(IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE, json);
        logger.info(" >>> Input PAYLOAD data: " + data + "");
        String[] metadataProperties = {
                IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE,
                IssueRegistryConstants.ENGINE_CAP_METADATA_JSON_FILE
        };
        EngineResultsBean dataEngineBean = engineService.validateAndProcessPayload(data, issueEngineRegistry, IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE, metadataProperties,
                apiFactory, logger);
        issueResponse = issueService.createOrUpdateIssueAndCAP(dataEngineBean, messageList, apiFactory, logger);
        logger.info(" >>> Output RESPONSE data: " + issueResponse + "");
        logger.trace("***** IssueController - createOrUpdateIssue END *****");
        Long endTime = System.currentTimeMillis();
        logger.info("Total transaction time = " + (endTime - startTime) + " milliseconds");
        return issueResponse;
    }

    @RequestMapping(value = "reset", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void resetRegistry(HttpServletRequest request, HttpServletResponse response) {
        issueEngineRegistry.reset();
    }
}
