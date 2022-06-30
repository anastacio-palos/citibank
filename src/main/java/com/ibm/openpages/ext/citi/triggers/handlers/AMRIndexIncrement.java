package com.ibm.openpages.ext.citi.triggers.handlers;

import java.util.HashMap;
import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.api.service.local.SessionFactory;
import com.ibm.openpages.api.trigger.events.CreateResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.openpages.aurora.common.AuroraConstants;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.sdk.OpenpagesSession;
import com.ibm.openpages.ext.common.util.CommonResourceUtils;
import com.ibm.openpages.ext.common.util.GRCObjectUtil;

public class AMRIndexIncrement extends DefaultEventHandler {

	// Static variable initialization
	private static final String TRIGGER_NAME = "AMRIndexIncrement";
	private static final String ENABLE_DEBUG_MODE = "enable.debug.mode";
	private static final String LOG_FILE_PATH = "log.file.path";
	private static final String DISABLE_TRIGGER = "disable.trigger";
	private static final String ATTR_AUDPH_INDEX = "audph.IndexField";
	private static final String ATTR_AUDPHR_REPORT_FIELD = "audph.reportType";
	private static final String ATTR_AUDPHR_REPORT_VALUE = "audph.reportTypeValue";
	// private static final String ATTR_AUD_PREVMRCOUNT = "aud.PrevAMRCountField";
	private static final String ATTR_AUD_DATE = "aud.DateField";
	private static final String ATTR_REP_NUM = "amr.RepNumField";
	private static final String ATTR_BMQS_DATE = "bmqs.DateField";
	private static final String ATTR_BMQS_INDEX = "bmqs.IndexField";

	private IServiceFactory serviceFactory = null;
	private IResourceService resourceService = null;
	private IConfigurationService configService = null;
	private IConfigProperties configProperties = null;

	private Logger logger = null;
	Context context = null;
	private static HashMap<String, String> attributes;

	public boolean handleEvent(CreateResourceEvent event) {

		try {
			context = event.getContext();
			attributes = getAttributes();
			String amrIndex = "";
			// String prevAMRCount="";
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

			if (isTriggerDisabled()) {
				LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "Trigger disabled => exiting handler", "");
				return true;
			}
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "", "Entered");

			IGRCObject amrObj = (IGRCObject) event.getCreatedResource();
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "amrObj== ", amrObj.getName());

			IGRCObject amrParentObj = resourceService.getGRCObject(amrObj.getPrimaryParent());
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "amrParentObj== ", amrParentObj.getName());

			if (amrParentObj.getType().getName().equalsIgnoreCase("AuditPhase")) {

				String audPhReportType = GRCObjectUtil
						.getFieldValue(amrParentObj.getField(attributes.get(ATTR_AUDPHR_REPORT_FIELD)));

				if (audPhReportType.equalsIgnoreCase(attributes.get(ATTR_AUDPHR_REPORT_VALUE))) {

					IGRCObject audPhaseParentObj = resourceService.getGRCObject(amrParentObj.getPrimaryParent());
					LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "audPhaseParentObj== ", audPhaseParentObj.getName());

					if (audPhaseParentObj.getType().getName().equalsIgnoreCase("AuditProgram")) {
						amrIndex = GRCObjectUtil.getFieldValue(amrParentObj.getField(attributes.get(ATTR_AUDPH_INDEX)));
						LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "amrIndex== ", amrIndex);

						dt = GRCObjectUtil.getFieldValue(audPhaseParentObj.getField(attributes.get(ATTR_AUD_DATE)));
						LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "dt== ", dt);
						if (!dt.isEmpty()) {

							year = dt.substring(0, 4);

							String uniqueId = year + "-" + audPhaseParentObj.getName() + "-" + amrIndex;
							LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "uniqueId== ", uniqueId);
							ResourceUtil.setFieldValue(amrObj.getField(attributes.get(ATTR_REP_NUM)), uniqueId);
							resourceService.saveResource(event.getCreatedResource());

						}

					}
					commonResourceUtils.setFieldValueOnResource(attributes.get(ATTR_AUDPH_INDEX),
							String.valueOf(Integer.parseInt(amrIndex) + 1), amrParentObj, configService);
					resourceService.saveResource(amrParentObj);
				}
			}

			if (amrParentObj.getType().getName().equalsIgnoreCase("Citi_AudEntityClusEval")) {

				year = GRCObjectUtil.getFieldValue(amrParentObj.getField(attributes.get(ATTR_BMQS_DATE)));

				if (year != null) {

					amrIndex = GRCObjectUtil.getFieldValue(amrParentObj.getField(attributes.get(ATTR_BMQS_INDEX)));

					if (amrIndex != null) {

						String uniqueId = year + "-" + amrParentObj.getName() + "-" + amrIndex;
						LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "uniqueId== ", uniqueId);
						ResourceUtil.setFieldValue(amrObj.getField(attributes.get(ATTR_REP_NUM)), uniqueId);
						resourceService.saveResource(amrObj);
					}
					commonResourceUtils.setFieldValueOnResource(attributes.get(ATTR_BMQS_INDEX),
							String.valueOf(Integer.parseInt(amrIndex) + 1), amrParentObj, configService);
					resourceService.saveResource(amrParentObj);
				}
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
