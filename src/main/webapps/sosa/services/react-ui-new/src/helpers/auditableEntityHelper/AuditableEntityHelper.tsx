import React, {useEffect} from 'react';
import GeneralDetailsAccordion from '../../components/generalDetailsAccordion/GeneralDetailsAccordion';
import CustomHeader from '../../components/header/CustomHeader';
import DsmtSearchAccordion from '../../components/dsmtSearchAccordion/DsmtSearchAccordion';
import {Accordion, AccordionItem} from 'carbon-components-react';
import {AdminModeExample} from '../../examples/admin/AdminModelExample';
import {StoreTypes, useAuditableEntityDsmtStore, useStore} from '../../RootStoreProvider';
import {observer} from 'mobx-react-lite';
import {ExistingDsmtAccordion} from '../../components/existingDsmtAccordion/ExistingDsmtAccordion';

const AuditableEntityHelper = observer(() => {

    const auditableEntityDsmtStore = useAuditableEntityDsmtStore();
    const {loadInitData} = auditableEntityDsmtStore;

    useEffect(() => {
        loadInitData();
    }, []);

    return (
        <>
            <CustomHeader headerInfo={auditableEntityDsmtStore.headerInfo}/>
            <h2 style={{marginTop: "3rem", marginBottom: "2rem"}}>Auditable Entity</h2>
            {<GeneralDetailsAccordion generalDetails={auditableEntityDsmtStore.generalInfo}/>}
            <Accordion align='start'>
                <AccordionItem title="Existing DSMT Links">
                    <ExistingDsmtAccordion
                        headers={auditableEntityDsmtStore.existingDSMTLinksGridInfo.headers}
                        rows={auditableEntityDsmtStore.existingDSMTLinksGridInfo.rows}
                    />
                </AccordionItem>
                <DsmtSearchAccordion/>
            </Accordion>
        </>
    );
});

export default AuditableEntityHelper;
