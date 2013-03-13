package de.uka.ipd.sdq.workflow.mocks;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * The Class FailingJob.
 */
public class FailingJob extends MockJob {

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.mocks.MockJob#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        super.execute(monitor);
        throw new JobFailedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.mocks.MockJob#getName()
     */
    @Override
    public String getName() {
        super.getName();
        return "FailingJob";
    }
}
