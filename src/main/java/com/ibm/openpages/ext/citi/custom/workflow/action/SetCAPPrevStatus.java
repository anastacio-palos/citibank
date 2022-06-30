package com.ibm.openpages.ext.citi.custom.workflow.action;

import java.util.List;
import org.apache.logging.log4j.core.Logger;

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
import com.ibm.openpages.ext.common.util.LoggerUtil;

public class SetCAPPrevStatus extends AbstractCustomAction {

	public SetCAPPrevStatus(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
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
			String workflowTransition = getPropertyValue("TRANSITION");
			
			logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);

			IGRCObject issObj = context.getResource();
			List<IAssociationNode> capList = getChildren("Citi_CAP", issObj);

			LoggerUtil.debugEXTLog(logger, "SetCAPPrevStatus", "workflowTransition", workflowTransition);
			LoggerUtil.debugEXTLog(logger, "SetCAPPrevStatus", "capList.size()", capList.size());
			
			if (capList.size() > 0) {
				
				for (IAssociationNode cap : capList) {

					IGRCObject capObj = resourceService.getGRCObject(cap.getId());
					IEnumField capStatusField = (IEnumField) capObj.getField(getPropertyValue("CAP_STATUS"));
					IEnumField prevStatusField = (IEnumField) capObj.getField(getPropertyValue("PREVIOUS_STATUS"));
					IEnumValue capStatusFieldValue=capStatusField.getEnumValue();
					IEnumValue prevStatusFieldValue=prevStatusField.getEnumValue();
					
					LoggerUtil.debugEXTLog(logger, "SetCAPPrevStatus", "capStatusFieldValue", capStatusFieldValue);
					LoggerUtil.debugEXTLog(logger, "SetCAPPrevStatus", "prevStatusFieldValue", prevStatusFieldValue);
					
					if (capStatusField!=null && prevStatusField!=null) {

						if (workflowTransition.equalsIgnoreCase("Initiate") || workflowTransition.equalsIgnoreCase("Send for Approval")){
							
								if (capStatusFieldValue != null) 
									prevStatusField.setEnumValue(prevStatusField.getFieldDefinition().getEnumValue(capStatusFieldValue.getName()));
						}
						
						if (workflowTransition.equalsIgnoreCase("Request Clarification") || workflowTransition.equalsIgnoreCase("Rescind")) {
							
							if (prevStatusFieldValue!=null)
								capStatusField.setEnumValue(capStatusField.getFieldDefinition().getEnumValue(prevStatusFieldValue.getName()));
						}
						
						resourceService.saveResource(capObj);
						LoggerUtil.debugEXTLog(logger, "SetCAPPrevStatus", "Message", "Success");
					}
				}
			}
		
		} catch (Exception e) {
			LoggerUtil.debugEXTLog(logger, "SetCAPPrevStatus", "ExceptionMessage", e.getLocalizedMessage());
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

}
