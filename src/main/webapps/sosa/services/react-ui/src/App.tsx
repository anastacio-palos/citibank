/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * OpenPages GRC Platform (PID: 5725-D51)
 *
 * © Copyright IBM Corporation  2020 - CURRENT_YEAR. All Rights Reserved.
 *
 * US Government Users Restricted Rights- Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 *******************************************************************************/
import React from 'react';
import Routing from './app/components/routes/app_routes'
import './App.scss';
import AuditScopingHelper from './app/helpers/audit-scoping-helper';
import {RootStoreProvider} from './app/providers/RootStoreProvider';
import {RootStore} from './app/stores/RootStore';
import AuditableEntityHelper from './app/components/auditableEntityHelper/AuditableEntityHelper';
import AuditHelper from './app/components/auditHelper/AuditHelper';

// configure mobx
// uncomment these lines only if IE11 support is required
//import { configure } from "mobx"
//configure({ useProxies: "never" })

export interface Map {
    [key: string]: string;
}

export default class AppContainer extends React.Component<object, object> {

    parameters: string;

    paramMap: Map = {};

    rootStore = new RootStore();

    constructor(props: object) {
        super(props)
        this.parameters = window.location.search;
        // ?key=11&type=Audit&subType
        let tempMap = this.parameters.replace('?', '').split('&');

        tempMap.map(a => a.split('=')).forEach(a => this.paramMap[a[0]] = a[1]);

        console.log(this.paramMap);

    }


    renderComponent() {
        switch (this.paramMap['type']) {
            case 'auditScoping':
                return <AuditScopingHelper id={this.paramMap['id']}/>;
            case 'auditDSMT':
                return <AuditHelper/>;
            case 'auditableEntityDSMT':
                return <AuditableEntityHelper/>;
        }
    }

    render() {
        return (
            <RootStoreProvider>
                {this.renderComponent()}
            </RootStoreProvider>
        );
    }
}
