import {makeAutoObservable} from "mobx";
import {RootStore} from "../../RootStore";
import {GeneralDetailsEntity} from '../../api/models/auditableEntityInitModel';
import {defaultHeaderInfo, IHeaderInfo} from "../../components/header/CustomHeader";
import {ITableProps} from "../../stores/nestingTable/nestingTableDataStore";
import {defaultGridInfo} from "../../stores/auditHelperDataStore";

export class AuditableEntityDsmtStore {
    root: RootStore;
    headerInfo: IHeaderInfo;
    generalInfo: GeneralDetailsEntity[];
    loaded = false;
    existingDSMTLinksGridInfo: ITableProps;

    constructor(root: RootStore) {
        this.root = root;
        this.headerInfo = defaultHeaderInfo;
        this.generalInfo = [{
            editable: false,
            fieldName: "",
            fieldValue: "",
            required: false
        }];
        this.existingDSMTLinksGridInfo = defaultGridInfo;
        makeAutoObservable(this);
    }

    loadInitData = async () => {
        console.log('hit loadinitdata');
        await this.root.api.getAuditableEntityInit()
            .then((response) => this.onLoad(response))
            .catch(alert => console.log(alert));
    }

    onLoad = (response: any) => {
        console.log(response.headerInfo);
        this.loaded = true;
        this.headerInfo = response.headerInfo;
        this.generalInfo = response?.helperObjectContentInfo?.generalDetails || [];
        this.existingDSMTLinksGridInfo = response.existingDSMTLinksGridInfo;
    }

}
