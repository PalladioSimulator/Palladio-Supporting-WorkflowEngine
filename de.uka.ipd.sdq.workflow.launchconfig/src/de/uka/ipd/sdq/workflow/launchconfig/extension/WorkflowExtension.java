package de.uka.ipd.sdq.workflow.launchconfig.extension;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;

import de.uka.ipd.sdq.workflow.Blackboard;

/**
 * A data class composing the relevant elements that form an instance of the work flow extension point.
 * 
 * @author Benjamin Klatt
 * @author Michael Hauck
 *
 */
public class WorkflowExtension<BlackboardType extends Blackboard<?>> {
	
	private AbstractLaunchConfigurationTab launchConfigurationTab = null;
	private AbstractWorkflowExtensionJob<BlackboardType> workflowExtensionJob = null;
	private AbstractWorkflowExtensionConfigurationBuilder extensionConfigurationBuilder = null;
	private int priority = 50;
	private String id = null;
	private String workflowId = null;
	
	public WorkflowExtension(String id, String workflowId) {
		this.id = id;
		this.workflowId = workflowId;
	}
	
	public String getId() {
		return id;
	}
	
	public AbstractLaunchConfigurationTab getLaunchConfigurationTab() {
		return launchConfigurationTab;
	}
	public void setLaunchConfigurationTab(
			AbstractLaunchConfigurationTab launchConfigurationTab) {
		this.launchConfigurationTab = launchConfigurationTab;
	}
	public AbstractWorkflowExtensionJob<?> getWorkflowExtensionJob() {
		return workflowExtensionJob;
	}
	public void setWorkflowExtensionJob(
			AbstractWorkflowExtensionJob<BlackboardType> workflowExtensionJob) {
		this.workflowExtensionJob = workflowExtensionJob;
	}
	public AbstractWorkflowExtensionConfigurationBuilder getExtensionConfigurationBuilder() {
		return extensionConfigurationBuilder;
	}
	public void setExtensionConfigurationBuilder(
	        AbstractWorkflowExtensionConfigurationBuilder extensionConfigurationBuilder) {
		this.extensionConfigurationBuilder = extensionConfigurationBuilder;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String getWorkflowId() {
		return workflowId;
	}

}
