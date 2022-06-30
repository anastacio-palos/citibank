package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.common.util.CommonConstants.INT_ZERO;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.resource.IDateField;

public class DateUtil {
	
	private static final String CLASS_NAME = "DateUtil";
	
	/**
	 * <p>
	 * Gets the current Date based on the default date format.
	 * </p>
	 * 
	 * @param format String
	 * @return A String representation of the Current Date.
	 */
	public static String getCurrentDate(String format) {
		
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	/**
	 * <p>
	 * Gets the Date object based on the String date.
	 * </p>
	 * 
	 * @param dateString Date in String
	 * @param format String
	 * @return A Date representation of the Current Date in String.
	 */
	public static Date getDateFromString(String dateString, String format) throws Exception {
		
		DateFormat formatter = new SimpleDateFormat(format);		
		return formatter.parse(dateString);
	}
	
	/**
	 * <p>
	 * Gets the Date object based on the date.
	 * </p>
	 * 
	 * @param Date
	 * @param format String
	 * @return A Date representation of the Current Date in String.
	 */
	public static String getDateFromString(Date date, String format) throws Exception {
		
		DateFormat formatter = new SimpleDateFormat(format);		
		return formatter.format(date);
	}
	
	/**
	 * <p>
	 * Gets the String object based on the Calendar instance.
	 * </p>
	 * 
	 * @param calendar Calendar instance
	 * @param format String
	 * @return A String representation of the Current Date in Calendar.
	 */
	public static String getStringFromCalendar(Calendar calendar, String format) throws Exception {
		
		DateFormat formatter = new SimpleDateFormat(format);		
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * <p>
	 * Get the time stamp based on the given date value.
	 * </p>
	 * 
	 * 
	 * @param date A string representation of a date value.
	 * 
	 * @return A Timestamp object based on the given date value.
	 * @throws Exception
	 */
	public static Timestamp getTimeStamp(String date) throws Exception {
		
		int offset = INT_ZERO;
		Calendar cal = null;
		String[] dateValues = null;

		Timestamp timestamp = null;
		SimpleDateFormat sdf = null;
		
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		if (isTodayDate(date)) {
			
			offset = getOffset(date);
			cal.add(Calendar.DATE, offset);
			timestamp = new Timestamp(cal.getTimeInMillis());
		}
		else if (date.matches("[0-9]([0-9])?/[0-9]([0-9])?/[0-9][0-9][0-9][0-9]")) {
			
			dateValues = date.split("/");
			cal.set(Calendar.MONTH, Integer.parseInt(dateValues[0]));
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateValues[1]));
			cal.set(Calendar.YEAR, Integer.parseInt(dateValues[2]));
			timestamp = new Timestamp(cal.getTimeInMillis());
		}
		else {
			
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try {
				timestamp = new Timestamp(sdf.parse(date).getTime());
			} 
			catch (Exception e) {
				
				sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
				try {
					
					timestamp = new Timestamp(sdf.parse(date).getTime());
				} 
				catch (ParseException e1) {
					
					throw new Exception("wrong date format", e);
				}
			}
		}
		
		return timestamp;
	}
	
	/**
	 * </p>
	 * Returns true if the passed in date value is the current Date.
	 * </p>
	 * 
	 * @param date A String representation of a date value.
	 * @return A boolean value that states if the given date value is the current date.
	 */
	public static boolean isTodayDate(String date) {
		
		return (date != null && (date.startsWith("TODAY") || date.startsWith("today")));
	}
	
	/**
	 * <p>
	 * Gets the number
	 * </p>
	 * 
	 * @param value A String value.	 * 
	 * @return Only the int value present in the String removing any other Strings. 
	 * @throws Exception
	 */
	public static int getOffset(String value) throws Exception {
		
		int offset = 0;
		String num = EMPTY_STRING;
		
		num = value.replaceAll("[a-zA-Z]", "");
		
		if (CommonUtil.isNotNullOrEmpty(num)) {
			
			try {
				
				offset = Integer.parseInt(num);
			} catch (NumberFormatException e) {
				
				throw new Exception("value next to today not a digit");
			}
		}
		
		return offset;
	}
    
    /**
	 * </p>
	 * Checks if the passed in date object is the current Date.
	 * </p>
	 * 
	 * @param date Date object 
	 * @return A boolean value that states if the given date value is the current date.
	 */
    public static boolean isToday(Date date) throws Exception {
        
    		return isSameDay(date, Calendar.getInstance().getTime());
    }
    
    /**
	 * </p>
	 * Checks if the passed in Calendar object is the current Date.
	 * </p>
	 * 
	 * @param calendar 
	 * 			Calendar object 
	 * @return boolean 
	 * 			true if the given Calendar value is the current date.
	 */
    public static boolean isToday(Calendar calendar) throws Exception {
    	
        return isSameDay(calendar, Calendar.getInstance());
    }
	
