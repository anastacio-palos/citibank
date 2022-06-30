package com.ibm.openpages.ext.batch.omu.util;

import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.*;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IServiceFactory;
import org.apache.commons.logging.Log;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * The <code>ServiceUtil</code> contains the methods used to interact with Openpages objects this is used in Openpages to CMP / iCAPS interfaces.
 * </p>
 *
 * @author Ezequiel Garcia <BR>
 * email : ezequiel@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 10-27-2021
 */
public class ServiceUtil {

    /**
     * <p>
     * Get a value from Openpages {@link IField}
     * </p>
     *
     * @param field with the data to be retrieved
     * @return the retrieved value
     */
    public static String getValueFromField(IField field, Log logger) {
        String value = "";
        Double floatValue;
        if (field instanceof IIdField) {
            IIdField idField = (IIdField) field;
            if (idField.getValue() != null) {
                Id val = idField.getValue();
                value = val.toString();
            }
        } else if (field instanceof ICurrencyField) {
            ICurrencyField currencyField = (ICurrencyField) field;
            floatValue = currencyField.getLocalAmount();
            if (floatValue != null) {
                value = Double.toString(floatValue);
            }
        } else if (field instanceof IEnumField) {
            IEnumField enumField = (IEnumField) field;
            if (enumField.getEnumValue() != null) {
                value = enumField.getEnumValue().getName();
            }
        } else if (field instanceof IStringField) {
            IStringField stringField = (IStringField) field;
            if (stringField.getValue() != null) value = stringField.getValue();
        } else if (field instanceof IFloatField) {
            IFloatField floatField = (IFloatField) field;
            floatValue = floatField.getValue();
            if (floatField.getValue() != null) value = Double.toString(floatValue);
        } else if (field instanceof IIntegerField) {
            IIntegerField integerField = (IIntegerField) field;
            if (integerField.getValue() != null) value = Integer.toString(integerField.getValue());
        } else if (field instanceof IBooleanField) {
            IBooleanField booleanField = (IBooleanField) field;
            boolean booleanValue = booleanField.getValue();
            value = Boolean.toString(booleanValue);
        } else if (field instanceof IMultiEnumField) {
            IMultiEnumField multiEnumField = (IMultiEnumField) field;
            logger.debug("Evaluating MultiEnum Field: '" + multiEnumField.getName());
            List<IEnumValue> enumValues = multiEnumField.getEnumValues();
            StringBuffer enumValStrings = new StringBuffer();
            Iterator enumValueIterator = enumValues.iterator();
            while (enumValueIterator.hasNext()) {
                IEnumValue enumValue = (IEnumValue) enumValueIterator.next();
                String stringValue = enumValue.getName();
                logger.debug("stringValue: '" + stringValue);
                enumValStrings.append(stringValue + ",");
            }
            logger.debug("enumValStrings: '" + enumValStrings);
            if (enumValStrings.length() > 0) enumValStrings = enumValStrings.deleteCharAt(enumValStrings.length() - 1);
            value = enumValStrings.toString();
        } else if (field instanceof IDateField) {
            if (((IDateField) field).getValue() != null) {
                Date date = ((IDateField) field).getValue();
                value = date.toString();
            }
        }
        return value;
    }

    /**
     * <p>
     * Set a value in an enum field
     * </p>
     *
     * @param enumField field in which the data will be set.
     * @param setValue  value to be set.
     */
    public static boolean setEnumValue(IField enumField, String setValue, Log logger) {
        boolean success = false;
        IFieldDefinition fieldDef = enumField.getFieldDefinition();
        List<IEnumValue> enumValues = fieldDef.getEnumValues();
        for (IEnumValue enumValue : enumValues) {
            String name = enumValue.getName();
            if (name.equals(setValue)) {
                ((IEnumField) enumField).setEnumValue(enumValue);
                success = true;
            }
        }
        logger.debug("Field: '" + fieldDef.getName() + "' value update is '" + success + "'");
        return success;
    }

    public static boolean appendMultiEnumValue(String newValue, IField field, Log logger) {
        boolean success = false;
        if (field instanceof IMultiEnumField) {
            IMultiEnumField multiEnum = (IMultiEnumField) field;
            IFieldDefinition fieldDef = field.getFieldDefinition();
            List<IEnumValue> fieldDefEnumValues = fieldDef.getEnumValues();
            List<IEnumValue> multiEnumValues = multiEnum.getEnumValues();
            if (newValue != null) {
                for (IEnumValue enumValue : fieldDefEnumValues) {
                    String name = enumValue.getName();
                    if (name.equals(newValue)) {
                        boolean alreadyExists = false;
                        for(IEnumValue i:multiEnumValues){
                            if(newValue.equals(i.getName())){
                                alreadyExists = true;
                                break;
                            }
                        }
                        if(!alreadyExists)
                            multiEnumValues.add(enumValue);
                    }
                }
            }
            if (multiEnumValues != null) {
                ((IMultiEnumField) field).setEnumValues(multiEnumValues);
                success = true;
            }
        }
        return success;
    }

