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

public class Node {

    private String keyId;
    private String msOrgType;
    private String manSegmentId;
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

//    @JsonIgnore
//    private boolean isSelected;
//
//    @JsonIgnore
//    private boolean isDisabled;

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

//    public boolean isDisabled() {
//
//        return isDisabled;
//    }
//
//    public void setDisabled(boolean isDisabled) {
//
//        this.isDisabled = isDisabled;
//    }
//
//    public boolean isSelected() {
//
//        return isSelected;
//    }
//
//    public void setSelected(boolean isSelected) {
//
//        this.isSelected = isSelected;
//    }

    @Override
    public String toString() {

        return "Node [keyId=" + keyId + ", msOrgType=" + msOrgType + ", manSegmentId=" + manSegmentId + ", manGeoId="
                + manGeoId + ", resOrgMgrGeid=" + resOrgMgrGeid + ", hrGenId=" + hrGenId + ", primaryMgr=" + primaryMgr
                + ", coheadMgr=" + coheadMgr + ", orgGrpMgr=" + orgGrpMgr + ", effDt=" + effDt + ", endDt=" + endDt
                + ", lastUpdoprId=" + lastUpdoprId + ", lastUpdtDwEttm=" + lastUpdtDwEttm + ", activeInd=" + activeInd
                + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastEditedBy=" + lastEditedBy
                + ", lastEditedDate=" + lastEditedDate + ", soeId=" + soeId + ", employeeStatus=" + employeeStatus
                + ", uniqueKey=" + uniqueKey + "]";
    }

}
