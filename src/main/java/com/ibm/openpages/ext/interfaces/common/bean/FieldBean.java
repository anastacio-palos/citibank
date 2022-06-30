package com.ibm.openpages.ext.interfaces.common.bean;

/**
 * <p>
 * This bean represents a field within interface layout validation engine.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 10-07-2021
 */
public class FieldBean {
    private String name;
    private String type;
    private int minOccurs;
    private String maxOccurs;
    private String defaultValue;
    private RestrictionBean restriction;
    private String mapTo;
    private SourceBean source;

    public FieldBean() {
        this.type = "string";
        this.minOccurs = 0;
        this.maxOccurs = "1";
    }

    public FieldBean(String name, String type, int minOccurs, String maxOccurs, String defaultValue,
                     RestrictionBean restriction, String mapTo, SourceBean source) {
        this.name = name;
        this.type = type;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
        this.defaultValue = defaultValue;
        this.restriction = restriction;
        this.mapTo = mapTo;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }

    public String getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(String maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public RestrictionBean getRestriction() {
        return restriction;
    }

    public void setRestriction(RestrictionBean restriction) {
        this.restriction = restriction;
    }

    public String getMapTo() {
        return mapTo;
    }

    public void setMapTo(String mapTo) {
        this.mapTo = mapTo;
    }

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "FieldBean{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", minOccurs=" + minOccurs +
                ", maxOccurs='" + maxOccurs + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", restriction=" + restriction +
                ", mapTo='" + mapTo + '\'' +
                ", source=" + source +
                '}';
    }
}
