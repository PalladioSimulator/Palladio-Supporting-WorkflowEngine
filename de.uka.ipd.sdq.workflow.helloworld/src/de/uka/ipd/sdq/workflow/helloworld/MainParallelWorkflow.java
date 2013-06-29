package de.uka.ipd.sdq.workflow.helloworld;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.ParallelJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * Class to start the test workflow.
 */
public class MainParallelWorkflow {

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
				
		ParallelJob parallelJob = new ParallelJob();
		
		parallelJob.add(new HelloWorldJob("Palladio"));
		parallelJob.add(new HelloWorldJob("Workflow"));
		parallelJob.add(new HelloWorldJob("Engine"));
		
		Workflow myWorkflow = new Workflow(parallelJob);
		myWorkflow.run();
	}
}
