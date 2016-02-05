package de.uka.ipd.sdq.workflow.mdsd.oaw;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.WorkflowContextDefaultImpl;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.issues.IssuesImpl;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitorAdapter;
import org.eclipse.xtend.expression.AbstractExpressionsUsingWorkflowComponent;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * This class allows the use of jobs of the openArchitectureWare workflow engine as part of the SDQ
 * workflow engine.
 *
 * @param <T>
 *            The class of the oAW job bridged by an specific instance of this class
 * @author Steffen Becker
 */
public abstract class AbstractOAWWorkflowJobBridge<T extends AbstractExpressionsUsingWorkflowComponent>

        implements IJob {

    /** The logger. */
    private final Logger logger = Logger.getLogger(AbstractOAWWorkflowJobBridge.class);

    /** The oaw job. */
    protected T oawJob = null;

    /** The slot contents. */
    private HashMap<String, Object> slotContents;

    /**
     * Constructor of the oAW bridge.
     * 
     * @param job
     *            The oAW job to wrap for execution in the Palladio workflow engine
     */
    public AbstractOAWWorkflowJobBridge(final T job) {
        this(job, new HashMap<String, Object>());
    }

    /**
     * Constructor of the oAW bridge.
     * 
     * @param job
     *            The oAW job to wrap for execution in the Palladio workflow engine
     * @param slotContents
     *            Contains models of the oAW workflow engine's blackboard, i.e., models to be
     *            transformed by the encapsulated oAW job
     */
    public AbstractOAWWorkflowJobBridge(final T job, final HashMap<String, Object> slotContents) {
        super();

        this.oawJob = job;
        this.slotContents = slotContents;
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        final Issues issues = new IssuesImpl();
        final WorkflowContext ctx = new WorkflowContextDefaultImpl();
        this.setupContext(ctx);

        try {
            this.setupOAWJob(this.oawJob);

            this.logger.debug("Validating oAW configuration settings...");
            this.oawJob.checkConfiguration(issues);
            if (issues.hasErrors()) {
                final String message = issues.toString();
                throw new JobFailedException("oAW Job configuration is invalid: " + message);
            }

            this.logger.debug("Running oAW task....");
            this.oawJob.invoke(ctx, new ProgressMonitorAdapter(monitor), issues);
            if (issues.hasErrors()) {
                throw new JobFailedException("oAW Job failed");
            }
        } catch (final Exception e) {
            throw new JobFailedException("oAW Failed", e);
        }
    }

    /**
     * Creates the oAW job's {@link WorkflowContext}.
     * 
     * @param ctx
     *            The context to configure. Add all the models needed by the oAW job
     */
    protected void setupContext(final WorkflowContext ctx) {
        for (final String slot : this.slotContents.keySet()) {
            ctx.set(slot, this.slotContents.get(slot));
        }
    }

    /**
     * Template method in which hiers can add logic to configure their specific oAW jobs.
     * 
     * @param oawJob2
     *            The job to be set up
     */
    protected abstract void setupOAWJob(T oawJob2);

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        return this.oawJob.getId() == null ? "oAW Job" : this.oawJob.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void cleanup(final IProgressMonitor monitor) throws CleanupFailedException {
    }

}
