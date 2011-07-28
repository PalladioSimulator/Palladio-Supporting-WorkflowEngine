package de.uka.ipd.sdq.workflow.pcm.runconfig;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;

public abstract class AbstractPCMLaunchConfigurationTab extends AbstractLaunchConfigurationTab {
	
	protected PCMLaunchConfigurationBlackboard blackboard = null;
	
	public void setBlackboard(PCMLaunchConfigurationBlackboard blackboard) {
		this.blackboard = blackboard;
	}

}
