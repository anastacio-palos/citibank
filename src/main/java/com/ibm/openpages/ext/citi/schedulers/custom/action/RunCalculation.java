package com.ibm.openpages.ext.citi.schedulers.custom.action;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.process.IProcessInfo;
import com.ibm.openpages.api.rule.BulkRunCalculationsOptions;
import com.ibm.openpages.api.rule.ITBRuleDefinition;
import com.ibm.openpages.api.scheduler.AbstractScheduledProcess;
import com.ibm.openpages.api.scheduler.ISchedJobConfiguration;
import com.ibm.openpages.api.scheduler.ISchedJobConfigurationItem;
import com.ibm.openpages.api.service.*;
import com.ibm.openpages.ext.common.util.LoggerUtil;
import org.apache.logging.log4j.core.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RunCalculation extends AbstractScheduledProcess {

	private IServiceFactory serviceFactory = null;
    private IConfigurationService configService = null;
    private IMetaDataService metadataService = null;
    private ITBRuleService tbruleService = null;
    private IProcessService processService = null;
    private Context context = null;
    private Logger logger = null;

    public RunCalculation() {
        super(4);
        setName("IssueCalcs");
    }

    public void execute() throws Exception {

        try {
            String schedulerName = "RunIssueCalculation";
            context = getApplicationContext();
            serviceFactory = ServiceFactory.getServiceFactory(context);
            configService = serviceFactory.createConfigurationService();
            metadataService = serviceFactory.createMetaDataService();
            processService = serviceFactory.createProcessService();
            IConfigProperties configProperties = configService.getConfigProperties();
            tbruleService = serviceFactory.createRuleService();
            List<Id> calcList = new ArrayList<>();

            ISchedJobConfiguration jobConfiguration = this.jobDetail.getJobConfiguration();
            ISchedJobConfigurationItem enableDebugConfigItem = jobConfiguration.getConfigurationItem("ENABLE_DEBUG_MODE");
            ISchedJobConfigurationItem logFilePathConfigItem = jobConfiguration.getConfigurationItem("LOG_FILE_PATH");
            ISchedJobConfigurationItem objTypeConfigItem = jobConfiguration.getConfigurationItem("OBJECT_TYPE");
            ISchedJobConfigurationItem calcConfigItem = jobConfiguration.getConfigurationItem("CALCULATION");
            ISchedJobConfigurationItem calcRunCounterConfigItem = jobConfiguration.getConfigurationItem("CALCULATION_RUN_COUNTER");
			ISchedJobConfigurationItem logIntervalConfigItem = jobConfiguration.getConfigurationItem("LOG_INTERVAL");

            String enableDebug = configProperties.getProperty(enableDebugConfigItem.getFieldValue());
            String logFilePath = configProperties.getProperty(logFilePathConfigItem.getFieldValue());
            logger = LoggerUtil.getEXTLogger(logFilePath, enableDebug);

            int calcRunCounter = Integer.parseInt(configProperties.getProperty(calcRunCounterConfigItem.getFieldValue()));
            String[] calcConfigProp = configProperties.getProperty(calcConfigItem.getFieldValue()).split(",");
            LoggerUtil.debugEXTLog(logger, schedulerName, "calcConfigProp.length==", calcConfigProp.length);
            String objectTypes = objTypeConfigItem.getFieldValue();
            StringTokenizer st = new StringTokenizer(objectTypes, ",");
            while (st.hasMoreTokens()) {
                String objType = st.nextToken();
                LoggerUtil.debugEXTLog(logger, schedulerName, "current object type==", objType);
                Id objTypeId = metadataService.getType(objType).getId();
                for (int i = 0; i < calcConfigProp.length; i++) {
                    for (ITBRuleDefinition ruleDef : tbruleService.getRuleDefinitionsByType(objTypeId)) {
                        if (ruleDef.getName().equalsIgnoreCase(calcConfigProp[i])) {
                            calcList.add(ruleDef.getId());
                        }
                    }
                }
            }

            LoggerUtil.debugEXTLog(logger, schedulerName, "calcList.size()==", calcList.size());
            String sleepInterval = configService.getConfigProperties().getProperty(logIntervalConfigItem.getFieldValue());
            LoggerUtil.debugEXTLog(logger, schedulerName, "sleepInterval==", sleepInterval + " seconds");
            for (Id calcId : calcList) {

                String calcName = tbruleService.getRuleDefinition(calcId, false).getName();
                LoggerUtil.debugEXTLog(logger, schedulerName, "Starting Calculation ", calcName);
                BulkRunCalculationsOptions bulkRunCalcOptions = new BulkRunCalculationsOptions();
                bulkRunCalcOptions.setRuleDefinitionId(calcId);
                Id processId = tbruleService.startCalculationsInBulk(bulkRunCalcOptions);
                IProcessInfo processInfo = processService.getProcess(processId);
                int counter = 0;
                while (!processInfo.isFinished()) {
                    if (counter > calcRunCounter) {
                        LoggerUtil.debugEXTLog(logger, schedulerName, "Counter Greater Than : ", calcRunCounter + " , terminating current calculation");
                        processService.terminateProcess(processId);
                    }
                    counter++;
                    IConfigurationService configService = serviceFactory.createConfigurationService();

                    Thread.sleep(Long.parseLong(sleepInterval)*1000);
                    processInfo = processService.getProcess(processId);
                    LoggerUtil.debugEXTLog(logger, schedulerName, "Isfinished ", processInfo.isFinished() + " Counter " + counter);
                }
            }

            LoggerUtil.debugEXTLog(logger, schedulerName, "Message==", "Success");


        } catch (Exception e) {
            LoggerUtil.debugEXTLog(logger, "RunCalculation", "ExceptionMessage", e.getLocalizedMessage());
            throw (e);
        }
    }
}