package com.ibm.openpages.ext.ui.service.impl;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.helpers.service.IHelperService;
import com.ibm.openpages.ext.tss.service.*;
import com.ibm.openpages.ext.ui.bean.DSMTLinkHelperAppInfo;
import com.ibm.openpages.ext.ui.bean.DSMTObjectGenericDetails;
import com.ibm.openpages.ext.ui.bean.GRCObjectDetailsInfo;
import com.ibm.openpages.ext.ui.util.DSMTLinkHelperUtil;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.ui.constant.DSMTLinkHelperConstants.*;

@Component
public class DSMTLinkBaseServiceImpl {


    protected Log logger;

    @Autowired
    IFieldUtil fieldUtil;

    @Autowired
    ILoggerUtil loggerUtil;

    @Autowired
    IHelperService helperService;

    @Autowired
    IGRCObjectUtil grcObjectUtil;

    @Autowired
    IApplicationUtil applicationUtil;

    @Autowired
    IHelperService commonHelperService;

    @Autowired
    DSMTLinkHelperUtil dsmtLinkHelperUtil;

    @Autowired
    IGRCObjectSearchUtil grcObjectSearchUtil;

    @Autowired
    DSMTLinkServiceUtilImpl dsmtLinkServiceUtil;


    /**
     * <p>
     * This method retrieves the Helper Landing Page Content Section. The Landing Page Title, Landing Page Content and
     * the fields and the values of the fields from the object from which the helper was launched are obtained. The
     * Landing page information is set in an instance of the {@link DSMTObjectGenericDetails}.
     * </P>
     *
     * @param dsmtLinkHelperInfo - an instance of the {@link DSMTLinkHelperAppInfo} a master bean that has all the details for the
     *                           current session
     * @throws Exception - Generic Exception
     */
    public void getBasicObjectInfoForDisplay(DSMTLinkHelperAppInfo dsmtLinkHelperInfo) throws Exception {

        logger.info("getBasicObjectInfoForDisplay() Start");

        // Method Level Variables.
        String contentBody;
        String contentHeader;
        String registrySetting;
        String grcObjFieldRegValue;

        IGRCObject grcObject;
        List<GRCObjectDetailsInfo> objFieldsInfo;
        DSMTObjectGenericDetails helperObjectContentInfo;

        /* Initialize Variables */
        helperObjectContentInfo = new DSMTObjectGenericDetails();

        /*
         * Prepare the registry setting path based on the base registry setting and the object type and obtain the list
         * of fields that needs to be displayed in the UI from the registry setting.
         */
        registrySetting = dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LINK_APP_OBJECT_DISPLAY_INFO;
        grcObjFieldRegValue = applicationUtil.getRegistrySetting(registrySetting);

        /* Obtain the object under execution, Get the fields and its values that needs to be displayed in the UI */
        logger.info("objectId : " + dsmtLinkHelperInfo.getObjectID());
        grcObject = grcObjectUtil.getObjectFromId(dsmtLinkHelperInfo.getObjectID());
        objFieldsInfo = dsmtLinkHelperUtil.getBasicObjectInformationForDisplay(grcObject, grcObjFieldRegValue);

        /* Get the Helper page content header and body to be displayed in the UI */
        contentHeader = applicationUtil.getRegistrySetting(
                dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LINK_APP_TITLE, EMPTY_STRING);
        contentBody = applicationUtil.getRegistrySetting(
                dsmtLinkHelperInfo.getObjRegistrySetting() + DSMT_LINK_APP_DISPLAY_NAME, EMPTY_STRING);

        /* Set all the obtained values in the bean to be returned */
        helperObjectContentInfo.setContentBody(contentBody);
        helperObjectContentInfo.setContentHeader(contentHeader);
        helperObjectContentInfo.setGeneralDetails(objFieldsInfo);

        /* Log information for debugging */
        logger.info("Helper Object Content Info: " + helperObjectContentInfo);

        dsmtLinkHelperInfo.setHelperObjectContentInfo(helperObjectContentInfo);
        logger.info("getBasicObjectInfoForDisplay() End");
    }
}
