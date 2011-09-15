package de.uka.ipd.sdq.workflow.launchconfig.extension;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Interface of the configuration of an extendible job.
 * 
 * This type of configuration is able to forward required data objects
 * to the extending job configuration builders.
 * 
 * @author Benjamin Klatt
 *
 */
public interface ExtendibleJobConfiguration {

    /**
     * Get the mode of the current launch.
     * 
     * @return the mode
     */
    public abstract String getMode();

    /**
     * Get the configuration of the current launch.
     * 
     * @return the launchConfiguration
     */
    public abstract ILaunchConfiguration getLaunchConfiguration();

}
