package com.ibm.openpages.ext.model;

public class DSMTTripletModel {

    private String tripletID;

    private String msid;

    private String mgid;

    private String lvid;

    private String status;

    public String getTripletID() {
        return tripletID;
    }

    public void setTripletID(final String tripletID) {
        this.tripletID = tripletID;
    }

    public String getMsid() {
        return msid;
    }

    public void setMsid(final String msid) {
        this.msid = msid;
    }

    public String getMgid() {
        return mgid;
    }

    public void setMgid(final String mgid) {
        this.mgid = mgid;
    }

    public String getLvid() {
        return lvid;
    }

    public void setLvid(final String lvid) {
        this.lvid = lvid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DSMTTripletModel{" +
               "tripletID='" + tripletID + '\'' +
               ", msid='" + msid + '\'' +
               ", mgid='" + mgid + '\'' +
               ", lvid='" + lvid + '\'' +
               ", status='" + status + '\'' +
               '}';
    }

    public boolean validateForInsert(){
        if(this.tripletID == null && this.msid ==null && this.mgid == null && this.lvid == null && this.status == null) return false;
        return true;
    }

    public boolean validateForDescope(){
        if(this.tripletID == null && this.status == null) return  false;
        return true;
    }
}
