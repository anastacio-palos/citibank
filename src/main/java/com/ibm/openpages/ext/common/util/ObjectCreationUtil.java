package com.ibm.openpages.ext.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IResourceFactory;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;

public class ObjectCreationUtil {
	
	private static final String COMMA_SEPARATOR = "\\,";
	private static final String SEPARATOR_EQUALS = "\\=";
	private static final String SEPARATOR_COLON = "\\:";
	Logger logger = null;
	CommonResourceUtils cru = null;

	public ObjectCreationUtil(Logger logger) {
		this.logger = logger;
		cru = new CommonResourceUtils(logger);
	}
	
	public void copyFieldsFromResource(IGRCObject parent, IGRCObject child, String copyFields,
			IServiceFactory serviceFactory) {
		GRCObjectFilter parentFieldsFilter = new GRCObjectFilter(
				serviceFactory.createConfigurationService().getCurrentReportingPeriod());
		
		//Important: before copying fields from parent object we make sure all fields to copy exist on the parent object
		//for instance, Control object has 'OPSS-Test:Test Procedure' field but for some reason it doesn't have 'OPSS-TestP: Test Procedure'
		//(cause could be the field name conflict).
		//so we enforce existence of 'OPSS-TestP:Test Procedure' field on Control object with GRC Filter object, by explicitly adding it
		List<IFieldDefinition> fieldsOnSource = new ArrayList<IFieldDefinition>();

		IMetaDataService metaDataService = serviceFactory.createMetaDataService();
		for (String fieldPair : copyFields.split("\\s*,\\s*")) {
			String fieldOnSource = fieldPair.split("=")[0].trim();
			fieldsOnSource.add(metaDataService.getField(fieldOnSource));
		}

		IFieldDefinition[] fieldsOnSourceArray = new IFieldDefinition[fieldsOnSource.size()];

		for (int i = 0; i < fieldsOnSource.size(); i++) {
			fieldsOnSourceArray[i] = fieldsOnSource.get(i);
		}
		logger.debug("fieldsOnSourceArray: " + fieldsOnSourceArray);
		
		parentFieldsFilter.setFieldFilters(fieldsOnSourceArray);
		parent = serviceFactory.createResourceService().getGRCObject(parent.getId(), parentFieldsFilter);

		copyFieldsFromResource(parent, child, copyFields, serviceFactory.createConfigurationService());
	}

	public void copyFieldsFromResource(IGRCObject parent,IGRCObject child,String copyFields, IConfigurationService ics) {
		logger.debug("Entered copyFieldsFromParent : "+copyFields);
		if (copyFields == null || copyFields.isEmpty())
		 {
				logger.debug("Copy fields value is null. Hence not copying any explicit fields.");
				return;
	     }
		String[] copyFieldArray = copyFields.split(COMMA_SEPARATOR);
		
        String fieldPair = null;
        String[] fieldPairValues;
        String childFieldName = null;
        String parentFieldName = null;
        IField parentSourceField = null;
        IField childDestField = null;
		for(int j=0; j<copyFieldArray.length;j++) {
			fieldPair = copyFieldArray[j];
			fieldPairValues = fieldPair.split(SEPARATOR_EQUALS); 
			if(fieldPairValues.length == 2){
				childFieldName = fieldPairValues[0];
				parentFieldName = fieldPairValues[1];
				logger.debug("parent id: "+parent.getId()+", name: "+parent.getName());
				logger.debug("srcFieldName: "+childFieldName + " destFieldName: "+parentFieldName);
				parentSourceField = parent.getField(parentFieldName.trim());
				childDestField = child.getField(childFieldName.trim());
				
				if(parentSourceField!=null && childDestField!=null) {
					Object valueForField = cru.getFieldValueAsObject(parentSourceField);
					cru.setFieldValueOnResource(childFieldName.trim(), valueForField,child, ics);
				} else {
					if (parentSourceField == null)
						logger.debug("parentSourceField " + parentFieldName + " is null !");
					if (childDestField == null)
						logger.debug("childDestField " + childFieldName + " is null !");
				}
			}
        } 
	}
	
