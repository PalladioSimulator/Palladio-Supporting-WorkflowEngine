package de.uka.ipd.sdq.workflow.mdsd.emf.qvto;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
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
    private static final Logger logger = Logger.getLogger(QVTOTransformationJob.class);

    /** The configuration. */
    private QVTOTransformationJobConfiguration configuration;

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /**
     * Instantiates a new qVTO transformation job.
     * 
     * @param conf
     *            the conf
     */
    public QVTOTransformationJob(QVTOTransformationJobConfiguration conf) {
        super();

        this.configuration = conf;
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        logger.info("Executing QVTO Transformation...");
        logger.debug("Script: " + configuration.getScriptFileURI());

        List<EObject>[] parameter = getModelContents(); // parameter is used as inout parameter
        QVTOResult result = QVTOExecutor.execute(configuration.getScriptFileURI(), configuration.getOptions(),
                parameter);
        if (!result.isSuccess()) {
            logger.error("Transformation job failed");
            logger.error(result.getDiagnosticResult().getMessage());
            result.logStackTrace(logger, Level.ERROR);
            throw new JobFailedException("Transformation execution failed");
        }
        storeResultOnBlackboard(parameter);
        logger.info("Transformation executed successfully");
    }

    /**
     * Store result on blackboard.
     * 
     * @param parameter
     *            the parameter
     */
    private void storeResultOnBlackboard(List<EObject>[] parameter) {
        for (int i = 0; i < parameter.length; i++) {
            blackboard.setContents(configuration.getInoutModels()[i], parameter[i]);
        }
    }

    /**
     * Gets the model contents.
     * 
     * @return the model contents
     */
    @SuppressWarnings("unchecked")
    private List<EObject>[] getModelContents() {
        List<EObject>[] modelContents = new List[configuration.getInoutModels().length];

        for (int i = 0; i < configuration.getInoutModels().length; i++) {
            if (blackboard.modelExists(configuration.getInoutModels()[i])) {
                modelContents[i] = blackboard.getContents(configuration.getInoutModels()[i]);
            } else {
                modelContents[i] = Collections.EMPTY_LIST;
            }
        }

        return modelContents;
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
     * @seede.uka.ipd.sdq.workflow.IJob#rollback(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void rollback(IProgressMonitor monitor) throws RollbackFailedException {
        // Not needed yet.
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IBlackboardInteractingJob#setBlackboard(de.uka.ipd.sdq.workflow.
     * Blackboard)
     */
    @Override
    public void setBlackboard(MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }
}
