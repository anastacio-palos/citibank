package com.ibm.openpages.ext.batch.omu.bean;

public class CAPBean {
    private String id;
    private String name;
    private String assignee;
    private String validation;
    private String status;
    private String type;
    public CAPBean() {
    }

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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CAPBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", assignee='" + assignee + '\'' +
                ", validation='" + validation + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
