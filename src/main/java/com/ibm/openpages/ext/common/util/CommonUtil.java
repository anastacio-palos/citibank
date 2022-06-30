package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.*;
import static com.ibm.openpages.ext.common.util.LoggerUtil.*;
import static java.util.UUID.randomUUID;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.openpages.apps.sosa.util.SoxPropertyConstants;
import com.openpages.aurora.common.AuroraEnv;
import com.openpages.tool.encrypt.CryptUtil;

public class CommonUtil {	
	
	public static String adminContext = null;
	
	private static final String CLASS_NAME = "CommonUtil";

    /**
     * <p>
     * Returns true if passed in String is null or empty else returns false.
     * </p>
     * 
     * @param str String Value.
     * @return A boolean value that states if the given String is null or Empty.
     */
    public static boolean isNullOrEmpty(String str) {

        return (str == null || str.trim().equals(EMPTY_STRING));
    }
    
    /**
     * <p>
     * Returns true if passed in String is not null or not empty else returns false.
     * </p>
     * 
     * @param str String Value.
     * @return A boolean value that states if the given String is not null or empty
     */
    public static boolean isNotNullOrEmpty(String str) {

        return !isNullOrEmpty(str);
    }

    /**
     * <p>
     * Returns true if passed in List is null or empty else returns false.
     * </p>
     * 
     * @param list List Object.
     * @return A boolean value that states if the given list is null or empty.
     */
    public static boolean isListNullOrEmpty(List<?> list) {

        return (list == null || list.size() == 0);
    }

    /**
     * <p>
     * Returns true if passed in List is not null or empty else returns false.
     * </p>
     * 
     * @param list List Object.
     * @return A boolean value that states if the given list is not null or empty.
     */
    public static boolean isListNotNullOrEmpty(List<?> list) {

        return !isListNullOrEmpty(list);
    }
    
    /**
     * <p>
     * Returns true if passed in Map is null or empty else returns false.
     * </p>
     * 
     * @param map Map Object.
     * @return A boolean value that states if the given Map is null or empty.
     */
    public static boolean isMapNullOrEmpty(Map<?, ?> map) {

        return (map == null || map.size() == 0);
    }

    /**
     * <p>
     * Returns true if passed in Map is not null or empty else returns false.
     * </p>
     * 
     * @param map Map Object.
     * @return A boolean value that states if the given Map is not null or empty.
     */
    public static boolean isMapNotNullOrEmpty(Map<?, ?> map) {

        return !isMapNullOrEmpty(map);
    }

    /**
     * <p>
     * Checks if the passed in String is null or empty if so returns an empty
     * String, else trims and returns the String.
     * </p>
     * 
     * @param str String Value.
     * @return An Empty String if the given String is null.
     */
    public static String rtrnEmptyIfNull(String str) {

        return (isNullOrEmpty(str) ? EMPTY_STRING : str.trim());
    }

    /**
     * <p>
     * Returns the Object as String by calling the objects toString method. if the object is 
     * null returns an Empty String.
     * </p>
     * 
     * @param obj Java Object.
     * @return The toString of a given object.  
     */
    public static String toString(Object obj) {

        return (isObjectNull(obj) ? EMPTY_STRING : obj.toString());
    }

    /**
     * <p>
     * Checks for null on both the strings and checks if they are equal.
     * </p>
     * 
     * @param str1 String Value
     * @param str2 String Value
     * 
     * @return A boolean value stating if the passed in Strings are equal.
     */
    public static boolean isEqual(String str1, String str2) {

        return (isNullOrEmpty(str1) && isNullOrEmpty(str2)) ? false
                : (rtrnEmptyIfNull(str1).equals(rtrnEmptyIfNull(str2)));
    }

    /**
     * <p>
     * Checks for null on both the strings and checks if they are not equal.
     * </p>
     * 
     * @param str1 String Value.
     * @param str2 String Value.
     * 
     * @return A boolean value stating if the passed in Strings are Not Equal.
     */
    public static boolean isNotEqual(String str1, String str2) {

        return !isEqual(str1, str2);
    }

    /**
     * <p>
     * Check if the passed in object is null.
     * </p>
     * 
     * @param obj Java Object.
     * @return A boolean value stating if the object is null.
     */
    public static boolean isObjectNull(Object obj) {

        return (obj == null);
    }

