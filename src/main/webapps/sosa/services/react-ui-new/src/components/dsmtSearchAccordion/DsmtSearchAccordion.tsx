import {AccordionItem} from 'carbon-components-react';
import React from 'react';
import SearchDsmtHelper from '../../helpers/search-dsmt-helper/SearchDsmtHelper';
import {RootStoreProvider} from '../../RootStoreProvider';

const DsmtSearchAccordion = () => {
    return (
        <RootStoreProvider>
            <AccordionItem
                title="Search DSMT">
                <SearchDsmtHelper/>
            </AccordionItem>
        </RootStoreProvider>
    );

};

export default DsmtSearchAccordion;
