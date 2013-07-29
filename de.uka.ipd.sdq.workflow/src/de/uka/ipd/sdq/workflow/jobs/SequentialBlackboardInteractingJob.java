package de.uka.ipd.sdq.workflow.jobs;

import org.eclipse.core.runtime.IProgressMonitor;

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

	/**
	 * Instantiates a new order preserving blackboard composite job.
	 */
	public SequentialBlackboardInteractingJob() {
		super();
	}

	/**
	 * Instantiates a new sequential blackboard job 
	 * with a specific name.
	 * @param name The name of the job.
	 */
	public SequentialBlackboardInteractingJob(String name) {
		super(name);
	}

	/**
	 * Instantiates a new order preserving blackboard composite job.
	 * @param cleanUpImmediately Flag if jobs should be cleaned up immediately or not.
	 */
	public SequentialBlackboardInteractingJob(boolean cleanUpImmediately) {
		super(cleanUpImmediately);
	}

	/**
	 * Instantiates a new sequential blackboard job with a specific name and specifying the clean up strategy.
	 * 
	 * @param name The name of the job sequence.
	 * @param cleanUpImmediately
	 *            Flag if jobs should be cleaned up immediately or not.
	 */
	public SequentialBlackboardInteractingJob(String name, boolean cleanUpImmediately) {
		super(name, cleanUpImmediately);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		for (IJob job : this.myJobs) {
			if (job instanceof IBlackboardInteractingJob) {
				((IBlackboardInteractingJob) job)
						.setBlackboard(this.myBlackboard);
			}
		}
		super.execute(monitor);
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
}
