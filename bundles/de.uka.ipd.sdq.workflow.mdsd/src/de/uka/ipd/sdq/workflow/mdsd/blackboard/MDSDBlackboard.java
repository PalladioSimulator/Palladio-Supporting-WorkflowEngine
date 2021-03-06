package de.uka.ipd.sdq.workflow.mdsd.blackboard;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * A blackboard implementation whose partitions use {@link ResourceSet} to store EMF Model
 * Resources.
 *
 * @author Steffen Becker
 */
public class MDSDBlackboard extends Blackboard<ResourceSetPartition> {

    /**
     * Get the list of top level elements from the given model.
     *
     * @param modelLocation
     *            The model
     * @return The list of top level elements of the given model
     */
    public List<EObject> getContents(final ModelLocation modelLocation) {
        if (!this.hasPartition(modelLocation.getPartitionID())) {
            throw new IllegalArgumentException("ResourceSetPartition does not exist");
        }
        final ResourceSetPartition partition = this.getPartition(modelLocation.getPartitionID());
        return partition.getContents(modelLocation.getModelID());
    }

    /**
     * Sets the contents.
     *
     * @param modelLocation
     *            the model location
     * @param newContents
     *            the new contents
     */
    public void setContents(final ModelLocation modelLocation, final List<EObject> newContents) {
        if (!this.hasPartition(modelLocation.getPartitionID())) {
            throw new IllegalArgumentException("ResourceSetPartition does not exist");
        }
        final ResourceSetPartition partition = this.getPartition(modelLocation.getPartitionID());
        partition.setContents(modelLocation.getModelID(), newContents);
    }

    /**
     * Model exists.
     *
     * @param modelLocation
     *            the model location
     * @return true, if successful
     */
    public boolean modelExists(final ModelLocation modelLocation) {
        if (this.hasPartition(modelLocation.getPartitionID())) {
            return this.getPartition(modelLocation.getPartitionID()).hasModel(modelLocation.getModelID());
        }
        return false;
    }
}
