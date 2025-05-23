package de.uka.ipd.sdq.workflow.launchconfig.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.WorkflowExceptionHandler;
import de.uka.ipd.sdq.workflow.WorkflowProcess;
import de.uka.ipd.sdq.workflow.configuration.InvalidWorkflowJobConfigurationException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.logging.console.LoggerAppenderStruct;
import de.uka.ipd.sdq.workflow.logging.console.StreamsProxyAppender;

/**
 * Abstract base class for Eclipse Launches (both Run and Debug mode are supported) which run based
 * on the Palladio workflow engine, i.e., the run has an IJob which gets executed. The class offers
 * features to convert the information contained in ILaunchConfiguration into a strongly typed
 * configuration object for the workflow job, features to suport logging into the Eclipse console,
 * support for an Eclipse process which can be used to interrupt, terminate or debug the run,
 * integration of the Eclipse progress bar, execption handling, etc.
 * 
 * The class is supposed to be used to implement Eclipse run configurations, like SimuCom, ProtoCom,
 * PCM2Java, PCM2LQN, SoMoX, Java2PCM, etc.
 * 
 * The class is abstract and defines some methods, which must be implemented by subclasses. See
 * method descriptions for details.
 * 
 * This class is based on code provided by Roman Andrej
 * 
 * @param <WorkflowConfigurationType>
 *            The type of the configuration object needed by the workflow job to configure itself.
 *            Out of the box support for the run mode (run or debug), log-level, and unit test runs
 *            is provided.
 * @param <WorkflowType>
 *            The type of the workflow to be executed. This can be simple workflows, workflows using
 *            a blackboard, etc.
 * 
 * @author Steffen Becker
 */
