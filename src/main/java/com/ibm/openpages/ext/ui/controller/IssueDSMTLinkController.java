/*********************************************************************************************************************
 IBM Confidential OCO Source Materials

 5725-D51, 5725-D52, 5725-D53, 5725-D54

 © Copyright IBM Corporation 2021

 The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 has been deposited with the U.S. Copyright Office.
 */

package com.ibm.openpages.ext.ui.controller;

import com.ibm.openpages.ext.ui.bean.*;
import com.openpages.apps.common.SimpleException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isObjectNotNull;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_INFORMATION;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_CITI_ISSUE_CONTROLLER_LOG_FILE_NAME;
import static com.ibm.openpages.ext.ui.constant.HelperAppExceptionConstants.GENERIC_EXCEPTION_MESSAGE;
import static com.ibm.openpages.ext.ui.constant.IssueDSMTLinkConstants.CITI_ISSUE_STATUS_FIELD_INFO;

/**
 * <p>
 * This class works as the Controller to branch the request coming into the Issue DSMT Link helper controller.
 * All requests are handled by their corresponding methods marked by the request mapping.
 * </P>
 *
 * @author : Praveen Ravi <BR>
 * email : raviprav@us.ibm.com <BR>
 * company : IBM OpenPages
 * @version : OpenPages 8.2.0
 * @custom.date : 06-28-2021
 * @custom.feature : Helper Controller
 * @custom.category : Helper App
 */
@Controller
@RequestMapping({ "/jspview/issueDsmtLinkApp" })
public class IssueDSMTLinkController extends DSMTLinkBaseController {

    @PostConstruct
    public void initController() {

        logger = loggerUtil.getExtLogger(DSMT_LINK_CITI_ISSUE_CONTROLLER_LOG_FILE_NAME);
    }

