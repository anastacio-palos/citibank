package com.ibm.openpages.ext.common.util;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.core.Logger;


public class EmailDetailsBean {
    Logger logger;
    String subjectAppText;
    String bodyAppText;
    String sourceAddress;
    List<String> destinationAddresses;
    String opObjectId;
    String opObjectName;
    Locale locale;

    public EmailDetailsBean(String subjectAppText, String bodyAppText, String sourceAddress,
            List<String> destinationAddresses, String opObjectId, String opObjectName, Locale locale) {
        this.subjectAppText = subjectAppText;
        this.bodyAppText = bodyAppText;
        this.sourceAddress = sourceAddress;
        this.destinationAddresses = destinationAddresses;
        this.opObjectId = opObjectId;
        this.opObjectName = opObjectName;
        this.locale = locale;
    }

    public String getBodyAppText() {
        return bodyAppText;
    }

    public void setBodyAppText(String bodyAppText) {
        this.bodyAppText = bodyAppText;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public String getOpObjectId() {
        return opObjectId;
    }

    public void setOpObjectId(String opObjectId) {
        this.opObjectId = opObjectId;
    }

    public String getOpObjectName() {
        return opObjectName;
    }

    public void setOpResourceName(String opResourceName) {
        this.opObjectName = opResourceName;
    }

    public String getSubjectAppText() {
        return subjectAppText;
    }

    public void setSubjectAppText(String subjectAppText) {
        this.subjectAppText = subjectAppText;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "subjectAppText: " + subjectAppText + ", \nbodyAppText: " + bodyAppText + ", \nsourceAddress: "
                + sourceAddress + ", \ndestinationAddresses: " + destinationAddresses + ", \nopObjectId: " + opObjectId
                + ", \nopObjectName: " + opObjectName + ", \nlocale: " + locale;
    }
}
