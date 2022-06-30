package com.ibm.openpages.ext.interfaces.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.bean.ConstraintBean;
import com.ibm.openpages.ext.interfaces.common.bean.EngineMetadataBean;
import com.ibm.openpages.ext.interfaces.common.bean.FieldBean;
import com.ibm.openpages.ext.interfaces.common.constant.EngineConstants;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * The <code>EngineRegistry</code> is a singleton instance with the settings used in Openpages to CMP / iCAPS interfaces.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 10-27-2021
 */
public class EngineRegistry {
    private static IServiceFactory apiFactory;
    private static IConfigurationService configService;
    private Logger logger;
    private Map<String, String> propertyMap;
    private Map<String, ConstraintBean> constraintMap;
    private Map<String, EngineMetadataBean> metadataMap;


    public EngineRegistry() {
        super();
    }

    /**
     * <p>
     *     Initializes the engine registry singleton instance
     * </p>
     * @param apiFactory
     *              an instance of {@link IServiceFactory}.
     * @param registryConstants
     *              list of registry constants to be initialized
     * @param logger
     *              an instance of {@link Logger}.
     */
    public void init(IServiceFactory apiFactory, List<String> registryConstants, Logger logger) {
        this.logger = logger;
        if (propertyMap == null) {
            this.apiFactory = apiFactory;
            configService = this.apiFactory.createConfigurationService();
            logger.trace(" >>>>>    ******  Initializing EngineRegistry  *******    <<<<<<<");
            propertyMap = new HashMap<>();
            for (String constant : registryConstants) {
                String properyValue = configService.getConfigProperties().getProperty(constant);
                logger.trace("Added " + constant + " From property " + properyValue);
                putProperty(constant, properyValue);
            }
            logger.trace("Map content: " + propertyMap.toString());
        }
        if (constraintMap == null) {
            constraintMap = new HashMap<>();
        }
        if(metadataMap == null){
            metadataMap = new HashMap<>();
        }
    }

    /**
     * <p>
     *     Reset the engine registry singleton instance collections.
     * </p>
     */
    public void reset(){
        propertyMap = null;
        constraintMap = null;
        metadataMap = null;
    }

    /**
     * <p>
     *     retrieve a property from Openpages Registry and put it on the engine singleton property map. On a second invocation
     *     it is retrieved from the singleton property map.
     * </p>
     * @param key
     *              The key of the property to be retrieved
     * @return
     *              a {@link String} with the retrieved property
     */
    public String getProperty(String key) {
        String property = this.propertyMap.get(key);
        if (property == null) {
            logger.trace("Registering new registry property = " + key);
            String newProperty = configService.getConfigProperties().getProperty(key);
            if (newProperty != null) {
                putProperty(key, newProperty);
                property = newProperty;
            }
        }
        return property;
    }

    /**
     * <p>
     *     Put a property on the engine singleton property map.
     * </p>
     * @param key
     *              The key of the property to be inserted
     * @param value
     *              Value of the property
     */
    public void putProperty(String key, String value) {
        this.propertyMap.put(key, value);
        logger.trace("Successfully registered property with key = " + key);
    }

    /**
     * <p>
     *     Put the field's constraints on the engine singleton constraint map.
     * </p>
     * @param fieldBean
     *              an instance of {@link FieldBean} with the constraints to be processed.
     * @return
     *              an instance of {@link List<ConstraintBean>}.
     */
    private List<ConstraintBean> putConstraint(FieldBean fieldBean) {
        List<ConstraintBean> constraintList = new ArrayList<>();
        for (ConstraintBean constraintBean : fieldBean.getRestriction().getConstraints()) {
            constraintList.add(getConstraint(constraintBean.getConstraintKey(), constraintBean.getConstraintRegistryProperty(),
                    constraintBean.getConstraintType()));
        }
        return constraintList;
    }

    /**
     * <p>
     *     Put a constraint on the engine singleton constraint map.
     * </p>
     * @param constraintKey
     *              the constraint key
     * @param constraintRegistryProperty
     *              the constraint related property
     * @param constraintType
     *              the constraint type
     * @return
     *              an instance of {@link ConstraintBean}.
     */
    public ConstraintBean putConstraint(String constraintKey, String constraintRegistryProperty, String constraintType) {
        ConstraintBean constraint = getConstraint(constraintKey, constraintRegistryProperty, constraintType);
        return constraint;
    }

