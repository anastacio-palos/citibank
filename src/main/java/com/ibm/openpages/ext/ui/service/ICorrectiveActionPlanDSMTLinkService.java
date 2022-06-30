package com.ibm.openpages.ext.ui.service;

import java.util.List;

import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkObjectInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;

public interface ICorrectiveActionPlanDSMTLinkService extends IDSMTLinkService {

    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    public DataGridInfo getAvailableDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    public List<DSMTLinkObjectInfo> descopeDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    public List<DSMTLinkObjectInfo> associateDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    public List<DSMTLinkObjectInfo> rescopeDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

}
