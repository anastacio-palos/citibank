package com.ibm.openpages.ext.common.util;

import static com.ibm.openpages.ext.common.util.CommonConstants.EMPTY_STRING;
import static com.ibm.openpages.ext.common.util.CommonUtil.getStackTrace;
import static com.ibm.openpages.ext.common.util.CommonUtil.isObjectNotNull;
import static com.openpages.apps.common.i18n.I18NClient.getApplicationText;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.openpages.aurora.common.Environment;
import com.ibm.openpages.api.logging.LoggerFactory;

public class LoggerUtil {	  

	private static final String CLASS_NAME = "LoggerUtil";
	
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String MAX_LOG_BACKUP_FILES = "200";
    private static final String MAX_LOG_FILE_SIZE = "128KB";
    private static final int LOG_IO_BUFFER_SIZE_BYTES = 1024;
    private static final String OPENPAGES_HOME = Environment.getOpenpagesHomeDir();
    private static final String LOG_DIR = OPENPAGES_HOME + FILE_SEPARATOR + "aurora" + FILE_SEPARATOR + "logs" + FILE_SEPARATOR + "custom logs";
    private static final boolean APPEND = true;
    private static final String PATTERN = "%-5p[%d](%F:%L)%n%m%n%n";
    
    private static final Log logger = LoggerFactory.getLoggerFactory().getLogger();
	
	/**
	 * <p>
	 * This method returns Logger referencing to a specific log file.
	 * <P>
     * @param logFilePath The registry value containing relative path and name of the custom log.
     * @param isDebugModeEnabled true/false String values.
     * @return Logger
     */
    public static Logger getEXTLogger(String logFilePath, String isDebugModeEnabled) {
		
		int index = logFilePath.lastIndexOf(File.separator);
		String fileName = logFilePath.substring(index + 1);
		String filePath = logFilePath.substring(0,logFilePath.lastIndexOf(File.separator));	
    	
		Logger extLogger = null;
    	Level level = null; 
		
    	// Method Implementation.
    	/* Get the debug level logging from the value in the Registry setting. This decides if a logger 
		 * Level should be enabled or not
		 */
        level = (isDebugModeEnabled.equalsIgnoreCase("true")) ? Level.ALL : Level.ERROR;

        
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);

		// Checking if a Logger with similar name already exists. If exists, retrieve it.
		if(ctx.hasLogger(fileName))
		{
			
			logger.error("Using existing logger having name " + fileName);
            extLogger = (Logger) LogManager.getLogger(fileName);
            extLogger.setLevel(level);
            return extLogger;
		}
    	
    	// Method Level Variables.
    	String file = EMPTY_STRING;
    	String fullPath = EMPTY_STRING;
    	File logDir = null;
    	File logFile = null;
    	
    	try {
            
            /* Check if the static variable LOGGER is null if so go on to create the Logger Object*/
    		
        	/* Instantiate all necessary Log File values. A new log will be 
        	 * created under the LOG DIR - <INSTALL_DIR>/OpenPages/aurora/logs.
        	 */
            fullPath = LOG_DIR + filePath;
            logDir = new File(fullPath);                    
            logDir.mkdirs();
            logFile = new File(logDir, String.format("%s%s", FILE_SEPARATOR, fileName));
            file = logFile.getCanonicalPath();
            
            logger.error(fileName + " logger is null - trying to init it...");
            extLogger = (Logger) LogManager.getLogger(fileName);
            
            /* Set the log file pattern and the Rolling File Appender if the file grows Larger. */
            PatternLayout  layout = PatternLayout.newBuilder().withPattern(PATTERN).build();

            final DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder().withMax(MAX_LOG_BACKUP_FILES).withFileIndex("max").build();
            
            RollingFileAppender appender = RollingFileAppender.newBuilder().withFileName(file).setName(fileName).withAppend(APPEND)
            		 .withBufferedIo(false).withBufferSize(LOG_IO_BUFFER_SIZE_BYTES).withImmediateFlush(true)
            		 .withFilePattern(LOG_DIR + logFilePath + ".%i")
                     .withPolicy((TriggeringPolicy)SizeBasedTriggeringPolicy.createPolicy(MAX_LOG_FILE_SIZE))
                     .withStrategy((RolloverStrategy)strategy).setLayout(layout).build();
            
            appender.start();
            
            /* Finally set all the Rolling File Appender options to the created Logger. */
            extLogger.addAppender(appender);
    	
        } catch (Exception e) {
            logger.info(
                    "could not init custom logger - " + e.getMessage());
        } finally {
            logger.info(
                    "Init custom logger Successfully - see " + file);
        }
        
