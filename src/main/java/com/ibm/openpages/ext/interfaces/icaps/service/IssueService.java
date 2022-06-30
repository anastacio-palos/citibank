package com.ibm.openpages.ext.interfaces.icaps.service;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import com.ibm.openpages.ext.interfaces.icaps.bean.IssueResponseBean;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public interface IssueService {

	/**
	 *
	 * @param dataMap set of data used as input to retrieve Issue from database
	 * @param messageList list of messages used to save and retrieve at the end of the transaction
	 * @param apiFactory service to use current session from openpages
	 * @param logger object used to set error and debug messages in log file.
	 * @return Issue found in openpages database otherwise null.
	 */
	IGRCObject getIssue(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
			Logger logger);

	/**
	 *
	 * @param dataMap set of data used as input to create new Issue in database
	 * @param messageList list of messages used to save and retrieve at the end of the transaction
	 * @param apiFactory service to use current session from openpages
	 * @param logger object used to set error and debug messages in log file.
	 * @return true if new issue was created false otherwise
	 */
	boolean createIssue(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory,
			Logger logger);

	/**
	 *
	 * @param Issue object used as input to be updated
	 * @param dataMap set of data used as input to retrieve and update Issue from database
	 * @param messageList list of messages used to save and retrieve at the end of the transaction
	 * @param apiFactory service to use current session from openpages
	 * @param logger object used to set error and debug messages in log file.
	 * @return true if issue was updated otherwise false.
	 */
	boolean updateIssue(IGRCObject Issue, Map<String, Object> dataMap, List<String> messageList,
			IServiceFactory apiFactory, Logger logger);

	/**
	 *
	 * @param dataEngineBean bean from validation engine, used as input to validate if something failed.
	 * @param messageList list of messages used to save and retrieve at the end of the transaction
	 * @param apiFactory  service to use current session from openpages
	 * @param logger object used to set error and debug messages in log file.
	 * @return final response with success or failure with message details of the transaction.
	 */
	IssueResponseBean createOrUpdateIssueAndCAP(EngineResultsBean dataEngineBean, List<String> messageList,
			IServiceFactory apiFactory, Logger logger);

}
