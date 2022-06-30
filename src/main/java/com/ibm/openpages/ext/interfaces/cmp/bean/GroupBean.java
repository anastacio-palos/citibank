package com.ibm.openpages.ext.interfaces.cmp.bean;

import com.ibm.openpages.api.security.IGroup;

public class GroupBean {
    private String groupName;
    private String id;

    public GroupBean() {
        super();
    }

    public GroupBean(String id, String groupName) {
        super();
        this.id = id;
        this.groupName = groupName;
    }

    public GroupBean(IGroup iGroup) {
        this.groupName = iGroup.getName();
        this.id = iGroup.getId().toString();
    }

    public String getGroupName() {
        return groupName;
    }


    public String getId() {
        return id;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GroupBean{" +
                "groupName='" + groupName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }



}
