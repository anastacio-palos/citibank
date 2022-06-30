package com.ibm.openpages.ext.ui.controller;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isEqual;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isObjectNotNull;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isSetNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.parseCommaDelimitedValues;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.AMR_AUDIT_REPORT_TYPE_FIELD_INFO;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.AMR_AUDIT_REPORT_TYPE_IS_BMIER;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.AMR_AUDIT_REPORT_TYPE_IS_CAIER;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.HELPER_ACCESS_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.NON_IER_VALUES;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.TYPE_IS_AUDIT;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.TYPE_IS_BMQS;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.AUDITABLE_ENTITY_OBJECT_NAME;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.AUDITPROGRAM_OBJECT_NAME;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.BMQS_OBJECT_NAME;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.CONTROL_ISSUE_HIERARCHY_PATH;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.CONTROL_ISSUE_SEARCH_CRITERIA;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.GENERAL_ISSUE_HIERARCHY_PATH;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.ISSUE_DRAFT_STATUS_SETTING;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.ISSUE_PENDING_STATUS_SETTING;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.ISSUE_PUBLISHED_STATUS_SETTING;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectTypeUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.IUserAccessUtil;
import com.ibm.openpages.ext.ui.bean.AuditMappingReportInfo;
import com.ibm.openpages.ext.ui.bean.CitiIssueInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.constant.AuditMappingReportType;
import com.ibm.openpages.ext.ui.constant.IssueStatus;
import com.ibm.openpages.ext.ui.constant.IssueSubType;
import com.ibm.openpages.ext.ui.constant.IssueType;
import com.ibm.openpages.ext.ui.service.IAuditMappingReportDSMTLinkService;
import com.ibm.openpages.ext.ui.service.IIssueDSMTLinkService;
import com.ibm.openpages.ext.ui.util.DSMTLinkHelperUtil;

@Component
public class DSMTLinkBaseController {

    // Class Level Variables
    protected Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IFieldUtil fieldUtil;

    @Autowired
    IGRCObjectUtil grcObjectUtil;

    @Autowired
    IApplicationUtil applicationUtil;

    @Autowired
    IGRCObjectTypeUtil objectTypeUtil;

    @Autowired
    IUserAccessUtil userAccessUtil;

    @Autowired
    DSMTLinkHelperUtil dsmtLinkHelperUtil;
    
    IIssueDSMTLinkService citiIssueService;
    
    IAuditMappingReportDSMTLinkService auditMappingReportService;
    
    @Autowired
    @Qualifier("auditIssueDSMTLinkServiceImpl")
    private IIssueDSMTLinkService auditIssueService;
    @Autowired
    @Qualifier("bmqsIssueDSMTLinkServiceImpl")
    private IIssueDSMTLinkService bmqsIssueService;
    @Autowired
    @Qualifier("adHocIssueDSMTLinkServiceImpl")
    private IIssueDSMTLinkService adHocIssueService;
    
    @Autowired
    @Qualifier("auditNonIerAmrServiceImpl")
    private IAuditMappingReportDSMTLinkService auditNonIerAmrService;
    @Autowired
    @Qualifier("auditIerAmrServiceImpl")
    private IAuditMappingReportDSMTLinkService auditIerAmrService;
    @Autowired
    @Qualifier("bmqsNonIerAmrServiceImpl")
    private IAuditMappingReportDSMTLinkService bmqsNonIerAmrService;
    @Autowired
    @Qualifier("bmqsIerAmrServiceImpl")
    private IAuditMappingReportDSMTLinkService bmqsIerAmrService;
    
    

