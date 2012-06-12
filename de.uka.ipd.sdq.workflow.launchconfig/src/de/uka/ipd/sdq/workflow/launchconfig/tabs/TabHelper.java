package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Provides methods helpful when working with {@link ILaunchConfiguration},.
 * 
 * {@link AbstractLaunchConfigurationTab} and related classes.
 * 
 * @author groenda
 * @author pmerkle
 */
public class TabHelper {

/**
	 * Given an {@link ILaunchConfigurationTabGroup}, this method constructs an.
	 *
	 * @param tabGroup the tab group from which the CTabFolder will be created
	 * @param dialog see {@link ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
	 * @param mode see {@link ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
	 * @param parent see {@link Composite#Composite(Composite, int)}
	 * @param style see {@link Composite#Composite(Composite, int)}
	 * @return the c tab folder
	 * {@link CTabFolder} containing the same tabs as the passed
	 * ILaunchConfigurationTabGroup.
	 * <p>
	 * Note that the resulting tab folder does not support launch-specific
	 * methods like {@link ILaunchConfigurationTabGroup#performApply(ILaunchConfigurationWorkingCopy)}.
	 * If the tab folder is intended to work in an launch configuration environment
	 * (e.g. in a nested tab setting), use delegates to the corresponding methods
	 * of the tabs provided by the passed tabGroup.
	 */
    public static CTabFolder createTabFolder(ILaunchConfigurationTabGroup tabGroup, ILaunchConfigurationDialog dialog,
            String mode, Composite parent, int style) {
        tabGroup.createTabs(dialog, mode);
        ILaunchConfigurationTab[] tabs = tabGroup.getTabs();

        CTabFolder tabFolder = new CTabFolder(parent, style);
        for (int i = 0; i < tabGroup.getTabs().length; i++) {
            ILaunchConfigurationTab tab = tabs[i];
            tab.setLaunchConfigurationDialog(dialog);
            tab.createControl(tabFolder);

            CTabItem tabItem = new CTabItem(tabFolder, SWT.NULL);
            tabItem.setText(tab.getName());
            tabItem.setControl(tab.getControl());
        }
        tabFolder.setSelection(0);

        return tabFolder;
    }

    /**
     * Creates a section in the parent container for file selection. Creates a {@link Group} with a
     * label. Inside the group, a text field for the file with the given extension, a button to load
     * from the workspace and a button to load from the file system are displayed. The dialog title
     * is derived from modelFileLabel.
     * 
     * @param parentContainer
     *            The parent container
     * @param modifyListener
     *            The listener for modifications
     * @param groupLabel
     *            The label of the group.
     * @param fileExtensionRestrictions
     *            The extensions to load
     * @param textFileNameToLoad
     *            The text field that contains the filename. Its parent will be reset to the
     *            appropriate group within this method.
     * @param dialogShell
     *            Shell used for the file selection dialogs.
     * @param defaultFileURI
     *            Default URI used for the file.
     */
    public static void createFileInputSection(final Composite parentContainer, final ModifyListener modifyListener,
            final String groupLabel, final String[] fileExtensionRestrictions, Text textFileNameToLoad,
            Shell dialogShell, String defaultFileURI) {
        createFileInputSection(parentContainer, modifyListener, groupLabel, fileExtensionRestrictions,
                textFileNameToLoad, "Select " + groupLabel, dialogShell, defaultFileURI);
    }
    
    /**
     * Creates a section in the parent container for file selection. Creates a {@link Group} with a
     * label. Inside the group, a text field for the file with the given extension, a button to load
     * from the workspace and a button to load from the file system are displayed. The dialog title
     * is derived from modelFileLabel.
     * 
     * @param parentContainer
     *            The parent container
     * @param modifyListener
     *            The listener for modifications
     * @param groupLabel
     *            The label of the group.
     * @param fileExtensionRestrictions
     *            The extensions to load
     * @param textFileNameToLoad
     *            The text field that contains the filename. Its parent will be reset to the
     *            appropriate group within this method.
     * @param dialogShell
     *            Shell used for the file selection dialogs.
     * @param defaultFileURI
     *            Default URI used for the file.
     * @param useMultipleSelection if true, multiple files can be selected.
     */
    public static void createFileInputSection(final Composite parentContainer, final ModifyListener modifyListener,
            final String groupLabel, final String[] fileExtensionRestrictions, Text textFileNameToLoad,
            Shell dialogShell, String defaultFileURI, boolean useMultipleSelection) {
        createFileInputSection(parentContainer, modifyListener, groupLabel, fileExtensionRestrictions,
                textFileNameToLoad, "Select " + groupLabel, dialogShell, defaultFileURI, useMultipleSelection);
    }

