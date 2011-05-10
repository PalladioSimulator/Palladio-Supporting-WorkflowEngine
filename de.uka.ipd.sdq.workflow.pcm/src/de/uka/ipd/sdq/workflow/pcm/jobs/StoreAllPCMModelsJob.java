package de.uka.ipd.sdq.workflow.pcm.jobs;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.pcm.blackboard.PCMResourceSetPartition;
import de.uka.ipd.sdq.workflow.pcm.configurations.AbstractPCMWorkflowRunConfiguration;

/**
 * Job to store the loaded PCM models.
 *
 * Prerequisite of this job:
 * The working copy must have been created. E.g. by {@link CreateWorkingCopyOfModelsJob}.
 *
 * @author groenda
 *
 */
public class StoreAllPCMModelsJob implements IJob,
	IBlackboardInteractingJob<MDSDBlackboard> {

	/** The logger for this class */
	private static final Logger logger = Logger.getLogger(StoreAllPCMModelsJob.class);

	/** The blackboard to interact with */
	private MDSDBlackboard blackboard = null;

	/**
	 * Constructor requiring the necessary configuration object.
	 *
	 * @param configuration The configuration for this job.
	 */
	public StoreAllPCMModelsJob(AbstractPCMWorkflowRunConfiguration configuration) {
	}

	/**
	 * Execute this job and create the model copy.
	 */
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {

		PCMResourceSetPartition partition = (PCMResourceSetPartition) this.blackboard.getPartition(LoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID);
		try {
			partition.storeAllResources();
		} catch (IOException e) {
			logger.error("unable to store all resources",e);
			throw new JobFailedException("Unable to store all Resources",e);
		}
	}

	public String getName() {
		return "Update working copy of models";
	}

	public void rollback(IProgressMonitor monitor)
			throws RollbackFailedException {
		// nothing to roll back
	}

	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
	}

}
