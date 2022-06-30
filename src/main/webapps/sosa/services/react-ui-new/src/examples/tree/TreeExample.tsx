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
import React from 'react';
import {toJS} from 'mobx';
import {observer} from "mobx-react-lite"
import Tree from 'rc-tree';
import {Key, EventDataNode, DataNode} from "rc-tree/lib/interface";
import {TreeExampleStore} from './TreeExampleStore';
import 'rc-tree/assets/index.css';
import './TreeExample.scss';

interface Props {
    treeExampleStore: TreeExampleStore | undefined;
}

function onCheck(treeExampleStore: TreeExampleStore, _checkedKeys: { checked: Key[]; halfChecked: Key[]; } | Key[], info: any) {
    const key = info.node.key;
    const checked = info.checked;
    treeExampleStore.checkNode(key, checked);
}

function onExpand(treeExampleStore: TreeExampleStore, _expandedKeys: Key[], info: {
    node: EventDataNode; expanded: boolean; nativeEvent: MouseEvent;
}) {
    const key = info.node.key as string;
    const checked = info.expanded;
    treeExampleStore.expandNode(key, checked);
}

function onSelect(treeExampleStore: TreeExampleStore, selectedKeys: Key[], info: {
    event: 'select'; selected: boolean; node: EventDataNode; selectedNodes: DataNode[]; nativeEvent: MouseEvent;
}) {
    const key = info.node.key as string;
    const selected = info.selected;
    treeExampleStore.selectNode(key, selected);
}

export const TreeExample = observer((props: Props) => {
    const {treeExampleStore} = props;

    if (!treeExampleStore) {
        return null;
    }

    const {treeData} = treeExampleStore
    if (!treeData) {
        return null;
    }


    // observable arrays must be converted to plain javascript
    // when passing to react components
    const checkedKeys = toJS(treeExampleStore.checkedKeys);
    const expandedKeys = toJS(treeExampleStore.expandedKeys);

    return (
        <div className="op-example-tree">
            <Tree
                checkable
                showIcon={false}
                checkStrictly
                treeData={treeData}
                checkedKeys={checkedKeys}
                expandedKeys={expandedKeys}
                onSelect={(checkedKeys, info) => onSelect(treeExampleStore, checkedKeys, info)}
                onCheck={(checkedKeys, info) => onCheck(treeExampleStore, checkedKeys, info)}
                onExpand={(expandedKeys, info) => onExpand(treeExampleStore, expandedKeys, info)}
            />
        </div>
    )
})
