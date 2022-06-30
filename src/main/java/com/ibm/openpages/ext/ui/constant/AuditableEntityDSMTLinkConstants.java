package com.ibm.openpages.ext.ui.constant;

public class AuditableEntityDSMTLinkConstants {

    public static final String LEGAL_VEHICLE_NODE = "LV";
    public static final String MANAGED_SEGMENT_NODE = "MS";
    public static final String MANAGED_GEOGRAPHY_NODE = "MG";

    public static final String DSMT_INSERT_STATUS = "I";
    public static final String DSMT_DESCOPE_STATUS = "D";
    
    public static final String AUD_ENTITY_DRAFT_STATUS = "Draft";
    public static final String AUD_ENTITY_ACTIVE_STATUS = "Active";
    public static final String AUD_ENTITY_STATUS_FIELD_INFO = "Citi-AE:EntStatus";

    public static final String DSMT_LINK_ACTIVE_FIELD = "Citi-DL:Active";
    public static final String DSMT_LINK_SCOPE_FIELD = "Citi-DL:Scp";
    public static final String DSMT_LINK_STATUS_FIELD = "Citi-DL:Status";
    public static final String DSMT_LINK_BASE_ID_FIELD = "Citi-DL:BaseID";
    public static final String DSMT_LINK_RANGE_FIELD = "Citi-DL:Range";
    public static final String DSMT_LINK_IS_OMU_FIELD = "Citi-DSMT:IsOMU";
    
    public static final String DSMT_LINK_AUDITABLE_ENTITY_ID_FIELD = "Citi-DL:AEID";
    public static final String DSMT_LINK_MANAGED_SEGMENT_ID_FIELD = "Citi-DSMT:MSID";
    public static final String DSMT_LINK_MANAGED_SEGMENT_NAME_FIELD = "Citi-DSMT:MSName";
    public static final String DSMT_LINK_MANAGED_SEGMENT_LEVEL_FIELD = "Citi-DSMT:MSLevel";
    public static final String DSMT_LINK_MANAGED_SEGMENT_HEIRARCHY = "Citi-DSMT:ManagedSegHier";

    public static final String DSMT_LINK_MANAGED_GEOGRAPHY_ID_FIELD = "Citi-DSMT:MGID";
    public static final String DSMT_LINK_MANAGED_GEOGRAPHY_NAME_FIELD = "Citi-DSMT:MGName";
    public static final String DSMT_LINK_MANAGED_GEOGRAPHY_LEVEL_FIELD = "Citi-DSMT:MGLevel";

    public static final String DSMT_LINK_LEGAL_VEHICLE_ID_FIELD = "Citi-DSMT:LVID";
    public static final String DSMT_LINK_LEGAL_VEHICLE_NAME_FIELD = "Citi-DSMT:LVName";
    public static final String DSMT_LINK_REGION_FIELD = "Citi-DSMT:Region";
    public static final String DSMT_LINK_COUNTRY_FIELD = "Citi-DSMT:Country";
    public static final String DSMT_LINK_TYPE_FIELD = "Citi-DSMT:Type";
    public static final String DSMT_LINK_RATIONAL_FIELD = "Citi-DL:Rtnle";
    

    public static final String DSMT_LINK_FIELD_VALUE_IN = "In";
    public static final String DSMT_LINK_FIELD_VALUE_OUT = "Out";
    public static final String DSMT_LINK_FIELD_VALUE_YES = "Yes";
    public static final String DSMT_LINK_FIELD_VALUE_NO = "No";
    public static final String DSMT_LINK_STATUS_VALUE_PENDING_ADDITION = "Pending Addition";
    public static final String DSMT_LINK_STATUS_VALUE_PENDING_REMOVAL = "Pending Removal";
    
    //
    public static final String LEGAL_VEHICLE = "Legal Vehicle/";
    public static final String MANAGED_SEGMENT = "Managed Segment/";
    public static final String MANAGED_GEOGRAPHY = "Managed Geography/";
    
    //
    public static final String MANAGED_SEGMENT_NODE_NAME = "Managed Segment";
    public static final String MANAGED_GEOGRAPHY_NODE_NAME = "Managed Geography";
    
