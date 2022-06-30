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
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import {AppStore} from './AppStore';
import {RootStoreProvider} from './RootStoreProvider';
import {getContextRoot} from './api/apiHelper';
import AuditableEntityHelper from './helpers/auditableEntityHelper/AuditableEntityHelper';
import AuditHelper from './components/auditHelper/AuditHelper';

const SearchDsmtHelper = lazy(() => import('./helpers/search-dsmt-helper/SearchDsmtHelper'));

interface Props {
    appStore: AppStore;
}

const renderLoader = () => <p>Loading</p>;

function getLink(link: string) {
    return `${getContextRoot()}app/services/helperapp/citi-helpers/${link}`;
}

function getPath(path: string) {
    return `${getContextRoot()}app/services/helperapp/citi-helpers/${path}`;
}

export const AppRoutes = observer((props: Props) => {
    const {} = props.appStore;

    return (
        <>
            <React.Suspense fallback={<p>Loading...</p>}>
                <Router>
                    <Switch>
                        <RootStoreProvider>
                            <Route path={getPath("auditableEntity")} component={() => <AuditableEntityHelper/>}/>
                            <Route path={getPath("search")} component={() => <SearchDsmtHelper/>}/>
                            <Route path={getPath("auditDSMT")} component={() => <AuditHelper/>}/>
                        </RootStoreProvider>
                        {/* <Route path={getPath("/")}>Welcome!</Route> */}
                    </Switch>
                </Router>
            </React.Suspense>
        </>
    )
})
