package com.ibm.openpages.ext.ui.constant;


//TODO delete this enum and also make sure its reference is removed from the gradle file
public enum AuditMappingReportStatus {
    DRAFT, SUBMITTED, OTHER, NOT_DETERMINED;

    public static boolean contains(String value) {
        for (AuditMappingReportStatus auditMappingReportStatus : values()) {
            if (auditMappingReportStatus.name().equals(value))
                return true;
        }
        return false;
    }

}
