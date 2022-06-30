package com.ibm.openpages.ext.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.metadata.DataType;
import com.ibm.openpages.api.resource.ICurrencyField;
import com.ibm.openpages.api.resource.IEnumField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IStringField;
import com.openpages.apps.common.util.StringUtil;
import com.openpages.sdk.OpenpagesServiceException;
import com.openpages.sdk.OpenpagesSessionException;
import com.openpages.sdk.metadata.BundleTypeNotFoundException;
import com.openpages.sdk.metadata.PropertyTypeNotFoundException;

/**
 * This class checks the conditions based on attribute value for the numeric calculations accepts AND, OR and
 * Bracket attributes Fieldname should be in bundle:property format Currently
 * supports only with Decimal and Currency datatype
 * 
 * @author IBM, Inc.
 * 
 */
public class FieldArithmeticPropertyConditionalUtil {

	public static final String OPEN_BRACKET = "(";

	public static final String CLOSE_BRACKET = ")";

	public static final String AND_OPERATOR_TEXT = "AND";

	public static final String AND_OPERATOR = "&&";

	public static final String OR_OPERATOR_TEXT = "OR";

	public static final String OR_OPERATOR = "||";

	public static final String EQUAL_OPERATOR = "=";

	public static final String NOT_EQUAL_OPERATOR = "!=";

	public static final String GREATER_THAN = "GREATER_THAN";

	public static final String GREATER_THAN_EQUAL_TO = "GREATER_THAN_EQUAL_TO";

	public static final String LESS_THAN_EQUAL_TO = "LESS_THAN_EQUAL_TO";

	public static final String LESS_THAN = "LESS_THAN";

	public static final String ARRANGE_OPERATION_OPERATOR = "OPERATOR";

	public static final String ARRANGE_OPERATION_NORMAL = "NORMAL";

	public static final String ESCAPE_CHARACTER = "\\";

	private static Logger logger = null;

	private static final String SEPARATOR_COLON = ":";

	// BundlePropertyConditionalUtil extended to allow parent type names in
	// bundle
	// field names
	private IGRCObject parent;

	public FieldArithmeticPropertyConditionalUtil(Logger theLogger) {
		logger = theLogger;
	}

	// Responsibility of caller of BundlePropertyConditionalUti
	// to specify the parent
	public void setParent(IGRCObject theParent) {
		logger.debug("remembering the Parent object");
		parent = theParent;
	}

	/**
	 * validating the input condition for equal number of open and close
	 * brackets
	 * 
	 * @param inputCondition
	 */
	public boolean validateInputCondition(String inputCondition) {
		if (inputCondition == null || inputCondition.equalsIgnoreCase("")) {
			return true;
		}
		boolean anyError = true;
		int openCount = 0;
		int closeCount = 0;
		for (int count = 0; count < inputCondition.length(); count++) {
			if (OPEN_BRACKET.equalsIgnoreCase(Character.toString(inputCondition.charAt(count)))) {
				openCount++;
			} else if (CLOSE_BRACKET.equalsIgnoreCase(Character.toString(inputCondition.charAt(count)))) {
				closeCount++;
			}
		}
		if (openCount == closeCount) {
			anyError = false;
		}

		return anyError;
	}

	/**
	 * This method checks the input condition against the resource
	 * 
	 * @param value
	 * @param session
	 * @param resource
	 * @return boolean if the condition matches resource property values
	 */
	public boolean processConditionalLogic(String inputCondition, IGRCObject object) throws Exception {
		boolean conditionalFlag = false;
		boolean tempFlag = false;
		String operator = "";
		List<String> inputList = getConditionList(inputCondition);

		int inputListSize = inputList.size();
		for (int count = 0; count < inputListSize; count++) {
			if (count > 0)
				operator = inputList.get(count++);
			String condition = inputList.get(count);
			logger.debug("condition is : " + condition);
			if (isConditionComplex(condition)) {
				try {
					tempFlag = processConditionalLogic(condition, object);
				} catch (Exception ex) {
					throw new Exception("Error in processing complex conditions : " + ex);
				}
				logger.debug("temp flag from loop is : " + tempFlag);
			} else {
				tempFlag = processConditionAgainstResource(condition, object);
			}
			logger.debug("temp flag : " + count + " : " + tempFlag);
			if (count == 0) {
				conditionalFlag = tempFlag;
			} else {
				conditionalFlag = processConditionalOperation(conditionalFlag, tempFlag, operator);
			}
		}
		logger.debug("inputCondition is : " + inputCondition + " result is "+conditionalFlag);
		return conditionalFlag;
	}

