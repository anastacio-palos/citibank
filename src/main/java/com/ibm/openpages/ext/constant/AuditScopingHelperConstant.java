package com.ibm.openpages.ext.constant;

public class AuditScopingHelperConstant {

    public static final String AUDIT_SCOPING_HELPER_LOG = "audit_scoping_helper.log";

    public static final String RETRIEVE_AUDITABLE_ENTITIES_QUERY = "SELECT [AuditableEntity].[Resource ID],[AuditableEntity].[Name], [AuditableEntity].[Description], [AuditableEntity].[Description] FROM [AuditableEntity] JOIN [Citi_AEApp] ON PARENT ([AuditableEntity]) JOIN [AuditProgram] ON PARENT ([Citi_AEApp]) WHERE [AuditProgram].[Resource ID] = %s";

    public static final String RETRIEVE_AUDITABLE_ENTITIY_PROCESS = "SELECT [SOXProcess].[Resource ID],[SOXProcess].[Name], [SOXProcess].[Description], [SOXProcess].[Citi-Src:Source], [SOXProcess].[Citi-Src:SrcRefID], [SOXProcess].[Citi-Prc:Scp]" + "FROM [AuditableEntity]" + "JOIN [SOXProcess] ON PARENT ([AuditableEntity])" + "WHERE [AuditableEntity].[Resource ID] = %s";

    public static final String RETRIEVE_AUDIT_PROCESS = "SELECT [SOXProcess].[Resource ID],[SOXProcess].[Name], [SOXProcess].[Description], " + "[SOXProcess].[Citi-Src:Source], [SOXProcess].[Citi-Src:SrcRefID],[SOXProcess].[Citi-Prc:Scp] " + "FROM [AuditProgram] JOIN [SOXProcess] ON PARENT ([AuditProgram])" + "WHERE [AuditProgram].[Resource ID] = %s";

    public static final String RETRIEVE_PROCESS_RISK = "SELECT [SOXRisk].[Resource ID], [SOXRisk].[Name], [SOXRisk].[Description],[SOXRisk].[Citi-Src:Source], [SOXRisk].[Citi-Src:SrcRefID], [SOXRisk].[Citi-Rsk:Scp] FROM [SOXProcess] JOIN [SOXRisk] ON PARENT ([SOXProcess]) WHERE [SOXProcess].[Resource ID] =%s";

    public static final String RETRIEVE_RISK_CONTROL = "SELECT [SOXControl].[Resource ID], [SOXControl].[Name],[SOXControl].[Description],[SOXControl].[Citi-Src:Source], [SOXControl].[Citi-Src:SrcRefID], [SOXControl].[Citi-Ctl:Scp]  FROM [SOXRisk] JOIN [SOXControl] ON PARENT ([SOXRisk]) WHERE [SOXRisk].[Resource ID] =%s";

    public static final String RETRIEVE_CONTROL_WORKPAPER = "SELECT [Workpaper].[Resource ID], [Workpaper].[Name], [Workpaper].[Description],[Workpaper].[Citi-Src:Source], [Workpaper].[Citi-Src:SrcRefID], [Workpaper].[Citi-WP:Scp] FROM [SOXControl] JOIN [Workpaper] ON PARENT ([SOXControl]) WHERE [SOXControl].[Resource ID] =%s";

    public static final String RETRIEVE_AUDIT_PROCESS_AEREF = "SELECT [SOXProcess].[Resource ID],[SOXProcess].[Name], [SOXProcess].[Description],[SOXProcess].[Citi-AEScp:AEID] " + "FROM [AuditProgram] JOIN [SOXProcess] ON PARENT ([AuditProgram])" + "WHERE [AuditProgram].[Resource ID] = %s";

