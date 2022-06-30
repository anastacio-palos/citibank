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
import com.ibm.openpages.ext.common.util.GRCObjectUtil;

import java.util.List;

public class SetParentWorkpaperDocAttestedFlag extends AbstractCustomAction {

  private static final String SOXDOCUMENT_OBJECT = "SOXDocument";
  
  private IServiceFactory serviceFactory = null;
  
  private IResourceService resourceService = null;
  
  private static final String DOC_TYPE_FIELD = "doc_type_field";

  private static final String ATTESTATION_STATUS_FIELD = "attestation_status_field";
  
  private static final String IS_ACTIVE_FIELD = "is_active_field";

  private static final String FIELD_TO_SET = "fieldToSet";

  public SetParentWorkpaperDocAttestedFlag(IWFOperationContext context, List<IWFCustomProperty> customProperties) {
    super(context, customProperties);
  }
  
  protected void process() throws Exception {
	  
    IWFOperationContext context = getContext();
    
    serviceFactory = context.getServiceFactory();
    resourceService = serviceFactory.createResourceService();
    
    if(getPropertyValue(DOC_TYPE_FIELD) == null) 
		throwException("Property 'doc_type_field' must be set.", null);
    if(getPropertyValue(ATTESTATION_STATUS_FIELD) == null) 
		throwException("Property 'attestation_status_field' must be set.", null);
    if(getPropertyValue(IS_ACTIVE_FIELD) == null) 
		throwException("Property 'is_active_field' must be set.", null);
    if(getPropertyValue(FIELD_TO_SET) == null) 
		throwException("Property 'fieldToSet' must be set.", null);
    
    
    IGRCObject documentObj = context.getResource();
    
    String sourceDocType = GRCObjectUtil.getFieldValue(documentObj,getPropertyValue(DOC_TYPE_FIELD));
    
    Id workpaperObjId = documentObj.getPrimaryParent();
    
    GRCObjectFilter includeChildren = new GRCObjectFilter(serviceFactory.createConfigurationService().getCurrentReportingPeriod());
    AssociationFilter associationFilter = includeChildren.getAssociationFilter();
    associationFilter.setIncludeAssociations(IncludeAssociations.CHILD);
    associationFilter.setTypeFilters(serviceFactory.createMetaDataService().getType(SOXDOCUMENT_OBJECT));
    associationFilter.setHonorPrimary(false);
    
    IGRCObject workpaperObj = resourceService.getGRCObject(workpaperObjId, includeChildren);
    
    List<IAssociationNode> childAssociations = workpaperObj.getChildren();
    
    boolean isActiveSiblingDocExists = false;
    
    for (IAssociationNode docNode : childAssociations)
    {
	     IGRCObject docObj = resourceService.getGRCObject(docNode.getId());
	     if(!docObj.getId().equals(documentObj.getId())) 
	     {
	    	 String docType = GRCObjectUtil.getFieldValue(docObj,getPropertyValue(DOC_TYPE_FIELD));
	    	 String attestationStatus = GRCObjectUtil.getFieldValue(docObj,getPropertyValue(ATTESTATION_STATUS_FIELD));
	    	 String isActive = GRCObjectUtil.getFieldValue(docObj,getPropertyValue(IS_ACTIVE_FIELD));
	    	 if(docType.equalsIgnoreCase(sourceDocType) && isActive.equalsIgnoreCase("Yes") && attestationStatus.equalsIgnoreCase("Attested"))
	    		 isActiveSiblingDocExists = true;
	     } 
    } 
    
    if(isActiveSiblingDocExists)
    {
    	IEnumField enumField = (IEnumField)workpaperObj.getField(getPropertyValue(FIELD_TO_SET));
    	IEnumValue enumValue = enumField.getFieldDefinition().getEnumValue("Yes");
   	  	enumField.setEnumValue(enumValue);
   	  	resourceService.saveResource((IResource)workpaperObj);
    }
  }
}