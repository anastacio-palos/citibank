package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;

public class GRCObjectDetailsInfo implements Serializable {

    private static final long serialVersionUID = 69085487095837671L;

    private boolean isEditable;
    private boolean isRequired;

    private String link;
    private String fieldName;
    private String fieldValue;
    
    /**
     * @return the isEditable
     */
    public boolean isEditable() {
    
        return isEditable;
    }
    
    /**
     * @param isEditable the isEditable to set
     */
    public void setEditable(boolean isEditable) {
    
        this.isEditable = isEditable;
    }
    
    /**
     * @return the isRequired
     */
    public boolean isRequired() {
    
        return isRequired;
    }
    
    /**
     * @param isRequired the isRequired to set
     */
    public void setRequired(boolean isRequired) {
    
        this.isRequired = isRequired;
    }
    
    /**
     * @return the link
     */
    public String getLink() {
    
        return link;
    }
    
    /**
     * @param link the link to set
     */
    public void setLink(String link) {
    
        this.link = link;
    }
    
    /**
     * @return the fieldName
     */
    public String getFieldName() {
    
        return fieldName;
    }
    
    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
    
        this.fieldName = fieldName;
    }
    
    /**
     * @return the fieldValue
     */
    public String getFieldValue() {
    
        return fieldValue;
    }
    
    /**
     * @param fieldValue the fieldValue to set
     */
    public void setFieldValue(String fieldValue) {
    
        this.fieldValue = fieldValue;
    }

    @Override
    public String toString() {

        return "GRCObjectDetailsInfo [isEditable=" + isEditable + ", isRequired=" + isRequired + ", link=" + link
                + ", fieldName=" + fieldName + ", fieldValue=" + fieldValue + "]";
    }
}