    public static final String PCRW_AUDITABLE_ENTITY_FIELD = "Citi-AEScp:AEID";
    public static final String PCRW_SOURCE_ID_FIELD = "Citi-Src:SrcRefID";
    public static final String L4_SOURCE_ID_FIELD = "Citi-IssApp:SrcRefID";
    public static final String L4_AUDIT_ID = "Citi-IssApp:AuditID";
    public static final String L4_APP_IssueID = "Citi-IssApp:IssueID";
    public static final String L4_APP_IssueTitle = "Citi-IssApp:IssueTitle";
    public static final String PROCESS_SCOPE = "Citi-Prc:Scp";
    public static final String RISK_SCOPE = "Citi-Rsk:Scp";
    public static final String CONTROL_SCOPE = "Citi-Ctl:Scp";
    public static final String WP_SCOPE = "Citi-WP:Scp";
    public static final String Issue_SCOPE = "Citi-IssApp:Scp";
    public static final String APP_SCOPE = "Citi-App:Scope";

    public static final String PCRW_SCOPE_YES = "In";
    public static final String PCRW_SCOPE_NO = "Out";
    public static final String PCRW_SCOPE_BLANK = "";

    public static final String PROCESS = "Process";
    public static final String RISK = "Risk";
    public static final String CONTROL = "Control";
    public static final String WORKPAPER = "Workpaper";
    public static final String L4Issues = "Issues";
    public static final String APP = "Application";

    // 4 params same as process.
    public static final String RETRIEVE_AUDIT_L4ISSUES = "SELECT [Citi_IssApp].[Resource ID],[Citi_IssApp].[Citi-IssApp:IssueID],[Citi_IssApp].[Citi-IssApp:IssueTItle], [Citi_IssApp].[Citi-IssApp:SrcRefID],[Citi_IssApp].[Citi-IssApp:Scp] FROM [AuditProgram] JOIN [Citi_IssApp] ON PARENT ([AuditProgram]) WHERE [AuditProgram].[Resource ID] = %s";
    public static final String RETRIEVE_AUDIT_APP = "SELECT [Citi_AEApp].[Resource ID], [Citi_AEApp].[Name],[Citi_AEApp].[Description], [Citi_AEApp].[Name] " + "FROM [AuditableEntity]" + "JOIN [Citi_AEApp] ON PARENT ([AuditableEntity])" + "JOIN [AuditProgram] ON PARENT ([Citi_AEApp])" + "WHERE [AuditProgram].[Resource ID] =  %s" + "AND [AuditableEntity].[Resource ID] = %s";
    public static final String DELTA_FIELD = "Citi-Aud:AuditDeltaCalc";
    public static final String DELTA_FIELD_VALUE = "Helper Error";

    public static final String RETRIEVE_AUDIT_APPLICATION = "SELECT [CitiApp].[Resource ID],[CitiApp].[Name], [CitiApp].[Description], [CitiApp].[Citi-Src:Source], [CitiApp].[Citi-Src:SrcRefID] , [CitiApp].[Citi-App:Scope] FROM [AuditProgram] JOIN [CitiApp] ON PARENT ([AuditProgram]) WHERE [AuditProgram].[Resource ID] = %s";

    public static final String RETRIEVE_AUDITITABLEENTITY_APPLICATIONS = "SELECT [CitiApp].[Resource ID],[CitiApp].[Name], [CitiApp].[Description], [CitiApp].[Citi-Src:Source], [CitiApp].[Citi-Src:SrcRefID], [CitiApp].[Citi-App:Scope]" + "FROM [AuditableEntity]" + "JOIN [CitiApp] ON PARENT ([AuditableEntity])" + "WHERE [AuditableEntity].[Resource ID] = %s";

    public static final String RETRIEVE_AUDITITABLEENTITY_ISSUES = "SELECT [Citi_Iss].[Resource ID],[Citi_Iss].[Name],[Citi_Iss].[Description] FROM [AuditableEntity] JOIN [Citi_Iss] ON PARENT ([AuditableEntity]) WHERE [AuditableEntity].[Resource ID] = %s AND [Citi_Iss].[Citi-Iss:Status] = 'Risk Mitigated - Validation Deferred' AND [Citi_Iss].[Citi-Iss:IssLvl] = 'Level 4'";

}
