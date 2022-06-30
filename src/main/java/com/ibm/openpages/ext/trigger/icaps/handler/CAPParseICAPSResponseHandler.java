package com.ibm.openpages.ext.trigger.icaps.handler;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.trigger.events.AbstractResourceEvent;
import com.ibm.openpages.api.trigger.events.CreateResourceEvent;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;
import com.ibm.openpages.ext.interfaces.common.util.LoggerUtil;
import com.ibm.openpages.ext.interfaces.icaps.service.PushAuditIssueToICapsService;
import com.ibm.openpages.ext.interfaces.icaps.service.impl.PushAuditIssueToICapsServiceImpl;
import com.ibm.openpages.ext.tss.triggers.handler.BaseEventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CAPParseICAPSResponseHandler extends BaseEventHandler {
    public static final String MESSAGE = "Push CAP Issue to iCAPS Trigger ERROR";
    private Logger logger = LogManager.getLogger(CAPParseICAPSResponseHandler.class);
    private static final String ENABLE_DEBUG = "log.enable.debug.mode";
    private static final String LOG_FILE_PATH = "log.file.path";
    private static final String LOG_FILE_SIZE = "log.file.size";
    private static final String LOG_LEVEL = "log.level";
    private Long startTime = System.currentTimeMillis();
    private PushAuditIssueToICapsService icapsService = null;
    private ServicesUtil servicesUtil = null;

    public CAPParseICAPSResponseHandler() {
        super();
    }

    private void init(AbstractResourceEvent event) {
        this.servicesUtil = new ServicesUtil(event.getContext());
        initApplicationUtilServices();
        initTriggerLogger();
        this.icapsService = new PushAuditIssueToICapsServiceImpl(this.servicesUtil.getServiceFactory(), applicationUtil, logger);
    }

    public void initTriggerLogger() {
        try {
            String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), this.servicesUtil.getConfigProperties());
            String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), this.servicesUtil.getConfigProperties());
            String logFileSize = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_SIZE, getAttributes()), this.servicesUtil.getConfigProperties());
            String logLevel = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_LEVEL, getAttributes()), this.servicesUtil.getConfigProperties());
            logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug, logFileSize, logLevel);
        } catch (Exception e) {
            logger.error("error while creating audit issue logger", e);
        }
    }

    /**
     * Handles create resource event.
     *
     * @param event a create resource event to be handled
     * @return true.
     */
    @Override
    public boolean handleEvent(CreateResourceEvent event) {
        init(event);
        logger.debug("*** Start CAP Parse ICAPSResponse Handler  ***");
        boolean result = executePush((IGRCObject) event.getCreatedResource(), event.getContext());
        logger.info("Total transaction time = " + (System.currentTimeMillis() - startTime) + " milliseconds");
        logger.debug("*** End CAP Parse ICAPSResponse Handler ***");
        return result;
    }

    /**
     * Handles update resource event.
     *
     * @param event an update resource event to be handled
     * @return true.
     */
    @Override
    public boolean handleEvent(UpdateResourceEvent event) {
        init(event);
        logger.debug("*** Start CAP Parse ICAPSResponse Handler  ***");
        boolean result = executePush((IGRCObject) event.getUpdatedResource(), event.getContext());
        logger.info("Total transaction time = " + (System.currentTimeMillis() - startTime) + " milliseconds");
        logger.debug("*** End CAP Parse ICAPSResponse Handler ***");
        return result;
    }

    public boolean executePush(IGRCObject object, Context context) {
        boolean result = false;
        try {
            logger.debug("will parse iCAPS response on a grcObject with reference: " + object + " Name : "+ object.getName() + " Id : " + object.getId());
            icapsService.parseICapsResponse(object);
            result = true;
        } catch (Exception e) {
            logger.error(MESSAGE,e);
            throwException(MESSAGE + ":" + e.getMessage(), new ArrayList<>(), e, context);
        }
        return result;
    }
}
