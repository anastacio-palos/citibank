package com.ibm.openpages.ext.constant;

import java.util.HashMap;
import java.util.Map;

public class DSMTConstant {

    public static final String DSMT_INTERFACE_LOGGER = "dsmt_interface.log";
    public static final String DSMT_TRIPLET_LOGGER = "dsmt_triplet.log";

    public static final String SELECT_INVALID_DSMT = "select TRIPLET_ID," +
                                                     "MS_ID," +
                                                     "MG_ID," +
                                                     "LV_ID" +
                                                     " from table (DSMT_NODE_PKG.get_invalid_Triplets_fn)";

    public static final String UPDATE_DSMT_PROCESSED = "DSMT_TRIPLET_PROCESS_FLAG_PRC (?,?)";

    public static final String UPDATE_DSMT_PROCESSED_SINGLE  = "CALL DSMT_ONE_TRIPLET_PROCESS_FLAG_PRC(?,?)";

    public static final String UPDATE_DSMT_PROCESSED_MESSAGE = "";

    public static final String DSMT_INVALID_FIELD = "Citi-DL:Active";

    public static final String DSMT_INVALID_FIELD_VALUE = "No";

    public static final String DSMT_INVALID_PARENT_FIELD = "Citi-Flg:DataSegFlag";

    public static final boolean DSMT_INVALID_PARENT_FIELD_VALUE = true;

    public static final String DSMT_BASEID_QUERY = "SELECT [Citi_DSMT_Link].[Resource ID] from [Citi_DSMT_Link] where [Citi_DSMT_Link].[Citi-DL:BaseID]=%s";

    public static final int DSMT_UPDATE_BATCH_SIZE = 100;

    public static final String DSMT_INVALID  = "Invalid";

    public static final String DSMT_UPDATE  = "Update";


//    public static final String DSMT_UPDATE_TO_PROCESS = "SELECT * FROM CHANGE_LOG_PROCESSING_DATA WHERE PROCESSING_STATUS = 'N' AND FAIL_COUNT < 5";

    public static final String DSMT_UPDATE_TO_PROCESS = "select NODE_ID, NODE_TYPE, PROCESS_FLAG, NODE_MODIFIED_DATE, FAIL_COUNT, ERROR_MSG from table (DSMT_NODE_PKG.get_non_processed_nodes_fn)";

    public static final String RETRIEVE_DSMT_OBJECT = "SELECT [Citi_DSMT_Link].[Resource ID] from [Citi_DSMT_Link] where [Citi_DSMT_Link].[Citi-DSMT:%s]='%s'";

    public static final Map<String, String>  NODE_TYPE_FIELD_MAP = new HashMap(){{
        put("LV", "LVID");
        put("MG", "MGID");
        put("MS", "MSID");

    }};

    public static final String RETRIEVE_DSMT_DETAIL_NODE_ID_TYPE = "SELECT" +
                                                         " node_type," +
                                                         "node_id," +
                                                         "node_name," +
                                                         "node_level," +
                                                         "node_region," +
                                                         "country_code," +
                                                         "child_hierarchy," +
                                                         "parent_node_id," +
                                                         "parent_hierarchy," +
                                                         "status" +
                                                         " from table (DSMT_NODE_PKG.dsmt_get_individual_node_fn(?,?))";


//    public static final String DSMT_PROCESS_STATUS_UPDATE = "UPDATE CHANGE_LOG_PROCESSING_DATA SET PROCESSING_STATUS=?, LAST_UPDATED=?, FAIL_COUNT=?, ERROR_DESCRIPTION=? WHERE NODE_ID=?";

    public static final String DSMT_PROCESS_STATUS_UPDATE = " CALL DSMT_NODE_PROCESS_FLAG_UPD"+
            "(?,"+
            " ?,"+
            "?,"+
            "?,"+
            "?)";
    // field details;
    // MS
    public static final String MS_FIELD_ID = "Citi-DSMT:MSID";
    public static final String MS_FIELD_NAME="Citi-DSMT:MSName";
    public static final String MS_FIELD_LEVEL = "Citi-DSMT:MSLevel";
    public static final String MS_FIELD_HIERARCHY =  "Citi-DSMT:ManagedSegHier";

    // MG
    public static final String MG_FIELD_ID = "Citi-DSMT:MGID";
    public static final String MG_FIELD_LEVEL = "Citi-DSMT:MGLevel";
    public static final String MG_FIELD_NAME = "Citi-DSMT:MGName";
    public static final String MG_FIELD_COUNTRY = "Citi-DSMT:Country";
    public static final String MG_FIELD_REGION = "Citi-DSMT:Region";

    // LV
    public static final String LV_FIELD_ID = "Citi-DSMT:LVID";
    public static final String LV_FIELD_NAME = "Citi-DSMT:LVName";


    public static final String DSMT_TRIPLET = "CALL DSMT_TRIPLET_PROC (?,?,?,?,?,?)";

    public static final String INVALID_SYNC_OPERATION = "InvalidSync";
    public static final String DSMT_SYNC_OPERATION = "DSMTSSync";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    public static final String EXCLUDE_STATUS_VALUES = "/OpenPages/Custom Deliverables/Interfaces/DSMT Interface/Exclude Audit Status";
    public static final String AUDIT_FIELD_STATUS = "OPSS-Aud:Status";
    public static final String AUDIT_PROGRAM_NAME = "AuditProgram";




}
