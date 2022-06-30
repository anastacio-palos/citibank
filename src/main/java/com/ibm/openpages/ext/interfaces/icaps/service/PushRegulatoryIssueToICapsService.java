package com.ibm.openpages.ext.interfaces.icaps.service;

import com.ibm.openpages.api.resource.IGRCObject;

/**
 *
 */
public interface PushRegulatoryIssueToICapsService {

    /**
     *
     * @param issueObject
     * @param context
     * @throws Exception
     */
    void pushRegulatoryIssue(IGRCObject issueObject, String context) throws Exception;

}
