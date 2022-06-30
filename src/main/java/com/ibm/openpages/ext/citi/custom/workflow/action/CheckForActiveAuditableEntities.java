package com.ibm.openpages.ext.citi.custom.workflow.action;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IIdField;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.ejb.workflow.impl.WFServiceException;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.CommonUtil;
import com.openpages.ext.solutions.common.LoggerUtilExtended;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.Logger;

public class CheckForActiveAuditableEntities extends AbstractCustomAction {
  
  public CheckForActiveAuditableEntities(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
    super(context, customProperties);
  }

  //private Logger logger = LoggerUtilExtended.getLogger("CheckForActiveAuditableEntities");
  private IServiceFactory serviceFactory = null;
  private IResourceService resourceService = null;
  private IQueryService queryService = null;
  private IConfigurationService configurationService = null;
  private CommonResourceUtils commonResourceUtils = null;
  private String aEStatusField = null;
  private String aEStatusValue = null;
  private String consolidatedErrorMsg = "";
  private static final String ISSUE_VALIDATION_ERROR_MESSAGE = "com.ibm.openpages.citi.issue.validation.error.message";
  private static final String AUDIT_VALIDATION_ERROR_MESSAGE = "com.ibm.openpages.citi.audit.validation.error.message";
  private static final String BMP_VALIDATION_ERROR_MESSAGE = "com.ibm.openpages.citi.bmp.validation.error.message";
  private static final String BMQS_VALIDATION_ERROR_MESSAGE = "com.ibm.openpages.citi.bmqs.validation.error.message";
  private static final String ISSUE_TYPE = "Citi_Iss";
  private static final String AUDITABLE_ENTITY_TYPE = "AuditableEntity";
  private static final String AUDITABLE_ENTITY_APP_TYPE = "Citi_AEApp";
  private static final String AUDIT_TYPE = "AuditProgram";
  private static final String BMP_TYPE = "Citi_AudEntityClus";
  private static final String BMQS_TYPE = "Citi_AudEntityClusEval";
  private static final String AUDITABLE_ENTITY_STATUS_FIELD = "aE_Status_Field";
  private static final String AUDITABLE_ENTITY_STATUS_VALUE = "aE_Status_Value";
  private static final String ISSUE_STATUS_FIELD = "issue_Status_Field";
  private static final String ISSUE_STATUS_VALUES = "issue_Status_Values";
  private static final String AEAPP_SCOPE_FIELD = "aeApp_Scope_Field";
  private static final String AEAPP_STATUS_FIELD = "aeApp_Status_Field";
  private static final String AUDIT_STATUS_FIELD = "audit_Status_Field";
  private static final String AUDIT_STATUS_VALUES = "audit_Status_Values";
  private static final String BMP_STATUS_FIELD = "bmp_Status_Field";
  private static final String BMP_STATUS_VALUES = "bmp_Status_Values";
  private static final String BMQS_STATUS_FIELD = "bmqs_Status_Field";
  private static final String BMQS_STATUS_VALUES = "bmqs_Status_Values";


  
  protected void process() throws Exception {
	  
    IWFOperationContext context = getContext();
    
    serviceFactory = context.getServiceFactory();
    resourceService = serviceFactory.createResourceService();
    queryService = serviceFactory.createQueryService();
    configurationService = serviceFactory.createConfigurationService();
    commonResourceUtils = new CommonResourceUtils(logger);
    ArrayList<String> parentTypes = new ArrayList<String>();
    
    parentTypes.add(AUDITABLE_ENTITY_TYPE);
    
	aEStatusField = getPropertyValue(AUDITABLE_ENTITY_STATUS_FIELD);
	if (aEStatusField == null) 
	  throwException("Property 'aE_Status_Field' must be set.", null);
	  
	aEStatusValue = getPropertyValue(AUDITABLE_ENTITY_STATUS_VALUE);
	if (aEStatusValue == null) 
	  throwException("Property 'aE_Status_Value' must be set.", null);
    
	logger.error("Inside Process");

    IGRCObject auditEntObj = context.getResource();
    
    String issuesWithSingleActiveAE = aEIssueValidationFail(auditEntObj.getId(),parentTypes);
    
    String auditsWithSingleActiveAE = aEAuditValidationFail(auditEntObj.getId());
    
    String bmpsWithSingleActiveAE = aEBMPValidationFail(auditEntObj.getId(),parentTypes);
    
    String bmqsWithSingleActiveAE = aEBMQSValidationFail(auditEntObj.getId());

    if(CommonUtil.isNotNullOrEmpty(issuesWithSingleActiveAE))
    {
    	String issueValidationMsg = commonResourceUtils.getAppText(ISSUE_VALIDATION_ERROR_MESSAGE, configurationService);
    	consolidatedErrorMsg += issueValidationMsg + " " + issuesWithSingleActiveAE + "\n"; 
    }
    
    if(CommonUtil.isNotNullOrEmpty(auditsWithSingleActiveAE))
    {
    	String auditValidationMsg = commonResourceUtils.getAppText(AUDIT_VALIDATION_ERROR_MESSAGE, configurationService);
    	consolidatedErrorMsg += auditValidationMsg + " " + auditsWithSingleActiveAE + "\n"; 
    }
    
    if(CommonUtil.isNotNullOrEmpty(bmpsWithSingleActiveAE))
    {
    	String bmpValidationMsg = commonResourceUtils.getAppText(BMP_VALIDATION_ERROR_MESSAGE, configurationService);
    	consolidatedErrorMsg += bmpValidationMsg + " " + bmpsWithSingleActiveAE + "\n"; 
    }
    
    if(CommonUtil.isNotNullOrEmpty(bmqsWithSingleActiveAE))
    {
    	String bmqsValidationMsg = commonResourceUtils.getAppText(BMQS_VALIDATION_ERROR_MESSAGE, configurationService);
    	consolidatedErrorMsg += bmqsValidationMsg + " " + bmqsWithSingleActiveAE + "\n"; 
    }
    
    if(!consolidatedErrorMsg.isEmpty())
    	throwException(consolidatedErrorMsg, null);
    
     
  }
  
  
  public String aEIssueValidationFail(Id currentAEId,ArrayList<String> parentTypes) throws WFServiceException
  {
	  
	  logger.error("Inside Issue Validation");

	  String issueStatusField = getPropertyValue(ISSUE_STATUS_FIELD);
	  if (issueStatusField == null) 
		  throwException("Property 'issue_Status_Field' must be set.", null);
	  
	  String issueStatusValues = getPropertyValue(ISSUE_STATUS_VALUES);
	  if (issueStatusValues == null) 
		  throwException("Property 'issue_Status_Values' must be set.", null);
	  
	  
	  ArrayList<String> childrenTypes = new ArrayList<String>();
	  
	  childrenTypes.add(ISSUE_TYPE);
	  
	  List<String> issueStatusList = Arrays.asList(issueStatusValues.split(","));;
	  
	  IGRCObject currentAEObj = commonResourceUtils.getGRCObjectWithChildren(currentAEId, childrenTypes, serviceFactory);
	  
	  List<IAssociationNode> childIssueNodes = currentAEObj.getChildren();
	  
	  String issuesWithSingleActiveAE = null;
	  
	  for (IAssociationNode childIssueNode : childIssueNodes)
	  {
		  IGRCObject issueObj = resourceService.getGRCObject(childIssueNode.getId());
		  
		  Object issueStatus = commonResourceUtils.getFieldValue(issueStatusField, issueObj);

		  //For Audit Issues, check if issue status list doesnot contain issueStatus 
		  
		  if(issueStatus!=null && !issueStatusList.contains(issueStatus.toString()))
		  {
			  issuesWithSingleActiveAE = getObjectsWithSingleActiveAE(childIssueNode.getId(),parentTypes,issuesWithSingleActiveAE);
		  }
		  
	  }
	  
	  logger.error("Issues with Current Active AE "+issuesWithSingleActiveAE);
	  
	  return issuesWithSingleActiveAE;
	  
  }
  
