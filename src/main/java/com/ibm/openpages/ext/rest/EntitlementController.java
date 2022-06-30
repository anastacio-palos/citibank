package com.ibm.openpages.ext.rest;

import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.EntitlementResponseBean;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementRegistryConstants;
import com.ibm.openpages.ext.interfaces.cmp.service.EntitlementService;
import com.ibm.openpages.ext.interfaces.cmp.util.EntitlementEngineRegistry;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import com.ibm.openpages.ext.interfaces.common.service.EngineService;
import com.ibm.openpages.ext.interfaces.common.util.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/cmp")
public class EntitlementController extends BaseExtController {

    @Autowired
    EngineService engineService;
    @Autowired
    EntitlementService entitlementService;
    @Autowired
    EntitlementEngineRegistry entitlementEngineRegistry;
    @Autowired
    EntitlementRegistryConstants entitlementRegistryConstants;
    private Logger logger = null;


    /**
     * Solicitud que recibe el PAYLOAD enviado por el usuario y lo redirecciona a una validacion
     * para posteriormente realizar las acciones requeridas por el usuario
     *
     * @param data     PAYLOAD enviado por el usuario
     * @param request  Tipo de solicitud enviada por el usuario
     * @param response
     * @return
     */
    @RequestMapping(value = "/security/cmpinterface", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public EntitlementResponseBean createEntitlement(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        IServiceFactory apiFactory = super.getServiceFactory(request);
        IConfigurationService configService = apiFactory.createConfigurationService();
        String enableDebug = configService.getConfigProperties().getProperty(EntitlementRegistryConstants.ENABLE_DEBUG);// ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), servicesUtil.getConfigProperties());
        String logFilePath = configService.getConfigProperties().getProperty(EntitlementRegistryConstants.LOG_FILE_PATH);// ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());
        String logFileSize = configService.getConfigProperties().getProperty(EntitlementRegistryConstants.LOG_FILE_SIZE);// ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());
        String logLevel = configService.getConfigProperties().getProperty(EntitlementRegistryConstants.LOG_LEVEL);
        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug, logFileSize, logLevel);
        logger.info("Starting Measure of elapsed time for current transaction");
        logger.debug("***** EntitlementController - createEntitlement START *****");
        EntitlementResponseBean entitlementResponseBean = null;
        List<String> messageList = new ArrayList<>();

        logger.debug("createEntitlement  >>>>   ***** Calling RegistryMap Init ***");
        entitlementEngineRegistry.init(apiFactory, entitlementRegistryConstants.getRegistryConstants(), logger);
        logger.debug("createEntitlement  >>>>   ***** MAP Content ***\n" + entitlementEngineRegistry);
        logger.info(" >>> PAYLOAD data{" + data + "}");
        EngineResultsBean dataEngineBean = engineService.validateAndProcessPayload(data, entitlementEngineRegistry, EntitlementRegistryConstants.ENGINE_METADATA_JSON_FILE, apiFactory, logger);
        entitlementResponseBean = entitlementService.createEntitlement(dataEngineBean, messageList, apiFactory, logger);
        logger.debug("***** EntitlementController - createEntitlement END *****");
        Long endTime = System.currentTimeMillis();
        logger.info("Transaction took " + (endTime - startTime) + " milliseconds");
        return entitlementResponseBean;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void resetRegistry(HttpServletRequest request, HttpServletResponse response) {
        entitlementEngineRegistry.reset();
    }

}
