package com.ibm.openpages.ext.ui.constant;

public class IssueDSMTLinkConstants {

    public static final String SCOPE_IN = "In";
    public static final String SCOPE_OUT = "Out";
    public static final String ACTIVE_YES = "Yes";
    public static final String DSMT_LINK_SCOPE_FIELD = "Citi-DL:Scp";
    public static final String DSMT_LINK_ACTIVE_FIELD = "Citi-DL:Active";

    public static final String EXCEPTION_OBJECT_NAME = "Finding";
    public static final String AUDITPROGRAM_OBJECT_NAME = "AuditProgram";
    public static final String BMQS_OBJECT_NAME = "Citi_AudEntityClusEval";
    public static final String AUDITABLE_ENTITY_OBJECT_NAME = "AuditableEntity";

    public static final String DSMT_LINK_MGNAME_FIELD = "Citi-DSMT:MGName";
    public static final String DSMT_LINK_MSNAME_FIELD = "Citi-DSMT:MSName";
    public static final String DSMT_LINK_LVNAME_FIELD = "Citi-DSMT:LVName";
    public static final String DSMT_LINK_WHEN_ADDED_FIELD = "Citi-DSMT:WhnAdd";
    public static final String CITI_ISSUE_STATUS_FIELD_INFO = "Citi-Iss:Status";
    public static final String ISSUE_DATA_SEG_FLAG_FIELD = "Citi-Flg:DataSegFlag";
    public static final String WHEN_ADDED_MONITORING_ISSUES_VALUE = "Monitoring Issues";

    public static final String DSMT_LINK_TYPE_FIELD = "Citi-DSMT:Type";
    public static final String DSMT_LINK_BASE_ID_FIELD = "Citi-DL:BaseID";
    public static final String AUDITABLE_ENTITY_TYPE_FIELD = "OPSS-AudEnt:Type";


    public static final String BASE_ID_STR = "<BASE_ID>";
    public static final String RESOURCE_ID_STR = "<RESOURCE_ID>";
    public static final String AUDITABLE_ENTITY_STR = "<AUDITABLE_ENTITY>";
    public static final String CONTROL_RESOURCE_ID_STR = "<CONTROL_RESOURCE_ID>";
    public static final String AUDITABLE_ENTITY_PATH_STR = "<AUDITABLE_ENTITY_PATH>";

    public static final String ISSUE_DRAFT_STATUS_SETTING = "/Status Info/Draft";
    public static final String PARENT_AE_FOLDER_TO_SEARCH = "/Search/AE Folder Path";
    public static final String ISSUE_PENDING_STATUS_SETTING = "/Status Info/Pending";
    public static final String ISSUE_PUBLISHED_STATUS_SETTING = "/Status Info/Published";
    public static final String BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE = "Base DSMT Link is out of scope.";


    public static final String DSMT_LINK_NOT_ACTIVE_EXCEPTION = "DSMT Link is not Active.";
    public static final String GENERAL_ISSUE_HIERARCHY_PATH = "AuditPhase,Workpaper,Finding,Citi_Iss";

    public static final String AE_LOWER_DEPENDECY_VALIDATION_MESSAGE = "Disabling Auditable Entity due to DSMT Link's" +
            " lower dependency.";

    public static final String CONTROL_ISSUE_SEARCH_CRITERIA = " AND [SOXControl].[Citi-Ctl:DEA Status]  NOT IN " +
            "('Cancelled')\n AND [SOXControl].[Citi-Ctl:Scp] IN ('In')";
    public static final String EXISTING_DSMT_LINK_FIELDS_INFO_SEARCH = "Name,Citi-DSMT:Type,Citi-DSMT:MSName," +
            "Citi-DSMT:MGName,Citi-DSMT:LVName";
    public static final String EXISTING_DSMT_LINK_TABLE_HEADER_FIELDS_INFO = "Name,Citi-DSMT:Type," +
            "Citi-DL:Scp,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName,Citi-DL:Active";
    public static final String EXISTING_CONTROL_DSMT_LINK_TABLE_HEADER_FIELDS_INFO = "Name,Citi-DSMT:Type," +
            "Controls,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName";
    public static final String EXISTING_DSMT_LINK_TABLE_DEA_OET_HEADER_FIELDS_INFO = "Name,Citi-DSMT:Type," +
            "Citi-DL:Scp,Controls,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName,Citi-DL:Active";

