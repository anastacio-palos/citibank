package com.ibm.openpages.ext.citi.triggers.handlers;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.application.MoveObjectOptions;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IIdField;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.api.service.local.SessionFactory;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.ibm.openpages.ext.common.util.CommonConstants;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.CommonUtil;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;
import com.openpages.aurora.common.AuroraConstants;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.sdk.OpenpagesSession;

public class ChangeAuditPlanOnAuditTriggerHandler extends DefaultEventHandler{

	private static final String AUDIT_PLAN_TYPE = "Citi_AuditPlan";
    
    private static final String LEAD_PROD_TEAM_APPR_FIELD = "lead.prod.team.appr.field";

    private static final String LEAD_PROD_TEAM_APPR_YES_VALUE = "lead.prod.team.appr.yes.value";
    
    private static final String LEAD_PROD_TEAM_APPR_NO_VALUE = "lead.prod.team.appr.no.value";
    
    private static final String PRP_LEAD_PROD_TEAM_FIELD = "prp.lead.prod.team.field";

    private static final String ENTITY_NOT_FOUND_ERROR_MSG = "app.text.error.entity.not.found";
    
    private static final String NO_AUDIT_PLAN_FOUND_ERROR_MSG = "app.text.error.no.audit.plan.found";

    private ServicesUtil servicesUtil = null;
    
    private IServiceFactory serviceFactory = null;

	private IResourceService resourceService = null;
	
	private IConfigurationService configurationService = null;

	private IConfigProperties configProperties = null;
	
	private CommonResourceUtils commonResourceUtils = null;

    private Logger logger = null;

    private static final String ENABLE_DEBUG = "enable.debug.mode";

    private static final String LOG_FILE_PATH = "log.file.path";
    
    private static final String DISABLE_TRIGGER = "disable.trigger";

 
	@Override
	public boolean handleEvent(UpdateResourceEvent event) {
		
		servicesUtil = new ServicesUtil(event.getContext());
        
		try {
			
	        serviceFactory = servicesUtil.getServiceFactory();
	        
	    	configurationService = servicesUtil.getConfigurationService();
	    	        
	    	configProperties = servicesUtil.getConfigProperties();
			
	        String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), configProperties);
	        
