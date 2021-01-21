package de.uka.ipd.sdq.workflow.jobs;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.ExecutionTimeLoggingProgressMonitor;
import de.uka.ipd.sdq.workflow.WorkflowFailedException;

/**
 * Implementation of a composite job that guarantees the execution of it's jobs in the order they
 * were added, similar to {@link SequentialJob}. In contrast, this class allows for dynamically
 * extending the job queue during job execution. As a drawback the progress reporting is not as
 * accurate anymore.
 * 
 * This should particularly allow for repeating the same Job multiple times until a certain
 * condition holds.
 * 
 * @author Sebastian Krach
 */
public class DynamicSequentialJob extends SequentialJob {

    /**
     * Instantiates a new order preserving composite job.
     */
    public DynamicSequentialJob() {
        this("Dynamic Sequential Job");
    }

    /**
     * Instantiates a new order preserving composite job with a specific name.
     * 
     * @param name
     *            The name of the job sequence.
     */
    public DynamicSequentialJob(final String name) {
        super(name);
    }

    /**
     * Instantiates a new sequential job.
     * 
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     */
    public DynamicSequentialJob(final boolean cleanUpImmediately) {
        super(cleanUpImmediately);
    }

    /**
     * Instantiates a new model workflow job with a specific name and specifying the clean up
     * strategy.
     * 
     * @param name
     *            The name of the job sequence.
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     */
    public DynamicSequentialJob(final String name, final boolean cleanUpImmediately) {
        super(name, cleanUpImmediately);
        setName(name);
    }

    /**
     * Executes all contained jobs, and runs their clean up methods when all jobs are finished.
     * 
     * @param monitor
     *            the monitor
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     */
    @Override
    protected void executeWithDelayedCleanUp(final IProgressMonitor monitor)
            throws JobFailedException, UserCanceledException {

        final IProgressMonitor subProgressMonitor = new ExecutionTimeLoggingProgressMonitor(monitor, 1);
        subProgressMonitor.beginTask(getName() + " Execution", IProgressMonitor.UNKNOWN);

        while (!this.isEmpty()) {
            if (monitor.isCanceled()) {
                throw new UserCanceledException();
            }
            var job = this.removeFirst();
            if (logger.isDebugEnabled()) {
                logger.debug("Palladio Workflow-Engine: Running job " + job.getName());
            }
            subProgressMonitor.subTask(job.getName());
            myExecutedJobs.push(job);
            executeJob(job, subProgressMonitor);
            subProgressMonitor.worked(1);
        }
        subProgressMonitor.done();
    }


    /**
     * Specialty: Calls cleanup after the execution of each nested job and deletes the reference to
     * that nested job. Thus, you need to make sure that no later jobs depend on these jobs
     * intermediate results that are deleted during cleanup.
     * 
     * @param monitor
     *            The monitor to report the progress to.
     * @throws JobFailedException
     *             identifies a failed job execution.
     * @throws UserCanceledException
     *             Identifies a user has canceled the execution of the job.
     */
    @Override
    protected void executeWithImmediateCleanUp(final IProgressMonitor monitor)
            throws JobFailedException, UserCanceledException {

        final IProgressMonitor subProgressMonitor = new ExecutionTimeLoggingProgressMonitor(monitor, 1);
        subProgressMonitor.beginTask(getName() + " Execution", IProgressMonitor.UNKNOWN);
        subProgressMonitor.worked(0);

        while (!this.isEmpty()) {
            if (monitor.isCanceled()) {
                throw new UserCanceledException();
            }
            var job = this.removeFirst();

            if (logger.isDebugEnabled()) {
                logger.debug("Palladio Workflow-Engine: Running job " + job.getName());
            }

            subProgressMonitor.subTask(job.getName());
            Exception workflowException = null;
            try {
                executeJob(job, subProgressMonitor);
            } catch (final Exception e) {
                workflowException = e;
            }
            subProgressMonitor.worked(1);
            subProgressMonitor.subTask("Cleaning up job " + job.getName());
            try {
                job.cleanup(subProgressMonitor);
            } catch (final CleanupFailedException e) {
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn("Failed to cleanup job " + job.getName());
                }
            }
            subProgressMonitor.worked(1);
            if (workflowException != null) {
                throw new WorkflowFailedException("A workflow job failed", workflowException);
            }
        }

        subProgressMonitor.done();
    }
    
    
    protected void executeJob(IJob job, IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        job.execute(monitor);
    }

}
