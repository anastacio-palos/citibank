package com.ibm.openpages.ext.interfaces.icaps.constant;

import java.util.ArrayList;
import java.util.List;

public class IssueConstants {
    //Only Issue
    public final static String ISSUE_ID = "id";
    public final static String NAME = "name";
    public final static String ISSUE_SUMMARY = "Citi-Iss:Summ";
    public final static String PRIMARY_PARENT_ID = "primaryParentId";
    //public final static String OLD_PRIMARY_PARENT_ID = "oldPrimaryParentId";
    //public final static String ISSUE_AUDIT_OWNER = "Citi-Iss:AudOwner";
    public final static String ISSUE_OWNER = "Citi-Iss:RespExcName";
    //public final static String ISSUE_AUDIT_CONTROLLER = "Citi-Iss:AudContlr";
    //public final static String ISSUE_LEAD_AUDITOR = "Citi-Iss:LeadAud";
    public final static String ISSUE_DESCRIPTION = "description";
    public final static String ISSUE_ACTION_COMMENT = "Citi-Iss:ActCom";
    public final static String ISSUE_CREATE_DATE = "Citi-iCAPSIss:iCAPSCreateDate";
    public final static String ISSUE_STATUS = "Citi-Iss:Status";
    public final static String ALREADY_NAMED_BY_CALCULATION = "NamingCalculationAid:AlreadyNamedByCalculation";


    public final static String ISSUE_DETAILED_STATUS = "Citi-Iss:IssDetStatus";
    public final static String ISSUE_TYPE_LEVEL_1 = "Citi-Iss:Issue Type";
    public final static String ISSUE_TYPE_LEVEL_2 = "Citi-Iss:Risk Issue Type - Level 2";
    public final static String ISSUE_TYPE_LEVEL_3 = "Citi-Iss:Risk Issue Type - Level 3";
    public final static String ISSUE_TYPE_LEVEL_4 = "Citi-Iss:Risk Issue Type - Level 4";
    public final static String RE_OPENED_ISSUE_COUNT = "Citi-Iss:NumReOpn";
    public final static String RE_OPENED_BY_REGULATOR_COUNT = "Citi-Iss:ReOpnRegCnt";
    public final static String IA_VALIDATION_DATE = "Citi-iCAPSIss:iCAPSIAValDate";
    public final static String ISSUE_BUSINESS_TARGET_DATE = "Citi-iCAPSIss:iCAPSBusTargetDate";
    public final static String ISSUE_CURRENT_TARGET_DATE = "Citi-Iss:Due Date";
    public final static String ISSUE_REOPEN_DATE = "Citi-Iss:IssReOpnDt";
    public final static String PRIMARY_ROOT_CAUSE_LEVEL_1 = "Citi-Iss:Root Cause Type";
    public final static String PRIMARY_ROOT_CAUSE_LEVEL_2 = "Citi-Iss:Risk Issue Root Cause - Level 2";
    public final static String REVISED_ISSUE_BUSINESS_TARGET_DATE = "Citi-iCAPSIss:iCAPSRevBusTargetDate";
    public final static String REGULATOR_RE_OPEN_DATE = "Citi-Iss:DtRegReOpn";

    public final static String REASON_FOR_FAILED_VALIDATION = "Citi-Iss:ResFailVal";
    public final static String AUDIT_ENTITY_ID = "Citi-Iss:LegEntID";
    public final static String AUDIT_ENTITY_NAME = "Citi-Iss:LegEntName";
    public final static String LEAD_PRODUCT_TEAM = "Citi-Iss:Original Lead Product Team";
    public final static String IA_FUNCTION = "Citi-Com:PrimPrdFunc";

