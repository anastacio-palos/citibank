package com.ibm.openpages.ext.batch.omu.notification;

import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.ext.batch.omu.bean.EmployeeKPIBean;
import com.ibm.openpages.ext.batch.omu.bean.EmployeeKPIDetailBean;
import com.ibm.openpages.ext.batch.omu.exception.EmployeesException;
import com.ibm.openpages.ext.util.CSVWriter;
import com.ibm.openpages.ext.util.ZipUtil;

import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

public class EmailContent {

    private final String table = "<table style='border:1px solid #dddddd; border-collapse:collapse; color: #222222; display: table; font-family: Helvetica,Arial,Tahoma,Verdana,sans-serif;" +
            " font-size: 12px;'>%s%s</table>";
    private final String tableRow = "<tr>%s%s%s%s%s</tr>";
    private final String tableHeader = "<th style='background-color: #F4F5F6; border: 1px solid #dddddd; font-weight: bold; padding: 4px 10px; text-align: left; display: table-cell;'>%s</th>";
    private final String tableData = "<td style='padding: 6px 6px 6px 10px; word-wrap: break-word; margin: 0; border-color: gray; display: table-cell; border-bottom: 1px solid #dddddd;'>%s</td>";

    private final String messageHeaderKey = "com.openpages.ext.citi.employee.job.email.header";
    private final String messageFooterKey = "com.openpages.ext.citi.employee.job.email.footer";
    private final String subjectKey = "com.openpages.ext.citi.employee.job.email.subject";
    private final String OMU_REPORT_HEADER = "SrNo, Object Name, Object Type, Source, Status, Error Details";
    private final LocalServiceFactory serviceFactory;
    private final IConfigurationService configurationService;
    private final String reportFilePath;
    private int invalidSyncSuccess;
    private int invalidSyncError;
    private int updateSyncSuccess;
    private int updateSyncError;
    private List<EmployeeKPIBean> processSummary;
    private List<EmployeeKPIDetailBean> processDetail;
    private Date startDate;
    private Date endDate;
    private File reportFile;
    private File zipFile;


    public EmailContent(LocalServiceFactory serviceFactory) {

        this.serviceFactory = serviceFactory;
        this.configurationService = serviceFactory.createConfigurationService();
        reportFilePath = "/tmp/EmployeeReport_" + System.currentTimeMillis() + ".csv";

    }

    public String getOmuProcessSummaryTemplate() {


        String[] headerRows = {
                String.format(tableHeader, "SrNo"),
                String.format(tableHeader, "Object Type"),
                String.format(tableHeader, "Source"),
                String.format(tableHeader, "Total Processed"),
                String.format(tableHeader, "Failed")
        };
        String headerRow = String.format(tableRow, headerRows);
        int i = 1;
        StringBuilder dataRows = new StringBuilder();
        if (processSummary != null) {
            for (EmployeeKPIBean data : processSummary) {
                String[] dataRow = {
                        String.format(tableData, i++),
                        String.format(tableData, data.getObjectType()),
                        String.format(tableData, data.getSource()),
                        String.format(tableData, data.getTotalProcessed()),
                        String.format(tableData, data.getFailed())
                };
                dataRows.append(String.format(tableRow, dataRow));
            }
        }
        return String.format(table, headerRow, dataRows);
    }

    public String buildEmail() {
        StringBuilder sb = new StringBuilder();
        sb.append("</br>");
        sb.append(MessageFormat.format(configurationService.getLocalizedApplicationText(messageHeaderKey), this.startDate, this.endDate));
        sb.append("</br>");
        sb.append("</br>");
        sb.append(getOmuProcessSummaryTemplate());
        sb.append("</br>");
        sb.append(configurationService.getLocalizedApplicationText(messageFooterKey));
        return sb.toString();

    }

    public String buildAttachment() throws EmployeesException {
        try {
            CSVWriter reportWriter = new CSVWriter(reportFilePath);
            reportWriter.writeHeader(OMU_REPORT_HEADER);
            int i = 1;
            for (EmployeeKPIDetailBean reportRecord : processDetail) {
                reportRecord.setSrNo(i++);
                reportWriter.writeRecord(reportRecord);
            }
            reportFile = new File(reportFilePath);
            zipFile = ZipUtil.zipFile(reportFile);
        } catch (Exception e) {
            throw new EmployeesException("The attachment generation has produced an error.", e);
        }
        return zipFile.getPath();
    }

    public boolean deleteAttachmentFiles() {

        boolean reportDeleteResult = reportFile.delete();
        boolean zipDeleteResult = zipFile.delete();

        return reportDeleteResult && zipDeleteResult;
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

    public String getSubject() {
        return configurationService.getLocalizedApplicationText(subjectKey);
    }


    public List<EmployeeKPIBean> getProcessSummary() {
        return processSummary;
    }


    public void setProcessSummary(List<EmployeeKPIBean> processSummary) {
        this.processSummary = processSummary;
    }


    public List<EmployeeKPIDetailBean> getProcessDetail() {
        return processDetail;
    }


    public void setProcessDetail(List<EmployeeKPIDetailBean> processDetail) {
        this.processDetail = processDetail;
    }

}