  public String aEAuditValidationFail(Id currentAEId) throws WFServiceException
  {

	  logger.error("Inside Audit Validation");
	  
	  String aeAppScopeField = getPropertyValue(AEAPP_SCOPE_FIELD);
	  if (aeAppScopeField == null) 
		  throwException("Property 'aeApp_Scope_Field' must be set.", null);
	  
	  String aeAppStatusField = getPropertyValue(AEAPP_STATUS_FIELD);
	  if (aeAppStatusField == null) 
		  throwException("Property 'aeApp_Status_Field' must be set.", null);
	  
	  String auditStatusField = getPropertyValue(AUDIT_STATUS_FIELD);
	  if (auditStatusField == null) 
		  throwException("Property 'audit_Status_Field' must be set.", null);
	  
	  String auditStatusValues = getStringWithSingleQuotes(getPropertyValue(AUDIT_STATUS_VALUES));
	  if (getPropertyValue(AUDIT_STATUS_VALUES) == null) 
		  throwException("Property 'audit_Status_Values' must be set.", null);

	  	
	  String auditsWithOneActiveAE = null;
			  
	  String queryStmt1 = "SELECT [{2}].[Resource ID] FROM [{0}] JOIN [{1}] ON PARENT([{0}]) JOIN [{2}] ON PARENT([{1}]) WHERE [{1}].[{7}] = 'In' AND ([{1}].[{8}] IS NULL) AND [{2}].[{3}] IN ({4}) AND [{0}].[Resource ID] = ";

	  String queryStmt2 = "SELECT [{0}].[Resource ID] FROM [{2}] JOIN [{1}] ON CHILD([{2}]) JOIN [{0}] ON CHILD([{1}]) WHERE [{1}].[{7}] = 'In' AND ([{1}].[{8}] IS NULL) AND [{0}].[{5}] = '{6}' AND [{2}].[Resource ID] = ";
	  
	  queryStmt1 = queryStmt1.replace("{0}", AUDITABLE_ENTITY_TYPE);
	  queryStmt1 = queryStmt1.replace("{1}", AUDITABLE_ENTITY_APP_TYPE);
	  queryStmt1 = queryStmt1.replace("{2}", AUDIT_TYPE);
	  queryStmt1 = queryStmt1.replace("{3}", auditStatusField);
	  queryStmt1 = queryStmt1.replace("{4}", auditStatusValues);
	  queryStmt1 = queryStmt1.replace("{7}", aeAppScopeField);
	  queryStmt1 = queryStmt1.replace("{8}", aeAppStatusField);
	  queryStmt1 = queryStmt1 + currentAEId.toString();

	  logger.error("queryStmt1 = "+queryStmt1);
	  
	  queryStmt2 = queryStmt2.replace("{0}", AUDITABLE_ENTITY_TYPE);
	  queryStmt2 = queryStmt2.replace("{1}", AUDITABLE_ENTITY_APP_TYPE);
	  queryStmt2 = queryStmt2.replace("{2}", AUDIT_TYPE);
	  queryStmt2 = queryStmt2.replace("{5}", aEStatusField);
	  queryStmt2 = queryStmt2.replace("{6}", aEStatusValue);
	  queryStmt2 = queryStmt2.replace("{7}", aeAppScopeField);
	  queryStmt2 = queryStmt2.replace("{8}", aeAppStatusField);
	  
	  //Get The Not Started Audits under the current auditable entity using queryStmt1
	  
	  ArrayList<String> auditList = getQueryResults(queryStmt1);
	  
	  //Traverse through each single audit that obtained
	  for(String auditId : auditList)
	  {
		  //Now Using the Audit, get the Active Auditable Entities using queryStmt2
		  
		  IGRCObject auditObj = commonResourceUtils.getGRCObject(new Id(auditId), serviceFactory);
		  
		  String queryStmt = queryStmt2 + auditId;

		  logger.error("queryStmt = "+queryStmt);
		  
		  ArrayList<String> aEList = getQueryResults(queryStmt);
		  
		  //If the aeList size is equal to 0, then current AE is the only Active Entity of Audit
		  
		  if(aEList.size()==0)
		  {
			  if(auditsWithOneActiveAE!=null)
				  auditsWithOneActiveAE += ","+auditObj.getName();
			  else
				  auditsWithOneActiveAE = auditObj.getName();
		  }
		  
	  }
	  
	  logger.error("Audits with Current Active AE "+auditsWithOneActiveAE);
	  
	  return auditsWithOneActiveAE;
	  
  }


