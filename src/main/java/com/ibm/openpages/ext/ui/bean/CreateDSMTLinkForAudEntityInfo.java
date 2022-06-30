package com.ibm.openpages.ext.ui.bean;

import java.util.List;

public class CreateDSMTLinkForAudEntityInfo {

    private boolean outOfRange; // Flag stating if the DSMT is out of range

    private String rationale; // A rationale message that needs to be set in the newly created object
    private String dsmtRegion; // The DSMT region that needs to be set in the newly created object
    private String dsmtCountry; // The DSMT Country that needs to be set in the newly created object
    private String homeOrImpacted; // The type of the DSMT - home or impacted
    private String dsmtResourceId; // The resource ID of the DSMT Object
    private String existingDSMTResourceId; // The resource ID of the existing DSMT Object

    private String legalVehicleId; // Legal Vehicle Node Id
    private String legalVehicleName; // Legal Vehicle Node Name
    private String legalVehicleLevel; // Legal Vehicle Level

    private String managedSegmentId; // Managed Segment IId
    private String managedSegmentName; // Managed Segment Node Name
    private String managedSegmentLevel; // Managed Segment Level
    private String managedSegmentHierarcy; // Managed Segment Hierarchy

    private String managedGeographyId; // Managed Segment Id
    private String managedGeogprahyName; // Managed Segment Name
    private String managedGeographyLevel; // Managed Segment Level

    private List<String> outOfRangeNodes; // A list of Nodes that are out of range

    /**
     * @return the outOfRange
     */
    public boolean isOutOfRange() {

        return outOfRange;
    }

    /**
     * @param outOfRange
     *            the outOfRange to set
     */
    public void setOutOfRange(boolean outOfRange) {

        this.outOfRange = outOfRange;
    }

    /**
     * @return the rationale
     */
    public String getRationale() {

        return rationale;
    }

    /**
     * @param rationale
     *            the rationale to set
     */
    public void setRationale(String rationale) {

        this.rationale = rationale;
    }

    /**
     * @return the dsmtRegion
     */
    public String getDsmtRegion() {

        return dsmtRegion;
    }

    /**
     * @param dsmtRegion
     *            the dsmtRegion to set
     */
    public void setDsmtRegion(String dsmtRegion) {

        this.dsmtRegion = dsmtRegion;
    }

    /**
     * @return the dsmtCountry
     */
    public String getDsmtCountry() {

        return dsmtCountry;
    }

    /**
     * @param dsmtCountry
     *            the dsmtCountry to set
     */
    public void setDsmtCountry(String dsmtCountry) {

        this.dsmtCountry = dsmtCountry;
    }

    /**
     * @return the homeOrImpacted
     */
    public String getHomeOrImpacted() {

        return homeOrImpacted;
    }

    /**
     * @param homeOrImpacted
     *            the homeOrImpacted to set
     */
    public void setHomeOrImpacted(String homeOrImpacted) {

        this.homeOrImpacted = homeOrImpacted;
    }

    /**
     * @return the dsmtResourceId
     */
    public String getDsmtResourceId() {

        return dsmtResourceId;
    }

    /**
     * @param dsmtResourceId
     *            the dsmtResourceId to set
     */
    public void setDsmtResourceId(String dsmtResourceId) {

        this.dsmtResourceId = dsmtResourceId;
    }

    /**
     * @return the legalVehicleId
     */
    public String getLegalVehicleId() {

        return legalVehicleId;
    }

    /**
     * @param legalVehicleId
     *            the legalVehicleId to set
     */
    public void setLegalVehicleId(String legalVehicleId) {

        this.legalVehicleId = legalVehicleId;
    }

    /**
     * @return the legalVehicleName
     */
    public String getLegalVehicleName() {

        return legalVehicleName;
    }

    /**
     * @param legalVehicleName
     *            the legalVehicleName to set
     */
    public void setLegalVehicleName(String legalVehicleName) {

        this.legalVehicleName = legalVehicleName;
    }

    /**
     * @return the legalVehicleLevel
     */
    public String getLegalVehicleLevel() {

        return legalVehicleLevel;
    }

    /**
     * @param legalVehicleLevel
     *            the legalVehicleLevel to set
     */
    public void setLegalVehicleLevel(String legalVehicleLevel) {

        this.legalVehicleLevel = legalVehicleLevel;
    }

