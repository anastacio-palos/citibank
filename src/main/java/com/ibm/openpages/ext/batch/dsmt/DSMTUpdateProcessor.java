package com.ibm.openpages.ext.batch.dsmt;

import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.util.ResourceUtil;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IQueryService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.batch.dsmt.model.DSMTData;
import com.ibm.openpages.ext.batch.dsmt.model.DSMTDetails;
import com.ibm.openpages.ext.constant.DSMTConstant;
import com.ibm.openpages.ext.model.DSMTReportRecord;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.util.CSVWriter;
import com.ibm.openpages.ext.util.CustomFieldUtil;
import com.ibm.openpages.ext.util.ParentFinderUtil;
import org.apache.commons.logging.Log;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.ibm.openpages.ext.notification.EmailContent;


public class DSMTUpdateProcessor {

    private ILoggerUtil loggerUtil;
    private Log log;
    private DataSource dataSource;
    private CustomFieldUtil customFieldUtil;
    private IGRCObjectUtil igrcObjectUtil;
    private IServiceFactory serviceFactory;
    private IResourceService resourceService;
    private CSVWriter reportWriter= null;

    private ParentFinderUtil parentFinderUtil;

    private IConfigProperties configProperties;



    public DSMTUpdateProcessor(ILoggerUtil loggerUtil, DataSource dataSource, CustomFieldUtil customFieldUtil, IServiceFactory serviceFactory, CSVWriter reportWriter){
        this.loggerUtil = loggerUtil;
        this.log = this.loggerUtil.getExtLogger(DSMTConstant.DSMT_INTERFACE_LOGGER);
        this.dataSource = dataSource;
        this.customFieldUtil = customFieldUtil;
        this.serviceFactory = serviceFactory;
        this.resourceService = serviceFactory.createResourceService();
        this.reportWriter = reportWriter;
        IConfigurationService configurationService = serviceFactory.createConfigurationService();
        this.configProperties = configurationService.getConfigProperties();
        parentFinderUtil = new ParentFinderUtil(this.log, resourceService);

    }


    public List<DSMTData> processDSMTUpdate(EmailContent emailContent){

        log.info("Action=processing_dsmt_update datasource="+ dataSource);

        List<DSMTData> dsmtToProcess = new ArrayList();

        try(Connection con = dataSource.getConnection(); Statement ps = con.createStatement()){

            log.info("Action=executing_query query=" + DSMTConstant.DSMT_UPDATE_TO_PROCESS);

            ResultSet rs = ps.executeQuery(DSMTConstant.DSMT_UPDATE_TO_PROCESS);

            log.info("Action=executing_query ps="+ ps + " :: fetch_size="+ rs.getFetchSize());


            while(rs.next()){

                log.info("Action=parse_resultset rs = " + rs);
                DSMTData dsmtData = new DSMTData(null);
                dsmtData.setNodeType(rs.getString("NODE_TYPE"));
                dsmtData.setNodeID(rs.getString("NODE_ID"));
                dsmtData.setFailCount(rs.getInt("FAIL_COUNT"));

                log.info("Action=dsmt_object_built dmst=" + dsmtData);

                dsmtToProcess.add(dsmtData);
            }


        }catch(Exception ex){
            log.error("Action=error_fetching_dsmt", ex);
        }

        log.info("Action=dsmt_retrieved number_of_dsmt_to_sync= " + dsmtToProcess.size());

        int errorCount = processDSMTs(dsmtToProcess);

        emailContent.setUpdateSyncError(errorCount);
        emailContent.setUpdateSyncSuccess(dsmtToProcess.size() - errorCount);

        return dsmtToProcess;

    }

