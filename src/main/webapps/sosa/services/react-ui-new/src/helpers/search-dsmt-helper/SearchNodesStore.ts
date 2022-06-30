import {makeAutoObservable} from "mobx";
// import { RootStore } from "../../RootStore";
// import { DataNode, PaginationData } from '../../api/models/nodesModel'
// import { Row, TableData } from "../../api/models/searchDsmtModel";
// import { defaultPaginationData, defaultTableData } from "./constants/SearchConstants";

// class SearchNodesStore {
//   root: RootStore
//   constructor(root: RootStore) {
//     this.root = root;
//     makeAutoObservable(this);
//   }

//   selectedRowId: string = ''
//   selectedRowValue: string = '';
//   selectedNode: string = '';
//   selectedTabIndex: number = 0;
//   tableDataFromStore: TableData = defaultTableData;

//   tableData: TableData = defaultTableData;

//   paginationData: PaginationData = defaultPaginationData;

//   loadTableData = async (payload: any) => {
//     this.root.api.fetchSearchResults(payload)
//     .then(response => {
//       this.setTableData(response)
//     })
//     .catch(alert => console.log(alert));

//   }


// setTableData = (data: TableData) => {
//   this.tableData = data;
//   const paginationData = this.paginationData;
//   const pageSizes = this.prepPageSizes(data.rows.length, paginationData.pageSize);
//   this.paginationData = {...paginationData, totalItems: data.rows.length, pageSizes: pageSizes}
//   if(this.selectedTabIndex !== 0){
//     const filteredData = data
//     filteredData.rows = data.rows.filter(row => !row.isSelected)
//     this.tableDataFromStore = this.getPages(paginationData, filteredData);
//   }

//   this.tableDataFromStore = this.getPages(paginationData, data);

// }

// prepPageSizes = (totalItems: number, pageSize: number) => {
//   const pageFraction = Math.floor(totalItems/pageSize);
//   const pageSizes: number[] = [];
//     for (let i = 1; i <= pageFraction; i++) {
//       pageSizes.push(i * 10)
//     }
//   return pageSizes;
// }

//  getPages = (pageData: PaginationData, tableData: TableData) => {
//     const currentPage = pageData.page;
//     const pageSize = pageData.pageSize;

//     const startIndex = (currentPage - 1) * pageSize;
//     console.log(`startIndex${startIndex}`);

//     const endIndex = Math.min(startIndex + pageSize - 1);

//     console.log(`endIndex${endIndex}`);

//     // calculate total pages
//     const totalPages = Math.ceil(tableData?.rows?.length / pageSize);
//     const rows = tableData?.rows?.slice(startIndex, endIndex)
//     return {headers: tableData.headers, rows: rows};
//     //return 
//   }

//   updateSelectedRow = (row: Row) => {
//     this.selectedRowId = row.id;
//     const rows = this.tableDataFromStore.rows.map((row) => {
//       row.isSelected = row.id === this.selectedRowId;
//       return row;
//     });

//     this.tableDataFromStore = {headers: this.tableDataFromStore.headers, rows: [...rows]}
//     console.log(this.tableDataFromStore);
//     this.selectedRowValue = row?.cells[0].value;
//   }

//   removeSelectedRow = (value: string) => {
//     console.log(value);
//     this.tableDataFromStore.rows = this.tableDataFromStore.rows.map((row) => {
//       row.isSelected = row.id === value;
//       return row;
//     })
//     this.selectedRowValue = '';
//   }

//   updateTableData = (paginationData: PaginationData) => {
//     this.tableDataFromStore = this.getPages(paginationData, this.tableData);
//   }

// }


// export default SearchNodesStore;
