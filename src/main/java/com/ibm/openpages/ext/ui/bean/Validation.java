package com.ibm.openpages.ext.ui.bean;

public class Validation {

    private boolean invalidOMUExec;
    private boolean inactiveOMU;
    private boolean omuUpdate;
    private boolean omuExecUpdate;
    private boolean scorecardHRUpdate;
    private boolean inactiveUser;
    private boolean inactiveActionOwner;
    private boolean actionOwnerHRUpdate;

    public Validation() {
        this.invalidOMUExec = false;
        this.inactiveOMU = false;
        this.omuUpdate = false;
        this.omuExecUpdate = false;
        this.scorecardHRUpdate = false;
        this.inactiveUser = false;
        this.inactiveActionOwner = false;
        this.actionOwnerHRUpdate = false;
    }

    public boolean isInvalidOMUExec() {
    
        return invalidOMUExec;
    }
    
    public void setInvalidOMUExec(boolean invalidOMUExec) {
    
        this.invalidOMUExec = invalidOMUExec;
    }
    
    public boolean isInactiveOMU() {
    
        return inactiveOMU;
    }
    
    public void setInactiveOMU(boolean inactiveOMU) {
    
        this.inactiveOMU = inactiveOMU;
    }
    
    public boolean isOmuUpdate() {
    
        return omuUpdate;
    }
    
    public void setOmuUpdate(boolean omuUpdate) {
    
        this.omuUpdate = omuUpdate;
    }
    
    public boolean isOmuExecUpdate() {
    
        return omuExecUpdate;
    }
    
    public void setOmuExecUpdate(boolean omuExecUpdate) {
    
        this.omuExecUpdate = omuExecUpdate;
    }
    
    public boolean isScorecardHRUpdate() {
    
        return scorecardHRUpdate;
    }
    
    public void setScorecardHRUpdate(boolean scorecardHRUpdate) {
    
        this.scorecardHRUpdate = scorecardHRUpdate;
    }
    
    public boolean isInactiveUser() {
    
        return inactiveUser;
    }
    
    public void setInactiveUser(boolean inactiveUser) {
    
        this.inactiveUser = inactiveUser;
    }
    
    public boolean isInactiveActionOwner() {
    
        return inactiveActionOwner;
    }
    
    public void setInactiveActionOwner(boolean inactiveActionOwner) {
    
        this.inactiveActionOwner = inactiveActionOwner;
    }
    
    public boolean isActionOwnerHRUpdate() {
    
        return actionOwnerHRUpdate;
    }
    
    public void setActionOwnerHRUpdate(boolean actionOwnerHRUpdate) {
    
        this.actionOwnerHRUpdate = actionOwnerHRUpdate;
    }

    @Override
    public String toString() {

        return "Validation [invalidOMUExec=" + invalidOMUExec + ", inactiveOMU=" + inactiveOMU + ", omuUpdate="
                + omuUpdate + ", omuExecUpdate=" + omuExecUpdate + ", scorecardHRUpdate=" + scorecardHRUpdate
                + ", inactiveUser=" + inactiveUser + ", inactiveActionOwner=" + inactiveActionOwner
                + ", actionOwnerHRUpdate=" + actionOwnerHRUpdate + "]";
    }



}
