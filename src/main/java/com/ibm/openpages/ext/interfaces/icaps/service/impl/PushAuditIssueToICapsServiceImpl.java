package com.ibm.openpages.ext.interfaces.icaps.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.constant.EngineConstants;
import com.ibm.openpages.ext.interfaces.common.service.EngineService;
import com.ibm.openpages.ext.interfaces.common.service.impl.EngineServiceImpl;
import com.ibm.openpages.ext.interfaces.common.util.RestClient;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import com.ibm.openpages.ext.interfaces.icaps.bean.ICapsResponse;
import com.ibm.openpages.ext.interfaces.icaps.bean.IssueResponse;
import com.ibm.openpages.ext.interfaces.icaps.constant.AuditIssuePushConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.RegulatoryIssuePushConstants;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.ICapsCallException;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.ICapsErrUpdateException;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.OPDataExtractException;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.OPDataTransformException;
import com.ibm.openpages.ext.interfaces.icaps.service.PushAuditIssueToICapsService;
import com.ibm.openpages.ext.interfaces.icaps.util.IssueToIcapsUtil;
import com.ibm.openpages.ext.interfaces.icaps.util.PushIssueRegistry;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Map;

/**
 *
 */
public class PushAuditIssueToICapsServiceImpl implements PushAuditIssueToICapsService {
    private IServiceFactory apiFactory;
    private IConfigurationService configService;
    private IResourceService resourceService;
    private EngineService engineService;
    private RestClient restClient;
    private IssueToIcapsUtil issueToIcapsUtil;
    private Logger logger;

    public PushAuditIssueToICapsServiceImpl(IServiceFactory apiFactory, IApplicationUtil applicationUtil, Logger logger) {
        init(apiFactory, applicationUtil, logger);
    }


