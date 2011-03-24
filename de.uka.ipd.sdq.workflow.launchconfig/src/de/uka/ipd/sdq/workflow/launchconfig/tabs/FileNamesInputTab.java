package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.uka.ipd.sdq.workflow.launchconfig.ConstantsContainer;
import de.uka.ipd.sdq.workflow.launchconfig.RunConfigImages;
import de.uka.ipd.sdq.workflow.launchconfig.RunConfigPlugin;

/**
 * The class defines a tab, which is responsible for the input of an instance of
 * the Palladio Component Model.
 * 
 * @author Roman Andrej
 */
public class FileNamesInputTab extends AbstractLaunchConfigurationTab {

	/** input fields */
//	private Text textResourceType;
//	private Text textResourceEnvironment;
//	private Text textRepository;
//	private Text textSystem;
	protected Text textAllocation;
	protected Text textUsage;
	protected Text mwtextRepository;
	protected Text eventMiddlewareRepository;
	
	//container for UI elements
	protected Composite container;
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	@Override
	public Image getImage() {
		return RunConfigImages.getFileNamesTabImage();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		final ModifyListener modifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				FileNamesInputTab.this.setDirty(true);
				FileNamesInputTab.this.updateLaunchConfigurationDialog();
			}
		};

		this.container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout());

/*		*//**
		 * Create resource type section
		 *//*
		textResourceType = new Text(container, SWT.SINGLE | SWT.BORDER);
		createFileInputSection(container, modifyListener, "Resource Type File", ConstantsContainer.RESOURCETYPE_EXTENSION, textResourceType);

		*//**
		 * Create resource environment section
		 *//*
		textResourceEnvironment = new Text(container, SWT.SINGLE | SWT.BORDER);
		createFileInputSection(container, modifyListener, "Resource Environment File", ConstantsContainer.RESOURCEENVIRONMENT_EXTENSION, textResourceEnvironment);

		*//**
		 * Create repository section
		 *//*
		textRepository = new Text(container, SWT.SINGLE | SWT.BORDER);
		createFileInputSection(container, modifyListener, "Repository File", ConstantsContainer.REPOSITORY_EXTENSION, textRepository);
*/
		/**
		 * Create MW repository section
		 */
		mwtextRepository = new Text(container, SWT.SINGLE | SWT.BORDER);
		TabHelper.createFileInputSection(container, modifyListener, "Middleware Repository File", ConstantsContainer.REPOSITORY_EXTENSION, mwtextRepository, "Select Middleware Repository File", getShell(), ConstantsContainer.DEFAULT_MIDDLEWARE_REPOSITORY_FILE);

		/**
		 * Create event MW repository section
		 */
		eventMiddlewareRepository = new Text(container, SWT.SINGLE | SWT.BORDER);
		TabHelper.createFileInputSection(container, modifyListener, "Event Middleware Repository File", ConstantsContainer.REPOSITORY_EXTENSION, eventMiddlewareRepository, "Select Event Middleware Repository File", getShell(), ConstantsContainer.DEFAULT_EVENT_MIDDLEWARE_FILE);

/*		*//**
		 * Create system Section
		 *//*
		textSystem = new Text(container, SWT.SINGLE | SWT.BORDER);
		createFileInputSection(container, modifyListener, "System File", ConstantsContainer.SYSTEM_EXTENSION, textSystem);
		*/
		/**
		 * Create allocation section
		 */
		textAllocation = new Text(container, SWT.SINGLE | SWT.BORDER);
		TabHelper.createFileInputSection(container, modifyListener, "Allocation File", ConstantsContainer.ALLOCATION_EXTENSION, textAllocation, "Select Allocation File", getShell(), ConstantsContainer.DEFAULT_ALLOCATION_FILE);
		
		/**
		 * Create usage section
		 */
		//First set the text like this, will be changed to the right parent in createFileInputSection
		textUsage = new Text(container, SWT.SINGLE | SWT.BORDER);
		TabHelper.createFileInputSection(container, modifyListener, "Usage File", ConstantsContainer.USAGEMODEL_EXTENSION, textUsage, "Select Usage File", getShell(), ConstantsContainer.DEFAULT_USAGE_FILE);
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "Models file";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			textAllocation.setText(configuration.getAttribute(
					ConstantsContainer.ALLOCATION_FILE, ""));
		} catch (CoreException e) {
			RunConfigPlugin.errorLogger(getName(),"Allocation File", e.getMessage());
		}

/*		try {
			textRepository.setText(configuration.getAttribute(
					ConstantsContainer.REPOSITORY_FILE, ""));
		} catch (CoreException e) {
			RunConfigPlugin.errorLogger(getName(),"Repository File", e.getMessage());
		}*/

		try {
			mwtextRepository.setText(configuration.getAttribute(
					ConstantsContainer.MWREPOSITORY_FILE, ""));
		} catch (CoreException e) {
			RunConfigPlugin.errorLogger(getName(),"Middleware Repository File", e.getMessage());
		}

		try {
			eventMiddlewareRepository.setText(configuration.getAttribute(
					ConstantsContainer.EVENT_MIDDLEWARE_REPOSITORY_FILE, ConstantsContainer.DEFAULT_EVENT_MIDDLEWARE_FILE));
		} catch (CoreException e) {
			RunConfigPlugin.errorLogger(getName(),"Event Middleware Repository File", e.getMessage());
		}

/*		try {
			textResourceType.setText(configuration.getAttribute(
					ConstantsContainer.RESOURCETYPEREPOSITORY_FILE, ""));
		} catch (CoreException e) {
			RunConfigPlugin.errorLogger(getName(),"Resource Type File", e.getMessage());
		}*/

