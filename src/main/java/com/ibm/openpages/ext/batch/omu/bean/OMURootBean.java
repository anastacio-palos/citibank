package com.ibm.openpages.ext.batch.omu.bean;

import java.util.List;

public class OMURootBean {

    private List<CRCDataBean> data;
    private String message;
    private boolean success;

    public OMURootBean() {
    }

    public OMURootBean(List<CRCDataBean> data) {
        this.data = data;
    }

    public List<CRCDataBean> getData() {
        return data;
    }

    public void setData(List<CRCDataBean> data) {
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
        return "OMURootBean{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
