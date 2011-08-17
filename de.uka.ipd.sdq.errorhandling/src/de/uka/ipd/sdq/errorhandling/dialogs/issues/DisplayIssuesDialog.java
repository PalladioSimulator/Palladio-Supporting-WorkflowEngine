/**
 *
 */
package de.uka.ipd.sdq.errorhandling.dialogs.issues;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

import de.uka.ipd.sdq.errorhandling.SeverityAndIssue;

/** Inner class used to display the dialog containing found validation errors. Needed by Eclipse to
 * display the dialog in Eclipse's UI thread
 *
 * @author Steffen Becker
 * @author groenda
 */
public class DisplayIssuesDialog implements Runnable {
	/**Shows the dialog at the current workbench display and returns after the issues are acknowledged.
	 * @param runner
	 */
	public static void showDialogSync(DisplayIssuesDialog runner) {
		PlatformUI.getWorkbench().getDisplay().syncExec(runner);
	}

	/**List of issues to display. */
	private List<SeverityAndIssue> issues;
	/** Indicator if the user selected to proceed after acknowledging the issues.*/
	private boolean shouldProceed;

	/**
	 * @return {@code true} if the user selected to proceed after acknowledging the issues.
	 */
	public boolean shouldProceedAfterErrorDialog() {
		return shouldProceed;
	}

	/**Creates a new instance.
	 * @param issues Issues to display.
	 */
	public DisplayIssuesDialog(List<SeverityAndIssue> issues) {
		super();
		this.issues = issues;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Dialog dialog = new IssuesDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell(), issues);
		dialog.open();
		this.shouldProceed = dialog.getReturnCode() == IssuesDialog.IGNORE_BUTTON;
	}

}
