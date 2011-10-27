/**
 * 
 */
package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import org.eclipse.swt.widgets.Text;

/**Listener for File selection dialogs.
 * Used to select a default value.
 * @author groenda
 */
public class DefaultFileSelectionAdapter extends FileSelectionAdapter {

	/** Default value. */
	String defaultFileURI;

	public DefaultFileSelectionAdapter(Text field, String[] fileExtension, String defaultFileURI) {
		super(field, fileExtension, "", null);
		this.defaultFileURI = defaultFileURI;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.workflow.launchconfig.tabs.FileSelectionAdapter#openFileDialog(org.eclipse.swt.widgets.Text, java.lang.String[])
	 */
	@Override
	protected String openFileDialog(Text textField, String[] fileExtension) {
		return defaultFileURI;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.workflow.launchconfig.tabs.FileSelectionAdapter#openFileDialog(org.eclipse.swt.widgets.Text, java.lang.String[])
	 */
	@Override
	protected String openFolderDialog(Text textField) {
		return defaultFileURI;
	}

}
