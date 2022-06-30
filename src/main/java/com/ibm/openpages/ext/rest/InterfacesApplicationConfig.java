package com.ibm.openpages.ext.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.ibm.openpages.ext.interfaces.icaps")
@ComponentScan("com.ibm.openpages.ext.interfaces.cmp")
@ComponentScan("com.ibm.openpages.ext.interfaces.common")
public class InterfacesApplicationConfig {
    @Bean
    public Logger getLogger(){
        return LogManager.getLogger(InterfacesApplicationConfig.class);
    }
}


