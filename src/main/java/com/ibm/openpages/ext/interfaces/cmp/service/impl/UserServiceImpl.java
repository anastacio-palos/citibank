package com.ibm.openpages.ext.interfaces.cmp.service.impl;

import com.ibm.openpages.api.configuration.IProfile;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.security.IGroup;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.security.UserInfo;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementConstants;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementMessageConstants;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementRegistryConstants;
import com.ibm.openpages.ext.interfaces.cmp.service.BackupService;
import com.ibm.openpages.ext.interfaces.cmp.service.FieldService;
import com.ibm.openpages.ext.interfaces.cmp.service.GroupService;
import com.ibm.openpages.ext.interfaces.cmp.service.UserService;
import com.ibm.openpages.ext.interfaces.cmp.util.EntitlementEngineRegistry;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    EntitlementEngineRegistry entitlementEngineRegistry;
    @Autowired
    BackupService backupService;
    @Autowired
    FieldService fieldService;
    @Autowired
    GroupService groupService;

    @Override
    public IUser getUser(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.debug("***** UserServiceImpl - getUser START *****");
        IUser iUser = null;
        ISecurityService service = apiFactory.createSecurityService();
        try {
            iUser = service.getUser((String) dataMap.get(EntitlementConstants.USERNAME));
            String message = MessageFormat.format(EntitlementMessageConstants.USER_EXIST,
                    dataMap.get(EntitlementConstants.USERNAME));
            logger.info(message);
        } catch (Exception onfe) {
            String message = MessageFormat.format(EntitlementMessageConstants.USER_NOT_EXIST,
                    dataMap.get(EntitlementConstants.USERNAME));
            logger.info(message);
        }
        logger.debug("***** UserServiceImpl - getUser END *****");
        return iUser;
    }

    @Override
    public UserBean getUserBean(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.debug("***** UserServiceImpl - getUserBean START *****");
        IUser iUser = null;
        UserBean userBean = null;
        ISecurityService service = apiFactory.createSecurityService();
        try {
            iUser = service.getUser((String) dataMap.get(EntitlementConstants.USERNAME));
            String message = MessageFormat.format(EntitlementMessageConstants.USER_EXIST,
                    dataMap.get(EntitlementConstants.USERNAME));
            logger.info(message);
        } catch (Exception e) {
            String message = MessageFormat.format(EntitlementMessageConstants.USER_NOT_EXIST,
                    dataMap.get(EntitlementConstants.USERNAME));
            logger.info(message);

        }
        if (iUser == null)
            return null;
        userBean = new UserBean(iUser);
        IConfigurationService configrServ = apiFactory.createConfigurationService();
        List<IProfile> availableProfilesList = configrServ.getProfiles(iUser);
        List<String> profiles = new ArrayList<>();
        for (IProfile profileItem : availableProfilesList) {
            profiles.add(profileItem.getName());
        }
        IProfile preferredProfile = configrServ.getPreferredProfile(iUser);
        logger.debug("Preferred Profile\n " + preferredProfile.getName());
        userBean.setPreferredProfileName(preferredProfile.getName());
        userBean.setAvailableProfileNames(profiles);

        //fieldService.getIdsFromEntityObject(userBean, apiFactory, true, logger);
        //fieldService.getIdsFromEntityObject(userBean, apiFactory, false, logger);
        userBean.setAllLeadProductTeams(fieldService.getAllLeadProductTeam(userBean.getUserName(), apiFactory, logger));
        userBean.setAllProductFunctionTeams(fieldService.getAllProductFunctionTeam(userBean.getUserName(), apiFactory, logger));

        logger.debug("userBean content: " + userBean);

        logger.debug("***** UserServiceImpl - getUserBean END *****");
        return userBean;
    }

    @Override
    public boolean createUser(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.debug("***** UserServiceImpl - createUser START *****");
        boolean result = false;
        if (dataMap != null && messageList != null && apiFactory != null) {
            IUser iUser = getUser(dataMap, messageList, apiFactory, logger);
            if (iUser == null) {
                //User does not exist, and it is created
                ISecurityService service = apiFactory.createSecurityService();
                IConfigurationService configrServ = apiFactory.createConfigurationService();

                try {

                    String isoCode = entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_DEFAULT_ISO_CODE);
                    String password = entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_DEFAULT_PASSWORD);
                    Locale local = new Locale(isoCode);
                    UserInfo information = new UserInfo((String) dataMap.get(EntitlementConstants.USERNAME),
                            (String) dataMap.get(EntitlementConstants.FIRSTNAME),
                            (String) dataMap.get(EntitlementConstants.LASTNAME),
                            (String) dataMap.get(EntitlementConstants.MIDDLENAME), password, local);
                    information.setEmail((String) dataMap.get(EntitlementConstants.EMAIL));
                    List<IGroup> groups = new ArrayList<>();
                    iUser = service.createUser(information, groups);
                    if (iUser != null) {
                        String profile = null;
                        String successMessage;
                        if (dataMap.get(EntitlementConstants.PROFILE) != null
                                || !(((String) dataMap.get(EntitlementConstants.PROFILE)).isEmpty())) {
                            profile = (String) (dataMap.get(EntitlementConstants.PROFILE));
                            logger.debug("Profile not empty " + profile);
                            /*
                             * } else { profile =
                             * engineRegistry.getProperty(RegistryConstants.ENGINE_METADATA_DEFAULT_PROFILE)
                             * ; logger.debug("Profile empty " + profile); }
                             */
                            try {
                                IProfile iProfile = configrServ.getProfile(profile);
                                logger.debug("IProfile content " + iProfile.getName());
                                List<Id> profileIds = new ArrayList<>();
                                profileIds.add(iProfile.getId());
                                configrServ.associateProfilesToUser(iUser.getId(), profileIds);
                                configrServ.setPreferredProfile(iProfile, iUser);

                                successMessage = EntitlementMessageConstants.SUCCESS_ADDING_PROFILE_USER;
                                logger.info(successMessage);
                            } catch (Exception ex) {
                                String errorMessage = EntitlementMessageConstants.ERROR_ADDING_PROFILE_USER;
                                logger.error(errorMessage, ex);
                                messageList.add(errorMessage);
                                // result = false;
                            }
                        }
                        result = true;
                        successMessage = MessageFormat.format(EntitlementMessageConstants.SUCCESS_CREATING_USER,
                                dataMap.get(EntitlementConstants.USERNAME));
                        messageList.add(successMessage);
                        logger.info(successMessage);
                    }

                } catch (Exception ex) {
                    String errorMessage = MessageFormat.format(EntitlementMessageConstants.ERROR_FAILED_CREATING_USER,
                            dataMap.get(EntitlementConstants.USERNAME));
                    logger.error(errorMessage, ex);
                    messageList.add(errorMessage);
                }
            } else {
                // TODO Validate if we need to fail this process or not.
                result = updateUser(dataMap, messageList, apiFactory, logger);

                String errorMessage = MessageFormat.format(EntitlementMessageConstants.ERROR_USER_ALREADY_EXIST,
                        dataMap.get(EntitlementConstants.USERNAME));
                // messageList.add(errorMessage);
                logger.info(errorMessage);

                // }
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
        }
        logger.debug("Result: " + result);
        logger.debug("***** UserServiceImpl - createUser END *****");
        return result;
    }

    @Override
    public boolean updateUser(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.debug("***** UserServiceImpl - updateUser START *****");
        boolean result = false;
        if (dataMap != null && messageList != null && apiFactory != null) {
            IUser iUser = getUser(dataMap, messageList, apiFactory, logger);
            if (iUser != null) {
                try {
                    if (!iUser.isEnabled()) {
                        iUser.enable();
                    }
                    if (dataMap.get(EntitlementConstants.DESCRIPTION) != null) {
                        iUser.setDescription((String) dataMap.get(EntitlementConstants.DESCRIPTION));
                    }
                    if (dataMap.get(EntitlementConstants.EMAIL) != null) {
                        iUser.setEmailAddress((String) dataMap.get(EntitlementConstants.EMAIL));
                    }
                    if (dataMap.get(EntitlementConstants.FIRSTNAME) != null) {
                        iUser.setFirstName((String) dataMap.get(EntitlementConstants.FIRSTNAME));
                    }
                    if (dataMap.get(EntitlementConstants.LASTNAME) != null) {
                        iUser.setLastName((String) dataMap.get(EntitlementConstants.LASTNAME));
                    }
                    if (dataMap.get(EntitlementConstants.MIDDLENAME) != null) {
                        iUser.setMiddleName((String) dataMap.get(EntitlementConstants.MIDDLENAME));
                    }
                    if (dataMap.get(EntitlementConstants.ISO_CODE) != null) {
                        iUser.setLocale(new Locale((String) dataMap.get(EntitlementConstants.ISO_CODE)));
                    }
                    IConfigurationService configrServ = apiFactory.createConfigurationService();
                    String profile;
                    if (dataMap.get(EntitlementConstants.PROFILE) != null
                            && !(((String) dataMap.get(EntitlementConstants.PROFILE)).trim().isEmpty())) {
                        profile = (String) (dataMap.get(EntitlementConstants.PROFILE));
                        List<IProfile> listProfiles = configrServ.getProfiles(iUser);
                        boolean profileExists = false;
                        for (IProfile currentProfile : listProfiles) {
                            if (profile.equals(currentProfile.getName())) {
                                profileExists = true;
                                break;
                            }
                        }

                        logger.debug("Profile not empty " + profile);
                        if (!profileExists) {
                            try {
                                IProfile iProfile = configrServ.getProfile(profile);
                                logger.debug("IProfile content " + iProfile.getName());
                                List<Id> profileIds = new ArrayList<>();
                                profileIds.add(iProfile.getId());
                                configrServ.associateProfilesToUser(iUser.getId(), profileIds);
                                logger.info(EntitlementMessageConstants.SUCCESS_UPDATING_PROFILE_USER);
                                result = true;
                            } catch (Exception ex) {
                                String errorMessage = EntitlementMessageConstants.ERROR_UPDATING_PROFILE_USER;
                                logger.error(errorMessage, ex);
                            }
                        }
                    }

                    iUser.update();
                    String successMessage = MessageFormat.format(EntitlementMessageConstants.SUCCESS_UPDATING_USER,
                            dataMap.get(EntitlementConstants.USERNAME));
                    logger.info(successMessage);
                    messageList.add(successMessage);
                    result = true;
                } catch (Exception ex) {
                    String errorMessage = MessageFormat.format(EntitlementMessageConstants.ERROR_FAILED_MODIFY_USER,
                            dataMap.get(EntitlementConstants.USERNAME));
                    logger.error(errorMessage, ex);
                    messageList.add(errorMessage);
                }
            } else {
                String message = MessageFormat.format(EntitlementMessageConstants.ERROR_FAILED_MODIFY_NON_EXISTENT_USER,
                        dataMap.get(EntitlementConstants.USERNAME));
                //messageList.add(message);
                logger.info(message);
                result = createUser(dataMap, messageList, apiFactory, logger);
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
        }
        logger.debug("Result: " + result);
        logger.debug("***** UserServiceImpl - updateUser END *****");
        return result;
    }

    @Override
    public boolean deleteUser(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.debug("***** UserServiceImpl - deleteUser START *****");
        boolean result = true;
        if (dataMap != null && messageList != null && apiFactory != null) {
            IUser iUser = getUser(dataMap, messageList, apiFactory, logger);
            if (iUser != null) {
                ISecurityService service = apiFactory.createSecurityService();
                IResourceService resourceService = apiFactory.createResourceService();
                try {
                    //Save current values of Citi-BE:PlanChamp and Citi-BE:RIMUsers

                    groupService.removeAllGroups(dataMap, messageList, apiFactory, logger);
                    List<IUser> userList = new ArrayList<>();
                    userList.add(iUser);
                    service.deleteUsers(userList);
                    String message = MessageFormat.format(EntitlementMessageConstants.SUCCESS_DELETED_USER,
                            dataMap.get(EntitlementConstants.USERNAME));
                    logger.info(message);
                    if (messageList.size() > 0) {
                        messageList.add(messageList.size() - 1, message);
                    } else {
                        messageList.add(message);
                    }

                    String user = iUser.getName();
                    //Sacar los currents (allTeams refers to LeadProductTeam and ProductFunctionTeam)
                    // ****Sacarlos mejor del json
                    Map<String, String> currentLeadProductTeam = fieldService.getAllLeadProductTeam(user, apiFactory, logger);
                    Map<String, String> currentProductFunctionTeam = fieldService.getAllProductFunctionTeam(user, apiFactory, logger);

                    //delete content from current LeadProductTeam
                    for (Map.Entry<String, String> lastTeamsSet : currentLeadProductTeam.entrySet()) {
                        IGRCObject entity = resourceService.getGRCObject(new Id(lastTeamsSet.getKey()));
                        IField field = entity.getField(entitlementEngineRegistry
                                .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD));
                        logger.debug("IField value " + field.getName());
                        logger.debug("Value target: " + lastTeamsSet);
                        String currentUsers = ServiceUtil.getLabelValueFromField(field);
                        logger.debug("Users from entity " + currentUsers);
                        currentUsers = currentUsers.replace((user + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR + ";"), "");
                        ServiceUtil.setValueFromField(currentUsers, field);
                        resourceService.saveResource(entity);
                    }

                    //delete content from current Product Function Team
                    for (Map.Entry<String, String> lastTeamsSet : currentProductFunctionTeam.entrySet()) {
                        IGRCObject entity = resourceService.getGRCObject(new Id(lastTeamsSet.getKey()));
                        IField field = entity.getField(entitlementEngineRegistry
                                .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD));
                        logger.debug("IField value " + field.getName());
                        String currentUsers = ServiceUtil.getLabelValueFromField(field);
                        logger.debug("Users from entity " + currentUsers);
                        currentUsers = currentUsers.replace((user + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR + ";"), "");
                        ServiceUtil.setValueFromField(currentUsers, field);
                        resourceService.saveResource(entity);
                    }

                } catch (Exception ex) {
                    String message = MessageFormat.format(EntitlementMessageConstants.ERROR_FAILED_DELETE_USER,
                            dataMap.get(EntitlementConstants.USERNAME));
                    messageList.add(message);
                    logger.error(message, ex);
                    result = false;
                }
            } else {
                String message = MessageFormat.format(EntitlementMessageConstants.ERROR_FAILED_DELETE_NON_EXISTENT_USER,
                        dataMap.get(EntitlementConstants.USERNAME));
                logger.info(message);
                messageList.add(message);
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
        }
        logger.debug("Result: " + result);
        logger.debug("***** UserServiceImpl - deleteUser END *****");
        return result;
    }
}
