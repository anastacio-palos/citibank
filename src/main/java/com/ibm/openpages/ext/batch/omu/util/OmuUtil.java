package com.ibm.openpages.ext.batch.omu.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.security.IUser;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.ISecurityService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.batch.omu.bean.EmployeeKPIBean;
import com.ibm.openpages.ext.batch.omu.bean.EmployeeKPIDetailBean;
import com.ibm.openpages.ext.batch.omu.bean.IssueBean;
import com.ibm.openpages.ext.batch.omu.bean.OMURootBean;
import com.ibm.openpages.ext.batch.omu.constant.OmuConstants;
import com.ibm.openpages.ext.service.CRCService;
import org.apache.commons.logging.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OmuUtil {

    private static OMURootBean jsonToOMURootBean(String JSONString, Log logger) {
        if (JSONString != null) {
            ObjectMapper mapper = new ObjectMapper();
            OMURootBean engineBean = null;
            try {
                engineBean = mapper.readValue(JSONString, OMURootBean.class);
            } catch (Exception e) {
                logger.error("OMUMetadataBean", e);
            }
            return engineBean;
        } else {
            return null;
        }
    }

    public static OMURootBean invokeCRCOmuDetails(CRCService crc, String uri, Log logger) {
        OMURootBean omuBean = null;
        logger.debug("Endpoint to invoke:" + uri);
        Object rBody = crc.invoke(uri, null);
        if (rBody != null) {
            String json = (String) rBody;
            logger.debug("Response json:" + json);
            omuBean = jsonToOMURootBean(json, logger);
        }
        return omuBean;
    }

    public static List<EmployeeKPIBean> getEmployeeKpiList(List<EmployeeKPIDetailBean> detailList, Log logger) {

        List<EmployeeKPIBean> kpiList = new ArrayList<>();
        EmployeeKPIBean crcIssue = new EmployeeKPIBean(0, OmuConstants.CITI_ISS, OmuConstants.CRC, 0, 0);
        EmployeeKPIBean crcCap = new EmployeeKPIBean(0, OmuConstants.CITI_CAP, OmuConstants.CRC, 0, 0);
        EmployeeKPIBean crcOmu = new EmployeeKPIBean(0, OmuConstants.CITI_DSMT_LINK, OmuConstants.CRC, 0, 0);
        EmployeeKPIBean deltaIssue = new EmployeeKPIBean(0, OmuConstants.CITI_ISS, OmuConstants.EMPLOYEE, 0, 0);
        EmployeeKPIBean deltaCap = new EmployeeKPIBean(0, OmuConstants.CITI_CAP, OmuConstants.EMPLOYEE, 0, 0);
        EmployeeKPIBean deltaOmu = new EmployeeKPIBean(0, OmuConstants.CITI_DSMT_LINK, OmuConstants.EMPLOYEE, 0, 0);

        for (EmployeeKPIDetailBean detail : detailList) {
            if (detail != null && (detail.getObjectType().equalsIgnoreCase(crcIssue.getObjectType())) && detail.getSource().equalsIgnoreCase(crcIssue.getSource())) {
                if (detail.getStatus().equalsIgnoreCase(OmuConstants.SUCCESS)) {
                    crcIssue.addProcessed();
                } else {
                    crcIssue.addFailed();
                }
            }
            if (detail != null && (detail.getObjectType().equalsIgnoreCase(crcCap.getObjectType())) && detail.getSource().equalsIgnoreCase(crcCap.getSource())) {
                if (detail.getStatus().equalsIgnoreCase(OmuConstants.SUCCESS)) {
                    crcCap.addProcessed();
                } else {
                    crcCap.addFailed();
                }
            }
            if (detail != null && (detail.getObjectType().equalsIgnoreCase(crcOmu.getObjectType())) && detail.getSource().equalsIgnoreCase(crcOmu.getSource())) {
                if (detail.getStatus().equalsIgnoreCase(OmuConstants.SUCCESS)) {
                    crcOmu.addProcessed();
                } else {
                    crcOmu.addFailed();
                }
            }
            if (detail != null && (detail.getObjectType().equalsIgnoreCase(deltaIssue.getObjectType())) && detail.getSource().equalsIgnoreCase(deltaIssue.getSource())) {
                if (detail.getStatus().equalsIgnoreCase(OmuConstants.SUCCESS)) {
                    deltaIssue.addProcessed();
                } else {
                    deltaIssue.addFailed();
                }
            }
            if (detail != null && (detail.getObjectType().equalsIgnoreCase(deltaCap.getObjectType())) && detail.getSource().equalsIgnoreCase(deltaCap.getSource())) {
                if (detail.getStatus().equalsIgnoreCase(OmuConstants.SUCCESS)) {
                    deltaCap.addProcessed();
                } else {
                    deltaCap.addFailed();
                }
            }
            if (detail != null && (detail.getObjectType().equalsIgnoreCase(deltaOmu.getObjectType())) && detail.getSource().equalsIgnoreCase(deltaOmu.getSource())) {
                if (detail.getStatus().equalsIgnoreCase(OmuConstants.SUCCESS)) {
                    deltaOmu.addProcessed();
                } else {
                    deltaOmu.addFailed();
                }
            }
        }

        if (crcIssue.getTotalProcessed() > 0 || crcIssue.getFailed() > 0) {
            logger.debug("crcIssue: " + crcIssue);
            kpiList.add(crcIssue);
        }
        if (crcCap.getTotalProcessed() > 0 || crcCap.getFailed() > 0) {
            logger.debug("crcCap: " + crcCap);
            kpiList.add(crcCap);
        }
        if (crcOmu.getTotalProcessed() > 0 || crcOmu.getFailed() > 0) {
            logger.debug("crcOmu: " + crcOmu);
            kpiList.add(crcOmu);
        }
        if (deltaIssue.getTotalProcessed() > 0 || deltaIssue.getFailed() > 0) {
            logger.debug("deltaIssue: " + deltaIssue);
            kpiList.add(deltaIssue);
        }
        if (deltaCap.getTotalProcessed() > 0 || deltaCap.getFailed() > 0) {
            logger.debug("deltaCap: " + deltaCap);
            kpiList.add(deltaCap);
        }
        if (deltaOmu.getTotalProcessed() > 0 || deltaOmu.getFailed() > 0) {
            logger.debug("deltaOmu: " + deltaOmu);
            kpiList.add(deltaOmu);
        }

        logger.debug("kpiList -> " + kpiList);

        return kpiList;
    }

    public static List<String> getUserEmailList(IssueBean issue, IServiceFactory apiFactory, Log logger) {
        IUser iUser = null;
        List<String> emails = new ArrayList<>();
        ISecurityService service = apiFactory.createSecurityService();
        IConfigurationService configService = apiFactory.createConfigurationService();
        for (String userName : issue.getUsernames()) {
            try {
                iUser = service.getUser(userName);
                emails.add(iUser.getEmailAddress());
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
        return emails;
    }


    public static String getCurrentDay() {

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(OmuConstants.DATE_PATTERN);
        String newDate = dateFormat.format(date);

        return newDate;
    }
}
