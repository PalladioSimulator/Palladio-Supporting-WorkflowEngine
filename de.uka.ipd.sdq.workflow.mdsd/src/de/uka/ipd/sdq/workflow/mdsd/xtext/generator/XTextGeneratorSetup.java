package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.xtext.ISetup;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class XTextGeneratorSetup implements ISetup {

	private final XTextGeneratorModule module;
	
	public XTextGeneratorSetup(XTextGeneratorModule module) {
		super();
		this.module = module;
	}

	
	@Override
	public Injector createInjectorAndDoEMFRegistration() {
				return Guice.createInjector(module);
	}
}