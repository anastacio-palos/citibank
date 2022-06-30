package com.ibm.openpages.ext.ui.constant;

public enum IssueSubType {
    DEA_OET, GENERIC;

    public static boolean contains(String value) {
        for (IssueSubType issueSubTypes : values()) {
            if (issueSubTypes.name().equals(value))
                return true;
        }
        return false;
    }
}
