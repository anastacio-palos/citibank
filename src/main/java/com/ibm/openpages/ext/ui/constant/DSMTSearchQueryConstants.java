package com.ibm.openpages.ext.ui.constant;


public class DSMTSearchQueryConstants {
    
    public static final String MANAGEMENT_SEGMENT_TREE_QUERY = "SELECT node_id AS Node_ID, \n"
            + " node_name        AS Node_Name,\n"
            + " node_level       AS Node_Level,\n"
            + " child_hierarchy  AS Child_Hierarchy,\n"
            + " parent_node_id   AS Parent_Node_ID,\n"
            + " parent_hierarchy AS Parent_Hierarchy,\n"
            + " country_code     AS Country_Code,\n"
            + " node_region      AS Node_Region\n"
            + " FROM TABLE(DSMT_NODE_PKG.dsmt_triplet_tree_fn('MS', ?, ?, ?))";
    
    //Manage Geography Tree Population Script
    public static final String MANAGEMENT_GEOGRAPHY_TREE_QUERY = "SELECT node_id AS Node_ID, \n"
            + " node_name        AS Node_Name,\n"
            + " node_level       AS Node_Level,\n"
            + " child_hierarchy  AS Child_Hierarchy,\n"
            + " parent_node_id   AS Parent_Node_ID,\n"
            + " parent_hierarchy AS Parent_Hierarchy,\n"
            + " country_code     AS Country_Code,\n"
            + " node_region      AS Node_Region\n"
            + " FROM TABLE(DSMT_NODE_PKG.dsmt_triplet_tree_fn('MG', ?, ?, ?))";


    //Legal Vehicle Tree Population Script
    public static final String LEGAL_VEHICLE_TREE_QUERY = "SELECT node_id AS Node_ID, \n"
            + " node_name        AS Node_Name,\n"
            + " node_level       AS Node_Level,\n"
            + " child_hierarchy  AS Child_Hierarchy,\n"
            + " parent_node_id   AS Parent_Node_ID,\n"
            + " parent_hierarchy AS Parent_Hierarchy,\n"
            + " count            AS Row_Count       \n"
            + " FROM TABLE(DSMT_NODE_PKG.dsmt_triplet_tree_fn('LV', ?, ?, ?))";
    
    
  //Manage Segment Search Script
    public static final String MANAGEMENT_SEGMENT_SEARCH_QUERY = "SELECT node_id AS Node_ID, \n"
            + " node_name        AS Node_Name,\n"
            + " node_level       AS Node_Level,\n"
            + " child_hierarchy  AS Child_Hierarchy,\n"
            + " parent_node_id   AS Parent_Node_ID,\n"
            + " parent_hierarchy AS Parent_Hierarchy,\n"
            + " country_code     AS Country_Code,\n"
            + " node_region      AS Node_Region\n"
            + " FROM TABLE(DSMT_NODE_PKG.dsmt_triplet_search_fn('MS',?,?,?))";

    //Manage Geography Search Script
    public static final String MANAGEMENT_GEOGRAPHY_SEARCH_QUERY = "SELECT node_id AS Node_ID, \n"
            + " node_name        AS Node_Name,\n"
            + " node_level       AS Node_Level,\n"
            + " child_hierarchy  AS Child_Hierarchy,\n"
            + " parent_node_id   AS Parent_Node_ID,\n"
            + " parent_hierarchy AS Parent_Hierarchy,\n"
            + " country_code     AS Country_Code,\n"
            + " node_region      AS Node_Region\n"
            + " FROM TABLE(DSMT_NODE_PKG.dsmt_triplet_search_fn('MG',?,?,?))";


    //Legal vehicle Search Script
    public static final String LEGAL_VEHICLE_SEARCH_QUERY = "SELECT node_id AS Node_ID, \n"
            + " node_name        AS Node_Name,\n"
            + " node_level       AS Node_Level,\n"
            + " child_hierarchy  AS Child_Hierarchy,\n"
            + " parent_node_id   AS Parent_Node_ID,\n"
            + " parent_hierarchy AS Parent_Hierarchy,\n"
            + " count            AS Row_Count       \n"
            + " FROM TABLE(DSMT_NODE_PKG.dsmt_triplet_search_fn('LV', ?, ?, ?))";

}
