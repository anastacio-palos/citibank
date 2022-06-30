import React from 'react';
import {observer} from 'mobx-react-lite';
import {ITableProps} from '../../stores/nestingTable/nestingTableDataStore';
import {
    DataTable,
    DataTableSkeleton,
    Table,
    TableBody,
    TableContainer,
    TableHead,
    TableHeader,
    TableRow,
    TableSelectAll
} from 'carbon-components-react';
import {NestableRow} from './NestableRow';
import {RootStoreProvider, useNestedTableDataStore} from '../../providers/RootStoreProvider';

const NestingTable: React.FC = observer(() => {
    const dataStore = useNestedTableDataStore();

    if (dataStore.loading) {
        return (
            <DataTableSkeleton showHeader={false} showToolbar={false}/>
        );
    }

    return (
        <RootStoreProvider>
            <DataTable
                rows={dataStore.rows}
                headers={dataStore.headers}>
                {({
                      rows,
                      headers,
                      getHeaderProps,
                      getRowProps,
                      getTableProps,
                      getSelectionProps,
                      selectedRows
                  }: ITableProps) => (
                    <TableContainer title={dataStore.title}>
                        <Table {...getTableProps()}>
                            <TableHead>
                                <TableRow>
                                    <TableHeader/>
                                    <TableSelectAll {...getSelectionProps()} />
                                    {headers.map(header => (
                                        <TableHeader colSpan={1} {...getHeaderProps({header})}>
                                            {header.header}
                                        </TableHeader>
                                    ))}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {rows.map((row, rowIndex) => (
                                    <NestableRow
                                        key={rowIndex}
                                        nestingTableDataStore={dataStore}
                                        getRowProps={getRowProps}
                                        getSelectionProps={getSelectionProps}
                                        activeRow={row}
                                        rowIndex={rowIndex}/>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </DataTable>
        </RootStoreProvider>
    );
});

export default NestingTable;
