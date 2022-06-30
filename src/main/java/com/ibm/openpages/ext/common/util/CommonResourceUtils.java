package com.ibm.openpages.ext.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.OpenPagesException;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.configuration.ICurrency;
import com.ibm.openpages.api.metadata.DataType;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IBooleanField;
import com.ibm.openpages.api.resource.ICurrencyField;
import com.ibm.openpages.api.resource.IDateField;
import com.ibm.openpages.api.resource.IEnumAnswer;
import com.ibm.openpages.api.resource.IEnumAnswersField;
import com.ibm.openpages.api.resource.IEnumField;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IFloatField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IIdField;
import com.ibm.openpages.api.resource.IIntegerField;
import com.ibm.openpages.api.resource.IMultiEnumField;
import com.ibm.openpages.api.resource.IReferenceField;
import com.ibm.openpages.api.resource.IStringField;
import com.ibm.openpages.api.resource.IncludeAssociations;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;

public class CommonResourceUtils {

    private static final String NULL = "null";
    public static final String DOT_TXT = ".txt";
    public static final String SLASH = "/";
    public static final String RECORD_COLUMN_SEPARATOR = "######";
    private static final String MULTIPLY = "MULTIPLY";
    private static final String DIVIDE = "DIVIDE";
    private static final String SUBTRACT = "SUBTRACT";
    private static final String ADD = "ADD";

    private static final String QUERY_GET_VALUE_OF_FIELD_ON_ENTITY = "SELECT [SOXBusEntity].[{0}]"
            //
            + " FROM [SOXBusEntity]"
            //
            + " WHERE [SOXBusEntity].[Resource ID] = {1}";
    private Logger logger = null;

    public CommonResourceUtils(Logger logger) {
        this.logger = logger;
    }

    public static String getQueryThatFetchesValueOfFieldOnEntity() {
        return QUERY_GET_VALUE_OF_FIELD_ON_ENTITY;
    }

    /**
     * Gets the field value as Object
     * 
     * @param field
     * @param logger
     * @return
     */
    public Object getFieldValueAsObject(final IField field) {
        if (field != null && !field.isNull()) {
            logger.debug("getFieldValue : " + field.getName() + " type " + field.getDataType());
            Object value = null;
            if (field instanceof IDateField) {
                IDateField dateField = (IDateField) field;
                if (dateField.getValue() != null) {
                    value = dateField.getValue();
                }
                logger.debug("IDateField value  : " + value);
            }
            else if (field instanceof IStringField) {
                IStringField stringField = (IStringField) field;
                value = stringField.getValue();
                logger.debug("IStringField value : " + value);
            }
            else if (field instanceof IEnumField) {
                IEnumField stringField = (IEnumField) field;
                value = stringField.getEnumValue().getName();
                logger.debug("IEnumField value : " + value);
            }
            else if (field instanceof IIdField) {
                IIdField idField = (IIdField) field;
                value = idField.getValue().toString();
                logger.debug("IIdField value : " + value);
            }
            else if (field instanceof ICurrencyField) {
                ICurrencyField idField = (ICurrencyField) field;
                Object[] currValues = new Object[2];
                currValues[0] = idField.getLocalCurrency();
                currValues[1] = idField.getLocalAmount().toString();
                value = currValues;
                logger.debug("ICurrencyField value : " + currValues[1]);
            }
            else if (field instanceof IFloatField) {
                IFloatField idField = (IFloatField) field;
                value = idField.getValue();
                logger.debug("IFloatField value : " + value);
            }
            else if (field instanceof IIntegerField) {
            	IIntegerField integerField = (IIntegerField) field;
                value = integerField.getValue();
                logger.debug("IIntegerField value : " + value);
            }
            if (value != null) {
                return value;
            }
        }
        else
            logger.debug("Field value not retrieved : " + field);
        return null;
    }

    /**
     * Gets the field value as Object
     * 
     * @param field
     * @param logger
     * @return
     */
    public Object getFieldValueAsObject(final IField field, boolean getBaseValue) {
        if (field != null && !field.isNull()) {
            logger.debug("getFieldValue : " + field.getName() + " type " + field.getDataType());
            Object value = null;
            if (field instanceof IDateField) {
                IDateField dateField = (IDateField) field;
                if (dateField.getValue() != null) {
                    value = dateField.getValue();
                }
                logger.debug("IDateField value  : " + value);
            }
            else if (field instanceof IStringField) {
                IStringField stringField = (IStringField) field;
                value = stringField.getValue();
                logger.debug("IStringField value : " + value);
            }
            else if (field instanceof IEnumField) {
                IEnumField stringField = (IEnumField) field;
                value = stringField.getEnumValue().getName();
                logger.debug("IEnumField value : " + value);
            }
            else if (field instanceof IIdField) {
                IIdField idField = (IIdField) field;
                value = idField.getValue().toString();
                logger.debug("IIdField value : " + value);
            }
            else if (field instanceof ICurrencyField) {
                ICurrencyField idField = (ICurrencyField) field;
                Object[] currValues = new Object[2];
            	if (!getBaseValue)
            	{
                currValues[0] = idField.getLocalCurrency();
                currValues[1] = idField.getLocalAmount().toString();
            	}
            	else
            	{
                currValues[0] = idField.getBaseCurrency();
                currValues[1] = idField.getBaseAmount().toString();
            	}
                value = currValues;
                logger.debug("ICurrencyField value : " + currValues[1]);
            }
            else if (field instanceof IFloatField) {
                IFloatField idField = (IFloatField) field;
                value = idField.getValue();
                logger.debug("IFloatField value : " + value);
            }
            if (value != null) {
                return value;
            }
        }
        else
            logger.debug("Field value not retrieved : " + field);
        return null;
    }
    /**
     * Gets the field value as Object
     * 
     * @param field
     * @param logger
     * @return
     */
    public Object getFieldValue(String fieldStr, IGRCObject resource) {
        IField field = resource.getField(fieldStr);
        Object fieldValue = getFieldValueAsObject(field);
        logger.debug("fieldStr :" + fieldStr + " : fieldValue :" + fieldValue);
        return fieldValue;

    }

