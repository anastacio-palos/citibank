package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;

public class DSMTLinkObjectInfo implements Serializable {

	private static final long serialVersionUID = -7607313021558226858L;

	private boolean isDSMTLinkDisabled;
	
	private String type;
    private String status;
	private String scope;
	private String baseId;
	private String active;
	
    private String dsmtLinkId;
    private String dsmtLinkName;
    private String homeImpacted;
    private String dsmtdescription;
    private String auditableEntityId;
    private String auditableEntityName;

    private DSMTLinkDisableInfo dsmtLinkDisableInfo;

    /**
     * @return the isDSMTLinkDisabled
     */
    public boolean isDSMTLinkDisabled() {
    
        return isDSMTLinkDisabled;
    }
    
    /**
     * @param isDSMTLinkDisabled the isDSMTLinkDisabled to set
     */
    public void setDSMTLinkDisabled(boolean isDSMTLinkDisabled) {
    
        this.isDSMTLinkDisabled = isDSMTLinkDisabled;
    }
    
    /**
     * @return the type
     */
    public String getType() {
    
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
    
        this.type = type;
    }
    
    /**
     * @return the status
     */
    public String getStatus() {
    
        return status;
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
    
        this.status = status;
    }
    
    /**
     * @return the scope
     */
    public String getScope() {
    
        return scope;
    }
    
    /**
     * @param scope the scope to set
     */
    public void setScope(String scope) {
    
        this.scope = scope;
    }
    
    /**
     * @return the baseId
     */
    public String getBaseId() {
    
        return baseId;
    }
    
    /**
     * @param baseId the baseId to set
     */
    public void setBaseId(String baseId) {
    
        this.baseId = baseId;
    }
    
    /**
     * @return the active
     */
    public String getActive() {
    
        return active;
    }
    
    /**
     * @param active the active to set
     */
    public void setActive(String active) {
    
        this.active = active;
    }
    
    /**
     * @return the dsmtLinkId
     */
    public String getDsmtLinkId() {
    
        return dsmtLinkId;
    }
    
    /**
     * @param dsmtLinkId the dsmtLinkId to set
     */
    public void setDsmtLinkId(String dsmtLinkId) {
    
        this.dsmtLinkId = dsmtLinkId;
    }
    
    /**
     * @return the dsmtLinkName
     */
    public String getDsmtLinkName() {
    
        return dsmtLinkName;
    }
    
    /**
     * @param dsmtLinkName the dsmtLinkName to set
     */
    public void setDsmtLinkName(String dsmtLinkName) {
    
        this.dsmtLinkName = dsmtLinkName;
    }
    
    /**
     * @return the homeImpacted
     */
    public String getHomeImpacted() {
    
        return homeImpacted;
    }
    
    /**
     * @param homeImpacted the homeImpacted to set
     */
    public void setHomeImpacted(String homeImpacted) {
    
        this.homeImpacted = homeImpacted;
    }
    
    /**
     * @return the dsmtdescription
     */
    public String getDsmtdescription() {
    
        return dsmtdescription;
    }
    
    /**
     * @param dsmtdescription the dsmtdescription to set
     */
    public void setDsmtdescription(String dsmtdescription) {
    
        this.dsmtdescription = dsmtdescription;
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
     * @return the dsmtLinkDisableInfo
     */
    public DSMTLinkDisableInfo getDsmtLinkDisableInfo() {
    
        return dsmtLinkDisableInfo;
    }
    
    /**
     * @param dsmtLinkDisableInfo the dsmtLinkDisableInfo to set
     */
    public void setDsmtLinkDisableInfo(DSMTLinkDisableInfo dsmtLinkDisableInfo) {
    
        this.dsmtLinkDisableInfo = dsmtLinkDisableInfo;
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

    @Override
    public String toString() {

        return "DSMTLinkObjectInfo [isDSMTLinkDisabled=" + isDSMTLinkDisabled + ", type=" + type + ", status=" + status
                + ", scope=" + scope + ", baseId=" + baseId + ", active=" + active + ", dsmtLinkId=" + dsmtLinkId
                + ", dsmtLinkName=" + dsmtLinkName + ", homeImpacted=" + homeImpacted + ", dsmtdescription="
                + dsmtdescription + ", auditableEntityId=" + auditableEntityId + ", dsmtLinkDisableInfo="
                + dsmtLinkDisableInfo + ", auditableEntityName=" + auditableEntityName + "]";
    }
}
