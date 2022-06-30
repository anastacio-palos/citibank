package com.ibm.openpages.ext.workflow.action;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.ejb.workflow.impl.WFServiceException;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.interfaces.common.util.LoggerUtil;
import com.ibm.openpages.ext.interfaces.icaps.constant.RegulatoryIssuePushConstants;
import com.ibm.openpages.ext.interfaces.icaps.service.PushRegulatoryIssueToICapsService;
import com.ibm.openpages.ext.interfaces.icaps.service.impl.PushRegulatoryIssueToICapsServiceImpl;
import com.ibm.openpages.ext.tss.workflow.action.BaseWorkflowCustomAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.List;

public class PushRegulatoryIssueAction extends BaseWorkflowCustomAction {
    public static final String MESSAGE = "Push Regulatory Issue to iCAPS Custom Action ERROR";
    private static final String CUSTOM_ACTION_CONTEXT = "Custom Action Context";
    private static final String CUSTOM_ACTION_SAVE_RESOURCE = "Save Resource";
    private Logger logger = LogManager.getLogger(PushRegulatoryIssueAction.class);
    private IServiceFactory apiFactory;
    private IConfigurationService configService;
    private IGRCObject igrcObject;

    public PushRegulatoryIssueAction(IWFOperationContext context, List<IWFCustomProperty> properties) {
        super(context, properties);
    }

    public void init() {
        logger.debug("+** PushRegulatoryIssueAction - init **+");
        initApplicationUtilServices();
        IWFOperationContext operationContext = getContext();
        if (this.apiFactory == null) this.apiFactory = operationContext.getServiceFactory();
        if (this.configService == null) this.configService = apiFactory.createConfigurationService();
        if (this.igrcObject == null) igrcObject = operationContext.getResource();
        try {
            logger = LoggerUtil.getEXTLogger(configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.LOG_FILE_PATH), configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.ENABLE_DEBUG), configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.LOG_FILE_SIZE), configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.LOG_LEVEL));
        } catch (Exception e) {
            logger.error("error while creating regulatory issue logger", e);
        }
    }

    /**
     * Default process method for the workflow to implement the increment logic.
     */
    @Override
    protected void process() throws WFServiceException {
        Long startTime = Calendar.getInstance().getTimeInMillis();
        logger.info("PushRegulatoryIssueAction START " + Calendar.getInstance().getTime());
        try {
            init();
            PushRegulatoryIssueToICapsService service = new PushRegulatoryIssueToICapsServiceImpl(apiFactory, applicationUtil, logger);
            String context = getPropertyValue(CUSTOM_ACTION_CONTEXT);
            logger.debug("Issue Id:" + igrcObject.getId().toString() + " with context: " + context);
            logger.debug("will push to iCAPS a grcObject with reference:" + igrcObject);
            service.pushRegulatoryIssue(igrcObject, context);
        } catch (Exception e) {
            logger.error(MESSAGE, e);
            throwException(MESSAGE + ":" + e.getMessage(), e);
        }
        logger.debug("PushRegulatoryIssueAction END " + Calendar.getInstance().getTime());
        logger.info("Total transaction time = " + (Calendar.getInstance().getTimeInMillis() - startTime) + " milliseconds");
    }
}
