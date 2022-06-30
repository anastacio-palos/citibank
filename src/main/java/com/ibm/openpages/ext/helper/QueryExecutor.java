package com.ibm.openpages.ext.helper;

import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.ext.constant.AuditScopingHelperConstant;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.proxy.IServiceFactoryProxy;
import org.springframework.stereotype.Component;
import org.apache.commons.logging.Log;

import java.util.function.Function;

@Component
public class QueryExecutor {

    private IServiceFactoryProxy serviceFactoryProxy;

    private Log log;

    public QueryExecutor(IServiceFactoryProxy serviceFactoryProxy, ILoggerUtil loggerUtil) {

        System.err.println(
                "service proxy =" + serviceFactoryProxy + " :: loggerUtil" + loggerUtil + " : servicefactory" + serviceFactoryProxy
                        .getServiceFactory());
        log = loggerUtil.getExtLogger(AuditScopingHelperConstant.AUDIT_SCOPING_HELPER_LOG);
        this.serviceFactoryProxy = serviceFactoryProxy;

    }

    public Object executeQuery(String query, Function<ITabularResultSet, Object> resultMapper) {

        IQueryService queryService = serviceFactoryProxy.getServiceFactory().createQueryService();

        log.info("Action=execute_query query={}" + query);

        IQuery queryToExecute = queryService.buildQuery(query);

        //        log.info("Action=execute_query query_to_execute="+ queryToExecute);

        return resultMapper.apply(queryToExecute.fetchRows(0));
    }
}