    public final static String HIERARCHY_ID = "MS_HIERARCHY_ID";  //Field Name on DSMT Table
    public final static String PLANNING_CAP = "Citi-CAP:Action Plan Type";
    public final static String CAP_ACTION_PLAN_NUMBER = "Citi-CAP:Action Plan Number";
    public final static String CAP_ID = "CapId";
    public final static String CAP_NAME = "Citi-Cap:Name";
    public final static String CAP_DESCRIPTION = "Citi-CAP:APDescription";
    public final static String CAP_OWNER = "Citi-CAP:Assignee";
    public final static String CAP_CREATE_DATE = "Citi-iCAPSCAP:iCAPSCreateDate";
    public final static String CAP_ACTION_PLAN_STATUS = "Citi-CAP:Status";
    public final static String ICAPS_CAP_STATUS = "Citi-CAP:Status";
    public final static String CAP_DETAIL_STATUS = "Citi-CAP:ICAPSts";
    public final static String CAP_COMPLETION_DATE = "Citi-CAP:CAP Completion Date";
    public final static String CAP_DATECHGAWTVALSTATUS = "Citi-CAP:DateChgAwtValStatus";
    public final static String GRC_ACTIVITYCAT1 = "Citi-Iss-Proc:GRC - Activity L1";
    public final static String GRC_ACTIVITYCAT2 = "Citi-Iss-Proc:GRC - Activity L2";
    public final static String GRC_ACTIVITYCAT3 = "Citi-Iss-Proc:GRC - Activity L3";
    public static final String ISSUE_BUSINESS_COMPLETION_DATE = "Citi-Iss:BusCompDt";
    public static final String SOURCE_LEVEL_2 = "Citi-Iss:SrcLvl2";
    public static final String ICAPSOWNERMNGSEGMENTID = "Citi-iCAPSIss:iCAPSOwnerMngSegmentID";
    public static final String ICAPSEXAMID = "Citi-iCAPSIss:iCAPSExamID";
    public static final String ICAPSEXAMNAME = "Citi-iCAPSIss:iCAPSExamName";
    public static final String ICAPSEXAMAGENCY = "Citi-iCAPSIss:iCAPSExamAgency";
    public static final String ICAPSCOMMITAGENCY = "Citi-iCAPSIss:iCAPSCommitAgency";
    public static final String ICAPSENFORCEAGENCY = "Citi-iCAPSIss:iCAPSEnforceAgency";
    public static final String ICAPSRETCLASS = "Citi-Iss:RETIssClass";
    public static final String ICAPSREPEATMRA = "Citi-iCAPSIss:iCAPSRepeatMRA";
    public static final String ICAPSMNGSEG_DSMT = "Citi-iCAPSIss:iCAPSMngSeg";
    public static final String ICAPSMNGGEO = "Citi-iCAPSIss:iCAPSMngGeo";
    public static final String ICAPSLEGVEH = "Citi-iCAPSIss:iCAPSLegVeh";
    public static final String ICAPSASSIGNEDFUNC = "Citi-iCAPSIss:iCAPSAssignedFunc";
    public static final String ICAPSENFACTTITLE = "Citi-iCAPSIss:iCAPSEnfActTitle";
    public static final String ICAPSIDENTDATE = "Citi-iCAPSIss:iCAPSIdentDate";
    public static final String ICAPSCOORDINATORS = "Citi-iCAPSIss:iCAPSCoordinators";
    public static final String CLOSUREDATE = "Citi-iCAPSIss:ClosureDate";
    public static final String SOURCE_SYSTEM = "Citi-iCAPSIss:Source System";
    public static final String ICAPSSEVRATING = "Citi-iCAPSIss:iCAPSSevRating";
    public static final String INITVALTARGETDATE = "Citi-iCAPSIss:InitValTargetDate";
    public static final String REVISEDVALTARGETDATE = "Citi-iCAPSIss:RevisedValTargetDate";
    public static final String ICAPSREJECTEDIAORREG = "Citi-iCAPSIss:iCAPSRejectedIAorReg";
    public static final String ICAPSRETARGETRAT = "Citi-iCAPSIss:iCAPSRetargetRat";
    public static final String ICAPSREGCLOSEREQ = "Citi-iCAPSIss:iCAPSRegCloseReq";
    public static final String ICAPSVALIDATIONDAYS = "Citi-iCAPSIss:iCAPSValidationDays";
    public static final String ICAPSREGFINDINGID = "Citi-iCAPSIss:iCAPSRegFindingID";
    public static final String ICAPSVALREQ = "Citi-Iss:IAValReq";
    public static final String ICAPSENFTYPE = "Citi-iCAPSIss:iCAPSEnfType";
    public static final String ICAPSCOMMITTITLE = "Citi-iCAPSIss:iCAPSCommitTitle";
    public static final String ICAPSISSCOUNTRY = "Citi-iCAPSIss:iCAPSIssCountry";
    public static final String ICAPSNUMRETARGETS = "Citi-iCAPSIss:iCAPSNumRetargets";
    public static final String ICAPSRISKPRIMARY = "Citi-iCAPSIss:iCAPSRiskPrimary";
    public static final String ICAPSMGE = "Citi-iCAPSIss:iCAPSMGE";
    public static final String ICAPSASSESSUNIT = "Citi-iCAPSIss:iCAPSAssessUnit";
    //public static final String ICAPSBUSTARGETDATE = "Citi-iCAPSIss:iCAPSBusTargetDate";
    public static final String ACTIVATIONDATE = "Citi-iCAPSIss:ActivationDate";
    public static final String ISSUE_SOURCE_LEVEL_1 = "Citi-iCAPSIss:Issue Source Level 1";
    public static final String RISK_LEVEL_1 = "Citi-GRC:GRC - Risk L1";
    public static final String RISK_LEVEL_2 = "Citi-GRC:GRC - Risk L2";
    public static final String RISK_LEVEL_3 = "Citi-GRC:GRC - Risk L3";
    public static final String RISK_LEVEL_4 = "Citi-GRC:GRC - Risk L4";
    public static final String CONTROL_LEVEL_1 = "Citi-Iss-Ctl:GRCCtlL1";

