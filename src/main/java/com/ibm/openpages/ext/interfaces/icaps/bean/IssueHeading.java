package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "IssueHeading")
public class IssueHeading {

	@XmlElement(name = "iCAPSIssueID")
	private String issueID;

	@XmlElement(name = "ValidationInIA")
	private String issueStatus;

	@XmlElement(name = "ReasonForFailedValidation")
	private String reasonFailValidation;

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

	public String getReasonFailValidation() {
		return reasonFailValidation;
	}

	public void setReasonFailValidation(String reasonFailValidation) {
		this.reasonFailValidation = reasonFailValidation;
	}

	@Override
	public String toString() {
		return "IssueHeading{" + "issueID='" + issueID + '\'' + ", issueStatus='" + issueStatus + '\''
				+ ", reasonFailValidation='" + reasonFailValidation + '\'' + '}';
	}
}

