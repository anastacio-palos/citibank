import React, {createContext, ReactNode, useContext} from "react";
import {RootStore} from "./RootStore";

export enum StoreTypes {
    AuditableEntityDsmtStore = 'auditableEntityDsmtStore',
    // SearchNodesStore = 'searchNodesStore',
    DsmtTabsStore = 'dsmtTabsStore',
    AuditHelperDataStore = 'auditHelperDataStore',
    ExistingDsmtAccordionUiStore = 'existingDsmtAccordionUiStore',
    // TODO: Below stores should be removed
    NestingTableDataStore = 'nestingTableDataStore',
    ExistingDsmtAccordionDataStore = 'existingDsmtAccordionDataStore',
    GeneralDetailsAccordionStore = 'generalDetailsAccordionStore',
}

let store: RootStore;
const StoreContext = createContext<RootStore | undefined>(undefined);
StoreContext.displayName = "StoreContext";

export function useRootStore() {
    const context = useContext(StoreContext);
    if (context === undefined) {
        throw new Error("useRootStore must be used within RootStoreProvider");
    }

    return context;
}

export function useAuditableEntityDsmtStore() {
    const {auditableEntityDsmtStore} = useRootStore();
    return auditableEntityDsmtStore;
}

// export function useSearchNodesStore() {
//   const { searchNodesStore } = useRootStore();
//   return searchNodesStore;
// }


export function useDsmtTabsStore() {
    const {dsmtTabsStore} = useRootStore();
    return dsmtTabsStore;
}

export function useNestedTableDataStore() {
    const {nestingTableDataStore} = useRootStore();
    return nestingTableDataStore;
}

export function useGeneralDetailsAccordionStore() {
    const {generalDetailsAccordionStore} = useRootStore();
    return generalDetailsAccordionStore;
}

export function useStore(storeType: StoreTypes) {
    const stores = useRootStore();

    return stores[storeType];
}

// export function useExampleTreeStore() {
//   const { treeExampleStore } = useRootStore();
//   return treeExampleStore;
// }

export function RootStoreProvider({children}: { children: ReactNode }) {
    // only create root store once (store is a singleton)
    const root = store ?? new RootStore();

    return <StoreContext.Provider value={root}>{children}</StoreContext.Provider>;
}
