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

import axios, {AxiosRequestConfig, AxiosPromise} from 'axios';
import {AlertModel} from './models/alertModel';

const qs = require('qs');

let CONTEXT_PATH: string = "/"; // context root of application, may be "", "/openpages", or "/xxx" (customer deployment)
let CSRF_TOKEN = "";
let JWT_TOKEN = "";

export function getContextRoot(): string {
    return CONTEXT_PATH;
}

export function setApiContext(contextPath: string, csrfToken: string, jwtToken: string) {
    CONTEXT_PATH = contextPath;
    CSRF_TOKEN = csrfToken;
    JWT_TOKEN = jwtToken;

    axios.interceptors.request.use((config) => {
        // add auth header
        const newHeaders = config.headers;
        newHeaders.authorization = JWT_TOKEN;
        config.headers = newHeaders;

        // csrf token
        if (config.method === 'post' || config.method === 'put' || config.method === 'delete') {
            config.url = appendCSRFToken(config.url || "");
        }

        return config;
    }, error => Promise.reject(error));
}

function parseErrorBlob(error: any): Promise<any> {
    return new Promise((resolve) => {
        const reader = new FileReader();
        reader.onload = () => {
            resolve(reader.result);
        };
        reader.readAsText(error.response.data);
    });
}

function constructAlert(response: any): AlertModel {
    const {data} = response;
    const alert: AlertModel = {
        type: 'error',
        httpStatusCode: response.status,
        httpStatusText: response.statusText
    };
    if (data) {
        alert.message = data.message;
        alert.code = data.errorCode;
        alert.title = (data.errorCode !== undefined) ? data.errorCode : '';
        alert.timestamp = data.timestamp;
    } else {
        alert.message = 'Unexpected error'; // TODO need general OP error/code here
    }
    return alert;
}

function convertToAlert(error: any): AlertModel {

    const {response} = error;

    if (!response || !response.headers) {

        //The message `Cannot read property 'status' of undefined` is generated when axios tries to access the status of an undefined response.
        //An undefined response is typically (always?) a Network error and it's more useful to say that.
        let message = 'Network error';

        if (!error?.message.includes('\'status\'')) { //just compare on status since the browser message may be translated
            message = `Unexpected error - ${error.message}`;
        }

        return {
            type: 'error',
            title: '',
            message
        } as AlertModel;
    }

    if (response.headers['content-type'] && response.headers['content-type'].indexOf('application/json') >= 0) {
        return constructAlert(response);
    }

    return {
        type: 'error',
        title: '',
        httpStatusCode: response.status,
        httpStatusText: response.statusText,
        message: 'Unexpected error'
    };
}

export function createRequest(url: string,
                              method = 'GET',
                              params: any = null,
                              body: any = null,
                              headers: any = null): Promise<any> {

    let apiUrl = url;
    if (!url.startsWith('/')) {
        apiUrl = CONTEXT_PATH + apiUrl;
    }

    const config: AxiosRequestConfig = {};

    if (params) {
        config.params = params;
        config.paramsSerializer = (params) => {
            return qs.stringify(params, {arrayFormat: 'repeat'});
        };
    }

    const isForm = (body instanceof FormData);

    let data;
    if (!isForm) {
        if (method === 'POST' || method === 'PATCH' || method === 'PUT') {
            data = body || {};
        }
    }

    config.headers = headers || (isForm ? {} : {
        'Content-Type': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
    });

    let axiosPromise: AxiosPromise;
    if (method === 'GET') {
        axiosPromise = axios.get(apiUrl, config);
    } else if (method === 'POST') {
        axiosPromise = axios.post(apiUrl, data, config);
    } else if (method === 'PUT') {
        axiosPromise = axios.put(apiUrl, data, config);
    } else if (method === 'DELETE') {
        axiosPromise = axios.delete(apiUrl, config);
    }
    const promise = new Promise<any>((resolve, reject) => {
        axiosPromise.then((response) => {
            resolve(response.data);
        }).catch((error) => {
            if (error?.config?.responseType === 'blob') {
                parseErrorBlob(error).then((data) => {
                    const err = JSON.parse(data.toString());
                    error.response.data = err;
                    const alert = convertToAlert(error);
                    reject(alert);
                });
            } else {
                const alert = convertToAlert(error);
                reject(alert);
            }
        });
    });

    return promise;
}

export function extractCSRFTokenFromDoc(): string {
    const tokenField = window.document.getElementsByName('org.apache.struts.taglib.html.TOKEN')[0];
    if (tokenField) {
        const token = tokenField.getAttribute('value');
        return token || "";
    }
    return "";
};

export function extractCSRFTokenFromText(text: string): string {
    let csrfToken = null;
    let matches = text.match(/input(.*?)org.apache.struts.taglib.html.TOKEN[\s\S]*?value="(.*?)"/gi);
    if (matches && matches.length > 0) {
        matches = matches[0].match(/"(.*?)"/gi);
        if (matches && matches.length > 3) {
            csrfToken = matches[3].replace(/"/g, '');
        }
    }
    return csrfToken || "";
};

export function appendCSRFToken(url: string) {
    const token = CSRF_TOKEN;
    let newUrl = url;
    if (token) {
        const param = `org.apache.struts.taglib.html.TOKEN=${token}`;
        newUrl += (url.indexOf('?') < 0) ? '?' + param : '&' + param;
    }
    return newUrl;
};

export function extractContextRootFromDoc(): string {
    const contextField = window.document.getElementsByName('org.apache.struts.taglib.html.CONTEXT_TOKEN')[0];
    if (contextField) {
        const contextRoot = contextField.getAttribute('value');
        return contextRoot || "";
    }
    return "";
};

export function extractJWTTokenFromText(text: string): string {
    let jwtToken = null;
    let matches = text.match(/input(.*?)org.apache.struts.taglib.html.JWT_TOKEN[\s\S]*?value="(.*?)"/gi);
    if (matches && matches.length > 0) {
        matches = matches[0].match(/"(.*?)"/gi);
        if (matches && matches.length > 3) {
            jwtToken = matches[3].replace(/"/g, '');
        }
    }
    return jwtToken || "";
};

export function extractJWTTokenFromDoc(): string {
    const tokenField = window.document.getElementsByName('org.apache.struts.taglib.html.JWT_TOKEN')[0];
    if (tokenField) {
        const token = tokenField.getAttribute('value');
        return token || "";
    }
    return "";
};
