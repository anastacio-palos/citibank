package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.common.util.LoggerUtil.debugEXTLog;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.metadata.DataType;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.AssociationFilter;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IBooleanField;
import com.ibm.openpages.api.resource.ICurrencyField;
import com.ibm.openpages.api.resource.IDateField;
import com.ibm.openpages.api.resource.IEnumField;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IFloatField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IIdField;
import com.ibm.openpages.api.resource.IIntegerField;
import com.ibm.openpages.api.resource.IMultiEnumField;
import com.ibm.openpages.api.resource.IStringField;
import com.ibm.openpages.api.resource.IncludeAssociations;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.openpages.apps.common.util.StringUtil;
import com.openpages.apps.sosa.util.SoxPropertyConstants;
import com.openpages.aurora.common.AuroraEnv;

public class GRCObjectUtil
{

    private static final String CLASS_NAME = "GRCObjectUtil";

    private static final String[] POSSIBLE_DISPLAY_DATE_FORMAT = { "MMM dd, yyyy", "yyyy-MM-dd hh:mm:ss", "MM/dd/yyyy" , "yyyy-MM-dd" };

    private static final String TODAY = "<today>";

    private static final String SYSTEM_FIELDS_NAME = "System Fields:Name";

    private static final String SYSTEM_FIELDS_DESCRIPTION = "System Fields:Description";

    private static final String NO_PREFERENCE_FOUND_MSG = "No Preference objects exist matching 'Preference Type = {0}' and 'Active = Yes'.";

    private static final String NO_ACTIVE_PREFERENCE_FOUND_MSG = "No Preference objects exist matching 'Active = Yes'.";

    private static final String MULTIPLE_PREFERENCE_FOUND_MSG = "Multiple matching Preference objects exist. There should be only one Preference object matching 'Preference Type' and 'Active' fields.";

    private static final String MULTIPLE_ACTIVE_PREFERENCE_FOUND_MSG = "Multiple matching Preference objects exist. There should be only one Preference object matching 'Active' fields.";

    /**
     * <p>
     * This method returns the field value.
     * <P>
     * 
     * @param grcObject
     *            GRC Object
     * @param fieldGroupFieldName
     *            Field name in <Field Group>:<Field Name> format
     * @return String Field value
     * @throws Exception
     */
    public static String getFieldValue(IGRCObject grcObject, String fieldGroupFieldName) throws Exception
    {

        IField field = grcObject.getField(fieldGroupFieldName);

        return getFieldValue(field);
    }

    /**
     * <p>
     * This method returns the field value.
     * <P>
     * 
     * @param field
     *            IField
     * @return String Field value
     * @throws Exception
     */
    public static String getFieldValue(IField field) throws Exception
    {

        if (CommonUtil.isObjectNull(field))
            return EMPTY_STRING;

        String value = "";

        if (field instanceof IIdField)
        {

            IIdField idField = (IIdField) field;
            Id id = idField.getValue();
            value = CommonUtil.isObjectNull(id) ? EMPTY_STRING : id.toString();
        }
        else if (field instanceof IStringField)
        {

            IStringField stringField = (IStringField) field;
            value = CommonUtil.isObjectNull(stringField) ? EMPTY_STRING : CommonUtil.isObjectNull(stringField.getValue()) ? EMPTY_STRING : stringField.getValue();
        }
        else if (field instanceof IBooleanField)
        {

            IBooleanField booleanField = (IBooleanField) field;
            value = CommonUtil.isObjectNull(booleanField) ? EMPTY_STRING : CommonUtil.isObjectNull(booleanField.getValue()) ? EMPTY_STRING : booleanField.getValue().toString();
        }
        else if (field instanceof ICurrencyField)
        {

            ICurrencyField currencyField = (ICurrencyField) field;
            value = CommonUtil.isObjectNull(currencyField) ? EMPTY_STRING : CommonUtil.isObjectNull(currencyField.getLocalAmount()) ? EMPTY_STRING : currencyField.getLocalAmount().toString();
        }
        else if (field instanceof IDateField)
        {

            IDateField dateField = (IDateField) field;
            Date date = dateField.getValue();
            value = CommonUtil.isObjectNull(date) ? EMPTY_STRING : (DateUtil.getDateFromString(date, CommonConstants.YYYY_MM_dd).toString());
        }
        else if (field instanceof IIntegerField)
        {

            IIntegerField integerField = (IIntegerField) field;
            value = CommonUtil.isObjectNull(integerField) ? EMPTY_STRING : CommonUtil.isObjectNull(integerField.getValue()) ? EMPTY_STRING : integerField.getValue().toString();
        }
        else if (field instanceof IFloatField)
        {

            IFloatField floatField = (IFloatField) field;
            value = CommonUtil.isObjectNull(floatField) ? EMPTY_STRING : CommonUtil.isObjectNull(floatField.getValue()) ? EMPTY_STRING : floatField.getValue().toString();
        }
        else if (field instanceof IEnumField)
        {

            IEnumField enumField = (IEnumField) field;
            IEnumValue enumFieldValue = enumField.getEnumValue();
            value = CommonUtil.isObjectNull(enumFieldValue) ? EMPTY_STRING : enumFieldValue.getName();
        }
        else if (field instanceof IMultiEnumField)
        {

            IMultiEnumField multiEnumField = (IMultiEnumField) field;
            List<IEnumValue> multiEnumFieldValues = multiEnumField.getEnumValues();
            value = CommonUtil.isObjectNull(multiEnumFieldValues) ? EMPTY_STRING : getStringFromMultiEnumFieldValues(multiEnumFieldValues);
        }

        return value;
    }

