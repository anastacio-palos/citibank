package com.ibm.openpages.ext.ui.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;

import static com.ibm.openpages.ext.ui.constant.ContextHelperConstants.*;

public class ContextHelperAppInfo {

    // The object id of the object from which the helper is launched
    private String objectID;

    // The object type of the object from which the helper is launched
    private String objectType;

    // The object type localized label of the object from which the helper is launched
    private String localizedLabel;

    private  String rootObject;

    private  String rootObjectPath;

    private  String objectToDisplay;

    private  String objectPathFromRoot;

    private  String columnFieldName;

    private  String filterFields;
 
    private String rootObjName;
    private String rootObjContextPath;
    private String rootObjitResourceId;

    // Display the IBM Header for all DSMT helperss
    private CarbonHeaderInfo headerInfo;

    // Display the Basic Object information present below the header in all the DSMT Helpers
    private DSMTObjectGenericDetails helperObjectContentInfo;

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectType() {
        return objectType;
    }

    public CarbonHeaderInfo getHeaderInfo() {
        return headerInfo;
    }

    public void setHeaderInfo(CarbonHeaderInfo headerInfo) {
        this.headerInfo = headerInfo;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getLocalizedLabel() {
        return localizedLabel;
    }

    public void setLocalizedLabel(String localizedLabel) {
        this.localizedLabel = localizedLabel;
    }

    public String getObjectRegistrySetting() {
        return ROOT_SETTING + localizedLabel;
    }

    public DSMTObjectGenericDetails getHelperObjectContentInfo() {
        return helperObjectContentInfo;
    }

    public void setHelperObjectContentInfo(DSMTObjectGenericDetails helperObjectContentInfo) {
        this.helperObjectContentInfo = helperObjectContentInfo;
    }

    public String getRootObject() {
        return rootObject;
    }

    public void setRootObject(String rootObject) {
        this.rootObject = rootObject;
    }

    public String getRootObjectPath() {
        return rootObjectPath;
    }

    public void setRootObjectPath(String rootObjectPath) {
        this.rootObjectPath = rootObjectPath;
    }

    public String getObjectToDisplay() {
        return objectToDisplay;
    }

    public void setObjectToDisplay(String objectToDisplay) {
        this.objectToDisplay = objectToDisplay;
    }

    public String getObjectPathFromRoot() {
        return objectPathFromRoot;
    }

    public void setObjectPathFromRoot(String objectPathFromRoot) {
        this.objectPathFromRoot = objectPathFromRoot;
    }

    public String getColumnFieldName() {
        return columnFieldName;
    }

    public void setColumnFieldName(String columnFieldName) {
        this.columnFieldName = columnFieldName;
    }

    public String getFilterFields() {
        return filterFields;
    }

    public void setFilterFields(String filterFields) {
        this.filterFields = filterFields;
    }

    public String getRootObjName() {
		return rootObjName;
	}

	public void setRootObjName(String rootObjName) {
		this.rootObjName = rootObjName;
	}

	public String getRootObjContextPath() {
		return rootObjContextPath;
	}

	public void setRootObjContextPath(String rootObjContextPath) {
		this.rootObjContextPath = rootObjContextPath;
	}

	public String getRootObjResourceId() {
		return rootObjitResourceId;
	}

	public void setRootObjResourceId(String rootObjResourceId) {
		this.rootObjitResourceId = rootObjResourceId;
	}

	@Override
    public String toString() {
        return "ContextHelperAppInfo{" +
                "objectID='" + objectID + '\'' +
                ", objectType='" + objectType + '\'' +
                ", localizedLabel='" + localizedLabel + '\'' +
                ", objRegistrySetting='" + ROOT_SETTING + localizedLabel + '\'' +
                ", rootObject='" + rootObject + '\'' +
                ", rootObjectPath='" + rootObjectPath + '\'' +
                ", objectToDisplay='" + objectToDisplay + '\'' +
                ", objectPathFromRoot='" + objectPathFromRoot + '\'' +
                ", columnFieldName='" + columnFieldName + '\'' +
                ", filterFields='" + filterFields + '\'' +
                ", rootObjName='" + rootObjName + '\'' +
                ", rootObjContextPath='" + rootObjContextPath + '\'' +
                ", rootObjResourceId='" + rootObjitResourceId + '\'' +
                ", headerInfo=" + headerInfo +
                ", helperObjectContentInfo=" + helperObjectContentInfo +
                '}';
    }
}