    public static final String FETCH_ANCESTOR_AUDITS_HIERARCHY = "AuditProgram:AuditPhase:Workpaper:Finding:" +
            "Citi_Iss," +
            "AuditProgram:SOXProcess:SOXRisk:SOXControl:Finding:Citi_Iss,AuditProgram:SOXProcess:SOXRisk:SOXControl:" +
            "Workpaper:Finding:Citi_Iss";

    public static final String CONTROL_ISSUE_HIERARCHY_PATH = "SOXControl,Finding,Citi_Iss|" +
            "SOXControl,Workpaper,Finding,Citi_Iss";

    public static final String ASSOCIATED_DSMT_LINK_BASE_QUERY = "SELECT [Citi_DSMT_Link].[Name], \n" +
            "[Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], \n" +
            "[Citi_DSMT_Link].[Citi-DL:Scp], [Citi_DSMT_Link].[Citi-DL:AEID], \n" +
            "[Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n" +
            "FROM [Citi_Iss] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n" +
            "AND [Citi_Iss].[Resource ID] = ";

    public static final String ASSOCIATED_DSMT_LINKS_CONTROL_ANCESTOR_CHECK_QUERY_1 = "SELECT [SOXControl].[Name] " +
            "FROM [Citi_DSMT_Link] \n" +
            "JOIN [SOXControl] ON CHILD ([Citi_DSMT_Link])\n" +
            "JOIN [Workpaper] ON PARENT([SOXControl]) \n" +
            "JOIN [Finding] ON PARENT ([Workpaper]) \n" +
            "JOIN [Citi_Iss] ON PARENT ([Finding]) \n" +
            "WHERE [Citi_Iss].[Resource ID] = '<RESOURCE_ID>' \n" +
            "AND [SOXControl].[Resource ID] = '<CONTROL_RESOURCE_ID>'" +
            "AND [SOXControl].[Citi-Ctl:DEA Status]  NOT IN ('Cancelled')\n" +
            "AND [SOXControl].[Citi-Ctl:Scp] IN ('In')\n" +
            "AND [Citi_DSMT_Link].[Citi-DL:BaseID] = '<BASE_ID>'\n" ;

    public static final String ASSOCIATED_DSMT_LINKS_CONTROL_ANCESTOR_CHECK_QUERY_2 = "SELECT [SOXControl].[Name] " +
            "FROM [Citi_DSMT_Link] \n" +
            "JOIN [SOXControl] ON CHILD ([Citi_DSMT_Link])\n" +
            "JOIN [Finding] ON PARENT ([SOXControl]) \n" +
            "JOIN [Citi_Iss] ON PARENT ([Finding]) \n" +
            "WHERE [Citi_Iss].[Resource ID] = '<RESOURCE_ID>' \n" +
            "AND [SOXControl].[Resource ID] = '<CONTROL_RESOURCE_ID>'" +
            "AND [SOXControl].[Citi-Ctl:DEA Status]  NOT IN ('Cancelled')\n" +
            "AND [SOXControl].[Citi-Ctl:Scp] IN ('In')\n" +
            "AND [Citi_DSMT_Link].[Citi-DL:BaseID] = '<BASE_ID>'\n" ;

    public static final String FETCH_ANCESTOR_AUDITS_QUERY = "SELECT [AuditProgram].[Resource ID] \n" +
            "FROM [AuditProgram] \n" +
            "JOIN [SOXProcess] ON PARENT ([AuditProgram]) \n" +
            "JOIN [SOXRisk] ON PARENT ([SOXProcess]) \n" +
            "JOIN [SOXControl] ON PARENT ([SOXRisk]) \n" +
            "JOIN [Finding] ON PARENT ([SOXControl]) \n" +
            "JOIN [Citi_Iss] ON PARENT ([Finding]) \n" +
            "WHERE [Citi_Iss].[Resource ID] = ";

    public static final String FETCH_ALL_ANCESTOR_AUDITABLE_ENTITY_QUERY = "SELECT " +
            "[AuditableEntity].[Resource ID] \n" +
            "FROM [AuditableEntity] \n" +
            "WHERE [AuditableEntity].[Citi-AE:EntStatus] IN ('Active') \n" +
            "AND [AuditableEntity].[Location] LIKE '<AUDITABLE_ENTITY_PATH>%' \n" +
            "AND ([AuditableEntity].[Name] LIKE '%<AUDITABLE_ENTITY>%' OR \n" +
            "[AuditableEntity].[Description] LIKE '%<AUDITABLE_ENTITY>%')";

