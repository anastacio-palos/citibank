package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import java.util.List;

import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonDataGridHeaderInfo;

public class DSMTLinkObjectAssociationInfo implements Serializable {

	private static final long serialVersionUID = 6615831887744486965L;
	
	private boolean isEntityDisabled;
	
	private String auditableEntityId;
    private String entityDisabledInfo;
	private String auditableEntityName;
    private String auditableEntityDescription;
	
	private List<DSMTLinkObjectInfo> childDSMTLinkObjectsList;
    private List<DSMTLinkObjectInfo> availableDSMTLinkObjectsList;
    private List<CarbonDataGridHeaderInfo> childDSMTLinkHeaderInfoList;
    private List<CarbonDataGridHeaderInfo> availableDSMTLinkHeaderList;
    
    /**
     * @return the isEntityDisabled
     */
    public boolean isEntityDisabled() {
    
        return isEntityDisabled;
    }
    
    /**
     * @param isEntityDisabled the isEntityDisabled to set
     */
    public void setEntityDisabled(boolean isEntityDisabled) {
    
        this.isEntityDisabled = isEntityDisabled;
    }
    
    /**
     * @return the auditableEntityId
     */
    public String getAuditableEntityId() {
    
        return auditableEntityId;
    }
    
    /**
     * @param auditableEntityId the auditableEntityId to set
     */
    public void setAuditableEntityId(String auditableEntityId) {
    
        this.auditableEntityId = auditableEntityId;
    }
    
    /**
     * @return the entityDisabledInfo
     */
    public String getEntityDisabledInfo() {
    
        return entityDisabledInfo;
    }
    
    /**
     * @param entityDisabledInfo the entityDisabledInfo to set
     */
    public void setEntityDisabledInfo(String entityDisabledInfo) {
    
        this.entityDisabledInfo = entityDisabledInfo;
    }
    
    /**
     * @return the auditableEntityName
     */
    public String getAuditableEntityName() {
    
        return auditableEntityName;
    }
    
    /**
     * @param auditableEntityName the auditableEntityName to set
     */
    public void setAuditableEntityName(String auditableEntityName) {
    
        this.auditableEntityName = auditableEntityName;
    }
    
    /**
     * @return the auditableEntityDescription
     */
    public String getAuditableEntityDescription() {
    
        return auditableEntityDescription;
    }
    
    /**
     * @param auditableEntityDescription the auditableEntityDescription to set
     */
    public void setAuditableEntityDescription(String auditableEntityDescription) {
    
        this.auditableEntityDescription = auditableEntityDescription;
    }
    
    /**
     * @return the childDSMTLinkObjectsList
     */
    public List<DSMTLinkObjectInfo> getChildDSMTLinkObjectsList() {
    
        return childDSMTLinkObjectsList;
    }
    
    /**
     * @param childDSMTLinkObjectsList the childDSMTLinkObjectsList to set
     */
    public void setChildDSMTLinkObjectsList(List<DSMTLinkObjectInfo> childDSMTLinkObjectsList) {
    
        this.childDSMTLinkObjectsList = childDSMTLinkObjectsList;
    }
    
    /**
     * @return the availableDSMTLinkObjectsList
     */
    public List<DSMTLinkObjectInfo> getAvailableDSMTLinkObjectsList() {
    
        return availableDSMTLinkObjectsList;
    }
    
    /**
     * @param availableDSMTLinkObjectsList the availableDSMTLinkObjectsList to set
     */
    public void setAvailableDSMTLinkObjectsList(List<DSMTLinkObjectInfo> availableDSMTLinkObjectsList) {
    
        this.availableDSMTLinkObjectsList = availableDSMTLinkObjectsList;
    }
    
    /**
     * @return the childDSMTLinkHeaderInfoList
     */
    public List<CarbonDataGridHeaderInfo> getChildDSMTLinkHeaderInfoList() {
    
        return childDSMTLinkHeaderInfoList;
    }
    
    /**
     * @param childDSMTLinkHeaderInfoList the childDSMTLinkHeaderInfoList to set
     */
    public void setChildDSMTLinkHeaderInfoList(List<CarbonDataGridHeaderInfo> childDSMTLinkHeaderInfoList) {
    
        this.childDSMTLinkHeaderInfoList = childDSMTLinkHeaderInfoList;
    }
    
    /**
     * @return the availableDSMTLinkHeaderList
     */
    public List<CarbonDataGridHeaderInfo> getAvailableDSMTLinkHeaderList() {
    
        return availableDSMTLinkHeaderList;
    }
    
    /**
     * @param availableDSMTLinkHeaderList the availableDSMTLinkHeaderList to set
     */
    public void setAvailableDSMTLinkHeaderList(List<CarbonDataGridHeaderInfo> availableDSMTLinkHeaderList) {
    
        this.availableDSMTLinkHeaderList = availableDSMTLinkHeaderList;
    }

    @Override
    public String toString() {

        return "DSMTLinkObjectAssociationInfo [isEntityDisabled=" + isEntityDisabled + ", auditableEntityId="
                + auditableEntityId + ", entityDisabledInfo=" + entityDisabledInfo + ", auditableEntityName="
                + auditableEntityName + ", auditableEntityDescription=" + auditableEntityDescription
                + ", childDSMTLinkObjectsList=" + childDSMTLinkObjectsList + ", availableDSMTLinkObjectsList="
                + availableDSMTLinkObjectsList + ", childDSMTLinkHeaderInfoList=" + childDSMTLinkHeaderInfoList
                + ", availableDSMTLinkHeaderList=" + availableDSMTLinkHeaderList + "]";
    }
}
