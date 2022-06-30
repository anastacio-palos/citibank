/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * � Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.constant;


public class AuditDSMTLinkConstants {

    public static final String DSMT_DISS_IDS = "dsmtDissIds";
    public static final String AE_DISS_IDS = "aeDissIds";
    public static final String DSMT_ASSO_IDS = "dsmtAssoMap";
    public static final String DSMT_SEARCH = "dsmtSearch";
    public static final String AE_SEARCH = "aeSearch";
    public static final String PROCESS_NEW_DSMT_LINKS = "processNewDSMTLinks";
    public static final String COMMA = ",";

    //Field Name
    public static final String DSMT_LINK_SCOPE_FIELD = "Citi-DL:Scp";
    public static final String DSMT_LINK_STATUS_FIELD = "Citi-DL:Status";
    public static final String DSMT_LINK_TYPE_FIELD = "Citi-DSMT:Type";
    public static final String AUDIT_STATUS_FIELD = "OPSS-Aud:Status";
    public static final String AUD_ENTITY_APP_SCOPE_FIELD = "Citi-AEApp:Scp";
    public static final String AUD_ENTITY_APP_STATUS_FIELD = "Citi-AEApp:Status";
    public static final String AUD_ENTITY_APP_ENTITY_ID_FIELD = "Citi-AEApp:Entity ID";
    public static final String AUD_ENTITY_APP_ENTITY_NAME_FIELD = "Citi-AEApp:Entity Name";
    public static final String AUD_ENTITY_APP_ENTITY_STATUS_FIELD = "Citi-AEApp:Entity Status";
    public static final String AUD_ENTITY_APP_ENTITY_TYPE_FIELD = "Citi-AEApp:Entity Type";
    public static final String AUD_ENTITY_APP_ENTITY_SUB_TYPE_FIELD = "Citi-AEApp:Entity Sub-type";
    public static final String AUD_ENTITY_APP_WHEN_ADDED_FIELD = "Citi-AEApp:When Added";
    public static final String AUD_ENTITY_APP_AUDIT_SCOPE_FIELD = "Citi-AEApp:Audit Scope";
    public static final String DSMT_LINK_AE_ID_FIELD = "Citi-DL:AEID";
    public static final String DSMT_LINK_BASE_ID_FIELD = "Citi-DL:BaseID";
    public static final String DSMT_LINK_ACTIVE_FIELD = "Citi-DL:Active";
    public static final String DSMT_LINK_MGNAME_FIELD = "Citi-DSMT:MGName";
    public static final String DSMT_LINK_MSNAME_FIELD = "Citi-DSMT:MSName";
    public static final String DSMT_LINK_LVNAME_FIELD = "Citi-DSMT:LVName";
    public static final String AUDIT_NEW_DSMT_LINK_FLAG_FIELD = "Citi-Aud:DSMTFlag";
    public static final String AUDIT_NEW_DSMT_LINK_LIST_FIELD = "Citi-Aud:DSMTList";
    public static final String AUD_ENTITY_APP_RATIONALE_DISCOPING_FIELD = "Citi-AEApp:Rationale for Scoping out the Entity";
    public static final String AUD_ENTITY_TYPE_FIELD = "OPSS-AudEnt:Type";
    public static final String AUD_ENTITY_STATUS_FIELD = "Citi-AE:EntStatus";
    public static final String AUD_ENTITY_SCOPE_FIELD = "Citi-AE:Scp";
    public static final String AUD_ENTITY_SUB_TYPE_FIELD = "Citi-AE:EntSubType";
    public static final String ISSUE_APPLICABILITY_SRC_REF_ID_FIELD = "Citi_IssApp:SrcRefID";


    public static final String DSMT_LINK_AUDIT_CONTROLLER_LOG_FILE_NAME = "dsmt_link_audit_helper_controller.log";
    public static final String AUD_ENTITY_ASSOCIATION_NOT_APPROVED_MESSAGE = "Auditable Entity Association is not approved yet.";
    public static final String AUD_ENTITY_ASSOCIATION_DOES_NOT_EXIST_MESSAGE = "Auditable Entity Association does not exist.";
    public static final String AUD_ENTITY_APP_STATUS_NOT_BLANK_MESSAGE = "Auditable Entity App Status is not blank.";
    public static final String AUD_ENTITY_APP_SCOPE_OUT_MESSAGE = "Auditable Entity App Scope is out.";
    public static final String AUD_ENTITY_LOWER_DEPENDENCY_MESSAGE = "Disabling Auditable Entity due to DSMT Link's lower dependency.";
    public static final String DSMT_LINK_LOWER_DEPENDENCY_MESSAGE = "Disabling DSMT Link due to lower dependency.";
    public static final String DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE = "DSMT Link status is not null or empty.";
    public static final String DSMT_LINK_IS_NOT_ACTIVE_MESSAGE = "DSMT Link is not Active.";
    public static final String DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE = "DSMT Link is out of scope.";
    public static final String BASE_DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE = "Base DSMT Link status is not null or empty.";
    public static final String BASE_DSMT_LINK_IS_NOT_ACTIVE_MESSAGE = "Base DSMT Link is not Active.";
    public static final String BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE = "Base DSMT Link is out of scope.";
    public static final String BASE_DSMT_LINK_NOT_FOUND_MESSAGE = "Base DSMT Link not found.";


