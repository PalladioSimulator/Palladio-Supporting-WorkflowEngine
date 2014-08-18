package de.uka.ipd.sdq.workflow.mdsd;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;

import de.uka.ipd.sdq.workflow.BlackboardBasedWorkflow;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedLaunchConfigurationDelegate;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration;
import de.uka.ipd.sdq.workflow.logging.console.LoggerAppenderStruct;
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
        extends
        AbstractWorkflowBasedLaunchConfigurationDelegate<WorkflowConfigurationType, BlackboardBasedWorkflow<MDSDBlackboard>> {

    /**
     * Factory method for the blackboard used in the workflow of this launch.
     * 
     * @return The blackboard to be used in the workflow
     */
    protected MDSDBlackboard createBlackboard() {
        return new MDSDBlackboard();
    }

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
        return new UIBasedWorkflow<MDSDBlackboard>(createWorkflowJob(workflowConfiguration, launch), monitor,
                createExcpetionHandler(workflowConfiguration.isInteractive()), createBlackboard());
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedLaunchConfigurationDelegate#
     * setupLogging(org.apache.log4j.Level)
     */
    @Override
    protected ArrayList<LoggerAppenderStruct> setupLogging(Level logLevel) throws CoreException {
        ArrayList<LoggerAppenderStruct> loggerList = super.setupLogging(logLevel);

        // Setup openArchitectureWare Logging
        loggerList.add(setupLogger("org.openarchitectureware", logLevel, SHORT_LOG_PATTERN));

        return loggerList;
    }
}
