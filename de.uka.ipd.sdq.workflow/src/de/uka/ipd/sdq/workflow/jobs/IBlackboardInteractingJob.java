package de.uka.ipd.sdq.workflow.jobs;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * Interface of a job which needs access to a certain type of blackboard to retrieve or store data.
 *
 * @param <BlackboardType> Type of the blackboard which is needed by this job
 * @author Steffen Becker
 */
public interface IBlackboardInteractingJob<BlackboardType extends Blackboard<?>> extends IJob {
	
	/**
	 * Sets the blackboard of this job to the given blackboard.
	 *
	 * @param blackboard The blackboard to be used by this job
	 */
	void setBlackboard(BlackboardType blackboard);
}
