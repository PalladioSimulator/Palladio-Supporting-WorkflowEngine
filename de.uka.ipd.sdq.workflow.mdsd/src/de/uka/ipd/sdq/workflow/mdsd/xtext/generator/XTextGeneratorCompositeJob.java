package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.emf.mwe2.runtime.workflow.WorkflowContextImpl;
import org.eclipse.xtext.generator.GeneratorComponent;
import org.eclipse.xtext.mwe.Reader;

import de.uka.ipd.sdq.workflow.mdsd.xtext.AbstractWorkflowComponentBridge;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2OrderPreservingCompositeJob;




public class XTextGeneratorCompositeJob extends MWE2OrderPreservingCompositeJob 
{
	
	public XTextGeneratorCompositeJob(XTextGeneratorConfiguration config)
	{
	
		//Setup all 
		config.initMWEBean();
		//Create context
		IWorkflowContext ctx = new WorkflowContextImpl();
		config.createGeneratorModuleAndGeneratorSetup();
		
		this.addJob(new AbstractWorkflowComponentBridge<XTextGeneratorSupport>(config.createGeneratorSupport(), ctx, "XTextGeneratorSupport Job"));
		this.addJob(new AbstractWorkflowComponentBridge<Reader>(config.createReader(), ctx, "MWEReader Job"));
		this.addJob(new AbstractWorkflowComponentBridge<GeneratorComponent>(config.createGenerator(), ctx, "XTextGenerator Job"));
		
		
	}

}
