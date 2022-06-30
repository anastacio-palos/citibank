package com.ibm.openpages.ext.ui.constant;

public class BMQSIERAMRConstants {

    // Common
    public static final String RESOURCE_ID = "<RESOURCE_ID>";
    public static final String BASE_ID = "<BASE_ID>";
    public static final String AUDITABLE_ENTITY_ID = "<AUDITABLE_ENTITY_ID>";
    public static final String STATUS_ASSOCIATED = "Associated";
    public static final String STATUS_DISSOCIATED = "Dissociated";
    public static final String AUDITABLEENTITY = "AuditableEntity";
    public static final String SCOPE_IN = "In";
    public static final String SCOPE_OUT = "Out";

    // Log File Names
    public static final String BMQS_IER_LOG_FILE_NAME = "bmqs_ier_dsmt_link_Service.log";

    // Table Header
    public static final String TABLE_HEADER_FIELDS_FOR_ISSUES = "Name," + "Title," + "Issue Level," + "Association Status";

    /* 1. [getAncestorIssues] : */
    public static final String GET_ALL_ANCESTOR_BMQS_ASSOCIATED_TO_CURRENT_AMR_QUERY =
            "SELECT \n"
                        + "[Citi_AudEntityClusEval].[Resource ID] \n"
                    + "FROM [Citi_AudEntityClusEval] \n"
                    + "JOIN [Citi_AudMpReport] ON PARENT ([Citi_AudEntityClusEval]) \n"
                    + "WHERE [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>' \n";

    /*  2. [getAncestorIssues], [associateIssues], and [dissociateIssues] : Issues directly associated to AMR : For given AMR resource Id  */
    public static final String GET_ALL_ISSUES_DIRECTLY_ASSOCIATED_TO_CURRENT_AMR_QUERY =
            "SELECT \n"
                        + "[Citi_Iss].[Resource ID] \n"
                    + "FROM [Citi_Iss] \n"
                    + "JOIN [Citi_AudMpReport] ON PARENT ([Citi_Iss]) \n"
                    + "WHERE [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>' \n";

    /* 3. [getAncestorIssues] : Get issues associated to each ancestor BMQS : For given BMQS resource Id */
    public static final String GET_ALL_ISSUES_ASSOCIATED_TO_EACH_ANCESTOR_BMQS_QUERY =
            "SELECT \n"
                        + "[Citi_Iss].[Resource ID], \n"
                        + "[Citi_Iss].[Name], \n"
                        + "[Citi_Iss].[Description], \n"
                        + "[Citi_Iss].[Citi-Iss:IssLvl], \n"
                        + "[Citi_Iss].[Citi-Iss:Status], \n"
                        + "[Citi_DSMT_Link].[Citi-DL:BaseID], \n"
                        + "[Citi_DSMT_Link].[Citi-DL:Scp] \n"
                    + "FROM [Citi_AudEntityClusEval] \n"
                    + "JOIN [Citi_Iss] ON PARENT ([Citi_AudEntityClusEval]) \n"
                    + "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n"
                    + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n"
                    + "AND [Citi_AudEntityClusEval].[Resource ID] = '<RESOURCE_ID>' \n";

    /* 4. [associateIssues], [dissociateIssues] : Get base Id's of DSMT's associated to Issue : For given Issue Resource Id */
    public static final String GET_ALL_DSMT_ASSOCIATED_TO_EACH_ISSUE_QUERY =
            "SELECT \n"
                        + "[Citi_DSMT_Link].[Citi-DL:BaseID], \n"
                        + "[Citi_DSMT_Link].[Resource ID], \n"
                        + "[Citi_DSMT_Link].[Citi-DL:AEID] \n"
                    + "FROM [Citi_Iss] \n"
                    + "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n"
                    + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n"
                    + "AND [Citi_Iss].[Resource ID] = '<RESOURCE_ID>'";

    /* 5. [dissociateIssues] : Gets the baseID of DSMT that is being shared between multiple issues*/
    public static final String GET_ALL_DSMT_SHARED_BY_MULTIPLE_ISSUES_QUERY =
            "SELECT \n"
                    + "[Citi_DSMT_Link].[Citi-DL:BaseID] \n"
                    + "FROM [Citi_DSMT_Link] \n"
                    + "JOIN [Citi_Iss] ON CHILD ([Citi_DSMT_Link]) \n"
                    + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] = '<BASE_ID>' \n"
                    + "AND [Citi_Iss].[Resource ID] = '<RESOURCE_ID>' \n";

    /* 6. [dissociateIssues] : Get DSMT's associated to AMR : For given AE ID*/
    public static final String GET_DSMTS_ASSOCIATED_TO_AMR_FOR_GIVEN_AEID_QUERY =
            "SELECT \n"
                    + "[Citi_DSMT_Link].[Resource ID] \n"
                    + "FROM [Citi_DSMT_Link] \n"
                    + "JOIN [Citi_AudMpReport] ON CHILD ([Citi_DSMT_Link]) \n"
                    + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IN ('<AUDITABLE_ENTITY_ID>') \n"
                    + "AND [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>' \n";

    /* 7. [dissociateIssues] : Get Base Id of DSMT Links associated (CHILD) to AMR */
    public static final String GET_RESOURCEID_OF_DSMTS_ASSOCIATED_TO_AMR_FOR_GIVEN_BASEID_QUERY =
            "SELECT \n"
                    + "[Citi_DSMT_Link].[Resource ID] \n"
                    + "FROM [Citi_DSMT_Link] \n"
                    + "JOIN [Citi_AudMpReport] ON CHILD([Citi_DSMT_Link]) \n"
                    + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] = '<BASE_ID>' \n";

//    /* 6.1 [dissociateIssues] : Get base Id's of DSMT's associated to Issue : For given Issue Resource Id */
//    public static final String INFO_OF_DSMTS_ASSOCIATED_TO_ISSUE_FOR_DISSOCIATE_QUERY =
//            "SELECT \n"
//                        + "[Citi_DSMT_Link].[Citi-DL:BaseID], \n"
//                        + "[Citi_DSMT_Link].[Resource ID], \n"
//                        + "[Citi_DSMT_Link].[Citi-DL:AEID] \n"
//                    + "FROM [Citi_Iss] \n"
//                    + "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n"
//                    + "WHERE [Citi_Iss].[Resource ID] = '<RESOURCE_ID>'"
//                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
//                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n";

    /* 6.3 [dissociateIssues] : Get all parent AE : For give AMR resourceID*/
//    public static final String GET_AEID_ASSOCIATED_TO_AMR_QUERY =
//            "SELECT \n"
//                        + "[AuditableEntity].[Resource ID] \n"
//                    + "FROM [AuditableEntity] \n"
//                    + "JOIN [Citi_AudMpReport] ON PARENT([AuditableEntity]) \n"
//                    + "WHERE [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>' \n";

    /* 2. [getAncestorIssues] : Get Base Id of DSMT Links associated (CHILD) to AMR */
//    public static final String GET_BASEID_OF_DSMTS_ASSOCIATED_TO_AMR =
//            "SELECT \n"
//                    + "[Citi_DSMT_Link].[Citi-DL:BaseID] \n"
//                    + "FROM [Citi_DSMT_Link] \n"
//                    + "JOIN [Citi_AudMpReport] ON CHILD([Citi_DSMT_Link]) \n"
//                    + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
//                    + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
//                    + "AND [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes') \n"
//                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
//                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n"
//                    + "AND [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>' \n";

}