    public static boolean removeMultiEnumValue(String value, IField field, Log logger) {
        boolean success = false;
        if (field instanceof IMultiEnumField) {
            IMultiEnumField multiEnum = (IMultiEnumField) field;
            List<IEnumValue> multiEnumValues = multiEnum.getEnumValues();
            if (value != null) {
                for(IEnumValue i:multiEnumValues){
                    if(value.equals(i.getName())){
                        multiEnumValues.remove(i);
                        break;
                    }
                }
            }
            if (multiEnumValues != null) {
                ((IMultiEnumField) field).setEnumValues(multiEnumValues);
                success = true;
            }
        }
        return success;
    }

    /**
     * <p>
     * Set a values array in a multi enum field
     * </p>
     *
     * @param enumField field in which the data will be set.
     * @param setValues array values to be set.
     */
    public static boolean setMultiEnumValue(IField enumField, String[] setValues, Log logger) {
        boolean success = false;
        IFieldDefinition fieldDef = enumField.getFieldDefinition();
        List<IEnumValue> enumValues = fieldDef.getEnumValues();
        List<IEnumValue> newValues = new ArrayList<>();
        if (setValues != null) {
            for (IEnumValue enumValue : enumValues) {
                String name = enumValue.getName();
                for (String setValue : setValues) {
                    if (name.equals(setValue)) {
                        logger.debug("Adding to Field: '" + fieldDef.getName() + "' a new value: '" + setValue + "'");
                        newValues.add(enumValue);
                    }
                }
            }
        }
        if (newValues != null) {
            ((IMultiEnumField) enumField).setEnumValues(newValues);
            success = true;
        }
        logger.debug("Field: '" + fieldDef.getName() + "' multivalue update is '" + success + "'");
        return success;
    }

    /**
     * <p>
     * Set a value in an field, it identifies the field type.
     * </p>
     *
     * @param value value to be set.
     * @param field field in which the data will be set.
     * @return
     */
    public static boolean setValueFromField(String value, IField field, Log logger) {
        boolean success = false;
        try {
            if (field != null && value != null) {
                if (field instanceof IEnumField) {
                    success = setEnumValue(field, value, logger);
                } else if (field instanceof IStringField) {
                    IStringField stringField = (IStringField) field;
                    if (value != null) {
                        stringField.setValue(value);
                        success = true;
                    }
                } else if (field instanceof IFloatField) {
                    IFloatField floatField = (IFloatField) field;
                    if (value != null) {
                        floatField.setValue(Double.valueOf(value));
                        success = true;
                    }
                } else if (field instanceof IIntegerField) {
                    IIntegerField integerField = (IIntegerField) field;
                    if (value != null) {
                        integerField.setValue(Integer.getInteger(value));
                        success = true;
                    }
                } else if (field instanceof IBooleanField) {
                    IBooleanField booleanField = (IBooleanField) field;
                    booleanField.setValue(Boolean.valueOf(value));
                    success = true;
                } else if (field instanceof IMultiEnumField) {
                    String[] values = {value};
                    success = setMultiEnumValue(field, values, logger);
                } else if (field instanceof IDateField) {
                    if (value != null) {
                        IDateField dateField = (IDateField) field;
                        Date date = null;
                        if (value.length() > 9) {
                            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
                        } else {
                            date = new SimpleDateFormat("yy-MMM-dd").parse(value);
                        }
                        dateField.setValue(date);
                        success = true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("An error has produced while field value was updated.", e);
            success = false;
        }
        logger.debug("Assigning the value: '" + value + "' to Field: '" + field.getName() + "' was success: '" + success + "'");
        return success;
    }

    /**
     * @param apiFactory
     * @param queryString
     * @param values
     * @return
     */
    public static List<List<String>> queryService(IServiceFactory apiFactory, String queryString, String[] values, Log logger) {
        List<List<String>> resultList = new ArrayList<>();
        IQueryService queryService = apiFactory.createQueryService();
        if (values != null && values.length > 0) {
            queryString = MessageFormat.format(queryString, values);
        }
        IQuery query = queryService.buildQuery(queryString);
        // Execute query and fetch results.
        ITabularResultSet resultset = query.fetchRows(0);
        for (IResultSetRow row : resultset) {
            String data = "";
            List<String> resultRow = new ArrayList<>();
            for (IField field : row) {
                resultRow.add(getValueFromField(field, logger));
            }
            resultList.add(resultRow);
        }
        return resultList;
    }


}
