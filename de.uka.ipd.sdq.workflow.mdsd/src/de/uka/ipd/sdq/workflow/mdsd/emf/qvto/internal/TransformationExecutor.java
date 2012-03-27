package de.uka.ipd.sdq.workflow.mdsd.emf.qvto.internal;

import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.InternalTransformationExecutor;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;

@SuppressWarnings("restriction")
public class TransformationExecutor 
	extends InternalTransformationExecutor {
	
	private Trace trace;

	public TransformationExecutor(URI uri) {
		super(uri);
	}

	@Override
	protected void handleExecutionTraces(Trace traces) {
		trace = traces;
	}

	public Trace getLastTrace() {
		return trace;
	}
}
