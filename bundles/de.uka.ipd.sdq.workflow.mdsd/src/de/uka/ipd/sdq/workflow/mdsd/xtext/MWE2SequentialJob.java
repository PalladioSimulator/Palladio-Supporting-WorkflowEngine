package de.uka.ipd.sdq.workflow.mdsd.xtext;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * SequentialJob for MWE2 WorkflowComponents.
 *
 * It executes xtext specific pre and post jobs for model transformation setup and tear down.
 *
 * @author Joerg Henss
 *
 */
public class MWE2SequentialJob extends SequentialJob {

    /**
     * Default model workflow job constructor.
     */
    public MWE2SequentialJob() {
        super();
    }

    /**
     * Instantiates a new model workflow job with a specific name.
     * 
     * @param name
     *            The name of the job.
     */
    public MWE2SequentialJob(final String name) {
        super(name);
    }

    /**
     * Instantiates a new model workflow job, specifying the clean up strategy.
     * 
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     */
    public MWE2SequentialJob(final boolean cleanUpImmediately) {
        super(cleanUpImmediately);
    }

    /**
     * Instantiates a new model workflow job with a specific name and specifying the clean up
     * strategy.
     * 
     * @param name
     *            The name of the job sequence.
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     */
    public MWE2SequentialJob(final String name, final boolean cleanUpImmediately) {
        super(name, cleanUpImmediately);
    }

    /**
     * Execute a mdsd job sequence with the following steps:
     * <ul>
     * <li>Run the pre jobs.</li>
     * <li>Execute the real jobs.</li>
     * <li>Run the post jobs.</li>
     * </ul>
     *
     * {@inheritDoc}
     */
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        for (final IJob job : this.myJobs) {
            if (job instanceof IPrePostJob) {
                ((IPrePostJob) job).preExecute();
            }
        }
        super.execute(monitor);

        for (final IJob job : this.myJobs) {
            if (job instanceof IPrePostJob) {
                ((IPrePostJob) job).postExecute();
            }
        }

    }

}
