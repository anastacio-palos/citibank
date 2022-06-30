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
package com.ibm.openpages.ext.interfaces.common.util;

import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.trigger.ext.DefaultRule;
//import com.ibm.openpages.ext.trigger.icaps.constant.ICapsSyncConstants;
import java.util.HashMap;


/**
 * Provide some common methods for trigger rules
 */
public final class TriggerRuleUtil {

    //private static EXTLogger logger;

    static  {
      //  logger = new EXTLogger(ICapsSyncConstants.Logger_Trigger_Rule_Util);
    }

    /**
     * evaluate if the object is the specific content type 
     */
    public boolean evaluateContentType(IGRCObject object, String contentType) {

    	System.out.println("--> class: TriggerRuleUtil --> Metodo: evaluateContentType" + "object: "
                           + object.getName() + " contentType: " + contentType ); 
    	
        if (contentType == null || contentType.isEmpty()) {
          //  logger.error("Attribute contentType is required.");
            throw new IllegalArgumentException("Attribute contentType is required.");
        }
        System.out.println("object-is-folder --> " + object.isFolder());
        if (object.isFolder()) {
            return false;
        }
        ITypeDefinition def = object.getType();

        //logger.info( contentType.trim() + "  ---> " + (def.getName().trim()));
        if (contentType.trim().equals(def.getName().trim())) {
           // logger.info("Equal ---> " + "contentType: " + contentType.trim() + " def: " + def.getName().trim());
            //logger.info("end --> evaluateContentType" + true);
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
    public static String getAttributeValue(final DefaultRule rule, final String attributeName, final String defaultValue) {
        //logger.info("--> class: TriggerRuleUtil --> Method : getAttributeValue" );
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
    public static String getRequiredAttributeValue(final DefaultRule rule, final String attributeName) {
        //logger.info("--> class: TriggerRuleUtil --> Method: getRequiredAttributeValue" );
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
