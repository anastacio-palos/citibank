import React, {useState} from 'react';
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
    TableHeaderProps
} from 'carbon-components-react';
import {useSearchNodesStore} from '../../../../providers/RootStoreProvider';
import {TableData, Row} from '../../../../api/searchDsmtModel'

interface IProps {
    rows: any[];
    headers: any[];
    selectedRows: any[];
    getHeaderProps: (props?: {}) => TableHeaderProps;
    getRowProps: (props?: {}) => TableRowProps;
    getSelectionProps: (props?: {}) => TableSelectRowProps;
    getTableProps: (props?: {}) => TableProps;
    getTableContainerProps: (props?: {}) => TableContainerProps;
}


export const DsmtTable = () => {
    const nodesStore = useSearchNodesStore();
    const {updateSelectedRow, tableData} = nodesStore;

    const onCheckedRow = (row: Row) => {
        updateSelectedRow(row);
    }
    return (
        <DataTable rows={tableData.rows} headers={tableData.headers} radio={true}>
            {({
                  rows,
                  headers,
                  selectedRows,
                  getHeaderProps,
                  getRowProps,
                  getSelectionProps,
                  getTableProps,
              }: IProps) => (
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
                            <TableRow key={i} {...getRowProps({row})}>
                                <TableSelectRow {...getSelectionProps({row, onClick: () => onCheckedRow(row)})} />
                                {row.cells.map((cell: any) => (
                                    <TableCell key={cell.id}>{cell.value}</TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            )}
        </DataTable>
    )
}

export default DsmtTable;
