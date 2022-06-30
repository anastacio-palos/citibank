/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * OpenPages GRC Platform (PID: 5725-D51)
 *
 * © Copyright IBM Corporation  2021 - CURRENT_YEAR. All Rights Reserved.
 *
 * US Government Users Restricted Rights- Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 *******************************************************************************/

import React, {Component, MouseEvent, FormEvent, ChangeEvent} from 'react';

import {Button, Form, TextInput, Dropdown} from 'carbon-components-react';
import * as API from './tfuiFieldApi';
import {FieldsExampleStore} from './FieldsExampleStore';
import './FieldsExample.scss';

type Dictionary = {
    [key: string]: any;
}

type Props = {
    fieldsExampleStore?: FieldsExampleStore;
}

type State = {
    formKey: number;
    fieldValues: Dictionary;
}

// only push these fields back to open pages
const UPDATE_KEYS_SET = new Set(['System Fields:Name']);

export class FieldsExample extends Component<Props, State> {

    resourceId?: string;

    state = {
        formKey: 0,
        fieldValues: {} as Dictionary
    }

    constructor(props: any) {
        super(props);

        // get resource id (recommend use router like react-router)
        const params = window.location.search.replace('?', '').split('&');
        const param = params.find((p) => p.indexOf('resourceId') >= 0);
        this.resourceId = param?.split('=')[1];

        if (this.resourceId) {
            API.listen(this.onMessage.bind(this));
        }
    }

    // process incoming message from OpenPages
    onMessage(message: API.TaskViewMessage) {
        if (message.type === API.TaskViewMessageType.taskViewFieldValues) {
            this.onFieldValues(message as API.TaskViewFieldValuesMessage);
        }
    }

    // setting state from incoming field values.
    onFieldValues(message: API.TaskViewFieldValuesMessage) {
        const fieldValues = {...this.state.fieldValues};
        for (var i = 0; i < message.fields.length; i++) {
            var field = message.fields[i];
            fieldValues[field.name] = field.value;
        }
        this.setState({formKey: this.state.formKey + 1, fieldValues})
    }

    onGetFieldsClick(e: MouseEvent) {
        e.preventDefault();
        if (this.resourceId) {
            API.getFields(this.resourceId);
        }
    }

    onSubmit(e: FormEvent) {
        e.preventDefault();
        if (this.resourceId) {
            const message: API.SetTaskViewFieldsMessage = {
                type: API.TaskViewMessageType.setTaskViewFields,
                resourceId: this.resourceId,
                fields: []
            }
            const keys = Array.from(UPDATE_KEYS_SET);
            for (const key of keys) {
                message.fields.push({
                    name: key,
                    value: this.state.fieldValues[key]
                });
            }
            API.sendMessage(message);
        }
    }

    onFieldChange = (e: ChangeEvent) => {
        const fieldValues = {...this.state.fieldValues};
        fieldValues[e.target.id] = (e.target as any).value;
        this.setState({fieldValues})
        UPDATE_KEYS_SET.add(e.target.id)
    }

    initialSelectedItem = (id: string) => {
        return this.getItems().find((x: any) => x.id === id)
    }

    // This is the sample method to populate dropdown items with the value of the field.

    getItems() {
        return [
            {
                id: '1694',
                value: '1694',
                text: 'Effective'
            },
            {
                id: '1695',
                value: '1695',
                text: 'Ineffective'
            },
            {
                id: '1696',
                value: '1696',
                text: 'Not Determined'
            }
        ]
    }

    getDescriptionItems() {
        return ['Description 1', 'Description 2'];
    }

    render() {
        const onSubmit = this.onSubmit.bind(this);
        const onFieldChange = this.onFieldChange.bind(this);
        const onGetFieldsClick = this.onGetFieldsClick.bind(this);
        const items = this.getItems();
        const descriptionItems = this.getDescriptionItems();

        return (
            <div className="op-example-fields">
                <p>
                    Resource Id: {this.resourceId}
                </p>

                <br/>
                <br/>

                <Form id="sampleForm" key={this.state.formKey} onSubmit={onSubmit}>
                    <TextInput
                        labelText="Name"
                        id="System Fields:Name"
                        defaultValue={this.state.fieldValues['System Fields:Name']}
                        onChange={onFieldChange}
                    />
                    <Dropdown
                        id="System Fields:Description"
                        ariaLabel="Description Options"
                        titleText="Description Options"
                        label="Description Options"
                        onChange={({selectedItem}) => {
                            const fieldValues = {...this.state.fieldValues};
                            fieldValues['System Fields:Description'] = selectedItem;
                            this.setState({fieldValues})
                            UPDATE_KEYS_SET.add('System Fields:Description')
                        }}
                        items={descriptionItems}
                        selectedItem={this.state.fieldValues['System Fields:Description']}
                    />
                    <Dropdown
                        id="OPSS-Ctl:Design Effectiveness"
                        titleText="Design Effectiveness"
                        ariaLabel="Design Effectiveness"
                        label="Design Effectiveness"
                        onChange={({selectedItem}) => {
                            const fieldValues = {...this.state.fieldValues};
                            fieldValues['OPSS-Ctl:Design Effectiveness'] = [selectedItem!.id];
                            this.setState({fieldValues})
                            UPDATE_KEYS_SET.add('OPSS-Ctl:Design Effectiveness')

                        }}
                        items={items}
                        itemToString={item => (item ? item.text : "")}
                        selectedItem={this.initialSelectedItem(this.state.fieldValues['OPSS-Ctl:Design Effectiveness'] && this.state.fieldValues['OPSS-Ctl:Design Effectiveness'][0])}
                    />
                    <Button type="button" onClick={onGetFieldsClick}>
                        Get Task View Fields
                    </Button>
                    <Button type="submit">
                        Set Task View Fields
                    </Button>
                </Form>
            </div>
        )
    }
}

