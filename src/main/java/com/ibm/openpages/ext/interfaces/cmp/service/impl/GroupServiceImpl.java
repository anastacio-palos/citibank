package com.ibm.openpages.ext.interfaces.cmp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.security.IGroup;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementConstants;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementMessageConstants;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementRegistryConstants;
import com.ibm.openpages.ext.interfaces.cmp.service.GroupService;
import com.ibm.openpages.ext.interfaces.cmp.service.UserService;
import com.ibm.openpages.ext.interfaces.cmp.util.EntitlementEngineRegistry;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    EntitlementEngineRegistry entitlementEngineRegistry;
    @Autowired
    UserService userService;

    @Override
    public boolean addRoles(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
                            Logger logger) {
        logger.debug("***** GroupServiceImpl - addRoles START *****");
        boolean result = true;
        if (dataMap != null && messageList != null && apiFactory != null) {
            if (dataMap.get(EntitlementConstants.ROLES) != null) {
                IUser iUser = userService.getUser(dataMap, messageList, apiFactory, logger);
                if (iUser != null) {
                    List<String> listAddGroups = (List<String>) dataMap.get(EntitlementConstants.ROLES);
                    logger.debug("Roles to be added: " + listAddGroups);
                    if (!listAddGroups.isEmpty()) {
                        ISecurityService service = apiFactory.createSecurityService();
                        Map<String, String> groupMap = (Map<String, String>) entitlementEngineRegistry
                                .getConstraint(EntitlementConstants.CONSTRAINT_KEY_ROLES).getConstraintData();
                        logger.debug("rolesMap " + groupMap);
                        Iterator<IGroup> groupsFromUser = iUser.getGroups();
                        List<IGroup> listOfGroupsFromUser;
                        Iterable<IGroup> iterable = () -> groupsFromUser;
                        listOfGroupsFromUser = StreamSupport.stream(iterable.spliterator(),
                                false).collect(Collectors.toList());
                        logger.debug("List of roles from User ");
                        for (IGroup theGroup : listOfGroupsFromUser)
                            logger.debug(theGroup.getName());

                        for (String roleFromAdd : listAddGroups) {
                            logger.debug("Validating current add_role " + groupMap.get(roleFromAdd));

                            try {
                                IGroup iGroup = service.getGroup(groupMap.get(roleFromAdd));
                                if (iUser.isMember(iGroup)) {
                                    logger.debug("Already exist " + iGroup.getName());
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.WARNING_USER_ALREADY_ADDED_GROUP, roleFromAdd,
                                            dataMap.get(EntitlementConstants.USERNAME));
                                    messageList.add(message);
                                    logger.info(message);
                                } else {
                                    logger.debug("Trying to add " + iGroup.getName());
                                    iGroup = service.getGroup(groupMap.get(roleFromAdd));
                                    iGroup.addMember(iUser);
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.SUCCESS_GROUPS_ADDED, roleFromAdd,
                                            dataMap.get(EntitlementConstants.USERNAME));
                                    messageList.add(message);
                                    logger.info(message);
                                }

                            } catch (Exception ex) {
                                String errorMessage = MessageFormat.format(
                                        EntitlementMessageConstants.ERROR_FAILED_ADD_GROUP,
                                        dataMap.get(EntitlementConstants.USERNAME), roleFromAdd);

                                messageList.add(errorMessage);
                                logger.error(errorMessage, ex);
                                result = false;
                                break;
                            }
                        }
                    } else {
                        logger.debug(EntitlementMessageConstants.WARNING_EMPTY_LIST);
                    }
                }
            } else {
                logger.debug(EntitlementMessageConstants.WARNING_EMPTY_LIST);
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
            result = false;
        }
        logger.debug("Result: " + result);
        logger.debug("***** GroupServiceImpl - addRoles END *****");
        return result;
    }

    @Override
    public boolean addGroupsCountry(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
                                    Logger logger) {
        logger.debug("***** GroupServiceImpl - addGroupsCountry START *****");
        boolean result = true;
        if (dataMap != null && messageList != null && apiFactory != null) {
            if (dataMap.get(EntitlementConstants.GROUPS_COUNTRY) != null) {
                IUser iUser = userService.getUser(dataMap, messageList, apiFactory, logger);
                if (iUser != null) {
                    List<String> listAddCountry = (List<String>) dataMap.get(EntitlementConstants.GROUPS_COUNTRY);
                    logger.debug("Groups to be added: " + listAddCountry);
                    if (!listAddCountry.isEmpty()) {
                        ISecurityService service = apiFactory.createSecurityService();
                        for (String groupCountry : listAddCountry) {
                            try {
                                IGroup iGroup = service.getGroup(groupCountry);
                                if (!iGroup.hasMember(iUser)) {
                                    iGroup.addMember(iUser);
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.SUCCESS_GROUPS_COUNTRY_ADDED, groupCountry,
                                            dataMap.get(EntitlementConstants.USERNAME));
                                    messageList.add(message);
                                    logger.info(message);
                                } else {
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.WARNING_USER_ALREADY_ADDED_COUNTRY,
                                            groupCountry, dataMap.get(EntitlementConstants.USERNAME));
                                    messageList.add(message);
                                    logger.info(message);
                                }
                            } catch (Exception ex) {
                                String errorMessage = MessageFormat.format(
                                        EntitlementMessageConstants.ERROR_FAILED_COUNTRY_ADDED,
                                        dataMap.get(EntitlementConstants.USERNAME), groupCountry);
                                messageList.add(errorMessage);
                                logger.error(errorMessage, ex);
                                result = false;
                                break;
                            }
                        }
                    } else {
                        logger.debug(EntitlementMessageConstants.WARNING_EMPTY_LIST);
                    }
                }
            } else {
                logger.debug(EntitlementMessageConstants.WARNING_EMPTY_LIST);
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
        }
        logger.debug("Result: " + result);
        logger.debug("***** GroupServiceImpl - addGroupsCountry END *****");
        return result;
    }

    @Override
    public boolean addGroupHelpDesk(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
                                    Logger logger) {
        logger.debug("***** GroupServiceImpl - addGroupHelpDesk START *****");
        boolean result = true;
        IGroup iGroupHelpDesk = null;
        if (dataMap != null && messageList != null && apiFactory != null) {
            IUser iUser = userService.getUser(dataMap, messageList, apiFactory, logger);
            if (iUser != null) {
                String gidaIsa = (String) dataMap.get(EntitlementConstants.GIDA_ISA);
                logger.debug("gidaIsa value : " + gidaIsa);
                ISecurityService service = apiFactory.createSecurityService();
                try {
                    iGroupHelpDesk = service.getGroup(entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_HELP_DESK));
                    logger.debug("iGroupHelpDesk  : " + iGroupHelpDesk.getName());

                    if (!iGroupHelpDesk.hasMember(iUser)) {
                        iGroupHelpDesk.addMember(iUser);
                        String message = MessageFormat.format(EntitlementMessageConstants.SUCCESS_ADD_HELP_DESK,
                                iGroupHelpDesk, dataMap.get(EntitlementConstants.USERNAME));
                        messageList.add(message);
                        logger.info(message);
                    } else {
                        String message = MessageFormat.format(
                                EntitlementMessageConstants.WARNING_USER_ALREADY_ADDED_HELP_DESK,
                                dataMap.get(EntitlementConstants.USERNAME));
                        messageList.add(message);
                        logger.info(message);
                    }
                } catch (Exception ex) {
                    String errorMessage = MessageFormat.format(EntitlementMessageConstants.ERROR_FAILED_ADD_HELP_DESK,
                            dataMap.get(EntitlementConstants.USERNAME), iGroupHelpDesk.getName());
                    messageList.add(errorMessage);
                    logger.error(errorMessage, ex);
                    result = false;
                }
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
        }
        logger.debug("Result: " + result);
        logger.debug("***** GroupServiceImpl - addGroupHelpDesk END *****");
        return result;
    }

    @Override
    public boolean removeGroupHelpDesk(Map<String, Object> dataMap, List<String> messageList,
                                       IServiceFactory apiFactory, Logger logger) {
        logger.debug("***** GroupServiceImpl - removeGroupHelpDesk START *****");
        boolean result = true;
        IGroup iGroupHelpDesk = null;
        if (dataMap != null && messageList != null && apiFactory != null) {
            IUser iUser = userService.getUser(dataMap, messageList, apiFactory, logger);
            if (iUser != null) {
                String gidaIsa = (String) dataMap.get(EntitlementConstants.GIDA_ISA);
                logger.debug("gidaIsa value : " + gidaIsa);
                ISecurityService service = apiFactory.createSecurityService();
                try {
                    iGroupHelpDesk = service.getGroup(entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_HELP_DESK));
                    logger.debug("iGroupHelpDesk  : " + iGroupHelpDesk);

                    if (iGroupHelpDesk.hasMember(iUser)) {
                        iGroupHelpDesk.remove(iUser);
                        String message = MessageFormat.format(EntitlementMessageConstants.SUCCESS_REMOVE_HELP_DESK,
                                dataMap.get(EntitlementConstants.USERNAME));
                        messageList.add(message);
                        logger.info(message);
                    } else {
                        String message = MessageFormat.format(
                                EntitlementMessageConstants.WARNING_USER_WAS_NOT_MEMBER_HELP_DESK,
                                dataMap.get(EntitlementConstants.USERNAME));
                        messageList.add(message);
                        logger.info(message);
                    }
                } catch (Exception ex) {
                    String errorMessage = MessageFormat.format(
                            EntitlementMessageConstants.ERROR_FAILED_REMOVE_HELP_DESK,
                            dataMap.get(EntitlementConstants.USERNAME));
                    messageList.add(errorMessage);
                    logger.error(errorMessage, ex);
                    result = false;
                }
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
        }
        logger.debug("Result: " + result);
        logger.debug("***** GroupServiceImpl - removeGroupHelpDesk END *****");
        return result;
    }

    @Override
    public boolean removeRoles(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
                               Logger logger) {
        logger.debug("***** GroupServiceImpl - removeGroups START *****");
        boolean result = true;
        if (dataMap != null && messageList != null && apiFactory != null) {
            if (dataMap.get(EntitlementConstants.REMOVE_GROUPS) != null) {
                IUser iUser = userService.getUser(dataMap, messageList, apiFactory, logger);
                if (iUser != null) {
                    List<String> listRemoveGroups = (List<String>) dataMap.get(EntitlementConstants.REMOVE_GROUPS);
                    logger.debug("Groups to be removed: " + listRemoveGroups);
                    if (!listRemoveGroups.isEmpty()) {
                        ISecurityService service = apiFactory.createSecurityService();
                        Map<String, String> groupMap = (Map<String, String>) entitlementEngineRegistry
                                .getConstraint(EntitlementConstants.CONSTRAINT_KEY_ROLES).getConstraintData();
                        logger.debug("CONSTRAINT_KEY_ROLES " + entitlementEngineRegistry
                                .getConstraint(EntitlementConstants.CONSTRAINT_KEY_ROLES));
                        Iterator<IGroup> allGroups = iUser.getGroups();
                        for (String groupToRemove : listRemoveGroups) { // Regulatory, Audit
                            try {
                                logger.debug("Group to remove from json input " + groupToRemove);
                                IGroup iGroupToRemove = service.getGroup(groupMap.get(groupToRemove));
                                logger.debug("IGroup to remove "
                                        + ((iGroupToRemove != null ? iGroupToRemove.getName() : "was null")));

                                // Check if groupToRemove was Planning Champion or RIM
                                String groupFieldTable = null;
                                String entityLevel = null;
                                String user = iUser.getName() + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR + ";";
                                String rimEntityValue = entitlementEngineRegistry.getProperty(
                                        EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_MANDATORY_GROUPS);
                                String planChampEntityValue = entitlementEngineRegistry.getProperty(
                                        EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_MANDATORY_GROUPS);

                                logger.debug("Values " + iGroupToRemove.getName() + " vs " + rimEntityValue);
                                if (rimEntityValue.contains(iGroupToRemove.getName())) {
                                    logger.debug("Removing RIM.");
                                    groupFieldTable = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD);// Citi-BE:RIMUsers
                                    entityLevel = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_ENTITY_LEVEL);// Lead
                                    // IA
                                    // Teams
                                }

                                logger.debug("Values " + iGroupToRemove.getName() + " vs " + planChampEntityValue);
                                if (planChampEntityValue.contains(iGroupToRemove.getName())) {
                                    logger.debug("Removing Planning Champion.");
                                    groupFieldTable = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD); // Citi-BE:PlanChamp
                                    entityLevel = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_ENTITY_LEVEL); // Division
                                }

                                if (groupFieldTable != null && entityLevel != null) {
                                    logger.debug("Reset Ids from " + iUser.getName() + " since is member of "
                                            + iGroupToRemove.getName());
                                    String parameters[] = {entityLevel, groupFieldTable, user};
                                    String query = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_BOTH_ENTITIES_BY_USER_AND_TYPE_QUERY);
                                    logger.debug("Query --> " + query);
                                    Map<String, String> queryResult = ServiceUtil.queryMapWithValues(apiFactory, query,
                                            parameters);
                                    logger.debug("Ids found " + queryResult.toString());
                                    if (!queryResult.isEmpty()) {
                                        for (Map.Entry<String, String> entry : queryResult.entrySet()) {
                                            String id = entry.getKey();
                                            IResourceService iResourceService = apiFactory.createResourceService();
                                            IGRCObject objEntity = iResourceService.getGRCObject(new Id(id));
                                            IField field = objEntity.getField(groupFieldTable);
                                            logger.debug("IField value " + field.getName());
                                            String allUsers = ServiceUtil.getLabelValueFromField(field);
                                            logger.debug(allUsers + " before remove of " + user);
                                            allUsers = allUsers.replace((user), "");
                                            ServiceUtil.setValueFromField(allUsers, field);
                                            logger.debug("Entity removed and updated successfully.");
                                            iResourceService.saveResource(objEntity);
                                        }
                                    }
                                    groupFieldTable = null;
                                    entityLevel = null;
                                }


                                boolean isMember = false;
                                while (allGroups.hasNext()) {
                                    String groupToCompare = allGroups.next().getName();
                                    logger.debug("Comparing iGroup [" + iGroupToRemove.getName() + "] against ["
                                            + groupToCompare + "]");
                                    if (iGroupToRemove.getName().equals(groupToCompare)) {
                                        isMember = true;
                                        break;
                                    }
                                }
                                if (isMember) {
                                    logger.debug("Trying to remove " + groupToRemove + " from " + iUser.getName());
                                    iGroupToRemove.remove(iUser);
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.SUCCESS_GROUPS_REMOVE, groupToRemove,
                                            dataMap.get(EntitlementConstants.USERNAME));
                                    messageList.add(message);
                                    logger.info(message);

                                } else {
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.WARNING_USER_WAS_NOT_MEMBER_GROUP,
                                            dataMap.get(EntitlementConstants.USERNAME), groupToRemove);
                                    messageList.add(message);
                                    logger.info(message);
                                }

                            } catch (Exception ex) {
                                String errorMessage = MessageFormat.format(
                                        EntitlementMessageConstants.ERROR_FAILED_REMOVE_GROUP,
                                        dataMap.get(EntitlementConstants.USERNAME), groupToRemove);
                                messageList.add(errorMessage);
                                result = false;
                                logger.error(errorMessage, ex);
                                break;
                            }
                        }
                    } else {
                        logger.debug(EntitlementMessageConstants.WARNING_EMPTY_LIST);
                    }
                }
            } else {
                logger.debug(EntitlementMessageConstants.WARNING_EMPTY_LIST);
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
            result = false;
        }
        logger.debug("Result: " + result);
        logger.debug("***** GroupServiceImpl - removeRoles END *****");
        return result;
    }

    @Override
    public boolean removeAllGroups(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
                                   Logger logger) {
        logger.debug("***** GroupServiceImpl - removeAllGroups START *****");
        IConfigurationService configrServ = apiFactory.createConfigurationService();
        boolean result = true;
        if (dataMap != null && messageList != null && apiFactory != null) {
            IUser iUser = userService.getUser(dataMap, messageList, apiFactory, logger);
            if (iUser != null) {
                try {
                    String notRemoveGroupsJson = entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_DEFAULT_NOT_REMOVE_GROUPS);
                    logger.debug(" >>> notRemoveGroupsJson: " + notRemoveGroupsJson);
                    ObjectMapper mapper = new ObjectMapper();
                    List keepGroupsList = mapper.readValue(notRemoveGroupsJson, List.class);
                    Iterator<IGroup> groupToRemove = iUser.getGroups();
                    while (groupToRemove.hasNext()) {
                        IGroup group = groupToRemove.next();
                        if (!keepGroupsList.contains(group.getName())) {
                            try {
                                logger.debug(" >>> groupToRemove: " + group.getName());
                                group.remove(iUser);
                            } catch (Exception ex) {
                                String errorMessage = MessageFormat.format(
                                        EntitlementMessageConstants.ERROR_FAILED_REMOVE_ALL_GROUP,
                                        dataMap.get(EntitlementConstants.USERNAME), group.getName());
                                messageList.add(errorMessage);
                                // result = false;
                                logger.error(errorMessage, ex);
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    String message = EntitlementMessageConstants.ERROR_FAILED_VALIDATION;
                    logger.error(message, ex);
                    // result = false;
                }

            }
        }
        logger.debug("***** GroupServiceImpl - removeAllGroups END *****");
        return result;
    }

    @Override
    public boolean removeGroupsCountry(Map<String, Object> dataMap, List<String> messageList,
                                       IServiceFactory apiFactory, Logger logger) {
        logger.debug("***** GroupServiceImpl - removeGroupsCountry START *****");
        boolean result = true;
        if (dataMap != null && messageList != null && apiFactory != null) {
            if (dataMap.get(EntitlementConstants.REMOVE_GROUPS_COUNTRY) != null) {
                IUser iUser = userService.getUser(dataMap, messageList, apiFactory, logger);
                if (iUser != null) {
                    List<String> listRemoveCountry = (List<String>) dataMap
                            .get(EntitlementConstants.REMOVE_GROUPS_COUNTRY);
                    logger.debug("Groups to be removed: " + listRemoveCountry);
                    if (!listRemoveCountry.isEmpty()) {
                        ISecurityService service = apiFactory.createSecurityService();
                        for (String group : listRemoveCountry) {
                            try {
                                IGroup iGroup = service.getGroup(group);
                                if (iGroup.hasMember(iUser)) {
                                    iGroup.remove(iUser);
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.SUCCESS_GROUPS_COUNTRY_REMOVE, group,
                                            dataMap.get(EntitlementConstants.USERNAME));
                                    messageList.add(message);
                                    logger.info(MessageFormat.format(
                                            EntitlementMessageConstants.SUCCESS_GROUPS_COUNTRY_REMOVE, group,
                                            dataMap.get(EntitlementConstants.USERNAME)));
                                } else {
                                    String errorMessage = MessageFormat.format(
                                            EntitlementMessageConstants.ERROR_FAILED_USER_NOT_BELONG_COUNTRY,
                                            dataMap.get(EntitlementConstants.USERNAME), group);
                                    logger.info(errorMessage);
                                }
                            } catch (Exception ex) {
                                String errorMessage = MessageFormat.format(
                                        EntitlementMessageConstants.ERROR_FAILED_REMOVE_GROUP_COUNTRY, group,
                                        dataMap.get(EntitlementConstants.USERNAME));
                                messageList.add(errorMessage);
                                result = false;
                                logger.error(errorMessage, ex);
                                break;
                            }
                        }
                    } else {
                        logger.debug(EntitlementMessageConstants.WARNING_EMPTY_LIST);
                    }
                }
            } else {
                logger.debug(EntitlementMessageConstants.WARNING_EMPTY_LIST);
            }
        } else {
            String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
            messageList.add(errorMessage);
            logger.debug(errorMessage);
            result = false;
        }
        logger.debug("Result: " + result);
        logger.debug("***** GroupServiceImpl - removeGroupsCountry END *****");
        return result;
    }

}
