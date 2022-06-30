package com.ibm.openpages.ext.citi.workflow.constant;

public class NewDSMTLinkConstants {

    public static final String LOG_FILE_NAME = "New_DSMT_Link_Custom_Action.log";

    public static final String DSMT_LINK_STATUS_VALUE_PROPERTY = "dsmt_link_status_value";
    public static final String AE_APP_SCOPE_VALUE_PROPERTY = "ae_app_scope_value";
    public static final String AUDIT_STATUS_IGNORE_VALUE_PROPERTY = "audit_status_ignore_value";
    public static final String AUDIT_DSMT_LINK_FLAG_VALUE_PROPERTY = "audit_dsmt_link_flag_value";

    public static final String QUERY_VALUE = "<QUERY_VALUE>";
    public static final String QUERY_IGNORE_VALUE = "<QUERY_IGNORE_VALUE>";
 
    public static final String AUDIT_NEW_DSMT_LINK_FLAG_FIELD = "Citi-Aud:DSMTFlag";
    public static final String AUDIT_NEW_DSMT_LINK_LIST_FIELD = "Citi-Aud:DSMTList";


    public static final String ALL_DSMT_LINKS_IN_AUDITABLE_ENTITY_QUERY =  "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Resource ID] \n"
            + "FROM [AuditableEntity] \n"
            + "JOIN [Citi_DSMT_Link] ON PARENT ([AuditableEntity]) \n"
            + "WHERE [Citi_DSMT_Link].[Citi-DL:Status] IN (<QUERY_VALUE>) \n"
            + "AND [AuditableEntity].[Resource ID] = ";

    public static final String ALL_AUDIT_IN_AUDITABLE_ENTITY_QUERY = "SELECT [AuditProgram].[Name], [AuditProgram].[Resource ID], [AuditProgram].[OPSS-Aud:Status], [Citi_AEApp].[Name], [Citi_AEApp].[Resource ID], [Citi_AEApp].[Citi-AEApp:Scp], [AuditableEntity].[Name], [AuditableEntity].[Resource ID] \n" 
            + "FROM [AuditableEntity] \n"
            + "JOIN [Citi_AEApp] ON PARENT ([AuditableEntity]) \n"
            + "JOIN [AuditProgram] ON PARENT ([Citi_AEApp]) \n"
            + "WHERE [AuditProgram].[OPSS-Aud:Status] NOT IN (<QUERY_IGNORE_VALUE>) \n" 
            + "AND [Citi_AEApp].[Citi-AEApp:Scp] IN (<QUERY_VALUE>) \n"
            + "AND [AuditableEntity].[Resource ID] = ";

}
