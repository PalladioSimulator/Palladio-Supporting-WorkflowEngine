package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.xtext.resource.generic.AbstractGenericResourceSupport;

import com.google.inject.Module;

public class XTextGeneratorSupport extends AbstractGenericResourceSupport 
{
	
	private final XTextGeneratorModule module;
	
	public XTextGeneratorSupport(XTextGeneratorModule module) {
		super();
		this.module = module;
	}

	@Override
	protected Module createGuiceModule() {
		return module;
	}

}
