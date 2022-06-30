package com.ibm.openpages.ext.model;

import java.util.List;

public class AEOutOfScope {

    private String id;
    private OPObject aes;
    private List<String> rejections;

    public OPObject getAes() {

        return aes;
    }

    public void setAes(final OPObject aes) {

        this.aes = aes;
    }

    public String getId() {

        return id;
    }

    public void setId(final String id) {

        this.id = id;
    }

    public List<String> getRejections() {

        return rejections;
    }

    public void setRejections(final List<String> rejections) {

        this.rejections = rejections;
    }
}
