/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * © Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.bean;

import java.util.List;

public class OMUDsmtRoot {

    private List<Data> data;
    private String message;
    private boolean success;

    public List<Data> getData() {

        return data;
    }

    public void setData(List<Data> data) {

        this.data = data;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public boolean isSuccess() {

        return success;
    }

    public void setSuccess(boolean success) {

        this.success = success;
    }

    @Override
    public String toString() {

        return "OMUDsmtRoot [data=" + data + ", message=" + message + ", success=" + success + "]";
    }

}
