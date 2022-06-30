package com.ibm.openpages.ext.ui.constant;

public enum IssueType {
    AUDIT, BMQS, AD_HOC, NOT_DETERMINED;

    public static boolean contains(String value) {
        for (IssueType issueTypes : values()) {
            if (issueTypes.name().equals(value))
                return true;
        }
        return false;
    }
}
