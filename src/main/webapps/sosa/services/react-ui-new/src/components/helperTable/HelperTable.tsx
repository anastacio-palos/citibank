import React, {useEffect} from 'react';
// import {
//     Table,
//     TableHead,
//     TableRow,
//     TableHeader,
//     TableBody,
//     TableCell,
//     TableSelectRow,
//     DataTable,
//     TableSelectRowProps,
//     TableContainerProps,
//     TableProps,
//     TableRowProps,
//     TableHeaderProps,
//     Pagination,
//     TableToolbar,
//     TableToolbarContent,
//     TableToolbarSearch,
//     PaginationPageSize
//   } from 'carbon-components-react';
// import { observer } from "mobx-react-lite";
// import { StoreTypes, useStore } from '../../RootStoreProvider';
// import { TableData, Row } from '../../api/models/searchDsmtModel'
// import HelperTableStore from './HelperTableStore';

// interface IProps {
//   rows: any[];
//   headers: any[];
//   selectedRows: any[];
//   getHeaderProps: ( props?: {} ) => TableHeaderProps;
//   getRowProps: ( props?: {} ) => TableRowProps;
//   getSelectionProps: ( props?: {} ) => TableSelectRowProps;
//   getTableProps: ( props?: {} ) => TableProps;
//   getTableContainerProps: ( props?: {} ) => TableContainerProps;
// }

// interface PaginationData {
//   page: number;
//   pageSize: number;
//   totalItems?: number;
//   pageSizes?: readonly number[];
// }

// interface Props {
//   tableData: TableData;
//   pagination: PaginationData;
//   tableSelectionType: string;
// }


// export const HelperTable = observer(({ tableData, pagination, tableSelectionType }: Props) => {
//   const helperTableStore = useStore(StoreTypes.HelperTableStore) as HelperTableStore;
//   useEffect(() => {
//     helperTableStore.setTableData(tableData)
//   },[helperTableStore, tableData])

//   const onCheckedRow = (row: Row) => {
//     helperTableStore.updateSelectedRow(row);
//   }

//   const handlePaginationChange = (paginationData: PaginationData) => {

//     helperTableStore.updateTableData(paginationData);
//   }

//   const handleSelectRow = (row: any) => {
//     console.log(row)
//   } 

//   const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
//     console.log(e.currentTarget.value);
//   }

//   return (
//       <>
//         <DataTable rows={helperTableStore.tableDataFromStore?.rows} headers={helperTableStore.tableDataFromStore?.headers} radio={tableSelectionType==='radio'}>
//         {({
//           rows,
//           headers,
//           getHeaderProps,
//           getRowProps,
//           getSelectionProps,
//           getTableProps,
//         }: IProps) => (
//           <>
//               <TableToolbar>
//                 <TableToolbarContent>
//                   <TableToolbarSearch onChange={onInputChange} />
//                 </TableToolbarContent>
//               </TableToolbar>
//               <Table {...getTableProps()}>
//                 <TableHead>
//                   <TableRow>
//                     {/* <TableSelectAll {...getSelectionProps()} /> */}
//                     <TableHeader />
//                     {headers.map((header, i) => (
//                       <TableHeader key={i} {...getHeaderProps({ header })}>
//                         {header.header}
//                       </TableHeader>
//                     ))}
//                   </TableRow>
//                 </TableHead>
//                 <TableBody>
//                   {rows.map((row, i) => (
//                     <TableRow key={i} {...getRowProps({ row, isSelected: row.isSelected })} >
//                       <TableSelectRow {...getSelectionProps({ row, onClick: () => onCheckedRow(row) }) } />
//                         {row.cells.map((cell: any) => (
//                           <TableCell key={cell.id}>{cell.value}</TableCell>
//                         ))}
//                     </TableRow>
//                   ))}
//                 </TableBody>
//               </Table>
//             </>
//         )}
//       </DataTable>
//       <Pagination 
//         onChange={(data) => handlePaginationChange(data)}
//         page={pagination.page}
//         totalItems={pagination.totalItems}
//         pageSizes = {[10, 20]}
//       />
//     </>
//     )
// })

// export default HelperTable;
