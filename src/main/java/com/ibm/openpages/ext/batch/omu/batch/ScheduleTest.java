package com.ibm.openpages.ext.batch.omu.batch;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.process.ProcessStatus;
import com.ibm.openpages.api.scheduler.AbstractScheduledProcess;
import com.ibm.openpages.ext.batch.omu.bean.EmailDataBean;
import com.ibm.openpages.ext.batch.omu.bean.EmployeeKPIBean;
import com.ibm.openpages.ext.batch.omu.constant.OmuRegistryConstants;
import com.ibm.openpages.ext.batch.omu.notification.EmailContent;
import com.ibm.openpages.ext.batch.omu.service.EmployeeService;
import com.ibm.openpages.ext.batch.omu.service.impl.EmployeeServiceImpl;
import com.ibm.openpages.ext.batch.omu.util.OmuUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.config.OPSServiceFactory;
import com.ibm.openpages.ext.tss.service.impl.LoggerUtil;
import com.openpages.sdk.OpenpagesServiceException;
import com.openpages.sdk.OpenpagesSession;
import com.openpages.sdk.OpenpagesSessionException;
import com.openpages.sdk.admin.AdminService;
import com.openpages.sdk.admin.process.Process;
import com.openpages.sdk.admin.process.ProcessConstants;
import com.openpages.sdk.admin.process.ProcessOptions;
import com.openpages.sdk.search.RelationalOperators;
import com.openpages.sdk.search.SimpleCondition;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * The <code>OmuEmployeeBach</code> contains the methods used to interact with Openpages objects this is used in Openpages scheduler
 * </p>
 *
 * @author Abdias Morales, Eduardo Alvarez<BR>
 * email : abdias.morales@ibm.com, eduardo.alvarez@ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 21-12-2021
 */
public class ScheduleTest extends AbstractScheduledProcess {
    @Autowired
    ILoggerUtil loggerUtil;
    private Log log;

    public ScheduleTest() throws Exception {
        super(4);
    }

    public ScheduleTest(int processType) {
        super(processType);
    }

    /*public Log initLogServices(String logname) {
        return OPSServiceFactory.getLoggerUtil().getExtLogger(logname);
    }*/

    public void init() throws Exception {
        if (this.loggerUtil == null) {
            this.loggerUtil = new LoggerUtil();
            this.loggerUtil.initService();
        }
        this.log = loggerUtil.getExtLogger(OmuRegistryConstants.OMU_INTERFACE_LOGGER);
    }


    @Override
    public void execute() throws Exception {
        init();
        Long startTime = System.currentTimeMillis();
        boolean isrunning = isRunning();
        log.debug("Scheduler into execute");

        if (isrunning) {
            log.info("Schedule job already running");
        } else {
            log.info("Executing batch=" + this.jobDetail);

        }

    }

    private boolean isRunning() { //throws OpenpagesSessionException, OpenpagesServiceException {

        //@SuppressWarnings("unchecked")

        log.info("Status of is_running=" );
        return true;
    }
}
