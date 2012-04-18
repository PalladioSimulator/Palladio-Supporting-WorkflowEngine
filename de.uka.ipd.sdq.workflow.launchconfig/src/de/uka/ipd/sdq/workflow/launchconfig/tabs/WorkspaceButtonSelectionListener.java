package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Listener for File selection dialogs. Used to select files within the eclipse workspace.
 * 
 * @author groenda
 */
public class WorkspaceButtonSelectionListener extends FileSelectionAdapter {

    /**
     * Instantiates a new workspace button selection listener.
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
    public WorkspaceButtonSelectionListener(Text field, String[] fileExtension, String dialogTitle, Shell shell) {
        super(field, fileExtension, dialogTitle, shell);
    }

    /**
     * Instantiates a new workspace button selection listener.
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
    public WorkspaceButtonSelectionListener(Text field, String[] fileExtension, String dialogTitle, Shell shell,
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

        /** Filter from the redundant files. */
        List<ViewerFilter> filters = new ArrayList<ViewerFilter>();
        if (fileExtension != null) {
            FilePatternFilter filter = new FilePatternFilter();
            filter.setPatterns(fileExtension);
            filters.add(filter);
        }

        
        IFile[] files = WorkspaceResourceDialog.openFileSelection(getShell(), null, getDialogTitle(), false, null,
                filters);

        if (files.length != 0 && files[0] != null) {
            String portableString = files[0].getFullPath().toPortableString();
            String target = "platform:/resource" + portableString;
            return target; // solution before 2011-03-22: file.getLocation().toOSString();
        } else {
            return null;
        }
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

        IContainer[] dirs = WorkspaceResourceDialog.openFolderSelection(getShell(), null, getDialogTitle(), false,
                null, null);

        if (dirs != null 
                && dirs.length != 0 
                && dirs[0] != null) {
            String portableString = dirs[0].getFullPath().toPortableString();
            String target = "platform:/resource" + portableString;
            return target; // solution before 2011-03-22: directory.getLocation().toOSString();
        } else {
            return null;
        }

    }
}
