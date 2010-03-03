package de.uka.ipd.sdq.workflow.pcm.configurations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import de.uka.ipd.sdq.completions.CompletionsPackage;
import de.uka.ipd.sdq.pcm.allocation.AllocationPackage;
import de.uka.ipd.sdq.pcm.core.CorePackage;
import de.uka.ipd.sdq.pcm.parameter.ParameterPackage;
import de.uka.ipd.sdq.pcm.repository.RepositoryPackage;
import de.uka.ipd.sdq.pcm.resourceenvironment.ResourceenvironmentPackage;
import de.uka.ipd.sdq.pcm.resourcetype.ResourcetypePackage;
import de.uka.ipd.sdq.pcm.seff.SeffPackage;
import de.uka.ipd.sdq.pcm.system.SystemPackage;
import de.uka.ipd.sdq.pcm.usagemodel.UsagemodelPackage;
import de.uka.ipd.sdq.stoex.StoexPackage;
import de.uka.ipd.sdq.workflow.exceptions.InvalidWorkflowJobConfiguration;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration;

/**
 * Base class of workflow configuration objects where the workflow has to deal with a PCM model instance. This
 * configuration class holds the locations of the PCM model parts, and (for convinience) a static list 
 * of EPackages needed to read the files.
 * 
 * @author Steffen Becker
 */
public abstract class AbstractPCMWorkflowRunConfiguration 
extends	AbstractWorkflowBasedRunConfiguration {
	
	/**
	 * Convinience field containing all PCM EPackages. Handy for loading PCM model files as it is needed to
	 * configure the loading resource set. 
	 */
	public static final EPackage[] PCM_EPACKAGES = new EPackage[]{
			SeffPackage.eINSTANCE,
			RepositoryPackage.eINSTANCE,
			ParameterPackage.eINSTANCE,
			UsagemodelPackage.eINSTANCE,
			SystemPackage.eINSTANCE,
			ResourcetypePackage.eINSTANCE,
			ResourceenvironmentPackage.eINSTANCE,
			AllocationPackage.eINSTANCE,
			StoexPackage.eINSTANCE,
			CorePackage.eINSTANCE,
			CompletionsPackage.eINSTANCE,
		};
	
	// BRG 04.12.09 configuration adoption to PCM model structure 
//	private String resourceTypeFile;
//	private String repositoryFile;
	private String middlewareFile;
//	private String systemFile;
	private List <String> allocationFiles;
	private String usageModelFile;


	/**
	 * @return Returns a list of string URIs containing all model files needed for a full PCM instance
	 */
	public List<String> getPCMModelFiles() {
		ArrayList<String> files = new ArrayList<String>();
	//BRG
	//	files.add(this.resourceTypeFile);
	//	files.add(this.repositoryFile);
	//	files.add(systemFile);
		files.addAll(allocationFiles);
		files.add(usageModelFile);
		
		return files;
	}


	//BRG 
	//public String getResourceTypeFile() {
	//	return resourceTypeFile;
	//}

//BRG
//	public void setResourceTypeFile(String resourceTypeFile) {
//		checkFixed();
//		this.resourceTypeFile = resourceTypeFile;
//	}


//	public String getRepositoryFile() {
//		return repositoryFile;
//	}

	/** Sets the PCM repository's file name
	 * @param repositoryFile The PCM repository file name
	 */
//	public void setRepositoryFile(String repositoryFile) {
//		checkFixed();
//		this.repositoryFile = repositoryFile;
//	}

	/**
	 * @return Returns the filename of the PCM's middleware completion repository
	 */
	public String getMiddlewareFile() {
		return middlewareFile;
	}

	/** Sets the filename of the PCM's middleware completion repository
	 * @param middlewareFile
	 */
	public void setMiddlewareFile(String middlewareFile) {
		checkFixed();
		this.middlewareFile = middlewareFile;
	}

	//BRG
//	public String getSystemFile() {
//		return systemFile;
//	}

//	public void setSystemFile(String systemFile) {
//		checkFixed();
//		this.systemFile = systemFile;
//	}

	public List<String> getAllocationFiles() {
		return allocationFiles;
	}

	public void setAllocationFiles(List<String> allocationFile) {
		checkFixed();
		this.allocationFiles = allocationFile;
	}

	public String getUsageModelFile() {
		return usageModelFile;
	}

	public void setUsageModelFile(String usageModelFile) {
		checkFixed();
		this.usageModelFile = usageModelFile;
	}



	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedRunConfiguration#validateAndFreeze()
	 */
	@Override
	public void validateAndFreeze() throws InvalidWorkflowJobConfiguration {
		super.validateAndFreeze();
		for (String fileURI : getPCMModelFiles()) {
			if (fileURI == null)
				throw new InvalidWorkflowJobConfiguration("Workflow configuration is invalid, not all PCM models are set");
			URI fileLocation = URI.createURI(fileURI);
			// TODO: Check whether file exists
		}
	}
}
