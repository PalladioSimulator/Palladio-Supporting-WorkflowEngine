package de.uka.ipd.sdq.workflow.workbench;

import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;


/** This listener prints a stream it listens to into a given MessageConsole 
 * 
 * @author hauck
 *
 */
public class MessageConsoleStreamListener implements IStreamListener {
	
	MessageConsoleStream out = null;
	
	public MessageConsoleStreamListener(MessageConsole messageConsole) {
		this.out = messageConsole.newMessageStream();
	}
	
	public void streamAppended(String text, IStreamMonitor monitor) {
		out.print(text);
	}

}