	        String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), configProperties);

	        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);      
	        
	        commonResourceUtils = new CommonResourceUtils(logger);
	        
	        debug("-----------------------Entered Change Audit Plan on Audit trigger handler-----------------------");
	
			if (isTriggerDisabled()) 
			{ 
				debug("Trigger disabled => exiting handler");
				return true; 
			}
			        
            IGRCObject currentAuditObj = (IGRCObject) event.getUpdatedResource();
            
            String leadProdTeamApprFieldVal = GRCObjectUtil.getFieldValue(currentAuditObj, TriggerUtil.getAttributeValue(LEAD_PROD_TEAM_APPR_FIELD, getAttributes()));
            
            String leadProdTeamApprYesVal = TriggerUtil.getAttributeValue(LEAD_PROD_TEAM_APPR_YES_VALUE, getAttributes());
            
            String leadProdTeamApprNoVal = TriggerUtil.getAttributeValue(LEAD_PROD_TEAM_APPR_NO_VALUE, getAttributes());

            if(!leadProdTeamApprFieldVal.equals(leadProdTeamApprYesVal))
            {
    	        debug("Lead Product Team Appr is not equal to Yes -- Exiting Handler");
            	return true;
            }
            
            String prpLeadProdTeamField = TriggerUtil.getAttributeValue(PRP_LEAD_PROD_TEAM_FIELD, getAttributes());
            
            String prpLeadProdTeamFieldVal = GRCObjectUtil.getFieldValue(currentAuditObj, prpLeadProdTeamField);

        	String queryStmt = "SELECT [SOXBusEntity].[Resource ID],[SOXBusEntity].[Name] FROM [SOXBusEntity] WHERE [SOXBusEntity].[Name] = '{0}'";
        	
        	String entityNotFoundErrorMsg = ApplicationUtil.getApplicationText(TriggerUtil.getAttributeValue(ENTITY_NOT_FOUND_ERROR_MSG, getAttributes()), configurationService);
        	
        	String noAuditPlanFoundErrorMsg = ApplicationUtil.getApplicationText(TriggerUtil.getAttributeValue(NO_AUDIT_PLAN_FOUND_ERROR_MSG, getAttributes()), configurationService);
        	
        	String beName = prpLeadProdTeamFieldVal;
        	
        	if(prpLeadProdTeamFieldVal.contains(CommonConstants.FOLDER_SEPARATOR))
        		beName = prpLeadProdTeamFieldVal.substring(prpLeadProdTeamFieldVal.lastIndexOf(CommonConstants.FOLDER_SEPARATOR)+1);
        	
        	queryStmt = queryStmt.replace("{0}", beName.trim());
        	
        	debug("queryStmt " + queryStmt);
        	
        	ArrayList<Id> queryResults = getQueryResults(queryStmt);
        	
            if(queryResults.isEmpty())
            {
    	        debug("Entity Not Found -- Exiting Handler");
    	        entityNotFoundErrorMsg = entityNotFoundErrorMsg.replace("{0}", prpLeadProdTeamFieldVal);
            	throwException(entityNotFoundErrorMsg,new ArrayList<Object>(), new Exception(), event.getContext());
            }
            
            else
            {
            	Id beId = queryResults.get(0);

            	ArrayList<String> auditPlanType = new ArrayList<String>();
          	  
            	auditPlanType.add(AUDIT_PLAN_TYPE);
            	
            	IGRCObject busEntObj = commonResourceUtils.getGRCObjectWithChildren(beId, auditPlanType, serviceFactory);
          	  
          	  	List<IAssociationNode> childAuditPlanNodes = busEntObj.getChildren();
          	  	
          	  	if(childAuditPlanNodes==null || childAuditPlanNodes.isEmpty() || childAuditPlanNodes.size()>1)
          	  	{
          	  		debug("Child Audit Plans Under Selected Business Entity Not found");
          	  		noAuditPlanFoundErrorMsg = noAuditPlanFoundErrorMsg.replace("{0}", prpLeadProdTeamFieldVal);
                	throwException(noAuditPlanFoundErrorMsg,new ArrayList<Object>(), new Exception(), event.getContext());
          	  	}
          	  	
          	  	else
          	  	{
              	  	IGRCObject auditPlanObj = commonResourceUtils.getGRCObject(childAuditPlanNodes.get(0).getId(), serviceFactory);
              	  	Id currentAuditId = currentAuditObj.getId();
              	  	
              	  	ArrayList<Id> auditObjToMove = new ArrayList<Id>();
              	  	auditObjToMove.add(currentAuditId);
              	  	//applicationService.moveGRCObject(auditPlanObj.getId(), auditObjToMove, new MoveObjectOptions());
              	  	getOPSystemServiceFactory().createApplicationService().moveGRCObject(auditPlanObj.getId(), auditObjToMove, new MoveObjectOptions());
              	  	
              	  	IGRCObject movedAuditObj = commonResourceUtils.getGRCObject(currentAuditId, serviceFactory);
              	  	ResourceUtil.setFieldValue(movedAuditObj.getField(prpLeadProdTeamField), null);
              	  	ResourceUtil.setFieldValue(movedAuditObj.getField(TriggerUtil.getAttributeValue(LEAD_PROD_TEAM_APPR_FIELD, getAttributes())), leadProdTeamApprNoVal);
              	  	getOPSystemServiceFactory().createResourceService().saveResource(movedAuditObj);
          	  	}
            }
        	     
		}
        
		catch (Exception e) {
			
        	throwException(e.getMessage(),new ArrayList<Object>(), new Exception(), event.getContext());
        	e.printStackTrace();
		}
		
		debug("END Change Audit Plan on Audit Trigger Handler");

		return true;
	}
	
	
	 private boolean isTriggerDisabled()
	 {
		 String disableTrigger = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(DISABLE_TRIGGER, getAttributes()),configProperties); 
		 
		 if (CommonUtil.isNotNullOrEmpty(disableTrigger) && disableTrigger.equalsIgnoreCase(CommonConstants.TRUE))
			return true;
		else
			return false;
	 }
	
	
    private void debug(String string)
    {

        LoggerUtil.debugEXTLog(logger, "", "", string);
    }
    
    public ArrayList<Id> getQueryResults(String queryStmt)
    {
  	  
  	  ArrayList<Id> objIdList = new ArrayList<Id>();
  	  
        try {
            IQuery query = serviceFactory.createQueryService().buildQuery(queryStmt);
            ITabularResultSet resultset = query.fetchRows(0);
            for (IResultSetRow row : resultset) 
            {
                IField field = row.getField(0);
                if (field instanceof IIdField) 
                {
                    IIdField idField = (IIdField) field;
                    if (idField.getValue()!= null) 
                    {
                        Id objId = idField.getValue();
                        objIdList.add(objId);
                    }
                }
             }
    		} catch (Exception ex) {
    		logger.error("getQueryResults : Error in Query Service execution" + ex);
    		}
  	  
  	  return objIdList;
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

