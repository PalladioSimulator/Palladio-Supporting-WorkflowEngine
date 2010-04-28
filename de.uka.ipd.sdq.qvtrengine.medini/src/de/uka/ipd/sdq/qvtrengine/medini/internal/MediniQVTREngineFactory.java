package de.uka.ipd.sdq.qvtrengine.medini.internal;

import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngineFactory;

public class MediniQVTREngineFactory extends QVTREngineFactory {

	@Override
	public QVTREngine createEngine() {
		return new MediniQVTREngine();
	}

}
