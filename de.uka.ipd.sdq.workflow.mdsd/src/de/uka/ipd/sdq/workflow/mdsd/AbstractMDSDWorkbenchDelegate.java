package de.uka.ipd.sdq.workflow.mdsd;

import org.eclipse.core.runtime.NullProgressMonitor;

import de.uka.ipd.sdq.workflow.BlackboardBasedWorkflow;
import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.configuration.AbstractJobConfiguration;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.workbench.AbstractWorkbenchDelegate;

/**
 * The Class AbstractMDSDWorkbenchDelegate.
 * 
 * @param <WorkflowConfigurationType>
 *            the generic type
 * @param <WorkflowType>
 *            the generic type
 */
public abstract class AbstractMDSDWorkbenchDelegate<WorkflowConfigurationType extends AbstractJobConfiguration, WorkflowType extends Workflow>
        extends AbstractWorkbenchDelegate<WorkflowConfigurationType, WorkflowType> {

    /**
     * Instantiate the workflow engine. By default a standard workflow engine is created.
     * 
     * @param workflowConfiguration
     *            Configuration of the workflow job
     * @return The workflow engine to use for this launch
     */
    @SuppressWarnings("unchecked")
    protected WorkflowType createWorkflow(WorkflowConfigurationType workflowConfiguration) {
        return (WorkflowType) new BlackboardBasedWorkflow<MDSDBlackboard>(createWorkflowJob(workflowConfiguration),
                new NullProgressMonitor(),
                // createExceptionHandler(workflowConfiguration.isInteractive()));
                createExceptionHandler(true), new MDSDBlackboard());
    }

}
