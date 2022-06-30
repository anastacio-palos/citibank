package com.ibm.openpages.ext.util;

import com.ibm.openpages.api.configuration.ICurrency;
import com.ibm.openpages.api.metadata.DataType;
import com.ibm.openpages.api.metadata.IEnumValue;
import com.ibm.openpages.api.metadata.IFieldDefinition;
import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.resource.*;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.ext.tss.service.*;
import com.ibm.openpages.ext.tss.service.beans.FieldValueChangeInfo;
import com.ibm.openpages.ext.tss.service.beans.FieldValueChangeOnConditionInfo;
import com.ibm.openpages.ext.tss.service.constants.CheckFor;
import com.ibm.openpages.ext.tss.service.constants.Comparators;
import com.ibm.openpages.ext.tss.service.impl.*;
import com.ibm.openpages.ext.tss.service.util.CommonUtil;
import com.ibm.openpages.ext.tss.service.util.DateUtil;
import com.ibm.openpages.ext.tss.service.util.NumericUtil;
import org.apache.commons.logging.Log;

import java.util.*;

public class CustomFieldUtil {

    private Log logger;
    LocalServiceFactory serviceFactoryProxy;
    IIDFieldUtil idFieldUtil;
    ICurrencyFieldUtil currencyFieldUtil;
    IDateFieldUtil dateFieldUtil;
    CustomEnumFieldUtil enumFieldUtil;
    IMultiEnumFieldUtil multiEnumFieldUtil;
    IFloatFieldUtil floatFieldUtil;
    IStringFieldUtil stringFieldUtil;
    IIntegerFieldUtil integerFieldUtil;
    ILoggerUtil loggerUtil;

    private static CustomFieldUtil customFieldUtil;

    public static CustomFieldUtil getInstanceOf(LocalServiceFactory sf){

        if(customFieldUtil == null){
            customFieldUtil = new CustomFieldUtil(sf);
        }

        return customFieldUtil;

    }

    private CustomFieldUtil(LocalServiceFactory sf) {

        this.loggerUtil = new LoggerUtil();
        loggerUtil.initService();
        this.logger = this.loggerUtil.getExtLogger();


        serviceFactoryProxy = sf;
        this.idFieldUtil = new IDFieldUtil();
        this.currencyFieldUtil = new CurrencyFieldUtil();
        this.dateFieldUtil = new DateFieldUtil();
        this.enumFieldUtil = CustomEnumFieldUtil.getInstanceOf();
        this.multiEnumFieldUtil = new MultiEnumFieldUtil();
        this.floatFieldUtil = new FloatFieldUtil();
        this.stringFieldUtil = new StringFieldUtil();
        this.integerFieldUtil = new IntegerFieldUtil();



    }


    public IField getField(IGRCObject object, String fieldInfo) throws Exception {
        return object.getField(fieldInfo);
    }

    public List<IField> getFields(IGRCObject object, List<String> fieldInfoList) throws Exception {
        List<IField> listOfFields = new ArrayList();
        if (CommonUtil.isListNotNullOrEmpty(fieldInfoList)) {
            Iterator var4 = fieldInfoList.iterator();

            while(var4.hasNext()) {
                String fieldInfo = (String)var4.next();
                if (CommonUtil.isNotNullOrEmpty(fieldInfo) && CommonUtil.isObjectNotNull(this.getField(object, fieldInfo))) {
                    listOfFields.add(this.getField(object, fieldInfo));
                }
            }
        }

        return listOfFields;
    }

    public boolean isFieldNull(IField field) throws Exception {
        return CommonUtil.isObjectNull(field);
    }

    public boolean isFieldNull(IGRCObject object, String fieldInfo) throws Exception {
        return this.isFieldNull(object.getField(fieldInfo));
    }

    public boolean isFieldNotNull(IField field) throws Exception {
        return !this.isFieldNull(field);
    }

    public boolean isFieldNotNull(IGRCObject object, String fieldInfo) throws Exception {
        return this.isFieldNotNull(object.getField(fieldInfo));
    }

