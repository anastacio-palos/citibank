package com.ibm.openpages.ext.citi.custom.workflow.action;

import java.util.List;
import java.util.Date;
import java.io.InputStream;
import java.sql.Timestamp;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.application.CognosOutputFormat;
import com.ibm.openpages.api.application.IReportParameters;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.IFileTypeDefinition;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.resource.AssociationFilter;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IDocument;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IResourceFactory;
import com.ibm.openpages.api.resource.IncludeAssociations;
import com.ibm.openpages.api.service.IApplicationService;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.api.service.local.SessionFactory;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.openpages.aurora.common.AuroraConstants;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.sdk.OpenpagesSession;

public class IssueSnapshot extends AbstractCustomAction {

	public IssueSnapshot(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
		super(context, customProperties);
	}
	
	private IServiceFactory serviceFactory = null;
	private IResourceService resourceService = null;
	private IConfigurationService configService = null;
	private IConfigProperties configProperties = null;
	private IMetaDataService metadataService = null;
	private IApplicationService applicationService = null;
	private IResourceFactory resFactory = null;
	private IDocument doc = null;
	private InputStream iostream = null;
	private IReportParameters reportParams = null;
	private ITypeDefinition typeDef = null;
	private IFileTypeDefinition fileTypeDef = null;
	private Logger logger = null;
	Context context = null;
	
	String resParam = getPropertyValue("RESID_PARAMETER");
	String reportPath = getPropertyValue("REPORT_PATH");

	protected void process() throws Exception {
		IWFOperationContext context = getContext();

		try {

			serviceFactory = getOPSystemServiceFactory(); //context.getServiceFactory();
			resourceService = serviceFactory.createResourceService();
			configService = serviceFactory.createConfigurationService();
			configProperties = configService.getConfigProperties();
			metadataService = serviceFactory.createMetaDataService();
			applicationService = serviceFactory.createApplicationService();
			resFactory = resourceService.getResourceFactory();
			
			Date dt = new Date();
			long time = dt.getTime();
			Timestamp ts = new Timestamp(time);
			Integer incSuffix = 0;

			String enableDebug = configProperties.getProperty(getPropertyValue("ENABLE_DEBUG_MODE"));
			String logFilePath = configProperties.getProperty(getPropertyValue("LOG_FILE_PATH"));

			logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);

			IGRCObject auditObj = context.getResource();
			List<ITypeDefinition> typeDefList = metadataService.getTypes();

			for (ITypeDefinition def : typeDefList) {
				if (def.getName().equalsIgnoreCase("SOXDocument")) {
					typeDef = def;
					break;
				}
			}
			List<IFileTypeDefinition> fileTypeDefList = typeDef.getFileTypes();
			for (IFileTypeDefinition file : fileTypeDefList) {
				if (file.getFileExtension().equalsIgnoreCase("pdf")) {
					fileTypeDef = file;
					break;
				}
			}

			List<IAssociationNode> issueList = getChildren(auditObj);

			for (IAssociationNode issue : issueList) {
				IGRCObject issueObj = resourceService.getGRCObject(issue.getId());
				LoggerUtil.debugEXTLog(logger, "Audit Close", "Issue== ", issueObj.getName());
				String issStatus = GRCObjectUtil.getFieldValue(issueObj, getPropertyValue("ISSUE_STATUS"));
				if (issStatus.equalsIgnoreCase(getPropertyValue("STATUS_FILTER"))) {
					incSuffix++;
					String attachmentName = "Audit Issue Snapshot Report";
					attachmentName = attachmentName.concat("-").concat(ts.toString()).concat("-")
							.concat((String.valueOf(incSuffix)));
					iostream = invokeCognosReport(issue.getId().toString());

					doc = resFactory.createDocument(attachmentName, typeDef, fileTypeDef);
					doc.setContent(IOUtils.toByteArray(iostream));
					doc.setPrimaryParent(issueObj);
					resourceService.saveResource(doc);

				}
			}

		} catch (Exception e) {
			LoggerUtil.debugEXTLog(logger, "Audit Close", "ExceptionMessage", e.getLocalizedMessage());
			throw new Exception();
		}

	}

	private List<IAssociationNode> getChildren(IGRCObject obj) {
		GRCObjectFilter currRptPeriod = new GRCObjectFilter(configService.getCurrentReportingPeriod());
		AssociationFilter associationFilter = currRptPeriod.getAssociationFilter();
		associationFilter.setIncludeAssociations(IncludeAssociations.CHILD);
		associationFilter.setTypeFilters(metadataService.getType("Citi_Iss"));
		IGRCObject grcObj = resourceService.getGRCObject(obj.getId(), currRptPeriod);
		List<IAssociationNode> childNode = grcObj.getChildren();
		return childNode;
	}

	private InputStream invokeCognosReport(String issueId) throws Exception {

		boolean isSuccess = false;
		int attemptNumber = 1;

		while (!isSuccess) {
			try {
				reportParams = applicationService.getParametersForReport(getPropertyValue("REPORT_PATH_ONLY_FOR_PARAMTER"));
				LoggerUtil.debugEXTLog(logger, "Audit Close", "reportParams.getAllParameterNames()== ", reportParams.getAllParameterNames());
				reportParams.setParameterValue(resParam, issueId);
				iostream = applicationService.invokeCognosReport(reportPath, CognosOutputFormat.PDF, reportParams);
				isSuccess = true;
				LoggerUtil.debugEXTLog(logger, "Audit Close", "Message", "Report generation success");
			} catch (Exception e) {
				if (attemptNumber >= Integer.parseInt(getPropertyValue("DEFAULT_RETRY_ATTEMPTS"))) {
					throw e;
				}

				// Sleep before trying to execute the report again.
				Thread.sleep(Integer.parseInt(getPropertyValue("DEFAULT_RETRY_WAIT_MILLISECONDS")));

				// Increment attempt count.
				attemptNumber++;
			}
		}
		return iostream;

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