	/**
	 * performing OR and AND operation based on the parameters return operation
	 * output
	 */
	private boolean processConditionalOperation(boolean conditionalFlag, boolean tempFlag, String operator) {
		boolean conditionValue = false;
		if (OR_OPERATOR_TEXT.equalsIgnoreCase(operator)) {
			conditionValue = conditionalFlag || tempFlag;
		} else {
			conditionValue = conditionalFlag && tempFlag;
		}
		return conditionValue;
	}

	/**
	 * Can be overridden to manipulate the resource and condition checking
	 * whether the condition has any brackets or operators if true - split it
	 */
	protected boolean processConditionAgainstResource(String input, IGRCObject object) {
		boolean conditionValue = false;
		String delimiter = null;
		logger.debug("input condition is : " + input);

			delimiter = getDelimiter(input);
			logger.debug("delimiter is : " + delimiter);
			String[] fieldArray = input.split(delimiter);
			String fieldValue = getPropertyValueAsString(fieldArray[0].trim(), object);
			Double fieldValueOne = Double.valueOf(fieldValue);
			String formatInputValue = fieldArray[1].trim();
			Double valueToCompare = null;
			String fieldValueTwo = null;
			Scanner numberScanner = new Scanner(formatInputValue);
			if (numberScanner.hasNextDouble()) {
				valueToCompare = numberScanner.nextDouble();
				numberScanner.close();
			} else {
				fieldValueTwo = getPropertyValueAsString(formatInputValue, object);
				if (fieldValueTwo!=null)
					valueToCompare = Double.valueOf(fieldValueTwo);
			}
			logger.debug("fieldValueOne : " + fieldValueOne);
			logger.debug("fieldValueTwo : " + fieldValueTwo);
			
			if (fieldValueOne!=null && fieldValueTwo !=null)
			{
				if (LESS_THAN_EQUAL_TO.equalsIgnoreCase(delimiter)) {
					conditionValue = fieldValueOne <= valueToCompare;
				} else if (LESS_THAN.equalsIgnoreCase(delimiter)) {
					conditionValue = fieldValueOne < valueToCompare;
				} else if (GREATER_THAN.equalsIgnoreCase(delimiter)) {
					conditionValue = fieldValueOne > valueToCompare;
				} else if (GREATER_THAN_EQUAL_TO.equalsIgnoreCase(delimiter)) {
					conditionValue = fieldValueOne >= valueToCompare;
				}
			}
			logger.debug("delimiter is " + delimiter + " conditionValue is : " + conditionValue);
		return conditionValue;
	}

	private String getDelimiter(String input) {
		if (input.contains(LESS_THAN_EQUAL_TO)) {
			return LESS_THAN_EQUAL_TO;
		}
		else
			if (input.contains(LESS_THAN)) {
				return LESS_THAN;
			} else if (input.contains(GREATER_THAN_EQUAL_TO)) {
				return GREATER_THAN_EQUAL_TO;
		} else if (input.contains(GREATER_THAN)) {
				return GREATER_THAN;
			} 

		return null;
	}

	/**
	 * This method splits the condition between operator(AND, OR)
	 * 
	 * @param attributeValue
	 * @return List of conditions
	 */
	private List<String> getConditionList(String attributeValue) throws Exception {
		List<String> conditionList = new ArrayList<String>();
		boolean endOfCondition = false;
		int initIndex = 0;
		int nextIndex = -1;
		while (!endOfCondition) {
			try {
				nextIndex = getNextImmediateOperatorIndex(attributeValue, initIndex);
			} catch (Exception ex) {
				throw ex;
			}
			if (nextIndex < 0) {
				endOfCondition = true;
				nextIndex = attributeValue.length();
			}
			if (initIndex > 0) {
				String operator = getNextOperator(attributeValue.substring(initIndex, nextIndex));
				if (operator != null) {
					conditionList.add(operator);
				} else {
					throw new Exception("Error in finding valid operator");
				}
			}
			String condition = getNextGeneralValidCondition(attributeValue.substring(initIndex, nextIndex));
			conditionList.add(condition);
			initIndex = nextIndex;
		}
		return conditionList;
	}

