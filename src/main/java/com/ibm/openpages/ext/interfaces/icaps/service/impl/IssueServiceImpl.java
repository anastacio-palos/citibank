package com.ibm.openpages.ext.interfaces.icaps.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.*;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.*;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import com.ibm.openpages.ext.interfaces.icaps.bean.IssueResponseBean;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueMessageConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueRegistryConstants;
import com.ibm.openpages.ext.interfaces.icaps.persistence.ManagedSegmentDAO;
import com.ibm.openpages.ext.interfaces.icaps.service.CapService;
import com.ibm.openpages.ext.interfaces.icaps.service.IssueService;
import com.ibm.openpages.ext.interfaces.icaps.util.IssueEngineRegistry;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

@Service
public class IssueServiceImpl implements IssueService {
    @Autowired
    private IssueEngineRegistry issueEngineRegistry;
    @Autowired
    private CapService capService;
    private Map<String,String> issueFieldPropertyMap;


    /**
     * This method retrieves IGRCObject from openpages if it already exists,
     * otherwise return null.
     *
     * @param dataMap     includes information from payload.
     * @param messageList list to save messages
     * @param apiFactory  service to use current session from openpages
     * @param logger      object used to set error and debug messages in log file.
     * @return IRGCObject found in openpages.
     */
    @Override
    public IGRCObject getIssue(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
                               Logger logger) {
        logger.trace("***** IssueServiceImpl - getIssue START *****");
        IGRCObject resultObject = null;
        IssueConstants.init();
        String name = dataMap.get(IssueConstants.NAME) != null ? (String) dataMap.get(IssueConstants.NAME) : "";
        logger.trace("Name = " + name);
        String id = ServiceUtil.queryValue(apiFactory,
                issueEngineRegistry.getProperty(IssueRegistryConstants.GET_ISSUE_QUERY), name);
        logger.trace("id = " + id);
        if (id != null) {
            IResourceService resourceService = apiFactory.createResourceService();
            resultObject = resourceService.getGRCObject(new Id(id));
        }
        logger.trace("***** IssueServiceImpl - getIssue END *****");
        return resultObject;
    }

