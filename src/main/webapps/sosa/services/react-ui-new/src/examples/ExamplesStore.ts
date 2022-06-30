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
import {TreeExampleStore} from "./tree/TreeExampleStore";
import {TableExampleStore} from "./table/TableExampleStore";
import {FieldsExampleStore} from "./fields/FieldsExampleStore";
import {AdminModeExampleStore} from "./admin/AdminModeExampleStore";
import {AppStore} from "../AppStore";

export class ExamplesStore {

    appStore: AppStore;
    selectedExample: string | undefined;
    treeExampleStore: TreeExampleStore | undefined;
    tableExampleStore: TableExampleStore | undefined;
    fieldsExampleStore: FieldsExampleStore | undefined;
    adminModelExampleStore: AdminModeExampleStore;

    constructor(appStore: AppStore) {
        this.appStore = appStore;
        makeAutoObservable(this);
        this.treeExampleStore = new TreeExampleStore();
        this.tableExampleStore = new TableExampleStore(this.appStore.api);
        this.fieldsExampleStore = new FieldsExampleStore();
        this.adminModelExampleStore = new AdminModeExampleStore(this.appStore.api);
    }

    selectExample(id: string) {
        if (id !== this.selectedExample) {
            this.selectedExample = id;
        }
    }
}