    /**
     * <p>
     * Checks if two given dates represent the same day ignoring time.
     * </p>
     * 
     * @param date1
     * 			First date, not null
     * @param date2
     * 			Second date, not null
     * @return boolean
     * 			true if both represent the same day
     * @throws Exception
     */
    public static boolean isSameDay(Date date1, Date date2) throws Exception {
        
	    	if (date1 == null || date2 == null) {
	            throw new IllegalArgumentException("Dates cannot be null");
	    }
    	
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        
        return isSameDay(calendar1, calendar2);
    }
    
    public static boolean isSameDay(IDateField dateField1, IDateField dateField2)
    {
      Date date1 = dateField1.getValue();
      Date date2 = dateField2.getValue();
      
      if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Dates cannot be null");
      }
    
      Calendar calendar1 = Calendar.getInstance();
      calendar1.setTime(date1);
      Calendar calendar2 = Calendar.getInstance();
      calendar2.setTime(date2);
      
      return isSameDay(calendar1, calendar2);
  
    }
    
    /**
     * <p>
     * Checks if two given calendars represent the same day ignoring time.
     * </p>
     * 
     * @param calendar1  
     *			First calendar, not null
     * @param calendar2  
     *			Second calendar, not null
     * @return boolean
     *			true if they both represent the same day
     * @throws Exception
     */
    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        
	    	if (calendar1 == null || calendar2 == null) {
	            throw new IllegalArgumentException("Calendars cannot be null");
	    }
	        
	    	return (calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA) &&
	        			calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
	        				calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));
    }
    
    /**
	 * <p>
	 * This method returns current year.
	 * </p>
	 * @return String Current Year
	 */	
    public static String getCurrentYear() {
        
    		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }
    
    /**
	 * <p>
	 * This method returns current quarter.
	 * </p>
	 * @return String Current Quarter
	 */	
    public static String getCurrentQuarter() {
    	
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int quarter = (month / 3) + 1;

        return "Q" + String.valueOf(quarter);
    }
    
    /**
	 * <p>
	 * This method returns nth date from a given date.
	 * </p>
	 * 
	 * @param date  
     *			Any given date
     * @param n  
     *			For future days: N > 0. For past days: N < 0
	 * @param logger 
	 * 			Instance of Logger
	 * @return Date
	 */	
    public static Date getNthDate(Date date, int n, Logger logger) {
    	
    		final String METHOD_NAME = CLASS_NAME + ":" + "getNthDate";
      
    		final Calendar calendar = Calendar.getInstance();
      
	    calendar.setTime(date);
	    calendar.add(Calendar.DAY_OF_YEAR, n);
	    LoggerUtil.debugEXTLog(logger, METHOD_NAME, "calendar.getTime()", calendar.getTime());
	      
	    return calendar.getTime();
    }
    
    /**
	 * <p>
	 * This method returns true if given date is after today.
	 * </p>
	 * 
	 * @param date  
     *			Any given date
	 * @param logger 
	 * 			Instance of Logger
	 * @return boolean
	 */	
    public static boolean isDateAfterToday(Date date, Logger logger) {
    	
		final String METHOD_NAME = CLASS_NAME + ":" + "isDateAfterToday";
         
        Date todaysDate = Calendar.getInstance().getTime();  
        LoggerUtil.debugEXTLog(logger, METHOD_NAME, "date.after(todaysDate)", date.after(todaysDate));
      
        return date.after(todaysDate);
    }
    
    /**
	 * <p>
	 * This method returns true if given date is before today.
	 * </p>
	 * 
	 * @param date  
     *			Any given date
	 * @param logger 
	 * 			Instance of Logger
	 * @return boolean
	 */	
    public static boolean isDateBeforeToday(Date date, Logger logger) {
    	
		final String METHOD_NAME = CLASS_NAME + ":" + "isDateBeforeToday";
         
		Date todaysDate = Calendar.getInstance().getTime();
		LoggerUtil.debugEXTLog(logger, METHOD_NAME, "date.before(todaysDate)", date.before(todaysDate));
      
		return date.before(todaysDate);
    }
    
   public static boolean isDate1AfterDate2(Date date1, Date date2)
    {
       return date1.after(date2);
    }
    
    public static boolean isDate1AfterDate2(IDateField dateField1, IDateField dateField2)
    {
            
      return isDate1AfterDate2(dateField1.getValue(), dateField2.getValue());
      
    }
    
    public static boolean isFutureDate(IDateField dateField, Logger logger)
    {
            
      return isDateAfterToday(dateField.getValue(), logger);
      
    }
    
}