package de.uka.ipd.sdq.workflow.launchconfig.tabs;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.text.StringMatcher;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Allows to select files based on their name. Uses inverted behavior of
 * {@link ViewerFilter} to filter files based on their name.
 * 
 */

public class FilePatternFilter extends ViewerFilter {

    private String[] patterns = new String[0];
    private StringMatcher[] matchers = new StringMatcher[0];

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (!(element instanceof IResource resource)) {
            return false;
        }
        if (resource instanceof IContainer) {
            return true;
        }
        for (StringMatcher matcher : this.matchers) {
            if (matcher.match(resource.getName())) {
                return true;
            }
        }
        return false;
    }

    public void setPatterns(String[] newPatterns) {
        this.patterns = newPatterns.clone();
        this.matchers = new StringMatcher[newPatterns.length];
        for (int i = 0; i < newPatterns.length; i++) {
            this.matchers[i] = new StringMatcher(newPatterns[i], true, false);
        }
    }

    public String[] getPatterns() {
        return this.patterns.clone();
    }
}
