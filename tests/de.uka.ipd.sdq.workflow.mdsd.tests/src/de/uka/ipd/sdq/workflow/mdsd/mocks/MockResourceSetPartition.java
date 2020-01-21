package de.uka.ipd.sdq.workflow.mdsd.mocks;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;

public class MockResourceSetPartition extends ResourceSetPartition {
	
	private boolean wasSaved = false;
	
	private boolean wasLoaded = false;

	@Override
	public Resource loadModel(URI modelURI) {
		this.wasLoaded = true;
		return super.loadModel(modelURI);
	}

	@Override
	public void storeAllResources() throws IOException {
		this.wasSaved = true;
	}

	@Override
	public void storeAllResources(Map<String, Object> saveOptions) throws IOException {
		this.wasSaved = true;
	}

	public boolean wasSaved() {
		return wasSaved;
	}

	public boolean wasLoaded() {
		return wasLoaded;
	}

	
	

}
