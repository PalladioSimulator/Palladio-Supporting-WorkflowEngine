package de.uka.ipd.sdq.workflow.launchconfig.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
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
public class WorkflowConfigurationTabExtensionHelper {

	/** The logger. */
	private static Logger logger = Logger
			.getLogger(WorkflowConfigurationTabExtensionHelper.class);

	/** The Constant WorkflowExtensionPointId. */
	private static final String WORKFLOW_EXTENSION_POINT_ID = "de.uka.ipd.sdq.workflow.tab.extension";

	/** The Constant WorkflowExtensionPointAttribute_RunconfigTab. */
	private static final String WORKFLOW_EXTENSION_POINT_ATTRIBUTE_RUNCONFIG_TAB = "runconfig_tab";

	/** The Constant WorkflowExtensionPointAttribute_Priority. */
	private static final String WORKFLOW_EXTENSION_POINT_ATTRIBUTE_PRIORITY = "priority";

	/**
	 * Get a list of work-flow extensions registered for a specific work-flow.
	 * This method also orders the work-flow extensions by their id.
	 * 
	 * @param workflowId
	 *            The id of the work flow to get extensions for.
	 * @return the workflow extensions sorted by priority
	 */
	@SuppressWarnings("rawtypes")
	public static List<WorkflowConfigurationTabExtension> getWorkflowExtensionsSortedByPriority(
			String workflowId) {
		List<WorkflowConfigurationTabExtension> extensions = getWorkflowExtensions(workflowId);
		Collections.sort(extensions,
				new Comparator<WorkflowConfigurationTabExtension>() {
					public int compare(
							WorkflowConfigurationTabExtension workflowExtensionA,
							WorkflowConfigurationTabExtension workflowExtensionB) {
						if (workflowExtensionA.getPriority() < workflowExtensionB
								.getPriority()) {
							return -1;
						} else if (workflowExtensionA.getPriority() == workflowExtensionB
								.getPriority()) {
							return 0;
						} else {
							return 1;
						}
					}
				});
		return extensions;
	}

	/**
	 * Build the list of configuration tab extensions registered for a 
	 * specific work-flow.
	 * 
	 * This method identifies plug-ins extending the work flow extension point
	 * and have configured to be applicable for a specific work-flow by its id.
	 * 
	 * When identified the extensions, it instantiates their job, triggers their
	 * configuration builder and injects the custom configuration instance into
	 * the new job.
	 * 
	 * @param workflowId
	 *            The id to get the registered extensions for.
	 * @return The list of identified work-flow extensions.
	 */
	@SuppressWarnings("rawtypes")
	public static List<WorkflowConfigurationTabExtension> getWorkflowExtensions(
			String workflowId) {
		List<WorkflowConfigurationTabExtension> extensions = new ArrayList<WorkflowConfigurationTabExtension>();
		IExtension[] registeredExtensions = getRegisteredWorkflowExtensions();
		if (registeredExtensions == null) {
			// No defined extensions found!
			return extensions;
		}
		for (int i = 0; i < registeredExtensions.length; i++) {
			IExtension registeredExtension = registeredExtensions[i];
			IConfigurationElement[] elements = registeredExtension
					.getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				IConfigurationElement element = elements[j];
				WorkflowConfigurationTabExtension extension = new WorkflowConfigurationTabExtension(
						registeredExtension.getUniqueIdentifier(), workflowId);
				try {
					Object o = element
							.createExecutableExtension(WORKFLOW_EXTENSION_POINT_ATTRIBUTE_RUNCONFIG_TAB);
					if ((o != null)
							&& (o instanceof AbstractLaunchConfigurationTab)) {
						extension
								.setLaunchConfigurationTab((AbstractLaunchConfigurationTab) o);
					}
				} catch (CoreException e) {
					logger.error("Unable to load config tab extension", e);
					continue;
				}
				String priorityString = element
						.getAttribute(WORKFLOW_EXTENSION_POINT_ATTRIBUTE_PRIORITY);
				if (priorityString != null) {
					try {
						extension.setPriority(Integer.parseInt(priorityString));
					} catch (NumberFormatException e) {
						logger.error(
								"Unable to load config tab extension priority",
								e);
						continue;
					}
				}

				extensions.add(extension);
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
		if (registry == null) {
			// No registry available
			return null;
		}
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint(WORKFLOW_EXTENSION_POINT_ID);
		if (extensionPoint == null) {
			// No extension point found!
			return null;
		}
		IExtension[] extensions = extensionPoint.getExtensions();
		return extensions;
	}

}
