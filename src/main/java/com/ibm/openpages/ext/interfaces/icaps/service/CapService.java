package com.ibm.openpages.ext.interfaces.icaps.service;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IServiceFactory;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public interface CapService {
    /**
     * @param dataMap     set of data to use as input to retrieve cap
     * @param messageList list of messages used to save and retrieve at the end of the transaction
     * @param apiFactory  service to use current session from openpages
     * @param logger      object used to set error and debug messages in log file.
     * @return object found on openpages otherwise null
     */
    IGRCObject getCorrectiveActionPlan(Map<String, Object> dataMap, List<String> messageList,
                                       IServiceFactory apiFactory, Logger logger);

    /**
     * @param dataMap set of data to use as input to create a new cap
     * @param messageList  list of messages used to save and retrieve at the end of the transaction
     * @param apiFactory  service to use current session from openpages
     * @param logger      object used to set error and debug messages in log file.
     * @return true if new object was created, otherwise false
     */
    boolean createCorrectiveActionPlan(IGRCObject parentIssueObject, Map<String, Object> dataMap, List<String> messageList,
                                       IServiceFactory apiFactory, Logger logger);

    /**
     * @param dataMap set of data to use as input to retrieve cap and updated
     * @param messageList  list of messages used to save and retrieve at the end of the transaction
     * @param apiFactory  service to use current session from openpages
     * @param logger      object used to set error and debug messages in log file.
     * @return true if new object was updated, otherwise false
     */
    boolean updateCorrectiveActionPlan(Map<String, Object> dataMap, List<String> messageList,
                                       IServiceFactory apiFactory, Logger logger);
}
