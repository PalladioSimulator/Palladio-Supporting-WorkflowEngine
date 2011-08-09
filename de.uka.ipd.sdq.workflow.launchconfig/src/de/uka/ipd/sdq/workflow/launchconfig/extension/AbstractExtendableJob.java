package de.uka.ipd.sdq.workflow.launchconfig.extension;

import java.util.List;

import de.uka.ipd.sdq.workflow.Blackboard;
import de.uka.ipd.sdq.workflow.OrderPreservingBlackboardCompositeJob;

public abstract class AbstractExtendableJob<BlackboardType extends Blackboard<?>> extends OrderPreservingBlackboardCompositeJob<Blackboard<?>>{
	
	/**
	 * 
	 * @param fromPriority do not consider jobs with a priority lower than
	 * or equal to the specified fromPriority.
	 * @param toPriority do not consider jobs with a priority higher than
	 * the specified toPriority.
	 */
	protected void handleJobExtensions(int fromPriority, int toPriority) {
		List<WorkflowExtension> workflowExtensions = ExtensionHelper.getWorkflowExtensionsSortedByPriority(getWorkflowId());
		for (WorkflowExtension workflowExtension : workflowExtensions) {
			if ((workflowExtension.getPriority()>fromPriority) && (workflowExtension.getPriority()<=toPriority)) {
				this.addJob(workflowExtension.getWorkflowExtensionJob());				
			}
		}
	}

	public abstract String getWorkflowId();
	
}
