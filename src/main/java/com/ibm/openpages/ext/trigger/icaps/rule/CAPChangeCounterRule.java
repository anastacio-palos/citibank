package com.ibm.openpages.ext.trigger.icaps.rule;

import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.trigger.events.AbstractResourceEvent;
import com.ibm.openpages.api.trigger.events.CreateResourceEvent;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.api.trigger.oob.ContentTypeMatchRule;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.LoggerUtil;
import com.openpages.aurora.common.logging.LoggerFactory;
import org.apache.commons.logging.Log;

import java.util.List;

public class CAPChangeCounterRule extends ContentTypeMatchRule {
    private final String INTERFACE_LOGGER = "CAPChangeCounter.log";
    private final String DISABLE_TRIGGER = "disable.trigger";
    private final String ATTR_IGNORED_FIELDS = "ignored.fields";

    private ILoggerUtil loggerUtil;
    private Log logger;
    private ServicesUtil servicesUtil = null;
    private boolean isTriggerDisabled = false;

    private void initLogger() {
        try {
            this.loggerUtil = new LoggerUtil();
            this.loggerUtil.initService();
            this.logger = loggerUtil.getExtLogger(INTERFACE_LOGGER);
        } catch (Exception e) {
            this.logger = LoggerFactory.getLogger();
            this.logger.error("Error while creating logger", e);
        }
    }

    private boolean evaluate(boolean isApplicable, AbstractResourceEvent event) {
        this.servicesUtil = new ServicesUtil(event.getContext());
        if (isApplicable) {
            this.isTriggerDisabled = TriggerUtil.isTriggerDisabled(TriggerUtil.getAttributeValue(DISABLE_TRIGGER, getAttributes()), this.servicesUtil.getConfigProperties());
            if (!this.isTriggerDisabled) {
                return isValidCAP(event);
            }
        }
        return false;
    }

    private boolean isValidCAP(AbstractResourceEvent event) {
        initLogger();
        String issueSrc = "";
        boolean result = false;
        try {
            IGRCObject object = (IGRCObject) event.getResource();
            issueSrc = ServiceUtil.getLabelValueFromField(object.getField(IssueCommonConstants.ISSUE_SOURCE_FIELD));

            if (!issueSrc.isEmpty() && !IssueCommonConstants.REGULATORY_VALUE.equals(issueSrc)) {
                String ignoredFields = TriggerUtil.getAttributeValue(ATTR_IGNORED_FIELDS, getAttributes());
                if (ignoredFields != null) {
                    String[] blackList = ignoredFields.trim().split(",");
                    logger.debug("blackList:" + ignoredFields);
                    List<IField> modifiedFields = ResourceUtil.getModifiedFields((IGRCObject) event.getResource());
                    if (!modifiedFields.isEmpty()) {
                        for (IField i : modifiedFields) {
                            logger.debug("modified field:" + i.getName());
                            boolean auxb = false;
                            for (String b : blackList) {
                                if (b.trim().equals(i.getName().trim())) {
                                    logger.debug("blacklisted field:" + i.getName());
                                    auxb = true;
                                    break;
                                }
                            }
                            result = !auxb;
                            if (!result) break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error while evaluating an audit issue/cap", e);
        }
        logger.debug("isValid CAP = " + result + " \n\n");
        return result;
    }

    public boolean isApplicable(CreateResourceEvent event) {
        if (!event.getResource().isFolder()) {
            return evaluate(super.isApplicable(event), event);
        }
        return false;
    }

    public boolean isApplicable(UpdateResourceEvent event) {
        if (!event.getResource().isFolder()) {
            return evaluate(super.isApplicable(event), event);
        }
        return false;
    }
}
