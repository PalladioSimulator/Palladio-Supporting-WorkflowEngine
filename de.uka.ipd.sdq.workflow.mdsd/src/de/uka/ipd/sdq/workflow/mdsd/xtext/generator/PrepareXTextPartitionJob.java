package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;

/**
 * The Class PrepareXTextPartitionJob.
 */
public class PrepareXTextPartitionJob implements IJob,
		IBlackboardInteractingJob<MDSDBlackboard> {

	/** The Constant logger. */
	private final Logger logger = Logger
			.getLogger(PrepareXTextPartitionJob.class);

	/** The blackboard. */
	private MDSDBlackboard blackboard;

	/** The model location. */
	private final ModelLocation modelLocation;

	/**
	 * Instantiates a new prepare x text partition job.
	 * 
	 * @param modelLocation
	 *            the model location
	 */
	public PrepareXTextPartitionJob(ModelLocation modelLocation) {
		this.modelLocation = modelLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		// Create and add the PCM and middleware model partition
		logger.debug("Creating XText model partition");

		ResourceSetPartition modelPartition = new ResourceSetPartition();
		this.blackboard.addPartition(modelLocation.getPartitionID(),
				modelPartition);

		modelPartition.loadModel(modelLocation.getModelID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uka.ipd.sdq.workflow.IJob#getName()
	 */
	@Override
	public String getName() {
		return "Prepare XText Blackboard Partions";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		this.blackboard.removePartition(modelLocation.getPartitionID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ipd.sdq.workflow.IBlackboardInteractingJob#setBlackboard(de.uka
	 * .ipd.sdq.workflow.Blackboard)
	 */
	@Override
	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
	}

}
