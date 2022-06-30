import {NestingTableDataStore} from "./nestingTable/nestingTableDataStore";
import {ExistingDsmtAccordionDataStore} from "./existingDsmtAccordion/existingDsmtAccordionDataStore";
import {GeneralDetailsAccordionStore} from "./generalDetailsAccordionStore/generalDetailsAccordionStore";
import {TreeExampleStore} from "./children/TreeExampleStore";
import SearchNodesStore from "./children/SearchNodesStore";
import {CustomHeaderDataStore} from "./customHeader/customHeaderDataStore";


export class RootStore {
    searchNodesStore: SearchNodesStore
    nestingTableDataStore: NestingTableDataStore
    generalDetailsAccordionStore: GeneralDetailsAccordionStore
    existingDsmtAccordionDataStore: ExistingDsmtAccordionDataStore
    treeExampleStore: TreeExampleStore;
    customHeaderStore: CustomHeaderDataStore;

    constructor() {
        this.searchNodesStore = new SearchNodesStore(this);
        this.nestingTableDataStore = new NestingTableDataStore();
        this.generalDetailsAccordionStore = new GeneralDetailsAccordionStore();
        this.existingDsmtAccordionDataStore = new ExistingDsmtAccordionDataStore();
        this.treeExampleStore = new TreeExampleStore(this);
        this.customHeaderStore = new CustomHeaderDataStore();
    }

}
