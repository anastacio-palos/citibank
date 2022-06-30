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
import {AlertModel} from "../../api/models/alertModel";
import {Api} from '../../api/api';
import {ConfigurationTextModel} from "../../api/configurationText";

export class TableExampleStore {
    api: Api;
    loading = false;
    headers: string[];
    rows: ConfigurationTextModel[];
    alert: AlertModel | undefined;

    constructor(api: Api) {
        this.api = api;
        this.headers = ['Name', 'Category', 'Localized Label'];
        this.rows = []
        makeAutoObservable(this);
    }

    load() {
        this.loading = true;
        this.alert = undefined;
        this.api.getConfigurationText()
            .then(response => this.onLoad(response))
            .catch(alert => this.onAlert(alert));
    }

    onLoad(text: ConfigurationTextModel[]) {
        this.loading = false;
        this.rows = text.length > 50 ? text.slice(0, 49) : text;// just cap at 50 rows
    }

    onAlert(alert: AlertModel) {
        this.loading = false;
        this.alert = alert;
    }

}