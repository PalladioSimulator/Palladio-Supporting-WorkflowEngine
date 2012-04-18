package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Listener for File selection dialogs. Used to select files on the local file system.
 * 
 * @author groenda
 */
public class LocalFileSystemButtonSelectionAdapter extends FileSelectionAdapter {

    /**
     * Instantiates a new local file system button selection adapter.
     * 
     * @param field
     *            the field
     * @param fileExtension
     *            the file extension
     * @param dialogTitle
     *            the dialog title
     * @param shell
     *            the shell
     */
    public LocalFileSystemButtonSelectionAdapter(Text field, String[] fileExtension, String dialogTitle, Shell shell) {
        super(field, fileExtension, dialogTitle, shell);

    }

    /**
     * Instantiates a new local file system button selection adapter.
     * 
     * @param field
     *            the field
     * @param fileExtension
     *            the file extension
     * @param dialogTitle
     *            the dialog title
     * @param shell
     *            the shell
     * @param useFolder
     *            the use folder
     */
    public LocalFileSystemButtonSelectionAdapter(Text field, String[] fileExtension, String dialogTitle, Shell shell,
            boolean useFolder) {
        super(field, fileExtension, dialogTitle, shell, useFolder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.workflow.launchconfig.tabs.FileSelectionAdapter#openFileDialog(org.eclipse
     * .swt.widgets.Text, java.lang.String[])
     */
    @Override
    public String openFileDialog(Text textField, String[] fileExtension) {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setFilterExtensions(fileExtension);
        dialog.setText(getDialogTitle());
        String filename = null;
        dialog.setFileName(textField.getText());

        if (dialog.open() != null) {
            String root = dialog.getFilterPath() + File.separatorChar;
            filename = root + dialog.getFileName();
        }

        return filename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.workflow.launchconfig.tabs.FileSelectionAdapter#openFolderDialog(org.eclipse
     * .swt.widgets.Text)
     */
    @Override
    public String openFolderDialog(Text textField) {
        DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.OPEN);
        dialog.setText(getDialogTitle());
        String filename = null;
        if (dialog.open() != null) {
            String root = dialog.getFilterPath() + File.separatorChar;
            filename = root;
        }

        return filename;
    }
}
