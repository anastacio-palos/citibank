/* eslint-disable no-alert, no-console, react/no-find-dom-node */
import React from 'react';
import Tree, {TreeNode} from 'rc-tree';
import axios from 'axios';
import CustomTreeStore from './custom_tree_store';
import {Tooltip} from 'carbon-components-react/lib/components/Tooltip/Tooltip';


class CustomTreeFolder extends React.Component {

    static defaultProps = {
        keys: ['pid1'],
    };

    checked = true;

    constructor(props) {
        super(props);
        const {keys} = props;
        console.log(keys)
        this.state = {
            defaultExpandedKeys: keys,
            defaultSelectedKeys: keys,
            defaultCheckedKeys: keys,
        };
        this.customTreeStore = new CustomTreeStore();
        this.checkedKeys = this.customTreeStore.checkedKeys
        this.expandedKeys = this.customTreeStore.expandedKeys

        this.state = {
            treeData: [], prop: props
        }

        this.treeRef = React.createRef();
        this.renderNodeBinded = this.renderNode.bind(this)

        console.log(props.service)
    }

    componentDidMount() {
        axios.get(this.props.service, this.config).then(response => {
            console.log("tree data : " + JSON.stringify(response.data))
            this.setState({treeData: response.data.children, prop: this.props})
            this.customTreeStore.treeData = response.data.children;
        });
    }


    onCheck(customTreeStore, _checkedKeys, info) {
        console.log("---------------");

        const key = info.node.key;
        const checked = info.checked;
        customTreeStore.checkNode(key, checked);
        console.log(this.customTreeStore.checkedKeys);
        this.sendData(this.props.key, this.customTreeStore.checkedKeys);
    }

    onExpand(customTreeStore, _expandedKeys, info) {
        console.log("onExpand");
        const key = info.node.key;
        const checked = info.expanded;
        customTreeStore.expandNode(key, checked);
    }

    onSelect(customTreeStore, selectedKeys, info) {
        console.log("onSelect");
        const key = info.node.key;
        const selected = info.selected;
        customTreeStore.selectNode(key, selected);
    }

    sendData = (key, checkedKeys) => {
        this.props.parentCallback(key, checkedKeys);
    }

    // onExpand = expandedKeys => {
    //   console.log('onExpand', expandedKeys);
    // };

    // onSelect = (selectedKeys, info) => {
    //   console.log('selected', selectedKeys, info);
    //   this.selKey = info.node.props.eventKey;
    // };

    // onCheck = (checkedKeys, info) => {
    //   console.log('onCheck', checkedKeys, info);
    // };

    // onEdit = () => {
    //   setTimeout(() => {
    //     console.log('current key: ', this.selKey);
    //   }, 0);
    // };

    // onDel = e => {
    //   if (!window.confirm('sure to delete?')) {
    //     return;
    //   }
    //   e.stopPropagation();
    // };

    setTreeRef = tree => {
        this.tree = tree;
    };

    renderNode(mainEntity) {
        let nodeList = mainEntity.children
        // console.log('creating tree nodes')
        if (!nodeList || nodeList.length == 0) {
            return;
        }
        let x = nodeList.map(n =>
            <TreeNode title={n.name} key={n.key} checked>
                {this.renderNode(n)}
            </TreeNode>
        )
        return x;

    }

    customLabel = (n) => (
        <div>
            <span><a href={'../../../app/jspview/react/grc/task-view/' + n.key}>{n.name}</a></span>
            <Tooltip></Tooltip>
            {/* <span>&nbsp; &nbsp; &nbsp; {this.getSTP(n)}</span> */}
        </div>
    );

    getSTP(n) {
        if (n.stp) {
            return <input></input>
        }
        return;
    }


    render() {
        const customLabel = (
            <span className="cus-label">
        <span>operations: </span>
        <span style={{color: 'blue'}} onClick={this.onEdit}>
          Edit
        </span>
                &nbsp;
                <label onClick={e => e.stopPropagation()}>
          <input type="checkbox"/> checked
        </label>
                &nbsp;
                <span style={{color: '#EB0000'}} onClick={this.onDel}>
          Delete
        </span>
      </span>
        );

        return (
            <div style={{margin: '0 20px'}}>
                {this.state.treeData && this.state.treeData.children &&
                <Tree
                    ref={this.setTreeRef}
                    className="myCls"
                    showLine
                    checkable={true}
                    selectable={false}
                    defaultExpandAll
                    // checkStrictly={false}
                    onExpand={this.onExpand}
                    defaultSelectedKeys={this.state.defaultSelectedKeys}
                    defaultCheckedKeys={this.state.defaultCheckedKeys}
                    // checkedKeys={this.state.defaultCheckedKeys}
                    onSelect={(checkedKeys, info) => this.onSelect(this.customTreeStore, this.checkedKeys, info)}
                    onCheck={(checkedKeys, info) => this.onCheck(this.customTreeStore, this.checkedKeys, info)}
                    onExpand={(expandedKeys, info) => this.onExpand(this.customTreeStore, this.expandedKeys, info)}
                    height="75vh"
                >
                </Tree>
                }


            </div>
        );
    }
}

export default CustomTreeFolder;