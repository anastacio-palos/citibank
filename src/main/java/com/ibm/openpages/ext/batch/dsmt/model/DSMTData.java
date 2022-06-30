package com.ibm.openpages.ext.batch.dsmt.model;

public class DSMTData implements Cloneable{

    private String nodeType;
    private String nodeID;
    private int failCount;
    private Long lastUpdated;
    private String errorMessage;
    private String resourceID;


    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(final String nodeType) {
        this.nodeType = nodeType;
    }


    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(final String nodeID) {
        this.nodeID = nodeID;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(final int failCount) {
        this.failCount = failCount;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(final String resourceID) {
        this.resourceID = resourceID;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String tripletID;

    public DSMTData(String tripletID){
        this.tripletID = tripletID;
    }


    public String getTripletID() {
        return tripletID;
    }

    public void setTripletID(final String tripletID) {
        this.tripletID = tripletID;
    }

    @Override
    public String toString() {
        return "DSMTData{" +
               "nodeType='" + nodeType + '\'' +
               ", nodeID='" + nodeID + '\'' +
               ", failCount=" + failCount +
               ", lastUpdated=" + lastUpdated +
               ", errorMessage='" + errorMessage + '\'' +
               ", resourceID='" + resourceID + '\'' +
               ", tripletID='" + tripletID + '\'' +
               '}';
    }


}