    /**
     * <p>
     *     Retrieve a constraint from the engine singleton constraint map. If it does not exists it is inserted.
     * </p>
     * @param constraintKey
     *              the constraint key
     * @param constraintRegistryProperty
     *              the constraint related property
     * @param constraintType
     *              the constraint type
     * @return
     *              an instance of {@link ConstraintBean}.
     */
    public ConstraintBean getConstraint(String constraintKey, String constraintRegistryProperty, String constraintType) {
        logger.trace("constraintKey="+constraintKey);
        logger.trace("constraintRegistryProperty="+constraintRegistryProperty);
        logger.trace("constraintType="+constraintType);
        Object constraintData = null;
        String registryConstraintPropertyValue = null;

        ConstraintBean constraint = constraintMap.get(constraintKey);
        if(constraint == null){
            constraint = new ConstraintBean();
            constraint.setConstraintKey(constraintKey);
            constraint.setConstraintRegistryProperty(constraintRegistryProperty);
            constraint.setConstraintType(constraintType);
            registryConstraintPropertyValue = getProperty(constraintRegistryProperty);
            logger.trace("registryConstraintPropertyValue="+registryConstraintPropertyValue);
            if (registryConstraintPropertyValue != null) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    if (EngineConstants.LIST_TYPE.equals(constraintType)) {
                        constraintData = mapper.readValue(registryConstraintPropertyValue, List.class);
                    } else if (EngineConstants.MAP_TYPE.equals(constraintType)) {
                        constraintData = mapper.readValue(registryConstraintPropertyValue, Map.class);
                    }
                    constraint.setConstraintData(constraintData);
                    logger.trace("Successfully registered constraint");
                } catch (IOException e) {
                    logger.trace("Error registering constraint", e);
                }
            }
            constraintMap.put(constraintKey, constraint);
        }

        if (constraint != null && EngineConstants.QUERY_MAP_TYPE.equals(constraintType)) {
            registryConstraintPropertyValue = getProperty(constraintRegistryProperty);
            logger.trace("registryConstraintPropertyValue="+registryConstraintPropertyValue);
            constraintData = ServiceUtil.queryMap(apiFactory, registryConstraintPropertyValue);
            constraint.setConstraintData(constraintData);

        }
        return constraint;
    }

    /**
     * <p>
     *     Retrieve a constraint from the engine singleton constraint map.
     * </p>
     * @param constraintKey
     *              the constraint key
     * @return
     *              an instance of {@link ConstraintBean}.
     */
    public ConstraintBean getConstraint(String constraintKey){
        logger.trace("constraintKey="+constraintKey);
        Object constraintData = null;
        String registryConstraintPropertyValue = null;
        ConstraintBean constraint = constraintMap.get(constraintKey);
        if (constraint != null && EngineConstants.QUERY_MAP_TYPE.equals(constraint.getConstraintType())) {
            registryConstraintPropertyValue = getProperty(constraint.getConstraintRegistryProperty());
            logger.trace("registryConstraintPropertyValue="+registryConstraintPropertyValue);
            constraintData = ServiceUtil.queryMap(apiFactory, registryConstraintPropertyValue);
            constraint.setConstraintData(constraintData);
        }
        return constraint;
    }

    public EngineMetadataBean putEngineMetadata(String engineMetadataJsonProperty) {
        logger.trace("putEngineMetadata property="+engineMetadataJsonProperty);
        EngineMetadataBean metadata = metadataMap.get(engineMetadataJsonProperty);
        if(metadata == null){
            String engineMetadataJson = getProperty(engineMetadataJsonProperty);
            logger.trace("putEngineMetadata engineMetadataJson="+engineMetadataJson);
            if(engineMetadataJson != null) {
                try {
                    metadata = EngineUtil.jsonToMetaEngineBean(engineMetadataJson, logger);
                } catch (IOException e) {
                    logger.error("Error during the transformation",e);
                }
                String name = metadata.getName();
                metadataMap.put(name, metadata);
            }
        }
        return metadata;
    }

    public EngineMetadataBean getEngineMetadata(String name){
        logger.trace("getEngineMetadata "+ name);
        return metadataMap.get(name);
    }
}
