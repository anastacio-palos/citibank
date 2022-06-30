package com.ibm.openpages.ext.batch.omu.persistence;


import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.batch.omu.bean.DeltaBean;
import com.ibm.openpages.ext.batch.omu.bean.UserHRFieldsBean;
import com.ibm.openpages.ext.batch.omu.constant.OmuRegistryConstants;
import com.ibm.openpages.ext.batch.omu.exception.EmployeesException;
import com.ibm.openpages.ext.batch.omu.util.OmuUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.dao.DSMTDataAccessConfig;
import com.ibm.openpages.ext.ui.util.DSMTDatasourceConfigReader;
import org.apache.commons.logging.Log;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    private IServiceFactory sf;
    private DataSource dataSource;
    private Log log;

    public EmployeeDao(IServiceFactory serviceFactory, ILoggerUtil iLoggerUtil) throws Exception {
        super();
        init(serviceFactory, iLoggerUtil);
    }

    public void init(IServiceFactory serviceFactory, ILoggerUtil loggerUtil) throws Exception {
        sf = serviceFactory;
        log = loggerUtil.getExtLogger(OmuRegistryConstants.OMU_INTERFACE_LOGGER);
        DSMTDatasourceConfigReader configReader = new DSMTDatasourceConfigReader(loggerUtil, null, serviceFactory);
        DSMTDataAccessConfig config = new DSMTDataAccessConfig(loggerUtil, configReader);
        dataSource = config.dataSource();
    }

    public boolean verifySoeIdEmployee(String soeID) throws EmployeesException {
        String SQL_SELECT_BY_SOE_ID = sf.createConfigurationService().getConfigProperties().getProperty(OmuRegistryConstants.SQL_SELECT_BY_SOE_ID);
        log.debug("SQL used to validate:" + SQL_SELECT_BY_SOE_ID);
        boolean result = false;
        if (soeID != null && !soeID.isEmpty()) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(SQL_SELECT_BY_SOE_ID)) {
                ps.setString(1, soeID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String soeId = rs.getString(1);
                    if (soeID.equalsIgnoreCase(soeId)) {
                        result = true;
                    }
                }
            } catch (Exception ex) {
                String errorMessage = "Error verifying Employee SoeId";
                log.error(errorMessage, ex);
                throw new EmployeesException(errorMessage, ex);
            }
        }
        return result;
    }

    public List<DeltaBean> getDelta(String failCount) throws EmployeesException {
        String SQL_SELECT_DELTA_OLD_NEW_STATUS = sf.createConfigurationService().getConfigProperties().getProperty(OmuRegistryConstants.SQL_SELECT_DELTA_OLD_NEW_STATUS);
        log.debug("******* SQL_SELECT_DELTA_OLD_NEW_STATUS ********* " + SQL_SELECT_DELTA_OLD_NEW_STATUS);
        List<DeltaBean> deltaBeanList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(SQL_SELECT_DELTA_OLD_NEW_STATUS)) {
            ps.setString(1, failCount);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DeltaBean deltaBean = new DeltaBean();
                deltaBean.setSoeId(rs.getString("SOE_ID"));
                deltaBean.setOldStatus(rs.getString("OLD_STATUS"));
                deltaBean.setNewStatus(rs.getString("NEW_STATUS"));
                deltaBean.setInvalidHR(rs.getString("INVALID_HR_DSMT"));
                deltaBeanList.add(deltaBean);
            }
        } catch (Exception ex) {
            String errorMessage = "Error retrieving delta";
            log.error(errorMessage, ex);
            throw new EmployeesException(errorMessage, ex);
        }
        return deltaBeanList;
    }

    public void updateDeltaProcessSuccess(String soeId, String invalidHR) throws EmployeesException {
        String SQL_UPDATE_DELTA_BY_SOE_ID = sf.createConfigurationService().getConfigProperties().getProperty(OmuRegistryConstants.SQL_UPDATE_DELTA_BY_SOE_ID);
        log.debug("SQL_UPDATE_DELTA_BY_SOE_ID " + SQL_UPDATE_DELTA_BY_SOE_ID);
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_DELTA_BY_SOE_ID)) {
            ps.setString(1, invalidHR);
            ps.setString(2, soeId);
            ps.executeUpdate();
        } catch (Exception ex) {
            String errorMessage = "Error updating delta";
            log.error(errorMessage, ex);
            throw new EmployeesException(errorMessage, ex);
        }
    }

    public void updateDeltaProcessError(String soeId, String error) throws EmployeesException {
        String SQL_UPDATE_OP_EMPLOYEE_ERROR = sf.createConfigurationService().getConfigProperties().getProperty(OmuRegistryConstants.SQL_UPDATE_OP_EMPLOYEE_ERROR);
        //UPDATE OP_EMPLOYEE SET ERROR_DESC =  ?, FAIL_COUNT = FAIL_COUNT + 1 WHERE SOE_ID =  ?
        log.debug("SQL_UPDATE_OP_EMPLOYEE_ERROR " + SQL_UPDATE_OP_EMPLOYEE_ERROR);
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_OP_EMPLOYEE_ERROR)) {
            ps.setString(1, error);
            ps.setString(2, soeId);
            ps.executeUpdate();
        } catch (Exception ex) {
            String errorMessage = "Error updating fail error";
            log.error(errorMessage, ex);
            throw new EmployeesException(errorMessage, ex);
        }
    }


    public UserHRFieldsBean getUserHRFields(String soeId) throws EmployeesException {
        log.debug("******* EmployeeDao getUserHRFields Start ********* ");
        UserHRFieldsBean employeeBean = null;
        String SQL_SELECT_GDW_EMPLOYEE_BYE_SOE_ID = sf.createConfigurationService().getConfigProperties().getProperty(OmuRegistryConstants.SQL_SELECT_GDW_EMPLOYEE_BYE_SOE_ID);
        log.debug("SQL_SELECT_GDW_EMPLOYEE_BYE_SOE_ID " + SQL_SELECT_GDW_EMPLOYEE_BYE_SOE_ID);
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(SQL_SELECT_GDW_EMPLOYEE_BYE_SOE_ID)) {
            ps.setString(1, soeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // "SELECT GDW_EMPLOYEE_LIST.CGH_GOC, 'dummy CGH Name' AS CGH_NAME from GDW_EMPLOYEE_LIST WHERE SOE_ID = ?"
                employeeBean = new UserHRFieldsBean();
                String cghGoc = rs.getString("CGH_GOC");
                String cghName = rs.getString("CGH_NAME");
                employeeBean.setCghGoc(cghGoc);
                employeeBean.setCghName(cghName);
            }
        } catch (Exception ex) {
            String errorMessage = "Error updating fail error";
            log.error(errorMessage, ex);
            throw new EmployeesException(errorMessage, ex);
        }
        log.debug("******* EmployeeDao getUserHRFields END ********* ");
        return employeeBean;
    }

    public String getLastRunDate() throws EmployeesException {
        log.debug("******* EmployeeDao getLastRunDate Start ********* ");
        String SQL_SELECT_VALUE_OMU_CONFIG = sf.createConfigurationService().getConfigProperties().getProperty(OmuRegistryConstants.SQL_SELECT_VALUE_OMU_CONFIG);
        log.debug("SQL_SELECT_VALUE_OMU_CONFIG " + SQL_SELECT_VALUE_OMU_CONFIG);
        String value = null;
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(SQL_SELECT_VALUE_OMU_CONFIG)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getString("VALUE");
                log.debug("value:  " + value);
            }
        } catch (Exception ex) {
            String errorMessage = "Error updating fail error";
            log.error(errorMessage, ex);
            throw new EmployeesException(errorMessage, ex);
        }
        log.debug("******* EmployeeDao getLastRunDate END ********* ");
        return value;
    }

    public void updateLastRunDate(String lastRunDate) throws EmployeesException {
        log.debug("******* EmployeeDao updateLastRunDate Start ********* ");
        String SQL_UPDATE_VALUE_OMU_CONFIG = sf.createConfigurationService().getConfigProperties().getProperty(OmuRegistryConstants.SQL_UPDATE_VALUE_OMU_CONFIG);
        log.debug("SQL_UPDATE_VALUE_OMU_CONFIG " + SQL_UPDATE_VALUE_OMU_CONFIG);
        log.debug("lastRunDate - >" + lastRunDate);
        lastRunDate = OmuUtil.getCurrentDay();
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_VALUE_OMU_CONFIG)) {
            ps.setString(1, lastRunDate);
            ps.executeUpdate();
        } catch (Exception ex) {
            String errorMessage = "Error updating fail error";
            log.error(errorMessage, ex);
            throw new EmployeesException(errorMessage, ex);
        }
        log.debug("******* EmployeeDao updateLastRunDate END ********* ");
    }
}
