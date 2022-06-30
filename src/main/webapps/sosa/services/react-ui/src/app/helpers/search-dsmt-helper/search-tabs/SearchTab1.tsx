import React, {useState, useEffect, ChangeEvent} from 'react';
import {
    Search,
    Select, SelectItem, Button, Tag
} from 'carbon-components-react';
import {
    Search20,
} from '@carbon/icons-react';
import DsmtTable from './data-table/DsmtTable';
import './search-tab1.scss'
import {observer} from "mobx-react-lite";
import {useSearchNodesStore} from '../../../providers/RootStoreProvider';
// import { TreeExample } from 'src/app/components/custom-tree/TreeExample';
import {DataNode} from '../../../api/nodesModel';

interface Props {
    nodes: DataNode[];
    selectedTabIndex: number;
}

export const SearchTab1 = observer((props: Props) => {
    const [showTree, setShowTree] = useState(false);
    const [searchInput, setSearchInput] = useState('');
    const nodesStore = useSearchNodesStore();
    const {updateTabSelection, removeSelectedRow, selectedRowValue} = nodesStore;


    const handleSearchInput = (event: ChangeEvent<{ value: string }>) => {
        setSearchInput(event?.currentTarget?.value);
    }

    const handleSearchTrigger = () => {
        const showTree = searchInput?.length < 3;
        setShowTree(showTree);
    }

    const handleNodeSelection = (e: React.ChangeEvent<HTMLSelectElement>) => {
        updateTabSelection(props.selectedTabIndex, e?.currentTarget?.value);
    }

    const handleCloseTag = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        console.log(e.currentTarget.value);
        removeSelectedRow();
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
            <div className="sample-spacer">
                <div className="bx--col-lg-8 bx--col-md-4 bx--col-sm-2 flex-container sample-spacer">
                    <Search
                        id="search-1" labelText='Search' value={searchInput} onChange={handleSearchInput}/>
                    <Button kind="tertiary" iconDescription="Search" hasIconOnly onClick={(handleSearchTrigger)}>
                        <Search20/>
                    </Button>
                </div>
                <div className="flex-container column">
                    {showTree ?
                        <div>
                            {/* <TreeExample /> */}
                        </div>
                        :
                        <div className="sample-spacer">
                            {selectedRowValue !== '' ?
                                <Tag type="gray" filter title={selectedRowValue} onClose={handleCloseTag}>
                                    {selectedRowValue}
                                </Tag> : selectedRowValue}
                            <DsmtTable/>
                        </div>
                    }
                </div>
            </div>
        </>
    )
})

export default SearchTab1;