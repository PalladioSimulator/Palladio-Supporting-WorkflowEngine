package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr;

public abstract class QVTREngineFactory {
	public abstract QVTREngine createEngine();
	public void dispose() {
		// Nothing to do... subclasses may override.
	}
}
