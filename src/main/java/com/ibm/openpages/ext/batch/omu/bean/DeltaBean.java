package com.ibm.openpages.ext.batch.omu.bean;

public class DeltaBean {

    private String soeId;

    private String oldStatus;

    private String newStatus;

    private String invalidHR;

    public DeltaBean() {
        soeId = "";
        oldStatus = "";
        newStatus = "";
        invalidHR = "";
    }

    public String getSoeId() {
        return soeId;
    }

    public void setSoeId(String soeId) {
        this.soeId = soeId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getInvalidHR() {
        return invalidHR;
    }

    public void setInvalidHR(String invalidHR) {
        this.invalidHR = invalidHR;
    }

    @Override
    public String toString() {
        return "DeltaBean{" +
                "soeId='" + soeId + '\'' +
                ", oldStatus='" + oldStatus + '\'' +
                ", newStatus='" + newStatus + '\'' +
                ", invalidHR='" + invalidHR + '\'' +
                '}';
    }
}
