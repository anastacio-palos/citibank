import React, {useState} from 'react';
import {Accordion, AccordionItem, TextInput} from 'carbon-components-react';
import {observer} from 'mobx-react-lite';
import {GeneralDetailsEntity} from '../../api/models/auditableEntityInitModel';

interface MyProps {
    generalDetails: GeneralDetailsEntity[];
    children?: any;
}

export const defaultGeneralDetailsInfo: GeneralDetailsEntity[] = [{
    editable: false,
    fieldName: '',
    fieldValue: '',
    required: false
}]

const GeneralDetailsAccordion = observer((props: MyProps) => {

    // No need for a setIsOpen() since this only applies to default state
    const [isOpen] = useState(true);

    return (
        <Accordion style={{marginBottom: "3rem"}} align='start'>
            <AccordionItem
                title="General Details"
                open={isOpen}>
                <Container records={props?.generalDetails}/>
            </AccordionItem>
            {props.children}
        </Accordion>
    );
});

interface IContainerProps {
    records: GeneralDetailsEntity[]
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
            {records?.map((record) => (
                <RecordItem
                    key={record.fieldName}
                    fieldName={record.fieldName}
                    fieldValue={record.fieldValue}
                    editMode={false}
                    required={record.required}
                    // isEditable={record.isEditable}
                    // isRequired={record.isRequired}
                />
            ))}
        </div>
    );
});

interface IRecordItem {
    editMode: boolean;
    fieldName: string;
    fieldValue: string;
    editable?: boolean;
    required: boolean;
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
