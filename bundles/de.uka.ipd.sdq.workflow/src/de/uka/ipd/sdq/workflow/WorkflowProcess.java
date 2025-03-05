package de.uka.ipd.sdq.workflow;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;

import de.uka.ipd.sdq.workflow.logging.console.Log4JBasedStreamsProxy;
import de.uka.ipd.sdq.workflow.logging.console.StreamsProxyAppender;

/**
 * An implementation of an IProcess which is able to log to the provided process console, but
 * otherwise unable to terminate, suspend, etc.
 * 
 * Extend this class if you can provide additional control over the running process.
 * 
 * @author Steffen Becker
 */
public class WorkflowProcess extends PlatformObject implements IProcess {

    /** The my launch. */
    private ILaunch myLaunch;

    /** The is terminated. */
    private boolean isTerminated;

    /** The my streams proxy. */
    private Log4JBasedStreamsProxy myStreamsProxy = null;

    /**
     * Instantiates a new workflow process.
     * 
     * @param myLaunch
     *            the my launch
     */
    public WorkflowProcess(ILaunch myLaunch) {
        super();
        this.myLaunch = myLaunch;
        this.myStreamsProxy = new Log4JBasedStreamsProxy();
    }

    /**
     * Instantiates a new workflow process.
     */
    public WorkflowProcess() {
        super();
        this.myStreamsProxy = new Log4JBasedStreamsProxy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IProcess#getAttribute(java.lang.String)
     */
    @Override
    public String getAttribute(String key) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IProcess#getExitValue()
     */
    @Override
    public int getExitValue() throws DebugException {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IProcess#getLabel()
     */
    @Override
    public String getLabel() {
        return "Simulation Process";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IProcess#getLaunch()
     */
    @Override
    public ILaunch getLaunch() {
        return this.myLaunch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IProcess#getStreamsProxy()
     */
    @Override
    public IStreamsProxy getStreamsProxy() {
        return myStreamsProxy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.IProcess#setAttribute(java.lang.String, java.lang.String)
     */
    @Override
    public void setAttribute(String key, String value) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.PlatformObject#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
     */
    @Override
    public boolean canTerminate() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        return this.isTerminated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.model.ITerminate#terminate()
     */
    @Override
    public void terminate() throws DebugException {
        this.isTerminated = true;

        this.myStreamsProxy.dispose();

        DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[] { new DebugEvent(this, DebugEvent.TERMINATE) });
    }

    /**
     * Adds the appender.
     * 
     * @param appender
     *            the appender
     */
    public void addAppender(StreamsProxyAppender appender) {
        this.myStreamsProxy.addAppender(appender);
    }

}
