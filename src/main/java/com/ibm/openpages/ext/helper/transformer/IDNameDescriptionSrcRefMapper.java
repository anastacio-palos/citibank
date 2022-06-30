package com.ibm.openpages.ext.helper.transformer;

import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.ext.constant.AuditScopingHelperConstant;
import com.ibm.openpages.ext.model.OPObject;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import org.apache.commons.logging.Log;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class IDNameDescriptionSrcRefMapper implements Function<ITabularResultSet, Object> {

    private Log log;
    private IFieldUtil fieldUtil;

    public IDNameDescriptionSrcRefMapper(IFieldUtil fieldUtil, ILoggerUtil iLoggerUtil) {

        this.fieldUtil = fieldUtil;
        this.log = iLoggerUtil.getExtLogger(AuditScopingHelperConstant.AUDIT_SCOPING_HELPER_LOG);
    }

    @Override
    public Object apply(final ITabularResultSet iResultSetRows) {

        Spliterator<IResultSetRow> resultSet = iResultSetRows.spliterator();

        return StreamSupport.stream(resultSet, false).map(v -> {
            OPObject opObject = new OPObject();
            try {
                opObject.setKey(fieldUtil.getFieldValueAsString(v.getField(0)));
                opObject.setTitle(fieldUtil.getFieldValueAsString(v.getField(1)));
                opObject.setDescription(fieldUtil.getFieldValueAsString(v.getField(2)));
                opObject.setSource(fieldUtil.getFieldValueAsString(v.getField(3)));
                opObject.setAeObjRefID(fieldUtil.getFieldValueAsString(v.getField(4)));
                opObject.setScope(fieldUtil.getFieldValueAsString(v.getField(5)));

            }
            catch (Exception ex) {
                log.error("Error retrieving the value", ex);
            }

            return opObject;

        }).filter(v -> v != null).collect(Collectors.toList());

    }
}
