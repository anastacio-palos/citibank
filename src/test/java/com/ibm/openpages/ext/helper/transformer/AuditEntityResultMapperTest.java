package com.ibm.openpages.ext.helper.transformer;

import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.ext.model.OPObject;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.openpages.aurora.common.logging.ConsoleLogger;
import org.apache.commons.logging.Log;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class AuditEntityResultMapperTest {

    ILoggerUtil iLoggerUtil = Mockito.mock(ILoggerUtil.class);
    IFieldUtil iFieldUtil = Mockito.mock(IFieldUtil.class);
    Log log = new ConsoleLogger("test");

    @Test
    public void testApply() throws Exception {

        log.info("testing");

        Mockito.when(iLoggerUtil.getExtLogger(Mockito.any())).thenReturn(log);

        IDNameDescriptionMapper underTest = new IDNameDescriptionMapper(iFieldUtil, iLoggerUtil);

        ITabularResultSet resultSet = Mockito.mock(ITabularResultSet.class);

        IResultSetRow iResultSetRow = Mockito.mock(IResultSetRow.class);
        Mockito.when(iResultSetRow.getField(Mockito.anyInt())).thenReturn(Mockito.mock(IField.class));
        Mockito.when(iFieldUtil.getFieldValueAsString(Mockito.any())).thenReturn("test");
        IResultSetRow[] arr = new IResultSetRow[1];
        arr[0] = iResultSetRow;
        Mockito.when(resultSet.spliterator()).thenReturn(Arrays.asList(iResultSetRow).spliterator());
        List<OPObject> response = (List<OPObject>) underTest.apply(resultSet);

        assert response != null;
        assert response.size() == 1;

    }
}