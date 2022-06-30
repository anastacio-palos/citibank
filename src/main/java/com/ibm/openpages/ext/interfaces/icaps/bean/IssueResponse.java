package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "IssueResponse")
public class IssueResponse {

    @XmlElement(name = "ResponseStatus")
    private String responseStatus;
    @XmlElement(name = "IssueId")
    private String issueId;
    @XmlTransient
    private String sourceSytemIssueId; // Not Found in Sample XML
    @XmlElement(name = "RequestId")
    private String requestId;
    @XmlElement(name = "ErrorData")
    private List<ErrorData> errorData;
    @XmlElement(name = "CapResponse")
    private List<CapResponse> capResponse;

    public IssueResponse() {
        this.responseStatus = "";
        this.issueId = "";
        this.sourceSytemIssueId = "";
        this.requestId = "";
        this.errorData = new ArrayList<>();
        this.capResponse = new ArrayList<>();
    }

    public IssueResponse(String responseStatus) {
        this.responseStatus = responseStatus;
        this.issueId = "";
        this.sourceSytemIssueId = "";
        this.requestId = "";
        this.errorData = new ArrayList<>();
        this.capResponse = new ArrayList<>();
    }

    public IssueResponse(String responseStatus, String issueId, String sourceSytemIssueId, String requestId,
                         List<ErrorData> errorData, List<CapResponse> capResponse) {
        this.responseStatus = responseStatus;
        this.issueId = issueId;
        this.sourceSytemIssueId = sourceSytemIssueId;
        this.requestId = requestId;
        this.errorData = errorData;
        this.capResponse = capResponse;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getSourceSytemIssueId() {
        return sourceSytemIssueId;
    }

    public void setSourceSytemIssueId(String sourceSytemIssueId) {
        this.sourceSytemIssueId = sourceSytemIssueId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<ErrorData> getErrorData() {
        return errorData;
    }

    public void setErrorData(List<ErrorData> errorData) {
        this.errorData = errorData;
    }

    public List<CapResponse> getCapResponse() {
        return capResponse;
    }

    public void setCapResponse(List<CapResponse> capResponse) {
        this.capResponse = capResponse;
    }

    @Override
    public String toString() {
        return "IssueResponse{" +
                "responseStatus='" + responseStatus + '\'' +
                ", issueId='" + issueId + '\'' +
                ", sourceSytemIssueId='" + sourceSytemIssueId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", errorData=" + errorData +
                ", capResponse=" + capResponse +
                '}';
    }
}
