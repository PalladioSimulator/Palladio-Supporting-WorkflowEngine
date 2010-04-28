package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.jobs;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngine;

public class QVTRTransformationJob implements
		IBlackboardInteractingJob<MDSDBlackboard> {
	
	private static final Logger logger = Logger.getLogger(QVTRTransformationJob.class);
	protected QVTRTransformationJobConfiguration configuration;
	private MDSDBlackboard blackboard;

	public QVTRTransformationJob(QVTRTransformationJobConfiguration configuration) {
		super();
		
		this.configuration = configuration;
	}
	
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		logger.info("Executing QVTR Transformation...");
		logger.debug("Script: "+ configuration.getQvtrScript());
		
		QVTREngine qvtrEngine = QVTREngine.getInstance(configuration.getQvtEngineID());
		if(qvtrEngine==null)
		{
			throw new JobFailedException("No QVT-R Engine available.");
		}
		qvtrEngine.setDebug(configuration.isDebug());
		
		if(configuration.getTracesPartitionName() != null) {
			ResourceSetPartition tracesPartition = this.blackboard.getPartition(configuration.getTracesPartitionName());
			if(tracesPartition == null)	{
				tracesPartition = new ResourceSetPartition();
				this.blackboard.addPartition(configuration.getTracesPartitionName(), tracesPartition);
			}
			qvtrEngine.setTracesResourceSet(tracesPartition.getResourceSet());
		}
		qvtrEngine.setWorkingDirectory(configuration.getWorkingDirectory());
		
		for(int i = 0; i < configuration.getModelPartitionNames().length;i++) {
			ResourceSetPartition modelPartition = configuration.getModelPartitionNames()[i];
			if(modelPartition != null) {
				modelPartition.resolveAllProxies();
				qvtrEngine.addModelResourceSet(modelPartition.getResourceSet());
			}
		}
		
		qvtrEngine.setQVTRScript(configuration.getQvtrScript());
		try {
			qvtrEngine.transform();
		} catch (Throwable e) {
			throw new JobFailedException("Error in mediniQVT Transformation",e);
		}
		
	}

	@Override
	public String getName() {
		return "Run a relational mediniQVT Transformation";
	}

	@Override
	public void rollback(IProgressMonitor monitor)
			throws RollbackFailedException {
		// Not needed
		
	}
	
	@Override
	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
	}

}