    //Field Value
    public static final String SCOPE_IN_STATUS = "In";
    public static final String SCOPE_OUT_STATUS = "Out";
    public static final String AUDIT_DRAFT_STATUS = "Draft";
    public static final String DSMT_LINK_YES_STATUS = "Yes";
    public static final String PENDING_REMOVAL_STATUS = "Pending Removal";
    public static final String PENDING_ADDITION_STATUS = "Pending Addition";
    public static final String AUDITPROGRAM_ID_STR = "<AuditProgram_ID>";
    public static final String AUDITABLE_ENTITY_ID_STR = "<AuditableEntity_ID>";
    public static final String AUDITABLE_ENTITY_STR = "<AUDITABLE_ENTITY>";
    public static final String AUDITABLE_ENTITY_PATH_STR = "<AUDITABLE_ENTITY_PATH>";
    public static final String HREF_STRING = "href";
    
    public static final String HELPER_UI_DISPLAY_FIELDS_INFO = "Name,Citi-DSMT:Type,Citi-DL:Status,Citi-DL:Scp,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName";
    public static final String HELPER_UI_DISPLAY_FIELDS_INFO_EXISTING = "Name,Citi-DSMT:Type,Citi-DL:Status,Citi-DL:Scp,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName,Citi-DL:Active";
    public static final String HELPER_UI_DISPLAY_FIELDS_INFO_SEARCH = "Name,Citi-DSMT:Type,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName";
    public static final String HELPER_UI_DISPLAY_FIELDS_INFO_NEW_DSMT_LINK = "Name,Citi-DSMT:Type,Citi-DL:Status,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName";

    public static final String EXISTING_DSMT_LINK_FIELDS_FOR_DISPLAY = "Resource ID, Name, Description";
    
