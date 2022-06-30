package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import java.util.List;

public class DSMTObjectGenericDetails implements Serializable {

    private static final long serialVersionUID = 6409253457879122267L;

    private String contentBody;
    private String contentHeader;

    private List<GRCObjectDetailsInfo> generalDetails;

    /**
     * @return the contentBody
     */
    public String getContentBody() {

        return contentBody;
    }

    /**
     * @param contentBody
     *            the contentBody to set
     */
    public void setContentBody(String contentBody) {

        this.contentBody = contentBody;
    }

    /**
     * @return the contentHeader
     */
    public String getContentHeader() {

        return contentHeader;
    }

    /**
     * @param contentHeader
     *            the contentHeader to set
     */
    public void setContentHeader(String contentHeader) {

        this.contentHeader = contentHeader;
    }

    /**
     * @return the generalDetails
     */
    public List<GRCObjectDetailsInfo> getGeneralDetails() {

        return generalDetails;
    }

    /**
     * @param generalDetails
     *            the generalDetails to set
     */
    public void setGeneralDetails(List<GRCObjectDetailsInfo> generalDetails) {

        this.generalDetails = generalDetails;
    }

    @Override
    public String toString() {

        return "DSMTObjectGenericDetails [contentBody=" + contentBody + ", contentHeader=" + contentHeader
                + ", generalDetails=" + generalDetails + "]";
    }
}
