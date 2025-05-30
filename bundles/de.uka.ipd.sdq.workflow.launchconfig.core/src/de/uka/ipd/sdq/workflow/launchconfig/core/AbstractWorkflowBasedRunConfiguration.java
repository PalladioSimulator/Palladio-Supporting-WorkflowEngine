/**
 * 
 */
package de.uka.ipd.sdq.workflow.launchconfig.core;

import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.configuration.AbstractJobConfiguration;
import de.uka.ipd.sdq.workflow.configuration.IJobConfiguration;

/**
 * An adapted job configuration with specifics for the Eclipse IDE - run mode (run or debug) -
 * interactice - clonable
 * 
 * Base class for all configurations used to configure an Eclipse based {@link Workflow} (or
 * launch). which internally executes a sequence of jobs (compile, M2M Transforms, simulations,
 * etc.)
 * 
 */
public abstract class AbstractWorkflowBasedRunConfiguration extends AbstractJobConfiguration
        implements IJobConfiguration, Cloneable {

    /** The is interactive. */
    protected boolean isInteractive = false;

    /** The is debug. */
    protected boolean isDebug = false;

    /**
     * Constructor.
     */
    public AbstractWorkflowBasedRunConfiguration() {
        super();
    }

    /**
     * Returns whether the workflow engine runs in interactive mode. In non-interactive mode it
     * throws an exception upon failing its task. In interactive mode it displays an error message
     * to the user and logs the failure in the OSGi log.
     * 
     * Non-interactive mode is useful for batch execution and unit testing. It is the default.
     * 
     * @return Whether the workflow engine runs in interactive (UI-based) mode
     */
    public boolean isInteractive() {
        return isInteractive;
    }

    /**
     * Return whether the workflow runs in interactive mode, i.e., whether user interaction is
     * possible.
     * 
     * @param isInteractive
     *            Whether the workflow runs in interactive mode
     */
    public void setInteractive(boolean isInteractive) {
        checkFixed();
        this.isInteractive = isInteractive;
    }

    /**
     * Is debug determines whether the run is executed as debug run. In debug mode, the run can be
     * interrupted to inspect the state of the run. Debug mode can have an impact on all steps
     * executed in a workflow.
     * 
     * @return Whether the workflow runs in debug mode
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * Set the isDebug property. Can only be called as long as the configuration is not validated
     * and frozen.
     * 
     * @param isDebug
     *            Whether debug is enabled or not
     */
    public void setDebug(boolean isDebug) {
        checkFixed();
        this.isDebug = isDebug;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        AbstractWorkflowBasedRunConfiguration config = (AbstractWorkflowBasedRunConfiguration) super.clone();
        config.isDebug = this.isDebug;
        config.isInteractive = this.isInteractive;
        return config;
    }
}
