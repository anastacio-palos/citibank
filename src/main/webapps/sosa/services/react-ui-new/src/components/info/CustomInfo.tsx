import {Accordion, AccordionItem, Column, Grid, Row} from 'carbon-components-react';
import {Tooltip} from 'carbon-components-react/lib/components/Tooltip/Tooltip';
import React, {Component} from 'react';

interface MyProps {
    id: String;
    name: string;
    key: string;
    description: string;
    sectionTitle: string;
};

class CustomInfo extends Component<MyProps> {

    constructor(props: MyProps) {
        super(props)
        console.log('custom info =', props)
    }

    render() {
        return (
            <div style={{width: '50%'}}>
                <Accordion title={this.props.sectionTitle}>
                    <AccordionItem title={this.props.sectionTitle}>
                        <Grid>
                            <Row>
                                {/* <Column style={{fontWeight:'bold' }} >ID</Column> */}
                                <Column style={{fontWeight: 'bold'}}>Object ID</Column>
                                <Column style={{fontWeight: 'bold'}}>Description</Column>
                            </Row>
                            <Row>
                                <Column> </Column>
                                <Column> </Column>
                                <Column> </Column>
                            </Row>
                            <Row style={{marginTop: '10px'}}>
                                {/* <Column>{this.props.key}</Column> */}
                                <Column><a
                                    href={"../../../app/jspview/react/grc/task-view/" + this.props.id}>{this.props.name}</a></Column>
                                <Column>{this.props.description}</Column>
                            </Row>
                        </Grid>
                    </AccordionItem>
                </Accordion>
            </div>
        )
    }


}

export default CustomInfo;