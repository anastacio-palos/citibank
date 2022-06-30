package com.ibm.openpages.ext.citi.custom.workflow.action;

import com.ibm.openpages.api.resource.IIntegerField;
import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IResource;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.api.service.local.SessionFactory;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.openpages.aurora.common.AuroraConstants;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.sdk.OpenpagesSession;

import java.util.List;

public class IncrementReopenedFieldCount extends AbstractCustomAction {
  
  public IncrementReopenedFieldCount(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
    super(context, customProperties);
  }

  //private IServiceFactory serviceFactory = null;
  
  private IResourceService resourceService = null;

  protected void process() throws Exception {
	  
    IWFOperationContext context = getContext();
    
    //serviceFactory = context.getServiceFactory();
    //resourceService = serviceFactory.createResourceService();
    resourceService = getOPSystemServiceFactory().createResourceService();
    
    IGRCObject grcObj = context.getResource();
        
    IIntegerField reOpenedFieldCount = (IIntegerField)grcObj.getField(getPropertyValue("fieldToSet"));

     if(reOpenedFieldCount.getValue()!=null)
	 {
    	 reOpenedFieldCount.setValue(reOpenedFieldCount.getValue()+1);   
	 }
	 else
	 {
		 reOpenedFieldCount.setValue(1);
	 }
     
     resourceService.saveResource((IResource)grcObj);
     
  }
  
  private IServiceFactory getOPSystemServiceFactory() {

		Context context = new Context();
		context.put(Context.SERVICE_USER_NAME, AuroraConstants.OP_SYSTEM_USER);
		context.put(Context.SERVICE_USER_PASSWORD, SecurityUtil.getSystemPassword());
		
		SessionFactory sessionFactory = new SessionFactory();
		OpenpagesSession session = (OpenpagesSession) sessionFactory.create(context);
		
		context = new Context();
		context.put(Context.SERVICE_SESSION, session);

		return new LocalServiceFactory(context);
	}
  
}