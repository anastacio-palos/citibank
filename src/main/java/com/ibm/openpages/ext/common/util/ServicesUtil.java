package com.ibm.openpages.ext.common.util;

/*
 Licensed Materials - Property of IBM


 5725-D51, 5725-D52, 5725-D53, 5725-D54

 © Copyright IBM Corporation 2017. All Rights Reserved.

 US Government Users Restricted Rights- Use, duplication or disclosure restricted 
 by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 {
 "$schema":{"$ref":"TS_Taxonomy_vMay132009"},
 "author":"Nikhil Komakula",
 "customer":"IBM",
 "date":"11/20/2016",
 "summary":"Common",
 "technology":"java",
 "feature":"Common Utilities",
 "rt_num":""
 }
 */

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.service.IApplicationService;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IQuestionnaireService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.ServiceFactory;
import com.openpages.apps.sosa.taglib.report.ReportTagClient;
import com.openpages.sdk.OpenpagesSession;

/**
 * @date 7/9/2015
 * @feature Service Utilities
 * @summary This utility class provides various services required to perform any action on OpenPages.
 * 
 * @author Nikhil Komakula
 * @email nikhil.komakula@in.ibm.com
 */
public class ServicesUtil
{

    private Context context = null;

    private IServiceFactory serviceFactory = null;

    private IConfigurationService configurationService = null;

    private ISecurityService securityService = null;

    private IConfigProperties configProperties = null;

    private IMetaDataService metaDataService = null;

    private IResourceService resourceService = null;

    private IQueryService queryService = null;

    private IApplicationService applicationService = null;

    private IQuestionnaireService questionnaireService = null;

    private ReportTagClient reportTagClient = null;

    private String loggedInUser = null;

    /**
     * <p>
     * Constructor to initialize ServicesUtil based on Logged-In Users Context.
     * <P>
     * 
     * @param loggedInUsersContext
     *            Context of the Logged-In User
     */
    public ServicesUtil(Context loggedInUsersContext)
    {

        initializeServices(loggedInUsersContext);
    }

    /**
     * <p>
     * Constructor to initialize ServicesUtil based on Logged-In Users Session.
     * <P>
     * 
     * @param opSession
     *            Logged-In Users session
     */
    public ServicesUtil(OpenpagesSession opSession)
    {

        Context loggedInUsersContext = initializeContext(opSession);
        initializeServices(loggedInUsersContext);
    }

    /**
     * <p>
     * Constructor to initialize ServicesUtil based on request object.
     * <P>
     * 
     * @param request
     *            HttpServletRequest for ReportTagClient
     */
    public ServicesUtil(HttpServletRequest request)
    {

        initializeServices(request);
        this.reportTagClient = new ReportTagClient(request);
    }

    /**
     * <p>
     * Constructor to initialize ServicesUtil based on Logged-In Users Session.
     * <P>
     * 
     * @param opSession
     *            Logged-In Users session
     * @param request
     *            HttpServletRequest for ReportTagClient
     */
    public ServicesUtil(OpenpagesSession opSession, HttpServletRequest request)
    {

        Context loggedInUsersContext = initializeContext(opSession);
        initializeServices(loggedInUsersContext);

        this.reportTagClient = new ReportTagClient(request);
    }

