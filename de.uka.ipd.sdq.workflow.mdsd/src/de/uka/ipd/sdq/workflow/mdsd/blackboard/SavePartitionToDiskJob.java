package de.uka.ipd.sdq.workflow.mdsd.blackboard;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * The Class SavePartitionToDiskJob.
 */
public class SavePartitionToDiskJob implements IJob, IBlackboardInteractingJob<MDSDBlackboard> {

    /** The Constant logger. */
    private final Logger logger = Logger.getLogger(SavePartitionToDiskJob.class);

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /** The partition id. */
    private final String partitionID;

    /** The save options. */
    private Map<String, Object> saveOptions = null;

    /**
     * Instantiates a new save partition to disk job.
     * 
     * @param partitionID
     *            the partition id
     */
    public SavePartitionToDiskJob(final String partitionID) {
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
    public SavePartitionToDiskJob(final String partitionID, final Map<String, Object> saveOptions) {
        super();
        this.partitionID = partitionID;
        this.saveOptions = saveOptions;

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Saving partition " + this.partitionID);
        }
        final ResourceSetPartition partition = this.blackboard.getPartition(this.partitionID);
        try {
            partition.storeAllResources(this.saveOptions);
        } catch (final IOException e) {
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
     * @see de.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime. IProgressMonitor)
     */
    @Override
    public void cleanup(final IProgressMonitor monitor) throws CleanupFailedException {
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
