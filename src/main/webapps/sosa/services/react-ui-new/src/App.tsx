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
import React from 'react';
import {observer} from "mobx-react-lite"
import {Examples} from './examples/Examples';
import {AppStore} from './AppStore';
import './App.scss';
import {AppRoutes} from './AppRoutes';

// configure mobx
// uncomment these lines only if IE11 support is required
//import { configure } from "mobx"
//configure({ useProxies: "never" }) 

interface Props {
    appStore: AppStore;
}

export const AppContainer = observer((props: Props) => {
    return (
        <React.Fragment>
            {props.appStore.loaded &&
            <AppRoutes appStore={props.appStore}/>
            }
        </React.Fragment>
    );
});

