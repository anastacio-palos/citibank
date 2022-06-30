package com.ibm.openpages.ext.citi.custom.workflow.action;

import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.AssociationFilter;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IEnumField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IResource;
import com.ibm.openpages.api.resource.IncludeAssociations;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.api.workflow.actions.AbstractCustomAction;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import java.util.List;

public class SetSiblingDocumentsISActiveToNo extends AbstractCustomAction {

  private static final String SOXDOCUMENT_OBJECT = "SOXDocument";
  
  private IServiceFactory serviceFactory = null;
  
  private IResourceService resourceService = null;
  
  private static final String FIELD_TO_SET = "fieldToSet";

  private static final String VALUE_TO_SET = "valueToSet";
  
  public SetSiblingDocumentsISActiveToNo(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
    super(context, customProperties);
  }
  
  protected void process() throws Exception {
	  
    IWFOperationContext context = getContext();
    
    serviceFactory = context.getServiceFactory();
    resourceService = serviceFactory.createResourceService();
    
    IGRCObject documentObj = context.getResource();
    
    if(getPropertyValue(FIELD_TO_SET) == null) 
		throwException("Property 'fieldToSet' must be set.", null);
    if(getPropertyValue(VALUE_TO_SET) == null) 
		throwException("Property 'valueToSet' must be set.", null);
    
    Id workpaperObjId = documentObj.getPrimaryParent();
    
    GRCObjectFilter includeChildren = new GRCObjectFilter(serviceFactory.createConfigurationService().getCurrentReportingPeriod());
    AssociationFilter associationFilter = includeChildren.getAssociationFilter();
    associationFilter.setIncludeAssociations(IncludeAssociations.CHILD);
    associationFilter.setTypeFilters(serviceFactory.createMetaDataService().getType(SOXDOCUMENT_OBJECT));
    associationFilter.setHonorPrimary(false);
    
    IGRCObject workpaperObj = resourceService.getGRCObject(workpaperObjId, includeChildren);
    
    List<IAssociationNode> childAssociations = workpaperObj.getChildren();
    
    for (IAssociationNode docNode : childAssociations)
    {
	     IGRCObject docObj = resourceService.getGRCObject(docNode.getId());
	     if(!docObj.getId().equals(documentObj.getId())) 
	     {
	    	  IEnumField enumField = (IEnumField)docObj.getField(getPropertyValue(FIELD_TO_SET));
	    	  IEnumValue enumValue = enumField.getFieldDefinition().getEnumValue(getPropertyValue(VALUE_TO_SET));
	    	  enumField.setEnumValue(enumValue);
	    	  resourceService.saveResource((IResource)docObj);
	     } 
    } 
  }
}