    /**
     * <p>
     * This method returns the comma-separated list of enum values.
     * <P>
     * 
     * @param multiEnumFieldValues
     *            List of Enum Values
     * @return String Comma-separated list of enum values
     * @throws Exception
     */
    private static String getStringFromMultiEnumFieldValues(List<IEnumValue> multiEnumFieldValues) throws Exception
    {

        StringBuilder sb = new StringBuilder();

        for (IEnumValue multiEnumFieldValue : multiEnumFieldValues)
        {

            if (sb.length() == 0)
                sb.append(multiEnumFieldValue.getName());
            else
                sb.append(",").append(multiEnumFieldValue.getName());
        }

        return sb.toString();
    }

    /**
     * <p>
     * This method returns the localized label of a field.
     * <P>
     * 
     * @param field
     *            Field name in the format Field Group:Field Name
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @return String Localized Field Label
     */
    public static String getFieldLocalizedLabel(String field, ServicesUtil servicesUtil)
    {

        try
        {
            return servicesUtil.getMetaDataService().getField(field).getLocalizedLabel();
        }
        catch (Exception e)
        {
            return field;
        }
    }

    /**
     * <p>
     * This method returns the URL to the details page of the object.
     * <P>
     * 
     * @param grcObjectName
     *            Name of the GRC Object
     * @param grcObjectId
     *            Id of the GRC Object
     * @return String Path to the details page of the GRC Object with html elements
     */
    public static String getRelativeResourceURL(String grcObjectName, String grcObjectId)
    {

        String VIEW_RESOURCE_EMAIL = "/view.resource.do?fileId={0}";

        final StringBuffer urlLink = new StringBuffer();
        urlLink.append("<a href=\"");
        urlLink.append(AuroraEnv.getProperty(SoxPropertyConstants.APPLICATION_URL_PATH) + StringUtil.formatMessage(VIEW_RESOURCE_EMAIL, new Object[] { "" + grcObjectId }));
        urlLink.append("\">");
        urlLink.append(grcObjectName.replace(".txt", ""));
        urlLink.append("</a>");

        return urlLink.toString();
    }

    /**
     * <p>
     * This method returns the URL to the details page of the object to load in the Parent window.
     * <P>
     * 
     * @param grcObjectName
     *            Name of the GRC Object
     * @param grcObjectId
     *            Id of the GRC Object
     * @return String Path to the details page of the GRC Object with html elements
     */
    public static String getRelativeResourceURLToLoadInParent(String grcObjectName, String grcObjectId)
    {

        String VIEW_RESOURCE_EMAIL = "/view.resource.do?fileId={0}";

        final StringBuffer urlLink = new StringBuffer();
        urlLink.append("<a href=\"#\" onclick=\"window.opener.location.href='");
        urlLink.append(AuroraEnv.getProperty(SoxPropertyConstants.APPLICATION_URL_PATH) + StringUtil.formatMessage(VIEW_RESOURCE_EMAIL, new Object[] { "" + grcObjectId }));
        urlLink.append("'; return false;\">");
        urlLink.append(StringUtil.removeExtension(grcObjectName));
        urlLink.append("</a>");

        return urlLink.toString();
    }