public abstract class AbstractWorkflowBasedLaunchConfigurationDelegate<WorkflowConfigurationType extends AbstractWorkflowBasedRunConfiguration, WorkflowType extends Workflow>
        implements ILaunchConfigurationDelegate {

    /** Log Pattern used for run mode. */
    protected static final String SHORT_LOG_PATTERN = "[%-10t] %-5p: %m%n";

    /** Log Pattern used for debug mode. */
    protected static final String DETAILED_LOG_PATTERN = "%-8r [%-10t] %-5p: %m [%c]%n";

    /** Logger of this class. */
    private static final Logger LOGGER = Logger.getLogger(AbstractWorkflowBasedLaunchConfigurationDelegate.class);

    /** Name of the entry in the configuration hashmap containing the log level. */
    public static final String VERBOSE_LOGGING = "verboseLogging";

    /** The Constant WORKFLOW_ENGINE_DEBUG_LEVEL. */
    public static final String WORKFLOW_ENGINE_DEBUG_LEVEL = "de.uka.ipd.sdq.workflowengine.debuglevel";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.
     * eclipse.debug.core. ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch,
     * org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
            throws CoreException {

        // Setup a new classloader to allow reconfiguration of apache commons
        // logging
        ClassLoader oldClassLoader = configureNewClassloader();
        List<LoggerAppenderStruct> loggerList = setupProcessAndLogger(configuration, launch);
        try {
            createAndRunWorkflow(configuration, mode, launch, monitor);
        } finally {
            // Reset classloader to original value
            Thread.currentThread()
                .setContextClassLoader(oldClassLoader);
        }
        tearDownProcessAndLogger(launch, loggerList);
    }

    /**
     * Remove the IProcess associated to this launch and deinstall loggers used in this run.
     * 
     * @param launch
     *            the launch
     * @param loggerList
     *            the LOGGER list
     * @throws DebugException
     *             the debug exception
     */
    private void tearDownProcessAndLogger(ILaunch launch, List<LoggerAppenderStruct> loggerList) throws DebugException {
        for (LoggerAppenderStruct l : loggerList) {
            l.getLogger()
                .removeAppender(l.getAppender());
        }

        // Singnal execution terminatation to Eclipse to update UI
        launch.getProcesses()[0].terminate();
    }

    /**
     * Setup the Eclipse IProcess used to communicate with the Eclipse UI and its logging.
     * 
     * @param configuration
     *            the configuration
     * @param launch
     *            the launch
     * @return the list
     * @throws CoreException
     *             the core exception
     */
    private List<LoggerAppenderStruct> setupProcessAndLogger(ILaunchConfiguration configuration, ILaunch launch)
            throws CoreException {
        // Reconfigure apache commons logging to use Log4J as backend LOGGER
        // FB: commented out according to bug 730.
        // System.setProperty(LogFactoryImpl.LOG_PROPERTY,
        // "org.apache.commons.logging.impl.Log4JLogger");

        // Add a process to this launch, needed for Eclipse UI updates
        WorkflowProcess workflowProcess = getProcess(launch);

        // Configure logging output to the Eclipse console
        List<LoggerAppenderStruct> loggerList = configureLogging(configuration);
        for (LoggerAppenderStruct currentLogger : loggerList) {
            workflowProcess.addAppender(currentLogger.getAppender());
        }

        launch.addProcess(workflowProcess);
        return loggerList;
    }

    protected List<LoggerAppenderStruct> configureLogging(ILaunchConfiguration configuration) throws CoreException {
        Level logLevel = getLogLevel(configuration);
        return setupLogging(logLevel);
    }

    /**
     * Get the log level based on the extended CommonTab in DebugEnabledCommonTab
     * 
     * TODO: Anne has set this to protected because the logging has to be re-enabled during
     * PerOpteryx. Check later whether there is a better solution.
     * 
     * @param configuration
     *            The configuration passed from the eclipse run dialog
     * @return The log level selected by the user
     */
    protected Level getLogLevel(ILaunchConfiguration configuration) {
        try {
            switch (configuration.getAttribute(WORKFLOW_ENGINE_DEBUG_LEVEL, 0)) {
            case 0:
                return Level.TRACE;
            case 1:
                return Level.DEBUG;
            case 2:
                return Level.INFO;
            case 3:
                return Level.WARN;
            case 4:
                return Level.ERROR;
            case 5:
                return Level.ALL;
            default:
                return Level.INFO;
            }
        } catch (CoreException e) {
            return Level.INFO;
        }
    }

    /**
     * Setup workflow engine and run workflow.
     * 
     * @param configuration
     *            the configuration
     * @param mode
     *            the mode
     * @param launch
     *            the launch
     * @param monitor
     *            the monitor
     * @throws CoreException
     *             the core exception
     */
    private void createAndRunWorkflow(ILaunchConfiguration configuration, String mode, ILaunch launch,
            IProgressMonitor monitor) throws CoreException {
        if (LOGGER.isEnabledFor(Level.INFO)) {
            LOGGER.info("Create workflow configuration");
        }
        WorkflowConfigurationType workflowConfiguration = deriveConfiguration(configuration, mode);
        if (workflowConfiguration == null) {
            if (LOGGER.isEnabledFor(Level.ERROR)) {
                LOGGER.error("No configuration instance has been created by the workflow [" + this.getClass()
                    .toString() + "]", new NullPointerException());
            }
            return;
        }

        if (LOGGER.isEnabledFor(Level.INFO)) {
            LOGGER.info("Validating workflow configuration");
        }
        try {
            workflowConfiguration.validateAndFreeze();
        } catch (InvalidWorkflowJobConfigurationException e) {
            if (LOGGER.isEnabledFor(Level.ERROR)) {
                LOGGER.error("Configuration invalid");
                LOGGER.error(e.getMessage());
            }
            return;
        }

        if (LOGGER.isEnabledFor(Level.INFO)) {
            LOGGER.info("Creating workflow engine");
        }
        Workflow workflow = createWorkflow(workflowConfiguration, monitor, launch);

        if (LOGGER.isEnabledFor(Level.INFO)) {
            LOGGER.info("Executing workflow");
        }
        workflow.run();
    }

    /**
     * Instanciate the workflow exception handler used to handle failures in the workflow. By
     * default returns an excpetion handler which uses Eclipse Dialogs to inform the user about the
     * failure.
     * 
     * @param interactive
     *            Whether the workflow runs interactive
     * @return A workflow exception handler
     */
    protected abstract WorkflowExceptionHandler createExceptionHandler(boolean interactive);

    /**
     * Instantiate the workflow engine. By default a standard workflow engine is created.
     * 
     * @param workflowConfiguration
     *            Configuration of the workflow job
     * @param monitor
     *            A progress monitor
     * @param launch
     *            The associated Eclipse launch
     * @return The workflow engine to use for this launch
     * @throws CoreException
     *             the core exception
     */
    @SuppressWarnings("unchecked")
    protected WorkflowType createWorkflow(WorkflowConfigurationType workflowConfiguration, IProgressMonitor monitor,
            ILaunch launch) throws CoreException {
        return (WorkflowType) new Workflow(createWorkflowJob(workflowConfiguration, launch), monitor,
                createExceptionHandler(workflowConfiguration.isInteractive()));
    }

    /**
     * Create a new classloader to be used by this thread. Return the old classloader for later
     * resets
     * 
     * @return Old classloader
     */
    private ClassLoader configureNewClassloader() {
        ClassLoader oldClassLoader = Thread.currentThread()
            .getContextClassLoader();
        URLClassLoader cl = new URLClassLoader(new URL[] {}, oldClassLoader);
        Thread.currentThread()
            .setContextClassLoader(cl);
        return oldClassLoader;
    }

    /**
     * Setup LOGGER for the workflow run. May be overridden by clients to configure further LOGGER
     * for other namespaces than de.uka.ipd.sdq.workflow. Use protected method setupLogger to
     * configure additional loggers
     * 
     * @param logLevel
     *            The apache log4j log level requested by the user as log level
     * @return the array list
     * @throws CoreException
     *             the core exception
     */
    protected List<LoggerAppenderStruct> setupLogging(Level logLevel) throws CoreException {
        ArrayList<LoggerAppenderStruct> loggerList = new ArrayList<>();

        // Setup Palladio workflow engine logging
        loggerList.add(setupLogger("de.uka.ipd.sdq.workflow", logLevel,
                Level.DEBUG == logLevel ? DETAILED_LOG_PATTERN : SHORT_LOG_PATTERN));

        return loggerList;
    }

    /**
     * Configure the named LOGGER to log on the given log level with the given PatternLayout.
     * 
     * @param loggerName
     *            The name of the LOGGER to configure
     * @param logLevel
     *            The log level to be used by the LOGGER to configure
     * @param layout
     *            The layout for the pattern layout to be used to format log messages. The layout
     *            may reuse the defined constants in this class for short and detailed log outputs
     * @return the LOGGER appender struct
     */
    protected LoggerAppenderStruct setupLogger(String loggerName, Level logLevel, String layout) {
        Logger localLogger = Logger.getLogger(loggerName);
        StreamsProxyAppender appender = new StreamsProxyAppender();

        localLogger.setLevel(logLevel);
        appender.setLayout(new PatternLayout(layout));
        localLogger.setAdditivity(false);
        localLogger.addAppender(appender);

        return new LoggerAppenderStruct(localLogger, logLevel, appender);
    }

    /**
     * Instantiate the Eclipse process used by the workflow engine. Override this method to return a
     * different process if you need support for debugging, etc.
     * 
     * @param launch
     *            The ILaunch passed to this launch by Eclipse
     * @return The process used to execute this launch
     */
    protected WorkflowProcess getProcess(ILaunch launch) {
        return new WorkflowProcess(launch);
    }

    /**
     * Instantiate the main job to be executed by the workflow engine. The job can be a single job
     * or any other job type like composite jobs. The job will be run by the workflow engine.
     * 
     * @param config
     *            The strongly-typed configuration object used to configure the main workflow job
     * @param launch
     *            The eclipse ILaunch associated to the workflow, needed to setup debugging if the
     *            workflow supports it
     * @return The main workflow job to be executed by the workflow engine
     * @throws CoreException
     *             the core exception
     */
    protected abstract IJob createWorkflowJob(WorkflowConfigurationType config, ILaunch launch) throws CoreException;

    /**
     * This method is called as template method and has to be overriden by clients. Its purpose is
     * to convert the Eclipse ILaunchConfiguration (which is basically an untyped hashmap) into a
     * strongly typed configuration object needed by this workflow's main workflow job.
     * 
     * @param configuration
     *            The ILaunchConfiguration to be converted into a strongly typed configuration
     *            object for the main workflow job
     * @param mode
     *            The mode of execution, can be either debug or run
     * @return The strongly typed configuration object for the main workflow job
     * @throws CoreException
     *             the core exception
     */
    protected abstract WorkflowConfigurationType deriveConfiguration(ILaunchConfiguration configuration, String mode)
            throws CoreException;
}
