package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.AbstractQVTREngine;

/**
 * A Job that executes a QVT-R transformation. It is configured through a
 * {@link QVTRTransformationJobConfiguration}.
 *
 * @author Thomas Schuischel
 *
 */
public class QVTRTransformationJob implements IBlackboardInteractingJob<MDSDBlackboard> {

    /**
     * log4j logger for this class.
     */
    private final Logger logger = Logger.getLogger(QVTRTransformationJob.class);
    /**
     * Configuration for this job.
     */
    protected QVTRTransformationJobConfiguration configuration;
    /**
     * Blackboard this job is interacting.
     */
    protected MDSDBlackboard blackboard;

    /**
     * Creates a new {@link QVTRTransformationJob} for a given
     * {@link QVTRTransformationJobConfiguration}.
     *
     * @param configuration
     *            a {@link QVTRTransformationJobConfiguration}
     */
    public QVTRTransformationJob(final QVTRTransformationJobConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {

        this.logger.info("Executing QVTR Transformation...");
        this.logger.debug("Script: " + this.configuration.getQVTRScript());

        // request the QVT-R engine we want to execute
        final AbstractQVTREngine qvtrEngine = AbstractQVTREngine.getInstance(this.configuration.getQvtEngineID());
        if (qvtrEngine == null) {
            throw new JobFailedException("No QVT-R Engine available.");
        }

        // enable debug
        qvtrEngine.setDebug(this.configuration.isDebug());

        // if options are provided
        if (this.configuration.getProperties() != null) {
            final Map<String, String> properties = this.configuration.getProperties();
            for (final Entry<String, String> property : properties.entrySet()) {
                qvtrEngine.setProperty(property.getKey(), property.getValue());
            }
        }

        // if a trace partition name is provided we create the partition
        if (this.configuration.getTracesPartitionName() != null) {
            ResourceSetPartition tracesPartition = this.blackboard
                    .getPartition(this.configuration.getTracesPartitionName());
            if (tracesPartition == null) {
                tracesPartition = new ResourceSetPartition();
                this.blackboard.addPartition(this.configuration.getTracesPartitionName(), tracesPartition);
            }
            qvtrEngine.setTracesResourceSet(tracesPartition.getResourceSet());
        }

        // if a old trace partition name is provided we sets the partition
        if (this.configuration.getOldTracesPartitionName() != null) {
            final ResourceSetPartition oldTracesPartition = this.blackboard
                    .getPartition(this.configuration.getOldTracesPartitionName());
            if (oldTracesPartition != null) {
                qvtrEngine.setOldTracesResourceSet(oldTracesPartition.getResourceSet());
            }
        }

        // set the working directory for traces
        qvtrEngine.setWorkingDirectory(this.configuration.getTraceFileURI());

        // add all model sets to the engine
        final Iterator<ModelLocation[]> iterator = this.configuration.getModelLocationSets().iterator();
        while (iterator.hasNext()) {
            final ModelLocation[] modelLocation = iterator.next();
            qvtrEngine.addModels(this.getResources(modelLocation));
        }

        // sets the script to execute to the engine
        qvtrEngine.setQVTRScript(this.configuration.getQVTRScript());

        // enables extended logging
        qvtrEngine.setExtendedDebugingLog(this.configuration.getExtendedDebuggingLog());

        // execute transformation
        try {
            qvtrEngine.transform();
        } catch (final Throwable e) {
            throw new JobFailedException("Error in mediniQVT Transformation", e);
        }

        this.logger.info("Transformation executed successfully");
    }

    /**
     * Returns a {@link Collection} of {@link Resource}s for a array of {@link ModelLocation}s.
     *
     * @param modelLocations
     *            an array of {@link ModelLocation}
     * @return a {@link Collection} of {@link Resource}
     */
    protected Collection<Resource> getResources(final ModelLocation[] modelLocations) {
        final Collection<Resource> resources = new ArrayList<Resource>(modelLocations.length);

        for (final ModelLocation modelLocation : modelLocations) {
            resources.add(this.getResource(modelLocation));
        }

        return resources;
    }

    /**
     * Returns a {@link Resource} for a {@link ModelLocation}.
     *
     * @param modelLocation
     *            the model location
     * @return a {@link Resource} for a {@link ModelLocation}
     */
    protected Resource getResource(final ModelLocation modelLocation) {

        final ResourceSetPartition partition = this.blackboard.getPartition(modelLocation.getPartitionID());
        final ResourceSet rSet = partition.getResourceSet();

        final Resource r = rSet.getResource(modelLocation.getModelID(), false);
        if (r == null) {
            new IllegalArgumentException("Model with URI " + modelLocation.getModelID() + " must be loaded first");
        }
        return r;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        return "Run a relational mediniQVT transformation";
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IJob#cleanup(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void cleanup(final IProgressMonitor monitor) throws CleanupFailedException {
    } // Not needed

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IBlackboardInteractingJob#setBlackboard(de.uka.ipd.sdq.workflow.
     * Blackboard)
     */
    @Override
    public void setBlackboard(final MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }

}
