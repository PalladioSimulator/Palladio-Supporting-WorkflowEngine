package de.uka.ipd.sdq.workflow.logging.console;

import org.apache.log4j.Logger;

/**
 * The Class LoggerAppenderStruct.
 */
public class LoggerAppenderStruct {

    /** The logger. */
    private Logger logger;

    /** The appender. */
    private StreamsProxyAppender appender;

    /**
     * Instantiates a new logger appender struct.
     * 
     * @param logger
     *            the logger
     * @param appender
     *            the appender
     */
    public LoggerAppenderStruct(Logger logger, StreamsProxyAppender appender) {
        super();
        this.logger = logger;
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
}
