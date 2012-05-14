package de.uka.ipd.sdq.workflow.mdsd.emf.qvto;

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

    /**
     * Instantiates a new qVTO result.
     * 
     * @param result
     *            the result
     */
    public QVTOResult(org.eclipse.m2m.qvt.oml.ExecutionDiagnostic result) {
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
    public QVTOResult(org.eclipse.m2m.qvt.oml.ExecutionDiagnostic result, Trace trace) {
        super();

        this.diagnosticResult = result;
    }

    /**
     * Gets the diagnostic result.
     * 
     * @return the diagnosticResult
     */
    public org.eclipse.m2m.qvt.oml.ExecutionDiagnostic getDiagnosticResult() {
        return diagnosticResult;
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
    public void logStackTrace(Logger logger, Level level) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        this.diagnosticResult.printStackTrace(pw);
        logger.log(level, sw.toString());
    }
}
