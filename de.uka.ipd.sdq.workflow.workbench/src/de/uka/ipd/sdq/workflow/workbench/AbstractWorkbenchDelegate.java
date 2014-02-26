package de.uka.ipd.sdq.workflow.workbench;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;

import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.WorkflowExceptionHandler;
import de.uka.ipd.sdq.workflow.configuration.AbstractJobConfiguration;
import de.uka.ipd.sdq.workflow.configuration.InvalidWorkflowJobConfigurationException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.logging.console.LoggerAppenderStruct;
import de.uka.ipd.sdq.workflow.logging.console.StreamsProxyAppender;
import de.uka.ipd.sdq.workflow.ui.UIBasedWorkflowExceptionHandler;
import de.uka.ipd.sdq.workflow.ui.WorkflowProcess;

/**
 * Abstract base class for Eclipse Workbench Actions which run based on the
 * Palladio workflow engine, i.e., the run has an IJob which gets executed. The
 * class offers features to support logging into the Eclipse console, support
 * for an Eclipse process which can be used to interrupt, terminate or debug the
 * run, integration of the Eclipse progress bar, exception handling, etc. * The
 * class is abstract and defines some methods, which must be implemented by
 * subclasses. See method descriptions for details.
 *
 * This class is based on code provided by Roman Andrej
 *
 * @param <WorkflowConfigurationType>
 *            The type of the configuration object needed by the workflow job to
 *            configure itself. Out of the box support for the run mode (run or
 *            debug), log-level, and unit test runs is provided.
 * @param <WorkflowType>
 *            The type of the workflow to be executed. This can be simple
 *            workflows, workflows using a blackboard, etc.
 *
 * @author Steffen Becker
 */
