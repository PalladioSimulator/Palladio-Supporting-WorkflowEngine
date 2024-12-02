package de.uka.ipd.sdq.workflow.mdsd;

import java.util.List;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.CoreException;

import de.uka.ipd.sdq.workflow.BlackboardBasedWorkflow;
import de.uka.ipd.sdq.workflow.launchconfig.core.AbstractWorkflowBasedLaunchConfigurationDelegate;
import de.uka.ipd.sdq.workflow.launchconfig.core.AbstractWorkflowBasedRunConfiguration;
import de.uka.ipd.sdq.workflow.logging.console.LoggerAppenderStruct;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

public abstract class AbstractWorkflowBasedMDSDBlackboardLaunchConfigurationDelegate<WorkflowConfigurationType extends AbstractWorkflowBasedRunConfiguration>
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
    protected List<LoggerAppenderStruct> setupLogging(final Level logLevel) throws CoreException {
        List<LoggerAppenderStruct> loggerList = super.setupLogging(logLevel);

        // Setup openArchitectureWare Logging
        loggerList.add(this.setupLogger("org.openarchitectureware", logLevel, SHORT_LOG_PATTERN));

        return loggerList;
    }
}
