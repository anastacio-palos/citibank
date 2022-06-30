package com.ibm.openpages.ext.interfaces.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.bean.ConstraintBean;
import com.ibm.openpages.ext.interfaces.common.bean.EngineMetadataBean;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import com.ibm.openpages.ext.interfaces.common.bean.FieldBean;
import com.ibm.openpages.ext.interfaces.common.constant.EngineConstants;
import com.ibm.openpages.ext.interfaces.common.constant.MessageConstants;
import com.ibm.openpages.ext.interfaces.common.service.EngineService;
import com.ibm.openpages.ext.interfaces.common.util.EngineRegistry;
import com.ibm.openpages.ext.interfaces.common.util.EngineTransformationUtil;
import com.ibm.openpages.ext.interfaces.common.util.EngineUtil;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * This is the implementation of the {@link EngineService} interface.
 * </p>
 *
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 07-10-2021
 */
@Service
public class EngineServiceImpl implements EngineService {

    @Override
    public EngineResultsBean validateAndProcessPayload(String data, EngineRegistry engineRegistry,
                                                       String metadataMainPropertyName, String[] metadataProperties,
                                                       IServiceFactory apiFactory, Logger logger) {
        logger.trace("***** EngineService - validateAndProcessPayload START *****");
        logger.trace("metadataMainPropertyName=" + metadataMainPropertyName);

        EngineResultsBean dataEngineBean = new EngineResultsBean();
        boolean result = true;
        ObjectMapper dataMapper = new ObjectMapper();
        if (data != null) {
            logger.trace("data=" + data);
            try {
                Map<String, Object> dataMap = dataMapper.readValue(data, Map.class);
                EngineMetadataBean metadata = null;
                for (String metadataProperty : metadataProperties) {
                    EngineMetadataBean metadataTemp = engineRegistry.putEngineMetadata(metadataProperty);
                    if (metadataProperty.equals(metadataMainPropertyName))
                        metadata = metadataTemp;
                }
                result = processEngineMetadataValidation(dataMap, engineRegistry,
                        dataEngineBean.getMessages(), dataEngineBean.getDataValues(), metadata, logger);
                dataEngineBean.setSuccess(result);
            } catch (IOException e) {
                String message = MessageConstants.ERROR_FAILED_JSON_PROCESSING;
                logger.warn(message, e);
                dataEngineBean.getMessages().add(message);
                result = false;
            }
        } else {
            dataEngineBean.getMessages().add(MessageConstants.ERROR_MISSING_DATA);
        }
        dataEngineBean.setSuccess(result);

        logger.debug(" >>> Engine Result: " + dataEngineBean);
        logger.trace("***** EngineService - validateAndProcessPayload END *****");
        return dataEngineBean;
    }

    @Override
    public EngineResultsBean validateAndProcessPayload(String data, EngineRegistry engineRegistry, String engineMetadataJsonProperty,
                                                       IServiceFactory apiFactory, Logger logger) {
        logger.trace("***** EngineService - validateAndProcessPayload START *****");
        logger.trace("data=" + data);
        logger.trace("engineMetadataJsonProperty=" + engineMetadataJsonProperty);
        EngineResultsBean dataEngineBean = new EngineResultsBean();
        boolean result = true;
        ObjectMapper dataMapper = new ObjectMapper();
        if (data != null) {
            try {
                Map<String, Object> dataMap = dataMapper.readValue(data, Map.class);
                EngineMetadataBean metadata = engineRegistry.putEngineMetadata(engineMetadataJsonProperty);
                result = processEngineMetadataValidation(dataMap, engineRegistry,
                        dataEngineBean.getMessages(), dataEngineBean.getDataValues(), metadata, logger);
                dataEngineBean.setSuccess(result);
            } catch (IOException e) {
                String message = MessageConstants.ERROR_FAILED_JSON_PROCESSING;
                logger.warn(message, e);
                dataEngineBean.getMessages().add(message);
                result = false;
            }
        } else {
            dataEngineBean.getMessages().add(MessageConstants.ERROR_MISSING_DATA);
        }
        dataEngineBean.setSuccess(result);

        logger.info(" >>> Engine Result: " + dataEngineBean);
        logger.trace("***** EngineService - validateAndProcessPayload END *****");
        return dataEngineBean;
    }

