package com.ibm.openpages.ext.ui.service;

import com.ibm.openpages.ext.ui.bean.CreateDSMTLinkForAudEntityInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkSearchInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;

import java.util.List;

/**
 * <p>
 * This class works as the Controller to branch the request coming into the AuditableEntity DSMT Link helper controller.
 * All requests are handled by their corresponding methods marked by the request mapping.
 * </P>
 *
 * @author : Praveen Ravi <BR>
 * email : raviprav@us.ibm.com <BR>
 * company : IBM OpenPages
 * @version : OpenPages 8.2.0
 * @custom.date : 02-15-2021
 * @custom.feature : Helper Service
 * @custom.category : Helper App
 */
public interface IAuditableEntityDSMTLinkService extends IDSMTLinkService {

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
     * @throws Exception - Generic Exception
     */
    DataGridInfo searchDSMT(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     * <p>
     * This method retrieves the child DSMT objects to display in the tree from the DSMT DB based on parent node
     * selected. The search information is present in the {@link DSMTLinkSearchInfo} in the given dsmtLinkHelperInfo.
     * The information present in the dsmtSearchInfo drives the query to get the search results back. Possible areas of
     * search are - Managed Segment - Managed Geography - Legal Vehicle The queries are constructed based on the Node
     * selected and the parent node id selected in the dsmtSearchInfo.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception - Generic Exception
     */
    DataGridInfo searchDSMTTree(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     * <p>
     * This method checks if each node in the selected DSMT triplet is within range. If any of the nodes are out of
     * range then the user will have to enter a rationale before he can request to create the DSMT Link. This will also
     * help us get the nodes that are out of range and updates the out of range field accordingly.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - a list of {@link CreateDSMTLinkForAudEntityInfo} that has the selected DSMT triplet information
     * @throws Exception - Generic Exception
     */
    List<CreateDSMTLinkForAudEntityInfo> processNodeRangeCheck(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception;

    /**
     * <p>
     * This method creates a DSMT Link with the selected DSMT's, and associates to the AuditableEntity updates the
     * fields in the created DSMT Link. The values of the fields are determined by the status of the AudtitableEntity.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - an instance of the DSMTLinkHelperAppInfo which has the information on creating new DSMT Links
     * @throws Exception - Generic Exception
     */
    DSMTLinkHelperAppInfo addDSMTLinksToAuditableEntity(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     * <p>
     * This method de'scopes the DSMT Link objects that were selected in the UI. Before de'scoping it checks for the
     * status of the AuditableEntity from which the helper was run. 1. If the Status is Draft - the method sets the
     * values of the following fields in the selected DSMT Link objects and save the DSMT Link object i. Set Scope Field
     * value to Out 2. If the Status is Active - the method checks for each DSMT Link selected to be de'scoped if there
     * are any Lower Level Dependencies. The Lower level dependencies are evaluated by checking if for the
     * AuditableEntity there are any child Audit, Control, Issue, CAP or AMR object who has child/children DSMT Links
     * whose DSMT ID matches the DSMT ID of the selected DSMT Link object to be de'scoped. If So the DSMT Link object is
     * stopped from de'scoping and a detailed message on all the grand child DSMT Links with matching DSMT ID's are
     * collected and displayed to the user.
     * <p>
     * If there are no Lower Level Dependencies then the method sets the values of the following fields in the selected
     * DSMT Link OBjects and save the DSMT Link object i. Set Scope Field value to Out
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return an instance of {@link DSMTLinkHelperAppInfo}
     * @throws Exception - Generic Exception
     */
    DSMTLinkHelperAppInfo descopeDSMTLinksFromAuditableEntity(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception;

}