    public static final String FETCH_BASE_ID_OF_EXISTING_SCOPE_IN_DSMT_LINKS_QUERY = "SELECT [Citi_DSMT_Link].[Citi-DL:BaseID]" +
            "FROM [Citi_Iss] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n" +
            "AND [Citi_Iss].[Resource ID] = ";

    public static final String FETCH_BASE_ID_OF_EXISTING_SCOPE_OUT_DSMT_LINKS_QUERY = "SELECT [Citi_DSMT_Link]" +
            ".[Resource ID], [Citi_DSMT_Link].[Citi-DL:BaseID]" +
            "FROM [Citi_Iss] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('Out') \n" +
            "AND [Citi_Iss].[Resource ID] = ";

    public static final String AVAILABLE_DSMT_LINK_FROM_ANCESTOR_AUDIABLE_ENTITY_QUERY = "SELECT " +
            "[Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description],\n" +
            " [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Status],\n" +
            " [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID],  [Citi_DSMT_Link].[Citi-DL:Active] " +
            "FROM [AuditableEntity] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([AuditableEntity]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Active] = 'Yes' \n" +
            "AND [AuditableEntity].[Citi-AE:EntStatus] IN ('Active') \n" +
            "AND [AuditableEntity].[Resource ID] = ";


    public static final String AVAILABLE_DSMT_LINK_FROM_ANCESTOR_AUDI_BASE_QUERY = "SELECT [Citi_DSMT_Link].[Name], " +
            "\n" +
            "[Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], \n" +
            "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], \n" +
            "[Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n" +
            "FROM [AuditProgram] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Active] = 'Yes' \n" +
            "AND [AuditProgram].[Resource ID] = ";


    public static final String AVAILABLE_DSMT_LINK_FROM_ANCESTOR_CONTROL_BASE_QUERY = "SELECT [Citi_DSMT_Link]" +
            ".[Name], " +
            "\n" +
            "[Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], \n" +
            "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], \n" +
            "[Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n" +
            "FROM [SOXControl] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([SOXControl]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Active] = 'Yes' \n" +
            "AND [SOXControl].[Resource ID] = ";

    public static final String AUDIT_ISSUE_LOWER_LEVEL_DEPENDENCY_BASE_QUERY =
            "SELECT [Citi_DSMT_Link].[Resource ID] ";

    public static final String AUDIT_ISSUE_LOWER_LEVEL_DEPENDENCY_CHECK_INFO =
            "Citi_Iss:Citi_CAP:Citi_DSMT_Link | "
                    + "FROM [Citi_Iss] |\n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
                    + "Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | "
                    + "FROM [Citi_Iss] |\n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                    + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] = \n";

    public static final String ASSOCIATED_INVALID_CHILD_DSMTS_WITH_ISSUE_QUERY = "SELECT " +
            "[Citi_DSMT_Link].[Resource ID] FROM [Citi_Iss]\n" +
            " JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss])\n" +
            " WHERE [Citi_DSMT_Link].[Citi-DL:Active] NOT IN ('Yes')" +
            " AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In')" +
            " AND [Citi_Iss].[Resource ID] = '<RESOURCE_ID>'";

    public static final String ASSOCIATED_INVALID_DSMTS_IN_CONTROL_QUERY = "SELECT " +
            "[Citi_DSMT_Link].[Resource ID] FROM [SOXControl]\n" +
            " JOIN [Citi_DSMT_Link] ON PARENT ([SOXControl])\n" +
            " WHERE ([Citi_DSMT_Link].[Citi-DL:Active] IN ('No')\n" +
            " OR [Citi_DSMT_Link].[Citi-DL:Scp] IN ('Out'))\n" +
            " AND [Citi_DSMT_Link].[Citi-DL:BaseID] = '<BASE_ID>'\n" +
            " AND [SOXControl].[Resource ID] = '<RESOURCE_ID>'";

    public static final String ASSOCIATED_INVALID_DSMTS_IN_AUDIT_QUERY = "SELECT " +
            "[Citi_DSMT_Link].[Resource ID] FROM [AuditProgram]\n" +
            " JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram])\n" +
            " WHERE ([Citi_DSMT_Link].[Citi-DL:Active] IN ('No')\n" +
            " OR [Citi_DSMT_Link].[Citi-DL:Scp] IN ('Out'))\n" +
            " AND [Citi_DSMT_Link].[Citi-DL:BaseID] = '<BASE_ID>'\n" +
            " AND [AuditProgram].[Resource ID] = '<RESOURCE_ID>'";
}
