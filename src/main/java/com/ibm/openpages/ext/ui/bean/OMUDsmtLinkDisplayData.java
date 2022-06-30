/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * © Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import java.util.Set;

public class OMUDsmtLinkDisplayData implements Serializable {

    private static final long serialVersionUID = -7700808007887856857L;

    private String id;
    private String name;
    private String link;
    private String resourceId;
    private String description;
    private String scope;
    private String status;
    private String active;
    private String baseId;
    private String objectType;
    private boolean expanded;
    private boolean selected;
    private boolean disabled;
    private boolean deleted;
    private boolean check;
    private Set<String> disableInfoSet;
    private String keyId;
    private String msOrgType;
    private String manSegmentId;
    private String manSegmentName;
    private String manGeoName;
    private String manGeoId;
    private String resOrgMgrGeid;
    private String hrGenId;
    private String primaryMgr;
    private String coheadMgr;
    private String orgGrpMgr;
    private String effDt;
    private String endDt;
    private String lastUpdoprId;
    private String lastUpdtDwEttm;
    private String activeInd;
    private String createdBy;
    private String createdDate;
    private String lastEditedBy;
    private String lastEditedDate;
    private String soeId;
    private String employeeStatus;
    private String uniqueKey;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getLink() {

        return link;
    }

    public void setLink(String link) {

        this.link = link;
    }

    public String getResourceId() {

        return resourceId;
    }

