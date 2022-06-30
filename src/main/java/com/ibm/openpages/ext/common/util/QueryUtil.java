package com.ibm.openpages.ext.common.util;

/*
Licensed Materials - Property of IBM


5725-D51, 5725-D52, 5725-D53, 5725-D54

© Copyright IBM Corporation 2018. All Rights Reserved.

US Government Users Restricted Rights- Use, duplication or disclosure restricted 
by GSA ADP Schedule Contract with IBM Corp.
*/

/*
{
"$schema":{"$ref":"TS_Taxonomy_vMay132009"},
"author":"Nikhil Komakula",
"customer":"IBM",
"date":"12/01/2018",
"summary":"Common",
"technology":"java",
"feature":"Query Utilities",
"rt_num":""
}
*/

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.query.IQuery;
import com.ibm.openpages.api.query.IResultSetRow;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.query.ParameterValue;
import com.ibm.openpages.api.resource.IField;

public class QueryUtil {
	
	private static final String CLASS_NAME = "QueryUtil";
	
	/**
	 * <p>
	 * This method returns Result Set after running the Query.
	 * </p>
	 * 
	 * @param queryStatement SQL Query Statement
	 * @param honorPrimary True to return only primary associations
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @return ITabularResultSet
	 * @throws Exception
	 */
	public static ITabularResultSet runQuery(String queryStatement, boolean honorPrimary, ServicesUtil servicesUtil) throws Exception {
		
		IQuery query = servicesUtil.getQueryService().buildQuery(queryStatement);
		query.setReportingPeriod(servicesUtil.getConfigurationService().getCurrentReportingPeriod());
		
		if (honorPrimary)
			query.setHonorPrimary(true);
		else
			query.setHonorPrimary(false);
	
		ITabularResultSet resultset = query.fetchRows(0);
		
		return resultset;
	}
	
	/**
	 * <p>
	 * This method returns Result Set after running the Query.
	 * </p>
	 * 
	 * @param queryStatement SQL Query Statement
	 * @param uniqueColumnIndex Column index whose values would be unique. Preferably GRC Object Id
	 * @param honorPrimary True to return only primary associations
	 * @param bindingParamList List of binding parameters
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @param logger Instance of Logger
	 * @return Map<String, List<String>> A Map having key with the value at uniqueColumnIndex. Map value would be a List with all the row data.
	 * @throws Exception
	 */
	public static Map<String, List<String>> runQueryWithBinding(String queryStatement, int uniqueColumnIndex, boolean honorPrimary, List<String> bindingParamList, ServicesUtil servicesUtil, Logger logger) throws Exception {
		
		IQuery query = servicesUtil.getQueryService().buildQuery(queryStatement);
		query.setReportingPeriod(servicesUtil.getConfigurationService().getCurrentReportingPeriod());
		
		if (honorPrimary)
			query.setHonorPrimary(true);
		else
			query.setHonorPrimary(false);
		
		ParameterValue param = null;
		ITabularResultSet resultset = null;
		
		Map<String, List<String>> queryResultsMap = new LinkedHashMap<String, List<String>>();
		
		for (int i=0; i<bindingParamList.size(); i++) {
			
			param = new ParameterValue(bindingParamList.get(i));
			query.bindParameter(1, param);
			
			resultset = query.fetchRows(0);
			
			queryResultsMap.putAll(getQueryResultsAsMap(uniqueColumnIndex, resultset, logger));
		}
		
		return queryResultsMap;
	}
	
	/**
	 * <p>
	 * This method returns a Map with the results after running the Query.
	 * </p>
	 * 
	 * @param queryStatement SQL Query Statement
	 * @param uniqueColumnIndex Column index whose values would be unique. Preferably GRC Object Id
	 * @param honorPrimary True to return only primary associations
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @param logger Instance of Logger
	 * @return Map<String, List<String>> A Map having key with the value at uniqueColumnIndex. Map value would be a List with all the row data.
	 * @throws Exception
	 */
	public static Map<String, List<String>> getQueryResultsAsMap(String queryStatement, int uniqueColumnIndex, boolean honorPrimary, ServicesUtil servicesUtil, Logger logger) throws Exception {
		
		return getQueryResultsAsMap(uniqueColumnIndex, runQuery(queryStatement, honorPrimary, servicesUtil), logger);
	}
	
