import React, {useState} from 'react';
import {Accordion, AccordionItem, TextInput} from 'carbon-components-react';
import {RootStoreProvider, StoreTypes, useStore} from '../../providers/RootStoreProvider';
import {observer} from 'mobx-react-lite';
import {
    GeneralDetailsAccordionStore,
    IRecord
} from '../../stores/generalDetailsAccordionStore/generalDetailsAccordionStore';

const GeneralDetailsAccordion = observer(() => {
    const generalDetailsStore = useStore(StoreTypes.GeneralDetailsAccordionStore) as GeneralDetailsAccordionStore;

    // No need for a setIsOpen() since this only applies to default state
    const [isOpen] = useState(true);

    return (
        <RootStoreProvider>
            <Accordion style={{marginBottom: "3rem"}} align='start'>
                <AccordionItem
                    title="General Details"
                    open={isOpen}>
                    <Container records={generalDetailsStore.records}/>
                </AccordionItem>
            </Accordion>
        </RootStoreProvider>
    );
});

interface IContainerProps {
    records: IRecord[]
}

const Container = observer(({records}: IContainerProps) => {
    const style = {
        display: 'grid',
        gridTemplateColumns: '33% 33% 33%',
        rowGap: '2rem',
        columnGap: '4rem'
    }

    return (
        <div style={style}>
            {records.map((record, index) => (
                <RecordItem
                    key={index}
                    fieldName={record.fieldName}
                    fieldValue={record.fieldValue}
                    editMode={false}
                    editable={record.editable}
                    required={record.required}
                />
            ))}
        </div>
    );
});

interface IRecordItem extends IRecord {
    editMode: boolean;
}

const RecordItem = ({fieldName, fieldValue, editMode, editable, required}: IRecordItem) => {
    const style = {
        display: 'grid',
        gridTemplateColumns: '100%',
        gridTemplateRows: '40% 60%'
    }

    if (editMode) {
        return (
            <TextInput
                id={fieldName}
                labelText={fieldName}
                value={fieldValue}
                light>
            </TextInput>
        );
    }

    return (
        <div style={style}>
            <p className="bx--label">{fieldName}</p>
            <p>{fieldValue}</p>
        </div>
    );

};

export default GeneralDetailsAccordion;
