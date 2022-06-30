package com.ibm.openpages.ext.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.ObjectNotFoundException;
import com.ibm.openpages.api.application.ICognosReportRow;
import com.ibm.openpages.api.application.ICognosReportRowReader;
import com.ibm.openpages.api.application.IReportParameters;
import com.ibm.openpages.api.security.IGroup;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.IApplicationService;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.ServiceFactory;

public class CognosReportUtils
{
	private Logger logger;
	private Context context;
	private IApplicationService applicationService;
	private ISecurityService securityService;

	private static final String DEFAULT_COGNOS_REPORT_PATH_ACTIONS = "/content/folder[@name='Custom Reports']/folder[@name='Trigger Reports']/report[@name='User Access Emails grouped by Resource']";
	private static final int DEFAULT_COGNOS_REPORT_RESULT_EMAIL_COLUMN_INDEX = 3;

	public CognosReportUtils(Logger logger, Context context)
	{
		this.logger = logger;
		this.context = context;
		this.applicationService = ServiceFactory.getServiceFactory(context).createApplicationService();
		this.securityService = ServiceFactory.getServiceFactory(context).createSecurityService();
	}

	public ICognosReportRowReader runCognosReport(String cognosReportPathActions, Map<String, String> parametersMap)
	{
//		logger.debug("Entered runCognosReport(..)");
		ICognosReportRowReader resultAsReportRowReader = null;
		try
		{
			IReportParameters parameters = applicationService.getParametersForReport(cognosReportPathActions);

			for (String parameterName : parametersMap.keySet())
			{
				String parameterValue = parametersMap.get(parameterName);
				parameters.setParameterValue(parameterName, parameterValue);
//				logger.debug("setting parameter " + parameterName + " to be " + parameterValue);
			}
			resultAsReportRowReader = applicationService.invokeCognosRowReport(cognosReportPathActions, parameters);
		}
		catch (Exception e)
		{
			logger.error("Error occurred trying to execute report cognos report at path:" + cognosReportPathActions);
		}
		return resultAsReportRowReader;
	}

	public List<String> getValuesFromRowsAtColumnIndex(ICognosReportRowReader reader, int columnIndex)
	{
		List<String> values = new ArrayList<String>();
		for (ICognosReportRow row : reader)
		{
			String employeeEmailAddress = (String) row.getColumnValue(columnIndex).getValue();// idx=3
			values.add(employeeEmailAddress);
			//logger.error("row.getColumnValue(" + columnIndex + ")");
		}

		return values;
	}

	public List<String> getDestinationEmailsFromUserOrUserGroupFieldUsingDefaultCognosReport(String userOrUserGroupFieldValue, String resourceIdParam)
	{
		List<String> destinationEmails = new ArrayList<String>();
		//		logger.debug("entered getDestinationEmailsFromUserOrUserGroupField(..); userOrUserGroupFieldValue: " + userOrUserGroupFieldValue + ", resourceIdParam: " + resourceIdParam);
		try
		{
			IUser user = securityService.getUser(userOrUserGroupFieldValue.toString());
			destinationEmails.add(user.getEmailAddress());
//			logger.debug("field value contains user name(not user group name); user name: " + user.getName() + ", email address: " + user.getEmailAddress());
		}
		catch (ObjectNotFoundException eu)
		{
//			logger.debug(eu.getMessage(), eu);
//			logger.debug(eu.getCause());
			try
			{
				IGroup userGroup = securityService.getGroup(userOrUserGroupFieldValue.toString());
//				logger.debug("field value contains user group(not user name); user name: " + userGroup.getName());

				CognosReportUtils cognosReportUtils = new CognosReportUtils(logger, context);
				Map<String, String> cognosParametersMap = new HashMap<String, String>();
				cognosParametersMap.put("groupName", userGroup.getName());
				cognosParametersMap.put("resourceId", resourceIdParam);
				ICognosReportRowReader cognosRowReader = cognosReportUtils.runCognosReport(DEFAULT_COGNOS_REPORT_PATH_ACTIONS, cognosParametersMap);

				destinationEmails = cognosReportUtils.getValuesFromRowsAtColumnIndex(cognosRowReader, DEFAULT_COGNOS_REPORT_RESULT_EMAIL_COLUMN_INDEX);
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage(), e);
				logger.debug(e.getCause());
			}
		}
		return destinationEmails;
	}
}
