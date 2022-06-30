import {makeAutoObservable} from "mobx";
import {RootStore} from "../RootStore";
import {DataNode} from '../../api/nodesModel'
import {TableData} from "../../api/searchDsmtModel";

export interface Row {
    id: string;
    name: string;
    category: string;
    localLabel: string;
    isExpanded?: boolean;
    isSelected?: boolean;
    hasChildren?: boolean;
    checked?: boolean;
    cells?: any; // cell meta data for this row, keyed by header/column id
}

export interface Tab {
    id: string;
    label: string;
    disabled: boolean;
}


class SearchNodesStore {
    constructor(root: RootStore) {
        makeAutoObservable(this);
    }

    nodes: DataNode[] = [
        {
            name: 'Segment',
            id: `segment`
        }, {
            name: 'Geography',
            id: `geography`
        }, {
            name: 'Legal',
            id: `legal`
        }
    ];
    tabs: Tab[] = [
        {
            label: 'Select a Node1',
            id: 'tab1',
            disabled: false
        }, {
            label: 'Select a Node2',
            id: 'tab2',
            disabled: false
        }, {
            label: 'Select a Node3',
            id: 'tab3',
            disabled: false
        }
    ];

    selectedRow: Row = {
        id: '',
        name: '',
        category: '',
        localLabel: '',
        isExpanded: false,
        isSelected: false,
        hasChildren: false
    }

    tableData: TableData = {
        headers: [
            {
                key: 'name',
                header: 'Name'
            },
            {
                key: 'category',
                header: 'Category'
            },
            {
                key: 'localLabel',
                header: 'Localized Label'
            }
        ],
        rows: [
            {
                id: 'row-one',
                name: 'Name One',
                category: 'Category One',
                localLabel: 'Local Label One',
                isExpanded: false,
                isSelected: false,
                hasChildren: false
            },
            {
                id: 'row-two',
                name: 'Name Two',
                category: 'Category Two',
                localLabel: 'Local Label Two',
                isExpanded: false,
                isSelected: false,
                hasChildren: true
            },
            {
                id: 'row-three',
                name: 'Name Three',
                category: 'Category Three',
                localLabel: 'Local Label Three',
                isExpanded: false,
                isSelected: false,
                hasChildren: false
            }
        ]
    };


    selectedRowValue: string = '';
    selectedNode: string = '';

    getAllNodes = () => {
        return [...this.nodes];
    }

    getFilteredNodes = () => {
        console.log(this.nodes.filter((node) => node.id !== this.selectedNode));
        return this.nodes.filter((node) => node.id !== this.selectedNode);
    }

    updateSelectedRow = (row: Row) => {
        this.selectedRow = {
            id: row.id,
            name: row?.cells[0].value,
            category: row?.cells[1].value,
            localLabel: row?.cells[2].value
        };
        const rows = this.tableData.rows.map((row) => {
            row.isSelected = row.id === this.selectedRow.id;
            return row;
        });

        this.tableData = {headers: this.tableData.headers, rows: [...rows]}
        console.log(this.tableData);
        this.selectedRowValue = row?.cells[0].value;
    }

    removeSelectedRow = () => {
        this.selectedRowValue = '';
    }

    updateTabSelection = (tabIndex: number, node: string) => {
        this.selectedNode = node;
        this.tabs = this.tabs.map((tab, index) => {
            if (index === tabIndex) {
                tab.label = this.getNodeName(node) || '';
            }

            return tab;
        })
    }

    getNodeName(id: string) {
        const matchedNode = this.nodes.find((node) => node.id === id);
        return matchedNode?.name;
    }

}


export default SearchNodesStore;
