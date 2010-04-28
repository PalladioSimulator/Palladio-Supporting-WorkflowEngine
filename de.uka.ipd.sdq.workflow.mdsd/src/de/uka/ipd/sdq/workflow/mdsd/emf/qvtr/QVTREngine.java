package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.internal.AbstractQVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.internal.QVTREngineType;

public abstract class  QVTREngine  implements AbstractQVTREngine{
	
	
	protected static class QVTRScriptInfoImpl {
		private HashMap<String,ArrayList<String> > transformationInfo;
		private boolean valid;
		
		private QVTRScriptInfoImpl(){};
		
		
		private QVTRScriptInfoImpl(HashMap<String,ArrayList<String> > transformationInfo, Boolean valid) {
			this.transformationInfo = transformationInfo;
			this.valid = valid;
		}
		
		String[] getDirections(String transformationName) {
			ArrayList<String> directions = transformationInfo.get(transformationName);
			if(directions != null)
				return directions.toArray(new String[0]);
			return null;
		}
		
		String[] getTransformations() {
			return transformationInfo.keySet().toArray(new String[0]);
		}
		
		Boolean isScriptValid() {
			return valid;
		}
	}

	protected QVTRScriptInfoImpl createQVTRScriptInfo(HashMap<String,ArrayList<String> > transformationInfo, Boolean valid)
	{
		return new QVTRScriptInfoImpl(transformationInfo,valid);
	}
	
	protected abstract QVTRScriptInfoImpl qvtrScriptInfoImpl();
	
	public static QVTREngine getInstance(String id)
	{
		QVTREngineType[] engineTypes = QVTREngineType.getEngineTyps();
		for (int i = 0;  i < engineTypes.length; i++) {
			if((id == null)||(engineTypes[i].getId() == id))
				return engineTypes[i].newQVTREngine();
		}
		
		return null;
	}
	
	public static QVTREngine getFirstInstance()
	{
		return getInstance(null);
	}
	
	public static Collection<String> getAllInstanceIDs()
	{
		ArrayList<String> engines = new ArrayList<String>();
		QVTREngineType[] engineTypes = QVTREngineType.getEngineTyps();
		for (int i = 0;  i < engineTypes.length; i++) {
			engines.add(engineTypes[i].getId());
		}
		
		return engines;
	}
	
}
