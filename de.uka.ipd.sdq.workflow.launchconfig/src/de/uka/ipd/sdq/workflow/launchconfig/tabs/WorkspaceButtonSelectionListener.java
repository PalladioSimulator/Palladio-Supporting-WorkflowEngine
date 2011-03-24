package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/** Listener for File selection dialogs.
 * Used to select files within the eclipse workspace.
 * @author groenda */
class WorkspaceButtonSelectionListener extends FileSelectionAdapter {

	public WorkspaceButtonSelectionListener(Text field,
			String[] fileExtension, String dialogTitle, Shell shell) {
		super(field, fileExtension, dialogTitle, shell);
	}

	@Override
	public String openFileDialog(Text textField, String[] fileExtension) {
		
		/** Filter from the redundant files. */
		List<ViewerFilter> filters = new ArrayList<ViewerFilter>();
		FilePatternFilter filter = new FilePatternFilter();
		filter.setPatterns(fileExtension);
		filters.add(filter);
		
		IFile file = null;

		IFile[] files = WorkspaceResourceDialog.openFileSelection(getShell(),
				null, getDialogTitle(), false, null, filters);
		
		if (files.length != 0)
			file = files[0];
		String portableString = file.getFullPath().toPortableString();
		String target = "platform:/resource" + portableString;
		if (file != null) {
			return target; //solution before 2011-03-22: file.getLocation().toOSString();
		} else {
			return null;
		}
	}
}