	//Setting couple of child field values as per configuration
	public void setChildFieldValues(IGRCObject newChildObject,String childObjectFields, IConfigurationService ics) {
		logger.debug("Entered setChildFieldValues : "+childObjectFields);
		if (childObjectFields == null || childObjectFields.isEmpty())
		 {
				logger.debug("childObjectField to set  is null. Hence not setting any child field values.");
				return;
	     }
		String[] childObjectFieldsArray = childObjectFields.split(COMMA_SEPARATOR);
		
		String fieldPair = null;
        String[] fieldPairValues;
        String fieldName = null;
        String fieldValue = null;
		IField childDestField = null;
		for(int i=0; i<childObjectFieldsArray.length;i++) {
			fieldPair = childObjectFieldsArray[i];
			fieldPairValues = fieldPair.split(SEPARATOR_EQUALS); 
			if(fieldPairValues.length == 2){
				fieldName = fieldPairValues[0];
				fieldValue = fieldPairValues[1];
				childDestField = newChildObject.getField(fieldName);
				if(childDestField!=null) { // check if field exists in child object
					cru.setFieldValueOnResource(fieldName.trim(), fieldValue.trim(), newChildObject, ics);
				}
				else
				{
					logger.error("Field does not exist: "+fieldName);
				}
			}
		}
	}

	public IGRCObject createChildObject(String createObjectTypeName, IGRCObject resource,
			IServiceFactory serviceFactory) {

		IMetaDataService metaDataService = serviceFactory.createMetaDataService();
		ITypeDefinition objectTypeDef = metaDataService.getType(createObjectTypeName);
		logger.debug("objectTypeDef : " + objectTypeDef.getName());

		IResourceService rs = serviceFactory.createResourceService();
		IResourceFactory rss = rs.getResourceFactory();

		IGRCObject newObject = rss.createAutoNamedGRCObject(objectTypeDef);

		logger.debug("newObject : " + newObject.getName());

		newObject.setPrimaryParent(resource.getId());
		logger.debug("newObject : " + newObject.getName());

		newObject = rs.saveResource(newObject);
		logger.debug("newObject saved: " + newObject.getName());
		return newObject;

	}
	//Copy field groups configured considering exclusion list	
	public void copyFieldGroupsFromSourceToTarget(IGRCObject src,IGRCObject trgt, String groupsToBeCopied, Set<String> exclusionListSet)
    {
        
		if (groupsToBeCopied == null || groupsToBeCopied.isEmpty())
		 {
			logger.debug("groupsToBeCopied is null in Trigger configuration xml file. Hence not copying any field groups.");
				
	     }
		String[] fieldGroupArray = groupsToBeCopied.split(COMMA_SEPARATOR);
        Set<String> fieldGroupSet = new HashSet<String>(Arrays.asList(fieldGroupArray));

        //Get all the fields from parent
        //Check if fieldgroup is configured
        //If yes,check exclusion list and add
        //If no,check copyfields list and check exclusion list again and add
        
        java.util.List<IField> fieldList = src.getFields();
        
		Iterator<IField> itr = fieldList.iterator();
		String parentFieldName = null;
		String fieldGroup = null;
		
		while(itr.hasNext()) {
			
			IField element = itr.next(); // source field to copy
			parentFieldName = element.getName().trim();
			logger.debug("Processing :"+parentFieldName+",");
			//check if the field is part of exclusion list first
			if(exclusionListSet.contains(parentFieldName)){
				logger.debug(parentFieldName+" is excluded.\n");
				continue;
			}
			//Get the fieldgroup from fieldname
			if(parentFieldName.split(SEPARATOR_COLON).length == 2) { //possible array index exception
				fieldGroup = parentFieldName.split(SEPARATOR_COLON)[0];
				
				if(fieldGroupSet.contains(fieldGroup)) {
					IField childDestField = trgt.getField(parentFieldName);
					if(childDestField!=null) // check if field exists in child object
						cru.copyField(element,childDestField);
				} 
			}
			
		}
    }
	

}