package de.uka.ipd.sdq.workflow.launchconfig.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

import de.uka.ipd.sdq.workflow.launchconfig.tabs.DebugEnabledCommonTab;

public abstract class ExtendableTabGroup extends AbstractLaunchConfigurationTabGroup {

	@Override
	public final void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		// TODO Auto-generated method stub
		
		List<ILaunchConfigurationTab> tabs = new ArrayList<ILaunchConfigurationTab>();
		List<ILaunchConfigurationTab> requiredTabs = getTabs(mode);
		if ((requiredTabs!= null) && (requiredTabs.size() > 0)) {
			tabs.addAll(requiredTabs);
		}
		List<AbstractLaunchConfigurationTab> extensionUiTabs = new ArrayList<AbstractLaunchConfigurationTab>();
		for (WorkflowExtension workflowExtension : ExtensionHelper.getWorkflowExtensionsSortedByPriority(getWorkflowId())) {
			if (workflowExtension.getLaunchConfigurationTab() != null) {
				AbstractLaunchConfigurationTab extensionTab = workflowExtension.getLaunchConfigurationTab();
				extensionUiTabs.add(extensionTab);
			}
		}
		if (!extensionUiTabs.isEmpty()) {
			tabs.addAll(extensionUiTabs);
		}
		ILaunchConfigurationTab commonTab = new DebugEnabledCommonTab();
		tabs.add(commonTab);

		setTabs(tabs.toArray(new ILaunchConfigurationTab[] {}));
	}
	
	protected abstract List<ILaunchConfigurationTab> getTabs(String mode);
	
	protected abstract String getWorkflowId();

}
