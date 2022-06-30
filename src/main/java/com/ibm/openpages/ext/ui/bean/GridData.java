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
public class GridData implements Serializable {

    private static final long serialVersionUID = 9035485443540895775L;

    private String warningMessage;
    private String inputMessage;
    private String firstName;
    private String lastName;
    private String soeId;
    private List<?> rows;
    private List<OMUDsmtLinkDisplayData> rowData;

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

    public String getInputMessage() {

        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {

        this.inputMessage = inputMessage;
    }

    
    public List<OMUDsmtLinkDisplayData> getRowData() {
    
        return rowData;
    }

    public void setRowData(List<OMUDsmtLinkDisplayData> rowData) {
    
        this.rowData = rowData;
    }

    public String getFirstName() {
    
        return firstName;
    }

    public void setFirstName(String firstName) {
    
        this.firstName = firstName;
    }

    public String getLastName() {
    
        return lastName;
    }

    
    public void setLastName(String lastName) {
    
        this.lastName = lastName;
    }

    
    public String getSoeId() {
    
        return soeId;
    }

    
    public void setSoeId(String soeId) {
    
        this.soeId = soeId;
    }

    @Override
    public String toString() {

        return "GridData [warningMessage=" + warningMessage + ", inputMessage=" + inputMessage + ", firstName="
                + firstName + ", lastName=" + lastName + ", soeId=" + soeId + ", rows=" + rows + ", rowData=" + rowData
                + "]";
    }


}
