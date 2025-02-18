/** */
package de.uka.ipd.sdq.workflow.launchconfig.core.configbuilder;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

import de.uka.ipd.sdq.workflow.launchconfig.core.AbstractWorkflowBasedRunConfiguration;

/**
 * Bridge for workflow engine-based configurations of type
 * {@link AbstractWorkflowBasedRunConfiguration} and eclipse launch configurations.
 *
 * Reads a workflow engine configuration and fills an eclipse launch configuration.
 */
public abstract class AbstractWorkflowConfigurationBuilder {

    /** Eclipse launch configuration. */
    protected ILaunchConfiguration configuration;

    /** The launch mode. */
    protected String mode;

    /** debug mode. */
    protected boolean isDebug;

    /** Mapped properties / attributes. */
    protected Map<String, Object> properties;

    /**
     * Instantiates a new abstract workflow configuration builder.
     *
     * @param configuration
     *            the configuration
     * @param mode
     *            the mode
     * @throws CoreException
     *             the core exception
     */
    public AbstractWorkflowConfigurationBuilder(final ILaunchConfiguration configuration, final String mode)
            throws CoreException {
        super();
        this.configuration = configuration;
        this.mode = mode;
        this.isDebug = mode.equals(ILaunchManager.DEBUG_MODE);
        this.properties = configuration.getAttributes();
    }

    /**
     * Fill configuration.
     *
     * @param configuration
     *            the configuration
     * @throws CoreException
     *             the core exception
     */
    public abstract void fillConfiguration(AbstractWorkflowBasedRunConfiguration configuration) throws CoreException;

    /**
     * Checks for attribute.
     *
     * @param attribute
     *            the attribute
     * @return true, if successful
     * @throws CoreException
     *             the core exception
     */
    protected boolean hasAttribute(final String attribute) throws CoreException {
        return configuration.hasAttribute(attribute);
    }

    /**
     * Checks for string attribute.
     *
     * @param attribute
     *            the attribute
     * @return true, if successful
     * @throws CoreException
     *             the core exception
     */
    protected boolean hasStringAttribute(final String attribute) throws CoreException {
        if (!configuration.hasAttribute(attribute)) {
            return false;
        } else {
            final Object value = getStringAttribute(attribute);
            return (value instanceof String && !value.equals(""));
        }
    }

    /**
     * Gets the string attribute.
     *
     * @param attribute
     *            the attribute
     * @return the string attribute
     * @throws CoreException
     *             the core exception
     */
    protected String getStringAttribute(final String attribute) throws CoreException {
        ensureAttributeExists(attribute);
        final Object value = configuration.getAttribute(attribute, "");
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Tried to read non-string value as string value");
        } else {
            return value.toString();
        }
    }

    /**
     * Gets the double attribute.
     *
     * @param attribute
     *            the attribute
     * @return the double attribute
     * @throws CoreException
     *             the core exception
     */
    protected double getDoubleAttribute(final String attribute) throws CoreException {
        ensureAttributeExists(attribute);
        final Object value = configuration.getAttribute(attribute, "");
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Tried to read non-double value as double value");
        }
        return Double.parseDouble(value.toString());
    }

    /**
     * Gets the integer attribute.
     *
     * @param attribute
     *            the attribute
     * @return the integer attribute
     * @throws CoreException
     *             the core exception
     */
    protected int getIntegerAttribute(final String attribute) throws CoreException {
        ensureAttributeExists(attribute);
        return configuration.getAttribute(attribute, 0);
    }

    /**
     * Gets the long attribute.
     *
     * @param attribute
     *            the attribute
     * @return the long attribute
     * @throws CoreException
     *             the core exception
     */
    protected long getLongAttribute(final String attribute) throws CoreException {
        ensureAttributeExists(attribute);
        return configuration.getAttribute(attribute, 0);
    }

    /**
     * Gets the boolean attribute.
     *
     * @param attribute
     *            the attribute
     * @return the boolean attribute
     * @throws CoreException
     *             the core exception
     */
    protected Boolean getBooleanAttribute(final String attribute) throws CoreException {
        if (!hasAttribute(attribute)) {
            return false;
        }

        final Object value = configuration.getAttribute(attribute, false);
        if (!(value instanceof Boolean)) {
            throw new IllegalArgumentException("Tried to read non-boolean value as boolean value");
        }

        return (Boolean) value;
    }

    /**
     * Ensure attribute exists.
     *
     * @param attribute
     *            the attribute
     * @throws CoreException
     *             the core exception
     */
    private void ensureAttributeExists(final String attribute) throws CoreException {
        if (!configuration.hasAttribute(attribute)) {
            throw new IllegalStateException("Tried to read non-existing configuration attribute."
                    + " If you have recently updated your Eclipse installation,"
                    + " please visit every tab of your launch configuration to make"
                    + " sure that newly added configuration entries are added to the"
                    + " internally stored launch configuration.");
        }
    }

}