//		try {
//			textResourceEnvironment.setText(configuration.getAttribute(
//					ConstantsContainer.RESOURCEENVIRONMENT_FILE, ""));
//		} catch (CoreException e) {
//			RunConfigPlugin.errorLogger(getName(),"Resource Environment File", e.getMessage());
//		}

//		try {
//			textSystem.setText(configuration.getAttribute(
//					ConstantsContainer.SYSTEM_FILE, ""));
//		} catch (CoreException e) {
//			RunConfigPlugin.errorLogger(getName(),"System File", e.getMessage());
//		}

		try {
			textUsage.setText(configuration.getAttribute(
					ConstantsContainer.USAGE_FILE, ""));
		} catch (CoreException e) {
			RunConfigPlugin.errorLogger(getName(),"Usage File", e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
//		configuration.setAttribute(
//				ConstantsContainer.RESOURCETYPEREPOSITORY_FILE,
//				textResourceType.getText());
//		configuration.setAttribute(
//				ConstantsContainer.RESOURCEENVIRONMENT_FILE,
//				textResourceEnvironment.getText());
//		configuration.setAttribute(ConstantsContainer.REPOSITORY_FILE,
//				textRepository.getText());
		configuration.setAttribute(ConstantsContainer.MWREPOSITORY_FILE,
				mwtextRepository.getText());
		
		// set either the selected or the default event middleware repository if none was selected
		// this is also used as fall back for existing projects where the setDefault method is not called
		if(eventMiddlewareRepository.getText() != null && !eventMiddlewareRepository.getText().equals("")) {
			configuration.setAttribute(ConstantsContainer.EVENT_MIDDLEWARE_REPOSITORY_FILE,
					eventMiddlewareRepository.getText());
		} else {
			configuration.setAttribute(ConstantsContainer.EVENT_MIDDLEWARE_REPOSITORY_FILE,
					ConstantsContainer.DEFAULT_EVENT_MIDDLEWARE_FILE);			
		}

		
//		configuration.setAttribute(ConstantsContainer.SYSTEM_FILE, textSystem
//				.getText());
		configuration.setAttribute(ConstantsContainer.ALLOCATION_FILE,
				textAllocation.getText());
		configuration.setAttribute(ConstantsContainer.USAGE_FILE, textUsage
				.getText());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
//		configuration.setAttribute(ConstantsContainer.RESOURCETYPEREPOSITORY_FILE,
//			PCM_RESOURCETYPE_FILE_URI);
		configuration.setAttribute(ConstantsContainer.MWREPOSITORY_FILE,
			ConstantsContainer.DEFAULT_MIDDLEWARE_REPOSITORY_FILE);
		configuration.setAttribute(ConstantsContainer.EVENT_MIDDLEWARE_REPOSITORY_FILE,
			ConstantsContainer.DEFAULT_EVENT_MIDDLEWARE_FILE);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);

//		if (!validateFilePath(textRepository.getText(),
//				ConstantsContainer.REPOSITORY_EXTENSION)) {
//			setErrorMessage("Repository is missing!");
//			return false;
//		}
		if (!validateFilePath(mwtextRepository.getText(),
				ConstantsContainer.REPOSITORY_EXTENSION)) {
			setErrorMessage("Middleware Repository is missing!");
			return false;
		}
		if (!validateFilePath(eventMiddlewareRepository.getText(),
				ConstantsContainer.REPOSITORY_EXTENSION)) {
			setErrorMessage("Event Middleware Repository is missing!");
			return false;
		}
//		if (!validateFilePath(textResourceType.getText(),
//				ConstantsContainer.RESOURCETYPE_EXTENSION)) {
//			setErrorMessage("ResourceTypeRepository is missing!");
//			return false;
//		}
//		if (!validateFilePath(textResourceEnvironment.getText(),
//				ConstantsContainer.RESOURCEENVIRONMENT_EXTENSION)) {
//			setErrorMessage("ResourceEnvironment is missing!");
//			return false;
//		}
//		if (!validateFilePath(textSystem.getText(),
//				ConstantsContainer.SYSTEM_EXTENSION)) {
//			setErrorMessage("System is missing!");
//			return false;
//		}
		if (!validateFilePath(textAllocation.getText(),
				ConstantsContainer.ALLOCATION_EXTENSION)) {
			setErrorMessage("Allocation is missing!");
			return false;
		}
		if (!validateFilePath(textUsage.getText(),
				ConstantsContainer.USAGEMODEL_EXTENSION)) {
			setErrorMessage("Usage is missing!");
			return false;
		}
		return true;
	}

	@Override
	public boolean canSave() {
		return true;
	}
	
	@Override
	public void activated(ILaunchConfigurationWorkingCopy workingCopy) {
		// Override this methods's super class implementation in order to
		// prevent multiple invocation of initializeFrom() and subsequent
		// invocations of performApply().
	}

	@Override
	public void deactivated(ILaunchConfigurationWorkingCopy workingCopy) {}
	
	protected boolean validateFilePath(String filePath, String[] extensions){
		if (filePath.equals(""))
			return false;
		String extension = getExtensionFromArray(extensions).replace("*", ""); 
		if (filePath.contains(extension))
			return true;
		return false;
	}

	protected String getExtensionFromArray(String[] array){
		return array[0];
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
	 */
	@Override
	public String getId() {
		return "de.uka.ipd.sdq.codegen.runconfig.tabs.FileNamesInputTab";
	}
}
