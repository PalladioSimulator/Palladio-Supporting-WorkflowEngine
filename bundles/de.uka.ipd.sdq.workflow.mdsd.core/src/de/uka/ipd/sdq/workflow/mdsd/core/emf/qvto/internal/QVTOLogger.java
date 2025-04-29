package de.uka.ipd.sdq.workflow.mdsd.core.emf.qvto.internal;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.m2m.qvt.oml.util.Log;

/**
 * The Class QVTOLogger.
 */
public class QVTOLogger implements Log {

    /** The Constant logger. */
    private final Logger logger = Logger.getLogger(QVTOLogger.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.m2m.qvt.oml.util.Log#log(java.lang.String)
     */
    @Override
    public void log(final String message) {
        this.log(1, message, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.m2m.qvt.oml.util.Log#log(int, java.lang.String)
     */
    @Override
    public void log(final int level, final String message) {
        this.log(level, message, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.m2m.qvt.oml.util.Log#log(java.lang.String, java.lang.Object)
     */
    @Override
    public void log(final String message, final Object param) {
        this.log(1, message, param);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.m2m.qvt.oml.util.Log#log(int, java.lang.String, java.lang.Object)
     */
    @Override
    public void log(final int level, final String message, final Object param) {
        final String paramToString = param == null ? "" : " <" + param + ">";
        if (this.logger.isEnabledFor(Level.INFO)) {
            this.logger.info(message + paramToString);
        }
    }

}
