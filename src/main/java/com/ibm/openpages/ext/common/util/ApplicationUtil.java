package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.EMPTY_STRING;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.service.IConfigurationService;
import com.openpages.apps.common.util.HttpContext;
import com.openpages.sdk.OpenpagesSession;
import com.openpages.sdk.admin.AdminService;
import com.openpages.sdk.admin.objectprofile.ObjectProfile;
import com.openpages.sdk.admin.objectprofile.ProfileFieldId;
import com.openpages.sdk.admin.objectprofile.ProfileObjectType;
import com.openpages.sdk.admin.objectprofile.ProfileObjectTypeId;
import com.openpages.sdk.admin.objectprofile.PropertyTypeField;
import com.openpages.sdk.metadata.MetaDataService;
import com.openpages.sdk.metadata.PropertyType;
import com.openpages.sdk.metadata.PropertyTypeId;
import com.openpages.sdk.security.ActorId;

public class ApplicationUtil {
	
	@SuppressWarnings("unused")
	private static final String CLASS_NAME = "ApplicationUtil";
	
	/**
	 * <p>
	 * This method returns the Registry Setting Value.
	 * <P>
	 * 
	 * @param registrySetting Registry Setting Key
	 * @param configProperties IConfigProperties
	 * @return String Registry Setting Value
	 */
	public static String getRegistrySetting(String registrySetting, IConfigProperties configProperties) {
		
		try {
		
			String registry = configProperties.getProperty(registrySetting);
		
			if (CommonUtil.isNullOrEmpty(registry))
				registry = EMPTY_STRING;
			
			return registry;
		}
		catch (Exception e) {
			
			return registrySetting;
		}
	}
	
	/**
	 * <p>
	 * This method sets the Registry Setting to the provided value.
	 * <P>
	 * 
	 * @param registrySetting Registry Setting Key
	 * @param registryValue Registry Setting Value
	 * @param request HttpServletRequest
	 * @throws Exception
	 */
	public static void setRegistrySetting(String registrySetting, String registryValue, HttpServletRequest request) throws Exception {
		
		AdminService adminService = HttpContext.getAdminService(request);
		adminService.setEntryValue(registrySetting, registryValue);
	}
	
	/**
	 * <p>
	 * This method sets the Registry Setting to the provided value.
	 * <P>
	 * 
	 * @param registrySetting Registry Setting Key
	 * @param registryValue Registry Setting Value
	 * @param opSession OpenpagesSession
	 * @throws Exception
	 */
	public static void setRegistrySetting(String registrySetting, String registryValue, OpenpagesSession opSession) throws Exception {
		
		AdminService adminService = opSession.getAdminService();
		adminService.setEntryValue(registrySetting, registryValue);
	}
	
	/**
	 * <p>
	 * This method sets the Registry Setting to the provided value.
	 * <P>
	 * 
	 * @param registrySetting Registry Setting Key
	 * @param registryValue Registry Setting Value
	 * @param context Context
	 * @throws Exception
	 */
	public static void setRegistrySetting(String registrySetting, String registryValue, Context context) throws Exception {
		
		OpenpagesSession session = SecurityUtil.getOPSessionFromContext(context);
		setRegistrySetting(registrySetting, registryValue, session);
	}
	
	/**
	 * <p>
	 * This method returns the Application Text Key value.
	 * <P>
	 * 
	 * @param key Application Text Key
	 * @param configurationService IConfigurationService
	 * @return String Application Text Value
	 */
	public static String getApplicationText(String key, IConfigurationService configurationService) {
		
		try {
			return configurationService.getLocalizedApplicationText(key);	
		}
		catch (Exception e) {
			return key;
		}				
	}
	
	/**
	 * <p>
	 * This method returns the Application Text Key value provided parameters.
	 * <P>
	 * 
	 * @param key Application Text Key
	 * @param parameters Array of Parameters
	 * @param configurationService IConfigurationService
	 * @return String Application Text Value with Parameters
	 */
	public static String getApplicationTextWithParameters(String key, String[] parameters, IConfigurationService configurationService) {
		
		String value = getApplicationText(key, configurationService);
		
		if (CommonUtil.isNullOrEmpty(value))
			return value;
		
		if (CommonUtil.isObjectNotNull(parameters)) {
			
			MessageFormat messageFormat = new MessageFormat(value);
			value = messageFormat.format(parameters);
		}
		
		return value;		
	}
    
