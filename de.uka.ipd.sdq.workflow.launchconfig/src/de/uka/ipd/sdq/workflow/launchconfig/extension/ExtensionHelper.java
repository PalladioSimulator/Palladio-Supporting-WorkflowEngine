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


/**
 * Helper class to access installed work-flow extensions.
 * 
 * @author Benjamin Klatt
 */
public class ExtensionHelper {
	
	private static final String WorkflowExtensionPointId = "de.uka.ipd.sdq.workflow.extension";
	private static final String WorkflowExtensionPointAttribute_RunconfigTab = "runconfig_tab";
	private static final String WorkflowExtensionPointAttribute_ExtensionJob = "extension_job";
	private static final String WorkflowExtensionPointAttribute_ExtensionConfigurationBuilder = "extension_configurationbuilder";
	private static final String WorkflowExtensionPointAttribute_Priority = "priority";
	private static final String WorkflowExtensionPointAttribute_WorkflowId = "workflow_id";
	
	/**
	 * Get a list of work-flow extensions registered for a specific work-flow. This method
	 * also orders the work-flow extensions by their id.
	 * 
	 * @param workflowId The id of the work flow to get extensions for.
	 * @return
	 */
	public static List<WorkflowExtension> getWorkflowExtensionsSortedByPriority(String workflowId) {
		List<WorkflowExtension> extensions = getWorkflowExtensions(workflowId);
		Collections.sort(extensions, new Comparator<WorkflowExtension>() {
			public int compare(WorkflowExtension workflowExtensionA, WorkflowExtension workflowExtensionB) {
				if (workflowExtensionA.getPriority() < workflowExtensionB.getPriority()) {
					return -1;
				} else if (workflowExtensionA.getPriority() == workflowExtensionB.getPriority()) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return extensions;
	}
	
	/**
	 * Build the list of extensions registered for a specific work-flow.
	 * 
	 * This method identifies plug-ins extending the work flow extension point
	 * and have configured to be applicable for a specific work-flow by its id.
	 * 
	 * When identified the extensions, it instantiates their job, triggers their 
	 * configuration builder and injects the custom configuration instance into the
	 * new job. 
	 * 
	 * @param workflowId The id to get the registered extensions for.
	 * @return The list of identified work-flow extensions.
	 */
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
						if ((o!= null) && (o instanceof AbstractWorkflowExtensionJob)) {
							extension.setWorkflowExtensionJob((AbstractWorkflowExtensionJob<?>)o);
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
						if ((o!= null) && (o instanceof AbstractWorkflowExtensionConfigurationBuilder)) {
							extension.setExtensionConfigurationBuilder((AbstractWorkflowExtensionConfigurationBuilder)o);
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
	
	/**
	 * Get the extensions providing the work-flow extension point.
	 * 
	 * @return The list of installed extensions.
	 */
	private static IExtension[] getRegisteredWorkflowExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if(registry == null){
            // No registry available
		    return null;
		}
		IExtensionPoint extensionPoint = registry.getExtensionPoint(WorkflowExtensionPointId);
		if (extensionPoint == null) {
			// No extension point found!
			return null;
		}
		IExtension[] extensions = extensionPoint.getExtensions();
		return extensions;
	}

}
