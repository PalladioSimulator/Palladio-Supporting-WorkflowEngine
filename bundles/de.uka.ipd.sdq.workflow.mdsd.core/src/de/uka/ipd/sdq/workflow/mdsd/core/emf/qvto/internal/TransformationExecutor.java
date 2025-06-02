package de.uka.ipd.sdq.workflow.mdsd.core.emf.qvto.internal;

import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.InternalTransformationExecutor;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;

/**
 * The Class TransformationExecutor.
 */
@SuppressWarnings("restriction")
public class TransformationExecutor extends InternalTransformationExecutor {

    /** The trace. */
    private Trace trace;

    /**
     * Instantiates a new transformation executor.
     *
     * @param uri
     *            the uri
     */
    public TransformationExecutor(final URI uri) {
        super(uri);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.m2m.internal.qvt.oml.InternalTransformationExecutor#handleExecutionTraces(org
     * .eclipse.m2m.internal.qvt.oml.trace.Trace)
     */
    @Override
    protected void handleExecutionTraces(final Trace traces) {
        this.trace = traces;
    }

    /**
     * Gets the last trace.
     *
     * @return the last trace
     */
    public Trace getLastTrace() {
        return this.trace;
    }
}
