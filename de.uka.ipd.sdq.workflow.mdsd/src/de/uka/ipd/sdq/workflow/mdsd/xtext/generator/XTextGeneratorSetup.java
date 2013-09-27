package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.xtext.ISetup;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The Class XTextGeneratorSetup.
 */
public class XTextGeneratorSetup implements ISetup {

    /** The module. */
    private final XTextGeneratorModule module;

    /**
     * Instantiates a new x text generator setup.
     * 
     * @param module
     *            the module
     */
    public XTextGeneratorSetup(XTextGeneratorModule module) {
        super();
        this.module = module;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.xtext.ISetup#createInjectorAndDoEMFRegistration()
     */
    @Override
    public Injector createInjectorAndDoEMFRegistration() {
        return Guice.createInjector(module);
    }
}