package com.ibm.openpages.ext.citi.triggers.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.ibm.openpages.ext.common.util.CommonConstants;
import com.ibm.openpages.ext.common.util.CommonUtil;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;

public class ActionPlanDependancyFlagHandler extends DefaultEventHandler{
	
	private static final String AP_DUE_DATE_FIELD = "ap.due.date.field";
    
    private static final String AP_REVISED_DUE_DATE_FIELD = "ap.revised.due.date.field";
    
    private static final String AP_FOLLOW_UP_DATE_FIELD = "ap.follow.up.date.field";
    
    private static final String AP_FIELD_DEPENDACNY_FLAG = "ap.field.dependancy.flag.field";
    
    private static final String ISSUE_STATUS_FIELD = "issue.status.field";

    private static final String ISSUE_STATUS_VALUES = "issue.status.values";
	
	private ServicesUtil servicesUtil = null;
	
	private IConfigProperties configProperties = null;
	
    private Logger logger = null;

    private static final String ENABLE_DEBUG = "enable.debug.mode";

    private static final String LOG_FILE_PATH = "log.file.path";
    
    private static final String DISABLE_TRIGGER = "disable.trigger";
    
	@Override
	public boolean handleEvent(UpdateResourceEvent event) {
		
		servicesUtil = new ServicesUtil(event.getContext());
		
		configProperties = servicesUtil.getConfigProperties();
				
		try {
			
				String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), configProperties);
		        
		        String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), configProperties);

		        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);      
		        
		        debug("-----------------------Entered Action Plan Field Dependancy Handler-----------------------");
		
				if (isTriggerDisabled()) 
				{ 
					debug("Trigger disabled => exiting handler");
					return true; 
				}
				
	        	String apDueDateField = TriggerUtil.getAttributeValue(AP_DUE_DATE_FIELD, getAttributes());

				String apRevisedDueDateField = TriggerUtil.getAttributeValue(AP_REVISED_DUE_DATE_FIELD, getAttributes());
	        	
	        	String apFollowUpDateField = TriggerUtil.getAttributeValue(AP_FOLLOW_UP_DATE_FIELD, getAttributes());
	        	
	        	String apFieldDependancyFlag = TriggerUtil.getAttributeValue(AP_FIELD_DEPENDACNY_FLAG, getAttributes());
	        	
	        	String issStatusField = TriggerUtil.getAttributeValue(ISSUE_STATUS_FIELD, getAttributes());

	        	String issueStatusValRegistry = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ISSUE_STATUS_VALUES, getAttributes()), configProperties);
	        	
	        	List<String> issueStatusValues = Arrays.asList(issueStatusValRegistry.split(","));
	        	
	        	IGRCObject apObj = (IGRCObject) event.getResource();
	        	
	        	String issueStatuVal = GRCObjectUtil.getFieldValue(apObj, issStatusField);
	        	
		        debug("AP Object "+ apObj.getName());
		        
		        debug("Issue Status "+ issueStatuVal);
	        	
	        	if(CommonUtil.isNotNullOrEmpty(issueStatuVal) && !issueStatusValues.contains(issueStatuVal))
	        	{
		        	String apDueDateVal = GRCObjectUtil.getFieldValue(apObj, apDueDateField);
		        	
		        	String apRevisedDueDateVal = GRCObjectUtil.getFieldValue(apObj, apRevisedDueDateField);

		        	String apFollowUpDateVal = GRCObjectUtil.getFieldValue(apObj, apFollowUpDateField);

		        	List<String> apFieldDependancyValues = new ArrayList<String>();

		        	if(CommonUtil.isNotNullOrEmpty(apDueDateVal))
		        	{
				        debug("AP Due Date is not empty ");
		        		apFieldDependancyValues.add("AP Due Date Set");
		        	}
		        	
		        	if(CommonUtil.isNotNullOrEmpty(apRevisedDueDateVal))
		        	{
				        debug("AP Revised Due Date is not empty");
		        		apFieldDependancyValues.add("AP Rev Due Date Set");
		        	}
		        	
		        	if(CommonUtil.isNotNullOrEmpty(apFollowUpDateVal))
		        	{
				        debug("AP Follow Up Date is not empty");
		        		apFieldDependancyValues.add("AP Follow Up Date Set");
		        	}
		        	
		        	if(CommonUtil.isListNotNullOrEmpty(apFieldDependancyValues))
		        	{
		        		ResourceUtil.setFieldValue(apObj.getField(apFieldDependancyFlag), apFieldDependancyValues.toArray(new String[apFieldDependancyValues.size()]));
		        	}

	        	}
		}
        
		catch (Exception e) {
			// TODO Auto-generated catch block
			debug("Exception Message "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		debug("END Action Plan Field Dependancy Handler");

		return true;
	}
	
	 private boolean isTriggerDisabled()
	 {
		 String disableTrigger = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(DISABLE_TRIGGER, getAttributes()),servicesUtil.getConfigProperties()); 
		 
		 if (CommonUtil.isNotNullOrEmpty(disableTrigger) && disableTrigger.equalsIgnoreCase(CommonConstants.TRUE))
			return true;
		else
			return false;
	 }
	
	
	 private void debug(String string)
	 {

		 LoggerUtil.debugEXTLog(logger, "", "", string);
	 }

}
