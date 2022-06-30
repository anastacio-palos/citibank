import {makeAutoObservable} from 'mobx';
import {GeneralDetailsEntity} from '../api/models/auditableEntityInitModel';
import {defaultHeaderInfo, IHeaderInfo} from '../components/header/CustomHeader';
import {RootStore} from '../RootStore';
import {ITableProps} from './nestingTable/nestingTableDataStore';

export const defaultGridInfo = {
    rows: [],
    headers: []
};

class AuditHelperDataStore {
    loading: boolean;
    rootStore: RootStore;
    generalDetails: GeneralDetailsEntity[];
    headerInfo: IHeaderInfo;
    existingDSMTLinksGridInfo: ITableProps;
    newDSMTLinksGridInfo: ITableProps;
    searchTableData: ITableProps;

    constructor(rootStore: RootStore) {
        this.rootStore = rootStore;
        this.loading = false;
        this.generalDetails = [];
        this.headerInfo = defaultHeaderInfo;
        this.existingDSMTLinksGridInfo = defaultGridInfo;
        this.newDSMTLinksGridInfo = defaultGridInfo;
        this.searchTableData = defaultGridInfo;
        makeAutoObservable(this);
    }

    loadInitData() {
        this.loading = true;
        this.rootStore.api.getAuditHelperInit()
            .then(response => this.onLoad(response))
            .catch(error => console.log(`ERROR ${error}`));

    }

    onSearch(string: string) {
        this.rootStore.api.fetchAuditSearchResults(string)
            .then(response => this.loadSearchData(response))
            .catch(error => console.log(error));
    }

    private loadSearchData(data: any) {
        this.searchTableData = data;
    }

    private onLoad(response: any) {
        const {helperObjectContentInfo, headerInfo, existingDSMTLinksGridInfo, newDSMTLinksGridInfo} = response;
        this.loading = false;
        this.generalDetails = helperObjectContentInfo.generalDetails;
        this.headerInfo = headerInfo;
        this.existingDSMTLinksGridInfo = existingDSMTLinksGridInfo;
        this.newDSMTLinksGridInfo = newDSMTLinksGridInfo;
    }
}

export default AuditHelperDataStore;
