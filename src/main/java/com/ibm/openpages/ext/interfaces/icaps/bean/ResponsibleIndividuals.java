package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CommonValidations")
public class ResponsibleIndividuals {

	@XmlElement(name = "IAEntityID")
	private String auditEntityID;

	@XmlElement(name = "IAEntityName")
	private String auditEntityName;

	@XmlElement(name = "IAValidationDate")
	private String dateBack2Business;

	@XmlElement(name = "ReOpenIssueCount")
	private String numberOfTimesIssueReOpened;

	@XmlElement(name = "IAValidationDate")
	private Date issueReopenDate;

	@XmlElement(name = "IAChiefAuditorOwner")
	private String iaChiefAuditorOwner;

	@XmlElement(name = "IAResponsibleChiefAuditor")
	private String iaRespChiefAuditor;

	@XmlElement(name = "AuditOwner")
	private String auditOwner;

	@XmlElement(name = "Controller")
	private String auditController;

	@XmlElement(name = "LeadAuditor")
	private String leadAuditor;

	@XmlElement(name = "LeadProductTeam")
	private String leadProductTeam;

	@XmlElement(name = "IAValidationDate")
	private String issueRiskMitigatedDate;

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

	public Date getIssueReopenDate() {
		return issueReopenDate;
	}

	public void setIssueReopenDate(Date issueReopenDate) {
		this.issueReopenDate = issueReopenDate;
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
		return "ResponsibleIndividuals{" + "auditEntityID='" + auditEntityID + '\'' + ", auditEntityName='"
				+ auditEntityName + '\'' + ", dateBack2Business='" + dateBack2Business + '\''
				+ ", numberOfTimesIssueReOpened='" + numberOfTimesIssueReOpened + '\'' + ", issueReopenDate='"
				+ issueReopenDate + '\'' + ", iaChiefAuditorOwner='" + iaChiefAuditorOwner + '\''
				+ ", iaRespChiefAuditor='" + iaRespChiefAuditor + '\'' + ", auditOwner='" + auditOwner + '\''
				+ ", auditController='" + auditController + '\'' + ", leadAuditor='" + leadAuditor + '\''
				+ ", leadProductTeam='" + leadProductTeam + '\'' + ", issueRiskMitigatedDate='" + issueRiskMitigatedDate
				+ '\'' + '}';
	}
}