    /**
     * Gets the field value as Double for calculation
     * 
     * @param resource
     * @param operandOneInfo
     * @return
     */
    public Double getFieldValueAsDouble(IGRCObject resource, String operandOneInfo) {
        Double theValue = null;
        Double number = isNumber(operandOneInfo);
        if (number != null) {
            theValue = number;
            return theValue;
        }
        IField field = resource.getField(operandOneInfo);

        if (field != null) {
            switch (field.getDataType()) {
            case CURRENCY_TYPE: {
                ICurrencyField source = (ICurrencyField) field;

                theValue = source.getBaseAmount();
                logger.debug("getDoubleFieldValue CURRENCY_TYPE :" + theValue);
                break;
            }
            case FLOAT_TYPE: {
                IFloatField source = (IFloatField) field;

                theValue = source.getValue();
                logger.debug("getDoubleFieldValue FLOAT_TYPE :" + theValue);
                break;
            }
            case INTEGER_TYPE: {
                IIntegerField source = (IIntegerField) field;
                try {
                    theValue = Double.parseDouble(source.getValue().toString());
                    logger.debug("getDoubleFieldValue INTEGER_TYPE :" + theValue);
                } catch (Exception e) {
                    theValue = null;
                }
                break;
            }
            case ENUM_TYPE: {
                IEnumField source = (IEnumField) field;
                try {
                    theValue = isNumber(source.getEnumValue().getName());
                    logger.debug("getDoubleFieldValue INTEGER_TYPE :" + theValue);
                } catch (Exception e) {
                    theValue = null;
                }
                break;
            }
            default:
                logger.debug("Unsupported Data type for calculation " + field.getDataType().name());
                break;
            }
            logger.debug("fieldStr :" + operandOneInfo + " : fieldValue :" + theValue);
        }
        else {
            logger.error("Field is not defined : " + operandOneInfo);
        }

        return theValue;
    }

    /**
     * Checks if the fieldValue is number
     * 
     * @param operandInfo
     * @return
     */
    private static Double isNumber(String operandInfo) {
        Double fieldValue = null;
        Scanner numberScanner = new Scanner(operandInfo);
        if (numberScanner.hasNextDouble()) {
            fieldValue = numberScanner.nextDouble();
        }
        numberScanner.close();
        return fieldValue;
    }

    /**
     * Checks if the fieldValue is number
	 * 
	 * @param operandInfo
	 * @return
	 */
	private static BigDecimal isNumber(String operandInfo, String type) {
		BigDecimal fieldValue = null;
		Scanner numberScanner = new Scanner(operandInfo);
		if (numberScanner.hasNextBigDecimal()) {
			fieldValue = numberScanner.nextBigDecimal();
		}
		numberScanner.close();
		return fieldValue;
	}


	/**
     * Sets the field value on Resource
     * 
     * @param fieldName
     * @param valueForField
     * @param sourceObject
     * @param ics
     */
    public void setFieldValueOnResource(String fieldName, Object valueForField, IGRCObject sourceObject,
            IConfigurationService ics) {
        logger.debug("Entered   setFieldValueOnResource :" + fieldName + " : value :" + valueForField);
        IField ifieldName = sourceObject.getField(fieldName);

        if (ifieldName != null) {
            DataType dataType = ifieldName.getDataType();

            switch (dataType) {
            case FLOAT_TYPE:
                // This is when decimal field is getting base currency value
                if (valueForField != null && valueForField instanceof Object[]) {
                    Object value = ((Object[]) valueForField)[1];
                    valueForField = Double.valueOf(value.toString());
                }
                else if(valueForField != null && valueForField instanceof BigDecimal) 
                {
                	valueForField = Double.valueOf(valueForField.toString());
                }
                ResourceUtil.setFieldValue(ifieldName, valueForField);
                break;
            case DATE_TYPE:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
                try {
                    if (valueForField != null) {
                        ResourceUtil.setFieldValue(ifieldName, sdf.parse(valueForField.toString()));
                    }
                } catch (ParseException e) {
                    logger.error("Exception Parsing the field value: " + valueForField);
                    e.printStackTrace();
                }
                break;
            case CURRENCY_TYPE:
                // Case when the Base currency value is passed.
                if (valueForField == null) {
                    ICurrencyField currField = (ICurrencyField) ifieldName;
                    logger.debug("set currency field  :" + fieldName + " : null :" + currField.getLocalCurrency()
                            + currField.getBaseCurrency());
                    currField.setLocalAmount(null);
                    currField.setLocalCurrency(ics.getBaseCurrency());
                    logger.debug("set currency field  :" + fieldName + " : null :");
                }
                else {
				if (valueForField instanceof BigDecimal){
					ICurrencyField currField = (ICurrencyField) ifieldName;
	                logger.debug("set currency field  :" + fieldName + " : null :" + currField.getLocalCurrency()
	                        + currField.getBaseCurrency());
	                valueForField = ((BigDecimal) valueForField).setScale(2, RoundingMode.CEILING);
	                currField.setLocalAmount(Double.valueOf(valueForField.toString()));
	                currField.setLocalCurrency(ics.getBaseCurrency());
					logger.debug("set field BigDecimal :" + fieldName + " : value :" + valueForField);
				}
				else 
				{ 
                    if (!(valueForField instanceof Object[])) {
					
                        Object[] currValue = new Object[2];
                        currValue[1] = valueForField.toString();
                        currValue[0] = ics.getBaseCurrency();
                        valueForField = currValue;
                    }
                    logger.debug("set field  :" + fieldName + " : value :" + valueForField);
                    ResourceUtil.setFieldValue(ifieldName, valueForField);
                }
			}
                break;
            case INTEGER_TYPE:
                if (valueForField != null) {
                    logger.debug("set integer  :" + fieldName + " : value :"
                            + Double.valueOf(valueForField.toString()).intValue());
                    ResourceUtil.setFieldValue(ifieldName, Double.valueOf(valueForField.toString()).intValue());
                }
                break;
            case STRING_TYPE:
            case LARGE_STRING_TYPE:
            case MEDIUM_STRING_TYPE:
            case UNLIMITED_STRING_TYPE:
                if (valueForField != null) {
                    logger.debug("set string field:" + fieldName + " : value :"
                            + valueForField);
                    ResourceUtil.setFieldValue(ifieldName, "" + valueForField);
                }
                break;
            default:
                logger.debug("set field  :" + fieldName + " : value :" + valueForField);
                ResourceUtil.setFieldValue(ifieldName, valueForField);
                break;
            }
        }
        else {
            logger.error(
                    "Exiting with null value: setFieldValueOnResource :" + fieldName + " : value :" + valueForField);
        }
    }

