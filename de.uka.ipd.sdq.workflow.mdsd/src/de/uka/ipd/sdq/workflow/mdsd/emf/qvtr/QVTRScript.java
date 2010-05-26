package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;


public class QVTRScript {
	private String qvtScriptFile;
	private String transformationName;
	private String transformationDirection;
	private Collection<Object> metaModels;
	
	
	
	public QVTRScript() {
		qvtScriptFile = "";
		transformationName = "";
		transformationDirection = "";
	}
	
	public String toString()
	{
		return qvtScriptFile;
	}
	
	public String getTransformationName() {
		return transformationName;
	}

	public String getTransformationDirection() {
		return transformationDirection;
	}

	public void setTransformationName(String transformationName) {
		this.transformationName = transformationName;
	}

	public void setTransformationDirection(String transformationDirection) {
		this.transformationDirection = transformationDirection;
	}

	public Reader toReader()
	{
		return new StringReader(qvtScriptFile);
	}
	
	public void setQVTFile(String fileName) {
		URI qvturl = null;
		try {
			qvturl = URI.createURI(fileName);
		} catch (IllegalArgumentException e) {
		//	logger.error("Wrong URI format.",e);
		}
		try {
			String fileuri = CommonPlugin.asLocalURI(qvturl).toFileString();
			BufferedReader br = new BufferedReader(
			           new InputStreamReader(
			           new FileInputStream(fileuri))); 
			StringBuffer contentOfFile = new StringBuffer();
			String line; 
			while ((line = br.readLine()) != null) {
			    contentOfFile.append(line);
			    contentOfFile.append("\n");
			}
			qvtScriptFile = contentOfFile.toString();
			
		} catch (FileNotFoundException fileNotFoundException) {
			//logger.error("QVT file not found!",fileNotFoundException);
			return;
		} catch (IOException ioException) {
			//logger.error("Could not read QVT file!",ioException,ioException);
			ioException.printStackTrace();
		}
	}
	

	public void setMetaModels(Collection<Object> metaModels) {
		this.metaModels = metaModels;
	}
	
	public Collection<Object> getMetaModels() {
		return metaModels;
	}
	
}
