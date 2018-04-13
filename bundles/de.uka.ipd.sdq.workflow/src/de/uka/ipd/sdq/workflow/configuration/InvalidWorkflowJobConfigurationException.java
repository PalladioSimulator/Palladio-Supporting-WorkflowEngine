package de.uka.ipd.sdq.workflow.configuration;

/**
 * The Class InvalidWorkflowJobConfiguration.
 */
public class InvalidWorkflowJobConfigurationException extends Exception {

	/**
     * Instantiates a new invalid workflow job configuration.
     * 
     * @param errorMessage
     *            the error message
     */
	public InvalidWorkflowJobConfigurationException(String errorMessage) {
		super(errorMessage);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 474083690679380656L;

}
