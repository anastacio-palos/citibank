import React, {Component} from 'react';
import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-enterprise';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine.css';
import axios from 'axios';
import {Button, InlineLoading, Toggle} from 'carbon-components-react';
import ToggleCellRenderer from './toggle_cell_renderer';
import ImageCellRenderer from './image_cell_renderer';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import aeDataService from '../../service/audit-entity-helper-service';

class CustomGrid extends Component {


    columnDefs = [
        {field: 'key', hide: 'true'},
        {
            field: '',
            cellRenderer: 'imageCellRenderer',
        },
        {field: 'title'},
        {field: 'aeObjRefID'},
        {field: 'description'},
        {field: 'source'},
        {
            field: 'scope',
            cellRenderer: 'toggleCellRenderer'
        },
        {
            field: 'ae',
            headerName: 'Auditable Entity'
        }
    ];


    constructor(props) {
        super(props);
        this.onSave = this.onSave.bind(this);
        this.state = {
            rowData: [],
            columnDefs: this.columnDefs,
            defaultColDef: {flex: 1},
            context: {componentParent: this},
            frameworkComponents: {
                toggleCellRenderer: ToggleCellRenderer,
                imageCellRenderer: ImageCellRenderer
            },
            autoGroupColumnDef: {
                headerName: 'Key',
                minWidth: 300,
                cellRendererParams: {suppressCount: true},
            },
            groupDefaultExpanded: -1,
            getDataPath: function (data) {
                return data.hierarchy;
            },
            saving: false
        };

    }


    onSave() {
        this.setState({saving: true});

        console.log(this.state.rowData);
        aeDataService.saveData(this.props.auditid, this.state.rowData).then(response => {
            console.log("---------- response from service" + JSON.stringify(response.data));
            NotificationManager.success('Data saved sucessfully', 'Succcess');
            this.setState({saving: false});

            this.loadData();

        }).catch(e => {
            NotificationManager.error('Error saving data, contact administrator', 'Error');
            this.setState({saving: false});
        });
    }


    loadData() {
        this.gridApi.showLoadingOverlay();
        axios.get(this.props.service, this.config).then(response => {
            console.log("row data : " + JSON.stringify(response.data));
            this.setState({
                rowData: response.data,
                columnDefs: this.columnDefs,
                context: {componentParent: this},
                defaultColDef: {flex: 1},
                autoGroupColumnDef: {
                    headerName: 'Key',
                    minWidth: 600,
                    cellRendererParams: {suppressCount: false},
                },
                groupDefaultExpanded: -1,
                getDataPath: function (data) {
                    return data.hierarchy;
                },
            });
            this.gridApi.hideOverlay();
            console.log('state', this.state);
        }).catch(e => {
            NotificationManager.error('Error loading data, contact administrator', 'Error');
        });

    }

    onGridReady = (params) => {
        this.gridApi = params.api;
        this.gridColumnApi = params.columnApi;
        this.gridApi.autoSizeColumns = true;

        this.loadData();
    };

    onFilterTextBoxChanged = () => {
        this.gridApi.setQuickFilter(
            document.getElementById('filter-text-box').value
        );
    };

    render() {
        return (
            <div style={{width: '100%', height: '80vh', marginTop: '10px'}}>
                <div className="example-wrapper">
                    <div style={{marginBottom: '10px'}}>
                        <input
                            type="text"
                            id="filter-text-box"
                            placeholder="Filter..."
                            onInput={() => this.onFilterTextBoxChanged()}
                        />
                    </div>
                    <div

                        style={{
                            height: '100%',
                            width: '100%',
                            flex: '1 1 0px'
                        }}
                        className="ag-theme-alpine"
                    >
                        <AgGridReact
                            rowData={this.state.rowData}
                            columnDefs={this.state.columnDefs}
                            defaultColDef={this.state.defaultColDef}
                            autoGroupColumnDef={this.state.autoGroupColumnDef}
                            treeData={true}
                            animateRows={true}
                            frameworkComponents={this.state.frameworkComponents}
                            // groupDefaultExpanded={this.state.groupDefaultExpanded}
                            getDataPath={this.state.getDataPath}
                            onGridReady={this.onGridReady}
                            context={this.state.context}
                        />
                    </div>
                </div>
                <div style={{bottom: '0', position: 'absolute', width: '98%', marginBottom: "10px"}}>
                    {/* <hr></hr> */}
                    <div style={{float: 'right', marginRight: '5px'}}>
                        {this.state.saving ?
                            <Button kind="primary" disabled="true"><InlineLoading></InlineLoading></Button>
                            :
                            <Button kind="primary" onClick={this.onSave}>Save</Button>
                        }
                    </div>
                </div>
                <NotificationContainer/>
            </div>
        );
    }
}

export default CustomGrid;