    /**
     * Performs the evaluation of the operator, add, subtract, multiply, divide
     * on Field Values
     * 
     * @param operandOneValue
     * @param operandTwoValue
     * @param operatorInfo
     * @return
     */
    public Double performEvaluation(Double operandOneValue, Double operandTwoValue, String operatorInfo) {

        Double resultValue = null;

        if ((operandOneValue != null) && (operandTwoValue != null)) {
            // Apply the operation on 2 operands.
            if (operatorInfo.equalsIgnoreCase(ADD)) {
                resultValue = operandOneValue + operandTwoValue;
                logger.debug("performEvaluation  operator is 'ADD' result=" + resultValue);
            }
            else if (operatorInfo.equalsIgnoreCase(SUBTRACT)) {
                resultValue = operandOneValue - operandTwoValue;
                logger.debug("performEvaluation  operator is 'SUBTRACT' result=" + resultValue);
            }
            else if (operatorInfo.equalsIgnoreCase(DIVIDE)) {
                // This is the only case where we need to check an operand value
                if (operandTwoValue.longValue() != 0) {
                    BigDecimal divideValue = new BigDecimal(operandOneValue).divide(new BigDecimal(operandTwoValue), 2,
                            RoundingMode.FLOOR);
                    logger.debug("performEvaluation  operator is 'DIVIDE' result=" + divideValue);
                    resultValue = divideValue.doubleValue();
                    logger.debug("performEvaluation  operator is 'DIVIDE' result=" + resultValue);
                }
                else {
                    resultValue = Double.NaN;
                    logger.debug("performEvaluation  attempted 'DIVIDE' by ZERO!! gives " + resultValue);
                }
                logger.debug("performEvaluation  operator is 'DIVIDE' result=" + resultValue);
            }
            else if (operatorInfo.equalsIgnoreCase(MULTIPLY)) {
                resultValue = operandOneValue * operandTwoValue;
                logger.debug("performEvaluation: Test  operator is 'MULTIPLY' result=" + resultValue);
            }
            else {
                logger.debug("performEvaluation: Test  has an unknown/unimplemented operator! (" + operatorInfo + ")");
            }
        }
        else {
            logger.debug("performEvaluation: Test  One of the operands (" + operandOneValue + "),(" + operandTwoValue
                    + ") fields has null value)");
        }
        return resultValue;
    }
	/**Gets the field value as Double for calculation
	 * 
	 * @param resource
	 * @param operandOneInfo
	 * @return
	 */
		public BigDecimal getFieldValueAsBigDecimal(IGRCObject resource, String operandOneInfo) {
			BigDecimal theValue = null;
			BigDecimal number = isNumber(operandOneInfo, "");
			logger.debug("operandOneInfo" + operandOneInfo + "number :" + number);

			if (number != null) {
				theValue = number;
				return theValue;
			}
			
			IField field = resource.getField(operandOneInfo);
			
			if (!field.isNull()){

			switch (field.getDataType()) {
			case CURRENCY_TYPE: {
				ICurrencyField source = (ICurrencyField) field;
				if (source.getBaseAmount()!=null) {
					logger.debug("getDoubleFieldValue CURRENCY_TYPE source.getBaseAmount():" + source.getBaseAmount());
					theValue = new BigDecimal(source.getBaseAmount());
					logger.debug("getDoubleFieldValue CURRENCY_TYPE :" + theValue);
					theValue = theValue.setScale(2, RoundingMode.CEILING);
					logger.debug("theValue CURRENCY_TYPE :" + theValue);
				}
				break;
			}
			case FLOAT_TYPE: {
				IFloatField source = (IFloatField) field;
				theValue = new BigDecimal(source.getValue());
				logger.debug("getDoubleFieldValue FLOAT_TYPE :" + theValue);
				break;
			}
			case INTEGER_TYPE: {
				IIntegerField source = (IIntegerField) field;
				try {
					theValue = new BigDecimal(source.getValue().toString());
					logger.debug("getDoubleFieldValue INTEGER_TYPE :" + theValue);
				} catch (Exception e) {
					theValue = null;
				}
				break;
			}
			case ENUM_TYPE: {
				IEnumField source = (IEnumField) field;
				try {
					theValue = new BigDecimal(isNumber(source.getEnumValue().getName()));
					logger.debug("getDoubleFieldValue INTEGER_TYPE :" + theValue);
				} catch (Exception e) {
					theValue = null;
				}
				break;
			}
			default:
				logger.debug("Unsupported Data type for calculation " + field.getDataType().name());
				break;
			}
			logger.debug("fieldStr :" + operandOneInfo + " : fieldValue :" + theValue);
			}
			else
			{
				logger.debug("fieldStr :" + operandOneInfo + " : is null :");
			}
			return theValue;
		}

