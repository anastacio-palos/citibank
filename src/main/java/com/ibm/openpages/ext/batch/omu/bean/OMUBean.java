package com.ibm.openpages.ext.batch.omu.bean;

public class OMUBean {
    //[Citi_DSMT_Link].[Citi-DSMT:IsOMU], [Citi_DSMT_Link].[Citi-DSMT:IsOMU], [Citi_DSMT_Link].[Citi-DL:Active], [Citi_DSMT_Link].[Citi-DL:Scp],
    // [Citi_DSMT_Link].[Citi-DSMT:OMUExec], [Citi_DSMT_Link].[Citi-DSMT:MSID], [Citi_DSMT_Link].[Citi-DSMT:MGID]
    private String id;
    private String name;
    private String isOmu;
    private String active;
    private String scp;
    private String omuExec;
    private String msId;
    private String mgId;
    private String type;

    public OMUBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsOmu() {
        return isOmu;
    }

    public void setIsOmu(String isOmu) {
        this.isOmu = isOmu;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getScp() {
        return scp;
    }

    public void setScp(String scp) {
        this.scp = scp;
    }

    public String getOmuExec() {
        return omuExec;
    }

    public void setOmuExec(String omuExec) {
        this.omuExec = omuExec;
    }

    public String getMsId() {
        return msId;
    }

    public void setMsId(String msId) {
        this.msId = msId;
    }

    public String getMgId() {
        return mgId;
    }

    public void setMgId(String mgId) {
        this.mgId = mgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OMUBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isOmu='" + isOmu + '\'' +
                ", active='" + active + '\'' +
                ", scp='" + scp + '\'' +
                ", omuExec='" + omuExec + '\'' +
                ", msId='" + msId + '\'' +
                ", mgId='" + mgId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
