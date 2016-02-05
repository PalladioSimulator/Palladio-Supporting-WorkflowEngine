package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.jobs;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.URI;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTRScript;

/**
 * A {@link QVTRTransformationJob} configuration. See also {@link QVTREngine} and {@link QVTRScript}
 *
 * @author Thomas Schuischel
 *
 */
public class QVTRTransformationJobConfiguration {

    /** A Map of options passed to QVT engine. */
    private Map<String, String> properties = null;

    /**
     * A collection of all model sets needed by the transformation.
     */
    private final Collection<ModelLocation[]> modelLocationSets = new ArrayList<ModelLocation[]>();

    /**
     * Name of the traces partition.
     */
    private String tracesPartitionName;

    /**
     * name of the old traces partition.
     */
    private String oldTracesPartitionName;

    /**
     * A QVTRScript file.
     */
    private QVTRScript qvtScript;

    /**
     * Path to the traces folder.
     */
    private URI traceFileURI;

    /**
     * PrintStream where extended logging should go to.
     */
    private PrintStream extendedDebuggingLog;

    /**
     * If debug is enabled.
     */
    private Boolean debug;

    /**
     * Id of the QVT-R engine.
     */
    private String qvtEngineID;

    /**
     * Returns the id of the {@link QVTREngine}.
     *
     * @return id of a QVT-R engine.
     */
    public String getQvtEngineID() {
        return this.qvtEngineID;
    }

    /**
     * Sets the id for the {@link QVTREngine} to use.
     *
     * @param qvtEngineID
     *            id of the QVT-R engine
     */
    public void setQvtEngineID(final String qvtEngineID) {
        this.qvtEngineID = qvtEngineID;
    }

    /**
     * Returns the {@link QVTRScript} to execute.
     *
     * @return the QVT-R script
     */
    public QVTRScript getQVTRScript() {
        return this.qvtScript;
    }

    /**
     * Sets the {@link QVTRScript} to execute.
     *
     * @param qvtScript
     *            the QVT-R script
     */
    public void setQVTRScript(final QVTRScript qvtScript) {
        this.qvtScript = qvtScript;
    }

    /**
     * Returns if the engine is in debug mode. See also {@link QVTREngine}
     *
     * @return true id the engine is in debug mode
     */
    public Boolean isDebug() {
        return this.debug;
    }

    /**
     * Sets the engine debug mode. See also {@link QVTREngine}
     *
     * @param debug
     *            true to enable debug mode
     */
    public void setDebug(final Boolean debug) {
        this.debug = debug;
    }

    /**
     * Returns the {@link URI} of the traces folder.
     *
     * @return traces folder URI
     */
    public URI getTraceFileURI() {
        return this.traceFileURI;
    }

    /**
     * Sets the traces folder {@link URI}.
     *
     * @param traceFileURI
     *            traces folder URI
     */
    public void setTraceFileURI(final URI traceFileURI) {
        this.traceFileURI = traceFileURI;
    }

    /**
     * Returns PrintStream the engine sends the extended logging information to.
     *
     * @return PrintStream for extended logging
     */
    public PrintStream getExtendedDebuggingLog() {
        return this.extendedDebuggingLog;
    }

    /**
     * Sets PrintStream the engine sends the extended logging information to.
     *
     * @param extendedDebuggingLog
     *            PrintStream for extended logging
     */
    public void setExtendedDebuggingLog(final PrintStream extendedDebuggingLog) {
        this.extendedDebuggingLog = extendedDebuggingLog;
    }

    /**
     * Returns the name of the {@link ResourceSetPartition} for traces.
     *
     * @return name of the traces {@link ResourceSetPartition}
     */
    public String getTracesPartitionName() {
        return this.tracesPartitionName;
    }

    /**
     * Sets the name of the {@link ResourceSetPartition} for traces.
     *
     * @param tracesPartitionName
     *            name of the traces {@link ResourceSetPartition}
     */
    public void setTracesPartitionName(final String tracesPartitionName) {
        this.tracesPartitionName = tracesPartitionName;
    }

    /**
     * Returns the name of the {@link ResourceSetPartition} for previous traces.
     *
     * @return name of the previous traces {@link ResourceSetPartition}
     */
    public String getOldTracesPartitionName() {
        return this.oldTracesPartitionName;
    }

    /**
     * Sets the name of the {@link ResourceSetPartition} with previous traces.
     *
     * @param oldTracesPartitionName
     *            name of the previous traces {@link ResourceSetPartition}
     */
    public void setOldTracesPartitionName(final String oldTracesPartitionName) {
        this.oldTracesPartitionName = oldTracesPartitionName;
    }

    /**
     * Returns the {@link Collection} of {@link ModelLocation}[]. Each array of
     * {@link ModelLocation} is a model set needed by the transformation.
     *
     * @return collection of model sets
     */
    public Collection<ModelLocation[]> getModelLocationSets() {
        return this.modelLocationSets;
    }

    /**
     * Adds a array of {@link ModelLocation} to {@link Collection}. Each array of
     * {@link ModelLocation} added is a model set needed by the transformation.
     *
     * @param modelLocations
     *            a model sets
     */
    public void addModelLocationSets(final ModelLocation[] modelLocations) {
        this.modelLocationSets.add(modelLocations);
    }

    /**
     * Get the properties for the engine.
     *
     * @return The properties
     */
    public Map<String, String> getProperties() {
        return this.properties;
    }

    /**
     * Set the properties for the engine as key value pair.
     *
     * @param properties
     *            The properties
     */
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

}