    public boolean processEngineMetadataValidation(Map<String, Object> dataMap, EngineRegistry engineRegistry,
                                                   //EngineResultsBean dataEngineBean,
                                                   List<String> messages, Map<String, Object> dataValues,
                                                   EngineMetadataBean metadata,
                                                   Logger logger) {
        logger.trace("***** EngineService - processEngineMetadata START *****");
        boolean result = true;
        try {

            if (metadata != null) {
                for (FieldBean field : metadata.getFields()) {
                    if (!processEngineFieldValidation(field, dataMap, messages, dataValues, engineRegistry, logger))
                        result = false;
                }
            } else {
                String message = MessageConstants.ERROR_NO_METADATA;
                logger.info(message);
                messages.add(message);
                result = false;
            }
        } catch (Exception e) {
            String message = MessageConstants.ERROR_FAILED_VALIDATION;
            logger.warn(message, e);
            messages.add(message);
            result = false;
        }
        logger.trace("***** EngineService - processEngineMetadata - result = " + result + "*****");
        logger.trace("***** EngineService - processEngineMetadata END *****");
        return result;
    }

    public boolean processEngineFieldValidation(FieldBean field, Map<String, Object> dataMap, //EngineResultsBean dataEngineBean,
                                                List<String> messages, Map<String, Object> dataValues,
                                                EngineRegistry engineRegistry, Logger logger) {
        logger.trace("***** EngineService - processEngineField START *****");
        logger.trace("Processing field = " + field.toString());
        boolean result = true;
        Object dataValue = dataMap.get(field.getName());
        //unbounded
        int maxOccurs = 0;
        boolean unbounded = false;
        boolean isList = false;
        //unbounded
        if (!EngineConstants.UNBOUNDED_VALUE.equals(field.getMaxOccurs())) {
            maxOccurs = Integer.parseInt(field.getMaxOccurs());
            if (maxOccurs > 1)
                isList = true;
        } else {
            unbounded = true;
            isList = true;
        }
        logger.trace(
                "unbounded =" + unbounded +
                        "\n" +
                        "isList =" + isList
        );

        //minOcurrs
        if (!isList && field.getMinOccurs() > 0 && dataValue == null) {
            String message = MessageFormat.format(MessageConstants.ERROR_MISSING_FIELD, field.getName());
            logger.info(message);
            messages.add(message);
            result = false;
        }

        if (isList && field.getMinOccurs() > 0 && dataValue instanceof List && ((List<String>) dataValue).size() < field.getMinOccurs()) {
            String message = MessageFormat.format(MessageConstants.ERROR_INVALID_MIN_SIZE, field.getName());
            logger.info(message);
            messages.add(message);
            result = false;
        }
        //maxOcurrs
        if (isList && !unbounded && dataValue instanceof List && ((List<String>) dataValue).size() > maxOccurs) {
            String message = MessageFormat.format(MessageConstants.ERROR_INVALID_MAX_SIZE, field.getName());
            logger.info(message);
            messages.add(message);
            result = false;
        }
        if (field.getType().equals(EngineConstants.STRING_TYPE)) {
            if (field.getRestriction() != null) {
                if (field.getDefaultValue() != null && !field.getDefaultValue().isEmpty() &&
                        (dataValue == null || (dataValue instanceof String && ((String) dataValue).isEmpty()))) {
                    dataValue = field.getDefaultValue();
                }
                //pattern
                if (EngineConstants.STRING_TYPE.equals(field.getType()) && !isList &&
                        field.getRestriction().getPattern() != null && dataValue instanceof String && !((String) dataValue).matches(field.getRestriction().getPattern())) {
                    String message = MessageFormat.format(MessageConstants.ERROR_INVALID_FORMAT, field.getName(), field.getRestriction().getPattern());
                    logger.info(message);
                    messages.add(message);
                    result = false;
                }
                //minlength
                if (EngineConstants.STRING_TYPE.equals(field.getType()) && !isList &&
                        field.getRestriction().getMinLength() > 0 && dataValue instanceof String && ((String) dataValue).length() < field.getRestriction().getMinLength()) {
                    String message = MessageFormat.format(MessageConstants.ERROR_INVALID_MIN_SIZE, field.getName());
                    logger.info(message);
                    messages.add(message);
                    result = false;
                }
                //maxlength
                if (EngineConstants.STRING_TYPE.equals(field.getType()) && !isList &&
                        field.getRestriction().getMaxLength() > 0 && dataValue instanceof String && ((String) dataValue).length() > field.getRestriction().getMaxLength()) {
                    String message = MessageFormat.format(MessageConstants.ERROR_INVALID_MAX_SIZE, field.getName());
                    logger.info(message);
                    messages.add(message);
                    result = false;
                }
                //constraints
                if (field.getRestriction().getConstraints() != null) {
                    for (ConstraintBean c : field.getRestriction().getConstraints()) {
                        if (c.getConstraintKey() != null && !c.getConstraintKey().isEmpty()) {
                            ConstraintBean constraint = engineRegistry.putConstraint(c.getConstraintKey(),
                                    c.getConstraintRegistryProperty(), c.getConstraintType());
                            logger.trace("constraint = " + constraint.toString());
                            if (constraint.getConstraintKey() != null && !constraint.getConstraintKey().isEmpty() &&
                                    constraint.getConstraintRegistryProperty() != null && !constraint.getConstraintRegistryProperty().isEmpty() &&
                                    constraint.getConstraintType() != null && !constraint.getConstraintType().isEmpty()
                            ) {
                                if (EngineConstants.FIELD_LIST_TYPE.equals(constraint.getConstraintType())) {
                                    List registryConstraintList = (List<String>) dataMap.get(constraint.getConstraintRegistryProperty());
                                    logger.trace("registryConstraintList=" + registryConstraintList.toString());

                                    if (!registryConstraintList.contains(constraint.getConstraintKey()) && dataValue != null && (
                                            isList && dataValue instanceof List && !((List<String>) dataValue).isEmpty() ||
                                                    !isList && dataValue instanceof String && !((String) dataValue).isEmpty())) {
                                        String message = MessageFormat.format(MessageConstants.ERROR_USER_IS_NOT_ALLOWED_GENERIC, field.getName(), constraint.getConstraintKey(), constraint.getConstraintRegistryProperty());
                                        logger.info(message);
                                        messages.add(message);
                                        result = false;
                                    }
                                } else {
                                    if (EngineConstants.LIST_TYPE.equals(constraint.getConstraintType())) {
                                        List registryConstraintList = (List) constraint.getConstraintData();
                                        logger.trace("registryConstraintList= " + registryConstraintList.toString());
                                        if (!isList) {
                                            if (!registryConstraintList.contains(dataValue)) {
                                                String message = MessageFormat.format(MessageConstants.ERROR_FIELD_CONSTRAINT, field.getName(), dataValue);
                                                logger.info(message);
                                                messages.add(message);
                                                result = false;
                                            }
                                        } else {
                                            for (String currentData : ((List<String>) dataValue)) {
                                                if (!registryConstraintList.contains(currentData)) {
                                                    String message = MessageFormat.format(MessageConstants.ERROR_FIELD_CONSTRAINT, field.getName(), currentData);
                                                    logger.info(message);
                                                    messages.add(message);
                                                    result = false;
                                                }
                                            }
                                        }

                                    } else if (EngineConstants.MAP_TYPE.equals(constraint.getConstraintType()) ||
                                            EngineConstants.QUERY_MAP_TYPE.equals(constraint.getConstraintType())) {
                                        Map<String, String> registryConstraintMap = (Map<String, String>) constraint.getConstraintData();
                                        logger.trace("registryConstraintMap= " + registryConstraintMap.toString());
                                        if (!isList) {
                                            if (registryConstraintMap.get(dataValue) == null) {
                                                String message = MessageFormat.format(MessageConstants.ERROR_FIELD_CONSTRAINT, field.getName(), dataValue);
                                                logger.info(message);
                                                messages.add(message);
                                                result = false;
                                            }
                                        } else {
                                            if (dataValue != null) {
                                                for (String currentData : ((List<String>) dataValue)) {
                                                    if (registryConstraintMap.get(currentData) == null) {
                                                        String message = MessageFormat.format(MessageConstants.ERROR_FIELD_CONSTRAINT, field.getName(), currentData);
                                                        logger.info(message);
                                                        messages.add(message);
                                                        result = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (field.getMapTo() != null && !field.getMapTo().isEmpty())
                dataValues.put(field.getMapTo(), dataValue);
        } else if (field.getType().equals(EngineConstants.DATE_TYPE)) {
            if (EngineConstants.DATE_TYPE.equals(field.getType()) && !isList
                    && dataValue instanceof String) {
                if (dataValue != null && !((String) dataValue).isEmpty()) {
                    if (field.getMapTo() != null && !field.getMapTo().isEmpty()) {
                        Date date = null;
                        String pattern = EngineConstants.EMPTY_VALUE;
                        try {
                            if (field.getRestriction() != null && field.getRestriction().getPattern() != null
                                    && !field.getRestriction().getPattern().isEmpty()) {
                                pattern = field.getRestriction().getPattern();
                            } else {
                                pattern = EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN;
                            }
                            date = new SimpleDateFormat(pattern).parse((String) dataValue);
                            Format f = new SimpleDateFormat(EngineConstants.OPENPAGES_DATE_PATTERN);
                            String value = f.format(date);
                            dataValues.put(field.getMapTo(), value);
                        } catch (Exception e) {
                            String message = MessageFormat.format(MessageConstants.ERROR_INVALID_FORMAT, field.getName(), pattern);
                            logger.info(message);
                            messages.add(message);
                            result = false;
                        }
                    }
                } else {
                    if (field.getRestriction() != null &&
                            field.getRestriction().getMinLength() > 0) {
                        String message = MessageFormat.format(MessageConstants.ERROR_INVALID_MIN_SIZE, field.getName());
                        logger.info(message);
                        messages.add(message);
                        result = false;
                    }
                }
            }
        } else {
            logger.trace("Field TYPE = " + field.getType());
            EngineMetadataBean relatedMetadata = engineRegistry.getEngineMetadata(field.getType());
            if (dataValue != null) {
                logger.trace("dataValue = " + dataValue);
                if (relatedMetadata != null) {
                    if (!isList) {
                        if (dataValue instanceof Map)
                            result = processEngineMetadataValidation((Map<String, Object>) dataValue, engineRegistry,
                                    messages, dataValues,
                                    relatedMetadata, logger);
                        else {
                            String message = MessageFormat.format(MessageConstants.ERROR_INVALID_DATA, field.getName());
                            logger.info(message);
                            messages.add(message);
                            result = false;
                        }
                    } else {
                        if (dataValue instanceof List) {
                            List<Object> dataValueList = new ArrayList<>();
                            for (Object currentData : ((List<Object>) dataValue)) {
                                Map<String, Object> currentDataValues = new HashMap<>();
                                result = processEngineMetadataValidation((Map<String, Object>) currentData, engineRegistry,
                                        messages, currentDataValues,
                                        relatedMetadata, logger);
                                dataValueList.add(currentDataValues);
                            }
                            if (field.getMapTo() != null && !field.getMapTo().isEmpty())
                                dataValues.put(field.getMapTo(), dataValueList);
                        } else {
                            String message = MessageFormat.format(MessageConstants.ERROR_INVALID_DATA, field.getName());
                            logger.info(message);
                            messages.add(message);
                            result = false;
                        }
                    }
                } else {
                    String message = MessageFormat.format(MessageConstants.ERROR_UNDECLARED_TYPE, field.getType());
                    logger.info(message);
                    messages.add(message);
                    result = false;
                }
            }
        }
        logger.trace("***** EngineService - processEngineField - result = " + result + "*****");
        logger.trace("***** EngineService - processEngineField END *****");
        return result;
    }

    public Map<String, Object> extractAndGeneratePayload(IGRCObject object, EngineRegistry engineRegistry,
                                                         String metadataMainPropertyName, String[] metadataProperties,
                                                         IServiceFactory apiFactory, Logger logger) {
        String result = null;
        Map<String, Object> resultMap = null;
        try {
            EngineMetadataBean metadata = null;
            for (String metadataProperty : metadataProperties) {
                EngineMetadataBean metadataTemp = engineRegistry.putEngineMetadata(metadataProperty);
                if (metadataProperty.equals(metadataMainPropertyName))
                    metadata = metadataTemp;
            }
            resultMap = processEngineMetadataExtraction(object, engineRegistry, metadata, apiFactory,
                    logger);
        } catch (Exception e) {
            String message = MessageConstants.ERROR_FAILED_JSON_PROCESSING;
            logger.error(message, e);
        }
        return resultMap;
    }

    private Map<String, Object> processEngineMetadataExtraction(IGRCObject object, EngineRegistry engineRegistry,
                                                                EngineMetadataBean metadata,
                                                                IServiceFactory apiFactory, Logger logger) {
        Map<String, Object> payloadMap = new LinkedHashMap<>();
        try {
            metadata.getFields().iterator().
                    forEachRemaining((field) -> processEngineFieldExtraction(object, field, engineRegistry, payloadMap, apiFactory,
                            logger));

        } catch (Exception e) {
            String message = MessageConstants.ERROR_FAILED_VALIDATION;
            logger.warn(message, e);
        }
        return payloadMap;
    }

    private void processEngineFieldExtraction(IGRCObject object, FieldBean field, EngineRegistry engineRegistry,
                                              Map<String, Object> payloadMap,
                                              IServiceFactory apiFactory, Logger logger) {
        if (field != null) {
            Object value = EngineConstants.UNDEFINED_VALUE;
            if (field.getSource() != null && object != null) {
                EngineMetadataBean relatedMetadata = null;
                if (!EngineConstants.STRING_TYPE.equals(field.getType())) {
                    relatedMetadata = engineRegistry.getEngineMetadata(field.getType());
                }
                StringTokenizer st;
                String aux = null;
                ObjectMapper mapper = new ObjectMapper();
                logger.trace("Extracting values for:" +
                        " field: " + field.getMapTo() +
                        " from: " + field.getSource().getName() +
                        " kind: " + field.getSource().getKind());
                try {
                    switch (field.getSource().getKind()) {
                        case EngineConstants.STATIC:
                            value = field.getSource().getName();
                            break;
                        case EngineConstants.GRCOBJECT:
                            if (relatedMetadata != null) {
                                value = processEngineMetadataExtraction(object, engineRegistry, relatedMetadata, apiFactory,
                                        logger);
                            }
                            break;
                        case EngineConstants.GRCOBJECT_ID:
                            value = object.getId().toString();
                            break;
                        case EngineConstants.GRCOBJECT_NAME:
                            value = object.getName();
                            break;
                        case EngineConstants.GRCOBJECT_DESCRIPTION:
                            value = object.getDescription();
                            break;
                        case EngineConstants.GRCOBJECT_FIELD:
                            value = ServiceUtil.getLabelValueFromField(object.getField(field.getSource().getName()));
                            break;
                        case EngineConstants.GRCOBJECT_FIELD_NAME:
                            value = ServiceUtil.getNameValueFromField(object.getField(field.getSource().getName()));
                            break;
                        case EngineConstants.GRCOBJECT_FIELD_EXTRACT:
                            String tmp = ServiceUtil.getLabelValueFromField(object.getField(field.getSource().getName()));
                            String[] delimiters = field.getSource().getDelimiters().split(EngineConstants.DEFAULT_SEPARATOR_REGEX);
                            if (delimiters.length == 2 && tmp.contains(delimiters[0]) && tmp.contains(delimiters[1]))
                                value = tmp.substring(tmp.indexOf(delimiters[0]) + 1, tmp.indexOf(delimiters[1]));
                            else
                                value = "";
                            break;
                        case EngineConstants.GRCOBJECT_FIELD_TRANSFORM:
                            String registryProperty = engineRegistry.getProperty(field.getSource().getMapper());
                            Map<String, String> transformationMap = mapper.readValue(registryProperty, Map.class);
                            value = ServiceUtil.getLabelValueFromField(object.getField(field.getSource().getName()));
                            logger.trace("Looking value: " + value + " in transformation map: " + transformationMap);
                            if (transformationMap.containsKey(value)) {
                                value = transformationMap.get(value);
                            }
                            break;
                        case EngineConstants.GRCOBJECT_FIELD_NAME_TRANSFORM:
                            registryProperty = engineRegistry.getProperty(field.getSource().getMapper());
                            transformationMap = mapper.readValue(registryProperty, Map.class);
                            value = ServiceUtil.getNameValueFromField(object.getField(field.getSource().getName()));
                            logger.trace("Looking value: " + value + " in transformation map: " + transformationMap);
                            if (transformationMap.containsKey(value)) {
                                value = transformationMap.get(value);
                            }
                            break;
                        case EngineConstants.GRCOBJECT_MULTIFIELD:
                            value = EngineUtil.getMultiFieldValue(object, field.getSource().getName(), field.getSource().getSeparator(), true, false);
                            break;
                        case EngineConstants.GRCOBJECT_MULTIFIELD_STATUS:
                            registryProperty = engineRegistry.getProperty(field.getSource().getMapper());
                            transformationMap = mapper.readValue(registryProperty, Map.class);
                            value = EngineTransformationUtil.getStatusFromStatusMap(
                                    EngineUtil.getMultiFieldValue(object, field.getSource().getName(), field.getSource().getSeparator(), true, false),
                                    transformationMap, logger);
                            break;
                        case EngineConstants.GRCOBJECT_MULTIFIELD_NAME:
                            value = EngineUtil.getMultiFieldValue(object, field.getSource().getName(), field.getSource().getSeparator(), false, false);
                            break;
                        case EngineConstants.GRCOBJECT_MULTIFIELD_FIRSTMATCH:
                            value = EngineUtil.getMultiFieldValue(object, field.getSource().getName(), field.getSource().getSeparator(), true, true);
                            break;
                        case EngineConstants.GRCOBJECT_MULTIFIELD_FIRSTMATCH_NAME:
                            st = new StringTokenizer(field.getSource().getName(), field.getSource().getSeparator());
                            aux = null;
                            while (st.hasMoreTokens()) {
                                aux = ServiceUtil.getNameValueFromField(object.getField(st.nextToken()));
                                if (aux != null && !aux.isEmpty())
                                    break;
                            }
                            value = aux;
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN:
                            if (relatedMetadata != null) {
                                List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                                for (IGRCObject o : EngineUtil.getPrimaryChildrenFromIssue(object, field.getSource().getName(), apiFactory, logger)) {
                                    resultList.add(processEngineMetadataExtraction(o, engineRegistry, relatedMetadata, apiFactory,
                                            logger));
                                }
                                value = resultList;
                            }
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN_FURTHEST_FUTURE:
                            boolean isDate = false;
                            boolean before = true;
                            boolean firstDay = true;
                            Date today = new Date();
                            Date tempDate = new Date();
                            int numDays;
                            int tempNumDays = 0;
                            Date dateFromField;
                            String date = EngineConstants.EMPTY_VALUE;
                            String[] params = field.getSource().getName().split(EngineConstants.NAME_SEPARATOR_REGEX);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);
                            for (IGRCObject o : EngineUtil.getChildrenFromIssue(object, params[1], apiFactory, logger)) {
                                dateFromField = ServiceUtil.getDateFromField(o.getField(params[0]));
                                if(dateFromField != null){
                                    if (tempDate.compareTo(dateFromField) > 0 && before) {
                                        numDays = (int) (( dateFromField.getTime() - today.getTime()) / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS ));
                                        numDays = (numDays < 0 ? - numDays : numDays);
                                        if (numDays < tempNumDays || firstDay) {
                                            tempNumDays = numDays;
                                            date = simpleDateFormat.format(dateFromField);
                                            isDate = true;
                                            firstDay = false;
                                        }else if(tempNumDays > numDays){
                                            tempNumDays = numDays;
                                            date = simpleDateFormat.format(dateFromField);
                                            isDate = true;
                                        }
                                    }else if(today.compareTo(dateFromField) < 0 && tempDate.compareTo(dateFromField) < 0 ){
                                        tempDate = dateFromField;
                                        date = simpleDateFormat.format(tempDate);
                                        isDate = true;
                                        before = false;
                                    }
                                }
                            }
                            if (isDate) {
                                value = date;
                            } else {
                                value = EngineConstants.EMPTY_VALUE;
                            }
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN_FURTHEST_PAST:
                            isDate = false;
                            firstDay = true;
                            boolean emptyPastDate = false;
                            today = new Date();
                            tempDate = new Date();
                            tempNumDays = 0;
                            date = EngineConstants.EMPTY_VALUE;
                            params = field.getSource().getName().split(EngineConstants.NAME_SEPARATOR_REGEX);
                            simpleDateFormat = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);
                            for (IGRCObject o : EngineUtil.getChildrenFromIssue(object, params[1], apiFactory, logger)) {
                                dateFromField = ServiceUtil.getDateFromField(o.getField(params[0]));
                                logger.debug("dateFromField : " + dateFromField);
                                if(dateFromField != null){
                                    if (today.compareTo(dateFromField) > 0) {
                                        if (tempDate.compareTo(dateFromField) > 0) {
                                            tempDate = dateFromField;
                                            date = simpleDateFormat.format(dateFromField);
                                            isDate = true;
                                            emptyPastDate = true;
                                        }
                                    }
                                }
                            }
                            if(!emptyPastDate){
                                for (IGRCObject o : EngineUtil.getChildrenFromIssue(object, params[1], apiFactory, logger)) {
                                    dateFromField = ServiceUtil.getDateFromField(o.getField(params[0]));
                                    if(dateFromField != null){
                                        if (today.compareTo(dateFromField) < 0) {
                                            numDays = (int) (( dateFromField.getTime() - today.getTime()) / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS ));
                                            numDays = (numDays < 0 ? - numDays : numDays);
                                            logger.debug(" tempNumDays : " + tempNumDays + " dateFromField : " + dateFromField + " numDays : " + numDays);
                                            if (firstDay) {
                                                tempNumDays = numDays;
                                                date = simpleDateFormat.format(dateFromField);
                                                isDate = true;
                                                firstDay = false;
                                            }else if(tempNumDays > numDays){
                                                tempNumDays = numDays;
                                                date = simpleDateFormat.format(dateFromField);
                                                isDate = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (isDate) {
                                value = date;
                            } else {
                                value = EngineConstants.EMPTY_VALUE;
                            }
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN_FURTHEST_ABSOLUTE:
                            today = new Date();
                            tempNumDays = 0;
                            date = EngineConstants.EMPTY_VALUE;
                            params = field.getSource().getName().split(EngineConstants.NAME_SEPARATOR_REGEX);
                            simpleDateFormat = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);isDate = false;
                            for (IGRCObject o : EngineUtil.getChildrenFromIssue(object, params[1], apiFactory, logger)) {
                                dateFromField = ServiceUtil.getDateFromField(o.getField(params[0]));
                                if(dateFromField != null){
                                    numDays = (int) (( dateFromField.getTime() - today.getTime()) / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS ));
                                    numDays = (numDays < 0 ? - numDays : numDays);
                                    if (numDays > tempNumDays) {
                                        tempNumDays = numDays;
                                        date = simpleDateFormat.format(dateFromField);
                                        isDate = true;
                                    }
                                }
                            }
                            if (isDate) {
                                value = date;
                            } else {
                                value = EngineConstants.EMPTY_VALUE;
                            }
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN_MULTIFIELD_FURTHEST_FUTURE:
                            today = new Date();
                            tempDate = new Date();
                            tempNumDays = 0;
                            before = true;
                            isDate = false;
                            firstDay = true;
                            String oVal = EngineConstants.EMPTY_VALUE;
                            params = field.getSource().getName().split(EngineConstants.NAME_SEPARATOR_REGEX);
                            for (IGRCObject o : EngineUtil.getChildrenFromIssue(object, params[1], apiFactory, logger)) {
                                if (field.getSource().getFilter() != null && !field.getSource().getFilter().isEmpty()) {
                                    dateFromField = ServiceUtil.getDateFromField(o.getField(field.getSource().getFilter()));
                                    logger.trace("Date value: " + dateFromField );
                                    if(dateFromField != null){
                                        if (tempDate.compareTo(dateFromField) > 0 && before) {
                                            numDays = (int) (( dateFromField.getTime() - today.getTime()) / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS ));
                                            numDays = (numDays < 0 ? - numDays : numDays);
                                            if (numDays < tempNumDays || firstDay) {
                                                tempNumDays = numDays;
                                                oVal = ServiceUtil.getLabelValueFromField(o.getField(params[0]));
                                                logger.trace(field.getSource().getFilter() + " with value: [" + oVal + "]");
                                                isDate = true;
                                                firstDay = false;
                                            }else if(tempNumDays > numDays){
                                                tempNumDays = numDays;
                                                oVal = ServiceUtil.getLabelValueFromField(o.getField(params[0]));
                                                logger.trace(field.getSource().getFilter() + " with value: [" + oVal + "]");
                                                isDate = true;
                                            }
                                        }else if(today.compareTo(dateFromField) < 0 && tempDate.compareTo(dateFromField) < 0 ){
                                            tempDate = dateFromField;
                                            oVal = ServiceUtil.getLabelValueFromField(o.getField(params[0]));
                                            logger.trace(field.getSource().getFilter() + " with value: [" + oVal + "]");
                                            isDate = true;
                                            before = false;
                                        }
                                    }
                                }
                            }
                            if (isDate) {
                                value = oVal;
                            } else {
                                value = EngineConstants.EMPTY_VALUE;
                            }
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN_MULTIFIELD_FIRSTMATCH:
                            value = EngineUtil.getMultiValueFromChildren(object, field, true, true, apiFactory, logger);
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN_MULTIFIELD_FIRSTMATCH_NAME:
                            value = EngineUtil.getMultiValueFromChildren(object, field, false, true, apiFactory, logger);
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN_MULTIFIELD:
                            value = EngineUtil.getMultiValueFromChildren(object, field, true, false, apiFactory, logger);
                            break;
                        case EngineConstants.GRCOBJECT_CHILDREN_MULTIFIELD_NAME:
                            value = EngineUtil.getMultiValueFromChildren(object, field, false, false, apiFactory, logger);
                            break;
                        case EngineConstants.GRCOBJECT_PARENT_FIELD:
                            String parentField = EngineConstants.EMPTY_VALUE;
                            params = field.getSource().getName().split(EngineConstants.NAME_SEPARATOR_REGEX);
                            for (IGRCObject o : EngineUtil.getParentFromIssue(object, params[1], apiFactory, logger)) {
                                String objectText = "";
                                if ("Name".equals(params[0])) {
                                    objectText = o.getName();
                                } else objectText = ServiceUtil.getLabelValueFromField(o.getField(params[0]));
                                if (objectText != null && !objectText.isEmpty()) {
                                    parentField += objectText + EngineConstants.SEMICOLON_SEPARATOR_REGEX;
                                }
                            }
                            if (!parentField.isEmpty()) {
                                value = parentField.substring(0, parentField.length() - 1);
                            } else {
                                value = EngineConstants.EMPTY_VALUE;
                            }
                            break;
                        case EngineConstants.GRCOBJECT_PARENT_FIELD_NAME:
                            parentField = EngineConstants.EMPTY_VALUE;
                            params = field.getSource().getName().split(EngineConstants.NAME_SEPARATOR_REGEX);
                            for (IGRCObject o : EngineUtil.getParentFromIssue(object, params[1], apiFactory, logger)) {
                                String objectText = "";
                                if ("Name".equals(params[0])) {
                                    objectText = o.getName();
                                } else objectText = ServiceUtil.getNameValueFromField(o.getField(params[0]));
                                if (objectText != null && !objectText.isEmpty()) {
                                    parentField += objectText + EngineConstants.SEMICOLON_SEPARATOR_REGEX;
                                }
                            }
                            if (!parentField.isEmpty()) {
                                value = parentField.substring(0, parentField.length() - 1);
                            } else {
                                value = EngineConstants.EMPTY_VALUE;
                            }
                            break;
                    }
                } catch (Exception e) {
                    logger.error("An error has occurred while processing the field extract.", e);
                    value = EngineConstants.EMPTY_VALUE;
                }
                logger.trace("Extract results :: " + value);
                payloadMap.put(field.getMapTo(), value);
            }
        }
    }


}
