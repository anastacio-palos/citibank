import {DsmtSubmitPayload, DataNode, Tab} from "../../../api/models/searchDsmtModel";

export const defaultTableData = {
    rows: [],
    headers: []
};

export const defaultPaginationData = {
    page: 1,
    pageSize: 10,
    totalItems: 0,
    pageSizes: [10]
}


export const defaultSearchPayload = {
    topLevel: false,
    nodeSelected: '',
    searchText: '',
    manageSegNodeId: '',
    legalVehicleNodeId: '',
    manageGeographyNodeId: ''
}

export const defaultDsmtSubmitPayload: DsmtSubmitPayload[] = [{
    dsmtCountry: '',
    rational: '',
    dsmtRegion: '',
    homeOrImpacted: '',
    legalVehicleId: '',
    legalVehicleName: '',
    managedGeogprahyName: '',
    managedGeographyId: '',
    managedGeographyLevel: '',
    managedGeoOutOfRange: false,
    managedSegmentId: '',
    managedSegmentName: '',
    managedSegOutOfRange: false
}]

export const defaultNodes: DataNode[] = [
    {
        name: 'Manage Segment',
        id: `MS`,
        selected: false
    }, {
        name: 'Managed Geography',
        id: `MG`,
        selected: false
    }, {
        name: 'Legal Vehicle',
        id: `LV`,
        selected: false
    }
];

export const defaultTabsMap: Map<string, Tab> = new Map<string, Tab>()

defaultTabsMap.set('tab1', {
    label: 'Select a Node',
    id: 'tab1',
    disabled: false,
    tabValue: '',
    selectedRowId: '',
    selectedRowValue: '',
    tableSelectionType: 'radio',
    nodeValues: [...defaultNodes],
    searchResults: defaultTableData
})
defaultTabsMap.set('tab2', {
    label: 'Select a Node',
    id: 'tab2',
    disabled: true,
    selectedRowId: '',
    selectedRowValue: '',
    tabValue: '',
    tableSelectionType: 'radio',
    nodeValues: [...defaultNodes],
    searchResults: defaultTableData
})
defaultTabsMap.set('tab3', {
    label: 'Select a Node',
    id: 'tab3',
    disabled: true,
    selectedRowId: '',
    selectedRowValue: '',
    selectedRowValues: [''],
    tabValue: '',
    tableSelectionType: 'checkbox',
    nodeValues: [...defaultNodes],
    searchResults: defaultTableData
})

//   const displaySearchResults = () => {
//     switch(dsmtTabsStore.currentTab){
//         case 1: return <div className="sample-spacer">
//         {dsmtTabsStore.selectedRowValue !== '' ?
//         <Tag type="gray" filter title={dsmtTabsStore.selectedRowValue} onClose={handleCloseTag}>
//             {dsmtTabsStore.selectedRowValue}
//         </Tag> : dsmtTabsStore.selectedRowValue}
//         <DsmtTable tableData={getTableData()} 
//         currentTab={props.currentTab}
//         pagination={dsmtTabsStore.paginationData} 
//         tableSelectionType={props.tableSelectionType}/>
//     </div>;
//         case 2: return <div className="sample-spacer">
//             {dsmtTabsStore.selectedRowValue !== '' ?
//             <Tag type="gray" filter title={dsmtTabsStore.selectedRowValue} onClose={handleCloseTag}>
//                 {dsmtTabsStore.selectedRowValue}
//             </Tag> : dsmtTabsStore.selectedRowValue}
//             <DsmtTable tableData={getTableData()} 
//             currentTab={props.currentTab}
//             pagination={dsmtTabsStore.paginationData} 
//             tableSelectionType={props.tableSelectionType}/>
//         </div>;
//         case 3: return <div className="sample-spacer">
//             {dsmtTabsStore.selectedRowValue !== '' ?
//             <Tag type="gray" filter title={dsmtTabsStore.selectedRowValue} onClose={handleCloseTag}>
//                 {dsmtTabsStore.selectedRowValue}
//             </Tag> : dsmtTabsStore.selectedRowValue}
//             <DsmtTable tableData={getTableData()} 
//             currentTab={props.currentTab}
//             pagination={dsmtTabsStore.paginationData} 
//             tableSelectionType={props.tableSelectionType}/>
//         </div>;
//     }
// }