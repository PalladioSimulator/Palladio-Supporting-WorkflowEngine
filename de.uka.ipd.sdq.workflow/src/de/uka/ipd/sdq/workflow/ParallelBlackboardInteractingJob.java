package de.uka.ipd.sdq.workflow;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;

/**
 * Parallel composite job which is capable to provide child jobs access to a
 * blackboard instance during instantiation
 * 
 */
public class ParallelBlackboardInteractingJob<BlackboardType extends Blackboard<?>> extends
		ParallelJob implements
		IBlackboardInteractingJob<BlackboardType> {

    /** The blackboard. */
    protected BlackboardType myBlackboard;

    /**
     * Set the blackboard reference to all child jobs which are black board interacting
     * and triggers the super class execution which takes care for the parallel execution itself.
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

    /**
     * {@inheritDoc}
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
