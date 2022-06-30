package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.ADMIN_PASSWORD_REGISTRY_SETTING;
import static com.ibm.openpages.ext.common.util.CommonConstants.ADMIN_USERNAME_REGISTRY_SETTING;
import static com.ibm.openpages.ext.common.util.CommonConstants.COMMA_SEPERATED_DELIMITER;
import static com.ibm.openpages.ext.common.util.LoggerUtil.debugEXTLog;
import static com.ibm.openpages.ext.common.util.LoggerUtil.errorEXTLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.security.IGroup;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.ServiceFactory;
import com.openpages.sdk.OpenpagesSession;
import com.openpages.sdk.admin.objectprofile.ObjectProfile;
import com.openpages.sdk.security.Actor;
import com.openpages.sdk.security.ActorId;
import com.openpages.sdk.security.SecurityService;

public class SecurityUtil {
	
	private static final String CLASS_NAME = "SecurityUtil";
	
	/**
	 * <p>
	 * This method will get the Administrator Credentials from the Registry settings.
	 * </p>
	 * 
	 * @param configProperties IConfigProperties
	 * @param logger Instance of Logger
	 * @return String[] Array of Administrator Credentials
	 */
	public static String[] getAdminUserCredentials(IConfigProperties configProperties, Logger logger) {
		
		final String METHOD_NAME = CLASS_NAME + ":" + "getAdminUserCredentials";
		
		String adminUserName = ApplicationUtil.getRegistrySetting(ADMIN_USERNAME_REGISTRY_SETTING, configProperties);
		//LoggerUtil.debugEXTLog(logger, METHOD_NAME, "adminUserName", adminUserName);
		
		String adminPassword = ApplicationUtil.getRegistrySetting(ADMIN_PASSWORD_REGISTRY_SETTING, configProperties);
		//LoggerUtil.debugEXTLog(logger, METHOD_NAME, "adminPassword", adminPassword);
		
		if (CommonUtil.isNotNullOrEmpty(adminUserName) && CommonUtil.isNotNullOrEmpty(adminPassword)) {
			
			return new String[]{adminUserName, adminPassword};
		}
		
		return null;
	}
	
