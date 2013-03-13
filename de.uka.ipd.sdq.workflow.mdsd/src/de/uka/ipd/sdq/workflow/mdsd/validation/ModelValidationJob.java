package de.uka.ipd.sdq.workflow.mdsd.validation;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.errorhandling.SeverityAndIssue;
import de.uka.ipd.sdq.errorhandling.SeverityEnum;
import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.IJobWithResult;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

/**
 * The Class ModelValidationJob.
 */
public abstract class ModelValidationJob implements IJobWithResult<List<SeverityAndIssue>>,
        IBlackboardInteractingJob<MDSDBlackboard> {

    /** The job result. */
    private List<SeverityAndIssue> jobResult = null;

    /** The error level. */
    private final SeverityEnum errorLevel;

    /**
     * Instantiates a new model validation job.
     * 
     * @param errorLevel
     *            the error level
     */
    public ModelValidationJob(SeverityEnum errorLevel) {
        super();
        this.errorLevel = errorLevel;
    }

    /**
     * Gets the error level.
     * 
     * @return the errorLevel
     */
    public SeverityEnum getErrorLevel() {
        return errorLevel;
    }

    /**
     * Sets the job result.
     * 
     * @param jobResult
     *            the jobResult to set
     */
    protected void setJobResult(List<SeverityAndIssue> jobResult) {
        if (jobResult == null) {
            throw new IllegalArgumentException("Error list must not be null.");
        }
        this.jobResult = jobResult;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJobWithResult#getResult()
     */
    @Override
    public List<SeverityAndIssue> getResult() {
        if (this.jobResult == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(jobResult);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
        // Not needed
    }
}
