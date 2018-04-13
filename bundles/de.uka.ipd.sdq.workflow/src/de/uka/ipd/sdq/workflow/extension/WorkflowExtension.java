package de.uka.ipd.sdq.workflow.extension;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * A data class composing the relevant elements that form an instance of the work flow extension
 * point.
 * 
 * @param <BlackboardType>
 *            the generic type
 * @author Benjamin Klatt
 * @author Michael Hauck
 */
public class WorkflowExtension<BlackboardType extends Blackboard<?>> {

    /** The workflow extension job. */
    private AbstractWorkflowExtensionJob<BlackboardType> workflowExtensionJob = null;

    /** The extension configuration builder. */
    private AbstractWorkflowExtensionConfigurationBuilder extensionConfigurationBuilder = null;

    /** The priority. */
    private int priority = 50;

    /** The id. */
    private String id = null;

    /** The workflow id. */
    private String workflowId = null;

    /**
     * Instantiates a new workflow extension.
     * 
     * @param id
     *            the id
     * @param workflowId
     *            the workflow id
     */
    public WorkflowExtension(String id, String workflowId) {
        this.id = id;
        this.workflowId = workflowId;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the workflow extension job.
     * 
     * @return the workflow extension job
     */
    public AbstractWorkflowExtensionJob<?> getWorkflowExtensionJob() {
        return workflowExtensionJob;
    }

    /**
     * Sets the workflow extension job.
     * 
     * @param workflowExtensionJob
     *            the new workflow extension job
     */
    public void setWorkflowExtensionJob(AbstractWorkflowExtensionJob<BlackboardType> workflowExtensionJob) {
        this.workflowExtensionJob = workflowExtensionJob;
    }

    /**
     * Gets the extension configuration builder.
     * 
     * @return the extension configuration builder
     */
    public AbstractWorkflowExtensionConfigurationBuilder getExtensionConfigurationBuilder() {
        return extensionConfigurationBuilder;
    }

    /**
     * Sets the extension configuration builder.
     * 
     * @param extensionConfigurationBuilder
     *            the new extension configuration builder
     */
    public void setExtensionConfigurationBuilder(
            AbstractWorkflowExtensionConfigurationBuilder extensionConfigurationBuilder) {
        this.extensionConfigurationBuilder = extensionConfigurationBuilder;
    }

    /**
     * Gets the priority.
     * 
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority.
     * 
     * @param priority
     *            the new priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the workflow id.
     * 
     * @return the workflow id
     */
    public String getWorkflowId() {
        return workflowId;
    }

}
