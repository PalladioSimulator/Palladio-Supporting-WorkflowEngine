package de.uka.ipd.sdq.workflow;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;

/**
 * A sequential workflow which may contain jobs which need access to a common blackboard for
 * information exchange.
 * 
 * @param <BlackboardType>
 *            The type of the blackboard needed by all jobs in the sequential workflow
 * @author Steffen
 */
public class SequentialBlackboardInteractingJob<BlackboardType extends Blackboard<?>> extends
        SequentialJob implements ICompositeJob, IBlackboardInteractingJob<BlackboardType> {

    /** The my blackboard. */
    protected BlackboardType myBlackboard;

    /**
     * Instantiates a new order preserving blackboard composite job.
     */
    public SequentialBlackboardInteractingJob() {
        super();
    }

    /**
     * Executes all contained jobs, i.e. call execute() for them. Contained jobs can thus
     * re-implement this method with functionality that should be executed.
     * 
     * @param monitor
     *            the monitor
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        for (IJob job : this.myJobs) {
            if (job instanceof IBlackboardInteractingJob) {
                ((IBlackboardInteractingJob) job).setBlackboard(this.myBlackboard);
            }
        }
        super.execute(monitor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.codegen.workflow.IBlackboardInteractingJob#setBlackbard(de.uka.ipd.sdq.codegen
     * .workflow.Blackboard)
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
