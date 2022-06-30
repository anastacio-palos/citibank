package com.ibm.openpages.ext.ui.bean;

public class AncestorControlsInfo {

    private String resourceId;
    private String controlName;
    private String controlObjLink;

    public String getResourceId() {

        return resourceId;
    }

    public void setResourceId(String resourceId) {

        this.resourceId = resourceId;
    }

    public String getControlName() {

        return controlName;
    }

    public void setControlName(String controlName) {

        this.controlName = controlName;
    }

    public String getControlObjLink() {

        return controlObjLink;
    }

    public void setControlObjLink(String controlObjLink) {

        this.controlObjLink = controlObjLink;
    }

    @Override
    public String toString() {

        return "AncestorControlsInfo{" + "resourceId='" + resourceId + '\'' + ", controlName='" + controlName + '\''
                + ", controlObjLink='" + controlObjLink + '}';
    }
}
