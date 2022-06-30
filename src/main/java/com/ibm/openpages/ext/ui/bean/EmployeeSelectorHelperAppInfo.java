/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * © Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;

public class EmployeeSelectorHelperAppInfo implements Serializable {

    private static final long serialVersionUID = -822334701066212439L;

    // The object id of the object from which the helper is launched
    private String objectID;

    // The object type of the object from which the helper is launched
    private String objectType;

    private String objectName;

    private String objectDescription;

    // The status of the Object from which the helper is launched
    private String objectStatus;

    // The Localized label of the object type for the object from which the helper
    // is launched
    private String objectTypeLabel;

    private OMUDsmtRoot omuDsmtRoot;

    // Scorecard Executive field info
    private String scorecardExeInfo;

    // Responsible executive title text box info
    private String responsibleExeTitle;

    private String executionType;

    // Display the IBM Header for all helpers
    private CarbonHeaderInfo headerInfo;

    // Display the Basic Object information present below the header in Employee Helper
    private EmployeeSelectorObjectGenericDetails helperObjectContentInfo;

    // This combines the Base registry setting to the Employee Selector Helper and the Object type leading the object
    // specific registry setting
    private String objRegistrySetting;

    private Employee employee;

    private EmployeeHRDSMTInfo employeeHRDSMTInfo;

    private Validation validation;

    private boolean hasAccess;

    private String message;

    private boolean isOMU;

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

    public String getObjectName() {
    
        return objectName;
    }

    
    public void setObjectName(String objectName) {
    
        this.objectName = objectName;
    }

    
    public String getObjectDescription() {
    
        return objectDescription;
    }

    
    public void setObjectDescription(String objectDescription) {
    
        this.objectDescription = objectDescription;
    }

    /**
     * @return the objectStatus
     */
    public String getObjectStatus() {

        return objectStatus;
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
     * @return the omuDsmtRoot
     */
    public OMUDsmtRoot getOMUDsmtRoot() {

        return omuDsmtRoot;
    }

    /**
     * @param omuDsmtRoot
     *            the omuDsmtRoot to set
     */
    public void setOMUDsmtRoot(OMUDsmtRoot omuDsmtRoot) {

        this.omuDsmtRoot = omuDsmtRoot;
    }

    public String getScorecardExeInfo() {

        return scorecardExeInfo;
    }

    public void setScorecardExeInfo(String scorecardExeInfo) {

        this.scorecardExeInfo = scorecardExeInfo;
    }

    public String getResponsibleExeTitle() {
    
        return responsibleExeTitle;
    }

    public void setResponsibleExeTitle(String responsibleExeTitle) {
    
        this.responsibleExeTitle = responsibleExeTitle;
    }

    /**
     * @return the executionType
     */
    public String getExecutionType() {

        return executionType;
    }

    /**
     * @param executionType
     *            the executionType to set
     */
    public void setExecutionType(String executionType) {

        this.executionType = executionType;
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
     * @return the helperObjectContentInfo
     */
    public EmployeeSelectorObjectGenericDetails getHelperObjectContentInfo() {

        return helperObjectContentInfo;
    }

    /**
     * @param helperObjectContentInfo
     *            the helperObjectContentInfo to set
     */
    public void setHelperObjectContentInfo(EmployeeSelectorObjectGenericDetails helperObjectContentInfo) {

        this.helperObjectContentInfo = helperObjectContentInfo;
    }

    /**
     * @return the objRegistrySetting
     */
    public String getObjRegistrySetting() {

        return objRegistrySetting;
    }

    /**
     * @param objRegistrySetting
     *            the objRegistrySetting to set
     */
    public void setObjRegistrySetting(String objRegistrySetting) {

        this.objRegistrySetting = objRegistrySetting;
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {

        return employee;
    }

    /**
     * @param employee
     *            the employee to set
     */
    public void setEmployee(Employee employee) {

        this.employee = employee;
    }

    /**
     * @return the employeeHRDSMTInfo
     */
    public EmployeeHRDSMTInfo getEmployeeHRDSMTInfo() {

        return employeeHRDSMTInfo;
    }

    /**
     * @param employeeHRDSMTInfo
     *            the employeeHRDSMTInfo to set
     */
    public void setEmployeeHRDSMTInfo(EmployeeHRDSMTInfo employeeHRDSMTInfo) {

        this.employeeHRDSMTInfo = employeeHRDSMTInfo;
    }



    
    public OMUDsmtRoot getOmuDsmtRoot() {
    
        return omuDsmtRoot;
    }

    
    public void setOmuDsmtRoot(OMUDsmtRoot omuDsmtRoot) {
    
        this.omuDsmtRoot = omuDsmtRoot;
    }

    
    public Validation getValidation() {
    
        return validation;
    }

    
    public void setValidation(Validation validation) {
    
        this.validation = validation;
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

    public boolean isOMU() {
    
        return isOMU;
    }

    public void setOMU(boolean isOMU) {
    
        this.isOMU = isOMU;
    }

    @Override
    public String toString() {

        return "EmployeeSelectorHelperAppInfo [objectID=" + objectID + ", objectType=" + objectType + ", objectName="
                + objectName + ", objectDescription=" + objectDescription + ", objectStatus=" + objectStatus
                + ", objectTypeLabel=" + objectTypeLabel + ", omuDsmtRoot=" + omuDsmtRoot + ", scorecardExeInfo="
                + scorecardExeInfo + ", responsibleExeTitle=" + responsibleExeTitle + ", executionType=" + executionType
                + ", headerInfo=" + headerInfo + ", helperObjectContentInfo=" + helperObjectContentInfo
                + ", objRegistrySetting=" + objRegistrySetting + ", employee=" + employee + ", employeeHRDSMTInfo="
                + employeeHRDSMTInfo + ", validation=" + validation + ", hasAccess=" + hasAccess + ", message="
                + message + ", isOMU=" + isOMU + "]";
    }

}
