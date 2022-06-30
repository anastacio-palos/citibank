import React, {useState, useContext, useEffect, FC} from 'react';
import {
    Tabs, Tab, Button
} from 'carbon-components-react';
import './search-dsmt.scss';
import SearchTab1 from './search-tabs/SearchTab1';
import {observer} from "mobx-react-lite";
import {useSearchNodesStore} from '../../providers/RootStoreProvider';
import {DataNode} from '../../api/nodesModel';


export const SearchDsmtHelper: FC = observer(() => {
    const [selectedTab, setSelectedTab] = useState(0);
    const nodesStore = useSearchNodesStore();
    const [allNodes, setAllNodes] = useState<DataNode[]>([]);
    const [filteredNodes, setFilteredNodes] = useState<DataNode[]>([]);
    const {tabs, getAllNodes, getFilteredNodes, selectedRowValue} = nodesStore;

    useEffect(() => {
        const allNodes = getAllNodes();
        setAllNodes(allNodes);
    }, getAllNodes());

    const handleNextButtonClick = () => {
        const nextTab = selectedTab + 1;
        const filteredNodes = getFilteredNodes();
        setSelectedTab(nextTab);
        setFilteredNodes(filteredNodes);
    }
    return (
        <div className="bx--grid bx--grid--full-width bx--grid--condensed landing-page">
            <div className="bx--row landing-page__banner">
                <div className="bx--col-lg-16 bx--col-md-8 bx--col-sm-4">
                    <h1 className="landing-page__heading">Search DSMT</h1>
                    <Tabs type="container" selected={selectedTab}>
                        <Tab id={tabs[0].id} label={tabs[0].label}>
                            <SearchTab1 nodes={allNodes} selectedTabIndex={selectedTab}/>
                        </Tab>
                        <Tab id={tabs[1].id}
                             disabled={tabs[1].disabled}
                             label={tabs[1].label}>
                            <SearchTab1 nodes={filteredNodes} selectedTabIndex={selectedTab}/>
                        </Tab>
                        <Tab
                            id={tabs[2].id}
                            disabled={tabs[2].disabled}
                            label="Tab label 3 shows truncation"
                            title="Tab label 3 shows truncation">
                            <p>Content for third tab goes here.</p>
                        </Tab>
                    </Tabs>
                    <Button kind="primary"
                            onClick={handleNextButtonClick}
                            iconDescription="Next"
                            className="self-align-right" disabled={!selectedRowValue}>Next
                    </Button>

                </div>
            </div>
        </div>
    );
})

export default SearchDsmtHelper;

