package de.uka.ipd.sdq.launchmultiple;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "de.uka.ipd.sdq.launchmultiple";

	/** The plugin. */
	private static Activator plugin;
	
	/**
     * The constructor.
     */
	public Activator() {
	}

	/**
	 * Start the plugin in a given context.
	 * 
	 * @param context The context the plugin runs in.
	 * @throws Exception Identifies that the plugin could not be started as intended.
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

    /**
     * Stop the plugin in a given context.
     * 
     * @param context The context the plugin runs in.
     * @throws Exception Identifies that the plugin could not be stopped as intended.
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
     * Returns the shared instance.
     * 
     * @return the shared instance
     */
	public static Activator getDefault() {
		return plugin;
	}

}
