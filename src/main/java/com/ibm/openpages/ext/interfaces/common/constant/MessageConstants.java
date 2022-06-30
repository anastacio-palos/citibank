package com.ibm.openpages.ext.interfaces.common.constant;

/**
 * <p>
 * This class contains the message constants used by the validation engine.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 10-07-2021
 */
public class MessageConstants {
    public final static String ERROR_FIELD_DEPENDENCY = "Field Dependency Error {0}.";
    public static final String ERROR_FIELD_CONSTRAINT = "Invalid {0} specified: {1}.";
    public final static String ERROR_INVALID_FORMAT = "Required property {0} has the wrong value or is missing. Only {1} characters are valid. Request invalid.";
    public final static String ERROR_INVALID_MAX_SIZE = "Invalid Maximum Size {0}.";
    public final static String ERROR_INVALID_MIN_SIZE = "Invalid Minimum Size {0}.";
    public final static String ERROR_MISSING_FIELD = "Required property {0} is missing or blank. Request invalid.";
    public static final String ERROR_NO_METADATA = "Engine METADATA was not loaded successfully.";
    public static final String ERROR_EMPTY_LIST = "The list is empty.";
    public static final String ERROR_FAILED_JSON_PROCESSING = "The given JSON data is invalid.";
    public static final String ERROR_USER_IS_NOT_ALLOWED_GENERIC = "Failed to assign {0}. The {1} must be in {2}.";
    public static final String WARNING_EMPTY_ROLES_LIST = "The user {0} has not roles assigned yet.";
    public static final String ERROR_FAILED_VALIDATION = "Engine general error.";
    public static final String ERROR_MISSING_DATA = "Some of the required parameters are missing.";
    public static final String ERROR_UNDECLARED_TYPE = "The Field Type {0} was not declared.";
    public static final String ERROR_INVALID_DATA = "The Field {0} has received unexpected data.";

}
