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

import com.ibm.openpages.ext.ui.bean.*;
import com.ibm.openpages.ext.ui.service.IAuditableEntityDSMTLinkService;
import com.openpages.apps.common.SimpleException;
import org.apache.commons.dbcp.DbcpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.sql.SQLRecoverableException;
import java.util.ArrayList;
import java.util.List;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isObjectNotNull;
import static com.ibm.openpages.ext.ui.constant.AuditableEntityDSMTLinkConstants.AUD_ENTITY_STATUS_FIELD_INFO;
import static com.ibm.openpages.ext.ui.constant.AuditableEntityDSMTLinkExceptionConstants.DSMT_DB_CONNECTION_ERROR;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_APP_INFORMATION;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.DSMT_LINK_AUDITABLE_ENTITY_CONTROLLER_LOG_FILE_NAME;
import static com.ibm.openpages.ext.ui.constant.HelperAppExceptionConstants.GENERIC_EXCEPTION_MESSAGE;

/**
 * <p>
 * This class works as the Controller to branch the request coming into the AuditableEntity DSMT Link helper controller.
 * All requests are handled by their corresponding methods marked by the request mapping.
 * </P>
 *
 * @author : Praveen Ravi <BR>
 * email : raviprav@us.ibm.com <BR>
 * company : IBM OpenPages
 * @version : OpenPages 8.2.0
 * @custom.date : 02-15-2021
 * @custom.feature : Helper Controller
 * @custom.category : Helper App
 */
@Controller
@RequestMapping(value = "/jspview/audEntityDsmtLinkApp")
public class AuditableEntityDSMTLinkController extends DSMTLinkBaseController {

    @Autowired
    IAuditableEntityDSMTLinkService auditableEntityService;

    /**
     * Initialize any required service post the object construction.
     */
    @PostConstruct
    public void initController() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger(DSMT_LINK_AUDITABLE_ENTITY_CONTROLLER_LOG_FILE_NAME);
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
     * @throws Exception - Generic Exception
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
            logger.info("Auditable Entity ID: " + objectId);
            dsmtLinkHelperInfo = setupHelperObjectInfo(objectId, AUD_ENTITY_STATUS_FIELD_INFO);

            /* Get the Helper header and the Landing Page information from the back end */
            auditableEntityService.getHelperHeaderInfo(dsmtLinkHelperInfo);
            auditableEntityService.getLandingPageInfo(dsmtLinkHelperInfo);
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
     * This method handles the initialization of the helper. The method fetches all necessary data for the landing page
     * of the helper. The helpers header information any application text strings and any associated object information
     * is sent back to the UI for the landing page display.
     * </P>
     *
     * @param session - HttpSession object
     * @return a JSON representation of the {@link DataGridInfo}
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/getExistingDSMTLinks", method = RequestMethod.GET)
    public @ResponseBody
    DataGridInfo getExistingDSMTLinks(HttpSession session) throws Exception {

        logger.info("getExistingDSMTLinks() Start");

        // Method Level Variables.
        DataGridInfo existingDSMTLinksGridInfo;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);

