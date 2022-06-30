/*******************************************************************************
 * Licensed Materials - Property of IBM
 *
 * OpenPages GRC Platform (PID: 5725-D51)
 *
 * (c) Copyright IBM Corporation 2015 - 2020. All Rights Reserved.
 *  
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U.S. Copyright Office.
 *******************************************************************************/
package com.ibm.openpages.ext.trigger.ext;

import java.util.HashMap;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.trigger.ext.DefaultRule;


/**
 * Provide some common methods for trigger rules
 */
final class TriggerRuleUtil {
	
	 //private static Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger();
	
	 //private static final Logger logger = (Logger) LogManager.getLogger(TriggerRuleUtil.class);
    
    /**
     * evaluate if the object is the specific content type 
     */
    static boolean evaluateContentType(IGRCObject object, String contentType) {
    	
    	System.out.println("--> class: TriggerRuleUtil --> Metodo: evaluateContentType" + "object: "
                           + object.getName() + " contentType: " + contentType ); 
    	
        if (contentType == null || contentType.isEmpty()) {
        	System.out.println("Attibute contentType is requiere: " + contentType);
            throw new IllegalArgumentException("Attribute contentType is required.");
        }
        System.out.println("object-is-folder --> " + object.isFolder());
        if (object.isFolder()) {
            return false;
        }
        ITypeDefinition def = object.getType();
        
        System.out.println("contentType: " + contentType.trim() + " def: " + def.getName().trim());
        System.out.println("igualamos ---> " + contentType.trim() + "  ---> " + (def.getName().trim()));
        if (contentType.trim().equals(def.getName().trim())) {
        	System.out.println("contentType: " + contentType.trim() + " def: " + def.getName().trim());
        	System.out.println("end");
            return true;
        }
        else {
        	
        	System.out.println("Entro al else -- >" + contentType.trim().equals(def.getName().trim()));
            return false;
        }
    }
    
    /**
     * return specific attribute value.
     * @param rule
     * @param attributeName
     * @param defaultValue
     * @return
     */
    static String getAttributeValue(final DefaultRule rule, final String attributeName, final String defaultValue) {
    	System.out.println("--> class: TriggerRuleUtil --> Metodo: getAttributeValue" ); 
        HashMap<String, String> map = rule.getAttributes();
        if (null == map) {
            return defaultValue;
        }
        
        String contentType = map.get(attributeName);
        if (null == contentType) {
            return defaultValue;
        }
        
        return contentType;
    }

    /**
     * return specific attribute value, throw error if the value is not provided.  
     * @param rule
     * @param attributeName
     * @return
     */
    static String getRequiredAttributeValue(final DefaultRule rule, final String attributeName) {
    	System.out.println("--> class: TriggerRuleUtil --> Metodo: getRequiredAttributeValue" ); 
        HashMap<String, String> map = rule.getAttributes();
        if (null == map) {
            throw new IllegalStateException("Attribute '" + attributeName + "' is required.");
        }
        String contentType = map.get(attributeName);
        if (null == contentType || contentType.isEmpty()) {
            throw new IllegalStateException("Attribute '" + attributeName + "' is required.");
        }
        
        return contentType;
    }
}
