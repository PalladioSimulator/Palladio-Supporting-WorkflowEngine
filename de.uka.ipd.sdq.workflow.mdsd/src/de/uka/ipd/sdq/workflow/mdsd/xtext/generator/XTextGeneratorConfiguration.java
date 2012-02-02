package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;


import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.mwe.utils.StandaloneSetup;
import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.generator.GeneratorComponent;
import org.eclipse.xtext.generator.GeneratorComponent.Outlet;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.mwe.Reader;
import org.eclipse.xtext.mwe.ResourceLoadingSlotEntry;


public class XTextGeneratorConfiguration 
{
	private XTextGeneratorModule module;
	private ISetup setup;
	
	private final Class<? extends IGenerator> generatorClass;
	private final String languageName;
	private final String fileExtension;
	private final String outputPackageName;
	
	private final Set<String> slots = new TreeSet<String>();
	private final Set<Outlet> outlets = new TreeSet<Outlet>();
	
	private final Set<String> readerPathes = new TreeSet<String>();
	private final Set<ResourceLoadingSlotEntry> loadEntries = new TreeSet<ResourceLoadingSlotEntry>();
	
	private GeneratorComponent generator;
	
	
	public void initMWEBean()
	{
		StandaloneSetup setup = new StandaloneSetup();
		setup.addRegisterGeneratedEPackage(outputPackageName);
		setup.setScanClassPath(true);
	
	}
	
	public void setModelPath(String modelPath)
	{
		readerPathes.add(modelPath);
	}
	
	public void setTargetDir(String targetDir)
	{
		Outlet o = new Outlet();
		o.setPath(targetDir);
		outlets.add(o);
	}
	
	
	public void setSourceSlot(String slotName)
	{
		slots.add(slotName);
		ResourceLoadingSlotEntry loadEntry = new ResourceLoadingSlotEntry();
		loadEntry.setSlot(slotName);
		loadEntries.add(loadEntry);
		
	}
	
	
	
	
	/**
	 * @return the generatorClass
	 */
	public Class<? extends IGenerator> getGeneratorClass() {
		return generatorClass;
	}


	/**
	 * @return the languageName
	 */
	public String getLanguageName() {
		return languageName;
	}


	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}


	public XTextGeneratorConfiguration(Class<? extends IGenerator> generatorClass,
			String languageName, String fileExtension, String outputPackageName) {
		super();
		this.generatorClass = generatorClass;
		this.languageName = languageName;
		this.fileExtension = fileExtension;
		this.outputPackageName = outputPackageName;
	}
	
	public Reader createReader()
	{
		Reader reader = new Reader();
		reader.addRegister(setup);
		for(String path : readerPathes)
			reader.addPath(path);
		
		for(ResourceLoadingSlotEntry entry : loadEntries)
			reader.addLoadResource(entry);
		
		return reader;
		
	}
	
	
	public GeneratorComponent createGenerator()
	{
		generator = new GeneratorComponent();
		generator.setRegister(setup);
		for(String slot : slots)
			generator.addSlot(slot);
				
		for(Outlet outlet : outlets)
			generator.addOutlet(outlet);
		
		return generator;
	}
	
	
	public void createGeneratorModuleAndGeneratorSetup()
	{
		this.module = new XTextGeneratorModule(generatorClass, languageName, fileExtension);
		this.setup = new XTextGeneratorSetup(this.module);
		
	}
	
	
	public XTextGeneratorSupport createGeneratorSupport()
	{
		return new XTextGeneratorSupport(module);
	}
	
	
	

}