public abstract class AbstractWorkbenchDelegate<WorkflowConfigurationType extends AbstractJobConfiguration, WorkflowType extends Workflow>
		implements IActionDelegate {

	/** Log Pattern used for run mode. */
	protected static final String SHORT_LOG_PATTERN = "[%-10t] %-5p: %m%n";

	/** Log Pattern used for debug mode. */
	protected static final String DETAILED_LOG_PATTERN = "%-8r [%-10t] %-5p: %m [%c]%n";

	/** Logger of this class. */
	private static Logger logger = Logger
			.getLogger(AbstractWorkbenchDelegate.class);

	/** Name of the entry in the configuration hashmap containing the log level. */
	public static final String VERBOSE_LOGGING = "verboseLogging";

	/** The window. */
	private IWorkbenchWindow window;

	/** The my process. */
	private WorkflowProcess myProcess = null;

	/** A list of listeners to inform about workflow status changes. */
	protected final List<WorkflowStatusListener> workflowListener = new ArrayList<WorkflowStatusListener>();

	/**
	 * The progress monitor to set in the workflow and to report any process
	 * progress to.
	 */
	private IProgressMonitor progressMonitor = new NullProgressMonitor();

	/*
	 * (non-Javadoc)
	 *
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	@Override
	public void run(IAction action) {

		// Setup a new classloader to allow reconfiguration of apache commons
		// logging
		ClassLoader oldClassLoader = configureNewClassloader();
		List<LoggerAppenderStruct> loggerList = setupProcessAndLogger();
		try {
			createAndRunWorkflow();
		} finally {
			// Reset classloader to original value
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
		try {
			tearDownProcessAndLogger(loggerList);
		} catch (DebugException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * Remove the IProcess associated to this launch and deinstall loggers used
	 * in this run.
	 *
	 * @param loggerList
	 *            the logger list
	 * @throws DebugException
	 *             the debug exception
	 */
	private void tearDownProcessAndLogger(List<LoggerAppenderStruct> loggerList)
			throws DebugException {
		for (LoggerAppenderStruct l : loggerList) {
			l.getLogger().removeAppender(l.getAppender());
		}

		// Singnal execution termination to Eclipse to update UI
		if (myProcess != null) {
			myProcess.terminate();
		}
		myProcess = null;
	}

	/**
	 * Setup the Eclipse IProcess used to communicate with the Eclipse UI and
	 * its logging.
	 *
	 * @return the list
	 */
	private List<LoggerAppenderStruct> setupProcessAndLogger() {
		// Reconfigure apache commons logging to use Log4J as backend logger
		// FB: commented out according to bug 730.
		// System.setProperty(LogFactoryImpl.LOG_PROPERTY,
		// "org.apache.commons.logging.impl.Log4JLogger");

		// Add a process to this launch, needed for Eclipse UI updates
		myProcess = getProcess();

		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		MessageConsole myConsole = null;
		if (!useSeparateConsoleForEachJobRun()) {
			// Look if a console already exists
			IConsole[] existing = conMan.getConsoles();
			for (int i = 0; i < existing.length; i++) {
				if (this.getClass().getSimpleName()
						.equals(existing[i].getName())) {
					myConsole = (MessageConsole) existing[i];
				}
			}
		}

		if (myConsole == null) {
			// no existing console found (or existing console is not used), so
			// create a new one
			myConsole = new MessageConsole(this.getClass().getSimpleName(),
					null);
		}
		conMan.addConsoles(new IConsole[] { myConsole });

		IStreamListener streamListener = new MessageConsoleStreamListener(
				myConsole);
		myProcess.getStreamsProxy().getOutputStreamMonitor()
				.addListener(streamListener);
		myProcess.getStreamsProxy().getErrorStreamMonitor()
				.addListener(streamListener);
		final MessageConsole consoleForUpdate = myConsole;
		final String id = IConsoleConstants.ID_CONSOLE_VIEW;
		if (window == null) {
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					window = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();
					try {
						final IWorkbenchPage page = window.getActivePage();
						IConsoleView view = (IConsoleView) page.showView(id);
						view.display(consoleForUpdate);
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}

		// Configure logging output to the Eclipse console
		List<LoggerAppenderStruct> loggerList = setupLogging(getLogLevel());
		for (LoggerAppenderStruct currentLogger : loggerList) {
			myProcess.addAppender(currentLogger.getAppender());
		}

		return loggerList;
	}

	/**
	 * Get the log level based on the extended CommonTab in
	 * DebugEnabledCommonTab.
	 *
	 * <p>
	 * TODO: Anne has set this to protected because the logging has to be
	 * re-enabled during PerOpteryx. Check later whether there is a better
	 * solution.
	 * </p>
	 *
	 * @return The log level selected by the user
	 */
	protected Level getLogLevel() {
		int logLevel = Level.DEBUG.toInt();
		switch (logLevel) {
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
	}

	/**
	 * Setup workflow engine and run workflow.
	 *
	 */
	private void createAndRunWorkflow() {
		if (logger.isEnabledFor(Level.INFO)) {
			logger.info("Create workflow configuration");
		}

		WorkflowConfigurationType workflowConfiguration = getConfiguration();
		if (workflowConfiguration == null) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error(
						"No configuration instance has been created by the workflow ["
								+ this.getClass().toString() + "]",
						new NullPointerException());
			}
			return;
		}

		if (logger.isEnabledFor(Level.INFO)) {
			logger.info("Validating workflow configuration");
		}
		try {
			workflowConfiguration.validateAndFreeze();
			for (WorkflowStatusListener listener : workflowListener) {
				listener.notifyConfigurationValid();
			}
		} catch (InvalidWorkflowJobConfigurationException e) {
			if (logger.isEnabledFor(Level.INFO)) {
				logger.error("Configuration invalid");
				logger.error(e.getMessage());
			}
			return;
		}

		if (logger.isEnabledFor(Level.INFO)) {
			logger.info("Creating workflow engine");
		}
		final Workflow workflow = createWorkflow(workflowConfiguration);
		for (WorkflowStatusListener listener : workflowListener) {
			listener.notifyCreated();
		}

		if (logger.isEnabledFor(Level.INFO)) {
			logger.info("Executing workflow");
		}

		workflow.run();
		for (WorkflowStatusListener listener : workflowListener) {
			listener.notifyFinished();
		}

	}

	/**
	 * Instanciate the workflow exception handler used to handle failures in the
	 * workflow. By default returns an excpetion handler which uses Eclipse
	 * Dialogs to inform the user about the failure.
	 *
	 * @param interactive
	 *            Whether the workflow runs interactive
	 * @return A workflow exception handler
	 */
	protected WorkflowExceptionHandler createExceptionHandler(
			boolean interactive) {
		return new UIBasedWorkflowExceptionHandler(!interactive);
	}

	/**
	 * Instantiate the workflow engine. By default a standard workflow engine is
	 * created.
	 *
	 * @param workflowConfiguration
	 *            Configuration of the workflow job
	 * @return The workflow engine to use for this launch
	 */
	@SuppressWarnings("unchecked")
	protected WorkflowType createWorkflow(
			WorkflowConfigurationType workflowConfiguration) {
		return (WorkflowType) new Workflow(
				createWorkflowJob(workflowConfiguration), progressMonitor,
				// createExceptionHandler(workflowConfiguration.isInteractive()));
				createExceptionHandler(true));
	}

	/**
	 * Create a new classloader to be used by this thread. Return the old
	 * classloader for later resets
	 *
	 * @return Old classloader
	 */
	private ClassLoader configureNewClassloader() {
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		URLClassLoader cl = new URLClassLoader(new URL[] {}, oldClassLoader);
		Thread.currentThread().setContextClassLoader(cl);
		return oldClassLoader;
	}

	/**
	 * Setup logger for the workflow run. May be overridden by clients to
	 * configure further logger for other namespaces than
	 * de.uka.ipd.sdq.workflow. Use protected method setupLogger to configure
	 * additional loggers
	 *
	 * @param logLevel
	 *            The apache log4j log level requested by the user as log level
	 * @return the array list
	 */
	protected ArrayList<LoggerAppenderStruct> setupLogging(Level logLevel) {
		ArrayList<LoggerAppenderStruct> loggerList = new ArrayList<LoggerAppenderStruct>();

		// Setup Palladio workflow engine logging
		loggerList.add(setupLogger("de.uka.ipd.sdq.workflow", logLevel,
				Level.DEBUG == logLevel ? DETAILED_LOG_PATTERN
						: SHORT_LOG_PATTERN));

		return loggerList;
	}

	/**
	 * Configure the named logger to log on the given log level with the given
	 * PatternLayout.
	 *
	 * @param loggerName
	 *            The name of the logger to configure
	 * @param logLevel
	 *            The log level to be used by the logger to configure
	 * @param layout
	 *            The layout for the pattern layout to be used to format log
	 *            messages. The layout may reuse the defined constants in this
	 *            class for short and detailed log outputs
	 * @return the logger appender struct
	 */
	protected LoggerAppenderStruct setupLogger(String loggerName,
			Level logLevel, String layout) {
		Logger loggerInstance = Logger.getLogger(loggerName);
		StreamsProxyAppender appender = new StreamsProxyAppender();

		loggerInstance.setLevel(logLevel);
		appender.setLayout(new PatternLayout(layout));
		loggerInstance.setAdditivity(false);
		loggerInstance.addAppender(appender);

		return new LoggerAppenderStruct(loggerInstance, appender);
	}

	/**
	 * Instantiate the Eclipse process used by the workflow engine. Override
	 * this method to return a different process if you need support for
	 * debugging, etc.
	 *
	 * @return The process used to execute this launch
	 */
	protected WorkflowProcess getProcess() {
		return new WorkflowProcess();
	}

	/**
	 * Access the list of registered workflow status listeners.
	 *
	 * @return The currently registered listeners
	 */
	public List<WorkflowStatusListener> getWorkflowListener() {
		return workflowListener;
	}

	/**
	 * Register a workflow status listener to the delegate.
	 *
	 * @param listner
	 *            The listener to register.
	 */
	public void register(WorkflowStatusListener listner) {
		this.workflowListener.add(listner);
	}

	/**
	 * Set another progress monitor the workflow should report to than the
	 * default null progress one.
	 *
	 * Note: The progress monitor must be set before the workflow is triggered! (run method)
	 *
	 * @param progressMonitor
	 *            The monitor to report to.
	 */
	public void setProgressMonitor(IProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	/**
	 * Instantiate the main job to be executed by the workflow engine. The job
	 * can be a single job or any other job type like composite jobs. The job
	 * will be run by the workflow engine.
	 *
	 * @param config
	 *            The strongly-typed configuration object used to configure the
	 *            main workflow job
	 * @return The main workflow job to be executed by the workflow engine
	 */
	protected abstract IJob createWorkflowJob(WorkflowConfigurationType config);

	/**
	 * Use separate console for each job run.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean useSeparateConsoleForEachJobRun();

	/**
	 * This method is called as template method and has to be overriden by
	 * clients. Its purpose is to return a strongly typed configuration object
	 * needed by this workflow's main workflow job.
	 *
	 * @return The strongly typed configuration object for the main workflow job
	 */
	protected abstract WorkflowConfigurationType getConfiguration();
}
