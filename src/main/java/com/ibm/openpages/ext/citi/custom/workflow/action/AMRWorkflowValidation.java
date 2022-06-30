package com.ibm.openpages.ext.citi.custom.workflow.action;

import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.resource.AssociationFilter;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IEnumField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IncludeAssociations;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.common.util.CommonUtil;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.core.Logger;

public class AMRWorkflowValidation extends AbstractCustomAction {
  private IServiceFactory serviceFactory;
  
  private IResourceService resourceService;
  
  private IConfigurationService configService;
  
  private IConfigProperties configProperties;
  
  private IMetaDataService metadataService;
  
  private Logger logger;
  
  private boolean validation;
  
  public AMRWorkflowValidation(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
    super(context, customProperties);
    this.serviceFactory = null;
    this.resourceService = null;
    this.configService = null;
    this.configProperties = null;
    this.metadataService = null;
    this.logger = null;
    this.validation = false;
  }
  
  protected void process() throws Exception {
    IWFOperationContext context = getContext();
    try {
      this.serviceFactory = context.getServiceFactory();
      this.resourceService = this.serviceFactory.createResourceService();
      this.configService = this.serviceFactory.createConfigurationService();
      this.configProperties = this.configService.getConfigProperties();
      this.metadataService = this.serviceFactory.createMetaDataService();
      String enableDebug = this.configProperties.getProperty(getPropertyValue("ENABLE_DEBUG_MODE"));
      String logFilePath = this.configProperties.getProperty(getPropertyValue("LOG_FILE_PATH"));
      this.logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);
      List<String> issueStatusEnumValuesList = Arrays.asList(new String[] { 
            "Approved", "Cancelled", "Open", 
            "Awaiting Issue Validation", "Re-Opened", "Pending Risk Mitigated", 
            "Pending Risk Mitigated - No IA Validation", "Pending Cancellation", "Pending Re-Opened", 
            "Risk Mitigated - Validation Deferred Completed", 
            "Risk Mitigated - No IA Validation", 
            "Risk Mitigated", "Risk Mitigated - Validation Deferred" });
      IGRCObject amrObj = context.getResource();
      IEnumField amrTypeField = (IEnumField)amrObj.getField("Citi-AMR:Audit Report Type");
      String amrTypeFieldEnumValueName = "";
      if (amrTypeField != null) {
        IEnumValue amrTypeFieldEnumValue = amrTypeField.getEnumValue();
        if (amrTypeFieldEnumValue != null)
          amrTypeFieldEnumValueName = amrTypeFieldEnumValue.getName(); 
      } 
      IGRCObject amrParentObj = this.resourceService.getGRCObject(amrObj.getPrimaryParent());
      if (CommonUtil.isObjectNotNull(amrParentObj)) {
        String amrParentObjType = amrParentObj.getType().getName();
        LoggerUtil.debugEXTLog(this.logger, "", "AMRParent== ", amrParentObj.getName());
        if (amrParentObjType.equalsIgnoreCase("AuditPhase")) {
          IGRCObject auditPhaseParentObj = this.resourceService.getGRCObject(amrParentObj.getPrimaryParent());
          if (CommonUtil.isObjectNotNull(auditPhaseParentObj)) {
            String auditPhaseParentObjType = auditPhaseParentObj.getType().getName();
            LoggerUtil.debugEXTLog(this.logger, "", "AuditPhaseParent== ", auditPhaseParentObj.getName());
            if (auditPhaseParentObjType.equalsIgnoreCase("AuditProgram")) {
              IGRCObject auditObj = auditPhaseParentObj;
              List<IAssociationNode> auditPhaseObjList = getChildren("AuditPhase", auditObj);
              IGRCObject auditPhase = null;
              for (IAssociationNode auditPhaseNode : auditPhaseObjList) {
                auditPhase = this.resourceService.getGRCObject(auditPhaseNode.getId());
                LoggerUtil.debugEXTLog(this.logger, "", "AuditPhase== ", auditPhase.getName());
                List<IAssociationNode> wpaperObjList = getChildren("Workpaper", auditPhase);
                IGRCObject wpaper = null;
                for (IAssociationNode wpaperNode : wpaperObjList) {
                  wpaper = this.resourceService.getGRCObject(wpaperNode.getId());
                  LoggerUtil.debugEXTLog(this.logger, "", "Workpaper==", wpaper.getName());
                  IEnumField wpaperTypeField = (IEnumField)wpaper.getField("OPSS-Work:Type");
                  if (wpaperTypeField != null) {
                    IEnumValue wpaperTypeFieldValue = wpaperTypeField.getEnumValue();
                    if (wpaperTypeFieldValue != null)
                      if (wpaperTypeFieldValue.getName().equalsIgnoreCase("IAM") || 
                        wpaperTypeFieldValue.getName().equalsIgnoreCase("AAM") || 
                        wpaperTypeFieldValue.getName()
                        .equalsIgnoreCase("Generic Workpaper")) {
                        IEnumField wpaperStatusField = (IEnumField)wpaper
                          .getField("OPSS-Work:Preparation Status");
                        if (wpaperStatusField != null) {
                          IEnumValue wpaperStatusFieldValue = wpaperStatusField
                            .getEnumValue();
                          if (wpaperStatusFieldValue != null)
                            if (!wpaperStatusFieldValue.getName().equalsIgnoreCase("Approved") && 
                              
                              !wpaperStatusFieldValue.getName().equalsIgnoreCase("Cancelled")) {
                              this.validation = true;
                              LoggerUtil.debugEXTLog(this.logger, "", "Validation failed for", 
                                  wpaper.getName());
                              break;
                            }  
                        } 
                      }  
                  } 
                } 
              } 
              List<IAssociationNode> issueObjList = getParent("Citi_Iss", amrObj);
              IGRCObject issue = null;
              for (IAssociationNode issueNode : issueObjList) {
                issue = this.resourceService.getGRCObject(issueNode.getId());
                LoggerUtil.debugEXTLog(this.logger, "", "Issue==", issue.getName());
                IEnumField issueStatusField = (IEnumField)issue.getField("Citi-Iss:Status");
                if (issueStatusField != null) {
                  IEnumValue issueStatusFieldValue = issueStatusField.getEnumValue();
                  if (issueStatusFieldValue != null) {
                    if (amrTypeFieldEnumValueName.equalsIgnoreCase("CA Quarterly Report")) {
                      if (!issueStatusEnumValuesList.contains(issueStatusFieldValue.getName())) {
                        this.validation = true;
                        LoggerUtil.debugEXTLog(this.logger, "", "Validation failed for", issue.getName());
                        break;
                      } 
                      continue;
                    } 
                    if (!issueStatusFieldValue.getName().equalsIgnoreCase("Approved") && 
                      !issueStatusFieldValue.getName().equalsIgnoreCase("Cancelled")) {
                      this.validation = true;
                      LoggerUtil.debugEXTLog(this.logger, "", "Validation failed for", issue.getName());
                      break;
                    } 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } catch (Exception e) {
      LoggerUtil.debugEXTLog(this.logger, "handleEvent", "", e.getLocalizedMessage());
    } finally {
      if (this.validation)
        throw new Exception(this.configService.getLocalizedApplicationText(getPropertyValue("VALIDATION"))); 
    } 
  }
  
  private List<IAssociationNode> getChildren(String objType, IGRCObject obj) {
    GRCObjectFilter currRptPeriod = new GRCObjectFilter(this.configService.getCurrentReportingPeriod());
    AssociationFilter associationFilter = currRptPeriod.getAssociationFilter();
    associationFilter.setIncludeAssociations(IncludeAssociations.CHILD);
    associationFilter.setTypeFilters(new ITypeDefinition[] { this.metadataService.getType(objType) });
    IGRCObject grcObj = this.resourceService.getGRCObject(obj.getId(), currRptPeriod);
    List<IAssociationNode> childNode = grcObj.getChildren();
    return childNode;
  }
  
  private List<IAssociationNode> getParent(String objType, IGRCObject obj) {
    GRCObjectFilter currRptPeriod = new GRCObjectFilter(this.configService.getCurrentReportingPeriod());
    AssociationFilter associationFilter = currRptPeriod.getAssociationFilter();
    associationFilter.setIncludeAssociations(IncludeAssociations.PARENT);
    associationFilter.setTypeFilters(new ITypeDefinition[] { this.metadataService.getType(objType) });
    IGRCObject grcObj = this.resourceService.getGRCObject(obj.getId(), currRptPeriod);
    List<IAssociationNode> parentNode = grcObj.getParents();
    return parentNode;
  }
}