    public static final String ISSUE_SOURCE = "Citi-Iss:Src";

    public static final String ICAPSCAPCREATEDATE = "Citi-iCAPSCAP:iCAPSCreateDate";
    public static final String ICAPSCAPCOORDINATORS = "Citi-iCAPSCAP:iCAPSCoordinators";
    public static final String ICAPSCAPBUSTARGETDATE = "Citi-iCAPSCAP:iCAPSBusTargetDate";
    public static final String ICAPSCAPREVBUSTARGETDATE = "Citi-iCAPSCAP:iCAPSRevBusTargetDate";

    // Only CAP
    //public final static String CAP_BUSINESS_TARGET_DATE = "Citi-CAP:Due Date";
    public static final String ICAPSCAPACTIVATIONDATE = "Citi-iCAPSCAP:iCAPSActivationDate";
    public static final String ICAPSDSMTMANAGEDSEGMENT = "Citi-iCAPSCAP:iCAPSDSMTManagedSegment";
    public static final String ICAPSCAPVALREQ = "Citi-iCAPSCAP:iCAPValReq";
    public static final String ICAPSCAPNUMVALDAYS = "Citi-iCAPSCAP:NumValDays";
    public static final String CAP_BUSINESS_COMPLETION_DATE = "Citi-iCAPSCAP:BusComplDate";
    public static final String CAP_COMMENT = "Citi-CAP:ActCom";

    public static final String LIST_OF_CAPS = "ListOfCaps";
    public static final String VERSION = "1.0.0";
    public static final String CITY_AUDIT_PLAN_QUERY = "select [Citi_AuditPlan].[Resource ID] from [SOXBusEntity] JOIN [Citi_AuditPlan] ON PARENT([SOXBusEntity]) WHERE [SOXBusEntity].[Resource ID] = ''{0}''";
    public static final String DSMT_HIERARCHY_QUERY = "select MS_HIERARCHY_ID from DSMT_MANAGE_SEGMENT WHERE CHILD_ORG_ID = ?";
    public static final String CITY_ENTITY_QUERY = "select [SOXBusEntity].[Resource ID] from [SOXBusEntity] WHERE [SOXBusEntity].[Name] = ''{0}''";
    public static final String TRUE_VALUE = "True";
    public static final String IS_SUBSCRIBED_CAP = "Citi-CAP:IsSubscribedCap";


    public static List<String> listOfUserFields;

    public static void init() {

        if (listOfUserFields == null) {
            listOfUserFields = new ArrayList<>();
            //listOfUserFields.add(ISSUE_AUDIT_OWNER);
            //listOfUserFields.add(ISSUE_OWNER);
            //listOfUserFields.add(ISSUE_AUDIT_CONTROLLER);
            //listOfUserFields.add(ISSUE_LEAD_AUDITOR);
            listOfUserFields.add(LEAD_PRODUCT_TEAM);
            //listOfUserFields.add(CAP_OWNER);
        }
    }


    //public final static String ISSUE_CURRENT_TARGET_DATE = "Citi-Iss:CurrIssDueDate";
    //public final static String DESCRIPTION = "description";
}