    /**
     * <p>
     * Check if the passed in object is null.
     * </p>
     * 
     * @param obj Java Object.
     * @return A boolean value stating if the object is not null.
     */
    public static boolean isObjectNotNull(Object obj) {

        return (obj != null);
    }

    /**
     * <p>
     * Returns the int value of the passed String.
     * </p>
     * 
     * @param val String Value.
     * @return A int value based on the given String. 0 if the String is null or empty.
     */
    public static int getIntValue(String val) {

        return (isNullOrEmpty(val) ? 0 : Integer.parseInt(val));
    }

    /**
     * <p>
     * Returns the String value of the passed int.
     * </p>
     * 
     * @param val int value.
     * @return A String based on the passed in int value.
     */
    public static String intToString(int val) {

        return new Integer(val).toString();
    }

    /**
     * <p>
     * Returns the String value of the passed int.
     * </p>
     * 
     * @param val double value.
     * @return A String based on the passed in double value.
     */
    public static String doubleToString(double val) {

        return new Double(val).toString();
    }

    /**
     * <p>
     * Returns the long value of the passed String.
     * </p>
     * 
     * @param val String value.
     * @return A long value based on the given String. 0 if the String is null or empty.
     */
    public static long getLongValue(String val) {

        return (isNullOrEmpty(val) ? 0 : Long.parseLong(val));
    }
    
    /**
     * <p>
     * This method generates a Unique ID each time its invoked.
     * </p>
     * 
     * @return A String Value of a Random UUID.
     */
    public static String generateUniqueId() {
    	
    	return randomUUID().toString(); 
    }

    /**
     * <p>
     * Adds the given value to the List only if its not present.
     * </p>
     * 
     * @param listToBeMerged List that contains values to be merged.
     * @param listWithMergedValues List that contains existing values.
     * 
     * @return A List<Integer> which contains all the values in the listWithMergedValues, and any Value present in listToBeMerged
     * and not present in listWithMergedValues.
     */
    public static List<Integer> addValueToListIfNotPresent(List<Integer> listToBeMerged, List<Integer> listWithMergedValues) {

    	// Method Level Variables.
        Set<Integer> nonDuplicateSet = null;

        // Method Implementation. 
        if (isObjectNotNull(listWithMergedValues))
        {

            nonDuplicateSet = new HashSet<Integer>(listWithMergedValues);

            if (isObjectNotNull(listToBeMerged))
            {

                nonDuplicateSet.addAll(listToBeMerged);
            }
        }
        return (isObjectNull(nonDuplicateSet)) ? new ArrayList<Integer>()
                : new ArrayList<Integer>(nonDuplicateSet);
    }

    /**
     * <p>
     * This method returns the stack trace of a given Exception.
     * </p>
     * 
     * @param throwable Throwable object.
     * @return A String representation of the Throwable's stack trace.
     */
    public static String getStackTrace(Throwable throwable) {
    	
    	// Method Level Variables.
        Writer writer = new StringWriter();

        // Method Implementation. 
        try {

            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
        }
        catch (Exception ex) {

        	error(CLASS_NAME, "getStackTrace()", "Exception: ", getStackTrace(ex));
        }

        return writer.toString();
    }
    
    /**
     * <p>
     * Parses string using the input delimiter. This method can be used for
     * separating delimited values defined in trigger configuration.
     * </p>
     * 
     * @param value String Value
     * @param delimiter Delimiter Value with which the given String needs to be parsed.
     * 
     * @return A List<String> created with the String value delimited by the delimiter.
     */
    public static List<String> parseDelimitedValues(String value, String delimiter) {
    	
    	// Method Level Variables.
        String[] valueArray = null;
        List<String> valuesList = null;
        
        // Method Implementation. 
        valuesList = new ArrayList<String>();
        
        if(!isNullOrEmpty(value)) {
        	
        	valueArray = value.split(delimiter);
        	
        	if (valueArray.length > 0)
            {	
                for(int i=0; i<valueArray.length; i++) {
                	
                	valuesList.add((isNullOrEmpty(valueArray[i])) ? EMPTY_STRING : valueArray[i].trim());
                }           
            }
        }
        
        return valuesList;
    }
    
