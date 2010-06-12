package de.uka.ipd.sdq.workflow;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;

/**
 * A sequential workflow which may contain jobs which need access to a common blackboard
 * for information exchange
 * @author Steffen
 * @param <BlackboardType> The type of the blackboard needed by all jobs in the sequential workflow
 */
public class LowMemoryFootprintCompositeJob<BlackboardType extends Blackboard<?>> 
extends OrderPreservingBlackboardCompositeJob<BlackboardType> {

	/**
	 * constructor
	 */
	public LowMemoryFootprintCompositeJob() {
		super();
	}

	/** 
	 * Executes all contained jobs, i.e. call execute() for them. Contained 
	 * jobs can thus re-implement this method with functionality that should 
	 * be executed.
	 */ 
	@SuppressWarnings("unchecked")
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {

		IProgressMonitor subProgressMonitor = new ExecutionTimeLoggingProgressMonitor(monitor, 1);
		subProgressMonitor.beginTask("Composite Job Execution", myJobs.size());
		
		int totalNumberOfJobs = myJobs.size();
		for (int i=0; i<totalNumberOfJobs; i++) {
			if (monitor.isCanceled()) {
				throw new UserCanceledException();
			}
			IJob job = myJobs.getFirst();
			if (job instanceof IBlackboardInteractingJob){
				((IBlackboardInteractingJob) job).setBlackboard(this.myBlackboard);
			}
			logger.debug("SDQ Workflow-Engine: Running job "+job.getName());
			subProgressMonitor.subTask(job.getName());
			job.execute(subProgressMonitor);
			subProgressMonitor.worked(1);
			myJobs.removeFirst();
			job = null;
		}
		subProgressMonitor.done();
	}
}
