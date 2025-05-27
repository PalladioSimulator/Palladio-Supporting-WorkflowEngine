package de.uka.ipd.sdq.workflow.mdsd.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PlatformUI;

import de.uka.ipd.sdq.errorhandling.core.SeverityAndIssue;
import de.uka.ipd.sdq.errorhandling.dialogs.issues.DisplayIssuesDialog;
import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration;

/**
 * The Class ShowValidationErrorsJob.
 *
 * @author Steffen Becker
 */
public class ShowValidationErrorsJob implements IJob {

    /** The validation jobs. */
    private final ModelValidationJob[] validationJobs;

    /** The Constant logger. */
    private final Logger logger = Logger.getLogger(ShowValidationErrorsJob.class);

    /** The configuration. */
    private final AbstractWorkflowBasedRunConfiguration configuration;

    /**
     * Instantiates a new show validation errors job.
     * 
     * @param configuration
     *            the configuration
     * @param validationJobs
     *            the validation jobs
     */
    public ShowValidationErrorsJob(final AbstractWorkflowBasedRunConfiguration configuration,
            final ModelValidationJob... validationJobs) {
        super();
        this.validationJobs = validationJobs;
        this.configuration = configuration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        final List<SeverityAndIssue> result = new ArrayList<SeverityAndIssue>();
        for (final ModelValidationJob validationJob : this.validationJobs) {
            result.addAll(validationJob.getResult());
        }

        if (result.size() > 0) {
            if (this.logger.isEnabledFor(Level.WARN)) {
                this.logger.warn("Found validation problems in the models");
            }
            this.displayValidationErrors(result);
            if (this.logger.isEnabledFor(Level.WARN)) {
                this.logger.warn("Continuing workflow, ignoring model validation issues");
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        return "Show validation errors";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void cleanup(final IProgressMonitor monitor) throws CleanupFailedException {
        // Not needed
    }

    /**
     * Display validation errors.
     * 
     * @param overallResult
     *            the overall result
     * @throws UserCanceledException
     *             the user canceled exception
     */
    private void displayValidationErrors(final List<SeverityAndIssue> overallResult) throws UserCanceledException {
        final DisplayIssuesDialog runner = new DisplayIssuesDialog(overallResult);

        /**
         * Disable the IssuesDialog, if SimuComConfig.SHOULD_THROW_EXCEPTION set of false
         */
        if (this.configuration.isInteractive()) {
            PlatformUI.getWorkbench().getDisplay().syncExec(runner);
            if (!runner.shouldProceedAfterErrorDialog()) {
                throw new UserCanceledException();
            }
        }
    }
}
