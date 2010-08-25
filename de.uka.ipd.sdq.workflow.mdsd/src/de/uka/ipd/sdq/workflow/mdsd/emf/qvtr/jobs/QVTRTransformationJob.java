package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngine;

public class QVTRTransformationJob implements
		IBlackboardInteractingJob<MDSDBlackboard> {
	
	private static final Logger logger = Logger.getLogger(QVTRTransformationJob.class);
	protected QVTRTransformationJobConfiguration configuration;
	private MDSDBlackboard blackboard;

	public QVTRTransformationJob(QVTRTransformationJobConfiguration configuration) {
		super();
		
		this.configuration = configuration;
	}
	
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		logger.info("Executing QVTR Transformation...");
		logger.debug("Script: "+ configuration.getQVTRScript());
		
		QVTREngine qvtrEngine = QVTREngine.getInstance(configuration.getQvtEngineID());
		if(qvtrEngine==null)
		{
			throw new JobFailedException("No QVT-R Engine available.");
		}
		qvtrEngine.setDebug(configuration.isDebug());
		
		if(configuration.getTracesPartitionName() != null) {
			ResourceSetPartition tracesPartition = this.blackboard.getPartition(configuration.getTracesPartitionName());
			if(tracesPartition == null)	{
				tracesPartition = new ResourceSetPartition();
				this.blackboard.addPartition(configuration.getTracesPartitionName(), tracesPartition);
			}
			qvtrEngine.setTracesResourceSet(tracesPartition.getResourceSet());
		}
		
		qvtrEngine.setWorkingDirectory(configuration.getTraceFileURI());
		
		Iterator<ModelLocation[]> iterator = configuration.getModelLocationSets().iterator();
		while(iterator.hasNext()) {
			ModelLocation[] modelLocation = iterator.next();
			qvtrEngine.addModels(getResources(modelLocation));
		}
		
		qvtrEngine.setQVTRScript(configuration.getQVTRScript());
		
		try {
			qvtrEngine.transform();
		} catch (Throwable e) {
			throw new JobFailedException("Error in mediniQVT Transformation",e);
		}
		
		logger.info("Transformation executed successfully");
	}
	
	public Collection<Resource> getResources(ModelLocation[] modelLocations) {
		Collection<Resource> resources = new ArrayList<Resource>(modelLocations.length);
		
		for(int i = 0; i < modelLocations.length; i++) {
			resources.add(getResource(modelLocations[i]));
		}
		
		return resources;
	}
	
	public Resource getResource(ModelLocation modelLocation) {
		ResourceSetPartition partition = this.blackboard.getPartition(modelLocation.getPartitionID());
		ResourceSet rSet = partition.getResourceSet();
		Resource r = rSet.getResource(modelLocation.getModelID(), false);
		if (r == null) {
			new IllegalArgumentException("Model with URI "+modelLocation.getModelID()+" must be loaded first");
		}
		return r;
	}

	@Override
	public String getName() {
		return "Run a relational mediniQVT transformation";
	}

	@Override
	public void rollback(IProgressMonitor monitor)
			throws RollbackFailedException {
		// Not needed
		
	}
	
	@Override
	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
	}

}
