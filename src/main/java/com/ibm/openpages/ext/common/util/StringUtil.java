package com.ibm.openpages.ext.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil
{

    private static final String SEPARATOR_COMMA = "\\,";
	private static final String EMPTY_STRING = "";

	/**
     * Convert comma separated value to string List
     * 
     * @param csvText
     * @return
     */
    public static List<String> convertStringToList(String csvText)
    {
        List<String> list = new ArrayList<String>();
        if (isGood(csvText))
        {
            list = Arrays.asList(csvText.split(SEPARATOR_COMMA));
        }
        return list;
    }

    /**
     * Is this a valid String
     * 
     * @param inputString
     * @return
     */
    public static boolean isGood(String inputString)
    {

        return !isNotGood(inputString);
    }

    /**
     * Is this a valid List
     * 
     * @param inputString
     * @return
     */
    public static boolean isGood(List<?> inputList)
    {

        return !isNotGood(inputList);
    }

    /**
     * <p>
     * Returns true if passed in List is null or empty else returns false.
     * </p>
     * 
     * @param list
     *            List Object.
     * @return A boolean value that states if the given list is null or empty.
     */
    public static boolean isNotGood(List<?> list)
    {

        return (list == null || list.size() == 0);

    }

    /**
     * <p>
     * Returns true if passed in String is null or empty else returns false.
     * </p>
     * 
     * @param inputString
     *            String Value.
     * @return A boolean value that states if the given String is null or Empty.
     */
    public static boolean isNotGood(String inputString)
    {

        return (inputString == null || inputString.trim().equals(EMPTY_STRING));
    }

}
