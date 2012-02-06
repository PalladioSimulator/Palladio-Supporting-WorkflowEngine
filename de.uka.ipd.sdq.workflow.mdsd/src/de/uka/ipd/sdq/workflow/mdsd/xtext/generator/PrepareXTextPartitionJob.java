package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;



public class PrepareXTextPartitionJob implements IJob, IBlackboardInteractingJob<MDSDBlackboard>
{
	
	private static final Logger logger = Logger.getLogger(PrepareXTextPartitionJob.class);
	private MDSDBlackboard blackboard;
	private final ModelLocation modelLocation;
	
	public PrepareXTextPartitionJob(ModelLocation modelLocation) 
	{
		this.modelLocation = modelLocation;
	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		// Create and add the PCM and middleware model partition
		logger.debug("Creating XText model partition");
		
		ResourceSetPartition modelPartition = new ResourceSetPartition();
		this.blackboard.addPartition(modelLocation.getPartitionID(), modelPartition);
		
		modelPartition.loadModel(modelLocation.getModelID());
	}

	@Override
	public String getName() {
		return "Prepare XText Blackboard Partions";
	}
	@Override
	public void rollback(IProgressMonitor monitor)
			throws RollbackFailedException {
		this.blackboard.removePartition(modelLocation.getPartitionID());
	}

	@Override
	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
	}
	

}
