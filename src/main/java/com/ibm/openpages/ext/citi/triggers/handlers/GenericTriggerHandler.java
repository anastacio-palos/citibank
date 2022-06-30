package com.ibm.openpages.ext.citi.triggers.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.Logger;

import static com.ibm.openpages.ext.common.util.CommonConstants.COMMA_SEPERATED_DELIMITER;
import static com.ibm.openpages.ext.common.util.CommonConstants.EMPTY_STRING;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.security.IGroup;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.trigger.events.AssociateResourceEvent;
import com.ibm.openpages.api.trigger.events.CopyResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.ibm.openpages.ext.common.util.CommonUtil;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;

public class GenericTriggerHandler extends DefaultEventHandler{

	private static final String CHILD_OBJECT_TYPES = "child.object.types";
	
	private static final String CHILD_EDIT_LIST_OBJECT_TYPES = "child.edit.list.object.types";
	
	private static final String APPR_STAGE_FIELD = "appr.stage.field";
	
	private static final String ANCS_APPR_STAGE_FIELD = "ancs.appr.stage.field";
	
	private static final String APPR_STAGE_COMPARE_VALUE = "n/a";
	
	private static final String EDIT_USER_LIST_FIELD = "edit.user.list.field";
	
	private static final String APPR_USER_FIELD = "appr.user.field";
	
	private static final String GROUP_NAMES = "group.names";

	private static final String ASSOCIATE_ERROR_MSG = "app.text.error.cannot.associate";
	
	private static final String COPY_ERROR_MSG = "app.text.error.cannot.copy";
	
	private ServicesUtil servicesUtil = null;
	
	private ISecurityService securityService = null;

    private static Logger logger = null;

    private static final String ENABLE_DEBUG = "enable.debug.mode";

    private static final String LOG_FILE_PATH = "log.file.path";
    
	@Override
	public boolean handleEvent(AssociateResourceEvent event) {
		
		servicesUtil = new ServicesUtil(event.getContext());
		
		securityService = servicesUtil.getSecurityService();
		
		boolean isActionAllowed = false;
		
		IGRCObject childObj = null;

		IGRCObject parentObj = null;

		try {
			
	        String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), servicesUtil.getConfigProperties());
	        
