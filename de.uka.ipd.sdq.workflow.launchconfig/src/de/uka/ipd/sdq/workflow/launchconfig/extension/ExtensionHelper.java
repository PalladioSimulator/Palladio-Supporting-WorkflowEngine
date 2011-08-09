package de.uka.ipd.sdq.workflow.launchconfig.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;


public class ExtensionHelper {
	
	private static final String WorkflowExtensionPointId = "de.uka.ipd.sdq.workflow.extension";
	private static final String WorkflowExtensionPointAttribute_RunconfigTab = "runconfig_tab";
	private static final String WorkflowExtensionPointAttribute_ExtensionJob = "extension_job";
	private static final String WorkflowExtensionPointAttribute_ExtensionConfigurationBuilder = "extension_configurationbuilder";
	private static final String WorkflowExtensionPointAttribute_Priority = "priority";
	private static final String WorkflowExtensionPointAttribute_WorkflowId = "workflow_id";
	
	public static List<WorkflowExtension> getWorkflowExtensionsSortedByPriority(String workflowId) {
		List<WorkflowExtension> extensions = getWorkflowExtensions(workflowId);
		Collections.sort(extensions, new Comparator<WorkflowExtension>() {
			public int compare(WorkflowExtension arg0, WorkflowExtension arg1) {
				if (arg0.getPriority() < arg1.getPriority()) {
					return -1;
				} else if (arg0.getPriority() == arg1.getPriority()) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return extensions;
	}
	
	public static List<WorkflowExtension> getWorkflowExtensions(String workflowId) {
		List<WorkflowExtension> extensions = new ArrayList<WorkflowExtension>();
		IExtension[] registeredExtensions = getRegisteredWorkflowExtensions();
		if (registeredExtensions == null) {
			// No defined extensions found!
			return extensions;
		}
		for (int i = 0; i < registeredExtensions.length; i++) {
			IExtension registeredExtension = registeredExtensions[i];
			IConfigurationElement[] elements = registeredExtension.getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				IConfigurationElement element = elements[j];
				String workflId = element.getAttribute(WorkflowExtensionPointAttribute_WorkflowId);
				if ((workflId != null) && workflId.equals(workflowId)) {
					WorkflowExtension extension = new WorkflowExtension(registeredExtension.getUniqueIdentifier(), workflowId);
					try {	
						Object o = element.createExecutableExtension(WorkflowExtensionPointAttribute_ExtensionJob);
						if ((o!= null) && (o instanceof AbstractExtensionJob)) {
							extension.setWorkflowExtensionJob((AbstractExtensionJob)o);
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
					try {	
						Object o = element.createExecutableExtension(WorkflowExtensionPointAttribute_RunconfigTab);
						if ((o!= null) && (o instanceof AbstractLaunchConfigurationTab)) {
							extension.setLaunchConfigurationTab((AbstractLaunchConfigurationTab)o);
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
					try {	
						Object o = element.createExecutableExtension(WorkflowExtensionPointAttribute_ExtensionConfigurationBuilder);
						if ((o!= null) && (o instanceof WorkflowExtensionConfigurationBuilder)) {
							extension.setExtensionConfigurationBuilder((WorkflowExtensionConfigurationBuilder)o);
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
					String priorityString = element.getAttribute(WorkflowExtensionPointAttribute_Priority);
					if (priorityString != null) {
						try {
							extension.setPriority(Integer.parseInt(priorityString)); 
						} catch (NumberFormatException e) {
						}
					}
					
					extensions.add(extension);	
				}
			}
		}
		return extensions;
	}
	
	private static IExtension[] getRegisteredWorkflowExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(WorkflowExtensionPointId);
		if (extensionPoint == null) {
			// No extension point found!
			return null;
		}
		IExtension[] extensions = extensionPoint.getExtensions();
		return extensions;
	}

}
