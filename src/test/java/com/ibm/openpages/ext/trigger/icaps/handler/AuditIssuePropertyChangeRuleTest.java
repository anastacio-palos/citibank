package com.ibm.openpages.ext.trigger.icaps.handler;

import com.ibm.openpages.api.resource.IField;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.ext.common.util.TriggerUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuditIssuePropertyChangeRuleTest {

    private static Logger logger = null;

    @BeforeClass
    public static void setLogger() throws MalformedURLException
    {
        logger = LogManager.getLogger();
    }

    @Test
    public void isValidAuditIssueTest(){
        boolean result = false;
        String fields = "Citi-Iss:ICAPSIssID,  Citi-iCAPSIss:iCAPSErr,  Citi-iCAPSIss:iCAPSErrMessage";
        if (fields != null) {
            List<String> blackList = Arrays.asList(fields.trim().split(","));
            logger.debug("blackList:"+blackList);
            List<String> modifiedFields = new ArrayList<>();
            IField errMsgF = Mockito.mock(IField.class);

            modifiedFields.add("Citi-iCAPSIss:validField");
            modifiedFields.add("Citi-Iss:ICAPSIssID");

            if (!modifiedFields.isEmpty()) {
                for (String i : modifiedFields) {
                    logger.debug("modified field:"+i);
                    boolean auxb = false;
                    for(String b : blackList){
                        if(b.trim().equals(i.trim())){
                            logger.debug("blacklisted field:"+i);
                            auxb = true;
                            break;
                        }
                    }
                    result = !auxb;
                    if(!result)
                        break;
                }
            }
        }

        logger.debug("result:"+result);

    }

}
