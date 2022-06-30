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
import React, {lazy} from 'react';
import {observer} from "mobx-react-lite"
import {BrowserRouter as Router, Route, Link as RouterLink, Switch} from 'react-router-dom';
import {Grid, Row, Column} from 'carbon-components-react';
import {ExamplesStore} from './ExamplesStore';
import {TreeExample} from './tree/TreeExample';
import {TableExample} from './table/TableExample';
import {FieldsExample} from './fields/FieldsExample';
import {AdminModeExample} from './admin/AdminModelExample';
import {AppStore} from '../AppStore';
import {RootStoreProvider} from '../RootStoreProvider';
import {getContextRoot} from '../api/apiHelper';
import AuditableEntityHelper from '../helpers/auditableEntityHelper/AuditableEntityHelper';

const SearchDsmtHelper = lazy(() => import('./../helpers/search-dsmt-helper/SearchDsmtHelper'));

interface Props {
    appStore: AppStore;
}

const renderLoader = () => <p>Loading</p>;

function onSelectExample(examplesStore: ExamplesStore, id: string, e: any) {
    examplesStore.selectExample(id);
}

function getLink(link: string) {
    return `${getContextRoot()}app/services/helperapp/citi-helpers${link}`;
}

function getPath(path: string) {
    return `${getContextRoot()}app/services/helperapp/citi-helpers${path}`;
}

export const Examples = observer((props: Props) => {
    const {
        examplesStore,
        examplesStore: {adminModelExampleStore, treeExampleStore, tableExampleStore, fieldsExampleStore}
    } = props.appStore;

    return (
        <React.Fragment>
            <Grid fullWidth>
                <Row>
                    <Column>
                        {/* <h1>React Carbon Helper</h1> */}
                    </Column>
                </Row>
                <Row className='op-example-container'>
                    <Column className="op-example-links">
                        <React.Suspense fallback={<p>Loading...</p>}>
                            <Router>
                                <div>
                                    <Switch>
                                        <Route path={getPath("/tree")}
                                               component={() => <TreeExample treeExampleStore={treeExampleStore}/>}/>
                                        <Route path={getPath("/table")}
                                               component={() => <TableExample tableExampleStore={tableExampleStore}/>}/>
                                        <Route path={getPath("/fields")} component={() => <FieldsExample
                                            fieldsExampleStore={fieldsExampleStore}/>}/>
                                        <Route path={getPath("/admin")} component={() => <AdminModeExample
                                            adminModelExampleStore={adminModelExampleStore}/>}/>
                                        <RootStoreProvider>
                                            <Route path={getPath("/auditableEntity")}
                                                   component={() => <AuditableEntityHelper/>}/>
                                            <Route path={getPath("/search")} component={() => <SearchDsmtHelper/>}/>
                                        </RootStoreProvider>
                                        {/* <Route path={getPath("/")}>Welcome!</Route> */}
                                    </Switch>
                                </div>
                            </Router>
                        </React.Suspense>
                    </Column>
                </Row>
            </Grid>
        </React.Fragment>
    )
})


