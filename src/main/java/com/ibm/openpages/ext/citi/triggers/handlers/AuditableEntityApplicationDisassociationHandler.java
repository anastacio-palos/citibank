package com.ibm.openpages.ext.citi.triggers.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IIdField;
import com.ibm.openpages.api.trigger.events.DisassociateResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;

public class AuditableEntityApplicationDisassociationHandler extends DefaultEventHandler{

	private static final String ERROR_MESSAGE = "app.text.error.app.disassociate";
    
	private static final String AUDITABLE_ENTITY_TYPE = "AuditableEntity";
	
	private static final String AUDITABLE_ENTITY_APP_TYPE = "Citi_AEApp";

	private static final String AUDIT_TYPE = "AuditProgram";
	
	private static final String APPLICATION_TYPE = "CitiApp";
	
	private static final String	AEAPP_SCOPE_FIELD = "aeapp.scope.field";
	
	private static final String	APP_SCOPE_FIELD = "app.scope.field";
	
	private static final String APP_AEID_FIELD = "app.aeid.field";
	
	private static final String APP_SRC_REFID_FIELD = "app.src.refid.field";
	
	private static final String AUDIT_STATUS_FIELD = "audit.status.field";
	
	private static final String	SCOPE_IN_VALUE = "scope.in.value";
	
	private static final String	SCOPE_OUT_VALUE = "scope.out.value";
	
	private static final String	SCOPE_OBSOLETE_VALUE = "scope.obsolete.value";
	
	private static final String AUDIT_STATUS_VALUES = "audit.status.values";
	
	private ServicesUtil servicesUtil = null;
	
	private CommonResourceUtils commonResourceUtils = null;

    private static Logger logger = null;

    private static final String ENABLE_DEBUG = "enable.debug.mode";

    private static final String LOG_FILE_PATH = "log.file.path";
    
    private String QUERY_STMT_1 = "SELECT [{2}].[Resource ID] FROM [{0}] JOIN [{1}] ON PARENT([{0}]) JOIN [{2}] ON PARENT([{1}]) JOIN [{3}] ON PARENT([{2}]) WHERE [{0}].[Resource ID] = '{11}' AND [{1}].[{4}] = '{8}' AND [{3}].[{5}] = '{9}' AND [{3}].[{6}] = '{10}' AND [{3}].[{7}] = '{11}' AND [{2}].[{12}] NOT IN ({13})";

    private String QUERY_STMT_2 = "SELECT [{3}].[Resource ID] FROM [{0}] JOIN [{1}] ON PARENT([{0}]) JOIN [{2}] ON PARENT([{1}]) JOIN [{3}] ON PARENT([{2}]) WHERE [{0}].[Resource ID] = '{11}' AND [{1}].[{4}] = '{8}' AND ([{3}].[{5}] = '{9}' OR [{3}].[{5}] IS NULL) AND [{3}].[{6}] = '{10}' AND [{3}].[{7}] = '{11}' AND [{2}].[{12}] NOT IN ({13})";

	@Override
	public boolean handleEvent(DisassociateResourceEvent event) {
		
		servicesUtil = new ServicesUtil(event.getContext());
		
		IGRCObject appObj = null;

		IGRCObject aeObj = null;

		boolean isException = false;
		
        String errorMsg = null;
        
        String errorAuditIds = null;

		try {
			
	        String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), servicesUtil.getConfigProperties());
	        
