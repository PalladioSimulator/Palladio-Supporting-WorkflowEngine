package de.uka.ipd.sdq.workflow.mdsd.validation;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.errorhandling.core.SeverityAndIssue;
import de.uka.ipd.sdq.errorhandling.core.SeverityEnum;
import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

/**
 * The Class ModelValidationJob.
 */
public abstract class ModelValidationJob implements IBlackboardInteractingJob<MDSDBlackboard> {

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
    public ModelValidationJob(final SeverityEnum errorLevel) {
        super();
        this.errorLevel = errorLevel;
    }

    /**
     * Gets the error level.
     *
     * @return the errorLevel
     */
    public SeverityEnum getErrorLevel() {
        return this.errorLevel;
    }

    /**
     * Sets the job result.
     *
     * @param jobResult
     *            the jobResult to set
     */
    protected void setJobResult(final List<SeverityAndIssue> jobResult) {
        if (jobResult == null) {
            throw new IllegalArgumentException("Error list must not be null.");
        }
        this.jobResult = jobResult;
    }

    /**
     * Returns the job result.
     * 
     * @return The list of issues tracked for the job.
     */
    public List<SeverityAndIssue> getResult() {
        if (this.jobResult == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.jobResult);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void cleanup(final IProgressMonitor monitor) throws CleanupFailedException {
        // Not needed
    }
}
