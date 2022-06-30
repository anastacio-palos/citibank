import React from 'react';
import {
    AccordionItem,
    DataTable,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableHeader,
    TableRow,
    TableSelectAll,
    TableSelectRow
} from 'carbon-components-react';
import {ITableProps} from '../../stores/nestingTable/nestingTableDataStore';
import {useStore} from '../../providers/RootStoreProvider';
import {StoreTypes} from '../../providers/RootStoreProvider'
import {ExistingDsmtAccordionDataStore} from '../../stores/existingDsmtAccordion/existingDsmtAccordionDataStore';
import {observer} from 'mobx-react-lite';

export const ExistingDsmtAccordion = observer(() => {

    return (
        <AccordionItem title="Existing DSMT Links">
            <ExistingDsmtTable/>
        </AccordionItem>
    );
});

export const ExistingDsmtTable = observer(() => {
    const dataStore = useStore(StoreTypes.ExistingDsmtAccordionDataStore) as ExistingDsmtAccordionDataStore;

    return (
        <DataTable rows={dataStore.rows} headers={dataStore.headers}>
            {({
                  rows,
                  headers,
                  getHeaderProps,
                  getSelectionProps,
                  getRowProps,
                  getTableProps,
              }: ITableProps) => (
                <TableContainer>
                    <Table {...getTableProps()} size='normal'>
                        <TableHead>
                            <TableRow>
                                <TableSelectAll {...getSelectionProps()} />
                                {headers.map((header) => (
                                    <TableHeader {...getHeaderProps({header})}>
                                        {header.header}
                                    </TableHeader>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow {...getRowProps({row})}>
                                    <TableSelectRow {...getSelectionProps({row})} />
                                    {row.cells?.map((cell) => (
                                        <TableCell key={cell.id}>{cell.value}</TableCell>
                                    ))}
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </DataTable>
    );
});