    /**
     * Create a new issued based on elements saved in dataMap (payload).
     *
     * @param dataMap     includes information from payload.
     * @param messageList list to save messages
     * @param apiFactory  service to use current session from openpages
     * @param logger      object used to set error and debug messages in log file.
     * @return flag to determine if process was created successfully.
     */
    @Override
    public boolean createIssue(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
                               Logger logger) {
        logger.trace("***** IssueServiceImpl - createIssue START *****");
        IResourceService iResourceService = apiFactory.createResourceService();
        IResourceFactory iResourceFactory = iResourceService.getResourceFactory();
        IMetaDataService iMetaDataService = apiFactory.createMetaDataService();
        //String dataType = issueEngineRegistry.getProperty(IssueRegistryConstants.GET_ISSUE_TYPE);
        IGRCObject issueObject = null;
        String Message;
        try {
            // Generate properties list from IssueConstants including primaryParentId
            List<String> properties = setProperty();
            ITypeDefinition typeToCreate = iMetaDataService.getType(IssueCommonConstants.CITI_ISS_TYPE);
            issueObject = iResourceFactory.createGRCObject((String) dataMap.get(IssueConstants.NAME), typeToCreate);
            issueObject.setDescription((String) dataMap.get(IssueConstants.ISSUE_DESCRIPTION));
            //Setting source of issue to Regulatory.
            IField regField = issueObject.getField(IssueCommonConstants.ISSUE_SOURCE_FIELD);
            String regulatoryVal = IssueCommonConstants.REGULATORY_VALUE;
            ServiceUtil.setValueFromField(regulatoryVal,regField);
            ObjectMapper mapper = new ObjectMapper();

            for (String property : properties) {
                IField field = issueObject.getField(property);
                String fieldValue = (String) dataMap.get(property);
                boolean isEnumValid = false;
                logger.trace("Property value " + property);
                logger.trace("Field value " + field.getName());
                if (fieldValue != null && IssueConstants.listOfUserFields.contains(property) && !fieldValue.isEmpty()) {
                    if (!validateUserExistAndIsActive(fieldValue, apiFactory, logger, messageList)) {
                        String message = MessageFormat.format(IssueMessageConstants.INVALID_USER_FOR_ITEM,
                                fieldValue, field.getName());
                        messageList.add(message);
                        logger.info(message);
                        return false;
                    }
                }
                if (fieldValue != null && field instanceof IEnumField && !fieldValue.toString().isEmpty() &&
                        !(property.equals(IssueConstants.ISSUE_STATUS) || property.equals(IssueConstants.ISSUE_DETAILED_STATUS))) {
                    // if field is enum, overwrite fieldValue value with value from database
                    IFieldDefinition iFieldDefinition = field.getFieldDefinition();
                    List<IEnumValue> allEnumValues = iFieldDefinition.getEnumValues();
                    Iterator<IEnumValue> iterator = allEnumValues.iterator();
                    //logger.trace("Enum values of field " + field.getName());
                    while (iterator.hasNext()) {
                        IEnumValue current = iterator.next();
                        logger.trace("--> " + current.getLocalizedLabel());
                        if (current.getLocalizedLabel().equals(fieldValue)) {
                            isEnumValid = true;
                            logger.trace("Both values are " + fieldValue);
                            fieldValue = current.getName();
                            break;
                        }
                    }
                    if (!isEnumValid) {
                        Message = MessageFormat.format(IssueMessageConstants.INVALID_INPUT_VALUE,
                                dataMap.get(field.getName()), issueFieldPropertyMap.get(field.getName()), issueObject.getName());
                        messageList.add(Message);
                        logger.info(Message);
                        return false;
                    }
                }
                if (property.equals(IssueConstants.ISSUE_DETAILED_STATUS)) {
                    fieldValue = calculateIssueStatus(dataMap, logger);
                    field = issueObject.getField(IssueConstants.ISSUE_STATUS);
                    /*if (fieldValue == null) {
                        Message = MessageFormat.format(IssueMessageConstants.INVALID_INPUT_COMBINATION,
                                (String) dataMap.get(IssueConstants.ISSUE_STATUS), (String) dataMap.get(IssueConstants.ISSUE_DETAILED_STATUS), issueObject.getName());
                        messageList.add(Message);
                        logger.info(Message);
                        return false;
                    }*/
                }

                logger.trace("ObjectField ----> " + field.getName() );
                logger.trace("Field value to save: " + fieldValue);
                ServiceUtil.setValueFromField(fieldValue, field);
            }
            ServiceUtil.setValueFromField(IssueConstants.TRUE_VALUE,issueObject.getField(IssueConstants.ALREADY_NAMED_BY_CALCULATION));
            logger.trace(" *********  object was created ******************+");
            // ID (DSMT Filtering by parent) -> OpenPages
            String[] arrSplited = null;
            try {
                ManagedSegmentDAO managedSegmentDAO = new ManagedSegmentDAO(apiFactory, logger);
                List<String> hierarchyList = managedSegmentDAO.getHierarchy((String) dataMap.get(IssueConstants.ICAPSMNGSEG_DSMT));
                logger.trace("First id from the hierarchy: " + hierarchyList.toString());
                arrSplited = hierarchyList.toString().split("\\|");
                logger.trace("Split Array: " + Arrays.toString(arrSplited));
            } catch (Exception e) {
                logger.error("Error ", e);
            }
            Stack<String> stack = new Stack<>();

            for (int i = 0; i < arrSplited.length; i++) {
                stack.push(arrSplited[i]);
            }
            String businessEntityId = "";
            String parentId = retrieveDSMTId(stack, dataMap, logger);// Technology
            logger.trace("ParentID retrieved: " + parentId);
            if (!parentId.equals("1")) {
                businessEntityId = ServiceUtil.queryValue(apiFactory, IssueConstants.CITY_ENTITY_QUERY, parentId);
            } else {
                businessEntityId = issueEngineRegistry.getProperty(IssueRegistryConstants.DSMT_NOT_FOUND);
            }
            logger.trace("Id from business entity: " + businessEntityId);
            if (businessEntityId == null || businessEntityId.isEmpty()) {
                Message = MessageFormat.format(IssueMessageConstants.BUSINESS_ENTITY_NOT_FOUND,
                        dataMap.get(IssueConstants.NAME));
                messageList.add(Message);
                logger.info(Message);
                return false;
            }
            String primaryParentId = ServiceUtil.queryValue(apiFactory, IssueConstants.CITY_AUDIT_PLAN_QUERY,
                    businessEntityId);
            if (primaryParentId == null) {
                Message = MessageFormat.format(IssueMessageConstants.AUDIT_PLAN_NOT_FOUND,
                        dataMap.get(IssueConstants.NAME));
                messageList.add(Message);
                logger.info(Message);
                return false;
            }
            IGRCObject templateObject = iResourceService.getGRCObject(new Id(primaryParentId));
            issueObject.setPrimaryParent(templateObject);
            IGRCObject savedObject = iResourceService.saveResource(issueObject);
            dataMap.put(IssueConstants.PRIMARY_PARENT_ID, savedObject.getPrimaryParent());
            dataMap.put(IssueConstants.ISSUE_ID, savedObject.getId());

            logger.trace("object Id: " + savedObject.getId());
            Message = MessageFormat.format(IssueMessageConstants.SUCCESS_CREATED_ISSUE,
                    dataMap.get(IssueConstants.NAME));
            messageList.add(Message);
            logger.trace(Message);

        } catch (Exception e) {
            logger.error("Error ", e);
        }
        logger.trace("***** IssueServiceImpl - createIssue END *****");
        return true;
    }

