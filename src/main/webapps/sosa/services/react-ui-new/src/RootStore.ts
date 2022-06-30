import {NestingTableDataStore} from "./../src/stores/nestingTable/nestingTableDataStore";
import {ExistingDsmtAccordionUiStore} from "./stores/existingDsmtAccordion/existingDsmtAccordionUiStore";
import {GeneralDetailsAccordionStore} from "./../src/stores/generalDetailsAccordionStore/generalDetailsAccordionStore";
import {AuditableEntityDsmtStore} from "./helpers/auditableEntityHelper/auditableEntityDsmtStore";
import {TreeExampleStore} from "./../src/stores/children/TreeExampleStore";
// import SearchNodesStore from "./../src/helpers/search-dsmt-helper/SearchNodesStore";
import DsmtTabsStore from "./../src/helpers/search-dsmt-helper/DsmtTabsStore";
import {Api} from "./api/api";
import AuditHelperDataStore from "./stores/auditHelperDataStore";


export class RootStore {
    auditableEntityDsmtStore: AuditableEntityDsmtStore;
    // searchNodesStore: SearchNodesStore;
    dsmtTabsStore: DsmtTabsStore;
    auditHelperDataStore: AuditHelperDataStore;
    api: Api;
    nestingTableDataStore: NestingTableDataStore
    generalDetailsAccordionStore: GeneralDetailsAccordionStore
    existingDsmtAccordionUiStore: ExistingDsmtAccordionUiStore
    treeExampleStore: TreeExampleStore;

    constructor() {
        this.api = new Api();
        this.auditableEntityDsmtStore = new AuditableEntityDsmtStore(this);
        // this.searchNodesStore = new SearchNodesStore(this);
        this.dsmtTabsStore = new DsmtTabsStore(this);
        this.auditHelperDataStore = new AuditHelperDataStore(this);
        this.nestingTableDataStore = new NestingTableDataStore();
        this.generalDetailsAccordionStore = new GeneralDetailsAccordionStore();
        this.existingDsmtAccordionUiStore = new ExistingDsmtAccordionUiStore();
        this.treeExampleStore = new TreeExampleStore(this);
    }

}
