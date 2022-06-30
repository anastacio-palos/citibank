package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.COMMA_SEPERATED_DELIMITER;
import static com.ibm.openpages.ext.common.util.CommonUtil.isNullOrEmpty;
import static com.ibm.openpages.ext.common.util.LoggerUtil.debugEXTLog;
import static com.ibm.openpages.ext.common.util.LoggerUtil.errorEXTLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.security.IUser;

/**
 * @feature Email Notification Utilities
 * @summary This utility class provides various methods to send Email notifications.
 * 
 * 
 * @author Nikhil Komakula
 * @email nikhil.komakula@in.ibm.com
 */
public class NotificationUtil
{

    private static final String CLASS_NAME = "NotificationUtil";

    public static final String EMPTY_STRING = "";

    private static final String EMAIL_SERVER_REGISTRY = "/OpenPages/Applications/Common/Email/Mail Server";
    
    /**
     * <p>
     * This method returns a list of email addresses based on the single/multi user/group selector field.
     * </p>
     * 
     * @param userOrGroupField
     *            Single/Multi user or group selector field
     * @param grcObject
     *            Instance of IGRCObject
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @return List<String> List of email addresses
     * @throws Exception
     */
    public static List<String> getEmailAddressesOfUserOrGroupSelectorFieldAsList(String userOrGroupField, IGRCObject grcObject, ServicesUtil servicesUtil, Logger logger) throws Exception
	{

	    	  final String METHOD_NAME = CLASS_NAME + ":" + "getEmailAddressesOfUserOrGroupSelectorFieldAsList";
	
		  String usersOrGroups = GRCObjectUtil.getFieldValue(grcObject, userOrGroupField);
		  debugEXTLog(logger, METHOD_NAME, "usersOrGroups", usersOrGroups);
		  
		  usersOrGroups = usersOrGroups.replaceAll("\\$;", ","); // for multi user/group selector content
		  usersOrGroups = usersOrGroups.replaceAll(";", ","); // for multi user/group selector content
		  usersOrGroups = usersOrGroups.replaceAll("$", ","); // for multi user/group selector content
		  
	    Set<String> actorsSet = new HashSet<String>(CommonUtil.parseDelimitedValues(usersOrGroups, COMMA_SEPERATED_DELIMITER));
	    
	    while(actorsSet.contains(EMPTY_STRING))
	    actorsSet.remove(EMPTY_STRING);

		  debugEXTLog(logger, METHOD_NAME, "actorsSet", actorsSet);
		  
		  usersOrGroups = EMPTY_STRING;
		   
	    Iterator<String> iter =  actorsSet.iterator();
	    while(iter.hasNext())
	    {
	      usersOrGroups = usersOrGroups + iter.next();
	      
	      if(iter.hasNext()){
	        usersOrGroups = usersOrGroups + COMMA_SEPERATED_DELIMITER;
	      }
	    }
	    
		  
		  return getEmailAddressesOfUsersOrGroupsAsList(usersOrGroups, servicesUtil, logger);	  
	}
    
    public static List<String> getUsersInFieldAsList(String userOrGroupField, IGRCObject grcObject, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

            final String METHOD_NAME = CLASS_NAME + ":" + "getEmailAddressesOfUserOrGroupSelectorFieldAsList";
    
        String usersOrGroups = GRCObjectUtil.getFieldValue(grcObject, userOrGroupField);
        debugEXTLog(logger, METHOD_NAME, "usersOrGroups", usersOrGroups);
        
        usersOrGroups = usersOrGroups.replaceAll("\\$;", ","); // for multi user/group selector content
        usersOrGroups = usersOrGroups.replaceAll(";", ","); // for multi user/group selector content
        usersOrGroups = usersOrGroups.replaceAll("$", ","); // for multi user/group selector content
        
        Set<String> actorsSet = new HashSet<String>(CommonUtil.parseDelimitedValues(usersOrGroups, COMMA_SEPERATED_DELIMITER));
        
        while(actorsSet.contains(EMPTY_STRING))
        actorsSet.remove(EMPTY_STRING);

        debugEXTLog(logger, METHOD_NAME, "actorsSet", actorsSet);
        
        usersOrGroups = EMPTY_STRING;
         
        Iterator<String> iter =  actorsSet.iterator();
        while(iter.hasNext())
        {
          usersOrGroups = usersOrGroups + iter.next();
          
          if(iter.hasNext()){
            usersOrGroups = usersOrGroups + COMMA_SEPERATED_DELIMITER;
          }
        }
        
        
        return getUsersOrGroupsAsList(usersOrGroups, servicesUtil, logger);   
    }
    