    private boolean checkUpdateCondition(DSMTData dsmtData){
        try {

            log.info("ConditionCheck_dsmt="+dsmtData);

            if(dsmtData.getTripletID() == null){

                return true;
            }

            IGRCObject auditObect = parentFinderUtil.getParent(resourceService.getGRCObject(new Id(dsmtData.getTripletID())), DSMTConstant.AUDIT_PROGRAM_NAME);

            log.info("ConditionCheck audit="+auditObect);

            if (auditObect == null) {
                return true;
            }

            String statusFieldValue = customFieldUtil.getFieldValueAsString(auditObect, DSMTConstant.AUDIT_FIELD_STATUS);

            String statusValues = configProperties.getProperty(DSMTConstant.EXCLUDE_STATUS_VALUES);

            log.info(String.format("Status_check_values_audit_id=%s, audit_name=%s, object=%s registryStatus=%s", auditObect.getId(), auditObect.getName(), statusFieldValue, statusValues));

            if(statusValues.contains(statusFieldValue)){
                return false;
            }
        }catch(Exception ex){

            log.error("Error checking the status of audit ", ex );

        }

        return true;

    }

    private int processDSMTs(List<DSMTData> dsmtData){

        Map<String, List<DSMTData>> nodeIDDSMT= dsmtData.stream().flatMap(this::retrieveDSMTObj).filter(this::checkUpdateCondition).collect(Collectors.groupingBy(v -> v.getNodeID()));

        log.info("Action=group_dsmt grouped="+ nodeIDDSMT);

        AtomicInteger count = new AtomicInteger(0);


        nodeIDDSMT.entrySet().stream().forEach(e -> {

            if(e.getValue().size() > 0) {

                DSMTDetails dsmtDetails = new DSMTDetails();

                try (Connection con = this.dataSource.getConnection(); PreparedStatement ps =
                        con.prepareStatement(DSMTConstant.RETRIEVE_DSMT_DETAIL_NODE_ID_TYPE)) {

                    log.info(String.format("Retrieving data for type=%s, id=%s",  e.getValue().get(0).getNodeType(), e.getKey()));
                    ps.setString(1, e.getValue().get(0).getNodeType());
                    ps.setString(2,  e.getKey());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        dsmtDetails.setNodeID(rs.getString("node_id"));
                        dsmtDetails.setNodeType(rs.getString("node_type"));
                        dsmtDetails.setNodeName(rs.getString("node_name"));
                        dsmtDetails.setNodeLevel(rs.getString("node_level"));
                        dsmtDetails.setNodeRegion(rs.getString("node_region"));
                        dsmtDetails.setCountryCode(rs.getString("country_code"));
                        dsmtDetails.setChildHierarchy(rs.getString("child_hierarchy"));
                        dsmtDetails.setParentNodeID(rs.getString("parent_node_id"));
                        dsmtDetails.setParentHierarchy(rs.getString("parent_hierarchy"));
                        dsmtDetails.setStatus(rs.getString("status"));
                        dsmtDetails.setProcessed(true);


                    }

                } catch (Exception ex) {
                    log.error("Action=error_retrieving_dsmt_details", ex);
                }

                if (dsmtDetails.isProcessed()) {

                    try {

                        e.getValue().stream().forEach(v ->{
                            DSMTReportRecord dsmtReportRecord = new DSMTReportRecord();
                            dsmtReportRecord.setNodeID(v.getNodeID());
                            dsmtReportRecord.setTripletID(v.getTripletID());
                            dsmtReportRecord.setOperation(DSMTConstant.DSMT_SYNC_OPERATION);
                            dsmtReportRecord.setNodeType(v.getNodeType());
                                try {


                                    this.updateDSMT(v, dsmtDetails);
                                    dsmtReportRecord.setStatus(DSMTConstant.SUCCESS);
                                    writeDSMTReport(dsmtReportRecord);

                                }catch(Exception ex){
                                    log.error("Action=error_updating_dsmt_processing stop dsmt="+ v, ex );
                                    dsmtReportRecord.setStatus(DSMTConstant.FAIL);
                                    dsmtReportRecord.setDetails(ex.getMessage());
                                    writeDSMTReport(dsmtReportRecord);
                                    throw new RuntimeException(ex);

                                }});
                    }catch(Exception ex){

                        count.incrementAndGet();

                        executeUpdate(DSMTConstant.DSMT_PROCESS_STATUS_UPDATE, dsmtDetails.getNodeID(), dsmtDetails.getNodeType(),"N", e.getValue().get(0).getFailCount()+1, ex.getMessage());

                    }

                    executeUpdate(DSMTConstant.DSMT_PROCESS_STATUS_UPDATE, dsmtDetails.getNodeID(), dsmtDetails.getNodeType(), "Y", 0, null);

                }
            }

        });


