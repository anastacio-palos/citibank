package com.ibm.openpages.ext.trigger.icaps.rule;

import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.trigger.events.AbstractResourceEvent;
import com.ibm.openpages.api.trigger.events.CreateResourceEvent;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.ext.common.util.TriggerUtil;

import java.util.List;

public class CertainFieldsChangeRule extends ContentTypeMatchAndTriggerDisabledRule{

    protected final String ATTR_KEY_FIELDS = "fields";

    private boolean evaluate(boolean isApplicable, AbstractResourceEvent event) {
        if (isApplicable) {
            if (isKeyField(event)) {
                return true;
            }
            return false;
        }
        return false;
    }

    //x y z


    private boolean isKeyField(AbstractResourceEvent event) {
        boolean result = false;
        logger.debug("Evaluating fields");
        try {
            String certainFields = TriggerUtil.getAttributeValue(ATTR_KEY_FIELDS, getAttributes());
            if (certainFields != null) {
                String[] whiteList = certainFields.trim().split(",");
                List<IField> modifiedFields = ResourceUtil.getModifiedFields((IGRCObject) event.getResource());
                if (!modifiedFields.isEmpty()) {
                    for (IField i : modifiedFields) {
                        logger.debug("modified field: " + i.getName());
                        boolean auxb = false;
                        for (String b : whiteList) {
                            if (b.trim().equals(i.getName().trim())) {
                                logger.debug("field is whitelisted");
                                auxb = true;
                                break;
                            }
                        }
                        if (auxb) result = true;
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
