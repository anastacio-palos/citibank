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
import com.ibm.openpages.api.trigger.events.AssociateResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;

public class AuditableEntityApplicationAssociationHandler extends DefaultEventHandler{

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
	
	private static final String	SCOPE_VALUES_TO_CHECK = "app.scope.values.to.check";
	
	private static final String	SCOPE_VALUE_TO_SET = "app.scope.value.to.set";
	
	private static final String AUDIT_STATUS_VALUES = "audit.status.values";
	
	private ServicesUtil servicesUtil = null;
	
	private CommonResourceUtils commonResourceUtils = null;

    private static Logger logger = null;

    private static final String ENABLE_DEBUG = "enable.debug.mode";

    private static final String LOG_FILE_PATH = "log.file.path";
    
    private String QUERY_STMT = "SELECT [{3}].[Resource ID] FROM [{0}] JOIN [{1}] ON PARENT([{0}]) JOIN [{2}] ON PARENT([{1}]) JOIN [{3}] ON PARENT([{2}]) WHERE [{0}].[Resource ID] = '{11}' AND [{1}].[{4}] = '{8}' AND [{3}].[{5}] IN ({9}) AND [{3}].[{6}] = '{10}' AND [{3}].[{7}] = '{11}' AND [{2}].[{12}] NOT IN ({13})";

	@Override
	public boolean handleEvent(AssociateResourceEvent event) {
		
		servicesUtil = new ServicesUtil(event.getContext());
		
		IGRCObject appObj = null;

		IGRCObject aeObj = null;

		try {
			
	        String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), servicesUtil.getConfigProperties());
	        
	        String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());

	        String appScopeField = TriggerUtil.getAttributeValue(APP_SCOPE_FIELD, getAttributes());
	        
	        String scopeInValue = TriggerUtil.getAttributeValue(SCOPE_IN_VALUE, getAttributes());
	        
        	String scopeValueToSet = TriggerUtil.getAttributeValue(SCOPE_VALUE_TO_SET, getAttributes());
        	
        	if(scopeValueToSet.isEmpty())
        		scopeValueToSet = null;
	        
	        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);
	        
	        commonResourceUtils = new CommonResourceUtils(logger);
	
	        debug("-----------------------Entered Application Association Trigger trigger handler-----------------------");
	
	        List<Id> associatingParents = event.getParents();
	        
	        List<Id> associatingChildren = event.getChildren();

	        for(Id aeId : associatingParents)
	        {
	        	for(Id appId : associatingChildren)
	        	{
	        		appObj = servicesUtil.getResourceService().getGRCObject(appId);
	    	        aeObj = servicesUtil.getResourceService().getGRCObject(aeId);
	    	              
	    	        if(!aeObj.getType().getName().equalsIgnoreCase(AUDITABLE_ENTITY_TYPE))
	    	        {
	    	        	debug("Associating Parent Object Type is not Auditable Entity Object => Exiting Handler ");
	    	        	return true;
	    	        }
	    	        
	    	        debug("Trying to Associate child Application "+appObj.getName()+" to parent "+ aeObj.getName());
	    	        
	    	        //Get the Apps whose SrcRefId matches with Associating App and AEID Matches the Associating AE
	    	        
	            	ArrayList<String> macthedAppIdList = getQueryResults(scopeInValue, appId.toString(), aeId.toString());

	    	        for(String macthedAppId : macthedAppIdList)
	    	        {
	    	        	IGRCObject childAppObj = servicesUtil.getResourceService().getGRCObject(new Id(macthedAppId));
	    	        	commonResourceUtils.setFieldValueOnResource(appScopeField, scopeValueToSet, childAppObj, servicesUtil.getConfigurationService());
	                    servicesUtil.getResourceService().saveResource(childAppObj);
	    	        }

	        	}
	        }
	        	        
		}
        
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		debug("END Application Association Trigger");
		return true;
	}
	
	
    private void debug(String string)
    {

        LoggerUtil.debugEXTLog(logger, "", "", string);
    }
    
    public ArrayList<String> getQueryResults(String aeAppScopeValue, String appId, String aeId)
    {
      	      	
    	String queryStmt = QUERY_STMT;
    	
      	queryStmt = queryStmt.replace("{0}", AUDITABLE_ENTITY_TYPE);
      	queryStmt = queryStmt.replace("{1}", AUDITABLE_ENTITY_APP_TYPE);
      	queryStmt = queryStmt.replace("{2}", AUDIT_TYPE);
      	queryStmt = queryStmt.replace("{3}", APPLICATION_TYPE);
      	queryStmt = queryStmt.replace("{4}", TriggerUtil.getAttributeValue(AEAPP_SCOPE_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{5}", TriggerUtil.getAttributeValue(APP_SCOPE_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{6}", TriggerUtil.getAttributeValue(APP_SRC_REFID_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{7}", TriggerUtil.getAttributeValue(APP_AEID_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{8}", aeAppScopeValue);
      	queryStmt = queryStmt.replace("{9}", getAttributeValueWithSingleQuotes(TriggerUtil.getAttributeValue(SCOPE_VALUES_TO_CHECK, getAttributes())));
      	queryStmt = queryStmt.replace("{10}", appId);
      	queryStmt = queryStmt.replace("{11}", aeId);
      	queryStmt = queryStmt.replace("{12}", TriggerUtil.getAttributeValue(AUDIT_STATUS_FIELD, getAttributes()));
      	queryStmt = queryStmt.replace("{13}", getAttributeValueWithSingleQuotes(TriggerUtil.getAttributeValue(AUDIT_STATUS_VALUES, getAttributes())));

      	debug("queryStmt "+queryStmt);
    	
    	ArrayList<String> appList = new ArrayList<String>();
	  	
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
	                       String applId = idField.getValue().toString();
	                       appList.add(applId);
	                   }
	               }
	           }
	    	} catch (Exception ex) {
	    		logger.error("getQueryResults : Error in Query Service execution" + ex);
	    	}
	  	  
	  	  return appList;
    }
    
    public String getAttributeValueWithSingleQuotes(String triggerAttribute)
    {
  	  String triggerAttributeValues = null;
  	  
  	  List<String> triggerAttributeValueList = Arrays.asList(triggerAttribute.split(","));

  	  for(String attribute : triggerAttributeValueList)
  	  {
  		  if(triggerAttributeValues!=null)
  			triggerAttributeValues += ",'" + attribute + "'";
  		  else
  			triggerAttributeValues = "'" + attribute + "'";
  	  }
  	  
  	  return triggerAttributeValues;
    }
    
}