	public BigDecimal performEvaluation(BigDecimal operandOneValue, BigDecimal operandTwoValue, String operatorInfo) {

		BigDecimal resultValue = null;

		if ((operandOneValue != null) && (operandTwoValue != null)) {
			operandOneValue = operandOneValue.setScale(2, RoundingMode.HALF_UP);
			operandTwoValue = operandTwoValue.setScale(2, RoundingMode.HALF_UP);
			logger.debug("operandOneValue=" + operandOneValue);
			logger.debug("operandTwoValue=" + operandTwoValue);

			//Apply the operation on 2 operands.
			if (operatorInfo.equalsIgnoreCase(ADD)) {
				resultValue = operandOneValue.add(operandTwoValue);
				logger.debug("performEvaluation  operator is 'ADD' result=" + resultValue);
			} else if (operatorInfo.equalsIgnoreCase(SUBTRACT)) {
				resultValue = operandOneValue.subtract(operandTwoValue);
				logger.debug("performEvaluation  operator is 'SUBTRACT' result=" + resultValue);
			} else if (operatorInfo.equalsIgnoreCase(DIVIDE)) {
				// This is the only case where we need to check an operand value
				if (operandTwoValue.longValue() != 0) {
					resultValue = operandOneValue.divide(operandTwoValue);
				} else {
					resultValue = new BigDecimal(Double.MAX_VALUE);
					logger.debug("performEvaluation  attempted 'DIVIDE' by ZERO!! gives " + resultValue);
				}
				logger.debug("performEvaluation  operator is 'DIVIDE' result=" + resultValue);
			} else if (operatorInfo.equalsIgnoreCase(MULTIPLY)) {
				resultValue = operandOneValue.multiply(operandTwoValue);
				logger.debug("performEvaluation: Test  operator is 'MULTIPLY' result=" + resultValue);
			} else {
				logger.error("performEvaluation: Test  has an unknown/unimplemented operator! ("
						+ operatorInfo + ")");
			}
		} else {
			logger.error("performEvaluation: Test  One of the operands (" + operandOneValue + "),("
					+ operandTwoValue + ") fields has null value)");
		}
		return resultValue;
	}

    /**
     * Performs the evaluation of the operator, add, subtract, multiply, divide
     * on Field Values in a string format fieldValue1 operator fieldValue2
     * 
     * @param operandOneValue
     * @param operandTwoValue
     * @param operatorInfo
     * @return
     */

	public BigDecimal getCalculateValue(String fieldValueStr, IGRCObject resource) {
        String operator = null;
        if (fieldValueStr.contains(ADD)) {
            operator = ADD;
        }
        else if (fieldValueStr.contains(SUBTRACT)) {
            operator = SUBTRACT;
        }
        else if (fieldValueStr.contains(MULTIPLY)) {
            operator = MULTIPLY;
        }
        else if (fieldValueStr.contains(DIVIDE)) {
            operator = DIVIDE;
        }
        logger.debug("operator : " + operator);
		BigDecimal evaluatedValue = null;
        String[] operands = fieldValueStr.split(operator);
		BigDecimal[] operandVals = new BigDecimal[2];
        for (int i = 0; i < operands.length; i++) {
			operandVals[i] = getFieldValueAsBigDecimal(resource, operands[i].trim());
        }
        logger.debug("field value : " + operandVals[0] + " and " + operandVals[1]);
        if (operandVals[0] != null && operandVals[1] != null) {
            evaluatedValue = performEvaluation(operandVals[0], operandVals[1], operator);
			evaluatedValue = evaluatedValue.setScale(2, RoundingMode.CEILING);
        }
        logger.debug("evaluatedValue : " + evaluatedValue);
        return evaluatedValue;
    }

