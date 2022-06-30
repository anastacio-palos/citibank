package com.ibm.openpages.ext.interfaces.icaps.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.icaps.exception.IssueException;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import com.ibm.openpages.ext.tss.service.config.OPSServiceFactory;
import com.ibm.openpages.ext.ui.dao.DSMTDataAccessConfig;
import com.ibm.openpages.ext.ui.util.DSMTDatasourceConfigReader;
import org.apache.logging.log4j.Logger;

import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public class ManagedSegmentDAO {

	private IServiceFactory sf;
	private DataSource dataSource;
	Logger logger;
	IServiceFactory serviceFactory;
	IConfigurationService configService;
	@Autowired
	ILoggerUtil loggerUtil;
	@Autowired
	DSMTDatasourceConfigReader configReader;
	@Autowired
	DSMTDataAccessConfig config;

	public ManagedSegmentDAO(IServiceFactory serviceFactory, Logger logger) throws Exception {
		super();
		this.logger = logger;
		this.serviceFactory = serviceFactory;
		this.configService = this.serviceFactory.createConfigurationService();
		if(config == null)
			init();
		dataSource = config.dataSource();
	}

	public void init() throws Exception {
		logger.debug("Initializing DSMTDataAccessConfig Manually");
		if(loggerUtil == null)
			loggerUtil = OPSServiceFactory.getLoggerUtil();
		configReader = new DSMTDatasourceConfigReader(loggerUtil, null, this.serviceFactory);
		config = new DSMTDataAccessConfig(loggerUtil, configReader);
	}


	/**
	 *  This method retrieves a list with all elements found in DSMT
	 *  sorted hierarchically from child to great-grandparent
	 *
	 * @param managedSegmentId input parameter to be executed in query.
	 * @return list of hierarchy from dsmt.
	 */
	public List<String> getHierarchy(String managedSegmentId) throws IssueException{
		logger.debug("Retrieving the managedSegment " + managedSegmentId + " hierarchy from the DB ");
		List<String> result = new ArrayList<>();
		try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(IssueConstants.DSMT_HIERARCHY_QUERY)) {
			ps.setString(1,managedSegmentId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result.add(rs.getString(IssueConstants.HIERARCHY_ID));
			}
		} catch (Exception ex) {
			String errorMessage = "Error retrivieng managedSegment hierarchy from DSMT";
			logger.error(errorMessage, ex);
			throw new IssueException(errorMessage, ex);
		}
		logger.debug("retrieved hierarchy: "+ result);
/*

		try {
			connection = DSMTConnectionUtil.getConection(configService, logger);
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1,managedSegmentId);
			logger.debug("Prepared Statement: "+ ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result.add(rs.getString(IssueConstants.HIERARCHY_ID));
			}
		} catch (Exception ex) {
			logger.error("Error ClassNotFoundException = ", ex);
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (Exception ex) {
				logger.error("Error while closing Db connection",ex);
			}
		}*/
		return result;
	}

}
