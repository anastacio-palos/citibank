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

public class ChangeHistory {

    private String changeLogId;
    private String manSegmentId;
    private String manGeoId;
    private String resOrgMgrGeid;
    private String changeType;
    private String oldValue;
    private String newValue;
    private String lastEditedDate;

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

    public Object getOldValue() {

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

        return "ChangeHistory [changeLogId=" + changeLogId + ", manSegmentId=" + manSegmentId + ", manGeoId=" + manGeoId
                + ", resOrgMgrGeid=" + resOrgMgrGeid + ", changeType=" + changeType + ", oldValue=" + oldValue
                + ", newValue=" + newValue + ", lastEditedDate=" + lastEditedDate + "]";
    }

}
