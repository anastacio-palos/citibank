package com.ibm.openpages.ext.common.util;

/*
 * Licensed Materials - Property of IBM
 * 
 * 
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 * 
 * © Copyright IBM Corporation 2017. All Rights Reserved.
 * 
 * US Government Users Restricted Rights- Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * { "$schema":{"$ref":"TS_Taxonomy_vMay132009"}, "author":"Nikhil Komakula",
 * "customer":"IBM", "date":"11/20/2016 12/08/2017", "summary":"Common",
 * "technology":"java", "feature":"Common Utilities", "rt_num":"" }
 */

public class CommonConstants
{

	public static final int INT_ZERO = 0;

	public static final int INT_ONE = 1;

	public static final String EQUAL_TO = "=";

	public static final String TRUE = "true";

	public static final String FALSE = "false";

	public static final String YES = "Yes";

	public static final String NO = "No";

	public final static String NAME = "name";

	public final static String START = "Start";

	public final static String END = "End";

	public final static String SUCCESS = "Success";

	public final static String FAIL = "Fail";

	public final static String INPUT = "input";

	public static final String DECIMAL = "decimal";

	public static final String PROFILE = "profile";

	public static final String EMPTY_STRING = "";

	public static final String SINGLE_SPACE = " ";

	public static final String PERIOD_CHARECTER = "\\.";

	public static final String QUESTION_MARK = "?";

	public static final String FOLDER_SEPARATOR = "/";

	public static final String TEXT_FILE_TYPE = "txt (text/plain)";

	public static final String PERCENTAGE_P = "%P;";

	public static final String PERCENTAGE_N = "%N";

	public static final String PLACE_HOLDER_ONE = "{0}";

	public static final String PLACE_HOLDER_TWO = "{1}";

	public static final String COLON = ":";

	public static final String SEMI_COLON = ":";

	public static final String PERIOD = "'.'";

	public static final String BACK_SLASH = "\\";

	public static final String TEXT_FILE_EXTENSION = ".txt";

	public static final String COMMA_SEPERATED_DELIMITER = ",";

	public static final String COLON_SEPERATED_DELIMITER = ":";

	public static final String SEMI_COLON_DELIMITER = ";";

	public static final String DOT = "\\.";

	public static final String ATTR_SOX_PROJECT_DEFAULT_NAME = "SOX.ProjectDefault";

	public final static String COMMAND_CENTER_REPORT_SUBMISSION_URL = "/openpages/report.tree.post.do?submitAction=preview&actionContext=preview&reportId=";

	public final static int DEFAULT_RETRY_ATTEMPTS = 5;

	public final static String JSON_IDENTIFIER = "identifier";

	public final static String JSON_LABEL = "label";

	public final static String JSON_VALUE = "value";

	public final static String JSON_ITEMS = "items";

	public final static String READ_ACCESS = "Read";

	public final static String WRITE_ACCESS = "Write";

	public final static String ASSOCIATE_ACCESS = "Associate";

	public final static String DELETE_ACCESS = "Delete";

	public final static int DEFAULT_RETRY_WAIT_MILLISECONDS = 10000;

	public static final String OPENPAGES_AUTO_NAMING_STRING = "/OpenPages/Applications/GRCM/Auto Naming/";

	public static final String AUTO_NAMING_FORMAT = "/Format";

	public static final String REGISTRY_LOCK_OBJECT_ROOT = "/OpenPages/Applications/GRCM/Locked Objects/Lock Child Types/";

	public static final String APPLICATION_URL_PROTOCOL = "/OpenPages/Platform/Reporting Schema/Object URL Generator/Protocol";

	public static final String APPLICATION_URL_HOST = "/OpenPages/Platform/Reporting Schema/Object URL Generator/Host";

	public static final String APPLICATION_URL_PORT = "/OpenPages/Platform/Reporting Schema/Object URL Generator/Port";

	public static final String APPLICATION_URL_DETAIL_PAGE = "/OpenPages/Platform/Reporting Schema/Object URL Generator/Detail Page";

	public static final String APPLICATION_URL_OBJECT_ID_PARAM = "/OpenPages/Platform/Reporting Schema/Object URL Generator/Object ID Parameter Name";

	public static final String REGISTRY_SECURITY_CONTEXT_PATH = "/OpenPages/Common/Security/Model";

	public static final String REGISTRY_SELF_CONTAINED_PATH = "/OpenPages/Common/Self Contained Object Types";

	public static final String AUTO_NAMING_REGISTRY_PATH = "/OpenPages/Applications/GRCM/Auto Naming/{0}/Format";

