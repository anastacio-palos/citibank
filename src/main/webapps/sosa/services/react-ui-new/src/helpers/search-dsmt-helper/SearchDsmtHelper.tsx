import React, {useState, FC} from 'react';
import {
    Tabs, Tab, Button, Tag
} from 'carbon-components-react';
import './search-dsmt.scss';
import SearchTab1 from './search-tabs/SearchTab1';
import {observer} from "mobx-react-lite";
import {StoreTypes, useStore} from '../../RootStoreProvider';
import DsmtTabsStore from './DsmtTabsStore';
import {DsmtTable} from './search-tabs/data-table/DsmtTable';
import {TableData, DataNode} from '../../api/models/searchDsmtModel';


export const SearchDsmtHelper: FC = observer(() => {
    const [selectedTab, setSelectedTab] = useState(0);
    const [showTree, setShowTree] = useState(false);
    const [searchInput, setSearchInput] = useState('');
    const dsmtTabsStore = useStore(StoreTypes.DsmtTabsStore) as DsmtTabsStore;

    const handleNextButtonClick = () => {
        const nextTabIndex = selectedTab + 1;
        setSelectedTab(nextTabIndex);
        dsmtTabsStore.updateNextTabData(nextTabIndex);
    }

    const handleSubmit = () => {
        console.log(dsmtTabsStore.searchPayload);
    }

    const handleSelectionChange = (index: number) => {
        console.log(index);
        dsmtTabsStore.currentTab = index + 1;
        setSelectedTab(index);
    }

    // const handleCloseTag = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    //     console.log(e.currentTarget.value);
    //     dsmtTabsStore.removeSelectedRow(props.selectedRowValue);
    // }

    const getTableData = () => {
        const currentTabData = dsmtTabsStore?.tabsMap?.get(`tab${dsmtTabsStore?.currentTab}`)
        return currentTabData?.searchResults as TableData
    }

    const getSelectedRowValues = (selectedRowValues: string[] = ['']) => {
        return selectedRowValues.map((rowValue) => {
            <Tag type="gray" filter title={rowValue}>
                {rowValue}
            </Tag>
        })
    }
    return (
        <div className="bx--grid bx--grid--full-width bx--grid--condensed landing-page">
            <div className="bx--row landing-page__banner">
                <div className="bx--col-lg-16 bx--col-md-8 bx--col-sm-4">
                    <Tabs type="container" selected={selectedTab} onSelectionChange={handleSelectionChange}>

                        {dsmtTabsStore.tabsMap.toJSON().map(([key, value], index) => {
                            return <Tab id={key} label={value.label} key={key} disabled={value.disabled}>
                                <SearchTab1 currentTab={key}
                                            selectedRowValue={value.selectedRowValue}
                                            nodes={value.nodeValues as DataNode[]}
                                            selectedTabIndex={selectedTab}
                                            tableSelectionType={value.tableSelectionType}/>
                                <div className="flex-container column">
                                    {
                                        showTree ?
                                            <div>
                                                {/* <TreeExample /> */}
                                            </div>
                                            :
                                            <div className="sample-spacer">
                                                {value.tableSelectionType === 'checkbox' &&
                                                value?.selectedRowValues?.map((rowValue) => {
                                                    <Tag type="gray" filter title={rowValue}>
                                                        {rowValue}
                                                    </Tag>
                                                })
                                                    // getSelectedRowValues(value.selectedRowValues)
                                                }
                                                {value.tableSelectionType === 'radio' && value.selectedRowValue &&
                                                <Tag type="gray" filter title={value.selectedRowValue}>
                                                    {value.selectedRowValue}
                                                </Tag>
                                                }
                                                {value.searchResults && value.searchResults.rows.length > 0 &&
                                                <DsmtTable tableData={value.searchResults as TableData}
                                                           currentTab={key}
                                                           pagination={dsmtTabsStore.paginationData}
                                                           tableSelectionType={value.tableSelectionType}/>
                                                }
                                            </div>
                                    }
                                </div>
                            </Tab>
                        })}
                    </Tabs>
                    {
                        dsmtTabsStore.currentTab === 3 ?
                            <Button kind="primary"
                                    onClick={handleSubmit}
                                    iconDescription="Next"
                                    className="self-align-right"
                                    disabled={!dsmtTabsStore.tabsMap.get(`tab${dsmtTabsStore.currentTab}`)?.selectedRowValues}>Submit
                            </Button>
                            :
                            <Button kind="primary"
                                    onClick={handleNextButtonClick}
                                    iconDescription="Next"
                                    className="self-align-right"
                                    disabled={!dsmtTabsStore.tabsMap.get(`tab${dsmtTabsStore.currentTab}`)?.selectedRowValue}>Next
                            </Button>

                    }
                </div>
            </div>
        </div>
    );
})

export default SearchDsmtHelper;

