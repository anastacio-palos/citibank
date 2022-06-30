package com.ibm.openpages.ext.common.util;

/*
 Licensed Materials - Property of IBM


 5725-D51, 5725-D52, 5725-D53, 5725-D54

 © Copyright IBM Corporation 2017. All Rights Reserved.

 US Government Users Restricted Rights- Use, duplication or disclosure restricted 
 by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 {
 "$schema":{"$ref":"TS_Taxonomy_vMay132009"},
 "author":"Nikhil Komakula",
 "customer":"IBM",
 "date":"11/20/2016, 12/8/2016",
 "summary":"Common",
 "technology":"java",
 "feature":"Common Utilities",
 "rt_num":""
 }
 */

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.ext.common.util.CommonUtil;
import com.openpages.sdk.repository.Resource;
import com.openpages.sdk.trigger.TriggerContext;
import com.openpages.sdk.trigger.object.TriggerConstants.ResourceTriggerConstants;

public class TriggerUtil {
	
	private static final String EMPTY_STRING = "";
	private static final String COMMON_ERROR_MSG_APP_TEXT = "com.openpages.triggers.error.message";
	
	/**
	 * <p>
	 * This method returns attribute value.
	 * </p>
	 * 
	 * @param attributeName Trigger Attribute Name
	 * @param map Attribute Map
	 * @return String Attribute value
	 */
	public static String getAttributeValue(String attributeName, Map<String, String> map) {
	    
	    if (null != map) {
	    	
	    	if (map.containsKey(attributeName)) {
	        
		    	String attributeValue = map.get(attributeName);
		    	
		        return attributeValue;
	    	}
	    	
	    	return EMPTY_STRING;
	    }
	    
	    return null;
	}
	
	/**
	 * <p>
	 * This method will return Id of the GRC Object Primary Parent for CREATE operation and PRE/POST position.
	 * </p>
	 * 
	 * @param triggerContext TriggerContext
	 * @return Id Id of the Primary Parent
	 */
	public static Id getPrimaryParentId(TriggerContext triggerContext) {
		
		Resource parentResource = (Resource) triggerContext.getContextAttribute(ResourceTriggerConstants.CONTEXT_ATTR_PRIMARY_PARENT);
		long id = parentResource.getResourceId().getId();			
		Id grcObjectPrimaryParentId = new Id(Long.toString(id));
		
		return grcObjectPrimaryParentId;
	}
	
	/**
	 * <p>
	 * This method will check if a field is modified.
	 * </p>
	 * 
	 * @param grcObject GRC Object whose field got updated
	 * @param field Field on the GRC Object in the format Field Group:Field Name
	 * @return boolean True, if a field is modified
	 * @throws Exception
	 */
	public static boolean isFieldModified(IGRCObject grcObject, String field) throws Exception {

        List<IField> modifiedFields = ResourceUtil.getModifiedFields(grcObject);
        
        if (CommonUtil.isObjectNull(modifiedFields))
        	return false;

        for (IField modifiedField : modifiedFields) {
        	
            if (modifiedField.getName().equals(field))            	
                return true;
        }
        
        return false;
    }
	
	/**
	 * <p>
	 * This method will return if a trigger is disabled or not.
	 * </p>
	 * 
	 * @param registrySetting Registry Setting Key 
	 * @param configProperties IConfigProperties
	 * @return boolean True if Trigger is disabled.
	 */
	public static boolean isTriggerDisabled(String registrySetting, IConfigProperties configProperties) {
		
		String disableTrigger = ApplicationUtil.getRegistrySetting(registrySetting, configProperties);
		
		if (CommonUtil.isNotNullOrEmpty(disableTrigger) && disableTrigger.equalsIgnoreCase(CommonConstants.TRUE))
			return true;
		else
			return false;
	}
	
	/**
	 * <p>
	 * This method will return the common error message.
	 * </p>
	 * 
	 * @param triggerName Name of the Trigger 
	 * @param servicesUtil Instance of ServicesUtil
	 * @return String returns common error message
	 */
	public static String getCommonTriggerErrorMsg(String triggerName, ServicesUtil servicesUtil) {
		
		String commonErrorMsg = ApplicationUtil.getApplicationTextWithParameters(COMMON_ERROR_MSG_APP_TEXT, new String[]{triggerName}, servicesUtil.getConfigurationService());
		
		return commonErrorMsg;
	}
	
	/**
	 * <p>
	 * This method disables triggers based on trigger paths.
	 * </p>
	 * 
	 * @param triggerDisablePaths List of Trigger Disable Paths
	 * @param context Context
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @param logger Instance of Logger
	 * @throws Exception
	 */
	public static void disableTriggers(List<String> triggerDisablePaths, Context context, ServicesUtil servicesUtil, Logger logger) throws Exception {
		
		final String METHOD_NAME = "disableTriggers";
		
		for (String triggerDisablePath: triggerDisablePaths) {
			
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "triggerDisablePath", triggerDisablePath);
			
			String isTriggerDisabled = ApplicationUtil.getRegistrySetting(triggerDisablePath, servicesUtil.getConfigProperties());
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "isTriggerDisabled BEFORE DISABLE", isTriggerDisabled);
			
			ApplicationUtil.setRegistrySetting(triggerDisablePath, CommonConstants.TRUE, context);
			
			isTriggerDisabled = ApplicationUtil.getRegistrySetting(triggerDisablePath, servicesUtil.getConfigProperties());
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "isTriggerDisabled AFTER DISABLE", isTriggerDisabled);
		}
	}
	
	/**
	 * <p>
	 * This method enables triggers based on trigger paths.
	 * </p>
	 * 
	 * @param triggerDisablePaths List of Trigger Disable Paths
	 * @param context Context
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @param logger Instance of Logger
	 * @throws Exception
	 */
	public static void enableTriggers(List<String> triggerDisablePaths, Context context, ServicesUtil servicesUtil, Logger logger) throws Exception {
		
		final String METHOD_NAME = "enableTriggers";
		
		for (String triggerDisablePath: triggerDisablePaths) {
			
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "triggerDisablePath", triggerDisablePath);
			
			String isTriggerDisabled = ApplicationUtil.getRegistrySetting(triggerDisablePath, servicesUtil.getConfigProperties());
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "isTriggerDisabled BEFORE DISABLE", isTriggerDisabled);
			
			ApplicationUtil.setRegistrySetting(triggerDisablePath, CommonConstants.FALSE, context);
			
			isTriggerDisabled = ApplicationUtil.getRegistrySetting(triggerDisablePath, servicesUtil.getConfigProperties());
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "isTriggerDisabled AFTER DISABLE", isTriggerDisabled);
		}	
	}
}