  public String aEBMPValidationFail(Id currentAEId,ArrayList<String> parentTypes) throws WFServiceException
  {
	  
	  logger.error("Inside BMP Validation");
	  
	  String bmpStatusField = getPropertyValue(BMP_STATUS_FIELD);
	  if (bmpStatusField == null) 
		  throwException("Property 'bmp_Status_Field' must be set.", null);
	  
	  String bmpStatusValues = getPropertyValue(BMP_STATUS_VALUES);
	  if (bmpStatusValues == null) 
		  throwException("Property 'bmp_Status_Values' must be set.", null);
	  
	  
	  ArrayList<String> childrenTypes = new ArrayList<String>();
	  
	  childrenTypes.add(BMP_TYPE);
	  
	  List<String> bmpStatusList = Arrays.asList(bmpStatusValues.split(","));;
	  
	  IGRCObject currentAEObj = commonResourceUtils.getGRCObjectWithChildren(currentAEId, childrenTypes, serviceFactory);
	  
	  List<IAssociationNode> childBmpNodes = currentAEObj.getChildren();
	  
	  String bmpsWithSingleActiveAE = null;
	  
	  for (IAssociationNode childBmpNode : childBmpNodes)
	  {
		  IGRCObject bmpObj = resourceService.getGRCObject(childBmpNode.getId());
		  
		  Object bmpStatus = commonResourceUtils.getFieldValue(bmpStatusField, bmpObj);

		  //For BMP, check if BMP status list contain Current BMP Status 
		  
		  if(bmpStatus!=null && bmpStatusList.contains(bmpStatus.toString()))
		  {
			  bmpsWithSingleActiveAE = getObjectsWithSingleActiveAE(childBmpNode.getId(),parentTypes,bmpsWithSingleActiveAE);
		  }
		  
	  }
	  
	  logger.error("BMPs with Current Active AE "+bmpsWithSingleActiveAE);
	  
	  return bmpsWithSingleActiveAE;
	  
  }

