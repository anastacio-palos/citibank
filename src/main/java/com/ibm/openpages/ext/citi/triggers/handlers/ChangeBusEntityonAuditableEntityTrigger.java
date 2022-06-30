package com.ibm.openpages.ext.citi.triggers.handlers;

/*
 * Created by:Sriram Nagappan
 * Purpose:Disassociate AE from existing Lead Product Team and Associate to new Lead Product Team
 * Date:07/10/2021
 */

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.core.Logger;
import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.application.MoveObjectOptions;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IEnumField;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IIdField;
import com.ibm.openpages.api.resource.IStringField;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.ServiceFactory;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.api.service.local.SessionFactory;
import com.ibm.openpages.api.trigger.events.UpdateResourceEvent;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import com.openpages.aurora.common.AuroraConstants;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.sdk.OpenpagesSession;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.query.ParameterValue;
import com.ibm.openpages.api.query.IQuery;

public class ChangeBusEntityonAuditableEntityTrigger extends DefaultEventHandler {

	// Static variable initialization
	private static final String TRIGGER_NAME = "ChangeBusEntityonAuditableEntityTrigger";
	private static final String ENABLE_DEBUG_MODE = "enable.debug.mode";
	private static final String LOG_FILE_PATH = "log.file.path";
	private static final String DISABLE_TRIGGER = "disable.trigger";
	private static final String queryStmnt = "SELECT [Resource ID] FROM [SOXBusEntity] WHERE [SOXBusEntity].[Name]=?";
	private static final String ATTR_AE_ERRORMESSSAGE = "ae.ChangeBusEntityonAuditableEntityTrigger.AppText";
	private static final String ATTR_AE_LEADPRODUCTTEAMAPPR = "lead.prod.team.appr.field";
	private static final String ATTR_AE_PRPLEADPRODUCTTEAM = "prp.lead.prod.team.field";
	private static final String ATTR_AE_APPROVALFLAG = "apprl.flag.field";

	private IServiceFactory serviceFactory = null;
	private IResourceService resourceService = null;
	private IConfigurationService configService = null;
	private IConfigProperties configProperties = null;
	private IQueryService queryService = null;
	private Logger logger = null;
	Context context = null;
	private static HashMap<String, String> attributes;
	boolean validationMessage = true;

	public boolean handleEvent(UpdateResourceEvent event) {

		try {
			context = event.getContext();

			attributes = getAttributes();

			serviceFactory = ServiceFactory.getServiceFactory(context);
			resourceService = serviceFactory.createResourceService();
			configService = serviceFactory.createConfigurationService();
			configProperties = configService.getConfigProperties();
			queryService = serviceFactory.createQueryService();

			String enableDebug = configProperties.getProperty(attributes.get(ENABLE_DEBUG_MODE));
			String logFilePath = configProperties.getProperty(attributes.get(LOG_FILE_PATH));

			logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);

			if (isTriggerDisabled()) {
				LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "Trigger disabled => exiting handler", "");
				return true;
			}

			IGRCObject aeObj = (IGRCObject) event.getUpdatedResource();
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "AE== ", aeObj.getName());

			IEnumField approvalField = (IEnumField) aeObj.getField(attributes.get(ATTR_AE_LEADPRODUCTTEAMAPPR));
			if (approvalField != null) {
				IEnumValue approvalFieldValue = approvalField.getEnumValue();
				if (approvalFieldValue != null) {

					if (approvalFieldValue.getName().equalsIgnoreCase("Yes")) {

						IStringField proposedLeadProductTeamField = (IStringField) aeObj
								.getField(attributes.get(ATTR_AE_PRPLEADPRODUCTTEAM));

						if (proposedLeadProductTeamField != null) {

							String proposedLeadProductTeamFieldVal = proposedLeadProductTeamField.getValue();

							if (proposedLeadProductTeamFieldVal != null) {

								if (proposedLeadProductTeamFieldVal.contains("/")) {

									Integer index = proposedLeadProductTeamFieldVal.lastIndexOf("/");
									proposedLeadProductTeamFieldVal = proposedLeadProductTeamFieldVal
											.substring(index + 2);
								}
								LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "proposedLeadProductTeamFieldVal== ",
										proposedLeadProductTeamFieldVal);
								IQuery query = queryService.buildQuery(queryStmnt);
								ParameterValue param1 = new ParameterValue(proposedLeadProductTeamFieldVal);
								query.bindParameter(1, param1);
								ITabularResultSet tabularResultSet = query.fetchRows(0);

								for (IResultSetRow rowResultSet : tabularResultSet) {

									for (IField field : rowResultSet) {
										IIdField busEntId = (IIdField) field;
										IGRCObject proposedLeadProductTeamObj = resourceService
												.getGRCObject(busEntId.getValue());

										LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "proposedLeadProductTeamObj== ",
												proposedLeadProductTeamObj.getName());
										Id parentBE = aeObj.getPrimaryParent();

										if (!proposedLeadProductTeamObj.getId().equals(parentBE)) {
											ArrayList<Id> aeObjId = new ArrayList<>();
											aeObjId.add(aeObj.getId());
											getOPSystemServiceFactory().createApplicationService().moveGRCObject(
													proposedLeadProductTeamObj.getId(), aeObjId,
													new MoveObjectOptions());
											
											IGRCObject movedAEObj = resourceService.getGRCObject(aeObj.getId());
											validationMessage = false;
											IField movedAEObjPropLPT = movedAEObj
													.getField(attributes.get(ATTR_AE_PRPLEADPRODUCTTEAM));
											ResourceUtil.setFieldValue(movedAEObjPropLPT, "");

											IEnumField apprvlFlg = (IEnumField) movedAEObj
													.getField(attributes.get(ATTR_AE_APPROVALFLAG));
											ResourceUtil.setFieldValue(apprvlFlg, "No");

											getOPSystemServiceFactory().createResourceService()
													.saveResource(movedAEObj);
											LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "Message== ", "Success");
										}
									}

								}
							}
						}

					} else {
						validationMessage = false;
					}
				}
			}
			if (validationMessage)
				throw new Exception(configService.getLocalizedApplicationText(attributes.get(ATTR_AE_ERRORMESSSAGE)));

		} catch (Exception e) {
			LoggerUtil.debugEXTLog(logger, TRIGGER_NAME, "Exception Message== ", e.getLocalizedMessage());
			super.throwException(e.getMessage(), new ArrayList<Object>(), e, event.getContext());
		}

		return super.handleEvent(event);

	}

	private boolean isTriggerDisabled() {
		String disableTrigger = configProperties.getProperty(attributes.get(DISABLE_TRIGGER));

		if (disableTrigger != null && disableTrigger.equalsIgnoreCase("true"))
			return true;
		else
			return false;
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