    /**
     * <p>
     * This method updates the fields on the GRC Object.
     * <P>
     * 
     * @param grcObject
     *            GRC Object which needs to be updated
     * @param paramMap
     *            Map of fields and field values
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public static void updateFields(IGRCObject grcObject, Map<String, String[]> paramMap, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "updateFields";

        try
        {

            Set<String> parameters = paramMap.keySet();

            for (String paramName : parameters)
            {

                String[] values = paramMap.get(paramName);

                if (values != null && values.length > 0)
                {

                    if (paramName.equals(SYSTEM_FIELDS_NAME))
                    {

                        if (StringUtil.isGood(values[0]))
                        {
                            grcObject.setName(values[0]);
                        }
                        else
                        {
                            throw new Exception("Name cannot be blank.");
                        }
                    }
                    else if (paramName.equals(SYSTEM_FIELDS_DESCRIPTION))
                    {

                        grcObject.setDescription(values[0]);
                    }
                    else
                    {

                        IField fieldToSet = grcObject.getField(paramName);
                        Object valueToSet = null;

                        if (null != fieldToSet)
                        {

                            if (fieldToSet.getDataType().equals(DataType.DATE_TYPE))
                            {

                                for (String dfs : POSSIBLE_DISPLAY_DATE_FORMAT)
                                {

                                    SimpleDateFormat sdf = new SimpleDateFormat(dfs, Locale.ENGLISH);
                                    debugEXTLog(logger, "GRCObjectUtil", "Date format", dfs);
                                    
                                    try
                                    {

                                        if (values[0].equalsIgnoreCase(TODAY))
                                            valueToSet = sdf.parse(new Timestamp(new Date().getTime()).toString());
                                        else
                                            valueToSet = sdf.parse(values[0]);
                                    }
                                    catch (Exception e)
                                    {
                                        continue;
                                    }

                                    break;
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.MULTI_VALUE_ENUM))
                            {
                                String multiEnumValues = values[0];
                                if (!CommonUtil.isNullOrEmpty(multiEnumValues))
                                {
                                    if (multiEnumValues.contains(","))
                                        valueToSet = multiEnumValues.split(",");
                                    else
                                        valueToSet = values;
                                }
                                else
                                {
                                    valueToSet = new String[0];
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.INTEGER_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = Integer.parseInt(values[0]);
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.BOOLEAN_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = Boolean.parseBoolean(values[0]);
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.CURRENCY_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = new Object[] { servicesUtil.getConfigurationService().getBaseCurrency(), values[0] };
                                }
                                else
                                {
                                    valueToSet = new Object[] { servicesUtil.getConfigurationService().getBaseCurrency(), "0" };
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.ENUM_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = values[0];
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.FLOAT_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = Double.parseDouble(values[0]);
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.ID_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = values[0];
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.LARGE_STRING_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = values[0];
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.MEDIUM_STRING_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = values[0];
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.REFERENCE_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = values[0];
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.REPORT_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = values[0];
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.STRING_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = values[0];
                                }
                            }
                            else if (fieldToSet.getDataType().equals(DataType.UNLIMITED_STRING_TYPE))
                            {
                                if (StringUtil.isGood(values[0]))
                                {
                                    valueToSet = values[0];
                                }
                            }
                            else
                            {
                                throw new Exception("Field Type " + fieldToSet.getDataType().name() + " is not supported.");
                            }

                            ResourceUtil.setFieldValue(fieldToSet, valueToSet);

                        }
                        else
                        {
                            throw new Exception("Unable to find the IField " + paramName + " for the Object " + grcObject.getId());
                        }
                    }
                }
            }

            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "Update Status", grcObject.getName() + " updated successfully.");
        }
        catch (Exception e)
        {
            LoggerUtil.errorEXTLog(logger, METHOD_NAME, "ERROR", e.getMessage());
            throw e;
        }
    }

    /**
     * <p>
     * This method returns the GRC Object.
     * <P>
     * 
     * @param idOrPath
     *            Id or Path of the GRC Object
     * @param isFolderPath
     *            True, if idOrPath is a Folder Path. False, if idOrPath is a Resource Id
     * @param fields
     *            List of Field Names in the format FieldGroup:FieldName. If no fields are required, can pass null
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @return GRCObject
     * @throws Exception
     */
    public static IGRCObject getGRCObject(Object idOrPath, boolean isFolderPath, List<String> fields, ServicesUtil servicesUtil) throws Exception
    {

        if (CommonUtil.isListNullOrEmpty(fields))
            return getGRCObject(idOrPath, isFolderPath, new IFieldDefinition[0], servicesUtil);

        List<IFieldDefinition> fieldDefinitions = new ArrayList<IFieldDefinition>();

        for (String field : fields)
        {

            fieldDefinitions.add(servicesUtil.getMetaDataService().getField(field));
        }

        return getGRCObject(idOrPath, isFolderPath, fieldDefinitions.toArray(new IFieldDefinition[fieldDefinitions.size()]), servicesUtil);
    }

    /**
     * <p>
     * This method returns the GRC Object.
     * <P>
     * 
     * @param idOrPath
     *            Id or Path of the GRC Object
     * @param isFolderPath
     *            Boolean Value. True if the parameter is a Folder Path. If not, false.
     * @param fieldDefinitions
     *            Array of Field Defintions
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @return GRCObject
     * @throws Exception
     */
    private static IGRCObject getGRCObject(Object idOrPath, boolean isFolderPath, IFieldDefinition[] fieldDefinitions, ServicesUtil servicesUtil) throws Exception
    {

        GRCObjectFilter myFilter = new GRCObjectFilter(servicesUtil.getConfigurationService().getCurrentReportingPeriod());
        myFilter.setFieldFilters(fieldDefinitions);

        myFilter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.NONE);

        IGRCObject grcObject = null;

        if (idOrPath instanceof String)
        {

            if (isFolderPath)
                grcObject = servicesUtil.getResourceService().getGRCObject(idOrPath.toString(), myFilter);
            else
                grcObject = servicesUtil.getResourceService().getGRCObject(new Id(idOrPath.toString()), myFilter);
        }
        else if (idOrPath instanceof Id)
        {

            grcObject = servicesUtil.getResourceService().getGRCObject((Id) idOrPath, myFilter);
        }
        else
        {
            throw new Exception("idOrPath is not of type String or Id.");
        }

