/*******************************************************************************
 * Copyright (c) 2014
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    CONTRIBUTR_FIRST_AND_LAST_NAME - WORK_DONE (e.g. "initial API and implementation and/or initial documentation")
 *******************************************************************************/
package de.uka.ipd.sdq.workflow.helloworld.workbench;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.helloworld.HelloWorldJob;
import de.uka.ipd.sdq.workflow.helloworld.MainWorkflowConfiguration;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.workbench.AbstractWorkbenchDelegate;

public class MainWorkflowDelegate extends AbstractWorkbenchDelegate<MainWorkflowConfiguration, Workflow>{

	@Override
	protected IJob createWorkflowJob(MainWorkflowConfiguration config) {
		HelloWorldJob job = new HelloWorldJob("World");
		Workflow myWorkflow = new Workflow(job);
		return myWorkflow;
	}

	@Override
	protected MainWorkflowConfiguration getConfiguration() {
		return new MainWorkflowConfiguration();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	protected boolean useSeparateConsoleForEachJobRun() {
		return false;
	}

}
