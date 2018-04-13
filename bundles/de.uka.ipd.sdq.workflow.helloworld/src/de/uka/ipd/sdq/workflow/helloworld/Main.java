package de.uka.ipd.sdq.workflow.helloworld;

import org.eclipse.core.runtime.NullProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * Class to start the test workflow.
 *
 */
public class Main {

	/** 
	 * Main Method to start a single job.
	 * @param args
	 * @throws JobFailedException
	 * @throws UserCanceledException
	 */
	public static void main(String[] args) throws JobFailedException, UserCanceledException {
		HelloWorldJob job = new HelloWorldJob("World");
		job.execute(new NullProgressMonitor());
	}

}
