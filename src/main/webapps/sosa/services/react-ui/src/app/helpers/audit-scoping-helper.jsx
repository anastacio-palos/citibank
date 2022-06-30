import {Button, Tab, Tabs, ToastNotification} from 'carbon-components-react';
import React, {Component} from 'react';
import CustomHeader from '../components/header/CustomHeader';
import CustomTree from '../components/tree/custom_tree'
import CustomInfo from '../components/info/CustomInfo'
import aeDataService from '../service/audit-entity-helper-service';
import Demo from '../components/tree/demo_tree'
import CustomTreeFolder from '../components/tree/custom_tree_folder';
import CustomGrid from '../components/grid/CustomGrid';


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

                </div>
                <CustomGrid service={"/openpages/app/jspview/auditScopingApp/audit/" + this.state.props.id}
                            auditid={this.state.props.id}></CustomGrid>

                {/* <div  style={{bottom:'0',position:'absolute', width:'98%', marginBottom:"10px"}}>
            <div style={{float: 'right', marginRight: '5px'}}>
              <Button kind="primary"  onClick={this.onSave}>Save</Button>
            </div>
        </div> */}
            </div>
        )

    }
}

export default AuditScopingHelper