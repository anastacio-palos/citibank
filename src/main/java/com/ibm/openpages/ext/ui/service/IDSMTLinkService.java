package com.ibm.openpages.ext.ui.service;

import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTLinkObjectAssociationInfo;
import com.ibm.openpages.ext.ui.bean.DSMTObjectGenericDetails;
import com.ibm.openpages.ext.ui.bean.DataGridInfo;

public interface IDSMTLinkService {

    /**
     * <P>
     * This method retrieves the Helpers Header Information from the Registry settings and sets the appropriate values
     * in the {@link } object. This will be used to display the content in the UI Header.
     * </P>
     *
     * @param dsmtLinkHelperInfo
     *            - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *            current session
     *
     * @return - the dsmtLinkHelperInfo with the helper header information set
     * @throws Exception
     */
    public DSMTLinkHelperAppInfo getHelperHeaderInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     * <P>
     * This method retrieves the Helper Landing Page Content Section. The Landing Page Title, Landing Page Content and
     * the fields and the values of the fields from the object from which the helper was launched are obtained. On
     * conditional basis pre existing associated object information is also retrieved. The Landing page information is
     * set in an instance of the {@link DSMTObjectGenericDetails}. The link object association information is set in an
     * instance of the {@link DSMTLinkObjectAssociationInfo}
     * </P>
     *
     * @param dsmtLinkHelperInfo
     *            - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *            current session
     *
     * @return - the dsmtLinkHelperInfo with the objects basic information and the association information set
     * @throws Exception
     */
    public DSMTLinkHelperAppInfo getLandingPageInfo(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;

    /**
     * <P>
     * This method retrieves a part of the Helper Landing Page Content Section. On conditional basis pre existing
     * associated object information is retrieved. The link object association information is set in an instance of the
     * {@link DataGridInfo}
     * </P>
     *
     * @param dsmtLinkHelperInfo
     *            - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *            current session
     *
     * @return - the dsmtLinkHelperInfo with the objects association information set
     * @throws Exception
     */
    public DataGridInfo getExistingDSMTLinks(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception;
}
