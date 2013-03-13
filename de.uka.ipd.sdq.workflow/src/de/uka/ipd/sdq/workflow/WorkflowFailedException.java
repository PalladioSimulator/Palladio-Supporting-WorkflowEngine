package de.uka.ipd.sdq.workflow;

/**
 * The Class WorkflowFailedException.
 */
public class WorkflowFailedException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2648045896419154872L;

    /**
     * Instantiates a new workflow failed exception.
     * 
     * @param msg
     *            the msg
     * @param e
     *            the e
     */
    public WorkflowFailedException(String msg, Exception e) {
        super(msg, e);
    }

    /**
     * Instantiates a new workflow failed exception.
     * 
     * @param msg
     *            the msg
     */
    public WorkflowFailedException(String msg) {
        super(msg);
    }

}