    /**
     * Creating list of groups returned by the query
     * 
     * @param context
     * @param queryStmt
     * @param iQueryService
     * @param grcObject
     */
    public ArrayList<String> getQueryResults(String queryStmt, IQueryService iQueryService, Object[] arr) {
        logger.debug("Starting getResults method " + queryStmt + " arr " + Arrays.toString(arr));
        ArrayList<String> recordsList = new ArrayList<String>();

        queryStmt = MessageFormat.format(queryStmt, arr);

        logger.debug("About to execute Query " + queryStmt);
        try {
            IQuery query = iQueryService.buildQuery(queryStmt);
            ITabularResultSet resultset = query.fetchRows(0);
            for (IResultSetRow row : resultset) {
                IField field = row.getField(0);
                if (field instanceof IIdField) {
                    IIdField idField = (IIdField) field;
                    if (idField.getValue() != null) {
                        String value = idField.getValue().toString();
                        recordsList.add(value);
                    }
                }
                else if (field instanceof IFloatField) {
                    IFloatField floatField = (IFloatField) field;
                    if (floatField.getValue() != null) {
                        String value = floatField.getValue().toString();
                        recordsList.add(value);
                    }
                }
                else if (field instanceof ICurrencyField) {
                    ICurrencyField currencyField = (ICurrencyField) field;
                    if (currencyField.getBaseAmount() != null) {
                        String value = currencyField.getBaseAmount().toString();
                        recordsList.add(value);
                    }
                }
                else if (field instanceof IIntegerField) {
                    IIntegerField integerField = (IIntegerField) field;
                    if (integerField.getValue() != null) {
                        String value = integerField.getValue().toString();
                        recordsList.add(value);
                    }
                }
                else if (field instanceof IStringField) {
                    IStringField stringField = (IStringField) field;
                    if (stringField.getValue() != null) {
                        String value = stringField.getValue().toString();
                        recordsList.add(value);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error in Query Service execution" + ex);
        }

        logger.debug("Query returned count=" + recordsList.size() + "; results: "+recordsList);
        return recordsList;
    }

    /**
     * Gets the Registry Values
     * 
     * @param registryEntry
     * @param m_cs
     * @param logger
     * @return
     */
    public String getRegistryValue(String registryEntry, IConfigurationService m_cs) {
        String registryEntryVal = null;
        try {
            IConfigProperties settings = m_cs.getConfigProperties();
            registryEntryVal = settings.getProperty(registryEntry);
            return registryEntryVal;
        } catch (Exception e) {
            logger.error(" Error while reading registry ", e);
            return null;
        }
    }

    /**
     * Sets null value for field
     * 
     * @param dstField
     */
    public void setNullValue(IField dstField) {
        switch (dstField.getDataType()) {
        case DATE_TYPE: {
            IDateField target = (IDateField) dstField;
            if (target != null)
                target.setValue(null);
            break;
        }
        case ENUM_TYPE: {
            IEnumField target = (IEnumField) dstField;
            if (target != null)
                target.setEnumValue(null);
            break;
        }

        case STRING_TYPE: {
            IStringField target = (IStringField) dstField;
            if (target != null)
                target.setValue(null);
            break;
        }
        case INTEGER_TYPE: {
            IIntegerField target = (IIntegerField) dstField;
            if (target != null)
                target.setValue(null);

            break;
        }
        case BOOLEAN_TYPE: {
            IBooleanField target = (IBooleanField) dstField;
            if (target != null)
                target.setValue(null);
            break;
        }
        case CURRENCY_TYPE: {

            ICurrencyField target = (ICurrencyField) dstField;
            if (target != null) {
                target.setLocalCurrency(null);
                target.setLocalAmount(null);
                target.setExchangeRate(null);
            }
            break;
        }
        case FLOAT_TYPE: {
            IFloatField target = (IFloatField) dstField;
            target.setValue(null);

            break;
        }
        case MULTI_VALUE_ENUM: {

            IMultiEnumField target = (IMultiEnumField) dstField;
            target.setEnumValues(null);

            break;
        }
        default: {
            logger.debug("setNullValue for " + dstField.getDataType() + " datatype not supported!");

            break;
        }
        }
    }

    /**
     * 
     * @param key
     * @param m_cs
     * @return
     */
    public String getAppText(String key, IConfigurationService m_cs) {
        try {
            return m_cs.getLocalizedApplicationText(key);
        } catch (Exception e) {
            logger.error("Error while reading app text ", e);
            return null;
        }
    }

    public String getFirstResultOfQuery(String queryStmt, IQueryService queryService, String...parameters) {
        List<String> results = getQueryResultsMultipleReturnFields(queryStmt, (Object[])parameters, 1, queryService, null);
        String result = "";
        if (results.size() > 0) {
            result = results.get(0);
        }
        return result;
    }

    public ArrayList<String> getQueryResultsMultipleReturnFields(String queryStmt, Object[] arr,
            int numberOfReturnParams, IQueryService queryService) {
        return getQueryResultsMultipleReturnFields(queryStmt, arr, numberOfReturnParams, queryService, null);
    }

    public ArrayList<String> getQueryResultsMultipleReturnFields(String queryStmt, Object[] arr,
            int numberOfReturnParams, IQueryService queryService, Boolean setCaseInsensitiveQuery) {
        ArrayList<String> results = new ArrayList<String>();

        queryStmt = MessageFormat.format(queryStmt, arr);

        logger.debug("About to execute Query " + queryStmt);
        try {
            IQuery query = queryService.buildQuery(queryStmt);
            if (setCaseInsensitiveQuery != null) {
                query.setCaseInsensitive(setCaseInsensitiveQuery);
            }

            ITabularResultSet resultset = query.fetchRows(0);
            for (IResultSetRow row : resultset) {
                String returnRecord = "";
                for (int i = 0; i < numberOfReturnParams; i++) {
                    IField field = row.getField(i);
                    String value = null;
                    if (field instanceof IIdField) {
                        value = ((IIdField) field).getValue() + "";
                    }
                    else if (field instanceof IStringField) {
                        value = ((IStringField) field).getValue() + "";
                    }
                    else if (field instanceof IFloatField) {
                        value = ((IFloatField) field).getValue() + "";
                    }
                    else if (field instanceof ICurrencyField) {
                        // local currency ISO Code, base amount, exchange rate
                        ICurrency currency = ((ICurrencyField) field).getLocalCurrency();
                        if (currency != null) {
                            String currencyCode = currency.getCurrencyCode().name();
                            if (currencyCode != null) {
                                value = currencyCode;
                            }
                        }
                        value += ",";
                        Double baseAmount = ((ICurrencyField) field).getBaseAmount();
                        if (baseAmount != null) {
                            value += new BigDecimal(baseAmount).toPlainString();
                        }
                        value += ",";

                        Double exchangeRate = ((ICurrencyField) field).getExchangeRate();
                        if (exchangeRate != null) {
                            value += exchangeRate;
                        }
                        //logger.debug("currency field value: " + value);
                    }
                    else if (field instanceof IEnumField) {
                        IEnumValue enumValue = ((IEnumField) field).getEnumValue();
                        if (enumValue != null) {
                            value = enumValue.getName();
                        }
                    }
                    returnRecord += RECORD_COLUMN_SEPARATOR
                            + ((value == null || value.equalsIgnoreCase(NULL)) ? "" : value.trim());
                }

                if (returnRecord.length() > 0) {
                    returnRecord = returnRecord.substring(RECORD_COLUMN_SEPARATOR.length());
                }
                results.add(returnRecord);
            }
        } catch (Exception e) {
            logger.error("Error in Query Service execution: " + e);
            StackTraceElement[] stes = e.getStackTrace();
            StringBuilder sb = new StringBuilder(e.getMessage() + "\n\n");

            for (int i = 0; i < stes.length; i++) {
                sb.append(stes[i] + "\n");
            }

            logger.error("" + sb);
        }
		logger.debug("Query returned count=" + results.size() + "; results: " + results);

		return results;
    }

    public Double getComplexFieldValueAsDouble(IGRCObject resource, String operandInfo) {
        Double result = null;
        logger.debug("operandInfo=" + operandInfo);

        if (operandInfo.contains(MULTIPLY)) {
            String[] operands = operandInfo.split(MULTIPLY);
            Double operandOne = getFieldValueAsDouble(resource, operands[0].trim());
            Double operandTwo = getFieldValueAsDouble(resource, operands[1].trim());
            result = performEvaluation(operandOne, operandTwo, MULTIPLY);
        }
        else {
            return getFieldValueAsDouble(resource, operandInfo);
        }
        return result;

    }
	public void copyField(IField srcField,IField dstField)
    {
        

        switch (dstField.getDataType())
        {
            case BOOLEAN_TYPE:
            {
                IBooleanField source = (IBooleanField) srcField;
                IBooleanField target = (IBooleanField) dstField;

                target.setValue(source.getValue());
                break;
            }
            case CURRENCY_TYPE:
            {
            	
            	ICurrencyField source = (ICurrencyField) srcField;
				ICurrencyField target = (ICurrencyField) dstField;
				if(source.getLocalAmount()!=null) {
					target.setLocalCurrency(source.getLocalCurrency());
					target.setLocalAmount(source.getLocalAmount());
					target.setExchangeRate(source.getExchangeRate());
				} 
				
                break;
            }
            case DATE_TYPE:
            {
                IDateField source = (IDateField) srcField;
                IDateField target = (IDateField) dstField;

                target.setValue(source.getValue());
                break;
            }
            case ENUM_ANSWERS_TYPE:
            {
                IEnumAnswersField source = (IEnumAnswersField) srcField;
                IEnumAnswersField target = (IEnumAnswersField) dstField;

                target.clearEnumAnswers();
                List<IEnumAnswer> cheatList = source.getEnumAnswers();
                for (IEnumAnswer cheat : cheatList)
                {
                    target.appendEnumAnswer(cheat.getName(), cheat.getScore().intValue() 
                            , cheat.isHidden(), cheat.getDescription(), cheat.getScore(), cheat.getRequires());
                }
                logger.debug("copyField assign for ENUM_ANSWERS may give incorrect value!");
                break;
            }
            case ENUM_TYPE:
            {
                IEnumField source = (IEnumField) srcField;
                IEnumField target = (IEnumField) dstField;

                target.setEnumValue(source.getEnumValue());
                
                break;
            }
            case FLOAT_TYPE:
            {
                IFloatField source = (IFloatField) srcField;
                IFloatField target = (IFloatField) dstField;

                target.setValue(source.getValue());
                
                break;
            }
            case ID_TYPE:
            {
            	logger.debug("copyField assign for ID is strictly forbidden!");
                
                break;
            }
            case INTEGER_TYPE:
            {
                IIntegerField source = (IIntegerField) srcField;
                IIntegerField target = (IIntegerField) dstField;

                target.setValue(source.getValue());
                
                break;
            }
            case MULTI_VALUE_ENUM:
            {
                IMultiEnumField source = (IMultiEnumField) srcField;
                IMultiEnumField target = (IMultiEnumField) dstField;

                target.setEnumValues(source.getEnumValues());
               
                break;
            }
            case REFERENCE_TYPE:
            {
                IReferenceField source = (IReferenceField) srcField;
                IReferenceField target = (IReferenceField) dstField;

                target.setValue(source.getValue());
               
                break;
            }
            case STRING_TYPE:
            {
                IStringField source = (IStringField) srcField;
                IStringField target = (IStringField) dstField;

                target.setValue(source.getValue());
                
                break;
            }
            case MEDIUM_STRING_TYPE:
            {
                IStringField source = (IStringField) srcField;
                IStringField target = (IStringField) dstField;

                target.setValue(source.getValue());
                
                break;
            }
            case LARGE_STRING_TYPE:
            {
                IStringField source = (IStringField) srcField;
                IStringField target = (IStringField) dstField;

                target.setValue(source.getValue());
                
                break;
            }
            case UNLIMITED_STRING_TYPE:
            {
                IStringField source = (IStringField) srcField;
                IStringField target = (IStringField) dstField;

                target.setValue(source.getValue());
                
                break;
            }
            default:
            {
            	logger.info("copyField from Parent to child for "+srcField.getDataType()+" datatype has been requested!"
                        + " Only known datatypes are:- BOOLEAN, CURRENCY, DATE, ENUM_ANSWERS, ENUM, FLOAT, INTEGER, MULTI_ENUM, REFERENCE,STRING,MEDIUM_STRING,LARGE_STRING AND UNLIMITED_STRING");
                
                break;
            }
        }
    }
	/**
     * 
     * @param string
     * @param resource
     * @return
     */
	public Object[] getParams(String string, IGRCObject resource) {
		Object[] params = new Object[]{};
		if (StringUtil.isGood(string))
		{
			String[] fields = string.split("\\,");
			params = new Object[fields.length];
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].equals("Name"))
				{
					params[i] = resource.getName();
				}
				else if (fields[i].equals("Resource ID"))
				{
					params[i] = resource.getId().toString();
				} else {
					Object fldValue = null;
					if (fields[i].startsWith("REMOVELINK:"))
					{
						fields[i] = fields[i].replace("REMOVELINK:", "");
						fldValue = getFieldValue(fields[i], resource);
						logger.debug("REMOVELINK "+fldValue);
						if (fldValue!=null)
						{
							String fieldvalue = (String) fldValue;
							fieldvalue = fieldvalue.substring(fieldvalue.indexOf("'>") + 2, fieldvalue.indexOf("</a>"));
							logger.debug("fieldvalue : "+fieldvalue);
							fldValue = fieldvalue;
						}
						params[i] = fldValue;
					}
					else 
					{
						params[i] = getFieldValue(fields[i], resource);
					}
				}
				logger.debug("params["+i+"] "+params[i]);
			}
		}
		return params;
	}

