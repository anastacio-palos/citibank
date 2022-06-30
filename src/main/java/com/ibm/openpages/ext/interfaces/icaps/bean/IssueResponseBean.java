package com.ibm.openpages.ext.interfaces.icaps.bean;

public class IssueResponseBean {

    private String status;
    private StringBuilder comments;

    public IssueResponseBean(String status, StringBuilder message) {
        this.status = status;
        this.comments = message;
    }

    public IssueResponseBean() {
        super();
        this.status = status="";
        this.comments = new StringBuilder();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StringBuilder getComments() {
        return comments;
    }

    public void setComments(StringBuilder comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "IssueResponseBean{" +
                "status='" + status + '\'' +
                ", comments=" + comments +
                '}';
    }
}
