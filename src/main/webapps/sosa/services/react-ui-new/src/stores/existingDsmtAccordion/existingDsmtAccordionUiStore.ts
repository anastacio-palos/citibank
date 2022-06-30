import {makeAutoObservable} from 'mobx';

// import { NotificationContainer, NotificationManager } from 'react-notifications';

export class ExistingDsmtAccordionUiStore {
    rowData: [];
    columnDefs: object[];
    defaultColumnDef: object;
    autoGroupColumnDef: object;
    groupDefaultExpanded: number;
    getDataPath: (data: any) => any;
    saving: boolean;
    gridApi: any;
    gridColumnApi: any;

    constructor() {
        this.rowData = [];
        this.columnDefs = [];
        this.defaultColumnDef = {flex: 1};
        this.autoGroupColumnDef = {
            headerName: 'Object ID',
            minWidth: 300,
            checkboxSelection: true,
            headerCheckboxSelection: true,
            cellRendererParams: {suppressCount: true}
        };
        this.groupDefaultExpanded = -1;
        this.getDataPath = (data) => {
            return data.hierarchy;
        };
        this.saving = false;
        makeAutoObservable(this);
    }

    loadData(rowData: any, headers: any) {
        // this.gridApi.showLoadingOverlay();
        console.log("row data : " + JSON.stringify(rowData));
        this.rowData = rowData;
        this.columnDefs = headers;
        // this.context = { componentParent: this };
        // this.autoGroupColumnDef = {
        //     headerName: 'Key',
        //     minWidth: 600,
        //     cellRendererParams: { suppressCount: false },
        // };
        // this.gridApi.hideOverlay();

    }

    onGridReady = (params: any) => {
        this.gridApi = params.api;
        this.gridColumnApi = params.columnApi;
        this.gridApi.autoSizeColumns = true;
        // this.loadData(params.rowData);
    };


}
