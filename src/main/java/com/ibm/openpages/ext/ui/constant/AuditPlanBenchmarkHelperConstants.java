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


public class AuditPlanBenchmarkHelperConstants {


    // Registry Settings
    public static final String ROOT_SETTING = "/OpenPages/Custom Deliverables/Helper App/Audit Plan Benchmark App";

    // Landing Page Info
    public static final String EMPTY_STRING = "";
    public static final String APP_TITLE = "/Header Info/App Title";
    public static final String APP_DISPLAY_NAME = "/Header Info/Display Name";

    public static final String AUDIT_PLAN_BENCHMARK_CONTROLLER_LOG_FILE_NAME = "audit_plan_benchmark_helper_controller.log";
    public static final String AUDIT_PLAN_BENCHMARK_SRVICE_LOG_FILE_NAME = "audit_plan_benchmark_helper_service.log";

    public static final String AUDIT_PLAN_PATH_STR = "<AUDI_PLAN_PATH>";
    public static final String BENCHMARK_YEARS_STR = "<BENCHMARK_YEARS_STR>";
    public static final String AUDIT_PLAN_BENCHMARK_HELPER_DISPLAY_FIELDS = "Name,Location,Citi-ResponsibleIndividuals:Lead Product Team,Citi-AudPlan:Benchmark";
    public static final String AUDIT_TEAM_RESOURCE = "Citi_ATR";
    public static final String GRAM = "GRAM";
    public static final String AUDIT_TYPE_FIELD = "OPSS-Aud:Type";
    public static final String AUDIT_BENCHMARK_DATE_FIELD = "Citi-Aud:BenchmarkDate";
    public static final String AUDIT_USER_LAST_BENCHMARK_FIELD = "Citi-Aud:UserLastBenchmark";
    public static final String AUDIT_STATUS_FIELD = "OPSS-Aud:Status";
    public static final String AUDIT_IS_BENCHMARK_DONE_FIELD = "Citi-Aud:IsBenchmarkDone";
    
    public static final String ATR_ADJHRSQ1_FIELD = "Citi-Aud:AdjHrsQ1";
    public static final String ATR_PLANHRSQ1_FIELD = "Citi-Aud:PlanHrsQ1";

    public static final String ATR_ADJHRSQ2_FIELD = "Citi-Aud:AdjHrsQ2";
    public static final String ATR_PLANHRSQ2_FIELD = "Citi-Aud:PlanHrsQ2";
    
    public static final String ATR_ADJHRSQ3_FIELD = "Citi-Aud:AdjHrsQ3";
    public static final String ATR_PLANHRSQ3_FIELD = "Citi-Aud:PlanHrsQ3";
    
    public static final String ATR_ADJHRSQ4_FIELD = "Citi-Aud:AdjHrsQ4";
    public static final String ATR_PLANHRSQ4_FIELD = "Citi-Aud:PlanHrsQ4";
    
    public static final String ATR_ADJUSTTED_HOURS_FIELD = "Citi-ATR:Adjusted Hours Int";
    public static final String ATR_PLANNED_HOURS_FIELD = "Citi-ATR:Planned Hours Int";
    
    public static final String AUDIT_ADJUSTED_PLANNING_START_DATE_FIELD = "Citi-Aud:Adjusted Planning Start Date";
    public static final String AUDIT_PLANNED_PLANNING_START_DATE_FIELD = "Citi-Aud:Planned Planning Start Date";
    
    public static final String AUDIT_ADJUSTED_FIELDWORK_START_DATE_FIELD = "Citi-Aud:Adjusted Fieldwork Start Date";
    public static final String AUDIT_PLANNED_FIELDWORK_START_DATE_FIELD = "Citi-Aud:Planned Fieldwork Start Date";
    
    public static final String AUDIT_ADJUSTED_FIELDWORK_END_DATE_FIELD = "Citi-Aud:Adjusted Fieldwork End Date";
    public static final String AUDIT_PLANNED_FIELDWORK_END_DATE_FIELD = "Citi-Aud:Planned Fieldwork End Date";

    public static final String AUDIT_ADJUSTED_REPORT_PUBLICATION_DATE_FIELD = "Citi-Aud:AdjRprtPubDt";
    public static final String AUDIT_PLANNED_REPORT_PUBLICATION_DATE_FIELD = "Citi-Aud:PlanRprtPubDt";
    
    public static final String AUDIT_DRAFT_STATUS = "Draft";
    public static final String AUDIT_NOT_STARTED_STATUS = "Not Started";
    public static final String YES_STATUS = "Yes";
    public static final String HELP_DESK_GRP = "Helpdesk";
    public static final String MI_GRP = "MI";
    public static final String RESOURCE_ID = "Resource ID";

    public static final String HELPER_ACCESS_MESSAGE = "You are not an Authorized user to run this helper.";
    public static final String BENCHMARK_YEAR_FIELD_EMPTY_MESSAGE = "Benchmark Year information is not available, please contact your System Administrator for more information.";

    // Session Variable
    public static final String AUDIT_PLAN_BENCHMARK_HELPER_APP_INFO = "Audit_Plan_Benchmark_Helper_App_Info";
    public static final String ENTITY_BENCHMARK_YEAR_FIELD = "Citi-BE:Benchmark Year";


    //Query
    public static final String AUDIT_PLAN_BASE_QUERY = "SELECT [Citi_AuditPlan].[Name], [Citi_AuditPlan].[Resource ID], [Citi_AuditPlan].[Location], "
                               + "[Citi_AuditPlan].[Citi-ResponsibleIndividuals:Lead Product Team], [Citi_AuditPlan].[Citi-AudPlan:Benchmark] \n"
                               + "FROM [Citi_AuditPlan] \n"
                               + "JOIN [SOXBusEntity] ON CHILD([Citi_AuditPlan]) \n"
                               + "WHERE [Citi_AuditPlan].[Citi-AudPlan:Benchmark]='Yes' \n"
                               + "AND [Citi_AuditPlan].[Location] LIKE '/<AUDI_PLAN_PATH>%' ;\n";


    public static final String AUDIT_PLAN_CONFIRM_QUERY = "SELECT [Citi_AuditPlan].[Name], [Citi_AuditPlan].[Description], [Citi_AuditPlan].[Resource ID], [Citi_AuditPlan].[Location], [AuditProgram].[Name], "
                              + "[AuditProgram].[Resource ID], [AuditProgram].[Citi-Aud:IsBenchmarkDone], [AuditProgram].[OPSS-Aud:Audit Commitment] \n"
                              + "FROM [AuditProgram] \n"
                              + "JOIN [Citi_AuditPlan] ON CHILD([AuditProgram]) \n"
                              + "JOIN [SOXBusEntity] ON CHILD([Citi_AuditPlan]) \n"
                              + "WHERE [Citi_AuditPlan].[Citi-AudPlan:Benchmark] = 'Yes' \n"
                              + "AND ([AuditProgram].[Citi-Aud:IsBenchmarkDone] IS NULL OR  [AuditProgram].[Citi-Aud:IsBenchmarkDone] = 'No') \n"
                              + "AND [AuditProgram].[OPSS-Aud:Audit Commitment] IN <BENCHMARK_YEARS_STR> \n"
                              + "AND [Citi_AuditPlan].[Location] LIKE '/<AUDI_PLAN_PATH>%' ;\n";

}