    public void setResourceId(String resourceId) {

        this.resourceId = resourceId;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getScope() {

        return scope;
    }

    public void setScope(String scope) {

        this.scope = scope;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getActive() {

        return active;
    }

    public void setActive(String active) {

        this.active = active;
    }

    public String getBaseId() {

        return baseId;
    }

    public void setBaseId(String baseId) {

        this.baseId = baseId;
    }

    public String getObjectType() {

        return objectType;
    }

    public void setObjectType(String objectType) {

        this.objectType = objectType;
    }

    public boolean isExpanded() {

        return expanded;
    }

    public void setExpanded(boolean expanded) {

        this.expanded = expanded;
    }

    public boolean isSelected() {

        return selected;
    }

    public void setSelected(boolean selected) {

        this.selected = selected;
    }

    public boolean isDisabled() {

        return disabled;
    }

    public void setDisabled(boolean disabled) {

        this.disabled = disabled;
    }

    public boolean isDeleted() {

        return deleted;
    }

    public void setDeleted(boolean deleted) {

        this.deleted = deleted;
    }

    public boolean isCheck() {
    
        return check;
    }

    public void setCheck(boolean check) {
    
        this.check = check;
    }

    public Set<String> getDisableInfoSet() {

        return disableInfoSet;
    }

    public void setDisableInfoSet(Set<String> disableInfoSet) {

        this.disableInfoSet = disableInfoSet;
    }

    public String getKeyId() {

        return keyId;
    }

    public void setKeyId(String keyId) {

        this.keyId = keyId;
    }

    public String getMsOrgType() {

        return msOrgType;
    }

    public void setMsOrgType(String msOrgType) {

        this.msOrgType = msOrgType;
    }

    public String getManSegmentId() {

        return manSegmentId;
    }

    public void setManSegmentId(String manSegmentId) {

        this.manSegmentId = manSegmentId;
    }

    public String getManSegmentName() {
    
        return manSegmentName;
    }

    public void setManSegmentName(String manSegmentName) {
    
        this.manSegmentName = manSegmentName;
    }

    public String getManGeoName() {
    
        return manGeoName;
    }

    public void setManGeoName(String manGeoName) {
    
        this.manGeoName = manGeoName;
    }

    public String getManGeoId() {

        return manGeoId;
    }

    public void setManGeoId(String manGeoId) {

        this.manGeoId = manGeoId;
    }

    public String getResOrgMgrGeid() {

        return resOrgMgrGeid;
    }

    public void setResOrgMgrGeid(String resOrgMgrGeid) {

        this.resOrgMgrGeid = resOrgMgrGeid;
    }

    public String getHrGenId() {

        return hrGenId;
    }

    public void setHrGenId(String hrGenId) {

        this.hrGenId = hrGenId;
    }

    public String getPrimaryMgr() {

        return primaryMgr;
    }

    public void setPrimaryMgr(String primaryMgr) {

        this.primaryMgr = primaryMgr;
    }

    public String getCoheadMgr() {

        return coheadMgr;
    }

    public void setCoheadMgr(String coheadMgr) {

        this.coheadMgr = coheadMgr;
    }

    public String getOrgGrpMgr() {

        return orgGrpMgr;
    }

    public void setOrgGrpMgr(String orgGrpMgr) {

        this.orgGrpMgr = orgGrpMgr;
    }

    public String getEffDt() {

        return effDt;
    }

    public void setEffDt(String effDt) {

        this.effDt = effDt;
    }

    public String getEndDt() {

        return endDt;
    }

    public void setEndDt(String endDt) {

        this.endDt = endDt;
    }

    public String getLastUpdoprId() {

        return lastUpdoprId;
    }

    public void setLastUpdoprId(String lastUpdoprId) {

        this.lastUpdoprId = lastUpdoprId;
    }

    public String getLastUpdtDwEttm() {

        return lastUpdtDwEttm;
    }

    public void setLastUpdtDwEttm(String lastUpdtDwEttm) {

        this.lastUpdtDwEttm = lastUpdtDwEttm;
    }

    public String getActiveInd() {

        return activeInd;
    }

    public void setActiveInd(String activeInd) {

        this.activeInd = activeInd;
    }

    public String getCreatedBy() {

        return createdBy;
    }

    public void setCreatedBy(String createdBy) {

        this.createdBy = createdBy;
    }

    public String getCreatedDate() {

        return createdDate;
    }

    public void setCreatedDate(String createdDate) {

        this.createdDate = createdDate;
    }

    public String getLastEditedBy() {

        return lastEditedBy;
    }

    public void setLastEditedBy(String lastEditedBy) {

        this.lastEditedBy = lastEditedBy;
    }

    public String getLastEditedDate() {

        return lastEditedDate;
    }

    public void setLastEditedDate(String lastEditedDate) {

        this.lastEditedDate = lastEditedDate;
    }

    public String getSoeId() {

        return soeId;
    }

    public void setSoeId(String soeId) {

        this.soeId = soeId;
    }

    public String getEmployeeStatus() {

        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {

        this.employeeStatus = employeeStatus;
    }

    public String getUniqueKey() {

        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {

        this.uniqueKey = uniqueKey;
    }

    @Override
    public String toString() {

        return "OMUDsmtLinkDisplayData [id=" + id + ", name=" + name + ", link=" + link + ", resourceId=" + resourceId
                + ", description=" + description + ", scope=" + scope + ", status=" + status + ", active=" + active
                + ", baseId=" + baseId + ", objectType=" + objectType + ", expanded=" + expanded + ", selected="
                + selected + ", disabled=" + disabled + ", deleted=" + deleted + ", check=" + check
                + ", disableInfoSet=" + disableInfoSet + ", keyId=" + keyId + ", msOrgType=" + msOrgType
                + ", manSegmentId=" + manSegmentId + ", manSegmentName=" + manSegmentName + ", manGeoName=" + manGeoName
                + ", manGeoId=" + manGeoId + ", resOrgMgrGeid=" + resOrgMgrGeid + ", hrGenId=" + hrGenId
                + ", primaryMgr=" + primaryMgr + ", coheadMgr=" + coheadMgr + ", orgGrpMgr=" + orgGrpMgr + ", effDt="
                + effDt + ", endDt=" + endDt + ", lastUpdoprId=" + lastUpdoprId + ", lastUpdtDwEttm=" + lastUpdtDwEttm
                + ", activeInd=" + activeInd + ", createdBy=" + createdBy + ", createdDate=" + createdDate
                + ", lastEditedBy=" + lastEditedBy + ", lastEditedDate=" + lastEditedDate + ", soeId=" + soeId
                + ", employeeStatus=" + employeeStatus + ", uniqueKey=" + uniqueKey + "]";
    }
}