    /**
     * @param objectId
     * @param statusFieldInfo
     * @return
     * @throws Exception
     */
    public DSMTLinkHelperAppInfo setupHelperObjectInfo(String objectId, String statusFieldInfo) throws Exception {

        logger.info("setupHelperObjectInfo() Start");

        // Method Level Variables.
        IGRCObject grcObject = null;
        String grcObjectType = null;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;

        /* Initialize Variables */
        dsmtLinkHelperInfo = new DSMTLinkHelperAppInfo();
        grcObject = grcObjectUtil.getObjectFromId(objectId);
        grcObjectType = grcObject.getType().getName();

        // Update the Object Type information
        dsmtLinkHelperInfo.setObjectID(objectId);
        dsmtLinkHelperInfo.setObjectType(grcObjectType);
        dsmtLinkHelperInfo.setObjectTypeLabel(grcObject.getType().getLocalizedLabel());
        dsmtLinkHelperInfo.setObjectStatus(fieldUtil.getFieldValueAsString(grcObject, statusFieldInfo));

        if(userAccessUtil.canLoggedInUserWriteObject(grcObject)) {

            logger.info("User have access to Object [" + grcObject.getName() + "] and Id [" + grcObject.getId() + "]");
            dsmtLinkHelperInfo.setHasAccess(true);
        } else {
            logger.info("User does not have access to Object [" + grcObject.getName() + "] and Id [" + grcObject.getId() + "]");
            dsmtLinkHelperInfo.setHasAccess(false);
            dsmtLinkHelperInfo.setMessage(HELPER_ACCESS_MESSAGE);
        }

        logger.info("setupHelperObjectInfo() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * @param dsmtLinkHelperInfo
     * @param scopeFieldInfo
     * @return
     * @throws Exception
     */
    public DSMTLinkHelperAppInfo setupHelperObjectScopeInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo, String scopeFieldInfo) throws Exception {

        logger.info("setupHelperObjectScopeInfo() Start");

        // Method Level Variables.
        IGRCObject grcObject = null;

        /* Initialize Variables */
        grcObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());

        // Update the Object Scope information
        dsmtLinkHelperInfo.setObjectScope(fieldUtil.getFieldValueAsString(grcObject, scopeFieldInfo));

        logger.info("setupHelperObjectScopeInfo() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * @param dsmtLinkHelperInfo
     * @return
     * @throws Exception
     */
    public DSMTLinkHelperAppInfo getIssueDetails(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getIssueType() Start");

        // Method Level Variables.
        String parentObjectType;

        IssueType issueType;
        IGRCObject issueObject;
        IGRCObject parentObject;
        IssueStatus issueStatus;
        CitiIssueInfo citiIssueInfo;
        List<String> draftStatusList;
        List<String> pendingStatusList;
        List<String> publishedStatusList;
        Set<String> ancestorControlsList;
        Set<String> auditPhaseParentsList;
        List<IssueSubType> issueSubTypeList;

        /* Initialize Variables */

        // Get the Basic Object information of the current object under execution
        citiIssueInfo = new CitiIssueInfo();
        issueSubTypeList = new ArrayList<>();
        issueObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        parentObject = grcObjectUtil.getObjectFromId(issueObject.getPrimaryParent());
        parentObjectType = parentObject.getType().getName();

        logger.info("Parent Object Id: " + issueObject.getPrimaryParent());
        logger.info("Parent Object Name: " + parentObject.getName());
        logger.info("Parent Object Name: " + parentObjectType);

        draftStatusList = parseCommaDelimitedValues(applicationUtil.getRegistrySetting(
                dsmtLinkHelperInfo.getObjRegistrySetting() + ISSUE_DRAFT_STATUS_SETTING));

        pendingStatusList = parseCommaDelimitedValues(applicationUtil.getRegistrySetting(
                dsmtLinkHelperInfo.getObjRegistrySetting() + ISSUE_PENDING_STATUS_SETTING));

        publishedStatusList = parseCommaDelimitedValues(applicationUtil.getRegistrySetting(
                dsmtLinkHelperInfo.getObjRegistrySetting() + ISSUE_PUBLISHED_STATUS_SETTING));

        logger.info("Draft status list: " + draftStatusList);
        logger.info("Submit status list: " + publishedStatusList);

        issueType = isEqual(parentObjectType, AUDITPROGRAM_OBJECT_NAME) ?
                IssueType.AUDIT :
                (isEqual(parentObjectType, BMQS_OBJECT_NAME) ?
                        IssueType.BMQS :
                        (isEqual(parentObjectType, AUDITABLE_ENTITY_OBJECT_NAME)) ?
                                IssueType.AD_HOC :
                                IssueType.NOT_DETERMINED);

        issueStatus = draftStatusList.contains(dsmtLinkHelperInfo.getObjectStatus()) ?
                IssueStatus.DRAFT :
                (publishedStatusList.contains(dsmtLinkHelperInfo.getObjectStatus()) ?
                        IssueStatus.PUBLISHED :
                        (pendingStatusList.contains(dsmtLinkHelperInfo.getObjectStatus()) ?
                                IssueStatus.PENDING :
                                IssueStatus.NOT_DETERMINED));

        ancestorControlsList = dsmtLinkHelperUtil.getAncestorObjectsOfSpecificType(issueObject,
                                                                                   CONTROL_ISSUE_HIERARCHY_PATH,
                                                                                   CONTROL_ISSUE_SEARCH_CRITERIA);
        citiIssueInfo.setAncestorControlsList(ancestorControlsList);
        logger.info("Control Parents List: " + ancestorControlsList);

        if (isSetNotNullOrEmpty(ancestorControlsList)) {
            issueSubTypeList.add(IssueSubType.DEA_OET);
        }

        auditPhaseParentsList = dsmtLinkHelperUtil.getAncestorObjectsOfSpecificType(issueObject,
                                                                                    GENERAL_ISSUE_HIERARCHY_PATH, "");
        logger.info("Audit Phase Parents List: " + auditPhaseParentsList);

        if (isSetNotNullOrEmpty(auditPhaseParentsList)) {
            issueSubTypeList.add(IssueSubType.GENERIC);
        }

        logger.info("Obtained Issue Type: " + issueType);
        logger.info("Obtained Issue Sub Type: " + issueSubTypeList);
        logger.info("Obtained Issue Status: " + issueStatus);
        citiIssueInfo.setIssueType(issueType);
        citiIssueInfo.setIssueStatus(issueStatus);
        citiIssueInfo.setIssueSubTypeList(issueSubTypeList);
        citiIssueInfo.setShowDSMTSearch(IssueStatus.PUBLISHED.equals(issueStatus));
        dsmtLinkHelperInfo.setCitiIssueInfo(citiIssueInfo);

        logger.info("getIssueType() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * @param dsmtLinkHelperInfo
     * @return
     * @throws Exception
     */
    public DSMTLinkHelperAppInfo getAMRDetails(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getAMRDetails() Start");

        // Method Level Variables.
        String parentObjectType;

        IGRCObject amrObject;
        IGRCObject parentObject;
 
        AuditMappingReportType amrType;
        AuditMappingReportInfo auditMappingReportInfo;
        
        String amrAuditReportTypeValue;
        List<String> nonIERList;

        /* Initialize Variables */
        amrType = AuditMappingReportType.NOT_DETERMINED;

        // Get the Basic Object information of the current object under execution
        auditMappingReportInfo = new AuditMappingReportInfo();
        amrObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        parentObject = grcObjectUtil.getObjectFromId(amrObject.getPrimaryParent());
        parentObjectType = parentObject.getType().getName();
        amrAuditReportTypeValue = fieldUtil.getFieldValueAsString(amrObject, AMR_AUDIT_REPORT_TYPE_FIELD_INFO );
        nonIERList = parseCommaDelimitedValues(NON_IER_VALUES);

        logger.info("Parent Object Id: " + amrObject.getPrimaryParent());
        logger.info("Parent Object Name: " + parentObject.getName());
        logger.info("Parent Object Type: " + parentObjectType);
        logger.info("AMR Audit Report Type (Field Value): " + amrAuditReportTypeValue);
        
        
        if (isEqual(parentObjectType, TYPE_IS_AUDIT)) {
        	
        	if (isEqual(amrAuditReportTypeValue, AMR_AUDIT_REPORT_TYPE_IS_CAIER )) {
        		amrType = AuditMappingReportType.AUDIT_IER;
        	} 
        	else if (nonIERList.contains(amrAuditReportTypeValue)) {
        		amrType = AuditMappingReportType.AUDIT_NON_IER;
        	}
        	
        }
        else if (isEqual(parentObjectType, TYPE_IS_BMQS)) {
        	
        	if (isEqual(amrAuditReportTypeValue, AMR_AUDIT_REPORT_TYPE_IS_BMIER )) {
        		amrType = AuditMappingReportType.BMQS_IER;
        	} 
        	else if (nonIERList.contains(amrAuditReportTypeValue)) {
        		amrType = AuditMappingReportType.BMQS_NON_IER;
        	}
        	
        }

        logger.info("Obtained Issue Type: " + amrType);
        auditMappingReportInfo.setAuditMappingReportType(amrType);
        dsmtLinkHelperInfo.setAuditMappingReportInfo(auditMappingReportInfo);

        logger.info("getAMRDetails() End");
        return dsmtLinkHelperInfo;
    }

    public void determineIssueService(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("determineIssueService() Start");
        CitiIssueInfo citiIssueInfo;

        citiIssueInfo = dsmtLinkHelperInfo.getCitiIssueInfo();

        if (IssueType.AUDIT.equals(citiIssueInfo.getIssueType())) {

            logger.info("Issue service is Audit Issue");
            citiIssueService = auditIssueService;
        }
        else if (IssueType.BMQS.equals(citiIssueInfo.getIssueType())) {

            logger.info("Issue service is BMQS Issue");
            citiIssueService = bmqsIssueService;
        }
        else if (IssueType.AD_HOC.equals(citiIssueInfo.getIssueType())) {

            logger.info("Issue service is Ad-Hoc Issue");
            citiIssueService = adHocIssueService;
        }

        logger.info("determineIssueService() End");
    }

    /**
     * @param dsmtLinkHelperInfo
     * @throws Exception
     */
    public void determineAMRService(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("determineAMRService() Start");

        AuditMappingReportInfo auditMappingReportInfo;

        auditMappingReportInfo = dsmtLinkHelperInfo.getAuditMappingReportInfo();

        if (isObjectNotNull(auditMappingReportInfo)) {
            
            if (AuditMappingReportType.AUDIT_NON_IER.equals(auditMappingReportInfo.getAuditMappingReportType())) {

                logger.info("AMR service is Audit Non-IER AMR");
                auditMappingReportService = auditNonIerAmrService;
            }
            else if (AuditMappingReportType.AUDIT_IER.equals(auditMappingReportInfo.getAuditMappingReportType())) {

                logger.info("AMR service is Audit IER AMR");
                auditMappingReportService = auditIerAmrService;
            }
            else if (AuditMappingReportType.BMQS_NON_IER.equals(auditMappingReportInfo.getAuditMappingReportType())) {

                logger.info("AMR service is BMQS Non-IER AMR");
                auditMappingReportService = bmqsNonIerAmrService;
            }
            else if (AuditMappingReportType.BMQS_IER.equals(auditMappingReportInfo.getAuditMappingReportType())) {

                logger.info("AMR service is BMQS IER AMR");
                auditMappingReportService = bmqsIerAmrService;
            }
        }

        logger.info("determineAMRService() End");
    }

}
