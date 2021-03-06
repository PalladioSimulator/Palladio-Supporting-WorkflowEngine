package de.uka.ipd.sdq.workflow.jobs;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Runs a set of jobs in parallel. The jobs are executed asynchronously and
 * joined on a barrier before continuing the main workflow
 * 
 * @author Steffen Becker
 */
public class ParallelJob extends AbstractCompositeJob {

	/**
	 * Size of the thread pool to be used in the job executor. If -1 then the
	 * number of CPU cores is used as thread pool size
	 */
	private int threadPoolSize = -1;

	/** The executor services which finally controls the job's execution. */
	private ExecutorService executorService;

	/** The executor completion service used to collect the finished jobs. */
	private ExecutorCompletionService<Throwable> executorCompletionService;

	/** A hashmap of jobs under execution. */
	private HashMap<Future<Throwable>, JobCallable> futures = new HashMap<Future<Throwable>, JobCallable>();

	/**
	 * Initialise a parallel job containing a number of child jobs with a thread
	 * pool size equal to the number of CPU cores.
	 */
	public ParallelJob() {
		this(-1);
	}

	/**
	 * Initialise a parallel job with the given thread pool size to be used for
	 * job execution.
	 * 
	 * @param threadPoolSize
	 *            The amount of threads to be used to execute jobs
	 */
	public ParallelJob(int threadPoolSize) {
		super();

		this.threadPoolSize = threadPoolSize;
		setupExecutor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ipd.sdq.workflow.AbstractCompositeJob#execute(org.eclipse.core
	 * .runtime.IProgressMonitor )
	 */
	@Override
	public void execute(final IProgressMonitor monitor)
			throws JobFailedException, UserCanceledException {

		// Submit all jobs for execution to the executor. They will start
		// executing asynchronously
		for (final IJob job : myJobs) {
			JobCallable task = new JobCallable(job, monitor);
			Future<Throwable> future = executorCompletionService.submit(task);
			futures.put(future, task);
		}

		boolean singleJobFailed = false;
		Throwable failedJobException = null;
		while (futures.size() > 0 && !singleJobFailed && !monitor.isCanceled()) {
			try {
				Future<Throwable> completedTask = executorCompletionService
						.take();
				Throwable result = completedTask.get();
				if (result != null) {
					// Job terminated with exception
					singleJobFailed = true;
					failedJobException = result;
				} else {
					myExecutedJobs.add(futures.get(completedTask).getJob());
					futures.remove(completedTask);
				}
			} catch (InterruptedException e) {
				throw new JobFailedException(
						"Failed waiting for job to finish", e);
			} catch (ExecutionException e) {
				throw new JobFailedException(
						"Failed waiting for job to finish", e);
			}
		}
		if (singleJobFailed) {
			throw new JobFailedException("A parallel child job failed", failedJobException);
		}
		if (monitor.isCanceled()) {
			throw new UserCanceledException();
		}
	}

	/**
	 * Setup executor.
	 */
	private void setupExecutor() {
		if (threadPoolSize < 0) {
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
					.availableProcessors());
		} else {
			executorService = Executors.newFixedThreadPool(threadPoolSize);
		}

		executorCompletionService = new ExecutorCompletionService<Throwable>(
				executorService);
	}

	/**
	 * Inner class which is a wrapper to execute a job as callable in a separate thread. 
	 * his is needed to submit the job to an executor for asynchronous processing.
	 * 
	 * @author Steffen Becker
	 */
	private class JobCallable implements Callable<Throwable> {

		/**
		 * The job to be executed asynchronously.
		 */
		private IJob job;

		/**
		 * The jobs progress monitor.
		 */
		private IProgressMonitor monitor;

		/**
		 * Creates a new callable job by passing the job to be executed and the
		 * progess monitor.
		 * 
		 * @param job
		 *            The job to be executed
		 * @param monitor
		 *            The progress monitor to be used to display progress
		 */
		public JobCallable(IJob job, IProgressMonitor monitor) {
			this.job = job;
			this.monitor = monitor;
		}

		/**
		 * Call the Job.
		 * 
		 * @return A possible exception.
		 * @see java.util.concurrent.Callable#call()
		 */
		public Throwable call() {
			try {
				job.execute(monitor);
			} catch (Exception e) {
				return e;
			}
			return null;
		}

		/**
		 * @return Returns the encapsulated job
		 */
		public IJob getJob() {
			return job;
		}
	}

}