  public String aEBMQSValidationFail(Id currentAEId) throws WFServiceException
  {

	  logger.error("Inside BMQS Validation");
	  
	  String bmqsStatusField = getPropertyValue(BMQS_STATUS_FIELD);
	  if (bmqsStatusField == null) 
		  throwException("Property 'bmqs_Status_Field' must be set.", null);
	  
	  String bmqsStatusValues = getStringWithSingleQuotes(getPropertyValue(BMQS_STATUS_VALUES));
	  if (getPropertyValue(BMQS_STATUS_VALUES) == null) 
		  throwException("Property 'bmqs_Status_Values' must be set.", null);

	  	
	  String bmqsWithOneActiveAE = null;
			  
	  String queryStmt1 = "SELECT [{2}].[Resource ID] FROM [{0}] JOIN [{1}] ON PARENT([{0}]) JOIN [{2}] ON PARENT([{1}]) WHERE [{2}].[{3}] IN ({4}) AND [{0}].[Resource ID] = ";

	  String queryStmt2 = "SELECT [{0}].[Resource ID] FROM [{2}] JOIN [{1}] ON CHILD([{2}]) JOIN [{0}] ON CHILD([{1}]) WHERE [{0}].[{5}] = '{6}' AND [{2}].[Resource ID] = ";
	  
	  queryStmt1 = queryStmt1.replace("{0}", AUDITABLE_ENTITY_TYPE);
	  queryStmt1 = queryStmt1.replace("{1}", BMP_TYPE);
	  queryStmt1 = queryStmt1.replace("{2}", BMQS_TYPE);
	  queryStmt1 = queryStmt1.replace("{3}", bmqsStatusField);
	  queryStmt1 = queryStmt1.replace("{4}", bmqsStatusValues);
	  queryStmt1 = queryStmt1 + currentAEId.toString();

	  logger.error("queryStmt1 = "+queryStmt1);
	  
	  queryStmt2 = queryStmt2.replace("{0}", AUDITABLE_ENTITY_TYPE);
	  queryStmt2 = queryStmt2.replace("{1}", BMP_TYPE);
	  queryStmt2 = queryStmt2.replace("{2}", BMQS_TYPE);
	  queryStmt2 = queryStmt2.replace("{5}", aEStatusField);
	  queryStmt2 = queryStmt2.replace("{6}", aEStatusValue);
	  
	  //Get The Not Approved BMQS under the current auditable entity using queryStmt1
	  
	  ArrayList<String> bmqsList = getQueryResults(queryStmt1);
	  
	  //Traverse through each single BMQS that obtained
	  for(String bmqsId : bmqsList)
	  {
		  //Now Using the BMQS, get the Active Auditable Entities using queryStmt2
		  
		  IGRCObject bmqsObj = commonResourceUtils.getGRCObject(new Id(bmqsId), serviceFactory);
		  
		  String queryStmt = queryStmt2 + bmqsId;

		  logger.error("queryStmt = "+queryStmt);
		  
		  ArrayList<String> aEList = getQueryResults(queryStmt);
		  
		  //If the aeList size is equal to 0, then current AE is the only Active Entity of BMQS
		  
		  if(aEList.size()==0)
		  {
			  if(bmqsWithOneActiveAE!=null)
				  bmqsWithOneActiveAE += ","+bmqsObj.getName();
			  else
				  bmqsWithOneActiveAE = bmqsObj.getName();
		  }
		  
	  }
	  
	  
	  logger.error("BMQS with Current Active AE "+bmqsWithOneActiveAE);

	  return bmqsWithOneActiveAE;
	  
  }

