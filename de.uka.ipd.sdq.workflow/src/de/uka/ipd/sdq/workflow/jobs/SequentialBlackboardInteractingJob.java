package de.uka.ipd.sdq.workflow.jobs;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.ExecutionTimeLoggingProgressMonitor;
import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * A sequential workflow which may contain jobs which need access to a common
 * blackboard for information exchange.
 * 
 * By default, the cleanup method of all jobs is called right after their
 * execution before starting the next job. In addition, the job is removed from
 * the job list to ensure, objects are freed for the garbage collection.
 * 
 * @param <BlackboardType>
 *            The type of the blackboard needed by all jobs in the sequential
 *            workflow
 * @author Steffen
 * @author Michael Hauck
 * @author Benjamin Klatt
 */
public class SequentialBlackboardInteractingJob<BlackboardType extends Blackboard<?>>
		extends SequentialJob implements
		IBlackboardInteractingJob<BlackboardType> {

	/** The my blackboard. */
	protected BlackboardType myBlackboard;

	/** Flag if the jobs should be cleaned up immediately after their execution. */
	private boolean cleanUpImmediately = true;

	/**
	 * Instantiates a new order preserving blackboard composite job.
	 */
	public SequentialBlackboardInteractingJob() {
		super();
	}

	/**
	 * Instantiates a new order preserving blackboard composite job.
	 * @param cleanUpImmediately Flag if jobs should be cleaned up immediately or not.
	 */
	public SequentialBlackboardInteractingJob(boolean cleanUpImmediately) {
		this();
		this.cleanUpImmediately = cleanUpImmediately;
	}

	/**
	 * Executes all contained jobs. Depending on the jobs configuration, the
	 * execution cleans up immediately or when all jobs are done.
	 * 
	 * @param monitor
	 *            the monitor
	 * @throws JobFailedException
	 *             the job failed exception
	 * @throws UserCanceledException
	 *             the user canceled exception
	 */
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		if (cleanUpImmediately) {
			executeWithImmediateCleanUp(monitor);
		} else {
			executeWithDelayedCleanUp(monitor);
		}
	}

	/**
	 * Executes all contained jobs, i.e. call execute() for them. Contained jobs
	 * can thus re-implement this method with functionality that should be
	 * executed.
	 * 
	 * The job's clean up method is called when all jobs are finished.
	 * 
	 * @param monitor
	 *            the monitor
	 * @throws JobFailedException
	 *             the job failed exception
	 * @throws UserCanceledException
	 *             the user canceled exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void executeWithDelayedCleanUp(IProgressMonitor monitor)
			throws JobFailedException, UserCanceledException {
		for (IJob job : this.myJobs) {
			if (job instanceof IBlackboardInteractingJob) {
				((IBlackboardInteractingJob) job)
						.setBlackboard(this.myBlackboard);
			}
		}
		super.execute(monitor);
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
	public void executeWithImmediateCleanUp(IProgressMonitor monitor)
			throws JobFailedException, UserCanceledException {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ipd.sdq.codegen.workflow.IBlackboardInteractingJob#setBlackbard
	 * (de.uka.ipd.sdq.codegen .workflow.Blackboard)
	 */
	@Override
	public void setBlackboard(BlackboardType blackboard) {
		this.myBlackboard = blackboard;
	}

	/**
	 * Gets the blackboard.
	 * 
	 * @return Returns the used blackboard.
	 */
	public BlackboardType getBlackboard() {
		return myBlackboard;
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
