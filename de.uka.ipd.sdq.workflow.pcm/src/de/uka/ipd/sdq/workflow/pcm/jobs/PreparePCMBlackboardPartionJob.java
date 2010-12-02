package de.uka.ipd.sdq.workflow.pcm.jobs;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.pcm.blackboard.PCMResourceSetPartition;
import de.uka.ipd.sdq.workflow.pcm.configurations.AbstractPCMWorkflowRunConfiguration;

public class PreparePCMBlackboardPartionJob 
implements IJob, IBlackboardInteractingJob<MDSDBlackboard> {
	
	private static final Logger logger = Logger.getLogger(PreparePCMBlackboardPartionJob.class);
	private MDSDBlackboard blackboard;

	static final URI PCM_PALLADIO_RESOURCE_TYPE_URI = URI.createURI("pathmap://PCM_MODELS/Palladio.resourcetype");
	static final URI PCM_PALLADIO_PRIMITIVE_TYPE_REPOSITORY_URI = URI.createURI("pathmap://PCM_MODELS/PrimitiveTypes.repository");

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		// Create and add the PCM and middleware model partition
		logger.debug("Creating PCM Model Partition");
		PCMResourceSetPartition myPartion = new PCMResourceSetPartition();
		this.blackboard.addPartition(LoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID, myPartion);
		
		ResourceSetPartition middlewareRepositoryPartition = new ResourceSetPartition();
		this.blackboard.addPartition(LoadPCMModelsIntoBlackboardJob.MIDDLEWARE_PARTITION_ID, middlewareRepositoryPartition);
		
		ResourceSetPartition eventMiddlewareRepositoryPartition = new ResourceSetPartition();
		this.blackboard.addPartition(LoadPCMModelsIntoBlackboardJob.EVENT_MIDDLEWARE_PARTITION_ID, eventMiddlewareRepositoryPartition);
		
		logger.debug("Initialising PCM EPackages");
		myPartion.initialiseResourceSetEPackages(AbstractPCMWorkflowRunConfiguration.PCM_EPACKAGES);
		middlewareRepositoryPartition.initialiseResourceSetEPackages(AbstractPCMWorkflowRunConfiguration.PCM_EPACKAGES);
		
		myPartion.loadModel(PCM_PALLADIO_PRIMITIVE_TYPE_REPOSITORY_URI);
		myPartion.loadModel(PCM_PALLADIO_RESOURCE_TYPE_URI);
	}

	@Override
	public String getName() {
		return "Prepare PCM Blackboard Partions";
	}

	@Override
	public void rollback(IProgressMonitor monitor)
			throws RollbackFailedException {
		this.blackboard.removePartition(LoadPCMModelsIntoBlackboardJob.EVENT_MIDDLEWARE_PARTITION_ID);
		this.blackboard.removePartition(LoadPCMModelsIntoBlackboardJob.MIDDLEWARE_PARTITION_ID);
		this.blackboard.removePartition(LoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID);
	}

	@Override
	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
	}

}
