/*********************************************************************************************************************
 IBM Confidential OCO Source Materials

 5725-D51, 5725-D52, 5725-D53, 5725-D54

 © Copyright IBM Corporation 2021

 The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 has been deposited with the U.S. Copyright Office.
 */

package com.ibm.openpages.ext.ui.bean;

public class AncestorIssueInfo extends ExistingDSMTLinkBaseInfo  {

	//TODO format the code, add the licensing at the top. Add code level comments. Look at the file DataGridInfo
	// bean for referernce.
	/**
	 * 
	 */
	private static final long serialVersionUID = 3826168945244440238L;
	
	private String resourceId;
	private String name;
	private String title;
	private String issuelevel;
	private String associationstatus;
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIssuelevel() {
		return issuelevel;
	}
	public void setIssuelevel(String issuelevel) {
		this.issuelevel = issuelevel;
	}
	public String getAssociationstatus() {
		return associationstatus;
	}
	public void setAssociationstatus(String associationstatus) {
		this.associationstatus = associationstatus;
	}
	@Override
	public String toString() {
		return "AncestorIssueInfo [resourceId=" + resourceId + ", name=" + name + ", title=" + title + ", issueLevel="
				+ issuelevel + ", associationStatus=" + associationstatus + "]";
	}
	
	
	
	
	

}
