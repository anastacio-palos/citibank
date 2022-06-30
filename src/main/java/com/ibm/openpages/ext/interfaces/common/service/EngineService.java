package com.ibm.openpages.ext.interfaces.common.service;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.bean.EngineResultsBean;
import com.ibm.openpages.ext.interfaces.common.util.EngineRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * <p>
 * This service have must of the validations applied to Openpages to CMP / iCAPS interfaces.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 11-11-2021
 */
public interface EngineService {

    /**
     * <p>
     * Use this to validate a json String using the configurations retrieved from the metadata configuration json.
     * </p>
     * @param data
     *              json String to validate
     * @param engineRegistry
     *              an instance of {@link EngineRegistry}.
     * @param metadataProperty
     *              metadata configuration json needed for this validation
     * @param apiFactory
     *              an instance of {@link IServiceFactory}.
     * @param logger
     *              an instance of {@link Logger}.
     * @return an instance of {@link EngineResultsBean} with the validation results.
     */
    public EngineResultsBean validateAndProcessPayload(String data, EngineRegistry engineRegistry,
                                                       String metadataProperty, IServiceFactory apiFactory, Logger logger);


    /**
     * <p>
     * Use this to validate a json String using the configurations retrieved from the metadata configuration json.
     * This supports more than one metadata configuration file.
     * </p>
     * @param data
     *              json String to validate
     * @param engineRegistry
     *              an instance of {@link EngineRegistry}.
     * @param metadataMainPropertyName
     *              main metadata configuration json used for this validation
     * @param metadataProperties
     *              metadata configurations json needed for this validation
     * @param apiFactory
     *              an instance of {@link IServiceFactory}.
     * @param logger
     *              an instance of {@link Logger}.
     * @return
     */
    public EngineResultsBean validateAndProcessPayload(String data, EngineRegistry engineRegistry,
                                                       String metadataMainPropertyName, String [] metadataProperties,
                                                       IServiceFactory apiFactory, Logger logger);

    public Map<String,Object> extractAndGeneratePayload(IGRCObject issueObject, EngineRegistry engineRegistry,
                                                        String metadataMainPropertyName, String[] metadataProperties,
                                                        IServiceFactory apiFactory, Logger logger);
}
