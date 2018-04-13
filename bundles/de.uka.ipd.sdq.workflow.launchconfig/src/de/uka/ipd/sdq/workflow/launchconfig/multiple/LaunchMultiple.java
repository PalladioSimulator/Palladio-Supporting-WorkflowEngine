package de.uka.ipd.sdq.workflow.launchconfig.multiple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

/**
 * The Class LaunchMultiple.
 *
 * @author Anne Martens
 */
public class LaunchMultiple implements ILaunchConfigurationDelegate {

    /** Logger for log4j. */
    private static final Logger LOGGER = Logger.getLogger(LaunchMultiple.class);

    /**
     * Load and execute the selected launch configurations.
     *
     * @param configuration
     *            the configuration
     * @param mode
     *            the mode
     * @param launch
     *            the launch
     * @param monitor
     *            the monitor
     * @throws CoreException
     *             the core exception
     */
    @Override
    public void launch(final ILaunchConfiguration configuration, final String mode,
            final ILaunch launch, final IProgressMonitor monitor) throws CoreException {

        final List<String> selectedLaunchConfigs = configuration.getAttribute(
                LaunchMultipleTab.SELECTED_LAUNCHES, new LinkedList<String>());

        final LaunchMultipleTab tab = new LaunchMultipleTab();
        final List<ILaunchConfiguration> availableLaunchconfis = tab.getLaunchConfigs();

        final List<ILaunchConfiguration> configsToBeLaunched = new ArrayList<ILaunchConfiguration>();
        for (final ILaunchConfiguration launchConfiguration : availableLaunchconfis) {
            if (selectedLaunchConfigs.contains(launchConfiguration.getName())) {
                configsToBeLaunched.add(launchConfiguration);
            }
        }

        executeLaunchConfigurations(mode, launch, monitor, configsToBeLaunched);

    }

    /**
     * Launch the selected launch configurations.
     *
     * Exceptions of single launches will be printed and the
     * next launch will be started.
     *
     * @param mode
     *            The mode to launch in (run or debug).
     * @param launch
     *            The launch context.
     * @param monitor
     *            The monitor for progress monitoring.
     * @param configsToBeLaunched
     *            The list of launch configs to start.
     */
    private void executeLaunchConfigurations(final String mode, final ILaunch launch,
            final IProgressMonitor monitor,
            final List<ILaunchConfiguration> configsToBeLaunched) {
        for (final ILaunchConfiguration launchConfiguration : configsToBeLaunched) {

            try {

                // retrieve SimuCom Launch Delegate
                final Set<String> modes = new HashSet<String>();
                modes.add(mode);
                final ILaunchConfigurationDelegate delegate = launchConfiguration
                        .getType().getDelegates(modes)[0].getDelegate();
                delegate.launch(launchConfiguration, mode, launch, monitor);

            } catch (final Exception e) {
                if (LOGGER.isEnabledFor(Level.ERROR)) {
                    LOGGER.error("Running " + launchConfiguration.getName()
                            + " failed. I will start the next one. Cause: "
                            + e.getMessage());
                }
                e.printStackTrace();
            }

        }
    }

}
