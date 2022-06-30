package com.ibm.openpages.ext.ui.controller;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ibm.openpages.ext.tss.service.ILoggerUtil;

import com.ibm.openpages.ext.ui.bean.AuditPlanBenchmarkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;
import com.ibm.openpages.ext.ui.service.IAuditPlanBenchmarkHelperService;
import com.openpages.apps.common.SimpleException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.ui.constant.AuditPlanBenchmarkHelperConstants.*;
import static com.ibm.openpages.ext.ui.constant.ContextHelperConstants.GENERIC_EXCEPTION_MESSAGE;

@Controller
@RequestMapping(value = "/jspview/auditPlanBenchmarkHelper")
public class AuditPlanBenchmarkHelperController
{

    private Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IAuditPlanBenchmarkHelperService auditPlanBenchmarkHelperService;

    /**
     * Initialize any required service post the object construction.
     */
    @PostConstruct
    public void initController()
    {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(AUDIT_PLAN_BENCHMARK_CONTROLLER_LOG_FILE_NAME);

    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public @ResponseBody AuditPlanBenchmarkHelperAppInfo getInitialPageForApp(@RequestParam("resourceId") String objectId,
            HttpSession session) throws Exception
    {

        logger.info("getInitialPageForApp() START");

        // Local Variables
        AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo = new AuditPlanBenchmarkHelperAppInfo();

        logger.info("Object ID: " + objectId);

        try
        {

            /*
             * Get the Helper header and the Landing Page information from the
             * back end
             */
            auditPlanBenchmarkHelperService.getHeaderAndLandingPageInfo(auditPlanBenchmarkHelperAppInfo, objectId);

        }
        catch (Exception ex)
        {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getInitialPageForApp()" + getStackTrace(ex));
            throw opException;

        }

        /* Set the Helper info bean in the session. */
        logger.info("Audit Plan Benchmark Helper Information before setting it in session: " + auditPlanBenchmarkHelperAppInfo);
        session.setAttribute(AUDIT_PLAN_BENCHMARK_HELPER_APP_INFO, auditPlanBenchmarkHelperAppInfo);

        logger.info("getInitialPageForApp() END");
        return auditPlanBenchmarkHelperAppInfo;

    }

    @RequestMapping(value = "/getBenchmarkPlans", method = RequestMethod.GET)
    public @ResponseBody DataGridInfo getBenchmarkPlans(HttpSession session) throws SimpleException
    {

        logger.info("getBenchmarkPlans() START");

        // Method Level Variables.
        DataGridInfo availableObjects = null;
        AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo;

        // Initialize Variables
        auditPlanBenchmarkHelperAppInfo = (AuditPlanBenchmarkHelperAppInfo) session.getAttribute(AUDIT_PLAN_BENCHMARK_HELPER_APP_INFO);

        try
        {

            availableObjects = auditPlanBenchmarkHelperService.getBenchmarkPlans(auditPlanBenchmarkHelperAppInfo);

        }
        catch (Exception ex)
        {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getAvailableObjects()" + getStackTrace(ex));
            throw opException;

        }
        logger.info("availableObjects : " + availableObjects);
        logger.info("getBenchmarkPlans() END");

        return availableObjects;
    }

    /**
     * <P>
     * This method benchmark the qualified audits.
     * </P>
     * 
     * @param session
     *            - HttpSession object
     *
     * @return Map<String, List<String>>
     * @throws Exception
     */
    @RequestMapping(value = "/auditPlanConfirm", method = RequestMethod.POST)
    public @ResponseBody DataGridInfo auditPlanConfirm(HttpSession session) throws SimpleException
    {

        logger.info("auditPlanConfirm() Start");

        // Method Level Variables.
        AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo = null;
        DataGridInfo dataGridInfo = null;

        try
        {

            /* Initialize Variables */
            auditPlanBenchmarkHelperAppInfo = (AuditPlanBenchmarkHelperAppInfo) session.getAttribute(AUDIT_PLAN_BENCHMARK_HELPER_APP_INFO);

            dataGridInfo = auditPlanBenchmarkHelperService.auditPlanConfirm(auditPlanBenchmarkHelperAppInfo);
        }
        catch (Exception ex)
        {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "auditPlanConfirm()" + getStackTrace(ex));
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        logger.info("Audit Plan Benchmark Helper Information before setting it in session: " + auditPlanBenchmarkHelperAppInfo);
        session.setAttribute(AUDIT_PLAN_BENCHMARK_HELPER_APP_INFO, auditPlanBenchmarkHelperAppInfo);

        /*
         * The initial request needs to be processed so that the configuration
         * is picked up from the pages-tiles.xml to forward the request to the
         * right JSP page
         */
        logger.info("dataGridInfo : " + dataGridInfo);
        logger.info("auditPlanConfirm() End");
        return dataGridInfo;
    }

}
