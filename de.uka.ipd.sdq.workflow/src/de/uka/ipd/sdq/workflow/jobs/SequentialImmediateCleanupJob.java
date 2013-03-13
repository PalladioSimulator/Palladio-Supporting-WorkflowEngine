package de.uka.ipd.sdq.workflow.jobs;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.ExecutionTimeLoggingProgressMonitor;
import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * A sequential workflow which may contain jobs which need access to a common
 * blackboard for information exchange. Compared to a
 * SequentialBlackboardInteractingJob, this job has a lower memory footprint. In
 * addition, a cleanup of a nested job is being executed immediately after the
 * nested job has completed.
 * 
 * @param <BlackboardType>
 *            The type of the blackboard needed by all jobs in the sequential
 *            workflow
 * @author Michael Hauck
 */
public class SequentialImmediateCleanupJob<BlackboardType extends Blackboard<?>>
		extends SequentialBlackboardInteractingJob<BlackboardType> {

	/**
	 * Instantiates a new low memory footprint composite job.
	 */
	public SequentialImmediateCleanupJob() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Specialty: Calls cleanup after the execution of each nested job and
	 * deletes the reference to that nested job. Thus, you need to make sure
	 * that no later jobs depend on these jobs intermediate results that are
	 * deleted during cleanup.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {

		IProgressMonitor subProgressMonitor = new ExecutionTimeLoggingProgressMonitor(
				monitor, 1);
		subProgressMonitor.beginTask("Composite Job Execution", myJobs.size());

		int totalNumberOfJobs = myJobs.size();
		for (int i = 0; i < totalNumberOfJobs; i++) {
			if (monitor.isCanceled()) {
				throw new UserCanceledException();
			}
			IJob job = myJobs.getFirst();
			if (job instanceof IBlackboardInteractingJob) {
				((IBlackboardInteractingJob) job)
						.setBlackboard(this.myBlackboard);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("SDQ Workflow-Engine: Running job "
						+ job.getName());
			}
			subProgressMonitor.subTask(job.getName());
			job.execute(subProgressMonitor);
			subProgressMonitor.worked(1);
			subProgressMonitor.subTask("Cleaning up job " + job.getName());
			try {
				job.cleanup(subProgressMonitor);
			} catch (CleanupFailedException e) {
				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn("Failed to cleanup job " + job.getName());
				}
			}
			subProgressMonitor.worked(1);
			myJobs.removeFirst();
			job = null;
		}
		subProgressMonitor.done();
	}

	/**
	 * {@inheritDoc}<br>
	 * <br>
	 * 
	 * Compared to a SequentialBlackboardInteractingJob, this method does not
	 * invoke a cleanup on nested jobs. For every nested job, the cleanup method
	 * is being called immediately after the job has completed (in
	 * {@link #execute(IProgressMonitor)}).
	 * 
	 */
	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		// Do nothing. Cleanup for every nested job is being called immediately
		// after the job has
		// been executed.
	}
}
