import React, {ChangeEvent, useEffect, useState} from 'react';
import {Search20} from '@carbon/icons-react';
import {AccordionItem, Button, ButtonSet, Search, Tab, Tabs} from 'carbon-components-react';
import {observer} from 'mobx-react-lite';
import {StoreTypes, useStore} from '../../RootStoreProvider';
import {ITableProps} from '../../stores/nestingTable/nestingTableDataStore';
import {ExistingDsmtAccordion} from '../existingDsmtAccordion/ExistingDsmtAccordion';
import GeneralDetailsAccordion from '../generalDetailsAccordion/GeneralDetailsAccordion';
import CustomHeader from '../header/CustomHeader';
import AuditHelperDataStore from '../../stores/auditHelperDataStore';
import {AgGridReact} from 'ag-grid-react';
import {ExistingDsmtAccordionUiStore} from '../../stores/existingDsmtAccordion/existingDsmtAccordionUiStore';

const AuditHelper: React.FC = observer(() => {
    const dataStore = useStore(StoreTypes.AuditHelperDataStore) as AuditHelperDataStore;

    useEffect(() => {
        dataStore.loadInitData();
    }, []);

    const {headerInfo, generalDetails, existingDSMTLinksGridInfo, newDSMTLinksGridInfo} = dataStore;
    const accordionChildren = () => {
        return (
            <NewDsmtLinksAccordionItem
                headers={newDSMTLinksGridInfo.headers}
                rows={newDSMTLinksGridInfo.rows}
            />
        )
    }
    return (
        <>
            <CustomHeader headerInfo={headerInfo}/>
            <h2 style={{marginTop: "3rem", marginBottom: "2rem"}}>Audit</h2>
            {generalDetails && generalDetails.length > 1 &&
            <GeneralDetailsAccordion generalDetails={generalDetails} children={accordionChildren()}/>}
            <Tabs scrollIntoView={false} style={{display: 'flex', justifyContent: 'center'}}>
                <Tab
                    href="#"
                    id="tab-1"
                    label="Existing DSMT Links"
                >
                    <ExistingDsmtAccordion
                        headers={existingDSMTLinksGridInfo.headers}
                        rows={existingDSMTLinksGridInfo.rows}
                    />
                </Tab>
                <Tab
                    href="#"
                    id="tab-2"
                    label="Search"
                >
                    <AuditHelperSearch dataStore={dataStore}/>
                    <AuditHelperTable headers={dataStore.searchTableData.headers}
                                      rows={dataStore.searchTableData.rows}/>
                </Tab>
            </Tabs>
        </>
    );
});

const AuditHelperSearch = ({dataStore}: any) => {
    const [searchInput, setSearchInput] = useState<string>('');

    const handleSearchInput = (event: ChangeEvent<{ value: string; }>) => {
        const currentTarget = event.currentTarget;

        if (currentTarget) {
            setSearchInput(currentTarget.value);
        } else {
            setSearchInput('');
        }
    };

    const handleSearchButtonClick = (input: string) => {
        dataStore.onSearch(input);
    };

    return (
        <div className="bx--col-lg-8 bx--col-md-4 bx--col-sm-2 sample-spacer" style={{display: 'flex'}}>
            <Search
                id="audit-helper-search" labelText='Search' value={searchInput}
                onChange={(event) => handleSearchInput(event)}/>
            <Button kind="tertiary" iconDescription="Search" hasIconOnly onClick={() => {
            }}>
                <Search20 onClick={() => handleSearchButtonClick(searchInput)}/>
            </Button>
        </div>
    );
};

const AuditHelperTable = (props: any) => {
    return (
        <NewDsmtLinksTable
            headers={props.headers}
            rows={props.rows}/>
    );
}

const NewDsmtLinksAccordionItem = (props: ITableProps) => {
    return (
        <AccordionItem title="New DSMT Links">
            <NewDsmtLinksTable
                headers={props.headers}
                rows={props.rows}/>
            <NewDsmtLinksControls/>
        </AccordionItem>
    )
};

const NewDsmtLinksTable = (props: ITableProps) => {
    const uiStore = useStore(StoreTypes.ExistingDsmtAccordionUiStore) as ExistingDsmtAccordionUiStore;
    uiStore.loadData(props.rows, props.headers);

    return (
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
    );
};

const NewDsmtLinksControls = () => {
    const containerStyle = {
        width: '100%',
        marginTop: '1rem',
        display: 'flex',
        justifyContent: 'flex-end'
    };

    return (
        <div style={containerStyle}>
            <ButtonSet>
                <Button kind="secondary">
                    Ignore
                </Button>
                <Button kind="primary">
                    Accept
                </Button>
            </ButtonSet>
        </div>
    )
}

export default AuditHelper;
