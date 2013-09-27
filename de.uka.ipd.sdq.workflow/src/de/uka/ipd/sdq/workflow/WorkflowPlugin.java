package de.uka.ipd.sdq.workflow;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class WorkflowPlugin extends Plugin {

    /** The Constant PLUGIN_ID. */
    public static final String PLUGIN_ID = "de.uka.ipd.sdq.codegen.workflow";

    /** The plugin. (The shared instance) */
    private static WorkflowPlugin plugin;

    /**
     * Instantiates a new workflow plugin.
     */
    public WorkflowPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
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
    public static WorkflowPlugin getDefault() {
        return plugin;
    }

    /**
     * Log.
     * 
     * @param severity
     *            the severity
     * @param message
     *            the message
     */
    public static void log(int severity, String message) {
        plugin.getLog().log(new Status(severity, PLUGIN_ID, message));
    }

}
