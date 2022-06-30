package com.ibm.openpages.ext.batch.omu.bean;

import com.ibm.openpages.ext.model.ReportRecord;

public class EmployeeKPIDetailBean implements ReportRecord {

    private int srNo;
    private String objectName;
    private String objectType;
    private String source;
    private String status;
    private String errorDetails;

    public EmployeeKPIDetailBean() {
        this.srNo = 0;
        this.objectName = "";
        this.objectType = "";
        this.source = "";
        this.status = "";
        this.errorDetails = "";

    }

    public EmployeeKPIDetailBean(int srNo, String objectName, String objectType, String source, String status, String errorDetails) {
        this.srNo = srNo;
        this.objectName = objectName;
        this.objectType = objectType;
        this.source = source;
        this.status = status;
        this.errorDetails = errorDetails;
    }

    public int getSrNo() {
        return srNo;
    }

    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    @Override
    public String toString() {
        return "EmployeeKPIDetailBean{" +
                "srNo=" + srNo +
                ", objectType='" + objectType + '\'' +
                ", objectName='" + objectName + '\'' +
                ", source='" + source + '\'' +
                ", status='" + status + '\'' +
                ", errorDetails='" + errorDetails + '\'' +
                '}';
    }

    @Override
    public String toReportRecord() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", this.srNo, this.objectName, this.objectType, this.source, this.status, this.errorDetails, System.lineSeparator());
    }
}
