package com.ibm.openpages.ext.ui.constant;

public enum AuditMappingReportType {

    AUDIT_NON_IER, AUDIT_IER, BMQS_NON_IER, BMQS_IER, NOT_DETERMINED;

    public static boolean contains(String value) {
        for (AuditMappingReportType auditMappingReportType : values()) {
            if (auditMappingReportType.name().equals(value))
                return true;
        }
        return false;
    }
}
