export interface Header {
    key: string;
    header: string;
}

export interface Row {
    id: string;
    name: string;
    category: string;
    localLabel: string;
    isExpanded: boolean;
    isSelected: boolean;
    hasChildren: boolean;
    checked?: boolean;
    cells?: any; // cell meta data for this row, keyed by header/column id
}

export interface TableData {
    headers: Header[];
    rows: Row[];
}
