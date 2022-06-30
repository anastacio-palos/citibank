package com.ibm.openpages.ext.interfaces.icaps.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuditIssueToICapsRequest {
    @JsonProperty("Issue ID")
    String objectId;

    @JsonProperty("Issue ID")
    public String getObjectId() {
        return objectId;
    }

    @JsonProperty("Issue ID")
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