	/**
	 * Getting next index of OR, AND operator
	 * 
	 * @param value
	 * @param beginIndex
	 * @return index value
	 */
	private int getNextImmediateOperatorIndex(String value, int beginIndex) throws Exception {
		int nextIndex = getNextValidIndex(value, beginIndex);
		// int openBracketIndex = value.indexOf(OPEN_BRACKET, beginIndex);
		int openBracketIndex = getIndexOfMatchedPattern(value, OPEN_BRACKET, beginIndex, false);
		logger.debug("IndexOf returns (openBracketIndex) : (" + value.indexOf(OPEN_BRACKET) + ") & method returns ("
				+ openBracketIndex + ")");
		if (openBracketIndex > 0 && openBracketIndex < nextIndex) {
			try {
				int closeBracketIndex = getValidCloseBracketIndex(value, openBracketIndex + 1);
				logger.debug("closeBracketIndex : " + closeBracketIndex);
				nextIndex = getNextValidIndex(value, closeBracketIndex + 1);
			} catch (Exception ex) {
				logger.error("Error in getting the index " + ex);
				throw ex;
			}
		}
		return nextIndex;
	}

	/**
	 * find the operator after a condition
	 * 
	 * @param tempCondition
	 * @return the operator
	 */
	private String getNextOperator(String tempCondition) {
		int andIndex = tempCondition.indexOf(AND_OPERATOR_TEXT);
		int orIndex = tempCondition.indexOf(OR_OPERATOR_TEXT);
		if (andIndex == 0) {
			return AND_OPERATOR_TEXT;
		} else if (orIndex == 0) {
			return OR_OPERATOR_TEXT;
		}
		return null;
	}

	/**
	 * Removes all brackets and other operators
	 * 
	 * @param tempCondition
	 * @return valid condition
	 */
	private String getNextGeneralValidCondition(String tempCondition) {
		int andIndex = tempCondition.indexOf(AND_OPERATOR_TEXT);
		int orIndex = tempCondition.indexOf(OR_OPERATOR_TEXT);
		int len = tempCondition.length();
		if (andIndex == 0) {
			tempCondition = tempCondition.substring(3, len);
		} else if (orIndex == 0) {
			tempCondition = tempCondition.substring(2, len);
		}
		tempCondition = tempCondition.trim();
		if (isAnyOpenBracketOperator(tempCondition)) {
			// int openBracketIndex = tempCondition.indexOf(OPEN_BRACKET);
			int openBracketIndex = getIndexOfMatchedPattern(tempCondition, OPEN_BRACKET, -1, false);
			logger.debug("IndexOf returns (openBracketIndex) : (" + tempCondition.indexOf(OPEN_BRACKET)
					+ ") & method returns (" + openBracketIndex + ")");
			if (openBracketIndex == 0) {
				len = tempCondition.length();
				tempCondition = tempCondition.substring(1, len);
			}
		}
		if (isAnyCloseBracketOperator(tempCondition)) {
			len = tempCondition.length();
			// int closeBracketIndex = tempCondition.lastIndexOf(CLOSE_BRACKET,
			// len - 1);
			int closeBracketIndex = getIndexOfMatchedPattern(tempCondition, CLOSE_BRACKET, len - 1, true);
			logger.debug("IndexOf returns (openBracketIndex) : (" + tempCondition.lastIndexOf(CLOSE_BRACKET)
					+ ") & method returns (" + closeBracketIndex + ")");
			if (closeBracketIndex == len - 1) {
				tempCondition = tempCondition.substring(0, len - 1);
			}
		}
		tempCondition = tempCondition.trim();
		return tempCondition;
	}

	/**
	 * To check the valid close bracket for an open bracket found
	 * 
	 * @param value
	 * @param beginIndex
	 * @return close bracket
	 */
	private int getValidCloseBracketIndex(String value, int beginIndex) throws Exception {
		int index = -1;
		int count = 1;
		// int openBracketIndex = value.indexOf(OPEN_BRACKET, beginIndex);
		// int closeBracketIndex = value.indexOf(CLOSE_BRACKET, beginIndex);
		int openBracketIndex = getIndexOfMatchedPattern(value, OPEN_BRACKET, beginIndex, false);
		int closeBracketIndex = getIndexOfMatchedPattern(value, CLOSE_BRACKET, beginIndex, false);
		logger.debug("IndexOf returns (openBracketIndex, closeBracketIndex): ("
				+ value.indexOf(OPEN_BRACKET, beginIndex) + "," + value.indexOf(CLOSE_BRACKET, beginIndex)
				+ ") & method returns (" + openBracketIndex + "," + closeBracketIndex + ")");
		if (openBracketIndex < 0 || closeBracketIndex < openBracketIndex) {
			index = closeBracketIndex;
			count--;
		}
		if (closeBracketIndex < 0) {
			throw new Exception("Error in getting the close bracket index");
		}
		while (count > 0) {
			// openBracketIndex = value.indexOf(OPEN_BRACKET, beginIndex);
			// closeBracketIndex = value.indexOf(CLOSE_BRACKET, beginIndex);
			openBracketIndex = getIndexOfMatchedPattern(value, OPEN_BRACKET, beginIndex, false);
			closeBracketIndex = getIndexOfMatchedPattern(value, CLOSE_BRACKET, beginIndex, false);
			logger.debug("IndexOf returns (openBracketIndex, closeBracketIndex): ("
					+ value.indexOf(OPEN_BRACKET, beginIndex) + "," + value.indexOf(CLOSE_BRACKET, beginIndex)
					+ ") & method returns (" + openBracketIndex + "," + closeBracketIndex + ")");
			if (openBracketIndex > 0 && openBracketIndex < closeBracketIndex) {
				count++;
				beginIndex = openBracketIndex + 1;
			}
			if (closeBracketIndex > 0 && (closeBracketIndex < openBracketIndex || openBracketIndex < 0)) {
				count--;
				beginIndex = closeBracketIndex + 1;
			}
			if (closeBracketIndex < 0) {
				throw new Exception("Error in getting the close bracket index");
			}
			if (count == 0)
				index = closeBracketIndex;
		}
		return index;
	}

