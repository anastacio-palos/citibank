package com.ibm.openpages.ext.interfaces.cmp.constant;

import com.ibm.openpages.api.logging.LoggerFactory;
import com.ibm.openpages.ext.interfaces.common.constant.RegistryConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class EntitlementRegistryConstants extends RegistryConstants {
    public static final String ENGINE_METADATA_INTERFACES_CMP_PATH = ENGINE_METADATA_INTERFACES_PATH + "/CMP/";
    public static final String ENGINE_METADATA_JSON_FILE = ENGINE_METADATA_INTERFACES_CMP_PATH + "meta-validations-engine.json";
    public static final String ENGINE_METADATA_IA_DIVISION_ROLE_TEMPLATES = ENGINE_METADATA_INTERFACES_CMP_PATH + "ia_division_role_templates";
    public static final String ENGINE_METADATA_LEAD_PRODUCT_TEAM_MANDATORY_GROUPS = ENGINE_METADATA_INTERFACES_CMP_PATH + "lead_team_mandatory_groups";
    public static final String ENGINE_METADATA_DEFAULT_PASSWORD = ENGINE_METADATA_INTERFACES_CMP_PATH + "default_password";
    public static final String ENGINE_METADATA_DEFAULT_ISO_CODE = ENGINE_METADATA_INTERFACES_CMP_PATH + "default_iso_code";
    public static final String ENGINE_METADATA_DEFAULT_NOT_REMOVE_GROUPS = ENGINE_METADATA_INTERFACES_CMP_PATH + "notRemoveGroups";
    public static final String ENGINE_METADATA_DOMAIN_PREFIX = ENGINE_METADATA_INTERFACES_CMP_PATH + "domain_prefix";
    public static final String ENGINE_METADATA_AUDITOR_ROLE_ROLE_TEMPLATES = ENGINE_METADATA_INTERFACES_CMP_PATH + "auditor_group_role_templates";
    public static final String ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_MANDATORY_GROUPS = ENGINE_METADATA_INTERFACES_CMP_PATH + "product_function_division_mandatory_groups";
    public static final String ENGINE_METADATA_HELP_DESK = ENGINE_METADATA_INTERFACES_CMP_PATH + "gida_isa_group_name";
    public static final String ENGINE_METADATA_LEAD_PRODUCT_TEAM_FIELD = ENGINE_METADATA_INTERFACES_CMP_PATH + "lead_team_field";
    public static final String ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_FIELD = ENGINE_METADATA_INTERFACES_CMP_PATH + "product_function_division_field";

    public static final String ENGINE_METADATA_VALID_OBJECT_ENTITIES_BY_LEVEL = ENGINE_METADATA_INTERFACES_CMP_PATH +"object_entities_by_level_query";
    public static final String ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_ENTITY_LEVEL = ENGINE_METADATA_INTERFACES_CMP_PATH +"product_function_entity_level_group_parameter";
    public static final String ENGINE_METADATA_LEAD_PRODUCT_TEAM_ENTITY_LEVEL = ENGINE_METADATA_INTERFACES_CMP_PATH +"lead_team_query_parameter";
    public static final String ENGINE_METADATA_GENERAL_PRODUCT_TEAM_QUERY = ENGINE_METADATA_INTERFACES_CMP_PATH + "both_product_function_lead_team_query";
    public static final String ENGINE_METADATA_VALID_PROFILES = ENGINE_METADATA_INTERFACES_CMP_PATH + "validProfiles";
    public static final String ENGINE_METADATA_BOTH_ENTITIES_BY_USER_AND_TYPE_QUERY = ENGINE_METADATA_INTERFACES_CMP_PATH + "entities_by_user_and_type_query";

    public static final String ENABLE_DEBUG = ENGINE_METADATA_INTERFACES_CMP_PATH + "enable_debug_mode";
    public static final String LOG_FILE_PATH = ENGINE_METADATA_INTERFACES_CMP_PATH + "log_file_path";
    public static final String LOG_FILE_SIZE = ENGINE_METADATA_INTERFACES_CMP_PATH + "log_file_size";
    public static final String LOG_LEVEL = ENGINE_METADATA_INTERFACES_CMP_PATH + "log_level";
    //public static final String ENGINE_METADATA_BUSINESS_ENTITY = ENGINE_METADATA_INTERFACES_CMP_PATH + "business_entity_type";
    public static final String ENGINE_METADATA_VALID_AUDIT_GROUPS = ENGINE_METADATA_INTERFACES_CMP_PATH +"product_function_valid_entities";
    // DELETED public static final String ENGINE_METADATA_IA_DIVISION_QUERY = ENGINE_METADATA_INTERFACES_CMP_PATH + "ia_division_entity_user_query";
    public static final String ENGINE_METADATA_VALID_IA_DIVISION = ENGINE_METADATA_INTERFACES_CMP_PATH + "lead_team_valid_entities";

    private static final Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger(EntitlementRegistryConstants.class);

    public void init() {
        logger.debug("***** EntitlementRegistryConstants - init START *****");
        if(registryConstants == null)
            registryConstants = new ArrayList<>();
        registryConstants.add(ENGINE_METADATA_JSON_FILE);
        registryConstants.add(ENGINE_METADATA_IA_DIVISION_ROLE_TEMPLATES);
        registryConstants.add(ENGINE_METADATA_LEAD_PRODUCT_TEAM_MANDATORY_GROUPS);
        registryConstants.add(ENGINE_METADATA_DEFAULT_PASSWORD);
        registryConstants.add(ENGINE_METADATA_DEFAULT_ISO_CODE);
        registryConstants.add(ENGINE_METADATA_DEFAULT_NOT_REMOVE_GROUPS);
        registryConstants.add(ENGINE_METADATA_DOMAIN_PREFIX);
        registryConstants.add(ENGINE_METADATA_AUDITOR_ROLE_ROLE_TEMPLATES);
        registryConstants.add(ENGINE_METADATA_PRODUCT_FUNCTION_TEAM_MANDATORY_GROUPS);
        registryConstants.add(ENGINE_METADATA_HELP_DESK);
        registryConstants.add(ENGINE_METADATA_VALID_IA_DIVISION);
        registryConstants.add(ENGINE_METADATA_VALID_PROFILES);
        logger.debug("registryConstants=" + registryConstants);
        logger.debug("***** EntitlementRegistryConstants - init END *****");
    }

    public List<String> getRegistryConstants() {
        if (registryConstants == null) {
            init();
        }
        return registryConstants;
    }
}