	public static final String SOXDOCUMENT_NAME = "SOXDocument";

	public static final String SOXSIGNATURE_NAME = "SOXSignature";

	public static final String MMM_dd_YYYY = "MMM dd, yyyy";

	public static final String MM_dd_YYYY = "MM/dd/yyyy";

	public static final String YYYY_MM_dd = "yyyy-MM-dd";

	public static final String MM_dd_YYYY_HH_mm_ss = "MM/dd/yyyy HH:mm:ss";

	public static final String YYYY_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

	public static final String HTML_NEW_LINE = "<br/>";

	public static final String HTML_TABLE_TAG = "<table>";

	public static final String HTML_TABLE_CLOSE_TAG = "</table>";

	public static final String HTML_TABLE_THEAD_TAG = "<thead>";

	public static final String HTML_TABLE_THEAD_CLOSE_TAG = "</thead>";

	public static final String HTML_TABLE_TBODY_TAG = "<tbody>";

	public static final String HTML_TABLE_TBODY_CLOSE_TAG = "</tbody>";

	public static final String HTML_TABLE_TR_TAG = "<tr>";

	public static final String HTML_TABLE_TR_CLOSE_TAG = "</tr>";

	public static final String HTML_TABLE_TH_TAG = "<th>";

	public static final String HTML_TABLE_TH_CLOSE_TAG = "</th>";

	public static final String HTML_TABLE_TD_TAG = "<td>";

	public static final String HTML_TABLE_TD_CLOSE_TAG = "</td>";

	public static final String HTML_ANCHOR_TAG = "<a href=\"";

	public static final String HTML_ANCHOR_CLOSE_TAG = "</a>";

	public static final String HTML_TAG_END_CLOSE = "\">";

	public static final String ADMIN_USERNAME_REGISTRY_SETTING = "/OpenPages/Custom Deliverables/Common/Administrator Credentials/Administrator Username";

	public static final String ADMIN_PASSWORD_REGISTRY_SETTING = "/OpenPages/Custom Deliverables/Common/Administrator Credentials/Administrator Password";

	public static final String ADMIN_CONTEXT_KEY = "adminContextKey";

	public static final String QUERY_SERVICE_DATE_FORMAT = "yyyy-MM-dd";

	public static final String BUSINESS_ENTITY_OBJECT_TYPE = "SOXBusEntity";

	public static final String PROCESS_OBJECT_TYPE = "SOXProcess";

	public static final String RISK_OBJECT_TYPE = "SOXRisk";

	public static final String CONTROL_OBJECT_TYPE = "SOXControl";

	public static final String TEST_PLAN_OBJECT_TYPE = "SOXTest";

	public static final String TEST_RESULT_OBJECT_TYPE = "SOXTestResult";

	public static final String RISK_EVAL_OBJECT_TYPE = "RiskEval";

	public static final String KRI_VAL_OBJECT_TYPE = "KeyRiskIndicatorValue";

	public static final String CONTROL_EVAL_OBJECT_TYPE = "CtlEval";

	public static final String ISSUE_OBJECT_TYPE = "SOXIssue";

	public static final String TOLERANCE_IN_SECONDS_REGISTRY_SETTING = "/OpenPages/Custom Deliverables/Common/Triggers/Copy Event/Tolerance in Seconds";

	public static final String EMAIL_CONTENT_URL = "OP.URL";

	public static final String SYSTEM_FIELDS = "System Fields";

	public static String ROW_DELIM = "\\|";

	public static String COLUMN_DELIM = "\\^";

	public static final String DESCRIPTION = "Description";

	public static final String LOCATION = "Location";

	public static final String HYPHEN = "-";

	public static final String OPTABLE = "OP.TABLE";

	public static final String STR_EMAIL_HTML_START_TAG = "<html>";
	public static final String STR_EMAIL_HTML_END_TAG = "</html>";
	public static final String STR_EMAIL_BODY_START_TAG = "<body>";
	public static final String STR_EMAIL_BODY_END_TAG = "</body>";
	public static final String STR_EMAIL_TABLE_STYLE = "<style type='text/css'>table{ width:100%; } table, th, td {border: 1px solid #D4E0EE;border-collapse: collapse;color: #555;} td, th {padding: 4px;}thead th {text-align: center;background: #E6EDF5;color: #4F76A3;}tbody tr {background: #FCFDFE;} tbody tr.odd {background: #F7F9FC;}table a:link {color: #718ABE;text-decoration: none;}</style>";

	public static final String SYSTEM_FIELDS_NAME = "System Fields:Name";
	public static final String SYSTEM_FIELDS_DESCRIPTION = "System Fields:Description";


}