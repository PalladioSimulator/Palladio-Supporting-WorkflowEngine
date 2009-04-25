package de.uka.ipd.sdq.codegen.workflow.exceptions;

public class WorkflowFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2648045896419154872L;

	public WorkflowFailedException(String msg, Exception e) {
		super(msg,e);
	}

}
