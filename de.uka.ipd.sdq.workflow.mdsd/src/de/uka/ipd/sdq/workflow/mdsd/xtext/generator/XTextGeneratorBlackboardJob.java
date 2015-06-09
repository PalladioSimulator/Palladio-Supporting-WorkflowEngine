package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.emf.mwe2.runtime.workflow.WorkflowContextImpl;
import org.eclipse.xtext.generator.GeneratorComponent;

import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.xtext.IBlackboardInteractingWorkflowComponent;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2BlackboardWorkflowComponentBridge;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2SequentialJob;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2WorkflowComponentBridge;

/**
 * The Class XTextGeneratorBlackboardCompositeJob.
 *
 * @author Jörg Henss
 */
public class XTextGeneratorBlackboardJob extends MWE2SequentialJob implements
IBlackboardInteractingJob<MDSDBlackboard> {

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /**
     * Instantiates a new x text generator blackboard composite job.
     *
     * @param config
     *            the config
     */
    public XTextGeneratorBlackboardJob(final XTextGeneratorConfiguration config) {
        this(config, "XTextGenerator Job", true);
    }

    /**
     * Instantiates a new x text generator blackboard composite job.
     *
     * @param config
     *            the config
     * @param name The name of the job sequence.
     */
    public XTextGeneratorBlackboardJob(final XTextGeneratorConfiguration config, final String name) {
        this(config, name, true);
    }

    /**
     * Instantiates a new x text generator blackboard composite job.
     *
     * @param config
     *            the config
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     */
    public XTextGeneratorBlackboardJob(final XTextGeneratorConfiguration config, final boolean cleanUpImmediately) {
        this(config, "XTextGenerator Job", cleanUpImmediately);
    }

    /**
     * Instantiates a new x text generator blackboard composite job.
     *
     * @param config
     *            the config
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     * @param name The name of the job sequence.
     */
    public XTextGeneratorBlackboardJob(final XTextGeneratorConfiguration config, final String name, final boolean cleanUpImmediately) {

        super(name, cleanUpImmediately);

        // Setup all
        config.initMWEBean();
        // Create context
        final IWorkflowContext ctx = new WorkflowContextImpl();
        config.initGeneratorModuleAndGeneratorSetup();

        this.addJob(new PrepareXTextPartitionJob(config.getBlackboardModelLocation()));

        this.addJob(new MWE2WorkflowComponentBridge<XTextGeneratorSupport>(config.createGeneratorSupport(), ctx,
                "XTextGeneratorSupport Job"));

        this.addJob(new MWE2BlackboardWorkflowComponentBridge<BlackboardReader>(config.createBlackboardReader(), ctx,
                "MWEReader Job"));

        this.addJob(new MWE2WorkflowComponentBridge<GeneratorComponent>(config.createGenerator(), ctx,
                "XTextGenerator Job"));

    }

    /**
     * Executes all contained jobs, i.e. call execute() for them. Contained jobs can thus
     * re-implement this method with functionality that should be executed.
     *
     * @param monitor
     *            the monitor
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        for (final IJob job : this.myJobs) {
            if (job instanceof IBlackboardInteractingJob) {
                ((IBlackboardInteractingJob) job).setBlackboard(this.blackboard);
            } else if (job instanceof IBlackboardInteractingWorkflowComponent) {
                ((IBlackboardInteractingWorkflowComponent) job).setBlackboard(this.blackboard);
            }

        }
        super.execute(monitor);
    }

    /* (non-Javadoc)
     * @see de.uka.ipd.sdq.workflow.IBlackboardInteractingJob#setBlackboard(de.uka.ipd.sdq.workflow.Blackboard)
     */
    @Override
    public void setBlackboard(final MDSDBlackboard blackboard) {
        this.blackboard = blackboard;

    }

}
