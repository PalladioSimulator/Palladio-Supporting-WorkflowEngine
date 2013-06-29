package de.uka.ipd.sdq.workflow.extension;

import java.util.Map;


/**
 * Provides an abstract class for which an implementation has to be provided by a workflow extension
 * job. The class has to provide the logic to derive job configuration values based on the given
 * Eclipse AbstractWorkflowBasedRunConfiguration.
 * 
 * @author hauck
 * @author Benjamin Klatt
 * 
 */
public abstract class AbstractWorkflowExtensionConfigurationBuilder {

    // ////////////////////////////////
    // ATTRIBUTES
    // ////////////////////////////////

    // ////////////////////////////////
    // CONSTRUCTORS
    // ////////////////////////////////

    // ////////////////////////////////
    // BUSINESS METHODS
    // ////////////////////////////////

    /**
     * Fill the configuration object with extension specific configurations.
     * 
     * @param attributes
     *            the attributes
     * @return The prepared configuration for the job extension
     */
    public abstract AbstractExtensionJobConfiguration buildConfiguration(Map<String, Object> attributes);

    // ////////////////////////////////
    // GETTER / SETTER
    // ////////////////////////////////
}
