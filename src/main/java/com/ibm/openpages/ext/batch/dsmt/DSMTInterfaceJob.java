package com.ibm.openpages.ext.batch.dsmt;

import com.ibm.openpages.api.Context;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.process.ProcessStatus;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.scheduler.AbstractScheduledProcess;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.ext.batch.dsmt.model.DSMTData;
import com.ibm.openpages.ext.constant.DSMTConstant;
import com.ibm.openpages.ext.notification.EmailConfiguration;
import com.ibm.openpages.ext.notification.EmailService;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.impl.LoggerUtil;
import com.ibm.openpages.ext.ui.dao.DSMTDataAccessConfig;
import com.ibm.openpages.ext.ui.util.DSMTDatasourceConfigReader;
import com.ibm.openpages.ext.util.CustomFieldUtil;
import com.ibm.openpages.ext.util.ZipUtil;
import com.openpages.sdk.OpenpagesServiceException;
import com.openpages.sdk.OpenpagesSession;
import com.openpages.sdk.OpenpagesSessionException;
import com.openpages.sdk.admin.AdminService;
import com.openpages.sdk.admin.process.Process;
import com.openpages.sdk.admin.process.ProcessConstants;
import com.openpages.sdk.admin.process.ProcessOptions;
import com.openpages.sdk.search.RelationalOperators;
import com.openpages.sdk.search.SimpleCondition;
import org.apache.commons.logging.Log;

import com.ibm.openpages.api.resource.util.ResourceUtil;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.ibm.openpages.ext.notification.EmailContent;
import com.ibm.openpages.ext.util.CSVWriter;
import com.ibm.openpages.ext.model.DSMTReportRecord;
import com.ibm.openpages.ext.util.ParentFinderUtil;
import org.quartz.DisallowConcurrentExecution;


@DisallowConcurrentExecution
public class DSMTInterfaceJob extends AbstractScheduledProcess {

    private Log log;

    private DataSource dataSource;

    private CustomFieldUtil fieldUtil;

    private IQueryService queryService;

    private ILoggerUtil loggerUtil;

    private DSMTUpdateProcessor dsmtUpdateProcessor;

    private IResourceService resourceService;

    public static String DSMT_REPORT_HEADER = "Operation, TripletID, NodeID, NodeType, Success/Fail, Details";

    private String reportFilePath = "/tmp/DSMTReport_" + System.currentTimeMillis() + ".csv";

    private CSVWriter reportWriter = null;

    private ParentFinderUtil parentFinderUtil;


    public DSMTInterfaceJob() throws Exception {
        super(ProcessConstants.TYPE_GENERAL);
        setName("DSMT Interface Batch Job");

    }


    public DSMTInterfaceJob(final int processType) throws Exception {
        super(processType);

        if (processType != -11111) {
            init(this.applicationContext);

        }

    }

    public void init(Context applicationContext) throws Exception {
        this.loggerUtil = new LoggerUtil();
        this.loggerUtil.initService();

        log = loggerUtil.getExtLogger(DSMTConstant.DSMT_INTERFACE_LOGGER);


        DSMTDatasourceConfigReader configReader = new DSMTDatasourceConfigReader(loggerUtil, null, serviceFactory);

        DSMTDataAccessConfig config = new DSMTDataAccessConfig(loggerUtil, configReader);

        this.dataSource = config.dataSource();

        this.fieldUtil = CustomFieldUtil.getInstanceOf(this.serviceFactory);
        this.queryService = this.serviceFactory.createQueryService();

        log.info("Datasource retrieved=" + this.dataSource);


        this.resourceService = serviceFactory.createResourceService();

        reportWriter = new CSVWriter(reportFilePath);

        reportWriter.writeHeader(DSMT_REPORT_HEADER);
        dsmtUpdateProcessor = new DSMTUpdateProcessor(loggerUtil, this.dataSource, this.fieldUtil, this.serviceFactory, reportWriter);

        parentFinderUtil = new ParentFinderUtil(log, resourceService);
        log.info(String.format("writer created with file=%s, exist=%s", reportFilePath, new File(reportFilePath).exists()));

        setName("DSMT Interface Batch Job");

    }


