package com.ibm.openpages.ext.interfaces.icaps.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "AIMSDetails")
public class AIMSDetailsInfo {

    public AIMSDetailsInfo() {
        this.retIssueValidationInfo = new RETIssueValidationsInfo();
        this.commValidations = new CommonValidations();
        this.listOfCaps = new ArrayList<>();
    }

    @XmlElement(name="RETIssueValidations")
    private RETIssueValidationsInfo retIssueValidationInfo;
 
    @XmlElement(name="CommonValidations")
    private CommonValidations commValidations;

    @XmlElement(name="CapForm")
    private List<CapForm> listOfCaps;

    
    public RETIssueValidationsInfo getRetIssueValidationInfo() {
    
        return retIssueValidationInfo;
    }

    
    public void setRetIssueValidationInfo(RETIssueValidationsInfo retIssueValidationInfo) {
    
        this.retIssueValidationInfo = retIssueValidationInfo;
    }

    
    public CommonValidations getCommValidations() {
    
        return commValidations;
    }

    
    public void setCommValidations(CommonValidations commValidations) {
    
        this.commValidations = commValidations;
    }

    public List<CapForm> getListOfCaps() {
        return listOfCaps;
    }

    public void setListOfCaps(List<CapForm> listOfCaps) {
        this.listOfCaps = listOfCaps;
    }

    @Override
    public String toString() {
        return "AIMSDetailsInfo{" +
                "retIssueValidationInfo=" + retIssueValidationInfo +
                ", commValidations=" + commValidations +
                ", listOfCaps=" + listOfCaps +
                '}';
    }
}


