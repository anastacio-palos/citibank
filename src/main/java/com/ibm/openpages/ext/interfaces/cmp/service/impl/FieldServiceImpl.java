package com.ibm.openpages.ext.interfaces.cmp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.security.IGroup;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementConstants;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementMessageConstants;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementRegistryConstants;
import com.ibm.openpages.ext.interfaces.cmp.service.FieldService;
import com.ibm.openpages.ext.interfaces.cmp.service.UserService;
import com.ibm.openpages.ext.interfaces.cmp.util.EntitlementEngineRegistry;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    EntitlementEngineRegistry entitlementEngineRegistry;
    @Autowired
    UserService userService;


    public void getIdsFromEntityObject(UserBean backUpBean, IServiceFactory apiFactory, boolean isLeadProductTeam, Logger logger) {
        logger.debug("***** FieldServiceImpl - getIdsFromEntityObject START *****");
        ISecurityService securityService = apiFactory.createSecurityService();
        IResourceService iResourceService = apiFactory.createResourceService();
        String fieldTableParameter = null;
        String entityLevel = null;
        if (isLeadProductTeam) {
            //Division Level
            entityLevel = entitlementEngineRegistry
                    .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_ENTITY_LEVEL);
            logger.debug(" >>> iaDivisionGroup Level ");

            fieldTableParameter = entitlementEngineRegistry.getProperty(
                    EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD); // Citi-BE:PlanCham
        } else {
            // Auditor Group Level
            entityLevel = entitlementEngineRegistry
                    .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_ENTITY_LEVEL);
            logger.debug(" >>> Auditor Groups Level ");

            fieldTableParameter = entitlementEngineRegistry.getProperty(
                    EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD); // Citi-BE:RIMUsers

        }
        Map<String, String> queryResult = getProductsFromUser(backUpBean.getUserName(), apiFactory, fieldTableParameter, entityLevel);

        logger.debug("Ids found " + queryResult);
        if (!queryResult.isEmpty()) {
            for (Map.Entry<String, String> entry : queryResult.entrySet()) {
                if (isLeadProductTeam) {
                    backUpBean.setLeadProductTeam(entry.getKey(), entry.getValue());
                } else {
                    backUpBean.setProductFunctionTeam(entry.getKey(), entry.getValue());
                }
            }
        }
        logger.debug("***** FieldServiceImpl - getIdsFromEntityObject END *****");
    }

    @Override
    public Map<String, String> getAllLeadProductTeam(String user, IServiceFactory apiFactory, Logger logger) {
        //OK
        logger.debug("***** FieldServiceImpl - getAllLeadProductTeam START *****");
        ISecurityService securityService = apiFactory.createSecurityService();
        IResourceService iResourceService = apiFactory.createResourceService();
        String fieldTableParameter = null;
        String entityLevel = null;
        Map<String, String> result = new HashMap<String, String>();

        //Division Level
        entityLevel = entitlementEngineRegistry
                .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_ENTITY_LEVEL);
        logger.debug(" >>> iaDivisionGroup Level ");

        fieldTableParameter = entitlementEngineRegistry.getProperty(
                EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD); // Citi-BE:PlanCham

        Map<String, String> queryResult = getProductsFromUser(user, apiFactory, fieldTableParameter, entityLevel);

        logger.debug("Ids found " + queryResult);

        logger.debug("***** FieldServiceImpl - getAllLeadProductTeam END *****");
        return queryResult;
    }

    @Override
    public Map<String, String> getAllProductFunctionTeam(String user, IServiceFactory apiFactory, Logger logger) {
        //OK
        logger.debug("***** FieldServiceImpl - getAllProductFunctionTeam START *****");
        ISecurityService securityService = apiFactory.createSecurityService();
        IResourceService iResourceService = apiFactory.createResourceService();
        String fieldTableParameter = null;
        String entityLevel = null;
        Map<String, String> result = new HashMap<String, String>();

        entityLevel = entitlementEngineRegistry
                .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_ENTITY_LEVEL);
        logger.debug(" >>> Auditor Groups Level ");

        fieldTableParameter = entitlementEngineRegistry.getProperty(
                EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD); // Citi-BE:RIMUsers

        Map<String, String> queryResult = getProductsFromUser(user, apiFactory, fieldTableParameter, entityLevel);

        logger.debug("Ids found " + queryResult);

        logger.debug("***** FieldServiceImpl - getAllProductFunctionTeam END *****");
        return queryResult;

    }

    @Override
    public Map<String, String> getProductsFromUser(String user, IServiceFactory apiFactory, String fieldTableParameter, String entityLevel) {

        //OK
        String currentUser = user + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR + ";";


        String parameters[] = {entityLevel, fieldTableParameter, currentUser};

        String query = entitlementEngineRegistry.getProperty(
                EntitlementRegistryConstants.ENGINE_METADATA_BOTH_ENTITIES_BY_USER_AND_TYPE_QUERY);

        Map<String, String> queryResult = ServiceUtil.queryMapWithValues(apiFactory, query,
                parameters);
        return queryResult;
    }

    @Override
    public boolean addUserToLeadProductTeam(Map<String, Object> jsonDataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.debug("***** FieldServiceImpl - addUserToLeadProductTeam START *****");
        boolean result = true;
        if (jsonDataMap != null && messageList != null && apiFactory != null) {
            IUser iUser = userService.getUser(jsonDataMap, messageList, apiFactory, logger);

            String userName = iUser.getName();
            if (iUser != null) {
                try {
                    ISecurityService securityService = apiFactory.createSecurityService();
                    IResourceService iResourceService = apiFactory.createResourceService();


                    //Planning Champion
                    String mandatoryGroupsJson = entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_MANDATORY_GROUPS);

                    // List of IADivision to add from JSON
                    List<String> listLeadProductTeams = (List<String>) jsonDataMap
                            .get(EntitlementConstants.ADD_LEAD_PRODUCT_TEAM);
                    String addUser = userName + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR;

                    logger.debug(" >>> mandatoryGroupsJson: " + mandatoryGroupsJson);
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> mandatoryGroupsList = mapper.readValue(mandatoryGroupsJson, List.class);


                    //Division Level
                    String[] leadProductTeamGroupLevel = {entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_ENTITY_LEVEL)};
                    logger.debug(" >>> Lead Product Team Division Level ");

                    // Aqui sacamos los iaDivision filtrados por nivel

                    Map<String, String> resultAllId = (Map<String, String>) entitlementEngineRegistry
                            .getConstraint(EntitlementConstants.CONSTRAINT_KEY_LEAD_PRODUCT_TEAM).getConstraintData();
                    String id = null;

                    logger.debug("Mapped resultset of all ids from level " + leadProductTeamGroupLevel[0] + " : " + resultAllId);
                    logger.debug("Ids to be added " + listLeadProductTeams.toString());

                    for (String mandatoryGroup : mandatoryGroupsList) { // RIM, Plannning Champion
                        IGroup iGroup = securityService.getGroup(mandatoryGroup);
                        for (String leadProductTeamGroupId : listLeadProductTeams) {
                            if (iGroup != null && iGroup.hasMember(iUser)) {
                                // Validate if auditorGroupName is found
                                for (Map.Entry<String, String> entry : resultAllId.entrySet()) {
                                    if (entry.getKey().equals(leadProductTeamGroupId)) {
                                        id = entry.getKey();
                                        break;
                                    }
                                }

                                if (id != null) {
                                    logger.debug("ID --> " + id);
                                    logger.debug(" >>> leadProductTeamGroupId = " + leadProductTeamGroupId);
                                    String fieldTableParameter = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD); // Citi-BE:HdAud

                                    logger.debug(" >>> fieldTable = " + fieldTableParameter);
                                    String parameters[] = {fieldTableParameter, addUser, leadProductTeamGroupId};
                                    logger.debug("iUser.getName() --> " + userName);

                                    String query = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_GENERAL_PRODUCT_TEAM_QUERY);
                                    logger.debug("Query --> " + query);
                                    List<List<String>> queryResult = ServiceUtil.queryService(apiFactory, query,
                                            parameters);

                                    IGRCObject leadProductTeamEntity = iResourceService.getGRCObject(new Id(id));
                                    IField field = leadProductTeamEntity.getField(fieldTableParameter);
                                    logger.debug("IField value " + field.getName());
                                    String users = ServiceUtil.getLabelValueFromField(field);

                                    // logger.debug("Backup of IaDivision: " + id + " Content: " + users);
                                    //userBackUp.setIa_division(id, users);

                                    if (queryResult.isEmpty()) {
                                        logger.debug(" >> start Setting Multi " + fieldTableParameter);

                                        logger.debug("Users from Entity " + users);
                                        if (users.isEmpty())
                                            users += (EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR + ";");
                                        users += (addUser + ";");
                                        ServiceUtil.setValueFromField(users, field);
                                        logger.debug("Entity added and updated successfully.");
                                        iResourceService.saveResource(leadProductTeamEntity);

                                        String message = MessageFormat.format(
                                                EntitlementMessageConstants.SUCCESS_ADDING_ROLE_ASSIGNMENT_LEAD_PRODUCT_TEAM,
                                                resultAllId.get(leadProductTeamGroupId), iUser.getName());
                                        messageList.add(message);
                                        logger.info(message);
                                    } else {
                                        logger.debug(userName + " was already assigned yet " + leadProductTeamGroupId);
                                    }
                                } else {
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.ERROR_USER_ID_DOES_NOT_BELONG, resultAllId.get(leadProductTeamGroupId),
                                            leadProductTeamGroupLevel[0]);
                                    messageList.add(message);
                                    logger.info(message);
                                }
                                id = null;


                            } else {
                                String message = MessageFormat.format(
                                        EntitlementMessageConstants.ERROR_USER_IS_NOT_ALLOWED, userName,
                                        mandatoryGroup);
                                messageList.add(message);
                                logger.info(message);
                                result = false;
                                break;
                            }
                        }
                    }

                } catch (Exception ex) {
                    String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
                    messageList.add(errorMessage);
                    logger.error(errorMessage, ex);
                    result = false;
                }
            }
        } else {
            String message = MessageFormat.format(EntitlementMessageConstants.ERROR_FAILED_MODIFY_NON_EXISTENT_USER,
                    jsonDataMap.get(EntitlementConstants.USERNAME));
            messageList.add(message);
            logger.info(message);
        }
        logger.debug("Result: " + result);
        logger.debug("***** FieldServiceImpl - addUserToLeadProductTeam END *****");
        return result;
    }

    @Override
    public boolean addUserToProductFunctionTeam(Map<String, Object> jsonDataMap, List<String> messageList,
                                                IServiceFactory apiFactory, Logger logger) {
        //AuditorGroup is Product Function Team
        logger.debug("***** FieldServiceImpl - addUserToProductFunctionTeam START *****");
        boolean result = true;
        if (jsonDataMap != null && messageList != null && apiFactory != null) {
            List<String> auditorGroupListJson = jsonDataMap.get(EntitlementConstants.MAP_TO_ADD_PRODUCT_FUNCTION_TEAM_ASSIGNMENT) != null
                    ? (List<String>) jsonDataMap.get(EntitlementConstants.MAP_TO_ADD_PRODUCT_FUNCTION_TEAM_ASSIGNMENT)
                    : null;
            if (auditorGroupListJson != null && !auditorGroupListJson.isEmpty()) {
                logger.debug("Auditors to be added: " + auditorGroupListJson);
                IUser iUser = userService.getUser(jsonDataMap, messageList, apiFactory, logger);
                if (iUser != null) {
                    ISecurityService securityService = apiFactory.createSecurityService();
                    IResourceService resourceService = apiFactory.createResourceService();
                    String userName = (String) jsonDataMap.get(EntitlementConstants.USERNAME);
                    try {
                        String mandatoryGroupsJson = entitlementEngineRegistry.getProperty(
                                EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_MANDATORY_GROUPS);
                        logger.debug(" >>> mandatoryGroupsJson: " + mandatoryGroupsJson);
                        ObjectMapper mapper = new ObjectMapper();
                        List<String> mandatoryGroupsList = mapper.readValue(mandatoryGroupsJson, List.class);
                        //RIM
                        // Lead IA Teams for Auditor Group

                        String[] auditGroupLevel = {entitlementEngineRegistry
                                .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_ENTITY_LEVEL)};

                        logger.debug(" >>> auditGroup Level " + auditGroupLevel[0]);

                        Map<String, String> resultAllId = (Map<String, String>) entitlementEngineRegistry
                                .getConstraint(EntitlementConstants.CONSTRAINT_KEY_AUDITOR_ROLE).getConstraintData();

                        String id = null;
                        String addUserName = iUser.getName() + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR;
                        logger.debug("Result of all auditors from level " + auditGroupLevel[0] + " : "
                                + resultAllId);

                        for (String mandatoryGroup : mandatoryGroupsList) { // RIM
                            IGroup iGroup = securityService.getGroup(mandatoryGroup);
                            for (String auditorGroupNameJson : auditorGroupListJson) {
                                if (iGroup != null && iGroup.hasMember(iUser)) {

                                    try {
                                        // Validate if auditorGroupNameJson is found
                                        for (Map.Entry<String, String> entry : resultAllId.entrySet()) {
                                            if (entry.getKey().equals(auditorGroupNameJson)) {
                                                id = entry.getKey();
                                                break;
                                            }
                                        }
                                        if (id != null) {
                                            logger.debug("ID --> " + id);
                                            logger.debug(" >>> auditorGroupNameJson = " + auditorGroupNameJson);
                                            // Citi-BE:RIMUsers
                                            String fieldTableParameter = entitlementEngineRegistry.getProperty(
                                                    EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD);


                                            logger.debug(" >>> fieldTable = " + fieldTableParameter);
                                            String parameters[] = {fieldTableParameter, addUserName, id};
                                            logger.debug("iUser.getName() --> " + iUser.getName());
                                            logger.debug(" >>> Auditor Group= " + auditorGroupNameJson);
                                            String query = entitlementEngineRegistry.getProperty(
                                                    EntitlementRegistryConstants.ENGINE_METADATA_GENERAL_PRODUCT_TEAM_QUERY);
                                            logger.debug("Query --> " + query);
                                            List<List<String>> queryResult = ServiceUtil.queryService(apiFactory, query,
                                                    parameters);
                                            IResourceService iResourceService = apiFactory.createResourceService();
                                            IGRCObject aiDivEntity = iResourceService.getGRCObject(new Id(id));
                                            IField field = aiDivEntity
                                                    .getField(entitlementEngineRegistry.getProperty(
                                                            EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD));
                                            logger.debug(
                                                    "IField value " + field.getName());
                                            String users = ServiceUtil.getLabelValueFromField(field);

                                            //logger.debug("Backup of Id-AuditGroup " + id + " value: " + users);
                                            //userBackUp.setAuditor(id, users);

                                            if (queryResult.size() == 0) {
                                                logger.debug(" >> start Setting Multi " + fieldTableParameter);
                                                if (users.isEmpty())
                                                    users = (EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR + ";");

                                                users += (addUserName + ";");
                                                logger.debug("Users from Entity " + users);

                                                ServiceUtil.setValueFromField(users, field);
                                                logger.debug("Entity updated");
                                                resourceService.saveResource(aiDivEntity);

                                                String message = MessageFormat.format(
                                                        EntitlementMessageConstants.SUCCESS_ADDING_PRODUCT_FUNCTION_TEAM,
                                                        resultAllId.get(auditorGroupNameJson), userName);
                                                messageList.add(message);
                                                logger.info(message);

                                            } else {
                                                logger.debug(iUser.getName() + " Already assigned to " + auditorGroupNameJson);
                                            }
                                            id = null;

                                        } else {

                                            String errorMessage = MessageFormat.format(
                                                    EntitlementMessageConstants.ERROR_USER_ID_DOES_NOT_BELONG,
                                                    id, auditGroupLevel[0]);
                                            messageList.add(errorMessage);
                                            logger.debug(errorMessage);
                                        }

                                    } catch (Exception ex) {
                                        String errorMessage = MessageFormat.format(
                                                EntitlementMessageConstants.ERROR_FAILED_ADDING_PRODUCT_FUNCTION_TEAM,
                                                resultAllId.get(auditorGroupNameJson), userName);
                                        messageList.add(errorMessage);
                                        logger.error(errorMessage, ex);
                                        result = false;
                                        break;
                                    }

                                } else {
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.ERROR_USER_IS_NOT_MEMBER_OF_PRODUCT_FUNCTION_GROUP, userName,
                                            mandatoryGroup);
                                    messageList.add(message);
                                    logger.info(message);
                                    result = false;
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        messageList.add(EntitlementMessageConstants.ERROR_INVALID_ROLE_TEMPLATE_AUDITOR_ROLE);
                        logger.error(EntitlementMessageConstants.ERROR_INVALID_ROLE_TEMPLATE_AUDITOR_ROLE, ex);
                        result = false;
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
        //logger.debug("BackUp content " + userBackUp.toString());
        logger.debug("Result: " + result);
        logger.debug("***** FieldServiceImpl - addUserToProductFunctionTeam END *****");
        return result;
    }


    @Override
    public boolean removeUserFromProductFunctionTeam(Map<String, Object> jsonDataMap, List<String> messageList,
                                                     IServiceFactory apiFactory, Logger logger) {
        //AuditorGroup is Product Function Team
        logger.debug("***** FieldServiceImpl - removeUserFromProductFunctionTeam START *****");
        boolean result = true;

        if (jsonDataMap != null && messageList != null && apiFactory != null) {
            List<String> auditorGroupListJson = jsonDataMap.get(EntitlementConstants.MAP_TO_REMOVE_PRODUCT_FUNCTION_TEAM_ASSIGNMENT) != null
                    ? (List<String>) jsonDataMap.get(EntitlementConstants.MAP_TO_REMOVE_PRODUCT_FUNCTION_TEAM_ASSIGNMENT)
                    : null;
            if (auditorGroupListJson != null && !auditorGroupListJson.isEmpty()) {
                logger.debug("Product Function Team to be removed: " + auditorGroupListJson);
                IUser iUser = userService.getUser(jsonDataMap, messageList, apiFactory, logger);
                if (iUser != null) {
                    ISecurityService securityService = apiFactory.createSecurityService();
                    IResourceService resourceService = apiFactory.createResourceService();
                    String userName = (String) jsonDataMap.get(EntitlementConstants.USERNAME);
                    try {
                        String mandatoryGroupsJson = entitlementEngineRegistry.getProperty(
                                EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_MANDATORY_GROUPS);
                        logger.debug(" >>> mandatory Rol from Json: " + mandatoryGroupsJson);
                        ObjectMapper mapper = new ObjectMapper();
                        List<String> mandatoryGroupsList = mapper.readValue(mandatoryGroupsJson, List.class);

                        // Lead IA Teams for Auditor Group
                        String[] auditGroupLevel = {entitlementEngineRegistry
                                .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_ENTITY_LEVEL)};
                        logger.debug(" >>> auditGroup Level " + auditGroupLevel[0]);

                        Map<String, String> resultAllId = (Map<String, String>) entitlementEngineRegistry
                                .getConstraint(EntitlementConstants.CONSTRAINT_KEY_AUDITOR_ROLE).getConstraintData();

                        String id = null;
                        String removeUserName = iUser.getName() + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR;
                        logger.debug("Values of all product function teams from level " + auditGroupLevel[0] + " : "
                                + resultAllId);

                        for (String mandatoryGroup : mandatoryGroupsList) { // RIM, Plannning Champion
                            IGroup iGroup = securityService.getGroup(mandatoryGroup);
                            for (String auditorGroupNameJson : auditorGroupListJson) {
                                if (iGroup != null && iGroup.hasMember(iUser)) {

                                    try {
                                        // Validate if auditorGroupNameJson is found
                                        for (Map.Entry<String, String> entry : resultAllId.entrySet()) {
                                            if (entry.getKey().equals(auditorGroupNameJson)) {
                                                id = entry.getKey();
                                                break;
                                            }
                                        }
                                        if (id != null) {
                                            logger.debug("ID --> " + id);
                                            logger.debug(" >>> auditorGroupNameJson = " + auditorGroupNameJson);
                                            // Citi-BE:RIMUsers
                                            String fieldTableParameter = entitlementEngineRegistry.getProperty(
                                                    EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD);

                                            logger.debug(" >>> fieldTable = " + fieldTableParameter);
                                            String parameters[] = {fieldTableParameter, removeUserName, id};
                                            logger.debug("iUser.getName() --> " + iUser.getName());
                                            logger.debug(" >>> Auditor Group= " + auditorGroupNameJson);
                                            String query = entitlementEngineRegistry.getProperty(
                                                    EntitlementRegistryConstants.ENGINE_METADATA_GENERAL_PRODUCT_TEAM_QUERY);
                                            logger.debug("Query --> " + query);
                                            List<List<String>> queryResult = ServiceUtil.queryService(apiFactory, query,
                                                    parameters);


                                            if (queryResult.size() > 0) {
                                                logger.debug(" >> start Setting Multi " + fieldTableParameter);

                                                IResourceService iResourceService = apiFactory.createResourceService();
                                                IGRCObject aiDivEntity = iResourceService.getGRCObject(new Id(id));
                                                IField field = aiDivEntity
                                                        .getField(entitlementEngineRegistry.getProperty(
                                                                EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD));
                                                logger.debug(
                                                        "IField value " + field.getName());
                                                String users = ServiceUtil.getLabelValueFromField(field);

                                                //  logger.debug("Backup of Id-AuditGroup " + id + " value: " + users);
                                                // userBackUp.setAuditor(id, users);

                                                logger.debug("Users from Entity before be deleted" + users);
                                                users = users.replace((removeUserName + ";"), "");

                                                ServiceUtil.setValueFromField(users, field);
                                                logger.debug("Entity updated");
                                                resourceService.saveResource(aiDivEntity);

                                                String message = MessageFormat.format(
                                                        EntitlementMessageConstants.SUCCESS_REMOVING_PRODUCT_FUNCTION_TEAM,
                                                        resultAllId.get(auditorGroupNameJson), userName);
                                                messageList.add(message);
                                                logger.info(message);

                                            } else {
                                                logger.debug(iUser.getName() + " was not assigned to " + auditorGroupNameJson + " Auditor Group.");
                                            }
                                            id = null;

                                        } else {
                                            String errorMessage = MessageFormat.format(
                                                    EntitlementMessageConstants.ERROR_USER_ID_DOES_NOT_BELONG,
                                                    resultAllId.get(auditorGroupNameJson), auditGroupLevel[0]);
                                            logger.debug(errorMessage);
                                        }

                                    } catch (Exception ex) {
                                        String errorMessage = MessageFormat.format(
                                                EntitlementMessageConstants.ERROR_FAILED_REMOVING_PRODUCT_FUNCTION_TEAM,
                                                resultAllId.get(auditorGroupNameJson), userName);
                                        messageList.add(errorMessage);
                                        logger.error(errorMessage, ex);
                                        result = false;
                                        break;
                                    }

                                } else {
                                    String message = MessageFormat.format(
                                            EntitlementMessageConstants.ERROR_USER_IS_NOT_MEMBER_OF_PRODUCT_FUNCTION_GROUP, userName,
                                            mandatoryGroup);
                                    messageList.add(message);
                                    logger.info(message);
                                    result = false;
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        messageList.add(EntitlementMessageConstants.ERROR_FAILED_REMOVING_PRODUCT_FUNCTION_TEAM);
                        logger.error(EntitlementMessageConstants.ERROR_FAILED_REMOVING_PRODUCT_FUNCTION_TEAM, ex);
                        result = false;
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
        // logger.debug("BackUp content " + userBackUp.toString());
        logger.debug("Result: " + result);
        logger.debug("***** FieldServiceImpl - removeUserFromProductFunctionTeam END *****");
        return result;
    }

    @Override
    public boolean removeUserFromLeadProductTeam(Map<String, Object> dataMap, List<String> messageList,
                                                 IServiceFactory apiFactory, UserBean userBackUp, Logger logger) {
        logger.debug("***** FieldServiceImpl - removeUserFromLeadProductTeam START *****");
        boolean result = true;
        if (dataMap != null && messageList != null && apiFactory != null) {
            IUser iUser = userService.getUser(dataMap, messageList, apiFactory, logger);

            String userName = iUser.getName();
            if (iUser != null) {
                try {
                    ISecurityService securityService = apiFactory.createSecurityService();
                    IResourceService iResourceService = apiFactory.createResourceService();

                    //Planning Champion
                    String mandatoryGroupsJson = entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_MANDATORY_GROUPS);

                    // List of IADivision to remove from JSON
                    List<String> listRemoveDivisions = (List<String>) dataMap
                            .get(EntitlementConstants.REMOVE_LEAD_PRODUCT_TEAM);
                    String removeUser = userName + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR;

                    logger.debug(" >>> mandatoryGroupsJson: " + mandatoryGroupsJson);
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> mandatoryGroupsList = mapper.readValue(mandatoryGroupsJson, List.class);


                    //Division Level
                    String[] iaDivisionGroupLevel = {entitlementEngineRegistry
                            .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_ENTITY_LEVEL)};
                    logger.debug(" >>> iaDivisionGroup Level ");

                    // Aqui sacamos los iaDivision filtrados por nivel
                    Map<String, String> resultAllId = (Map<String, String>) entitlementEngineRegistry
                            .getConstraint(EntitlementConstants.CONSTRAINT_KEY_LEAD_PRODUCT_TEAM).getConstraintData();
                    String id = null;

                    logger.debug("Mapped result of all ids from level " + iaDivisionGroupLevel[0] + " : " + resultAllId);

                    for (String mandatoryGroup : mandatoryGroupsList) { // RIM, Plannning Champion
                        IGroup iGroup = securityService.getGroup(mandatoryGroup);
                        for (String iaDivisionGroupId : listRemoveDivisions) {
                            if (iGroup != null && iGroup.hasMember(iUser)) {

                                // Validate if auditorGroupName is found
                                for (Map.Entry<String, String> entry : resultAllId.entrySet()) {
                                    if (entry.getKey().equals(iaDivisionGroupId)) {
                                        id = entry.getKey();
                                        break;
                                    }
                                }

                                if (id != null) {
                                    logger.debug("ID --> " + id);
                                    logger.debug(" >>> LeadProductTeamGroup = " + iaDivisionGroupId);
                                    String fieldTableParameter = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD); // Citi-BE:HdAud

                                    logger.debug(" >>> fieldTable = " + fieldTableParameter);
                                    String parameters[] = {fieldTableParameter, removeUser, iaDivisionGroupId};
                                    logger.debug("iUser.getName() --> " + userName);

                                    String query = entitlementEngineRegistry.getProperty(
                                            EntitlementRegistryConstants.ENGINE_METADATA_GENERAL_PRODUCT_TEAM_QUERY);
                                    logger.debug("Query --> " + query);
                                    List<List<String>> queryResult = ServiceUtil.queryService(apiFactory, query,
                                            parameters);

                                    if (queryResult.size() > 0) {

                                        IGRCObject aiDivEntity = iResourceService.getGRCObject(new Id(id));
                                        IField field = aiDivEntity.getField(fieldTableParameter);
                                        logger.debug("IField value " + field.getName());
                                        String users = ServiceUtil.getLabelValueFromField(field);

                                        logger.debug("Backup of Lead Product Member: " + id + " Content: " + users);
                                        userBackUp.setLeadProductTeam(id, users);

                                        logger.debug(" >> start Setting Multi " + fieldTableParameter);

                                        logger.debug("Users from Entity " + users);
                                        users = users.replace((removeUser + ";"), "");
                                        ServiceUtil.setValueFromField(users, field);
                                        logger.debug("Entity removed and updated successfully.");
                                        iResourceService.saveResource(aiDivEntity);

                                        String message = MessageFormat.format(
                                                EntitlementMessageConstants.SUCCESS_LEAD_PRODUCT_TEAM_REMOVE,
                                                resultAllId.get(iaDivisionGroupId), userName);
                                        messageList.add(message);
                                        logger.info(message);
                                    } else {
                                        logger.debug(userName + " is not assigned yet to " + iaDivisionGroupId);
                                    }
                                } else {
                                    String errorMessage = MessageFormat.format(
                                            EntitlementMessageConstants.ERROR_USER_ID_DOES_NOT_BELONG, resultAllId.get(iaDivisionGroupId), iaDivisionGroupLevel[0]);
                                    messageList.add(errorMessage);
                                    logger.debug(errorMessage);
                                }
                                id = null;

                            } else {
                                String message = MessageFormat.format(
                                        EntitlementMessageConstants.ERROR_USER_IS_NOT_ALLOWED, userName,
                                        mandatoryGroup);
                                messageList.add(message);
                                logger.info(message);
                                result = false;
                                break;
                            }
                        }
                    }

                } catch (Exception ex) {
                    String errorMessage = EntitlementMessageConstants.ERROR_MISSING_DATA;
                    messageList.add(errorMessage);
                    logger.error(errorMessage, ex);
                    result = false;
                }
            }
        } else {
            String message = MessageFormat.format(EntitlementMessageConstants.ERROR_FAILED_MODIFY_NON_EXISTENT_USER,
                    dataMap.get(EntitlementConstants.USERNAME));
            messageList.add(message);
            logger.info(message);
        }
        logger.debug("Result: " + result);
        logger.debug("***** FieldServiceImpl - removeUserFromLeadProductTeam END *****");
        return result;

    }
}
