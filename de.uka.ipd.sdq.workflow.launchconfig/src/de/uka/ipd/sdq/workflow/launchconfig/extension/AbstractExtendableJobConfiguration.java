package de.uka.ipd.sdq.workflow.launchconfig.extension;

import org.eclipse.debug.core.ILaunchConfiguration;

import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration;

/**
 * A configuration for extendible jobs which is able to carry the required launch information
 * to inject this into the extending job delegates.
 * 
 * @author Benjamin Klatt
 * 
 */
public abstract class AbstractExtendableJobConfiguration extends AbstractWorkflowBasedRunConfiguration 
            implements ExtendibleJobConfiguration {

    // ////////////////////////////////
    // ATTRIBUTES
    // ////////////////////////////////

    /** The mode of the current launch (run/debug). */
    private String mode = null;

    /** The configuration of the current launch to work with. */
    private ILaunchConfiguration launchConfiguration = null;

    // ////////////////////////////////
    // CONSTRUCTORS
    // ////////////////////////////////
    
    /**
     * Constructor that requires access to the launch configuration and the launch mode.
     * 
     * @param launchConfiguration The configuration to set.
     * @param mode The mode to set.
     */
    public AbstractExtendableJobConfiguration(ILaunchConfiguration launchConfiguration,String mode) {
        this.launchConfiguration = launchConfiguration;
        this.mode = mode;
    }

    // ////////////////////////////////
    // BUSINESS METHODS
    // ////////////////////////////////

    // ////////////////////////////////
    // GETTER / SETTER
    // ////////////////////////////////

    /* (non-Javadoc)
     * @see de.uka.ipd.sdq.workflow.launchconfig.extension.ExtendableJobConfiguration#getMode()
     */
    @Override
    public String getMode() {
        return mode;
    }

    /* (non-Javadoc)
     * @see de.uka.ipd.sdq.workflow.launchconfig.extension.ExtendableJobConfiguration#getLaunchConfiguration()
     */
    @Override
    public ILaunchConfiguration getLaunchConfiguration() {
        return launchConfiguration;
    }

}
