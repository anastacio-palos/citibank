package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;

public class ContextHelperDisplayInfo implements Serializable {

    private static final long serialVersionUID = -7700808007887848867L;

    private String id;
    private String objectid;
    private String resourceid;
    private String title;
    private String disposition;
    private String exceptionsource;
    private String issuestatus;
    private String objectType;
    private String auditableentitystatus;
    private String auditableentitytype;
    private String auditableentitysubtype;
    private boolean isSelected;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getObjectid() {
        return objectid;
    }
    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }
    public String getResourceid() {
        return resourceid;
    }
    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDisposition() {
        return disposition;
    }
    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }
    public String getExceptionsource() {
        return exceptionsource;
    }
    public void setExceptionsource(String exceptionsource) {
        this.exceptionsource = exceptionsource;
    }
    public String getIssuestatus() {
        return issuestatus;
    }
    public void setIssuestatus(String issuestatus) {
        this.issuestatus = issuestatus;
    }
    public String getObjectType() {
        return objectType;
    }
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    public String getAuditableentitystatus() {
        return auditableentitystatus;
    }
    public void setAuditableentitystatus(String auditableentitystatus) {
        this.auditableentitystatus = auditableentitystatus;
    }
    public String getAuditableentitytype() {
        return auditableentitytype;
    }
    public void setAuditableentitytype(String auditableentitytype) {
        this.auditableentitytype = auditableentitytype;
    }
    public String getAuditableentitysubtype() {
        return auditableentitysubtype;
    }
    public void setAuditableentitysubtype(String auditableentitysubtype) {
        this.auditableentitysubtype = auditableentitysubtype;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    @Override
    public String toString() {
        return "ContextHelperDisplayInfo [id=" + id + ", objectid=" + objectid + ", resourceid=" + resourceid
                + ", title=" + title + ", disposition=" + disposition + ", exceptionsource=" + exceptionsource
                + ", issuestatus=" + issuestatus + ", objectType=" + objectType + ", auditableentitystatus="
                + auditableentitystatus + ", auditableentitytype=" + auditableentitytype + ", auditableentitysubtype="
                + auditableentitysubtype + ", isSelected=" + isSelected + "]";
    }
}
