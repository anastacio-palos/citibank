package com.ibm.openpages.ext.citi.triggers.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.trigger.events.CopyResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;

public class AuditPhaseCopyValidateTriggerHandler extends DefaultEventHandler{

	
	private static final String AUDIT_TYPE = "AuditProgram";

	private static final String AUDIT_PHASE_TYPE = "AuditPhase";

	private static final String AUDIT_TYPE_FIELD = "audit.type.field";
	
	private static final String AUDIT_TYPE_RISK_VALUE = "audit.type.risk.val";
	
	private static final String	AUDIT_TYPE_OTHER_VALUE = "audit.type.other.val";
	
	private static final String AUDIT_SUB_TYPE_FIELD = "audit.sub.type.field";

	private static final String	AUDIT_SUB_TYPE_CONT_AUD_VALUE = "audit.sub.type.cont.aud.val";
	
	private static final String AUDIT_PHASE_TYPE_FIELD = "audit.phase.type.field";

	private static final String RISK_BASED_AUDIT_PHASES = "risk.based.auditphases";
	
	private static final String CONT_AUDIT_BASED_AUDIT_PHASES = "cont.aud.auditphases";

	private static final String NON_CONT_AUD_BASED_AUDIT_PHASES = "non.cont.aud.auditphases";
	
    private static final String INCORRECT_AUDIT_PHASE_ERROR_MSG = "app.text.error.incorrect.audit.phase";

    private static final String AUDIT_PHASE_ALREADY_EXISTS_ERROR_MSG = "app.text.error.audit.phase.already.exists";
	
	private ServicesUtil servicesUtil = null;
	
	private CommonResourceUtils commonResourceUtils = null;

    private static Logger logger = null;

    private static final String ENABLE_DEBUG = "enable.debug.mode";

    private static final String LOG_FILE_PATH = "log.file.path";
    

	@Override
	public boolean handleEvent(CopyResourceEvent event) {
		
		servicesUtil = new ServicesUtil(event.getContext());
		
		IGRCObject auditObj = null;

		IGRCObject auditPhaseObj = null;

		try {
			
	        String enableDebug = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), servicesUtil.getConfigProperties());
	        
