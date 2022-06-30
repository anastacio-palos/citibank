package com.ibm.openpages.ext.interfaces.icaps.util;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.constant.EngineConstants;
import com.ibm.openpages.ext.interfaces.common.util.EngineUtil;
import com.ibm.openpages.ext.interfaces.common.util.ServiceUtil;
import com.ibm.openpages.ext.interfaces.icaps.bean.CapResponse;
import com.ibm.openpages.ext.interfaces.icaps.bean.ErrorData;
import com.ibm.openpages.ext.interfaces.icaps.bean.ICapsResponse;
import com.ibm.openpages.ext.interfaces.icaps.bean.IssueResponse;
import com.ibm.openpages.ext.interfaces.icaps.constant.AuditIssuePushConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;
import com.ibm.openpages.ext.interfaces.icaps.exception.IssueException;
import com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps.ICapsErrUpdateException;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import com.ibm.openpages.ext.tss.service.beans.SuperUserInformation;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class IssueToIcapsUtil {
    private final IServiceFactory apiFactory;
    private final IApplicationUtil applicationUtil;
    private final Logger logger;

    public IssueToIcapsUtil(IApplicationUtil applicationUtil, IServiceFactory apiFactory, Logger logger) {
        super();
        this.apiFactory = apiFactory;
        this.applicationUtil = applicationUtil;
        this.logger = logger;
    }


    private IServiceFactory getSuperUserResourceService(String superUserRegistrySettingPath) throws Exception {
        IServiceFactory serviceFactory;
        SuperUserInformation superUserInformation = new SuperUserInformation();
        superUserInformation.setSuperUserInfoInConfigFile(true);
        logger.trace("superUserRegistrySettingPath:" + superUserRegistrySettingPath);
        superUserInformation.setConfigFilePath(superUserRegistrySettingPath);
        logger.trace(superUserInformation.toString());
        serviceFactory = applicationUtil.createServiceFactoryForUser(superUserInformation);
        return serviceFactory;
    }

    /**
     * Converts a xml formatted <code>String</code> to a <code>ICapsResponse</code> object.
     *
     * @param xml formatted <code>String</code>
     * @return <code>ICapsResponse</code> object
     * @throws IssueException when the input param doesn't meet the expected format.
     */
    public ICapsResponse xmlToICapsResponse(String xml) throws IssueException {
        if (xml != null && !xml.isEmpty()) {
            ICapsResponse pushToICapsResponse = new ICapsResponse();
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ICapsResponse.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                pushToICapsResponse.setIssueResponse((IssueResponse) unmarshaller.unmarshal(new StringReader(xml)));
            } catch (JAXBException e) {
                logger.error("An error has been generated on the mapping process", e);
                throw new IssueException("An error has been generated on the mapping process", e);
            }
            logger.debug(" pushToICapsResponse " + pushToICapsResponse);
            return pushToICapsResponse;
        } else {
            logger.debug("--> class: ICapsHelperUtil --> Method: JsonToICapsResponse  --> response: null");
            return null;
        }
    }


    /**
     * Converts a JSON formatted <code>String</code> to a <code>ICapsResponse</code> object.
     *
     * @param json formatted <code>String</code>
     * @return <code>ICapsResponse</code> object
     */
    public ICapsResponse jsonToICapsResponse(String json) {
        logger.debug("JSON Response: " + json);
        if (json != null) {
            ObjectMapper mapper = new ObjectMapper();
            ICapsResponse pushToICapsResponse = null;
            try {
                if (json.contains("issueResponse")) {
                    pushToICapsResponse = mapper.readValue(json, ICapsResponse.class);
                } else {
                    pushToICapsResponse = new ICapsResponse();
                    pushToICapsResponse.setIssueResponse(mapper.readValue(json, IssueResponse.class));
                }
                logger.debug(" pushToICapsResponse " + pushToICapsResponse);
            } catch (JsonMappingException e) {
                logger.error("The given JSON is distinct than the expected.", e);
            } catch (IOException e) {
                logger.error("The given JSON is invalid.", e);
            } catch (Exception e) {
                logger.error("An error has been generated on the json process.", e);
            }
            return pushToICapsResponse;
        } else {
            logger.debug("--> class: ICapsHelperUtil --> Method: JsonToICapsResponse  --> response: null");
            return null;
        }
    }

    /**
     * Generates a Map wich contains the required headers to be incorporated in an ICaps processAIMSDataUpdates requests.
     *
     * @param headersJson
     * @return the Map with the required headers
     */
    public Map<String, String> getRegulatoryIssueToICapsRequestHeadersMap(String headersJson) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> headerMap = null;
        try {
            headerMap = mapper.readValue(headersJson, Map.class);
        } catch (Exception e) {
            logger.error("Error during headers generation", e);
        }
        return headerMap;
    }

    public void processICapsResponse(ICapsResponse pushToICapsResponse, IGRCObject issueObject, boolean isAuditIssue, boolean useSaveResource, boolean isProcessCapsActive, String userRegistrySettingPath, boolean useSuperUser) throws Exception {
        IResourceService service;
        if (useSuperUser) {
            logger.debug("Using Admin Session");
            IServiceFactory superApiFactory = getSuperUserResourceService(userRegistrySettingPath);
            service = superApiFactory.createResourceService();
        } else service = apiFactory.createResourceService();
        //ISSUE
        String issueErrMessage = "";
        String issueErr = "";
        processICapsResponseOnIssue(service, issueObject, pushToICapsResponse, isAuditIssue, useSaveResource, issueErr, issueErrMessage);
        //CAPS
        if (isProcessCapsActive) {
            logger.debug("Updating iCAPS Response on CAPS");
            List<IGRCObject> child = EngineUtil.getPrimaryChildrenFromIssue(issueObject, IssueCommonConstants.CITI_CAP_TYPE, apiFactory, logger);
            for (IGRCObject children : child) {
                String capErr = issueErr;
                String capErrMessage = issueErrMessage;
                processICapsResponseOnCAP(service, children, pushToICapsResponse, isAuditIssue, useSaveResource, capErr, capErrMessage);
            }
        }
    }

    public void processICapsResponseOnAuditIssue(ICapsResponse pushToICapsResponse, IGRCObject issueObject, String userRegistrySettingPath, boolean useSuperUser, boolean useSaveResource) throws Exception {

        IResourceService service;
        if (useSuperUser) {
            logger.debug("Using Admin Session");
            IServiceFactory superApiFactory = getSuperUserResourceService(userRegistrySettingPath);
            service = superApiFactory.createResourceService();
        } else service = apiFactory.createResourceService();
        //ISSUE
        String issueErrMessage = "";
        String issueErr = "";
        //populate the iCAPS response ONLY on the Issue
        ServiceUtil.setValueFromField(pushToICapsResponse.getJson(), issueObject.getField(IssueCommonConstants.ISSUE_ICAPS_RESPONSE));
        processICapsResponseOnIssue(service, issueObject, pushToICapsResponse, true, useSaveResource, issueErr, issueErrMessage);
    }

    public void processICapsResponseOnAuditCAP(ICapsResponse pushToICapsResponse, IGRCObject igrcObject, String userRegistrySettingPath, boolean useSuperUser, boolean useSaveResource) throws Exception {
        String capErrMessage = "DEFAULT";
        String capErr = "No";
        IResourceService service;
        if (useSuperUser) {
            logger.debug("Using Admin Session");
            IServiceFactory superApiFactory = getSuperUserResourceService(userRegistrySettingPath);
            logger.debug("superApiFactory created successfully");
            service = superApiFactory.createResourceService();
            logger.debug("Create Resource Service successfully");
        } else {
            service = apiFactory.createResourceService();
            logger.debug("ELSE Create Resource Service successfully");
        }
        processICapsResponseOnCAP(service, igrcObject, pushToICapsResponse, true, useSaveResource, capErr, capErrMessage);
    }

    public void incrementChangeCounterOnAuditCAP(IGRCObject igrcObject, long limit, String userRegistrySettingPath, boolean useSuperUser, boolean useSaveResource) throws Exception {

        IResourceService service;
        if (useSuperUser) {
            logger.debug("Using Admin Session");
            IServiceFactory superApiFactory = getSuperUserResourceService(userRegistrySettingPath);
            service = superApiFactory.createResourceService();
        } else service = apiFactory.createResourceService();


        IField field = igrcObject.getField(IssueCommonConstants.CAP_CHANGE_COUNTER);
        long counter = ServiceUtil.getLabelValueFromField(field).isEmpty()?0
                :Long.parseLong(ServiceUtil.getLabelValueFromField(field));
        logger.debug("counter: " + counter);
        logger.debug("counter limit: " + limit);
        counter = counter < limit ? counter + 1 : 0;
        logger.debug("new counter: " + counter);
        ServiceUtil.setValueFromField(Long.toString(counter),field);
        if(useSaveResource) {
            service.saveResource(igrcObject);
            logger.debug("grcObject with reference: " + igrcObject + " Name : " + igrcObject.getName() + " Id : " + igrcObject.getId() + " was updated!");
        }
    }

    private void processICapsResponseOnIssue(IResourceService service, IGRCObject issueObject, ICapsResponse pushToICapsResponse, boolean isAuditIssue, boolean useSaveResource, String issueErr, String issueErrMessage) {
        if (pushToICapsResponse != null) {
            if (pushToICapsResponse.getIssueResponse().getResponseStatus().equalsIgnoreCase(IssueCommonConstants.SUCCESS_VALUE)) {
                issueErr = IssueCommonConstants.NO_VALUE;
                if (isAuditIssue) {
                    IField issueField = issueObject.getField(IssueCommonConstants.ICAPS_ISSUE_ID_FIELD);
                    String iCapsId = ServiceUtil.getLabelValueFromField(issueField);
                    //only parse and populate the iCAPSID field IF the field is not already populated.
                    if (iCapsId.isEmpty()) {
                        ServiceUtil.setValueFromField(pushToICapsResponse.getIssueResponse().getIssueId(), issueField);
                    }
                    ServiceUtil.setValueFromField(pushToICapsResponse.getIssueResponse().getIssueId(), issueField);
                }
            } else {
                issueErr = IssueCommonConstants.YES_VALUE;
                issueErrMessage = (new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN)).format(Calendar.getInstance().getTime()) + " \n";
                if (pushToICapsResponse.getIssueResponse() != null && pushToICapsResponse.getIssueResponse().getErrorData() != null && !pushToICapsResponse.getIssueResponse().getErrorData().isEmpty()) {
                    for (ErrorData errorData : pushToICapsResponse.getIssueResponse().getErrorData()) {
                        if ((errorData.getErrorCode() != null && !errorData.getErrorCode().isEmpty()) || (errorData.getErrorMsg() != null && !errorData.getErrorMsg().isEmpty()))
                            issueErrMessage += errorData.getErrorCode() + " : " + errorData.getErrorMsg() + " \n";
                    }
                }
                if (issueErrMessage.length() > 4000) {
                    issueErrMessage = issueErrMessage.substring(0, 3900);
                }
            }
            IField iCapsErrField = issueObject.getField(IssueCommonConstants.ICAPS_ISSUE_ERR_FIELD);
            ServiceUtil.setValueFromField(issueErr, iCapsErrField);
            IField iCapsErrMessageField = issueObject.getField(IssueCommonConstants.ICAPS_ISSUE_ERR_MESSAGE_FIELD);
            ServiceUtil.setValueFromField(issueErrMessage, iCapsErrMessageField);
            logger.debug("Issue's new iCaps ERR: '" + issueErr + "' with MSG:'" + issueErrMessage + "'");

            if (useSaveResource) {
                service.saveResource(issueObject);
                logger.debug("grcObject with reference: " + issueObject + " Name : "+ issueObject.getName() + " Id : " + issueObject.getId() + " was updated!");
            }
        }
    }

    private void processICapsResponseOnCAP(IResourceService service, IGRCObject igrcObject, ICapsResponse pushToICapsResponse, boolean isAuditIssue, boolean useSaveResource, String capErr, String capErrMessage) {
        logger.debug("Process iCaps Response on Cap ");
        if(pushToICapsResponse != null ) {
            logger.debug("IF Process iCaps Response not null");
            if (pushToICapsResponse.getIssueResponse() != null && pushToICapsResponse.getIssueResponse().getCapResponse() != null && !pushToICapsResponse.getIssueResponse().getCapResponse().isEmpty()) {
                for (CapResponse capResponse : pushToICapsResponse.getIssueResponse().getCapResponse()) {
                    if (capResponse.getSourceSytemCapId().equalsIgnoreCase(igrcObject.getName()) || capResponse.getCapId().equalsIgnoreCase(igrcObject.getName())) {
                        if (capResponse.getResponseStatus().equals(IssueCommonConstants.SUCCESS_VALUE)) {
                            logger.debug("Cap Response Success Value");
                            capErr = IssueCommonConstants.NO_VALUE;
                            capErrMessage = "";
                            if (isAuditIssue) {
                                IField capIdField = igrcObject.getField(IssueCommonConstants.ICAPS_CAPID_FIELD);
                                String iCapsId = ServiceUtil.getLabelValueFromField(capIdField);
                                //only parse and populate the iCAPSID field IF the field is not already populated.
                                if (iCapsId.isEmpty()) {
                                    ServiceUtil.setValueFromField(capResponse.getCapId(), capIdField);
                                }
                            }
                        } else {
                            logger.debug("ELSE Cap Response NOT Success Value");
                            capErr = IssueCommonConstants.YES_VALUE;
                            capErrMessage = (new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN)).format(Calendar.getInstance().getTime()) + " \n";
                            if (capResponse.getErrorData() != null && !capResponse.getErrorData().isEmpty()) {
                                for (ErrorData errorData : capResponse.getErrorData()) {
                                    if ((errorData.getErrorCode() != null && !errorData.getErrorCode().isEmpty()) || (errorData.getErrorMsg() != null && !errorData.getErrorMsg().isEmpty()))
                                        capErrMessage += errorData.getErrorCode() + " : " + errorData.getErrorMsg() + " \n";
                                }
                                if (capErrMessage.length() > 4000) {
                                    capErrMessage = capErrMessage.substring(0, 3900);
                                }
                            }
                        }
                        //HERE


                        break;
                    }
                }
            }
            IField capErrField = igrcObject.getField(IssueCommonConstants.ICAPS_CAP_ERR_FIELD);
            ServiceUtil.setValueFromField(capErr, capErrField);
            IField capErrorMessageField = igrcObject.getField(IssueCommonConstants.ICAPS_CAP_ERR_MESSAGE_FIELD);
            ServiceUtil.setValueFromField(capErrMessage, capErrorMessageField);
            if (useSaveResource) {
                service.saveResource(igrcObject);
                logger.debug("CAP's new iCaps ERR: '" + capErr + "' with MSG:'" + capErrMessage + "'");
                logger.debug("grcObject with reference: " + igrcObject + " Name : "+ igrcObject.getName() + " Id : " + igrcObject.getId() + " was updated!");
            }
        }
    }


}