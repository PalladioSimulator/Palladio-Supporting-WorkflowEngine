package de.uka.ipd.sdq.workflow.mocks;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;

/**
 * Mock implementation of a job with methods to help testing.
 * 
 * @author Philipp Meier
 */
public class MockJob implements IJob {

    /** The execution number. */
    private static int executionNumber = 0;

    /**
     * Reset execution number.
     */
    public static void resetExecutionNumber() {
        executionNumber = 0;
    }

    /**
     * Next execution number.
     * 
     * @return the int
     */
    static int nextExecutionNumber() {
        executionNumber += 1;
        return executionNumber;
    }

    /** The my was executed. */
    private boolean myWasExecuted = false;

    /** The my was rolled back. */
    private boolean myWasRolledBack = false;

    /** The my was asked name. */
    private boolean myWasAskedName = false;

    /** The my execution number. */
    private int myExecutionNumber = 0;

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        myWasExecuted = true;
        myExecutionNumber = nextExecutionNumber();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        myWasAskedName = true;
        return "MockJob";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#rollback(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void rollback(IProgressMonitor monitor) throws RollbackFailedException {
        myWasRolledBack = true;
    }

    /**
     * Was executed.
     * 
     * @return true, if successful
     */
    public boolean wasExecuted() {
        return myWasExecuted;
    }

    /**
     * Was rolled back.
     * 
     * @return true, if successful
     */
    public boolean wasRolledBack() {
        return myWasRolledBack;
    }

    /**
     * Was asked name.
     * 
     * @return true, if successful
     */
    public boolean wasAskedName() {
        return myWasAskedName;
    }

    /**
     * Gets the execution number.
     * 
     * @return the execution number
     */
    public int getExecutionNumber() {
        return myExecutionNumber;
    }
}