    /**
     * <p>
     * This method handles the initialization of the helper. The method fetches all necessary data for the landing page
     * of the helper. The helpers header information any application text strings and any associated object information
     * is sent back to the UI for the landing page display.
     * </P>
     *
     * @param objectId - the id of the object from which the DSMT Helper was launched.
     * @param session  - HttpSession object
     * @return a JSON representation of the {@link DSMTLinkHelperAppInfo}
     * @throws Exception a generic exception
     */
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public @ResponseBody
    DSMTLinkHelperAppInfo getInitialPageForApp(@RequestParam("resourceId") String objectId, HttpSession session)
            throws Exception {

        logger.info("getInitialPageForApp() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = setupHelperObjectInfo(objectId, CITI_ISSUE_STATUS_FIELD_INFO);
            dsmtLinkHelperInfo = getIssueDetails(dsmtLinkHelperInfo);
            determineIssueService(dsmtLinkHelperInfo);
            logger.info("Basic Object Information: " + dsmtLinkHelperInfo);

            /* Get the Helper header and the Landing Page information from the back end . The type of the Issue
             * determines the Issue service
             */
            if (isObjectNotNull(citiIssueService)) {

                citiIssueService.getHelperHeaderInfo(dsmtLinkHelperInfo);
                citiIssueService.getLandingPageInfo(dsmtLinkHelperInfo);
            }
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getInitialPageForApp()" + getStackTrace(ex));
            opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        logger.info("DSMT Link Helper Information before setting it in session: " + dsmtLinkHelperInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("getInitialPageForApp() End");
        return dsmtLinkHelperInfo;
    }

    /**
     * <p>
     * This method returns the Existing DSMT Links associated to the given Issue object. The resource id of the issue
     * object is obtained from the DSMTLinkHelperAppInfo object in the session. This object is created and all
     * necessary information is initialized and the bean is set in the session.
     * </P>
     *
     * @param session - HttpSession object
     * @return a JSON representation of the {@link DataGridInfo}
     * @throws Exception a generic exception
     */
    @RequestMapping(value = "/getExistingDSMTLinks", method = RequestMethod.GET)
    public @ResponseBody
    DataGridInfo getExistingDSMTLinks(HttpSession session) throws Exception {

        logger.info("getExistingDSMTLinks() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;
        DataGridInfo existingDSMTLinksGridInfo = null;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            /* Get the existing DSMT links for the Issue in the current session. The type of the Issue determines
             * the Issue service
             */
            if (isObjectNotNull(citiIssueService)) {
                existingDSMTLinksGridInfo = citiIssueService.getExistingDSMTLinks(dsmtLinkHelperInfo);
            }
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getExistingDSMTLinks()" + getStackTrace(ex));
            opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        logger.info("Existing DSMT Links Grid Info: " + existingDSMTLinksGridInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("getExistingDSMTLinks() End");
        return existingDSMTLinksGridInfo;
    }

    /**
     * <p>
     * This method get the available DSMT Links for the given Issue object. The available issue object is obtained
     * based on the Ancestor AuditProgram object or the Ancestor Control object. The type of the Issue determines if
     * the available DSMT Links should be fetched from the AuditProgram or Control. All Ancestory AuditProgram or
     * Control are obtained and their child DSMT Links are obtained. This list is filtered based on the Existing DSMT
     * Links of the Issue object by matching the Base Id of the DSMT Link objects.
     * </P>
     *
     * @param session - HttpSession object
     * @return a JSON representation of the {@link DataGridInfo}
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/getAvailableDSMTLinks", method = RequestMethod.GET)
    public @ResponseBody
    DataGridInfo getAvailableDSMTLinks(HttpSession session) throws Exception {

        logger.info("getAvailableDSMTLinks() Start");

        // Method Level Variables.
        DataGridInfo availableDSMTLinks = null;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            /* Get the available DSMT links for the Issue in the current session. The type of the Issue determines
             * the Issue service
             */
            if (isObjectNotNull(citiIssueService)) {

                availableDSMTLinks = citiIssueService.getAvailableDSMTLinks(dsmtLinkHelperInfo);
            }
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getExistingDSMTLinks()" + getStackTrace(ex));
            opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);
            throw opException;
        }

        /* Log for debugging. */
        logger.info("Search results for DSMT Links: " + availableDSMTLinks);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("getAvailableDSMTLinks() End");
        return availableDSMTLinks;
    }

    /**
     * <p>
     * This method get the available DSMT Links under searched Auditable Entities.
     * </P>
     *
     * @param session - HttpSession object
     * @return a JSON representation of the DataGridInfo
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/searchDSMTLinks", method = RequestMethod.GET)
    public @ResponseBody
    DataGridInfo searchDSMTLinks(@RequestParam("searchText") String searchText, HttpSession session) throws Exception {

        logger.info("searchDSMTLinks() Start");

        // Method Level Variables.
        DataGridInfo availableDSMTLinks = null;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            dsmtLinkHelperInfo.setObjectSearchText(searchText);
            if (isObjectNotNull(citiIssueService)) {
                availableDSMTLinks = citiIssueService.searchDSMTLinks(dsmtLinkHelperInfo);
            }
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getExistingDSMTLinks()" + getStackTrace(ex));
            opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);
            throw opException;
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
     * <p>
     * This method handles the New DSMT Link Flag case. If New DSMT Link Flag is true
     * </P>
     *
     * @param dsmtLinkUpdateInfo - List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo
     * @param session            - HttpSession object
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/processAssociateNewDSMT", method = RequestMethod.POST)
    public @ResponseBody
    List<DSMTLinkObjectInfo> processAssociateNewDSMT(@RequestBody List<UpdateDSMTLinkInfo> dsmtLinkUpdateInfo,
            HttpSession session) throws Exception {

        logger.info("processAssociateNewDSMT() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;
        List<DSMTLinkObjectInfo> dsmtLinkScopingFailInfo;

        try {

            /* Initialize Variables */
            dsmtLinkScopingFailInfo = new ArrayList<>();
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            logger.info("associateNewDSMTLinks Input : " + dsmtLinkUpdateInfo);
            dsmtLinkHelperInfo.setUpdateDsmtLinkInfo(dsmtLinkUpdateInfo);

            if (isObjectNotNull(citiIssueService)) {
                dsmtLinkHelperInfo = citiIssueService.addDSMTLinksToIssue(dsmtLinkHelperInfo);
            }
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getExistingDSMTLinks()" + getStackTrace(ex));
            opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        logger.info("DSMT Link Helper Information before setting it in session: " + dsmtLinkHelperInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("processAssociateNewDSMT() End");
        return dsmtLinkScopingFailInfo;
    }

    /**
     * <p>
     * This method processes the de'scoping of DSMT objects from the existing Issue from which the helper was
     * launched. The de'scoping could be as simple as a field being updated in the DSMT object or deleting the DSMT
     * object or disassociating the DSMT object based on the status of the Issue.
     * </P>
     *
     * @param dsmtLinkDescopingInfo - a list of {@link DSMTLinkUpdateInfo} that has the information of each DSMT that needs to be
     *                              associated to the Issue
     * @param session               - HttpSession object
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/processDescopeExistingDSMT", method = RequestMethod.POST)
    public @ResponseBody
    void processDescopeExistingDSMT(@RequestBody List<DSMTLinkUpdateInfo> dsmtLinkDescopingInfo, HttpSession session)
            throws Exception {

        logger.info("processDescopeExistingDSMT() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            dsmtLinkHelperInfo.setDsmtLinkUpdateInfo(dsmtLinkDescopingInfo);

            /* Log information for debugging. */
            logger.info("Descoping Info : " + dsmtLinkDescopingInfo);

            /*
             * Process the de'scoping of DSMT links based on the selected information in the UI with the Issue
             * from which the helper was launched.
             */
            dsmtLinkHelperInfo = citiIssueService.descopeDSMTLinksFromIssue(dsmtLinkHelperInfo);
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "processAssociateNewDSMT()" + getStackTrace(ex));
            opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);
            throw opException;
        }

        /* Set the Helper info bean in the session. */
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("processDescopeExistingDSMT() End");
    }
}
