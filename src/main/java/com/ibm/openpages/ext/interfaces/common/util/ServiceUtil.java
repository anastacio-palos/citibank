package com.ibm.openpages.ext.interfaces.common.util;

import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.*;
import com.ibm.openpages.api.service.*;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.interfaces.common.constant.EngineConstants;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.LoggerUtil;
import com.openpages.aurora.common.logging.LoggerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.Format;
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

    private static final String INTERFACE_LOGGER = "ServiceUtil.log";
    private static ILoggerUtil loggerUtil;
    private static Log logger = LogFactory.getLog(ServiceUtil.class);
    private ServicesUtil servicesUtil = null;
    private boolean isTriggerDisabled = false;

    private static void initLogger() {
        try {
            loggerUtil = new LoggerUtil();
            loggerUtil.initService();
            logger = loggerUtil.getExtLogger(INTERFACE_LOGGER);
        } catch (Exception e) {
            logger = LoggerFactory.getLogger();
            logger.error("Error while creating logger", e);
        }
    }

    public static String getDateFromField(IField field, String pattern) {
        String value = "";
        if (field instanceof IDateField) {
            if (((IDateField) field).getValue() != null) {
                Date date = ((IDateField) field).getValue();
                //02/15/2022 02:15:00 PM EST
                Format f = new SimpleDateFormat(pattern);
                value = f.format(date);
            }
        }
        return value;
    }

    public static Date getDateFromField(IField field) {
        Date value = null;
        if (field instanceof IDateField) {
            if (((IDateField) field).getValue() != null) {
                //02/15/2022 02:15:00 PM EST
                value = ((IDateField) field).getValue();
            }
        }
        return value;
    }


    /**
     * <p>
     * Get a value from Openpages {@link IField}
     * </p>
     *
     * @param field with the data to be retrieved
     * @return the retrieved value
     */
    public static String getLabelValueFromField(IField field) {
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
                value = enumField.getEnumValue().getLocalizedLabel();
            }
        } else if (field instanceof IStringField) {
            IStringField stringField = (IStringField) field;
            if (stringField.getValue() != null)
                value = stringField.getValue();
        } else if (field instanceof IFloatField) {
            IFloatField floatField = (IFloatField) field;
            floatValue = floatField.getValue();
            if (floatField.getValue() != null)
                value = Double.toString(floatValue);
        } else if (field instanceof IIntegerField) {
            IIntegerField integerField = (IIntegerField) field;
            if (integerField.getValue() != null)
                value = Integer.toString(integerField.getValue());
        } else if (field instanceof IBooleanField) {
            IBooleanField booleanField = (IBooleanField) field;
            boolean booleanValue = booleanField.getValue();
            value = Boolean.toString(booleanValue);
        } else if (field instanceof IMultiEnumField) {
            IMultiEnumField multiEnumField = (IMultiEnumField) field;
            List<IEnumValue> enumValues = multiEnumField.getEnumValues();
            StringBuffer enumValStrings = new StringBuffer();
            Iterator var6 = enumValues.iterator();
            while (var6.hasNext()) {
                IEnumValue enumVal = (IEnumValue) var6.next();
                enumValStrings.append(enumVal.getLocalizedLabel() + ",");
            }
            if (enumValStrings.length() > 1)
                enumValStrings = enumValStrings.deleteCharAt(enumValStrings.length() - 1);
            value = enumValStrings.toString();
        } else if (field instanceof IDateField) {
            if (((IDateField) field).getValue() != null) {
                Date date = ((IDateField) field).getValue();
                Format f = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);
                value = f.format(date);
            }
        }
        return value;
    }

    public static String getNameValueFromField(IField field) {
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
            if (stringField.getValue() != null)
                value = stringField.getValue();
        } else if (field instanceof IFloatField) {
            IFloatField floatField = (IFloatField) field;
            floatValue = floatField.getValue();
            if (floatField.getValue() != null)
                value = Double.toString(floatValue);
        } else if (field instanceof IIntegerField) {
            IIntegerField integerField = (IIntegerField) field;
            if (integerField.getValue() != null)
                value = Integer.toString(integerField.getValue());
        } else if (field instanceof IBooleanField) {
            IBooleanField booleanField = (IBooleanField) field;
            boolean booleanValue = booleanField.getValue();
            value = Boolean.toString(booleanValue);
        } else if (field instanceof IMultiEnumField) {
            IMultiEnumField multiEnumField = (IMultiEnumField) field;
            List<IEnumValue> enumValues = multiEnumField.getEnumValues();
            StringBuffer enumValStrings = new StringBuffer();
            Iterator var6 = enumValues.iterator();
            while (var6.hasNext()) {
                IEnumValue enumVal = (IEnumValue) var6.next();
                enumValStrings.append(enumVal.getName() + ",");
            }
            if (enumValStrings.length() > 1)
                enumValStrings = enumValStrings.deleteCharAt(enumValStrings.length() - 1);
            value = enumValStrings.toString();
        } else if (field instanceof IDateField) {
            if (((IDateField) field).getValue() != null) {
                Date date = ((IDateField) field).getValue();
                Format f = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);
                value = f.format(date);
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
    public static boolean setEnumValue(IField enumField, String setValue) {
        boolean success = false;
        IFieldDefinition fieldDef = enumField.getFieldDefinition();
        List<IEnumValue> enumValues = fieldDef.getEnumValues();
        if(setValue != null && !setValue.isEmpty()){
            for (IEnumValue enumValue : enumValues) {
                String name = enumValue.getName();
                if (name.equals(setValue)) {
                    logger.debug("Adding to Field: '" + fieldDef.getName() + "' a new value: '" + setValue + "'");
                    ((IEnumField) enumField).setEnumValue(enumValue);
                    success = true;
                }
            }
        } else {
            ((IEnumField) enumField).setEnumValue(null);
            success = true;
        }
        logger.debug("Field: '" + fieldDef.getName() + "' value update is '" + success + "'");
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
    public static boolean setMultiEnumValue(IField enumField, String[] setValues) {
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
    public static boolean setValueFromField(String value, IField field) {
        initLogger();
        logger.debug("** Value : " + value);
        boolean success = false;
        try {
            if (field != null) {
                if (field instanceof IEnumField) {
                    //value - the field value with an element of an enumeration. A null value is allowed.
                    success = setEnumValue(field, value);

                } else if (field instanceof IStringField) {
                    //value - the field value with a string. A null value is allowed.
                    IStringField stringField = (IStringField) field;
                    stringField.setValue(value);
                    success = true;

                } else if (field instanceof IFloatField) {
                    //value - the field value with a double. A null value is allowed.
                    IFloatField floatField = (IFloatField) field;
                    floatField.setValue(Double.valueOf(value));
                    success = true;

                } else if (field instanceof IIntegerField) {
                    //value - the field value, represented as an integer. A null value is allowed
                    IIntegerField integerField = (IIntegerField) field;
                    if (value != null && !value.isEmpty()) {
                        integerField.setValue(Integer.parseInt(value));
                    } else {
                        integerField.setValue(null);
                    }
                    success = true;

                } else if (field instanceof IBooleanField) {
                    //value - the field value with a boolean. A null value is allowed.
                    IBooleanField booleanField = (IBooleanField) field;
                    booleanField.setValue(Boolean.valueOf(value));
                    success = true;
                } else if (field instanceof IMultiEnumField) {
                    //value - the field value with list of elements of an enumeration. A null value is allowed.
                    String[] values = {value};
                    success = setMultiEnumValue(field, values);
                } else if (field instanceof IDateField) {
                    logger.debug("** IDateField instance");
                    IDateField dateField = (IDateField) field;
                    Date date = null;
                    if (value != null) {
                        if (value.length() > 9) {
                            date = new SimpleDateFormat(EngineConstants.OPENPAGES_DATE_PATTERN).parse(value);
                        } else {
                            date = new SimpleDateFormat(EngineConstants.OPENPAGES_SHORT_DATE_PATTERN).parse(value);
                        }
                    }
                    dateField.setValue(date);
                    success = true;
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
     * Query the Openpages database with the specified
     *
     * @param apiFactory  an instance of {@link IServiceFactory}.
     * @param queryString
     * @return
     */
    public static Map<String, String> queryMap(IServiceFactory apiFactory, String queryString) {
        Map<String, String> dataMap = new HashMap<String, String>();
        List<List<String>> resultSet = queryService(apiFactory, queryString, null);
        if (!resultSet.isEmpty()) {
            for (List<String> row : resultSet) {
                dataMap.put(row.get(0), row.get(1));
            }
        }
        return dataMap;
    }

    /**
     * @param apiFactory
     * @param queryString
     * @param value
     * @return
     */
    public static Map<String, String> queryMapWithValues(IServiceFactory apiFactory, String queryString, String[] value) {
        Map<String, String> dataMap = new HashMap<String, String>();
        List<List<String>> resultSet = queryService(apiFactory, queryString, value);
        if (!resultSet.isEmpty()) {
            for (List<String> row : resultSet) {
                dataMap.put(row.get(0), row.get(1));
            }
        }
        return dataMap;
    }

    /**
     * @param apiFactory
     * @param queryString
     * @param value
     * @return
     */
    public static String queryValue(IServiceFactory apiFactory, String queryString, String value) {
        String resultValue = null;
        String[] values = {value};
        List<List<String>> resultSet = queryService(apiFactory, queryString, values);
        if (!resultSet.isEmpty()) {
            for (List<String> row : resultSet) {
                resultValue = row.get(0);
            }
        }
        return resultValue;
    }

    /**
     * @param apiFactory
     * @param queryString
     * @param value
     * @return
     */
    public static List<String> queryAllValues(IServiceFactory apiFactory, String queryString, String value) {
        List<String> resultValue = new ArrayList<>();
        String[] values = {value};
        List<List<String>> resultSet = queryService(apiFactory, queryString, values);
        if (!resultSet.isEmpty()) {
            for (List<String> row : resultSet) {
                resultValue.add(row.get(0));
            }
        }
        return resultValue;
    }

    /**
     * @param apiFactory
     * @param queryString
     * @param values
     * @return
     */
    public static List<List<String>> queryService(IServiceFactory apiFactory, String queryString, String[] values) {
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
                resultRow.add(getLabelValueFromField(field));
            }
            resultList.add(resultRow);
        }
        return resultList;
    }

    public static List<IGRCObject> getChildrenFromIssue(IGRCObject parentIssueObject, String typeName,
                                                        IServiceFactory apiFactory, org.apache.logging.log4j.Logger logger) {
        List<IGRCObject> childList = new ArrayList<>();
        IConfigurationService configService = apiFactory.createConfigurationService();
        IMetaDataService iMetaDataService = apiFactory.createMetaDataService();
        IResourceService rss = apiFactory.createResourceService();
        GRCObjectFilter filter = new GRCObjectFilter(configService.getCurrentReportingPeriod());
        filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.CHILD);
        filter.getAssociationFilter().setTypeFilters(
                iMetaDataService.getType(typeName));
        IGRCObject returnObject = rss.getGRCObject(parentIssueObject.getId(), filter);
        List<IAssociationNode> child = returnObject.getChildren();
        returnObject.getChildren().stream().forEach(currentChild ->
                childList.add(rss.getGRCObject(currentChild.getId()))
        );
        return childList;
    }
}
