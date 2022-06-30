package com.ibm.openpages.ext.trigger.icaps.rule;

import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.trigger.events.*;
import com.ibm.openpages.ext.common.util.TriggerUtil;

import java.util.Arrays;
import java.util.List;

public class IgnoredFieldsChangeRule extends ContentTypeMatchAndTriggerDisabledRule{

    protected final String ATTR_IGNORED_FIELDS = "ignored.fields";

    private boolean evaluate(boolean isApplicable, AbstractResourceEvent event) {
        if (isApplicable) {
            if (isNotIgnoredField(event)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean isNotIgnoredField(AbstractResourceEvent event) {
        boolean result = false;
        logger.debug("Evaluating fields");
        try {
                        /*

2022/05/31 14:58:44,053 [DEBUG ] ChangeCounterRule.debug(EXTLogger.java:192) – Processing a Citi_CAP named 673936-34
2022/05/31 14:58:44,070 [DEBUG ] ChangeCounterRule.debug(EXTLogger.java:192) – CAP parent:673936
2022/05/31 14:58:44,243 [DEBUG ] ChangeCounterRule.debug(EXTLogger.java:192) – Evaluating fields
2022/05/31 14:58:44,244 [DEBUG ] ChangeCounterRule.debug(EXTLogger.java:192) – modified field: Citi-iCAPSCAP:changeCounter
2022/05/31 14:58:44,245 [DEBUG ] ChangeCounterRule.debug(EXTLogger.java:192) – field is not blacklisted
2022/05/31 14:58:44,245 [DEBUG ] ChangeCounterRule.debug(EXTLogger.java:192) – Trigger fired
             */
            String ignoredFields = TriggerUtil.getAttributeValue(ATTR_IGNORED_FIELDS, getAttributes());
            if (ignoredFields != null) {
                String[] blackList = ignoredFields.trim().split(",");
                List<IField> modifiedFields = ResourceUtil.getModifiedFields((IGRCObject) event.getResource());
                if (!modifiedFields.isEmpty()) {
                    logger.debug("blackList:"+ Arrays.asList(blackList));
                    for (IField i : modifiedFields) {
                        boolean blackListMatch = false;
                        for (String b : blackList) {
                            if (b.trim().equals(i.getName().trim())) {
                                blackListMatch = true;
                                break;
                            }
                        }
                        if(!blackListMatch) {
                            result = true;
                            logger.debug("modified field " + i.getName().trim() + " is NOT blacklisted");
                        } else {
                            logger.debug("modified field " + i.getName().trim() + " is blacklisted");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while evaluating", e);
        }
        if(result)
            this.logger.debug("Trigger fired \n");
        else
            this.logger.error("Trigger not fired \n");
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
