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
import {Button} from 'carbon-components-react';
import {AdminModeExampleStore} from './AdminModeExampleStore';
import './AdminModeExample.scss';

interface Props {
    adminModelExampleStore: AdminModeExampleStore;
}

function onSetAdminMode(e: any, adminModelExampleStore: AdminModeExampleStore, set: boolean) {
    e.preventDefault();
    adminModelExampleStore.setAdminMode(set);
}

export const AdminModeExample = observer((props: Props) => {
    const {adminModelExampleStore} = props;

    useEffect(() => {
        adminModelExampleStore.getAdminMode();
    }, [adminModelExampleStore]);

    return (
        <div className="op-example-admin-mode">
            Admin Mode: {adminModelExampleStore.adminMode}
            <br/>
            <br/>
            <Button type="button" onClick={(e) => onSetAdminMode(e, adminModelExampleStore, true)}>
                Turn on
            </Button>
            <Button type="button" onClick={(e) => onSetAdminMode(e, adminModelExampleStore, false)}>
                Turn off
            </Button>
        </div>
    )
})