    /**
	 * <p>
	 * This method returns Profile object type Id required for User/Group Selector widget.
	 * </p>
	 * @param objectType 
	 * 			Object Type name on which the user/group selector exists
	 * @param opSession 
	 * 			Instance of OpenpagesSession
	 * @param logger 
	 * 			Instance of Logger
	 * @return ProfileObjectTypeId 
	 * 			Profile Object Type Id
	 * @throws Exception
	 */	
    public static ProfileObjectTypeId getProfileObjectTypeId(String objectType, OpenpagesSession opSession, Logger logger) throws Exception {  
    	
    	final String METHOD_NAME = "getProfileObjectTypeId";

    	ProfileObjectType profileObjectType = getProfileObjectType(objectType, opSession, logger);        
    	ProfileObjectTypeId profileObjectTypeId = profileObjectType.getObjectTypeId();
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "profileObjectTypeId", profileObjectTypeId);
        
        return profileObjectTypeId;
    }
    
    /**
	 * <p>
	 * This method returns Profile Field Id required for User/Group Selector widget.
	 * </p>
	 * @param userOrGroupSelectorField 
	 * 			User or Group Selector field in the format FieldGroup.FieldName
	 * @param objectType 
	 * 			Object Type name on which the user/group selector exists
	 * @param opSession 
	 * 			Instance of OpenpagesSession
	 * @param logger 
	 * 			Instance of Logger
	 * @return ProfileFieldId 
	 * 			Instance of Profile Field Id
	 * @throws Exception
	 */	
    public static ProfileFieldId getProfileFieldId(String userOrGroupSelectorField, String objectType, OpenpagesSession opSession, Logger logger) throws Exception {
    	
    	final String METHOD_NAME = "getProfileFieldId";
    	
    	 ProfileFieldId profileFieldId = null;
    	 ProfileObjectType profileObjectType = getProfileObjectType(objectType, opSession, logger);
         List<PropertyTypeField> profileFields = profileObjectType.getFields();
         
         PropertyTypeId propertyTypeId = getPropertyType(userOrGroupSelectorField, opSession.getMetaDataService()).getPropertyTypeId();         
         
         for (Iterator<PropertyTypeField> fieldItr = profileFields.iterator(); fieldItr.hasNext();)
         {
             PropertyTypeField ppType = (PropertyTypeField) fieldItr.next();
             if (ppType.getPropertyTypeId().equals(propertyTypeId))
             {
            	 profileFieldId = ppType.getFieldId();
             }
         }
         
         LoggerUtil.debugEXTLog(logger, METHOD_NAME, "profileFieldId", profileFieldId);         
         return profileFieldId;
    }
    
    /**
	 * <p>
	 * This method returns the Property Type of a Field.
	 * </p>
	 * @param bundleProperty 
	 * 			Field in the format FieldGroup.FieldName
	 * @param metaDataService 
	 * 			Instance of MetaDataService
	 * @return String 
	 * 			Property Type of a Field
	 * @throws Exception
	 */
    public static PropertyType getPropertyType(String bundleProperty, MetaDataService metaDataService) throws Exception {

        if (bundleProperty.indexOf(".") == -1)
            throw new IllegalArgumentException("Invalid Bundle Property reference.");

        String[] bundlePropertyArray = bundleProperty.split("\\.");
        
        return metaDataService.getPropertyType(metaDataService.getBundleType(bundlePropertyArray[0]).getBundleTypeId(), bundlePropertyArray[1]);
    }
    
    /**
	 * <p>
	 * This method returns Profile object type required for User/Group Selector widget.
	 * </p>
	 * @param objectType 
	 * 			Object Type name on which the user/group selector exists
	 * @param opSession 
	 * 			Instance of OpenpagesSession
	 * @param logger 
	 * 			Instance of Logger
	 * @return ProfileObjectType 
	 * 			Profile Object Type
	 * @throws Exception
	 */	
    public static ProfileObjectType getProfileObjectType(String objectType, OpenpagesSession opSession, Logger logger) throws Exception {
    	
    	final String METHOD_NAME = "getProfileObjectType";
    	
    	ActorId actorId = SecurityUtil.getActor(opSession, logger).getActorId();
    	ObjectProfile userProfile = SecurityUtil.getUserProfile(actorId, opSession, logger);
        ProfileObjectType profileObjectType = userProfile.getObjectType(opSession.getMetaDataService().getContentType(objectType).getContentTypeId());
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "profileObjectType", profileObjectType.getName());
        
        return profileObjectType;
    }

}
