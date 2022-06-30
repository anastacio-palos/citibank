package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "RETIssueValidations")
public class RETIssueValidationsInfo {


    @XmlElement(name = "ReasonFailValidation")
    private String reasonFailValidation;

    @XmlElement(name = "IAEntityID")
    private String auditEntityID;

    @XmlElement(name = "IAEntityName")
    private String auditEntityName;

    @XmlElement(name = "DataBackBusiness")
    private String dateBack2Business;

    @XmlElement(name = "ReopenIssueCount")
    private String numberOfTimesIssueReOpened;

    @XmlElement(name = "IssueReopenDate")
    private String issueReopenDate;

    public String getReasonFailValidation() {
        return reasonFailValidation;
    }

    public void setReasonFailValidation(String reasonFailValidation) {
        this.reasonFailValidation = reasonFailValidation;
    }

    public String getAuditEntityID() {
        return auditEntityID;
    }

    public void setAuditEntityID(String auditEntityID) {
        this.auditEntityID = auditEntityID;
    }

    public String getAuditEntityName() {
        return auditEntityName;
    }

    public void setAuditEntityName(String auditEntityName) {
        this.auditEntityName = auditEntityName;
    }

    public String getDateBack2Business() {
        return dateBack2Business;
    }

    public void setDateBack2Business(String dateBack2Business) {
        this.dateBack2Business = dateBack2Business;
    }

    public String getNumberOfTimesIssueReOpened() {
        return numberOfTimesIssueReOpened;
    }

    public void setNumberOfTimesIssueReOpened(String numberOfTimesIssueReOpened) {
        this.numberOfTimesIssueReOpened = numberOfTimesIssueReOpened;
    }

    public String getIssueReopenDate() {
        return issueReopenDate;
    }

    public void setIssueReopenDate(String issueReopenDate) {
        this.issueReopenDate = issueReopenDate;
    }

    @Override
    public String toString() {
        return "RETIssueValidationsInfo{" +
                "reasonFailValidation='" + reasonFailValidation + '\'' +
                ", auditEntityID='" + auditEntityID + '\'' +
                ", auditEntityName='" + auditEntityName + '\'' +
                ", dateBack2Business='" + dateBack2Business + '\'' +
                ", numberOfTimesIssueReOpened='" + numberOfTimesIssueReOpened + '\'' +
                ", issueReopenDate='" + issueReopenDate + '\'' +
                '}';
    }
}
