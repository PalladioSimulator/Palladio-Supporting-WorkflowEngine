package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.text.StringMatcher;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Allows to select files based on their name.
 *
 */
public class FilePatternFilter extends ViewerFilter {

    private String[] patterns = new String[0];
    private StringMatcher[] matchers = new StringMatcher[0];

    public void setPatterns(String[] newPatterns) {
        if (newPatterns == null) {
            this.patterns = new String[0];
            this.matchers = new StringMatcher[0];
            return;
        }
        this.patterns = newPatterns;
        this.matchers = new StringMatcher[newPatterns.length];
        for (int i = 0; i < newPatterns.length; i++) {
            this.matchers[i] = new StringMatcher(newPatterns[i], true, false);
        }
    }

    public String[] getPatterns() {
        return this.patterns;
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (!(element instanceof IResource resource)) {
            return true;
        }
        int type = resource.getType();
        if (type == IResource.ROOT || type == IResource.PROJECT || type == IResource.FOLDER) {
            return true;
        }
        for (StringMatcher matcher : this.matchers) {
            if (matcher.match(resource.getName())) {
                return true;
            }
        }
        return false;
    }
}
