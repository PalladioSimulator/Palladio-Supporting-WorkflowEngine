package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.internal;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTRScript;

public interface AbstractQVTREngine {

	public void setProperty(String name, String value);
	public void setDebug(Boolean debug);
	
	public void setQVTRScript(QVTRScript qvtrScript);
	public void addModels(Collection<Resource> models);
	
	public void setTracesResourceSet(ResourceSet rSet);
	public void setOldTracesResourceSet(ResourceSet rSet);
	
	public void setWorkingDirectory(URI directoryURI);
	
	public void transform();
	
}
