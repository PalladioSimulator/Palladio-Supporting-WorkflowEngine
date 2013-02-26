package de.uka.ipd.sdq.workflow.mdsd.blackboard;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.CleanupFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;

/**
 * The Class SavePartitionToDiskJob.
 */
public class SavePartitionToDiskJob implements IJob, IBlackboardInteractingJob<MDSDBlackboard> {

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(SavePartitionToDiskJob.class);

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /** The partition id. */
    private String partitionID;

    /** The save options. */
    private Map<String, Object> saveOptions = null;

    /**
     * Instantiates a new save partition to disk job.
     * 
     * @param partitionID
     *            the partition id
     */
    public SavePartitionToDiskJob(String partitionID) {
        super();
        this.partitionID = partitionID;
    }

    /**
     * Instantiates a new save partition to disk job.
     * 
     * @param partitionID
     *            the partition id
     * @param saveOptions
     *            the save options
     */
    public SavePartitionToDiskJob(String partitionID, Map<String, Object> saveOptions) {
        super();
        this.partitionID = partitionID;
        this.saveOptions = saveOptions;

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
    	if(logger.isDebugEnabled())
    		logger.debug("Saving partition " + partitionID);
        ResourceSetPartition partition = this.blackboard.getPartition(this.partitionID);
        try {
            partition.storeAllResources(saveOptions);
        } catch (IOException e) {
            throw new JobFailedException("Failed to save models", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        return "Store all resources of a partion to disk";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
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
