package com.ibm.openpages.ext.citi.triggers.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IDateField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.ibm.openpages.ext.common.util.CommonConstants;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.CommonUtil;
import com.ibm.openpages.ext.common.util.DateUtil;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;

public class IssueStatusRulesHandler extends DefaultEventHandler{

	private static final String ACTION_ITEM_TYPE = "Citi_CAP";
    
    private static final String CAP_COUNT_FIELD = "cap.count.field";
    
    private static final String ISSUE_SOURCE_FIELD = "issue.source.field";

    private static final String ISSUE_STATUS_FIELD = "issue.status.field";
    
    private static final String ISSUE_STATUS_OPEN_VALUE = "issue.status.open.value";
    
    private static final String ISSUE_STATUS_AWAITING_ISSUE_VALIDATION_VALUE = "issue.status.awaitingIssueValidation.value";
   
    private static final String DATE_CHANGED_TO_AW_STATUS_FIELD = "date.changed.to.aw.status.field";

    private static final String DAYS_IN_AW_STATUS_FIELD = "days.in.aw.status.field";

    private static final String IA_ISSUE_VAL_DATE_FIELD = "ia.iss.val.date.field";

    private static final String IA_ISSUE_VAL_DAYS_REM_FIELD = "ia.iss.val.days.rem.field";
    
    private static final String ISSUE_LEVEL_FIELD = "issue.level.field";

    private static final String CURRENT_ISSUE_DUE_DATE_FIELD = "current.issue.due.date.field";

    private static final String REVISED_ISSUE_DUE_DATE_FIELD = "revised.issue.due.date.field";

    private static final String AP_DUE_DATE_FIELD = "ap.due.date.field";
    
    private static final String AP_REVISED_DUE_DATE_FIELD = "ap.revised.due.date.field";
    
    private static final String REMED_PLAN_FINALIZED_FIELD = "rem.plan.finalized.field";
    
    private static final String FIRE_ONCE_FIELD = "fire.once.field";

	private ServicesUtil servicesUtil = null;
	
	private IConfigProperties configProperties = null;

	private IConfigurationService configurationService = null;
	
	private CommonResourceUtils commonResourceUtils = null;

    private Logger logger = null;

    private static final String ENABLE_DEBUG = "enable.debug.mode";

    private static final String LOG_FILE_PATH = "log.file.path";
    
    private static final String DISABLE_TRIGGER = "disable.trigger";
    
    private static final String OVERWRITE_DATE_CHANGED_TO_AW_STATUS = "overwrite.date.changed.to.aw.status";
    
    private static final String RUN_DAYS_IN_AW_STATUS_CALC = "run.days.in.aw.status.calc";
    
    private static final String RUN_IA_ISSUE_VAL_DATE_CALC = "run.ia.issue.val.date.calc";
    
    private static final String RUN_IA_ISSUE_VAL_DAYS_REM_CALC = "run.ia.issue.val.days.rem.calc";

    private static final String SHORT_TIME_FRAME = "short.time.frame";
    
    private static final String LONG_TIME_FRAME = "long.time.frame";
    
    private static final String SHORT_LEVELS = "short.levels";
    
    private static final String LONG_LEVELS = "long.levels";
    
	@Override
	public boolean handleEvent(UpdateResourceEvent event) {
		
		servicesUtil = new ServicesUtil(event.getContext());
		
		configurationService = servicesUtil.getConfigurationService();
		
		configProperties = servicesUtil.getConfigProperties();
		
		try {
			
	        String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), configProperties);
	        