    @Override
    public void execute() throws Exception {

        init(this.applicationContext);
        boolean isrunning = isRunning();
        EmailService emailService = new EmailService(serviceFactory, loggerUtil);

        EmailContent emailContent = new EmailContent(this.serviceFactory);

        emailContent.setStartDate(new Date());

        if(isrunning){
                log.info("DSMT job already running ignoring the run");

            emailService.sendEmail(emailContent.getConflictMessageSub(), emailContent.getConflictMessageBody(), emailContent.getConflictMessageFooter());

            return;
        }


        log.info("Executing batch=" + this.jobDetail);

        syncDSMT(DSMTConstant.DSMT_INVALID, emailContent);

        List<DSMTData> updateResult = dsmtUpdateProcessor.processDSMTUpdate(emailContent);

        log.info("Number of DSMT Updated=" + updateResult.size());

        try {
            emailContent.setEndDate(new Date());

            log.info("Email Content=" + emailContent);
            log.info("Email config=" + EmailConfiguration.getInstance(serviceFactory));


            File reportFile = new File(reportFilePath);
            File zipFile = ZipUtil.zipFile(reportFile);
            emailService.sendEmail(
                    String.format(emailContent.getSubject(), new Date()), emailContent.buildEmail(), zipFile.getPath());

            boolean reportDeleteResult = reportFile.delete();
            boolean zipDeleteResult = zipFile.delete();

            log.info(String.format("Report Delete result=%s, ZIP delete result=%s", reportDeleteResult, zipDeleteResult));
        } catch (Exception ex) {
            log.error("Error sending notification", ex);
        }

    }

    private boolean isRunning() throws OpenpagesSessionException, OpenpagesServiceException {
        OpenpagesSession session = (OpenpagesSession) this.serviceFactory.getContext().get(Context.SERVICE_SESSION);
        AdminService adminService = session.getAdminService();
        ProcessOptions options = new ProcessOptions();

        options.addProcessType(ProcessConstants.TYPE_GENERAL);
        String processName = getName() + "%";
        options.addCondition(new SimpleCondition(Process.NAME, RelationalOperators.LIKE, processName));
        options.addStatus(ProcessStatus.STATUS_RUNNING.ordinal());
        @SuppressWarnings("unchecked")
        List<Process> processlogs = adminService.getProcessList(options);

        log.info("Status of is_running="+ processlogs.size());

        return processlogs == null || processlogs.size() <= 1 ? false : true;
    }

    private void syncDSMT(String opType, EmailContent emailContent) {

        List<DSMTData> dsmtToProcess = new ArrayList();
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(
                Optional.of(opType).filter(v -> DSMTConstant.DSMT_INVALID.equalsIgnoreCase(v)).map(v -> DSMTConstant.SELECT_INVALID_DSMT).orElse(DSMTConstant.UPDATE_DSMT_PROCESSED))) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                log.info("rs=" + rs.getString("TRIPLET_ID"));

                dsmtToProcess.add(new DSMTData(rs.getString("TRIPLET_ID")));
            }


        } catch (Exception ex) {
            log.error("Error running the invalid sync", ex);
        }

