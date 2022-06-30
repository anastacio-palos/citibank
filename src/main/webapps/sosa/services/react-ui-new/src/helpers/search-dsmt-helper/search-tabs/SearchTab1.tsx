import React, {useState, ChangeEvent} from 'react';
import {
    Search,
    Select, SelectItem, Button, Tag
} from 'carbon-components-react';
import {
    Search20,
} from '@carbon/icons-react';
import './search-tab1.scss'
import {observer} from "mobx-react-lite";
import {StoreTypes, useStore} from '../../../RootStoreProvider';
import {DataNode} from '../../../api/models/searchDsmtModel';
import DsmtTabsStore from '../DsmtTabsStore';

interface Props {
    nodes: DataNode[];
    selectedTabIndex: number;
    tableSelectionType: string;
    currentTab: string;
    selectedRowValue: string;
}


export const SearchTab1 = observer((props: Props) => {
    const [searchInput, setSearchInput] = useState('');
    const dsmtTabsStore = useStore(StoreTypes.DsmtTabsStore) as DsmtTabsStore;

    const handleSearchInput = (event: ChangeEvent<{ value: string }>) => {
        console.log(event?.currentTarget?.value);
        setSearchInput(event?.currentTarget?.value || '');
    }

    const handleSearchTrigger = () => {
        const showTree = searchInput?.length < 3;

        const payload = dsmtTabsStore.searchPayload;
        payload.nodeSelected = dsmtTabsStore.selectedNode;
        payload.searchText = searchInput;

        dsmtTabsStore.updateSearchPayload(payload)
        dsmtTabsStore.loadTableData(payload, props.currentTab);
    }

    const handleNodeSelection = (e: React.ChangeEvent<HTMLSelectElement>) => {
        dsmtTabsStore.updateTabSelection(props.selectedTabIndex, e?.currentTarget?.value);
    }

    return (
        <>
            <div className="bx--col-lg-8 bx--col-md-4 bx--col-sm-2 sample-spacer">
                <Select id="select-1"
                        onChange={handleNodeSelection}
                        defaultValue="placeholder-item">
                    <SelectItem
                        disabled
                        hidden
                        value="placeholder-item"
                        text="Choose an option"
                    />
                    {props.nodes.map((node) => {
                        return <SelectItem key={node.id}
                                           value={node.id}
                                           text={node.name}
                        />
                    })}
                </Select>
            </div>
            <div className="sample-spacer  bx--col-lg-8 bx--col-md-4 bx--col-sm-2">
                <div className="flex-container sample-spacer">
                    <Search
                        id="search-1" labelText='Search' value={searchInput} onChange={handleSearchInput}/>
                    <Button kind="tertiary" iconDescription="Search" hasIconOnly onClick={(handleSearchTrigger)}>
                        <Search20/>
                    </Button>
                </div>
            </div>
        </>
    )
})

export default SearchTab1;