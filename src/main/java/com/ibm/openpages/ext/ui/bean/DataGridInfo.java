package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <P>
 * This class represents a AG Data Grid. The bean has the Grid header information and rows to display
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
public class DataGridInfo implements Serializable {

    private static final long serialVersionUID = 9035485443540895665L;

    private String warningMessage;
    private List<DataGridHeaderColumnInfo> headers; // A list of Header columns in the AG Grid
    private List<?> rows; // A list of rows to be displayed in the AG Grid

    /**
     * <P>
     * Get the List of {@link DataGridHeaderColumnInfo} representing each column in the AG Data Grid
     * </P>
     * 
     * @return the headers
     */
    public List<DataGridHeaderColumnInfo> getHeaders() {

        return headers;
    }

    /**
     * <P>
     * Set the List of {@link DataGridHeaderColumnInfo} representing each column in the AG Data Grid
     * </P>
     * 
     * @param headers
     *            the headers to set
     */
    public void setHeaders(List<DataGridHeaderColumnInfo> headers) {

        this.headers = headers;
    }

    /**
     * <P>
     * Get a List of custom representation of the rows. The rows bean should match the header column representing each
     * column in the AG Data Grid.
     * </P>
     * 
     * @return the rows
     */
    public List<?> getRows() {

        return rows;
    }

    /**
     * <P>
     * Set a List of custom representation of the rows. The rows bean should match the header column representing each
     * column in the AG Data Grid.
     * </P>
     * 
     * @param rows
     *            the rows to set
     */
    public void setRows(List<?> rows) {

        this.rows = rows;
    }

    public String getWarningMessage() {

        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {

        this.warningMessage = warningMessage;
    }

    /**
     * <P>
     *  Overridden to String method to display the Data Grid information
     * </P>
     */
    @Override
    public String toString() {

        return "DataGridInfo [headers=" + headers + ", rows=" + rows + ", warningMessage=" + warningMessage + "]";
    }
}
