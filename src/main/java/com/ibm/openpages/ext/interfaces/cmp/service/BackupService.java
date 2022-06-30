package com.ibm.openpages.ext.interfaces.cmp.service;

import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.UserBean;
import org.apache.logging.log4j.Logger;

import java.util.List;

public interface BackupService {

    /**
     * <p>Method that restore user data.</p>
     * @param userToRestore
     * @param messageList
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean restoreUserData(UserBean userToRestore, List<String> messageList, IServiceFactory apiFactory, Logger logger);

    /**
     * <p>Method that resets user's audit groups.</p>
     * @param backUpBean
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean restoreProductFunctionTeam(UserBean backUpBean, IServiceFactory apiFactory, Logger logger);

    /**
     *
     * @param backUpBean
     * @param apiFactory
     * @param logger
     * @return
     */
    public boolean restoreLeadProductTeam(UserBean backUpBean, IServiceFactory apiFactory, Logger logger);
}
