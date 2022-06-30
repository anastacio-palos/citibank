import {RowNode} from 'ag-grid-community';
import {Toggle} from 'carbon-components-react';
import React, {Component} from 'react';

export default class ToggleCellRenderer extends Component {

    constructor(props) {
        super(props)
        this.state = {checkedValue: props.value}
        console.log('toggle props', props)
    }

    scopeChangeArray = [];
    onChange = (a) => {
        this.scopeChangeArray = [];
        console.log('on chage', this.props, a.target.checked);
        let scopeValue = 'In'
        if (a.target.checked) {
            this.setState({checkedValue: 'In'})
            // this.props.value='In'
            this.props.node.setDataValue('scope', 'In')
            this.traverseParent(this.props.node.parent, scopeValue);

        } else {
            scopeValue = 'Out'
            this.setState({checkedValue: 'Out'})
            this.props.node.setDataValue('scope', 'Out')


        }
        this.traverseChildren(this.props.node, scopeValue);


        console.log('scope change', this.props);

    }

    refresh(params) {
        // set value into cell again
        console.log('in the refresh renderer', params);
        this.setState({checkedValue: params.data.scope})
        // this.props.node.setDataValue('scope',params.data.scope)

        // return true to tell the grid we refreshed successfully
        return true;
    }

    traverseParent(rowNode, scopeValue) {

        if (rowNode.parent) {
            this.traverseParent(rowNode.parent, scopeValue)
        }
        if (rowNode.rowIndex != null) {

            rowNode.setDataValue('scope', scopeValue);
            console.log(rowNode)

            this.scopeChangeArray.push(rowNode);
            console.log(this.scopeChangeArray)
        }
    }

    traverseChildren(rowNode, scopeValue) {
        rowNode.allLeafChildren.forEach(a => a.setDataValue('scope', scopeValue))
    }

    render() {
        return <div>
            <Toggle
                id={this.props.data.key}
                labelA='Out'
                labelB='In'
                onChange={this.onChange}
                toggled={this.state.checkedValue == 'In'}
                className='toggleClass'>
            </Toggle>
        </div>;
    }
}