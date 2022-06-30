/*****************************************************************************
 * Licensed Materials - Property of IBM
 *
 * OpenPages GRC Platform (PID: 5725-D51)
 *
 * (c) Copyright IBM Corporation 2018 - 2020. All Rights Reserved.
 *  
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U.S. Copyright Office.
 ******************************************************************************
 */

package com.ibm.openpages.ext.rest;

import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import com.ibm.openpages.ext.interfaces.query.constant.QueryRegistryConstants;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/*
* /grc/ext/
*/
@RestController
@RequestMapping(value = "/api")
public class QueryController extends BaseExtController {

	private Logger logger;

	@RequestMapping(value = "query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<List<String>> queryHandler(@RequestBody String query, HttpServletRequest request, HttpServletResponse response) {
		IServiceFactory apiFactory = super.getServiceFactory(request);
		IConfigurationService configService = apiFactory.createConfigurationService();
		String enableDebug = configService.getConfigProperties().getProperty(QueryRegistryConstants.ENABLE_DEBUG);// ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(ENABLE_DEBUG, getAttributes()), servicesUtil.getConfigProperties());
		String logFilePath = configService.getConfigProperties().getProperty(QueryRegistryConstants.LOG_FILE_PATH);// ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());
		String logFileSize = configService.getConfigProperties().getProperty(QueryRegistryConstants.LOG_FILE_SIZE);// ApplicationUtil.getRegistrySetting(TriggerUtil.getAttributeValue(LOG_FILE_PATH, getAttributes()), servicesUtil.getConfigProperties());
		String logLevel = configService.getConfigProperties().getProperty(QueryRegistryConstants.LOG_LEVEL);
		logger = com.ibm.openpages.ext.interfaces.common.util.LoggerUtil.getEXTLogger(logFilePath, enableDebug, logFileSize,logLevel);//Referencia de la variable del registry
		logger.debug("***** QueryController - queryHandler START *****");
		IServiceFactory factory = super.getServiceFactory(request);
		logger.debug("***** QueryController - queryHandler END *****");
		return ServiceUtil.queryService(factory, query, null);
	}

}

