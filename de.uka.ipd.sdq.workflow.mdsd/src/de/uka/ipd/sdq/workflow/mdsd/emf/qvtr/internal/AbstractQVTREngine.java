package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.internal;

import org.eclipse.emf.ecore.resource.ResourceSet;

import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTRScript;

public interface AbstractQVTREngine {

	public void setProperty(String name, String value);
	public void setDebug(Boolean debug);
	
	public void setQVTRScript(QVTRScript qvtrScript);
	public void addModelResourceSet(ResourceSet rSet);
	
	public void setTracesResourceSet(ResourceSet rSet);
	public void setOldTracesResourceSet(ResourceSet rSet);
	
	public void setWorkingDirectory(String directoryName);
	
	public void transform();
	
}
