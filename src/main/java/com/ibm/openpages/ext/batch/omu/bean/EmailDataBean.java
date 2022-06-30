package com.ibm.openpages.ext.batch.omu.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmailDataBean {

    private final Set<String> emailToList;
    private final List<EmployeeKPIDetailBean> employeeKPIDetailList;

    public EmailDataBean() {
        super();
        this.emailToList = new HashSet<>();
        this.employeeKPIDetailList = new ArrayList<>();
    }

    public Set<String> getEmailToList() {
        return emailToList;
    }

    public List<EmployeeKPIDetailBean> getEmployeeKPIDetailList() {
        return employeeKPIDetailList;
    }

    @Override
    public String toString() {
        return "EmailDataBean{" +
                "emailToList=" + emailToList +
                ", employeeKPIDetailList=" + employeeKPIDetailList +
                '}';
    }
}
