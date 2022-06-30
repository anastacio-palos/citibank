package com.ibm.openpages.ext.batch.omu.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.ext.batch.omu.bean.*;
import com.ibm.openpages.ext.batch.omu.constant.OmuConstants;
import com.ibm.openpages.ext.batch.omu.constant.OmuRegistryConstants;
import com.ibm.openpages.ext.batch.omu.exception.EmployeesException;
import com.ibm.openpages.ext.batch.omu.notification.EmailContent;
import com.ibm.openpages.ext.batch.omu.persistence.EmployeeDao;
import com.ibm.openpages.ext.batch.omu.service.EmployeeService;
import com.ibm.openpages.ext.batch.omu.util.OmuUtil;
import com.ibm.openpages.ext.batch.omu.util.ServiceUtil;
import com.ibm.openpages.ext.config.AppConfig;
import com.ibm.openpages.ext.notification.EmailService;
import com.ibm.openpages.ext.service.CRCService;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EmployeeServiceImpl implements EmployeeService {

    private final ILoggerUtil loggerUtil;
    private final LocalServiceFactory apiFactory;
    private final IConfigurationService configService;
    private final Log logger;
    IResourceService resourceService;
    @Autowired
    private CRCService crcService;
    @Autowired
    private RestTemplate restTemplate;

    public EmployeeServiceImpl(LocalServiceFactory apiFactory, ILoggerUtil loggerUtil) {
        this.apiFactory = apiFactory;
        this.configService = apiFactory.createConfigurationService();
        this.resourceService = apiFactory.createResourceService();
        this.loggerUtil = loggerUtil;
        this.logger = this.loggerUtil.getExtLogger(OmuRegistryConstants.OMU_INTERFACE_LOGGER);
    }

    private boolean updateCAPKeyValidation(CAPBean cap, String value, String action, String source, EmailDataBean emailData) {
        boolean response = false;
        try {
            IGRCObject capObject = resourceService.getGRCObject(new Id(cap.getId()));
            if (OmuConstants.ACTION_APPEND.equalsIgnoreCase(action)) {
                ServiceUtil.appendMultiEnumValue(value, capObject.getField(OmuConstants.CAP_VAL), logger);
            } else if (OmuConstants.ACTION_REMOVE.equalsIgnoreCase(action)) {
                ServiceUtil.removeMultiEnumValue(value, capObject.getField(OmuConstants.CAP_VAL), logger);
            }
            resourceService.saveResource(capObject);
            emailData.getEmployeeKPIDetailList().add(new EmployeeKPIDetailBean(0, cap.getName(), capObject.getType().getName(), source, OmuConstants.SUCCESS, OmuConstants.EMPTY));
            logger.debug("The CAP " + cap.getId() + " - " + cap.getName() + "; field " + OmuConstants.CAP_VAL + " was updated with:" + action + " " + value);
            response = true;
        } catch (Exception e) {
            String errorMsg = "Error while updating the CAP:" + e.getMessage();
            logger.error("Error while updating the CAP", e);
            emailData.getEmployeeKPIDetailList().add(new EmployeeKPIDetailBean(0, cap.getId(), cap.getType(), source, OmuConstants.FAILURE, errorMsg));
        }
        return response;
    }

    private boolean updateOMUActiveFlag(OMUBean omuBean, String isActive, String source, EmailDataBean emailData) {
        boolean response = false;
        try {
            IGRCObject omu = resourceService.getGRCObject(new Id(omuBean.getId()));
            ServiceUtil.setValueFromField(isActive, omu.getField(OmuConstants.ACTIVE), logger);
            resourceService.saveResource(omu);
            emailData.getEmployeeKPIDetailList().add(new EmployeeKPIDetailBean(0, omu.getName(), omu.getType().getName(), source, OmuConstants.SUCCESS, OmuConstants.EMPTY));
            logger.debug("The OMU " + omu.getId().toString() + " - " + omu.getName() + "; field " + OmuConstants.ACTIVE + " was updated with:" + isActive);
            response = true;
        } catch (Exception e) {
            String errorMsg = "Error while updating the Issue:" + e.getMessage();
            logger.error("Error while updating the Issue", e);
            emailData.getEmployeeKPIDetailList().add(new EmployeeKPIDetailBean(0, omuBean.getId(), omuBean.getType(), source, OmuConstants.FAILURE, errorMsg));
        }
        return response;
    }


    private boolean updateIssueKeyValidation(IssueBean issue, String value, String action, String source, EmailDataBean emailData) {
        boolean response = false;
        try {
            IGRCObject issueObject = resourceService.getGRCObject(new Id(issue.getId()));
            if (OmuConstants.ACTION_APPEND.equalsIgnoreCase(action)) {
                ServiceUtil.appendMultiEnumValue(value, issueObject.getField(OmuConstants.ISS_VAL), logger);
            } else if (OmuConstants.ACTION_REMOVE.equalsIgnoreCase(action)) {
                ServiceUtil.removeMultiEnumValue(value, issueObject.getField(OmuConstants.ISS_VAL), logger);
            }
            resourceService.saveResource(issueObject);
            emailData.getEmployeeKPIDetailList().add(new EmployeeKPIDetailBean(0, issue.getName(), issueObject.getType().getName(), source, OmuConstants.SUCCESS, OmuConstants.EMPTY));
            logger.debug("The Issue " + issue.getId() + " - " + issue.getName() + "; field " + OmuConstants.ISS_VAL + " was updated with:" + action + " " + value);
            response = true;
        } catch (Exception e) {
            String errorMsg = "Error while updating the Issue:" + e.getMessage();
            logger.error("Error while updating the Issue", e);
            emailData.getEmployeeKPIDetailList().add(new EmployeeKPIDetailBean(0, issue.getId(), issue.getType(), source, OmuConstants.FAILURE, errorMsg));
        }
        return response;
    }

    private boolean validateCAP(CAPBean cap, String soeId) {
        boolean isValidOMU = false;
        if (soeId != null) {
            isValidOMU = cap.getAssignee().contains(soeId.toLowerCase());
        }
        if (!isValidOMU) {
            logger.debug("The evaluated CAP " + cap.getName() + " with the following properties is NOT valid!");
            if (soeId != null) logger.debug(" omuExecutive='" + cap.getAssignee() + "' against soeId=" + soeId + "'");
        } else {
            logger.debug("The evaluated OMU " + cap.getName() + " is valid!");
        }
        return isValidOMU;
    }

    private boolean validateOMU(String process, OMUBean omu, String soeId, String manSegmentId, String manGeoId) {
        boolean isValidOMU = false;
        logger.debug("Validating OMU in " + process);
        if (OmuConstants.DELTA_ACTIVE_TO_INACTIVE.equalsIgnoreCase(process)) {
            isValidOMU = OmuConstants.ACTIVE_YES.equals(omu.getIsOmu()) && OmuConstants.ACTIVE_YES.equals(omu.getActive()) && OmuConstants.SCOPE_IN.equals(omu.getScp()) && omu.getOmuExec().contains(soeId.toLowerCase());

        } else if (OmuConstants.DELTA_INACTIVE_TO_ACTIVE.equalsIgnoreCase(process)) {
            isValidOMU = OmuConstants.ACTIVE_YES.equals(omu.getIsOmu()) && OmuConstants.ACTIVE_NO.equals(omu.getActive()) && omu.getOmuExec().contains(soeId.toLowerCase());

        } else if (OmuConstants.CRC_STATUS_INACTIVE_TO_ACTIVE.equalsIgnoreCase(process)) {
            isValidOMU = OmuConstants.ACTIVE_YES.equals(omu.getIsOmu()) && OmuConstants.ACTIVE_NO.equals(omu.getActive()) && OmuConstants.SCOPE_IN.equals(omu.getScp()) && omu.getOmuExec().contains(soeId.toLowerCase()) && omu.getMsId().trim().equals(manSegmentId.trim()) && omu.getMgId().trim().equals(manGeoId.trim());

        } else if (OmuConstants.CRC_STATUS_ACTIVE_TO_INACTIVE.equalsIgnoreCase(process)) {
            isValidOMU = OmuConstants.ACTIVE_YES.equals(omu.getIsOmu()) && OmuConstants.ACTIVE_YES.equals(omu.getActive()) && OmuConstants.SCOPE_IN.equals(omu.getScp()) && omu.getOmuExec().contains(soeId.toLowerCase()) && omu.getMsId().trim().equals(manSegmentId.trim()) && omu.getMgId().trim().equals(manGeoId.trim());
        } else if (OmuConstants.CRC_DELETE_LIST_OMU_EMPTY.equalsIgnoreCase(process)) {
            isValidOMU = OmuConstants.ACTIVE_YES.equals(omu.getIsOmu()) && OmuConstants.ACTIVE_YES.equals(omu.getActive()) && OmuConstants.SCOPE_IN.equals(omu.getScp()) && omu.getOmuExec().contains(soeId.toLowerCase());
        } else if (OmuConstants.CRC_DELETE_WITH_LIST_OMU.equalsIgnoreCase(process)) {
            isValidOMU = OmuConstants.ACTIVE_YES.equals(omu.getIsOmu()) && OmuConstants.ACTIVE_YES.equals(omu.getActive()) && OmuConstants.SCOPE_IN.equals(omu.getScp()) && omu.getOmuExec().contains(soeId.toLowerCase()) && omu.getMsId().trim().equals(manSegmentId.trim()) && omu.getMgId().trim().equals(manGeoId.trim());
        }

        if (!isValidOMU) {
            logger.debug("The evaluated OMU " + omu.getName() + " with the following properties is NOT valid!");

            logger.debug(" isOmu='" + omu.getIsOmu() + "'");
            logger.debug(" active='" + omu.getActive() + "'");
            logger.debug(" scp='" + omu.getScp() + "'");

            logger.debug(" omuExecutive='" + omu.getOmuExec() + "' against soeId=" + soeId + "'");
            if (manSegmentId != null)
                logger.debug(" omuMSID='" + omu.getMsId() + "' against ManSegmentId=" + manSegmentId + "'");
            if (manGeoId != null) logger.debug(" omuMGID='" + omu.getMgId() + "' against ManGeoId='" + manGeoId + "'");
        } else {
            logger.debug("The evaluated OMU " + omu.getName() + " is valid!");
        }
        return isValidOMU;
    }

    private boolean validateCAPStatus(CAPBean cap, List<String> capValidStatusList) {
        boolean isValidIssue = false;
        try {
            String capStatus = cap.getStatus();
            if (capValidStatusList.contains(capStatus)) {
                isValidIssue = true;
                logger.debug("CAP " + cap.getId() + " - " + cap.getName() + ", status " + capStatus + " is VALID");
            } else {
                logger.debug("CAP " + cap.getId() + " - " + cap.getName() + ", status " + capStatus + " is INVALID");
            }
        } catch (Exception e) {
            logger.error("Error validating the CAP status", e);
        }
        return isValidIssue;
    }

    private boolean validateIssueStatus(IssueBean issue, List<String> issueValidStatusList) {
        boolean isValidIssue = false;
        try {
            String issueStatus = issue.getStatus();
            if (issueValidStatusList.contains(issueStatus)) {
                isValidIssue = true;
                logger.debug("Issue " + issue.getId() + " - " + issue.getName() + ", status " + issueStatus + " is VALID");
            } else {
                logger.debug("Issue " + issue.getId() + " - " + issue.getName() + ", status " + issueStatus + " is INVALID");
            }
        } catch (Exception e) {
            logger.error("Error validating the issue status", e);
        }
        return isValidIssue;
    }

    private boolean validateIssueHRFields(IssueBean issue, String gocNumber, String gocName, Log logger) {
        boolean existHRChange = false;
        try {
            logger.debug("Employee HR Fields: gocNumber=" + gocNumber + " gocName=" + gocName);
            if ((gocNumber != null && !(gocNumber.equalsIgnoreCase(issue.getGocNumber()))) || (gocName != null && !(gocName.equalsIgnoreCase(issue.getGocName())))) {
                existHRChange = true;
                logger.debug("Issue " + issue.getId() + " - " + issue.getName() + " HR Fields: GocNumber=" + issue.getGocNumber() + " AND GocName=" + issue.getGocName() + " has CHANGES");
            }
        } catch (Exception e) {
            logger.error("Error validating the issue status", e);
        }
        return existHRChange;
    }

    private List<IssueBean> getIssueBeanList(String soeId) {
        String queryIssue = configService.getConfigProperties().getProperty(OmuRegistryConstants.OP_QUERY_TO_RETRIEVE_ISSUES);
        List<IssueBean> getIssuesResponse = new ArrayList<>();
        String[] values = new String[]{soeId.toLowerCase(), soeId.toUpperCase()};
        List<List<String>> qsList = ServiceUtil.queryService(apiFactory, queryIssue, values, logger);
        if (qsList != null && !qsList.isEmpty()) {
            for (List<String> row : qsList) {
                IssueBean issue = new IssueBean();
                issue.setId(row.get(0));
                issue.setName(row.get(1));
                issue.setStatus(row.get(2));
                issue.setGocNumber(row.get(3));
                issue.setGocName(row.get(4));
                for (int i = 5; i <= 7; i++) {
                    if (row.get(i) != null) issue.getUsernames().add(row.get(i));
                }
                issue.setType(OmuConstants.CITI_ISS);
                getIssuesResponse.add(issue);
            }
        }
        return getIssuesResponse;
    }

    public List<CAPBean> getCapBeanList(String parentIssueObjectId) {

        String query = configService.getConfigProperties().getProperty(OmuRegistryConstants.OP_QUERY_TO_RETRIEVE_CAPS);
        List<CAPBean> childList = new ArrayList<>();
        String[] values = new String[]{parentIssueObjectId};
        List<List<String>> qsList = ServiceUtil.queryService(apiFactory, query, values, logger);
        if (qsList != null && !qsList.isEmpty()) {
            for (List<String> row : qsList) {
                CAPBean cap = new CAPBean();
                cap.setId(row.get(0));
                cap.setName(row.get(1));
                cap.setAssignee(row.get(2));
                cap.setValidation(row.get(3));
                cap.setStatus(row.get(4));
                cap.setType(OmuConstants.CITI_CAP);
                childList.add(cap);
            }
        }
        return childList;
    }

    public List<OMUBean> getOmuBeanList(String parentIssueObjectId) {

        String query = configService.getConfigProperties().getProperty(OmuRegistryConstants.OP_QUERY_TO_RETRIEVE_OMUS);
        List<OMUBean> childList = new ArrayList<>();
        String[] values = new String[]{parentIssueObjectId};
        List<List<String>> qsList = ServiceUtil.queryService(apiFactory, query, values, logger);
        if (qsList != null && !qsList.isEmpty()) {
            for (List<String> row : qsList) {
                OMUBean omu = new OMUBean();
                omu.setId(row.get(0));
                omu.setName(row.get(1));
                omu.setIsOmu(row.get(2));
                omu.setActive(row.get(3));
                omu.setScp(row.get(4));
                omu.setOmuExec(row.get(5));
                omu.setMsId(row.get(6));
                omu.setMgId(row.get(7));
                omu.setType(OmuConstants.CITI_DSMT_LINK);
                childList.add(omu);
            }
        }
        return childList;
    }


    /**
     * <p>
     * Process value from from Openpages  {@link Id}
     * </p>
     *
     * @process all data
     */

    public void processCRCUpdates(EmailDataBean emailData) throws EmployeesException {
        String source = OmuConstants.CRC;
        logger.debug("**********************************************************************");
        logger.debug("*******************  Process CRC Updates Start *********************** ");
        logger.debug("**********************************************************************");

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<String> issueValidStatusList = mapper.readValue(configService.getConfigProperties().getProperty(OmuRegistryConstants.LIST_ISSUE_STATUS), List.class);
            EmployeeDao employeeDao = new EmployeeDao(apiFactory, loggerUtil);
            String lastRunDate = employeeDao.getLastRunDate();
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat(OmuConstants.DATE_PATTERN);
            String newDate = dateFormat.format(date);
            if (lastRunDate == null) {
                lastRunDate = newDate;
            }
            String crcEndpointApi2 = configService.getConfigProperties().getProperty(OmuRegistryConstants.URI_RESPONSE_SOE_ID);
            String uri = configService.getConfigProperties().getProperty(OmuRegistryConstants.URI);

            String uriLastRunDate = MessageFormat.format(uri, lastRunDate);
            if (restTemplate == null) {
                AppConfig appConfig = new AppConfig();
                restTemplate = appConfig.restTemplate();
            }
            if (crcService == null) crcService = new CRCService(restTemplate, loggerUtil);
            OMURootBean omuDetail = OmuUtil.invokeCRCOmuDetails(crcService, uriLastRunDate, logger);
            if (omuDetail != null && omuDetail.getData() != null) {
                for (CRCDataBean data : omuDetail.getData()) {
                    String soeId = OmuConstants.EMPTY;
                    if (data.getNode() != null) soeId = data.getNode().getSoeId();
                    logger.debug("**********************************************************************");
                    if (employeeDao.verifySoeIdEmployee(soeId)) {
                        logger.debug("Processing SOEID:" + soeId);
                        logger.debug("**********************************************************************");
                        if (data.getChangeHistory() != null) {
                            for (CRCChangeHistoryBean changeHistory : data.getChangeHistory()) {
                                logger.debug("ChangeHistory changeType is -> " + changeHistory.getChangeType());
                                if (OmuConstants.ADDED.equalsIgnoreCase(changeHistory.getChangeType().trim())) {
                                    logger.debug("Processing a ADDED change type");
                                    //For issues meeting the criteria in #4.1 and #4.2, if the Scorecard Executive Name matches the SOE ID,
                                    List<IssueBean> associatedIssues = getIssueBeanList(soeId);
                                    logger.debug("For the soeid " + soeId + " the following issues are associated " + associatedIssues);
                                    associatedIssues.stream().filter(issue -> validateIssueStatus(issue, issueValidStatusList)).forEach(issue -> {
                                        emailData.getEmailToList().addAll(OmuUtil.getUserEmailList(issue, apiFactory, logger));
                                        //then set the Validation to "Scorecard Responsible Executive becomes responsible for one or more additional OMUs "
                                        updateIssueKeyValidation(issue, OmuConstants.VALIDATION_OMU_UPDATE, OmuConstants.ACTION_APPEND, source, emailData);
                                    });
                                } else if (OmuConstants.DELETED.equalsIgnoreCase(changeHistory.getChangeType().trim())) {
                                    logger.debug("Processing a DELETED change type");
                                    //Invoke the following service with the soeid /dsmt-ref-service/omu/soeId?soeId=<soeid> which will return a list of OMU for the user
                                    String _crcEndpointApi2 = MessageFormat.format(crcEndpointApi2, soeId);
                                    OMURootBean listOMU = OmuUtil.invokeCRCOmuDetails(crcService, _crcEndpointApi2, logger);
                                    List<IssueBean> associatedIssues = getIssueBeanList(soeId);
                                    logger.debug("For the soeid " + soeId + " the following issues are associated " + associatedIssues);
                                    if (listOMU.getData() != null && !listOMU.getData().isEmpty()) {
                                        logger.debug("listOMU.size > 0");
                                        associatedIssues.stream().filter(issue -> validateIssueStatus(issue, issueValidStatusList)).forEach(issue -> {
                                            emailData.getEmailToList().addAll(OmuUtil.getUserEmailList(issue, apiFactory, logger));
                                            //then set the Validation to "Scorecard Responsible Executive no longer has responsibility for OMU "
                                            updateIssueKeyValidation(issue, OmuConstants.VALIDATION_INVALID_OMU_EXEC, OmuConstants.ACTION_APPEND, source, emailData);
                                            //1b) For all OMUs under the issues in 1a and that match criteria in #4.3,
                                            // if the OMU Executive name matches the inactive SOEID,
                                            // MSID matches the Managed Segment or GOC Number (Citi_DSMT_Link:Citi-DL:MSID),
                                            // MGID matches the Managed Geography Node Number (Citi_DSMT_Link:Citi-DL:MGID),
                                            // ( all the values to be checked are from the CRC delta query NOT the user query)
                                            getOmuBeanList(issue.getId()).forEach(omu -> {
                                                if (validateOMU(OmuConstants.CRC_DELETE_WITH_LIST_OMU, omu, data.getNode().getSoeId(), changeHistory.getManSegmentId(), changeHistory.getManGeoId())) {
                                                    //update DSMT Link Active (Citi_DSMT_Link:Citi-DL:Active) = No
                                                    updateOMUActiveFlag(omu, OmuConstants.ACTIVE_NO, source, emailData);
                                                }
                                            });
                                        });
                                    } else {
                                        logger.debug("listOMU.size == 0");
                                        associatedIssues.stream().filter(issue -> validateIssueStatus(issue, issueValidStatusList)).forEach(issue -> {
                                            emailData.getEmailToList().addAll(OmuUtil.getUserEmailList(issue, apiFactory, logger));
                                            //then set the Validation to "Scorecard Responsible Executive no longer has responsibility for OMU "
                                            updateIssueKeyValidation(issue, OmuConstants.VALIDATION_INVALID_OMU_EXEC, OmuConstants.ACTION_APPEND, source, emailData);
                                            //1b) For all OMUs under the issues in 1a and that match criteria in #4.3,
                                            // if the OMU Executive name matches the inactive SOEID
                                            getOmuBeanList(issue.getId()).forEach(omu -> {
                                                if (validateOMU(OmuConstants.CRC_DELETE_LIST_OMU_EMPTY, omu, data.getNode().getSoeId(), null, null)) {
                                                    //update DSMT Link Active (Citi_DSMT_Link:Citi-DL:Active) = No
                                                    updateOMUActiveFlag(omu, OmuConstants.ACTIVE_NO, source, emailData);
                                                }
                                            });
                                        });
                                    }
                                    logger.debug(listOMU.toString());
                                } else if ("STATUS".equalsIgnoreCase(changeHistory.getChangeType().trim())) {
                                    logger.debug("Processing a STATUS change type");
                                    String oldValue = changeHistory.getOldValue();
                                    String newValue = changeHistory.getNewValue();
                                    logger.debug("oldValue=" + oldValue + " && newValue=" + newValue);
                                    if ((OmuConstants.A.equalsIgnoreCase(newValue) && OmuConstants.I.equalsIgnoreCase(oldValue)) || (OmuConstants.I.equalsIgnoreCase(newValue) && OmuConstants.A.equalsIgnoreCase(oldValue))) {
                                        //For issues meeting the criteria in #4.1 and #4.2, if the Scorecard Executive Name matches the SOE ID goto 1b
                                        List<IssueBean> associatedIssues = getIssueBeanList(soeId);
                                        logger.debug("For the soeid " + soeId + " the following issues are associated " + associatedIssues);
                                        associatedIssues.stream().filter(issue -> validateIssueStatus(issue, issueValidStatusList)).forEach(issue -> {
                                            if (OmuConstants.I.equalsIgnoreCase(newValue) && OmuConstants.A.equalsIgnoreCase(oldValue))
                                                updateIssueKeyValidation(issue, OmuConstants.VALIDATION_INACTIVE_OMU, OmuConstants.ACTION_APPEND, source, emailData);
                                            emailData.getEmailToList().addAll(OmuUtil.getUserEmailList(issue, apiFactory, logger));
                                            List<OMUBean> omus = getOmuBeanList(issue.getId());
                                            omus.forEach(omu -> {
                                                if (OmuConstants.I.equalsIgnoreCase(oldValue) && OmuConstants.A.equalsIgnoreCase(newValue)) {
                                                    logger.debug(OmuConstants.INACTIVE_TO_ACTIVE);
                                                    //update DSMT Link Active (Citi_DSMT_Link:Citi-DL:Active) = Yes
                                                    if (validateOMU(OmuConstants.CRC_STATUS_INACTIVE_TO_ACTIVE, omu, data.getNode().getSoeId(), data.getNode().getManSegmentId(), data.getNode().getManGeoId()))
                                                        updateOMUActiveFlag(omu, OmuConstants.ACTIVE_YES, source, emailData);
                                                } else if (OmuConstants.A.equalsIgnoreCase(oldValue) && OmuConstants.I.equalsIgnoreCase(newValue)) {
                                                    logger.debug(OmuConstants.ACTIVE_TO_INACTIVE);
                                                    //update DSMT Link Active (Citi_DSMT_Link:Citi-DL:Active) = No
                                                    if (validateOMU(OmuConstants.CRC_STATUS_ACTIVE_TO_INACTIVE, omu, data.getNode().getSoeId(), data.getNode().getManSegmentId(), data.getNode().getManGeoId()))
                                                        updateOMUActiveFlag(omu, OmuConstants.ACTIVE_NO, source, emailData);
                                                }
                                            });
                                        });
                                    }
                                }
                            }
                        }
                    } else {
                        logger.debug("The soeid " + soeId + " is not valid!");
                    }
                }
            }
            employeeDao.updateLastRunDate(lastRunDate);
        } catch (Exception e) {
            String msg = "Unhandled Error while processing CRC Updates";
            logger.error(msg, e);
            throw new EmployeesException(msg, e);
        }
        logger.debug("**********************************************************************");
        logger.debug("********************  Process CRC Updates End  *********************** ");
        logger.debug("**********************************************************************");
    }

    @Override
    public void processEmployeeDelta(EmailDataBean emailData) throws EmployeesException {
        logger.debug("**********************************************************************");
        logger.debug("**************  Process Employee Delta Updates Start ***************** ");
        logger.debug("**********************************************************************");
        String source = OmuConstants.EMPLOYEE;
        try {
            EmployeeDao employeeDao = new EmployeeDao(apiFactory, loggerUtil);
            ObjectMapper mapper = new ObjectMapper();
            List<String> issueValidStatusList = mapper.readValue(configService.getConfigProperties().getProperty(OmuRegistryConstants.LIST_ISSUE_STATUS), List.class);
            List<String> capValidStatusList = mapper.readValue(configService.getConfigProperties().getProperty(OmuRegistryConstants.LIST_CAP_STATUS), List.class);
            String failCount = configService.getConfigProperties().getProperty(OmuRegistryConstants.FAIL_COUNT);
            employeeDao.getDelta(failCount).stream().forEach(delta -> {
                String newInvalidHR = delta.getInvalidHR();
                logger.debug("delta -> " + delta);
                boolean result;
                String processError = null;
                try {
                    List<IssueBean> associatedIssues = getIssueBeanList(delta.getSoeId());
                    logger.debug("For the soeid " + delta.getSoeId() + " the following issues are associated " + associatedIssues);
                    if (delta.getOldStatus() != null && delta.getNewStatus() != null && delta.getOldStatus().trim().equalsIgnoreCase(OmuConstants.I) && delta.getNewStatus().trim().equalsIgnoreCase(OmuConstants.A)) {
                        logger.debug(OmuConstants.INACTIVE_TO_ACTIVE);
                        associatedIssues.stream().filter(issue -> validateIssueStatus(issue, issueValidStatusList)).forEach(issue -> {
                            emailData.getEmailToList().addAll(OmuUtil.getUserEmailList(issue, apiFactory, logger));
                            //then if the validation is set to "Scorecard Responsible Executive becomes an Inactive User", remove that validation
                            updateIssueKeyValidation(issue, OmuConstants.VALIDATION_INACTIVE_USER, OmuConstants.ACTION_REMOVE, source, emailData);
                            //2) For all OMUs under the issues in 1a and that match criteria in #4.3, if the OMU Executive name matches the inactive SOEID, update DSMT Link Active (Citi_DSMT_Link:Citi-DL:Active) = Yes
                            getOmuBeanList(issue.getId()).forEach(omu -> {
                                if (validateOMU(OmuConstants.DELTA_INACTIVE_TO_ACTIVE, omu, delta.getSoeId(), null, null)) {
                                    //update DSMT Link Active (Citi_DSMT_Link:Citi-DL:Active) = No
                                    updateOMUActiveFlag(omu, OmuConstants.ACTIVE_YES, source, emailData);
                                }
                            });
                            //3) For all CAPS meeting the criteria in #4.4, if the CAPS assignee is same as invalid SOE ID,
                            getCapBeanList(issue.getId()).stream().filter(cap -> validateCAPStatus(cap, capValidStatusList)).forEach(cap -> {
                                if (validateCAP(cap, delta.getSoeId())) {
                                    //then remoev the value "Inactive Action Owner " from the CAP Validation field
                                    updateCAPKeyValidation(cap, OmuConstants.VALIDATION_INACTIVE_ACTION_OWNER, OmuConstants.ACTION_REMOVE, source, emailData);
                                }
                            });
                        });
                        result = true;
                    } else if (delta.getOldStatus() != null && delta.getNewStatus() != null && delta.getOldStatus().trim().equalsIgnoreCase(OmuConstants.A) && delta.getNewStatus().trim().equalsIgnoreCase(OmuConstants.I)) {
                        logger.debug(OmuConstants.ACTIVE_TO_INACTIVE);
                        associatedIssues.stream().filter(issue -> validateIssueStatus(issue, issueValidStatusList)).forEach(issue -> {
                            emailData.getEmailToList().addAll(OmuUtil.getUserEmailList(issue, apiFactory, logger));
                            //set the Validation to "Scorecard Responsible Executive becomes an Inactive User"
                            updateIssueKeyValidation(issue, OmuConstants.VALIDATION_INACTIVE_USER, OmuConstants.ACTION_APPEND, source, emailData);
                            //2) For all OMUs under the issues in 1a and that match criteria in #4.3, if the OMU Executive name matches the inactive SOEID, update DSMT Link Active (Citi_DSMT_Link:Citi-DL:Active) = No
                            getOmuBeanList(issue.getId()).forEach(omu -> {
                                if (validateOMU(OmuConstants.DELTA_ACTIVE_TO_INACTIVE, omu, delta.getSoeId(), null, null)) {
                                    //update DSMT Link Active (Citi_DSMT_Link:Citi-DL:Active) = No
                                    updateOMUActiveFlag(omu, OmuConstants.ACTIVE_NO, source, emailData);
                                }
                            });
                            //3) For all CAPS meeting the criteria in #4.4, if the CAPS assignee is same as invalid SOE ID,
                            getCapBeanList(issue.getId()).stream().filter(cap -> validateCAPStatus(cap, capValidStatusList)).forEach(cap -> {
                                if (validateCAP(cap, delta.getSoeId())) {
                                    //then set the  CAP val field to Invalid Action Owner
                                    updateCAPKeyValidation(cap, OmuConstants.VALIDATION_INACTIVE_ACTION_OWNER, OmuConstants.ACTION_APPEND, source, emailData);
                                }
                            });
                        });
                        result = true;
                    } else if (OmuConstants.INVALID_HR_YES.equalsIgnoreCase(delta.getInvalidHR())) {
                        associatedIssues.stream().filter(issue -> validateIssueStatus(issue, issueValidStatusList)).forEach(issue -> {
                            emailData.getEmailToList().addAll(OmuUtil.getUserEmailList(issue, apiFactory, logger));
                            updateIssueKeyValidation(issue, OmuConstants.VALIDATION_OMU_EXEC_UPDATE, OmuConstants.ACTION_APPEND, source, emailData);

                        });
                        newInvalidHR = OmuConstants.INVALID_HR_NO;
                        result = true;
                    } else {
                        // 2.1 Compare the HR fields for the user to the HR fields on the issue -
                        //UserHRFieldsBean employeeHRFields = employeeDao.getUserHRFields(delta.getSoeId());
                        associatedIssues//.stream().filter(issue -> validateIssueHRFields(issue, employeeHRFields.getCghGoc(), employeeHRFields.getCghName(), logger))
                                .forEach(issue -> {

                                    emailData.getEmailToList().addAll(OmuUtil.getUserEmailList(issue, apiFactory, logger));
                                    // if there are any changes, set the Issue status(Citi-Iss:IssVal:Status)=  Change in Scorecard Responsible Executive’s HR DSMT
                                    updateIssueKeyValidation(issue, OmuConstants.VALIDATION_SCORECARD_HR_UPDATE, OmuConstants.ACTION_APPEND, source, emailData);
                                });
                        result = true;
                    }
                } catch (Exception e) {
                    logger.error("Error Processing Delta: ", e);
                    processError = e.getMessage();
                    result = false;
                }
                //3.Update delta status
                if (!result) employeeDao.updateDeltaProcessError(delta.getSoeId(), processError);
                else employeeDao.updateDeltaProcessSuccess(delta.getSoeId(), newInvalidHR);
            });
        } catch (Exception e) {
            String msg = "Unhandled Error while processing Delta Updates";
            logger.error(msg, e);
            throw new EmployeesException(msg, e);
        }
        logger.debug("**********************************************************************");
        logger.debug("**************  Process Employee Delta Updates End ***************** ");
        logger.debug("**********************************************************************");
    }

    @Override
    public void processEmailService(EmailContent emailContent, List<EmployeeKPIBean> employeeKpiList, EmailDataBean emailData) throws EmployeesException {
        try {
            logger.debug("employeeKPIDetailBean -> " + emailData.getEmployeeKPIDetailList());
            logger.debug("employeeKpiList -> " + employeeKpiList);
            logger.debug("emailToList -> " + emailData.getEmailToList());
            emailContent.setProcessSummary(employeeKpiList);
            emailContent.setProcessDetail(emailData.getEmployeeKPIDetailList());
            logger.debug("Attachment Creation");
            EmailService emailService = new EmailService(apiFactory, loggerUtil);
            logger.debug("----------- Send Email -----------");
            emailContent.setEndDate(new Date());
            emailService.sendEmail(String.format(emailContent.getSubject(), new Date()), emailContent.buildEmail(), emailData.getEmailToList(), null, emailContent.buildAttachment(), OmuConstants.OMU_INTERFACE_JOB_REPORT_ZIP);
            emailContent.deleteAttachmentFiles();
            logger.debug("Send Email :) successfully");

        } catch (Exception e) {
            String msg = "Unhandled Error while processing Email";
            logger.error(msg, e);
            throw new EmployeesException(msg, e);
        }
    }
}
