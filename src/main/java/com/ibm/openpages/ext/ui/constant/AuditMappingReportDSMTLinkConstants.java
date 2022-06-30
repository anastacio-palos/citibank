package com.ibm.openpages.ext.ui.constant;

//TODO format the code, add the licensing at the top.
public class AuditMappingReportDSMTLinkConstants {

	// Log file names
    public static final String AMR_IER_LOG_FILE_NAME = "amr_ier_dsmt_link_Service.log";
    public static final String AMR_NON_IER_LOG_FILE_NAME = "amr_non_ier_dsmt_link_Service.log";
    
    // Other
    public static final String SCOPE_IN = "In";
    public static final String SCOPE_OUT = "Out";
    public static final String ACTIVE_YES = "Yes";
    public static final String DSMT_LINK_ACTIVE_FIELD = "Citi-DL:Active";
    public static final String DSMT_LINK_SCOPE_FIELD = "Citi-DL:Scp";
    public static final String DSMT_LINK_WHEN_ADDED_FIELD = "Citi-DSMT:WhnAdd";
    public static final String WHEN_ADDED_MONITORING_ISSUES_VALUE = "Monitoring Issues";
    public static final String STATUS_ASSOCIATED = "Associated";
    public static final String STATUS_DISSOCIATED = "Dissociated";
    public static final String DSMT_LINK_NOT_ACTIVE_EXCEPTION = "DSMT Link is not Active.";
    public static final String NO = "No";
    public static final String AMR_ISSUE_STATUS_FIELD_INFO = "Citi-CAP:Status";
    public static final String AUDITABLE_ENTITY = "AuditableEntity";
    public static final String DSMT_LINK_BASE_ID_FIELD = "Citi-DL:BaseID";
    public static final String RESOURCE_ID_STR = "<RESOURCE_ID>";

    // 1. NON-IER : Get Existing DSMT Links
    
    // Table Header Fields
    public static final String TABLE_HEADER_EXISTING_DSMTS = "Name," + 
    "Citi-DSMT:Type," + "Citi-DL:Scp," + "Citi-DSMT:MSName," + "Citi-DSMT:MGName," + "Citi-DSMT:LVName," + "Citi-DL:Active";
    
    // Query : Get DSMT Links : Associated (CHILD) to AMR
    public static final String GET_DSMT_LINKS_QUERY = 
    		"SELECT" +
    		"[Citi_DSMT_Link].[Citi-DL:AEID], " +
    		"[Citi_DSMT_Link].[Resource ID], \n" +
            "[Citi_DSMT_Link].[Name], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:Type], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:MSName], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:MGName], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:LVName], " +
    		"[Citi_DSMT_Link].[Citi-DL:BaseID], " +
            "[Citi_DSMT_Link].[Description], " +
            "[Citi_DSMT_Link].[Citi-DL:Scp], " +
            "[Citi_DSMT_Link].[Citi-DL:Active] \n" +
            "FROM [Citi_AudMpReport] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_AudMpReport]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n" +
            "AND [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>'";
    
    // 2. NON-IER : Get Available DSMT Links
    
    // Table Header Fields
    public static final String TABLE_HEADER_FIELDS_AVAILABLE_DSMTS = "Name," +
    "Citi-DSMT:Type," + "Citi-DSMT:MSName," + "Citi-DSMT:MGName," + "Citi-DSMT:LVName";
    
    // ** ** ** this query is used by getAncestorIssues method as well
    // Query : Get ancestor audit ID's : For given AMR Resource Id
    public static final String GET_ANCESTOR_AUDIT_IDS_QUERY = 
    		"SELECT \n" + 
    		"[AuditProgram].[Resource ID] \n" +
    	    "FROM [Citi_AudMpReport] \n" +
    		"JOIN [AuditPhase] ON CHILD ([Citi_AudMpReport]) \n" +
    		"JOIN [AuditProgram] ON CHILD ([AuditPhase]) \n" +
    		"WHERE [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>' \n";
    
    // Query : Get Available DSMT Links : For given Ancestor Audit Id
    public static final String GET_DSMTS_FOR_NON_IER_FOR_GIVEN_ANCESTOR_ID_QUERY  = 
    		"SELECT" + 
    		"[Citi_DSMT_Link].[Citi-DL:AEID], " +
    		"[Citi_DSMT_Link].[Resource ID], " +
    		"[Citi_DSMT_Link].[Name], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:Type], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:MSName], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:MGName], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:LVName], " +
    		"[Citi_DSMT_Link].[Citi-DL:BaseID]" +
    		"FROM [AuditProgram]\n" +
    		"JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram])\n" +
    		"WHERE [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes')\n" +
    		"AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In')\n" +
    		"AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n" +
    		"AND [AuditProgram].[Resource ID] = '<RESOURCE_ID>'";
    
