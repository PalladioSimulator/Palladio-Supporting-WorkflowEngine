package de.uka.ipd.sdq.workflow.mdsd.xtext;

import org.eclipse.core.runtime.IProgressMonitor;


import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.OrderPreservingCompositeJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;

/**
 * OrderPreservingCompositeJob for MWE2 WorkflowComponents.
 * 
 * @author Joerg Henss
 *
 */
public class MWE2OrderPreservingCompositeJob extends
		OrderPreservingCompositeJob 
		{
	
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		for (IJob job : this.myJobs){
			if (job instanceof IPrePostJob){
				((IPrePostJob) job).preExecute();
			}
		}
		super.execute(monitor);
		
		for (IJob job : this.myJobs){
			if (job instanceof IPrePostJob){
				((IPrePostJob) job).postExecute();
			}
		}
		
	}

}
