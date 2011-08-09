package de.uka.ipd.sdq.workflow.launchconfig.extension;

import org.eclipse.core.runtime.CoreException;

import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration;


/**
 * Provides an abstract class for which an implementation has to be provided
 * by a workflow extension job.
 * The class has to provide the logic to derive job configuration values based
 * on the given Eclipse AbstractWorkflowBasedRunConfiguration.
 * @author hauck
 *
 */
public abstract class WorkflowExtensionConfigurationBuilder {
	
	public abstract void fillConfiguration(AbstractWorkflowBasedRunConfiguration configuration) throws CoreException;

}
