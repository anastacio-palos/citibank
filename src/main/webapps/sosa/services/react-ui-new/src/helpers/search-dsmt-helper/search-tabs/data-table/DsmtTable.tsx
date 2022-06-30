import React from 'react';
import {
    Table,
    TableHead,
    TableRow,
    TableHeader,
    TableBody,
    TableCell,
    TableSelectRow,
    DataTable,
    TableSelectRowProps,
    TableContainerProps,
    TableProps,
    TableRowProps,
    TableHeaderProps,
    Pagination,
    TableToolbar,
    TableToolbarContent,
    TableToolbarSearch,
    PaginationPageSize
} from 'carbon-components-react';
import {observer} from "mobx-react-lite";
import {StoreTypes, useStore} from '../../../../RootStoreProvider';
import {TableData, Row, PaginationData, Header} from '../../../../api/models/searchDsmtModel'
import DsmtTabsStore from '../../DsmtTabsStore';

interface IProps {
    rows: Row[];
    headers: Header[];
    selectedRows: any[];
    getHeaderProps: (props?: {}) => TableHeaderProps;
    getRowProps: (props?: {}) => TableRowProps;
    getSelectionProps: (props?: {}) => TableSelectRowProps;
    getTableProps: (props?: {}) => TableProps;
    getTableContainerProps: (props?: {}) => TableContainerProps;
}

interface Props {
    tableData: TableData;
    pagination: PaginationData;
    tableSelectionType: string;
    currentTab: string;
}


export const DsmtTable = observer((props: Props) => {
    const dsmtTabsStore = useStore(StoreTypes.DsmtTabsStore) as DsmtTabsStore;

    const onCheckedRow = (row: Row) => {
        dsmtTabsStore.updateSelectedRow(row);
    }


    const handlePaginationChange = (paginationData: PaginationData) => {
        console.log('hit', paginationData);

        dsmtTabsStore.updateTableData(paginationData, props.currentTab);
    }

    const handleSelectRow = (row: any) => {
        console.log(row)
    }

    const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        console.log(e.currentTarget.value);
    }

    return (
        <>
            <DataTable rows={props.tableData?.rows} headers={props.tableData?.headers}
                       radio={props.tableSelectionType === 'radio'}>
                {({
                      rows,
                      headers,
                      getHeaderProps,
                      getRowProps,
                      getSelectionProps,
                      getTableProps,
                  }: IProps) => (
                    <>
                        {/* <TableToolbar>
                <TableToolbarContent>
                  <TableToolbarSearch onChange={onInputChange} />
                </TableToolbarContent>
              </TableToolbar> */}
                        <Table {...getTableProps()}>
                            <TableHead>
                                <TableRow>
                                    {/* <TableSelectAll {...getSelectionProps()} /> */}
                                    <TableHeader/>
                                    {headers.map((header, i) => (
                                        <TableHeader key={i} {...getHeaderProps({header})}>
                                            {header.header}
                                        </TableHeader>
                                    ))}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {rows.map((row, i) => (
                                    <TableRow key={i} {...getRowProps({row, isSelected: row.isSelected})} >
                                        <TableSelectRow {...getSelectionProps({
                                            row,
                                            onClick: () => onCheckedRow(row)
                                        })} />
                                        {row.cells.map((cell: any) => (
                                            <TableCell key={cell.id}>{cell.value}</TableCell>
                                        ))}
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </>
                )}
            </DataTable>
            <Pagination
                onChange={(data) => handlePaginationChange(data)}
                page={props.pagination.page}
                totalItems={props.pagination.totalItems}
                pageSizes={[10, 20]}
            />
        </>
    )
})

export default DsmtTable;
