package de.uka.ipd.sdq.workflow.exceptions;

/**
 * Exception to be thrown if the rollback method of a job terminates unrecoverable.
 * @author Steffen Becker
 *
 */
public class RollbackFailedException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5121021317089764991L;

	/**
     * Instantiates a new rollback failed exception.
     */
	public RollbackFailedException() {
		super();
	}
	
	/**
     * Instantiates a new rollback failed exception.
     * 
     * @param string
     *            the string
     */
	public RollbackFailedException(String string) {
		super(string);
	}	
	
	/**
     * Instantiates a new rollback failed exception.
     * 
     * @param string
     *            the string
     * @param e
     *            the e
     */
	public RollbackFailedException(String string, Throwable e) {
		super(string, e);
	}
}
