package de.uka.ipd.sdq.workflow.mdsd.core.emf.qvto;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;

/**
 * The Class QVTOResult.
 */
@SuppressWarnings("restriction")
public class QVTOResult {

    /** The diagnostic result. */
    private final org.eclipse.m2m.qvt.oml.ExecutionDiagnostic diagnosticResult;
    private final Trace trace;

    /**
     * Instantiates a new qVTO result.
     * 
     * @param result
     *            the result
     */
    public QVTOResult(final org.eclipse.m2m.qvt.oml.ExecutionDiagnostic result) {
        this(result, null);
    }

    /**
     * Instantiates a new qVTO result.
     * 
     * @param result
     *            the result
     * @param trace
     *            the trace
     */
    public QVTOResult(final org.eclipse.m2m.qvt.oml.ExecutionDiagnostic result, final Trace trace) {
        super();
        this.diagnosticResult = result;
        this.trace = trace;
    }

    /**
     * Gets the diagnostic result.
     * 
     * @return the diagnosticResult
     */
    public org.eclipse.m2m.qvt.oml.ExecutionDiagnostic getDiagnosticResult() {
        return this.diagnosticResult;
    }

    /**
     * Gets the trace of the transformation execution.
     * 
     * @return the transformation trace.
     */
    public Trace getTrace() {
        return this.trace;
    }

    /**
     * Checks if is success.
     * 
     * @return true, if is success
     */
    public boolean isSuccess() {
        return this.diagnosticResult.getSeverity() == Diagnostic.OK;
    }

    /**
     * Dump the stack trace of the QVTO Engine.
     * 
     * @param logger
     *            Logger used to dump the stack trace
     * @param level
     *            The log level used to log the stack trace
     */
    public void logStackTrace(final Logger logger, final Level level) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        this.diagnosticResult.printStackTrace(pw);
        if (logger.isEnabledFor(level)) {
            logger.log(level, sw.toString());
        }
    }
}
