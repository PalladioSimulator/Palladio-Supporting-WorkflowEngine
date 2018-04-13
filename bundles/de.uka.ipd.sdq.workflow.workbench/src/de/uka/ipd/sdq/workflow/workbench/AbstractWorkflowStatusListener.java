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
package de.uka.ipd.sdq.workflow.workbench;

/**
 * Default implementation of a workflow status listener. This implementation
 * provides default implementation of all notification methods with no
 * operations. Allowing to override only those methods required by the plugin.
 */
public abstract class AbstractWorkflowStatusListener implements
		WorkflowStatusListener {

	@Override
	public void notifyConfigurationValid() {
	}

	@Override
	public void notifyCreated() {
	}

	@Override
	public void notifyFinished() {
	}


}
