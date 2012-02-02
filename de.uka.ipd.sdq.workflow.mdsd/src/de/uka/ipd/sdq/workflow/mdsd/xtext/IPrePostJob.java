package de.uka.ipd.sdq.workflow.mdsd.xtext;

import de.uka.ipd.sdq.workflow.IJob;

public interface IPrePostJob extends IJob 
{
	public void preExecute();
	
	public void postExecute();

}
