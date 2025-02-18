package de.uka.ipd.sdq.workflow.mdsd;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.CoreException;

import de.uka.ipd.sdq.workflow.BlackboardBasedWorkflow;
import de.uka.ipd.sdq.workflow.launchconfig.core.AbstractWorkflowBasedLaunchConfigurationDelegate;
import de.uka.ipd.sdq.workflow.launchconfig.core.AbstractWorkflowBasedRunConfiguration;
import de.uka.ipd.sdq.workflow.logging.console.LoggerAppenderStruct;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;

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
     * setupLogging(org.apache.log4j.Level)
     */
    @Override
    protected ArrayList<LoggerAppenderStruct> setupLogging(final Level logLevel) throws CoreException {
        final ArrayList<LoggerAppenderStruct> loggerList = new ArrayList<>(super.setupLogging(logLevel));

        // Setup openArchitectureWare Logging
        loggerList.add(this.setupLogger("org.openarchitectureware", logLevel, SHORT_LOG_PATTERN));

        return loggerList;
    }
}
