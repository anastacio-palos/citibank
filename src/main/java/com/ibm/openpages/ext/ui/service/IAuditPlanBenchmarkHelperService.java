/*
 IBM Confidential OCO Source Materials
 5725-D51, 5725-D52, 5725-D53, 5725-D54
 © Copyright IBM Corporation 2021
 The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 has been deposited with the U.S. Copyright Office.
 */

package com.ibm.openpages.ext.ui.service;

import com.ibm.openpages.ext.ui.bean.AuditPlanBenchmarkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;

public interface IAuditPlanBenchmarkHelperService {

	AuditPlanBenchmarkHelperAppInfo getHeaderAndLandingPageInfo(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo, String objectId) throws Exception;

    DataGridInfo getBenchmarkPlans(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo) throws Exception;

    DataGridInfo auditPlanConfirm(AuditPlanBenchmarkHelperAppInfo auditPlanBenchmarkHelperAppInfo) throws Exception;
}
