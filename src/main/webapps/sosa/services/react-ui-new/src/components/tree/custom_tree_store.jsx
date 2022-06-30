import React, {Component} from 'react';
import {DataNode} from "rc-tree/lib/interface";
import {makeAutoObservable} from "mobx"


class CustomTreeStore {

    checkedKeys;
    expandedKeys;
    selectedKeys;
    treeData;

    constructor() {
        this.checkedKeys = [];
        this.expandedKeys = [];
        this.selectedKeys = [];
        this.treeData = [];
        makeAutoObservable(this);

    };

    checkNode(key, checked) {
        this.addRemoveArray(key, checked, this.checkedKeys);
        console.log(this.checkedKeys);
    }

    expandNode(key, expanded) {
        this.addRemoveArray(key, expanded, this.expandedKeys);
    }

    selectNode(key, selected) {
        // this.selectedKeys = [];
        if (selected) {
            this.selectedKeys.push(key);
        }
    }

    addRemoveArray(key, add, a) {
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

export default CustomTreeStore