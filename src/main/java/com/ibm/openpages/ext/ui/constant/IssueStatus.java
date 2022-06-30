package com.ibm.openpages.ext.ui.constant;

public enum IssueStatus {
    DRAFT, PENDING, PUBLISHED, NOT_DETERMINED;

    public static boolean contains(String value) {
        for (IssueStatus issueStatus : values()) {
            if (issueStatus.name().equals(value))
                return true;
        }
        return false;
    }
}