    public boolean isFieldValueNull(IField field) throws Exception {
        boolean isFieldValueNull = false;
        if (CommonUtil.isObjectNotNull(field) && field.isNull()) {
            isFieldValueNull = true;
        } else if (DataType.FLOAT_TYPE.equals(field.getDataType())) {
            isFieldValueNull = Float.isNaN(this.floatFieldUtil.getFloatFieldValueAsFloat(field));
        } else if (this.stringFieldUtil.isStringFieldDataType(field)) {
            isFieldValueNull = CommonUtil.isNullOrEmpty(this.stringFieldUtil.getStringFieldValue(field));
        } else if (DataType.DATE_TYPE.equals(field.getDataType())) {
            isFieldValueNull = CommonUtil.isNullOrEmpty(this.dateFieldUtil.getDateFieldValueAsString(field));
        } else if (DataType.ENUM_TYPE.equals(field.getDataType())) {
            isFieldValueNull = CommonUtil.isNullOrEmpty(this.enumFieldUtil.getEnumFieldSelectedValue(field));
        } else if (DataType.ID_TYPE.equals(field.getDataType())) {
            isFieldValueNull = CommonUtil.isNullOrEmpty(this.idFieldUtil.getIdFieldValueAsString(field));
        }

        return isFieldValueNull;
    }

    public boolean isFieldValueNull(IGRCObject object, String fieldInfo) throws Exception {
        return this.isFieldValueNull(object.getField(fieldInfo));
    }

    public boolean isFieldValueNotNull(IField field) throws Exception {
        return !this.isFieldValueNull(field);
    }

    public boolean isFieldValueNotNull(IGRCObject object, String fieldInfo) throws Exception {
        return this.isFieldValueNotNull(object.getField(fieldInfo));
    }

    public boolean isFieldNullOrFieldValueNull(IField field) throws Exception {
        return this.isFieldNull(field) || CommonUtil.isObjectNotNull(field) && field.isNull();
    }

    public boolean isFieldNullOrFieldValueNull(IGRCObject object, String fieldInfo) throws Exception {
        return CommonUtil.isObjectNull(object.getField(fieldInfo)) || CommonUtil.isObjectNotNull(object.getField(fieldInfo)) && object.getField(fieldInfo).isNull();
    }

    public boolean isFieldValueNullOrEmpty(IField field) throws Exception {
        String fieldValue = "";
        if (this.isFieldNotNull(field)) {
            if (DataType.FLOAT_TYPE.equals(field.getDataType())) {
                fieldValue = this.floatFieldUtil.getFloatFieldValueAsFloat(field) + "";
            } else if (this.stringFieldUtil.isStringFieldDataType(field)) {
                fieldValue = this.stringFieldUtil.getStringFieldValue(field);
            } else if (DataType.DATE_TYPE.equals(field.getDataType())) {
                fieldValue = this.dateFieldUtil.getDateFieldValueAsString(field);
            } else if (DataType.ID_TYPE.equals(field.getDataType())) {
                fieldValue = this.idFieldUtil.getIdFieldValueAsString(field);
            }
        }

        return CommonUtil.isNullOrEmpty(fieldValue);
    }

    public boolean isFieldValueNullOrEmpty(IGRCObject object, String fieldInfo) throws Exception {
        return this.isFieldValueNullOrEmpty(object.getField(fieldInfo));
    }

    public boolean isFieldValueNotNullOrEmpty(IField field) throws Exception {
        return !this.isFieldValueNullOrEmpty(field);
    }

    public boolean isFieldValueNotNullOrEmpty(IGRCObject object, String fieldInfo) throws Exception {
        return this.isFieldValueNotNullOrEmpty(object.getField(fieldInfo));
    }

    public String getFieldName(IField field) throws Exception {
        return this.isFieldNotNull(field) ? field.getName() : "Null";
    }

    public String getFieldName(IGRCObject object, String fieldInfo) throws Exception {
        return this.getFieldName(object.getField(fieldInfo));
    }

    public String getFieldValueAsString(IField field) throws Exception {
        String fieldValue = "";
        if (this.isFieldNotNull(field)) {
            if (DataType.INTEGER_TYPE.equals(field.getDataType())) {
                fieldValue = this.integerFieldUtil.getIntegerFieldValueAsString(field);
            } else if (DataType.FLOAT_TYPE.equals(field.getDataType())) {
                fieldValue = this.floatFieldUtil.getFloatFieldValueAsString(field);
            } else if (this.stringFieldUtil.isStringFieldDataType(field)) {
                fieldValue = this.stringFieldUtil.getStringFieldValue(field);
            } else if (DataType.DATE_TYPE.equals(field.getDataType())) {
                fieldValue = this.dateFieldUtil.getDateFieldValueAsString(field);
            } else if (DataType.ENUM_TYPE.equals(field.getDataType())) {
                fieldValue = this.enumFieldUtil.getEnumFieldSelectedValue(field);
            } else if (DataType.MULTI_VALUE_ENUM.equals(field.getDataType())) {
                fieldValue = CommonUtil.unParseCommaDelimitedValues(this.multiEnumFieldUtil.getEnumFieldSelectedValue(field));
            } else if (DataType.ID_TYPE.equals(field.getDataType())) {
                fieldValue = this.idFieldUtil.getIdFieldValueAsString(field);
            } else if (DataType.CURRENCY_TYPE.equals(field.getDataType())) {
                fieldValue = this.currencyFieldUtil.getCurrencyFieldLocalAmountAsString(field);
            }
        }

        return fieldValue;
    }

