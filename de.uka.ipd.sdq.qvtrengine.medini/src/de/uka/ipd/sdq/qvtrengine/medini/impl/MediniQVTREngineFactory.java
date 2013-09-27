package de.uka.ipd.sdq.qvtrengine.medini.impl;

import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.AbstractQVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngineFactory;

/**
 * Factory for the creation of a mediniQVT engine.
 * 
 * @author Thomas Schuischel
 * 
 */
public class MediniQVTREngineFactory implements QVTREngineFactory {

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngineFactory#createEngine()
     */
    @Override
    public AbstractQVTREngine createEngine() {
        return new MediniQVTREngine();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngineFactory#dispose()
     */
    @Override
    public void dispose() {
    } // nothing to do

}
