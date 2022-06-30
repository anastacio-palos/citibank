package com.ibm.openpages.ext.batch.omu.bean;

import java.util.List;

public class CRCDataBean {

    private CRCNodeBean node;
    private List<CRCChangeHistoryBean> changeHistory;

    public CRCDataBean() {
    }

    public CRCDataBean(CRCNodeBean crcNodeBean, List<CRCChangeHistoryBean> crcChangeHistory) {
        this.node = crcNodeBean;
        this.changeHistory = crcChangeHistory;
    }

    public CRCNodeBean getNode() {
        return node;
    }

    public void setNode(CRCNodeBean node) {
        this.node = node;
    }

    public List<CRCChangeHistoryBean> getChangeHistory() {
        return changeHistory;
    }

    public void setChangeHistory(List<CRCChangeHistoryBean> changeHistory) {
        this.changeHistory = changeHistory;
    }

    @Override
    public String toString() {
        return "CRCDataBean{" +
                "crcNodeBean=" + node +
                ", crcChangeHistory=" + changeHistory +
                '}';
    }
}
