package de.uka.ipd.sdq.workflow.launchconfig.multiple;

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
 * Launch configuration tab to choose from existing launch configurations.
 * 
 * Configurations of all types except the own one are provided.
 * 
 * @author Anne Martens
 * @author Benjamin Klatt
 */
public class LaunchMultipleTab extends AbstractLaunchConfigurationTab {

	/** Identifier for the selected launches attribute. */
	public static final String SELECTED_LAUNCHES = "selected.launches";

	/** The Constant NUMBER_OF_COLUMNS. */
	private static final int NUMBER_OF_COLUMNS = 1;

	/** The launch configuration id of the multiple launch config type. */
	private static final String CONFIG_TYPE_ID = "de.uka.ipd.sdq.workflow.launchconfig.multiple.launchMultipleType";

	/** The buttons. */
	private List<Button> buttons = new ArrayList<Button>();

	/** Logger for log4j. */
	private static final Logger LOGGER = Logger.getLogger(LaunchMultipleTab.class);

	/**
	 * Create the view to configure the launch configuration as part of the ui
	 * tab.
	 * 
	 * @param parent
	 *            The composite the ui control will be placed in.
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
    public void createControl(Composite parent) {

		// build the tab pane container
		GridLayout layout = new GridLayout(NUMBER_OF_COLUMNS, false);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		setControl(container);

		// Logging group
		final Group loggingGroup = new Group(container, SWT.FILL);
		loggingGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		loggingGroup.setText("Choose Launch Configs to run.");
		loggingGroup.setLayout(layout);

		// build checkboxes
		buttons = new ArrayList<Button>();
		List<ILaunchConfiguration> configs = getLaunchConfigs();
		for (ILaunchConfiguration launchConfiguration : configs) {
			buttons.add(createCheckBox(launchConfiguration, loggingGroup));
		}

	}

	/**
	 * Gets the launch configurations to present to the user. The method loads
	 * the configuration instances for all types except of the launch multiple
	 * type itself.
	 * 
	 * @return The list of launch configurations.
	 */
	public List<ILaunchConfiguration> getLaunchConfigs() {
		ILaunchManager manager = this.getLaunchManager();
		List<ILaunchConfiguration> allTypes = new LinkedList<ILaunchConfiguration>();
		ILaunchConfigurationType[] launchTypes = manager
				.getLaunchConfigurationTypes();
		for (ILaunchConfigurationType launchType : launchTypes) {

			try {

				if (!launchType.getIdentifier().equals(CONFIG_TYPE_ID)) {
					ILaunchConfiguration[] configs = null;
					configs = manager.getLaunchConfigurations(launchType);

					for (ILaunchConfiguration iLaunchConfiguration : configs) {
						allTypes.add(iLaunchConfiguration);
					}
				}

			} catch (CoreException e) {
				if (LOGGER.isEnabledFor(Level.ERROR)) {
					LOGGER.error("Could not find a configuration type for id "
							+ launchType.getIdentifier() + ", skipping it.");
				}
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
	private Button createCheckBox(ILaunchConfiguration launchConfiguration,
			Group launchConfigGroup) {

		Button launchConfigButton = new Button(launchConfigGroup, SWT.CHECK);
		launchConfigButton.setText(launchConfiguration.getName());
		launchConfigButton.addSelectionListener(new SelectionAdapter() {
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
	@Override
    public String getName() {
		return "Launch Multiple Runs Tab";
	}

	/**
	 * Initialize the configuration view with the values of a launch
	 * configuration object.
	 * 
	 * @param configuration
	 *            The configuration object to read the values from.
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.
	 *      ILaunchConfiguration)
	 */
	@Override
    public void initializeFrom(ILaunchConfiguration configuration) {

		try {
			@SuppressWarnings("unchecked")
			List<String> selectedLaunchConfigs = configuration.getAttribute(
					SELECTED_LAUNCHES, new LinkedList<String>());

			for (String configName : selectedLaunchConfigs) {
				Button button = getButtonFor(configName);
				if (button != null) {
					button.setSelection(true);
				}
			}

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a button element for a given name.
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
	 * Handle the apply action. Build the list of selected launch configurations
	 * identified by the button text and store the list of selected launches in
	 * a configuration attribute.
	 * 
	 * @param config
	 *            The configuration to be filled as part of the apply.
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.
	 *      ILaunchConfigurationWorkingCopy)
	 */
	@Override
    public void performApply(ILaunchConfigurationWorkingCopy config) {
		LinkedList<String> selectedConfigs = new LinkedList<String>();
		for (Button button : this.buttons) {
			if (button.getSelection()) {
				selectedConfigs.add(button.getText());
			}
		}
		config.setAttribute(SELECTED_LAUNCHES, selectedConfigs);

	}

	/**
	 * Set the default values in the configuration view.
	 * 
	 * @param config
	 *            The configuration object to set the default values in.
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.
	 *      ILaunchConfigurationWorkingCopy)
	 */
	@Override
    public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		for (Button button : this.buttons) {
			config.setAttribute(button.getText(), true);
		}

	}
}
