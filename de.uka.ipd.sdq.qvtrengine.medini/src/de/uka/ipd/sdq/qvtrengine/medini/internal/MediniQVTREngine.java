package de.uka.ipd.sdq.qvtrengine.medini.internal;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.oslo.ocl20.semantics.model.contexts.ContextDeclaration;

import de.ikv.emf.qvt.EMFQvtProcessorImpl;
import de.ikv.medini.qvt.QVTProcessorConsts;
import de.ikv.medini.qvt.execution.debug.QVTExitDebugSessionException;
import de.ikv.medini.qvt.model.qvtbase.Transformation;
import de.ikv.medini.qvt.model.qvtbase.TypedModel;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTRScript;

public class MediniQVTREngine extends QVTREngine {

	private final Boolean enforce = true;
	
	QVTRScript qvtScript;
	EMFQvtProcessorImpl processorImpl;
	private Collection<Collection<Resource>> modelResources;
	private Collection<Resource> oldTraces;
	private StringWriter logs;
	
	public MediniQVTREngine() {
		processorImpl = new EMFQvtProcessorImpl(new LogWrapper(MediniQVTREngine.class.getName()));
		modelResources = new ArrayList<Collection<Resource>>();
		oldTraces = new ArrayList<Resource>();
		logs = new StringWriter();
		Logger.getLogger(MediniQVTREngine.class.getName()).addAppender(
				new WriterAppender(new PatternLayout("%-5p [%t]: %m%n")	, logs));
	}
	
	@Override
	public void addModelResourceSet(ResourceSet rSet) {
		modelResources.add(rSet.getResources());
		addMetaModels(rSet.getPackageRegistry().values());
	}

	@Override
	public void setDebug(Boolean debug) {
		String value = "false";
		
		if(debug)
			value = "true";
		
		setProperty(QVTProcessorConsts.PROP_DEBUG, value);
		setProperty(QVTProcessorConsts.PROP_DEBUG_TASKS, value);
	}

	@Override
	public void setProperty(String name, String value) {
		processorImpl.setProperty(name, value);
		
	}

	@Override
	public void setQVTRScript(QVTRScript qvtrScript) {
		this.qvtScript = qvtrScript;
		
	}

	@Override
	public void setWorkingDirectory(String directoryName) {
		URI directoryURI = URI.createURI(directoryName);
		processorImpl.setWorkingLocation(directoryURI);
		
	}

	@Override
	public void setOldTracesResourceSet(ResourceSet rSet) {
		oldTraces.addAll(rSet.getResources());
		
	}
	
	@Override
	public void setTracesResourceSet(ResourceSet rSet) {
		processorImpl.setResourceSetForTraces(rSet);
		
	}

	@Override
	public void transform() {
		processorImpl.setModels(modelResources);
		processorImpl.evaluateQVT(qvtScript.toReader(), qvtScript.getTransformationName(), enforce, qvtScript.getTransformationDirection(), null, oldTraces, processorImpl.getLog());
		processorImpl.setModels(Collections.EMPTY_LIST);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected QVTRScriptInfoImpl qvtrScriptInfoImpl()
	{
		HashMap<String, ArrayList<String>> transformationInfo = new HashMap<String, ArrayList<String>>();
		Boolean valid = true;

		
		//setDebug(true);
		try{
			List<ContextDeclaration> contextDeclarations = processorImpl.analyseQvt(qvtScript.toReader(), processorImpl.getLog() );
			
			if (contextDeclarations == null ) {
				
				throw new RuntimeException("Could not analyse QVT script. Aborting evaluation!");
			}
			
			ArrayList<String> transformations = new ArrayList<String>();
			
			Iterator<ContextDeclaration > iterator = contextDeclarations.iterator();
			while (iterator.hasNext()) {
				Transformation transformation = (Transformation) iterator.next();
				ArrayList<String> directions = new ArrayList<String>();

				transformations.add(transformation.getName());
				EList modelParameter = transformation.getModelParameter();
				for (Iterator iter = modelParameter.iterator(); iter.hasNext();) {
					TypedModel currentTypedModel = (TypedModel) iter.next();
					directions.add(currentTypedModel.getName());
				}
				
				transformationInfo.put(transformation.getName(), directions);
			}
		}
		catch (QVTExitDebugSessionException e) {
			valid = false;
		}
		
		
		return createQVTRScriptInfo(transformationInfo, valid);
	}
	
	/**
	 * Sets the meta models for the QVT transformation
	 * 
	 * @param metamodels
	 * 				List of meta model packages
	 */
	protected void addMetaModels(Collection<Object> metamodels) {
		for(Object p : metamodels){
			if(p instanceof EPackage )
				this.processorImpl.addMetaModel(p);
		}
	}

}
