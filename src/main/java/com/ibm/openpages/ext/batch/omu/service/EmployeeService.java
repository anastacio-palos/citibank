package com.ibm.openpages.ext.batch.omu.service;

import com.ibm.openpages.ext.batch.omu.bean.EmailDataBean;
import com.ibm.openpages.ext.batch.omu.bean.EmployeeKPIBean;
import com.ibm.openpages.ext.batch.omu.exception.EmployeesException;
import com.ibm.openpages.ext.batch.omu.notification.EmailContent;

import java.util.List;


public interface EmployeeService {

    void processCRCUpdates(EmailDataBean emailData) throws EmployeesException;

    void processEmployeeDelta(EmailDataBean emailData) throws EmployeesException;

    void processEmailService(EmailContent emailContent, List<EmployeeKPIBean> employeeKpiList, EmailDataBean emailData) throws EmployeesException;

}
