package de.uka.ipd.sdq.workflow.launchconfig.extension;

import java.util.List;

import de.uka.ipd.sdq.workflow.Blackboard;
import de.uka.ipd.sdq.workflow.OrderPreservingBlackboardCompositeJob;

/**
 * An extendible job which is able to look up and instantiate job extension which have registered
 * for a specific work flow.
 * 
 * The job itself can call the handleJobExtension for a specific work flow id to load jobs
 * registered for the id of this work flow.
 * 
 * @param <BlackboardType>
 *            the generic type
 * @author Michael Hauck
 * @author Benjamin Klatt
 */
public abstract class AbstractExtendableJob<BlackboardType extends Blackboard<?>> extends
        OrderPreservingBlackboardCompositeJob<Blackboard<?>> {

    // ////////////////////////////////
    // ATTRIBUTES
    // ////////////////////////////////

    // ////////////////////////////////
    // CONSTRUCTORS
    // ////////////////////////////////

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
