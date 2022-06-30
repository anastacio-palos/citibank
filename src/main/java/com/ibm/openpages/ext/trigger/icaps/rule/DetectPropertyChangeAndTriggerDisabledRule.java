package com.ibm.openpages.ext.trigger.icaps.rule;


import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.trigger.events.*;
import com.ibm.openpages.api.trigger.oob.DetectPropertyChangeRule;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.LoggerUtil;
import com.openpages.aurora.common.logging.LoggerFactory;
import org.apache.commons.logging.Log;


public class DetectPropertyChangeAndTriggerDisabledRule extends DetectPropertyChangeRule {
    private final String INTERFACE_LOGGER = "log.filename";
    private final String DISABLE_TRIGGER = "disable.trigger";

    private ILoggerUtil loggerUtil;
    private Log logger;

    private ServicesUtil servicesUtil = null;

    private boolean isTriggerDisabled = false;

    private void initLogger() {
        try {
            this.loggerUtil = new LoggerUtil();
            this.loggerUtil.initService();
            this.logger = loggerUtil.getExtLogger(TriggerUtil.getAttributeValue(INTERFACE_LOGGER, getAttributes()));
        } catch (Exception e) {
            this.logger = LoggerFactory.getLogger();
            this.logger.error("Error while creating logger", e);
        }
    }

    private boolean evaluate(boolean isApplicable, Context context) {
        initLogger();
        this.servicesUtil = new ServicesUtil(context);
        this.isTriggerDisabled = TriggerUtil.isTriggerDisabled(TriggerUtil.getAttributeValue(DISABLE_TRIGGER, getAttributes()), this.servicesUtil.getConfigProperties());
        if (isApplicable) {
            if (!this.isTriggerDisabled) {
                this.logger.debug("Trigger fired \n");
                return true;
            }
            this.logger.error("Trigger not fired, with disabled at: " + TriggerUtil.getAttributeValue(DISABLE_TRIGGER, getAttributes()) + " \n");
            return false;
        }
        return false;
    }

    public boolean isApplicable(CreateResourceEvent event) {
        if (!event.getResource().isFolder())
            return evaluate(super.isApplicable(event), event.getContext());
        return false;
    }

    public boolean isApplicable(UpdateResourceEvent event) {
        if (!event.getResource().isFolder())
            return evaluate(super.isApplicable(event), event.getContext());
        return false;
    }

    public boolean isApplicable(AssociateResourceEvent event) {
        return evaluate(super.isApplicable(event), event.getContext());
    }

    public boolean isApplicable(CopyResourceEvent event) {
        return evaluate(super.isApplicable(event), event.getContext());
    }

    public boolean isApplicable(DeleteResourceEvent event) {
        return evaluate(super.isApplicable(event), event.getContext());
    }

    public boolean isApplicable(DisassociateResourceEvent event) {
        return evaluate(super.isApplicable(event), event.getContext());
    }

    public boolean isApplicable(QueryEvent event) {
        return evaluate(super.isApplicable(event), event.getContext());
    }

    public boolean isApplicable(SearchEvent event) {
        return evaluate(super.isApplicable(event), event.getContext());
    }
}
