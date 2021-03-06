package de.uka.ipd.sdq.workflow;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

/**
 * The Class ExecutionTimeLoggingProgressMonitor.
 */
public class ExecutionTimeLoggingProgressMonitor extends SubProgressMonitor {

    /**
     * Instantiates a new execution time logging progress monitor.
     * 
     * @param monitor
     *            the monitor
     * @param ticks
     *            the ticks
     */
    public ExecutionTimeLoggingProgressMonitor(IProgressMonitor monitor, int ticks) {
        super(monitor, ticks);
    }

    /** The start time. */
    private double startTime = 0;

    /** The task name. */
    private String taskName;

    /** The logger. */
    private final Logger logger = Logger.getLogger(ExecutionTimeLoggingProgressMonitor.class);

    /**
     * Begin task.
     * 
     * @param name
     *            the name
     * @param totalWork
     *            the total work
     * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String, int)
     */
    @Override
    public void beginTask(String name, int totalWork) {
        this.startTime = System.nanoTime();
        this.taskName = name;
        super.beginTask(name, totalWork);
    }

    /**
     * Done.
     * 
     * TODO Check whether <code>taskName</code> should be mandatory
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#done()
     */
    @Override
    public void done() {
        double endTime = System.nanoTime();
        super.done();
        if (logger.isEnabledFor(Level.INFO)) {
            if (taskName.isEmpty()) {
                logger.info("Task completed in " + (endTime - startTime) / Math.pow(10, 9)
                        + " seconds");
            } else {
                logger.info("Task " + taskName + " completed in " + (endTime - startTime) / Math.pow(10, 9)
                        + " seconds");
            }
        }
    }

    /**
     * Internal worked.
     * 
     * @param work
     *            the work
     * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
     */
    @Override
    public void internalWorked(double work) {
        super.internalWorked(work);
    }

    /**
     * Checks if is canceled.
     * 
     * @return true, if is canceled
     * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
     */
    @Override
    public boolean isCanceled() {
        return super.isCanceled();
    }

    /**
     * Sets the canceled.
     * 
     * @param value
     *            the new canceled
     * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
     */
    @Override
    public void setCanceled(boolean value) {
        super.setCanceled(value);
    }

    /**
     * Sets the task name.
     * 
     * @param name
     *            the new task name
     * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
     */
    @Override
    public void setTaskName(String name) {
        taskName = name;
        super.setTaskName(name);
    }

    /**
     * Sub task.
     * 
     * @param name
     *            the name
     * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
     */
    @Override
    public void subTask(String name) {
        super.subTask(name);
    }

    /**
     * Worked.
     * 
     * @param work
     *            the work
     * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
     */
    @Override
    public void worked(int work) {
        super.worked(work);
    }

}