    //Query
    public static final String ASSOCIATED_DSMT_LINK_BASE_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Scp], "
                               + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n"
                               + "FROM [AuditProgram] \n" 
                               + "JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram]) \n"
                               + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
                               + "AND [AuditProgram].[Resource ID] = ";

    public static final String INVALID_DSMT_LINK_ASSOCIATED_AUDIT_BASE_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Scp], "
                               + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n"
                               + "FROM [AuditProgram] \n" 
                               + "JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram]) \n"
                               + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Active] NOT IN ('Yes') \n"
                               + "AND [AuditProgram].[Resource ID] = ";

    public static final String EXISTING_DSMT_LINK_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Scp], "
                               + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n"
                               + "FROM [AuditProgram] \n" 
                               + "JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram]) \n"
                               + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Active] = 'Yes' \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:AEID] =  <AuditableEntity_ID> \n"
                               + "AND [AuditProgram].[Resource ID] = <AuditProgram_ID>";

    public static final String AVAILABLE_DSMT_LINK_BASE_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Scp], [Citi_DSMT_Link].[Citi-DL:Active], "
                               + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Resource ID] \n"
                               + "FROM [AuditProgram] \n" 
                               + "JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram]) \n"
                               + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL "
                               + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
                               + "AND [AuditProgram].[Resource ID] = ";

    public static final String AVAILABLE_AUDITABLE_ENTITY_BASE_QUERY = "SELECT [AuditableEntity].[Name], [AuditableEntity].[Resource ID], [AuditableEntity].[Description], "
                               + "[AuditableEntity].[Citi-AE:EntStatus], [AuditableEntity].[Location] \n"
                               + "FROM [AuditableEntity] \n" 
                               + "WHERE [AuditableEntity].[Citi-AE:EntStatus] IN ('Active') \n"
                               + "AND [AuditableEntity].[Location] LIKE '<AUDITABLE_ENTITY_PATH>%' \n"
                               + "AND ([AuditableEntity].[Name] LIKE '%<AUDITABLE_ENTITY>%' OR [AuditableEntity].[Description] LIKE '%<AUDITABLE_ENTITY>%')";

    public static final String AVAILABLE_DSMT_LINK_IN_AUDITABLE_ENTITY_BASE_QUERY =  "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], "
                               + "[Citi_DSMT_Link].[Citi-DL:Scp], [Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID], "
                               + "[Citi_DSMT_Link].[Citi-DL:Active] \n"
                               + "FROM [AuditableEntity] \n"
                               + "JOIN [Citi_DSMT_Link] ON PARENT ([AuditableEntity]) \n"
                               + "WHERE [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Active] IN ('Yes') \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Status] IS NULL \n"
                               + "AND [AuditableEntity].[Resource ID] = ";

    public static final String AUDITABLE_ENTITY_APP_QUERY = "SELECT [Citi_AEApp].[Name], [Citi_AEApp].[Resource ID], [Citi_AEApp].[Citi-AEApp:Status], [Citi_AEApp].[Citi-AEApp:Scp], [AuditableEntity].[Name], [AuditableEntity].[Resource ID], [AuditProgram].[Name], [AuditProgram].[Resource ID] \n" 
                               + "FROM [AuditableEntity] \n"
                               + "JOIN [Citi_AEApp] ON PARENT ([AuditableEntity]) \n"
                               + "JOIN [AuditProgram] ON PARENT ([Citi_AEApp]) \n"
                               + "WHERE [AuditProgram].[Resource ID] =  <AuditProgram_ID> \n"
                               + "AND [AuditableEntity].[Resource ID] = <AuditableEntity_ID>";

    public static final String ALL_AUDITABLE_ENTITY_APP_QUERY = "SELECT [AuditableEntity].[Name], [AuditableEntity].[Resource ID], [Citi_AEApp].[Name], [Citi_AEApp].[Resource ID], [Citi_AEApp].[Citi-AEApp:Status], [Citi_AEApp].[Citi-AEApp:Scp], [AuditProgram].[Name], [AuditProgram].[Resource ID] \n" 
                               + "FROM [AuditableEntity] \n"
                               + "JOIN [Citi_AEApp] ON PARENT ([AuditableEntity]) \n"
                               + "JOIN [AuditProgram] ON PARENT ([Citi_AEApp]) \n"
                               + "WHERE ([Citi_AEApp].[Citi-AEApp:Scp] = 'In' OR ([Citi_AEApp].[Citi-AEApp:Scp] IS NULL AND [Citi_AEApp].[Citi-AEApp:Status] IS NOT NULL)) \n" 
                               + "AND [AuditProgram].[Resource ID] = ";

    public static final String APPLICATION_QUERY = "SELECT [CitiApp].[Resource ID], [CitiApp].[Name], [CitiApp].[Citi-App:AEID], [CitiApp].[Citi-App:Scope], [AuditProgram].[Resource ID] \n"
                               + "FROM [AuditProgram] \n"
                               + "JOIN [CitiApp] ON PARENT ([AuditProgram]) \n"
                               + "WHERE [CitiApp].[Citi-App:Scope] IN ('In') \n"
                               + "AND [CitiApp].[Citi-App:AEID] LIKE '%<AuditableEntity_ID>%' \n"
                               + "AND [AuditProgram].[Resource ID] = <AuditProgram_ID>";

    public static final String ISSUE_APP_QUERY = "SELECT [Citi_IssApp].[Resource ID], [Citi_IssApp].[Name], [Citi_IssApp].[Citi-IssApp:AEID], [Citi_IssApp].[Citi-IssApp:Scp], [AuditProgram].[Resource ID] \n"
                               + "FROM [AuditProgram] \n"
                               + "JOIN [Citi_IssApp] ON PARENT ([AuditProgram]) \n"
                               + "WHERE [Citi_IssApp].[Citi-IssApp:Scp] IN ('In') \n"
                               + "AND [Citi_IssApp].[Citi-IssApp:AEID] LIKE '%<AuditableEntity_ID>%' \n"
                               + "AND [AuditProgram].[Resource ID] = <AuditProgram_ID>";

    public static final String PROCESS_QUERY = "SELECT [SOXProcess].[Resource ID], [SOXProcess].[Name], [SOXProcess].[Citi-AEScp:AEID], [SOXProcess].[Citi-Prc:Scp], [AuditProgram].[Resource ID] \n"
                               + "FROM [AuditProgram] \n"
                               + "JOIN [SOXProcess] ON PARENT ([AuditProgram]) \n"
                               + "WHERE [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                               + "AND [SOXProcess].[Citi-AEScp:AEID] LIKE '%<AuditableEntity_ID>%' \n"
                               + "AND [AuditProgram].[Resource ID] = <AuditProgram_ID>";

    public static final String AUDIT_LOWER_LEVEL_DEPENDENCY_CHECK_INFO = "AuditProgram:SOXProcess:SOXRisk:SOXControl:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Citi_Iss:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Citi_Iss:Citi_CAP:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Finding:Citi_Iss:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Finding:Citi_Iss:Citi_CAP:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Finding:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Workpaper:Finding:Citi_Iss:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Workpaper:Finding:Citi_Iss:Citi_CAP:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:SOXProcess:SOXRisk:SOXControl:Workpaper:Finding:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:Citi_Iss:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:Citi_Iss:Citi_CAP:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:Citi_IssApp:Citi_Iss:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:Citi_IssApp:Citi_Iss:Citi_CAP:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:Citi_IssApp:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
                               + "AuditProgram:AuditPhase:Citi_AudMpReport:Citi_DSMT_Link | \n"
                               + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
                               + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =";
            
}
