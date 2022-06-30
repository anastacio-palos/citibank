package com.ibm.openpages.ext.ui.bean;

import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;

import java.util.List;

import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_BASE_SETTING;

public class DSMTLinkHelperAppInfo {

    // The object id of the object from which the helper is launched
    private String objectID;

    // The object type of the object from which the helper is launched
    private String objectType;

    // The status of the Object from which the helper is launched
    private String objectStatus;

    // The scope of the Object from which the helper is launched
    private String objectScope;

    // The Localized label of the object type for the object from which the helper is launched
    private String objectTypeLabel;

    private String objectSearchText;

    private boolean hasAccess;

    private String message;

    private AuditMappingReportInfo auditMappingReportInfo;

    private CitiIssueInfo citiIssueInfo;

    // This combines the Base registry setting to the DSMT Helper and the Object type leading the object specific
    // registry setting
    private String objRegistrySetting;

    // TODO review why we need this
    private DSMTLinkSearchInfo dsmtSearchInfo;

    // Display the Basic Object information present below the header in all the DSMT Helpers
    private DSMTObjectGenericDetails helperObjectContentInfo;

    // Display the IBM Header for all DSMT helpers
    private CarbonHeaderInfo headerInfo;

    private List<CreateDSMTLinkForAudEntityInfo> duplicateDSMTLinksOnCreation;

    // The Scoping/ Descoping information used to update the DSMT object selected in the UI
    private List<DSMTLinkUpdateInfo> dsmtLinkUpdateInfo;

    private List<UpdateDSMTLinkInfo> updateDsmtLinkInfo;

    // Information for associating new DSMTS selected in the UI to the AE
    private List<CreateDSMTLinkForAudEntityInfo> associateNewDSMTInfoList;

    /**
     * @return the objectID
     */
    public String getObjectID() {

        return objectID;
    }

    /**
     * @param objectID
     *            the objectID to set
     */
    public void setObjectID(String objectID) {

        this.objectID = objectID;
    }

    /**
     * @return the objectType
     */
    public String getObjectType() {

        return objectType;
    }

    /**
     * @param objectType
     *            the objectType to set
     */
    public void setObjectType(String objectType) {

        this.objectType = objectType;
    }

    /**
     * @return the objectStatus
     */
    public String getObjectStatus() {

        return objectStatus;
    }

    /**
     * @param objectScope
     *            the objectScope to set
     */
    public void setObjectScope(String objectScope) {

        this.objectScope = objectScope;
    }

    /**
     * @return the objectScope
     */
    public String getObjectScope() {

        return objectScope;
    }

    /**
     * @param objectStatus
     *            the objectStatus to set
     */
    public void setObjectStatus(String objectStatus) {

        this.objectStatus = objectStatus;
    }

    /**
     * @return the objectTypeLabel
     */
    public String getObjectTypeLabel() {

        return objectTypeLabel;
    }

    /**
     * @param objectTypeLabel
     *            the objectTypeLabel to set
     */
    public void setObjectTypeLabel(String objectTypeLabel) {

        this.objectTypeLabel = objectTypeLabel;
    }

    /**
     * @return the dsmtSearchInfo
     */
    public DSMTLinkSearchInfo getDsmtSearchInfo() {

        return dsmtSearchInfo;
    }

    /**
     * @param dsmtSearchInfo
     *            the dsmtSearchInfo to set
     */
    public void setDsmtSearchInfo(DSMTLinkSearchInfo dsmtSearchInfo) {

        this.dsmtSearchInfo = dsmtSearchInfo;
    }

    /**
     * @return the helperObjectContentInfo
     */
    public DSMTObjectGenericDetails getHelperObjectContentInfo() {

        return helperObjectContentInfo;
    }

    /**
     * @param helperObjectContentInfo
     *            the helperObjectContentInfo to set
     */
    public void setHelperObjectContentInfo(DSMTObjectGenericDetails helperObjectContentInfo) {

        this.helperObjectContentInfo = helperObjectContentInfo;
    }

    /**
     * @return the headerInfo
     */
    public CarbonHeaderInfo getHeaderInfo() {

        return headerInfo;
    }

    /**
     * @param headerInfo
     *            the headerInfo to set
     */
    public void setHeaderInfo(CarbonHeaderInfo headerInfo) {

        this.headerInfo = headerInfo;
    }

