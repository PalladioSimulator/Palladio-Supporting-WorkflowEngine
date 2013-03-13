package de.uka.ipd.sdq.workflow.mdsd.xtext;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * SequentialJob for MWE2 WorkflowComponents.
 * 
 * @author Joerg Henss
 * 
 */
public class MWE2SequentialJob extends SequentialJob {

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.SequentialJob#execute(org.eclipse.core.runtime.
     * IProgressMonitor)
     */
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        for (IJob job : this.myJobs) {
            if (job instanceof IPrePostJob) {
                ((IPrePostJob) job).preExecute();
            }
        }
        super.execute(monitor);

        for (IJob job : this.myJobs) {
            if (job instanceof IPrePostJob) {
                ((IPrePostJob) job).postExecute();
            }
        }

    }

}
