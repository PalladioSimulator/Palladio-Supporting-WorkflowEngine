package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;

import de.uka.ipd.sdq.workflow.AbstractJobConfiguration;
import de.uka.ipd.sdq.workflow.launchconfig.configbuilder.AbstractUIBasedConfigurationBuilder;

/**
 * The Class AbstractConfigBuilderTab.
 */
public abstract class AbstractConfigBuilderTab extends AbstractLaunchConfigurationTab {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse.debug.core.
     * ILaunchConfiguration)
     */
    @Override
    public boolean isValid(ILaunchConfiguration launchConfig) {
        AbstractUIBasedConfigurationBuilder<?> builder;
        try {
            builder = getConfigurationBuilder(launchConfig);
        } catch (CoreException e) {
            return false;
        }
        AbstractJobConfiguration config = builder.build();

        if (config.isValid()) {
            this.setErrorMessage(null);
            return true;
        } else {
            String errorMessage = config.getErrorMessage();
            this.setErrorMessage(errorMessage);

            return false;
        }
    }

    /**
     * Gets the configuration builder.
     * 
     * @param launchConfig
     *            the launch config
     * @return the configuration builder
     * @throws CoreException
     *             the core exception
     */
    protected abstract AbstractUIBasedConfigurationBuilder<?> getConfigurationBuilder(ILaunchConfiguration launchConfig)
            throws CoreException;
}