    /**
     * <p>
     * This method returns a list of email addresses based on the users/usergroups passed as a comma-separated String.
     * </p>
     * 
     * @param usersOrGroups
     *            comma-separated list of users or user groups
     * @param servicesUtil
     *            Instance of ServicesUtil Class
     * @param logger
     *            Instance of Logger
     * @return List<String> List of email addresses
     * @throws Exception
     */
    public static List<String> getEmailAddressesOfUsersOrGroupsAsList(String usersOrGroups, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "getEmailAddressesOfUsersOrGroupsAsList";

        if (isNullOrEmpty(usersOrGroups))
            return Collections.emptyList();

        List<IUser> usersList = SecurityUtil.getUsersAsListFromString(usersOrGroups, servicesUtil, logger);
        debugEXTLog(logger, METHOD_NAME, "usersList", usersList);

        List<String> emailAddressesList = new ArrayList<String>();

        for (IUser user : usersList)
        {

            emailAddressesList.add(user.getEmailAddress());
            debugEXTLog(logger, METHOD_NAME, "user email", user.getEmailAddress());
        }

        debugEXTLog(logger, METHOD_NAME, "emailAddressesList", emailAddressesList);

        return emailAddressesList;
    }

    public static List<String> getUsersOrGroupsAsList(String usersOrGroups, ServicesUtil servicesUtil, Logger logger) throws Exception
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "getEmailAddressesOfUsersOrGroupsAsList";

        if (isNullOrEmpty(usersOrGroups))
            return Collections.emptyList();

        List<IUser> usersList = SecurityUtil.getUsersAsListFromString(usersOrGroups, servicesUtil, logger);
        debugEXTLog(logger, METHOD_NAME, "usersList", usersList);

        List<String> userList = new ArrayList<String>();

        for (IUser user : usersList)
        {
          userList.add(user.getName());
          debugEXTLog(logger, METHOD_NAME, "user name", user.getName());
        }

        debugEXTLog(logger, METHOD_NAME, "userList", userList);

