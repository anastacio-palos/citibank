package com.ibm.openpages.ext.batch.omu.bean;

import java.util.ArrayList;
import java.util.List;

public class IssueBean {
    private String id;
    private String name;
    private String status;
    private String gocNumber;
    private String gocName;
    private String type;
    private List<String> usernames;

    public IssueBean() {
        usernames = new ArrayList<>();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGocNumber() {
        return gocNumber;
    }

    public void setGocNumber(String gocNumber) {
        this.gocNumber = gocNumber;
    }

    public String getGocName() {
        return gocName;
    }

    public void setGocName(String gocName) {
        this.gocName = gocName;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "IssueBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", gocNumber='" + gocNumber + '\'' +
                ", gocName='" + gocName + '\'' +
                ", type='" + type + '\'' +
                ", usernames=" + usernames +
                '}';
    }
}
