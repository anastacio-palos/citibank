package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CommonValidations")
public class CommonValidations {

    @XmlElement(name = "RequestID")
    private String requestID;

    @XmlElement(name = "IssueID")
    private String issueID;

    @XmlElement(name = "IssueStatus")
    private String issueStatus;

    @XmlElement(name = "IAChiefAudiotor")
    private String iaChiefAuditorOwner;

    @XmlElement(name = "IAResponsibleChiefAuditor")
    private String iaRespChiefAuditor;

    @XmlElement(name = "AuditOwner")
    private String auditOwner;

    @XmlElement(name = "AuditController")
    private String auditController;

    @XmlElement(name = "IALeadAuditor")
    private String leadAuditor;

    @XmlElement(name = "LeadProductTeam")
    private String leadProductTeam;

    @XmlElement(name = "IssueRiskMitigatedDate")
    private String issueRiskMitigatedDate;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getIaChiefAuditorOwner() {
        return iaChiefAuditorOwner;
    }

    public void setIaChiefAuditorOwner(String iaChiefAuditorOwner) {
        this.iaChiefAuditorOwner = iaChiefAuditorOwner;
    }

    public String getIaRespChiefAuditor() {
        return iaRespChiefAuditor;
    }

    public void setIaRespChiefAuditor(String iaRespChiefAuditor) {
        this.iaRespChiefAuditor = iaRespChiefAuditor;
    }

    public String getAuditOwner() {
        return auditOwner;
    }

    public void setAuditOwner(String auditOwner) {
        this.auditOwner = auditOwner;
    }

    public String getAuditController() {
        return auditController;
    }

    public void setAuditController(String auditController) {
        this.auditController = auditController;
    }

    public String getLeadAuditor() {
        return leadAuditor;
    }

    public void setLeadAuditor(String leadAuditor) {
        this.leadAuditor = leadAuditor;
    }

    public String getLeadProductTeam() {
        return leadProductTeam;
    }

    public void setLeadProductTeam(String leadProductTeam) {
        this.leadProductTeam = leadProductTeam;
    }

    public String getIssueRiskMitigatedDate() {
        return issueRiskMitigatedDate;
    }

    public void setIssueRiskMitigatedDate(String issueRiskMitigatedDate) {
        this.issueRiskMitigatedDate = issueRiskMitigatedDate;
    }

    @Override
    public String toString() {
        return "CommonValidations{" +
                "requestID='" + requestID + '\'' +
                ", issueID='" + issueID + '\'' +
                ", issueStatus='" + issueStatus + '\'' +
                ", iaChiefAuditorOwner='" + iaChiefAuditorOwner + '\'' +
                ", iaRespChiefAuditor='" + iaRespChiefAuditor + '\'' +
                ", auditOwner='" + auditOwner + '\'' +
                ", auditController='" + auditController + '\'' +
                ", leadAuditor='" + leadAuditor + '\'' +
                ", leadProductTeam='" + leadProductTeam + '\'' +
                ", issueRiskMitigatedDate='" + issueRiskMitigatedDate + '\'' +
                '}';
    }
}
