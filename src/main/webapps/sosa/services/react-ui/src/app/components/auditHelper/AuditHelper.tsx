import {Search20} from '@carbon/icons-react';
import {
    Accordion,
    AccordionItem,
    Button,
    ButtonSet,
    DataTable,
    Search,
    Tab,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableHeader,
    TableRow,
    Tabs
} from 'carbon-components-react';
import {observer} from 'mobx-react-lite';
import React, {ChangeEvent, useState} from 'react';
import DsmtTable from '../../helpers/search-dsmt-helper/search-tabs/data-table/DsmtTable';
import {RootStoreProvider, StoreTypes, useStore} from '../../providers/RootStoreProvider';
import {ExistingDsmtAccordionDataStore} from '../../stores/existingDsmtAccordion/existingDsmtAccordionDataStore';
import {ITableProps} from '../../stores/nestingTable/nestingTableDataStore';
import {ExistingDsmtTable} from '../existingDsmtAccordion/ExistingDsmtAccordion';
import GeneralDetailsAccordion from '../generalDetailsAccordion/GeneralDetailsAccordion';
import CustomHeader, {ICustomHeaderProps} from '../header/CustomHeader';
import {HelperNames} from '../nestingTable/mockApi/mockApi';

const AuditHelper: React.FC = () => {
    const props: ICustomHeaderProps = {
        helperName: HelperNames.auditHelper,
        url: 'http://opservflin1811.fyre.ibm.com:10108/openpages/app/jspview/auditDsmtLinkApp/init?resourceId=22256'
    }

    return (
        <RootStoreProvider>
            <CustomHeader {...props} />
            <h2 style={{marginTop: "3rem", marginBottom: "2rem"}}>Audit Helper</h2>
            <GeneralDetailsAccordion/>
            <Tabs scrollIntoView={false} type='container'>
                <Tab
                    href="#"
                    id="tab-1"
                    label="Existing DSMT Links"
                >
                    <ExistingDsmtTable/>
                    <NewDsmtLinksAccordion/>
                </Tab>
                <Tab
                    href="#"
                    id="tab-2"
                    label="Search"
                >
                    <AuditHelperSearch/>
                    <AuditHelperTable/>
                </Tab>
            </Tabs>
        </RootStoreProvider>
    );
}

const AuditHelperSearch = () => {
    const [searchInput, setSearchInput] = useState<string | number>('');

    const handleSearchInput = (event: ChangeEvent<{ value: string | number; }>) => {
        const currentTarget = event.currentTarget;

        if (currentTarget) {
            setSearchInput(currentTarget.value);
        } else {
            setSearchInput('');
        }
    }

    return (
        <div className="bx--col-lg-8 bx--col-md-4 bx--col-sm-2 flex-container sample-spacer">
            <Search
                id="audit-helper-search" labelText='Search' value={searchInput}
                onChange={(event) => handleSearchInput(event)}/>
            <Button kind="tertiary" iconDescription="Search" hasIconOnly onClick={() => {
            }}>
                <Search20/>
            </Button>
        </div>
    );
}

const AuditHelperTable = () => {
    return (
        <div className="flex-container column">
            <div className="sample-spacer">
                <DsmtTable/>
            </div>
        </div>
    );
}

const NewDsmtLinksAccordion = observer(() => {
    return (
        <Accordion style={{marginTop: "1rem"}} align='start'>
            <AccordionItem title="New DSMT Links">
                <NewDsmtLinksTable/>
                <NewDsmtLinksControls/>
            </AccordionItem>
        </Accordion>
    )
});

const NewDsmtLinksTable = observer(() => {
    const dataStore = useStore(StoreTypes.ExistingDsmtAccordionDataStore) as ExistingDsmtAccordionDataStore;

    return (
        <DataTable rows={dataStore.rows} headers={dataStore.headers}>
            {({
                  rows,
                  headers,
                  getHeaderProps,
                  getRowProps,
                  getTableProps,
              }: ITableProps) => (
                <TableContainer>
                    <Table {...getTableProps()} size='normal'>
                        <TableHead>
                            <TableRow>
                                {headers.map((header) => (
                                    <TableHeader {...getHeaderProps({header})}>
                                        {header.header}
                                    </TableHeader>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow {...getRowProps({row})}>
                                    {row.cells?.map((cell) => (
                                        <TableCell key={cell.id}>{cell.value}</TableCell>
                                    ))}
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </DataTable>
    );
});

const NewDsmtLinksControls = () => {

    return (
        <div>
            <ButtonSet>
                <Button kind="secondary">
                    Ignore All
                </Button>
                <Button kind="primary">
                    Accept All
                </Button>
            </ButtonSet>
        </div>
    )
}

export default AuditHelper;
