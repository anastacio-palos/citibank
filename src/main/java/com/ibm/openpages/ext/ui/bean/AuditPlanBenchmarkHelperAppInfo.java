package com.ibm.openpages.ext.ui.bean;

import com.ibm.openpages.ext.tss.helpers.react.carbon.bean.CarbonHeaderInfo;

public class AuditPlanBenchmarkHelperAppInfo {

    // The object id of the object from which the helper is launched
    private String objectID;

    // The object type of the object from which the helper is launched
    private String objectType;

    // The object type localized label of the object from which the helper is launched
    private String localizedLabel;
 
    private String benchmarkYear;

    private boolean hasAccess;

    private boolean isBenchmarkYearAvailable;

    private String message;

    // Display the IBM Header info
    private CarbonHeaderInfo headerInfo;

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

    public String getBenchmarkYear() {
        return benchmarkYear;
    }

    public void setBenchmarkYear(String benchmarkYear) {
        this.benchmarkYear = benchmarkYear;
    }

    public boolean isHasAccess()
    {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess)
    {
        this.hasAccess = hasAccess;
    }

    public boolean isBenchmarkYearAvailable()
    {
        return isBenchmarkYearAvailable;
    }

    public void setBenchmarkYearAvailable(boolean isBenchmarkYearAvailable)
    {
        this.isBenchmarkYearAvailable = isBenchmarkYearAvailable;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public DSMTObjectGenericDetails getHelperObjectContentInfo() {
        return helperObjectContentInfo;
    }

    public void setHelperObjectContentInfo(DSMTObjectGenericDetails helperObjectContentInfo) {
        this.helperObjectContentInfo = helperObjectContentInfo;
    }

    @Override
    public String toString() {
        return "AuditPlanBenchmarkHelperAppInfo{" +
                "objectID='" + objectID + '\'' +
                ", objectType='" + objectType + '\'' +
                ", localizedLabel='" + localizedLabel + '\'' +
                ", benchmarkYear=" + benchmarkYear +
                ", hasAccess=" + hasAccess +
                ", isBenchmarkYearAvailable=" + isBenchmarkYearAvailable +
                ", message=" + message +
                ", headerInfo=" + headerInfo +
                ", helperObjectContentInfo=" + helperObjectContentInfo +
                '}';
    }
}