    public Object getOriginalValueOfField(IGRCObject object, String fieldInfo) throws Exception {
        return CommonUtil.isObjectNotNull(ResourceUtil.getOriginalValueForField(fieldInfo, object)) ? ResourceUtil.getOriginalValueForField(fieldInfo, object) : null;
    }

    public String getOriginalValueOfFieldAsString(IGRCObject object, String fieldInfo) throws Exception {
        return CommonUtil.isObjectNotNull(ResourceUtil.getOriginalValueForField(fieldInfo, object)) ? ResourceUtil.getOriginalValueForField(fieldInfo, object).toString() : null;
    }

    public Object getFieldValue(IField field) throws Exception {
        Object fieldValue = null;
        if (this.isFieldNotNull(field)) {
            if (DataType.INTEGER_TYPE.equals(field.getDataType())) {
                fieldValue = this.integerFieldUtil.getIntegerFieldValue(field);
            } else if (DataType.FLOAT_TYPE.equals(field.getDataType())) {
                fieldValue = this.floatFieldUtil.getFloatFieldValue(field);
            } else if (this.stringFieldUtil.isStringFieldDataType(field)) {
                fieldValue = this.stringFieldUtil.getStringFieldValue(field);
            } else if (DataType.DATE_TYPE.equals(field.getDataType())) {
                fieldValue = this.dateFieldUtil.getDateFieldValue(field);
            } else if (DataType.ENUM_TYPE.equals(field.getDataType())) {
                fieldValue = this.enumFieldUtil.getEnumFieldValue(field);
            } else if (DataType.ID_TYPE.equals(field.getDataType())) {
                fieldValue = this.idFieldUtil.getIdFieldValueAsString(field);
            } else if (DataType.MULTI_VALUE_ENUM.equals(field.getDataType())) {
                fieldValue = this.multiEnumFieldUtil.getEnumFieldValues(field);
            } else if (DataType.CURRENCY_TYPE.equals(field.getDataType())) {
                fieldValue = this.currencyFieldUtil.getCurrencyFieldLocalAmount(field);
            }
        }

        return fieldValue;
    }

    public String getFieldValueAsString(IGRCObject object, String fieldInfo) throws Exception {
        return this.getFieldValueAsString(object.getField(fieldInfo));
    }

    public void setFieldValue(IField field, String value) throws Exception {
        if (this.isFieldNotNull(field)) {
            if (DataType.INTEGER_TYPE.equals(field.getDataType())) {
                this.integerFieldUtil.setIntegerField((IIntegerField)field, value);
            } else if (DataType.FLOAT_TYPE.equals(field.getDataType())) {
                this.floatFieldUtil.setFloatField((IFloatField)field, value);
            } else if (this.stringFieldUtil.isStringFieldDataType(field)) {
                this.stringFieldUtil.setStringFieldValue((IStringField)field, value);
            } else if (DataType.DATE_TYPE.equals(field.getDataType())) {
                this.dateFieldUtil.setDateField((IDateField)field, value);
            } else if (DataType.ENUM_TYPE.equals(field.getDataType())) {
                this.enumFieldUtil.setEnumFieldValue((IEnumField)field, value);
            } else if (DataType.MULTI_VALUE_ENUM.equals(field.getDataType())) {
                this.multiEnumFieldUtil.setEnumFieldValue(field, value);
            } else if (DataType.CURRENCY_TYPE.equals(field.getDataType())) {
                this.currencyFieldUtil.setCurrencyFieldLocalAmount(field, Double.parseDouble(value));
            }
        }

    }