	        String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), configProperties);

	        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);      
	        
	        commonResourceUtils = new CommonResourceUtils(logger);
	
	        debug("-----------------------Entered Issue Status Rules trigger handler-----------------------");
	
			if (isTriggerDisabled()) 
			{ 
				debug("Trigger disabled => exiting handler");
				return true; 
			}
			        
            IGRCObject currentIssObj = (IGRCObject) event.getUpdatedResource();
            
            String capCountFieldValue = GRCObjectUtil.getFieldValue(currentIssObj, TriggerUtil.getAttributeValue(CAP_COUNT_FIELD, getAttributes()));
            
            if(CommonUtil.isNullOrEmpty(capCountFieldValue))
            {
            	return true;
            }
            
        	String issueSource = GRCObjectUtil.getFieldValue(currentIssObj, TriggerUtil.getAttributeValue(ISSUE_SOURCE_FIELD, getAttributes()));

        	String issueStatusField = TriggerUtil.getAttributeValue(ISSUE_STATUS_FIELD, getAttributes());  
            
        	String issueStatus = GRCObjectUtil.getFieldValue(currentIssObj, issueStatusField); 

        	String issueStatusOpenVal = TriggerUtil.getAttributeValue(ISSUE_STATUS_OPEN_VALUE, getAttributes());  
        	
        	String issueStatusAwIssVal = TriggerUtil.getAttributeValue(ISSUE_STATUS_AWAITING_ISSUE_VALIDATION_VALUE, getAttributes());  
    
	        debug("issueStatus "+issueStatus);
        	
        	if(CommonUtil.isNotNullOrEmpty(issueSource) && (!issueSource.equalsIgnoreCase("Regulatory")) &&(issueStatus.equals(issueStatusOpenVal) || issueStatus.equals(issueStatusAwIssVal)))
        	{

            	Map<String, Integer> capCountValueMap = new LinkedHashMap<String, Integer>();
    	        ArrayList<String> childrenTypes = new ArrayList<String>();
    	  	  	childrenTypes.add(ACTION_ITEM_TYPE);
                IGRCObject currentIssObjWithCAP = commonResourceUtils.getGRCObjectWithChildren(currentIssObj.getId(), childrenTypes, servicesUtil.getServiceFactory());
          	  	int childCAPCount = currentIssObjWithCAP.getChildren().size();
    	        List<String> capCountValuesList = Arrays.asList(capCountFieldValue.split("\\|"));
    	        
    	        debug("childCAPCount "+childCAPCount);
    	        
    	        for(String capValue : capCountValuesList)
    	        {
    	        	if(capValue.startsWith("OPRE"))
    	        	{	
	    	        	String key = capValue.substring(0,4);
	    	        	int value = Integer.parseInt(capValue.substring(4));
	    	        	capCountValueMap.put(key, value);
    	        	}
    	        	else
    	        	{
    	        		String key = capValue.substring(0,2);
	    	        	int value = Integer.parseInt(capValue.substring(2));
	    	        	capCountValueMap.put(key, value);
    	        	}
    	        }
    	        
    	        debug("capCountValueMap "+capCountValueMap);
          	  	
    	        if(issueStatus.equals(issueStatusAwIssVal))
            	{
	          	  	if(capCountValueMap.get("OP")>=1 && (capCountValueMap.get("OPRE") + capCountValueMap.get("AV") + capCountValueMap.get("OP") + capCountValueMap.get("PE") + capCountValueMap.get("RM") + capCountValueMap.get("CA")) == childCAPCount)
	          	  	{
	        	        debug("Rule A");
	        	        
	        	        //Variables required to set Current Issue Due Date and Revised Issue Due Date When Status Changes to Open

	                	String currentIssueDueDateField = TriggerUtil.getAttributeValue(CURRENT_ISSUE_DUE_DATE_FIELD, getAttributes());
	                	
	                	Date currentIssueDueDateVal = ((IDateField) currentIssObj.getField(currentIssueDueDateField)).getValue();
	                	
	                	String revisedIssueDueDateField = TriggerUtil.getAttributeValue(REVISED_ISSUE_DUE_DATE_FIELD, getAttributes());

	                	String apDueDateField = TriggerUtil.getAttributeValue(AP_DUE_DATE_FIELD, getAttributes());

	                	String apRevisedDueDateField = TriggerUtil.getAttributeValue(AP_REVISED_DUE_DATE_FIELD, getAttributes());
	                	
	                	String remPlanFinalizedField = TriggerUtil.getAttributeValue(REMED_PLAN_FINALIZED_FIELD, getAttributes());
	                	
	                	String remPlanFinalizedVal = GRCObjectUtil.getFieldValue(currentIssObj, remPlanFinalizedField);
	                	
	                	String fireOnceField = TriggerUtil.getAttributeValue(FIRE_ONCE_FIELD, getAttributes());
	                	
	                	String fireOnceFieldVal = GRCObjectUtil.getFieldValue(currentIssObj, fireOnceField);
	                	
	          	  		commonResourceUtils.setFieldValueOnResource(issueStatusField, issueStatusOpenVal, currentIssObj, configurationService);
		          	  		
	          	  		if(!fireOnceFieldVal.contains("StatusOpen"))
	          	  		{
			              	List<IAssociationNode> childAPNodes = currentIssObjWithCAP.getChildren();
			              	  	
			              	IGRCObject childCAPObj = commonResourceUtils.getGRCObject(childAPNodes.get(0).getId(), servicesUtil.getServiceFactory());
		
			              	Date maxAPDueDate = ((IDateField) childCAPObj.getField(apDueDateField)).getValue();
			              	  	
			              	Date maxAPRevisedDueDate = ((IDateField) childCAPObj.getField(apRevisedDueDateField)).getValue();
			              	  	
			              	for(int i=1;i<childAPNodes.size();i++)
			              	{
			              		IGRCObject childCAP = commonResourceUtils.getGRCObject(childAPNodes.get(i).getId(), servicesUtil.getServiceFactory());
		
				              	Date apDueDate = ((IDateField) childCAP.getField(apDueDateField)).getValue();
				              	  	
				              	Date apRevisedDueDate = ((IDateField) childCAP.getField(apRevisedDueDateField)).getValue();
				              	  	
				              	if(apDueDate!=null)
				              	{
				              	  	if(maxAPDueDate==null)
				              	  		maxAPDueDate = apDueDate;
				              	  	else
				              	  	{
				              	  		if(apDueDate.after(maxAPDueDate))
						              	  	maxAPDueDate = apDueDate;
				              	  	}
				              	}
				              	  	
				              	if(apRevisedDueDate!=null)
				              	{
				              		if(maxAPRevisedDueDate==null)
				              			maxAPRevisedDueDate = apRevisedDueDate;
				              	  	else
				              	  	{
				              	  		if(apRevisedDueDate.after(maxAPRevisedDueDate))
				              	  			maxAPRevisedDueDate = apRevisedDueDate;
				              	  	}		
				              	}
			              	}
		              	  	
		              	
		              		if(maxAPDueDate!=null && !DateUtil.isSameDay(currentIssueDueDateVal, maxAPDueDate))
		              	  	{
		        	        	commonResourceUtils.setFieldValueOnResource(currentIssueDueDateField, maxAPDueDate, currentIssObj, configurationService);
		        	        	if(maxAPRevisedDueDate!=null && maxAPRevisedDueDate.after(maxAPDueDate) && remPlanFinalizedVal.equalsIgnoreCase("Yes") )
		        	          		commonResourceUtils.setFieldValueOnResource(revisedIssueDueDateField, maxAPRevisedDueDate, currentIssObj, configurationService);
		        	          	else
		        	          		((IDateField) currentIssObj.getField(revisedIssueDueDateField)).setValue(null);
		              	  	}
		              		
		              		if(CommonUtil.isNotNullOrEmpty(fireOnceFieldVal))
		              			commonResourceUtils.setFieldValueOnResource(fireOnceField, "StatusOpen " + fireOnceFieldVal, currentIssObj, configurationService);
		              		else
		              			commonResourceUtils.setFieldValueOnResource(fireOnceField, fireOnceFieldVal, currentIssObj, configurationService);
		              	}
	          	  	}
            	}
            	if(issueStatus.equals(issueStatusOpenVal))
            	{
	          	  	if(capCountValueMap.get("AV")>=1 && (capCountValueMap.get("AV") + capCountValueMap.get("PE") + capCountValueMap.get("RM") + capCountValueMap.get("CA")) == childCAPCount)
	          	  	{
	        	        debug("Rule B");
	          	  		commonResourceUtils.setFieldValueOnResource(issueStatusField, issueStatusAwIssVal, currentIssObj, configurationService);
	        	        setFieldsRelatedToAwStatus(currentIssObj);
	          	  	}
	          	  	else if(childCAPCount>0 && capCountValueMap.get("RM") + capCountValueMap.get("CA") == childCAPCount)
	          	  	{
	        	        debug("Rule D");
	        	        commonResourceUtils.setFieldValueOnResource(issueStatusField, issueStatusAwIssVal, currentIssObj, configurationService);
	        	        setFieldsRelatedToAwStatus(currentIssObj);
	        	  	}
            	}  	
				servicesUtil.getResourceService().saveResource(currentIssObj);
        	}
		}
        
		catch (Exception e) {
			// TODO Auto-generated catch block
			debug("Exception Message "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		debug("END Issue Status Rules Trigger Handler");

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
	
	 private void setFieldsRelatedToAwStatus(IGRCObject currentIssObj) throws Exception
	 {
		 
     	//Variables Required When Issue Changes to Awaiting Validation
     	String dateChangedToAwStatusField = TriggerUtil.getAttributeValue(DATE_CHANGED_TO_AW_STATUS_FIELD, getAttributes());  

     	String dateChangedToAwStatusFieldValue = GRCObjectUtil.getFieldValue(currentIssObj, dateChangedToAwStatusField);
     	
      	String daysInAwStatusField = TriggerUtil.getAttributeValue(DAYS_IN_AW_STATUS_FIELD, getAttributes());  

     	String iaIssueValidationDtField = TriggerUtil.getAttributeValue(IA_ISSUE_VAL_DATE_FIELD, getAttributes());  
     	
     	String iaIssueValidationDaysRemField = TriggerUtil.getAttributeValue(IA_ISSUE_VAL_DAYS_REM_FIELD, getAttributes());  
     	
     	String issueLevelField = TriggerUtil.getAttributeValue(ISSUE_LEVEL_FIELD, getAttributes());
     	
     	String issueLevelVal = GRCObjectUtil.getFieldValue(currentIssObj, issueLevelField);
     	
     	//Registry Settings
        String overwriteDateChangedToAW = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(OVERWRITE_DATE_CHANGED_TO_AW_STATUS, getAttributes()), configProperties);

        String runDaysInAwStatusCalc = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(RUN_DAYS_IN_AW_STATUS_CALC, getAttributes()), configProperties);

        String runIAIssueValDateCalc = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(RUN_IA_ISSUE_VAL_DATE_CALC, getAttributes()), configProperties);

        String runIAIssueValDaysRemCalc = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(RUN_IA_ISSUE_VAL_DAYS_REM_CALC, getAttributes()), configProperties);

        String shortTimeFrame = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(SHORT_TIME_FRAME, getAttributes()), configProperties);

        String longTimeFrame = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LONG_TIME_FRAME, getAttributes()), configProperties);

        List<String> shortLevels = Arrays.asList(ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(SHORT_LEVELS, getAttributes()), configProperties).split(","));

        List<String> longLevels = Arrays.asList(ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LONG_LEVELS, getAttributes()), configProperties).split(","));

		if(overwriteDateChangedToAW.equalsIgnoreCase(CommonConstants.TRUE) || 
 				(CommonUtil.isNullOrEmpty(dateChangedToAwStatusFieldValue) && overwriteDateChangedToAW.equalsIgnoreCase(CommonConstants.FALSE)))   
 		{
 		
 			IDateField dt1=(IDateField) currentIssObj.getField(dateChangedToAwStatusField);
 			Date date = new Date();
 			dt1.setValue(date);
 				
 			if(CommonUtil.isNotNullOrEmpty(runDaysInAwStatusCalc) && runDaysInAwStatusCalc.equalsIgnoreCase(CommonConstants.TRUE))
       		commonResourceUtils.setFieldValueOnResource(daysInAwStatusField, 0, currentIssObj, configurationService);
       	  		
       		if(CommonUtil.isNotNullOrEmpty(issueLevelVal) && shortLevels.contains(issueLevelVal))
       		{
     			IDateField iaIssueDateField =(IDateField) currentIssObj.getField(iaIssueValidationDtField);
     			Date iaIssueDate = DateUtil.getNthDate(new Date(), Double.valueOf(shortTimeFrame.toString()).intValue(), logger);
     			
     			if(CommonUtil.isNotNullOrEmpty(runIAIssueValDateCalc) && runIAIssueValDateCalc.equalsIgnoreCase(CommonConstants.TRUE))
     				iaIssueDateField.setValue(iaIssueDate);
     			
     			if(CommonUtil.isNotNullOrEmpty(runIAIssueValDaysRemCalc) && runIAIssueValDaysRemCalc.equalsIgnoreCase(CommonConstants.TRUE))
           	  		commonResourceUtils.setFieldValueOnResource(iaIssueValidationDaysRemField, shortTimeFrame, currentIssObj, configurationService);
       	  	}
       	  	else if(CommonUtil.isNotNullOrEmpty(issueLevelVal) && longLevels.contains(issueLevelVal))
       	  	{
           		IDateField iaIssueDateField =(IDateField) currentIssObj.getField(iaIssueValidationDtField);
     			Date iaIssueDate = DateUtil.getNthDate(new Date(), Double.valueOf(longTimeFrame.toString()).intValue(), logger);
     			
     			if(CommonUtil.isNotNullOrEmpty(runIAIssueValDateCalc) && runIAIssueValDateCalc.equalsIgnoreCase(CommonConstants.TRUE))
     				iaIssueDateField.setValue(iaIssueDate);	     
     			
     			if(CommonUtil.isNotNullOrEmpty(runIAIssueValDaysRemCalc) && runIAIssueValDaysRemCalc.equalsIgnoreCase(CommonConstants.TRUE))
     				commonResourceUtils.setFieldValueOnResource(iaIssueValidationDaysRemField, longTimeFrame, currentIssObj, configurationService);
       	  	}
       	  	else
       	  	{
           		IDateField iaIssueDateField =(IDateField) currentIssObj.getField(iaIssueValidationDtField);
     			Date iaIssueDate = new Date();
     			
     			if(CommonUtil.isNotNullOrEmpty(runIAIssueValDateCalc) && runIAIssueValDateCalc.equalsIgnoreCase(CommonConstants.TRUE))
     				iaIssueDateField.setValue(iaIssueDate);	     
     				
     			if(CommonUtil.isNotNullOrEmpty(runIAIssueValDaysRemCalc) && runIAIssueValDaysRemCalc.equalsIgnoreCase(CommonConstants.TRUE))
     				commonResourceUtils.setFieldValueOnResource(iaIssueValidationDaysRemField, 0, currentIssObj, configurationService);	
       	  	}
 				
 		}
	} 
	 
    private void debug(String string)
    {

        LoggerUtil.debugEXTLog(logger, "", "", string);
    }
    
}