	        String logFilePath = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());

	        String auditTypeField = TriggerUtil.getAttributeValue(AUDIT_TYPE_FIELD, getAttributes());
	        
	        String auditTypeRiskVal = TriggerUtil.getAttributeValue(AUDIT_TYPE_RISK_VALUE, getAttributes());

	        String auditTypeOtherVal = TriggerUtil.getAttributeValue(AUDIT_TYPE_OTHER_VALUE, getAttributes());

	        String auditSubTypeField = TriggerUtil.getAttributeValue(AUDIT_SUB_TYPE_FIELD, getAttributes());

	        String auditSubTypeContAudVal = TriggerUtil.getAttributeValue(AUDIT_SUB_TYPE_CONT_AUD_VALUE, getAttributes());

	        String auditPhaseTypeField = TriggerUtil.getAttributeValue(AUDIT_PHASE_TYPE_FIELD, getAttributes());

	        String riskBasedAudPhase = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(RISK_BASED_AUDIT_PHASES, getAttributes()), servicesUtil.getConfigProperties());

	        String contAudBasedAudPhase = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(CONT_AUDIT_BASED_AUDIT_PHASES, getAttributes()), servicesUtil.getConfigProperties());

        	String nonContAudBasedAudPhase = ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(NON_CONT_AUD_BASED_AUDIT_PHASES, getAttributes()), servicesUtil.getConfigProperties());
        	
	        String incorrectAuditPhaseErrorMsg = ApplicationUtil.getApplicationText(TriggerUtil.getAttributeValue(INCORRECT_AUDIT_PHASE_ERROR_MSG, getAttributes()), servicesUtil.getConfigurationService());

	        String auditPhaseAlreadyExistsErrorMsg = ApplicationUtil.getApplicationText(TriggerUtil.getAttributeValue(AUDIT_PHASE_ALREADY_EXISTS_ERROR_MSG, getAttributes()), servicesUtil.getConfigurationService());

	        logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);
	        
	        commonResourceUtils = new CommonResourceUtils(logger);
	        
	        auditObj = servicesUtil.getResourceService().getGRCObject(event.getTargetResourceId());

	        auditPhaseObj = servicesUtil.getResourceService().getGRCObject(event.getSourceResouceId());

	        if(!auditObj.getType().getName().equalsIgnoreCase(AUDIT_TYPE) || !auditPhaseObj.getType().getName().equalsIgnoreCase(AUDIT_PHASE_TYPE))
	        {
		        debug("Current copy is not from AuditPhase to Audit -- Exiting Trigger");
	        	return true;
	        }
	        
	        debug("-----------------------Entered AuditPhase Copy Validate Trigger Handler-----------------------");

	        String currentAuditType = GRCObjectUtil.getFieldValue(auditObj, auditTypeField);
	        
	        String currentAuditSubType = GRCObjectUtil.getFieldValue(auditObj, auditSubTypeField);      
	        
	        String currentAuditPhaseType = GRCObjectUtil.getFieldValue(auditPhaseObj, auditPhaseTypeField);
	        
	        List<String> riskBasedAudPhases = Arrays.asList(riskBasedAudPhase.split(","));
	        
	        List<String> contAudBasedAudPhases = Arrays.asList(contAudBasedAudPhase.split(","));
	        
	        List<String> nonContAudBasedAudPhases = Arrays.asList(nonContAudBasedAudPhase.split(","));

	        if((currentAuditType.equalsIgnoreCase(auditTypeRiskVal) && riskBasedAudPhases.contains(currentAuditPhaseType)) ||
	           (currentAuditType.equalsIgnoreCase(auditTypeOtherVal) && currentAuditSubType.equalsIgnoreCase(auditSubTypeContAudVal) && contAudBasedAudPhases.contains(currentAuditPhaseType)) ||
	           (currentAuditType.equalsIgnoreCase(auditTypeOtherVal) && !currentAuditSubType.equalsIgnoreCase(auditSubTypeContAudVal) && nonContAudBasedAudPhases.contains(currentAuditPhaseType)))
	        {
	        	
		        debug("Trying to Copy AuditPhase to Audit");

	        	ArrayList<String> auditPhaseType = new ArrayList<String>();
	          	  
	        	auditPhaseType.add(AUDIT_PHASE_TYPE);
            	
	        	IGRCObject auditObjWithChildren = commonResourceUtils.getGRCObjectWithChildren(auditObj.getId(), auditPhaseType, servicesUtil.getServiceFactory());
	          	  
          	  	List<IAssociationNode> childAuditPhaseNodes = auditObjWithChildren.getChildren();
          	  	
          	  	for(IAssociationNode childAuditPhaseNode : childAuditPhaseNodes)
          	  	{
          	  		IGRCObject childAudPhaseObj = servicesUtil.getResourceService().getGRCObject(childAuditPhaseNode.getId());
          	  		
          	  		if(GRCObjectUtil.getFieldValue(childAudPhaseObj, auditPhaseTypeField).equalsIgnoreCase(currentAuditPhaseType))
          	  		{
          	  			auditPhaseAlreadyExistsErrorMsg = auditPhaseAlreadyExistsErrorMsg.replace("{0}", currentAuditPhaseType);
                    	throwException(auditPhaseAlreadyExistsErrorMsg,new ArrayList<Object>(), new Exception(), event.getContext());
          	  		}
          	  		
          	  	}
	        }
	        
	        else
	        {
            	throwException(incorrectAuditPhaseErrorMsg,new ArrayList<Object>(), new Exception(), event.getContext());
	        }
	        
		}
        
		catch (Exception e) {
			// TODO Auto-generated catch block
        	throwException(e.getMessage(),new ArrayList<Object>(), new Exception(), event.getContext());
			e.printStackTrace();
		}
		
		debug("AuditPhase Copied to Audit");

		debug("END AuditPhase Copy Validate Trigger Handler");
		return true;
	}
	
	
    private void debug(String string)
    {

        LoggerUtil.debugEXTLog(logger, "", "", string);
    }
    
}