    // Query : Get Base Id of DSMT links which are in the Existing DSMT Links table
    public static final String GET_BASE_ID_OF_EXISTING_DSMT_LINKS_QUERY =
    		"SELECT " + 
    		"[Citi_DSMT_Link].[Citi-DL:BaseID]" +
            "FROM [Citi_AudMpReport] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_AudMpReport]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n" +
            "AND [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>'";
    
    // Query : Get AE Resource ID which are associated (PARENT) to AMR 
    public static final String GET_ASSOCIATED_AUDITABLE_ENTITY_ID = 
    		"SELECT" +
    		"[AuditableEntity].[Resource ID]" +
    		"FROM [AuditableEntity] \n" +
    		"JOIN [Citi_AudMpReport] ON PARENT ([AuditableEntity]) \n" +
    		"WHERE [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>'";
    
    // 3. NON-IER : Get Ancestor Issues
    
    // Table Header Fields  
    public static final String TABLE_HEADER_FIELDS_FOR_ISSUES = "Name," + "Title," + "Issue Level," + "Association Status";
    
    // Query : Get Issues associated (PARENT) to Audit
    public static final String GET_ISSUES_ASSOCITED_TO_AUDIT_QUERY = 
    		"SELECT \n" +
    		"[Citi_Iss].[Resource ID], \n" +
    		"[Citi_Iss].[Name], \n" + 
    		"[Citi_Iss].[Description], \n" +
    		"[Citi_Iss].[Citi-Iss:IssLvl], \n" +
    		"[Citi_Iss].[Citi-Iss:Status], \n" +
    		"[Citi_DSMT_Link].[Citi-DL:BaseID], \n" +
    		"[Citi_DSMT_Link].[Citi-DL:Scp] \n" +
    		"FROM [AuditProgram] \n" +
    		"JOIN [Citi_Iss] ON PARENT ([AuditProgram]) \n" +
    		"JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss]) \n" +
    		"WHERE [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n" +
    		"AND [AuditProgram].[Resource ID] = '<RESOURCE_ID>' \n";
    
    // Query : Get Base Id of DSMT Links associated (CHILD) to AMR
    public static final String GET_BASEID_OF_DSMTS_ASSOCIATED_TO_AMR = 
    		"SELECT \n" +
    		"[Citi_DSMT_Link].[Citi-DL:BaseID] \n" +
    		"FROM [Citi_DSMT_Link] \n" +
    		"JOIN [Citi_AudMpReport] ON CHILD([Citi_DSMT_Link]) \n" +
    		"WHERE [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n" +
    		"AND [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>' \n";
    
    // *** *** *** this query is used by processScopeIssuesFromAMR
    // Query : Get Issues which are directly associated (CHILD) to AMR
    public static final String GET_ISSUES_DIRECTLY_ASSOCIATED_TO_AMR = "SELECT \n" +
    		"[Citi_Iss].[Resource ID] \n" +
    		"FROM [Citi_AudMpReport] \n" +
    		"JOIN [Citi_Iss] ON CHILD ([Citi_AudMpReport]) \n" +
    		"WHERE [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>'";
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    				

    
    // QUERY 5 : Get Available DSMT Links : Query 1
    public static final String GET_DSMTS_FOR_EACHISSUE_FOR_GIVEN_ANCESTOR_ID_QUERY  = "SELECT" + 
    		"[Citi_DSMT_Link].[Citi-DL:AEID], " +
    		"[Citi_DSMT_Link].[Resource ID], " +
    		"[Citi_DSMT_Link].[Name], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:Type], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:MSName], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:MGName], " +
    		"[Citi_DSMT_Link].[Citi-DSMT:LVName], " +
    		"[Citi_DSMT_Link].[Citi-DL:BaseID]" +
    		"FROM [AuditProgram]\n" +
    		"JOIN [Citi_Iss] ON PARENT ([AuditProgram])\n" +
    		"JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss])\n" +
    		"WHERE [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes')\n" +
    		"AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In')\n" +
    		"AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n" +
    		"AND [AuditProgram].[Resource ID] = '<RESOURCE_ID>'";
    
    // QUERY 9 :
    public static final String GET_LIST_OF_SCOPEDIN_DSMTS_BASEID_QUERY =  "SELECT" + 
    		"[Citi_DSMT_Link].[Citi-DL:BaseID]\n" +
    		 "FROM [Citi_AudMpReport]\n" +
    		 "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_AudMpReport])\n" +
    		 "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL\n" +
    		 "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL\n" +
    		 "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In')\n" +
    		 "AND [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>'";
    
    // QUERY 10:
    public static final String GET_LIST_OF_SCOPEDOUT_DSMTS_INFO_QUERY = "SELECT" +
    		"[Citi_DSMT_Link].[Resource ID]," +
            "[Citi_DSMT_Link].[Citi-DL:BaseID]" +
            "FROM [Citi_AudMpReport] \n" +
            "JOIN [Citi_DSMT_Link] ON PARENT ([Citi_AudMpReport]) \n" +
            "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n" +
            "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('Out') \n" +
            "AND [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>'";
    
