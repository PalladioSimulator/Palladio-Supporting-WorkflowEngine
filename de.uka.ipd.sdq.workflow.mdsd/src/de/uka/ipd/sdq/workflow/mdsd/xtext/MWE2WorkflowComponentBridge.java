package de.uka.ipd.sdq.workflow.mdsd.xtext;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowComponent;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;

import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;

/**
 * A bridge allowing the simple execution of MWE2 WorkflowComponents.
 * 
 * @param <T>
 *            the generic type
 * @author Joerg Henss
 */
public class MWE2WorkflowComponentBridge<T extends IWorkflowComponent>

implements IPrePostJob {

    /** The logger. */
    private Logger logger = Logger.getLogger(MWE2WorkflowComponentBridge.class);

    /** The mwe2 job. */
    protected T mwe2Job = null;

    /** The ctx. */
    private final IWorkflowContext ctx;

    /** The name. */
    private final String name;

    /**
     * Constructor of the oAW bridge.
     * 
     * @param job
     *            The oAW job to wrap for execution in the SDQ workflow engine
     * @param ctx
     *            the ctx
     * @param name
     *            the name
     */
    public MWE2WorkflowComponentBridge(T job, IWorkflowContext ctx, String name) {
        super();
        this.mwe2Job = job;
        this.ctx = ctx;
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.mdsd.xtext.IPrePostJob#preExecute()
     */
    @Override
    public void preExecute() {
        logger.debug("Running preInvoke....");
        mwe2Job.preInvoke();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.mdsd.xtext.IPrePostJob#postExecute()
     */
    @Override
    public void postExecute() {
        logger.debug("Running postInvoke....");
        mwe2Job.postInvoke();
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        try {
            logger.debug("Running MWE2 Workflow task....");
            mwe2Job.invoke(ctx);
        } catch (Exception e) {
            throw new JobFailedException("MWE2 workflow component failed", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uka.ipd.sdq.workflow.IJob#rollback(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void rollback(IProgressMonitor monitor) throws RollbackFailedException {
    }

}
