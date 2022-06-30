package com.ibm.openpages.ext.workflow.action;

import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;

import java.util.List;

public class CacheUpdateSaveResource extends AbstractCustomAction {

    public CacheUpdateSaveResource(IWFOperationContext context, List<IWFCustomProperty> properties) {
        super(context, properties);
    }

    protected void process() throws Exception {
        IResourceService resourceService = getContext().getServiceFactory().createResourceService();
        resourceService.saveResource(getContext().getResource());
    }

}
