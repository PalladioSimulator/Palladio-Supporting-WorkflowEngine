package de.uka.ipd.sdq.workflow.extension;

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

/**
 * Helper class to access installed work-flow extensions.
 * 
 * @author Benjamin Klatt
 */
public class ExtensionHelper {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(ExtensionHelper.class);

	/** The Constant WorkflowExtensionPointId. */
	private static final String WORKFLOW_EXTENSION_POINT_ID = "de.uka.ipd.sdq.workflow.job";

	/** The Constant WorkflowExtensionPointAttribute_ExtensionJob. */
	private static final String WORKFLOW_EXTENSION_POINT_ATTRIBUTE_EXTENSION_JOB = "extension_job";

	/**
	 * The configuration builder extension point attribute id.
	 */
	private static final String WORKFLOW_EXTENSION_POINT_ATTRIBUTE_EXTENSION_CONFIGURATION_BUILDER = "extension_configurationbuilder";

	/** The Constant WorkflowExtensionPointAttribute_Priority. */
	private static final String WORKFLOW_EXTENSION_POINT_ATTRIBUTE_PRIORITY = "priority";

	/** The Constant WorkflowExtensionPointAttribute_WorkflowId. */
	private static final String WORKFLOW_EXTENSION_POINT_ATTRIBUTE_WORKFLOW_ID = "workflow_id";

	/**
	 * Get a list of work-flow extensions registered for a specific work-flow.
	 * This method also orders the work-flow extensions by their id.
	 * 
	 * @param workflowId
	 *            The id of the work flow to get extensions for.
	 * @return the workflow extensions sorted by priority
	 */
	@SuppressWarnings("rawtypes")
	public static List<WorkflowExtension> getWorkflowExtensionsSortedByPriority(
			String workflowId) {
		List<WorkflowExtension> extensions = getWorkflowExtensions(workflowId);
		Collections.sort(extensions, new Comparator<WorkflowExtension>() {
			@Override
            public int compare(WorkflowExtension workflowExtensionA,
					WorkflowExtension workflowExtensionB) {
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
	 * Build the list of extensions registered for a specific work-flow.
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<WorkflowExtension> getWorkflowExtensions(
			String workflowId) {
		List<WorkflowExtension> extensions = new ArrayList<WorkflowExtension>();
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
				String workflId = element
						.getAttribute(WORKFLOW_EXTENSION_POINT_ATTRIBUTE_WORKFLOW_ID);
				if ((workflId != null) && workflId.equals(workflowId)) {
					WorkflowExtension extension = new WorkflowExtension(
							registeredExtension.getUniqueIdentifier(),
							workflowId);
					try {
						Object o = element
								.createExecutableExtension(WORKFLOW_EXTENSION_POINT_ATTRIBUTE_EXTENSION_JOB);
						if ((o != null)
								&& (o instanceof AbstractWorkflowExtensionJob)) {
							extension
									.setWorkflowExtensionJob((AbstractWorkflowExtensionJob<?>) o);
						}
					} catch (CoreException e) {
						// No workflow extension job specified
						LOGGER.error("Unable to load extension job");
					}
					try {
						String builderAttribute = element.getAttribute(WORKFLOW_EXTENSION_POINT_ATTRIBUTE_EXTENSION_CONFIGURATION_BUILDER);
						if(builderAttribute != null && !builderAttribute.isEmpty()) {
							Object o = element
									.createExecutableExtension(WORKFLOW_EXTENSION_POINT_ATTRIBUTE_EXTENSION_CONFIGURATION_BUILDER);
							if ((o != null)
									&& (o instanceof AbstractWorkflowExtensionConfigurationBuilder)) {
								extension
										.setExtensionConfigurationBuilder((AbstractWorkflowExtensionConfigurationBuilder) o);
							}
						}
					} catch (CoreException e) {
						// No extension configuration builder specified
						LOGGER.error("Failed to load extension job configuration builder");
					}
					String priorityString = element
							.getAttribute(WORKFLOW_EXTENSION_POINT_ATTRIBUTE_PRIORITY);
					if (priorityString != null) {
						try {
							extension.setPriority(Integer
									.parseInt(priorityString));
						} catch (NumberFormatException e) {
							// No extension configuration builder specified
							LOGGER.error("Failed to load process extension job priority.");
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
