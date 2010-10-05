package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.jobs;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTRScript;

/**
 * A {@link QVTRTransformationJob} configuration.
 * See also {@link QVTREngine} and {@link QVTRScript}
 * 
 * @author Thomas Schuischel
 *
 */
public class QVTRTransformationJobConfiguration {
	
	// a collection of all model sets needed by the transformation
	protected Collection<ModelLocation[]> modelLocationSets = new ArrayList<ModelLocation[]>();
	
	// name of the traces partition
	protected String tracesPartitionName;
	
	// name of the old traces partition
	protected String oldTracesPartitionName;
	
	// a QVTRScript file
	protected QVTRScript qvtScript;
	
	// path to the traces folder
	protected URI traceFileURI;
	
	// PrintStream where extended logging should go to
	protected PrintStream extendedDebuggingLog;
	
	// if debug is enabled
	protected Boolean debug;
	
	// id of the QVT-R engine
	protected String qvtEngineID;

	/**
	 * Returns the id of the {@link QVTREngine}.
	 * 
	 * @return id of a QVT-R engine.
	 */
	public String getQvtEngineID() {
		return qvtEngineID;
	}
	
	/**
	 * Sets the id for the {@link QVTREngine} to use
	 * 
	 * @param qvtEngineID	id of the QVT-R engine
	 */
	public void setQvtEngineID(String qvtEngineID) {
		this.qvtEngineID = qvtEngineID;
	}
	
	/**
	 * Returns the {@link QVTRScript} to execute.
	 * 
	 * @return the QVT-R script
	 */
	public QVTRScript getQVTRScript() {
		return qvtScript;
	}
	
	/**
	 * Sets the {@link QVTRScript} to execute.
	 * 
	 * @param qvtScript the QVT-R script
	 */
	public void setQVTRScript(QVTRScript qvtScript) {
		this.qvtScript = qvtScript;
	}

	/**
	 * Returns if the engine is in debug mode.
	 * See also {@link QVTREngine}
	 * 
	 * @return true id the engine is in debug mode
	 */
	public Boolean isDebug() {
		return debug;
	}

	/**
	 * Sets the engine debug mode.
	 * See also {@link QVTREngine}
	 * 
	 * @param debug	true to enable debug mode
	 */
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * Returns the {@link URI} of the traces folder.
	 * 
	 * @return traces folder URI
	 */
	public URI getTraceFileURI() {
		return traceFileURI;
	}
	
	/**
	 * Sets the traces folder {@link URI}.
	 * 
	 * @param traceFileURI	traces folder URI
	 */
	public void setTraceFileURI(URI traceFileURI) {
		this.traceFileURI = traceFileURI;
	}

	/**
	 * Returns PrintStream the engine sends the extended logging information to.
	 * 
	 * @return PrintStream for extended logging
	 */
	public PrintStream getExtendedDebuggingLog() {
		return extendedDebuggingLog;
	}
	
	/**
	 * Sets PrintStream the engine sends the extended logging information to.
	 * 
	 * @param extendedDebuggingLog	PrintStream for extended logging
	 */
	public void setExtendedDebuggingLog(PrintStream extendedDebuggingLog) {
		this.extendedDebuggingLog = extendedDebuggingLog;
	}
	
	/**
	 * Returns the name of the {@link ResourceSetPartition} for traces.
	 * 
	 * @return name of the traces {@link ResourceSetPartition}
	 */
	public String getTracesPartitionName() {
		return tracesPartitionName;
	}
	
	/**
	 * Sets the name of the {@link ResourceSetPartition} for traces.
	 * 
	 * @param tracesPartitionName name of the traces {@link ResourceSetPartition}
	 */
	public void setTracesPartitionName(String tracesPartitionName) {
		this.tracesPartitionName = tracesPartitionName;
	}
	
	/**
	 * Returns the name of the {@link ResourceSetPartition} for 
	 * previous traces.
	 * 
	 * @return name of the previous traces {@link ResourceSetPartition}
	 */
	public String getOldTracesPartitionName() {
		return oldTracesPartitionName;
	}

	/**
	 * Sets the name of the {@link ResourceSetPartition} with 
	 * previous traces.
	 * 
	 * @param oldTracesPartitionName name of the previous traces {@link ResourceSetPartition}
	 */
	public void setOldTracesPartitionName(String oldTracesPartitionName) {
		this.oldTracesPartitionName = oldTracesPartitionName;
	}
	
	/**
	 * Returns the {@link Collection} of {@link ModelLocation}[].
	 * Each array of {@link ModelLocation} is a model set needed by the
	 * transformation.
	 * 
	 * @return collection of model sets
	 */
	public Collection<ModelLocation[]> getModelLocationSets() {
		return modelLocationSets;
	}
	
	/**
	 * Adds a array of {@link ModelLocation} to {@link Collection}.
	 * Each array of {@link ModelLocation} added is a model set needed by the
	 * transformation.
	 *  
	 * @param modelLocations a model sets
	 */
	public void addModelLocationSets(ModelLocation[] modelLocations) {
		modelLocationSets.add(modelLocations);
	}
	
}
