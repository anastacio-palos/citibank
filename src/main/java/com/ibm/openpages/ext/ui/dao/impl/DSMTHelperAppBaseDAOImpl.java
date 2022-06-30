/***********************************************************************************************************************
 * IBM Confidential OCO Source Materials
 *
 * 5725-D51, 5725-D52, 5725-D53, 5725-D54
 *
 * � Copyright IBM Corporation 2018
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 **********************************************************************************************************************/

package com.ibm.openpages.ext.ui.dao.impl;

import static com.ibm.openpages.ext.tss.service.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.tss.service.util.CommonUtil.isObjectNotNull;
import static com.ibm.openpages.ext.ui.constant.DSMTDBConfigurationConstants.DSMT_DATA_HELPER_APP_BASE_LOG_FILE_NAME;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.dsmt.DSMTTripletDataProcessor;
import com.ibm.openpages.ext.model.DSMTTripletModel;
import com.ibm.openpages.ext.tss.service.beans.IGRCObjectCreateInformation;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.ui.dao.IDSMTHelperAppBaseDAO;

/**
 * <P>
 * The Helper App Base DAO Implementation provides the Helper DAO classes with base methods to manage Database
 * connections.
 * </P>
 * 
 * @since : OpenPages 7.2.0
 * @version : OpenPages 7.4.0
 * @author : Praveen Ravi <BR>
 *         email : raviprav@us.ibm.com <BR>
 *         company : IBM OpenPages
 * 
 * @custom.date : 10-19-2016
 * @custom.feature : Base DAO Implementation
 * @custom.category :Data Access
 */
@Service("dsmtHelperAppBaseDAO")
public class DSMTHelperAppBaseDAOImpl implements IDSMTHelperAppBaseDAO {

    public Log logger;

    @Autowired
    ILoggerUtil loggerUtil;

    @Lazy
    @Qualifier("dsmt-db")
    @Autowired
    DataSource dataAccessConfig;

    /**
     * Construct any information once the bean is created.
     */
    @PostConstruct
    public void intDAO() {

        logger = loggerUtil.getExtLogger(DSMT_DATA_HELPER_APP_BASE_LOG_FILE_NAME);
    }

    /**
     * <P>
     * This method returns an instance of the {@link Connection}. The bean is a spring transaction managed singleton
     * bean.
     * </P>
     * 
     * @return an instance of the the database connection object.
     * @throws Exception
     *             any runtime exception
     */
    @Override
    public Connection getConnection() throws Exception {

        logger.debug(dataAccessConfig.getClass().getName());
        return dataAccessConfig.getConnection();
    }

    /**
     * <P>
     * This method closes the active {@link Connection} that was created.
     * </P>
     * 
     * @param connection
     *            the instance of database connection that needs to be closed
     * @throws Exception
     *             any runtime exception
     */
    @Override
    public void closeConnection(Connection connection) throws Exception {

        logger.info("closeConnection() START");

        try {

            logger.info("Is connection not null? " + isObjectNotNull(connection));
            if (isObjectNotNull(connection)) {

                logger.info("Closing Connection...");
                connection.close();
            }
        }
        catch (Exception ex) {

            logger.error("Error closing connection: " + getStackTrace(ex));
        }
        finally {

            connection = null;
        }

        logger.info("closeConnection() END");
    }

    /** ghb
     * <P>
     * This methods closes and clears the given {@link CallableStatement}
     * 
     * @param callableStatement
     *            the instance of callable statement that needs to be closed and cleared
     */
    @Override
    public void clearCallableStatement(CallableStatement callableStatement) throws Exception {

        logger.info("clearCallableStatement() START");

        if (isObjectNotNull(callableStatement)) {

            try {

                callableStatement.close();
            }
            catch (Exception ex) {

                logger.error("Error closing statement: " + getStackTrace(ex));
            }
            finally {
                callableStatement = null;
            }
        }

        logger.info("clearCallableStatement() END");
    }

    /**
     * <P>
     * This methods closes and clears the given {@link Statement}
     * 
     * @param statement
     *            the instance of statement that needs to be closed and cleared
     */
    @Override
    public void clearStatement(Statement statement) throws Exception {

        logger.info("clearStatement() START");

        if (isObjectNotNull(statement)) {

            try {

                statement.close();
            }
            catch (Exception ex) {

                logger.error("Error closing statement: " + getStackTrace(ex));
            }
            finally {
                statement = null;
            }
        }

        logger.info("clearStatement() END");
    }

    /**
     * <P>
     * This methods closes and clears the given {@link ResultSet}
     * 
     * @param resultSet
     *            the instance of result set that needs to be closed and cleared
     */
    @Override
    public void clearResultSet(ResultSet resultSet) throws Exception {

        logger.info("clearResultSet() START");

        if (isObjectNotNull(resultSet)) {

            try {

                resultSet.close();
            }
            catch (Exception ex) {

                logger.error("Error closing Result Set: " + getStackTrace(ex));
            }
            finally {
                resultSet = null;
            }
        }

        logger.info("clearResultSet() END");
    }

    /**
     *
     * @param dsmtTripletModel
     * @return
     * @throws Exception
     */
    @Override
    public boolean createDSMTTriplet(DSMTTripletModel dsmtTripletModel) throws Exception {

        logger.info("createDSMTTriplet() Start");

        // Method Level Variables.
        boolean isCreateDSMTTriplet;
        DSMTTripletDataProcessor dsmtTripletDataProcessor;

        // Method Implementation
        dsmtTripletDataProcessor = new DSMTTripletDataProcessor(dataAccessConfig, loggerUtil);
        logger.info("Creating Triplet with values: " + dsmtTripletModel);

        isCreateDSMTTriplet = dsmtTripletDataProcessor.createTriplet(dsmtTripletModel);
        logger.info("Creation successful: " + isCreateDSMTTriplet);

        logger.info("createDSMTTriplet() End");
        return isCreateDSMTTriplet;
    }

    /**
     *
     * @param dsmtTripletModel
     * @return
     * @throws Exception
     */
    @Override
    public boolean descopeDSMTTriplet(DSMTTripletModel dsmtTripletModel) throws Exception {

        logger.info("descopeDSMTTriplet() Start");

        // Method Level Variables.
        boolean isDescopeDSMTTriplet;
        DSMTTripletDataProcessor dsmtTripletDataProcessor;

        // Method Implementation
        dsmtTripletDataProcessor = new DSMTTripletDataProcessor(dataAccessConfig, loggerUtil);
        logger.info("Descoping Triplet with values: " + dsmtTripletModel);

        isDescopeDSMTTriplet = dsmtTripletDataProcessor.descopeTriplet(dsmtTripletModel);
        logger.info("Descoping successful: " + isDescopeDSMTTriplet);

        logger.info("descopeDSMTTriplet() End");
        return isDescopeDSMTTriplet;
    }
}
