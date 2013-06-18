package de.uka.ipd.sdq.workflow.launchconfig.extension;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * A data class composing the relevant elements that form an 
 * instance of the work flow configuration tab extension
 * point.
 * 
 * @param <BlackboardType>
 *            the generic type
 * @author Benjamin Klatt
 * @author Michael Hauck
 */
public class WorkflowConfigurationTabExtension<BlackboardType extends Blackboard<?>> {

    /** The launch configuration tab. */
    private AbstractLaunchConfigurationTab launchConfigurationTab = null;

    /** The priority of the tab. Used to order the tabs in the UI. */
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
    public WorkflowConfigurationTabExtension(String id, String workflowId) {
        this.id = id;
        this.workflowId = workflowId;
    }

    /**
     * Gets the launch configuration tab.
     * 
     * @return the launch configuration tab
     */
    public AbstractLaunchConfigurationTab getLaunchConfigurationTab() {
        return launchConfigurationTab;
    }

    /**
     * Sets the launch configuration tab.
     * 
     * @param launchConfigurationTab
     *            the new launch configuration tab
     */
    public void setLaunchConfigurationTab(AbstractLaunchConfigurationTab launchConfigurationTab) {
        this.launchConfigurationTab = launchConfigurationTab;
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

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

}
