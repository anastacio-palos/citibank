package com.ibm.openpages.ext.trigger.icaps.rule;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.trigger.events.AssociateResourceEvent;
import com.ibm.openpages.api.trigger.events.CopyResourceEvent;
import com.ibm.openpages.api.trigger.events.CreateResourceEvent;
import com.ibm.openpages.api.trigger.events.DeleteResourceEvent;
import com.ibm.openpages.api.trigger.events.DisassociateResourceEvent;
import com.ibm.openpages.api.trigger.events.QueryEvent;
import com.ibm.openpages.api.trigger.events.SearchEvent;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.api.trigger.oob.ContentTypeMatchRule;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.LoggerUtil;
import com.openpages.aurora.common.logging.LoggerFactory;
import org.apache.commons.logging.Log;

public class ContentTypeMatchAndTriggerDisabledRule extends ContentTypeMatchRule {
    protected final String DISABLE_TRIGGER = "disable.trigger";
    protected final String INTERFACE_LOGGER = "log.filename";
    protected ILoggerUtil loggerUtil;
    protected Log logger;

    protected ServicesUtil servicesUtil = null;

    private boolean isTriggerDisabled = false;

    protected void initLogger() {
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
        if (isApplicable) {
            this.isTriggerDisabled = TriggerUtil.isTriggerDisabled(TriggerUtil.getAttributeValue(DISABLE_TRIGGER, getAttributes()), this.servicesUtil.getConfigProperties());
            if (!this.isTriggerDisabled)
                return true;
            this.logger.error("Trigger disabled at: " + TriggerUtil.getAttributeValue(DISABLE_TRIGGER, getAttributes()));
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