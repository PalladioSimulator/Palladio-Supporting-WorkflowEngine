package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.jobs;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTRScript;

public class QVTRTransformationJobConfiguration {
	protected ResourceSetPartition[] modelPartitionNames;
	protected String tracesPartitionName;
	
	protected QVTRScript qvtScript;
	
	protected String workingDirectory;
	
	protected Boolean debug;
	
	protected String qvtEngineID;

	public String getQvtEngineID() {
		return qvtEngineID;
	}

	public QVTRScript getQvtrScript() {
		return qvtScript;
	}

	public Boolean isDebug() {
		return debug;
	}

	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public String getTracesPartitionName() {
		return tracesPartitionName;
	}

	public ResourceSetPartition[] getModelPartitionNames() {
		return modelPartitionNames;
	}
}
