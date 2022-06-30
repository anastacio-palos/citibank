package com.ibm.openpages.ext.interfaces.cmp.service.impl;

import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.EntitlementResponseBean;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import com.ibm.openpages.ext.interfaces.cmp.constant.EntitlementConstants;
import com.ibm.openpages.ext.interfaces.cmp.service.*;
import com.ibm.openpages.ext.interfaces.cmp.util.EntitlementEngineRegistry;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntitlementServiceImpl implements EntitlementService {

    @Autowired
    EntitlementEngineRegistry entitlementEngineRegistry;
    @Autowired
    UserService userService;
    @Autowired
    BackupService backupService;
    @Autowired
    GroupService groupService;
    @Autowired
    FieldService fieldService;

    @Override
    public EntitlementResponseBean createEntitlement(EngineResultsBean dataEngineBean, List<String> messageList,
                                                     IServiceFactory apiFactory, Logger logger) {
        UserBean userBackUp = null;
        logger.debug("***** EntitlementServiceImpl - createEntitlement START *****");
        logger.debug(" >>> incoming dataEngineBean {" + dataEngineBean.toString() + "}");
        logger.debug(" >>> incoming messageList {" + messageList.toString() + "}");
        EntitlementResponseBean entitlementResponseBean = new EntitlementResponseBean();

        boolean result = false;
        boolean wasCreate = false;
        for (String dataEngineBeanMessage : dataEngineBean.getMessages()) {
            messageList.add(dataEngineBeanMessage);
        }

        if (dataEngineBean.isSuccess()) {
            String action = (String) dataEngineBean.getDataValues().get(EntitlementConstants.ACTION);
            action = action.toUpperCase();

            logger.debug(">>>  User saved as BackUp ");
            userBackUp = userService.getUserBean(dataEngineBean.getDataValues(), messageList, apiFactory, logger);

            if (action != null && action.equals(EntitlementConstants.ACTION_DELETE)) {
                userBackUp = userService.getUserBean(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                result = userService.deleteUser(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
            } else {
                if (dataEngineBean.isSuccess() && action.equals(EntitlementConstants.ACTION_CREATE)) {
                    logger.debug("Getting into create action process.");
                    result = userService.createUser(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                    userBackUp = userService.getUserBean(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                    wasCreate = true;
                } else if (dataEngineBean.isSuccess() && action.equals(EntitlementConstants.ACTION_MODIFY)) {
                    userBackUp = userService.getUserBean(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                    result = userService.updateUser(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                }
                if (result) {
                    result = groupService.removeGroupsCountry(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                }
                if (result) {
                    if (userBackUp == null) {
                        userBackUp = userService.getUserBean(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                    }
                    result = fieldService.removeUserFromLeadProductTeam(dataEngineBean.getDataValues(), messageList, apiFactory,
                            userBackUp, logger);
                }
                if (result) {
                    result = fieldService.removeUserFromProductFunctionTeam(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                }
                if (result) {
                    result = groupService.removeRoles(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                }
                if (result) {
                    result = groupService.addRoles(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                }
                if (result) {
                    result = groupService.addGroupsCountry(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                }

                if (result) {
                    result = fieldService.addUserToLeadProductTeam(dataEngineBean.getDataValues(), messageList, apiFactory,
                            logger);
                }
                if (result) {
                    result = fieldService.addUserToProductFunctionTeam(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                }

            }
            if (!result) {
                if (userBackUp != null) {
                    logger.debug("***** EntitlementServiceImpl - Starting Restore process *****");
                    // clear content and lock
                    //if scenario comes from create and failed, it needs to disable user since it was not exist.
                    if (wasCreate) {
                        userBackUp.setEnabled(false);
                    }
                    userService.deleteUser(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                    backupService.restoreUserData(userBackUp, messageList, apiFactory, logger);
                    entitlementResponseBean.setStatus("Error");

                } else {
                    logger.debug("No backup needed since it comes from NEW and this scenario failed.");
                    entitlementResponseBean.setStatus("Error");
                }
            } else {
                entitlementResponseBean.setStatus("Success");
            }
        } else {
            entitlementResponseBean.setStatus("Error");
        }

        if (entitlementResponseBean.getStatus().contains("Success")) {
            String separator = " ";
            int lastElement = messageList.size() - 1;
            for (int i = 0; i < messageList.size(); i++) {
                if (i == lastElement) {
                    entitlementResponseBean.getComments().append(messageList.get(i));
                } else {
                    entitlementResponseBean.getComments().append(messageList.get(i) + separator);
                }
            }
        } else {
            entitlementResponseBean.getComments().append(messageList.get(messageList.size() - 1));
        }
        entitlementResponseBean.setUserId((String) dataEngineBean.getDataValues().get(EntitlementConstants.USERNAME));
        logger.debug("Last Comment --> " + messageList.get(messageList.size() - 1));
        logger.info(" >>> Entitlement Result: " + result);
        logger.debug(" >>> Outgoing messageList{" + messageList + "}");
        logger.debug("***** EntitlementServiceImpl - createEntitlement END *****");
        return entitlementResponseBean;
    }

}
