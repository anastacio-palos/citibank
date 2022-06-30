package com.ibm.openpages.ext.citi.custom.workflow.action;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.AssociationFilter;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IAssociationNode;
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
import com.ibm.openpages.ext.common.util.CommonUtil;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.aurora.common.AuroraConstants;
import com.openpages.sdk.OpenpagesSession;

public class AssociateIssueToAuditPlanForIssueIncludedInAMR extends AbstractCustomAction {

	public AssociateIssueToAuditPlanForIssueIncludedInAMR(IWFOperationContext context,
			List<IWFCustomProperty> customProperties) {
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

			serviceFactory = getOPSystemServiceFactory();
			resourceService = serviceFactory.createResourceService();
			configService = serviceFactory.createConfigurationService();
			configProperties = configService.getConfigProperties();
			metadataService = serviceFactory.createMetaDataService();

			String enableDebug = configProperties.getProperty(getPropertyValue("ENABLE_DEBUG_MODE"));
			String logFilePath = configProperties.getProperty(getPropertyValue("LOG_FILE_PATH"));

			logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);

			LoggerUtil.debugEXTLog(logger, "AMR Publish Workflow", "Associate Issues to Audit Plan", "Custom Action Started");
			IGRCObject amrObj = context.getResource();
			List<IAssociationNode> amrParentList = getParents("Citi_Iss",amrObj);

			for (IAssociationNode amrParent : amrParentList) {
				
					IGRCObject issueObj = resourceService.getGRCObject(amrParent.getId());

					String amrType = GRCObjectUtil.getFieldValue(amrObj, "Citi-AMR:Audit Report Type");

					String objPath = "";
					if (amrType.equalsIgnoreCase("BM IER"))
						objPath = getPropertyValue("BM_IER_OBJECT_PATH");
					else
						objPath = getPropertyValue("CA_IER_OBJECT_PATH");

					List<String> objPathList = CommonUtil.parseDelimitedValues(objPath, ">");
					LoggerUtil.debugEXTLog(logger, "AMR Publish Workflow", "objPathList", objPathList);
					Integer objPathListSize = objPathList.size();

					List<Id> approvedIssues = new ArrayList<Id>();
					approvedIssues.add(issueObj.getId());

					IGRCObject parentObj = resourceService.getGRCObject(issueObj.getPrimaryParent());
					String parentObjType = "";

					if (CommonUtil.isObjectNotNull(parentObj)) {
						for (int i = 0; i < objPathListSize - 1; i++) {

							parentObjType = parentObj.getType().getName();
							LoggerUtil.debugEXTLog(logger, "AMR Publish Workflow", "parentObjType", parentObjType);
							LoggerUtil.debugEXTLog(logger, "AMR Publish Workflow", "parentObj", parentObj.getName());

							if (parentObjType.equalsIgnoreCase(objPathList.get(0)))
								break;

							if (parentObjType.equalsIgnoreCase(objPathList.get((objPathListSize - 1) - i - 1)))
								parentObj = resourceService.getGRCObject(parentObj.getPrimaryParent());

						}
						LoggerUtil.debugEXTLog(logger, "AMR Publish Workflow", "Audit Plan", parentObj.getName());

						resourceService.associate(parentObj.getId(), new ArrayList<>(), approvedIssues);
					}
				
			}
			LoggerUtil.debugEXTLog(logger, "AMR Publish Workflow", "Associate Issues to Audit Plan", "Custom Action Completed");
		} catch (Exception e) {
			LoggerUtil.debugEXTLog(logger, "AMR Publish Workflow", "ExceptionMessage", e.getLocalizedMessage());
		}
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
	
	private List<IAssociationNode> getParents(String objType, IGRCObject obj) {

		GRCObjectFilter currRptPeriod = new GRCObjectFilter(configService.getCurrentReportingPeriod());
		AssociationFilter associationFilter = currRptPeriod.getAssociationFilter();
		associationFilter.setIncludeAssociations(IncludeAssociations.PARENT);
		associationFilter.setTypeFilters(metadataService.getType(objType));
		IGRCObject grcObj = resourceService.getGRCObject(obj.getId(), currRptPeriod);
		List<IAssociationNode> parentNodeList = grcObj.getParents();
		return parentNodeList;
	}
}
