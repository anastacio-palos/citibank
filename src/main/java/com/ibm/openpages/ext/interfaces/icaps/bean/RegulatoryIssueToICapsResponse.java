package com.ibm.openpages.ext.interfaces.icaps.bean;

import java.util.Objects;

public class RegulatoryIssueToICapsResponse {
    private String status;
    private String message;
    private String requestID;

    public RegulatoryIssueToICapsResponse() {
    }

    public RegulatoryIssueToICapsResponse(String status, String message, String requestID) {
        this.status = status;
        this.message = message;
        this.requestID = requestID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    @Override
    public String toString() {
        return "ICapsResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", requestID='" + requestID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegulatoryIssueToICapsResponse that = (RegulatoryIssueToICapsResponse) o;
        return Objects.equals(status, that.status) && Objects.equals(message, that.message) && Objects.equals(requestID, that.requestID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, requestID);
    }
}
