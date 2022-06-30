package com.ibm.openpages.ext.interfaces.common.constant;

import com.ibm.openpages.api.logging.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * <p>
 * This class contains the registry constants used by the validation engine.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 11-11-2021
 */
public abstract class RegistryConstants {
    public static final String ENGINE_METADATA_INTERFACES_PATH = "/OpenPages/Custom Deliverables/Interfaces";

    public List<String> registryConstants;

    public abstract List<String> getRegistryConstants();

    public abstract void init();
}
