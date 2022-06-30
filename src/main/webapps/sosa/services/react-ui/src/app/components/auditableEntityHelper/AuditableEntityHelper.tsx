import React from 'react';
import GeneralDetailsAccordion from '../generalDetailsAccordion/GeneralDetailsAccordion';
import CustomHeader, {ICustomHeaderProps} from '../header/CustomHeader';
import {RootStoreProvider} from '../../providers/RootStoreProvider';
import {ExistingDsmtAccordion} from '../existingDsmtAccordion/ExistingDsmtAccordion';
import DsmtSearchAccordion from '../dsmtSearchAccordion/DsmtSearchAccordion';
import {Accordion} from 'carbon-components-react';
import {HelperNames} from '../nestingTable/mockApi/mockApi';

const AuditableEntityHelper = () => {
    const props: ICustomHeaderProps = {
        helperName: HelperNames.auditHelper,
        url: 'http://opservflin1811.fyre.ibm.com:10108/openpages/jspview/audEntityDsmtLinkApp/init?resourceId=3002'
    }

    return (
        <RootStoreProvider>
            <CustomHeader {...props}/>
            <h2 style={{marginTop: "3rem", marginBottom: "2rem"}}>Auditable Entity Helper</h2>
            <GeneralDetailsAccordion/>
            <Accordion align='start'>
                <ExistingDsmtAccordion/>
                <DsmtSearchAccordion/>
            </Accordion>
        </RootStoreProvider>
    );
}

export default AuditableEntityHelper;
