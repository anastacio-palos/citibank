package com.ibm.openpages.ext.citi.custom.workflow.action;


import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.Id;
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
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.api.service.local.SessionFactory;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.aurora.common.AuroraConstants;
import com.openpages.sdk.OpenpagesSession;

public class AssociateIssuesToAuditPlan extends AbstractCustomAction {

	public AssociateIssuesToAuditPlan(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
		super(context, customProperties);
	}

	private IServiceFactory serviceFactory = null;
	private IResourceService resourceService = null;
	private IConfigurationService configService = null;
	private IConfigProperties configProperties = null;
	private IMetaDataService metadataService = null;
	private Logger logger = null;
	

	protected void process() throws Exception {
		IWFOperationContext context = getContext();
		try {
			serviceFactory = context.getServiceFactory();
			resourceService = serviceFactory.createResourceService();
			configService = serviceFactory.createConfigurationService();
			configProperties = configService.getConfigProperties();
			metadataService = serviceFactory.createMetaDataService();
	
			String enableDebug = configProperties.getProperty(getPropertyValue("ENABLE_DEBUG_MODE"));
			String logFilePath = configProperties.getProperty(getPropertyValue("LOG_FILE_PATH"));
			
			logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);

			IGRCObject auditObj = context.getResource();
			IGRCObject auditPlanObj = resourceService.getGRCObject(auditObj.getPrimaryParent());
			if (auditPlanObj != null) {
				String auditPlanObjType = auditPlanObj.getType().getName();
				if (auditPlanObjType.equalsIgnoreCase("Citi_AuditPlan")) {
					LoggerUtil.debugEXTLog(logger, "Audit Close", "auditPlanObj", auditPlanObj.getName());
					List<Id> issuesToMove = getChildren("Citi_Iss", auditObj);
					List<Id> approvedIssues = new ArrayList<Id>();
					for (Id issue : issuesToMove) {

						IGRCObject issueObj = resourceService.getGRCObject(issue);
						IEnumField issueStatusField = (IEnumField) issueObj.getField(getPropertyValue("ISSUE_STATUS"));
						if (issueStatusField != null) {
							IEnumValue issueStatusFieldValue = issueStatusField.getEnumValue();
							if (issueStatusFieldValue != null) {
								if (issueStatusFieldValue.getName().equalsIgnoreCase(getPropertyValue("STATUS_FILTER")))
									approvedIssues.add(issue);
							}
						}

					}
					if (!issuesToMove.isEmpty()) {
						getOPSystemServiceFactory().createResourceService().associate(auditPlanObj.getId(), new ArrayList(), approvedIssues);
						LoggerUtil.debugEXTLog(logger, "Audit Close", "Message==", "Success");
					}
				}
			}
		} catch (Exception e) {
			LoggerUtil.debugEXTLog(logger, "Audit Close", "ExceptionMessage", e.getLocalizedMessage());
		}
	}

	private List<Id> getChildren(String objType, IGRCObject obj) {

		GRCObjectFilter currRptPeriod = new GRCObjectFilter(configService.getCurrentReportingPeriod());
		AssociationFilter associationFilter = currRptPeriod.getAssociationFilter();
		associationFilter.setIncludeAssociations(IncludeAssociations.CHILD);
		associationFilter.setTypeFilters(metadataService.getType(objType));
		IGRCObject grcObj = resourceService.getGRCObject(obj.getId(), currRptPeriod);
		List<IAssociationNode> childNode = grcObj.getChildren();
		List<Id> childId = new ArrayList<Id>();
		for (IAssociationNode child : childNode) {
			childId.add(child.getId());
		}
		return childId;
	}
	
	private IServiceFactory getOPSystemServiceFactory() {

		Context context = new Context();
		context.put(Context.SERVICE_USER_NAME, AuroraConstants.OP_SYSTEM_USER);
		context.put(Context.SERVICE_USER_PASSWORD, SecurityUtil.getSystemPassword());
		
		SessionFactory sessionFactory = new SessionFactory();
		OpenpagesSession session = (OpenpagesSession) sessionFactory.create(context);
		
		context = new Context();
		context.put(Context.SERVICE_SESSION, session);

		return new LocalServiceFactory(context);
	}
}
