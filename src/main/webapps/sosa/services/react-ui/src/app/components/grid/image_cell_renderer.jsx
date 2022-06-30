import {Toggle} from 'carbon-components-react';
import React, {Component} from 'react';

export default class ImageCellRenderer extends Component {

    constructor(props) {
        super(props)

    }

    render() {
        return <div style={{textAlign: 'end'}}>
            {this.props.data.processed || (this.props.data.aeObjRefID && this.props.data.aeObjRefID != null) ?
                <svg style={{color: 'green'}} focusable="false" preserveAspectRatio="xMidYMid meet"
                     xmlns="http://www.w3.org/2000/svg" fill="currentColor" aria-label="Complete" width="20" height="20"
                     viewBox="0 0 32 32" role="img" class="op-task-view-guidance-panel-icon-complete">
                    <path d="M14 21.414L9 16.413 10.413 15 14 18.586 21.585 11 23 12.415 14 21.414z"></path>
                    <path
                        d="M16,2A14,14,0,1,0,30,16,14,14,0,0,0,16,2Zm0,26A12,12,0,1,1,28,16,12,12,0,0,1,16,28Z"></path>
                </svg>
                :
                <svg style={{color: 'red'}} focusable="false" preserveAspectRatio="xMidYMid meet"
                     xmlns="http://www.w3.org/2000/svg" fill="currentColor" aria-label="Validation error" width="20"
                     height="20" viewBox="0 0 32 32" role="img" class="op-task-view-guidance-panel-icon-invalid">
                    <path d="M16 22a1.5 1.5 0 101.5 1.5A1.5 1.5 0 0016 22zM15 11H17V20H15z"></path>
                    <path
                        d="M29,29H3a1,1,0,0,1-.89-1.46l13-25a1,1,0,0,1,1.78,0l13,25A1,1,0,0,1,29,29ZM4.65,27h22.7L16,5.17Z"></path>
                </svg>
            }
        </div>;
    }
}