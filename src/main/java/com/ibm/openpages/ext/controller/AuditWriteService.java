package com.ibm.openpages.ext.controller;

import com.ibm.openpages.api.application.CopyConflictBehavior;
import com.ibm.openpages.api.application.CopyObjectOptions;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IApplicationService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.ServiceFactory;
import com.ibm.openpages.components.common.util.CommonUtil;
import com.ibm.openpages.ext.constant.AuditScopingHelperConstant;
import com.ibm.openpages.ext.model.AEOutOfScope;
import com.ibm.openpages.ext.model.OPObject;
import com.ibm.openpages.ext.model.Rejection;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import org.apache.commons.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/jspview/auditScopingApp")
public class AuditWriteService {

    private IGRCObjectUtil grcObjectUtil;
    private Log log;
    private IServiceFactoryProxy serviceFactoryProxy;
    private IFieldUtil fieldUtil;
    private AuditValidator auditValidator;
    private CopyObjectOptions copyOptions;

    /**
     * constructor
     *
     * @param grcObjectUtil
     * @param serviceFactoryProxy
     * @param fieldUtil
     * @param loggerUtil
     */
    public AuditWriteService(IGRCObjectUtil grcObjectUtil, IServiceFactoryProxy serviceFactoryProxy,
            IFieldUtil fieldUtil, AuditValidator auditValidator, ILoggerUtil loggerUtil) {

        this.grcObjectUtil = grcObjectUtil;
        this.log = loggerUtil.getExtLogger(AuditScopingHelperConstant.AUDIT_SCOPING_HELPER_LOG);
        this.serviceFactoryProxy = serviceFactoryProxy;
        this.fieldUtil = fieldUtil;
        copyOptions = new CopyObjectOptions();
        copyOptions.setConflictBehavior(CopyConflictBehavior.OVERWRITE);
        copyOptions.setIncludeChildren(false);
        this.auditValidator = auditValidator;

    }

    private static boolean test = true;

    @RequestMapping(value = "/audit/{auditID}", method = RequestMethod.POST)
    public ResponseEntity<String> updateAudit(@PathVariable String auditID, @RequestBody List<OPObject> opObjects) {

        log.info(String.format("Action=Update_Audit auditid=%s size=%s", auditID, opObjects.size()));

        for(OPObject obj: opObjects){
            if(AuditScopingHelperConstant.PCRW_SCOPE_BLANK.equalsIgnoreCase(obj.getScope())){
                obj.setScope(AuditScopingHelperConstant.PCRW_SCOPE_YES);
            }
        }

        Map<String, List<OPObject>> objectByType = opObjects.stream().collect(Collectors.groupingBy(a -> a.getType()));

        log.info("Action=Update_Audit types=" + objectByType.keySet().stream().collect(Collectors.joining(",")));

        List<OPObject> processObject = objectByType.get(AuditScopingHelperConstant.PROCESS);
        process(new OPObject(auditID), AuditScopingHelperConstant.PROCESS_SCOPE, processObject);

        List<OPObject> applications = objectByType.get(AuditScopingHelperConstant.APP);
        process(new OPObject(auditID), AuditScopingHelperConstant.APP_SCOPE, applications);

        List<OPObject> issues = objectByType.get(AuditScopingHelperConstant.L4Issues);
        processIssue(new OPObject(auditID), AuditScopingHelperConstant.Issue_SCOPE, issues);

        exractAndProcess(AuditScopingHelperConstant.RISK, objectByType, AuditScopingHelperConstant.RISK_SCOPE,
                         AuditScopingHelperConstant.PROCESS);
        exractAndProcess(AuditScopingHelperConstant.CONTROL, objectByType, AuditScopingHelperConstant.CONTROL_SCOPE,
                         AuditScopingHelperConstant.RISK);
        exractAndProcess(AuditScopingHelperConstant.WORKPAPER, objectByType, AuditScopingHelperConstant.WP_SCOPE,
                         AuditScopingHelperConstant.CONTROL);

        try {
            auditValidator.validateAudititableEntityAssociation(auditID);
        }
        catch (Exception ex) {

            log.info("Validation exception", ex);
        }

        return new ResponseEntity("Done", HttpStatus.OK);

    }

    private void exractAndProcess(String type, Map<String, List<OPObject>> objectByType, String scopeField,
            String parentType) {

        Map<String, OPObject> aeRefToAuditRefMapping = objectByType.get(parentType).stream()
                .collect(Collectors.toMap(p -> p.getAeObjRefID(), p -> p));

        List<OPObject> objectToProcess = objectByType.get(type);

        objectToProcess.stream().forEach(r -> {
            try {

                IGRCObject childObject = grcObjectUtil.getObjectFromId(r.getKey());
                Id parentObject = childObject.getPrimaryParent();
                OPObject parentObj = aeRefToAuditRefMapping.get(parentObject.toString());
                log.info("Parent object : this cant be null = " + parentObj);

                if (r.isParentProcessed()) {
                    process(new OPObject(r.getParent()), scopeField, r);

                }
                else {
                    process(aeRefToAuditRefMapping.get(r.getParent()), scopeField, r);

                }

            }
            catch (Exception ex) {

            }

        });

    }