    private IGRCObject getGRCObject(Id objectId, boolean withChildren, boolean withParents,
            List<String> associationTypes, IServiceFactory serviceFactory) {
        GRCObjectFilter filter = new GRCObjectFilter(
                serviceFactory.createConfigurationService().getCurrentReportingPeriod());

        if (withChildren && withParents) {
            filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.BOTH);
        }
        else if (withChildren) {
            filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.CHILD);
        }
        else if (withParents) {
            filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.PARENT);
        }

        List<ITypeDefinition> types = new ArrayList<ITypeDefinition>();
        if (associationTypes != null && associationTypes.size() > 0) {
            for (String typeStr : associationTypes) {
                types.add(serviceFactory.createMetaDataService().getType(typeStr));
            }
            filter.getAssociationFilter().setTypeFilters(types.toArray(new ITypeDefinition[types.size()]));
            //TODO: check if setIncludeAssociations should be set ??
        }

        IGRCObject object = serviceFactory.createResourceService().getGRCObject(objectId, filter);
        return object;
    }
    
    public IGRCObject getGRCObject(Id objectId, IServiceFactory serviceFactory) {
        return getGRCObject(objectId, false, false, null, serviceFactory);
    }

    public IGRCObject getGRCObjectWithChildren(Id objectId, IServiceFactory serviceFactory) {
        return getGRCObject(objectId, true, false, null, serviceFactory);
    }

    public IGRCObject getGRCObjectWithParents(Id objectId, IServiceFactory serviceFactory) {
        return getGRCObject(objectId, false, true, null, serviceFactory);
    }

    public IGRCObject getGRCObjectWithParentsAndChildren(Id objectId, IServiceFactory serviceFactory) {
        return getGRCObject(objectId, true, true, null, serviceFactory);
    }

    public IGRCObject getGRCObjectWithChildren(Id objectId, List<String> childrenTypes,
            IServiceFactory serviceFactory) {
        return getGRCObject(objectId, true, false, childrenTypes, serviceFactory);
    }

    public IGRCObject getGRCObjectWithParents(Id objectId, List<String> parentTypes,
            IServiceFactory serviceFactory) {
        return getGRCObject(objectId, false, true, parentTypes, serviceFactory);
    }

    private IGRCObject getGRCObject(String relativePath, String objectType, boolean withChildren, boolean withParents,
            List<String> associationTypes, IServiceFactory serviceFactory) {
        GRCObjectFilter filter = new GRCObjectFilter(
                serviceFactory.createConfigurationService().getCurrentReportingPeriod());
       // logger.debug("entered getGRCObject; relativePath: " + relativePath + ", objectType: " + objectType
       //         + ", withChildren: " + withChildren + " withParents: " + withParents + ", associationTypes: "
        //        + associationTypes);
        if (withChildren && withParents) {
            filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.BOTH);
        }
        else if (withChildren) {
            filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.CHILD);
        }
        else if (withParents) {
            filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.PARENT);
        }

        List<ITypeDefinition> types = new ArrayList<ITypeDefinition>();
        if (associationTypes != null && associationTypes.size() > 0) {
            for (String typeStr : associationTypes) {
                types.add(serviceFactory.createMetaDataService().getType(typeStr));
            }
            filter.getAssociationFilter().setTypeFilters(types.toArray(new ITypeDefinition[types.size()]));
        }
        //works for business entities; not tested for other object types yet
        String basePath = serviceFactory.createMetaDataService().getType(objectType).getRootFolderPath();
       // logger.debug("base path for \"" + objectType + "\" object tye: " + basePath);

        String relativePathLastEntry = relativePath.substring(relativePath.lastIndexOf(SLASH)).trim();
       // logger.debug("relativePathLastEntry: " + relativePathLastEntry);
        while (relativePath.startsWith(SLASH)) {
            relativePath = relativePath.substring(SLASH.length());
        }
        String fullPath = basePath + SLASH + relativePath.trim() + relativePathLastEntry + DOT_TXT;
       // logger.debug("Full path: " + fullPath);

        IGRCObject object = serviceFactory.createResourceService().getGRCObject(fullPath, filter);
        return object;
    }

    public IGRCObject getGRCObject(String relativePath, String objectType, IServiceFactory serviceFactory) {
        return getGRCObject(relativePath, objectType, false, false, null, serviceFactory);
    }

    public IGRCObject getGRCObjectWithChildren(String relativePath, String objectType,
            IServiceFactory serviceFactory) {
        return getGRCObject(relativePath, objectType, true, false, null, serviceFactory);
    }

    public IGRCObject getGRCObjectWithParents(String relativePath, String objectType, IServiceFactory serviceFactory) {
        return getGRCObject(relativePath, objectType, false, true, null, serviceFactory);
    }

    public IGRCObject getGRCObjectWithParentsAndChildren(String relativePath, String objectType,
            IServiceFactory serviceFactory) {
        return getGRCObject(relativePath, objectType, true, true, null, serviceFactory);
    }

    public IGRCObject getGRCObjectWithChildren(String relativePath, String objectType, List<String> childrenTypes,
            IServiceFactory serviceFactory) {
        return getGRCObject(relativePath, objectType, true, false, childrenTypes, serviceFactory);
    }

    public IGRCObject getGRCObjectWithParents(String relativePath, String objectType, List<String> parentTypes,
            IServiceFactory serviceFactory) {
        return getGRCObject(relativePath, objectType, true, false, parentTypes, serviceFactory);
    }
    
    public List<String> getEntitiesExcludingOnesUnderPath(String basePathOfEntitiesToExclude, List<String> entities, int indexOfLocationColumnInEntityRecord) {
        List<String> entitiesToExclude = new ArrayList<String>();
        for (String entity : entities) {
            String path = entity.split(CommonResourceUtils.RECORD_COLUMN_SEPARATOR)[indexOfLocationColumnInEntityRecord].trim();

            if (!basePathOfEntitiesToExclude.startsWith(SLASH)) {
                basePathOfEntitiesToExclude = SLASH + basePathOfEntitiesToExclude;
            }

            if (path.equals(basePathOfEntitiesToExclude) || path.startsWith(basePathOfEntitiesToExclude + SLASH)) {
              //  logger.debug("entity relative path: " + path + "; adding it to excluded entities list");
                entitiesToExclude.add(entity);
            }
        }
        //  logger.debug("entities to exclude: " + entitiesToExclude);

        for (String entityToExclude : entitiesToExclude) {
            entities.remove(entityToExclude);
        }

        //  logger.debug("entities after filtering ones under path \"" + entities + "\": " + entities);

        return entities;
    }
    
    /**
     * Creating list of groups returned by the query
     * 
     * @param context
     * @param queryStmt
     * @param iQueryService
     * @param grcObject
     */
    public ArrayList<String> getQueryResults(String queryStmt, IQueryService iQueryService, Object[] arr, boolean honorPrimary) {
        logger.debug("Starting getResults method " + queryStmt + " arr " + Arrays.toString(arr));
        ArrayList<String> recordsList = new ArrayList<String>();

        queryStmt = MessageFormat.format(queryStmt, arr);

        logger.debug("About to execute Query " + queryStmt);
        try {
            IQuery query = iQueryService.buildQuery(queryStmt);
            query.setHonorPrimary(honorPrimary);
            ITabularResultSet resultset = query.fetchRows(0);
            for (IResultSetRow row : resultset) {
                IField field = row.getField(0);
                if (field instanceof IIdField) {
                    IIdField idField = (IIdField) field;
                    if (idField.getValue() != null) {
                        String value = idField.getValue().toString();
                        recordsList.add(value);
                    }
                }
                else if (field instanceof IFloatField) {
                    IFloatField floatField = (IFloatField) field;
                    if (floatField.getValue() != null) {
                        String value = floatField.getValue().toString();
                        recordsList.add(value);
                    }
                }
                else if (field instanceof ICurrencyField) {
                    ICurrencyField currencyField = (ICurrencyField) field;
                    if (currencyField.getBaseAmount() != null) {
                        String value = currencyField.getBaseAmount().toString();
                        recordsList.add(value);
                    }
                }
                else if (field instanceof IIntegerField) {
                    IIntegerField integerField = (IIntegerField) field;
                    if (integerField.getValue() != null) {
                        String value = integerField.getValue().toString();
                        recordsList.add(value);
                    }
                }
                else if (field instanceof IStringField) {
                    IStringField stringField = (IStringField) field;
                    if (stringField.getValue() != null) {
                        String value = stringField.getValue().toString();
                        recordsList.add(value);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error in Query Service execution" + ex);
        }
        logger.debug("Query returned count=" + recordsList.size());
        return recordsList;
    }

    public IGRCObject copyUsersOrGroupsFromFirstAncestorEntityWhereValuesAreNonEmpty(IGRCObject object,
            String querySourceObject, String sourceFieldsListCsv, String destinationFieldsListCsv,
            IQueryService queryService, IResourceService resourceService, String parentIdParameter) {
        return copyUsersOrGroupsFromFirstAncestorEntityWhereValuesAreNonEmpty(object, querySourceObject,
                sourceFieldsListCsv, destinationFieldsListCsv, queryService, resourceService, parentIdParameter, true);
    }

    public IGRCObject copyUsersOrGroupsFromFirstAncestorEntityWhereValuesAreNonEmpty(IGRCObject object,
            String querySourceObject, String sourceFieldsListCsv, String destinationFieldsListCsv,
            IQueryService queryService, IResourceService resourceService, String initialParentIdParameter, boolean doSave) {
        logger.debug("entered copyUsersOrGroupsFromFirstAncestorWhereValuesAreNonEmpty(..)");
        logger.debug("object id: " + object.getId() + ", sourceFieldsListCsv: " + sourceFieldsListCsv
                + ", destinationFieldsListCsv: " + destinationFieldsListCsv + ", initialParentIdParameter: "
                + initialParentIdParameter + ", doSave: " + doSave);

        if (sourceFieldsListCsv == null || destinationFieldsListCsv == null) {
            logger.error("sourceFieldsListCsv or destinationFieldsListCsv is null; can't continue");
            return object;
        }

        List<String> fieldNamesOnSourceObject = Arrays.asList(sourceFieldsListCsv.split("\\s*,\\s*", -1));
        List<String> fieldNamesOnDestinationObject = Arrays.asList(destinationFieldsListCsv.split("\\s*,\\s*", -1));
        if (fieldNamesOnSourceObject.size() != fieldNamesOnDestinationObject.size()) {
            logger.error("fieldNamesOnSourceObject count !=fieldNamesOnDestinationObject; "
                    + "will use smaller length to iterate through them");
        }

        int numberOfFieldsToCopy = fieldNamesOnSourceObject.size();
        if (fieldNamesOnDestinationObject.size() < numberOfFieldsToCopy) {
            numberOfFieldsToCopy = fieldNamesOnDestinationObject.size();
        }

        int hierarchyUpNavigationLimit = 20;
        boolean fieldsWereModified = false;
        for (int i = 0; i < numberOfFieldsToCopy; i++) {
            int attemptsCount = 0;
            String fieldValue = "";
            String parentIdParameter = initialParentIdParameter;
            while (parentIdParameter != null && !parentIdParameter.isEmpty() && fieldValue.isEmpty()
                    && attemptsCount < hierarchyUpNavigationLimit) {
                // navigate up the hierarchy until non-empty field value is
                // found
                if (attemptsCount > 0) {
                    try {
                        parentIdParameter = ""
                                + resourceService.getGRCObject(new Id(parentIdParameter)).getPrimaryParent();
                    } catch (Exception e) {
                        logger.error(
                                "Error trying to go up the entity hierarchy. current object ID:" + parentIdParameter,
                                e);
                        parentIdParameter = "";
                    }
                }

				logger.debug("parentIdParameter on attempt 1: " + parentIdParameter);
				if (parentIdParameter != null && !parentIdParameter.trim().isEmpty()) {
					attemptsCount = attemptsCount + 1;
					try {
						fieldValue = getFirstResultOfQuery(querySourceObject, queryService,
								fieldNamesOnSourceObject.get(i), parentIdParameter);
					} catch (OpenPagesException ope) {
						fieldValue = "";
						logger.error("Error running query. Probably queried field is not defined on object type.");
					}
					logger.debug("Value of  field " + fieldNamesOnSourceObject.get(i) + " on entity(id "
                            + parentIdParameter + "): \"" + fieldValue + "\"; attempts: " + attemptsCount);
                }

                if (!fieldValue.isEmpty()) {
                    fieldsWereModified = true;
                    logger.debug("will set value of \"" + fieldNamesOnDestinationObject.get(i)
                            + "\" field on Loss Event to \"" + fieldValue + "\"");
                    IField field = object.getField(fieldNamesOnDestinationObject.get(i));
                    ResourceUtil.setFieldValue(field, fieldValue);
                }
            }
        }

        if (doSave && fieldsWereModified) {
            resourceService.saveResource(object);
        }
        logger.debug("exiting copyUsersOrGroupsFromFirstAncestorWhereValuesAreNonEmpty(..)");
        return object;
    }
}
