package com.ibm.openpages.ext.ui.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.List;

public class DSMTLinkSearchInfo implements Serializable {

    private static final long serialVersionUID = -4875002309233244762L;

    @JsonIgnore
    private boolean isTreeSearch;
    
    private boolean topLevel;

    private String parentNodeId;

    // The Node selected possible values MS, MG or NV
    private String nodeSelected;

    // Value of the search entered in the UI
    private String searchText;

    // The ID of the Managed Segment based on the selection
    private String manageSegNodeId;

    // The ID of the Legal Vehicle based on the selection
    private String legalVehicleNodeId;

    // The ID of the Managed Geography based on the selection\
    private String manageGeographyNodeId;

    @JsonIgnore
    private String parentNodeHierarchy;

    @JsonIgnore
    private boolean isManagedGeography;

    @JsonIgnore
    private String selectedNode;
    
    @JsonIgnore
    private String preparedStmtQuery;

    @JsonIgnore
    private ResultSet queryResults;

    @JsonIgnore
    private List<String> preparedStmtValues;
    
    @JsonIgnore
    private List<String> restrictedNodesList;
    
    @JsonIgnore
    private List<String> headerFieldNamesList;

    /**
     * @return the isTreeSearch
     */
    public boolean isTreeSearch() {

        return isTreeSearch;
    }

    /**
     * @param isTreeSearch
     *            the isTreeSearch to set
     */
    public void setTreeSearch(boolean isTreeSearch) {

        this.isTreeSearch = isTreeSearch;
    }

    /**
     * @return the topLevel
     */
    public boolean isTopLevel() {

        return topLevel;
    }

    /**
     * @param topLevel
     *            the topLevel to set
     */
    public void setTopLevel(boolean topLevel) {

        this.topLevel = topLevel;
    }

    /**
     * @return the parentNodeId
     */
    public String getParentNodeId() {

        return parentNodeId;
    }

    /**
     * @param parentNodeId
     *            the parentNodeId to set
     */
    public void setParentNodeId(String parentNodeId) {

        this.parentNodeId = parentNodeId;
    }

    /**
     * @return the nodeSelected
     */
    public String getNodeSelected() {

        return nodeSelected;
    }

    /**
     * @param nodeSelected
     *            the nodeSelected to set
     */
    public void setNodeSelected(String nodeSelected) {

        this.nodeSelected = nodeSelected;
    }

    /**
     * @return the searchText
     */
    public String getSearchText() {

        return searchText;
    }

    /**
     * @param searchText
     *            the searchText to set
     */
    public void setSearchText(String searchText) {

        this.searchText = searchText;
    }

    /**
     * @return the manageSegNodeId
     */
    public String getManageSegNodeId() {

        return manageSegNodeId;
    }

    /**
     * @param manageSegNodeId
     *            the manageSegNodeId to set
     */
    public void setManageSegNodeId(String manageSegNodeId) {

        this.manageSegNodeId = manageSegNodeId;
    }

    /**
     * @return the legalVehicleNodeId
     */
    public String getLegalVehicleNodeId() {

        return legalVehicleNodeId;
    }

    /**
     * @param legalVehicleNodeId
     *            the legalVehicleNodeId to set
     */
    public void setLegalVehicleNodeId(String legalVehicleNodeId) {

        this.legalVehicleNodeId = legalVehicleNodeId;
    }

    /**
     * @return the manageGeographyNodeId
     */
    public String getManageGeographyNodeId() {

        return manageGeographyNodeId;
    }

    /**
     * @param manageGeographyNodeId
     *            the manageGeographyNodeId to set
     */
    public void setManageGeographyNodeId(String manageGeographyNodeId) {

        this.manageGeographyNodeId = manageGeographyNodeId;
    }

    /**
     * @return the parentNodeHierarchy
     */
    public String getParentNodeHierarchy() {

        return parentNodeHierarchy;
    }

    /**
     * @param parentNodeHierarchy
     *            the parentNodeHierarchy to set
     */
    public void setParentNodeHierarchy(String parentNodeHierarchy) {

        this.parentNodeHierarchy = parentNodeHierarchy;
    }

    /**
     * @return the isManagedGeography
     */
    public boolean isManagedGeography() {

        return isManagedGeography;
    }

    /**
     * @param isManagedGeography
     *            the isManagedGeography to set
     */
    public void setManagedGeography(boolean isManagedGeography) {

        this.isManagedGeography = isManagedGeography;
    }

    /**
     * @return the selectedNode
     */
    public String getSelectedNode() {

        return selectedNode;
    }

    /**
     * @param selectedNode
     *            the selectedNode to set
     */
    public void setSelectedNode(String selectedNode) {

        this.selectedNode = selectedNode;
    }

    /**
     * @return the preparedStmtQuery
     */
    public String getPreparedStmtQuery() {

        return preparedStmtQuery;
    }

    /**
     * @param preparedStmtQuery
     *            the preparedStmtQuery to set
     */
    public void setPreparedStmtQuery(String preparedStmtQuery) {

        this.preparedStmtQuery = preparedStmtQuery;
    }

    /**
     * @return the queryResults
     */
    public ResultSet getQueryResults() {

        return queryResults;
    }

    /**
     * @param queryResults
     *            the queryResults to set
     */
    public void setQueryResults(ResultSet queryResults) {

        this.queryResults = queryResults;
    }

    /**
     * @return the preparedStmtValues
     */
    public List<String> getPreparedStmtValues() {

        return preparedStmtValues;
    }

    /**
     * @param preparedStmtValues
     *            the preparedStmtValues to set
     */
    public void setPreparedStmtValues(List<String> preparedStmtValues) {

        this.preparedStmtValues = preparedStmtValues;
    }

    /**
     * @return the restrictedNodesList
     */
    public List<String> getRestrictedNodesList() {

        return restrictedNodesList;
    }

    /**
     * @param restrictedNodesList
     *            the restrictedNodesList to set
     */
    public void setRestrictedNodesList(List<String> restrictedNodesList) {

        this.restrictedNodesList = restrictedNodesList;
    }

    /**
     * @return the headerFieldNamesList
     */
    public List<String> getHeaderFieldNamesList() {

        return headerFieldNamesList;
    }

    /**
     * @param headerFieldNamesList
     *            the headerFieldNamesList to set
     */
    public void setHeaderFieldNamesList(List<String> headerFieldNamesList) {

        this.headerFieldNamesList = headerFieldNamesList;
    }

    @Override
    public String toString() {

        return "DSMTLinkSearchInfo [isTreeSearch=" + isTreeSearch + ", topLevel=" + topLevel + ", parentNodeId="
                + parentNodeId + ", nodeSelected=" + nodeSelected + ", searchText=" + searchText + ", manageSegNodeId="
                + manageSegNodeId + ", legalVehicleNodeId=" + legalVehicleNodeId + ", manageGeographyNodeId="
                + manageGeographyNodeId + ", parentNodeHierarchy=" + parentNodeHierarchy + ", isManagedGeography="
                + isManagedGeography + ", selectedNode=" + selectedNode + ", preparedStmtQuery=" + preparedStmtQuery
                + ", preparedStmtValues=" + preparedStmtValues + ", restrictedNodesList=" + restrictedNodesList
                + ", headerFieldNamesList=" + headerFieldNamesList + "]";
    }
}
