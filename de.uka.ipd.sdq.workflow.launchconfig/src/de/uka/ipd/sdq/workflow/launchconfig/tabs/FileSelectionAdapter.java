package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

abstract class FileSelectionAdapter extends SelectionAdapter {
	
	/** Text field containing the path of the selected file. */
	private Text field;
	/** Restrictions on file extensions within selection dialog. */
	private String[] extensions;
	/** The shell used to open the dialog. */
	private Shell shell;
	/** Title/message of the dialog. */
	private String dialogTitle;
	
	/**Initializes a new file selection dialog.
	 * @param field Text field which receives the path of the selected file.
	 * @param fileExtension List of file extension restriction for file selection.
	 * @param dialogTitle Title/message of the dialog.
	 * @param shell The shell which is used to open the dialog.
	 */
	public FileSelectionAdapter(Text field, String[] fileExtension, String dialogTitle, Shell shell){
		this.field = field;
		this.extensions = fileExtension;
		this.shell = shell;
		this.dialogTitle = dialogTitle;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		String selectedFile = openFileDialog(field, extensions);
		if (selectedFile != null) {
			field.setText(selectedFile);			
		}
	}
	
	/**Gets the shell used for the dialog.
	 * @return The shell.
	 */
	protected Shell getShell() {
		return shell;
	}

	/**Geths the title/message for the dialog.
	 * @return The title.
	 */
	public String getDialogTitle() {
		return dialogTitle;
	}

	/**Opens a file selection dialog.
	 * Use {@link #getShell()} to retrieve the shell and {@link #getDialogTitle()} to retrieve the title within subclasses.
	 * @param textField The text field which receives the path of the selected file.
	 * @param fileExtension The extension which are allowed for the selected file.
	 * @return
	 */
	abstract protected String openFileDialog(Text textField, String[] fileExtension);
}
