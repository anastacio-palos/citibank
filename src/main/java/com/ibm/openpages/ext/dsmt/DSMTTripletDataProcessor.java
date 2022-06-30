package com.ibm.openpages.ext.dsmt;

import com.ibm.openpages.ext.constant.DSMTConstant;
import com.ibm.openpages.ext.model.DSMTTripletModel;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class DSMTTripletDataProcessor {

    private DataSource ds;

    private ILoggerUtil iLoggerUtil;

    private Log log;

    public DSMTTripletDataProcessor(DataSource ds, ILoggerUtil iLoggerUtil){
        this.ds = ds;
        this.iLoggerUtil = iLoggerUtil;
        this.log = iLoggerUtil.getExtLogger(DSMTConstant.DSMT_TRIPLET_LOGGER);
    }


    public boolean createTriplet(DSMTTripletModel tripletModel){

        log.info("Action=update/insertdsmt dsmtdata="+ tripletModel);

        if(!tripletModel.validateForInsert())
            throw new IllegalArgumentException("Validation failed - all the fields are mandatory");

        return executeProcedure(tripletModel, DSMTConstant.DSMT_TRIPLET);

    }

    public boolean descopeTriplet(DSMTTripletModel tripletModel){

        log.info("Action=descope_dsmt dsmtdata="+ tripletModel);

        if(!tripletModel.validateForDescope())
            throw new IllegalArgumentException("Validation failed - either triplet id or status is null");

        return executeProcedure(tripletModel, DSMTConstant.DSMT_TRIPLET);

    }


    private boolean executeProcedure(DSMTTripletModel tripletModel, String query){

        log.info("Action=update/descope dsmt new dsmtdata="+ tripletModel);

        boolean result = false;

        try (Connection con = this.ds.getConnection(); CallableStatement ps =
                con.prepareCall(DSMTConstant.DSMT_TRIPLET)) {

            ps.setString(1, tripletModel.getTripletID());
            ps.setString(2, tripletModel.getMsid());
            ps.setString(3, tripletModel.getMgid());
            ps.setString(4, tripletModel.getLvid());
            ps.setString(5, tripletModel.getStatus());
            ps.registerOutParameter(6, java.sql.Types.VARCHAR);

            result = ps.execute();


        }catch(Exception ex){
            log.error("Error creating the triplet record data="+ tripletModel, ex);

        }

        log.info("Action=update/insertdsmt result="+ result);

        return result;

    }
}
