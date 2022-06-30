package com.ibm.openpages.ext.interfaces.cmp.util;

import com.ibm.openpages.ext.interfaces.common.util.EngineRegistry;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class EntitlementEngineRegistry extends EngineRegistry {
    public EntitlementEngineRegistry(){
        super();
    }
}
