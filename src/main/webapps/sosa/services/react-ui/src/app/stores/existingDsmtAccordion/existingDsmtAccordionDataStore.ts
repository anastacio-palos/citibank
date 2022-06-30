import {makeAutoObservable} from 'mobx';
import {MockApi} from '../../components/nestingTable/mockApi/mockApi';
import {ITableHeader, ITableProps, ITableRow} from '../nestingTable/nestingTableDataStore';

export class ExistingDsmtAccordionDataStore {
    api: MockApi;
    loading: boolean;
    headers: ITableHeader[];
    rows: ITableRow[];
    title: string;

    constructor() {
        this.api = MockApi.getInstance();
        this.loading = false;
        this.headers = [];
        this.rows = [];
        this.title = 'Existing DSMT Links';
        makeAutoObservable(this);
        this.load();
    }

    private load() {
        this.loading = true;
        this.api.getTableData()
            .then(response => this.onLoad(response))
            .catch(error => console.log(`ERROR ${error}`));
    }

    private onLoad(tableData: ITableProps) {
        this.loading = false;
        this.headers = tableData.headers;
        this.rows = tableData.rows;
    }
}
