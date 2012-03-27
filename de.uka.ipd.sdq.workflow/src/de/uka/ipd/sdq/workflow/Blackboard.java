package de.uka.ipd.sdq.workflow;

import java.util.HashMap;

/**
 * A blackboard is a storage space where workflow jobs can retrieve and store data from respectively to.
 * It implements the well known architecture pattern "Blackboard" of POSA I
 * 
 * The blackboard contains information pieces called partitions. Each partition contains data
 * needed for a specific type of job. For example, in a compiler, there would be a partition called
 * symbol table to store the detected variable declarations.
 * 
 * @author Steffen
 * @param <T> The type of the partitions of the blackboard
 */
public class Blackboard<T> implements IBlackboard<T> {

	private HashMap<String,T> blackboardStore = new HashMap<String, T>();
	
	/**
	 * Add a specific partition to the blackboard.
	 * 
	 * @param id The identifier for the partition to add.
	 * @param partition The partition itself.
	 * 
	 * @see de.uka.ipd.sdq.codegen.workflow.IBlackboard#addPartition(java.lang.String, java.lang.Object)
	 */
	public void addPartition(String id, T partition) {
		assert(!blackboardStore.containsKey(id));
		
		blackboardStore.put(id, partition);
	}

	/**
	 * Get a specific partition from the blackboard identified by the id it was registered with.
	 * 
	 * @param id The id to get the partition for.
	 * @return The requested partition or null if none matching found.
	 * 
	 * @see de.uka.ipd.sdq.codegen.workflow.IBlackboard#getPartition(java.lang.String)
	 */
	public T getPartition(String id) {
		assert (blackboardStore.containsKey(id));
		return blackboardStore.get(id);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.codegen.workflow.IBlackboard#hasPartition(java.lang.String)
	 */
	public boolean hasPartition(String id) {
		return blackboardStore.containsKey(id);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.codegen.workflow.IBlackboard#removePartition(java.lang.String)
	 */
	public void removePartition(String id) {
		blackboardStore.remove(id);
	}

}
