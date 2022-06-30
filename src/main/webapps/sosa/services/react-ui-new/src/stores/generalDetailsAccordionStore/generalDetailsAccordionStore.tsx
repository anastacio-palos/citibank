import {makeAutoObservable} from 'mobx';
import {MockApi} from '../../components/nestingTable/mockApi/mockApi';

export interface IRecord {
    header: string;
    value: string | number;
    isEditable: boolean;
    isRequired: boolean;
}

export class GeneralDetailsAccordionStore {
    api: MockApi;
    loading: boolean;
    records: IRecord[];

    constructor() {
        this.api = MockApi.getInstance();
        this.loading = false;
        this.records = [];
        makeAutoObservable(this);
        this.load();
    }

    private load() {
        this.loading = true;
        // this.api.getGeneralDetailsData()
        //   .then(response => this.onLoad(response))
        //   // TODO: implement proper error handling
        //   .catch(error => console.log(`ERROR ${error}`));
    }

    private onLoad(generalDetailsData: IRecord[]) {
        this.loading = false;
        this.records = generalDetailsData;
    }
}