        return userList;
    }
    /**
     * <p>
     * This method returns the mail server host name from Registry.
     * <P>
     * 
     * @param configProperties
     *            IConfigProperties
     * @return String
     */
    public static String getDefaultEmailServer(IConfigProperties configProperties)
    {

        return ApplicationUtil.getRegistrySetting(EMAIL_SERVER_REGISTRY, configProperties);
    }

    /**
     * <p>
     * This method returns the email body paragraph with style and content.
     * 
     * @param emailBody
     *            String
     *            <P>
     * @return String
     */
    public static String getEmailParagraphElementWithContent(String emailBody)
    {

        return "<p style='font-family: Helvetica,Arial,Tahoma,Verdana,sans-serif; font-size: 12px;'>" + emailBody + "</p>";
    }

    /**
     * <p>
     * This method returns the email table with style and content.
     * 
     * @param tableContent
     *            String
     *            <P>
     * @return String
     */
    public static String getEmailTableElementWithContent(String tableContent)
    {

        return "<table style='border:1px solid #dddddd; border-collapse:collapse; color: #222222; display: table; font-family: Helvetica,Arial,Tahoma,Verdana,sans-serif; font-size: 12px;'>"
                + tableContent + "</table>";
    }

    /**
     * <p>
     * This method returns the email table header element with style and content.
     * 
     * @param tableHeaderContent
     *            String
     *            <P>
     * @return String
     */
    public static String getEmailTableHeaderStyle(String tableHeaderContent)
    {

        return "<th style='background-color: #F4F5F6; border: 1px solid #dddddd; font-weight: bold; padding: 4px 10px; text-align: left; display: table-cell;'>" + tableHeaderContent + "</th>";
    }

    /**
     * <p>
     * This method returns the email table cell element with style and content.
     * 
     * @param tableCellContent
     *            String
     *            <P>
     * @return String
     */
    public static String getEmailTableCellStyle(String tableCellContent)
    {

        return "<td style='padding: 6px 6px 6px 10px; word-wrap: break-word; margin: 0; border-color: gray; display: table-cell; border-bottom: 1px solid #dddddd;'>" + tableCellContent + "</td>";
    }

    /**
     * <p>
     * Method to send an email to email addresses. Can accept multiple TO and CC values.
     * </p>
     * 
     * @param emailNotificationVO
     *            The information required to send a valid Email.
     * @param logger
     *            Instance of Logger.
     * @return true if email was sent successfully. Otherwise, false.
     */
    public static boolean sendNotification(final EmailNotificationVO emailNotificationVO, Logger logger)
    {

        final String METHOD_NAME = CLASS_NAME + ":" + "sendNotification";

        // Method Level Variables.
        Properties properties = null;
        Session session = null;
        MimeMessage msg = null;
        InternetAddress addressFrom = null;
        InternetAddress[] addressesTo = null;
        InternetAddress[] addressesCc = null;

        // Method Implementation.
        try
        {

            /*
             * Initializations and getting the Required Values - These information are needed to get implement the business logic. Initialize the Property and the Session object to get the Message
             * Object.
             */
            debugEXTLog(logger, METHOD_NAME, "Email Sent with the following Values: ", emailNotificationVO.toString());

            if (CommonUtil.isListNotNullOrEmpty(emailNotificationVO.getToAddresses()))
            {

                properties = new Properties();
                properties.put("mail.smtp.host", emailNotificationVO.getMailServer());

                if (CommonUtil.isNotNullOrEmpty(emailNotificationVO.getMailPort()))
                    properties.put("mail.smtp.port", emailNotificationVO.getMailPort());

                if (CommonUtil.isNotNullOrEmpty(emailNotificationVO.getMailUsername()) && CommonUtil.isNotNullOrEmpty(emailNotificationVO.getMailPassword()))
                {

                    properties.put("mail.smtp.auth", "true");

                    // Setup authentication, get session
                    session = Session.getInstance(properties, new javax.mail.Authenticator()
                    {
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication(emailNotificationVO.getMailUsername(), emailNotificationVO.getMailPassword());
                        }
                    });
                }
                else
                {

                    session = Session.getInstance(properties, null);
                }

                msg = new MimeMessage(session);

                /* Get if the From Name is not empty frame the From Address with email Address and the Name else just use the Email Address. */
                if (!isNullOrEmpty(emailNotificationVO.getFromName()))
                    addressFrom = new InternetAddress(emailNotificationVO.getFromAddress(), emailNotificationVO.getFromName());
                else
                    addressFrom = new InternetAddress(emailNotificationVO.getFromAddress());

                /* Set the values in the Mime Message object and send the message. */
                msg.setFrom(addressFrom);

                addressesTo = getInternetAddressArray(emailNotificationVO.getToAddresses());
                msg.setRecipients(Message.RecipientType.TO, addressesTo);

                if (CommonUtil.isListNotNullOrEmpty(emailNotificationVO.getCcAddresses()))
                {

                    addressesCc = getInternetAddressArray(emailNotificationVO.getCcAddresses());
                    msg.setRecipients(Message.RecipientType.CC, addressesCc);
                }

                msg.setSubject(emailNotificationVO.getEmailSubject(), "utf-8");
                msg.setContent(emailNotificationVO.getEmailContent(), "text/html;charset=utf-8");
                Transport.send(msg);

                /* Finally Clean up. */
                msg = null;
                session = null;

                return true;
            }
            else
            {

                /* Only for logging purposes. */
                debugEXTLog(logger, METHOD_NAME, EMPTY_STRING, "WARNING!!!!!!!!!! : No email was sent - To Addresses are Null or Empty");
                return false;
            }

        }
        catch (Exception ex)
        {

            errorEXTLog(logger, METHOD_NAME, EMPTY_STRING, CommonUtil.getStackTrace(ex));
            return false;
        }
    }

    /**
     * Creates an array of InternetAddress objects containing all the valid emails from the list of user names passed in.
     * 
     * @param emailAddresses
     *            - List of email address as strings
     * @return array of InternetAddress objects
     */
    private static InternetAddress[] getInternetAddressArray(List<String> emailAddresses) throws Exception
    {

        List<InternetAddress> emailAddressesList = new ArrayList<InternetAddress>();

        for (String emailAddress : emailAddresses)
        {

            emailAddressesList.add(new InternetAddress(emailAddress));
        }

        InternetAddress[] internetAddresses = (InternetAddress[]) emailAddressesList.toArray(new InternetAddress[emailAddressesList.size()]);
        return internetAddresses;
    }
    
    public static List<String> getEmailAddressesOfFields(String[] fields, IGRCObject grcObject, ServicesUtil servicesUtil, Logger logger) throws Exception
  {

    final String METHOD_NAME = CLASS_NAME + ":" + "getEmailAddressesOfFields";

    List<String> emailAddressesList = new ArrayList<String>();

    for (String field : fields)
    {
      emailAddressesList.addAll(getEmailAddressesOfUserOrGroupSelectorFieldAsList(field, grcObject, servicesUtil, logger));
    }

    debugEXTLog(logger, METHOD_NAME, "usersOrGroups", emailAddressesList);

    return emailAddressesList;
  }
    

}