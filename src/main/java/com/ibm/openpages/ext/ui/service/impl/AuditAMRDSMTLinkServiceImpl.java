package com.ibm.openpages.ext.ui.service.impl;

import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkObjectInfo;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;
import com.ibm.openpages.ext.ui.service.IAuditMappingReportDSMTLinkService;
import org.springframework.stereotype.Service;

//TODO format the code, add the licensing at the top.
@Service("auditAMRDSMTLinkServiceImpl")
public class AuditAMRDSMTLinkServiceImpl extends AuditMappingResourceBaseServiceImpl implements
        IAuditMappingReportDSMTLinkService {

    /**
     * <p>
     * This method retrieves a part of the Helper Landing Page Content Section. On conditional basis pre existing
     * associated object information is retrieved. The link object association information is set in an instance of the
     * {@link DataGridInfo}
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @return - the dsmtLinkHelperInfo with the objects association information set
     * @throws Exception
     */
    @Override
    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        return null;
    }

    /**
     * <p>
     * This method retrieves the DSMT objects from the DSMT DB based on the search text. The search information is
     * present in the {@link DataGridInfo} in the given dsmtLinkHelperInfo. The information present in the
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
    @Override
    public DataGridInfo getAvailableDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        return null;
    }

    /**
     * <p>
     * This method retrieves the DSMT objects from the DSMT DB based on the search text. The search information is
     * present in the {@link DataGridInfo} in the given dsmtLinkHelperInfo. The information present in the
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
    @Override
    public DataGridInfo searchDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        return null;
    }

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
    @Override
    public DSMTLinkHelperAppInfo addDSMTLinksToAMR(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        return null;
    }

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
     * @return a list of {@link DSMTLinkObjectInfo}
     * @throws Exception
     */
    @Override
    public DSMTLinkHelperAppInfo descopeDSMTLinksFromAMR(DSMTLinkHelperAppInfo dsmtLinkHelperInfo)
            throws Exception {

        return null;
    }

	@Override
	public DataGridInfo getAncestorIssues(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DSMTLinkHelperAppInfo processScopeIssuesFromAMR(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
