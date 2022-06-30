package com.ibm.openpages.ext.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.constant.AuditScopingHelperConstant;
import com.ibm.openpages.ext.helper.QueryExecutor;
import com.ibm.openpages.ext.helper.transformer.AuditProcessAEMapper;
import com.ibm.openpages.ext.helper.transformer.IDNameDescriptionMapper;
import com.ibm.openpages.ext.helper.transformer.IDNameDescriptionSrcRefMapper;
import com.ibm.openpages.ext.model.OPObject;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectTypeUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AuditValidator {

    private IGRCObjectUtil grcObjectUtil;
    private IGRCObjectTypeUtil objectTypeUtil;
    private QueryExecutor queryExecutor;
    private Log log;
    private Function idNameDescriptionMapper;
    private Function idNameDescriptionSrcRefMapper;
    private Function auditProcessAEMapper;
    IFieldUtil fieldUtil;

    public AuditValidator(IGRCObjectUtil grcObjectUtil, IGRCObjectTypeUtil objectTypeUtil, QueryExecutor queryExecutor,
            IFieldUtil iFieldUtil, ILoggerUtil loggerUtil) {

        this.grcObjectUtil = grcObjectUtil;
        this.objectTypeUtil = objectTypeUtil;
        this.queryExecutor = queryExecutor;
        this.fieldUtil = iFieldUtil;
        this.log = loggerUtil.getExtLogger(AuditScopingHelperConstant.AUDIT_SCOPING_HELPER_LOG);
        this.idNameDescriptionMapper = new IDNameDescriptionMapper(iFieldUtil, loggerUtil);
        this.idNameDescriptionSrcRefMapper = new IDNameDescriptionSrcRefMapper(iFieldUtil, loggerUtil);
        this.auditProcessAEMapper = new AuditProcessAEMapper(iFieldUtil, loggerUtil);
    }

    public boolean validateAudititableEntityAssociation(String auditID) {

        log.info("Action=validate_audit-start auditid =" + auditID);

        boolean result = true;

        List<OPObject> auditableEntities = (List<OPObject>) queryExecutor
                .executeQuery(String.format(AuditScopingHelperConstant.RETRIEVE_AUDITABLE_ENTITIES_QUERY, auditID),
                              idNameDescriptionMapper);

        log.info("auditableEntities = " + auditableEntities);

        List<OPObject> auditProcessObject = (List<OPObject>) queryExecutor
                .executeQuery(String.format(AuditScopingHelperConstant.RETRIEVE_AUDIT_PROCESS_AEREF, auditID),
                              auditProcessAEMapper);

        List<String> availableEntities = auditableEntities.stream().map(a -> a.getKey()).collect(Collectors.toList());
        List<String> mappedEntities = auditProcessObject.stream().map(a -> a.getAeID()).collect(Collectors.toList());

        log.info(String.format("Auditable Entitieslist =%s vs Mapped EntityList =%s ", availableEntities,
                               mappedEntities));

        availableEntities.removeAll(mappedEntities);

        result = auditableEntities.isEmpty();

        auditableEntities.stream().map(v -> queryAndFetch(auditID, v.getKey())).filter(v -> v != null)
                .map(v -> v.getKey()).forEach(this::updateDeltFlag);
        log.info(String.format("Action=validate_audit-start auditid =%s result=%s", auditID, result));

        return result;
    }

    private void updateDeltFlag(String id) {

        log.info("Action=Update_Delta_Flag auditablEntityAppID=" + id);
        try {
            IGRCObject auditAppID = grcObjectUtil.getObjectFromId(id);
            fieldUtil.setFieldValue(auditAppID, AuditScopingHelperConstant.DELTA_FIELD,
                                    AuditScopingHelperConstant.DELTA_FIELD_VALUE);

        }
        catch (Exception ex) {
            log.error("Error setting the delta flag", ex);
        }

    }

    private OPObject queryAndFetch(String auditID, String aeID) {

        List<OPObject> aeapps = (List<OPObject>) queryExecutor
                .executeQuery(String.format(AuditScopingHelperConstant.RETRIEVE_AUDIT_APP, auditID, aeID),
                              idNameDescriptionMapper);

        log.info("Size ae app list should be 1 = " + aeapps.size());

        return Optional.ofNullable(aeapps).filter(v -> !v.isEmpty()).map(v -> v.get(0)).orElse(null);

    }

}
