package com.ibm.openpages.ext.trigger.icaps.rule;

import com.ibm.openpages.api.metadata.Id;
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
import com.ibm.openpages.ext.interfaces.icaps.constant.*;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.LoggerUtil;
import com.openpages.aurora.common.logging.LoggerFactory;
import org.apache.commons.logging.Log;
import org.codehaus.jackson.map.ObjectMapper;


import java.util.ArrayList;
import java.util.List;

public class AuditIssuePropertyChangeRule extends ContentTypeMatchRule {

    private final String INTERFACE_LOGGER = "log.filename";
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
            this.logger = loggerUtil.getExtLogger(TriggerUtil.getAttributeValue(INTERFACE_LOGGER, getAttributes()));
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
                return isValidAuditIssue(event);
            }
        }
        return false;
    }

    private boolean isValidAuditIssue(AbstractResourceEvent event) {
        initLogger();
        boolean result = false;
        String issueSrc = "";
        String issueStatus = "";
        String capStatus = "";
        List capStatusBlackList = new ArrayList<>();
        List statusBlackList = new ArrayList<>();
        try {
            IGRCObject object = (IGRCObject) event.getResource();
            if (object.getType().getName().equals(IssueCommonConstants.CITI_CAP_TYPE)) {
                Id parentId = object.getPrimaryParent();
                capStatusBlackList =
                        (new ObjectMapper()).readValue(this.servicesUtil.getConfigProperties().getProperty(AuditIssuePushConstants.TRIGGER_IGNORED_CAP_STATUS_LIST), List.class);
                logger.debug("Ignored CAP Status=" + capStatusBlackList);
                capStatus = ServiceUtil.getLabelValueFromField(object.getField(IssueCommonConstants.CAP_STATUS_FIELD));
                logger.debug("processing a CAP with status " + capStatus);
                object = this.servicesUtil.getResourceService().getGRCObject(parentId);
            }
            issueSrc = ServiceUtil.getLabelValueFromField(object.getField(IssueCommonConstants.ISSUE_SOURCE_FIELD));
            issueStatus = ServiceUtil.getLabelValueFromField(object.getField(IssueCommonConstants.ISSUE_STATUS_FIELD));
            logger.debug("processing a " + issueSrc + " type issue with status " + issueStatus);
            statusBlackList =
                    (new ObjectMapper()).readValue(this.servicesUtil.getConfigProperties().getProperty(AuditIssuePushConstants.TRIGGER_IGNORED_STATUS_LIST), List.class);
            logger.debug("Ignored Status=" + statusBlackList);
        } catch (Exception e) {
            logger.error("Error while evaluating an audit issue/cap", e);
        }
        if(capStatusBlackList.contains(capStatus))
            return false;
        try {
            if (!issueSrc.isEmpty() && !IssueCommonConstants.REGULATORY_VALUE.equals(issueSrc) &&
                    !statusBlackList.contains(issueStatus)
            ) {

                String fields = TriggerUtil.getAttributeValue(ATTR_IGNORED_FIELDS, getAttributes());
                if (fields != null) {
                    String[] blackList = fields.trim().split(",");
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
            logger.error("Error while evaluating ignored fields", e);
        }
        logger.debug("isValidAuditIssue = " + result + " \n\n");
        return result;
    }

    /**
     * Check if the create resource event should be applied.
     *
     * @param event a create resource event to be checked.
     * @return true if the event should be applied, otherwise return false.
     */
    public boolean isApplicable(CreateResourceEvent event) {
        if (!event.getResource().isFolder()) {
            return evaluate(super.isApplicable(event), event);
        }
        return false;
    }

    /**
     * Check if the update resource event should be applied.
     *
     * @param event an update resource event to be checked.
     * @return true if the event should be applied, otherwise return false.
     */
    public boolean isApplicable(UpdateResourceEvent event) {
        if (!event.getResource().isFolder()) {
            return evaluate(super.isApplicable(event), event);
        }
        return false;
    }

}
