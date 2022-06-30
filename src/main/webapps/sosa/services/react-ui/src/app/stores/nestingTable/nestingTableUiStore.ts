import {makeAutoObservable} from 'mobx';
import {ITableRow} from './nestingTableDataStore';

export const defaultTableRow: ITableRow = {
    id: 'default',
    name: 'Default',
    type: 'Default',
    status: 'Default',
    scope: 'Default',
    link: '',
    hasChildren: false,
    isExpanded: false,
    isSelected: false,
    cells: [
        {
            id: 'default-cell-one',
            value: 'Default Cell'
        },
        {
            id: 'default-cell-two',
            value: 'Default Cell'
        },
        {
            id: 'default-cell-three',
            value: 'Default Cell'
        }
    ]
};

export class NestingTableUiStore {
    activeRow: ITableRow;
    activeRowIndex: number;
    checkedRows: string[];
    expandedRows: string[];
    parentRows: string[];

    constructor() {
        this.activeRow = defaultTableRow;
        this.activeRowIndex = 0;
        this.checkedRows = [];
        this.expandedRows = [];
        this.parentRows = [];
        makeAutoObservable(this);
    }

    get activeRowId() {
        return this.activeRow.id;
    }

    setActiveRow(row: ITableRow, index: number) {
        this.activeRow = row;
        this.activeRowIndex = index;
    }

    toggleChecked() {
        this.activeRow.isSelected = !this.activeRow.isSelected;
    }

    toggleExpanded() {
        this.activeRow.isExpanded = !this.activeRow.isExpanded;
    }
}