    /**
     * Update issue information with new data set in dataMap (payload).
     *
     * @param dataMap     includes information from payload.
     * @param messageList list to save messages
     * @param apiFactory  service to use current session from openpages
     * @param logger      object used to set error and debug messages in log file.
     * @return flag to determine if process was success.
     */

    @Override
    public boolean updateIssue(IGRCObject Issue, Map<String, Object> dataMap, List<String> messageList,
                               IServiceFactory apiFactory, Logger logger) {
        logger.trace("***** IssueServiceImpl - updateIssue START *****");

        IResourceService iResourceService = apiFactory.createResourceService();
        boolean sameDSMT = false;
        boolean result = false;
        String message;
        try {

            IGRCObject updateIssue = Issue;
            updateIssue.setDescription((String) dataMap.get(IssueConstants.ISSUE_DESCRIPTION));
            // Generate properties list from IssueConstants excluding primaryParentId since
            // it is update
            List<String> properties = setProperty();
            logger.trace("Propertied loaded for new Issue:\n" + properties);
            // issueObject = iResourceFactory.createGRCObject((String)
            // dataMap.get(IssueConstants.NAME), typeToCreate);

            //remove dsmt field since update of dsmt is not valid.
            properties.remove(IssueConstants.ICAPSMNGSEG_DSMT);


            // update fields
            ObjectMapper mapper = new ObjectMapper();
            for (String property : properties) {
                IField field = updateIssue.getField(property);
                String fieldValue = (String) dataMap.get(property);
                boolean isEnumValid = false;
                logger.trace("Property value: " + property);
                logger.trace("Field value: " + fieldValue);
                logger.trace("Value before update: " + ServiceUtil.getLabelValueFromField(field));

                if ((fieldValue != null && !fieldValue.isEmpty()) && IssueConstants.listOfUserFields.contains(property)) {
                    if (!validateUserExistAndIsActive(fieldValue, apiFactory, logger, messageList)) {
                        message = MessageFormat.format(IssueMessageConstants.INVALID_USER_FOR_ITEM,
                                fieldValue, field.getName());
                        messageList.add(message);
                        logger.info(message);
                        return false;
                    }
                }

                //for issue status and detail status skip validation against openpages
                if (fieldValue != null && field instanceof IEnumField && !fieldValue.isEmpty() &&
                        !(property.equals(IssueConstants.ISSUE_STATUS) || property.equals(IssueConstants.ISSUE_DETAILED_STATUS))) {
                    // if field is enum, overwrite fieldValue value with value from database
                    IFieldDefinition iFieldDefinition = field.getFieldDefinition();
                    List<IEnumValue> allEnumValues = iFieldDefinition.getEnumValues();
                    Iterator<IEnumValue> iterator = allEnumValues.iterator();
                    logger.trace("Enum values of field " + field.getName());
                    while (iterator.hasNext()) {
                        IEnumValue current = iterator.next();
                        //logger.trace("Values to compare\n Translated: " + fieldValue.toString() + " and " + current.getLocalizedLabel());
                        logger.trace("Localized --> " + current.getLocalizedLabel());
                        logger.trace("Name --> " + current.getName());

                        if (current.getLocalizedLabel().equals(fieldValue)) {
                            isEnumValid = true;
                            logger.trace("Both values are " + fieldValue);
                            fieldValue = current.getName();
                            break;

                        }
                    }
                    if (!isEnumValid) {
                        message = MessageFormat.format(IssueMessageConstants.INVALID_INPUT_VALUE, dataMap.get(field.getName()),
                                issueFieldPropertyMap.get(field.getName()), updateIssue.getName());
                        messageList.add(message);
                        logger.info(message);
                        return false;
                    }

                }


                if (property.equals(IssueConstants.ISSUE_DETAILED_STATUS)) {
                    fieldValue = calculateIssueStatus(dataMap, logger);
                    logger.trace("Issue fieldValue " + fieldValue);
                    field = updateIssue.getField(IssueConstants.ISSUE_STATUS);
                    logger.trace("field: " + field.getName());
                    /*if (fieldValue == null) {
                        //this combination is not set or allowed by registry Issue_Map
                        logger.trace("Issue fieldValue is null");
                        message = MessageFormat.format(IssueMessageConstants.INVALID_INPUT_COMBINATION,
                                (String) dataMap.get(IssueConstants.ISSUE_STATUS), (String) dataMap.get(IssueConstants.ISSUE_DETAILED_STATUS), updateIssue.getName());
                        messageList.add(message);
                        logger.info(message);
                        return false;
                    }*/
                }
                logger.trace("ObjectField ----> " + field.getName() );
                logger.trace("Field value to save: " + fieldValue);
                ServiceUtil.setValueFromField(fieldValue, field);
            }
            ServiceUtil.setValueFromField(IssueConstants.TRUE_VALUE,updateIssue.getField(IssueConstants.ALREADY_NAMED_BY_CALCULATION));

            logger.trace("Parent Id to be linked with " + updateIssue.getPrimaryParent().toString());

            IGRCObject savedObject = iResourceService.saveResource(updateIssue);
            logger.info("  >>>>>>>>>>>>>  Object Id updated >>>>>>>>>>>>>>>>");
            dataMap.put(IssueConstants.ISSUE_ID, savedObject.getId());
            logger.info("object Id: " + savedObject.getId());

            /*
            // disassociate Old ActionPlan
            if (!sameDSMT) {
                logger.trace("Executing disassociation of old Action Plan");
                List<Id> parent = new ArrayList<>();
                Id oldActPl = (Id) dataMap.get(IssueConstants.OLD_PRIMARY_PARENT_ID);
                logger.trace("Old Action Plan to disassociate " + oldActPl);
                parent.add(oldActPl);
                List<Id> children = new ArrayList<>();
                iResourceService.dissociate(savedObject.getId(), parent, children);
                logger.trace(
                        "Old Action Plan [" + oldActPl + "] disassociated from Issue [" + savedObject.getId() + "]");
            }
            */


            message = MessageFormat.format(IssueMessageConstants.SUCCESS_UPDATED_ISSUE,
                    dataMap.get(IssueConstants.NAME));
            messageList.add(message);
            logger.info(message);
            result = true;

        } catch (Exception e) {
            logger.error("Error: ", e);
        }
        logger.trace("***** IssueServiceImpl - updateIssue END *****");
        // TODO
        return result;
    }

