package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;

/**
 * The Class XTextGeneratorModule.
 */
public class XTextGeneratorModule extends AbstractGenericResourceRuntimeModule {

    /** The generator class. */
    private final Class<? extends IGenerator> generatorClass;

    /** The language name. */
    private final String languageName;

    /** The file extension. */
    private final String fileExtension;

    /**
     * Instantiates a new x text generator module.
     *
     * @param generatorClass
     *            the generator class
     * @param languageName
     *            the language name
     * @param fileExtension
     *            the file extension
     */
    public XTextGeneratorModule(final Class<? extends IGenerator> generatorClass, final String languageName,
            final String fileExtension) {
        super();
        this.generatorClass = generatorClass;
        this.languageName = languageName;
        this.fileExtension = fileExtension;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule#getLanguageName()
     */
    @Override
    protected String getLanguageName() {
        return this.languageName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule#getFileExtensions()
     */
    @Override
    protected String getFileExtensions() {
        return this.fileExtension;
    }

    /**
     * Bind i generator.
     *
     * @return the class<? extends i generator>
     */
    public Class<? extends IGenerator> bindIGenerator() {
        return this.generatorClass;
    }

    /**
     * Bind resource set.
     *
     * @return the class<? extends resource set>
     */
    public Class<? extends ResourceSet> bindResourceSet() {
        return ResourceSetImpl.class;
    }

}
