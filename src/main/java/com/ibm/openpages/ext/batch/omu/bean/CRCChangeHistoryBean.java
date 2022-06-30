package com.ibm.openpages.ext.batch.omu.bean;

public class CRCChangeHistoryBean {

    private String changeLogId;
    private String manSegmentId;
    private String manGeoId;
    private String resOrgMgrGeid;
    private String changeType;
    private String oldValue;
    private String newValue;
    private String lastEditedDate;

    public CRCChangeHistoryBean() {
    }

    public CRCChangeHistoryBean(String changeLogId, String manSegmentId, String manGeoId, String resOrgMgrGeid, String changeType, String oldValue, String newValue, String lastEditedDate) {
        this.changeLogId = changeLogId;
        this.manSegmentId = manSegmentId;
        this.manGeoId = manGeoId;
        this.resOrgMgrGeid = resOrgMgrGeid;
        this.changeType = changeType;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.lastEditedDate = lastEditedDate;
    }

    public String getChangeLogId() {
        return changeLogId;
    }

    public void setChangeLogId(String changeLogId) {
        this.changeLogId = changeLogId;
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

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(String lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    @Override
    public String toString() {
        return "CRCChangeHistory{" +
                "changeLogId='" + changeLogId + '\'' +
                ", manSegmentId='" + manSegmentId + '\'' +
                ", manGeoId='" + manGeoId + '\'' +
                ", resOrgMgrGeid='" + resOrgMgrGeid + '\'' +
                ", changeType='" + changeType + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", lastEditedDate='" + lastEditedDate + '\'' +
                '}';
    }
}
