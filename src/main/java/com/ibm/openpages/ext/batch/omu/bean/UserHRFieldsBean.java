package com.ibm.openpages.ext.batch.omu.bean;

public class UserHRFieldsBean {

    private String cghGoc;
    private String cghName;

    public UserHRFieldsBean() {
    }

    public UserHRFieldsBean(String cghGoc, String cghName) {
        this.cghGoc = cghGoc;
        this.cghName = cghName;
    }

    public String getCghGoc() {
        return cghGoc;
    }

    public void setCghGoc(String cghGoc) {
        this.cghGoc = cghGoc;
    }

    public String getCghName() {
        return cghName;
    }

    public void setCghName(String cghName) {
        this.cghName = cghName;
    }

    @Override
    public String toString() {
        return "EmployeeBean{" +
                "cghGoc='" + cghGoc + '\'' +
                ", cghName='" + cghName + '\'' +
                '}';
    }
}
