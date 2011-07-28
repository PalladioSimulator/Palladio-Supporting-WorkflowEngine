package de.uka.ipd.sdq.workflow.pcm.runconfig;

import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.uka.ipd.sdq.pcm.allocation.Allocation;
import de.uka.ipd.sdq.pcm.usagemodel.UsageModel;

/**
 * This class allows different tabs of the PCM launch configuration to share
 * objects between each other.
 * 
 * This class is not to be mixed up with the blackboard used during workflow job execution! 
 * 
 * @author hauck
 *
 */
public class PCMLaunchConfigurationBlackboard {
	
	private Allocation selectedAllocationModel = null;
	private UsageModel selectedUsageModel = null;
	
	/** Common EMF resource set for all models in this partition. */
	protected ResourceSet rs = new ResourceSetImpl();
	
	public void updateAllocationFile(String allocationFilePath) {
		Resource r = null;
		try {
			r = loadModel(allocationFilePath);
		} catch (WrappedException e) {
			// Maybe an invalid file has been specified.
			// Not a problem here, do nothing.
		}
		if ((r != null) && (r.getContents() != null) && (r.getContents().size() > 0)) {
			EObject rootObject = r.getContents().get(0);
			if (rootObject instanceof Allocation) {
				selectedAllocationModel = (Allocation)rootObject;
			} else {
				selectedAllocationModel = null;
			}
		} else {
			selectedAllocationModel = null;
		}
	}
	
	public void updateUsageModelFile(String usageModelFilePath) {
		Resource r = null;
		try {
			r = loadModel(usageModelFilePath);
		} catch (WrappedException e) {
			// Maybe an invalid file has been specified.
			// Not a problem here, do nothing.
		}
		if ((r != null) && (r.getContents() != null) && (r.getContents().size() > 0)) {
			EObject rootObject = r.getContents().get(0);
			if (rootObject instanceof UsageModel) {
				selectedUsageModel = (UsageModel)rootObject;
			} else {
				selectedUsageModel = null;
			}
		} else {
			selectedUsageModel = null;
		}
	}
	
	/**
	 * Returns null if no or invalid usage model is selected.
	 * @return
	 */
	public UsageModel getSelectedUsageModel() {
		return selectedUsageModel;
	}
	
	/**
	 * Returns null if no or invalid allocation model is selected.
	 * @return
	 */
	public Allocation getSelectedAllocationModel() {
		return selectedAllocationModel;
	}
	
	private Resource loadModel(String modelPath) throws WrappedException {
		Resource r;
		if (URI.createURI(modelPath).isPlatform() || modelPath.indexOf("://") >= 0) {
			r = rs.getResource(URI.createURI(modelPath), true);
		} else {
			r = rs.getResource(URI.createFileURI(modelPath), true);
		}
		resolveAllProxies();
		return r;
	}
	
	/**
	 * Resolve all model proxies, i.e., load all dependent models
	 */
	private void resolveAllProxies() {
		ArrayList<Resource> currentResources = null;
		// The resolveAll() call is not recursive; thus we have to repeat
		// the thing until the resource set does not grow any more:
		do {
			// Copy list to avoid concurrent modification exceptions:
			currentResources = new ArrayList<Resource>(this.rs.getResources());
			// TODO: check if this loop can be replaced through a single call
			// to EcoreUtil.resolveAll(rs). Maybe this is even already recursive,
			// i.e. we can eliminate the outer do..while loop?
			for (Resource r : currentResources) {
				EcoreUtil.resolveAll(r);
			}
		} while (currentResources.size() != this.rs.getResources().size());
	}

}
