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

import {
    createRequest,
    extractContextRootFromDoc,
    extractCSRFTokenFromDoc,
    extractCSRFTokenFromText,
    extractJWTTokenFromDoc,
    extractJWTTokenFromText,
    setApiContext
} from "./apiHelper";
import {ConfigurationTextModel} from "./configurationText";

// request types
const GET = 'GET';
const POST = 'POST';
const PUT = 'PUT';

//const DELETE = 'DELETE';

export class Api {

    constructor() {
        if (process.env.NODE_ENV !== 'development') {
            const csrfToken = extractCSRFTokenFromDoc();
            let contextRoot = extractContextRootFromDoc();
            if (contextRoot) {
                contextRoot = `${contextRoot}/`;
            } else {
                contextRoot = '/'
            }
            const jwtToken = extractJWTTokenFromDoc();
            setApiContext(contextRoot, csrfToken, jwtToken);
        }
    }

    // in development environment we automatically logon to OpenPages
    async devLogon(): Promise<void> {
        const username = process.env.REACT_APP_USERNAME;
        const password = process.env.REACT_APP_PASSWORD;

        if (!username || !password) {
            throw Error("Please specify logon credentials when starting the dev server");
        }

        // logon
        const headers: any = {'Content-Type': 'application/x-www-form-urlencoded'};
        const data = `j_username=${username}&j_password=${password}`;
        await createRequest('/j_security_check', POST, null, data, headers);
        const ssoHeaders: any = {Accept: 'text/html'};
        await createRequest('/singlesignon.do', GET, null, null, ssoHeaders);

        // get index.html
        const pageHeaders: any = {Accept: 'text/html'};
        const page = await createRequest('openpages/app/services/helperapp/citi-helpers/index.html', GET, null, null, pageHeaders); // used to obtain CSRF token in dev environment

        // extract tokens
        const csrfToken = extractCSRFTokenFromText(page);
        const jwtToken = extractJWTTokenFromText(page);
        setApiContext('/', csrfToken, jwtToken);
    }

    async getConfigurationText() {
        return createRequest('api/configuration/adminMode', GET);
    }

    async getAuditableEntityInit() {
        return createRequest('openpages/app/services/api/audEntityDsmtLinkApp/init?resourceId=22518', GET);
    }

    async getAuditHelperInit() {
        return createRequest('/openpages/app/services/api/auditDsmtLinkApp/init?resourceId=22256', GET);
    }

    async fetchSearchResults(payload: any) {
        return createRequest('openpages/app/services/api/audEntityDsmtLinkApp/searchDSMT', POST, {}, payload);
    }

    async fetchAuditSearchResults(searchTerm: string) {
        return createRequest(`/openpages/app/services/api/auditDsmtLinkApp/searchDSMTLinks?searchText=${searchTerm}`, GET);
    }

    async getAdminMode(): Promise<boolean> {
        return createRequest('api/configuration/adminMode', GET);
    }

    async setAdminMode(enable: boolean): Promise<void> {
        return createRequest('api/configuration/adminMode', PUT, {enable});
    }
}