    /**
     * Creates a section in the parent container for selection files. Creates a {@link Group} with a
     * label. Inside the group, a text field for the file with the given extension, a button to load
     * from the workspace and a button to load from the file system are displayed.
     * 
     * @param parentContainer
     *            The parent container
     * @param modifyListener
     *            The listener for modifications
     * @param groupLabel
     *            The label of the group.
     * @param fileExtensionRestrictions
     *            The extensions to load
     * @param textFileNameToLoad
     *            The text field that contains the filename. Its parent will be reset to the
     *            appropriate group within this method.
     * @param dialogTitle
     *            Title used for the file selection dialogs.
     * @param dialogShell
     *            Shell used for the file selection dialogs.
     * @param defaultFileURI
     *            Default URI used for the file.
     */
    public static void createFileInputSection(final Composite parentContainer, final ModifyListener modifyListener,
            final String groupLabel, final String[] fileExtensionRestrictions, Text textFileNameToLoad,
            String dialogTitle, Shell dialogShell, String defaultFileURI) {
        createFileInputSection(parentContainer, modifyListener, groupLabel, fileExtensionRestrictions,
                textFileNameToLoad, dialogTitle, dialogShell, true, true, defaultFileURI);
    }
    
    /**
     * Creates a section in the parent container for selection files. Creates a {@link Group} with a
     * label. Inside the group, a text field for the file with the given extension, a button to load
     * from the workspace and a button to load from the file system are displayed.
     * 
     * @param parentContainer
     *            The parent container
     * @param modifyListener
     *            The listener for modifications
     * @param groupLabel
     *            The label of the group.
     * @param fileExtensionRestrictions
     *            The extensions to load
     * @param textFileNameToLoad
     *            The text field that contains the filename. Its parent will be reset to the
     *            appropriate group within this method.
     * @param dialogTitle
     *            Title used for the file selection dialogs.
     * @param dialogShell
     *            Shell used for the file selection dialogs.
     * @param defaultFileURI
     *            Default URI used for the file.
     * @param allowMultipleSelection if true, multiple files can be selected.
     */
    public static void createFileInputSection(final Composite parentContainer, final ModifyListener modifyListener,
            final String groupLabel, final String[] fileExtensionRestrictions, Text textFileNameToLoad,
            String dialogTitle, Shell dialogShell, String defaultFileURI, boolean allowMultipleSelection) {
        createFileInputSection(parentContainer, modifyListener, groupLabel, fileExtensionRestrictions,
                textFileNameToLoad, dialogTitle, dialogShell, true, true, defaultFileURI, allowMultipleSelection);
    }
    
    /**
     * Creates a section in the parent container for selection files. Creates a {@link Group} with a
     * label. Inside the group, a text field for the file with the given extension, a button to load
     * from the workspace and a button to load from the file system are displayed.
     * 
     * @param parentContainer
     *            The parent container
     * @param modifyListener
     *            The listener for modifications
     * @param groupLabel
     *            The label of the group.
     * @param fileExtensionRestrictions
     *            The extensions to load
     * @param textFileNameToLoad
     *            The text field that contains the filename. Its parent will be reset to the
     *            appropriate group within this method.
     * @param dialogTitle
     *            Title used for the file selection dialogs.
     * @param dialogShell
     *            Shell used for the file selection dialogs.
     * @param showWorkspaceSelectionButton
     *            indicates whether a workspace selection button is shown
     * @param showFileSystemSelectionButton
     *            indicates whether a file system selection button is shown
     * @param defaultFileURI
     *            Default URI used for the file.
     */
    public static void createFileInputSection(final Composite parentContainer, final ModifyListener modifyListener,
            final String groupLabel, final String[] fileExtensionRestrictions, Text textFileNameToLoad,
            String dialogTitle, Shell dialogShell, boolean showWorkspaceSelectionButton,
            boolean showFileSystemSelectionButton, String defaultFileURI) {
    	createFileInputSection(parentContainer, modifyListener, groupLabel, fileExtensionRestrictions, textFileNameToLoad, dialogTitle, dialogShell, showWorkspaceSelectionButton, showFileSystemSelectionButton, defaultFileURI, false);
    }

