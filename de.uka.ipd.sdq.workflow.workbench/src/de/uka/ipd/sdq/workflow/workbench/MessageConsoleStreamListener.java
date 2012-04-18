package de.uka.ipd.sdq.workflow.workbench;

import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * This listener prints a stream it listens to into a given MessageConsole.
 * 
 * @author Michael Hauck
 */
public class MessageConsoleStreamListener implements IStreamListener {

    /** The out. */
    private MessageConsoleStream out = null;

    /**
     * Instantiates a new message console stream listener.
     * 
     * @param messageConsole
     *            the message console
     */
    public MessageConsoleStreamListener(MessageConsole messageConsole) {
        this.out = messageConsole.newMessageStream();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.IStreamListener#streamAppended(java.lang.String,
     * org.eclipse.debug.core.model.IStreamMonitor)
     */
    @Override
    public void streamAppended(String text, IStreamMonitor monitor) {
        out.print(text);
    }

}
