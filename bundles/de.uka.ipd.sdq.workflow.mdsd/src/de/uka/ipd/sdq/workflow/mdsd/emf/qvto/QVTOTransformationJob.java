package de.uka.ipd.sdq.workflow.mdsd.emf.qvto;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

import com.google.common.base.Strings;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvto.internal.QVTOExecutor;

/**
 * A job that performs a QVT Operational transformation.
 *
 * @author Michael Hauck
 *
 */
public class QVTOTransformationJob implements IBlackboardInteractingJob<MDSDBlackboard> {

    /** The Constant logger. */
    private final Logger logger = Logger.getLogger(QVTOTransformationJob.class);

    /** The configuration. */
    private final QVTOTransformationJobConfiguration configuration;

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /**
     * Instantiates a new qVTO transformation job.
     * 
     * @param conf
     *            the conf
     */
    public QVTOTransformationJob(final QVTOTransformationJobConfiguration conf) {
        super();

        this.configuration = conf;
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        if (this.logger.isEnabledFor(Level.INFO)) {
            this.logger.info("Executing QVTO Transformation...");
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Script: " + this.configuration.getScriptFileURI());
        }

        final List<EObject>[] parameter = this.getModelContents(); // parameter is used as
        // inout parameter
        final QVTOResult result = QVTOExecutor.execute(this.configuration.getScriptFileURI(),
                this.configuration.getOptions(), parameter);
        if (!result.isSuccess()) {
            if (this.logger.isEnabledFor(Level.ERROR)) {
                this.logger.error("Transformation job failed");
                this.logger.error(getFullDiagnosticMessage(result.getDiagnosticResult()));
            }
            result.logStackTrace(this.logger, Level.ERROR);
            throw new JobFailedException("Transformation execution failed");
        }
        this.storeResultOnBlackboard(parameter);
        if (this.logger.isEnabledFor(Level.INFO)) {
            this.logger.info("Transformation executed successfully");
        }
    }

    /**
     * Store result on blackboard.
     * 
     * @param parameter
     *            the parameter
     */
    private void storeResultOnBlackboard(final List<EObject>[] parameter) {
        for (int i = 0; i < parameter.length; i++) {
            this.blackboard.setContents(this.configuration.getInoutModels()[i], parameter[i]);
        }
    }

    /**
     * Gets the model contents.
     * 
     * @return the model contents
     */
    @SuppressWarnings("unchecked")
    private List<EObject>[] getModelContents() {
        final List<EObject>[] modelContents = new List[this.configuration.getInoutModels().length];

        for (int i = 0; i < this.configuration.getInoutModels().length; i++) {
            if (this.blackboard.modelExists(this.configuration.getInoutModels()[i])) {
                modelContents[i] = this.blackboard.getContents(this.configuration.getInoutModels()[i]);
            } else {
                modelContents[i] = Collections.EMPTY_LIST;
            }
        }

        return modelContents;
    }

	/**
	 * Produces a full diagnostic message consisting of the main and all child
	 * messages.
	 * 
	 * @param diagnostic The diagnostic to convert to a message.
	 * @return Multiline string containing all diagnostic messages.
	 */
    private static String getFullDiagnosticMessage(Diagnostic diagnostic) {
    	String mainStatus = String.format("code %d: %s", diagnostic.getCode(), diagnostic.getMessage());
    	return mainStatus + getFullDiagnosticMessage(diagnostic.getChildren(), 1);
    }

	/**
	 * Recursive method to produce multiline diagnostic messages.
	 * 
	 * The diagnostic messages will be indented by one level compared to its parent.
	 * One line represents one diagnostic message.
	 * 
	 * @param diagnostics The set of diagnostics to be converted.
	 * @param indentation The level of indentation to apply to each serialized line.
	 * @return A multiline string containing the given diagnostic messages.
	 */
    private static String getFullDiagnosticMessage(Iterable<Diagnostic> diagnostics, int indentation) {
    	String buffer = "";
    	for (Diagnostic diagnostic : diagnostics) {
    		buffer += System.lineSeparator() + Strings.repeat("\t", indentation) + diagnostic;
    		buffer += getFullDiagnosticMessage(diagnostic.getChildren(), indentation + 1);
    	}
    	return buffer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        return "Perform QVT Operational Transformation";
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void cleanup(final IProgressMonitor monitor) throws CleanupFailedException {
        // Not needed yet.
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IBlackboardInteractingJob#setBlackboard(de.uka
     * .ipd.sdq.workflow. Blackboard)
     */
    @Override
    public void setBlackboard(final MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }
}
