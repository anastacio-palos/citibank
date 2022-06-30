export interface Header {
    key: string;
    header: string;
}

export interface Row {
    id: string;
    name: string;
    parentId: string;
    nodeId: string;
    level: string;
    region: string;
    isExpanded?: boolean;
    isSelected?: boolean;
    hasChildren?: boolean;
    cells?: any; // cell meta data for this row, keyed by header/column id
}

export interface TableData {
    headers: Header[];
    rows: Row[];
}

export interface PaginationData {
    page: number;
    pageSize: number;
    totalItems?: number,
    pageSizes?: readonly number[]
}

export interface SearchPayload {
    topLevel: boolean,
    nodeSelected: string,
    searchText: string,
    manageSegNodeId: string,
    legalVehicleNodeId: string,
    manageGeographyNodeId: string
}

export interface DsmtSubmitPayload {
    dsmtCountry: string;
    rational: string;
    dsmtRegion: string;
    homeOrImpacted: string;
    legalVehicleId: string;
    legalVehicleName: string;
    managedGeogprahyName: string;
    managedGeographyId: string;
    managedGeographyLevel: string;
    managedGeoOutOfRange: boolean;
    managedSegmentId: string;
    managedSegmentName: string;
    managedSegOutOfRange: boolean;
}

export interface DataNode {
    id: string;
    name: string;
    selected: boolean;
}

export interface Tab {
    id: string;
    label: string;
    disabled: boolean;
    selectedRowId: string;
    selectedRowValue: string;
    selectedRowValues?: string[];
    tabValue?: string;
    nodeValues?: DataNode[];
    tableSelectionType: string;
    searchResults?: TableData;
}