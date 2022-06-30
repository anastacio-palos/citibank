package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "IcapsResponse")
public class ICapsResponse {

    private String json;
    @XmlElement(name="IssueResponse")
    private IssueResponse issueResponse;

    public ICapsResponse() {
        this.issueResponse = new IssueResponse();
    }

    public ICapsResponse(IssueResponse issueResponse) {
        this.issueResponse = issueResponse;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public IssueResponse getIssueResponse() {
        return issueResponse;
    }

    public void setIssueResponse(IssueResponse issueResponse) {
        this.issueResponse = issueResponse;
    }

    @Override
    public String toString() {
        return "IcapsResponse{" +
                "issueResponse=" + issueResponse +
                '}';
    }
}

