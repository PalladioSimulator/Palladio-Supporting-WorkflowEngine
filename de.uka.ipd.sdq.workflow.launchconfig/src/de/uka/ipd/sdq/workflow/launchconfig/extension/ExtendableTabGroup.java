package de.uka.ipd.sdq.workflow.launchconfig.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;


public abstract class ExtendableTabGroup extends AbstractLaunchConfigurationTabGroup {

    /**
     * Create the tabs for installed plug-ins contributing to this extension point and registered for the specified work
     * flow.
     * 
     * @param dialog
     *            The launch configuration dialog to access.
     * @param mode
     *            The mode of the launch.
     * @param worflowId
     *            The id of the work flow the extensions should have registered for.
     */
    public final List<ILaunchConfigurationTab> createExtensionTabs(ILaunchConfigurationDialog dialog, String mode, String workflowId) {

        List<ILaunchConfigurationTab> tabs = new ArrayList<ILaunchConfigurationTab>();
        List<AbstractLaunchConfigurationTab> extensionUiTabs = new ArrayList<AbstractLaunchConfigurationTab>();
        for (WorkflowExtension workflowExtension : ExtensionHelper
                .getWorkflowExtensionsSortedByPriority(workflowId)) {
            if (workflowExtension.getLaunchConfigurationTab() != null) {
                AbstractLaunchConfigurationTab extensionTab = workflowExtension.getLaunchConfigurationTab();
                extensionUiTabs.add(extensionTab);
            }
        }
        if (!extensionUiTabs.isEmpty()) {
            tabs.addAll(extensionUiTabs);
        }

        return tabs;
    }
}
