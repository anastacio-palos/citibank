package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;

public class AuditPlanBenchmarkHelperDisplayInfo implements Serializable {

    private static final long serialVersionUID = -7700808007887848867L;

    private String id;
    private String objectid;
    private String resourceid;
    private String title;
    private String location;
    private String leadproductteam;
    private String benchmark;
    private String objectType;

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
    public String getLocation() {
        return location;
    }
    public void setLocation(String folder) {
        this.location = folder;
    }
    public String getLeadproductteam() {
        return leadproductteam;
    }
    public void setLeadproductteam(String leadproductteam) {
        this.leadproductteam = leadproductteam;
    }
    public String getBenchmark() {
        return benchmark;
    }
    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }
    public String getObjectType() {
        return objectType;
    }
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return "AuditPlanBenchmarkHelperDisplayInfo [id=" + id + ", objectid=" + objectid + ", resourceid=" + resourceid
                + ", title=" + title + ", location=" + location + ", leadproductteam=" + leadproductteam + ", benchmark="
                + benchmark + ", objectType=" + objectType + "]";
    }
}
