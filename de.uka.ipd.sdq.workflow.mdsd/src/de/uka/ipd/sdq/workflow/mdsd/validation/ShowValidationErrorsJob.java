package de.uka.ipd.sdq.workflow.mdsd.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PlatformUI;

import de.uka.ipd.sdq.errorhandling.SeverityAndIssue;
import de.uka.ipd.sdq.errorhandling.dialogs.issues.DisplayIssuesDialog;
import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration;

public class ShowValidationErrorsJob
implements IJob {

	private final ModelValidationJob[] validationJobs;
	private static final Logger logger = Logger.getLogger(ShowValidationErrorsJob.class);
	private final AbstractWorkflowBasedRunConfiguration configuration;

	public ShowValidationErrorsJob(AbstractWorkflowBasedRunConfiguration configuration, ModelValidationJob...validationJobs) {
		super();
		this.validationJobs = validationJobs;
		this.configuration = configuration;
	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		List<SeverityAndIssue> result = new ArrayList<SeverityAndIssue>();
		for (ModelValidationJob validationJob : validationJobs) {
			result.addAll(validationJob.getResult());
		}

		if (result.size() > 0) {
			logger.warn("Found validation problems in the models");
			displayValidationErrors(result);
			logger.warn("Continuing workflow, ignoring model validation issues");
		}

	}

	@Override
	public String getName() {
		return "Show validation errors";
	}

	@Override
	public void rollback(IProgressMonitor monitor)
			throws RollbackFailedException {
		// Not needed
	}

	private void displayValidationErrors(
			List<SeverityAndIssue> overallResult)
			throws UserCanceledException {
		DisplayIssuesDialog runner = new DisplayIssuesDialog(overallResult);

		/**
		 * Disable the IssuesDialog, if SimuComConfig.SHOULD_THROW_EXCEPTION set
		 * of false
		 */
		if (configuration.isInteractive()) {
			PlatformUI.getWorkbench().getDisplay().syncExec(runner);
			if (!runner.shouldProceedAfterErrorDialog())
				throw new UserCanceledException();
		}
	}
}
