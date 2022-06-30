package com.ibm.openpages.ext.interfaces.cmp.service;

import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.EntitlementResponseBean;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public interface EntitlementService {

    /**
     *
     * @param dataEngineBean
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public EntitlementResponseBean createEntitlement(EngineResultsBean dataEngineBean, List<String> messageList,
                                                     IServiceFactory apiFactory, Logger logger);
}
