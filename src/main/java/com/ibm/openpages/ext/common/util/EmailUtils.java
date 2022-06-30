package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.COLUMN_DELIM;
import static com.ibm.openpages.ext.common.util.CommonConstants.EMAIL_CONTENT_URL;
import static com.ibm.openpages.ext.common.util.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_NEW_LINE;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_TABLE_CLOSE_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_TABLE_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_TABLE_TD_CLOSE_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_TABLE_TD_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_TABLE_TH_CLOSE_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_TABLE_TH_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_TABLE_TR_CLOSE_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.HTML_TABLE_TR_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.OPTABLE;
import static com.ibm.openpages.ext.common.util.CommonConstants.ROW_DELIM;
import static com.ibm.openpages.ext.common.util.CommonConstants.STR_EMAIL_BODY_END_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.STR_EMAIL_BODY_START_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.STR_EMAIL_HTML_END_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.STR_EMAIL_HTML_START_TAG;
import static com.ibm.openpages.ext.common.util.CommonConstants.STR_EMAIL_TABLE_STYLE;
import static com.ibm.openpages.ext.common.util.CommonConstants.SYSTEM_FIELDS_DESCRIPTION;
import static com.ibm.openpages.ext.common.util.CommonConstants.SYSTEM_FIELDS_NAME;
import static com.ibm.openpages.ext.common.util.CommonUtil.isNotNullOrEmpty;

import java.util.List;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.application.EmailUtil;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.openpages.aurora.common.AuroraEnv;

public class EmailUtils
{

	private Logger logger;
	private Context context;

	public EmailUtils(Context context, Logger logger)
	{
		this.logger = logger;
		this.context = context;
	}

	public void sendEmail(EmailDetailsBean emailDetailsBean)
	{
		String uri = AuroraEnv.getAttribute("application.url.path") + "/view.resource.do?fileId=" + emailDetailsBean.getOpObjectId().toString();
		uri = "<a href=\"" + uri + "\">" + emailDetailsBean.getOpObjectName() + "</a>";
		logger.debug("Entered sendEmail; EmailDetailsBean: " + emailDetailsBean);
		logger.debug("before sending email - uri: " + uri);

		try
        {
            List<String> destinationEmailAddresses = emailDetailsBean.getDestinationAddresses();
            logger.debug("destinationEmailAddresses: " + destinationEmailAddresses);
            String destinationEmailAddressesStr = "";
            for (String email : destinationEmailAddresses) {
                destinationEmailAddressesStr += "," + email;
            }
            if (destinationEmailAddressesStr.length() > 0) {
                destinationEmailAddressesStr = destinationEmailAddressesStr.substring(1);
            }
            
            EmailUtil.sendEmail(emailDetailsBean.getSourceAddress(), destinationEmailAddressesStr,
                    emailDetailsBean.getSubjectAppText(), new Object[] { emailDetailsBean.getOpObjectName() },
                    emailDetailsBean.getBodyAppText(), new Object[] { uri }, false, context, null,
                    emailDetailsBean.getLocale());
        }
		catch (Exception e)
		{
			logger.error("Exception while sending email." + e);
		}
	}