    public void setFieldValue(IField field, Object value) throws Exception {
        if (this.isFieldNotNull(field)) {
            if (DataType.INTEGER_TYPE.equals(field.getDataType()) && value instanceof Integer) {
                this.integerFieldUtil.setIntegerField((IIntegerField)field, (Integer)value);
            } else if (DataType.FLOAT_TYPE.equals(field.getDataType()) && value instanceof Double) {
                this.floatFieldUtil.setFloatField((IFloatField)field, (Double)value);
            } else if (this.stringFieldUtil.isStringFieldDataType(field) && value instanceof String) {
                this.stringFieldUtil.setStringFieldValue((IStringField)field, (String)value);
            } else if (DataType.DATE_TYPE.equals(field.getDataType()) && value instanceof Date) {
                this.dateFieldUtil.setDateField((IDateField)field, (Date)value);
            } else if (DataType.ENUM_TYPE.equals(field.getDataType()) && value instanceof IEnumValue) {
                this.enumFieldUtil.setEnumFieldValue((IEnumField)field, (IEnumValue)value);
            } else if (DataType.MULTI_VALUE_ENUM.equals(field.getDataType()) && value instanceof List) {
                this.multiEnumFieldUtil.setEnumFieldValue((IMultiEnumField)field, (List)value);
            } else if (DataType.CURRENCY_TYPE.equals(field.getDataType()) && value instanceof ICurrencyField) {
                this.currencyFieldUtil.setCurrencyFieldLocalAmount(field, ((ICurrencyField)value).getLocalAmount());
                this.currencyFieldUtil.setCurrencyFieldLocalCurrency(field, ((ICurrencyField)value).getLocalCurrency());
            }
        }

    }

    public void setFieldValue(IGRCObject object, String fieldInfo, String value) throws Exception {
        if (CommonUtil.isNotNullOrEmpty(fieldInfo) && fieldInfo.contains("System Fields:")) {
            if (CommonUtil.isEqualIgnoreCase(fieldInfo, "System Fields:Description")) {
                object.setDescription(value);
            }
        } else {
            this.setFieldValue(this.getField(object, fieldInfo), value);
        }

    }

    public void setFieldValueWithNull(IField field) throws Exception {
        if (this.isFieldNotNull(field)) {
            if (DataType.INTEGER_TYPE.equals(field.getDataType())) {
                this.integerFieldUtil.getIntegerField(field).setValue((Integer)null);
            } else if (DataType.FLOAT_TYPE.equals(field.getDataType())) {
                this.floatFieldUtil.getFloatField(field).setValue((Double)null);
            } else if (this.stringFieldUtil.isStringFieldDataType(field)) {
                this.stringFieldUtil.setStringFieldValue((IStringField)field, (String)null);
            } else if (DataType.DATE_TYPE.equals(field.getDataType())) {
                this.dateFieldUtil.getDateField((IDateField)field).setValue((Date)null);
            } else if (DataType.ENUM_TYPE.equals(field.getDataType())) {
                this.enumFieldUtil.getEnumField((IEnumField)field).setEnumValue((IEnumValue)null);
            } else if (DataType.CURRENCY_TYPE.equals(field.getDataType())) {
                this.currencyFieldUtil.getCurrencyField((ICurrencyField)field).setLocalAmount((Double)null);
                this.currencyFieldUtil.getCurrencyField((ICurrencyField)field).setLocalCurrency((ICurrency)null);
            }
        }

    }

    public void setFieldValueWithNull(IGRCObject object, String fieldInfo) throws Exception {
        this.setFieldValueWithNull(this.getField(object, fieldInfo));
    }

    public boolean isFieldValueChanged(IGRCObject object, String fieldInfo) throws Exception {
        boolean hasChanged = false;
        List<IField> modifiedFields = this.getModifiedFields(object);
        Iterator var5 = modifiedFields.iterator();

        while(var5.hasNext()) {
            IField modifiedField = (IField)var5.next();
            if (modifiedField.getName().equals(fieldInfo)) {
                hasChanged = true;
                break;
            }
        }

        return hasChanged;
    }

    public boolean isAllorAnyFieldValueChanged(IGRCObject object, List<String> fieldNamesList, String checkFor) throws Exception {
        boolean isAllOrAnyFieldsChanged = false;
        if (CommonUtil.isListNotNullOrEmpty(fieldNamesList)) {
            isAllOrAnyFieldsChanged = CommonUtil.isEqualIgnoreCase(checkFor, CheckFor.CHECK_ALL.toString());
            Iterator var5 = fieldNamesList.iterator();

            label40:
            do {
                String fieldName;
                do {
                    do {
                        if (!var5.hasNext()) {
                            return isAllOrAnyFieldsChanged;
                        }

                        fieldName = (String)var5.next();
                    } while(!CommonUtil.isNotNullOrEmpty(fieldName));

                    if (CommonUtil.isEqualIgnoreCase(checkFor, CheckFor.CHECK_ALL.toString())) {
                        isAllOrAnyFieldsChanged = this.isFieldValueChanged(object, fieldName) && isAllOrAnyFieldsChanged;
                        continue label40;
                    }
                } while(!CommonUtil.isEqualIgnoreCase(checkFor, CheckFor.CHECK_ANY.toString()) || !this.isFieldValueChanged(object, fieldName));

                isAllOrAnyFieldsChanged = true;
                break;
            } while(isAllOrAnyFieldsChanged);
        }

        return isAllOrAnyFieldsChanged;
    }

