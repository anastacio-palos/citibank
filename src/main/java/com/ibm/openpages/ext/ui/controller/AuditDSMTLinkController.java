/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * � Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.controller;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkObjectInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;
import com.ibm.openpages.ext.ui.bean.UpdateDSMTLinkInfo;
import com.ibm.openpages.ext.ui.service.IAuditDSMTLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.ui.constant.AuditDSMTLinkConstants.AUDIT_STATUS_FIELD;
import static com.ibm.openpages.ext.ui.constant.AuditDSMTLinkConstants.DSMT_LINK_AUDIT_CONTROLLER_LOG_FILE_NAME;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_INFORMATION;

@Controller
@RequestMapping(value = "jspview/auditDsmtLinkApp")
public class AuditDSMTLinkController extends DSMTLinkBaseController {

    @Autowired
    IAuditDSMTLinkService auditService;

    /**
     * Initialize any required service post the object construction.
     */
    @PostConstruct
    public void initController() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(DSMT_LINK_AUDIT_CONTROLLER_LOG_FILE_NAME);
    }

    /**
     * <P>
     * This method handles the initialization of the helper. The method fetches all necessary data for the landing page
     * of the helper. The helpers header information any application text strings and any associated object information
     * is sent back to the UI for the landing page display.
     * </P>
     *
     * @param model
     *             - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DSMTLinkHelperAppInfo
     * @throws Exception
     */
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public @ResponseBody DSMTLinkHelperAppInfo getInitialPageForApp(@RequestParam("resourceId") String objectId,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.info("Audit DSMT Link Helper Init Start");

        // Method Level Variables.
        String grcObjectType = EMPTY_STRING;

        IGRCObject grcObject = null;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;

        try {

            /* Initialize Variables */
            logger.info("Resource Id: " + objectId);
            dsmtLinkHelperInfo = setupHelperObjectInfo(objectId, AUDIT_STATUS_FIELD);

            auditService.getHelperHeaderInfo(dsmtLinkHelperInfo);
            auditService.getLandingPageInfo(dsmtLinkHelperInfo);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getInitialPageForApp()" + getStackTrace(ex));
        }

        /* Set the Helper info bean in the session. */
        logger.info("DSMT Link Helper Information before setting it in session: " + dsmtLinkHelperInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("DSMT Link Helper Init End");
        return dsmtLinkHelperInfo;
    }

    /**
     * <P>
     * This method get the existing DSMT Links under Audit
     * </P>
     *
     * @param model
     *             - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception
     */
    @RequestMapping(value = "/getExistingDSMTLinks", method = RequestMethod.GET)
    public @ResponseBody DataGridInfo getExistingDSMTLinks(Model model,
            HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("getExistingDSMTLinks() Start");

        // Method Level Variables.
        DataGridInfo existingDSMTLinksGridInfo = null;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            existingDSMTLinksGridInfo = auditService.getExistingDSMTLinks(dsmtLinkHelperInfo);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getExistingDSMTLinks()" + getStackTrace(ex));
        }

        /* Log for debugging. */
        logger.info("Existing DSMT Links: " + existingDSMTLinksGridInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("getExistingDSMTLinks() End");
        return existingDSMTLinksGridInfo;
    }

    /**
     * <P>
     * This method get the existing DSMT Links under Audit
     * </P>
     *
     * @param model
     *             - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception
     */
    @RequestMapping(value = "/getNewDSMTLinks", method = RequestMethod.GET)
    public @ResponseBody DataGridInfo getNewDSMTLinks(Model model,
            HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("getNewDSMTLinks() Start");

        // Method Level Variables.
        DataGridInfo existingDSMTLinksGridInfo = null;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            existingDSMTLinksGridInfo = auditService.getNewDSMTLinkObject(dsmtLinkHelperInfo);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getExistingDSMTLinks()" + getStackTrace(ex));
        }

        /* Log for debugging. */
        logger.info("Existing DSMT Links: " + existingDSMTLinksGridInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("getNewDSMTLinks() End");
        return existingDSMTLinksGridInfo;
    }

    /**
     * <P>
     * This method get the available DSMT Links under searched Auditable Entities.
     * </P>
     *
     * @param searchText
     *             - String searchText
     * @param model
     *             - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DataGridInfo
     * @throws Exception
     */
    @RequestMapping(value = "/searchDSMTLinks", method = RequestMethod.GET)
    public @ResponseBody DataGridInfo searchDSMTLinks(@RequestParam("searchText") String searchText, Model model,
            HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("searchDSMTLinks() Start");

        // Method Level Variables.
        DataGridInfo availableDSMTLinks = null;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;

        try {

            /* Initialize Variables */
            logger.info("Auditable Entity search text : " + searchText);
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            availableDSMTLinks = auditService.getAvailableObjectInformation(dsmtLinkHelperInfo, searchText);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "searchDSMTLinks()" + getStackTrace(ex));
        }

        /* Log for debugging. */
        logger.info("Search results for DSMT Links: " + availableDSMTLinks);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("searchDSMTLinks() End");
        return availableDSMTLinks;
    }

    /**
     * <P>
     * This method handles the associating existing DSMT Links.
     * </P>
     *
     * @param model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception
     */
    @RequestMapping(value = "/associateDSMTLinks", method = RequestMethod.POST)
    public @ResponseBody List<DSMTLinkObjectInfo> associateDSMTLinks(
            @RequestBody List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo, Model model, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("associateDSMTLinks() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkUpdateFailInfo = null;

        try {

            /* Initialize Variables */
            logger.info("associateDSMTLinks Input : " + dsmtLinkUpdateInfo);
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            dsmtLinkHelperInfo.setUpdateDsmtLinkInfo(dsmtLinkUpdateInfo);
            dsmtLinkUpdateFailInfo = auditService.associateDSMTLinksToAudit(dsmtLinkHelperInfo, false);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "associateDSMTLinks()" + getStackTrace(ex));
        }

        /* Set the Helper info bean in the session. */
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("associateDSMTLinks() End");
        return dsmtLinkUpdateFailInfo;
    }

    /**
     * <P>
     * This method handles the re-scoping existing DSMT Links from existing table
     * </P>
     * 
     * @param  dsmtLinkUpdateInfo
     *                                - List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo
     * @param  model
     *                                - Model model
     * @param  session
     *                                - HttpSession session
     * @param  request
     *                                - HttpServletRequest request
     * @param  response
     *                                - HttpServletResponse response
     *
     * @return                    a JSON representation of the List<DSMTLinkObjectInfo>
     * @throws Exception
     */
    @RequestMapping(value = "/rescopeDSMTLinks", method = RequestMethod.POST)
    public @ResponseBody List<DSMTLinkObjectInfo> rescopeDSMTLinks(
            @RequestBody List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo, Model model, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("rescopeDSMTLinks() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkUpdateFailInfo = null;

        try
        {

            /* Initialize Variables */
            logger.info("rescopeDSMTLinks Input : " + dsmtLinkUpdateInfo);
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            dsmtLinkHelperInfo.setUpdateDsmtLinkInfo(dsmtLinkUpdateInfo);
            dsmtLinkUpdateFailInfo = auditService.rescopeDSMTLinks(dsmtLinkHelperInfo);
        }
        catch (Exception ex)
        {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "rescopeDSMTLinks()" + getStackTrace(ex));
        }

        /* Set the Helper info bean in the session. */
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("rescopeDSMTLinks() End");
        return dsmtLinkUpdateFailInfo;
    }

    /**
     * <P>
     * This method handles the New DSMT Link Flag case. If New DSMT Link Flag is true
     * </P>
     *
     * @param dsmtLinkUpdateInfo
     *             - List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo
     * @param model
     *             - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception
     */
    @RequestMapping(value = "/associateNewDSMTLinks", method = RequestMethod.POST)
    public @ResponseBody List<DSMTLinkObjectInfo> associateNewDSMTLinks(
            @RequestBody List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo, Model model, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("associateNewDSMTLinks() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkScopingFailInfo = null;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            logger.info("associateNewDSMTLinks Input : " + dsmtLinkUpdateInfo);
            dsmtLinkHelperInfo.setUpdateDsmtLinkInfo(dsmtLinkUpdateInfo);
            dsmtLinkScopingFailInfo = auditService.associateDSMTLinksToAudit(dsmtLinkHelperInfo, true);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "associateNewDSMTLinks()" + getStackTrace(ex));
        }

        /* Set the Helper info bean in the session. */
        logger.info("DSMT Link Helper Information before setting it in session: " + dsmtLinkHelperInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("associateNewDSMTLinks() End");
        return dsmtLinkScopingFailInfo;
    }

    /**
     * <P>
     * This method handles the ignore New DSMT Link Flag case. If New DSMT Link Flag is true
     * </P>
     *
     * @param dsmtLinkUpdateInfo
     *             - List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo
     * @param model
     *             - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the List<DSMTLinkObjectInfo>
     * @throws Exception
     */
    @RequestMapping(value = "/ignoreNewDSMTLinks", method = RequestMethod.POST)
    public @ResponseBody List<DSMTLinkObjectInfo> ignoreNewDSMTLinks(
            @RequestBody List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo, Model model, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("ignoreNewDSMTLinks() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkScopingFailInfo = null;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            logger.info("ignoreNewDSMTLinks input : " + dsmtLinkUpdateInfo);
            dsmtLinkHelperInfo.setUpdateDsmtLinkInfo(dsmtLinkUpdateInfo);
            dsmtLinkScopingFailInfo = auditService.ignoreNewDSMTLinksFromAudit(dsmtLinkHelperInfo);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "associateNewDSMTLinks()" + getStackTrace(ex));
        }

        /* Set the Helper info bean in the session. */
        logger.info("DSMT Link Helper Information before setting it in session: " + dsmtLinkHelperInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("ignoreNewDSMTLinks() End");
        return dsmtLinkScopingFailInfo;
    }

    /**
     * <P>
     * This method handles the descoping existing DSMT Links.
     * </P>
     *
     * @param dsmtLinkUpdateInfo
     *             - List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo
     * @param model
     *             - Model model
     * @param session
     *            - HttpSession object
     * @param request
     *            - HttpServletRequest object
     * @param response
     *            - HttpServletResponse object
     *
     * @return a JSON representation of the List<DSMTLinkObjectInfo>
     * @throws Exception
     */
    @RequestMapping(value = "/descopeExistingDSMTLinks", method = RequestMethod.POST)
    public @ResponseBody List<DSMTLinkObjectInfo> descopeExistingDSMTLinks(
            @RequestBody List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo, Model model, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("descopeExistingDSMTLinks() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo = null;
        List<DSMTLinkObjectInfo> dsmtLinkUpdateFailInfo = null;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            logger.info("descopeExistingDSMTLinks input : " + dsmtLinkUpdateInfo);
            dsmtLinkHelperInfo.setUpdateDsmtLinkInfo(dsmtLinkUpdateInfo);
            dsmtLinkUpdateFailInfo = auditService.descopeDSMTLinksFromAudit(dsmtLinkHelperInfo);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "descopeExistingDSMTLinks()" + getStackTrace(ex));
        }

        /* Set the Helper info bean in the session. */
        logger.info("DSMT Link Helper Information before setting it in session: " + dsmtLinkHelperInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("descopeExistingDSMTLinks() End");
        return dsmtLinkUpdateFailInfo;
    }
}
