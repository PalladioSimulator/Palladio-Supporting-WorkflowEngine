package de.uka.ipd.sdq.errorhandling;

import org.eclipse.emf.ecore.EObject;

import de.uka.ipd.sdq.errorhandling.dialogs.issues.IssuesDialog;

/**Issue combined with a severity level.
 * Used to record and report issues to the user using the {@link IssuesDialog}.
 *
 * @author groenda
 */
public class SeverityAndIssue implements Comparable<SeverityAndIssue> {

	/** Default resource name. */
	private static final String DEFAULT_RESOURCE_NAME = "";

	/** Severity of the issue. */
	private final SeverityEnum severity;
	/** Issues description. */
	private final String message;
	/** Element which caused an issue. */
	private final Object element;
	/** Name of the resource containing the object (if applicable).*/
	private final String objectResourceName;

	/**Create a new instance.
	 *
	 * @param error Severity of the issue.
	 * @param message Description of the issue.
	 * @param element Element causing the issue (if applicable).
	 */
	public SeverityAndIssue(SeverityEnum error, String message, EObject element) {
		this(error, message, element, null);
	}

	/**Create a new instance.
	 * @param error Severity of the issue.
	 * @param message Description of the issue.
	 * @param element Element causing the issue (if applicable).
	 * @param objectResourceName Name of the resource containing the object (if the element is not an {@link EObject}).
	 */
	public SeverityAndIssue(SeverityEnum error, String message, Object element, String objectResourceName) {
		this.severity = error;
		this.message = message;
		this.element = element;
		this.objectResourceName = objectResourceName;
	}

	/**
	 * @return The issue message.
	 */
	public String getMessage(){
		return message;
	}

	/**
	 * @return Details on the issue's message.
	 */
	public String getDetails() {
		String text = "Severity: " + (severity == SeverityEnum.ERROR ? "ERROR" : "WARNING")+"\n\n";
		text+="Message: " + message + "\n\n";
		if (element != null){
			text+="Element: " + element + "\n\n";
		}
		if (objectResourceName != null) {
			text += "Resource location: " + objectResourceName + "\n\n";
		}
		return text;
	}

	/**
	 * May be null.
	 * @return The Object or null.
	 */
	public Object getElement() {
		return element;
	}

	/**
	 * Return the resource name or an empty String if this {@link SeverityAndIssue} has no element.
	 * @return
	 */
	public String getResourceName() {
		if (element != null){
			if (element instanceof EObject) {
				return ((EObject)element).eResource().getURI().toFileString();
			} else {
				return objectResourceName == null ? DEFAULT_RESOURCE_NAME : objectResourceName;
			}
		} else
			return DEFAULT_RESOURCE_NAME;

	}

	public SeverityEnum getError() {
		return severity;
	}

	public int compareTo(SeverityAndIssue o) {
		if (this.severity == o.getError())
			return 0;
		if (this.severity == SeverityEnum.ERROR && o.getError() == SeverityEnum.WARNING)
			return -1;
		else
			return 1;
	}

}
