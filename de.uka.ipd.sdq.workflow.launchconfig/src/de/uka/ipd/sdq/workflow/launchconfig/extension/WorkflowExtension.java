package de.uka.ipd.sdq.workflow.launchconfig.extension;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;

public class WorkflowExtension {
	
	private AbstractLaunchConfigurationTab launchConfigurationTab = null;
	private AbstractExtensionJob workflowExtensionJob = null;
	private WorkflowExtensionConfigurationBuilder extensionConfigurationBuilder = null;
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
	public AbstractExtensionJob getWorkflowExtensionJob() {
		return workflowExtensionJob;
	}
	public void setWorkflowExtensionJob(
			AbstractExtensionJob workflowExtensionJob) {
		this.workflowExtensionJob = workflowExtensionJob;
	}
	public WorkflowExtensionConfigurationBuilder getExtensionConfigurationBuilder() {
		return extensionConfigurationBuilder;
	}
	public void setExtensionConfigurationBuilder(
			WorkflowExtensionConfigurationBuilder extensionConfigurationBuilder) {
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
