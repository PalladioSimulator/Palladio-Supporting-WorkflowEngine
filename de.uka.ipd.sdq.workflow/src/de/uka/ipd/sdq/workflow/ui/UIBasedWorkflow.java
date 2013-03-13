package de.uka.ipd.sdq.workflow.ui;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.BlackboardBasedWorkflow;
import de.uka.ipd.sdq.workflow.WorkflowExceptionHandler;
import de.uka.ipd.sdq.workflow.blackboard.Blackboard;
import de.uka.ipd.sdq.workflow.jobs.IJob;

/**
 * A workflow which is able to interact with the Eclipse GUI, i.e., for message logging or error
 * dialogs. Additionally the workflow supports the use of a blackboard for all its jobs to exchange
 * information, e.g., EMF models
 * 
 * @param <T>
 *            The type of the blackboard to be used in this workflow
 * @author Steffen
 */
public class UIBasedWorkflow<T extends Blackboard<?>> 
extends BlackboardBasedWorkflow<T> {

	/**
     * Instantiates a new uI based workflow.
     * 
     * @param job
     *            the job
     * @param monitor
     *            the monitor
     * @param workflowExceptionHandler
     *            the workflow exception handler
     * @param blackboard
     *            the blackboard
     */
	public UIBasedWorkflow(
			IJob job, 
			IProgressMonitor monitor, 
			WorkflowExceptionHandler workflowExceptionHandler,
			T blackboard) {
		super(job, monitor, workflowExceptionHandler, blackboard);
	}
}
