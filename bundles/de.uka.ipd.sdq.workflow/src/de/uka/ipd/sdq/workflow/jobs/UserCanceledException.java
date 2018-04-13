package de.uka.ipd.sdq.workflow.jobs;

/**
 * The Class UserCanceledException.
 */
public class UserCanceledException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1628079109828905830L;

	/**
     * Instantiates a new user canceled exception.
     */
	public UserCanceledException() {
		super();
	}
	
	/**
     * Instantiates a new user canceled exception.
     * 
     * @param string
     *            the string
     */
	public UserCanceledException(String string) {
		super(string);
	}	
	
	/**
     * Instantiates a new user canceled exception.
     * 
     * @param string
     *            the string
     * @param e
     *            the e
     */
	public UserCanceledException(String string, Throwable e) {
		super(string, e);
	}
}
