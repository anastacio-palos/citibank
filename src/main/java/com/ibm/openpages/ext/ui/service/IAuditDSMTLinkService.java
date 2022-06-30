/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * � Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.service;

import java.util.List;

import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkObjectInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;

public interface IAuditDSMTLinkService extends IDSMTLinkService {

    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception;

    public DataGridInfo getAvailableObjectInformation(DSMTLinkHelperAppInfo dsmtLinkHelperInfo, String aeSearch)
            throws Exception;

    public List<DSMTLinkObjectInfo> ignoreNewDSMTLinksFromAudit(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception;

    public List<DSMTLinkObjectInfo> descopeDSMTLinksFromAudit(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception;

    public List<DSMTLinkObjectInfo> associateDSMTLinksToAudit(DSMTLinkHelperAppInfo dsmtLinkHelperInfo,
            boolean isNewDSMTLinkFlag) throws Exception;

    public List<DSMTLinkObjectInfo> rescopeDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     * <P>
     * This method return the new DSMT Links available for association to the current object grouped by the Auditable
     * Entity ID present in the DSMT link object.
     * </P>
     * 
     * @param dsmtLinkHelperInfo
     * @return
     * @throws Exception
     */
    public DataGridInfo getNewDSMTLinkObject(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

}