    /**
     * <p>
     * This method returns a String of values separated by the delimiter.
     * </p>
     * 
     * @param list List of String values
     * @param delimiter Delimiter Value with which the given String needs to be parsed.
     * 
     * @return A String created with the List provided delimited by the delimiter.
     */
    public static String unParseWithDelimitedValue(List<String> list, String delimiter) {
    	
    	StringBuilder unParsedValue = null;    	
    	
    	unParsedValue = new StringBuilder();
    	if(isListNotNullOrEmpty(list)) {
    		
			for (int i=0; i<list.size(); i++) {
    			
    			unParsedValue.append(list.get(i));
    			if (list.size()-1 != i)
    				unParsedValue.append(delimiter);
    		}
    	}
    	
    	return unParsedValue.toString();
    }
    
    /**
     * <p>
     * Parses string using the input delimiter. This method can be used for
     * separating delimited values defined in trigger configuration.
     * </p>
     * 
     * @param value String Value
     * @param firstDelimiter Delimiter Value with which the given String needs to be parsed.
     * @param secondDelimiter Delimiter Value with which the given String needs to be parsed.
     * @return A List<String> created with the String value delimited by the delimiter.
     */
    public static Map<String, String> parseDelimitedValuesAsMap(String value, String firstDelimiter, String secondDelimiter) {
    	
    	// Method Level Variables.
        String[] valueArray = null;
        String[] secondValueArray = null;
        Map<String, String> valuesMap = null;
        
        // Method Implementation. 
        valuesMap = new HashMap<String, String>();
        
        if(!isNullOrEmpty(value)) {
        	
        	valueArray = value.split(firstDelimiter);
        	
        	if (valueArray.length > 0)
            {	
                for(int i=0; i<valueArray.length; i++) {
                	
                	if((isNotNullOrEmpty(valueArray[i]))) {
                		
                		secondValueArray = valueArray[i].trim().split(secondDelimiter);
                		valuesMap.put((isNullOrEmpty(secondValueArray[INT_ZERO])) ? EMPTY_STRING : secondValueArray[INT_ZERO].trim(), 
                				(isNullOrEmpty(secondValueArray[INT_ONE])) ? EMPTY_STRING : secondValueArray[INT_ONE].trim());
                	}
                }           
            }
        }
        
        return valuesMap;
    }
    
    /**
     * <p>
     * Parses string using the input delimiter. This method can be used for
     * separating delimited values defined in trigger configuration.
     * </p>
     * 
     * @param values List of String values
     * @param keyValue String value
     * @param firstDelimiter Delimiter Value with which the given String needs to be parsed.
     * @param secondDelimiter Delimiter Value with which the given String needs to be parsed.
     * @return A Map<String, Map<String, String>> created with the String value delimited by the delimiters.
     */
    public static Map<String, Map<String, String>> parseDelimitedValuesAsMapWithSpecificKey(List<String> values, String keyValue, String firstDelimiter, String secondDelimiter) {
    	
    	// Method Level Variables.
        String[] valueArray = null;
        String[] secondValueArray = null;
        Map<String, String> valuesMap = null;
        Map<String, Map<String, String>> finalMap = null;
        
        // Method Implementation.       
        finalMap = new HashMap<String, Map<String, String>>();
        
        if(isListNotNullOrEmpty(values)) {
        	
        	
        	for(String firsSetOfValue : values) {
        		
        		String mainKey = EMPTY_STRING;
        		valuesMap = new HashMap<String, String>();
        		valueArray = firsSetOfValue.split(firstDelimiter);
        		
        		if (valueArray.length > 0) {
            		
                    for(int i=0; i<valueArray.length; i++) {
                    	
                    	String key = EMPTY_STRING;
                    	String value = EMPTY_STRING;
                    	
                    	if((isNotNullOrEmpty(valueArray[i]))) {
                    		
                    		secondValueArray = valueArray[i].trim().split(secondDelimiter);
                    		if(secondValueArray != null && secondValueArray.length ==2) {
                    			
                    			key = (isNullOrEmpty(secondValueArray[INT_ZERO])) ? EMPTY_STRING : secondValueArray[INT_ZERO].trim();
                        		value = (isNullOrEmpty(secondValueArray[INT_ONE])) ? EMPTY_STRING : secondValueArray[INT_ONE].trim();
                        		if(keyValue.equals(key)) {
                        			
                        			mainKey = value;                    			
                        		}
                        		else {
                        			
                        			valuesMap.put(key, value);
                        		}
                    		}                    		                    		
                    	}
                    }           
                }
        		
        		if(isNotNullOrEmpty(mainKey) && isObjectNotNull(valuesMap))
        			finalMap.put(mainKey, valuesMap);
        	}
        }
        
        return finalMap;
    }
    
    
    /**
     * <p>
     * This method adds a Integer/Enum Value to the List 
     * </p>
     * 
     * @param list List Object.
     * @param value Integer value.
     * 
     * @return A List<String> from which the given Integer value is added.
     */
	public static List<Integer> addIntegertoList (List<Integer> list, Integer value) {
    	
    	if(isObjectNotNull(list)) {
    		
    		list.add(value);
    	}
    	
    	return list;
    }
    
