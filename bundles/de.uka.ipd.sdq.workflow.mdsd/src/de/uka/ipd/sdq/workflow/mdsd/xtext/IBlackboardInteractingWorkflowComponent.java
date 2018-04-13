package de.uka.ipd.sdq.workflow.mdsd.xtext;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * The Interface IBlackboardInteractingWorkflowComponent.
 *
 * @param <BlackboardType>
 *            the generic type
 */
public interface IBlackboardInteractingWorkflowComponent<BlackboardType extends Blackboard<?>> {

    /**
     * Sets the blackboard of this job to the given blackboard.
     *
     * @param blackboard
     *            The blackboard to be used by this job
     */
    public void setBlackboard(BlackboardType blackboard);
}
