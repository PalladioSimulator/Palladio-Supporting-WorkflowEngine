package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;



import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;


public class XTextGeneratorModule extends AbstractGenericResourceRuntimeModule 
{
	
	private final Class<? extends IGenerator> generatorClass;
	private final String languageName;
	private final String fileExtension;
	
	public XTextGeneratorModule(Class<? extends IGenerator> generatorClass,
			String languageName, String fileExtension) {
		super();
		this.generatorClass = generatorClass;
		this.languageName = languageName;
		this.fileExtension = fileExtension;
	}

	@Override
	protected String getLanguageName() {
		return languageName;
	}

	@Override
	protected String getFileExtensions() {
		return fileExtension;
	}
	
	public Class<? extends IGenerator> bindIGenerator() {
		return generatorClass;
	}

	public Class<? extends ResourceSet> bindResourceSet() {
		return ResourceSetImpl.class;
	}

}
