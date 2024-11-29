package de.uka.ipd.sdq.workflow.mdsd;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;

import de.uka.ipd.sdq.workflow.WorkflowExceptionHandler;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.launchconfig.core.AbstractWorkflowBasedRunConfiguration;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.ui.UIBasedWorkflow;

/**
 * Base class of workflows running as Eclipse run or debug launches which need a blackboard to load,
 * manipulate, and transform EMF models along their job processing. The blackboard configured here
 * is a {@link MDSDBlackboard} containing partitions where each partition is a
 * {@link ResourceSetPartition}.
 *
 * @param <WorkflowConfigurationType>
 *            The type of the workflow configuration which should be used to configure this workflow
 * @author Steffen Becker
 */
public abstract class AbstractWorkflowBasedMDSDLaunchConfigurationDelegate<WorkflowConfigurationType extends AbstractWorkflowBasedRunConfiguration>
        extends AbstractWorkflowBasedMDSDBlackboardLaunchConfigurationDelegate<WorkflowConfigurationType> {

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedLaunchConfigurationDelegate#
     * createWorkflow(de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration,
     * org.eclipse.core.runtime.IProgressMonitor, org.eclipse.debug.core.ILaunch)
     */
    @Override
    protected UIBasedWorkflow<MDSDBlackboard> createWorkflow(WorkflowConfigurationType workflowConfiguration,
            IProgressMonitor monitor, ILaunch launch) throws CoreException {
        WorkflowExceptionHandler exceptionHandler = createExceptionHandler(workflowConfiguration.isInteractive());
        MDSDBlackboard blackboard = createBlackboard();
        IJob workflowJob = createWorkflowJob(workflowConfiguration, launch);
        return new UIBasedWorkflow<>(workflowJob, monitor, exceptionHandler, blackboard);
    }

}
