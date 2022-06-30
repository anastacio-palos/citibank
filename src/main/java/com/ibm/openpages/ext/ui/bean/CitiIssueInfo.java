package com.ibm.openpages.ext.ui.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.ext.ui.constant.IssueStatus;
import com.ibm.openpages.ext.ui.constant.IssueSubType;
import com.ibm.openpages.ext.ui.constant.IssueType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CitiIssueInfo {

    private boolean isShowDSMTSearch;

    private IssueType issueType;
    private String aeSearchWarning;
    private IssueStatus issueStatus;
    private List<IssueSubType> issueSubTypeList;

    @JsonIgnore
    List<Id> dsmtLinksToCopy;
    @JsonIgnore
    List<Id> dsmtLinksToUpdate;
    @JsonIgnore
    List<Id> auditableEntityToAssociate;
    @JsonIgnore
    private Set<String> ancestorAuditsList;
    @JsonIgnore
    private Set<String> ancestorControlsList;
    @JsonIgnore
    private Set<String> scopedInDSMTBaseIdList;
    @JsonIgnore
    private Map<String, String> scopedOutDSMTInfoMap;
    private List<AncestorControlsInfo> ancestorControlsInfoList;

    public IssueType getIssueType() {

        return issueType;
    }

    public void setIssueType(IssueType issueType) {

        this.issueType = issueType;
    }

    public IssueStatus getIssueStatus() {

        return issueStatus;
    }

    public void setIssueStatus(IssueStatus issueStatus) {

        this.issueStatus = issueStatus;
    }

    public boolean isShowDSMTSearch() {

        return isShowDSMTSearch;
    }

    public void setShowDSMTSearch(boolean showDSMTSearch) {

        isShowDSMTSearch = showDSMTSearch;
    }

    public List<IssueSubType> getIssueSubTypeList() {

        return issueSubTypeList;
    }

    public void setIssueSubTypeList(List<IssueSubType> issueSubTypeList) {

        this.issueSubTypeList = issueSubTypeList;
    }

    public Set<String> getAncestorControlsList() {

        return ancestorControlsList;
    }

    public void setAncestorControlsList(Set<String> ancestorControlsList) {

        this.ancestorControlsList = ancestorControlsList;
    }

    public List<AncestorControlsInfo> getAncestorControlsInfoList() {

        return ancestorControlsInfoList;
    }

    public void setAncestorControlsInfoList(List<AncestorControlsInfo> ancestorControlsInfoList) {

        this.ancestorControlsInfoList = ancestorControlsInfoList;
    }

    public String getAeSearchWarning() {

        return aeSearchWarning;
    }

    public void setAeSearchWarning(String aeSearchWarning) {

        this.aeSearchWarning = aeSearchWarning;
    }

    public Set<String> getAncestorAuditsList() {

        return ancestorAuditsList;
    }

    public void setAncestorAuditsList(Set<String> ancestorAuditsList) {

        this.ancestorAuditsList = ancestorAuditsList;
    }

    public Set<String> getScopedInDSMTBaseIdList() {

        return scopedInDSMTBaseIdList;
    }

    public void setScopedInDSMTBaseIdList(Set<String> scopedInDSMTBaseIdList) {

        this.scopedInDSMTBaseIdList = scopedInDSMTBaseIdList;
    }

    public Map<String, String> getScopedOutDSMTInfoMap() {

        return scopedOutDSMTInfoMap;
    }

    public void setScopedOutDSMTInfoMap(Map<String, String> scopedOutDSMTInfoMap) {

        this.scopedOutDSMTInfoMap = scopedOutDSMTInfoMap;
    }

    public List<Id> getDsmtLinksToCopy() {

        return dsmtLinksToCopy;
    }

    public void setDsmtLinksToCopy(List<Id> dsmtLinksToCopy) {

        this.dsmtLinksToCopy = dsmtLinksToCopy;
    }

    public List<Id> getDsmtLinksToUpdate() {

        return dsmtLinksToUpdate;
    }

    public void setDsmtLinksToUpdate(List<Id> dsmtLinksToUpdate) {

        this.dsmtLinksToUpdate = dsmtLinksToUpdate;
    }

    public List<Id> getAuditableEntityToAssociate() {

        return auditableEntityToAssociate;
    }

    public void setAuditableEntityToAssociate(List<Id> auditableEntityToAssociate) {

        this.auditableEntityToAssociate = auditableEntityToAssociate;
    }

    @Override
    public String toString() {

        return "CitiIssueInfo{" + "isShowDSMTSearch=" + isShowDSMTSearch + ", issueType=" + issueType +
                ", issueStatus=" + issueStatus + ", issueSubTypeList=" + issueSubTypeList +
                ", ancestorAuditsList=" + ancestorAuditsList +
                ", ancestorControlsList=" + ancestorControlsList +
                ", ancestorControlsInfoList=" + ancestorControlsInfoList +
                ", scopedInDSMTBaseIdList=" + scopedInDSMTBaseIdList +
                ", scopedOutDSMTInfoMap=" + scopedOutDSMTInfoMap +
                ", dsmtLinksToCopy=" + dsmtLinksToCopy +
                ", dsmtLinksToUpdate=" + dsmtLinksToUpdate +
                ", auditableEntityToAssociate=" + auditableEntityToAssociate +
                ", aeSearchWarning=" + aeSearchWarning+ '}';
    }
}
