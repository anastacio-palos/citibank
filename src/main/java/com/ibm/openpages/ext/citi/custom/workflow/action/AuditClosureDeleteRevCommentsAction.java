package com.ibm.openpages.ext.citi.custom.workflow.action;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IIdField;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.api.service.local.SessionFactory;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.common.util.ApplicationUtil;
import com.openpages.aurora.common.AuroraConstants;
import com.openpages.aurora.service.security.SecurityUtil;
import com.openpages.ext.solutions.common.LoggerUtilExtended;
import com.openpages.sdk.OpenpagesSession;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.Logger;

public class AuditClosureDeleteRevCommentsAction extends AbstractCustomAction {
  
  public AuditClosureDeleteRevCommentsAction(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
    super(context, customProperties);
  }

  private Logger logger = LoggerUtilExtended.getLogger(this.getClass());

  private IServiceFactory serviceFactory = null;
  
  private IResourceService resourceService = null;
  
  private IConfigurationService configurationService = null;
  
  private IQueryService queryService = null;
  
  private static final String QUERY_STMT = "/OpenPages/Custom Deliverables/WorkflowCustomActions/Audit Closure/Delete Review Comments Query";
  

  protected void process() throws Exception {
	  
    IWFOperationContext context = getContext();
    
    
    serviceFactory = context.getServiceFactory();
    queryService = serviceFactory.createQueryService();
    configurationService = serviceFactory.createConfigurationService();
    resourceService = getOPSystemServiceFactory().createResourceService();
    
    IGRCObject auditObj = context.getResource();
    
    String queryStmt = ApplicationUtil.getRegistrySetting(QUERY_STMT, configurationService.getConfigProperties());
    
    queryStmt = queryStmt.replace("{0}", auditObj.getId().toString());

    ArrayList<Id> revCommentIdList = getQueryResults(queryStmt);
    
    for(Id revCommentId : revCommentIdList)
    {
    	resourceService.deleteResource(revCommentId);
    }
     
  }
  
  
  public ArrayList<Id> getQueryResults(String queryStmt)
  {
	  
	  ArrayList<Id> objIdList = new ArrayList<Id>();
	  
      try {
          IQuery query = queryService.buildQuery(queryStmt);
          ITabularResultSet resultset = query.fetchRows(0);
          for (IResultSetRow row : resultset) 
          {
              IField field = row.getField(0);
              if (field instanceof IIdField) 
              {
                  IIdField idField = (IIdField) field;
                  if (idField.getValue()!= null) 
                  {
                      Id objId = idField.getValue();
                      objIdList.add(objId);
                  }
              }
           }
  		} catch (Exception ex) {
  		logger.error("getQueryResults : Error in Query Service execution" + ex);
  		}
	  
	  return objIdList;
  }
  
	private IServiceFactory getOPSystemServiceFactory() {

		Context context = new Context();
		context.put(Context.SERVICE_USER_NAME, AuroraConstants.OP_SYSTEM_USER);
		context.put(Context.SERVICE_USER_PASSWORD, SecurityUtil.getSystemPassword());
		
		SessionFactory sessionFactory = new SessionFactory();
		OpenpagesSession session = (OpenpagesSession) sessionFactory.create(context);
		
		context = new Context();
		context.put(Context.SERVICE_SESSION, session);

		return new LocalServiceFactory(context);
	}
  
}