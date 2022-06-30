package com.ibm.openpages.ext.ui.service;

import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkSearchInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;

import java.util.List;

public interface IIssueDSMTLinkService extends IDSMTLinkService {

    /**
     * <p>
     * This method retrieves the DSMT objects from the DSMT DB based on the search text. The search information is
     * present in the {@link DSMTLinkSearchInfo} in the given dsmtLinkHelperInfo. The information present in the
     * dsmtSearchInfo drives the query to get the search results back. Possible areas of search are - Managed Segment -
     * Managed Geography - Legal Vehicle The queries are constructed based on the Node selected and the search text in
     * the dsmtSearchInfo.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception a generic exception
     */
    DataGridInfo getAvailableDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     * <p>
     * This method retrieves the DSMT objects from the DSMT DB based on the search text. The search information is
     * present in the {@link DSMTLinkSearchInfo} in the given dsmtLinkHelperInfo. The information present in the
     * dsmtSearchInfo drives the query to get the search results back. Possible areas of search are - Managed Segment -
     * Managed Geography - Legal Vehicle The queries are constructed based on the Node selected and the search text in
     * the dsmtSearchInfo.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception
     */
    DataGridInfo searchDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     * <p>
     * This method creates a DSMT Link with the selected DSMT's, and associates to the AuditableEntity updates the
     * fields in the created DSMT Link. The values of the fields are determined by the status of the AudtitableEntity.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return
     * @throws Exception
     */
    DSMTLinkHelperAppInfo addDSMTLinksToIssue(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     *
     * @param dsmtLinkHelperAppInfo
     * @return
     * @throws Exception
     */
    DSMTLinkHelperAppInfo descopeDSMTLinksFromIssue(DSMTLinkHelperAppInfo dsmtLinkHelperAppInfo) throws Exception;

}
