import React from 'react';
import {Checkbox, TableCell} from 'carbon-components-react';
import {observer} from 'mobx-react-lite';
import {NestingTableUiStore} from '../../stores/nestingTable/nestingTableUiStore';

interface ILeadingColumnCellProps {
    uiStore: NestingTableUiStore;
}


export const LeadingColumnCell: React.FC<ILeadingColumnCellProps> = observer((props: ILeadingColumnCellProps) => {
    const {uiStore} = props;

    let value = '';

    if (uiStore.activeRow.cells) {
        value = uiStore.activeRow.cells[0].value;
    }

    return (
        <>
            <TableCell style={{width: "3rem", height: "3rem"}}/>
            <TableCell>
                <Checkbox
                    id={uiStore.activeRow.id}
                    checked={uiStore.activeRow.isSelected}
                    labelText={value}
                    onChange={() => uiStore.toggleChecked()}/>
            </TableCell>
        </>
    );
});
