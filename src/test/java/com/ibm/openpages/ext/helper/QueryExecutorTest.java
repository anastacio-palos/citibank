package com.ibm.openpages.ext.helper;

import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;

import org.apache.commons.logging.Log;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.function.Function;

public class QueryExecutorTest {

    ILoggerUtil iLoggerUtil = Mockito.mock(ILoggerUtil.class);
    IQueryService queryService = Mockito.mock(IQueryService.class);

    //    @Test
    //    public void testExecuteQuery() {
    //
    //        String query = "testquery";
    //
    //        IQuery queryObj = Mockito.mock(IQuery.class);
    //
    //        Mockito.when(queryService.buildQuery(Mockito.anyString())).thenReturn(queryObj);
    //        Mockito.when(iLoggerUtil.getExtLogger(Mockito.anyString())).thenReturn(Mockito.mock(Log.class));
    //
    //        Mockito.when(queryObj.fetchRows(0)).thenReturn(Mockito.mock(ITabularResultSet.class));
    //
    //        QueryExecutor underTest = new QueryExecutor(queryService, iLoggerUtil);
    //
    //        Function<ITabularResultSet, Object> testMapper = new TestMapper();
    //        Object response = underTest.executeQuery(query, testMapper);
    //
    //        assert response != null;
    //        assert response.toString().equalsIgnoreCase("Ashish");
    //
    //    }
}

class TestMapper implements Function<ITabularResultSet, Object> {

    @Override
    public String apply(final ITabularResultSet iResultSetRows) {

        return "Ashish";
    }
}