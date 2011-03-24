package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**Listener for File selection dialogs.
 * Used to select files on the local file system.
 * @author groenda
 */
class LocalFileSystemButtonSelectionAdapter extends FileSelectionAdapter {
	/** The selection dialog. */
	private FileDialog dialog;

	public LocalFileSystemButtonSelectionAdapter(Text field,
			String[] fileExtension, String dialogTitle, Shell shell) {
		super(field, fileExtension, dialogTitle, shell);
		dialog = new FileDialog(getShell(), SWT.OPEN);
		dialog.setFilterExtensions(fileExtension);
		dialog.setText(getDialogTitle());
	}

	@Override
	public String openFileDialog(Text textField, String[] fileExtension) {
		String filename = null;
		dialog.setFileName(textField.getText());
		
		if (dialog.open() != null) {
			String root = dialog.getFilterPath() + File.separatorChar;
			filename = root + dialog.getFileName();	
		}	
		
		return filename;
	}
}
