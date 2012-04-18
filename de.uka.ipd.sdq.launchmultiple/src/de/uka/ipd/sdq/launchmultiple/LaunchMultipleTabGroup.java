package de.uka.ipd.sdq.launchmultiple;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * The Class LaunchMultipleTabGroup.
 * 
 * @author Anne
 */
public class LaunchMultipleTabGroup extends AbstractLaunchConfigurationTabGroup {

    /**
     * Creates the tabs contained in this tab group for the specified launch mode. The tabs
     * control's are not created. This is the fist method called in the lifecycle of a tab group.
     * 
     * @param dialog
     *            the launch configuration dialog this tab group is contained in
     * @param mode
     *            the mode the launch configuration dialog was opened in
     * 
     * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse.debug.ui.ILaunchConfigurationDialog,
     *      java.lang.String)
     */
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] { new LaunchMultipleTab() };
        setTabs(tabs);

    }

}
