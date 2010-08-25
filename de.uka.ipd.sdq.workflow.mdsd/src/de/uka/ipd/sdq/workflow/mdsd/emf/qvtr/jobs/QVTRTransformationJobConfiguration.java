package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.jobs;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTRScript;

public class QVTRTransformationJobConfiguration {
	
	protected Collection<ModelLocation[]> modelLocationSets;
	
	protected String tracesPartitionName;
	
	protected QVTRScript qvtScript;
	
	protected URI traceFileURI;
	
	protected Boolean debug;
	
	protected String qvtEngineID;

	public String getQvtEngineID() {
		return qvtEngineID;
	}

	public QVTRScript getQVTRScript() {
		return qvtScript;
	}
	
	public void setQVTRScript(QVTRScript qvtScript) {
		this.qvtScript = qvtScript;
	}

	public Boolean isDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
	
	public URI getTraceFileURI() {
		return traceFileURI;
	}
	
	public void setTraceFileURI(URI traceFileURI) {
		this.traceFileURI = traceFileURI;
	}

	public String getTracesPartitionName() {
		return tracesPartitionName;
	}

	public Collection<ModelLocation[]> getModelLocationSets() {
		return modelLocationSets;
	}
	
	public void addModelLocationSets(ModelLocation[] modelLocations) {
		modelLocationSets.add(modelLocations);
	}
	
}