  public ArrayList<String> getQueryResults(String queryStmt)
  {
	  
	  ArrayList<String> objIdList = new ArrayList<String>();
	  
      try {
          IQuery query = queryService.buildQuery(queryStmt);
          ITabularResultSet resultset = query.fetchRows(0);
          for (IResultSetRow row : resultset) 
          {
              IField field = row.getField(0);
              if (field instanceof IIdField) 
              {
                  IIdField idField = (IIdField) field;
                  if (idField.getValue()!= null) 
                  {
                      String objId = idField.getValue().toString();
                      objIdList.add(objId);
                  }
              }
           }
  		} catch (Exception ex) {
  		logger.error("getQueryResults : Error in Query Service execution" + ex);
  		}
	  
	  return objIdList;
  }
  
  
  //Used for both Issues and BMPs
  public String getObjectsWithSingleActiveAE(Id objId,ArrayList<String> parentTypes,String objWithOneActiveAE)
  {
	  IGRCObject objWithAE = commonResourceUtils.getGRCObjectWithParents(objId, parentTypes, serviceFactory);
	  
	  List<IAssociationNode> parentAENodes = objWithAE.getParents();

	  int activeAECount = 0;//Reset activeAECount to zero to check for every issue and bmp
	  
	  for (IAssociationNode parentAENode : parentAENodes)
	  {
		  IGRCObject parentAEObj = resourceService.getGRCObject(parentAENode.getId());
		  Object aEstatus = commonResourceUtils.getFieldValue(aEStatusField, parentAEObj);
		  if(aEstatus!=null && aEstatus.toString().equalsIgnoreCase(aEStatusValue))
		  {
			  activeAECount += 1;
		  }	
	  }
	  
	  if(activeAECount==0)
	  {
		  if(objWithOneActiveAE!=null)
			  objWithOneActiveAE += ","+objWithAE.getName();
		  else
			  objWithOneActiveAE = objWithAE.getName();
	  }

	  return objWithOneActiveAE;
  }
  
  public String getStringWithSingleQuotes(String auditStatuses)
  {
	  String auditStatusValues = null;
	  
	  List<String> auditStatusList = Arrays.asList(auditStatuses.split(","));

	  for(String auditStatus : auditStatusList)
	  {
		  if(auditStatusValues!=null)
			  auditStatusValues += ",'" + auditStatus + "'";
		  else
			  auditStatusValues = "'" + auditStatus + "'";
	  }
	  
	  return auditStatusValues;
  }
  
}