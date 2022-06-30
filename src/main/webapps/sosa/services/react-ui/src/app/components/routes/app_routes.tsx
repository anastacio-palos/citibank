import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import AuditScopingHelper from '../../helpers/audit-scoping-helper';
import NestingTable from '../../components/nestingTable/NestingTable';
import AuditableEntityHelper from '../auditableEntityHelper/AuditableEntityHelper';
import SearchDsmtHelper from '../../helpers/search-dsmt-helper/SearchDsmtHelper';
import AuditHelper from '../auditHelper/AuditHelper';

class ApplicationRoutes extends React.Component {
    render() {
        return (
            <main>
                <BrowserRouter>
                    <Switch>
                        <Route path="/auditScoping/:auditID" component={AuditScopingHelper}/>
                        <Route path="/search" component={SearchDsmtHelper}/>
                        <Route path="/nestingTable" component={NestingTable}/>
                        <Route path="/citi-helpers/dsmthelpers/auditableEntity" component={AuditableEntityHelper}/>
                        <Route path="/citi-helpers/dsmthelpers/audit" component={AuditHelper}/>
                    </Switch>
                </BrowserRouter>
            </main>
        )
    }
}

export default ApplicationRoutes;
