package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;

/**
 * <P>
 * This class represents a single column in the AG Data Grid. The class contains information on
 *  1. If the column is hidden or not
 *  2. The name of the column that will be used by the AG Grid (field)
 *  3. The display name of the column that will be in the AG Grid.
 * </P>
 * 
 * @version : OpenPages 8.2.0
 * @author : Praveen Ravi <BR>
 *         email : raviprav@us.ibm.com <BR>
 *         company : IBM OpenPages
 * 
 * @custom.date : 02-15-2021
 * @custom.feature : Helper Bean Value object
 * @custom.category : Helper App
 */
public class DataGridHeaderColumnInfo implements Serializable {

    private static final long serialVersionUID = -8361129727737965804L;

    private boolean hide; // Flag to show or hide the column in the AG Grid
    private String field; // The name of the column to be used by the AG Grid internally
    private String headerName; // The name of the column for display

    /**
     * <P>
     * Return the boolean value to display the column in the AG Grid
     * </P>
     * 
     * @return the hide
     */
    public boolean isHide() {

        return hide;
    }

    /**
     * <P>
     * Set the boolean value to display the column in the AG Grid
     * </P>
     * 
     * @param hide
     *            the hide to set
     */
    public void setHide(boolean hide) {

        this.hide = hide;
    }

    /**
     * <P>
     * Return the the value of the column name that will be used by the AG Grid internally
     * </P>
     * 
     * @return the field
     */
    public String getField() {

        return field;
    }

    /**
     * <P>
     *  Set the the value of the column name that will be used by the AG Grid internally
     * </P>
     * 
     * @param field
     *            the field to set
     */
    public void setField(String field) {

        this.field = field;
    }

    /**
     * <P>
     *  Return the the value of the column name that will be used for display by the AG Grid internally
     * </P>
     * 
     * @return the headerName
     */
    public String getHeaderName() {

        return headerName;
    }

    /**
     * <P>
     *  Set the the value of the column name that will be used for display by the AG Grid internally
     * </P>
     * 
     * @param headerName
     *            the headerName to set
     */
    public void setHeaderName(String headerName) {

        this.headerName = headerName;
    }

    /**
     * <P>
     *  Overridden to String method to display the field values
     * </P>
     */
    @Override
    public String toString() {

        return "DataGridHeaderInfo [hide=" + hide + ", field=" + field + ", headerName=" + headerName + "]";
    }
}