	/**
	 * <p>
	 * This method returns a Map with the results after running the Query.
	 * </p>
	 * 
	 * @param queryStatement SQL Query Statement
	 * @param uniqueColumnIndex Column index whose values would be unique. Preferably GRC Object Id
	 * @param honorPrimary True to return only primary associations
	 * @param bindingParamList List of binding parameters
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @param logger Instance of Logger
	 * @return Map<String, List<String>> A Map having key with the value at uniqueColumnIndex. Map value would be a List with all the row data.
	 * @throws Exception
	 */
	public static Map<String, List<String>> getQueryResultsAsMapWithBinding(String queryStatement, int uniqueColumnIndex, boolean honorPrimary, List<String> bindingParamList, ServicesUtil servicesUtil, Logger logger) throws Exception {
		
		return runQueryWithBinding(queryStatement, uniqueColumnIndex, honorPrimary, bindingParamList, servicesUtil, logger);
	}
	
	/**
	 * <p>
	 * This method returns a Map with the results after running the Query.
	 * </p>
	 * 
	 * @param uniqueColumnIndex Column index whose values would be unique. Preferably GRC Object Id.
	 * @param tabularResultSet ITabularResultSet after running Query.
	 * @param logger Instance of Logger
	 * @return Map<String, List<String>> A Map having key with the value at uniqueColumnIndex. Map value would be a List with all the row data.
	 * @throws Exception
	 */
	public static Map<String, List<String>> getQueryResultsAsMap(int uniqueColumnIndex, ITabularResultSet tabularResultSet, Logger logger) throws Exception {
		
		final String METHOD_NAME = CLASS_NAME + ":" + "getQueryResultsAsMap";
		
		Map<String, List<String>> queryResultsMap = new LinkedHashMap<String, List<String>>();
		
		List<String> rowData;
		
		if (CommonUtil.isObjectNull(tabularResultSet))
			return null;
		
		for (IResultSetRow resultSetRow : tabularResultSet) {
			
			if (CommonUtil.isObjectNull(resultSetRow))
				continue;
			
			rowData = new ArrayList<String>();
			
			for (IField field : resultSetRow) {
				
				rowData.add(GRCObjectUtil.getFieldValue(field));
			}
			
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "rowData.get(uniqueColumnIndex)", rowData.get(uniqueColumnIndex));
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "rowData", rowData);
			
			queryResultsMap.put(rowData.get(uniqueColumnIndex), rowData);
		}
		
		LoggerUtil.debugEXTLog(logger, METHOD_NAME, "queryResultsMap", queryResultsMap);
		return queryResultsMap;
	}
	
	/**
	 * <p>
	 * This method returns a List with the results of uniqueColumnIndex of each row after running the Query.
	 * </p>
	 * 
	 * @param queryStatement SQL Query Statement
	 * @param uniqueColumnIndex Column index whose values would be unique. Preferably GRC Object Id
	 * @param honorPrimary True to return only primary associations
	 * @param servicesUtil Instance of ServicesUtil Class
	 * @param logger Instance of Logger
	 * @return List<String> A List with values of uniqueColumnIndex of each row
	 * @throws Exception
	 */
	public static List<String> getQueryResultsAsList(String queryStatement, int uniqueColumnIndex, boolean honorPrimary, ServicesUtil servicesUtil, Logger logger) throws Exception {
		
		return getQueryResultsAsList(uniqueColumnIndex, runQuery(queryStatement, honorPrimary, servicesUtil), logger);
	}
	
	/**
	 * <p>
	 * This method returns a List with the results of uniqueColumnIndex of each row after running the Query.
	 * </p>
	 * 
	 * @param uniqueColumnIndex Column index whose values would be unique. Preferably GRC Object Id.
	 * @param tabularResultSet ITabularResultSet after running Query.
	 * @param logger Instance of Logger
	 * @return List<String> A List with values of uniqueColumnIndex of each row
	 * @throws Exception
	 */
	public static List<String> getQueryResultsAsList(int uniqueColumnIndex, ITabularResultSet tabularResultSet, Logger logger) throws Exception {
		
		final String METHOD_NAME = CLASS_NAME + ":" + "getQueryResultsAsList";
		
		List<String> queryResultsList = new ArrayList<String>();
		
		List<String> rowData;
		
		if (CommonUtil.isObjectNull(tabularResultSet))
			return null;
		
		for (IResultSetRow resultSetRow : tabularResultSet) {
			
			if (CommonUtil.isObjectNull(resultSetRow))
				continue;
			
			rowData = new ArrayList<String>();
			
			for (IField field : resultSetRow) {
				
				rowData.add(GRCObjectUtil.getFieldValue(field));
			}
			
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "rowData.get(uniqueColumnIndex)", rowData.get(uniqueColumnIndex));
			LoggerUtil.debugEXTLog(logger, METHOD_NAME, "rowData", rowData);
			
			queryResultsList.add(rowData.get(uniqueColumnIndex));
		}
		
		LoggerUtil.debugEXTLog(logger, METHOD_NAME, "queryResultsList", queryResultsList);
		return queryResultsList;
	}
}