	        String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());

	        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);
	        
	        debug("-----------------------Entered Generic Association Trigger handler-----------------------");
	        
	        String childObjectTypes = TriggerUtil.getAttributeValue(CHILD_OBJECT_TYPES, getAttributes());
	        
	        List<String> childObjectTypeList = CommonUtil.parseDelimitedValues(childObjectTypes, COMMA_SEPERATED_DELIMITER);
	        
	        String inValidAssociateErrorMsg = ApplicationUtil.getApplicationText(TriggerUtil.getAttributeValue(ASSOCIATE_ERROR_MSG, getAttributes()), servicesUtil.getConfigurationService());
	        
	        String childEditListObjectTypes = TriggerUtil.getAttributeValue(CHILD_EDIT_LIST_OBJECT_TYPES, getAttributes());
	        
	        List<Id> associatingParents = event.getParents();
	        
	        List<Id> associatingChildren = event.getChildren();

	        for(Id parentId : associatingParents)
	        {
	        	for(Id childId : associatingChildren)
	        	{
	        		childObj = servicesUtil.getResourceService().getGRCObject(childId);
	        		parentObj = servicesUtil.getResourceService().getGRCObject(parentId);
	    	              
	    	        if(!childObjectTypeList.contains(childObj.getType().getName()))
	    	        {
		    	        debug(childObj.getType().getName() + " is not in the given object types " + childObjectTypeList);
	    	        	return true;
	    	        }
	    	        
	    	        debug("Trying to Associate child "+ childObj.getName() +" to parent "+ parentObj.getName());
	    	        
	    	        if(CommonUtil.isNotNullOrEmpty(childEditListObjectTypes))
	    	        {
	    		        List<String> childEditListObjectTypeList = CommonUtil.parseDelimitedValues(childEditListObjectTypes, COMMA_SEPERATED_DELIMITER);
	    		        if(childEditListObjectTypeList.contains(childObj.getType().getName()))
		    	        	isActionAllowed = performLogic(childObj,inValidAssociateErrorMsg,event.getContext());
	    		        else
		    	        	isActionAllowed = performLogic(parentObj,inValidAssociateErrorMsg,event.getContext());
	    	        }
	    	        else
	    	        {
	    	        	isActionAllowed = performLogic(parentObj,inValidAssociateErrorMsg,event.getContext());
	    	        }
	    	        
	    	        debug("Associated child "+ childObj.getName() +" to parent "+ parentObj.getName());
	    	        
	        	}
	        }
		}
        
		catch (Exception e) {
			// TODO Auto-generated catch block
        	throwException(e.getMessage(),new ArrayList<Object>(), new Exception(), event.getContext());
			e.printStackTrace();
		}
		
		debug("END Generic Association Trigger Handler");
        return isActionAllowed;
	}
	
	

	@Override
	public boolean handleEvent(CopyResourceEvent event) {
		
		servicesUtil = new ServicesUtil(event.getContext());
		
		securityService = servicesUtil.getSecurityService();
		
		boolean isActionAllowed = false;
		
		IGRCObject parentObj = null;

		IGRCObject childObj = null;

		try {
			
			String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), servicesUtil.getConfigProperties());
	        
	        String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());

	        String childObjectTypes = TriggerUtil.getAttributeValue(CHILD_OBJECT_TYPES, getAttributes());
	        
	        List<String> childObjectTypeList = CommonUtil.parseDelimitedValues(childObjectTypes, COMMA_SEPERATED_DELIMITER);
	        
	        String inValidCopyErrorMsg = ApplicationUtil.getApplicationText(TriggerUtil.getAttributeValue(COPY_ERROR_MSG, getAttributes()), servicesUtil.getConfigurationService());

	        String childEditListObjectTypes = TriggerUtil.getAttributeValue(CHILD_EDIT_LIST_OBJECT_TYPES, getAttributes());

	        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);
	        
	        debug("-----------------------Entered Generic Copy Trigger handler-----------------------");
	        
	        parentObj = servicesUtil.getResourceService().getGRCObject(event.getTargetResourceId());

	        childObj = servicesUtil.getResourceService().getGRCObject(event.getSourceResouceId());
	        
	        if(!childObjectTypeList.contains(childObj.getType().getName()))
	        {
    	        debug(childObj.getType().getName() + " is not in the given object types " + childObjectTypeList);
	        	return true;
	        }
			
	        debug("Trying to Copy child "+ childObj.getName() +" to parent "+ parentObj.getName());
	        
	        if(CommonUtil.isNotNullOrEmpty(childEditListObjectTypes))
	        {
		        List<String> childEditListObjectTypeList = CommonUtil.parseDelimitedValues(childEditListObjectTypes, COMMA_SEPERATED_DELIMITER);
		        if(childEditListObjectTypeList.contains(childObj.getType().getName()))
			        isActionAllowed = performLogic(childObj,inValidCopyErrorMsg,event.getContext());
		        else
			        isActionAllowed = performLogic(parentObj,inValidCopyErrorMsg,event.getContext());
	        }
	        else
	        {
		        isActionAllowed = performLogic(parentObj,inValidCopyErrorMsg,event.getContext());
	        }
	        
	        debug("Copied child "+ childObj.getName() +" to parent "+ parentObj.getName());
		}
	
		catch (Exception e) {
			// TODO Auto-generated catch block
        	throwException(e.getMessage(),new ArrayList<Object>(), new Exception(), event.getContext());
			e.printStackTrace();
		}
		
		debug("END Generic Copy Trigger Handler");
        return isActionAllowed;
	}
		
    private void debug(String string)
    {

        LoggerUtil.debugEXTLog(logger, "", "", string);
    }
    
    private boolean performLogic(IGRCObject grcObj,String errorMsg,Context context) throws Exception
    {
    
    	String apprStageField = TriggerUtil.getAttributeValue(APPR_STAGE_FIELD, getAttributes());

        String ancsApprStageField = TriggerUtil.getAttributeValue(ANCS_APPR_STAGE_FIELD, getAttributes());

        String editUserListField = TriggerUtil.getAttributeValue(EDIT_USER_LIST_FIELD, getAttributes());

        String apprUserField = TriggerUtil.getAttributeValue(APPR_USER_FIELD, getAttributes());
        
        String groupNames = TriggerUtil.getAttributeValue(GROUP_NAMES, getAttributes());
        
        List<String> userGroupList = CommonUtil.parseDelimitedValues(groupNames, COMMA_SEPERATED_DELIMITER);
        
        debug("userGroups " + userGroupList);

        IUser endUser = securityService.getCurrentUser();
        
        debug("endUser " + endUser.getName());
        
        List<String> editUserList = getUsersInFieldAsList(editUserListField, grcObj);
        
        debug("editUserList " + editUserList);
        
        String apprUser = GRCObjectUtil.getFieldValue(grcObj, apprUserField);
        
        debug("apprUser " + apprUser);
        
        String apprStage = GRCObjectUtil.getFieldValue(grcObj, apprStageField);
        
        debug("apprStage " + apprStage);
        
        boolean isUserInGroups = checkUserInGivenGroups(userGroupList,endUser);
        
        if(isUserInGroups)
        {
            debug("isUserInGroups " + isUserInGroups);
        	return true;
        }
        else 
        {
        	if(CommonUtil.isNotNullOrEmpty(ancsApprStageField))
        	{
        		String ancsApprStage = GRCObjectUtil.getFieldValue(grcObj, ancsApprStageField);
        		
    	        debug("ancsApprStage " + ancsApprStage);
        		
        		if( apprStage.equals(APPR_STAGE_COMPARE_VALUE) && ancsApprStage.equals(APPR_STAGE_COMPARE_VALUE) && CommonUtil.isListNotNullOrEmpty(editUserList) && editUserList.contains(endUser.getName()) )
        			return true;
        		else if( ((!apprStage.equals(APPR_STAGE_COMPARE_VALUE)) || (!ancsApprStage.equals(APPR_STAGE_COMPARE_VALUE))) && CommonUtil.isNotNullOrEmpty(apprUser) && apprUser.equals(endUser.getName()) )
        			return true;
        		else
                	throwException(errorMsg,new ArrayList<Object>(), new Exception(), context);
        	}
        	else
        	{
        		if( apprStage.equals(APPR_STAGE_COMPARE_VALUE) && CommonUtil.isListNotNullOrEmpty(editUserList) && editUserList.contains(endUser.getName()) )
        			return true;
        		else if( CommonUtil.isNotNullOrEmpty(apprUser) && apprUser.equals(endUser.getName()) && !apprStage.equals(APPR_STAGE_COMPARE_VALUE) )
        			return true;
        		else
                	throwException(errorMsg,new ArrayList<Object>(), new Exception(), context);
        	}
        }
        
        return true;
    }
    
    private boolean checkUserInGivenGroups(List<String> userGroups, IUser endUser)
    {
    	for(String groupName : userGroups)
    	{
    		IGroup userGroup = securityService.getGroup(groupName);
    		
    		if(userGroup.hasMember(endUser))
    			return true;
    	}
    	
    	return false;
    }
    
    private List<String> getUsersInFieldAsList(String editUserListField, IGRCObject parentObj) throws Exception
    {
    	
        String users = GRCObjectUtil.getFieldValue(parentObj, editUserListField);
        
        List<String> usersList = new ArrayList<String>();
        
        if(CommonUtil.isNullOrEmpty(users))
        	return usersList;

		users = users.replaceAll("\\$;", ",");
		users = users.replaceAll(";", ",");
        users = users.replaceAll("$", ",");
        		
        usersList = CommonUtil.parseDelimitedValues(users,COMMA_SEPERATED_DELIMITER);
        
        while(usersList.contains(EMPTY_STRING))
        	usersList.remove(EMPTY_STRING);
        
    	return usersList;
    }
}