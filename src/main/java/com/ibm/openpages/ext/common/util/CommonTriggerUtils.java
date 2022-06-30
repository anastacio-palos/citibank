package com.ibm.openpages.ext.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.core.Logger;


public class CommonTriggerUtils {

	/**
	 * 
	 * @param keyAtributeName
	 * @param valueAttributeName
	 * @param attributes
	 * @return
	 */
	public static Map<String, String> createConfigAttrMap(String keyAtributeName, String valueAttributeName,
			HashMap<String, String> attributes, Logger logger) {
		Map<String, String> attributesMap = new HashMap<String, String>();
		String keyAttribute;
		int index = 1;

		while ((keyAttribute = attributes.get(keyAtributeName + index)) != null) {
			logger.debug(keyAtributeName + index + ": " + keyAttribute);

			String valueAttribute = attributes.get(valueAttributeName + index);
			logger.debug(valueAttributeName + index + ": " + valueAttribute);

			if (valueAttribute == null) {
				logger.debug("Error mapping attributes: '" + keyAtributeName + index + "' to non-existing attribute '"
						+ valueAttributeName + index + "': .");
			}

			attributesMap.put(keyAttribute, valueAttribute);

			index++;
		}
		return attributesMap;
	}
	
}