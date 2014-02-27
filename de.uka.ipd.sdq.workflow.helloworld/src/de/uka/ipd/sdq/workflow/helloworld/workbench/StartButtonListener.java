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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class StartButtonListener extends MouseAdapter {

	@Override
	public void mouseUp(MouseEvent e) {
		super.mouseUp(e);
		MainWorkflowDelegate delegate = new MainWorkflowDelegate();
		Action action = new Action() {};
		delegate.run(action);
	}
}
