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

import {makeAutoObservable} from "mobx"
import {Api} from '../../api/api';

export class AdminModeExampleStore {
    api: Api;
    adminMode = 'loading';

    constructor(api: Api) {
        this.api = api;
        makeAutoObservable(this);
    }

    getAdminMode() {
        this.api.getAdminMode()
            .then(response => this.onGetAdminMode(response))
    }

    onGetAdminMode(adminMode: boolean) {
        this.adminMode = adminMode ? 'true' : 'false';
    }

    setAdminMode(set: boolean) {
        this.api.setAdminMode(set).then(() => this.getAdminMode())
    }
}