package de.uka.ipd.sdq.workflow.launchconfig.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;


/**
 * Provides an abstract class for which an implementation has to be provided by a workflow extension job. The class has
 * to provide the logic to derive job configuration values based on the given Eclipse
 * AbstractWorkflowBasedRunConfiguration.
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
     * @param configuration
     *            The launch configuration object to derive the job configuration from.
     * @param mode
     *            The mode of the launch (run/debug)
     * @return The prepared configuration for the job extension
     * @throws CoreException
     *             Identifying that an error occurred when trying to fill the configuration.
     */
    public abstract AbstractExtensionJobConfiguration buildConfiguration(ILaunchConfiguration configuration,
            String mode) throws CoreException;

    // ////////////////////////////////
    // GETTER / SETTER
    // ////////////////////////////////
}