    private String calculateIssueStatus(Map<String, Object> dataMap, Logger logger) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        logger.trace("Property was Issue Detail Status");
        String issueStatus = (String) dataMap.get(IssueConstants.ISSUE_STATUS);
        String issueDetStatus = (String) dataMap.get(IssueConstants.ISSUE_DETAILED_STATUS);
        logger.trace("Values to compare: Status [" + issueStatus + "] Detail Status [" + issueDetStatus + "]");
        Map<String, Map<String, String>> issueStatusMap = mapper.readValue(issueEngineRegistry.getProperty(IssueRegistryConstants.ISSUE_STATUS_MAP), Map.class);
        logger.trace("Issue Detailed Status Map " + issueStatusMap);
        String fieldValue = null;

        logger.trace("Map contains " + issueStatus + ": " + issueStatusMap.containsKey(issueStatus));
        if (issueStatusMap.containsKey(issueStatus)) {
            Map<String, String> issueDetStatusMap = issueStatusMap.get(issueStatus);
            if (issueDetStatusMap.containsKey(issueDetStatus))
                fieldValue = issueDetStatusMap.get(issueDetStatus);
        }
        return fieldValue;
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
            logger.trace("Error ", e);
        }
        logger.trace("***** IssueServiceImpl - validateUserExistAndIsActive END *****");

        return result;
    }

    /**
     * Determines if an issue and it cap(s) will be created or updated.
     *
     * @param dataEngineBean includes information of first validation from payload.
     * @param messageList    list to save messages
     * @param apiFactory     service to use current session from openpages
     * @param logger         object used to set error and debug messages in log
     *                       file.
     * @return IssueResponseBean object set properly
     */
    @Override
    public IssueResponseBean createOrUpdateIssueAndCAP(EngineResultsBean dataEngineBean, List<String> messageList,
                                                       IServiceFactory apiFactory, Logger logger) {
        logger.trace("***** IssueServiceImpl - createOrUpdateIssueAndCAP START *****");
        //logger.trace(" >>> incoming dataEngineBean {" + dataEngineBean.toString() + "}");
        logger.trace(" >>> incoming messageList {" + messageList.toString() + "}");

        IssueResponseBean issueResponseBean = new IssueResponseBean();
        IResourceService rss = apiFactory.createResourceService();
        IConfigurationService configService = apiFactory.createConfigurationService();
        IMetaDataService iMetaDataService = apiFactory.createMetaDataService();
        issueFieldPropertyMap = issueEngineRegistry.putEngineMetadata(IssueRegistryConstants.ENGINE_ISSUE_METADATA_JSON_FILE).getFieldPropertyMap();

        boolean result = false;
        try {
            for (String dataEngineBeanMessage : dataEngineBean.getMessages()) {
                messageList.add(dataEngineBeanMessage);
            }
            if (dataEngineBean.isSuccess()) {
                IGRCObject issueObject = getIssue(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                if (issueObject == null) {
                    // New Issue
                    result = this.createIssue(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                    issueObject = getIssue(dataEngineBean.getDataValues(), messageList, apiFactory, logger);
                } else {
                    // Modify existing Issue
                    result = this.updateIssue(issueObject, dataEngineBean.getDataValues(), messageList, apiFactory,
                            logger);
                }
                if (result) {
                    dataEngineBean.getDataValues().put(IssueConstants.ISSUE_ID, issueObject.getId());
                    List<Id> listOfIssueChildren = getChildrenFromIssue(issueObject, apiFactory, logger);
                    List<String> controlCapList = new ArrayList<>();
                    List<Object> listOfCaps = (List<Object>) dataEngineBean.getDataValues()
                            .get(IssueConstants.LIST_OF_CAPS);
                    if (listOfCaps != null) {
                        for (Object inboundCap : listOfCaps) {
                            Map<String, Object> inboundCapDataMap = (Map<String, Object>) inboundCap;
                            logger.trace(("Starting process with CAPs"));

                            if(IssueCommonConstants.YES_VALUE.equals(inboundCapDataMap.get(IssueConstants.IS_SUBSCRIBED_CAP))) {
                                inboundCapDataMap.put(IssueConstants.CAP_NAME, dataEngineBean.getDataValues().get(IssueConstants.NAME) + "-" + inboundCapDataMap.get(IssueConstants.CAP_NAME));
                                logger.debug("Subscribed CAP Name:" + inboundCapDataMap.get(IssueConstants.CAP_NAME));
                            }
                            // Retrieve from openpages cap with name matching with json cap name.

                            // IGRCObject currentCap = rss.getGRCObject((String)
                            // dataMapOfCap.get(IssueConstants.CAP_NAME));

                            IGRCObject existingCapObject = capService.getCorrectiveActionPlan(inboundCapDataMap, messageList,
                                    apiFactory, logger);
                            /*
                             * String currentCapID = ServiceUtil.queryValue(apiFactory,
                             * issueEngineRegistry.getProperty(IssueRegistryConstants.GET_CAP_QUERY),
                             * (String) dataMapOfCap.get(IssueConstants.CAP_NAME));
                             */

                            if (existingCapObject != null) {
                                if (controlCapList.contains(existingCapObject.getName())) {
                                    String errorMessage = MessageFormat.format(
                                            IssueMessageConstants.CAP_DUPLICATED_IN_SAME_TRANSACTION, existingCapObject.getName());
                                    messageList.add(errorMessage);
                                    logger.trace(errorMessage);
                                    result = false;
                                }
                                controlCapList.add(existingCapObject.getName());
                                if (!listOfIssueChildren.contains(existingCapObject.getId())) {
                                    // if cap is already created and is associated to another child, skip the
                                    // process and add warning message.
                                    String errorMessage = MessageFormat.format(
                                            IssueMessageConstants.CAP_ASSIGNED_OTHER_ISSUE, existingCapObject.getName());
                                    messageList.add(errorMessage);
                                    logger.trace(errorMessage);
                                    // ERROR
                                    result = false;
                                } else {
                                    // cap exists and owns to same issue.
                                    inboundCapDataMap.put(IssueConstants.CAP_ID, existingCapObject.getId());
                                    if (result)
                                        result = capService.updateCorrectiveActionPlan(inboundCapDataMap, messageList,
                                                apiFactory, logger);

                                }
                            } else {
                                // CAP is new and needs to be associated after creation
                                result = capService.createCorrectiveActionPlan(issueObject, inboundCapDataMap, messageList, apiFactory,
                                        logger);
                                /*if (result) {
                                    capsToAssociate.add((Id) capDataMap.get(IssueConstants.CAP_ID));
                                    associatesCapWithParent(issueObject, capsToAssociate, parentsToAssociate, apiFactory,
                                            logger);
                                }*/
                            }
                        }
                    }

                }

            }
            logger.trace("Setting status for response");
            issueResponseBean.setStatus("Success");
            if (!result) {
                issueResponseBean.setStatus("Error");
            }

            for (String message : messageList) {
                issueResponseBean.getComments().append(message + ". ");
            }
            logger.info(" >>> Entitlement Result: " + result);
            logger.trace(" >>> Outgoing messageList{" + messageList + "}");
            logger.trace("***** IssueServiceImpl - createOrUpdateIssueAndCAP END *****");

        } catch (Exception ex) {
            result = false;
            logger.error(ex.getMessage(), ex);
        }

        return issueResponseBean;
    }

    /**
     * @param issueObject receive the IGRCObject issue to get all children
     *                    associated from this object.
     * @param apiFactory  service to use current session from openpages
     * @param logger      object used to set error and debug messages in log file.
     * @return List of children
     */
    private List<Id> getChildrenFromIssue(IGRCObject issueObject, IServiceFactory apiFactory, Logger logger) {
        List<Id> childIds = new ArrayList<>();
        IConfigurationService configService = apiFactory.createConfigurationService();
        IMetaDataService iMetaDataService = apiFactory.createMetaDataService();
        IResourceService rss = apiFactory.createResourceService();
        GRCObjectFilter filter = new GRCObjectFilter(configService.getCurrentReportingPeriod());
        filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.CHILD);
        filter.getAssociationFilter().setTypeFilters(
                iMetaDataService.getType(IssueCommonConstants.CITI_CAP_TYPE));

        Id issueId = issueObject.getId();
        IGRCObject returnObject = rss.getGRCObject(issueId, filter);
        logger.trace("IGRCObject Name " + returnObject.getName());
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("NAME", returnObject.getName());
        objectMap.put("DESCRIPTION", returnObject.getDescription());
        objectMap.put("PATH", returnObject.getPath());
        List<IAssociationNode> child = returnObject.getChildren();

        for (IAssociationNode currentChild : child) {
            childIds.add(currentChild.getId());
        }
        return childIds;
    }

    /**
     * @param parentIssueObject object target to be associated with children and parents
     * @param capsToAssociate   List of children Ids to associate with object
     * @param parentsToAssociate    Listo of parents Ids to associate with object
     * @param apiFactory service to use current session from openpages
     * @param logger     object used to set error and debug messages in log file.
     */
    private void associatesCapWithParent(IGRCObject parentIssueObject, List<Id> capsToAssociate, List<Id> parentsToAssociate,
                                         IServiceFactory apiFactory, Logger logger) {
        logger.trace("***** IssueServiceImpl - associateCapWitParent START *****");
        logger.trace("Associating CAP and Issue after was created.");
        IResourceService iResourceService = apiFactory.createResourceService();
        //rss.associate(issueObject.getId(), parentsToAssociate, capsToAssociate);
        capsToAssociate.stream().forEach(capId->{

            logger.trace("CAP Id to associate [" + capId + "] with parent ISSUE [" + parentIssueObject.getId().toString() + "]");
            IGRCObject cap = iResourceService.getGRCObject(capId);
            logger.trace("CAP path [" + cap.getPath() + "] with parent ISSUE path [" + parentIssueObject.getPath().toString() + "]");
            cap.setPrimaryParent(parentIssueObject);
            logger.trace("new CAP path [" + cap.getPath() + "]");
            iResourceService.saveResource(cap);
        });




    }

    /**
     * find the element id that matches with openpages and provided from dsmt.
     *
     * @param stack   has all elements to be compared against map from registry.
     * @param dataMap includes information from payload.
     * @param logger  object used to set error and debug messages in log file.
     * @return element matched between dsmt and openpages
     */
    private String retrieveDSMTId(Stack stack, Map dataMap, Logger logger) {
        ObjectMapper mapper = new ObjectMapper();
        String result = "1";
        try {
            Map<String, String> dsmtMap =
                    mapper.readValue(issueEngineRegistry.getProperty(IssueRegistryConstants.DSMT_MAP), Map.class);

            logger.trace("Generated Map: " + dsmtMap);
            String currentKey = (String) stack.pop();

            while (!currentKey.equals("1")) {
                logger.trace("Checking key: " + currentKey);
                if (dsmtMap.containsKey(currentKey)) {
                    return dsmtMap.get(currentKey);
                }
                currentKey = (String) stack.pop();
            }

        } catch (Exception e) {
            logger.error("Error ", e);
        }

        return result;
    }

    /**
     * @return list of properties to be set for each issue.
     */
    private List<String> setProperty() {
        List<String> properties = new ArrayList<>();
        properties.add(IssueConstants.ISSUE_SUMMARY);
        properties.add(IssueConstants.ISSUE_OWNER);
        //properties.add(IssueConstants.ISSUE_AUDIT_OWNER);
        //properties.add(IssueConstants.ISSUE_AUDIT_CONTROLLER);
        properties.add(IssueConstants.ISSUE_ACTION_COMMENT);
        //properties.add(IssueConstants.ISSUE_LEAD_AUDITOR);
        properties.add(IssueConstants.ISSUE_CREATE_DATE);
        properties.add(IssueConstants.ISSUE_TYPE_LEVEL_1);
        properties.add(IssueConstants.ISSUE_TYPE_LEVEL_2);
        properties.add(IssueConstants.ISSUE_TYPE_LEVEL_3);
        properties.add(IssueConstants.ISSUE_TYPE_LEVEL_4);
        properties.add(IssueConstants.REASON_FOR_FAILED_VALIDATION);
        properties.add(IssueConstants.AUDIT_ENTITY_ID);
        properties.add(IssueConstants.AUDIT_ENTITY_NAME);
        //properties.add(IssueConstants.LEAD_PRODUCT_TEAM);
        properties.add(IssueConstants.IA_FUNCTION);
        properties.add(IssueConstants.RE_OPENED_ISSUE_COUNT);
        properties.add(IssueConstants.RE_OPENED_BY_REGULATOR_COUNT);
        properties.add(IssueConstants.IA_VALIDATION_DATE);
        properties.add(IssueConstants.ISSUE_BUSINESS_TARGET_DATE);
        properties.add(IssueConstants.REVISED_ISSUE_BUSINESS_TARGET_DATE);
        properties.add(IssueConstants.ISSUE_BUSINESS_COMPLETION_DATE);
        properties.add(IssueConstants.REGULATOR_RE_OPEN_DATE);
        properties.add(IssueConstants.ISSUE_REOPEN_DATE);
        properties.add(IssueConstants.ISSUE_CURRENT_TARGET_DATE);
        properties.add(IssueConstants.PRIMARY_ROOT_CAUSE_LEVEL_1);
        properties.add(IssueConstants.PRIMARY_ROOT_CAUSE_LEVEL_2);
        properties.add(IssueConstants.SOURCE_LEVEL_2);
        properties.add(IssueConstants.RISK_LEVEL_1);
        properties.add(IssueConstants.RISK_LEVEL_2);
        properties.add(IssueConstants.RISK_LEVEL_3);
        properties.add(IssueConstants.RISK_LEVEL_4);
        properties.add(IssueConstants.CONTROL_LEVEL_1);
        properties.add(IssueConstants.GRC_ACTIVITYCAT1);
        properties.add(IssueConstants.GRC_ACTIVITYCAT2);
        properties.add(IssueConstants.GRC_ACTIVITYCAT3);
        properties.add(IssueConstants.ICAPSOWNERMNGSEGMENTID);
        properties.add(IssueConstants.ICAPSEXAMID);
        properties.add(IssueConstants.ICAPSEXAMNAME);
        properties.add(IssueConstants.ICAPSEXAMAGENCY);
        properties.add(IssueConstants.ICAPSCOMMITAGENCY);
        properties.add(IssueConstants.ICAPSENFORCEAGENCY);
        properties.add(IssueConstants.ICAPSRETCLASS);
        properties.add(IssueConstants.ICAPSREPEATMRA);
        properties.add(IssueConstants.ICAPSMNGSEG_DSMT);
        properties.add(IssueConstants.ICAPSMNGGEO);
        properties.add(IssueConstants.ICAPSLEGVEH);
        properties.add(IssueConstants.ICAPSASSIGNEDFUNC);
        properties.add(IssueConstants.ICAPSENFACTTITLE);
        properties.add(IssueConstants.ICAPSIDENTDATE);
        properties.add(IssueConstants.CLOSUREDATE);
        properties.add(IssueConstants.ISSUE_SOURCE_LEVEL_1);
        properties.add(IssueConstants.SOURCE_SYSTEM);
        properties.add(IssueConstants.ICAPSSEVRATING);
        properties.add(IssueConstants.INITVALTARGETDATE);
        properties.add(IssueConstants.REVISEDVALTARGETDATE);
        properties.add(IssueConstants.ICAPSREJECTEDIAORREG);
        properties.add(IssueConstants.ICAPSRETARGETRAT);
        properties.add(IssueConstants.ICAPSREGCLOSEREQ);
        properties.add(IssueConstants.ICAPSVALIDATIONDAYS);
        properties.add(IssueConstants.ICAPSREGFINDINGID);
        properties.add(IssueConstants.ICAPSVALREQ);
        properties.add(IssueConstants.ICAPSENFTYPE);
        properties.add(IssueConstants.ICAPSCOMMITTITLE);
        properties.add(IssueConstants.ICAPSISSCOUNTRY);
        properties.add(IssueConstants.ICAPSNUMRETARGETS);
        properties.add(IssueConstants.ICAPSRISKPRIMARY);
        properties.add(IssueConstants.ICAPSMGE);
        properties.add(IssueConstants.ICAPSASSESSUNIT);


        // Both
        properties.add(IssueConstants.ICAPSCOORDINATORS);
        properties.add(IssueConstants.ACTIVATIONDATE);
        properties.add(IssueConstants.ISSUE_STATUS);
        properties.add(IssueConstants.ISSUE_DETAILED_STATUS);

        return properties;
    }

}
