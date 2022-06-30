package com.ibm.openpages.ext.util;


import com.ibm.openpages.api.metadata.DataType;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.resource.IEnumField;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.LoggerUtil;
import com.ibm.openpages.ext.tss.service.util.CommonUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;


public class CustomEnumFieldUtil {
    private Log logger;
    ILoggerUtil loggerUtil;

    private CustomEnumFieldUtil() {
        this.loggerUtil = new LoggerUtil();
        loggerUtil.initService();
        this.logger = this.loggerUtil.getExtLogger();
    }

    private static CustomEnumFieldUtil customEnumFieldUtil;

    public static CustomEnumFieldUtil getInstanceOf(){

        if(customEnumFieldUtil == null){
            customEnumFieldUtil = new CustomEnumFieldUtil();
        }

        return customEnumFieldUtil;

    }


    public IEnumField getEnumField(IField field) throws Exception {
        return DataType.ENUM_TYPE.equals(field.getDataType()) ? (IEnumField)field : null;
    }

    public IEnumField getEnumField(IGRCObject object, String fieldInfo) throws Exception {
        return this.getEnumField(object.getField(fieldInfo));
    }

    public boolean isEnumFieldNull(IField field) throws Exception {
        return CommonUtil.isObjectNull(this.getEnumField(field));
    }

    public boolean isEnumFieldNull(IGRCObject object, String fieldInfo) throws Exception {
        return this.isEnumFieldNull(this.getEnumField(object, fieldInfo));
    }

    public boolean isEnumFieldNotNull(IField field) throws Exception {
        return !this.isEnumFieldNull(field);
    }

    public boolean isEnumFieldNotNull(IGRCObject object, String fieldInfo) throws Exception {
        return this.isEnumFieldNotNull(this.getEnumField(object, fieldInfo));
    }

    public boolean isEnumFieldValueNull(IField field) throws Exception {
        return CommonUtil.isObjectNull(this.getEnumFieldValue(field));
    }

    public boolean isEnumFieldValueNull(IGRCObject object, String fieldInfo) throws Exception {
        return this.isEnumFieldValueNull(this.getEnumField(object, fieldInfo));
    }

    public boolean isEnumFieldValueNotNull(IField field) throws Exception {
        return !this.isEnumFieldValueNull(field);
    }

    public boolean isEnumFieldValueNotNull(IGRCObject object, String fieldInfo) throws Exception {
        return this.isEnumFieldValueNotNull(this.getEnumField(object, fieldInfo));
    }

    public IEnumValue getEnumFieldValue(IField field) throws Exception {
        return this.isEnumFieldNotNull(field) ? this.getEnumField(field).getEnumValue() : null;
    }

    public IEnumValue getEnumFieldValue(IGRCObject object, String fieldInfo) throws Exception {
        return this.getEnumFieldValue(this.getEnumField(object, fieldInfo));
    }

    public String getEnumFieldSelectedValue(IField field) throws Exception {
        return CommonUtil.isObjectNotNull(this.getEnumFieldValue(field)) ? this.getEnumFieldValue(field).getName() : null;
    }

    public String getEnumFieldSelectedValue(IGRCObject object, String fieldInfo) throws Exception {
        return this.getEnumFieldSelectedValue(this.getEnumField(object, fieldInfo));
    }

    public List<String> getAllValuesInEnumFieldAsList(IFieldDefinition fieldDefinition) throws Exception {
        List<String> enumValues = null;
        List<IEnumValue> enumVals = null;
        enumValues = new ArrayList();
        enumVals = fieldDefinition.getEnumValues();
        if (CommonUtil.isListNotNullOrEmpty(enumVals)) {
            Iterator var4 = enumVals.iterator();

            while(var4.hasNext()) {
                IEnumValue enumVal = (IEnumValue)var4.next();
                enumValues.add(enumVal.getName());
            }
        }

        return enumValues;
    }

    public List<String> getActiveValuesInEnumFieldAsList(IFieldDefinition fieldDefinition) throws Exception {
        List<String> enumValues = null;
        List<IEnumValue> enumVals = null;
        enumValues = new ArrayList();
        enumVals = fieldDefinition.getEnumValues();
        Iterator var4 = enumVals.iterator();

        while(var4.hasNext()) {
            IEnumValue enumVal = (IEnumValue)var4.next();
            if (!enumVal.isHidden()) {
                enumValues.add(enumVal.getName());
            }
        }

        return enumValues;
    }

    public List<String> getHiddenValuesInEnumFieldAsList(IFieldDefinition fieldDefinition) throws Exception {
        List<String> enumValues = null;
        enumValues = new ArrayList();
        List<IEnumValue> enumVals = fieldDefinition.getEnumValues();
        Iterator var4 = enumVals.iterator();

        while(var4.hasNext()) {
            IEnumValue enumVal = (IEnumValue)var4.next();
            if (enumVal.isHidden()) {
                enumValues.add(enumVal.getName());
            }
        }

        return enumValues;
    }

