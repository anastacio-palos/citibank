package com.ibm.openpages.ext.interfaces.icaps.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IEnumField;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IResourceFactory;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueMessageConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueRegistryConstants;
import com.ibm.openpages.ext.interfaces.icaps.service.CapService;
import com.ibm.openpages.ext.interfaces.icaps.util.IssueEngineRegistry;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CapServiceImpl implements CapService {
    @Autowired
    private IssueEngineRegistry issueEngineRegistry;
    private Map<String,String> capFieldPropertyMap;

    @Override
    public IGRCObject getCorrectiveActionPlan(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.trace("***** CapServiceImpl - getCorrectiveActionPlan START *****");

        IGRCObject resultObject = null;
        IssueConstants.init();
        String name = dataMap.get(IssueConstants.CAP_NAME) != null ? (String) dataMap.get(IssueConstants.CAP_NAME) : "";
        logger.trace("Name = " + name);
        String id = ServiceUtil.queryValue(apiFactory, issueEngineRegistry.getProperty(IssueRegistryConstants.GET_CAP_QUERY), name);
        logger.trace("id = " + id);
        if (id != null) {
            logger.trace("Id " + id + " already exist.");
            IResourceService resourceService = apiFactory.createResourceService();
            resultObject = resourceService.getGRCObject(new Id(id));
        }
        logger.trace("***** CapServiceImpl - getCap END *****");
        return resultObject;

    }

    @Override
    public boolean createCorrectiveActionPlan(IGRCObject parentIssueObject, Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.trace("***** CAPServiceImpl - createCorrectiveActionPlan START *****");
        if(issueEngineRegistry != null)
            capFieldPropertyMap = issueEngineRegistry.putEngineMetadata(IssueRegistryConstants.ENGINE_CAP_METADATA_JSON_FILE).getFieldPropertyMap();
        IResourceService iResourceService = apiFactory.createResourceService();
        IResourceFactory iResourceFactory = iResourceService.getResourceFactory();
        IMetaDataService iMetaDataService = apiFactory.createMetaDataService();
        boolean result = true;
        String message;

        try {
            //String dataType = issueEngineRegistry.getProperty(IssueRegistryConstants.GET_CAP_TYPE);

            ITypeDefinition typeToCreate = iMetaDataService.getType(IssueCommonConstants.CITI_CAP_TYPE);

            String capId = (String)dataMap.get(IssueConstants.CAP_NAME);
            logger.trace("Cap Name Description --> " + capId);
            IGRCObject newObjectCap = iResourceFactory.createGRCObject(capId, typeToCreate);
            logger.trace(" ************** Before Set CAP data *****************************");
            newObjectCap.setDescription(capId);
            logger.trace("Value of CAP Name before setting value:  " + newObjectCap.getName());
            newObjectCap.setName(capId);
            logger.trace("Value of CAP Name AFTER setting value:  " + newObjectCap.getName());
            newObjectCap.setPrimaryParent(parentIssueObject);

            List<String> properties = setCapProperty();
            logger.trace("All CAP Properties loaded: \n" + properties);
            // for status will be two scenarios
            // for Action_Plan_Status : Citi-Cap:Status the status will be translated from
            // map on registry
            // for ICAPS_Cap_Status : Citi-CAP:ICAPSts the status will be saved with
            // original value.
            ObjectMapper mapper = new ObjectMapper();

            for (String property : properties) {
                logger.trace("Field ----> " + property);
                IField field = newObjectCap.getField(property);
                String fieldValue = (String) dataMap.get(property);
                boolean isEnumValid = false;
                if (property.equals(IssueConstants.PLANNING_CAP)) {
                    Map<String, String> planningCAPMap = mapper.readValue(issueEngineRegistry.getProperty(IssueRegistryConstants.PLANNING_CAP_MAP), Map.class);
                    fieldValue = planningCAPMap.get(fieldValue);
                }


                if (IssueConstants.listOfUserFields.contains(property) && !fieldValue.isEmpty()) {
                    logger.trace("Property value " + property);
                    logger.trace("Field value " + fieldValue);
                    if (!validateUserExistAndIsActive(fieldValue, apiFactory, logger, messageList)) {
                        message = MessageFormat.format(IssueMessageConstants.INVALID_USER_FOR_ITEM, fieldValue, field.getName());
                        messageList.add(message);
                        logger.info(message);
                        result = false;
                        break;
                    }
                }

                if (field instanceof IEnumField && !(property.equals(IssueConstants.ICAPS_CAP_STATUS) || property.equals(IssueConstants.CAP_DETAIL_STATUS))) {
                    // if field is enum, overwrite fieldValue value with value from database
                    IFieldDefinition iFieldDefinition = field.getFieldDefinition();
                    List<IEnumValue> allEnumValues = iFieldDefinition.getEnumValues();
                    Iterator<IEnumValue> iterator = allEnumValues.iterator();

                    //logger.trace("Enum values of field " + field.getName());
                    while (iterator.hasNext()) {
                        IEnumValue current = iterator.next();
                        logger.trace("  --> " + current.getLocalizedLabel());
                        if (current.getLocalizedLabel().equals(fieldValue)) {
                            isEnumValid = true;
                            logger.trace("Both values are " + fieldValue);
                            break;
                        }
                    }
                    if (!isEnumValid) {
                        message = MessageFormat.format(IssueMessageConstants.INVALID_INPUT_VALUE, dataMap.get(field.getName()), capFieldPropertyMap.get(field.getName()), newObjectCap.getName());
                        messageList.add(message);
                        logger.info(message);
                        result = false;
                    }
                }
                //CAP_ACTION_PLAN_STATUS - Cap Detailed Status
                //ICAPS_CAP_STATUS - Cap Status
                if (property.equals(IssueConstants.ICAPS_CAP_STATUS)) {//todo check
                    fieldValue = calculateCAPStatus(dataMap, logger);
                    //field = newObjectCap.getField(IssueConstants.ICAPS_CAP_STATUS);
                    if (fieldValue == null) {
                        message = MessageFormat.format(IssueMessageConstants.INVALID_INPUT_COMBINATION, dataMap.get(IssueConstants.ICAPS_CAP_STATUS), dataMap.get(IssueConstants.CAP_DETAIL_STATUS), newObjectCap.getName());
                        messageList.add(message);
                        logger.info(message);
                        return false;
                    }
                }
                logger.trace("Assigning a new value to ----> " + field.getName() + "=" + fieldValue);

                ServiceUtil.setValueFromField(fieldValue, field);
            }
            if(IssueCommonConstants.YES_VALUE.equals(dataMap.get(IssueConstants.IS_SUBSCRIBED_CAP))) {
                ServiceUtil.setValueFromField(IssueCommonConstants.YES_VALUE, newObjectCap.getField(IssueConstants.IS_SUBSCRIBED_CAP));
                logger.debug("--> This is a Subscribed CAP");
            } else
                ServiceUtil.setValueFromField(IssueCommonConstants.NO_VALUE, newObjectCap.getField(IssueConstants.IS_SUBSCRIBED_CAP));
            ServiceUtil.setValueFromField(IssueConstants.TRUE_VALUE,newObjectCap.getField(IssueConstants.ALREADY_NAMED_BY_CALCULATION));
            // newObjectCap.setName((String) dataMap.get(IssueConstants.CAP_NAME));
            if (result) {
                newObjectCap = iResourceService.saveResource(newObjectCap);
                logger.trace(" *********  object was created ******************+");

                message = MessageFormat.format(IssueMessageConstants.SUCCESS_CREATED_CAP, dataMap.get(IssueConstants.CAP_NAME));
                messageList.add(message);
                logger.info(message);

                logger.trace("CAP created with ID [" + newObjectCap.getId().toString() + "]");
                Id cap = newObjectCap.getId();
                logger.trace("Saving ID [" + cap.toString() + "] to associate later");
                dataMap.put(IssueConstants.CAP_ID, cap);
                logger.trace("CapObject Name: " + newObjectCap.getName());

            }
            logger.trace("***** CapServiceImpl - createCorrectiveActionPlan END *****");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean updateCorrectiveActionPlan(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger) {
        logger.trace("***** CapServiceImpl - updateCorrectiveActionPlan START *****");
        if(issueEngineRegistry != null)
            capFieldPropertyMap = issueEngineRegistry.putEngineMetadata(IssueRegistryConstants.ENGINE_CAP_METADATA_JSON_FILE).getFieldPropertyMap();
        IResourceService iResourceService = apiFactory.createResourceService();
        ObjectMapper mapper = new ObjectMapper();
        boolean result = true;
        String message;

        boolean sameDSMT = false;
        try {
            logger.trace("DataMap content: " + dataMap);

            Id CapId = (Id) dataMap.get(IssueConstants.CAP_ID);
            logger.trace("Updating Existing CAP [" + CapId.toString() + "]");
            IGRCObject updateObjectCap = iResourceService.getGRCObject(CapId);
            logger.trace(" ************** Before Set CAP data *****************************");
            updateObjectCap.setDescription((String) dataMap.get(IssueConstants.CAP_NAME));

            logger.trace("ObjectCap -->" + updateObjectCap.getName() + " " + updateObjectCap.getId() + " " + updateObjectCap.getDescription());
            List<String> properties = setCapProperty();
            logger.trace("All CAP Properties loaded: \n" + properties);

            for (String property : properties) {
                logger.trace("Field ----> " + property);
                IField field = updateObjectCap.getField(property);
                String fieldValue = (String) dataMap.get(property);
                boolean isEnumValid = false;
                if (property.equals(IssueConstants.PLANNING_CAP)) {
                    Map<String, String> planningCAPMap = mapper.readValue(issueEngineRegistry.getProperty(IssueRegistryConstants.PLANNING_CAP_MAP), Map.class);
                    fieldValue = planningCAPMap.get(fieldValue);
                }
                logger.trace("Property value " + property);
                logger.trace("Field value " + fieldValue);
                if (IssueConstants.listOfUserFields.contains(property) && !fieldValue.isEmpty()) {
                    if (!validateUserExistAndIsActive(fieldValue, apiFactory, logger, messageList)) {
                        message = MessageFormat.format(IssueMessageConstants.INVALID_USER_FOR_ITEM, fieldValue, field.getName());
                        messageList.add(message);
                        logger.info(message);
                        result = false;
                        break;
                    }
                }

//todo check
                if (field instanceof IEnumField && !(property.equals(IssueConstants.ICAPS_CAP_STATUS) || property.equals(IssueConstants.CAP_DETAIL_STATUS))) {
                    // if field is enum, overwrite fieldValue value with value from database
                    IFieldDefinition iFieldDefinition = field.getFieldDefinition();
                    List<IEnumValue> allEnumValues = iFieldDefinition.getEnumValues();
                    Iterator<IEnumValue> iterator = allEnumValues.iterator();

                    //logger.trace("Enum values of field " + field.getName());
                    while (iterator.hasNext()) {
                        IEnumValue current = iterator.next();
                        logger.trace("Values to compare\nOriginal: '" + fieldValue + "' and '" + current.getLocalizedLabel() + "'");
                        logger.trace("  --> " + current.getLocalizedLabel());
                        if (current.getLocalizedLabel().equals(fieldValue)) {
                            fieldValue = current.getName();
                            isEnumValid = true;
                            logger.trace("Both values are " + fieldValue);
                            break;
                        }
                    }
                    // In this section, we are going to skip the cap detail status since it does not exist in openpages.
                    if (!isEnumValid) {
                        message = MessageFormat.format(IssueMessageConstants.INVALID_INPUT_VALUE, dataMap.get(field.getName()), capFieldPropertyMap.get(field.getName()), updateObjectCap.getName());
                        messageList.add(message);
                        logger.info(message);
                        result = false;
                    }
                }

                //CAP_DETAIL_STATUS - Cap Detailed Status
                //ICAPS_CAP_STATUS - Cap Status
                if (property.equals(IssueConstants.ICAPS_CAP_STATUS)) {
                    fieldValue = calculateCAPStatus(dataMap, logger);
                    //field = updateObjectCap.getField(IssueConstants.ICAPS_CAP_STATUS);
                    if (fieldValue == null) {
                        message = MessageFormat.format(IssueMessageConstants.INVALID_INPUT_COMBINATION, dataMap.get(IssueConstants.ICAPS_CAP_STATUS), dataMap.get(IssueConstants.CAP_DETAIL_STATUS), updateObjectCap.getName());
                        messageList.add(message);
                        logger.info(message);
                        return false;
                    }
                }


                logger.trace("ObjectField ----> " + fieldValue);
                // for status will be two scenarios
                // for Action_Plan_Status : Citi-Cap:Status the status will be translated from
                // map on registry
                // for ICAPS_Cap_Status : Citi-CAP:ICAPSts the status will be saved with
                // original value.

                ServiceUtil.setValueFromField(fieldValue, field);
            }
            ServiceUtil.setValueFromField(IssueConstants.TRUE_VALUE,updateObjectCap.getField(IssueConstants.ALREADY_NAMED_BY_CALCULATION));
            IGRCObject savedObject;
            if (result) {
                savedObject = iResourceService.saveResource(updateObjectCap);
                logger.trace(" *********  object was updated ******************+");

                message = MessageFormat.format(IssueMessageConstants.SUCCESS_UPDATED_CAP, dataMap.get(IssueConstants.CAP_NAME));
                messageList.add(message);
                logger.info(message);
                logger.trace("CapObject: " + savedObject.getId());
                logger.trace("CapObject: " + savedObject.getName());
                logger.trace("***** CapServiceImpl - updateCorrectiveActionPlan END *****");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;

    }

    private boolean validateUserExistAndIsActive(String userName, IServiceFactory apiFactory, Logger logger, List<String> messageList) {
        logger.trace("***** IssueServiceImpl - validateUserExistAndIsActive START *****");
        boolean result = false;
        IUser iUser = null;
        ISecurityService service = apiFactory.createSecurityService();
        logger.trace("userName entered " + userName);
        try {
            iUser = service.getUser(userName);

            if (iUser != null) {
                logger.trace("user found " + iUser.getName());
                if (iUser.isLocked()) {
                    String message = MessageFormat.format(IssueMessageConstants.USER_IS_LOCKED, userName);
                    messageList.add(message);
                    logger.trace(message);
                } else {
                    logger.trace("User not locked!");
                    result = true;
                }
            }
        } catch (Exception e) {
            logger.trace(e.getMessage());
        }
        logger.trace("***** IssueServiceImpl - validateUserExistAndIsActive END *****");

        return result;
    }

    private List<String> setCapProperty() {
        List<String> properties = new ArrayList<>();
        properties.add(IssueConstants.PLANNING_CAP);
        properties.add(IssueConstants.CAP_OWNER);
        properties.add(IssueConstants.CAP_CREATE_DATE);
        properties.add(IssueConstants.CAP_DESCRIPTION);
        properties.add(IssueConstants.CAP_ACTION_PLAN_NUMBER);
        properties.add(IssueConstants.ICAPS_CAP_STATUS);
        //properties.add(IssueConstants.CAP_DETAIL_STATUS);
        properties.add(IssueConstants.CAP_ACTION_PLAN_STATUS);
        //properties.add(IssueConstants.CAP_BUSINESS_TARGET_DATE);
        properties.add(IssueConstants.CAP_COMPLETION_DATE);
        properties.add(IssueConstants.CAP_DATECHGAWTVALSTATUS);
        // Both, each one with it own header.

        properties.add(IssueConstants.ICAPSCAPCREATEDATE);
        properties.add(IssueConstants.ICAPSCAPCOORDINATORS);
        properties.add(IssueConstants.ICAPSCAPBUSTARGETDATE);
        properties.add(IssueConstants.ICAPSCAPREVBUSTARGETDATE);
        properties.add(IssueConstants.ICAPSCAPACTIVATIONDATE);
        // Only CAP
        properties.add(IssueConstants.ICAPSDSMTMANAGEDSEGMENT);
        properties.add(IssueConstants.ICAPSCAPVALREQ);
        properties.add(IssueConstants.ICAPSCAPNUMVALDAYS);
        properties.add(IssueConstants.CAP_BUSINESS_COMPLETION_DATE);
        properties.add(IssueConstants.CAP_COMMENT);
        properties.add(IssueConstants.IS_SUBSCRIBED_CAP);

        return properties;
    }

    private String calculateCAPStatus(Map<String, Object> dataMap, Logger logger) throws IOException {
        logger.trace("**********  Getting into calculateCAPStatus   ---  Start   ************");
        //CAP_DETAIL_STATUS - Cap Detailed Status
        //ICAPS_CAP_STATUS - Cap Status


        ObjectMapper mapper = new ObjectMapper();
        logger.trace("Property was CAP Detail Status");
        String capStatus = (String) dataMap.get(IssueConstants.ICAPS_CAP_STATUS);
        String capDetStatus = (String) dataMap.get(IssueConstants.CAP_DETAIL_STATUS);
        logger.trace("Values to compare: Status [" + capStatus + "] Detail Status [" + capDetStatus + "]");
        Map<String, Map<String, String>> capStatusMap = mapper.readValue(issueEngineRegistry.getProperty(IssueRegistryConstants.CAP_STATUS_MAP), Map.class);
        logger.trace("CAP Status Map " + capStatusMap);
        String fieldValue = null;

        logger.trace("Map contains " + capStatus + ": " + capStatusMap.containsKey(capStatus));

        if (capStatusMap.containsKey(capStatus)) {
            Map<String, String> detailedStatusMap = capStatusMap.get(capStatus);
            logger.trace("CAP Detailed Status Map " + detailedStatusMap);
            logger.trace("Map contains " + capDetStatus + ": " + detailedStatusMap.containsKey(capDetStatus));
            if (detailedStatusMap.containsKey(capDetStatus)) fieldValue = detailedStatusMap.get(capDetStatus);
        }
        logger.trace("fieldValue assigned: " + fieldValue);
        return fieldValue;

    }
}
