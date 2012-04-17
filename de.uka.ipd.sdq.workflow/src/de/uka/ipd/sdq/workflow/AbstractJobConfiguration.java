package de.uka.ipd.sdq.workflow;

import de.uka.ipd.sdq.workflow.exceptions.InvalidWorkflowJobConfiguration;

/**
 * The Class AbstractJobConfiguration.
 */
public abstract class AbstractJobConfiguration implements IJobConfiguration, Cloneable {

    /**
     * Boolean flag indicating that this configuration object is immutable.
     */
    protected boolean isFixed = false;

    /**
     * Constructor of an abstract job configuration.
     */
    public AbstractJobConfiguration() {
        super();
    }

    /**
     * The last method to call by any builder or code creating an configuration for the workflow
     * engine. The configuration will be checked and if the check is successful it cannot be changed
     * any longer.
     * 
     * If check fails it throws an Exception.
     *
     * @throws InvalidWorkflowJobConfiguration the invalid workflow job configuration
     */
    public void validateAndFreeze() throws InvalidWorkflowJobConfiguration {
        if (!this.isValid())
            throw new InvalidWorkflowJobConfiguration(this.getErrorMessage());
        this.isFixed = true;
    }

    /**
     * Get the status of this configuration. If it is frozen, it cannot be changed any longer and
     * hence is ready to be passed to the workflow engine.
     * 
     * @return Whether the configuration can still be changed
     */
    public boolean isFrozen() {
        return this.isFixed;
    }

    /**
     * Used in setter methods to check whether the object can still be modified.
     */
    protected void checkFixed() {
        if (this.isFixed) {
            throw new UnsupportedOperationException("Configuration cannot be changed any longer after fixing it");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJobConfiguration#getErrorMessage()
     */
    abstract public String getErrorMessage();

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJobConfiguration#isValid()
     */
    public boolean isValid() {
        return getErrorMessage() == null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJobConfiguration#setDefaults()
     */
    public abstract void setDefaults();

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        AbstractJobConfiguration config = (AbstractJobConfiguration) super.clone();
        config.isFixed = this.isFixed;
        return config;
    }
}