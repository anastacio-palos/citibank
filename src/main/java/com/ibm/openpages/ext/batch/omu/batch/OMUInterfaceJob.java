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
public class OMUInterfaceJob extends AbstractScheduledProcess {
    @Autowired
    ILoggerUtil loggerUtil;
    private Log log;

    public OMUInterfaceJob() throws Exception {
        super(4);
    }

    public OMUInterfaceJob(int processType) {
        super(processType);
    }

    public Log initLogServices(String logname) {
        return OPSServiceFactory.getLoggerUtil().getExtLogger(logname);
    }

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
        EmailContent emailContent = new EmailContent(this.serviceFactory);
        emailContent.setStartDate(new Date());
        EmployeeService employeeService = new EmployeeServiceImpl(this.serviceFactory, this.loggerUtil);
        EmailDataBean emailData = new EmailDataBean();
        if (isrunning) {
            log.info("OMU job already running ignoring the run");
        } else {
            log.info("Executing batch=" + this.jobDetail);
            employeeService.processCRCUpdates(emailData);
            employeeService.processEmployeeDelta(emailData);
            List<EmployeeKPIBean> employeeKpiList = OmuUtil.getEmployeeKpiList(emailData.getEmployeeKPIDetailList(), log);
            if (emailData.getEmployeeKPIDetailList() != null)
                log.info("Number of OMU Updated=" + emailData.getEmployeeKPIDetailList().size());
            employeeService.processEmailService(emailContent, employeeKpiList, emailData);
        }
        Long endTime = System.currentTimeMillis();
        log.info("Total transaction time = " + (endTime - startTime) + " milliseconds");
    }

    private boolean isRunning() throws OpenpagesSessionException, OpenpagesServiceException {
        OpenpagesSession session = (OpenpagesSession) this.serviceFactory.getContext().get(Context.SERVICE_SESSION);
        AdminService adminService = session.getAdminService();
        ProcessOptions options = new ProcessOptions();
        options.addProcessType(ProcessConstants.TYPE_GENERAL);
        String processName = getName() + "%";
        options.addCondition(new SimpleCondition(Process.NAME, RelationalOperators.LIKE, processName));
        options.addStatus(ProcessStatus.STATUS_RUNNING.ordinal());
        @SuppressWarnings("unchecked")
        List<Process> processlogs = adminService.getProcessList(options);
        log.info("Status of is_running=" + processlogs.size());
        return processlogs != null && processlogs.size() > 1;
    }
}
