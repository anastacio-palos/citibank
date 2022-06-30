package com.ibm.openpages.ext.ui.bean;

import java.io.Serializable;
import java.util.List;

public class GRCObjectInfo implements Serializable {

    private static final long serialVersionUID = 6409253457879122267L;

    private String id;
    private String objectId;
    private String objectName;
    private String objectLink;
    private String objectType;
    private String parentResourceId;
    private boolean hasChildren;
    private List<String> hierarchy;
    private List<GRCObjectInfo> children;

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getObjectId()
    {
        return objectId;
    }
    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }
    public String getObjectName()
    {
        return objectName;
    }
    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }
    public String getObjectLink()
    {
        return objectLink;
    }
    public void setObjectLink(String objectLink)
    {
        this.objectLink = objectLink;
    }
    public String getObjectType()
    {
        return objectType;
    }
    public void setObjectType(String objectType)
    {
        this.objectType = objectType;
    }
    public String getParentResourceId()
    {
        return parentResourceId;
    }
    public void setParentResourceId(String parentResourceId)
    {
        this.parentResourceId = parentResourceId;
    }
    public boolean isHasChildren()
    {
        return hasChildren;
    }
    public void setHasChildren(boolean hasChildren)
    {
        this.hasChildren = hasChildren;
    }
    public List<String> getHierarchy()
    {
        return hierarchy;
    }
    public void setHierarchy(List<String> hierarchy)
    {
        this.hierarchy = hierarchy;
    }
    public List<GRCObjectInfo> getChildren()
    {
        return children;
    }
    public void setChildren(List<GRCObjectInfo> children)
    {
        this.children = children;
    }

    @Override
    public boolean equals(Object obj) {
        return ((GRCObjectInfo) obj).objectId.equals(this.objectId);
    }
    @Override
    public String toString()
    {
        return "GRCObjectInfo [id=" + id + ", objectId=" + objectId + ", objectName=" + objectName + ", objectLink=" + objectLink
                + ", objectType=" + objectType + ", parentResourceId=" + parentResourceId + ", hasChildren=" + hasChildren + ", hierarchy="
                + hierarchy + ", children=" + children + "]";
    }


}
