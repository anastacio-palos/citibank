package com.ibm.openpages.ext.batch.dsmt.model;

public class DSMTDetails {

    private String nodeType;
    private String nodeID;
    private String nodeName;
    private String nodeLevel;
    private String nodeRegion;
    private String countryCode;
    private String childHierarchy;
    private String parentNodeID;
    private String parentHierarchy;
    private String status;
    private boolean processed;

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(final boolean processed) {
        this.processed = processed;
    }


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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(final String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(final String nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getNodeRegion() {
        return nodeRegion;
    }

    public void setNodeRegion(final String nodeRegion) {
        this.nodeRegion = nodeRegion;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getChildHierarchy() {
        return childHierarchy;
    }

    public void setChildHierarchy(final String childHierarchy) {
        this.childHierarchy = childHierarchy;
    }

    public String getParentNodeID() {
        return parentNodeID;
    }

    public void setParentNodeID(final String parentNodeID) {
        this.parentNodeID = parentNodeID;
    }

    public String getParentHierarchy() {
        return parentHierarchy;
    }

    public void setParentHierarchy(final String parentHierarchy) {
        this.parentHierarchy = parentHierarchy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DSMTDetails{" +
               "nodeType='" + nodeType + '\'' +
               ", nodeID='" + nodeID + '\'' +
               ", nodeName='" + nodeName + '\'' +
               ", nodeLevel='" + nodeLevel + '\'' +
               ", nodeRegion='" + nodeRegion + '\'' +
               ", countryCode='" + countryCode + '\'' +
               ", childHierarchy='" + childHierarchy + '\'' +
               ", parentNodeID='" + parentNodeID + '\'' +
               ", parentHierarchy='" + parentHierarchy + '\'' +
               ", status='" + status + '\'' +
               ", processed=" + processed +
               '}';
    }

}
