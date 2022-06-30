package com.ibm.openpages.ext.batch.omu.bean;

public class CRCNodeBean {

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

    public CRCNodeBean() {
    }

    public CRCNodeBean(String keyId, String msOrgType, String manSegmentId, String manGeoId, String resOrgMgrGeid, String hrGenId, String primaryMgr, String coheadMgr, String orgGrpMgr, String effDt, String endDt, String lastUpdoprId, String lastUpdtDwEttm, String activeInd, String createdBy, String createdDate, String lastEditedBy, String lastEditedDate, String soeId, String employeeStatus, String uniqueKey) {
        this.keyId = keyId;
        this.msOrgType = msOrgType;
        this.manSegmentId = manSegmentId;
        this.manGeoId = manGeoId;
        this.resOrgMgrGeid = resOrgMgrGeid;
        this.hrGenId = hrGenId;
        this.primaryMgr = primaryMgr;
        this.coheadMgr = coheadMgr;
        this.orgGrpMgr = orgGrpMgr;
        this.effDt = effDt;
        this.endDt = endDt;
        this.lastUpdoprId = lastUpdoprId;
        this.lastUpdtDwEttm = lastUpdtDwEttm;
        this.activeInd = activeInd;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastEditedBy = lastEditedBy;
        this.lastEditedDate = lastEditedDate;
        this.soeId = soeId;
        this.employeeStatus = employeeStatus;
        this.uniqueKey = uniqueKey;
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
        return "CRCNodeBean{" +
                "keyId='" + keyId + '\'' +
                ", msOrgType='" + msOrgType + '\'' +
                ", manSegmentId='" + manSegmentId + '\'' +
                ", manGeoId='" + manGeoId + '\'' +
                ", resOrgMgrGeid='" + resOrgMgrGeid + '\'' +
                ", hrGenId='" + hrGenId + '\'' +
                ", primaryMgr='" + primaryMgr + '\'' +
                ", coheadMgr='" + coheadMgr + '\'' +
                ", orgGrpMgr='" + orgGrpMgr + '\'' +
                ", effDt='" + effDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", lastUpdoprId='" + lastUpdoprId + '\'' +
                ", lastUpdtDwEttm='" + lastUpdtDwEttm + '\'' +
                ", activeInd='" + activeInd + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", lastEditedBy='" + lastEditedBy + '\'' +
                ", lastEditedDate='" + lastEditedDate + '\'' +
                ", soeId='" + soeId + '\'' +
                ", employeeStatus='" + employeeStatus + '\'' +
                ", uniqueKey='" + uniqueKey + '\'' +
                '}';
    }
}