	/**
	 * find the valid operator index
	 * 
	 * @param value
	 * @param beginIndex
	 * @return valid operator index
	 */
	private int getNextValidIndex(String value, int beginIndex) {
		int nextIndex = -1;
		int andIndex = value.indexOf(AND_OPERATOR_TEXT, beginIndex);
		int orIndex = value.indexOf(OR_OPERATOR_TEXT, beginIndex);
		if (andIndex == beginIndex) {
			andIndex = value.indexOf(AND_OPERATOR_TEXT, beginIndex + 3);
		} else if (orIndex == beginIndex) {
			orIndex = value.indexOf(OR_OPERATOR_TEXT, beginIndex + 2);
		}
		if (andIndex > 0 || orIndex > 0) {
			if (andIndex > 0 && orIndex > 0) {
				if (andIndex < orIndex) {
					nextIndex = andIndex;
				} else {
					nextIndex = orIndex;
				}
			} else {
				if (andIndex > 0) {
					nextIndex = andIndex;
				} else {
					nextIndex = orIndex;
				}
			}
		}
		return nextIndex;
	}

	/**
	 * Return the value of fieldName from resource.
	 * 
	 * @param fieldName
	 *            - expects property in bundle:property format
	 * @param resource
	 * @return property value as String
	 * @throws OpenpagesServiceException
	 * @throws OpenpagesSessionException
	 * @throws BundleTypeNotFoundException
	 * @throws CacheException
	 * @throws PropertyTypeNotFoundException
	 */
	protected String getPropertyValueAsString(String fieldName, IGRCObject object)  {
		// Set these up as default values (ass-u-me that the fieldName is of
		// form B:F)
		// the resource,searchFieldName may be amended (if fieldName is of form
		// P:B:F)
		IGRCObject resource = object;
		String sourceFieldName = fieldName;

		String srcValue = null;
		if (StringUtil.isGood(fieldName)) {
			logger.debug("Checking the value of the field :" + fieldName);

			// However, the FieldName could have 2 forms:
			// 1) B:F => Field F in Bundle B in the object
			// 2) P:B:F => Field F in Bundle B in the primary parent (of type P)
			// of the
			// object
			// assume case 1 for the moment

			String[] subNames = fieldName.split(SEPARATOR_COLON);
			if (subNames.length == 3) {
				// So, it's the situation P:B:F NOT B:F
				// Therefore we need to
				String typeName = subNames[0];
				sourceFieldName = subNames[1] + SEPARATOR_COLON + subNames[2];

				logger.debug("Requested field is actually :" + typeName + " - " + sourceFieldName);
				// This implies the form P:X:Y
				// Do we have a parent defined?
				if (parent != null) {
					// Yes, parent present
					logger.debug("Parent is present");
					// Now, check that the parent type is that required
					if (parent.getType().getName().equalsIgnoreCase(typeName)) {
						logger.debug("Parent is of required type");
						resource = parent;
					}
				} else {
					// Oops! parent was not set up by the caller of this class
					// AND it was needed
					logger.debug("FieldName '" + fieldName
							+ "' requires a parent IGRCObject to be present! Remember to use the setParent method on this class!");
					resource = null;
				}
			}
			// At this point resource,searchFieldName could have been altered
			// (and resource
			// is possibly null)
			// At this point we have 3 possibilities
			// 1) original object + field name are unchanged - but still could
			// be an invalid
			// combination
			// 2) object, field name both changed to parent+field name - still
			// possibly
			// invalid combination
			// 3) object unchanged but field name is P:B:F (=> parent not
			// found/wrong type)

			// So, carefully get the contents of fieldName in the object
			DataType propertyType = null;
			if ((resource != null) && (resource.getField(sourceFieldName) != null)) {
				logger.debug("Getting the propertyType for field='" + sourceFieldName + "' on '"
						+ resource.getType().getName() + "'");

				// this means the P:B:F OR B:F is a valid fieldName
				propertyType = resource.getField(sourceFieldName).getDataType();
			}

			if (propertyType != null) {
				logger.debug("Checking the datatype of the property field");
				if (DataType.CURRENCY_TYPE.equals(propertyType)) {
					logger.debug("It's a CURRENCY");
					ICurrencyField currencyField = (ICurrencyField) resource.getField(sourceFieldName);
					if (currencyField != null && currencyField.getBaseAmount()!=null)
						srcValue = currencyField.getBaseAmount().toString();
				} else if (DataType.ENUM_TYPE.equals(propertyType)) {
					logger.debug("It's an ENUM");
					IEnumField enumField = (IEnumField) resource.getField(sourceFieldName);
					if (enumField != null) {
						// logger.debug("Got the ENUM field-READ?
						// '"+enumField.canRead()+"' WRITE?
						// '"+enumField.canWrite()+"'");
						logger.debug("Localised value="
								+ ((IEnumField) resource.getField(sourceFieldName)).getEnumValue().getLocalizedLabel());
						logger.debug("Field value="
								+ ((IEnumField) resource.getField(sourceFieldName)).getEnumValue().getName());
						if (enumField.getEnumValue() != null) {
							logger.debug("Got the ENUM field enumValue");
							srcValue = enumField.getEnumValue().getName();
						} else {
							logger.debug("Failed to get the ENUM field enumValue");
						}
					} else {
						logger.debug("Failed to get the ENUM field");
					}
				} else if (DataType.STRING_TYPE.equals(propertyType)) {
					logger.debug("It's a STRING");
					IStringField stringField = (IStringField) resource.getField(sourceFieldName);
					if (stringField != null)
						srcValue = stringField.getValue();
				} else {
					logger.debug("TO DO : This Datatype is not yet supported");
				}
			}
		}
		logger.debug("The value is " + srcValue);
		return srcValue;
	}

