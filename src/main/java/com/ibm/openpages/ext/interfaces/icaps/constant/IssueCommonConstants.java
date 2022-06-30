package com.ibm.openpages.ext.interfaces.icaps.constant;

public class IssueCommonConstants {
    public static final String REGISTRY_PATH = "/OpenPages/Custom Deliverables/Interfaces/iCAPS/common";

    public static final String CITI_ISS_TYPE = "Citi_Iss";
    public static final String CITI_CAP_TYPE = "Citi_CAP";
    public static final String CAP_STATUS_FIELD = "Citi-CAP:Status";
    public static final String ISSUE_STATUS_FIELD = "Citi-Iss:Status";
    public static final String ISSUE_SOURCE_FIELD = "Citi-Iss:Src";
    public static final String ICAPS_ISSUE_ID_FIELD = "Citi-Iss:ICAPSIssID";
    public static final String ICAPS_ISSUE_ERR_FIELD = "Citi-iCAPSIss:iCAPSErr";
    public static final String ICAPS_ISSUE_ERR_MESSAGE_FIELD = "Citi-iCAPSIss:iCAPSErrMessage";
    public static final String ICAPS_ISSUE_MINGPUBDATE_FIELD = "Citi-Iss:MigPubDate";
    public static final String ICAPS_ISSUE_MINGREPNUM_FIELD = "Citi-Iss:MigRepNum";
    public static final String ICAPS_CAPID_FIELD = "Citi-CAP:ICapsCAPID";
    public static final String ICAPS_CAP_ERR_FIELD = "Citi-iCAPSCAP:iCAPSErr";
    public static final String ICAPS_CAP_ERR_MESSAGE_FIELD = "Citi-iCAPSCAP:iCAPSErrMessage";

    public static final String YES_VALUE = "Yes";
    public static final String NO_VALUE = "No";
    public static final String FAILED_VALUE = "Error";
    public static final String SUCCESS_VALUE = "Success";
    public static final String REGULATORY_VALUE = "Regulatory";

    public static final String JSON_MEDIA_TYPE = "application/json";
    public static final String XML_MEDIA_TYPE = "application/xml";
    public static final String DEFAULT_TIMEOUT = "2000";


    public static final String SUPER_USER_REGISTRY_SETTING_PATH = REGISTRY_PATH + "/iCAPS Properties File Name";
    public static final String USE_SUPER_USER_REGISTRY_SETTING = REGISTRY_PATH + "/Use Admin Session";
    public static final String USE_SAVE_RESOURCE_REGISTRY_SETTING = REGISTRY_PATH + "/Use Save Resource";

    public static final String ICAPS_CALL_ERROR_MESSAGE = "An ERROR occurred on iCaps call ";
    public static final String ICAPS_ERR_UPDATE_ERROR_MESSAGE = "An ERROR occurred on Issue fields update - *** POTENTIAL ROLLBACK ***";
    public static final String RETRIEVE_DATA_ERROR_MESSAGE = "An ERROR occurred on data retrieval";
    public static final String TRANSFORMING_DATA_ERROR_MESSAGE = "An ERROR occurred on data transformation";
    public static final String CAP_CHANGE_COUNTER = "Citi-iCAPSCAP:changeCounter";
    public static final String ISSUE_ICAPS_RESPONSE = "Citi-iCAPSIss:iCAPSResponse";
    public static final String CAP_ICAPS_RESPONSE = "Citi-iCAPSCAP:iCAPSResponse";
    public static final String FALSE = "false";
}