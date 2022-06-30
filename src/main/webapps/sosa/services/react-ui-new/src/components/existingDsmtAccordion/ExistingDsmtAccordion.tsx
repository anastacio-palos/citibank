import React from 'react';
import {Button} from 'carbon-components-react';
import {ITableProps} from '../../stores/nestingTable/nestingTableDataStore';
import {observer} from 'mobx-react-lite';
import {StoreTypes, useStore} from '../../RootStoreProvider';
import {ExistingDsmtAccordionUiStore} from '../../stores/existingDsmtAccordion/existingDsmtAccordionUiStore';
import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-enterprise';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine.css';

export const ExistingDsmtAccordion: React.FC<ITableProps> = observer((props: ITableProps) => {
    const uiStore = useStore(StoreTypes.ExistingDsmtAccordionUiStore) as ExistingDsmtAccordionUiStore;
    uiStore.loadData(props.rows, props.headers);

    return (
        <>
            <div
                style={{
                    height: '20rem',
                    width: '100%',
                    flex: '1 1 0px'
                }}
                className="ag-theme-alpine"
            >
                <AgGridReact
                    rowData={uiStore.rowData}
                    columnDefs={uiStore.columnDefs}
                    defaultColDef={uiStore.defaultColumnDef}
                    autoGroupColumnDef={uiStore.autoGroupColumnDef}
                    treeData={true}
                    animateRows={true}
                    // frameworkComponents={uiStore.frameworkComponents}
                    // groupDefaultExpanded={uiStore.groupDefaultExpanded}
                    getDataPath={uiStore.getDataPath}
                    onGridReady={uiStore.onGridReady}
                    rowSelection={"multiple"}
                    pagination={true}
                    paginationPageSize={10}
                    // context={uiStore.context}
                />
            </div>
            <ExistingDsmtLinksControls/>
        </>
    );
});

const ExistingDsmtLinksControls: React.FC = () => {
    const containerStyle = {
        marginTop: '1rem',
        display: 'flex',
        justifyContent: 'flex-end'
    };

    return (
        <div style={containerStyle}>
            <Button
                kind='primary'
                disabled={true}
                onClick={() => {
                }}
            >
                Submit
            </Button>
        </div>
    );
};