    /**
     * @return the managedSegmentId
     */
    public String getManagedSegmentId() {

        return managedSegmentId;
    }

    /**
     * @param managedSegmentId
     *            the managedSegmentId to set
     */
    public void setManagedSegmentId(String managedSegmentId) {

        this.managedSegmentId = managedSegmentId;
    }

    /**
     * @return the managedSegmentName
     */
    public String getManagedSegmentName() {

        return managedSegmentName;
    }

    /**
     * @param managedSegmentName
     *            the managedSegmentName to set
     */
    public void setManagedSegmentName(String managedSegmentName) {

        this.managedSegmentName = managedSegmentName;
    }

    /**
     * @return the managedSegmentLevel
     */
    public String getManagedSegmentLevel() {

        return managedSegmentLevel;
    }

    /**
     * @param managedSegmentLevel
     *            the managedSegmentLevel to set
     */
    public void setManagedSegmentLevel(String managedSegmentLevel) {

        this.managedSegmentLevel = managedSegmentLevel;
    }

    /**
     * @return the managedSegmentHierarcy
     */
    public String getManagedSegmentHierarcy() {

        return managedSegmentHierarcy;
    }

    /**
     * @param managedSegmentHierarcy
     *            the managedSegmentHierarcy to set
     */
    public void setManagedSegmentHierarcy(String managedSegmentHierarcy) {

        this.managedSegmentHierarcy = managedSegmentHierarcy;
    }

    /**
     * @return the managedGeographyId
     */
    public String getManagedGeographyId() {

        return managedGeographyId;
    }

    /**
     * @param managedGeographyId
     *            the managedGeographyId to set
     */
    public void setManagedGeographyId(String managedGeographyId) {

        this.managedGeographyId = managedGeographyId;
    }

    /**
     * @return the managedGeogprahyName
     */
    public String getManagedGeogprahyName() {

        return managedGeogprahyName;
    }

    /**
     * @param managedGeogprahyName
     *            the managedGeogprahyName to set
     */
    public void setManagedGeogprahyName(String managedGeogprahyName) {

        this.managedGeogprahyName = managedGeogprahyName;
    }

    /**
     * @return the managedGeographyLevel
     */
    public String getManagedGeographyLevel() {

        return managedGeographyLevel;
    }

    /**
     * @param managedGeographyLevel
     *            the managedGeographyLevel to set
     */
    public void setManagedGeographyLevel(String managedGeographyLevel) {

        this.managedGeographyLevel = managedGeographyLevel;
    }

    /**
     * @return the outOfRangeNodes
     */
    public List<String> getOutOfRangeNodes() {

        return outOfRangeNodes;
    }

    /**
     * @param outOfRangeNodes
     *            the outOfRangeNodes to set
     */
    public void setOutOfRangeNodes(List<String> outOfRangeNodes) {

        this.outOfRangeNodes = outOfRangeNodes;
    }

    public String getExistingDSMTResourceId() {

        return existingDSMTResourceId;
    }

    public void setExistingDSMTResourceId(String existingDSMTResourceId) {

        this.existingDSMTResourceId = existingDSMTResourceId;
    }

    @Override
    public String toString() {

        return "DSMTLinkForAssociateAuditableEntityInfo [outOfRange=" + outOfRange + ", rational=" + rationale
                + ", dsmtRegion=" + dsmtRegion + ", dsmtCountry=" + dsmtCountry + ", homeOrImpacted=" + homeOrImpacted
                + ", dsmtResourceId=" + dsmtResourceId + ", existingDSMTResourceId=" + existingDSMTResourceId  + ", "
                + ", legalVehicleId=" + legalVehicleId + ", legalVehicleName="
                + legalVehicleName + ", legalVehicleLevel=" + legalVehicleLevel + ", managedSegmentId="
                + managedSegmentId + ", managedSegmentName=" + managedSegmentName + ", managedSegmentLevel="
                + managedSegmentLevel + ", managedSegmentHierarcy=" + managedSegmentHierarcy + ", managedGeographyId="
                + managedGeographyId + ", managedGeogprahyName=" + managedGeogprahyName + ", managedGeographyLevel="
                + managedGeographyLevel + ", outOfRangeNodes=" + outOfRangeNodes + "]";
    }
}
