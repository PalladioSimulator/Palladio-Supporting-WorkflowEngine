package de.uka.ipd.sdq.workflow.extension;

import java.util.List;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;

/**
 * The super class for an extendible job which is able to look up 
 * and instantiate job extension which have registered for a specific work flow by it's id.
 * 
 * Implementing an extendible job requires to define the jobs 
 * that should be executed. This can be done in the constructor 
 * of your class
 * - implement the execute method
 * - add the required jobs via this.addJob(job);
 * - call  handleJobExtension("myworkflowid") at the point the job extensions should be loaded at
 * 
 * handleJobExtension() can be called multiple times with different or even the same workflow id
 * 
 * @param <BlackboardType>
 *            the generic type
 * @author Michael Hauck
 * @author Benjamin Klatt
 */
public abstract class AbstractExtendableJob<BlackboardType extends Blackboard<?>> extends
        SequentialBlackboardInteractingJob<Blackboard<?>> {

    // ////////////////////////////////
    // ATTRIBUTES
    // ////////////////////////////////

    // ////////////////////////////////
    // CONSTRUCTORS
    // ////////////////////////////////

	/**
	 * Instantiates a new extendible job.
	 */
	public AbstractExtendableJob() {
		super();
	}

	/**
	 * Instantiates a new extendible job composite job and setting the cleanup behavior.
	 * @param cleanUpImmediately Flag if jobs should be cleaned up immediately or not.
	 */
	public AbstractExtendableJob(boolean cleanUpImmediately) {
		super(cleanUpImmediately);
	}

    // ////////////////////////////////
    // BUSINESS METHODS
    // ////////////////////////////////

    /**
     * Check for plug-ins implementing the job extension point and add those jobs to the current
     * workflow.
     * 
     * @param workflowId
     *            The id of the work-flow plug-ins have been registered for to extend.
     * @param configuration
     *            The configuration object to access the overall work flow configuration.
     */
    @SuppressWarnings("rawtypes")
    protected void handleJobExtensions(String workflowId, ExtendableJobConfiguration configuration) {
        List<WorkflowExtension> workflowExtensions = ExtensionHelper.getWorkflowExtensionsSortedByPriority(workflowId);
        for (WorkflowExtension workflowExtension : workflowExtensions) {

            // init the job
            AbstractWorkflowExtensionJob<?> job = workflowExtension.getWorkflowExtensionJob();

            // build and inject the configuration into the job
            AbstractWorkflowExtensionConfigurationBuilder jobConfigurationBuilder = workflowExtension
                    .getExtensionConfigurationBuilder();
            if (jobConfigurationBuilder != null) {
                job.setJobConfiguration(jobConfigurationBuilder.buildConfiguration(configuration.getAttributes()));
            }

            // add the job to the work flow
            this.addJob(job);
        }
    }

    // ////////////////////////////////
    // GETTER / SETTER
    // ////////////////////////////////
}
