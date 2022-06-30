package com.ibm.openpages.ext.interfaces.cmp.service;

import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public interface FieldService {

    /**
     *
     * @param backUpBean
     * @param apiFactory
     * @param isIADivisionLevel
     * @param logger
     */
    public void getIdsFromEntityObject(UserBean backUpBean, IServiceFactory apiFactory, boolean isIADivisionLevel, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean addUserToLeadProductTeam(Map<String, Object> dataMap, List<String> messageList,
                                            IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean addUserToProductFunctionTeam(Map<String, Object> dataMap, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean removeUserFromProductFunctionTeam(Map<String, Object> dataMap, List<String> messageList,
                                                     IServiceFactory apiFactory,  Logger logger);

    /**
     *
     * @param dataMap
     * @param messageList
     * @param apiFactory
     * @param userBackUp
     * @param logger
     * @return
     */
    public boolean removeUserFromLeadProductTeam(Map<String, Object> dataMap, List<String> messageList,
                                                 IServiceFactory apiFactory, UserBean userBackUp, Logger logger);


    /**
     *
     * @param user
     * @param apiFactory
     * @param logger
     * @return
     */
    public Map<String, String> getAllLeadProductTeam(String user, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param user
     * @param apiFactory
     * @param logger
     * @return
     */
    public Map<String, String> getAllProductFunctionTeam(String user, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param user
     * @param apiFactory
     * @param fieldTableParameter
     * @param entityLevel
     * @return
     */
    public Map<String, String> getProductsFromUser(String user, IServiceFactory apiFactory, String fieldTableParameter, String entityLevel);
}
