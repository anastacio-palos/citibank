/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * � Copyright IBM Corporation 2021
 *
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.citi.workflow.actions;

import static com.ibm.openpages.ext.citi.workflow.constant.IncrementCounterConstants.INCREMENT_BY_PROPERTY;
import static com.ibm.openpages.ext.citi.workflow.constant.IncrementCounterConstants.INCREMENT_FAILED_EXCEPTION;
import static com.ibm.openpages.ext.citi.workflow.constant.IncrementCounterConstants.INPUT_FIELD_PROPERTY;
import static com.ibm.openpages.ext.citi.workflow.constant.IncrementCounterConstants.LOG_FILE_NAME;
import static com.ibm.openpages.ext.citi.workflow.constant.IncrementCounterConstants.TARGET_FIELD_PROPERTY;
import static com.ibm.openpages.ext.citi.workflow.constant.IncrementCounterConstants.UPDATE_PRIMARY_PARENT;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.INT_ZERO;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.TRUE;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isNotNullOrEmpty;
import static com.ibm.openpages.ext.tss.service.util.NumericUtil.getDoubleValueWithLocale;
import static com.ibm.openpages.ext.tss.service.util.NumericUtil.isNumeric;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import com.ibm.openpages.api.metadata.DataType;
import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.workflow.actions.IWFCustomProperty;
import com.ibm.openpages.api.workflow.actions.IWFOperationContext;
import com.ibm.openpages.ext.tss.service.logging.EXTLoggerFactoryImpl;
import com.ibm.openpages.ext.tss.workflow.action.BaseWorkflowCustomAction;

/**
 * <p>
 * The class implements the code for the custom action to increment a value from an input field based on an increment by
 * property and store the value in a target field. This custom action can be used in any workflow, but requires the
 * following properties to be set during the workflow configuration. Note: The property names should match exactly as
 * given below
 * <p>
 * input_field - Input field group and field name. Only 1 field group/field name. This field should be on the same
 * object as where the workflow is called from. Field group and field name should be separated by : Example:
 * OPSS-Issue:Counter
 * <p>
 * target_field - Target field group and field name that should be updated by the action. Only 1 field group/field name.
 * This field should be on the same object as where the workflow is called from. Field group and field name should be
 * separated by : Example: OPSS-Issue:Counter
 * <p>
 * increment_by - Integer / Decimal value to be incremented by Example: 1
 *
 * </P>
 *
 * @custom.feature : Workflow Custom Actions
 * @custom.category : Custom Actions
 */
public class IncrementCounterCustomAction extends BaseWorkflowCustomAction {

    // Class Level Variables
    private Log logger = null;

    /**
     * Constructor implementation for the AbstractCustomAction
     *
     * @param context
     * @param properties
     */
    public IncrementCounterCustomAction(IWFOperationContext context, List<IWFCustomProperty> properties) {

        super(context, properties);
    }

    /**
     * Default process method for the workflow to implement the increment logic.
     */
    @Override
    protected void process() throws Exception {

        // Method Level Variables
        double updateValue = 0.0;
        boolean isException = false;

        String updatedValue = null;
        String inputFieldInfo = null;
        String targetFieldInfo = null;
        String inputFieldValue = null;
        String exceptionMessage = null;
        String incrementByValue = null;
        String updateParentObj = null;

        IField targetField = null;
        IGRCObject currentObject = null;
        IWFOperationContext context = null;

        try {

            /* Initialize required services */
            initFieldUtilServices();
            initFieldUtilServices();
            initGRCObjectUtilServices();
            initApplicationUtilServices();

            // Initialize
            logger = new EXTLoggerFactoryImpl().getInstance(LOG_FILE_NAME);
            logger.debug("\nStarting IncrementCounterCustomAction...");

            /* Get the workflow context object, log it for information */
            context = getContext();
            updateParentObj = getPropertyValue(UPDATE_PRIMARY_PARENT);

            if (TRUE.equalsIgnoreCase(updateParentObj)) {
                logger.debug("Updating primary parent object...");
                currentObject = grcObjectUtil.getObjectFromId(context.getResource().getPrimaryParent());
                logger.debug("Object under workflow execution: " + currentObject.getName());
            }
            else {
                currentObject = context.getResource();
                logger.debug("Object under workflow execution: " + currentObject.getName());
            }


            /*
             * Get the incrementBy property, input field info and target field info from the workflow properties and log
             * it for information
             */
            inputFieldInfo = getPropertyValue(INPUT_FIELD_PROPERTY);
            targetFieldInfo = getPropertyValue(TARGET_FIELD_PROPERTY);
            incrementByValue = getPropertyValue(INCREMENT_BY_PROPERTY);
            inputFieldValue = fieldUtil.getFieldValueAsString(currentObject, inputFieldInfo);
            inputFieldValue = isNotNullOrEmpty(inputFieldValue) ? inputFieldValue : INT_ZERO + EMPTY_STRING;
            logger.debug(
                    "Input Field: " + inputFieldInfo + " whose value is " + inputFieldValue + ", will be incremented By: " + incrementByValue + ", on Target Field: " + targetFieldInfo);

            /* Validate the two information that needs to be numeric to perform the update. */
            isException = !(isNumeric(inputFieldValue) && isNumeric(incrementByValue));
            logger.debug("Checked if the Input Field Value and the IncrementByValue are Numeric: " + !isException);

            /* If no validation exception (both properties are numeric) then proceed to update the target field. */
            if (!isException) {

                /* Calculate the update value and log it for debug purposes. s */
                updateValue = getDoubleValueWithLocale(inputFieldValue) + getDoubleValueWithLocale(incrementByValue);
                logger.debug("Calculated value to update: " + updateValue);

                targetField = fieldUtil.getField(currentObject, targetFieldInfo);
                updatedValue = (DataType.INTEGER_TYPE.equals(targetField.getDataType())) ?
                        (int) updateValue + EMPTY_STRING :
                        updateValue + EMPTY_STRING;

                logger.debug("Value that will be updated: " + updatedValue);
                logger.debug("Target Field Data Type: " + targetField.getDataType());

                /* Set the value in the field in the object and save it. */
                fieldUtil.setFieldValue(targetField, updatedValue);
                grcObjectUtil.saveResource(currentObject);
                logger.debug("IncrementCounterCustomAction Completed.\n");
            }
        }
        catch (Exception ex) {

            /* On any other exception set the isException flag to true and soft log the exception and proceed. */
            isException = true;
            logger.error(
                    "EXCEPTION!!!!!!!!!!! An exception ocdured while incrementing the counter: " + getStackTrace(ex));
        }

        /*
         * If Exception, then proceed to get the application string and pass it on to the throw exception in the super
         * class which will take care of sending the exception message to the screen.
         */
        if (isException) {

            IField inputField = fieldUtil.getField(currentObject, inputFieldInfo);
            String inputFieldLabel = inputField.getFieldDefinition().getLocalizedLabel();
            String errorMsg = "\"" + inputFieldLabel + "\" of a object \"" + currentObject.getName() + "\"";
            if (TRUE.equalsIgnoreCase(updateParentObj)) {
                errorMsg = "\"" + inputFieldLabel + "\" of a parent object \"" + currentObject.getName() + "\"";
            }

            List<String> placeHolderValues = new ArrayList<String>();
            placeHolderValues.add(errorMsg);
            exceptionMessage = applicationUtil.getApplicationString(INCREMENT_FAILED_EXCEPTION, placeHolderValues);
            logger.error("Exception occured, throwing an exception " + exceptionMessage);
            throwException(exceptionMessage, null);
        }
    }
}
