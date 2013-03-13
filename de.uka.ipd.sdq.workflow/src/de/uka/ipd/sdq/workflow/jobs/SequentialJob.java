package de.uka.ipd.sdq.workflow.jobs;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.ExecutionTimeLoggingProgressMonitor;

/**
 * Implementation of a composite job that guarantees the execution of it's jobs
 * in the order they were added.
 * 
 * @author Philipp Meier
 */

public class SequentialJob extends AbstractCompositeJob implements
		ICompositeJob {

	/**
	 * Instantiates a new order preserving composite job.
	 */
	public SequentialJob() {
		super();
	}

	/**
	 * Executes all contained jobs, i.e. call execute() for them. Contained jobs
	 * can thus re-implement this method with functionality that should be
	 * executed.
	 * 
	 * @param monitor
	 *            the monitor
	 * @throws JobFailedException
	 *             the job failed exception
	 * @throws UserCanceledException
	 *             the user canceled exception
	 */
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		IProgressMonitor subProgressMonitor = new ExecutionTimeLoggingProgressMonitor(
				monitor, 1);
		subProgressMonitor.beginTask("Composite Job Execution", myJobs.size());

		for (IJob job : myJobs) {
			if (monitor.isCanceled()) {
				throw new UserCanceledException();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("SDQ Workflow-Engine: Running job "
						+ job.getName());
			}
			subProgressMonitor.subTask(job.getName());
			myExecutedJobs.push(job);
			job.execute(subProgressMonitor);
			subProgressMonitor.worked(1);
		}
		subProgressMonitor.done();
	}
}
