/**
 IBM Confidential OCO Source Materials
 5725-D51, 5725-D52, 5725-D53, 5725-D54
 © Copyright IBM Corporation 2022
 The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 has been deposited with the U.S. Copyright Office.
 */

package com.ibm.openpages.ext.ui.controller;

import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.bean.ContextHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;
import com.ibm.openpages.ext.ui.service.IContextHelperService;
import com.openpages.apps.common.SimpleException;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.ui.constant.ContextHelperConstants.*;

import java.util.List;

/**
 * <p>
 * This class works as the Controller to branch the request coming into the Issue Context helper controller.
 * All requests are handled by their corresponding methods marked by the request mapping.
 * </P>
 *
 * @author : Praveen Ravi <BR>
 * email : raviprav@us.ibm.com <BR>
 * company : IBM OpenPages
 * @version : OpenPages 8.2.0
 * @custom.date : 01-24-2022
 * @custom.feature : Helper Controller
 * @custom.category : Helper App
 */

@Controller
@RequestMapping(value = "jspview/context")
public class ContextHelperController {

    // Class Level Variables
    protected Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IContextHelperService contextHelperService;

    @PostConstruct
    public void initController() {

        logger = loggerUtil.getExtLogger(CONTEXT_HELPER_CONTROLLER_LOG_FILE_NAME);
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public @ResponseBody ContextHelperAppInfo getInitialPageForApp(@RequestParam("resourceId") String objectId, HttpSession session) throws Exception {

        logger.info("getInitialPageForApp() START");

        // Local Variables
        ContextHelperAppInfo contextHelperAppInfo = new ContextHelperAppInfo();

        logger.info("Object ID: " + objectId);

        try{

            /* Get the Helper header and the Landing Page information from the back end */
            contextHelperService.getHeaderAndLandingPageInfo(contextHelperAppInfo, objectId);

        } catch (Exception ex) {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getInitialPageForApp()" + getStackTrace(ex));
            throw opException;

        }

        /* Set the Helper info bean in the session. */
        logger.info("Context Helper Information before setting it in session: " + contextHelperAppInfo);
        session.setAttribute(CONTEXT_HELPER_APP_INFO, contextHelperAppInfo);

        logger.info("getInitialPageForApp() END");
        return contextHelperAppInfo;

    }

    @RequestMapping(value = "/getAvailableObjects", method = RequestMethod.GET)
    public @ResponseBody DataGridInfo getAvailableObjects(HttpSession session) throws SimpleException {

        logger.info("getAvailableObjects() START");

        // Method Level Variables.
        DataGridInfo availableObjects = null;
        ContextHelperAppInfo contextHelperAppInfo;

        // Initialize Variables
        contextHelperAppInfo = (ContextHelperAppInfo) session.getAttribute(CONTEXT_HELPER_APP_INFO);

        try {

            availableObjects = contextHelperService.getAvailableObjects(contextHelperAppInfo);

        } catch (Exception ex) {

            // Exception handling variable.
            SimpleException opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);

            // Log the event, construct the exception and throw
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "getAvailableObjects()" + getStackTrace(ex));
            throw opException;

        }

        logger.info("getAvailableObjects() END");

        return availableObjects;
    }

    /**
     * <P>
     * This method handles the associating the selected Objects.
     * </P>
     * @param resourceIdlist
     *            - List<String> object
     * @param model
     *            - Model object
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
    @RequestMapping(value = "/associateObjects", method = RequestMethod.POST)
    public @ResponseBody String associateObjects(
            @RequestBody List<String> resourceIdlist, Model model, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("associateObjects() Start");

        // Method Level Variables.
        ContextHelperAppInfo contextHelperAppInfo = null;
        String returnString = EMPTY_STRING;

        try {

            /* Initialize Variables */
            logger.info("resourceIdlist : " + resourceIdlist);
            contextHelperAppInfo = (ContextHelperAppInfo) session.getAttribute(CONTEXT_HELPER_APP_INFO);

            returnString = contextHelperService.associateObjects(contextHelperAppInfo, resourceIdlist);
        }
        catch (Exception ex) {

            /*
             * Create the Results information bean and set the required information to process the exception in the UI.
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "associateObjects()" + getStackTrace(ex));
        }

        /* Set the Helper info bean in the session. */
        logger.info("Context Helper Information before setting it in session: " + contextHelperAppInfo);
        session.setAttribute(CONTEXT_HELPER_APP_INFO, contextHelperAppInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("associateObjects() End");
        return returnString;
    }
}