	public static String formatEmailContent(String applicationTxt, List<IGRCObject> objectList, Logger logger) throws Exception
	{
		String METHOD_NAME = "formatEmailContent";
		String formattedString = "";
		String tableContent = applicationTxt;
		if (!applicationTxt.isEmpty())
		{
			LoggerUtil.debugEXTLog(logger, "", METHOD_NAME, "applicationTxt == " + applicationTxt);
			LoggerUtil.debugEXTLog(logger, "", METHOD_NAME, "applicationTxt.indexOf(OPTABLE) - 1 == " + applicationTxt.indexOf(OPTABLE));
			LoggerUtil.debugEXTLog(logger, "", METHOD_NAME, "applicationTxt.indexOf(], applicationTxt.indexOf(OPTABLE)) == " + applicationTxt.indexOf("]", applicationTxt.indexOf(OPTABLE)));
			String opTable = applicationTxt.substring(applicationTxt.indexOf(OPTABLE) - 1, applicationTxt.indexOf("]", applicationTxt.indexOf(OPTABLE)) + 1);
			if (!opTable.isEmpty())
			{
				formattedString = formattedString + STR_EMAIL_HTML_START_TAG + STR_EMAIL_BODY_START_TAG;
				formattedString = formattedString + STR_EMAIL_TABLE_STYLE;

				String[] opTableArray = opTable.split(ROW_DELIM);
				String[] tableHeader = opTableArray[1].split(COLUMN_DELIM);

				formattedString = formattedString + HTML_NEW_LINE + HTML_NEW_LINE + HTML_TABLE_TAG + HTML_TABLE_TR_TAG;
				for (String tabHeader : tableHeader)
				{
					formattedString = formattedString + HTML_TABLE_TH_TAG + tabHeader + HTML_TABLE_TH_CLOSE_TAG;
				}
				formattedString = formattedString + HTML_TABLE_TR_CLOSE_TAG;
				String[] tableData = opTableArray[2].split(COLUMN_DELIM);

				for (IGRCObject object : objectList)
				{
					formattedString = formattedString + HTML_TABLE_TR_TAG;
					for (String tabData : tableData)
					{
						if (tabData.endsWith("]"))
							tabData = tabData.substring(0, tabData.length() - 1);

						if (tabData.contains(SYSTEM_FIELDS_NAME))
						{
							if (tabData.startsWith(EMAIL_CONTENT_URL))
							{
								tabData = tabData.replace(EMAIL_CONTENT_URL, "").trim();
								formattedString = formattedString + HTML_TABLE_TD_TAG + (CommonUtil.isObjectNull(object.getName()) ? EMPTY_STRING : GRCObjectUtil.getRelativeResourceURL(object.getName(), object.getId().toString())) + HTML_TABLE_TD_CLOSE_TAG;
							}
							else
							{
								formattedString = formattedString + HTML_TABLE_TD_TAG + (CommonUtil.isObjectNull(object.getName()) ? EMPTY_STRING : object.getName()) + HTML_TABLE_TD_CLOSE_TAG;
							}
						}
						else if (tabData.contains(SYSTEM_FIELDS_DESCRIPTION))
						{
							if (tabData.startsWith(EMAIL_CONTENT_URL))
							{
								tabData = tabData.replace(EMAIL_CONTENT_URL, "").trim();
								formattedString = formattedString + HTML_TABLE_TD_TAG + (CommonUtil.isObjectNull(object.getDescription()) ? EMPTY_STRING : GRCObjectUtil.getRelativeResourceURL(object.getDescription(), object.getId().toString())) + HTML_TABLE_TD_CLOSE_TAG;
							}
							else
							{

								formattedString = formattedString + HTML_TABLE_TD_TAG + (CommonUtil.isObjectNull(object.getDescription()) ? EMPTY_STRING : object.getDescription()) + HTML_TABLE_TD_CLOSE_TAG;
							}
						}
						else
						{
							IField objField = object.getField(tabData);
							formattedString = formattedString + HTML_TABLE_TD_TAG + getFormattedUserNameOrGroup(GRCObjectUtil.getFieldValue(objField)) + HTML_TABLE_TD_CLOSE_TAG;
						}
					}
					formattedString = formattedString + HTML_TABLE_TR_CLOSE_TAG;
				}
				formattedString = formattedString + HTML_TABLE_CLOSE_TAG + HTML_NEW_LINE;
				formattedString = formattedString + STR_EMAIL_BODY_END_TAG + STR_EMAIL_HTML_END_TAG;
				tableContent = tableContent.replace(opTable, formattedString);
				LoggerUtil.debugEXTLog(logger, "", METHOD_NAME, "tableContent == " + tableContent);
			}
		}
		return tableContent;
	}

	/**
	 * @param name
	 * @return
	 */
	public static String getFormattedUserNameOrGroup(String name)
	{
		String userName = "";

		if (isNotNullOrEmpty(name.trim()))
		{
			String nameSubStr = name.trim();

			if (nameSubStr.contains("$;"))
			{
				nameSubStr = nameSubStr.substring(2, nameSubStr.length() - 2);

				userName = nameSubStr.replace("$;", ",");
			}
			else
				userName = nameSubStr;
		}

		return userName.trim();
	}

}
