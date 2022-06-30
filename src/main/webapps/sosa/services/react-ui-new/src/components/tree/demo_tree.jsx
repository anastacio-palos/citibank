/* eslint-disable no-alert, no-console, react/no-find-dom-node */
import React from 'react';
import Tree, {TreeNode} from 'rc-tree';
import axios from 'axios';


class Demo extends React.Component {

    tempKeys = [];
    incomingData = [];

    constructor(props) {
        super(props);
        this.state = {
            // defaultExpandedKeys: keys,
            defaultSelectedKeys: [],
            defaultCheckedKeys: [],
            treeData: [], prop: props,
            isTreeData: false

        };


        this.renderNodeBinded = this.renderNode.bind(this)

        this.treeRef = React.createRef();
    }

    componentDidMount() {
        axios.get(this.props.service, this.config).then(response => {
            console.log("tree data : " + JSON.stringify(response.data))
            this.retrieveKeys(response.data.children)
            this.setState({
                treeData: response.data.children,
                prop: this.props,
                checkedKeys: this.tempKeys,
                defaultCheckedKeys: this.tempKeys,
                defaultSelectedKeys: this.tempKeys,
                isTreeData: true
            })
            console.log(this.state)
            this.incomingData = response.data;
        });

    }

    retrieveKeys(node) {

        node.map(n => {
            this.tempKeys.push(n.key);

            if (n.children && n.children.length > 0) {
                this.retrieveKeys(n.children)
            }

        })

    };


    onExpand = expandedKeys => {
        console.log('onExpand', expandedKeys);
    };

    onSelect = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
        console.log(info.node.leaf);
        this.selKey = info.node.props.eventKey;
    };

    unscope = [];
    onCheck = (checkedKeys, info) => {
        console.log('onCheck', checkedKeys);
        console.log(info);

        if (!info.checked) {

            console.log('key to delete', info.node.key)
            this.unscope.push(info.node.key)
            this.removeFromList(this.tempKeys, info.node.key)
            this.removeChildrenIteratievely(info.node.children, this.tempKeys)


        } else {
            let index = this.unscope.findIndex(akey => akey === info.node.key);

            if (index > -1) {
                this.unscope.splice(index, 1);
            }

            this.tempKeys.push(info.node.key)

            this.addChildrenRecursively(info.node.children, this.tempKeys)

            this.checkParent(info.node.pos)

        }
        console.log('-- unscope array --', this.unscope);

        console.log('new checked array', this.tempKeys)

        this.setState({
            treeData: this.incomingData.children,
            checkedKeys: this.tempKeys,
            defaultCheckedKeys: this.tempKeys,
            isTreeData: true
        })

        this.props.parentCallback(this.incomingData, this.unscope);

    };

    checkParent(index) {

        let indexes = index.split("-");

        let children = this.incomingData.children

        for (let count = 1; count < indexes.length - 1; count++) {
            let child = children[indexes[count]];
            let key = child.key;
            console.log('index', indexes[count], 'key', key)

            let index = this.unscope.findIndex(akey => akey === key);

            if (index > -1) {
                this.unscope.splice(index, 1);
            }

            index = this.tempKeys.findIndex(akey => akey === key);

            if (index < 0) {
                this.tempKeys.push(key);
            }


            children = child.children

        }


    }

    addChildrenRecursively(children, keys) {
        if (children && children.length > 0) {
            children.forEach(element => {
                console.log('processing child - ', element)
                this.removeFromList(this.unscope, element.key)
                keys.push(element.key)
                this.addChildrenRecursively(element.children, keys)
            });
        }

    }

    removeChildrenIteratievely(children, keys) {

        console.log('children - ', children)
        if (children && children.length > 0) {
            children.forEach(element => {
                console.log('processing child - ', element)
                this.unscope.push(element.key)
                this.removeFromList(this.tempKeys, element.key)
                this.removeChildrenIteratievely(element.children)
            });
        }
    }

    removeFromList(objectList, key) {
        let index = objectList.findIndex(akey => akey === key);
        if (index > -1) {
            objectList.splice(index, 1);
        }

    }

    onEdit = () => {
        setTimeout(() => {
            console.log('current key: ', this.selKey);
        }, 0);
    };

    onDel = e => {
        if (!window.confirm('sure to delete?')) {
            return;
        }
        e.stopPropagation();
    };

    setTreeRef = tree => {
        this.tree = tree;
    };

    renderNode(mainEntity) {

        let y = <TreeNode title="" key="0-0-0-0"></TreeNode>
        console.log(y);
        let nodeList = mainEntity.children
        console.log('creating tree nodes')
        if (nodeList && nodeList.length > 0) {
            let x = nodeList.map(n =>
                <TreeNode title={n.name} key={n.key}>
                    {/* {this.renderNode(n)} */}
                </TreeNode>
            )
            console.log(x);
            return x;
        }
        // return y;

    };

    customLabel = (n) => (
        <div>
            <span><a href={'../../../app/jspview/react/grc/task-view/' + n.key}>{n.name}</a></span>
            {/* <Tooltip></Tooltip> */}
            {/* <span>&nbsp; &nbsp; &nbsp; {this.getSTP(n)}</span> */}
        </div>
    );

    render() {


        return (
            <>
                {this.state.isTreeData &&
                <Tree
                    ref={this.setTreeRef}
                    className="myCls"
                    showLine
                    checkable
                    checkStrictly={true}
                    defaultExpandedKeys={this.state.defaultExpandedKeys}
                    onExpand={this.onExpand}
                    defaultSelectedKeys={this.state.defaultSelectedKeys}
                    checkedKeys={this.tempKeys}
                    defaultCheckedKeys={this.state.defaultCheckedKeys}
                    onSelect={this.onSelect}
                    onCheck={this.onCheck}
                    onActiveChange={key => console.log('Active:', key)}
                    treeData={this.state.treeData}
                    height="75vh"
                >
                    {/* {this.renderNode(this.state.treeData)} */}
                    {/* <TreeNode title="parent 1" key="0-0-0-0"></TreeNode> */}
                </Tree>
                }
            </>
        );
    }
}

export default Demo;