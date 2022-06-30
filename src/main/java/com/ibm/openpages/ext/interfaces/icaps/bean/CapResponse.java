package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CapResponse")
public class CapResponse {

    @XmlElement(name = "ResponseStatus")
    private String responseStatus;
    @XmlElement(name = "CapId")
    private String capId;
    @XmlTransient
    private String sourceSytemCapId; // Not Found in Sample XML
    @XmlElement(name = "RequestId")
    private String requestId;
    @XmlElement(name = "ErrorData")
    private List<ErrorData> errorData;

    public CapResponse() {
        this.responseStatus = "";
        this.capId = "";
        this.sourceSytemCapId = "";
        this.requestId = "";
        this.errorData = new ArrayList<>();
    }

    public CapResponse(String responseStatus, String capId, String sourceSytemCapId, String requestId,
                       List<ErrorData> errorData) {
        this.responseStatus = responseStatus;
        this.capId = capId;
        this.sourceSytemCapId = sourceSytemCapId;
        this.requestId = requestId;
        this.errorData = errorData;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getCapId() {
        return capId;
    }

    public void setCapId(String capId) {
        this.capId = capId;
    }

    public String getSourceSytemCapId() {
        return sourceSytemCapId;
    }

    public void setSourceSytemCapId(String sourceSytemCapId) {
        this.sourceSytemCapId = sourceSytemCapId;
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

    @Override
    public String toString() {
        return "CapResponse{" +
                "responseStatus='" + responseStatus + '\'' +
                ", capId='" + capId + '\'' +
                ", sourceSytemCapId='" + sourceSytemCapId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", errorData=" + errorData +
                '}';
    }
}
