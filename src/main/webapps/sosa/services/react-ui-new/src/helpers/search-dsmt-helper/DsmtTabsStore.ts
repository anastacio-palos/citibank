import {makeAutoObservable, observable, runInAction} from "mobx";
import {RootStore} from "../../RootStore";
import {
    Row,
    SearchPayload,
    TableData,
    DataNode,
    PaginationData,
    Tab,
    DsmtSubmitPayload
} from "../../api/models/searchDsmtModel";
import {
    defaultDsmtSubmitPayload,
    defaultNodes,
    defaultPaginationData,
    defaultSearchPayload,
    defaultTableData,
    defaultTabsMap
} from "./constants/SearchConstants";
import {thisTypeAnnotation, throwStatement} from "@babel/types";

class DsmtTabsStore {
    root: RootStore;
    tabsMap = observable(defaultTabsMap);
    currentTab: number = 1;
    searchPayload = defaultSearchPayload;
    dsmtSubmitPayload: DsmtSubmitPayload[] = defaultDsmtSubmitPayload;
    tableData: TableData = defaultTableData;
    selectedRowId: string = ''
    selectedRowValue: string = '';
    selectedNode: string = '';
    selectedTabIndex: number = 0;
    tableDataFromStore: TableData = defaultTableData;
    paginationData: PaginationData = defaultPaginationData;
    nodes: DataNode[] = defaultNodes;

    constructor(root: RootStore) {
        this.root = root;
        makeAutoObservable(this);
    }

    getAllNodes = () => {
        return [...this.nodes];
    }

    updateTabSelection = (tabIndex: number, selectedNode: string) => {
        this.selectedNode = selectedNode;
        const selectedNodeValue = this.getNodeName(selectedNode);
        this.selectedTabIndex = tabIndex;
        const values = this.tabsMap.get(`tab${this.currentTab}`) as Tab;
        this.tabsMap.set(`tab${this.currentTab}`, {...values, label: selectedNodeValue})
        console.log(this.tabsMap.toJSON());
    }

    updateNextTabData = (nextTabIndex: number) => {
        this.currentTab = this.currentTab + 1;
        const currentTabValues = this.tabsMap.get(`tab${this.currentTab}`) as Tab;
        // console.log(this.currentTab);
        currentTabValues.disabled = false;
        const filteredNodes = currentTabValues?.nodeValues?.filter((node) => node.id !== this.selectedNode) as DataNode[];
        currentTabValues.nodeValues = filteredNodes;
        // if(this.currentTab === 2) {
        //   const nextTabValues = this.tabsMap.get(`tab${this.currentTab + 1}`) as Tab;
        //   const filteredNodes = nextTabValues?.nodeValues?.filter((node) => node.id !== this.selectedNode) as DataNode[];
        //   nextTabValues.nodeValues = filteredNodes;
        //   this.tabsMap.set(`tab${this.currentTab + 1}`, {...currentTabValues});
        // }

        this.tabsMap.set(`tab${this.currentTab}`, {...currentTabValues});
    }

    getNodeName(id: string) {
        const currentTabValues = this.tabsMap.get(`tab${this.currentTab}`)
        const matchedNode = currentTabValues?.nodeValues?.find((node) => node.id === id);
        return matchedNode?.name || '';
    }

    updateSearchPayload(payload: SearchPayload) {
        console.log(payload);
        this.searchPayload = payload;
    }

    loadTableData = async (payload: any, currentTab: string) => {
        this.root.api.fetchSearchResults(payload)
            .then(response => {
                this.setTableData(response, currentTab)
            })
            .catch(alert => console.log(alert));

    }


    setTableData = (data: TableData, currentTab: string) => {
        this.tableData = data;
        const paginationData = this.paginationData;
        const pageSizes = this.prepPageSizes(data.rows.length, paginationData.pageSize);
        this.paginationData = {...paginationData, totalItems: data.rows.length, pageSizes: pageSizes}
        if (this.selectedTabIndex !== 0) {
            const filteredData = data
            filteredData.rows = data.rows.filter(row => !row.isSelected)
            this.tableDataFromStore = this.getPages(paginationData, filteredData);
        }
        this.tableDataFromStore = this.getPages(paginationData, data);
        let currentValue = this.tabsMap.get(currentTab) as Tab;
        currentValue.searchResults = this.tableDataFromStore as TableData;
        this.tabsMap.set(currentTab, {...currentValue});
        console.log(this.tabsMap.toJSON());
    }

