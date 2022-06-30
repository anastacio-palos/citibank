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

import React from 'react';
import ReactDOM from 'react-dom';
import {AppContainer} from './App';
import * as serviceWorker from './serviceWorker';
import {AppStore} from './AppStore';
import './index.scss';

const appStore = new AppStore();
appStore.bootstrap();

ReactDOM.render(
    <React.StrictMode>
        <AppContainer appStore={appStore}/>
    </React.StrictMode>,
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
