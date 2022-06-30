import React, {Component} from 'react';
import Tree, {TreeNode} from 'rc-tree';
import {toJS} from 'mobx';
import aeDataService from '../../service/audit-entity-helper-service';
import './custom_tree.scss';
import './custom_tree_store';
import CustomTreeStore from './custom_tree_store';
import {observer} from "mobx-react-lite"
import {Key, EventDataNode, DataNode} from "rc-tree/lib/interface";
import 'rc-tree/assets/index.css';
import axios from 'axios';
import auditEntityHelperService from '../../service/audit-entity-helper-service';
import {Tooltip} from 'carbon-components-react/lib/components/Tooltip/Tooltip';

class CustomTree extends Component {

    checkedKeys;
    expandedKeys;
    customTreeStore;

    customLabel = (n) => (
        <div>
            <span>{n.name}</span>
            <Tooltip></Tooltip>
            <span>&nbsp; &nbsp; &nbsp; {this.getSTP(n)}</span>
        </div>
    );

    getSTP(n) {
        if (n.stp) {
            return <input></input>
        }
        return;
    }

    sendData = (checkedKeys) => {
        this.props.parentCallback(checkedKeys);
    }

    constructor(props) {
        super(props)
        this.customTreeStore = new CustomTreeStore();
        this.checkedKeys = this.customTreeStore.checkedKeys
        this.expandedKeys = this.customTreeStore.expandedKeys

        this.state = {
            treeData: [], prop: props
        }

        this.treeRef = React.createRef();
        this.renderNodeBinded = this.renderNode.bind(this)

        console.log(props.service)
        axios.get(props.service, this.config).then(response => {
            console.log("tree data : " + JSON.stringify(response.data))
            this.setState({treeData: response.data, prop: props})
            this.customTreeStore.treeData = response.data;
        });

    }

    // onCheck(customTreeStore , _checkedKeys, info) {
    //     console.log("oncheck");
    //     const key = info.node.key;
    //     const checked = info.checked;
    //     customTreeStore.checkNode(key, checked);
    //     this.sendData(this.customTreeStore.checkedKeys);
    // }

    onExpand(customTreeStore, _expandedKeys, info) {
        console.log("onExpand");
        const key = info.node.key;
        const checked = info.expanded;
        customTreeStore.expandNode(key, checked);
    }

    // onSelect(customTreeStore, selectedKeys, info) {
    //     console.log("onSelect");
    //     const key = info.node.key;
    //     const selected = info.selected;
    //     customTreeStore.selectNode(key, selected);
    // }

    renderNode(nodeList) {
        console.log('creating tree nodes')
        if (!nodeList || nodeList.length == 0) {
            return;
        }
        let x = nodeList.map(n =>
            <TreeNode title={this.customLabel(n)} key={n.key}>
                {this.renderNode(n.children)}
            </TreeNode>
        )
        console.log(x);
        return x;


    }


    onSelect = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
        this.selKey = info.node.props.eventKey;
    };

    onCheck = (checkedKeys, info) => {
        console.log('onCheck', checkedKeys, info);
    };


    render() {

        return (<div className="custom-tree">
            {/* if(this.state.treeData) */}
            {/* <Tree
                className="myCls"
                ref={this.setTreeRef}
				checkable={true}
				showIcon={false}
                checkStrictly={false}
                showLine={true}
                multipleSelect={true}
                showPartiallySelected={true}
                // treeData={this.state.treeData}
			    // checkedKeys={this.checkedKeys}
				// expandedKeys={this.expandedKeys}
				// onSelect={(checkedKeys, info) => this.onSelect(this.customTreeStore, this.checkedKeys, info)}
                // onCheck={(checkedKeys, info) => this.onCheck(this.customTreeStore, this.checkedKeys, info)}
                
                // onExpand={(expandedKeys, info) => this.onExpand(this.customTreeStore, this.expandedKeys, info)}
                height={850}
			>
               {this.renderNode(this.state.treeData)}
        
                
            </Tree> */}

            <Tree
                className="myCls"
                showLine
                checkable
                selectable={false}
                defaultExpandAll
                onExpand={this.onExpand}
                defaultSelectedKeys={this.state.defaultSelectedKeys}
                defaultCheckedKeys={this.state.defaultCheckedKeys}
                onSelect={this.onSelect}
                onCheck={this.onCheck}
                height='75%'
            >
                {this.renderNode(this.state.treeData)}
            </Tree>
        </div>);
    }


}

export default CustomTree;