package de.uka.ipd.sdq.workflow.jobs;

import org.eclipse.core.runtime.CoreException;

/**
 * Exception to be thrown if the execute method of a job terminates unrecoverable.
 * @author Steffen Becker
  */
public class JobFailedException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2638800346598141354L;

	/**
     * Instantiates a new job failed exception.
     */
	public JobFailedException() {
		super();
	}
	
	/**
     * Instantiates a new job failed exception.
     * 
     * @param string
     *            the string
     */
	public JobFailedException(String string) {
		super(string);
	}	
	
	/**
     * Instantiates a new job failed exception.
     * 
     * @param string
     *            the string
     * @param e
     *            the e
     */
	public JobFailedException(String string, Throwable e) {
		super(string, e);
	}

	/**
     * Instantiates a new job failed exception.
     * 
     * @param e
     *            the e
     */
	public JobFailedException(CoreException e) {
		super(e);
	}
}
