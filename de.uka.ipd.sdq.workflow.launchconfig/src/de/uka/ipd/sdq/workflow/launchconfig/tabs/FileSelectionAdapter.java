package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The Class FileSelectionAdapter.
 */
abstract class FileSelectionAdapter extends SelectionAdapter {

    /** Text field containing the path of the selected file. */
    private Text field;
    /** Restrictions on file extensions within selection dialog. */
    private String[] extensions;
    /** The shell used to open the dialog. */
    private Shell shell;
    /** Title/message of the dialog. */
    private String dialogTitle;
    private boolean useMultipleSelection;

    /**
     * Initializes a new file selection dialog.
     * 
     * @param field
     *            Text field which receives the path of the selected file.
     * @param fileExtension
     *            List of file extension restriction for file selection.
     * @param dialogTitle
     *            Title/message of the dialog.
     * @param shell
     *            The shell which is used to open the dialog.
     */
    public FileSelectionAdapter(Text field, String[] fileExtension, String dialogTitle, Shell shell) {
        this.field = field;
        this.extensions = fileExtension;
        this.shell = shell;
        this.dialogTitle = dialogTitle;
    }
    
    /**
     * Initializes a new file selection dialog.
     * 
     * @param field
     *            Text field which receives the path of the selected file.
     * @param fileExtension
     *            List of file extension restriction for file selection.
     * @param dialogTitle
     *            Title/message of the dialog.
     * @param shell
     *            The shell which is used to open the dialog.
     */
    public FileSelectionAdapter(Text field, String[] fileExtension, String dialogTitle, Shell shell, boolean useFolder, boolean useMultipleSelection) {
        this.field = field;
        this.extensions = fileExtension;
        this.shell = shell;
        this.dialogTitle = dialogTitle;
        this.useMultipleSelection = useMultipleSelection;
        this.useFolder = useFolder;
    }

    /** The use folder. */
    private boolean useFolder = false;

    /**
     * Initializes a new file selection dialog.
     * 
     * @param field
     *            Text field which receives the path of the selected file.
     * @param fileExtension
     *            List of file extension restriction for file selection.
     * @param dialogTitle
     *            Title/message of the dialog.
     * @param shell
     *            The shell which is used to open the dialog.
     * @param useFolder
     *            the use folder
     */
    public FileSelectionAdapter(Text field, String[] fileExtension, String dialogTitle, Shell shell, boolean useFolder) {
        this.field = field;
        this.extensions = fileExtension;
        this.shell = shell;
        this.dialogTitle = dialogTitle;
        this.useFolder = useFolder;
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent e) {
        String selectedFile = null;
        if (useFolder) {
        	selectedFile = openFolderDialog(field);
        } else {
        	if (useMultipleSelection) {
        		selectedFile = openFileDialog(field, extensions, true);
        	} else {
        		selectedFile = openFileDialog(field, extensions);
        	}
        }
        if (selectedFile != null) {
            field.setText(selectedFile);
        }
    }

    /**
     * Gets the shell used for the dialog.
     * 
     * @return The shell.
     */
    protected Shell getShell() {
        return shell;
    }

    /**
     * Geths the title/message for the dialog.
     * 
     * @return The title.
     */
    public String getDialogTitle() {
        return dialogTitle;
    }

    /**
     * Opens a file selection dialog. Use {@link #getShell()} to retrieve the shell and
     * {@link #getDialogTitle()} to retrieve the title within subclasses.
     * 
     * @param textField
     *            The text field which receives the path of the selected file.
     * @param fileExtension
     *            The extension which are allowed for the selected file.
     * @return the string
     */
    protected abstract String openFileDialog(Text textField, String[] fileExtension);
    
    /**
     * Opens a file selection dialog. Use {@link #getShell()} to retrieve the shell and
     * {@link #getDialogTitle()} to retrieve the title within subclasses.
     * 
     * @param textField
     *            The text field which receives the path of the selected file.
     * @param fileExtension
     *            The extension which are allowed for the selected file.
     * @param multipleSelection indicates whether multiple elements can be selected in the file dialog.
     * @return the string
     */
    protected abstract String openFileDialog(Text textField, String[] fileExtension, boolean multipleSelection);

    /**
     * Opens a folder selection dialog. Use {@link #getShell()} to retrieve the shell and
     * {@link #getDialogTitle()} to retrieve the title within subclasses.
     * 
     * @param textField
     *            The text field which receives the path of the selected folder.
     * @return the string
     */
    protected abstract String openFolderDialog(Text textField);
}
