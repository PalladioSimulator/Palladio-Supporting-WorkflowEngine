package de.uka.ipd.sdq.workflow.mdsd.blackboard;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;

public class SavePartitionToDiskJob implements IJob,
		IBlackboardInteractingJob<MDSDBlackboard> {

	private static final Logger logger = Logger.getLogger(SavePartitionToDiskJob.class);
	
	private MDSDBlackboard blackboard;
	private String partitionID;
	private Map<String, Object> saveOptions = null;

	public SavePartitionToDiskJob(String partitionID) {
		super();
		this.partitionID = partitionID;
	}
	
	public SavePartitionToDiskJob(String partitionID, Map<String, Object> saveOptions) 
	{
		super();
		this.partitionID = partitionID;
		this.saveOptions = saveOptions;
		
	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		logger.debug("Saving partition "+partitionID);
		ResourceSetPartition partition = this.blackboard.getPartition(this.partitionID);
		try {
			partition.storeAllResources(saveOptions);
		} catch (IOException e) {
			throw new JobFailedException("Failed to save models",e);
		}
	}

	@Override
	public String getName() {
		return "Store all resources of a partion to disk";
	}

	@Override
	public void rollback(IProgressMonitor monitor)
			throws RollbackFailedException {
	}

	@Override
	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
	}
}
