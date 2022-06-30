package com.ibm.openpages.ext.notification;

import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.local.LocalServiceFactory;

import java.text.MessageFormat;
import java.util.Date;

public class EmailContent {

    private String table = "<table style='border:1px solid #dddddd; border-collapse:collapse; color: #222222; display: table; font-family: Helvetica,Arial,Tahoma,Verdana,sans-serif;" +
                           " font-size: 12px;'>%s%s</table>";
    private String tableRow ="<tr>%s%s</tr>";
    private String tableHeader = "<th style='background-color: #F4F5F6; border: 1px solid #dddddd; font-weight: bold; padding: 4px 10px; text-align: left; display: table-cell;'>%s</th>";
    private String tableData = "<td style='padding: 6px 6px 6px 10px; word-wrap: break-word; margin: 0; border-color: gray; display: table-cell; border-bottom: 1px solid #dddddd;'>%s</td>";

    private String messageHeaderKey =  "com.openpages.ext.citi.dsmt.interf.email.header";
    private String messageFooterKey = "com.openpages.ext.citi.dsmt.interf.email.footer";
    private String subjectKey = "com.openpages.ext.citi.dsmt.interf.email.subject";

    private String conflictMessageFooter = "com.openpages.ext.citi.dsmt.interf.conflict.email.footer";

    private String conflictMessageBody = "com.openpages.ext.citi.dsmt.interf.conflict.email.body";

    private String conflictMessageSub = "com.openpages.ext.citi.dsmt.interf.conflict.email.subject";

    private int invalidSyncSuccess;

    private int invalidSyncError;

    private int updateSyncSuccess;

    private int updateSyncError;

    private LocalServiceFactory serviceFactory;

    private IConfigurationService configurationService;

    private Date startDate;

    private Date endDate;


    public EmailContent(LocalServiceFactory serviceFactory){

        this.serviceFactory = serviceFactory;
        this.configurationService = serviceFactory.createConfigurationService();

    }


    public EmailContent(LocalServiceFactory serviceFactory, int invalidSyncSuccess, int invalidSyncError, int updateSyncSuccess, int updateSyncError){
        this.invalidSyncSuccess = invalidSyncSuccess;
        this.invalidSyncError = invalidSyncError;
        this.updateSyncSuccess = updateSyncSuccess;
        this.updateSyncError = updateSyncError;
        this.serviceFactory = serviceFactory;
        this.configurationService = serviceFactory.createConfigurationService();
    }


    public String getInvalidDSMTTemplate() {

        String success = String.format(tableData, invalidSyncSuccess);
        String failure = String.format(tableData, invalidSyncError);

        String headerRowSuccess = String.format(tableHeader, "Invalid Sync Success" );
        String headerRowError = String.format(tableHeader, "Invalid Sync Failure" );

        String headerRow = String.format(tableRow, headerRowSuccess, headerRowError );
        String dataRow = String.format(tableRow, success, failure );

        return String.format(table,headerRow ,dataRow);
    }



    public String getUpdateDSMTTemplate() {
        String success = String.format(tableData, updateSyncSuccess);
        String failure = String.format(tableData, updateSyncError);

        String headerRowSuccess = String.format(tableHeader, "Update Sync Success" );
        String headerRowError = String.format(tableHeader, "Update Sync Error" );

        String headerRow = String.format(tableRow, headerRowSuccess, headerRowError );
        String dataRow = String.format(tableRow, success, failure );

        return String.format(table,headerRow ,dataRow);
    }

    public String buildEmail(){
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append("</br>");
        sb.append(MessageFormat.format(configurationService.getLocalizedApplicationText(messageHeaderKey), this.startDate));
        sb.append("</br>");
        sb.append("</br>");
        sb.append(getInvalidDSMTTemplate());
        sb.append("</br>");
        sb.append("</br>");
        sb.append(getUpdateDSMTTemplate());
        sb.append("</br>");
        sb.append(MessageFormat.format(configurationService.getLocalizedApplicationText(messageFooterKey), this.endDate));
        return sb.toString();

    }

    public int getInvalidSyncSuccess() {
        return invalidSyncSuccess;
    }

    public void setInvalidSyncSuccess(final int invalidSyncSuccess) {
        this.invalidSyncSuccess = invalidSyncSuccess;
    }

    public int getInvalidSyncError() {
        return invalidSyncError;
    }

    public void setInvalidSyncError(final int invalidSyncError) {
        this.invalidSyncError = invalidSyncError;
    }

    public int getUpdateSyncSuccess() {
        return updateSyncSuccess;
    }

    public void setUpdateSyncSuccess(final int updateSyncSuccess) {
        this.updateSyncSuccess = updateSyncSuccess;
    }

    public int getUpdateSyncError() {
        return updateSyncError;
    }

    public void setUpdateSyncError(final int updateSyncError) {
        this.updateSyncError = updateSyncError;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public String getSubject(){
        return configurationService.getLocalizedApplicationText(subjectKey);
    }

    public String getConflictMessageBody() {
        return configurationService.getLocalizedApplicationText(conflictMessageBody);
    }

    public String getConflictMessageFooter() {
        return configurationService.getLocalizedApplicationText(conflictMessageFooter);
    }

    public String getConflictMessageSub() {
        return configurationService.getLocalizedApplicationText(conflictMessageSub);
    }
}