    // QUERY 11 :
    public static final String GET_PARENT_OBJECTS_ID_OF_TYPE_AUDITABLE_ENTITY =  "SELECT" +
    		"[AuditableEntity].[Resource ID]" +
    		"FROM [AuditableEntity] \n" +
    		"JOIN [Citi_AudMpReport] ON PARENT ([AuditableEntity]) \n" +
    		"WHERE [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>'";
    
    // QUERY 12 : Get all issues for the given ancestor Id's
    public static final String GET_ISSUES_FOR_GIVEN_ANCESTOR_IDS = "SELECT" +
    		"[Citi_Iss].[Resource ID]," + 
    		"[Citi_Iss].[Name]," +
    		"[Citi_Iss].[Description]," +
    		"[Citi_Iss].[Citi-Iss:IssLvl]," +
    		"[Citi_Iss].[Citi-Iss:Status]" + 
    		"FROM [Citi_Iss]" +
    		"JOIN [AuditProgram]  ON CHILD ([Citi_Iss])" +
    		"WHERE [AuditProgram].[Resource ID] = '<RESOURCE_ID>'";
    
    // QUERY 13 : Get list of issues associated to AMR
    public static final String GET_ISSUES_ASSOCIATED_TO_AMR = "SELECT" +
    		"[Citi_Iss].[Resource ID]" +
    		"FROM [Citi_AudMpReport]" +
    		"JOIN [Citi_Iss] ON CHILD ([Citi_AudMpReport])" +
    		"WHERE [Citi_AudMpReport].[Resource ID] = '<RESOURCE_ID>'";
    
    // QUERY 14 : Get list of issues associated to DSMT
    public static final String GET_RESOURCE_ID_OF_ISSUE_WITH_DSMT_AS_CHILD = "SELECT" + 
    		"[Citi_Iss].[Resource ID]" +
    		"FROM [Citi_Iss]" +
    		"JOIN [Citi_DSMT_Link] ON PARENT ([Citi_Iss])" +
    		"WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] = '<RESOURCE_ID>'";
    
    public static final String ASSOCIATED_INVALID_DSMTS_IN_CONTROL_QUERY = "SELECT" +
    		"[Citi_DSMT_Link].[Resource ID]" +
    		"FROM [AuditProgram]" +
    		"JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram])" +
    		"WHERE ([Citi_DSMT_Link].[Citi-DL:Active] IN ('No')" +
    		"OR [Citi_DSMT_Link].[Citi-DL:Scp] IN ('Out')) \n" +
    		"AND [Citi_DSMT_Link].[Citi-DL:BaseID] = '<BASE_ID>'" +
    		"AND [AuditProgram].[Resource ID] = '<RESOURCE_ID>'";
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // Audit Fields
    public static final String AUDIT_TYPE_FIELD = "OPSS-Aud:Type";
    public static final String AUDIT_TYPE_IS_RISK_BASED = "Risk-based";
    public static final String AUDIT_TYPE_IS_OTHER = "Other";
    
    public static final String AUDIT_SUB_TYPE_FIELD = "Citi-Aud:AuditSubType";
    public static final String AUDIT_SUB_TYPE_IS_CONTINUOUS_AUDITING = "Continuous Auditing";
    
    // AEAPP Fields
    public static final String AEAPP_AMRCOUNT_FIELD = "Citi-AEApp:AMRCount";
    public static final String AEAPP_QTLYAMRCOUNT_FIELD = "Citi-AEApp:QtlyAMRCount";
    
    public static final String AE_RESOURCE_ID_STR = "<AE_RESOURCE_ID>"; 
    public static final String AEAPP_RESOURCE_STR = "<AEAPP_RESOURCE_ID>"; 
    
    public static final String FLAG_ASSOCIATE = "Associate";
    public static final String FLAG_DISSOCIATE = "Dissociate";
    
    public static final String GET_PARENT_AEAPPS_QUERY = 
    		  "SELECT "
    		+ "[Citi_AEApp].[Resource ID]"
    		+ "FROM [Citi_AEApp]"
    		+ "JOIN [AuditProgram] ON PARENT ([Citi_AEApp])"
    		+ "WHERE [AuditProgram].[Resource ID] = '<RESOURCE_ID>'";
    
    public static final String AE_RESOURCE_IDS_QUERY = 
    		  "SELECT "
    		+ "[AuditableEntity].[Resource ID]"
    		+ "FROM [AuditableEntity]"
    		+ "JOIN [Citi_AEApp] ON PARENT ([AuditableEntity])"
    		+ "WHERE [AuditableEntity].[Resource ID] = '<AE_RESOURCE_ID>'"
    		+ "AND [Citi_AEApp].[Resource ID] = '<AEAPP_RESOURCE_ID>'";
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