    /**
     * <p>
     * This method removes a Integer/Enum Value to the ArrayList
     * 
     * </p>
     * 
     * @param list List Object.
     * @param value Integer value.
     * 
     * @return A List<String> from which the given Integer value is removed.
     */
	public static List<Integer> removeIntegerFromList (List<Integer> list, Integer value) {
    	
    	if(isObjectNotNull(list)) {
    		
    		list.remove(value);
    	}
    	
    	return list;
    }
	
	/**
	 * <p>
	 * Decrypts an encrypted String value using the decryption provided by Openpages Application.
	 * </p>
	 * 
	 * @param encryptedVal A String value thats encrypted.
	 * 
	 * @return A decrypted String value.
	 * @throws Exception
	 */
	public static String decryptString(String encryptedVal) throws Exception {
		
		return CryptUtil.decrypt(encryptedVal);
	}

	/**
	 * <p>
	 * This method iterates through the Messages and returns it as a String with line breaks.
	 * <p>
	 * 
	 * @param messages A List<String> that contains all the messages.	 * 
	 * @return A String with all the Messages present in the List with line breaks introduced between each message.
	 */
	public static String toStringWithLineBreak(List<String> messages) {
		
		// Method Level Variables.
	    StringBuilder sb = new StringBuilder();
	    
	    // Method Implementation.
	    for(String msg : messages) {
	    	
	        sb.append("\n");
	        sb.append(msg);
	    }
	    
	    return sb.toString();
	}
	
	/**
	 * <p>
	 * This method returns the URL to the OpenPages application.
	 * </p>
	 * @return String 
	 * 			Returns the URL to OpenPages application.
	 * @throws Exception
	 */
    public static String getOpenPagesURL() throws Exception {
    	
		StringBuffer opURL = new StringBuffer();
		opURL.append("<a target='_blank' href='");
		opURL.append(AuroraEnv.getProperty(SoxPropertyConstants.APPLICATION_URL_PATH));
		opURL.append("'>");
		opURL.append(AuroraEnv.getProperty(SoxPropertyConstants.APPLICATION_URL_PATH));
		opURL.append("</a>");	
		
		return opURL.toString();		
	}
    
    /**
  	 * <p>
  	 * This method returns the Table html element with content.
  	 * <P>
       * @param tableContent
       * 	Table Content
       * @return String
       */
	 public static String getTableElementWithContent(String tableContent) {
		 
		 return "<table class='summarytable content'>" + tableContent + "</table>";
	 }
     
     /**
   	 * <p>
   	 * This method returns the Table Header Row html element with content.
   	 * <P>
        * @param tableHeaderRowContent
        * 	Table Header Row
        * @return String
        */
      public static String getTableHeaderRowElementWithContent(String tableHeaderRowContent) {
     	 
     	 return "<tr>" + tableHeaderRowContent + "</tr>";
      }
      
      /**
     	 * <p>
     	 * This method returns the Table Header html element with content.
     	 * <P>
          * @param tableHeaderColumnContent
          * 	Table Header Column
          * @return String
          */
    public static String getTableHeaderElementWithContent(String tableHeaderColumnContent) {
   	 
   	 	return "<th class='subheading'>" + tableHeaderColumnContent + "</th>";
    }
        
    /**
   	 * <p>
   	 * This method returns the Table Data Row html element with content.
   	 * <P>
        * @param tableDataRowContent
        * 	Table Data Row
        * @return String
        */
	  public static String getTableDataRowElementWithContent(String tableDataRowContent) {
	 	 
	 	 return "<tr class='objectList'>" + tableDataRowContent + "</tr>";
	  }
          
      /**
     	 * <p>
     	 * This method returns the Table Data Cell html element with content.
     	 * <P>
          * @param tableDataCellContent
          * 	Table Data Cell
          * @return String
          */
        public static String getTableDataCellElementWithContent(String tableDataCellContent) {
       	 
       	 return "<td class='item'>" + tableDataCellContent + "</td>";
        }
}