package com.ibm.openpages.ext.ui.bean;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.ext.ui.constant.AuditMappingReportStatus;
import com.ibm.openpages.ext.ui.constant.AuditMappingReportType;

public class AuditMappingReportInfo {

    public List<String> getIssueDirectlyAssociatedToAMR() {
		return issueDirectlyAssociatedToAMR;
	}

	public void setIssueDirectlyAssociatedToAMR(List<String> issueDirectlyAssociatedToAMR) {
		this.issueDirectlyAssociatedToAMR = issueDirectlyAssociatedToAMR;
	}

	private AuditMappingReportType auditMappingReportType;
    private AuditMappingReportStatus auditMappingReportStatus;
    @JsonIgnore
    private Set<String> scopedInDSMTBaseIdList;
    @JsonIgnore
    private Map<String, String> scopedOutDSMTInfoMap;
    @JsonIgnore
    List<Id> dsmtLinksToCopy;
    @JsonIgnore
    List<Id> dsmtLinksToUpdate;
    @JsonIgnore
    List<Id> auditableEntityToAssociate;
    @JsonIgnore
    List<String> ancestorAuditIdList;
    @JsonIgnore
    List<String> issueDirectlyAssociatedToAMR;

    public List<String> getAncestorAuditIdList() {
		return ancestorAuditIdList;
	}

	public void setAncestorAuditIdList(List<String> ancestorAuditIdList) {
		this.ancestorAuditIdList = ancestorAuditIdList;
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

	public AuditMappingReportType getAuditMappingReportType() {

        return auditMappingReportType;
    }

    public void setAuditMappingReportType(AuditMappingReportType auditMappingReportType) {

        this.auditMappingReportType = auditMappingReportType;
    }

    public AuditMappingReportStatus getAuditMappingReportStatus() {

        return auditMappingReportStatus;
    }

    public void setAuditMappingReportStatus(AuditMappingReportStatus auditMappingReportStatus) {

        this.auditMappingReportStatus = auditMappingReportStatus;
    }

	@Override
	public String toString() {
		return "AuditMappingReportInfo [auditMappingReportType=" + auditMappingReportType
				+ ", auditMappingReportStatus=" + auditMappingReportStatus + ", scopedInDSMTBaseIdList="
				+ scopedInDSMTBaseIdList + ", scopedOutDSMTInfoMap=" + scopedOutDSMTInfoMap + ", dsmtLinksToCopy="
				+ dsmtLinksToCopy + ", dsmtLinksToUpdate=" + dsmtLinksToUpdate + ", auditableEntityToAssociate="
				+ auditableEntityToAssociate + ", ancestorAuditIdList=" + ancestorAuditIdList
				+ ", issueDirectlyAssociatedToAMR=" + issueDirectlyAssociatedToAMR + "]";
	}
}
