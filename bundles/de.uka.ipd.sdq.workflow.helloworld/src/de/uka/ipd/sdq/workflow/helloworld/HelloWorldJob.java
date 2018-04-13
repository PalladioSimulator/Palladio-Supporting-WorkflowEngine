package de.uka.ipd.sdq.workflow.helloworld;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * Basic example job just printing hello world to the system out.
 * @author Benjamin Klatt
 *
 */
public class HelloWorldJob implements IJob {
	
	private String who = "World";
	
	public HelloWorldJob(String who) {
		this.who = who;
	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException,
			UserCanceledException {
		System.out.println("Hello "+who);
	}

	@Override
	public String getName() {
		return "Hello World Job";
	}

	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		// Nothing to clean up after a run.
	}

}
