package de.uka.ipd.sdq.workflow.exceptions;

/**
 * The Class InvalidWorkflowJobConfiguration.
 */
public class InvalidWorkflowJobConfiguration extends Exception {

	/**
     * Instantiates a new invalid workflow job configuration.
     * 
     * @param errorMessage
     *            the error message
     */
	public InvalidWorkflowJobConfiguration(String errorMessage) {
		super(errorMessage);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 474083690679380656L;

}
