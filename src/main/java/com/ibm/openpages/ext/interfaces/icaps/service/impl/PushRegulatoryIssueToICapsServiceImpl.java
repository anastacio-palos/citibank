package com.ibm.openpages.ext.interfaces.icaps.service.impl;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.service.EngineService;
import com.ibm.openpages.ext.interfaces.common.service.impl.EngineServiceImpl;
import com.ibm.openpages.ext.interfaces.common.util.EngineTransformationUtil;
import com.ibm.openpages.ext.interfaces.common.util.RestClient;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import com.ibm.openpages.ext.interfaces.icaps.bean.ICapsResponse;
import com.ibm.openpages.ext.interfaces.icaps.bean.IssueResponse;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.RegulatoryIssuePushConstants;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.ICapsCallException;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.ICapsErrUpdateException;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.OPDataExtractException;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.OPDataTransformException;
import com.ibm.openpages.ext.interfaces.icaps.service.PushRegulatoryIssueToICapsService;
import com.ibm.openpages.ext.interfaces.icaps.util.IssueToIcapsUtil;
import com.ibm.openpages.ext.interfaces.icaps.util.PushIssueRegistry;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PushRegulatoryIssueToICapsServiceImpl implements PushRegulatoryIssueToICapsService {

    private IConfigurationService configService;
    private IResourceService resourceService;
    private EngineService engineService;
    private RestClient restClient;
    private IServiceFactory apiFactory;
    private IssueToIcapsUtil issueToIcapsUtil;
    private Logger logger;

    public PushRegulatoryIssueToICapsServiceImpl(IServiceFactory apiFactory, IApplicationUtil applicationUtil, Logger logger) {
        init(apiFactory, applicationUtil, logger);
    }

    public void pushRegulatoryIssue(IGRCObject object, String context) throws Exception {
        //step 1 - retrieve the issue object
        boolean isCapSource = false;
        IGRCObject issueObject = null;
        if (object.getType().getName().equals(IssueCommonConstants.CITI_ISS_TYPE)) {
            logger.debug("processing an issue");
            issueObject = object;
        } else {
            logger.debug("retrieving cap parent");
            isCapSource = true;
            issueObject = resourceService.getGRCObject(object.getPrimaryParent());
        }
        //step 2 evaluate if is a regulatory issue
        String issueSrc = ServiceUtil.getLabelValueFromField(issueObject.getField(IssueCommonConstants.ISSUE_SOURCE_FIELD));
        logger.debug(" Issue name " + issueObject.getName() + " with source " + issueSrc);
        if (IssueCommonConstants.REGULATORY_VALUE.equals(issueSrc)) {
            // step 3 - Obtain GRCObject data, then set it to engine
            Map<String, Object> regIssueExtractMap = extractIssueData(issueObject);
            //TODO create a new method to get CAP information
            processCAPSource(isCapSource, regIssueExtractMap, object);
            // step 4 - Data Extract Post processing
            String timeStamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
            dataPostProcessing(regIssueExtractMap, issueObject, context, timeStamp);
            // step 5 - Transform to xml String
            String xml = getXml(regIssueExtractMap);
            // step 6 - call to iCAPS
            ICapsResponse pushToICapsResponse = callToICaps(xml, timeStamp);
            // step 7 - update OP fields
            updateICapsErrFields(issueObject, pushToICapsResponse);
        }
    }

    private void init(IServiceFactory apiFactory, IApplicationUtil applicationUtil, Logger logger) {
        this.logger = logger;
        this.apiFactory = apiFactory;
        this.configService = apiFactory.createConfigurationService();
        this.resourceService = apiFactory.createResourceService();
        this.engineService = new EngineServiceImpl();
        this.restClient = new RestClient(logger);
        this.issueToIcapsUtil = new IssueToIcapsUtil(applicationUtil, apiFactory, logger);
    }

    private void dataPostProcessing(Map<String, Object> regIssueExtractMap, IGRCObject igrcObject, String context, String timeStamp) {
        try {
            Map<String, String> commonValidationsMap = (Map<String, String>) ((Map<String, Object>) regIssueExtractMap.get("AIMSDetails")).get("CommonValidations");
            commonValidationsMap.put("RequestID", timeStamp);
            if ("Returned to Owner".equals(commonValidationsMap.get("IssueStatus")) && !"Issue Not Ready Workflow".equals(context))
                commonValidationsMap.put("IssueStatus", "");
        } catch (Exception e) {
            throw new OPDataTransformException(e.getLocalizedMessage(), e);
        }
    }

    private String getXml(Map<String, Object> regIssueExtractMap) {
        String xml = "";
        try {
            xml = EngineTransformationUtil.mapOfMapsToXML(regIssueExtractMap, true);
            logger.info("Generated XML Request PAYLOAD: " + xml);
        } catch (Exception e) {
            throw new OPDataTransformException(e.getLocalizedMessage(), e);
        }
        return xml;
    }

    private ICapsResponse callToICaps(String xmlEngine, String timeStamp) {
        ICapsResponse pushToICapsResponse = new ICapsResponse();
        try {
            // step 6.1 - Generate the request headers
            String requestHeadersJson = configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.REQUEST_HEADERS);
            Map<String, String> headersMap = issueToIcapsUtil.getRegulatoryIssueToICapsRequestHeadersMap(requestHeadersJson);
            String requestIdHeader = configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.REQUEST_ID_HEADER);
            if (requestIdHeader != null && !requestIdHeader.isEmpty()) {
                //headersMap.put(requestIdHeader, regulatoryIssueToICapsRequest.getCommValidations().getRequestID());
                headersMap.put(requestIdHeader, timeStamp);
            }
            logger.debug("headersMap: " + headersMap);
            String mediaType = configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.REQUEST_MEDIA_TYPE);
            if (mediaType == null || mediaType.isEmpty()) mediaType = IssueCommonConstants.XML_MEDIA_TYPE;
            logger.debug("mediaType: " + mediaType);
            String timeout = configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.TIMEOUT);
            logger.debug("timeout: " + timeout);
            if (timeout == null || timeout.isEmpty()) timeout = IssueCommonConstants.DEFAULT_TIMEOUT;

            // step 6.2 - get the endpoint from the registry
            String icapsServiceURI = configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.REMOTE_SERVICE_ENDPOINT);
            logger.debug("Remote Endpoint: " + icapsServiceURI);
            pushToICapsResponse.setIssueResponse(new IssueResponse(IssueCommonConstants.FAILED_VALUE));
            // step 6.3 - invoque post service
            ResponseEntity<String> response = restClient.postInvocation(xmlEngine, headersMap, mediaType, icapsServiceURI, Integer.parseInt(timeout));
            logger.info("iCAPS Response: " + response);
            // step 6.4 - evaluate the service response, if errors detected set it on GRCObject
            pushToICapsResponse = issueToIcapsUtil.xmlToICapsResponse(response.getBody());
        } catch (Exception e) {
            throw new ICapsCallException(e.getLocalizedMessage(), e);
        }
        return pushToICapsResponse;
    }

    private void updateICapsErrFields(IGRCObject issueObject, ICapsResponse pushToICapsResponse) {
        try {
            String superUserRegistrySettingPath = configService.getConfigProperties().getProperty(IssueCommonConstants.SUPER_USER_REGISTRY_SETTING_PATH);
            String useSuperUserRegistrySetting = configService.getConfigProperties().getProperty(IssueCommonConstants.USE_SUPER_USER_REGISTRY_SETTING);
            boolean saveResource = !IssueCommonConstants.FALSE.equalsIgnoreCase(configService.getConfigProperties().getProperty(IssueCommonConstants.USE_SAVE_RESOURCE_REGISTRY_SETTING));
            boolean isProcessCapsActive = !IssueCommonConstants.FALSE.equalsIgnoreCase(configService.getConfigProperties().getProperty(RegulatoryIssuePushConstants.IS_UPDATE_CAPS_ACTIVE_REGISTRY_SETTING));

            boolean useSuperUser = useSuperUserRegistrySetting != null && !useSuperUserRegistrySetting.isEmpty() && useSuperUserRegistrySetting.equalsIgnoreCase("true");
            issueToIcapsUtil.processICapsResponse(pushToICapsResponse, issueObject, false, saveResource, isProcessCapsActive, superUserRegistrySettingPath, useSuperUser);
        } catch (Exception e) {
            throw new ICapsErrUpdateException(e.getLocalizedMessage(), e);
        }
    }

    private Map<String, Object> extractIssueData(IGRCObject issueObject) {
        Map<String, Object> regIssueExtract = null;
        try {
            RegulatoryIssuePushConstants pushConstants = new RegulatoryIssuePushConstants();
            PushIssueRegistry pushIssueRegistry = new PushIssueRegistry();
            pushIssueRegistry.init(apiFactory, pushConstants.getRegistryConstants(), logger);
            String[] metadataProperties = {RegulatoryIssuePushConstants.METADATA_REG_ISSUE_AIMS_DETAILS, RegulatoryIssuePushConstants.METADATA_REG_ISSUE_CAP, RegulatoryIssuePushConstants.METADATA_REG_ISSUE_COMMON, RegulatoryIssuePushConstants.METADATA_REG_ISSUE_REQUEST, RegulatoryIssuePushConstants.METADATA_REG_ISSUE_RET};
            regIssueExtract = engineService.extractAndGeneratePayload(issueObject, pushIssueRegistry, RegulatoryIssuePushConstants.METADATA_REG_ISSUE_REQUEST, metadataProperties, apiFactory, logger);
        } catch (Exception e) {
            throw new OPDataExtractException(e.getLocalizedMessage(), e);
        }
        return regIssueExtract;
    }

    private void processCAPSource(boolean isCapSource, Map<String, Object> regIssueExtractMap, IGRCObject capObject){
        if(isCapSource) {

            try {
                RegulatoryIssuePushConstants pushConstants = new RegulatoryIssuePushConstants();
                PushIssueRegistry pushIssueRegistry = new PushIssueRegistry();
                pushIssueRegistry.init(apiFactory, pushConstants.getRegistryConstants(), logger);
                String[] metadataProperties = {RegulatoryIssuePushConstants.METADATA_REG_ISSUE_CAP};
                Map<String, Object> capExtract = engineService.extractAndGeneratePayload(capObject, pushIssueRegistry, RegulatoryIssuePushConstants.METADATA_REG_ISSUE_CAP, metadataProperties, apiFactory, logger);
                List<Map<String, Object>> capFormList = (List<Map<String, Object>>) ((Map<String, Object>) regIssueExtractMap.get("AIMSDetails")).get("CapForm");
                logger.trace("capFormList after change:"+capFormList);
                for(int i = 0; i < capFormList.size(); i++){
                    Map<String, Object> m = capFormList.get(i);
                    if(capObject.getName().equals( m.get("iCAPSCapID") )){
                        capFormList.remove(i);
                        capFormList.add(capExtract);
                    }
                }
                logger.trace("capFormList before change:"+capFormList);
            } catch (Exception e) {
                throw new OPDataExtractException(e.getLocalizedMessage(), e);
            }
        }
    }
}