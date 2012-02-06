package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.emf.mwe2.runtime.workflow.WorkflowContextImpl;
import org.eclipse.xtext.generator.GeneratorComponent;


import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

import de.uka.ipd.sdq.workflow.mdsd.xtext.IBlackboardInteractingWorkflowComponent;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2BlackboardWorkflowComponentBridge;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2WorkflowComponentBridge;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2OrderPreservingCompositeJob;




public class XTextGeneratorBlackboardCompositeJob extends MWE2OrderPreservingCompositeJob implements
IBlackboardInteractingJob<MDSDBlackboard>
{
	private MDSDBlackboard blackboard;
	
	
	/**
	 * Executes all contained jobs, i.e. call execute() for them. Contained
	 * jobs can thus re-implement this method with functionality that should
	 * be executed.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		for (IJob job : this.myJobs){
			if (job instanceof IBlackboardInteractingJob){
				((IBlackboardInteractingJob) job).setBlackboard(this.blackboard);
			}
			else if (job instanceof IBlackboardInteractingWorkflowComponent)
				((IBlackboardInteractingWorkflowComponent) job).setBlackboard(this.blackboard);
			
		}
		super.execute(monitor);
	}
	
	
	public XTextGeneratorBlackboardCompositeJob(XTextGeneratorConfiguration config)
	{
	
		//Setup all 
		config.initMWEBean();
		//Create context
		IWorkflowContext ctx = new WorkflowContextImpl();
		config.initGeneratorModuleAndGeneratorSetup();
		
		this.addJob(new PrepareXTextPartitionJob(config.getBlackboardModelLocation()));
		
		this.addJob(new MWE2WorkflowComponentBridge<XTextGeneratorSupport>(config.createGeneratorSupport(), ctx, "XTextGeneratorSupport Job"));
		
		this.addJob(new MWE2BlackboardWorkflowComponentBridge<BlackboardReader>(config.createBlackboardReader(), ctx, "MWEReader Job"));
		
		this.addJob(new MWE2WorkflowComponentBridge<GeneratorComponent>(config.createGenerator(), ctx, "XTextGenerator Job"));
		
		
	}

	@Override
	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
		
	}

}
