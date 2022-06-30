package com.ibm.openpages.ext.trigger.icaps.rule;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.trigger.events.AbstractResourceEvent;
import com.ibm.openpages.api.trigger.events.CreateResourceEvent;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;

public class NotRegulatoryIssueSrcRule extends IgnoredFieldsChangeRule{

    private boolean evaluate(boolean isApplicable, AbstractResourceEvent event) {
        if (isApplicable) {
            if (isNotRegulatoryIssueSrc(event)) {
                return true;
            }
            this.logger.error("Trigger not fired, with a Regulatory Issue src \n\n");
            return false;
        }
        return false;
    }

    private boolean isNotRegulatoryIssueSrc(AbstractResourceEvent event) {
        boolean result = false;
        try {
            IGRCObject object = (IGRCObject) event.getResource();
            logger.debug("Processing a " + object.getType().getName() + " named " + object.getName());
            if (IssueCommonConstants.CITI_CAP_TYPE.equals(object.getType().getName())) {
                Id parentId = object.getPrimaryParent();
                if(parentId != null){
                    object = this.servicesUtil.getResourceService().getGRCObject(parentId);
                    logger.debug("CAP parent:" + object.getName() );
                }
            }
            if (IssueCommonConstants.CITI_ISS_TYPE.equals(object.getType().getName())) {
                if(!IssueCommonConstants.REGULATORY_VALUE.equals(ServiceUtil.getLabelValueFromField(object.getField(IssueCommonConstants.ISSUE_SOURCE_FIELD))))
                    result = true;
            }
        } catch (Exception e) {
            logger.error("Error while evaluating", e);
        }
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
