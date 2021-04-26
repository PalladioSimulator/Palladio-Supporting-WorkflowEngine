package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import java.io.File;
import java.util.StringTokenizer;

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
     * @param useMultipleSelection if true, multiple files can be selected.
     */
    public LocalFileSystemButtonSelectionAdapter(Text field, String[] fileExtension, String dialogTitle, Shell shell,
            boolean useFolder, boolean useMultipleSelection) {
        super(field, fileExtension, dialogTitle, shell, useFolder, useMultipleSelection);
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
    
    @Override
    public String openFileDialog(Text textField, String[] fileExtension, boolean multipleSelection) {
        FileDialog dialog = new FileDialog(getShell(), SWT.MULTI);
        dialog.setFilterExtensions(fileExtension);
        dialog.setText(getDialogTitle());
        String filename = "";
        String fileName = textField.getText();
        StringTokenizer tokenizer = new StringTokenizer(fileName, ";");
        if (tokenizer.countTokens() > 0) {
        	dialog.setFileName(tokenizer.nextToken());
        } else {
        	dialog.setFileName(fileName);
        }
        

        if (dialog.open() != null) {
            String root = dialog.getFilterPath() + File.separatorChar;
            if (multipleSelection) {
            	for (int i = 0; i < dialog.getFileNames().length; i++) {
            		if (i > 0) {
            			filename = filename + ";";
            		}
            		filename = filename + root + dialog.getFileNames()[i];
            	}
            } else {
            	filename = root + dialog.getFileName();
            }
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
        String filename = dialog.open();
        if (filename != null) {
            filename += File.separatorChar;
        }
        return filename;
    }
}
