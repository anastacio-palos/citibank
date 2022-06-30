package com.ibm.openpages.ext.interfaces.cmp.bean;

public class EntitlementResponseBean {

    private String userId;
    private String status;
    private StringBuilder comments;

    public EntitlementResponseBean(String userId, String status, StringBuilder message) {
        this.status = status;
        this.comments = message;
        this.userId = userId;
    }
    public EntitlementResponseBean() {
        super();
        this.status =  "";
        this.userId =  "Undefined";
        this.comments = new StringBuilder();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return "EntitlementResponseBean{" +
                "userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", comments=" + comments +
                '}';
    }

}
