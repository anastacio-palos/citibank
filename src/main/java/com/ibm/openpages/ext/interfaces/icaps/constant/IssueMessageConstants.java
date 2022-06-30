package com.ibm.openpages.ext.interfaces.icaps.constant;

import com.ibm.openpages.ext.interfaces.common.constant.MessageConstants;

public class IssueMessageConstants extends MessageConstants {

    public static final String SUCCESS_CREATED_ISSUE = "Issue {0} created";
    public static final String SUCCESS_UPDATED_ISSUE = "Issue {0} updated";

    public static final String SUCCESS_CREATED_CAP = "CAP {0} created successfully";
    public static final String SUCCESS_UPDATED_CAP = "CAP {0} updated successfully";

    public static final String CAP_ASSIGNED_OTHER_ISSUE = "[SKIPPED] CAP {0} is already assigned to another Issue, please validate CAP Name is correct.";
    public static final String AUDIT_PLAN_NOT_FOUND = "Audit Plan Not Found for issue {0}";
    public static final String BUSINESS_ENTITY_NOT_FOUND = "Business Entity Not Found for issue {0}";
    public static final String INVALID_INPUT_VALUE = "Input value <{0}> is not valid for property <{1}> from Item {2}";
    public static final String INVALID_INPUT_COMBINATION = "Input combination <{0}> and <{1}> are not valid for Item {2}.";

    public static final String INVALID_USER_FOR_ITEM = "User <{0}> does not exist, please validate information provided for field {1}.";
    public static final String USER_IS_LOCKED = "The user {0} is locked or inactive.";

    public static final String INPUT_NOT_DEFINED = "Value or property <{0}> not defined in registry file";
    public static final String VERIFY_JSON_FORMAT_IS_CORRECT = "Validate Json Format is correct.";
    public static final String CAP_DUPLICATED_IN_SAME_TRANSACTION = "CAP {0} is duplicated. Skipping CAP";
}