        return count.get();
    }

    private void writeDSMTReport(DSMTReportRecord reportRecord){

        try {
            reportWriter.writeRecord(reportRecord);
        } catch (Exception e) {
            log.error("Error writing syn report ", e);
        }

    }

    private Stream<DSMTData> retrieveDSMTObj(DSMTData dsmtData){

        IQueryService queryService = this.serviceFactory.createQueryService();

        String query = String.format(DSMTConstant.RETRIEVE_DSMT_OBJECT,DSMTConstant.NODE_TYPE_FIELD_MAP.get(dsmtData.getNodeType()), dsmtData.getNodeID());

        log.info("Final query="+ query);

        return StreamSupport.stream(queryService.buildQuery(query).fetchRows(0).spliterator(), false)
                            .map(v ->
                                    {try{return this.customFieldUtil.getFieldValueAsString(v.getField(0));}catch(Exception ex){log.error("error getting the dsmt id", ex);return null;}}
                            )
                            .filter(v -> v!=null)
                            .map(v ->
                                {
                                    // cloning as 1 node id will have mutiple dsmt
                                    DSMTData cloneDSMT = new DSMTData(v);
                                    cloneDSMT.setNodeID(dsmtData.getNodeID());
                                    cloneDSMT.setNodeType(dsmtData.getNodeType());
                                    cloneDSMT.setFailCount(dsmtData.getFailCount());
                                    return cloneDSMT;
                                }
                            );

    }

    private void updateDSMT(DSMTData data, DSMTDetails details) throws Exception{

        log.info(String.format("Updating DSMT dsmtdata=%s, details=%s", data, details));

        IGRCObject igrcObject = resourceService.getGRCObject(new Id(data.getTripletID()));

        switch (data.getNodeType()) {
            case "MG":
                ResourceUtil.setFieldValue(igrcObject.getField(DSMTConstant.MG_FIELD_NAME), details.getNodeName());
                ResourceUtil.setFieldValue(igrcObject.getField(DSMTConstant.MG_FIELD_LEVEL), Integer.valueOf(details.getNodeLevel()));
                ResourceUtil.setFieldValue(igrcObject.getField(DSMTConstant.MG_FIELD_COUNTRY), details.getCountryCode());
                ResourceUtil.setFieldValue(igrcObject.getField(DSMTConstant.MG_FIELD_REGION), details.getNodeRegion());
                break;
            case "MS":
                ResourceUtil.setFieldValue(igrcObject.getField(DSMTConstant.MS_FIELD_NAME), details.getNodeName());
                ResourceUtil.setFieldValue(igrcObject.getField(DSMTConstant.MS_FIELD_LEVEL), Integer.valueOf(details.getNodeLevel()));
                ResourceUtil.setFieldValue(igrcObject.getField(DSMTConstant.MS_FIELD_HIERARCHY), details.getParentHierarchy());
                break;
            case "LV":
                ResourceUtil.setFieldValue(igrcObject.getField(DSMTConstant.LV_FIELD_NAME), details.getNodeName());
                break;
        }

        IGRCObject savedResource = resourceService.saveResource(igrcObject);

        log.info("Action=resource_saved savedResource="+ savedResource);

    }

    private void executeUpdate(String query, String nodeID, String nodeType, String status, int failCount, String error){

        try (Connection con = this.dataSource.getConnection(); CallableStatement ps =
                con.prepareCall(query)) {
            ps.setString(1, nodeID);
            ps.setString(2, nodeType);
            ps.setString(3, status);
            ps.setString(4, error);
            ps.registerOutParameter(5, java.sql.Types.VARCHAR);

            boolean result = ps.execute();

            log.info(String.format("Number of rows updated nodeid=%s nodeType=%s updatedRows=%s", nodeID, nodeType, result));

        }catch(Exception ex){
            log.error(String.format("Error updating the data query=%s, nodeid=%s, nodeType=%s, status=%s, failCount=%s, error=%s",
                    query, nodeID, nodeType, status, failCount, error), ex);

        }


    }


}
