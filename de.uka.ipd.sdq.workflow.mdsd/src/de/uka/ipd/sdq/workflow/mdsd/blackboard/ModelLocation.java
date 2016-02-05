package de.uka.ipd.sdq.workflow.mdsd.blackboard;

import org.eclipse.emf.common.util.URI;

/**
 * Immutable class used to uniquely identify a model (aka a resource) on an MDSD blackboard.
 *
 * @author Steffen Becker
 */
public class ModelLocation {

    /** The partition id. */
    private final String partitionID;

    /** The model id. */
    private final URI modelID;

    /**
     * Constructor.
     *
     * @param partitionID
     *            The ID of the blackboard partition containing the model
     * @param modelID
     *            The URI of the model (the normalized URI is used to identify the model in a
     *            partition)
     */
    public ModelLocation(final String partitionID, final URI modelID) {
        super();
        this.partitionID = partitionID;
        this.modelID = modelID;
    }

    /**
     * Gets the partition id.
     *
     * @return the partitionID
     */
    public String getPartitionID() {
        return this.partitionID;
    }

    /**
     * Gets the model id.
     *
     * @return the modelID
     */
    public URI getModelID() {
        return this.modelID;
    }
}