        return grcObject;
    }

    /**
     * <p>
     * This method will return the Parents of given GRC Object Id.
     * </p>
     * 
     * @param grcObjectId
     *            Id of the GRC Object
     * @param parentObjectType
     *            Object type of the Parents to fetch
     * @param honorPrimary
     *            True to return only primary associations
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @return List<IAssociationNode> List of Parent Association Nodes
     * @throws Exception
     */
    public static List<IAssociationNode> getParentObjects(Id grcObjectId, String parentObjectType, boolean honorPrimary, ServicesUtil servicesUtil) throws Exception
    {

        List<IAssociationNode> parentAssociations = null;

        try
        {

            GRCObjectFilter includeParents = new GRCObjectFilter(servicesUtil.getConfigurationService().getCurrentReportingPeriod());

            AssociationFilter associationFilter = includeParents.getAssociationFilter();
            associationFilter.setIncludeAssociations(IncludeAssociations.PARENT);
            associationFilter.setTypeFilters(servicesUtil.getMetaDataService().getType(parentObjectType));

            if (honorPrimary)
                associationFilter.setHonorPrimary(true);
            else
                associationFilter.setHonorPrimary(false);

            IGRCObject grcObject = servicesUtil.getResourceService().getGRCObject(grcObjectId, includeParents);
            parentAssociations = grcObject.getParents();

        }
        catch (Exception e)
        {
            throw e;
        }
        return parentAssociations;
    }

    /**
     * <p>
     * This method will return the Children of given GRC Object Id.
     * </p>
     * 
     * @param grcObjectId
     *            Id of the GRC Object
     * @param childObjectType
     *            Object type of the Children to fetch
     * @param honorPrimary
     *            True to return only primary associations
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @return List<IAssociationNode> List of Child Association Nodes
     * @throws Exception
     */
    public static List<IAssociationNode> getChildObjects(Id grcObjectId, String childObjectType, boolean honorPrimary, ServicesUtil servicesUtil) throws Exception
    {

        List<IAssociationNode> childAssociations = null;

        try
        {

            GRCObjectFilter includeChildren = new GRCObjectFilter(servicesUtil.getConfigurationService().getCurrentReportingPeriod());

            AssociationFilter associationFilter = includeChildren.getAssociationFilter();
            associationFilter.setIncludeAssociations(IncludeAssociations.CHILD);
            associationFilter.setTypeFilters(servicesUtil.getMetaDataService().getType(childObjectType));

            if (honorPrimary)
                associationFilter.setHonorPrimary(true);
            else
                associationFilter.setHonorPrimary(false);

            IGRCObject grcObject = servicesUtil.getResourceService().getGRCObject(grcObjectId, includeChildren);
            childAssociations = grcObject.getChildren();

        }
        catch (Exception e)
        {
            throw e;
        }
        return childAssociations;
    }

    /**
     * <p>
     * This method will return a list of Enum values for a given Enumerated Field String.
     * </p>
     * 
     * @param enumField
     *            Enum Field in <Field Group>:<Field Name> format
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @return List<String> List of Enum Values
     */
    public static List<String> getEnumValuesAsList(String enumField, ServicesUtil servicesUtil)
    {

        List<String> enumValuesList = new ArrayList<String>();

        IFieldDefinition fieldDefinition = servicesUtil.getMetaDataService().getField(enumField);

        if (fieldDefinition.getDataType().equals(DataType.ENUM_TYPE) || fieldDefinition.getDataType().equals(DataType.MULTI_VALUE_ENUM))
        {

            List<IEnumValue> enumValues = fieldDefinition.getEnumValues();

            for (IEnumValue enumValue : enumValues)
            {

                enumValuesList.add(enumValue.getName());
            }
        }

        return enumValuesList;
    }

    /**
     * <p>
     * This method will return a Map of Enum values for a given Enumerated Field String with Enum Name as the Key and Enum label as the value.
     * </p>
     * 
     * @param enumField
     *            Enum Field in <Field Group>:<Field Name> format
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @return Map<String, String> Map of Enum Values
     */
    public static Map<String, String> getEnumValuesAsMap(String enumField, ServicesUtil servicesUtil)
    {

        Map<String, String> enumValuesMap = new HashMap<String, String>();

        IFieldDefinition fieldDefinition = servicesUtil.getMetaDataService().getField(enumField);

        if (fieldDefinition.getDataType().equals(DataType.ENUM_TYPE) || fieldDefinition.getDataType().equals(DataType.MULTI_VALUE_ENUM))
        {

            List<IEnumValue> enumValues = fieldDefinition.getEnumValues();

            for (IEnumValue enumValue : enumValues)
            {

                enumValuesMap.put(enumValue.getName(), enumValue.getLocalizedLabel());
            }
        }

        return enumValuesMap;
    }

    /**
     * <p>
     * This method returns a newly created GRC Object based on the provided Object Type.
     * </p>
     * 
     * @param grcObjectType
     *            Object Type of the GRC Object to be created
     * @param parentObjectId
     *            Parent GRC Object Id under which the new Child object has to be created
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public static IGRCObject createAutoNamedGRCObject(String grcObjectType, Id parentObjectId, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "createAutoNamedGRCObject";

        IGRCObject newAutoNamedGRCObject = servicesUtil.getResourceService().getResourceFactory().createAutoNamedGRCObject(servicesUtil.getMetaDataService().getType(grcObjectType));

        newAutoNamedGRCObject.setPrimaryParent(parentObjectId);
        newAutoNamedGRCObject = servicesUtil.getResourceService().saveResource(newAutoNamedGRCObject);

        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "newAutoNamedGRCObject", newAutoNamedGRCObject.getName());
        
        return newAutoNamedGRCObject;
    }

    /**
     * <p>
     * This method returns a newly created GRC Object based on the provided Object Type.
     * </p>
     * 
     * @param grcObjectType
     *            Object Type of the GRC Object to be created
     * @param parentObject
     *            Parent GRC Object under which the new Child object has to be created
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public static IGRCObject createAutoNamedGRCObject(String grcObjectType, IGRCObject parentObject, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        return createAutoNamedGRCObject(grcObjectType, parentObject.getId(), servicesUtil, logger);
    }

    /**
     * <p>
     * This method returns a newly created GRC Object based on the provided Object Type.
     * </p>
     * 
     * @param grcObjectName
     *            Name of the GRC Object to be created
     * @param grcObjectType
     *            Object Type of the GRC Object to be created
     * @param parentObjectId
     *            Parent GRC Object Id under which the new Child object has to be created
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public static IGRCObject createGRCObject(String grcObjectName, String grcObjectType, Id parentObjectId, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "createGRCObject";

        IGRCObject newGRCObject = servicesUtil.getResourceService().getResourceFactory().createGRCObject(grcObjectName, servicesUtil.getMetaDataService().getType(grcObjectType));
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "newGRCObject", newGRCObject.getName());

        newGRCObject.setPrimaryParent(parentObjectId);
        newGRCObject = servicesUtil.getResourceService().saveResource(newGRCObject);

        return newGRCObject;
    }
    
    /**
     * <p>
     * This method returns a newly created GRC Object based on the provided Object Type
     * 
     * </p>
     * 
     * @param grcObjectName
     *            Name of the GRC Object to be created
     * @param grcObjectType
     *            Object Type of the GRC Object to be created
     * @param parentObjectId
     *            Parent GRC Object Id under which the new Child object has to be created
     * @param paramMap
     *           Map of fields and field values
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public static IGRCObject createGRCObjectWithProperties(String grcObjectName, String grcObjectType, Id parentObjectId, Map<String, String[]> paramMap, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "createGRCObject";

        IGRCObject newGRCObject = servicesUtil.getResourceService().getResourceFactory().createGRCObject(grcObjectName, servicesUtil.getMetaDataService().getType(grcObjectType));
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "newGRCObject", newGRCObject.getName());

        updateFields(newGRCObject, paramMap, servicesUtil, logger);
        newGRCObject.setPrimaryParent(parentObjectId);
        newGRCObject = servicesUtil.getResourceService().saveResource(newGRCObject);

        return newGRCObject;
    }

    /**
     * <p>
     * This method returns a newly created GRC Object based on the provided Object Type.
     * </p>
     * 
     * @param grcObjectName
     *            Name of the GRC Object to be created
     * @param grcObjectType
     *            Object Type of the GRC Object to be created
     * @param parentObject
     *            Parent GRC Object under which the new Child object has to be created
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public static IGRCObject createGRCObject(String grcObjectName, String grcObjectType, IGRCObject parentObject, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        return createGRCObject(grcObjectName, grcObjectType, parentObject.getId(), servicesUtil, logger);
    }

    /**
     * <p>
     * This method returns the updated GRC Object after fields are copied from Source to Target object. saveResource() method should be called to commit the changes.
     * </p>
     * 
     * @param sourceObject
     *            Source GRC Object from which the field values have to be copied
     * @param targetObject
     *            Target GRC Object to which the field values have to be copied
     * @param fieldsToCopy
     *            Comma-separated list of fields in the format <fieldgroup:fieldname>=<fieldgroup:fieldname>,<fieldgroup:fieldname>=<fieldgroup:fieldname>
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @return IGRCObject Updated Target GRC Object
     * @throws Exception
     */
    public static IGRCObject copyFieldsFromSourceToTarget(IGRCObject sourceObject, IGRCObject targetObject, String fieldsToCopy, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "copyFieldsFromSourceToTarget";

        List<String> fieldsToCopyAsList = CommonUtil.parseDelimitedValues(fieldsToCopy, ",");
        LoggerUtil.error("GRCObjectUtil :::: ", METHOD_NAME, "fieldsToCopyAsList", fieldsToCopyAsList);
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "fieldsToCopyAsList", fieldsToCopyAsList);

        String[] fieldsArray = null;
        String sourceField = null;
        String targetField = null;
        String sourceFieldValue = null;
        Map<String, String[]> paramMap = new HashMap<String, String[]>();

        LoggerUtil.error("GRCObjectUtil :::: ", METHOD_NAME, "sourceObject.getName() :::: ", sourceObject.getName());
        LoggerUtil.error("GRCObjectUtil :::: ", METHOD_NAME, "targetObject.getName() :::: ", targetObject.getName());
        for (String fields : fieldsToCopyAsList)
        {

            fieldsArray = fields.split("=");

            targetField = fieldsArray[0];
            LoggerUtil.error("GRCObjectUtil :::: ", METHOD_NAME, "targetField", targetField);
            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "targetField", targetField);

            sourceField = fieldsArray[1];
            LoggerUtil.error("GRCObjectUtil :::: ", METHOD_NAME, "sourceField", sourceField);
            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "sourceField", sourceField);

            sourceFieldValue = getFieldValue(sourceObject, sourceField);
            LoggerUtil.error("GRCObjectUtil :::: ", METHOD_NAME, "sourceFieldValue", sourceFieldValue);
          LoggerUtil.debugEXTLog(logger, METHOD_NAME, "sourceFieldValue", sourceFieldValue);

            paramMap.put(targetField, new String[] { sourceFieldValue });
        }

        updateFields(targetObject, paramMap, servicesUtil, logger);

        return targetObject;
    }

    /**
     * <p>
     * This method returns the updated GRC Object after fields are copied from Source to Target object. saveResource() method should be called to commit the changes.
     * </p>
     * 
     * @param sourceObjectId
     *            Source GRC Object Id from which the field values have to be copied
     * @param targetObjectId
     *            Target GRC Object Id to which the field values have to be copied
     * @param fieldsToCopy
     *            Comma-separated list of fields in the format <fieldgroup:fieldname>=<fieldgroup:fieldname>,<fieldgroup:fieldname>=<fieldgroup:fieldname>
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @return IGRCObject Updated Target GRC Object
     * @throws Exception
     */
    public static IGRCObject copyFieldsFromSourceToTarget(String sourceObjectId, String targetObjectId, String fieldsToCopy, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "copyFieldsFromSourceToTarget";

        IGRCObject sourceObject = servicesUtil.getResourceService().getGRCObject(new Id(sourceObjectId));
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "sourceObject", sourceObject.getName());

        IGRCObject targetObject = servicesUtil.getResourceService().getGRCObject(new Id(targetObjectId));
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "targetObject", targetObject.getName());

        return copyFieldsFromSourceToTarget(sourceObject, targetObject, fieldsToCopy, servicesUtil, logger);
    }

    /**
     * <p>
     * This method returns the Id of the Primary Parent Entity for any Object of any type passed.
     * </p>
     * 
     * @param grcObject
     *            Instance of IGRCObject
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @return Id Parent Entity Id
     * @throws Exception
     */
    public static Id getPrimaryParentEntityFromAnyObject(IGRCObject grcObject, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "getPrimaryParentEntityFromAnyObject";

        if (CommonUtil.isObjectNotNull(grcObject.getPrimaryParent()))
        {

            IGRCObject primaryParent = getGRCObject(grcObject.getPrimaryParent(), false, new ArrayList<String>(), servicesUtil);
            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "primaryParent.getName()", primaryParent.getName());
            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "primaryParent.getType().getName()", primaryParent.getType().getName());

            if (primaryParent.getType().getName().equalsIgnoreCase(CommonConstants.BUSINESS_ENTITY_OBJECT_TYPE))
                return primaryParent.getId();
            else
                return getPrimaryParentEntityFromAnyObject(primaryParent, servicesUtil, logger);
        }

        return null;
    }

    /**
     * <p>
     * This method returns the update GRC Object after fields are copied from Source to Target object.
     * </p>
     * 
     * @param sourceObjectId
     *            Source GRC Object Id from which the field values have to be copied
     * @param targetObject
     *            Target GRC Object to which the field values have to be copied
     * @param fieldsToCopy
     *            Comma-separated list of fields in the format <fieldgroup:fieldname>=<fieldgroup:fieldname>,<fieldgroup:fieldname>=<fieldgroup:fieldname>
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @return IGRCObject Updated Target GRC Object
     * @throws Exception
     */
    public static IGRCObject copyFieldsFromSourceToTarget(String sourceObjectId, IGRCObject targetObject, String fieldsToCopy, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "copyFieldsFromSourceToTarget";

        IGRCObject sourceObject = servicesUtil.getResourceService().getGRCObject(new Id(sourceObjectId));
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "sourceObject", sourceObject.getName());

        return copyFieldsFromSourceToTarget(sourceObject, targetObject, fieldsToCopy, servicesUtil, logger);
    }

    /**
     * <p>
     * This method returns the URL to the details page of the object to load in a new window.
     * <P>
     * 
     * @param grcObjectName
     *            Name of the GRC Object
     * @param grcObjectId
     *            Id of the GRC Object
     * @return String Path to the details page of the GRC Object with html elements
     */
    public static String getRelativeResourceURLToLoadInNewWindow(String grcObjectName, String grcObjectId)
    {

        String VIEW_RESOURCE_EMAIL = "/view.resource.do?fileId={0}";

        final StringBuffer urlLink = new StringBuffer();
        urlLink.append("<a target='_blank' href='");
        urlLink.append(AuroraEnv.getProperty(SoxPropertyConstants.APPLICATION_URL_PATH) + StringUtil.formatMessage(VIEW_RESOURCE_EMAIL, new Object[] { "" + grcObjectId }));
        urlLink.append("'>");
        urlLink.append(grcObjectName.replace(".txt", ""));
        urlLink.append("</a>");

        return urlLink.toString();
    }

    /**
     * <p>
     * This method returns the relative path of the GRC object as it looks in the UI.
     * <P>
     * 
     * @param grcObject
     *            GRC Object
     * @return String Path of the GRC Object
     */
    public static String getGRCObjectPath(IGRCObject grcObject)
    {

        if (grcObject == null)
        {
            return EMPTY_STRING;
        }

        String rootFolderPath = grcObject.getType().getRootFolderPath();
        rootFolderPath = rootFolderPath.substring(0, rootFolderPath.lastIndexOf(CommonConstants.FOLDER_SEPARATOR) + 1);

        return grcObject.getParentFolder().getPath().replace(rootFolderPath, EMPTY_STRING);
    }

    /**
     * 
     * @param grcObject
     *            GRCObject for which the nearest active Preference record need to be found
     * @param preferenceType
     *            Preference type of Preference object which need to match
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger Class
     * @return Id Preference Id which is found
     * @throws Exception
     * <br>
     *             <b>Error Messages:</b><br>
     *             No Preference objects exist matching 'Preference Type = {0}' and 'Active = Yes'.<br>
     *             Multiple matching Preference objects exist. There should be only one Preference object matching 'Preference Type' and 'Active' fields.
     */
    public static Id getNearestPreferenceObject(IGRCObject grcObject, String preferenceType, ServicesUtil servicesUtil, Logger logger) throws Exception
    {
        String METHOD_NAME = "getNearestPreferenceObject";
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "getNearestPreferenceObject", "Start");

        if (CommonUtil.isObjectNull(grcObject))
        {
            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "grcObject", "null");
            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "getNearestPreferenceObject", "End");
            return null;
        }

        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "preferenceType", preferenceType);
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "grcObject", grcObject.getName());

        String objectType = new String();
        objectType = grcObject.getType().getName();
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "objectType", objectType);
        
        Context adminContext = SecurityUtil.getAdminContext(servicesUtil.getContext(), logger);
        
        ServicesUtil adminServicesUtil = new ServicesUtil(adminContext);
        
        if (CommonConstants.BUSINESS_ENTITY_OBJECT_TYPE.equalsIgnoreCase(objectType))
        {

            return getNearestPreferenceObject(grcObject, preferenceType, true, adminServicesUtil, logger);
        }
        else
        {
            return getNearestPreferenceObject(grcObject, preferenceType, false, adminServicesUtil, logger);
        }
    }

    /**
     * 
     * @param grcObject
     *            GRCObject for which the nearest active Preference record need to be found
     * @param preferenceType
     *            Preference type of Preference object which need to match
     * @param isEntity
     *            true if object is Business Entity, else false;
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger Class
     * @return Id Preference Id which is found
     * @throws Exception
     * <br>
     *             <b>Error Messages:</b><br>
     *             No Preference objects exist matching 'Preference Type = {0}' and 'Active = Yes'.<br>
     *             Multiple matching Preference objects exist. There should be only one Preference object matching 'Preference Type' and 'Active' fields.
     */
    private static Id getNearestPreferenceObject(IGRCObject grcObject, String preferenceType, Boolean isEntity, ServicesUtil servicesUtil, Logger logger) throws Exception
    {
        String METHOD_NAME = "getNearestPreferenceObject";

        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "getNearestPreferenceObject", "Start");
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "preferenceType", preferenceType);

        try
        {

            if (CommonUtil.isObjectNull(grcObject))
            {
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, "getNearestPreferenceObject", "End");
                return null;
            }

            Id primaryEntityId = null;

            if (!isEntity)
            {

                primaryEntityId = getPrimaryParentEntityFromAnyObject(grcObject, servicesUtil, logger);
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, "primaryEntityId", primaryEntityId);
            }
            else
            {

                primaryEntityId = grcObject.getId();
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, "currentEntityId", primaryEntityId);
            }

            // if primaryEntityId is null..it means Root is reached
            if (CommonUtil.isObjectNull(primaryEntityId))
            {
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, "NO PREFERENCE FOUND", "End");

                if (CommonUtil.isNullOrEmpty(preferenceType))
                    throw new Exception(NO_ACTIVE_PREFERENCE_FOUND_MSG);
                else
                    throw new Exception(NO_PREFERENCE_FOUND_MSG.replace("{0}", preferenceType));
            }

            IGRCObject primaryEntityObject = servicesUtil.getResourceService().getGRCObject(primaryEntityId);
            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "primaryEntityObject Name", primaryEntityObject.getName());

            // check for Pref which are directly under BE
            List<String> activePrefIdLists = getChildPreferences(primaryEntityObject, preferenceType, servicesUtil, logger);
            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "activePrefIdLists size", activePrefIdLists.size());

            if (CommonUtil.isObjectNull(activePrefIdLists) || activePrefIdLists.isEmpty())
            {
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, "No Preferences in BE", primaryEntityObject.getName());

                // check for Pref which are directly under PrefGrp <- BE
                activePrefIdLists = getChildPreferencesUnderPreferenceGroup(primaryEntityObject, preferenceType, servicesUtil, logger);
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, "activePrefIdLists size", activePrefIdLists.size());

                if (CommonUtil.isObjectNull(activePrefIdLists) || activePrefIdLists.isEmpty())
                {
                    LoggerUtil.debugEXTLog(logger, METHOD_NAME, "No Preferences in Pref Group", primaryEntityObject.getName());
                    return getNearestPreferenceObject(primaryEntityObject, preferenceType, false, servicesUtil, logger);
                }
            }

            if (activePrefIdLists.isEmpty())
            {
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, " NO ACTIVE PREFERENCE FOUND MATCHING TYPE", "Preference are there but not matching");
                getNearestPreferenceObject(primaryEntityObject, preferenceType, false, servicesUtil, logger);
            }
            else if (activePrefIdLists.size() > 1)
            {
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, " MULTIPLE ACTIVE PREFERENCE FOUND MATCHING TYPE", "End");

                if (CommonUtil.isNullOrEmpty(preferenceType))
                    throw new Exception(MULTIPLE_ACTIVE_PREFERENCE_FOUND_MSG);
                else
                    throw new Exception(MULTIPLE_PREFERENCE_FOUND_MSG);
            }
            else
            {
                LoggerUtil.debugEXTLog(logger, METHOD_NAME, " ONLY ONE ACTIVE PREFERENCE FOUND MATCHING TYPE", "End");
                return new Id(activePrefIdLists.get(0));
            }

            LoggerUtil.debugEXTLog(logger, METHOD_NAME, "getNearestPreferenceObject", "End");
            return null;
        }
        catch (Exception e)
        {
            LoggerUtil.errorEXTLog(logger, METHOD_NAME, "ERROR", e.getMessage());
            throw e;
        }
    }

    /**
     * 
     * @param primaryEntityObject
     *            GRCObject of Business Entity
     * @param preferenceType
     *            Preference type of preference object which need to match
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger Class
     * @return List of Preference IDs
     * @throws Exception
     */
    private static List<String> getChildPreferences(IGRCObject primaryEntityObject, String preferenceType, ServicesUtil servicesUtil, Logger logger) throws Exception
    {
        String METHOD_NAME = "getChildPreferences";

        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "getChildPreferences", "Start");
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "preferenceType", preferenceType);

        String queryStr = new String();
        List<String> activePrefIdLists = new ArrayList<String>();

        String entityId = primaryEntityObject.getId().toString();

        if (CommonUtil.isNullOrEmpty(preferenceType))
        {
            queryStr = "SELECT [Preference].[Resource ID],[Preference].[Location],[Preference].[OPSS-Pref:Active] FROM [Preference] JOIN [SOXBusEntity] ON CHILD([Preference]) "
                    + " WHERE [Preference].[OPSS-Pref:Active]= 'Yes' AND [SOXBusEntity].[Resource ID]= '" + entityId + "'";

        }
        else
        {
            queryStr = "SELECT [Preference].[Resource ID],[Preference].[Location],[Preference].[OPSS-Pref:Preference Type],[Preference].[OPSS-Pref:Active] FROM [Preference] JOIN [SOXBusEntity] ON CHILD([Preference]) "
                    + " WHERE [Preference].[OPSS-Pref:Active]= 'Yes' AND [Preference].[OPSS-Pref:Preference Type] IN ('" + preferenceType + "') AND [SOXBusEntity].[Resource ID]= '" + entityId + "'";

        }
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "queryStr", queryStr);

        if (CommonUtil.isNotNullOrEmpty(queryStr))
        {
            activePrefIdLists = QueryUtil.getQueryResultsAsList(queryStr, 0, true, servicesUtil, logger);

        }

        return activePrefIdLists;
    }

    /**
     * 
     * @param primaryEntityObject
     *            GRCObject of Business Entity
     * @param preferenceType
     *            Preference type of preference object which need to match
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger Class
     * @return List of Preference IDs
     * @throws Exception
     */
    private static List<String> getChildPreferencesUnderPreferenceGroup(IGRCObject primaryEntityObject, String preferenceType, ServicesUtil servicesUtil, Logger logger) throws Exception
    {
        String METHOD_NAME = "getChildPreferencesUnderPreferenceGroup";

        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "getChildPreferencesUnderPreferenceGroup", "Start");
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "preferenceType", preferenceType);

        String queryStr = new String();
        List<String> activePrefIdLists = new ArrayList<String>();

        String entityId = primaryEntityObject.getId().toString();

        if (CommonUtil.isNullOrEmpty(preferenceType))
        {
            queryStr = "SELECT [Preference].[Resource ID],[Preference].[Location],[Preference].[OPSS-Pref:Active] FROM [Preference]  JOIN [PrefGrp] ON CHILD([Preference]) JOIN [SOXBusEntity] ON CHILD([PrefGrp]) "
                    + " WHERE [Preference].[OPSS-Pref:Active]= 'Yes' AND [SOXBusEntity].[Resource ID]= '" + entityId + "'";

        }
        else
        {
            queryStr = "SELECT [Preference].[Resource ID],[Preference].[Location],[Preference].[OPSS-Pref:Preference Type],[Preference].[OPSS-Pref:Active] FROM [Preference] JOIN [PrefGrp] ON CHILD([Preference]) JOIN [SOXBusEntity] ON CHILD([PrefGrp]) "
                    + " WHERE [Preference].[OPSS-Pref:Active]= 'Yes' AND [Preference].[OPSS-Pref:Preference Type]= '" + preferenceType + "' AND [SOXBusEntity].[Resource ID]= '" + entityId + "'";

        }

        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "queryStr PrefGrp", queryStr);

        if (CommonUtil.isNotNullOrEmpty(queryStr))
        {
            activePrefIdLists = QueryUtil.getQueryResultsAsList(queryStr, 0, true, servicesUtil, logger);

        }

        return activePrefIdLists;
    }
}