	/**
	 * Check if there are any open brackets in the condition
	 * 
	 * @param value
	 * @return boolean
	 */
	private boolean isAnyOpenBracketOperator(String value) {
		/*
		 * boolean conditionValue = false; if (value.contains(OPEN_BRACKET) &&
		 * !value.contains(ESCAPE_CHARACTER+OPEN_BRACKET)){ conditionValue =
		 * true; } return conditionValue;
		 */
		boolean conditionValue = getIndexOfMatchedPattern(value, OPEN_BRACKET, -1, false) > -1;
		logger.debug("Method returns: " + conditionValue + ", contains: " + value.contains(OPEN_BRACKET));
		return conditionValue;
	}

	/**
	 * Check if there are any close brackets in the condition
	 * 
	 * @param value
	 * @return boolean
	 */
	private boolean isAnyCloseBracketOperator(String value) {
		boolean conditionValue = getIndexOfMatchedPattern(value, CLOSE_BRACKET, -1, false) > -1;
		logger.debug("Method returns: " + conditionValue + ", contains: " + value.contains(OPEN_BRACKET));
		return conditionValue;
	}

	/**
	 * checking whether the condition has any brackets or operators return
	 * boolean
	 */
	private boolean isConditionComplex(String value) {
		return (isAnyOpenBracketOperator(value) || value.contains(AND_OPERATOR_TEXT)
				|| value.contains(OR_OPERATOR_TEXT));
	}

	private int getIndexOfMatchedPattern(String value, String pattern, int fromIndex, boolean lookReverse) {
		int index = -1;

		Pattern p = Pattern.compile("(?<!" + ESCAPE_CHARACTER + ESCAPE_CHARACTER + ")" + ESCAPE_CHARACTER + pattern);
		Matcher m = p.matcher(value);

		if (lookReverse) {
			if (fromIndex > -1) {
				if (m.find()) {
					int groups = m.groupCount();
					for (int i = groups; i >= 0; i--) {
						if (m.start(i) <= fromIndex) {
							index = m.start(groups);
							break;
						}
					}
				}
			} else {
				if (m.find()) {
					int groups = m.groupCount();
					index = m.start(groups);
				}
			}
		} else {
			if (fromIndex > -1) {
				if (m.find(fromIndex)) {
					index = m.start();
				}
			} else {
				if (m.find()) {
					index = m.start();
				}
			}

		}
		return index;
	}
}