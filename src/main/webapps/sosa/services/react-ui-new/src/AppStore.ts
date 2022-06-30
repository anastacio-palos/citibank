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

import {makeAutoObservable} from 'mobx';
import {Api} from './api/api';
import {ExamplesStore} from './examples/ExamplesStore';

export class AppStore {

    loaded: boolean = false;
    api: Api;
    examplesStore: ExamplesStore;

    constructor() {
        this.api = new Api();
        this.examplesStore = new ExamplesStore(this);
        makeAutoObservable(this);
    }

    async bootstrap(): Promise<void> {
        if (process.env.NODE_ENV === 'development') {
            return this.api.devLogon().then(() => {
                this.loaded = true;
            });
        } else {
            return new Promise<void>((resolve) => {
                this.loaded = true;
                resolve();
            });
        }
    }
}