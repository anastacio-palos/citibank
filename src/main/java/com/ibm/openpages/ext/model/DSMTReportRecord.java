package com.ibm.openpages.ext.model;

public class DSMTReportRecord implements ReportRecord{

    private String operation;

    private String tripletID;

    private String nodeID;

    private String nodeType;

    private String status;

    private String details;

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public String getTripletID() {
        return tripletID;
    }

    public void setTripletID(final String tripletID) {
        this.tripletID = tripletID;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(final String nodeID) {
        this.nodeID = nodeID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(final String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public String toReportRecord() {
        String reportRecord =  String.format("%s,%s,%s,%s,%s,%s", this.operation, this.tripletID,this.nodeID,
            this.nodeType, this.status, this.details);

        return reportRecord + System.lineSeparator();
    }
}
