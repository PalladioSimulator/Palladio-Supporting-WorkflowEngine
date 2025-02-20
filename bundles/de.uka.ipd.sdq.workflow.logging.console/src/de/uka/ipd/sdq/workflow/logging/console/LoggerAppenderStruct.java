package de.uka.ipd.sdq.workflow.logging.console;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * The Class LoggerAppenderStruct.
 */
public class LoggerAppenderStruct {

    private final Logger logger;
    private final Level logLevel;

    /** The appender. */
    private final StreamsProxyAppender appender;

    /**
     * Instantiates a new logger appender struct.
     * 
     * @param logger
     *            the logger
     * @param appender
     *            the appender
     */
    public LoggerAppenderStruct(Logger logger, Level logLevel, StreamsProxyAppender appender) {
        this.logger = logger;
        this.logLevel = logLevel;
        this.appender = appender;
    }

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets the appender.
     * 
     * @return the appender
     */
    public StreamsProxyAppender getAppender() {
        return appender;
    }

    public Level getLogLevel() {
        return logLevel;
    }
}
