package com.ibm.openpages.ext.citi.workflow.constant;

public class NextAuditDateConstants {

    public static final String LOG_FILE_NAME = "Next_Audit_Date_Custom_Action.log";
    public static final String SUPER_USER_SETTING = "/OpenPages/Custom Deliverables/Helper App/DSMT Link App/DSMT Super User Info/Helper Admin Properties File Name";


    public static final String AE_APP_SCOPE_VALUE_PROPERTY = "ae_app_scope_value";
    public static final String AUDIT_ENTITY_TYPE_VALUE_PROPERTY = "ae_type_value";
    public static final String AUDIT_ENTITY_STATUS_VALUE_PROPERTY = "ae_status_value";
    public static final String AMR_TYPE_VALUE_PROPERTY = "amr_type_value";

    public static final String AUDIT_STATUS_FIELD = "OPSS-Aud:Status";
    public static final String AUD_ENTITY_APP_SCOPE_FIELD = "Citi-AEApp:Scp";
    public static final String AUD_ENTITY_TYPE_FIELD = "OPSS-AudEnt:Type";
    public static final String AUD_ENTITY_STATUS_FIELD = "Citi-AE:EntStatus";
    public static final String AUD_ENTITY_LAST_AUDIT_DATE_FIELD = "OPSS-AudEnt:Previous Audit Completed";
    public static final String AMR_TYPE_FIELD = "Citi-AMR:Audit Report Type";
    public static final String AMR_PUBLICATION_DATE_FIELD = "Citi-AMR:ARPubDate";
 
    public static final String AE_TYPE_VALUE = "<AE_TYPE_VALUE>";
    public static final String AE_STATUS_VALUE = "<AE_STATUS_VALUE>";
    public static final String AEAPP_SCOPE_VALUE = "<AEAPP_SCOPE_VALUE>";
    public static final String AuditProgram_ID = "<AuditProgram_ID>";

    public static final String AMR_TYPE_VALUE = "<AMR_TYPE_VALUE>";
    public static final String AuditableEntity_ID = "<AuditableEntity_ID>";


    public static final String AMR_QUERY =  "SELECT [Citi_AudMpReport].[Name], [Citi_AudMpReport].[Resource ID], [Citi_AudMpReport].[Citi-AMR:Audit Report Type], [Citi_AudMpReport].[Citi-AMR:ARPubDate], [AuditableEntity].[Name], [AuditableEntity].[Resource ID] \n"
            + "FROM [AuditableEntity] \n"
            + "JOIN [Citi_AudMpReport] ON PARENT ([AuditableEntity]) \n"
            + "WHERE [Citi_AudMpReport].[Citi-AMR:Audit Report Type] = '<AMR_TYPE_VALUE>' \n"
            + "AND [AuditableEntity].[Resource ID] = <AuditableEntity_ID>";

    public static final String AUDITABLE_ENTITY_APP_QUERY = "SELECT [AuditableEntity].[Name], [AuditableEntity].[Resource ID], [AuditableEntity].[OPSS-AudEnt:Type], [AuditableEntity].[Citi-AE:EntStatus], [AuditableEntity].[OPSS-AudEnt:Previous Audit Completed], [Citi_AEApp].[Name], [Citi_AEApp].[Resource ID], [Citi_AEApp].[Citi-AEApp:Scp], [AuditProgram].[Name], [AuditProgram].[Resource ID], [AuditProgram].[OPSS-Aud:Type] \n" 
            + "FROM [AuditableEntity] \n"
            + "JOIN [Citi_AEApp] ON PARENT ([AuditableEntity]) \n"
            + "JOIN [AuditProgram] ON PARENT ([Citi_AEApp]) \n"
            + "WHERE [AuditableEntity].[OPSS-AudEnt:Type] = '<AE_TYPE_VALUE>' \n"
            + "AND [AuditableEntity].[Citi-AE:EntStatus] = '<AE_STATUS_VALUE>' \n"
            + "AND [Citi_AEApp].[Citi-AEApp:Scp] = '<AEAPP_SCOPE_VALUE>' \n"
            + "AND [AuditProgram].[Resource ID] = <AuditProgram_ID>";

}
