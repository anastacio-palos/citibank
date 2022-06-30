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

public class ControlDSMTLinkConstants
{

    //Object Type
    public static final String PROCESS = "SOXProcess";

    // Field Name
    public static final String AUDITABLE_ENTITY_TYPE_FIELD = "OPSS-AudEnt:Type";

    public static final String CONTROL_SCOPE_FIELD = "Citi-Ctl:Scp";

    public static final String CONTROL_STATUS_FIELD = "OPSS-Ctl:Status";

    public static final String DSMT_LINK_ACTIVE_FIELD = "Citi-DL:Active";

    public static final String DSMT_LINK_AE_ID_FIELD = "Citi-DL:AEID";

    public static final String DSMT_LINK_BASE_ID_FIELD = "Citi-DL:BaseID";

    public static final String DSMT_LINK_MGNAME_FIELD = "Citi-DSMT:MGName";

    public static final String DSMT_LINK_MSNAME_FIELD = "Citi-DSMT:MSName";

    public static final String DSMT_LINK_LVNAME_FIELD = "Citi-DSMT:LVName";

    public static final String DSMT_LINK_SCOPE_FIELD = "Citi-DL:Scp";

    public static final String DSMT_LINK_STATUS_FIELD = "Citi-DL:Status";

    public static final String DSMT_LINK_TYPE_FIELD = "Citi-DSMT:Type";

    public static final String PROCESS_AEID_FIELD = "Citi-AEScp:AEID";


    // Log and info
    public static final String BASE_DSMT_LINK_IS_NOT_ACTIVE_MESSAGE = "Base DSMT Link is not Active.";

    public static final String BASE_DSMT_LINK_IS_OUT_OF_SCOPE_MESSAGE = "Base DSMT Link is out of scope.";

    public static final String BASE_DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE = "Base DSMT Link status is not null or empty.";

    public static final String DSMT_LINK_CONTROL_CONTROLLER_LOG_FILE_NAME = "dsmt_link_control_helper_controller.log";

    public static final String DSMT_LINK_IS_NOT_ACTIVE_MESSAGE = "DSMT Link is not Active.";

    public static final String DSMT_LINK_STATUS_IS_NOT_NULL_MESSAGE = "DSMT Link status is not null or empty.";

    public static final String ANCESTOR_AUDIT_OBJECT_NOT_FOUND_MESSAGE = "Ancestor audit object not found.";

    public static final String ORIGINAL_DSMT_LINK_NOT_FOUND_MESSAGE = "Original DSMT Link not found.";

    // Field Value
    public static final String IN = "In";

    public static final String OUT = "Out";

    public static final String BASE_ID_STR = "<BASE_ID>";

    // Field List
    public static final String EXISTING_DSMT_LINK_FIELDS_FOR_DISPLAY = "Resource ID, Name, Description";

    public static final String HELPER_UI_DISPLAY_FIELDS_INFO = "Name,Citi-DSMT:Type,Citi-DL:Status,Citi-DL:Scp,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName";

    public static final String HELPER_UI_DISPLAY_FIELDS_INFO_EXISTING = "Name,Citi-DSMT:Type,Citi-DL:Status,Citi-DL:Scp,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName,Citi-DL:Active";

    public static final String HELPER_UI_DISPLAY_FIELDS_INFO_SEARCH = "Name,Citi-DSMT:Type,Citi-DSMT:MSName,Citi-DSMT:MGName,Citi-DSMT:LVName";

    // Query
    public static final String ASSOCIATED_DSMT_LINK_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Scp], "
            + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n"
            + "FROM [SOXControl] \n"
            + "JOIN [Citi_DSMT_Link] ON PARENT ([SOXControl]) \n"
            + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
            + "AND [SOXControl].[Resource ID] = ";

    public static final String ASSOCIATED_DSMT_LINK_BASE_ID_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Scp], "
            + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Citi-DL:Active] \n"
            + "FROM [SOXControl] \n"
            + "JOIN [Citi_DSMT_Link] ON PARENT ([SOXControl]) \n"
            + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
            + "AND [SOXControl].[Resource ID] = ";

    public static final String INVALID_DSMT_LINK_ASSOCIATED_CONTROL_BASE_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Scp], "
            + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n"
            + "FROM [SOXControl] \n"
            + "JOIN [Citi_DSMT_Link] ON PARENT ([SOXControl]) \n"
            + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Active] NOT IN ('Yes') \n"
            + "AND [SOXControl].[Resource ID] = ";

    public static final String AVAILABLE_DSMT_LINK_UNDER_AUDIT_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Scp], "
            + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n"
            + "FROM [AuditProgram] \n"
            + "JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram]) \n"
            + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] IS NOT NULL \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Active] = 'Yes' \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Status] IS NULL \n"
            + "AND [AuditProgram].[Resource ID] = ";

    public static final String ORIGINAL_DSMT_LINK_UNDER_AUDIT_QUERY = "SELECT [Citi_DSMT_Link].[Name], [Citi_DSMT_Link].[Resource ID], [Citi_DSMT_Link].[Description], [Citi_DSMT_Link].[Citi-DSMT:Type], [Citi_DSMT_Link].[Citi-DL:Scp], "
            + "[Citi_DSMT_Link].[Citi-DL:Status], [Citi_DSMT_Link].[Citi-DL:AEID], [Citi_DSMT_Link].[Citi-DL:BaseID], [Citi_DSMT_Link].[Citi-DL:Active] \n"
            + "FROM [AuditProgram] \n"
            + "JOIN [Citi_DSMT_Link] ON PARENT ([AuditProgram]) \n"
            + "WHERE [Citi_DSMT_Link].[Citi-DL:BaseID] = <BASE_ID> \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:AEID] IS NOT NULL \n"
            + "AND [AuditProgram].[Resource ID] = ";

    public static final String CONTORL_LOWER_LEVEL_DEPENDENCY_CHECK_INFO = "SOXControl:Citi_Iss:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
            + "SOXControl:Citi_Iss:Citi_CAP:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
            + "SOXControl:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
            + "SOXControl:Finding:Citi_Iss:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
            + "SOXControl:Finding:Citi_Iss:Citi_CAP:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
            + "SOXControl:Finding:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
            + "SOXControl:Workpaper:Finding:Citi_Iss:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
            + "SOXControl:Workpaper:Finding:Citi_Iss:Citi_CAP:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =, "
            + "SOXControl:Workpaper:Finding:Citi_Iss:Citi_AudMpReport:Citi_DSMT_Link | \n"
            + "AND [Citi_DSMT_Link].[Citi-DL:Scp] IN ('In') "
            + "AND [Citi_DSMT_Link].[Citi-DL:BaseID] =";

}
