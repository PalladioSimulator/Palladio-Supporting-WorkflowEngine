package de.uka.ipd.sdq.workflow.launchconfig.extension;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;

import de.uka.ipd.sdq.workflow.Blackboard;
import de.uka.ipd.sdq.workflow.OrderPreservingBlackboardCompositeJob;

/**
 * An extendible job which is able to look up and instantiate job extension which have registered for a specific work
 * flow.
 * 
 * The job itself can call the handleJobExtension for a specific work flow id to load jobs registered for the id of this
 * work flow.
 * 
 * @author Michael Hauck
 * @author Benjamin Klatt
 * 
 * @param <BlackboardType>
 */
public abstract class AbstractExtendableJob<BlackboardType extends Blackboard<?>> extends
        OrderPreservingBlackboardCompositeJob<Blackboard<?>> {

    // ////////////////////////////////
    // ATTRIBUTES
    // ////////////////////////////////

    /** The logger to work with in this class. */
    private static final Logger LOGGER = Logger.getLogger(AbstractExtendableJob.class);

    // ////////////////////////////////
    // CONSTRUCTORS
    // ////////////////////////////////

    // ////////////////////////////////
    // BUSINESS METHODS
    // ////////////////////////////////

    /**
     * Check for plug-ins implementing the job extension point and add those jobs to the current workflow.
     * 
     * @param workflowId
     *            The id of the work-flow plug-ins have been registered for to extend.
     * @param fromPriority
     *            do not consider jobs with a priority lower than or equal to the specified fromPriority.
     * @param toPriority
     *            do not consider jobs with a priority higher than the specified toPriority.
     * @param configuration
     *            The configuration object to access the overall work flow configuration.
     * @throws CoreException
     *             Identifying that a job could not be instantiated correctly.
     */
    @SuppressWarnings("rawtypes")
    protected void handleJobExtensions(String workflowId, ExtendibleJobConfiguration configuration) {
        List<WorkflowExtension> workflowExtensions =
                ExtensionHelper.getWorkflowExtensionsSortedByPriority(workflowId);
        for (WorkflowExtension workflowExtension : workflowExtensions) {

            // init the job
            AbstractWorkflowExtensionJob<?> job = workflowExtension.getWorkflowExtensionJob();

            // build and inject the configuration into the job
            AbstractWorkflowExtensionConfigurationBuilder jobConfigurationBuilder =
                    workflowExtension.getExtensionConfigurationBuilder();
            if (jobConfigurationBuilder != null) {
                try {
                    job.setJobConfiguration(jobConfigurationBuilder.buildConfiguration(configuration.getLaunchConfiguration(), configuration.getMode()));
                } catch (CoreException coreException) {
                    LOGGER.error("Failed to build job configuration", coreException);
                }
            }
            
            // add the job to the work flow
            this.addJob(job);
        }
    }

    // ////////////////////////////////
    // GETTER / SETTER
    // ////////////////////////////////
}
