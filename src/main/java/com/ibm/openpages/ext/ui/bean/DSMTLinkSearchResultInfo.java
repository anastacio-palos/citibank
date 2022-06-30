package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import java.util.List;

public class DSMTLinkSearchResultInfo implements Serializable {

    private static final long serialVersionUID = 4510188131188551856L;
    private boolean disabled;
    
    private String id;
    private String name;
    private String level;
    private String region;
    private String nodeId;
    private String country;
    private String parentHierarchy;
    private List<String> hierarchy;
    
    /**
     * @return the disabled
     */
    public boolean isDisabled() {
    
        return disabled;
    }
    
    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
    
        this.disabled = disabled;
    }
    
    /**
     * @return the id
     */
    public String getId() {
    
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
    
        this.id = id;
    }
    
    /**
     * @return the name
     */
    public String getName() {
    
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
    
        this.name = name;
    }
    
    /**
     * @return the level
     */
    public String getLevel() {
    
        return level;
    }
    
    /**
     * @param level the level to set
     */
    public void setLevel(String level) {
    
        this.level = level;
    }
    
    /**
     * @return the region
     */
    public String getRegion() {
    
        return region;
    }
    
    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
    
        this.region = region;
    }
    
    /**
     * @return the nodeId
     */
    public String getNodeId() {
    
        return nodeId;
    }
    
    /**
     * @param nodeId the nodeId to set
     */
    public void setNodeId(String nodeId) {
    
        this.nodeId = nodeId;
    }
    
    /**
     * @return the country
     */
    public String getCountry() {
    
        return country;
    }
    
    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
    
        this.country = country;
    }
    
    /**
     * @return the parentHierarchy
     */
    public String getParentHierarchy() {
    
        return parentHierarchy;
    }
    
    /**
     * @param parentHierarchy the parentHierarchy to set
     */
    public void setParentHierarchy(String parentHierarchy) {
    
        this.parentHierarchy = parentHierarchy;
    }
    
    /**
     * @return the hierarchy
     */
    public List<String> getHierarchy() {
    
        return hierarchy;
    }
    
    /**
     * @param hierarchy the hierarchy to set
     */
    public void setHierarchy(List<String> hierarchy) {
    
        this.hierarchy = hierarchy;
    }

    @Override
    public String toString() {

        return "SearchDSMTLinkInfo [disabled=" + disabled + ", id=" + id + ", name=" + name + ", level=" + level
                + ", region=" + region + ", nodeId=" + nodeId + ", country=" + country + ", parentHierarchy="
                + parentHierarchy + ", hierarchy=" + hierarchy + "]";
    }
}