    private void process(final OPObject parent, final String scopeField, final List<OPObject> objectList) {

        objectList.stream().forEach(p -> process(parent, scopeField, p));
    }

    private void process(final OPObject parent, final String scopeField, final OPObject v) {

        try {
            log.info(String.format("Object=%s copy=%s scopeFile=%s", v, parent, scopeField));
            IApplicationService applicationService = serviceFactoryProxy.getServiceFactory().createApplicationService();

            IGRCObject object = grcObjectUtil.getObjectFromId(v.getKey());
            if (v.isProcessed()) {
                // just sync the status
                justSyncTheStatus(scopeField, v, object);

            }
            else {

                List<IGRCObject> copiedObject = applicationService
                        .copyToParent(new Id(parent.getKey()), Arrays.<Id>asList(new Id(v.getKey())), copyOptions);
                log.info("Process/L4/App Expected size=1 actual=" + copiedObject.size());
                IGRCObject createdObj = copiedObject.get(0);
                log.info(String.format("Created process object id=%s name=%s", createdObj.getId(),
                                       createdObj.getName()));

                fieldUtil.setFieldValue(createdObj.getField(AuditScopingHelperConstant.PCRW_AUDITABLE_ENTITY_FIELD),
                                        v.getAe());
                fieldUtil.setFieldValue(createdObj.getField(AuditScopingHelperConstant.PCRW_SOURCE_ID_FIELD),
                                        v.getKey());
                fieldUtil.setFieldValue(createdObj.getField(scopeField), v.getScope());
                createdObj = grcObjectUtil.saveResource(createdObj);

                // flipping the object to the audit object ( hoping rest remains the same
                v.setAeObjRefID(v.getKey());
                v.setKey(createdObj.getId().toString());

            }
        }
        catch (Exception ex) {
            log.error("Exception updating objecgt", ex);
        }
    }

    private void justSyncTheStatus(final String scopeField, final OPObject v, final IGRCObject object)
            throws Exception {

        String scopeValue = fieldUtil.getFieldValueAsString(object, scopeField);
        log.info(String.format("Object id=%s, name=%s, scope=%s inputscope=%s", object.getId(), object.getName(),
                               scopeValue, v.getScope()));
        if (!v.getScope().equalsIgnoreCase(scopeValue)) {
            fieldUtil.setFieldValue(object, scopeField, v.getScope());
            grcObjectUtil.saveResource(object);
        }
    }

    private void processIssue(final OPObject parent, final String scopeField, final List<OPObject> objectList) {

        IServiceFactory factory = serviceFactoryProxy.getServiceFactory();
        IApplicationService applicationService = serviceFactoryProxy.getServiceFactory().createApplicationService();
        IResourceService resourceService = factory.createResourceService();

        objectList.forEach(v -> {
            try {

                IGRCObject issueOrIssueAppObj = grcObjectUtil.getObjectFromId(v.getKey());

                if (v.isProcessed()) {

                    justSyncTheStatus(scopeField, v, issueOrIssueAppObj);
                }
                else {
                    IGRCObject createdObj = resourceService.getResourceFactory()
                            .createAutoNamedGRCObject(factory.createMetaDataService().getType("Citi_IssApp"));

                    createdObj.setPrimaryParent(new Id(parent.getKey()));
                    createdObj = resourceService.saveResource(createdObj);

                    log.info("Saved issue appObject" + createdObj);

                    fieldUtil.setFieldValue(createdObj.getField(AuditScopingHelperConstant.L4_AUDIT_ID), v.getAe());
                    fieldUtil.setFieldValue(createdObj.getField(AuditScopingHelperConstant.L4_APP_IssueID),
                                            v.getTitle());
                    fieldUtil.setFieldValue(createdObj.getField(AuditScopingHelperConstant.L4_APP_IssueTitle),
                                            v.getDescription());
                    fieldUtil.setFieldValue(createdObj.getField(AuditScopingHelperConstant.L4_SOURCE_ID_FIELD),
                                            v.getKey());
                    fieldUtil.setFieldValue(createdObj.getField(scopeField), v.getScope());

                    createdObj = grcObjectUtil.saveResource(createdObj);

                    log.info("Info Issue - applicabiliyobject created=" + createdObj.getName());

                }
            }
            catch (Exception ex) {
                log.error("Error updating the issue object", ex);

            }

        });

    }

}
