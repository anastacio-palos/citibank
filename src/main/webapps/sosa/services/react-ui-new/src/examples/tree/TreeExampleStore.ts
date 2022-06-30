/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * OpenPages GRC Platform (PID: 5725-D51)
 *
 * © Copyright IBM Corporation  2021 - CURRENT_YEAR. All Rights Reserved.
 *
 * US Government Users Restricted Rights- Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 *******************************************************************************/

import {makeAutoObservable} from "mobx"
import {DataNode} from "rc-tree/lib/interface";

const treeExampleJson: DataNode = require('./TreeExampleData.json');

export class TreeExampleStore {

    checkedKeys: string[]; // ids of the nodes that are checked
    expandedKeys: string[]; // ids of the nodes that are expanded
    selectedKeys: string[]; // ids of the nodes that are selected
    treeData: DataNode[];

    constructor() {
        this.treeData = treeExampleJson as any;
        this.checkedKeys = [];
        this.expandedKeys = []
        this.selectedKeys = []
        makeAutoObservable(this);
    }

    checkNode(key: string, checked: boolean) {
        TreeExampleStore.addRemoveArray(key, checked, this.checkedKeys);
    }

    expandNode(key: string, expanded: boolean) {
        TreeExampleStore.addRemoveArray(key, expanded, this.expandedKeys);
    }

    selectNode(key: string, selected: boolean) {
        this.selectedKeys = [];
        if (selected) {
            this.selectedKeys.push(key);
        }
    }

    private static addRemoveArray(key: string, add: boolean, a: string[]) {
        if (add) {
            if (!a.includes(key)) {
                a.push(key);
            }
        } else {
            const index = a.findIndex(akey => akey === key);
            if (index > -1) {
                a.splice(index, 1);
            }
        }
    }
}