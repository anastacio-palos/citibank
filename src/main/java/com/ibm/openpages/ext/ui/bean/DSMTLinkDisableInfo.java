package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import java.util.Set;

/**
 * <P>
 * This class represents the DSMT Link disable information. A DSMT link can be disabled for various reasons. If the
 * reason is a lower level dependency then a link to the objects that has the lower level dependency is set. If its
 * something else like if the status is not null or empty then we set that information in the disable Info list.
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
public class DSMTLinkDisableInfo implements Serializable {

    private static final long serialVersionUID = -3673263660958921717L;

    private Set<String> disableInfoList; // A List of disable info messages
    private Set<String> lowerLevelDepObjectsList; // A list of Object URL's pointing to the Lower Level dependency of a
                                                  // DSMT link object

    /**
     * <P>
     * Returns a list of lower level dependency DSMT Link object URLs for the current object
     * </P>
     * 
     * @return the lowerLevelDepObjectsList
     */
    public Set<String> getLowerLevelDepObjectsList() {

        return lowerLevelDepObjectsList;
    }

    /**
     * <P>
     * Set a list of lower level dependency DSMT Link object URLs for the current object
     * </P>
     * 
     * @param lowerLevelDepObjectsList
     *            the lowerLevelDepObjectsList to set
     */
    public void setLowerLevelDepObjectsList(Set<String> lowerLevelDepObjectsList) {

        this.lowerLevelDepObjectsList = lowerLevelDepObjectsList;
    }

    /**
     * <P>
     * Returns a list of reasons to disable the DSMT Link for the current object
     * </P>
     * 
     * @return the disableInfoList
     */
    public Set<String> getDisableInfoList() {

        return disableInfoList;
    }

    /**
     * <P>
     * Sets a list of reasons to disable the DSMT Link for the current object
     * </P>
     * 
     * @param disableInfoList
     *            the disableInfoList to set
     */
    public void setDisableInfoList(Set<String> disableInfoList) {

        this.disableInfoList = disableInfoList;
    }

    /**
     * <P>
     * Overridden to String method to display the Data Grid information
     * </P>
     */
    @Override
    public String toString() {

        return "DSMTLinkDisableInfo [lowerLevelDepObjectsList=" + lowerLevelDepObjectsList + ", disableInfoList="
                + disableInfoList + "]";
    }
}
