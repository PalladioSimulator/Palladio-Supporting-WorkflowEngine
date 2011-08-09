package de.uka.ipd.sdq.workflow.launchconfig.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowConfigurationBuilder;

public abstract class AbstractExtendableWorkflowConfigurationBuilder extends AbstractWorkflowConfigurationBuilder{

	public AbstractExtendableWorkflowConfigurationBuilder(
			ILaunchConfiguration configuration, String mode) throws CoreException {
		super(configuration, mode);
	}
	
	public abstract void fillConfigurationInternal(AbstractWorkflowBasedRunConfiguration configuration) throws CoreException;
	
	public void fillConfiguration(AbstractWorkflowBasedRunConfiguration configuration) throws CoreException {
		fillConfigurationInternal(configuration);
		for (WorkflowExtension workflowExtension : ExtensionHelper.getWorkflowExtensions(getWorkflowId())) {
			if (workflowExtension.getExtensionConfigurationBuilder() != null) {
				workflowExtension.getExtensionConfigurationBuilder().fillConfiguration(configuration);
			}
		}
	}
	
	public abstract String getWorkflowId();

}