    /**
     * @return the duplicateDSMTLinksOnCreation
     */
    public List<CreateDSMTLinkForAudEntityInfo> getDuplicateDSMTLinksOnCreation() {

        return duplicateDSMTLinksOnCreation;
    }

    /**
     * @param duplicateDSMTLinksOnCreation
     *            the duplicateDSMTLinksOnCreation to set
     */
    public void setDuplicateDSMTLinksOnCreation(List<CreateDSMTLinkForAudEntityInfo> duplicateDSMTLinksOnCreation) {

        this.duplicateDSMTLinksOnCreation = duplicateDSMTLinksOnCreation;
    }

    /**
     * @return the dsmtLinkUpdateInfo
     */
    public List<DSMTLinkUpdateInfo> getDsmtLinkUpdateInfo() {

        return dsmtLinkUpdateInfo;
    }

    /**
     * @param dsmtLinkUpdateInfo
     *            the dsmtLinkUpdateInfo to set
     */
    public void setDsmtLinkUpdateInfo(List<DSMTLinkUpdateInfo> dsmtLinkUpdateInfo) {

        this.dsmtLinkUpdateInfo = dsmtLinkUpdateInfo;
    }

    /**
     * @return the updateDsmtLinkInfo
     */
    public List<UpdateDSMTLinkInfo> getUpdateDsmtLinkInfo() {

        return updateDsmtLinkInfo;
    }

    /**
     * @param updateDsmtLinkInfo
     *            the updateDsmtLinkInfo to set
     */
    public void setUpdateDsmtLinkInfo(List<UpdateDSMTLinkInfo> updateDsmtLinkInfo) {

        this.updateDsmtLinkInfo = updateDsmtLinkInfo;
    }

    /**
     * @return the associateNewDSMTInfoList
     */
    public List<CreateDSMTLinkForAudEntityInfo> getAssociateNewDSMTInfoList() {

        return associateNewDSMTInfoList;
    }

    /**
     * @param associateNewDSMTInfoList
     *            the associateNewDSMTInfoList to set
     */
    public void setAssociateNewDSMTInfoList(List<CreateDSMTLinkForAudEntityInfo> associateNewDSMTInfoList) {

        this.associateNewDSMTInfoList = associateNewDSMTInfoList;
    }

    /**
     * @return the objRegistrySetting
     */
    public String getObjRegistrySetting() {

        return DSMT_LINK_APP_BASE_SETTING + getObjectTypeLabel();
    }

    public String getObjectSearchText() {

        return objectSearchText;
    }

    public void setObjectSearchText(String objectSearchText) {

        this.objectSearchText = objectSearchText;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuditMappingReportInfo getAuditMappingReportInfo() {

        return auditMappingReportInfo;
    }

    public void setAuditMappingReportInfo(AuditMappingReportInfo auditMappingReportInfo) {

        this.auditMappingReportInfo = auditMappingReportInfo;
    }

    public CitiIssueInfo getCitiIssueInfo() {

        return citiIssueInfo;
    }

    public void setCitiIssueInfo(CitiIssueInfo citiIssueInfo) {

        this.citiIssueInfo = citiIssueInfo;
    }

    @Override
    public String toString() {
        return "DSMTLinkHelperAppInfo [objectID=" + objectID + ", objectType=" + objectType + ", objectStatus="
                + objectStatus + ", objectScope=" + objectScope + ", objectTypeLabel=" + objectTypeLabel
                + ", objectSearchText=" + objectSearchText + ", hasAccess=" + hasAccess + ", message=" + message
                + ", auditMappingReportInfo=" + auditMappingReportInfo + ", citiIssueInfo=" + citiIssueInfo
                + ", objRegistrySetting=" + objRegistrySetting + ", dsmtSearchInfo=" + dsmtSearchInfo
                + ", helperObjectContentInfo=" + helperObjectContentInfo + ", headerInfo=" + headerInfo
                + ", duplicateDSMTLinksOnCreation=" + duplicateDSMTLinksOnCreation + ", dsmtLinkUpdateInfo="
                + dsmtLinkUpdateInfo + ", updateDsmtLinkInfo=" + updateDsmtLinkInfo + ", associateNewDSMTInfoList="
                + associateNewDSMTInfoList + "]";
    }


}