    public boolean isAllOrAnyFieldHasValues(IGRCObject object, List<String> fieldNamesList, String checkFor) throws Exception {
        boolean isAllOrAnyEnumFieldHasValues = false;
        if (CommonUtil.isListNotNullOrEmpty(fieldNamesList)) {
            isAllOrAnyEnumFieldHasValues = CommonUtil.isEqualIgnoreCase(checkFor, CheckFor.CHECK_ALL.toString());
            Iterator var5 = fieldNamesList.iterator();

            label40:
            do {
                IField field;
                do {
                    do {
                        if (!var5.hasNext()) {
                            return isAllOrAnyEnumFieldHasValues;
                        }

                        String fieldName = (String)var5.next();
                        field = object.getField(fieldName);
                    } while(!CommonUtil.isObjectNotNull(field));

                    if (CommonUtil.isEqualIgnoreCase(checkFor, CheckFor.CHECK_ALL.toString())) {
                        isAllOrAnyEnumFieldHasValues = !field.isNull() && isAllOrAnyEnumFieldHasValues;
                        continue label40;
                    }
                } while(!CommonUtil.isEqualIgnoreCase(checkFor, CheckFor.CHECK_ANY.toString()) || field.isNull());

                isAllOrAnyEnumFieldHasValues = true;
                break;
            } while(isAllOrAnyEnumFieldHasValues);
        }

        return isAllOrAnyEnumFieldHasValues;
    }

    public boolean isAllOrAnyFieldHasValues(IGRCObject object, String fieldNamesList, String checkFor) throws Exception {
        return this.isAllOrAnyFieldHasValues(object, CommonUtil.parseDelimitedValues(fieldNamesList, ","), checkFor);
    }

    public boolean isAllFieldsHasMatchingValues(IGRCObject object, List<String> fieldNamesList, List<String> matchingFieldValuesList) throws Exception {
        int count = 0;
        boolean isSpecialEvent = true;
        String fieldValue = "";

        for(Iterator var7 = fieldNamesList.iterator(); var7.hasNext(); ++count) {
            String specialEventsFldInfo = (String)var7.next();
            fieldValue = this.getFieldValueAsString(object, specialEventsFldInfo);
            if (!CommonUtil.isEqual(fieldValue, (String)matchingFieldValuesList.get(count))) {
                isSpecialEvent = false;
                break;
            }
        }

        return isSpecialEvent;
    }

    public Double getNumericFieldValueAsDouble(IField field) throws Exception {
        Double numbericFieldValue = null;
        this.logger.debug("Is Field Value null: " + CommonUtil.isObjectNotNull(field));
        this.logger.debug("Field Data type: " + (CommonUtil.isObjectNotNull(field) ? field.getDataType() : "Null Field"));
        if (DataType.INTEGER_TYPE.equals(field.getDataType()) && this.integerFieldUtil.isIntegerFieldValueNotNull(field)) {
            numbericFieldValue = new Double(this.integerFieldUtil.getIntegerFieldValueAsDouble(field));
        } else if (DataType.FLOAT_TYPE.equals(field.getDataType()) && this.floatFieldUtil.isFloatFieldValueNotNull(field)) {
            numbericFieldValue = new Double(this.floatFieldUtil.getFloatFieldValueAsDouble(field));
        }

        return numbericFieldValue;
    }

    public Double getNumericFieldValueAsDouble(IGRCObject object, String fieldInfo) throws Exception {
        return this.getNumericFieldValueAsDouble(this.getField(object, fieldInfo));
    }

    public void displayAllFieldsInObject(IGRCObject object) throws Exception {
        List<IField> fields = object.getFields();
        Iterator var3 = fields.iterator();

        while(var3.hasNext()) {
            IField field = (IField)var3.next();
            this.logger.debug("Is Field Null: " + CommonUtil.isObjectNotNull(field));
            this.logger.debug("Field Name: " + field.getName());
            this.logger.debug("Field DataType: " + field.getDataType());
        }

    }

