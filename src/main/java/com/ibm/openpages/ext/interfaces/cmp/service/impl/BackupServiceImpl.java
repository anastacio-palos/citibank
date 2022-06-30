package com.ibm.openpages.ext.interfaces.cmp.service.impl;

import com.ibm.openpages.api.configuration.IProfile;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IFolder;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.security.IGroup;
import com.ibm.openpages.api.security.IRoleTemplate;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.GroupBean;
import com.ibm.openpages.ext.interfaces.cmp.bean.RoleAssignmentBean;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementConstants;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementMessageConstants;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementRegistryConstants;
import com.ibm.openpages.ext.interfaces.cmp.service.BackupService;
import com.ibm.openpages.ext.interfaces.cmp.util.EntitlementEngineRegistry;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BackupServiceImpl implements BackupService {
    @Autowired
    EntitlementEngineRegistry entitlementEngineRegistry;
    @Autowired
    FieldServiceImpl fieldService;

    @Override
    public boolean restoreLeadProductTeam(UserBean backUpBean, IServiceFactory apiFactory, Logger logger) {
        logger.debug("--> class: BackupServiceImpl --> method: restoreLeadProductTeam --> START ");
        boolean result = true;
        try {
            //IResourceService service = apiFactory.createResourceService();
            IResourceService resourceService = apiFactory.createResourceService();
            Map<String, String> leadProductTeamFromBackUp = backUpBean.getAllLeadProductTeam();
            logger.debug("Lead Product Team to restore" + leadProductTeamFromBackUp);

            //Map<String, String> currentLeadProductTeam = fieldService.getAllLeadProductTeam(user, apiFactory, logger);
            String user = backUpBean.getUserName();

            //delete content from current LeadProductTeam
            for (Map.Entry<String, String> lastTeamsSet : leadProductTeamFromBackUp.entrySet()) {
                IGRCObject entity = resourceService.getGRCObject(new Id(lastTeamsSet.getKey()));
                IField field = entity.getField(entitlementEngineRegistry
                        .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD));
                logger.debug("IField value " + field.getName());
                String currentUsers = ServiceUtil.getLabelValueFromField(field);
                logger.debug("Users from entity " + currentUsers);
                currentUsers = user + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR + ";" + currentUsers;
                ServiceUtil.setValueFromField(currentUsers, field);
                resourceService.saveResource(entity);
            }


        } catch (Exception e) {
            result = false;
            logger.error(EntitlementMessageConstants.ERROR_RESTORE_USER_INFORMATION_FAILED, e);
        }
        logger.debug("--> class: BackupServiceImpl --> method: restoreLeadProductTeam --> END ");
        return result;
    }

    @Override
    public boolean restoreProductFunctionTeam(UserBean backUpBean, IServiceFactory apiFactory, Logger logger) {
        logger.debug("--> class: BackupServiceImpl --> method: restoreProductFunctionTeam --> START ");
        boolean result = true;
        try {
            IResourceService service = apiFactory.createResourceService();
            IResourceService resourceService = apiFactory.createResourceService();
            Map<String, String> productFunctionTeamFromBackUp = backUpBean.getAllProductFunctionTeam();
            logger.debug("Product Function Team to restore" + productFunctionTeamFromBackUp);
            String entityLevel = entitlementEngineRegistry
                    .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD);
            String fieldTableParameter = entitlementEngineRegistry.getProperty(
                    EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD); // Citi-BE:RIM

            //delete content from current Product Function Team
            String user = backUpBean.getUserName();

            //delete content from current LeadProductTeam
            for (Map.Entry<String, String> lastTeamsSet : productFunctionTeamFromBackUp.entrySet()) {
                IGRCObject entity = resourceService.getGRCObject(new Id(lastTeamsSet.getKey()));
                IField field = entity.getField(entitlementEngineRegistry
                        .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD));
                logger.debug("IField value " + field.getName());
                String currentUsers = ServiceUtil.getLabelValueFromField(field);
                logger.debug("Users from entity " + currentUsers);
                currentUsers = user + EntitlementConstants.MULTI_VALUED_FIELD_SEPARATOR + ";" + currentUsers;
                ServiceUtil.setValueFromField(currentUsers, field);
                resourceService.saveResource(entity);
            }

        } catch (Exception e) {
            result = false;
            logger.error(EntitlementMessageConstants.ERROR_RESTORE_USER_INFORMATION_FAILED, e);
        }
        logger.debug("--> class: BackupServiceImpl --> method: restoreProductFunctionTeam --> END ");
        return result;
    }

    @Override
    public boolean restoreUserData(UserBean userToRestore, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        boolean result = false;
        logger.debug(" --> class: BackupServiceImpl --> method: restoreUserData --> START");
        logger.debug("UserBean content " + userToRestore.toString());
        // Restore Groups
        result = restoreUserGroups(userToRestore.getUserName(), userToRestore.getGroups(), apiFactory, logger);
        result = restoreUserInformation(userToRestore, apiFactory, logger);
        result = restoreLeadProductTeam(userToRestore, apiFactory, logger);
        result = restoreProductFunctionTeam(userToRestore, apiFactory, logger);
        logger.debug(" --> class: BackupServiceImpl --> method: restoreUserData --> END");
        return result;
    }

    /**
     * @param userId
     * @param groups
     * @param apiFactory
     * @param logger
     * @return
     */
    private boolean restoreUserGroups(String userId, List<GroupBean> groups, IServiceFactory apiFactory, Logger logger) {
        boolean result = true;
        ISecurityService service = apiFactory.createSecurityService();
        logger.debug("--> class: BackupServiceImpl --> method: restoreUserGroups --> START ");
        try {
            IUser iUser = service.getUser(userId);
            logger.debug("iUser: " + iUser.getName());

            logger.debug("Groups to be restored: " + groups.toString());
            for (GroupBean group : groups) {
                IGroup iGroup = service.getGroup(group.getGroupName());
                logger.debug("Group restored " + group.getGroupName() + " to " + iUser.getName());
                if (!iGroup.hasMember(iUser))
                    iGroup.addMember(iUser);
            }
            if (!iUser.isEnabled()) {
                iUser.enable();
            }
        } catch (Exception e) {
            logger.error(EntitlementMessageConstants.ERROR_RESTORE_GROUPS_FAILED, e);
            result = false;
        }
        logger.debug("--> class: BackupServiceImpl --> method: restoreUserGroups --> END");
        return result;
    }

    /**
     * @param userId
     * @param roles
     * @param iServiceFactory
     * @param logger
     * @return
     */
    private boolean restoreUserRoles(String userId, List<RoleAssignmentBean> roles, IServiceFactory iServiceFactory, Logger logger) {
        boolean result = true;
        logger.debug("--> class: BackupServiceImpl --> method: restoreUserRoles --> START ");
        // Add try catch to change flag state.

        try {

            ISecurityService securityService = iServiceFactory.createSecurityService();
            IResourceService resourceService = iServiceFactory.createResourceService();
            IUser theUser = securityService.getUser(userId);
            for (RoleAssignmentBean roleAssignmentBean : roles) {
                IRoleTemplate iRole = securityService.getRoleTemplate(roleAssignmentBean.getRoleTemplate());
                IFolder iFolder = resourceService.getFolder(roleAssignmentBean.getFolderPath());
                securityService.assignRole(theUser, iRole, iFolder);
            }
        } catch (Exception e) {
            logger.error(EntitlementMessageConstants.ERROR_RESTORE_ROLES_FAILED, e);
            result = false;
        }
        logger.debug("--> class: BackupServiceImpl --> method: restoreUserRoles --> END");
        return result;
    }

    /**
     * @param userBackedUp
     * @param iServiceFactory
     * @param logger
     * @return
     */
    private boolean restoreUserInformation(UserBean userBackedUp, IServiceFactory iServiceFactory, Logger logger) {
        logger.debug("--> class: BackupServiceImpl --> method: restoreUserInformation --> START ");
        boolean result = true;
        try {
            IUser originalUser = iServiceFactory.createSecurityService().getUser(userBackedUp.getUserName());
            IConfigurationService confgrServ = iServiceFactory.createConfigurationService();

            originalUser.setFirstName(userBackedUp.getFirstName());
            originalUser.setEmailAddress(userBackedUp.getEmailAddress());
            originalUser.setLastName(userBackedUp.getLastName());
            logger.debug("UserBack is enabled? " + userBackedUp.isEnabled());
            if (userBackedUp.isEnabled()) {
                originalUser.enable();
            } else {
                originalUser.disable();
            }
            logger.debug("After enable/disable originalUser is " + originalUser.isEnabled());
            List<IProfile> originalProfiles = new ArrayList<>();
            for (String backUpProf : userBackedUp.getAvailableProfileNames()) {
                IProfile profile = confgrServ.getProfile(backUpProf);
                originalProfiles.add(profile);// "OpenPages RCM Master",
            }
            List<IProfile> currentProfiles = confgrServ.getProfiles(originalUser);// ["OpenPages RCM Master","Citi
            // Master"],
            // currentProfiles.removeAll(originalProfiles);//"Citi Master"
            List<Id> profileIds = new ArrayList<>();
            for (IProfile currentProfile : currentProfiles) {
                if (!userBackedUp.getAvailableProfileNames().contains(currentProfile.getName())) {
                    profileIds.add(currentProfile.getId());
                    logger.debug("List profile ID disassociate " + currentProfile.getName());
                }
            }
            confgrServ.disassociateProfilesFromUser(new Id(userBackedUp.getId()), profileIds);
            originalUser.update();
        } catch (Exception e) {
            result = false;
            logger.error(EntitlementMessageConstants.ERROR_RESTORE_USER_INFORMATION_FAILED, e);
        }
        logger.debug("--> class: BackupServiceImpl --> method: restoreUserInformation --> START ");
        return result;
    }

    /**
     * @param userBackedUp
     * @param iServiceFactory
     * @param logger
     * @return
     */
    private boolean restoreIaDivision(UserBean userBackedUp, IServiceFactory iServiceFactory, Logger logger) {
        logger.debug("--> class: BackupServiceImpl --> method: restoreIaDivision --> START ");
        boolean result = true;
        try {
            IResourceService service = iServiceFactory.createResourceService();
            Map<String, String> allIaDivisions = userBackedUp.getAllLeadProductTeam();

            for (Map.Entry<String, String> entry : allIaDivisions.entrySet()) {
                IGRCObject entity = service.getGRCObject(new Id(entry.getKey()));

                IField field = entity.getField(entitlementEngineRegistry
                        .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD));
                logger.debug("IField value " + field.getName());
                String currentUsers = entry.getValue();
                logger.debug("Users from entity " + currentUsers);
                ServiceUtil.setValueFromField(currentUsers, field);
                logger.debug("IA Division Entity updated");
                service.saveResource(entity);
                String message = MessageFormat.format(EntitlementMessageConstants.SUCCESS_RESTORE_IA_DIVISION, entry.getKey(),
                        entry.getValue());
                logger.info(message);
            }

        } catch (Exception e) {
            result = false;
            logger.error(EntitlementMessageConstants.ERROR_RESTORE_USER_INFORMATION_FAILED, e);
        }
        logger.debug("--> class: BackupServiceImpl --> method: restoreIaDivision --> END ");
        return result;
    }

    /**
     * @param userToRestore
     * @param iServiceFactory
     * @param logger
     * @return
     */
    private boolean restoreAuditGroups(UserBean userToRestore, IServiceFactory iServiceFactory, Logger logger) {
        logger.debug("--> class: BackupServiceImpl --> method: restoreAuditGroups --> START ");
        boolean result = true;
        try {
            IResourceService service = iServiceFactory.createResourceService();
            Map<String, String> all_Auditors = userToRestore.getAllProductFunctionTeam();

            for (Map.Entry<String, String> entry : all_Auditors.entrySet()) {
                IGRCObject entity = service.getGRCObject(new Id(entry.getKey()));

                IField field = entity.getField(entitlementEngineRegistry
                        .getProperty(EntitlementRegistryConstants.ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD));
                logger.debug("IField value " + field.getName());
                String currentUsers = entry.getValue();
                logger.debug("Users from entity " + currentUsers);
                ServiceUtil.setValueFromField(currentUsers, field);
                logger.debug("Entity Auditor Groups updated");
                service.saveResource(entity);
                String message = MessageFormat.format(EntitlementMessageConstants.SUCCESS_RESTORE_IA_DIVISION, entry.getKey(),
                        entry.getValue());
                logger.info(message);
            }

        } catch (Exception e) {
            result = false;
            logger.error(EntitlementMessageConstants.ERROR_RESTORE_USER_INFORMATION_FAILED, e);
        }
        logger.debug("--> class: BackupServiceImpl --> method: restoreAuditGroups --> END ");
        return result;
    }
}
