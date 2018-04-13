package de.uka.ipd.sdq.workflow.mdsd.emf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;

import de.uka.ipd.sdq.errorhandling.SeverityAndIssue;
import de.uka.ipd.sdq.errorhandling.SeverityEnum;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.validation.ModelValidationJob;

/**
 * A job which checks all model constraints implemented by EMF directly or generated using the EMF
 * OCL extension.
 *
 * @author Steffen Becker
 *
 */
public class CheckEMFConstraintsJob extends ModelValidationJob {

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /** The partition name. */
    private final String partitionName;

    /**
     * Create a new constrains check job.
     *
     * @param errorLevel
     *            the error level
     * @param partitionName
     *            The blackboard partition containing the model to be checked
     */
    public CheckEMFConstraintsJob(final SeverityEnum errorLevel, final String partitionName) {
        super(errorLevel);

        this.partitionName = partitionName;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {

        final ArrayList<SeverityAndIssue> result = new ArrayList<SeverityAndIssue>();
        final ResourceSetPartition partition = this.blackboard.getPartition(this.partitionName);
        partition.resolveAllProxies();

        for (final Resource r : partition.getResourceSet().getResources()) {

            // Check that model is loaded.
            // If not, add an error to the resulting diagnostics regardless of the
            // error level of this CheckEMFConstraintsJob
            final List<EObject> contents = r.getContents();
            if (contents == null || contents.size() == 0) {
                final Diagnostic d = new BasicDiagnostic(Diagnostic.ERROR, this.getClass().getCanonicalName(), 0,
                        "Requested file " + r.getURI()
                                + " cannot be loaded. Make sure it exists and is valid, or fix your model's references.",
                        new Object[] { null });
                this.appendSeverityAndIssueFromDiagnostic(result, d, SeverityEnum.ERROR);
            } else {
                // Check model internal OCL constraints
                final Diagnostician diagnostician = new Diagnostician();
                final Diagnostic d = diagnostician.validate(contents.get(0));
                this.appendSeverityAndIssueFromDiagnostic(result, d);
            }
        }

        this.setJobResult(result);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        return "Check EMF Model Constraints";
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IBlackboardInteractingJob#setBlackboard(de.uka.ipd.sdq.workflow.
     * Blackboard)
     */
    @Override
    public void setBlackboard(final MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }

    /**
     * Append severity and issue from diagnostic.
     *
     * @param result
     *            the result
     * @param d
     *            the d
     * @param severity
     *            the severity
     */
    private void appendSeverityAndIssueFromDiagnostic(final ArrayList<SeverityAndIssue> result, final Diagnostic d,
            final SeverityEnum severity) {
        if (d.getSeverity() >= Diagnostic.ERROR) {
            final SeverityAndIssue sai = new SeverityAndIssue(severity, d.getMessage(), (EObject) d.getData().get(0));
            result.add(sai);
        }
        for (final Diagnostic child : d.getChildren()) {
            this.appendSeverityAndIssueFromDiagnostic(result, child);
        }
    }

    /**
     * Append severity and issue from diagnostic.
     *
     * @param result
     *            the result
     * @param d
     *            the d
     */
    private void appendSeverityAndIssueFromDiagnostic(final ArrayList<SeverityAndIssue> result, final Diagnostic d) {
        this.appendSeverityAndIssueFromDiagnostic(result, d, this.getErrorLevel());
    }
}
