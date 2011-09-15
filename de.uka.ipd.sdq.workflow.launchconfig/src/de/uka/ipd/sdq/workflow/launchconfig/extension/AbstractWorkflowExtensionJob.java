package de.uka.ipd.sdq.workflow.launchconfig.extension;

import de.uka.ipd.sdq.workflow.Blackboard;
import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;

/***
 * Abstract extension job. This class specifies jobs which can be plugged in into an extendible work flow as provided by
 * the work flow engine.
 * 
 * 
 * @author Benjamin Klatt
 * @author Michael Hauck
 * 
 */
public abstract class AbstractWorkflowExtensionJob<BlackboardType extends Blackboard<?>> implements IBlackboardInteractingJob<BlackboardType> {

    // ////////////////////////////////
    // ATTRIBUTES
    // ////////////////////////////////

    /** The configuration of this job extension. */
    private AbstractExtensionJobConfiguration jobConfiguration = null;

    /** The blackboard accessible for this job. */
    protected BlackboardType blackboard;

    // ////////////////////////////////
    // CONSTRUCTORS
    // ////////////////////////////////

    // ////////////////////////////////
    // BUSINESS METHODS
    // ////////////////////////////////

    // ////////////////////////////////
    // GETTER / SETTER
    // ////////////////////////////////

    /**
     * Get the blackboard to work with.
     * @return the blackboard
     */
    public BlackboardType getMyBlackboard() {
        return blackboard;
    }

    /**
     * Set the blackboard to work with. 
     * 
     * @param blackboard the blackboard to set
     */
    public void setBlackboard(BlackboardType blackboard) {
        this.blackboard = blackboard;
    }

    /**
     * Get the configuration of this job.
     * 
     * @return the jobConfiguration
     */
    public AbstractExtensionJobConfiguration getJobConfiguration() {
        return jobConfiguration;
    }

    /**
     * Set the configuration of this job.
     * 
     * @param jobConfiguration
     *            the jobConfiguration to set
     */
    public void setJobConfiguration(AbstractExtensionJobConfiguration jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
    }

    /**
     * Set the blackboard for the extension job. 
     * A type cast is required by the method due to the fact that
     * the general extension point is not able to determine the 
     * blackboard type only available at runtime.
     * 
     * @param blackboard The blackboard to set.
     */
    @SuppressWarnings("unchecked")
    public void setJobExtensionBlackboard(Blackboard<?> blackboard) throws ClassCastException {
        this.blackboard = (BlackboardType) blackboard;
    }
}
