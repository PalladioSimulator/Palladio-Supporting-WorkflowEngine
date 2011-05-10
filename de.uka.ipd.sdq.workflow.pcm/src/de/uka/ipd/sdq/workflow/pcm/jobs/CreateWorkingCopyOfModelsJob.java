package de.uka.ipd.sdq.workflow.pcm.jobs;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.uka.ipd.sdq.workflow.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.IJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.RollbackFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.pcm.blackboard.PCMResourceSetPartition;
import de.uka.ipd.sdq.workflow.pcm.configurations.AbstractPCMWorkflowRunConfiguration;

/**
 * Job to create a working copy of the models to simulate.
 * This ensures that any downstream job changing the models
 * does not modify the original models.
 *
 * Prerequisite of this job:
 * This job copies the models to the configured project created in
 * the workflow. It has to exist to be able to store the model copy
 * into it.
 *
 * @author Benjamin Klatt
 *
 */
public class CreateWorkingCopyOfModelsJob implements IJob,
	IBlackboardInteractingJob<MDSDBlackboard> {

	/** The logger for this class */
	private static final Logger logger = Logger.getLogger(CreateWorkingCopyOfModelsJob.class);

	/** The blackboard to interact with */
	private MDSDBlackboard blackboard = null;

	/** The work flow configuration to get the required information from */
	private AbstractPCMWorkflowRunConfiguration configuration;

	/**
	 * Constructor requiring the necessary configuration object.
	 *
	 * @param configuration The configuration for this job.
	 */
	public CreateWorkingCopyOfModelsJob(AbstractPCMWorkflowRunConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Execute this job and create the model copy.
	 */
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {

		assert (this.configuration != null);
		IProject project = CreatePluginProjectJob.getProject(this.configuration
				.getStoragePluginID());
		assert (project != null);

		// prepare the target path
		IFolder modelFolder = project.getFolder("model");
		if (project.isOpen() && !modelFolder.exists()) {
			logger.debug("Creating folder " + modelFolder.getName());
			try {
				modelFolder.create(false, true, null);
			} catch (CoreException e) {
				logger.error("unable to create model folder");
				throw new JobFailedException(e);
			}
		}
		String modelBasePath = "file:/"+modelFolder.getLocation().toOSString();

		// access the resources
		PCMResourceSetPartition partition = (PCMResourceSetPartition) this.blackboard.getPartition(LoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID);
		ResourceSet resourceSet = partition.getResourceSet();
		for (Resource ressource : resourceSet.getResources()) {

			// we only need to copy the file models
			if (ressource.getURI().scheme() != "pathmap") {
				URI uri = ressource.getURI();
				String relativePath = uri.lastSegment();
				URI newURI = URI.createURI(modelBasePath +"/"+ relativePath);
				ressource.setURI(newURI);

				try {
					ressource.save(null);
					partition.setContents(ressource.getURI(), ressource.getContents());
				} catch (IOException e) {
					logger.error("Unable to store resource "+ressource.getURI(),e);
				}
			}
		}
		try {
			partition.storeAllResources();
		} catch (IOException e) {
			logger.error("unable to store all resources",e);
			throw new JobFailedException("Unable to store all Resources",e);
		}
//		partition.resolveAllProxies();
//		this.blackboard.removePartition(LoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID);
//		this.blackboard.addPartition(LoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID, partition);
	}

	public String getName() {
		return "Create working copy of models";
	}

	public void rollback(IProgressMonitor monitor)
			throws RollbackFailedException {
		// nothing to roll back
	}

	public void setBlackboard(MDSDBlackboard blackboard) {
		this.blackboard = blackboard;
	}

}
