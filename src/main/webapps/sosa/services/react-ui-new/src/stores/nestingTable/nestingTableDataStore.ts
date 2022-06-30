import {makeAutoObservable} from "mobx";
import {MockApi} from '../../components/nestingTable/mockApi/mockApi';

export interface ITableHeader {
    key: string;
    header: string;
}

export interface ITableCells {
    id: string;
    value: string;
}

// this generic row interface enables the use of Object.keys() in a type-safe way
interface IRow<T, U, V, W> {
    [key: string]: T | U | V | W | undefined;
}

export interface ITableRow extends IRow<string, boolean, ITableCells[], ITableRow[]> {
    id: string;
    name: string;
    type: string;
    status: string;
    scope: string;
    hasChildren: boolean;
    link: string;
    disabled?: boolean;
    isExpanded: boolean;
    isSelected: boolean;
    expanded?: boolean;
    selected?: boolean;
    cells?: ITableCells[];
    children?: ITableRow[];
}

export interface ITableProps {
    rows: ITableRow[];
    headers: ITableHeader[];
    title?: string;
    isLoading?: boolean;
    getHeaderProps?: any;
    getRowProps?: any;
    getTableProps?: any;
    getSelectionProps?: any;
    selectedRows?: any;
}

export class NestingTableDataStore {
    api: MockApi;
    loading: boolean;
    headers: ITableHeader[];
    rows: ITableRow[];
    title: string;

    constructor() {
        this.api = MockApi.getInstance();
        this.loading = false;
        this.headers = [];
        this.rows = [];
        this.title = 'Nesting DataTable';
        makeAutoObservable(this);
        this.load();
    }

    private load() {
        this.loading = true;
        this.api.getTableData()
            .then(response => this.onLoad(response))
            // TODO: Implement proper error handling
            .catch(error => console.log(`ERROR ${error}`));
    }

    private onLoad(tableData: ITableProps) {
        this.loading = false;
        this.headers = tableData.headers;
        this.rows = tableData.rows;
    }

    getChildRows(parentRowIndex: number) {
        if (this.rows[parentRowIndex].children) {
            return this.rows[parentRowIndex].children;
        }

        return undefined;
    }

    updateChildRowCells(parentRowIndex: number, childRowIndex: number, cells: any[]) {
        const targetRow = this.rows[parentRowIndex];

        if (targetRow.children) {
            targetRow.children[childRowIndex].cells = cells;
        }
    }
}
