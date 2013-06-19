package de.uka.ipd.sdq.workflow.jobs;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.ExecutionTimeLoggingProgressMonitor;

/**
 * Implementation of a composite job that guarantees the execution of it's jobs
 * in the order they were added.
 * 
 * @author Philipp Meier
 * @author Benjamin Klatt
 */
public class SequentialJob extends AbstractCompositeJob {

	/** Flag if the jobs should be cleaned up immediately after their execution. */
	private boolean cleanUpImmediately = true;

	/**
	 * Instantiates a new order preserving composite job.
	 */
	public SequentialJob() {
		super();
		setName("Sequential Job");
	}

	/**
	 * Instantiates a new sequential job.
	 * 
	 * @param cleanUpImmediately
	 *            Flag if jobs should be cleaned up immediately or not.
	 */
	public SequentialJob(boolean cleanUpImmediately) {
		this();
		this.cleanUpImmediately = cleanUpImmediately;
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
		if (cleanUpImmediately) {
			executeWithImmediateCleanUp(monitor);
		} else {
			executeWithDelayedCleanUp(monitor);
		}
	}

	/**
	 * Executes all contained jobs, and runs their clean up methods when all
	 * jobs are finished.
	 * 
	 * @param monitor
	 *            the monitor
	 * @throws JobFailedException
	 *             the job failed exception
	 * @throws UserCanceledException
	 *             the user canceled exception
	 */
	private void executeWithDelayedCleanUp(IProgressMonitor monitor)
			throws JobFailedException, UserCanceledException {

		IProgressMonitor subProgressMonitor = new ExecutionTimeLoggingProgressMonitor(
				monitor, 1);
		subProgressMonitor.beginTask(getName() + " Execution", myJobs.size());

		for (IJob job : myJobs) {
			if (monitor.isCanceled()) {
				throw new UserCanceledException();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Palladio Workflow-Engine: Running job "
						+ job.getName());
			}
			subProgressMonitor.subTask(job.getName());
			myExecutedJobs.push(job);
			job.execute(subProgressMonitor);
			subProgressMonitor.worked(1);
		}
		subProgressMonitor.done();
	}

	/**
	 * Specialty: Calls cleanup after the execution of each nested job and
	 * deletes the reference to that nested job. Thus, you need to make sure
	 * that no later jobs depend on these jobs intermediate results that are
	 * deleted during cleanup.
	 * 
	 * @param monitor
	 *            The monitor to report the progress to.
	 * @throws JobFailedException
	 *             identifies a failed job execution.
	 * @throws UserCanceledException
	 *             Identifies a user has canceled the execution of the job.
	 */
	private void executeWithImmediateCleanUp(IProgressMonitor monitor)
			throws JobFailedException, UserCanceledException {

		IProgressMonitor subProgressMonitor = new ExecutionTimeLoggingProgressMonitor(
				monitor, 1);
		subProgressMonitor.beginTask(getName() + " Execution", myJobs.size());

		int totalNumberOfJobs = myJobs.size();
		for (int i = 0; i < totalNumberOfJobs; i++) {
			if (monitor.isCanceled()) {
				throw new UserCanceledException();
			}
			IJob job = myJobs.getFirst();
			if (logger.isDebugEnabled()) {
				logger.debug("Palladio Workflow-Engine: Running job "
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
	 * If the sequential job is configured to not clean up immediately, the
	 * parents behavior is triggered. Otherwise, the clean up has already be
	 * done. {@inheritDoc}
	 */
	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		if (!cleanUpImmediately) {
			super.cleanup(monitor);
		}
	}
}
