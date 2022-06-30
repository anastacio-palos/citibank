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

export interface AlertModel {
    httpStatusCode?: number;
    httpStatusText?: string;
    title?: string;
    code?: string; // open pages code
    message?: string; // open pages message
    data?: any; // open pages additional data
    status?: string; // open pages status
    closed?: boolean;
    type: string; // Valid types: error, warning, info, success
    timestamp?: string;
}

export enum AlertType {
    error = 'error',
    warning = 'warning',
    info = 'info',
    success = 'success'
}

