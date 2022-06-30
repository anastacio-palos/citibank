import {Button, Tab, Tabs} from 'carbon-components-react';
import React, {Component} from 'react';
import CustomHeader from '../components/header/CustomHeader';
import CustomTree from '../components/tree/custom_tree'
import CustomInfo from '../components/info/CustomInfo'
import aeDataService from '../service/audit-entity-helper-service';
import Demo from '../components/tree/demo_tree'
import CustomTreeFolder from '../components/tree/custom_tree_folder';


class AuditScopingHelper extends Component {

    headerProps = {
        headerTitle: 'Audit Scoping Helper',
        hideSearch: true,
        hideNotificationBell: true,
        hideAppSwitcher: true,
        hideUserIcon: false,
        userInfo: {
            userName: 'Test User'
        },
        aboutInfo: {
            title: 'string',
            version: 'string',
            licenseInfo: 'string',
            helperInfo: 'string',
        },
    }

    infoProps = {}

    constructor(props) {
        super(props);
        this.onSave = this.onSave.bind(this);
        this.state = {checkedKeys: []};
        this.state = {
            audit: {}, aes: [], props: props
        }

    }

    componentDidMount() {
        aeDataService.retrieveAuditData(this.state.props.id).then(response => {
            console.log("audit data : " + JSON.stringify(response.data))
            this.infoProps = {
                name: response.data.title,
                key: response.data.key,
                description: response.data.description,
                sectionTitle: response.data.title,
                id: response.data.key
            }
            this.setState({audit: response.data})
        });

        aeDataService.retrieveAuditableEntity(this.state.props.id).then(response => {
            console.log("tree data : " + JSON.stringify(response.data))
            this.setState({aes: response.data})
        });

    }

    dataToSave = [];

    callbackFunction = (aeData, checkedKeys) => {
        console.log('-------------------')
        console.log(aeData)
        console.log(checkedKeys)
        // this.setState({checkedKeys: checkedKeys})

        let index = this.dataToSave.findIndex(akey => akey.ae === aeData.key);
        if (index > -1) {
            this.dataToSave.splice(index, 1);
        }

        let temp = {
            key: aeData.key,
            aeData: aeData,
            scopedOut: checkedKeys
        }

        this.dataToSave.push(temp);

        console.log('final----', this.dataToSave)

    }


    auditEntityTransformer = (data) => {
    }


    onSave() {
        aeDataService.saveData(this.state.props.id, this.dataToSave.map(a => {
                let aes = {
                    id: a.key,
                    rejections: a.scopedOut,
                    aes: a.aeData
                }
                return aes;
            }
        ));
    }

    getHyperLink(obj) {

        return <a href={'../../../app/jspview/react/grc/task-view/' + obj.key}>{obj.title}</a>
    }

    render() {
        return (
            <div>
                <CustomHeader {...this.headerProps}/>
                <div style={{marginTop: `40px`}}>
                    {/* <CustomInfo sectionTitle={this.state.audit.title} key={this.state.props.id} name={this.state.audit.title} description={this.state.audit.description}></CustomInfo> */}
                    <CustomInfo {...this.infoProps}></CustomInfo>

                    <Tabs>
                        <Tab key={this.state.audit.key} label={this.getHyperLink(this.state.audit)}>
                            <Demo parentCallback={this.callbackFunction}
                                  service={"/openpages/app/jspview/auditScopingApp/audit/tree/" + this.state.props.id}
                                  key={this.state.audit.key}></Demo>

                        </Tab>
                        {this.state.aes.map(ae =>
                            <Tab key={ae.key} label={this.getHyperLink(ae)}>
                                {/* TODO - pass in the aeid */}
                                <div style={{overflowY: `scroll`}}>
                                    <Demo parentCallback={this.callbackFunction}
                                          service={"/openpages/app/jspview/auditScopingApp/aes/tree/" + ae.key + "/" + this.state.props.id}
                                          key={ae.key}></Demo>
                                </div>
                            </Tab>
                        )}

                    </Tabs>

                </div>


                <div style={{bottom: '0', position: 'absolute', width: '98%', marginBottom: "10px"}}>
                    <hr></hr>
                    <div style={{float: 'right'}}>
                        <Button kind="secondary" onClick={this.onSave}>Save</Button>
                    </div>
                </div>
            </div>
        )

    }
}

export default AuditScopingHelper