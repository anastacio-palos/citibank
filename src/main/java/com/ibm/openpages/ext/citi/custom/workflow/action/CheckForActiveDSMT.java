package com.ibm.openpages.ext.citi.custom.workflow.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;
import com.openpages.ext.solutions.common.LoggerUtilExtended;

public class CheckForActiveDSMT extends AbstractCustomAction {
	  
	  public CheckForActiveDSMT(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
	    super(context, customProperties);
	  }

	  private Logger logger = LoggerUtilExtended.getLogger("CheckForActiveDSMT");
	  private IServiceFactory serviceFactory = null;
	  private IResourceService resourceService = null;
	  private CommonResourceUtils commonResourceUtils = null;

	  
	  private static final String CAP_OBJECT_TYPE = "Citi_CAP";
	  private static final String DSMT_OBJECT_TYPE = "Citi_DSMT_Link";

	  private static final String DSMT_SCOPE_FIELD = "dsmt_scope_field";
	  private static final String DSMT_TYPE_FIELD = "dsmt_type_field";
	  private static final String DSMT_VALID_FIELD = "dsmt_valid_field";

	  private static final String DSMT_VALIDATION_ERROR_MESSAGE = "com.ibm.openpages.citi.dsmt.validation.error.message";

	  protected void process() throws Exception {
		  
		  
		  IWFOperationContext context = getContext();
		  serviceFactory = context.getServiceFactory();

		  resourceService = serviceFactory.createResourceService();
		  
		  commonResourceUtils = new CommonResourceUtils(logger);

		  IGRCObject issueObj = context.getResource();
		  
		  String dsmtMissingCAPS = null;
		  
		  ArrayList<String> capType = new ArrayList<String>();
		  
		  capType.add(CAP_OBJECT_TYPE);
		  
		  IGRCObject issueObject = commonResourceUtils.getGRCObjectWithChildren(issueObj.getId(), capType, serviceFactory);
		  
		  List<IAssociationNode> childCAPNodes = issueObject.getChildren();

		  for (IAssociationNode childCAPNode : childCAPNodes)
		  {
			  IGRCObject capObj = commonResourceUtils.getGRCObject(childCAPNode.getId(), serviceFactory);
			  
			  boolean isDSMTExists = checkChildDSMT(capObj.getId());
			  
			  if(!isDSMTExists)
			  {
				  if(dsmtMissingCAPS!=null)
					  dsmtMissingCAPS += ","+capObj.getName();
				  else
					  dsmtMissingCAPS = capObj.getName();
			  }
			  

		  }
		  
		  if(dsmtMissingCAPS!=null)
		  {
			  String dsmtErrorMsg = commonResourceUtils.getAppText(DSMT_VALIDATION_ERROR_MESSAGE, serviceFactory.createConfigurationService());
			  
			  dsmtErrorMsg = dsmtErrorMsg.replace("{0}", dsmtMissingCAPS);
			  
			  throwException(dsmtErrorMsg, null);
		  }
		  
		    
	  }
	   
	  private boolean checkChildDSMT(Id capId) throws Exception
	  {
		  
		  String dsmtScopeField = getPropertyValue(DSMT_SCOPE_FIELD);
		  
		  String dsmtTypeField = getPropertyValue(DSMT_TYPE_FIELD);

		  String dsmtValidField = getPropertyValue(DSMT_VALID_FIELD);
		  
		  boolean dsmtCheck = false;
		  
		  ArrayList<String> dsmtType = new ArrayList<String>();
			  
		  dsmtType.add(DSMT_OBJECT_TYPE);

		  IGRCObject capObj = commonResourceUtils.getGRCObjectWithChildren(capId, dsmtType, serviceFactory);
			  
		  List<IAssociationNode> childDSMTNodes = capObj.getChildren();
			  
		  if(childDSMTNodes.size()<1)
		  {
			  return false;
		  }
			  
		  for(IAssociationNode childDSMTNode : childDSMTNodes)
		  {
			  IGRCObject dsmtObj = resourceService.getGRCObject(childDSMTNode.getId());
			  String dsmtScopeValue = GRCObjectUtil.getFieldValue(dsmtObj, dsmtScopeField);
			  String dsmtTypeValue = GRCObjectUtil.getFieldValue(dsmtObj, dsmtTypeField);
			  String dsmtValidValue = GRCObjectUtil.getFieldValue(dsmtObj, dsmtValidField);
			  
			  if( dsmtScopeValue.equalsIgnoreCase("In") && dsmtTypeValue.equalsIgnoreCase("Home") && dsmtValidValue.equalsIgnoreCase("Yes") )
			  {
				  dsmtCheck = true;
				  break;
			  }
			  else
			  {
				  dsmtCheck = false;
			  }

		  }
			  
		  return dsmtCheck;
	  }	  
			  
}