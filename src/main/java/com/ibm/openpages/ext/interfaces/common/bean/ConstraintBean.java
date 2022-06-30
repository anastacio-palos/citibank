package com.ibm.openpages.ext.interfaces.common.bean;

/**
 * <p>
 * This bean represents a constraint within interface layout validation engine.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 11-05-2021
 */
public class ConstraintBean {
    private String constraintKey;
    private String constraintRegistryProperty;
    private String constraintType;
    private Object constraintData;

    public ConstraintBean() {
        super();
    }

    public String getConstraintKey() {
        return constraintKey;
    }

    public void setConstraintKey(String constraintKey) {
        this.constraintKey = constraintKey;
    }

    public String getConstraintRegistryProperty() {
        return constraintRegistryProperty;
    }

    public void setConstraintRegistryProperty(String constraintRegistryProperty) {
        this.constraintRegistryProperty = constraintRegistryProperty;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public Object getConstraintData() {
        return constraintData;
    }

    public void setConstraintData(Object constraintData) {
        this.constraintData = constraintData;
    }

    @Override
    public String toString() {
        return "ConstraintBean{" +
                "constraintKey='" + constraintKey + '\'' +
                ", constraintRegistryProperty='" + constraintRegistryProperty + '\'' +
                ", constraintType='" + constraintType + '\'' +
                ", constraintData=" + constraintData +
                '}';
    }
}
