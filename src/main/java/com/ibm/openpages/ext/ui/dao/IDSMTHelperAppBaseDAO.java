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

package com.ibm.openpages.ext.ui.dao;

import com.ibm.openpages.ext.model.DSMTTripletModel;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
public interface IDSMTHelperAppBaseDAO {

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
    Connection getConnection() throws Exception;

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
    void closeConnection(Connection connection) throws Exception;

    /**
     * <P>
     * This methods closes and clears the given {@link CallableStatement}
     * 
     * @param callableStatement
     *            the instance of callable statement that needs to be closed and cleared
     *            
     * @throws Exception
     *             any runtime exception
     */
    void clearCallableStatement(CallableStatement callableStatement) throws Exception;

    /**
     * <P>
     * This methods closes and clears the given {@link Statement}
     * 
     * @param statement
     *            the instance of statement that needs to be closed and cleared
     *            
     * @throws Exception
     *             any runtime exception
     */
    void clearStatement(Statement statement) throws Exception;

    /**
     * <P>
     * This methods closes and clears the given {@link ResultSet}
     * 
     * @param resultSet
     *            the instance of result set that needs to be closed and cleared
     *            
     * @throws Exception
     *             any runtime exception
     */
    void clearResultSet(ResultSet resultSet) throws Exception;

    /**
     *
     * @param dsmtTripletModel
     * @return
     * @throws Exception
     */
    boolean createDSMTTriplet(DSMTTripletModel dsmtTripletModel) throws Exception;

    /**
     *
     * @param dsmtTripletModel
     * @return
     * @throws Exception
     */
    boolean descopeDSMTTriplet(DSMTTripletModel dsmtTripletModel) throws Exception;

}
