/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 *
 * OpenPages GRC Platform (PID: 5725-D51)
 *
 * © Copyright IBM Corporation 2016 - CURRENT_YEAR. All Rights Reserved.
 *
 * US Government Users Restricted Rights- Use, duplication or disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 *
 *******************************************************************************/
package com.ibm.openpages.ext.ui.controller;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.openpages.apps.common.util.CookieUtil;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.openpages.apps.common.util.StrutsConstants;

@Controller
public class ExtReactHelperController {

    private Log logger;
    private static final SimpleDateFormat lastModFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    @Autowired
    ILoggerUtil loggerUtil;

    /**
     * Initialize any required service post the object construction.
     */
    @PostConstruct
    public void initController() {

        // use ILoggerUtil service in projects
        logger = loggerUtil.getExtLogger("react_helper-controller.log");
    }
    
    static {
        lastModFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Serve the react helper application, i.e. /helper
     */
    @RequestMapping(value = { "services/helperapp/**" }, method = RequestMethod.GET)
    public @ResponseBody byte[] serveReactResource(Model model, HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        logger.info("Serve React Resource Start");

        // convert /app/jspview/helper/ path to /helper resource
        String path = request.getPathInfo().replace("/jspview", "");
        ServletContext sc = request.getServletContext();
        sc.getContextPath();
        logger.info("Path Info: " + request.getPathInfo());
        logger.info("Path: " + path);
        logger.info("Context Path: " + sc.getContextPath());
        String fileName = sc.getRealPath(path);

        // If a directory (react route) was requested, serve the index.html file
        boolean isIndex = false;
        int index = path.lastIndexOf("helperapp");
        logger.info("Index of Path: " + index);
        
        
        if (index >= 0) {
            
            
            String resource = path.substring(index);
            logger.info("Resource: " + resource);
            logger.info("Resource Index greater than 0: " + (resource.indexOf(".") < 0));
            logger.info("without / equal to resource: " + ("services/helperapp/citi-helpers/index.html".equals(resource)));
            
            if (resource.indexOf(".") < 0 || "helperapp/citi-helpers/index.html".equals(resource)) {
                
                fileName = sc.getRealPath("/services/helperapp/citi-helpers/index.html");
                logger.info("fileName: " + fileName);
                isIndex = true;
                logger.info("Is Index: " + true);
            }
        }

        // We are only setting this so that the
        // browser will send us back an If-Modified-Since if the user hits
        // Refresh(F5). We never parse
        // the If-Modified-Since since just it's presence is enough to have us
        // sent a 304. RTC 60688
        response.setHeader("Last-Modified", lastModFormat.format(new Date()));

        // get the file
        File file = new File(fileName);
        if (!file.exists()) {
            String m = "Unable to locate file [" + fileName + "], real path = [" + sc.getRealPath(path) + "] ";
            m += " path info = [" + request.getPathInfo() + "] servlet path=[" + path + "] ";
            logger.error("Exception!!!!!!!!!!!!!! " + m);
            response.setStatus(404);
            
            logger.info("Serve React Resource File not present End");
            return null;
        }

        if (isIndex) { // index.html
            // never cache this page, otherwise we risk having a stale CSRF token
            response.setHeader("Cache-Control", "no-store");

            // replace struts token value
            String s = new String(Files.readAllBytes(file.toPath()), "UTF-8");

            Enumeration<String> attNames = session.getAttributeNames();

            while (attNames.hasMoreElements()){
                String att = attNames.nextElement();
                String value = session.getAttribute(att).toString();
                logger.info(String.format("att=%s value=%s", att, value));
            }



            final String sessionToken = (String) session.getAttribute(StrutsConstants.TokenKey.SESSION);
            logger.info("Session Token:::: " + sessionToken);

            if(sessionToken != null) {
                logger.info("Replaced __STRUTS_TOKEN_VALUE__ with sessionToken");
                s = s.replace("__STRUTS_TOKEN_VALUE__", sessionToken);
                logger.info("Replaced Session Token: " + s);
            }

            // TODO START commented out based on John Owens instructions might have to debug and figure this out later
//             replace the JWT token value
            String jwtSessionToken = request.getHeader("AUTHORIZATION");

            Enumeration<String> requestHeader = request.getHeaderNames();


            logger.info("request header : " + requestHeader);

            while (requestHeader.hasMoreElements()){
                String att = requestHeader.nextElement();
                String value = String.valueOf(request.getAttribute(att));

                logger.info(String.format("header att=%s value=%s", att, value));
            }

            logger.info("JWTSession token="+ jwtSessionToken);
             if (jwtSessionToken != null)
//                 s = s.replace("__STRUTS_TOKEN_VALUE__", jwtSessionToken);
                s = s.replace("__JWT_TOKEN_VALUE__", jwtSessionToken);

//             replace the cookie path
//             s = s.replace("__OP_COOKIE_PATH__", CookieUtil.getContextPath(request));
            // TODO END commented out based on John Owens instructions might have to debug and figure this out later

            // replace static asset references with path including the context root
            String contextPath = sc.getContextPath();
            logger.info("Context Path obtained" + contextPath);
            
            s = s.replace("/app/jspview/helper", contextPath + "/app/jspview/helper");
            logger.info("Context path replaced: " + s);
            

            // replace the context value
            s = s.replace("__CONTEXT_TOKEN_VALUE__", contextPath);
            logger.info("Context token replaced: " + s);

            byte[] bytes = s.getBytes();
            response.setContentLength(bytes.length);
            
            logger.info("Serve React Resource isIndex End");
            return bytes;
        }
        else {
            // Public means can cache on intermediate servers since not user-specific
            response.setHeader("Cache-Control", "public, max-age=" + 60 * 60 * 24 * 30); // 30 days

            response.setContentLength((int) file.length());
            
            logger.info("Serve React Resource else End");
            return Files.readAllBytes(file.toPath());
        }
    }
}