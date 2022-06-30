package com.ibm.openpages.ext.citi.triggers.handlers;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.api.service.local.SessionFactory;
import com.ibm.openpages.api.trigger.events.CopyResourceEvent;

import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.ibm.openpages.ext.common.util.ServicesUtil;

import com.openpages.aurora.common.AuroraConstants;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.sdk.OpenpagesSession;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;

public class CopyAuditPhasewithAMR extends DefaultEventHandler {

	// Static variable initialization
	private static final String TRIGGER_NAME = "CopyAuditPhasewithAMR";
	private static final String ENABLE_DEBUG_MODE = "enable.debug.mode";
	private static final String LOG_FILE_PATH = "log.file.path";
	private static final String DISABLE_TRIGGER = "disable.trigger";
	private static final String ATTR_AUDPH_INDEX = "audph.IndexField";
	private static final String ATTR_AUD_DATE = "aud.DateField";
	private static final String ATTR_REP_NUM = "amr.RepNumField";
	//private static final String ATTR_BMQS_DATE = "bmqs.DateField";
	//private static final String ATTR_BMQS_INDEX = "bmqs.IndexField";

	private IServiceFactory serviceFactory = null;
	private IResourceService resourceService = null;
	private IConfigurationService configService = null;
	private IConfigProperties configProperties = null;

	private Logger logger = null;
	Context context = null;
	private static HashMap<String, String> attributes;

	public boolean handleEvent(CopyResourceEvent event) {

		try {
			context = event.getContext();
			attributes = getAttributes();
			String amrIndex = "";
			String dt = "";
			String year = "";

			serviceFactory = getOPSystemServiceFactory();
			resourceService = serviceFactory.createResourceService();
			configService = serviceFactory.createConfigurationService();
			configProperties = configService.getConfigProperties();

			String enableDebug = configProperties.getProperty(attributes.get(ENABLE_DEBUG_MODE));
			String logFilePath = configProperties.getProperty(attributes.get(LOG_FILE_PATH));

			logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);
			CommonResourceUtils commonResourceUtils = new CommonResourceUtils(logger);
			ServicesUtil servicesUtil = new ServicesUtil(event.getContext());

			if (isTriggerDisabled()) {
				LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "Trigger disabled => exiting handler", "");
				return true;
			}
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "", "Entered");
			Id tarObjId = event.getTargetResourceId();
			IGRCObject tarObj = resourceService.getGRCObject(tarObjId);
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "tarObj== ", tarObj.getName());

			IGRCObject audPhObj = resourceService.getGRCObject(event.getCopiedObjectId());
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "audPhObj== ", audPhObj.getName());

			IGRCObject amrObj = null;

			if (tarObj.getType().getName().equalsIgnoreCase("AuditProgram")) {
				List<IAssociationNode> amrList = GRCObjectUtil.getChildObjects(audPhObj.getId(), "Citi_AudMpReport",
						true, servicesUtil);

				for (IAssociationNode amr : amrList) {
					amrObj = resourceService.getGRCObject(amr.getId());
				}

				dt = GRCObjectUtil.getFieldValue(tarObj.getField(attributes.get(ATTR_AUD_DATE)));
				LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "dt== ", dt);
				if (!dt.isEmpty()) {
					amrIndex=GRCObjectUtil.getFieldValue(audPhObj, attributes.get(ATTR_AUDPH_INDEX));
					year = dt.substring(0, 4);

					String uniqueId = year + "-" + tarObj.getName() + "-" + amrIndex;
					LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "uniqueId== ", uniqueId);
					ResourceUtil.setFieldValue(amrObj.getField(attributes.get(ATTR_REP_NUM)), uniqueId);
					resourceService.saveResource(amrObj);

				}
				commonResourceUtils.setFieldValueOnResource(attributes.get(ATTR_AUDPH_INDEX),
						String.valueOf(Integer.parseInt(amrIndex) + 1), audPhObj, configService);
				resourceService.saveResource(audPhObj);
			}
			
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "", "Exited");

		} catch (Exception e) {
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "Exception Message== ", e.getLocalizedMessage());
		}
		return super.handleEvent(event);
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

	private boolean isTriggerDisabled() {
		String disableTrigger = configProperties.getProperty(attributes.get(DISABLE_TRIGGER));

		if (disableTrigger != null && disableTrigger.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}
}
