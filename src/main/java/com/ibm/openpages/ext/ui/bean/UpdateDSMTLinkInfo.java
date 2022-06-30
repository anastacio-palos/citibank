package com.ibm.openpages.ext.ui.bean;

import java.util.List;

public class UpdateDSMTLinkInfo {

    // The id of the Auditable Entity under which the DSMT's to update is present
    private String auditableEntityId;

    // Comma separated list of ID's of DSMT Link objects that will be updated to be scoped/ descoped
    private List<String> dsmtLinkIdsToUpdate;

    /**
     * @return the auditableEntityId
     */
    public String getAuditableEntityId() {

        return auditableEntityId;
    }

    /**
     * @param auditableEntityId
     *            the auditableEntityId to set
     */
    public void setAuditableEntityId(String auditableEntityId) {

        this.auditableEntityId = auditableEntityId;
    }

    /**
     * @return the dsmtLinkIdsToUpdate
     */
    public List<String> getDsmtLinkIdsToUpdate() {

        return dsmtLinkIdsToUpdate;
    }

    /**
     * @param dsmtLinkIdsToUpdate
     *            the dsmtLinkIdsToUpdate to set
     */
    public void setDsmtLinkIdsToUpdate(List<String> dsmtLinkIdsToUpdate) {

        this.dsmtLinkIdsToUpdate = dsmtLinkIdsToUpdate;
    }

    @Override
    public String toString() {

        return "DSMTLinkUpdateInfo [auditableEntityId=" + auditableEntityId + ", dsmtLinkIdsToUpdate="
                + dsmtLinkIdsToUpdate + "]";
    }
}
