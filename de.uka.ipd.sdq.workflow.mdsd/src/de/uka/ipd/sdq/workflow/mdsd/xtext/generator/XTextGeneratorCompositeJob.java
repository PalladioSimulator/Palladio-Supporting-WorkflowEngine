package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.emf.mwe2.runtime.workflow.WorkflowContextImpl;
import org.eclipse.xtext.generator.GeneratorComponent;
import org.eclipse.xtext.mwe.Reader;

import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2WorkflowComponentBridge;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2OrderPreservingCompositeJob;




public class XTextGeneratorCompositeJob extends MWE2OrderPreservingCompositeJob 
{
	
	public XTextGeneratorCompositeJob(XTextGeneratorConfiguration config)
	{
	
		//Setup all 
		config.initMWEBean();
		//Create context
		IWorkflowContext ctx = new WorkflowContextImpl();
		config.initGeneratorModuleAndGeneratorSetup();
		
		this.addJob(new MWE2WorkflowComponentBridge<XTextGeneratorSupport>(config.createGeneratorSupport(), ctx, "XTextGeneratorSupport Job"));
		this.addJob(new MWE2WorkflowComponentBridge<Reader>(config.createReader(), ctx, "MWEReader Job"));
		this.addJob(new MWE2WorkflowComponentBridge<GeneratorComponent>(config.createGenerator(), ctx, "XTextGenerator Job"));
		
		
	}

}
