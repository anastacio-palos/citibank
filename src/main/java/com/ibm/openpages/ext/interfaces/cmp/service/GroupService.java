package com.ibm.openpages.ext.interfaces.cmp.service;

import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public interface GroupService {

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean addRoles(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean addGroupsCountry(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean removeRoles(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean removeAllGroups(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean removeGroupsCountry(Map<String, Object> dataMap, List<String> messageList,
                                       IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean addGroupHelpDesk(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean removeGroupHelpDesk(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);




}
