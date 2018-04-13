package de.uka.ipd.sdq.workflow.jobs;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * Abstract job providing access to the blackboard of the generic type.
 *
 * @param <BlackboardType>
 *            The type of blackboard supported.
 */
public abstract class AbstractBlackboardInteractingJob<BlackboardType extends Blackboard<?>> extends AbstractJob
        implements IBlackboardInteractingJob<BlackboardType> {

    /** The my blackboard. */
    protected BlackboardType myBlackboard;

    /**
     * {@inheritDoc}
     *
     * @see IBlackboardInteractingJob#setBlackbard(de.uka.ipd.sdq.codegen .workflow.Blackboard)
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