//        dsmtToProcess.add(new DSMTData("16001"));

        log.info("DSMT_LIST= " + dsmtToProcess);

        List<String> erroredDSMT = new ArrayList<>();


        dsmtToProcess.stream().forEach(v -> {
            log.info("Processing dsmt=" + v);

            retrieveDSMTObj(v.getTripletID()).forEach(d -> {
                DSMTReportRecord dsmtReportRecord = new DSMTReportRecord();
                dsmtReportRecord.setNodeID(d.getNodeID());
                dsmtReportRecord.setTripletID(d.getTripletID());
                dsmtReportRecord.setOperation(DSMTConstant.INVALID_SYNC_OPERATION);
                dsmtReportRecord.setNodeType(d.getNodeType());

                try {

                    log.info("Action=process_actual_dsmt id=" + d.getTripletID());

                    processInvalidDSMT(d);
                    dsmtReportRecord.setStatus(DSMTConstant.SUCCESS);


                } catch (Exception ex) {
                    log.error("Error updating the object", ex);

                    // mark the dsmt not to be marked processed.

                    erroredDSMT.add(v.getTripletID());
                    dsmtReportRecord.setStatus(DSMTConstant.FAIL);
                    dsmtReportRecord.setDetails(ex.getMessage());

                }


                try {
                    reportWriter.writeRecord(dsmtReportRecord);
                } catch (Exception ex) {
                    log.error("Error writing the report", ex);
                }
            });

        });

        emailContent.setInvalidSyncSuccess(dsmtToProcess.size() - erroredDSMT.size());
        emailContent.setInvalidSyncError(erroredDSMT.size());
        updateProcessedDSMT(dsmtToProcess, erroredDSMT);

    }


    private void processInvalidDSMT(DSMTData d) throws Exception {
        log.info("DSMT object=" + d);

        IGRCObject dsmtObject = this.resourceService.getGRCObject(new Id(d.getResourceID()));
        log.info("DSMT grc object=" + dsmtObject +
                 " :: init value=" + fieldUtil.getFieldValueAsString(dsmtObject, DSMTConstant.DSMT_INVALID_FIELD) +
                 ":: scope value=" + fieldUtil.getFieldValueAsString(dsmtObject, "Citi-DL:Scp"));

        //fieldUtil.setFieldValue(dsmtObject, DSMTConstant.DSMT_INVALID_FIELD, DSMTConstant.DSMT_INVALID_FIELD_VALUE);

        ResourceUtil.setFieldValue(dsmtObject.getField(DSMTConstant.DSMT_INVALID_FIELD), DSMTConstant.DSMT_INVALID_FIELD_VALUE);

        dsmtObject = this.resourceService.saveResource(dsmtObject);

        dsmtObject = this.resourceService.getGRCObject(new Id(d.getResourceID()));

        String invalidFiledValue = fieldUtil.getFieldValueAsString(dsmtObject, DSMTConstant.DSMT_INVALID_FIELD);

        log.info("ResourceUtil DSMT marked invalid=" + dsmtObject.getId() + " :: valueSet=" + invalidFiledValue);

        IGRCObject parentDSMT = this.resourceService.getGRCObject(dsmtObject.getPrimaryParent());

//        fieldUtil.setFieldValue(parentDSMT, DSMTConstant.DSMT_INVALID_PARENT_FIELD, String.valueOf(DSMTConstant.DSMT_INVALID_PARENT_FIELD_VALUE));


        log.info("Parent DSMT :" + parentDSMT.getId() + " :: name : " + parentDSMT.getName() + " :: field =" + DSMTConstant.DSMT_INVALID_PARENT_FIELD);
        ResourceUtil.setFieldValue(parentDSMT.getField(DSMTConstant.DSMT_INVALID_PARENT_FIELD), DSMTConstant.DSMT_INVALID_PARENT_FIELD_VALUE);

        parentDSMT = this.resourceService.saveResource(parentDSMT);


        log.info("Parent DSMT marked invalid=" + parentDSMT);
    }


    private Stream<DSMTData> retrieveDSMTObj(String baseid) {

        String query = String.format(DSMTConstant.DSMT_BASEID_QUERY, baseid);

        log.info("Final query=" + query);

        return StreamSupport.stream(queryService.buildQuery(query).fetchRows(0).spliterator(), false)
                            .map(v ->
                                    {
                                        try {
                                            return fieldUtil.getFieldValueAsString(v.getField(0));
                                        } catch (Exception ex) {
                                            return null;
                                        }
                                    }
                            )
                            .filter(v -> v != null)
                            .map(v -> {
                                DSMTData d = new DSMTData(baseid);
                                d.setResourceID(v);
                                return d;
                            });

    }

    private void updateProcessedDSMT(List<DSMTData> dsmtData, List<String> erroredDSMT) {

        log.info(String.format("DSMT=%s, ERROR_DSMT=%s", dsmtData.size(), erroredDSMT.size()));

        List<DSMTData> deltaDSMT = dsmtData.stream().filter(d -> !erroredDSMT.contains(d.getTripletID())).collect(Collectors.toList());

        log.info(String.format("Final_DSMT=%s", deltaDSMT.size()));


        List<String> dsmts = new ArrayList<>();

        for (int count = 0; count < deltaDSMT.size(); count++) {

            dsmts.add(dsmtData.get(count).getTripletID());

            if (count % DSMTConstant.DSMT_UPDATE_BATCH_SIZE == 0) {

                updateDSMT(dsmts);

                dsmts = new ArrayList<>();
            }

        }

        updateDSMT(dsmts);

    }

    private void updateDSMT(List<String> dsmts) {

        if (dsmts.isEmpty()) return;

        dsmts.stream().forEach(d -> {
            try (Connection connection = this.dataSource.getConnection(); CallableStatement ps = connection.prepareCall(DSMTConstant.UPDATE_DSMT_PROCESSED_SINGLE)) {

                try {
                    ps.setString(1, d);
                    ps.setString(2, DSMTConstant.UPDATE_DSMT_PROCESSED_MESSAGE);

                } catch (Exception ex) {
                    log.error("Error adding to batch fo dsmt_id=" + d, ex);
                }

                int result = ps.executeUpdate();

                log.info("Action=DSMT_UPDATE result=" + result);

            } catch (Exception ex) {
                log.error("Error updating the dsmt processed status", ex);

            }
        });

    }

}
