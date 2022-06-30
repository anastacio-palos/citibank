package com.ibm.openpages.ext.batch.omu.bean;

public class EmployeeKPIBean {

    private int srNo;
    private String objectType;
    private String source;
    private int totalProcessed;
    private int failed;

    public EmployeeKPIBean() {
        this.totalProcessed = 0;
        this.failed = 0;
        this.srNo = 0;
    }

    public EmployeeKPIBean(int srNo, String objectType, String source, int totalProcessed, int failed) {
        this.srNo = srNo;
        this.objectType = objectType;
        this.source = source;
        this.totalProcessed = totalProcessed;
        this.failed = failed;
    }

    public void addProcessed() {
        this.totalProcessed++;
    }

    public void addFailed() {
        this.failed++;
    }

    public int getSrNo() {
        return srNo;
    }

    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    @Override
    public String toString() {
        return "EmployeeEmailBean{" +
                "srNo=" + srNo +
                ", objectType='" + objectType + '\'' +
                ", source='" + source + '\'' +
                ", totalProcessed=" + totalProcessed +
                ", failed=" + failed +
                '}';
    }
}
