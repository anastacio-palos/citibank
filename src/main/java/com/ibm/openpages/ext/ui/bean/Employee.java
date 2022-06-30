package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;

public class Employee implements Serializable {

    private static final long serialVersionUID = 4510188131188545656L;

    private String soeId;
    private String firstName;
    private String lastName;
    private String gocId;
    private String emailId;

    public String getSoeId() {

        return soeId;
    }

    public void setSoeId(String soeId) {

        this.soeId = soeId;
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

    public String getGocId() {

        return gocId;
    }

    public void setGocId(String gocId) {

        this.gocId = gocId;
    }

    public String getEmailId() {

        return emailId;
    }

    public void setEmailId(String emailId) {

        this.emailId = emailId;
    }

    @Override
    public String toString() {

        return "Employee [soeId=" + soeId + ", firstName=" + firstName + ", lastName=" + lastName + ", gocId=" + gocId
                + ", emailId=" + emailId + "]";
    }

}
