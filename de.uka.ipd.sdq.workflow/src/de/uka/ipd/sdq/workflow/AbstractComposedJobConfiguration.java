package de.uka.ipd.sdq.workflow;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * Job configuration which is composed from several child job configurations.
 * 
 * @author Steffen Becker
 *
 */
public class AbstractComposedJobConfiguration 
extends AbstractJobConfiguration
implements IJobConfiguration {
	
	/** The child configurations. */
	private Map<Integer, AbstractJobConfiguration> childConfigurations = new HashMap<Integer, AbstractJobConfiguration>();
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.workflow.AbstractJobConfiguration#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		String errorMessage = "";
		for (AbstractJobConfiguration config : childConfigurations.values()) {
			if (config.getErrorMessage() != null) {
				errorMessage += config.getErrorMessage();
			}
		}
		return errorMessage.length() == 0 ? null : errorMessage;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.workflow.AbstractJobConfiguration#isValid()
	 */
	@Override
	public boolean isValid() {
		return getErrorMessage() == null;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.workflow.AbstractJobConfiguration#setDefaults()
	 */
	@Override
	public void setDefaults() {
		for (AbstractJobConfiguration config : childConfigurations.values()) {
			config.setDefaults();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (AbstractJobConfiguration config : childConfigurations.values()) {
			builder.append(config.toString() + "\n");
		}
		return builder.toString();
	}

	/**
	 * Adds the child configuration.
	 *
	 * @param id the id
	 * @param childConfig the child config
	 */
	protected void addChildConfiguration(int id, AbstractJobConfiguration childConfig) {
		childConfigurations.put(id, childConfig);
	}

	/**
	 * Gets the child configuration.
	 *
	 * @param id the id
	 * @return the child configuration
	 */
	protected AbstractJobConfiguration getChildConfiguration(int id) {
		return childConfigurations.get(id);
	}
}
