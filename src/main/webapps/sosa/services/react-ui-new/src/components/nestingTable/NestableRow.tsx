import React from 'react';
import {
    TableCell,
    TableExpandRow,
    TableRow,
    TableRowProps,
    TableSelectRow,
    TableSelectRowProps
} from 'carbon-components-react';
import {defaultTableRow, NestingTableUiStore} from '../../stores/nestingTable/nestingTableUiStore';
import {ITableRow, NestingTableDataStore} from '../../stores/nestingTable/nestingTableDataStore';
import {LeadingColumnCell} from './LeadingColumnCell';

export interface INestableRowProps {
    nestingTableDataStore: NestingTableDataStore;
    getRowProps: (props: {}) => TableRowProps;
    getSelectionProps: (props?: {}) => TableSelectRowProps;
    activeRow: ITableRow;
    rowIndex: number;
}

export const generateRowCells = (tableRow: ITableRow = defaultTableRow, isChild: boolean = false) => {
    const tableRowCells = tableRow.cells;

    if (tableRowCells) {
        const cells = tableRowCells.map((cell) => {
            if (isChild && tableRowCells.indexOf(cell) === 0) {
                return;
            }
            return (
                <TableCell colSpan={1} key={cell.id}>
                    {cell.value}
                </TableCell>
            );

        }).filter(element => element !== undefined);

        return cells
    }

    //  TODO: below is for debugging. need to remove later
    return [
        <TableCell key="error-one" colSpan={2}>ERROR</TableCell>,
        <TableCell key="error-two" colSpan={2}>ERROR</TableCell>,
        <TableCell key="error-three" colSpan={2}>ERROR</TableCell>
    ];
};

export const NestableRow: React.FC<INestableRowProps> = (props: INestableRowProps) => {
    const {nestingTableDataStore, getRowProps, getSelectionProps, activeRow, rowIndex} = props;

    // Create mobx ui store
    const uiStore = new NestingTableUiStore();
    uiStore.setActiveRow(activeRow, rowIndex);

    // Pull child row array from mobx data store
    // (activeRow is a version that has been modified by the DataTable component.)
    const children = nestingTableDataStore.getChildRows(uiStore.activeRowIndex);

    if (children) {
        /* TODO: below function triggers mobx warning. Must find alt method to update childRow**/
        const generateChildRowCells = (childRow: ITableRow, index: number) => {
            const childRowCells = Object.keys(childRow).map((key: keyof ITableRow) => {
                if (key !== 'id' && key !== 'isExpanded' && key !== 'hasChildren' && key !== 'isSelected' && key !== 'cells') {
                    return {
                        id: `${uiStore.activeRow.id}:${key}`,
                        value: childRow[key]
                    };
                }
                return undefined;
            }).filter(element => element !== undefined);

            nestingTableDataStore.updateChildRowCells(uiStore.activeRowIndex, index, childRowCells);
        };
        /* TODO ******************************************************************/

        /* TODO: pull out below component into its own folder*********************/
        interface IExpandedChildRowProps {
            childRow: ITableRow;
            index: number;
        }

        const ExpandedChildRow: React.FC<IExpandedChildRowProps> = ({childRow, index}: IExpandedChildRowProps) => {
            const childUiStore = new NestingTableUiStore();
            childUiStore.setActiveRow(childRow, index);
            generateChildRowCells(childRow, index);

            return (
                <TableRow {...getRowProps({row: childRow})}>
                    <TableCell style={{width: "3rem", height: "3rem"}}/>
                    <LeadingColumnCell
                        uiStore={childUiStore}/>
                    {generateRowCells(childRow, true)}
                </TableRow>
            );
        };
        /* TODO ******************************************************************/
        return (
            <React.Fragment>
                <TableExpandRow {...getRowProps({row: uiStore.activeRow})}>
                    <TableSelectRow {...getSelectionProps({row: uiStore.activeRow})} />
                    {generateRowCells(uiStore.activeRow)}
                </TableExpandRow>
                {uiStore.activeRow.isExpanded && children.map((childRow, index) => (
                    <ExpandedChildRow
                        key={index}
                        childRow={childRow}
                        index={index}/>
                ))}
            </React.Fragment>
        );
    }

    return (
        <React.Fragment>
            <TableRow {...getRowProps({row: uiStore.activeRow})}>
                <TableCell style={{width: "3rem", height: "3rem"}}/>
                <TableSelectRow {...getSelectionProps({row: uiStore.activeRow})} />
                {generateRowCells(uiStore.activeRow)}
            </TableRow>
        </React.Fragment>
    );
};