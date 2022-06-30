package com.ibm.openpages.ext.interfaces.cmp.service;

import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public IUser getUser(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public UserBean getUserBean(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean createUser(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean updateUser(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean deleteUser(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

}
