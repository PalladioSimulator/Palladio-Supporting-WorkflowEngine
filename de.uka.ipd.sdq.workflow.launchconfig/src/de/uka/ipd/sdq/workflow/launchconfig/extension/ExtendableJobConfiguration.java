package de.uka.ipd.sdq.workflow.launchconfig.extension;

import java.util.Map;

/**
 * Interface of the configuration of an extendable job.
 * 
 * This type of configuration is able to forward required data objects
 * to the extending job configuration builders.
 * 
 * @author Benjamin Klatt, Michael Hauck
 *
 */
public interface ExtendableJobConfiguration {
    
    /**
     * Get the configuration of the current launch.
     * 
     * @return the launchConfiguration
     */
    public abstract Map<String, Object> getAttributes();
    
}
