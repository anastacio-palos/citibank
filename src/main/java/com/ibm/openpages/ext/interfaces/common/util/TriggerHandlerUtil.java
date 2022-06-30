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

import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.resource.*;
import com.ibm.openpages.api.trigger.ext.DefaultEventHandler;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Provide some common methods for trigger handlers
 */
public final class TriggerHandlerUtil {

    /**
     * Handler attribute 'enum.field'.
     */
    protected static final String ATTR_ENUM_FIELD = "enum.field";

    /**
     * Handler attribute 'set.value'.
     */
    protected static final String ATTR_SET_VALUE = "set.value";


    /**
     * return specific attribute value.
     *
     * @param handler
     * @param attributeName
     * @param defaultValue
     * @return
     */
    public static String getAttributeValue(final DefaultEventHandler handler, final String attributeName, final String defaultValue) {
        HashMap<String, String> map = handler.getAttributes();
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
     *
     * @param handler
     * @param attributeName
     * @return
     */
    public static String getRequiredAttributeValue(final DefaultEventHandler handler, final String attributeName) {

        HashMap<String, String> map = handler.getAttributes();
        if (null == map) {
            throw new IllegalStateException("Attribute '" + attributeName + "' is required.");
        }
        String contentType = map.get(attributeName);

        if (null == contentType || contentType.isEmpty()) {
            throw new IllegalStateException("Attribute '" + attributeName + "' is required.");
        }
        return contentType;
    }

    /*
    public static IField getField(final DefaultEventHandler handler,
                           final IGRCObject object,
                           final String attributeName) {
        //logger.info("--> class: TriggerHandlerUtil --> Method: getField" + " attributeName: " + attributeName +
        //        " " + "object: " +  object.toString());
        return object.getField(getRequiredAttributeValue(handler, attributeName, logger));
    }


    public static void setStringValue(IGRCObject object,DefaultEventHandler handler, String field, String value){
        System.out.println("Metod: --> setStringValue");
        IStringField send = (IStringField)TriggerHandlerUtil.getField(handler,object,field);
        System.out.println("IStringField value --> " + send.getValue());
        System.out.println("value from updateOriginalValue --> " + value);
        if(value !=null) {
            send.setValue(value);
        }else {
            send.setValue("");
        }
        System.out.println("end");
    }
    */
    /*
    public static void setEnumValue(IGRCObject object, AbstractEvent event, DefaultEventHandler handler, String field, String value) {
        //logger.info("--> class: TriggerHandlerUtil --> Method: setEnumValue " + event.getTriggerEventType());

        IField enumField = TriggerHandlerUtil.getField(handler, object, field);

       // logger.info("enumField: " + enumField.getName() + " object: " + object.getName());

        ITypeDefinition def = object.getType();
        IFieldDefinition fieldDef = def.getField(TriggerHandlerUtil.getRequiredAttributeValue(handler, field, logger));

        //logger.info("IFieldDefinition --> " + fieldDef.toString());

        List<IEnumValue> enumValues =  fieldDef.getEnumValues();
        String setValue = TriggerHandlerUtil.getRequiredAttributeValue(handler, value, logger);

        //logger.info("setValue: " + setValue);

        for(IEnumValue enumValue : enumValues){
            String name = enumValue.getName();
            //logger.info(" enumValue --> name: " + name);
            if (name.equals(setValue)) {
                //logger.info("same --> name --> " + name + " setValue --> " + setValue );
                setEnumFieldValue(enumField, enumValue);
                return;
            }
        }
        //logger.error("attribute value : " + value + " " + "is invalid.");
        throw new IllegalStateException("The value of attribute '" + value + "' is invalid.");
    }
      */

    public static String getValueFromField(IField field) {
        String value = "";
        Double floatValue;
        if (field instanceof ICurrencyField) {
            //logger.info("Was ICurrencyField");
            ICurrencyField currencyField = (ICurrencyField) field;
            floatValue = currencyField.getLocalAmount();
            if (floatValue != null) {
                value = Double.toString(floatValue);
            }
        } else if (field instanceof IEnumField) {
            //logger.info("Was IEnumField");
            IEnumField enumField = (IEnumField) field;
            if (enumField.getEnumValue() != null) {
                value = enumField.getEnumValue().getName();
            }
        } else if (field instanceof IStringField) {
            //logger.info("Was IStringField");
            IStringField stringField = (IStringField) field;
            if (stringField.getValue() != null)
                value = stringField.getValue();
        } else if (field instanceof IFloatField) {
            // logger.info("Was IFloatField");
            IFloatField floatField = (IFloatField) field;
            floatValue = floatField.getValue();
            if (floatField.getValue() != null)
                value = Double.toString(floatValue);
        } else if (field instanceof IIntegerField) {
            //logger.info("Was IIntegerField");
            IIntegerField integerField = (IIntegerField) field;
            //logger.info(integerField.getValue());
            if (integerField.getValue() != null)
                value = Integer.toString(integerField.getValue());
        } else if (field instanceof IBooleanField) {
            //logger.info("Was IBooleanField");
            IBooleanField booleanField = (IBooleanField) field;
            boolean booleanValue = booleanField.getValue();
            value = Boolean.toString(booleanValue);
        } else if (field instanceof IMultiEnumField) {
            //logger.info("Was IMultiEnumField");
            IMultiEnumField multiEnumField = (IMultiEnumField) field;
            List<IEnumValue> enumValues = multiEnumField.getEnumValues();
            StringBuffer enumValStrings = new StringBuffer();
            Iterator var6 = enumValues.iterator();

            while (var6.hasNext()) {
                IEnumValue enumVal = (IEnumValue) var6.next();
                enumValStrings.append(enumVal.getName() + ",");
            }

            enumValStrings = enumValStrings.deleteCharAt(enumValStrings.length() - 1);
            value = enumValStrings.toString();
        } else if (field instanceof IDateField) {
            //logger.info("Was IDateField");
            if (((IDateField) field).getValue() != null) {
                Date date = ((IDateField) field).getValue();
                value = date.toString();
            }
        } else {
            //logger.info("Was not cataloged");
        }

        return value;
    }

    public static void setEnumFieldValue(IField enumField, IEnumValue value) {
        System.out.println("--> class: SetEnumFieldHandler --> Method: setEnumFieldValue " + " enumField: " + enumField.getName()
                + " value: " + value.getName());
        ((IEnumField) enumField).setEnumValue(value);
    }


}