    public List<String> getFieldNamesAsList(List<IField> fields) {
        List<String> fieldNames = null;
        fieldNames = new ArrayList();
        if (CommonUtil.isListNotNullOrEmpty(fields)) {
            Iterator var3 = fields.iterator();

            while(var3.hasNext()) {
                IField field = (IField)var3.next();
                fieldNames.add(field.getName());
            }
        }

        return fieldNames;
    }

    public List<IField> getFieldsFromFieldNamesList(IGRCObject object, List<String> fieldsInfoList) throws Exception {
        List<IField> fieldsList = null;
        fieldsList = new ArrayList();
        Iterator var4 = fieldsInfoList.iterator();

        while(var4.hasNext()) {
            String fieldInfo = (String)var4.next();
            fieldsList.add(this.getField(object, fieldInfo));
        }

        return fieldsList;
    }

    public List<String> getFieldValuesFromFieldNamesList(List<IField> fieldsInfoList) throws Exception {
        List<String> fieldValuesList = null;
        fieldValuesList = new ArrayList();
        Iterator var3 = fieldsInfoList.iterator();

        while(var3.hasNext()) {
            IField field = (IField)var3.next();
            fieldValuesList.add(this.getFieldValueAsString(field));
        }

        return fieldValuesList;
    }

    public List<String> getFieldValuesFromFieldNamesList(IGRCObject object, List<String> fieldsInfoList) throws Exception {
        List<String> fieldValuesList = null;
        fieldValuesList = new ArrayList();
        Iterator var4 = fieldsInfoList.iterator();

        while(var4.hasNext()) {
            String fieldInfo = (String)var4.next();
            fieldValuesList.add(this.getFieldValueAsString(object, fieldInfo));
        }

        return fieldValuesList;
    }

    public boolean isFieldValueChangedToExpectedCondition(FieldValueChangeOnConditionInfo fieldValueChangeOnConditionInfo) throws Exception {
        int index = 0;
        boolean isApplicable = false;
        boolean returnValForAll = true;
        boolean returnValForAny = false;
        String fieldValue = "";
        List<List<?>> listofLists = null;
        listofLists = new ArrayList();
        listofLists.add(fieldValueChangeOnConditionInfo.getFieldsInfoList());
        listofLists.add(fieldValueChangeOnConditionInfo.getFieldValuesList());
        listofLists.add(fieldValueChangeOnConditionInfo.getConditionsList());
        if (CommonUtil.isListsOfTheSameSize(listofLists)) {
            for(Iterator var8 = fieldValueChangeOnConditionInfo.getFieldsInfoList().iterator(); var8.hasNext(); ++index) {
                String fieldInfo = (String)var8.next();
                boolean flag = false;
                this.getFieldValueAsString(fieldValueChangeOnConditionInfo.getObject(), fieldInfo);
                if (Comparators.EQUAL.equals(fieldValueChangeOnConditionInfo.getConditionsList().get(index)) && CommonUtil.isEqualIgnoreCase(fieldValue, (String)fieldValueChangeOnConditionInfo.getFieldValuesList().get(index))) {
                    if (CheckFor.CHECK_ANY.equals(fieldValueChangeOnConditionInfo.getCheckFor())) {
                        returnValForAny = true;
                        break;
                    }

                    if (CheckFor.CHECK_ALL.equals(fieldValueChangeOnConditionInfo.getCheckFor())) {
                        flag = true;
                    }
                } else if (Comparators.NOT_EQUAL.equals(fieldValueChangeOnConditionInfo.getConditionsList().get(index)) && CommonUtil.isEqualIgnoreCase(fieldValue, (String)fieldValueChangeOnConditionInfo.getFieldValuesList().get(index))) {
                    if (CheckFor.CHECK_ANY.equals(fieldValueChangeOnConditionInfo.getCheckFor())) {
                        returnValForAny = true;
                        break;
                    }

                    if (CheckFor.CHECK_ALL.equals(fieldValueChangeOnConditionInfo.getCheckFor())) {
                        flag = true;
                    }
                }

                if (CheckFor.CHECK_ALL.equals(fieldValueChangeOnConditionInfo.getCheckFor()) && !flag) {
                    returnValForAll = false;
                    break;
                }
            }
        }

        if (CheckFor.CHECK_ALL.equals(fieldValueChangeOnConditionInfo.getCheckFor())) {
            isApplicable = returnValForAll;
        } else {
            if (!CheckFor.CHECK_ANY.equals(fieldValueChangeOnConditionInfo.getCheckFor())) {
                return false;
            }

            isApplicable = returnValForAny;
        }

        return isApplicable;
    }