    //
    public static final String TOP_LEVEL_NODE_ID_REGISTRY_SETTIING = "Id";
    public static final String TOP_LEVEL_NODE_NAME_REGISTRY_SETTIING = "Name";
    public static final String TOP_LEVEL_NODE_LEVEL_REGISTRY_SETTIING = "Level";
    public static final String TOP_LEVEL_NODE_ROOT_REGISTRY_SETTIING = "/OpenPages/Custom Deliverables/Helper App/DSMT Link App/DSMT Search/Top Level Entity/";
    
    //
    public static final String RESTRICTED_NODES_ROOT_SETTING = "/OpenPages/Custom Deliverables/Helper App/DSMT Link App/Auditable Entity/Restricted Nodes/";
    public static final String RESTRRICTED_NODE_SETTING = "Restricted Nodes List ";

    public static final String EXISTING_DSMT_LINK_FIELDS_FOR_DISPLAY = "Resource ID, Name, Description";

    public static final String DSMT_LEGAL_VEHICLE_NODE_LEVEL_RANGE_SETTING = "/Node Level Range/Legal Vehicle";
    public static final String DSMT_MANAGED_SEGMENT_NODE_LEVEL_RANGE_SETTING = "/Node Level Range/Managed Segment";
    public static final String DSMT_MANAGED_GEOGRAPHY_NODE_LEVEL_RANGE_SETTING = "/Node Level Range/Managed Geography";
    
    public static final String EXISTING_DSMT_LINK_TABLE_HEADER_FIELDS_INFO = "Name,Citi-DSMT:Type,Citi-DL:Status,Citi-DL:Scp,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName,Citi-DL:Active";
    public static final String EXISTING_DSMT_LINK_FIELDS_INFO = "Resource ID,Name,Citi-DSMT:Type,Citi-DL:Status," +
            "Citi-DL:Scp,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName,Citi-DL:Active";

    public static final String DSMT_SEARCH_TREE_HEADER_INFO = "Name";
    public static final String TOTAL_NUMBER_DSMT_RESULTS_RETURNED_SETTING = "/DSMT Search Results Returned";
    public static final String DSMT_SEARCH_TABLE_HEADER_INFO = "Name,Level,Node Id,Parent Hierarchy";

    public static final String AUD_ENTITY_LOWER_LEVEL_DEPENDENCY_CHECK_INFO = 
            "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n" 
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Citi_Iss:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Citi_Iss:Citi_CAP:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Finding:Citi_Iss:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In')  \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Finding:Citi_Iss:Citi_CAP:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Finding:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Workpaper:Finding:Citi_Iss:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Workpaper].[Citi-WP:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Workpaper:Finding:Citi_Iss:Citi_CAP:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Workpaper].[Citi-WP:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:SOXProcess:SOXRisk:SOXControl:Workpaper:Finding:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [SOXProcess].[Citi-Prc:Scp] IN ('In') \n"
                + "AND [SOXRisk].[Citi-Rsk:Scp] IN ('In') \n"
                + "AND [SOXControl].[Citi-Ctl:Scp] IN ('In') \n"
                + "AND [Workpaper].[Citi-WP:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:Citi_Iss:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:Citi_Iss:Citi_CAP:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:Citi_IssApp:Citi_Iss:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:Citi_IssApp:Citi_Iss:Citi_CAP:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:Citi_IssApp:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,\n"
            + "AuditableEntity:Citi_AEApp:AuditProgram:AuditPhase:Citi_AudMpReport:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =,"
            + "AuditableEntity:Citi_AEApp:AuditProgram:Citi_DSMT_Link | AND [Citi_AEApp].[Citi-AEApp:Scp] IN ('In') \n"
                + "AND [Citi_AEApp].[Citi-AEApp:Status]  IS NULL\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Cancelled'\n"
                + "AND [AuditProgram].[OPSS-Aud:Status] <> 'Completed'\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
                + "AND [Citi_DSMT_Link].[Citi-DL:Status]  IS NULL\n"
                + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =";
}
