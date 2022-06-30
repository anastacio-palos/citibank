package com.ibm.openpages.ext.interfaces.common.util;

import com.ibm.openpages.api.logging.LoggerFactory;
import com.openpages.aurora.common.Environment;
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


import java.io.File;
import static com.openpages.apps.common.util.StringConstants.EMPTY_STRING;

/**
 * <p>
 * The <code>LoggerUtil</code> contains the methods used to generate logs used in Openpages to CMP / iCAPS interfaces.
 * </p>
 * @author Abdias Morales <BR>
 * email : abdias.morales@ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 12-13-2021
 */
public class LoggerUtil {

    private static final String CLASS_NAME = "LoggerUtil";
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String OPENPAGES_HOME = Environment.getOpenpagesHomeDir();
    private static final String LOG_DIR = OPENPAGES_HOME + FILE_SEPARATOR + "aurora" + FILE_SEPARATOR + "logs"+ FILE_SEPARATOR + "interface";
    private static final String MAX_LOG_BACKUP_FILES = "25";
    private static final String MAX_LOG_FILE_SIZE = "128KB";
    private static final int LOG_IO_BUFFER_SIZE_BYTES = 1024;
    private static final boolean APPEND = true;
    private static final String PATTERN = "%-5p[%d](%F:%L)%n%m%n%n";
    private static final String DEBUG = "debug";
    private static final String WARN = "warn";
    private static final String INFO = "info";
    private static final String ERROR = "error";
    private static final String ALL = "all";
    private static final String FATAL = "fatal";
    private static final String OFF = "off";
    private static final String TRACE = "trace";

    private static final Log logger = LoggerFactory.getLoggerFactory().getLogger();
    /**
     * <p>
     *  This method returns Logger referencing to a specific log file.
     * <P>
     * @param logFilePath
     *              The registry value containing relative path and name of the custom log.
     * @param isDebugModeEnabled
     *              true/false String values.
     * @param logFileSize
     *              limit filesize of the log file.
     * @return an instance of {@link Logger}.
     */
    public static org.apache.logging.log4j.Logger getEXTLogger(String logFilePath, String isDebugModeEnabled, String logFileSize,String logLevel) {
        return getEXTLogger(logFilePath, isDebugModeEnabled, logFileSize, logLevel, MAX_LOG_BACKUP_FILES);
    }


    /**
     * <p>
     *  This method returns Logger referencing to a specific log file.
     * <P>
     * @param logFilePath
     *              The registry value containing relative path and name of the custom log.
     * @param isDebugModeEnabled
     *              true/false String values.
     * @param logFileSize
     *              limit filesize of the log file.
     * @return an instance of {@link Logger}.
     */
    public static org.apache.logging.log4j.Logger getEXTLogger(String logFilePath, String isDebugModeEnabled, String logFileSize,String logLevel, String maxFiles) {

        int index = logFilePath.lastIndexOf(File.separator);
        String fileName = logFilePath.substring(index + 1);
        String filePath = logFilePath.substring(0,logFilePath.lastIndexOf(File.separator));

        Logger extLogger = null;
        Level level;

        // Method Implementation.
        /* Get the debug level logging from the value in the Registry setting. This decides if a logger
         * Level should be enabled or not
         */

        level = getLogLevel(isDebugModeEnabled,logLevel);

        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);

        // Checking if a Logger with similar name already exists. If exists, retrieve it.
        if (ctx.hasLogger(fileName)) {

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
        PatternLayout layout = null;
        RollingFileAppender rollingFileAppender = null;

        try {

            /* Check if the static variable LOGGER is null if so go on to create the Logger Object*/
            logger.debug(fileName + " logger is null - trying to init it...");
            extLogger = (Logger) LogManager.getLogger(fileName);

            /* Instantiate all necessary Log File values. A new log will be
             * created under the LOG DIR - <INSTALL_DIR>/OpenPages/aurora/logs.
             */
            fullPath = LOG_DIR + filePath;
            logDir = new File(fullPath);
            logDir.mkdirs();
            logFile = new File(logDir, String.format("%s%s", FILE_SEPARATOR, fileName));
            file = logFile.getCanonicalPath();



            /* Set the log file pattern and the Rolling File Appender if the file grows Larger. */
            /* The Logger should flush its output to the log file stream immediately. If not Log4j will
             * buffer the log file output which is not ideal.
             */
            /* Set the maximum log file size and the Max backup files required. */
            /* Set the log file pattern and the Rolling File Appender if the file grows Larger. */
            layout = PatternLayout.newBuilder().withPattern(PATTERN).build();
            final DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder().withMax(maxFiles).withFileIndex("max").build();

            rollingFileAppender = RollingFileAppender.newBuilder().withFileName(file).setName(fileName).withAppend(APPEND)
                    .withBufferedIo(false).withBufferSize(LOG_IO_BUFFER_SIZE_BYTES).withImmediateFlush(true)
                    .withFilePattern(LOG_DIR + logFilePath + ".%i")
                    .withPolicy((TriggeringPolicy) SizeBasedTriggeringPolicy.createPolicy(logFileSize))
                    .withStrategy((RolloverStrategy) strategy).setLayout(layout).build();

            rollingFileAppender.start();

            /* Finally set all the Rolling File Appender options to the created Logger. */
            extLogger.addAppender(rollingFileAppender);

        } catch (Exception e) {
            logger.error(
                    "could not init custom logger - " + e.getMessage());
        } finally {
            logger.debug(
                    "Init custom logger Successfully - see " + file);
        }

        /* Finally set the Logger level and return the Ext Logger. */
        extLogger.setLevel(level);
        return extLogger;
    }

    /**
     * <p>
     *  This method returns the current log level.
     * <P>
     * @param isDebugModeEnabled
	 *				true/false {@link String} values.
     * @param logLevel
	 *				DEBUG/INFO/ERROR/ALL/FATAL/OFF/TRACE {@link String} values.
     * @return an instance of {@link Level}.
     */
    private static Level getLogLevel(String isDebugModeEnabled, String logLevel){
        Level level = null;
        if(isDebugModeEnabled.equalsIgnoreCase("true")){
            if(logLevel.equalsIgnoreCase(DEBUG)){
                level=Level.DEBUG;
            }else if(logLevel.equalsIgnoreCase(WARN)){
                level=Level.WARN;
            }else if(logLevel.equalsIgnoreCase(INFO)){
                level=Level.INFO;
            }else if(logLevel.equalsIgnoreCase(ERROR)){
                level=Level.ERROR;
            }else if(logLevel.equalsIgnoreCase(ALL)){
                level=Level.ALL;
            }else if(logLevel.equalsIgnoreCase(FATAL)){
                level=Level.FATAL;
            }else if(logLevel.equalsIgnoreCase(OFF)){
                level=Level.OFF;
            }else if(logLevel.equalsIgnoreCase(TRACE)){
                level=Level.TRACE;
            }
        }else {
            level = Level.ERROR;
        }
        return level;
    }
}