    /**
     * Creates a section in the parent container for selection files. Creates a {@link Group} with a
     * label. Inside the group, a text field for the file with the given extension, a button to load
     * from the workspace and a button to load from the file system are displayed.
     * 
     * @param parentContainer
     *            The parent container
     * @param modifyListener
     *            The listener for modifications
     * @param groupLabel
     *            The label of the group.
     * @param fileExtensionRestrictions
     *            The extensions to load
     * @param textFileNameToLoad
     *            The text field that contains the filename. Its parent will be reset to the
     *            appropriate group within this method.
     * @param dialogTitle
     *            Title used for the file selection dialogs.
     * @param dialogShell
     *            Shell used for the file selection dialogs.
     * @param showWorkspaceSelectionButton
     *            indicates whether a workspace selection button is shown
     * @param showFileSystemSelectionButton
     *            indicates whether a file system selection button is shown
     * @param defaultFileURI
     *            Default URI used for the file.
     * @param allowMultipleSelection if true, multiple files can be selected.
     */
    public static void createFileInputSection(final Composite parentContainer, final ModifyListener modifyListener,
            final String groupLabel, final String[] fileExtensionRestrictions, Text textFileNameToLoad,
            String dialogTitle, Shell dialogShell, boolean showWorkspaceSelectionButton,
            boolean showFileSystemSelectionButton, String defaultFileURI, boolean allowMultipleSelection) {

        final Group fileInputGroup = new Group(parentContainer, SWT.NONE);
        final GridLayout glFileInputGroup = new GridLayout();
        int numColumns = 1;
        if (defaultFileURI != null) {
            numColumns++;
        }
        if (showWorkspaceSelectionButton) {
            numColumns++;
        }
        if (showFileSystemSelectionButton) {
            numColumns++;
        }
        glFileInputGroup.numColumns = numColumns;
        fileInputGroup.setLayout(glFileInputGroup);
        fileInputGroup.setText(groupLabel); // The group name
        fileInputGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        textFileNameToLoad.setParent(fileInputGroup);
        final GridData gridDataTextFileName = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridDataTextFileName.widthHint = 200;
        textFileNameToLoad.setLayoutData(gridDataTextFileName);
        textFileNameToLoad.addModifyListener(modifyListener);

        // new String[]{"*diagram","*.settings","*.project"} used before 2011-03-22
        if (showWorkspaceSelectionButton) {
            final Button workspaceButton = new Button(fileInputGroup, SWT.NONE);
            workspaceButton.setText("Workspace...");
            workspaceButton.addSelectionListener(new WorkspaceButtonSelectionListener(textFileNameToLoad,
                    fileExtensionRestrictions, dialogTitle, dialogShell, false, allowMultipleSelection));
        }

        if (showFileSystemSelectionButton) {
            final Button localFileSystemButton = new Button(fileInputGroup, SWT.NONE);
            localFileSystemButton.setText("File System...");
            localFileSystemButton.addSelectionListener(new LocalFileSystemButtonSelectionAdapter(textFileNameToLoad,
                    fileExtensionRestrictions, dialogTitle, dialogShell, false, allowMultipleSelection));
        }
        if (defaultFileURI != null) {
            final Button defaultFileURIButton = new Button(fileInputGroup, SWT.NONE);
            defaultFileURIButton.setText("Default");
            defaultFileURIButton.addSelectionListener(new DefaultFileSelectionAdapter(textFileNameToLoad,
                    fileExtensionRestrictions, defaultFileURI));
        }
    }

    /**
     * Checks if a file name is not empty and has one of the given extensions.
     * 
     * @param filePath
     *            Path of the file.
     * @param extensions
     *            Set of extensions. The strings have to be of the format {@code*.[extension]}. An
     *            example string value is <code>*.system</code>.
     * @return {@code true} if, and only if, the check succeeds.
     */
    public static boolean validateFilenameExtension(String filePath, String[] extensions) {
        // check emptiness
        if (filePath == null) {
            return false;
        }
        if (filePath.equals("")) {
            return false;
        }
        // check extension
        String extension;
        boolean extensionValid = false;
        for (int position = 0; position < extensions.length; position++) {
            extension = extensions[position].replace("*", "");
            if (filePath.endsWith(extension)) {
                extensionValid = true;
            }
        }
        if (!extensionValid) {
            return false;
        }
        // default
        return true;
    }
}
