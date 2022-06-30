/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * OpenPages GRC Platform (PID: 5725-D51)
 *
 * © Copyright IBM Corporation  2021 - CURRENT_YEAR. All Rights Reserved.
 *
 * US Government Users Restricted Rights- Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 *******************************************************************************/
import React, {useEffect} from 'react';
import {observer} from "mobx-react-lite"
import {
    InlineNotification,
    DataTableSkeleton,
    Table,
    TableHead,
    TableHeader,
    TableRow,
    TableBody,
    TableCell,
    NotificationKind
} from 'carbon-components-react';
import {TableExampleStore} from './TableExampleStore';
import './TableExample.scss';
import {AlertModel} from '../../api/models/alertModel';

interface Props {
    tableExampleStore: TableExampleStore | undefined;
}

function renderAlert(alert: AlertModel) {
    return (
        <InlineNotification
            title={alert.title ? alert.title : ''}
            subtitle={alert.message ? alert.message : ''}
            kind={alert.type ? alert.type as NotificationKind : 'error'}
        />
    )
}

export const TableExample = observer((props: Props) => {
    const {tableExampleStore} = props;

    useEffect(() => {
        tableExampleStore?.load();
    }, [tableExampleStore]);

    if (!tableExampleStore) {
        return null;
    }

    if (tableExampleStore.loading) {
        return (
            <DataTableSkeleton showHeader={false} showToolbar={false}/>
        );
    }

    const {headers, rows, alert} = tableExampleStore;

    return (
        <div className="op-example-table">
            {alert && renderAlert(alert)}
            {/* <Table>
				<TableHead>
					<TableRow>
						{headers.map((header) => (
							<TableHeader key={header}>{header}</TableHeader>
						))}
					</TableRow>
				</TableHead>
				<TableBody>
					{rows.map((row: any) => (
						<TableRow key={row.name}>
							{Object.keys(row)
								.map((key) => {
									return <TableCell key={key}>{row[key]}</TableCell>;
								})}
						</TableRow>
					))}
				</TableBody>
			</Table> */}
        </div>
    )
})
