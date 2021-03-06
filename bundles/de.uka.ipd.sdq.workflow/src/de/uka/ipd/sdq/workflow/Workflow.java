package de.uka.ipd.sdq.workflow;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.ICompositeJob;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * Implementation of a workflow. A workflow is a special composite job which
 * executes all other jobs while displaying the progress in a progress bar to
 * the user. Additionally, the Workflow has an internal WorkflowExceptionHandler
 * that handles exception based on the environment (UI available or not). The
 * given Progress Monitor will receive updates before and after a job is
 * executed.
 * 
 * @author Philipp Meier Steffen Becker
 */
public class Workflow extends SequentialJob implements ICompositeJob {

	/** The my monitor. */
	private IProgressMonitor myMonitor;

	/** The exception handler. */
	protected WorkflowExceptionHandler exceptionHandler = null;

	/**
	 * Instantiates a new workflow.
	 * 
	 * @param job
	 *            the job
	 */
	public Workflow(IJob job) {
		this(job, null, new WorkflowExceptionHandler(false));
	}

	/**
	 * Instantiates a new workflow.
	 * 
	 * @param job
	 *            the job
	 * @param exceptionHandler
	 *            the exception handler
	 */
	public Workflow(IJob job, WorkflowExceptionHandler exceptionHandler) {
		this(job, null, exceptionHandler);
	}

	/**
	 * Instantiates a new workflow.
	 * 
	 * @param job
	 *            the job
	 * @param monitor
	 *            the progress monitor to use
	 * @param workflowExceptionHandler
	 *            the workflow exception handler
	 */
	public Workflow(IJob job, IProgressMonitor monitor,
			WorkflowExceptionHandler workflowExceptionHandler) {
		this.addJob(job);
		this.exceptionHandler = workflowExceptionHandler;
		if (monitor != null) {
			myMonitor = monitor;
		} else {
			myMonitor = new NullProgressMonitor();
		}
		logger = Logger.getLogger(Workflow.class);
		setName("Workflow");
	}

	/**
	 * Run.
	 */
	public void run() {
		if (logger.isEnabledFor(Level.INFO)) {
			logger.info("Creating workflow engine and starting workflow");
		}

		myMonitor.beginTask("Workflow", 1);
		myMonitor.subTask(this.getName());

		try {
			this.execute(myMonitor);
		} catch (JobFailedException e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Workflow job failed, handling failure...");
				logger.error("Failure reason was: ", e);
			}
			this.exceptionHandler.handleJobFailed(e);
		} catch (UserCanceledException e) {
			if (logger.isEnabledFor(Level.INFO)) {
				logger.info("User canceled workflow");
			}
			this.exceptionHandler.handleUserCanceled(e);
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.FATAL)) {
				logger.fatal("Workflow terminated unexpectedly", e);
			}
			this.exceptionHandler.handleFatalFailure(e);
		} finally {
			if (logger.isEnabledFor(Level.INFO)) {
				logger.info("Cleaning up...");
			}
			try {
				this.cleanup(myMonitor);
			} catch (CleanupFailedException e) {
				if (logger.isEnabledFor(Level.ERROR)) {
					logger.error("Critical failure during workflow cleanup");
				}
				this.exceptionHandler.handleCleanupFailed(e);
			}
		}

		myMonitor.worked(1);
		myMonitor.done();

		if (logger.isEnabledFor(Level.INFO)) {
			logger.info("Workflow engine completed task");
		}
	}
}
