package com.ibm.openpages.ext.citi.custom.workflow.action;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.IWorkflowService;
import com.ibm.openpages.api.workflow.IWFProcess;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.openpages.ext.solutions.common.LoggerUtilExtended;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.core.Logger;

public class EndChildCAPWorkflows extends AbstractCustomAction {
  
  public EndChildCAPWorkflows(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
    super(context, customProperties);
  }

  private Logger logger = LoggerUtilExtended.getLogger("EndChildCAPWorkflows");
  
  private static final String CAP_OBJECT_TYPE = "Citi_CAP";
  
  private IServiceFactory serviceFactory = null;
  
  private IResourceService resourceService = null;
  
  private IWorkflowService workflowService = null;
  
  private CommonResourceUtils commonResourceUtils = null;

  protected void process() throws Exception {
	  
    IWFOperationContext context = getContext();
    
    
    serviceFactory = context.getServiceFactory();
    resourceService = serviceFactory.createResourceService();
    workflowService = serviceFactory.createWorkflowService();
    commonResourceUtils = new CommonResourceUtils(logger);

    ArrayList<String> childrenTypes = new ArrayList<String>();
    childrenTypes.add(CAP_OBJECT_TYPE);
    
    IGRCObject issueObj = context.getResource();
      
    IGRCObject issueObject = commonResourceUtils.getGRCObjectWithChildren(issueObj.getId(), childrenTypes, serviceFactory);
    
    List<IAssociationNode> capNodes = issueObject.getChildren();
    
	Set<Id> capWorkflowProcesses = new HashSet<Id>();

	if(capNodes!=null)
	{
	    for (IAssociationNode capNode : capNodes)
	    {
	    	IGRCObject capObj = resourceService.getGRCObject(capNode.getId());
	    	List<IWFProcess> activeProcesses = workflowService.getActiveProcesses(capObj.getId());
	    	if(activeProcesses!=null)
	    	{
				for (IWFProcess proc : activeProcesses) 
				{
					capWorkflowProcesses.add(proc.getId());
				}
	    	}
		}
	}
    if(!capWorkflowProcesses.isEmpty())
    	workflowService.terminateProcesses(capWorkflowProcesses);
  }
  
}