    prepPageSizes = (totalItems: number, pageSize: number) => {
        const pageFraction = Math.floor(totalItems / pageSize);
        const pageSizes: number[] = [];
        for (let i = 1; i <= pageFraction; i++) {
            pageSizes.push(i * 10)
        }
        return pageSizes;
    }

    getPages = (pageData: PaginationData, tableData: TableData) => {
        const currentPage = pageData.page;
        const pageSize = pageData.pageSize;

        const startIndex = (currentPage - 1) * pageSize;
        console.log(`startIndex${startIndex}`);

        const endIndex = Math.min(startIndex + pageSize - 1);

        console.log(`endIndex${endIndex}`);

        // calculate total pages
        const totalPages = Math.ceil(tableData?.rows?.length / pageSize);
        const rows = tableData?.rows?.slice(startIndex, endIndex)
        return {headers: tableData.headers, rows: rows};
        //return
    }

    updateSelectedRow = (row: Row) => {
        this.selectedRowId = row.id;
        const currentTabValues = this.tabsMap.get(`tab${this.currentTab}`) as Tab;
        currentTabValues.selectedRowId = row.nodeId;
        if (currentTabValues.tableSelectionType === 'checkbox') {
            currentTabValues.selectedRowValues?.push(row?.cells[0].value);
            const selectedRowId = currentTabValues.selectedRowId as string;
            if (this.searchPayload.nodeSelected === 'MG') {
                this.searchPayload.manageGeographyNodeId = selectedRowId;
            } else if (this.searchPayload.nodeSelected === 'MS') {
                this.searchPayload.manageSegNodeId = selectedRowId;
            } else if (this.searchPayload.nodeSelected === 'LV') {
                this.searchPayload.legalVehicleNodeId = selectedRowId;
            }

            this.dsmtSubmitPayload.push({...this.dsmtSubmitPayload[0], ...this.searchPayload})
            console.log(this.dsmtSubmitPayload);
        } else {
            currentTabValues.selectedRowValue = row?.cells[0].value;
            currentTabValues.searchResults?.rows.map((row) => {
                if (row.id === this.selectedRowId) {
                    row.isSelected = true;
                    currentTabValues.selectedRowId = row.nodeId;
                }
                return row;
            });
            const selectedRowId = currentTabValues.selectedRowId as string;
            if (this.searchPayload.nodeSelected === 'MG') {
                this.searchPayload.manageGeographyNodeId = selectedRowId;
            } else if (this.searchPayload.nodeSelected === 'MS') {
                this.searchPayload.manageSegNodeId = selectedRowId;
            } else if (this.searchPayload.nodeSelected === 'LV') {
                this.searchPayload.legalVehicleNodeId = selectedRowId;
            }
        }
        this.tabsMap.set(`tab${this.currentTab}`, {...currentTabValues});
        // this.tableDataFromStore = {headers: this.tableDataFromStore.headers, rows: [...rows]}
        // console.log(this.tableDataFromStore);
        // this.selectedRowValue = row?.cells[0].value;
    }

    removeSelectedRow = (value: string) => {
        this.tableDataFromStore.rows = this.tableDataFromStore.rows.map((row) => {
            row.isSelected = row.id === value;
            return row;
        })
        this.selectedRowValue = '';
    }

    updateTableData = (paginationData: PaginationData, currentTab: string) => {
        this.tableDataFromStore = this.getPages(paginationData, this.tableData);
        let currentValue = this.tabsMap.get(currentTab) as Tab;
        currentValue.searchResults = this.tableDataFromStore as TableData;
        this.tabsMap.set(currentTab, {...currentValue});
        console.log(this.tableDataFromStore);
    }

}


export default DsmtTabsStore;
