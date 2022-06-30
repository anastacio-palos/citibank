package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;

public class ExistingDSMTLinkBaseInfo implements Serializable {

    private static final long serialVersionUID = -5242334701066212439L;
    private boolean expanded;
    private boolean selected;
    private boolean disabled;
    private boolean hasChildren;

    private String id;

    /**
     * @return the expanded
     */
    public boolean isExpanded() {

        return expanded;
    }

    /**
     * @param expanded
     *            the expanded to set
     */
    public void setExpanded(boolean expanded) {

        this.expanded = expanded;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {

        return selected;
    }

    /**
     * @param selected
     *            the selected to set
     */
    public void setSelected(boolean selected) {

        this.selected = selected;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {

        return disabled;
    }

    /**
     * @param disabled
     *            the disabled to set
     */
    public void setDisabled(boolean disabled) {

        this.disabled = disabled;
    }

    /**
     * @return the hasChildren
     */
    public boolean isHasChildren() {

        return hasChildren;
    }

    /**
     * @param hasChildren
     *            the hasChildren to set
     */
    public void setHasChildren(boolean hasChildren) {

        this.hasChildren = hasChildren;
    }

    /**
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {

        this.id = id;
    }

    @Override
    public String toString() {

        return "ExistingDSMTLinkBaseInfo [expanded=" + expanded + ", selected=" + selected + ", disabled=" + disabled
                + ", hasChildren=" + hasChildren + ", id=" + id + "]";
    }
}
