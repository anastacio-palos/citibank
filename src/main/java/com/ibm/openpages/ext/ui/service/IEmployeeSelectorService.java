/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * © Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.service;

import javax.servlet.http.HttpSession;

import com.ibm.openpages.ext.ui.bean.EmployeeSelectorHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.GridData;

public interface IEmployeeSelectorService {

    public EmployeeSelectorHelperAppInfo getHelperHeaderInfo(
            EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo) throws Exception;

    public GridData getExistingOMU(EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo, String soeId,
            HttpSession session) throws Exception;

    public GridData searchEmployee(String soeId, String firstName, String lastName) throws Exception;

    public GridData getEmployeeHRDSMTInfo(String soeId) throws Exception;

    public EmployeeSelectorHelperAppInfo processIssueHelperLogic(
            EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo, GridData gridData) throws Exception;

    public EmployeeSelectorHelperAppInfo processCAPHelperLogic(
            EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo) throws Exception;

    public EmployeeSelectorHelperAppInfo getLandingPageInfo(EmployeeSelectorHelperAppInfo employeeSelectorHelperAppInfo)
            throws Exception;

}