            /* Get the existing DSMT links for the AuditableEntity in the current session */
            existingDSMTLinksGridInfo = auditableEntityService.getExistingDSMTLinks(dsmtLinkHelperInfo);
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
     * This method handles the searching of DSMT's in the system. The payload comes in the form of
     * {@link DSMTLinkSearchInfo}. The necessary information needs to be passed from the UI. The method returns an
     * instance of the {@link DataGridInfo} that is in the format required by the AG Grid table that displays the search
     * information.
     * </P>
     *
     * @param dsmtSearchInfo - an instance of the {@link DSMTLinkSearchInfo} that matches the payload from the UI
     * @param session        - HttpSession object
     * @return a JSON representation of the {@link DataGridInfo}
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/searchDSMT", method = RequestMethod.POST)
    public @ResponseBody
    DataGridInfo searchDSMT(@RequestBody DSMTLinkSearchInfo dsmtSearchInfo, HttpSession session) throws Exception {

        logger.info("searchDSMT() Start");
        logger.info("Search Info from UI: " + dsmtSearchInfo);

        // Method Level Variables.
        DataGridInfo carbonDataGridInfo;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            dsmtLinkHelperInfo.setDsmtSearchInfo(dsmtSearchInfo);

            /* Log information for debugging. */
            logger.info("DSMT Search Info: " + dsmtSearchInfo);

            /* Search for the DSMT's based on the user search information entered in the UI. */
            carbonDataGridInfo = auditableEntityService.searchDSMT(dsmtLinkHelperInfo);
        }
        catch (DbcpException dbex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("DB EXCEPTION!!!!!!!!!!!!!!! " + "searchDSMT()" + getStackTrace(dbex));
            opException = new SimpleException(HttpStatus.FAILED_DEPENDENCY, DSMT_DB_CONNECTION_ERROR);
            throw opException;
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            if (ex instanceof SQLRecoverableException) {

                logger.error("DB EXCEPTION!!!!!!!!!!!!!!! " + "searchDSMT()" + getStackTrace(ex));
                opException = new SimpleException(HttpStatus.FAILED_DEPENDENCY, DSMT_DB_CONNECTION_ERROR);
            }
            else {

                logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "searchDSMT()" + getStackTrace(ex));
                opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);
            }

            throw opException;
        }

        /* Set the Helper info bean in the session. */
        logger.info("Search Results obtained: " + carbonDataGridInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("searchDSMT() End");
        return carbonDataGridInfo;
    }

    /**
     * <p>
     * This method handles the searching of DSMT's in the system to be displayed as a Tree in the UI. The payload comes
     * in the form of {@link DSMTLinkSearchInfo}. The necessary information needs to be passed from the UI. The method
     * returns an instance of the {@link DataGridInfo} that is in the format required by the AG Grid table that displays
     * the search information.
     * </P>
     *
     * @param session - HttpSession object
     * @return a JSON representation of the {@link DataGridInfo}
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/searchDSMTTree", method = RequestMethod.POST)
    public @ResponseBody
    DataGridInfo searchDSMTTree(@RequestBody DSMTLinkSearchInfo dsmtSearchInfo, HttpSession session) throws Exception {

        logger.info("searchDSMTTree() Start");
        logger.info("Search Tree Info from UI: " + dsmtSearchInfo);

        // Method Level Variables.
        DataGridInfo carbonDataGridInfo;
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;

        try {

            /* Initialize Variables */
            dsmtSearchInfo.setTreeSearch(true);
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            dsmtLinkHelperInfo.setDsmtSearchInfo(dsmtSearchInfo);

            /* Log information for debugging. */
            logger.info("DSMT Search Info: " + dsmtSearchInfo);

            /* Search for the child items of the passed node in the tree selected by the user in the UI. */
            carbonDataGridInfo = auditableEntityService.searchDSMTTree(dsmtLinkHelperInfo);
        }
        catch (DbcpException dbex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("DB EXCEPTION!!!!!!!!!!!!!!! " + "searchDSMTTree()" + getStackTrace(dbex));
            opException = new SimpleException(HttpStatus.FAILED_DEPENDENCY, DSMT_DB_CONNECTION_ERROR);
            throw opException;
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            if (ex instanceof SQLRecoverableException) {

                logger.error("DB EXCEPTION!!!!!!!!!!!!!!! " + "searchDSMT()" + getStackTrace(ex));
                opException = new SimpleException(HttpStatus.FAILED_DEPENDENCY, DSMT_DB_CONNECTION_ERROR);
            }
            else {

                logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "searchDSMT()" + getStackTrace(ex));
                opException = new SimpleException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION_MESSAGE);
            }

            throw opException;
        }

        /* Set the Helper info bean in the session. */
        logger.info("DSMT Data grid info: " + carbonDataGridInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("searchDSMTTree() End");
        return carbonDataGridInfo;
    }

    /**
     * <p>
     * This method processes the association of the selected DSMT's in the UI to the AuditableEntity from which the
     * helper is launched.
     * </P>
     *
     * @param associateNewDSMTInfoList - a list of {@link CreateDSMTLinkForAudEntityInfo} that has the information of each DSMT that needs to
     *                                 be associated to the AuditableEntity
     * @param session                  - HttpSession object
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/processNodeRangeCheck", method = RequestMethod.POST)
    public @ResponseBody
    List<CreateDSMTLinkForAudEntityInfo> processNodeRangeCheck(
            @RequestBody List<CreateDSMTLinkForAudEntityInfo> associateNewDSMTInfoList, HttpSession session)
            throws Exception {

        logger.info("processNodeRangeCheck() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;
        List<CreateDSMTLinkForAudEntityInfo> associatedDSMTLinkInfo;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            dsmtLinkHelperInfo.setAssociateNewDSMTInfoList(associateNewDSMTInfoList);

            /* Log information for debugging. */
            logger.info("List of DSMT that needs to be associated: " + associateNewDSMTInfoList);

            /*
             * Process the associate of DSMT links based on the selected information in the UI with the AuditableEntity
             * from which the helper was launched.
             */
            associatedDSMTLinkInfo = auditableEntityService.processNodeRangeCheck(dsmtLinkHelperInfo);
        }
        catch (Exception ex) {

            // Exception handling Variables.
            SimpleException opException;

            /*
             * Log the event, construct the exception and throw
             */
            logger.error("EXCEPTION!!!!!!!!!!!!!!! " + "processNodeRangeCheck()" + getStackTrace(ex));
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
        logger.info("processNodeRangeCheck() End");
        return associatedDSMTLinkInfo;
    }

    /**
     * <p>
     * This method processes the association of the selected DSMT's in the UI to the AuditableEntity from which the
     * helper is launched.
     * </P>
     *
     * @param associateNewDSMTInfoList - a list of {@link CreateDSMTLinkForAudEntityInfo} that has the information of each DSMT that needs to
     *                                 be associated to the AuditableEntity
     * @param session                  - HttpSession object
     * @return a JSON representation of the DSMTLinkHelperInfo
     * @throws Exception - Generic Exception
     */
    @RequestMapping(value = "/processAssociateNewDSMT", method = RequestMethod.POST)
    public @ResponseBody
    List<CreateDSMTLinkForAudEntityInfo> processAssociateNewDSMT(
            @RequestBody List<CreateDSMTLinkForAudEntityInfo> associateNewDSMTInfoList, HttpSession session)
            throws Exception {

        logger.info("processAssociateNewDSMT() Start");

        // Method Level Variables.
        DSMTLinkHelperAppInfo dsmtLinkHelperInfo;
        List<CreateDSMTLinkForAudEntityInfo> duplicateDSMTLinksOnCreation;

        try {

            /* Initialize Variables */
            dsmtLinkHelperInfo = (DSMTLinkHelperAppInfo) session.getAttribute(DSMT_LINK_APP_INFORMATION);
            dsmtLinkHelperInfo.setAssociateNewDSMTInfoList(associateNewDSMTInfoList);

            /* Log information for debugging. */
            logger.info("List of DSMT that needs to be associated: " + associateNewDSMTInfoList);

            /*
             * Process the associate of DSMT links based on the selected information in the UI with the AuditableEntity
             * from which the helper was launched.
             */
            dsmtLinkHelperInfo = auditableEntityService.addDSMTLinksToAuditableEntity(dsmtLinkHelperInfo);
            duplicateDSMTLinksOnCreation = dsmtLinkHelperInfo.getDuplicateDSMTLinksOnCreation();
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
        logger.info("DSMT Link Helper Information before setting it in session: " + dsmtLinkHelperInfo);
        session.setAttribute(DSMT_LINK_APP_INFORMATION, dsmtLinkHelperInfo);
        duplicateDSMTLinksOnCreation = isObjectNotNull(duplicateDSMTLinksOnCreation) ?
                duplicateDSMTLinksOnCreation :
                new ArrayList<>();

        /*
         * The initial request needs to be processed so that the configuration is picked up from the pages-tiles.xml to
         * forward the request to the right JSP page
         */
        logger.info("processAssociateNewDSMT() End");
        return duplicateDSMTLinksOnCreation;
    }

    /**
     * <p>
     * This method processes the de'scoping of DSMT objects from the existing AuditableEntity from which the helper was
     * launched. The de'scoping could be as simple as a field being updated in the DSMT object or deleting the DSMT
     * object or disassociating the DSMT object based on the status of the AuditableEntity.
     * </P>
     *
     * @param dsmtLinkDescopingInfo - a list of {@link DSMTLinkUpdateInfo} that has the information of each DSMT that needs to be
     *                              associated to the AuditableEntity
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
             * Process the de'scoping of DSMT links based on the selected information in the UI with the AuditableEntity
             * from which the helper was launched.
             */
            dsmtLinkHelperInfo = auditableEntityService.descopeDSMTLinksFromAuditableEntity(dsmtLinkHelperInfo);
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
