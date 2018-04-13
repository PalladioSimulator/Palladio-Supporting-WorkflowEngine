package de.uka.ipd.sdq.workflow.jobs;

/**
 * Exception to be thrown if the cleanup method of a job terminates unrecoverable.
 * @author Steffen Becker
 *
 */
public class CleanupFailedException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5121021317089764991L;

	/**
     * Instantiates a new cleanup failed exception.
     */
	public CleanupFailedException() {
		super();
	}
	
	/**
     * Instantiates a new cleanup failed exception.
     * 
     * @param string
     *            the string
     */
	public CleanupFailedException(String string) {
		super(string);
	}	
	
	/**
     * Instantiates a new cleanup failed exception.
     * 
     * @param string
     *            the string
     * @param e
     *            the e
     */
	public CleanupFailedException(String string, Throwable e) {
		super(string, e);
	}
}
