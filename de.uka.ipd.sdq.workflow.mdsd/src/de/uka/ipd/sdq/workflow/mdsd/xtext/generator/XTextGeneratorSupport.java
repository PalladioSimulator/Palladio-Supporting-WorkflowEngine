package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.xtext.resource.generic.AbstractGenericResourceSupport;

import com.google.inject.Module;

/**
 * The Class XTextGeneratorSupport.
 */
public class XTextGeneratorSupport extends AbstractGenericResourceSupport {

    /** The module. */
    private final XTextGeneratorModule module;

    /**
     * Instantiates a new x text generator support.
     * 
     * @param module
     *            the module
     */
    public XTextGeneratorSupport(XTextGeneratorModule module) {
        super();
        this.module = module;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.xtext.resource.generic.AbstractGenericResourceSupport#createGuiceModule()
     */
    @Override
    protected Module createGuiceModule() {
        return module;
    }

}
