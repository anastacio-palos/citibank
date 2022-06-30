/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * © Copyright IBM Corporation 2021
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Data {

    private Node node;

    @JsonIgnore
    private List<ChangeHistory> changeHistory;

    public Node getNode() {

        return node;
    }

    public void setNode(Node node) {

        this.node = node;
    }

    public List<ChangeHistory> getChangeHistory() {

        return changeHistory;
    }

    public void setChangeHistory(List<ChangeHistory> changeHistory) {

        this.changeHistory = changeHistory;
    }

    @Override
    public String toString() {

        return "Data [node=" + node + ", changeHistory=" + changeHistory + "]";
    }

}
