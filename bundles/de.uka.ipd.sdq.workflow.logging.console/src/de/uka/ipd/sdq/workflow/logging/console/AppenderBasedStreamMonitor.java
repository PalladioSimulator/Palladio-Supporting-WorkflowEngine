package de.uka.ipd.sdq.workflow.logging.console;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IFlushableStreamMonitor;

/**
 * Base class of appender monitors. An appender monitor watches its added appenders for newly added
 * log lines. Whenever a new log line arrives at one of the appenders, this monitor sends out an
 * notification event to Eclipse which then updates its GUI or file based logs.
 * 
 * @author Steffen
 * 
 */
public class AppenderBasedStreamMonitor implements IFlushableStreamMonitor, IAppenderListener {

    /**
     * The Enum ComparisonOperator.
     */
    public enum ComparisonOperator {

        /** The LES s_ than. */
        LESS_THAN,

        /** The GREATE r_ o r_ equa l_ than. */
        GREATER_OR_EQUAL_THAN
    }

    /**
     * List of listeners provided by Eclipse which need to be informed of new log lines arriving at
     * one of the appenders added to this monitor.
     */
    private final List<IStreamListener> myListener = new ArrayList<>();

    /** Log level of this appender. */
    private final Priority internalLogLevel;

    /** The comparision operator. */
    private final ComparisonOperator comparisionOperator;

    /** Container for all log messages recorded by this monitor. */
    private StringBuffer logBuffer = new StringBuffer();
    private boolean buffered = true;

    /**
     * Base class constructor.
     * 
     * @param logLevel
     *            the log level
     * @param op
     *            the op
     */
    public AppenderBasedStreamMonitor(Level logLevel, ComparisonOperator op) {
        this.internalLogLevel = logLevel;
        this.comparisionOperator = op;
    }

    @Override
    public void flushContents() {
        logBuffer.setLength(0);
    }

    @Override
    public void setBuffered(boolean buffer) {
        this.buffered = buffer;
    }

    @Override
    public boolean isBuffered() {
        return buffered;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IStreamMonitor#addListener(org.eclipse.debug.core.
     * IStreamListener)
     */
    @Override
    public void addListener(IStreamListener listener) {
        myListener.add(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IStreamMonitor#removeListener(org.eclipse.debug.core.
     * IStreamListener)
     */
    @Override
    public void removeListener(IStreamListener listener) {
        this.myListener.remove(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IStreamMonitor#getContents()
     */
    @Override
    public String getContents() {
        return logBuffer.toString();
    }

    /**
     * Add an appender of type StreamsProxyAppender to this monitor. Whenever you write something to
     * the added appender, this monitor will notify all its listeners of the newly added text if it
     * is responsible for it (depending on the log level, see subclasses of this class)
     * 
     * @param appender
     *            A Log4J appender which will be used to write to Eclipse's run console
     */
    public void addAppender(StreamsProxyAppender appender) {
        appender.addAppenderListener(this);
    }

    /**
     * Removes the appender.
     * 
     * @param appender
     *            the appender
     */
    public void removeAppender(StreamsProxyAppender appender) {
        appender.removeAppenderListener(this);
    }

    /**
     * Helper method which informs all listeners of the newly recorded text.
     * 
     * @param text
     *            The new text on one of the Log4J appenders
     */
    protected void notifyListeners(String text) {
        for (IStreamListener listener : myListener) {
            listener.streamAppended(text, this);
        }
        if (buffered) {
            logBuffer.append(text);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.workflow.logging.console.IAppenderListener#textAddedEvent(java.lang.String,
     * org.apache.log4j.Level)
     */
    @Override
    public void textAddedEvent(String text, Level level) {
        if (comparisionOperator == ComparisonOperator.LESS_THAN) {
            if (!level.isGreaterOrEqual(internalLogLevel)) {
                notifyListeners(text);
            }
        } else {
            if (level.isGreaterOrEqual(internalLogLevel)) {
                notifyListeners(text);
            }
        }
    }
}
