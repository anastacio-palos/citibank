package com.ibm.openpages.ext.interfaces.icaps.service;

import com.ibm.openpages.api.resource.IGRCObject;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service("pushAuditIssueToICapsService")
public interface PushAuditIssueToICapsService {

    /**
     *
     * @param issueObject
     * @throws Exception
     */
    void pushAuditIssue(IGRCObject issueObject) throws Exception;

    void incrementCAPChangeCounter(IGRCObject issueObject) throws Exception;

    void parseICapsResponse(IGRCObject object) throws Exception;
}
