package de.uka.ipd.sdq.workflow.helloworld;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * Class to start the test workflow.
 *
 */
public class MainWorkflow {

	/** 
	 * Main Method to start a single job.
	 * @param args
	 * @throws JobFailedException
	 * @throws UserCanceledException
	 */
	public static void main(String[] args) {
		
		// set up a basic logging configuration
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%m%n")));
				
		HelloWorldJob job = new HelloWorldJob("World");
		Workflow myWorkflow = new Workflow(job);
		myWorkflow.run();
	}
}
