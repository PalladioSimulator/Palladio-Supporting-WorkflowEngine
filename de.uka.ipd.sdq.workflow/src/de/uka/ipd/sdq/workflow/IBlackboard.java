package de.uka.ipd.sdq.workflow;

/**
 * Interface of a blackboard as defined in POSA I. See also class Blackboard for further information.
 *
 * @param <T> The type of the data stored in the blackboard. T is not limited to a certain interface to allow arbitrary values to be stored here.
 * @author Steffen
 */
public interface IBlackboard<T> {
	/**
	 * Retrieve a named partition from this blackboard. The partition has to exist on the 
	 * blackboard
	 * @param id ID of the partition to retrieve
	 * @return The requested partition
	 */
	T getPartition(String id);
	
	/**
	 * Add a new partition to this blackboard. The partition is required to not exist before.
	 * @param id The ID of the partition to add
	 * @param newPartition The partition to add
	 */
	void addPartition(String id, T newPartition);
	
	/**
	 * Query the blackboard for a named partition.
	 *
	 * @param id The ID of the named partition
	 * @return true if the partition exists on the blackboard
	 */
	boolean hasPartition(String id);
	
	/**
	 * Remove a named partition from the blackboard.
	 *
	 * @param id ID of the partition to removeS
	 */
	void removePartition(String id);
}
