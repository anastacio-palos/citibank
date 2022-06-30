package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_NEW_LINE;

import java.util.ArrayList;
import java.util.List;

public class EmailNotificationVO {
	
	private int emailSent;
	private int emailFailed;	

	private boolean isCCRequired;
	private boolean isFormattedEmail;
	private boolean isEmailNotification;
	private boolean isEmailToMultipleCCAddress;
	private boolean isEmailToMultipleEmailAddresses;
	
	private String emailBody;
	private String mailServer;
	private String mailPort;
	private String mailUsername;
	private String mailPassword;
	private String fromName;
	private String toAddress;
	private String fromAddress;
	private String emailSubject;
	private String emailContent;
	private String emailHeader;
	private String emailFooter;
	private String secondaryToAddress;
	private String emailLayoutStyle;
	private String ccAddress;
	private String invalidFromAddress;
	private String toEmailSeperator;
	private String ccEmailSeperator;
	
	private List<String> toAddresses;
	private List<String> ccAddresses;
	private List<String> invalidToAddresses;
	private List<String> invalidCCAddresses;
	private List<String> emailBodytableHeaderInfo;
	private List<String> emailBodytableBodyInfo;
	private List<String> invalidBodyRows;
	
	/**
	 * @return the mailServer
	 */
	public String getMailServer() {
		return mailServer;
	}
	/**
	 * @param mailServer to set
	 */
	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}
	/**
	 * @return the mailUsername
	 */
	public String getMailUsername() {
		return mailUsername;
	}
	/**
	 * @param mailUsername to set
	 */
	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}	
	/**
	 * @return the mailPassword
	 */
	public String getMailPassword() {
		return mailPassword;
	}
	/**
	 * @param mailPassword to set
	 */
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}	
	/**
	 * @return the mailPort
	 */
	public String getMailPort() {
		return mailPort;
	}
	/**
	 * @param mailPort to set
	 */
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}
	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}
	/**
	 * @param fromName the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	/**
	 * @return the fromAddress
	 */
	public String getFromAddress() {
		return fromAddress;
	}
	/**
	 * @param fromAddress the fromAddress to set
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject() {
		return emailSubject;
	}
	/**
	 * @param emailSubject the emailSubject to set
	 */
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	/**
	 * @return the emailContent
	 */
	public String getEmailContent() {
		return emailContent;
	}
	/**
	 * @param emailContent the emailContent to set
	 */
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}	
	/**
	 * @return the isEmailNotification
	 */
	public boolean isEmailNotification() {
		return isEmailNotification;
	}
	/**
	 * @param isEmailNotification the isEmailNotification to set
	 */
	public void setEmailNotification(boolean isEmailNotification) {
		this.isEmailNotification = isEmailNotification;
	}
	/**
	 * @param toAddress to set
	 */
	public void setToAddress(String toAddress) {
		List<String> toAddressList = new ArrayList<String>();
		toAddressList.add(toAddress);
		this.setToAddresses(toAddressList);
	}
	/**
	 * @return toAddresses
	 */
	public List<String> getToAddresses() {
		return toAddresses;
	}
	/**
	 * @param toAddresses to set
	 */
	public void setToAddresses(List<String> toAddresses) {
		this.toAddresses = toAddresses;
	}	
	/**
	 * @return the emailSent
	 */
	public int getEmailSent() {
		return emailSent;
	}
	/**
	 * @param emailSent the emailSent to set
	 */
	public void setEmailSent(int emailSent) {
		this.emailSent = emailSent;
	}
	/**
	 * @return the emailFailed
	 */
	public int getEmailFailed() {
		return emailFailed;
	}
	/**
	 * @param emailFailed the emailFailed to set
	 */
	public void setEmailFailed(int emailFailed) {
		this.emailFailed = emailFailed;
	}	
	/**
	 * @return the emailLayoutStyle
	 */
	public String getEmailLayoutStyle() {
		return emailLayoutStyle;
	}
	/**
	 * @param emailLayoutStyle the emailLayoutStyle to set
	 */
	public void setEmailLayoutStyle(String emailLayoutStyle) {
		this.emailLayoutStyle = emailLayoutStyle;
	}	
	/**
	 * @return the emailBody
	 */
	public String getEmailBody() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(this.emailLayoutStyle);
		sb.append(this.emailHeader);
		sb.append(HTML_NEW_LINE);
		sb.append(HTML_NEW_LINE);
		sb.append(HTML_NEW_LINE);
		sb.append(this.emailContent);
		sb.append(HTML_NEW_LINE);
		sb.append(HTML_NEW_LINE);
		sb.append(HTML_NEW_LINE);
		sb.append(this.emailFooter);	
		
		return sb.toString();
	}
	
	/**
	 * @return the emailHeader
	 */
	public String getEmailHeader() {
		return emailHeader;
	}
	/**
	 * @param emailHeader the emailHeader to set
	 */
	public void setEmailHeader(String emailHeader) {
		this.emailHeader = emailHeader;
	}
	/**
	 * @return the emailFooter
	 */
	public String getEmailFooter() {
		return emailFooter;
	}
	/**
	 * @param emailFooter the emailFooter to set
	 */
	public void setEmailFooter(String emailFooter) {
		this.emailFooter = emailFooter;
	}	
	
	/**
	 * @return the isFormattedEmail
	 */
	public boolean isFormattedEmail() {
		return isFormattedEmail;
	}
	/**
	 * @param isFormattedEmail the isFormattedEmail to set
	 */
	public void setFormattedEmail(boolean isFormattedEmail) {
		this.isFormattedEmail = isFormattedEmail;
	}
	/**
	 * @return the ccAddresses
	 */
	public List<String> getCcAddresses() {
		return ccAddresses;
	}
	/**
	 * @param ccAddresses the ccAddresses to set
	 */
	public void setCcAddresses(List<String> ccAddresses) {
		this.ccAddresses = ccAddresses;
	}	
	
	/**
	 * @return the invalidFromAddress
	 */
	public String getInvalidFromAddress() {
		return invalidFromAddress;
	}
	/**
	 * @param invalidFromAddress the invalidFromAddress to set
	 */
	public void setInvalidFromAddress(String invalidFromAddress) {
		this.invalidFromAddress = invalidFromAddress;
	}
	/**
	 * @return the invalidToAddresses
	 */
	public List<String> getInvalidToAddresses() {
		return invalidToAddresses;
	}
	/**
	 * @param invalidToAddresses the invalidToAddresses to set
	 */
	public void setInvalidToAddresses(List<String> invalidToAddresses) {
		this.invalidToAddresses = invalidToAddresses;
	}
	/**
	 * @return the invalidCCAddresses
	 */
	public List<String> getInvalidCCAddresses() {
		return invalidCCAddresses;
	}
	/**
	 * @param invalidCCAddresses the invalidCCAddresses to set
	 */
	public void setInvalidCCAddresses(List<String> invalidCCAddresses) {
		this.invalidCCAddresses = invalidCCAddresses;
	}		
	/**
	 * @return the isCCRequired
	 */
	public boolean isCCRequired() {
		return isCCRequired;
	}
	/**
	 * @param isCCRequired the isCCRequired to set
	 */
	public void setCCRequired(boolean isCCRequired) {
		this.isCCRequired = isCCRequired;
	}
	
	/**
	 * @return the emailBodytableHeaderInfo
	 */
	public List<String> getEmailBodytableHeaderInfo() {
		return emailBodytableHeaderInfo;
	}
	/**
	 * @param emailBodytableHeaderInfo the emailBodytableHeaderInfo to set
	 */
	public void setEmailBodytableHeaderInfo(List<String> emailBodytableHeaderInfo) {
		this.emailBodytableHeaderInfo = emailBodytableHeaderInfo;
	}
	/**
	 * @return the emailBodytableBodyInfo
	 */
	public List<String> getEmailBodytableBodyInfo() {
		return emailBodytableBodyInfo;
	}
	/**
	 * @param emailBodytableBodyInfo the emailBodytableBodyInfo to set
	 */
	public void setEmailBodytableBodyInfo(List<String> emailBodytableBodyInfo) {
		this.emailBodytableBodyInfo = emailBodytableBodyInfo;
	}
	/**
	 * @return the invalidBodyRows
	 */
	public List<String> getInvalidBodyRows() {
		return invalidBodyRows;
	}
	/**
	 * @param invalidBodyRows the invalidBodyRows to set
	 */
	public void setInvalidBodyRows(List<String> invalidBodyRows) {
		this.invalidBodyRows = invalidBodyRows;
	}	
	
	/**
	 * @return the toEmailSeperator
	 */
	public String getToEmailSeperator() {
		return toEmailSeperator;
	}
	/**
	 * @param toEmailSeperator the toEmailSeperator to set
	 */
	public void setToEmailSeperator(String toEmailSeperator) {
		this.toEmailSeperator = toEmailSeperator;
	}
	/**
	 * @return the ccEmailSeperator
	 */
	public String getCcEmailSeperator() {
		return ccEmailSeperator;
	}
	/**
	 * @param ccEmailSeperator the ccEmailSeperator to set
	 */
	public void setCcEmailSeperator(String ccEmailSeperator) {
		this.ccEmailSeperator = ccEmailSeperator;
	}
	
	
	@Override 
	public String toString(){
		
		StringBuilder sb = new StringBuilder();

		sb.append("\n Is CC Required: " + isCCRequired);
		sb.append("\n Is Email Notification: " + isEmailNotification);
		sb.append("\n Is Formatted Email Notification: " + isFormattedEmail);
		sb.append("\n Is Email To Multiple Addresses: " + isEmailToMultipleEmailAddresses);
		sb.append("\n Is Email To Multiple CC Addresses: " + isEmailToMultipleCCAddress);
		
		sb.append("\n Total Email Sent: " + emailSent);
		sb.append("\n Total Email Failed: " + emailFailed);
		
		sb.append("\n Mail Server: " + mailServer);
		sb.append("\n From Name: " + fromName);
		sb.append("\n From Address: " + fromAddress);
		sb.append("\n To Address: " + toAddress);
		sb.append("\n To Addresses: " + toAddresses);
		sb.append("\n To Email Seperator: " + toEmailSeperator);
		sb.append("\n CC Addresses: " + ccAddress);
		sb.append("\n CC Addresses: " + ccAddresses);
		sb.append("\n CC Email Seperator: " + ccEmailSeperator);
		sb.append("\n Secondary To Address: " + secondaryToAddress);
		sb.append("\n Email Subject: " + emailSubject);
		sb.append("\n Email Header: " + emailHeader);
		sb.append("\n Email Body: " + emailBody);
		sb.append("\n Email Body Table Header: " + emailBodytableHeaderInfo);
		sb.append("\n Email Body Table Body: " + emailBodytableBodyInfo);
		sb.append("\n Email Footer: " + emailFooter);
		sb.append("\n Email Content: " + emailContent);
		sb.append("\n Email Layout Style: " + emailLayoutStyle);
		
		sb.append("\n Invalid From Address: " + invalidFromAddress);
		sb.append("\n Invalid To Addresses: " + invalidToAddresses);
		sb.append("\n Invalid CC Addresses: " + invalidCCAddresses);
		sb.append("\n Invalid Body Rows: " + invalidBodyRows);
		sb.append("\n");
		
		return sb.toString();
	}
}