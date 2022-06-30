package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "IssueForm")
public class RegulatoryIssueToICapsRequest {

	public RegulatoryIssueToICapsRequest() {
		this.issueHeading = new IssueHeading();
		this.responsibleIndividuals = new ResponsibleIndividuals();
		this.listOfCaps = new ArrayList<>();
	}

	@XmlElement(name = "IssueHeading")
	private IssueHeading issueHeading;

	@XmlElement(name = "ResponsibleIndividuals")
	private ResponsibleIndividuals responsibleIndividuals;

	@XmlElement(name = "CapForm")
	private List<CapForm> listOfCaps;

	public IssueHeading getIssueHeading() {
		return issueHeading;
	}

	public void setIssueHeading(IssueHeading issueHeading) {
		this.issueHeading = issueHeading;
	}

	public ResponsibleIndividuals getResponsibleIndividuals() {
		return responsibleIndividuals;
	}

	public void setResponsibleIndividuals(ResponsibleIndividuals responsibleIndividuals) {
		this.responsibleIndividuals = responsibleIndividuals;
	}

	public List<CapForm> getListOfCaps() {
		return listOfCaps;
	}

	public void setListOfCaps(List<CapForm> listOfCaps) {
		this.listOfCaps = listOfCaps;
	}

	@Override
	public String toString() {
		return "IssueForm{" + "issueHeading=" + issueHeading + ", responsibleIndividuals=" + responsibleIndividuals
				+ ", listOfCaps=" + listOfCaps + '}';
	}
}