	/**
	 * <p>
	 * This method will return the Admin Context based on the Administrator Credentials specified in the Registry Settings.
	 * </p>
	 * 
	 * @param loggedInUsersContext Logged-In Users Context.
	 * @param logger Instance of Logger
	 * @return Context
	 * @throws Exception
	 */
	public static Context getAdminContext(Context loggedInUsersContext, Logger logger) throws Exception {
		
		final String METHOD_NAME = CLASS_NAME + ":" + "getAdminContext";
		
		Object adminContextObject = loggedInUsersContext.get(CommonConstants.ADMIN_CONTEXT_KEY);
		
		boolean doesAdminSessionAlreadyExists = CommonUtil.isObjectNotNull(adminContextObject);		
		LoggerUtil.debugEXTLog(logger, METHOD_NAME, "doesAdminSessionAlreadyExists", doesAdminSessionAlreadyExists);
		
		if (!doesAdminSessionAlreadyExists) {
		
			IServiceFactory serviceFactory = ServiceFactory.getServiceFactory(loggedInUsersContext);
			IConfigProperties configProperties = serviceFactory.createConfigurationService().getConfigProperties();
			String loggedInUserName = getLoggedInUserName(loggedInUsersContext);
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "loggedInUserName", loggedInUserName);
			
			String[] adminUserCredentials = getAdminUserCredentials(configProperties, logger);
			
			if (CommonUtil.isObjectNotNull(adminUserCredentials)) {
				
				if (loggedInUserName.equalsIgnoreCase(adminUserCredentials[0])) {
					
					LoggerUtil.debugEXTLog(logger, METHOD_NAME, "loggedInUserName", "Logged-In User is same as the Administrator.");
					return loggedInUsersContext;
				}
				
				Context adminContext = new Context();
				
				adminContext.put(Context.SERVICE_USER_NAME, adminUserCredentials[0]);
				adminContext.put(Context.SERVICE_USER_PASSWORD, adminUserCredentials[1]);
				
				loggedInUsersContext.put(CommonConstants.ADMIN_CONTEXT_KEY, adminContext);
				
				return adminContext;
			}
			else {
				
				throw new Exception("Administrator User Credentials are not valid.");
			}
		}
		else {
			
			return (Context) adminContextObject;
		}
	}
	
	/**
	 * <p>
	 * This method will return the logged-in user's username.
	 * </p>
	 * 
	 * @param loggedInUsersContext Logged-In Users Context.
	 * @return String Logged-In User Username
	 */
	public static String getLoggedInUserName(Context loggedInUsersContext) {
		
		IServiceFactory serviceFactory = ServiceFactory.getServiceFactory(loggedInUsersContext);
		ISecurityService securityService = serviceFactory.createSecurityService();
		
		return securityService.getCurrentUser().getName();
	}
	
	/**
	 * <p>
	 * This method returns OpenpagesSession based on Context.
	 * </p>
	 * 
	 * @param context Context
	 * @return OpenpagesSession
	 */
	public static OpenpagesSession getOPSessionFromContext(Context context) {
		
		return (OpenpagesSession) context.get(Context.SERVICE_SESSION);		
	}
    
    /**
	 * <p>
	 * This method returns the formatted name of a user in the format "FirstName LastName [Username]".
	 * </p>
	 * 
	 * @param username Username
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @param logger Instance of Logger
	 * @return String Full Name of a User
	 */
    public static String getUserFormattedName(String username, ServicesUtil servicesUtil, Logger logger) {
    	
        final String METHOD_NAME = CLASS_NAME + ":" + "getUserFormattedName";
        
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "username", username);

        if (CommonUtil.isNotNullOrEmpty(username)) {
        	
            StringBuilder sb = new StringBuilder();
            
            IUser user = servicesUtil.getSecurityService().getUser(username);
            
            if (CommonUtil.isNotNullOrEmpty(user.getFirstName()))
                sb.append(user.getFirstName() + " ");
            if (CommonUtil.isNotNullOrEmpty(user.getLastName()))
                sb.append(user.getLastName() + " ");
            if (CommonUtil.isNotNullOrEmpty(user.getName()))
                sb.append("[" + user.getName() + "]");

            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "Formatted Name", sb.toString());
            
            return sb.toString();
        }        
        
        return username;
    }
    
    /**
	 * <p>
	 * This method returns a unique list of Users based on the users/usergroups passed as a comma-separated String.
	 * </p>
	 * @param actors comma-separated list of users or user groups 
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @param logger Instance of Logger
	 * @return List<IUser> List of Users
	 * @throws Exception
	 */	 
    public static List<IUser> getUsersAsListFromString(String actors, ServicesUtil servicesUtil, Logger logger) throws Exception {
    	
    	final String METHOD_NAME = CLASS_NAME + ":" + "getUsersAsListFromString";
    	
    	if(CommonUtil.isNullOrEmpty(actors))
            return Collections.emptyList();
    	
    	//actors may be a comma-separated list of users/groups/both users and groups. Get the list of all the actors       
        Set<String> actorsSet = new HashSet<String>(CommonUtil.parseDelimitedValues(actors, COMMA_SEPERATED_DELIMITER));
        debugEXTLog(logger, METHOD_NAME, "actorsSet", actorsSet);
        
        Set<IUser> usersSet = new HashSet<IUser>();
        IUser user = null;
        
        for (String actorName : actorsSet) {
         
        	debugEXTLog(logger, METHOD_NAME, "actorName", actorName);
        	
        	OpenpagesSession opSession = SecurityUtil.getOPSessionFromContext(servicesUtil.getContext());
        	SecurityService securityService = opSession.getSecurityService();
        	Actor actor = securityService.getActor(actorName);
        	
        	if (actor.isGroup()) {
        		
        		debugEXTLog(logger, METHOD_NAME, actorName, "Is a User Group");
        		
        		IGroup iGroup = servicesUtil.getSecurityService().getGroup(actorName);
                Iterator<IUser> iUserIterator = iGroup.getUsers();
                
                while (iUserIterator.hasNext())
                {
                    user = iUserIterator.next();
                    usersSet.add(user);
                }
        	}
        	else if (actor.isUser()) {
        		
        		debugEXTLog(logger, METHOD_NAME, actorName, "Is a User");
        		
                user = servicesUtil.getSecurityService().getUser(actorName);
                usersSet.add(user);
        	}
        	else {
        		
        		errorEXTLog(logger, METHOD_NAME, "Invalid Actor", "Actor: " + actorName + " is not a valid group or user");
        	}
        }
        
        List<IUser> uniqueListOfUsers = new ArrayList<IUser>(usersSet);
        debugEXTLog(logger, METHOD_NAME, "uniqueListOfUsers", uniqueListOfUsers);
        
        return uniqueListOfUsers;
    }
    
    /**
	 * <p>
	 * This method returns the user or actor of a session.
	 * </p>
	 * @param opSession 
	 * 			Instance of OpenpagesSession
	 * @param logger 
	 * 			Instance of Logger
	 * @return Actor 
	 * 			Actor of the session
	 * @throws Exception
	 */
    public static Actor getActor(OpenpagesSession opSession, Logger logger) throws Exception {
    	
    	final String METHOD_NAME = "getActor";

    	Actor actor = opSession.getSecurityService().getActor(opSession.getInfo().getUserName());
    	LoggerUtil.debugEXTLog(logger, METHOD_NAME, "actor", actor.getName());
    	
    	return actor;
    }
    
    /**
	 * <p>
	 * This method returns the Profile of a User.
	 * </p>
	 * @param actorId 
	 * 			Actor Id
	 * @param opSession 
	 * 			Instance of OpenpagesSession
	 * @param logger 
	 * 			Instance of Logger
	 * @return ObjectProfile 
	 * 			Instance of Object Profile
	 * @throws Exception
	 */
    public static ObjectProfile getUserProfile(ActorId actorId, OpenpagesSession opSession, Logger logger) throws Exception {
    	
    	final String METHOD_NAME = "getUserProfile";
    	
    	ObjectProfile userProfile = opSession.getAdminService().getPreferredObjectProfile(actorId);
    	LoggerUtil.debugEXTLog(logger, METHOD_NAME, "userProfile", userProfile.getName());
    	
    	return userProfile;
    }
    
    
    /**
     * @param username
     * @param grcObj
     * @param fieldName
     * @param servicesUtil
     * @param logger
     * @return
     * @throws Exception
     */
    public static boolean isUserInTheField(String username, IGRCObject grcObj, String fieldName, ServicesUtil servicesUtil, Logger logger) throws Exception
    {
      List<String> userList =  NotificationUtil.getUsersInFieldAsList(fieldName, grcObj, servicesUtil, logger);
      return userList.contains(username);
    }


}