    public List<String> getAllValuesInEnumFieldAsList(IField field) throws Exception {
        return this.getAllValuesInEnumFieldAsList(this.getEnumField(field).getFieldDefinition());
    }

    public List<String> getAllValuesInEnumFieldAsList(IGRCObject object, String fieldInfo) throws Exception {
        return this.getAllValuesInEnumFieldAsList((IField)this.getEnumField(object, fieldInfo));
    }

    public List<String> getActiveValuesInEnumFieldAsList(IField field) throws Exception {
        return this.getAllValuesInEnumFieldAsList(this.getEnumField(field).getFieldDefinition());
    }

    public List<String> getActiveValuesInEnumFieldAsList(IGRCObject object, String fieldInfo) throws Exception {
        return this.getAllValuesInEnumFieldAsList((IField)this.getEnumField(object, fieldInfo));
    }

    public List<String> getHiddenValuesInEnumFieldAsList(IField field) throws Exception {
        return this.getAllValuesInEnumFieldAsList(this.getEnumField(field).getFieldDefinition());
    }

    public List<String> getHiddenValuesInEnumFieldAsList(IGRCObject object, String fieldInfo) throws Exception {
        return this.getAllValuesInEnumFieldAsList((IField)this.getEnumField(object, fieldInfo));
    }

    public boolean isEnumFieldValueMatchExpectedValue(IField field, String expectedFieldValue) throws Exception {
        String enumFieldValue = "";
        enumFieldValue = this.getEnumFieldSelectedValue(field);
        return CommonUtil.isNotNullOrEmpty(enumFieldValue) && enumFieldValue.equalsIgnoreCase(expectedFieldValue);
    }

    public boolean isEnumFieldValueMatchExpectedValue(IGRCObject object, String fieldInfo, String expectedFieldValue) throws Exception {
        return this.isEnumFieldValueMatchExpectedValue(this.getEnumField(object, fieldInfo), expectedFieldValue);
    }

    public boolean isEnumFieldValueMatchAnyDesiredValues(IField field, List<String> desiredValuesList) throws Exception {
        boolean isEnumFieldValueMatchDesiredValues = false;
        Iterator var4 = desiredValuesList.iterator();

        while(var4.hasNext()) {
            String fieldValue = (String)var4.next();
            isEnumFieldValueMatchDesiredValues = isEnumFieldValueMatchDesiredValues || this.isEnumFieldValueMatchExpectedValue(field, fieldValue);
            if (isEnumFieldValueMatchDesiredValues) {
                break;
            }
        }

        return isEnumFieldValueMatchDesiredValues;
    }

    public boolean isEnumFieldValueMatchAnyDesiredValues(IGRCObject object, String fieldInfo, List<String> desiredValuesList) throws Exception {
        return this.isEnumFieldValueMatchAnyDesiredValues(this.getEnumField(object, fieldInfo), (List)desiredValuesList);
    }

    public boolean isEnumFieldValueMatchAnyDesiredValues(IField field, String desiredValues) throws Exception {
        return this.isEnumFieldValueMatchAnyDesiredValues(field, CommonUtil.parseDelimitedValues(desiredValues, ","));
    }

    public boolean isEnumFieldValueMatchAnyDesiredValues(IGRCObject object, String fieldInfo, String desiredValues) throws Exception {
        return this.isEnumFieldValueMatchAnyDesiredValues(this.getEnumField(object, fieldInfo), (String)desiredValues);
    }

    public void setEnumFieldValue(IEnumField enumField, IEnumValue value) throws Exception {
        if (this.isEnumFieldNotNull(enumField)) {
            enumField.setEnumValue(value);
        }

    }

    public void setEnumFieldValue(IEnumField field, String value) throws Exception {
        this.setEnumFieldValue(field, this.getEnumValueFromField(field, value));
    }

    public void setEnumFieldValue(IGRCObject object, String fieldInfo, String value) throws Exception {
        this.setEnumFieldValue(this.getEnumField(object, fieldInfo), value);
    }

    private IEnumValue getEnumValueFromField(IField field, String enumVal) throws Exception {
        IEnumValue returnValue = null;
        IFieldDefinition fieldDef = null;
        List<IEnumValue> enumValues = null;
        if (CommonUtil.isObjectNotNull(field)) {
            fieldDef = field.getFieldDefinition();
            enumValues = fieldDef.getEnumValues();
            if (CommonUtil.isObjectNotNull(enumVal)) {
                Iterator var6 = enumValues.iterator();

                while(var6.hasNext()) {
                    IEnumValue val = (IEnumValue)var6.next();
                    this.logger.debug("Value in the Enumerated field: " + val.getName());
                    this.logger.debug("Is " + val.getName() + " Equal to " + enumVal + ": " + val.getName().equals(enumVal));
                    if (val.getName().equals(enumVal)) {
                        returnValue = val;
                    }
                }
            }
        }

        return returnValue;
    }
}