// Json for all the field types and its values.Its a sample Json-- >
/**
 {
		"type": "taskViewFieldValues",
		"resourceId": "10835",
		"fields": [
			{
				"name": "System Fields:Name",
				"value": "Africa and Middle East",
				"readOnly": false
			},
			{
				"name": "Helper_Field_Group:Helper_Field",
				"value": {
					"enabled": true,
					"forwardUrl": "https://www.example.com",
					"popupAttributes": "height=550,width=890,menubar=no,status=no,toolbar=no,scrollbars=yes,resizable=yes",
					"titleText": "View Policy"
				},
				"readOnly": true
			},
			{
				"name": "All_FieldTypes:f_BESelector",
				"value": "Employees",
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_GroupSelector",
				"value": [
					{
						"id": 5,
						"userName": "OPAdministrators",
						"firstName": "",
						"lastName": "",
						"email": "OPAdministrators@openpages.local",
						"displayName": "OPAdministrators",
						"isEnabled": true,
						"isEditable": true,
						"type": "group"
					}
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_MultiValuedGroupSelector",
				"value": [
					{
						"id": 5,
						"userName": "OPAdministrators",
						"firstName": "",
						"lastName": "",
						"email": "OPAdministrators@openpages.local",
						"displayName": "OPAdministrators",
						"isEnabled": true,
						"isEditable": true,
						"type": "group"
					},
					{
						"id": 8,
						"userName": "Workflow, Reporting and Others",
						"firstName": "",
						"lastName": "",
						"email": "others@openpages.local",
						"displayName": "Workflow, Reporting and Others",
						"isEnabled": true,
						"isEditable": false,
						"type": "group"
					}
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_MultiValuedUserORGroupSelector",
				"value": [
					{
						"id": 6,
						"userName": "OpenPagesAdministrator",
						"firstName": "System",
						"lastName": "Administrator",
						"email": "OpenPagesAdministrator@openpages.local",
						"displayName": "System Administrator - OpenPagesAdministrator",
						"isEnabled": true,
						"isEditable": true,
						"type": "user"
					},
					{
						"id": 5,
						"userName": "OPAdministrators",
						"firstName": "",
						"lastName": "",
						"email": "OPAdministrators@openpages.local",
						"displayName": "OPAdministrators",
						"isEnabled": true,
						"isEditable": true,
						"type": "group"
					}
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_RichText",
				"value": "<p dir="ltr">this is the rich text example

				\n",
"readOnly": false
},
			{
				"name": "All_FieldTypes:f_MultiValuedUserSelector",
				"value": [
					{
						"id": 6,
						"userName": "OpenPagesAdministrator",
						"firstName": "System",
						"lastName": "Administrator",
						"email": "OpenPagesAdministrator@openpages.local",
						"displayName": "System Administrator - OpenPagesAdministrator",
						"isEnabled": true,
						"isEditable": true,
						"type": "user"
					},
					{
						"id": 1076,
						"userName": "opuser",
						"firstName": "OP",
						"lastName": "User",
						"email": "op.user@domain.com",
						"displayName": "OP User - opuser",
						"isEnabled": true,
						"isEditable": true,
						"type": "user"
					}
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_UserDropdown",
				"value": [
					{
						"id": 6,
						"userName": "OpenPagesAdministrator",
						"firstName": "System",
						"lastName": "Administrator",
						"email": "OpenPagesAdministrator@openpages.local",
						"displayName": "System Administrator - OpenPagesAdministrator",
						"isEnabled": true,
						"isEditable": true,
						"type": "user"
					}
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_URL",
				"value": "https://www.google.com",
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_UserORGroupSelector",
				"value": [
					{
						"id": 1076,
						"userName": "opuser",
						"firstName": "OP",
						"lastName": "User",
						"email": "op.user@domain.com",
						"displayName": "OP User - opuser",
						"isEnabled": true,
						"isEditable": true,
						"type": "user"
					}
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_UserSelector",
				"value": [
					{
						"id": 1076,
						"userName": "opuser",
						"firstName": "OP",
						"lastName": "User",
						"email": "op.user@domain.com",
						"displayName": "OP User - opuser",
						"isEnabled": true,
						"isEditable": true,
						"type": "user"
					}
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_boolean",
				"value": "true",
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_computed",
				"readOnly": true
			},
			{
				"name": "All_FieldTypes:f_computed2",
				"readOnly": true
			},
			{
				"name": "All_FieldTypes:f_enum2_MultiList",
				"value": [
					"8402",
					"8403"
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_enum1_single",
				"value": [
					"8416"
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_decimal",
				"value": 10.332,
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_enum2_singlelist",
				"value": [
					"8409"
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_integer",
				"value": 10,
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_reportfragment1",
				"readOnly": true
			},
			{
				"name": "All_FieldTypes:f_enum2_multi",
				"value": [
					"8420",
					"8421"
				],
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_simple_TextArea",
				"value": "this is text area",
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_simple",
				"value": "this is simple string",
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_date",
				"value": "1/21/2020",
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_currency_5f_ex",
				"value": {
					"lc": "USD",
					"la": "12.00",
					"er": "1.00",
					"ls": "$"
				},
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_currency_5f",
				"value": {
					"lc": "",
					"la": "",
					"er": "",
					"ls": ""
				},
				"readOnly": false
			},
			{
				"name": "All_FieldTypes:f_currency_2f",
				"value": {
					"lc": "",
					"la": "",
					"er": "",
					"ls": ""
				},
				"readOnly": false
			}
		]
	}

 */

