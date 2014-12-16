package de.uka.ipd.sdq.workflow.extension;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;

/**
 * Abstract extension job. This class specifies jobs which can be plugged in into an extendible work
 * flow as provided by the work flow engine.
 * 
 * @param <BlackboardType>
 *            the generic type
 * @author Benjamin Klatt
 * @author Michael Hauck
 * @author Sebastian Lehrig
 */
public abstract class AbstractWorkflowExtensionJob<BlackboardType extends Blackboard<?>> extends
        SequentialBlackboardInteractingJob<BlackboardType> {

    /** The configuration of this job extension. */
    private AbstractExtensionJobConfiguration jobConfiguration = null;

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
}
