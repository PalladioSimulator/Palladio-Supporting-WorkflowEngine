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
 * Listener to be informed about workflow status changes.
 */
public interface WorkflowStatusListener {

	/** Trigger the listener when the workflow is finished. */
	public void notifyFinished();

	/** Trigger the listener when the workflow has been created incl. configuration. */
	public void notifyCreated();

	/** Trigger the listener when the workflow configuration has been successfully validated. */
	public void notifyConfigurationValid();
}
