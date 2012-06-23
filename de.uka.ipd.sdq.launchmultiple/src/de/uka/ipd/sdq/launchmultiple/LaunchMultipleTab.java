package de.uka.ipd.sdq.launchmultiple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * The Class LaunchMultipleTab.
 * 
 * @author Anne Martens
 */
public class LaunchMultipleTab extends AbstractLaunchConfigurationTab {

    /** The Constant NUMBER_OF_COLUMNS. */
    private static final int NUMBER_OF_COLUMNS = 3;

    /** The buttons. */
    private List<Button> buttons = new ArrayList<Button>();

    // The id attribute in the org.eclipse.debug.core.launchConfigurationTypes extension point.
    /** The launch types. */
    private String[] launchTypes = { "de.uka.ipd.sdq.simucontroller.SimuLaunching",
            "de.uka.ipd.sdq.dsolver_plugin.PCMSolverLaunchConfigurationType",
            "de.uka.ipd.sdq.dsolver_plugin.PCMSolverLaunchConfigurationType.Reliability",
            "de.uka.ipd.sdq.dsexplore.launchDSE", "edu.kit.ipd.sdq.simqpnsolver.SimQPNSolverLaunchConfigurationType" };

    // FIXME: retrieve instance of this using the extension point
    /** The instance. */
    private static LaunchMultipleTab instance;

    /** Logger for log4j. */
    private static Logger logger = Logger.getLogger("de.uka.ipd.sdq.launchmultiple");

    /**
     * Create the view to configure the launch configuration as part of the ui tab.
     * 
     * @param parent
     *            The composite the ui control will be placed in.
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {

        // FIXME: See above
        instance = this;

        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layoutOuter = new GridLayout();
        layoutOuter.numColumns = NUMBER_OF_COLUMNS;
        container.setLayout(layoutOuter);
        setControl(container);

        buttons = new ArrayList<Button>();

        /** Logging group */
        final Group loggingGroup = new Group(container, SWT.NONE);
        loggingGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        loggingGroup.setText("Choose Launch Configs to run.");
        GridLayout layoutInner = new GridLayout();
        layoutInner.numColumns = NUMBER_OF_COLUMNS;
        loggingGroup.setLayout(layoutInner);

        List<ILaunchConfiguration> configs = getLaunchConfigs();

        for (ILaunchConfiguration launchConfiguration : configs) {
            buttons.add(createCheckBox(launchConfiguration, loggingGroup));
        }

    }

    /**
     * Gets the launch configs.
     * 
     * @return the launch configs
     */
    public List<ILaunchConfiguration> getLaunchConfigs() {
        ILaunchManager manager = this.getLaunchManager();

        List<ILaunchConfiguration> allTypes = new LinkedList<ILaunchConfiguration>();
        for (String launchTypeID : this.launchTypes) {

            try {
                ILaunchConfigurationType launchType = manager.getLaunchConfigurationType(launchTypeID);
                if (launchType != null) {
                    ILaunchConfiguration[] configs = null;
                    configs = manager.getLaunchConfigurations(launchType);

                    for (ILaunchConfiguration iLaunchConfiguration : configs) {
                        allTypes.add(iLaunchConfiguration);
                    }
                }

            } catch (CoreException e) {
            	if(logger.isEnabledFor(Level.ERROR)) 
            		logger.error("Could not find a configuration type for id " + launchTypeID + ", skipping it.");
                e.printStackTrace();
            }

        }
        return allTypes;
    }

    /**
     * Creates the check box.
     * 
     * @param launchConfiguration
     *            the launch configuration
     * @param launchConfigGroup
     *            the launch config group
     * @return the button
     */
    private Button createCheckBox(ILaunchConfiguration launchConfiguration, Group launchConfigGroup) {

        Button launchConfigButton = new Button(launchConfigGroup, SWT.CHECK);
        launchConfigButton.setText(launchConfiguration.getName());
        launchConfigButton.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.
             * SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                LaunchMultipleTab.this.updateLaunchConfigurationDialog();
            }
        });
        launchConfigButton.setSelection(false);

        return launchConfigButton;

    }

    /**
     * Get the name of the launch configuration tab.
     * 
     * @return The name.
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
     */
    public String getName() {
        return "Launch Multiple Runs Tab";
    }

    /**
     * Initialize the configuration view with the values of a launch configuration object.
     * 
     * @param configuration
     *            The configuration object to read the values from.
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.
     *      ILaunchConfiguration)
     */
    public void initializeFrom(ILaunchConfiguration configuration) {

        List<ILaunchConfiguration> configs = getLaunchConfigs();

        for (ILaunchConfiguration launchConfiguration : configs) {
            boolean selected = false;
            try {
                selected = configuration.getAttribute(launchConfiguration.getName(), false);
            } catch (CoreException e) {
                e.printStackTrace();
            }
            Button button = getButtonFor(launchConfiguration.getName());

            if (button != null) {
                button.setSelection(selected);
            }

        }

    }

    /**
     * Can be null.
     * 
     * @param name
     *            the name
     * @return Button with that name or null
     */
    private Button getButtonFor(String name) {
        for (Button button : this.buttons) {
            if (button.getText().equals(name)) {
                return button;
            }
        }
        return null;
    }

    /**
     * Handle the apply action.
     * 
     * @param config
     *            The configuration to be handled as part of the apply.
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.
     *      ILaunchConfigurationWorkingCopy)
     */
    public void performApply(ILaunchConfigurationWorkingCopy config) {
        for (Button button : this.buttons) {
            config.setAttribute(button.getText(), button.getSelection());
        }

    }

    /**
     * Set the default values in the configuration view.
     * 
     * @param config The configuration object to set the default values in.
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.
     * ILaunchConfigurationWorkingCopy)
     */
    public void setDefaults(ILaunchConfigurationWorkingCopy config) {
        for (Button button : this.buttons) {
            config.setAttribute(button.getText(), true);
        }

    }

    /**
     * FIXME Do this better, e.g. by extension point No guarantee which instance is returned!
     * 
     * @return the any instance
     */
    public static LaunchMultipleTab getAnyInstance() {
        return instance;
    }

}
