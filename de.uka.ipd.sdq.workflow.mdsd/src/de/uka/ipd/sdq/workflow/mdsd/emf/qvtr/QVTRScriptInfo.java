package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr;

public class QVTRScriptInfo {
	
	protected QVTREngine.QVTRScriptInfoImpl qvtScriptInfoImpl = null;
	
	public QVTRScriptInfo(QVTRScript qvtrScript) {
		this(qvtrScript,QVTREngine.getFirstInstance());
	}

	public QVTRScriptInfo(QVTRScript qvtrScript, String qvtrEngineID) {
		this(qvtrScript,QVTREngine.getInstance(qvtrEngineID));
	}
	
	protected QVTRScriptInfo(QVTRScript qvtrScript, QVTREngine qvtrEngine) {
		if(qvtrEngine != null){
			qvtrEngine.setQVTRScript(qvtrScript);
			qvtScriptInfoImpl = qvtrEngine.qvtrScriptInfoImpl();
		}
	}
	
	public String[] getDirections(String transformationName) {
		if(qvtScriptInfoImpl != null)
			return qvtScriptInfoImpl.getDirections(transformationName);
		
		return null;
	}
	
	public String[] getTransformations() {
		if(qvtScriptInfoImpl != null)
			return qvtScriptInfoImpl.getTransformations();
		
		return null;
	}
	public boolean isScriptValid() {
		if(qvtScriptInfoImpl != null)
			return qvtScriptInfoImpl.isScriptValid();
		
		return false;
	}
}
