package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CAPForm")
public class CapForm {

	@XmlElement(name = "iCAPSCapID")
	private String capName;

	@XmlElement(name = "ValidationInIA")
	private String capStatus;

	@XmlElement(name = "ReasonFailValidation")
	private String reasonFailValidation;

	@XmlElement(name = "NarrativeComments")
	private String narrativeComments;

	@XmlElement(name = "AuditOwner")
	private String auditOwner;

	@XmlElement(name = "Controller")
	private String auditController;

	@XmlElement(name = "LeadAuditor")
	private String leadAuditor;

	@XmlElement(name = "CapReopenDate")
	private String capReopenDate;

	@XmlElement(name = "ReOpenedCAPCount")
	private String numberOfTimesCapReOpened;

	@XmlElement(name = "CapRiskMitigatedDate")
	private String capRiskMitigatedDate;

	public String getCapName() {
		return capName;
	}

	public void setCapName(String capName) {
		this.capName = capName;
	}

	public String getCapStatus() {
		return capStatus;
	}

	public void setCapStatus(String capStatus) {
		this.capStatus = capStatus;
	}

	public String getReasonFailValidation() {
		return reasonFailValidation;
	}

	public void setReasonFailValidation(String reasonFailValidation) {
		this.reasonFailValidation = reasonFailValidation;
	}

	public String getNarrativeComments() {
		return narrativeComments;
	}

	public void setNarrativeComments(String narrativeComments) {
		this.narrativeComments = narrativeComments;
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

	public String getCapReopenDate() {
		return capReopenDate;
	}

	public void setCapReopenDate(String capReopenDate) {
		this.capReopenDate = capReopenDate;
	}

	public String getNumberOfTimesCapReOpened() {
		return numberOfTimesCapReOpened;
	}

	public void setNumberOfTimesCapReOpened(String numberOfTimesCapReOpened) {
		this.numberOfTimesCapReOpened = numberOfTimesCapReOpened;
	}

	public String getCapRiskMitigatedDate() {
		return capRiskMitigatedDate;
	}

	public void setCapRiskMitigatedDate(String capRiskMitigatedDate) {
		this.capRiskMitigatedDate = capRiskMitigatedDate;
	}

	@Override
	public String toString() {
		return "CapForm [capName=" + capName + ", capStatus=" + capStatus + ", reasonFailValidation="
				+ reasonFailValidation + ", narrativeComments=" + narrativeComments + ", auditOwner=" + auditOwner
				+ ", auditController=" + auditController + ", leadAuditor=" + leadAuditor + ", capReopenDate="
				+ capReopenDate + ", numberOfTimesCapReOpened=" + numberOfTimesCapReOpened + ", capRiskMitigatedDate="
				+ capRiskMitigatedDate + "]";
	}

}