    @Override
    public void incrementCAPChangeCounter(IGRCObject object) throws Exception {
        try {
            long limit = Long.parseLong(configService.getConfigProperties().getProperty(AuditIssuePushConstants.CAP_CHANGE_COUNTER_LIMIT));
            String superUserRegistrySettingPath = configService.getConfigProperties().getProperty(IssueCommonConstants.SUPER_USER_REGISTRY_SETTING_PATH);
            String useSuperUserRegistrySetting = configService.getConfigProperties().getProperty(IssueCommonConstants.USE_SUPER_USER_REGISTRY_SETTING);
            boolean useSuperUser = useSuperUserRegistrySetting != null && !useSuperUserRegistrySetting.isEmpty() && useSuperUserRegistrySetting.equalsIgnoreCase("true");
            boolean saveResource = !IssueCommonConstants.FALSE.equalsIgnoreCase(configService.getConfigProperties().getProperty(IssueCommonConstants.USE_SAVE_RESOURCE_REGISTRY_SETTING));
            issueToIcapsUtil.incrementChangeCounterOnAuditCAP(object, limit, superUserRegistrySettingPath, useSuperUser, saveResource);
        } catch (Exception e) {
            throw new ICapsErrUpdateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void parseICapsResponse(IGRCObject igrcObject) throws Exception {
        try {
            String superUserRegistrySettingPath = configService.getConfigProperties().getProperty(IssueCommonConstants.SUPER_USER_REGISTRY_SETTING_PATH);
            String useSuperUserRegistrySetting = configService.getConfigProperties().getProperty(IssueCommonConstants.USE_SUPER_USER_REGISTRY_SETTING);
            boolean useSuperUser = useSuperUserRegistrySetting != null && !useSuperUserRegistrySetting.isEmpty() && useSuperUserRegistrySetting.equalsIgnoreCase("true");
            boolean saveResource = !IssueCommonConstants.FALSE.equalsIgnoreCase(configService.getConfigProperties().getProperty(IssueCommonConstants.USE_SAVE_RESOURCE_REGISTRY_SETTING));
            String iCapsResponse = ServiceUtil.getLabelValueFromField(igrcObject.getField(IssueCommonConstants.CAP_ICAPS_RESPONSE));
            logger.debug("iCapsResponse:"+iCapsResponse);
            if(!iCapsResponse.isEmpty()){
                ICapsResponse pushToICapsResponse = issueToIcapsUtil.jsonToICapsResponse(iCapsResponse);
                issueToIcapsUtil.processICapsResponseOnAuditCAP(pushToICapsResponse, igrcObject, superUserRegistrySettingPath, useSuperUser, saveResource);
            }
        } catch (Exception e) {
            throw new ICapsErrUpdateException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void pushAuditIssue(IGRCObject object) throws Exception {
        //step 1 - retrieve the issue object
        IGRCObject issueObject = null;
        if (object.getType().getName().equals(IssueCommonConstants.CITI_ISS_TYPE)) {
            logger.debug("processing an issue");
            issueObject = object;
        } else {
            logger.debug("retrieving cap parent");
            issueObject = resourceService.getGRCObject(object.getPrimaryParent());
        }
        //step 2 evaluate if is an audit issue
        String issueSrc = ServiceUtil.getLabelValueFromField(issueObject.getField(IssueCommonConstants.ISSUE_SOURCE_FIELD));
        logger.debug(" Issue name " + issueObject.getName() + " with source " + issueSrc);
        if (!IssueCommonConstants.REGULATORY_VALUE.equals(issueSrc)) {
            // step 3 - Obtain GRCObject data, then set it to engine
            Map<String, Object> auditIssueExtractMap = extractIssueData(issueObject);
            // step 4 - Data Extract Post processing
            dataPostProcessing(auditIssueExtractMap, issueObject);
            // step 5 - Transform to json String
            String json = getJson(auditIssueExtractMap);
            // step 6 - call to iCAPS
            ICapsResponse pushToICapsResponse = callToICaps(json);
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

    private Map<String, Object> extractIssueData(IGRCObject issueObject) {
        Map<String, Object> auditIssueExtractMap = null;
        try {
            String[] metadataProperties = {AuditIssuePushConstants.METADATA_AUDIT_REQUEST_JSON_FILE, AuditIssuePushConstants.METADATA_AUDIT_ISSUE_JSON_FILE, AuditIssuePushConstants.METADATA_AUDIT_CAP_JSON_FILE};
            PushIssueRegistry pushIssueRegistry = new PushIssueRegistry();
            AuditIssuePushConstants pushConstants = new AuditIssuePushConstants();
            pushIssueRegistry.init(apiFactory, pushConstants.getRegistryConstants(), logger);
            String issue_metadata = pushIssueRegistry.getProperty(AuditIssuePushConstants.METADATA_AUDIT_ISSUE_FIRST_JSON_FILE) + pushIssueRegistry.getProperty(AuditIssuePushConstants.METADATA_AUDIT_ISSUE_SECOND_JSON_FILE);
            pushIssueRegistry.putProperty(AuditIssuePushConstants.METADATA_AUDIT_ISSUE_JSON_FILE, issue_metadata);
            auditIssueExtractMap = engineService.extractAndGeneratePayload(issueObject, pushIssueRegistry, AuditIssuePushConstants.METADATA_AUDIT_REQUEST_JSON_FILE, metadataProperties, apiFactory, logger);
        } catch (Exception e) {
            throw new OPDataExtractException(e.getLocalizedMessage(), e);
        }
        return auditIssueExtractMap;
    }

    private String getJson(Map<String, Object> auditIssueExtractMap) {
        String json = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(auditIssueExtractMap);
            logger.info("Generated JSON Request PAYLOAD: " + json);
        } catch (Exception e) {
            throw new OPDataTransformException(e.getLocalizedMessage(), e);
        }
        return json;
    }

    private void dataPostProcessing(Map<String, Object> auditIssueExtractMap, IGRCObject issueObject) {
        try {
            Map<String, String> issueRequestMap = (Map<String, String>) auditIssueExtractMap.get(AuditIssuePushConstants.ISSUE_REQUEST);
            if (issueRequestMap != null && !issueRequestMap.isEmpty()) {

                String dsmtAttribute = issueRequestMap.get(AuditIssuePushConstants.DSMT_ATTRIBUTE);
                String impactedDsmtAttributes = issueRequestMap.get(AuditIssuePushConstants.IMPACTED_DSMT_ATTRIBUTES);

                logger.trace("dsmtAttribute:" + dsmtAttribute);
                logger.trace("impactedDsmtAttributes:" + impactedDsmtAttributes);

                if (dsmtAttribute != null && !dsmtAttribute.isEmpty()) {
                    int separatorFirstIndex = dsmtAttribute.indexOf(EngineConstants.SEMICOLON_SEPARATOR_REGEX);
                    if (separatorFirstIndex > 0) {
                        if (impactedDsmtAttributes.isEmpty())
                            impactedDsmtAttributes = dsmtAttribute.substring(separatorFirstIndex + 1);
                        else impactedDsmtAttributes += dsmtAttribute.substring(separatorFirstIndex);

                        dsmtAttribute = dsmtAttribute.substring(0, separatorFirstIndex);
                        logger.trace("NEW dsmtAttribute:" + dsmtAttribute);
                        logger.trace("NEW impactedDsmtAttributes:" + impactedDsmtAttributes);
                        issueRequestMap.put(AuditIssuePushConstants.IMPACTED_DSMT_ATTRIBUTES, impactedDsmtAttributes);
                        issueRequestMap.put(AuditIssuePushConstants.DSMT_ATTRIBUTE, dsmtAttribute);
                    }
                }

                if (issueRequestMap.get("eci").contains("Escalation")) issueRequestMap.put("eci", "No");
                else issueRequestMap.put("eci", "Yes");

                String migPubDate = ServiceUtil.getLabelValueFromField(issueObject.getField(IssueCommonConstants.ICAPS_ISSUE_MINGPUBDATE_FIELD));
                String migRepNum = ServiceUtil.getLabelValueFromField(issueObject.getField(IssueCommonConstants.ICAPS_ISSUE_MINGREPNUM_FIELD));

                if(!migPubDate.isEmpty() || !migRepNum.isEmpty()){
                    issueRequestMap.put(AuditIssuePushConstants.ISSUE_IDENTIFICATION_DATE, migPubDate );
                    issueRequestMap.put(AuditIssuePushConstants.ISSUE_ACTIVATION_DATE, migPubDate );
                    issueRequestMap.put(AuditIssuePushConstants.ISSUE_REPORT_NUMBER, migRepNum );
                }

            }
        } catch (Exception e) {
            throw new OPDataTransformException(e.getLocalizedMessage(), e);
        }
    }

    private void updateICapsErrFields(IGRCObject issueObject, ICapsResponse pushToICapsResponse) {
        try {
            String superUserRegistrySettingPath = configService.getConfigProperties().getProperty(IssueCommonConstants.SUPER_USER_REGISTRY_SETTING_PATH);
            String useSuperUserRegistrySetting = configService.getConfigProperties().getProperty(IssueCommonConstants.USE_SUPER_USER_REGISTRY_SETTING);
            boolean saveResource = !IssueCommonConstants.FALSE.equalsIgnoreCase(configService.getConfigProperties().getProperty(IssueCommonConstants.USE_SAVE_RESOURCE_REGISTRY_SETTING));
            boolean useSuperUser = useSuperUserRegistrySetting != null && !useSuperUserRegistrySetting.isEmpty() && useSuperUserRegistrySetting.equalsIgnoreCase("true");
            issueToIcapsUtil.processICapsResponseOnAuditIssue(pushToICapsResponse, issueObject, superUserRegistrySettingPath, useSuperUser, saveResource);
        } catch (Exception e) {
            throw new ICapsErrUpdateException(e.getLocalizedMessage(), e);
        }
    }


    private ICapsResponse callToICaps(String json) {
        ICapsResponse pushToICapsResponse = new ICapsResponse();
        try {
            // step 6.1 - Generate the request headers
            String requestHeadersJson = configService.getConfigProperties().getProperty(AuditIssuePushConstants.REQUEST_HEADERS);
            Map<String, String> headersMap = issueToIcapsUtil.getRegulatoryIssueToICapsRequestHeadersMap(requestHeadersJson);
            String requestIdHeader = configService.getConfigProperties().getProperty(AuditIssuePushConstants.REQUEST_ID_HEADER);
            if (requestIdHeader != null && !requestIdHeader.isEmpty()) {
                headersMap.put(requestIdHeader, String.valueOf(Calendar.getInstance().getTimeInMillis()));
            }
            logger.debug("headersMap: " + headersMap);
            String mediaType = configService.getConfigProperties().getProperty(AuditIssuePushConstants.REQUEST_MEDIA_TYPE);
            logger.debug("mediaType: " + mediaType);
            if (mediaType == null || mediaType.isEmpty()) mediaType = IssueCommonConstants.JSON_MEDIA_TYPE;
            String timeout = configService.getConfigProperties().getProperty(AuditIssuePushConstants.TIMEOUT);
            logger.debug("timeout: " + timeout);
            if (timeout == null || timeout.isEmpty()) timeout = IssueCommonConstants.DEFAULT_TIMEOUT;
            // step 6.2 - get the endpoint from the registry
            String icapsServiceURI = configService.getConfigProperties().getProperty(AuditIssuePushConstants.REMOTE_SERVICE_ENDPOINT);
            logger.debug("Remote Endpoint: " + icapsServiceURI);
            pushToICapsResponse.setIssueResponse(new IssueResponse(IssueCommonConstants.FAILED_VALUE));
            // step 6.3 - invoke post service
            ResponseEntity<String> response = restClient.postInvocation(json, headersMap, mediaType, icapsServiceURI, Integer.parseInt(timeout));
            // step 6.4 - evaluate the service response, if errors detected set it on GRCObject
            pushToICapsResponse = issueToIcapsUtil.jsonToICapsResponse(response.getBody());
            pushToICapsResponse.setJson(response.getBody());
        } catch (Exception e) {
            throw new ICapsCallException(e.getLocalizedMessage(), e);
        }
        return pushToICapsResponse;
    }


}