    public boolean hasValueChangedForFields(FieldValueChangeInfo fieldValueChangeInfo) throws Exception {
        boolean isApplicable = false;
        boolean returnValForAll = true;
        boolean returnValForAny = false;
        Iterator var5 = fieldValueChangeInfo.getFieldNamesList().iterator();

        while(var5.hasNext()) {
            String field = (String)var5.next();
            boolean flag = false;
            Iterator var8 = fieldValueChangeInfo.getModifiedFields().iterator();

            while(var8.hasNext()) {
                IField modifiedField = (IField)var8.next();
                if (modifiedField.getName().equals(field)) {
                    if (CheckFor.CHECK_ANY.equals(fieldValueChangeInfo.getCheckFor())) {
                        returnValForAny = true;
                        break;
                    }

                    if (CheckFor.CHECK_ALL.equals(fieldValueChangeInfo.getCheckFor())) {
                        flag = true;
                    }
                } else if (CheckFor.CHECK_ALL.equals(fieldValueChangeInfo.getCheckFor())) {
                    flag = false;
                    break;
                }
            }

            if (CheckFor.CHECK_ALL.equals(fieldValueChangeInfo.getCheckFor()) && !flag) {
                returnValForAll = false;
                break;
            }
        }

        if (CheckFor.CHECK_ALL.equals(fieldValueChangeInfo.getCheckFor())) {
            isApplicable = returnValForAll;
        } else {
            if (!CheckFor.CHECK_ANY.equals(fieldValueChangeInfo.getCheckFor())) {
                return false;
            }

            isApplicable = returnValForAny;
        }

        return isApplicable;
    }

    public List<IField> getModifiedFields(IGRCObject object) throws Exception {
        return ResourceUtil.getModifiedFields(object);
    }

    public List<IField> getModifiedFieldsMatchingGivenFields(FieldValueChangeInfo fieldValueChangeInfo) throws Exception {
        List<IField> changedFields = null;
        changedFields = new ArrayList();
        Iterator var3 = fieldValueChangeInfo.getFieldNamesList().iterator();

        while(var3.hasNext()) {
            String field = (String)var3.next();
            Iterator var5 = fieldValueChangeInfo.getModifiedFields().iterator();

            while(var5.hasNext()) {
                IField modifiedField = (IField)var5.next();
                if (modifiedField.getName().equals(field)) {
                    changedFields.add(modifiedField);
                }
            }
        }

        return changedFields;
    }

    public List<String> getModifiedFieldsInfoMatchingGivenFields(FieldValueChangeInfo fieldValueChangeInfo) throws Exception {
        List<String> changedFields = null;
        changedFields = new ArrayList();
        Iterator var3 = fieldValueChangeInfo.getFieldNamesList().iterator();

        while(var3.hasNext()) {
            String field = (String)var3.next();
            Iterator var5 = fieldValueChangeInfo.getModifiedFields().iterator();

            while(var5.hasNext()) {
                IField modifiedField = (IField)var5.next();
                if (modifiedField.getName().equals(field)) {
                    changedFields.add(modifiedField.getName());
                }
            }
        }

        return changedFields;
    }

    public boolean compareFields(IField compareField1, String compareField2, String comparator) throws Exception {
        return this.compareFields(this.getFieldValueAsString(compareField1), compareField2, compareField1.getDataType().toString(), comparator);
    }

    public boolean compareFields(String compareField1, IField compareField2, String comparator) throws Exception {
        return this.compareFields(compareField1, this.getFieldValueAsString(compareField2), compareField2.getDataType().toString(), comparator);
    }

    public boolean compareFields(IField compareField1, IField compareField2, String comparator) throws Exception {
        return this.compareFields(this.getFieldValueAsString(compareField1), this.getFieldValueAsString(compareField2), compareField1.getDataType().toString(), comparator);
    }

