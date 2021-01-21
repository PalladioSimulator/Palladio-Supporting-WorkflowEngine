package de.uka.ipd.sdq.workflow.jobs;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * A sequential workflow which may contain jobs which need access to a common blackboard for
 * information exchange.
 *
 * By default, the cleanup method of all jobs is called right after their execution before starting
 * the next job. In addition, the job is removed from the job list to ensure, objects are freed for
 * the garbage collection.
 * 
 * This job implementation allows to extend the job queue dynamically during execution.
 *
 * @param <BlackboardType>
 *            The type of the blackboard needed by all jobs in the sequential workflow
 * @author Sebastian Krach
 */
public class DynamicSequentialBlackboardInteractingJob<BlackboardType extends Blackboard<?>> extends DynamicSequentialJob implements
        IBlackboardInteractingJob<BlackboardType> {

    /** The my blackboard. */
    protected BlackboardType myBlackboard;

    /**
     * Instantiates a new order preserving blackboard composite job.
     */
    public DynamicSequentialBlackboardInteractingJob() {
        super();
    }

    /**
     * Instantiates a new sequential blackboard job with a specific name.
     *
     * @param name
     *            The name of the job.
     */
    public DynamicSequentialBlackboardInteractingJob(String name) {
        super(name);
    }

    /**
     * Instantiates a new order preserving blackboard composite job.
     *
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     */
    public DynamicSequentialBlackboardInteractingJob(boolean cleanUpImmediately) {
        super(cleanUpImmediately);
    }

    /**
     * Instantiates a new sequential blackboard job with a specific name and specifying the clean up
     * strategy.
     *
     * @param name
     *            The name of the job sequence.
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     */
    public DynamicSequentialBlackboardInteractingJob(String name, boolean cleanUpImmediately) {
        super(name, cleanUpImmediately);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void executeJob(IJob job, IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        if (job instanceof IBlackboardInteractingJob) {
            ((IBlackboardInteractingJob) job).setBlackboard(this.myBlackboard);
        }
        super.executeJob(job, monitor);
    }

    /*
     * (non-Javadoc)
     *
     * @see IBlackboardInteractingJob#setBlackbard (de.uka.ipd.sdq.codegen .workflow.Blackboard)
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
