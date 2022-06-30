package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import java.util.List;

//TODO format the code, add the licensing at the top. Add code level comments. Look at the file DataGridInfo
// bean for referernce. getters and setters should not be inside the variable sectioin
public class ExistingDSMTLinkInfo extends ExistingDSMTLinkBaseInfo implements Serializable {

    private static final long serialVersionUID = -7700808007887848857L;

    private String name;
    private String type;
    private String link;
    private String scope;
    private String status;
    private String baseId;
    private String active;
    private String resourceId;
    private String description;

    private String legalvehiclename;
    private String parentResourceId;
    private String managedsegmentname;
    private String managedgeographyname;

    private List<AncestorControlsInfo> controls;
    private List<String> hierarchy;
    private List<String> ancestorIssues;

	public List<String> getAncestorIssues() {
		return ancestorIssues;
	}

	public void setAncestorIssues(List<String> ancestorIssues) {
		this.ancestorIssues = ancestorIssues;
	}

	private List<ExistingDSMTLinkInfo> children;
    private DSMTLinkDisableInfo dsmtLinkDisableInfo;
    
    /**
     * @return the name
     */
    public String getName() {
    
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
    
        this.name = name;
    }
    
    /**
     * @return the type
     */
    public String getType() {
    
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
    
        this.type = type;
    }
    
    /**
     * @return the link
     */
    public String getLink() {
    
        return link;
    }
    
    /**
     * @param link the link to set
     */
    public void setLink(String link) {
    
        this.link = link;
    }
    
    /**
     * @return the scope
     */
    public String getScope() {
    
        return scope;
    }
    
    /**
     * @param scope the scope to set
     */
    public void setScope(String scope) {
    
        this.scope = scope;
    }
    
    /**
     * @return the status
     */
    public String getStatus() {
    
        return status;
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
    
        this.status = status;
    }
    
    /**
     * @return the baseId
     */
    public String getBaseId() {
    
        return baseId;
    }
    
    /**
     * @param baseId the baseId to set
     */
    public void setBaseId(String baseId) {
    
        this.baseId = baseId;
    }
    
    /**
     * @return the active
     */
    public String getActive() {
    
        return active;
    }
    
    /**
     * @param active the active to set
     */
    public void setActive(String active) {
    
        this.active = active;
    }
    
    /**
     * @return the resourceId
     */
    public String getResourceId() {
    
        return resourceId;
    }
    
    /**
     * @param resourceId the resourceId to set
     */
    public void setResourceId(String resourceId) {
    
        this.resourceId = resourceId;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
    
        return description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
    
        this.description = description;
    }
    
//    /**
//     * @return the disableInfo
//     */
//    public String getDisableInfo() {
//    
//        return disableInfo;
//    }
//    
//    /**
//     * @param disableInfo the disableInfo to set
//     */
//    public void setDisableInfo(String disableInfo) {
//    
//        this.disableInfo = disableInfo;
//    }
    
    /**
     * @return the managedsegmentname
     */
    public String getManagedsegmentname() {
    
        return managedsegmentname;
    }
    
    /**
     * @param managedsegmentname the managedsegmentname to set
     */
    public void setManagedsegmentname(String managedsegmentname) {
    
        this.managedsegmentname = managedsegmentname;
    }
    
    /**
     * @return the managedgeographyname
     */
    public String getManagedgeographyname() {
    
        return managedgeographyname;
    }
    
    /**
     * @param managedgeographyname the managedgeographyname to set
     */
    public void setManagedgeographyname(String managedgeographyname) {
    
        this.managedgeographyname = managedgeographyname;
    }
    
    /**
     * @return the legalvehiclename
     */
    public String getLegalvehiclename() {
    
        return legalvehiclename;
    }
    
    /**
     * @param legalvehiclename the legalvehiclename to set
     */
    public void setLegalvehiclename(String legalvehiclename) {
    
        this.legalvehiclename = legalvehiclename;
    }
    
    /**
     * @return the parentResourceId
     */
    public String getParentResourceId() {
    
        return parentResourceId;
    }
    
    /**
     * @param parentResourceId the parentResourceId to set
     */
    public void setParentResourceId(String parentResourceId) {
    
        this.parentResourceId = parentResourceId;
    }
    
    /**
     * @return the hierarchy
     */
    public List<String> getHierarchy() {
    
        return hierarchy;
    }
    
    /**
     * @param hierarchy the hierarchy to set
     */
    public void setHierarchy(List<String> hierarchy) {
    
        this.hierarchy = hierarchy;
    }
    
    /**
     * @return the children
     */
    public List<ExistingDSMTLinkInfo> getChildren() {
    
        return children;
    }
    
    /**
     * @param children the children to set
     */
    public void setChildren(List<ExistingDSMTLinkInfo> children) {
    
        this.children = children;
    }
    
    /**
     * @return the dsmtLinkDisableInfo
     */
    public DSMTLinkDisableInfo getDsmtLinkDisableInfo() {
    
        return dsmtLinkDisableInfo;
    }
    
    /**
     * @param dsmtLinkDisableInfo the dsmtLinkDisableInfo to set
     */
    public void setDsmtLinkDisableInfo(DSMTLinkDisableInfo dsmtLinkDisableInfo) {
    
        this.dsmtLinkDisableInfo = dsmtLinkDisableInfo;
    }

    public List<AncestorControlsInfo> getControls() {

        return controls;
    }

    public void setControls(List<AncestorControlsInfo> controls) {

        this.controls = controls;
    }

    @Override
	public String toString() {
		return "ExistingDSMTLinkInfo [name=" + name + ", type=" + type + ", link=" + link + ", scope=" + scope
				+ ", status=" + status + ", baseId=" + baseId + ", active=" + active + ", resourceId=" + resourceId
				+ ", description=" + description + ", legalvehiclename=" + legalvehiclename + ", parentResourceId="
				+ parentResourceId + ", managedsegmentname=" + managedsegmentname + ", managedgeographyname="
				+ managedgeographyname + ", controls=" + controls + ", hierarchy=" + hierarchy + ", ancestorIssues="
				+ ancestorIssues + ", children=" + children + ", dsmtLinkDisableInfo=" + dsmtLinkDisableInfo + "]";
	}
}