	        String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());

	        String appScopeField = TriggerUtil.getAttributeValue(APP_SCOPE_FIELD, getAttributes());

	        String scopeInValue = TriggerUtil.getAttributeValue(SCOPE_IN_VALUE, getAttributes());
	        
	        String scopeOutValue = TriggerUtil.getAttributeValue(SCOPE_OUT_VALUE, getAttributes());
	        
	        Object scopeObsoleteValue = TriggerUtil.getAttributeValue(SCOPE_OBSOLETE_VALUE, getAttributes());
	        
	        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);
	        
	        commonResourceUtils = new CommonResourceUtils(logger);
	
	        debug("-----------------------Entered Application DisAssociation Trigger trigger handler-----------------------");
	        
	        //
	        
	        List<Id> disAssociatingParents = event.getParents();
	        
	        List<Id> disAssociatingChildren = event.getChildren();

	        for(Id aeId : disAssociatingParents)
	        {
	        	for(Id appId : disAssociatingChildren)
	        	{
	        		appObj = servicesUtil.getResourceService().getGRCObject(appId);
	    	        aeObj = servicesUtil.getResourceService().getGRCObject(aeId);
	    	              
	    	        if(!aeObj.getType().getName().equalsIgnoreCase(AUDITABLE_ENTITY_TYPE))
	    	        {
	    	        	debug("Disassociating Parent Object Type is not Auditable Entity Object => Exiting Handler ");
	    	        	return true;
	    	        }
	    	        
	    	        debug("Trying to disassociate child Application "+appObj.getName()+" from parent "+ aeObj.getName());
	    	
	    	        //Get the Audits whose child App is IN scope and SrcRefId matches with Dissociating App and AEID Matches the disassociating AE
	    	        
	    	        ArrayList<String> auditIdList = getQueryResults(scopeInValue, scopeInValue, appId.toString(), aeId.toString(), QUERY_STMT_1);

	    	        for(String auditId : auditIdList)
	    	        {
	    	        	IGRCObject childAuditObj = servicesUtil.getResourceService().getGRCObject(new Id(auditId));
	    	        	if(errorAuditIds==null)
	    	        		errorAuditIds = childAuditObj.getName();
	    	        	else
	    	        		errorAuditIds += ","+childAuditObj.getName();
	    	        }
	    	        
	    	        
	    	        if(auditIdList.size()>0)
	    	        {
	    	        	isException = true;
	    	        }
	    	        
	    	        else
	    	        {
	    	        	// If the disassociation is allowed, mark the out scope app ids as Obsolete
	    	        	
	    	        	// Get the App which are in OUT scope and SrcRefId matches with Dissociating App and AEID Matches the disassociating AE
	    		        
	    		        ArrayList<String> outScopeappIdList = getQueryResults(scopeInValue, scopeOutValue, appId.toString(), aeId.toString(), QUERY_STMT_2);

	    		        for(String outScopeappId : outScopeappIdList)
	    		        {
	    		        	IGRCObject childAppObj = servicesUtil.getResourceService().getGRCObject(new Id(outScopeappId));
	    		        	commonResourceUtils.setFieldValueOnResource(appScopeField, scopeObsoleteValue, childAppObj, servicesUtil.getConfigurationService());
	    	                servicesUtil.getResourceService().saveResource(childAppObj);
	    		        }
	    	        }

	        	}
	        }
	        
	        //
	        	        
        	errorMsg = ApplicationUtil.getApplicationText(TriggerUtil.getAttributeValue(ERROR_MESSAGE, getAttributes()), servicesUtil.getConfigurationService()) + errorAuditIds;

		}
        
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(isException)
		{
			throwException(errorMsg, new ArrayList<Object>(), new Exception(), event.getContext());
		}
		
		debug("END Application DisAssociation Trigger");
		return true;
	}
	
	
    private void debug(String string)
    {

        LoggerUtil.debugEXTLog(logger, "", "", string);
    }
    
    public ArrayList<String> getQueryResults(String aeAppScopeValue, String appScopeValue, String appId, String aeId, String queryStmt)
    {
      	      	
      	queryStmt = queryStmt.replace("{0}", AUDITABLE_ENTITY_TYPE);
      	queryStmt = queryStmt.replace("{1}", AUDITABLE_ENTITY_APP_TYPE);
      	queryStmt = queryStmt.replace("{2}", AUDIT_TYPE);
      	queryStmt = queryStmt.replace("{3}", APPLICATION_TYPE);
      	queryStmt = queryStmt.replace("{4}", TriggerUtil.getAttributeValue(AEAPP_SCOPE_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{5}", TriggerUtil.getAttributeValue(APP_SCOPE_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{6}", TriggerUtil.getAttributeValue(APP_SRC_REFID_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{7}", TriggerUtil.getAttributeValue(APP_AEID_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{8}", aeAppScopeValue);
      	queryStmt = queryStmt.replace("{9}", appScopeValue);
      	queryStmt = queryStmt.replace("{10}", appId);
      	queryStmt = queryStmt.replace("{11}", aeId);
      	queryStmt = queryStmt.replace("{12}", TriggerUtil.getAttributeValue(AUDIT_STATUS_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{13}", getAuditStatusValuesWithSingleQuotes(TriggerUtil.getAttributeValue(AUDIT_STATUS_VALUES, getAttributes())));

      	debug("queryStmt "+queryStmt);
    	
    	ArrayList<String> objIdList = new ArrayList<String>();
	  	
	    try {
	           IQuery query = servicesUtil.getQueryService().buildQuery(queryStmt);
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
    
    public String getAuditStatusValuesWithSingleQuotes(String auditStatuses)
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