    public boolean compareFields(String compareFieldValue1, String compareFieldValue2, String dataType, String comparator) throws Exception {
        boolean isCompareField = false;
        if (!DataType.FLOAT_TYPE.toString().equals(dataType) && !DataType.INTEGER_TYPE.toString().equals(dataType) && !DataType.CURRENCY_TYPE.toString().equals(dataType)) {
            if (!DataType.STRING_TYPE.toString().equals(dataType) && !DataType.ENUM_TYPE.toString().equals(dataType)) {
                if (DataType.DATE_TYPE.toString().equals(dataType)) {
                    Calendar calField1 = DateUtil.convertDateStringToCalendar(compareFieldValue1);
                    Calendar calField2 = DateUtil.convertDateStringToCalendar(compareFieldValue2);
                    if (CommonUtil.isObjectNotNull(calField1) && CommonUtil.isObjectNotNull(calField2)) {
                        if (CommonUtil.isEqualIgnoreCase(Comparators.LESSER_THAN.toString(), comparator)) {
                            isCompareField = calField2.compareTo(calField1) < 0;
                        } else if (CommonUtil.isEqualIgnoreCase(Comparators.LESSER_THAN_OR_EQUAL.toString(), comparator)) {
                            isCompareField = calField2.compareTo(calField1) <= 0;
                        } else if (CommonUtil.isEqualIgnoreCase(Comparators.GREATER_THAN.toString(), comparator)) {
                            isCompareField = calField2.compareTo(calField1) > 0;
                        } else if (CommonUtil.isEqualIgnoreCase(Comparators.GREATER_THAN_OR_EQUAL.toString(), comparator)) {
                            isCompareField = calField2.compareTo(calField1) >= 0;
                        } else if (CommonUtil.isEqualIgnoreCase(Comparators.EQUAL.toString(), comparator)) {
                            isCompareField = calField2.compareTo(calField1) == 0;
                        } else if (CommonUtil.isEqualIgnoreCase(Comparators.NOT_EQUAL.toString(), comparator)) {
                            isCompareField = calField2.compareTo(calField1) != 0;
                        }
                    }
                } else if (DataType.MULTI_VALUE_ENUM.toString().equals(dataType)) {
                    new ArrayList();
                    List<String> multiValues = CommonUtil.parseCommaDelimitedValues(compareFieldValue2);
                    isCompareField = multiValues.contains(compareFieldValue1);
                }
            } else if (CommonUtil.isEqualIgnoreCase(Comparators.EQUAL.toString(), comparator)) {
                isCompareField = CommonUtil.isEqualIgnoreCase(compareFieldValue1, compareFieldValue2);
            } else if (CommonUtil.isEqualIgnoreCase(Comparators.NOT_EQUAL.toString(), comparator)) {
                isCompareField = CommonUtil.isNotEqualIgnoreCase(compareFieldValue1, compareFieldValue2);
            }
        } else {
            isCompareField = NumericUtil.compareValues(compareFieldValue1, compareFieldValue2, Comparators.valueOf(comparator));
        }

        this.logger.debug("Returning: " + isCompareField);
        return isCompareField;
    }

    public List<String> getLocalizedLabelsForFields(IGRCObject object, List<String> fieldInfoList) {
        this.logger.debug("Object Name: " + object.getName());
        this.logger.debug("Object Type: " + object.getType());
        return this.getLocalizedLabelsForFields(object.getType(), fieldInfoList);
    }

    public List<String> getLocalizedLabelsForFields(String objectType, List<String> fieldInfoList) {
        ITypeDefinition definition = null;
        if (CommonUtil.isNotNullOrEmpty(objectType)) {
            definition = this.serviceFactoryProxy.createMetaDataService().getType(objectType);
        }

        return this.getLocalizedLabelsForFields(definition, fieldInfoList);
    }

    public List<String> getLocalizedLabelsForFields(ITypeDefinition objectType, List<String> fieldInfoList) {
        this.logger.debug("getLocalizedLabelsForFields() START");
        List<String> fieldLabelsList = new ArrayList();
        if (CommonUtil.isObjectNotNull(objectType)) {
            this.logger.debug("Object Type: " + objectType.getName());
            this.logger.debug("Fields Info List: " + fieldInfoList);
            Iterator var4 = fieldInfoList.iterator();

            while(var4.hasNext()) {
                String fieldName = (String)var4.next();
                fieldName = fieldName.trim();
                fieldName = fieldName.replaceFirst("System Fields:", "");
                List<IFieldDefinition> iFieldDefinitions = objectType.getFieldsDefinition();
                Iterator var7 = iFieldDefinitions.iterator();

                while(var7.hasNext()) {
                    IFieldDefinition field = (IFieldDefinition)var7.next();
                    if (fieldName.equals(field.getName())) {
                        this.logger.debug("Field Name in list: " + fieldName);
                        this.logger.debug("Field Name from object: " + field.getName());
                        this.logger.debug("Are fields equal: " + fieldName.equals(field.getName()));
                        fieldLabelsList.add(field.getLocalizedLabel());
                    }
                }
            }
        }

        this.logger.debug("Localized labels obtained: " + fieldLabelsList);
        this.logger.debug("getLocalizedLabelsForFields() END");
        return fieldLabelsList;
    }
}