    	/* Finally set the Logger level and return the Ext Logger. */
    	extLogger.setLevel(level);
        return extLogger;
    }
    
    /**
     * <p>
     * Method that logs the information in DEBUG mode in the Custom Log file using the EXT Logger.
     * </p>
     * 
     * @param logger Logger handle to custom log.
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void debugEXTLog(final Logger logger, final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;    	
    	
    	try {
    		
    		logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";
			logger.debug("[ " + methodName + " ] [ " + name + " ] : " + logMessage);
		} catch (Exception ex) {
			
			debug(CLASS_NAME, "debugEXTLog()",  " Error while Logging EXT debug",  getStackTrace(ex));
		}
        
    }

    /**
     * <p>
     * Method that logs the information in ERROR mode in the Custom Log file using the EXT Logger.
     * </p>
     * 
     * @param logger Logger handle to custom log.
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void errorEXTLog(final Logger logger, final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;
    	
    	try {
    		
    		logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";
			logger.error(" [ " + methodName + " ] [ " + name + " ] : " + logMessage);
			error(methodName, name, logMessage);
		} catch (Exception ex) {
			
			error(CLASS_NAME, "errorEXTLog()",  " Error while Logging EXT Error",  getStackTrace(ex));
		}
    }    

    /**
     * <p>
     * Method that logs the information in INFO mode in the Custom Log file using the EXT Logger.
     * </p>
     * 
     * @param logger Logger handle to custom log.
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void infoEXTLog(final Logger logger, final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;
    	
    	try {
    		
    		logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";
    		logger.info(" [ " + methodName + " ] [ " + name + " ] : " + logMessage);
		} catch (Exception ex) {
			
			error(CLASS_NAME, "infoEXTLog()",  " Error while Logging EXT Info",  getStackTrace(ex));
		}
    }

    /**
     * <p>
     * Method that logs the information in FATAL mode in the Custom Log file using the EXT Logger.
     * </p>
     * 
     * @param logger Logger handle to custom log.
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void fatalEXTLog(final Logger logger, final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;
    	
    	try {
    		
    		logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";
    		logger.fatal(" [ " + methodName + " ] [ " + name + "] : " + logMessage);
		} catch (Exception ex) {
			
			error(CLASS_NAME, "fatalEXTLog()",  " Error while Logging EXT Fatal",  getStackTrace(ex));
		}        
    }
    
    /**
     * <p>
     * Method that logs the information in DEBUG mode.
     * </p>
     * 
     * @param className The name of the class invoking the logger.
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void debug(final String className, final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;
    	
    	logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";
    	logger.debug("[ " + className + " ] [ " + methodName + " ] [ " + name + "] : " + logMessage);
        
    }

    /**
     * <p>
     * Method that logs the information in ERROR mode.
     * </p>
     * 
     * @param className The name of the class invoking the logger.
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void error(final String className, final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;
    	
    	logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";
    	logger.error("[ " + className + " ] [ " + methodName + " ] [ " + name + "] : " + logMessage);
    }  
    
    /**
     * <p>
     * Method that logs the information in ERROR mode.
     * </p>
     * 
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void error(final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;
    	
    	logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";
    	logger.error("[ " + methodName + " ] [ " + name + "] : " + logMessage);
    }

    /**
     * <p>
     * Method that logs the information in INFO mode.
     * </p>
     * 
     * @param className The name of the class invoking the logger.
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void info(final String className, final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;
    	
    	logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";    	
    	logger.info("[ " + className + " ] [ " + methodName + " ] [ " + name + "] : " + logMessage);
    }

    /**
     * <p>
     * Method that logs the information in FATAL mode.
     * </p>
     * 
     * @param className The name of the class invoking the logger.
     * @param methodName The name of the method thats invoking the logger.
     * @param name Any name or prefix for the message or content that needs to be logged.
     * @param message Actual log message or content that needs to be logged.
     */
    public static void fatal(final String className, final String methodName, final String name, final Object message) {
    	
    	String logMessage = EMPTY_STRING;
    	
    	logMessage = (isObjectNotNull(message)) ? message.toString() : "Null";    	
    	logger.fatal("[ " + className + " ] [ " + methodName + " ] [ " + name + "] : " + logMessage);
        
    }

	/**
	 * <p>
	 * This method get the Application Text and logs the value accordingly.
	 * </p>
	 * 
	 * @param request
	 * @param appStringKey
	 */
	public static void logMessageFromAppString(HttpServletRequest request, String appStringKey) {
		
		// Method Level Variables.
		String logMessage = EMPTY_STRING;
		
		// Method Implementation.
	    try {
	    	
	        logMessage = getApplicationText(appStringKey, request);
	        logger.info(logMessage);
	    }
	    catch (Exception ex) {
	    	
	    	logger.error(ex);
	    }
	}

	/**
	 * <p>
	 * Logs message from Application Text with the parameters passed
	 * </p>
	 * 
	 * @param request
	 * @param appStringKey
	 * @param paramArray
	 * @throws Exception
	 */
	public static void logFormattedAppString(HttpServletRequest request, 
											 String appStringKey, 
											 Object[] paramArray) throws Exception {    	
	
		// Method Implementation.
		logger.info("");
	}
}
