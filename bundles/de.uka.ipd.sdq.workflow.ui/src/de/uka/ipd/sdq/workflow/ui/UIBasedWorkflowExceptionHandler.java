package de.uka.ipd.sdq.workflow.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.PlatformUI;
import de.uka.ipd.sdq.dialogs.error.ErrorDisplayDialog;
import de.uka.ipd.sdq.workflow.WorkflowExceptionHandler;
import de.uka.ipd.sdq.workflow.WorkflowPlugin;

/**
 * Workflow exception handler which is able to interact with the Eclipse UI.
 * 
 * @author Steffen Becker
 */
public class UIBasedWorkflowExceptionHandler extends WorkflowExceptionHandler {

    /**
     * An Eclipse UI based workflow exception handler which interacts with the Eclipse UI in case of
     * failures.
     * 
     * @param shouldThrowException
     *            Set this to true to disable UI interaction and only to throw exceptions if sth.
     *            goes wrong. Usefull for unit tests or batch runs
     */
    public UIBasedWorkflowExceptionHandler(boolean shouldThrowException) {
        super(shouldThrowException);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.workflow.exceptions.WorkflowExceptionHandler#handleCriticalException(java.
     * lang.Exception)
     */
    @Override
    protected void handleCriticalException(Exception e) {
        super.handleCriticalException(e);

        PlatformUI.getWorkbench().getDisplay().syncExec(new ErrorDisplayRunner(e));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.workflow.exceptions.WorkflowExceptionHandler#logException(java.lang.Exception)
     */
    @Override
    protected void logException(Exception e) {
        WorkflowPlugin.log(IStatus.ERROR, e.getMessage());
    }

    /**
     * Helper class that allows an error display dialog to appear from a non user interface thread
     * because the workbench shell is otherwise not accessible.
     * 
     * @author Philipp Meier
     */
    private class ErrorDisplayRunner implements Runnable {

        /** The e. */
        private Throwable e;

        /**
         * Instantiates a new error display runner.
         * 
         * @param e
         *            the throwable to display in the error display dialog
         */
        public ErrorDisplayRunner(Throwable e) {
            super();
            this.e = e;
        }

        
        /**
         * Run method to realize the continuously updated error display in the user interface.
         * @see java.lang.Runnable#run()
         */
        public void run() {
            new ErrorDisplayDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), e).open();
        }
    }

}