    /**
     * <p>
     * Constructor to initialize Admin ServicesUtil based on Logged-In Users Context.
     * <P>
     * 
     * @param loggedInUsersContext
     *            Context of the Logged-In User
     * @param useAdminContext
     *            True to initialize Admin ServicesUtil based on Administrator Credentials. Else, uses Logged-In Users Context.
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public ServicesUtil(Context loggedInUsersContext, boolean useAdminContext, Logger logger) throws Exception
    {

        if (!useAdminContext)
        {

            initializeServices(loggedInUsersContext);
        }
        else
        {

            if (CommonUtil.isObjectNull(logger))
                throw new Exception("Logger cannot be null.");

            Context adminContext = initializeAdminContext(loggedInUsersContext, logger);
            initializeServices(adminContext);
        }
    }

    /**
     * <p>
     * Constructor to initialize Admin ServicesUtil based on Logged-In Users Session.
     * <P>
     * 
     * @param opSession
     *            Logged-In User Session
     * @param useAdminContext
     *            True to initialize Admin ServicesUtil based on Administrator Credentials. Else, uses Logged-In Users Context.
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public ServicesUtil(OpenpagesSession opSession, boolean useAdminContext, Logger logger) throws Exception
    {

        if (!useAdminContext)
        {

            Context loggedInUsersContext = initializeContext(opSession);
            initializeServices(loggedInUsersContext);
        }
        else
        {

            if (CommonUtil.isObjectNull(logger))
                throw new Exception("Logger cannot be null.");

            Context adminContext = initializeAdminContext(opSession, logger);
            initializeServices(adminContext);
        }
    }

    /**
     * <p>
     * Constructor to initialize Admin ServicesUtil based on Logged-In Users Context.
     * <P>
     * 
     * @param loggedInUsersContext
     *            Context of the Logged-In User
     * @param useAdminContext
     *            True to initialize Admin ServicesUtil based on Administrator Credentials. Else, uses Logged-In Users Context.
     * @param request
     *            HttpServletRequest for ReportTagClient
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public ServicesUtil(Context loggedInUsersContext, boolean useAdminContext, HttpServletRequest request, Logger logger) throws Exception
    {

        if (!useAdminContext)
        {

            initializeServices(loggedInUsersContext);
        }
        else
        {

            if (CommonUtil.isObjectNull(logger))
                throw new Exception("Logger cannot be null.");

            Context adminContext = initializeAdminContext(loggedInUsersContext, logger);
            initializeServices(adminContext);
        }

        this.reportTagClient = new ReportTagClient(request);
    }

    /**
     * <p>
     * Constructor to initialize Admin ServicesUtil based on Logged-In Users Session.
     * <P>
     * 
     * @param opSession
     *            Logged-In User Session
     * @param useAdminContext
     *            True to initialize Admin ServicesUtil based on Administrator Credentials. Else, uses Logged-In Users Context.
     * @param request
     *            HttpServletRequest for ReportTagClient
     * @param logger
     *            Instance of Logger
     * @throws Exception
     */
    public ServicesUtil(OpenpagesSession opSession, boolean useAdminContext, HttpServletRequest request, Logger logger) throws Exception
    {

        if (!useAdminContext)
        {

            Context loggedInUsersContext = initializeContext(opSession);
            initializeServices(loggedInUsersContext);
        }
        else
        {

            if (CommonUtil.isObjectNull(logger))
                throw new Exception("Logger cannot be null.");

            Context adminContext = initializeAdminContext(opSession, logger);
            initializeServices(adminContext);
        }

        this.reportTagClient = new ReportTagClient(request);
    }

    private void initializeServices(Context context)
    {

        this.context = context;
        this.serviceFactory = ServiceFactory.getServiceFactory(context);
        this.configurationService = serviceFactory.createConfigurationService();
        this.securityService = serviceFactory.createSecurityService();
        this.configProperties = configurationService.getConfigProperties();
        this.metaDataService = serviceFactory.createMetaDataService();
        this.resourceService = serviceFactory.createResourceService();
        this.queryService = serviceFactory.createQueryService();
        this.applicationService = serviceFactory.createApplicationService();
        this.questionnaireService = serviceFactory.createQuestionnaireService();
        this.loggedInUser = SecurityUtil.getLoggedInUserName(context);
    }

    private void initializeServices(HttpServletRequest request)
    {

        this.serviceFactory = ServiceFactory.getServiceFactory(request);
        this.configurationService = serviceFactory.createConfigurationService();
        this.securityService = serviceFactory.createSecurityService();
        this.configProperties = configurationService.getConfigProperties();
        this.metaDataService = serviceFactory.createMetaDataService();
        this.resourceService = serviceFactory.createResourceService();
        this.queryService = serviceFactory.createQueryService();
        this.applicationService = serviceFactory.createApplicationService();
        this.questionnaireService = serviceFactory.createQuestionnaireService();
        this.loggedInUser = SecurityUtil.getLoggedInUserName(context);
    }



    private Context initializeContext(OpenpagesSession opSession)
    {

        Context context = new Context();
        context.put(Context.SERVICE_SESSION, opSession);

        return context;
    }

    private Context initializeAdminContext(OpenpagesSession opSession, Logger logger) throws Exception
    {

        Context loggedInUsersContext = initializeContext(opSession);
        Context adminContext = SecurityUtil.getAdminContext(loggedInUsersContext, logger);

        return adminContext;
    }

    private Context initializeAdminContext(Context loggedInUsersContext, Logger logger) throws Exception
    {

        Context adminContext = SecurityUtil.getAdminContext(loggedInUsersContext, logger);

        return adminContext;
    }

    public ReportTagClient getReportTagClient()
    {

        return this.reportTagClient;
    }

    public Context getContext()
    {

        return this.context;
    }

    public IServiceFactory getServiceFactory()
    {

        return this.serviceFactory;
    }

    public IConfigurationService getConfigurationService()
    {

        return this.configurationService;
    }

    public ISecurityService getSecurityService()
    {

        return this.securityService;
    }

    public IConfigProperties getConfigProperties()
    {

        return this.configProperties;
    }

    public IMetaDataService getMetaDataService()
    {

        return this.metaDataService;
    }

    public IResourceService getResourceService()
    {

        return this.resourceService;
    }

    public IQueryService getQueryService()
    {

        return this.queryService;
    }

    public IApplicationService getApplicationService()
    {

        return this.applicationService;
    }

    public IQuestionnaireService getQuestionnaireService()

    {

        return this.questionnaireService;

    }

    public String getLoggedInUser()
    {

        return this.loggedInUser;
    }
}