package de.uka.ipd.sdq.workflow.extension;

import java.util.Map;

import de.uka.ipd.sdq.workflow.configuration.AbstractJobConfiguration;

/**
 * A configuration for extendible jobs which is able to carry the required launch information to
 * inject this into the extending job delegates.
 * 
 * @author Benjamin Klatt
 * 
 */
public abstract class AbstractExtendableJobConfiguration extends AbstractJobConfiguration implements
        ExtendableJobConfiguration {

    // ////////////////////////////////
    // ATTRIBUTES
    // ////////////////////////////////

    /** The mode of the current launch (run/debug). */
    private String mode = null;

    /** The configuration of the current launch to work with. */
    private Map<String, Object> attributes = null;

    // ////////////////////////////////
    // CONSTRUCTORS
    // ////////////////////////////////

    /**
     * Constructor that requires access to the launch configuration and the launch mode.
     * 
     * @param attributes
     *            the attributes
     * @param mode
     *            The mode to set.
     */
    public AbstractExtendableJobConfiguration(Map<String, Object> attributes, String mode) {
        this.attributes = attributes;
        this.mode = mode;
    }

    // ////////////////////////////////
    // BUSINESS METHODS
    // ////////////////////////////////

    // ////////////////////////////////
    // GETTER / SETTER
    // ////////////////////////////////

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.launchconfig.extension.ExtendableJobConfiguration#getMode()
     */
    /**
     * Gets the mode.
     * 
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.workflow.launchconfig.extension.ExtendableJobConfiguration#getLaunchConfiguration
     * ()
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

}
