package de.uka.ipd.sdq.workflow;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * A workflow implementation which may contain jobs which need to access a blackboard.
 *
 * @param <T>
 *            The type of the blackboard which the jobs in the workflow can access
 * @author Steffen Becker
 */
public class BlackboardBasedWorkflow<T extends Blackboard<?>> extends Workflow {

    /** The my blackboard. */
    private T myBlackboard;

    /**
     * Instantiates a new blackboard based workflow.
     *
     * @param job
     *            the job
     * @param blackboard
     *            the blackboard
     */
    public BlackboardBasedWorkflow(IJob job, T blackboard) {
        this(job, null, new WorkflowExceptionHandler(false), blackboard);
    }

    /**
     * Instantiates a new blackboard based workflow.
     *
     * @param job
     *            the job
     * @param handler
     *            the handler
     * @param blackboard
     *            the blackboard
     */
    public BlackboardBasedWorkflow(IJob job, WorkflowExceptionHandler handler, T blackboard) {
        this(job, null, new WorkflowExceptionHandler(false), blackboard);
    }

    /**
     * Instantiates a new blackboard based workflow.
     *
     * @param job
     *            the job
     * @param monitor
     *            the monitor
     * @param handler
     *            the handler
     * @param blackboard
     *            the blackboard
     */
    public BlackboardBasedWorkflow(IJob job, IProgressMonitor monitor, WorkflowExceptionHandler handler, T blackboard) {
        super(job, monitor, handler);
        this.myBlackboard = blackboard;
    }

    /*
     * (non-Javadoc)
     *
     * @see SequentialJob#execute(org.eclipse.core.runtime .IProgressMonitor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        for (IJob job : this.myJobs) {
            if (job instanceof IBlackboardInteractingJob<?>) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting Blackboard of job " + job.getClass().getCanonicalName());
                }
                ((IBlackboardInteractingJob<T>) job).setBlackboard(this.myBlackboard);
            }
        }
        super.execute(monitor);
    }

}
