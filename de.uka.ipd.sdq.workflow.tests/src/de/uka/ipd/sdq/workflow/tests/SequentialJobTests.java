package de.uka.ipd.sdq.workflow.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

import de.uka.ipd.sdq.workflow.WorkflowFailedException;
import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mocks.CancelingJob;
import de.uka.ipd.sdq.workflow.mocks.FailingJob;
import de.uka.ipd.sdq.workflow.mocks.MockJob;

/**
 * The Class SequentialJobTests.
 */
public class SequentialJobTests {

    /** The my comp job. */
    private SequentialJob myCompJob = null;

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() {
        myCompJob = new SequentialJob();
        MockJob.resetExecutionNumber();
    }

    /**
     * add a job. execute the composite job. check if the added job was executed. cleanup the
     * composite job. check if the added job was cleaned up.
     *
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     * @throws CleanupFailedException
     *             the cleanup failed exception
     */
    @Test
    public void testJobHandling() throws JobFailedException, UserCanceledException, CleanupFailedException {
        final MockJob job = new MockJob();
        myCompJob.addJob(job);
        final NullProgressMonitor monitor = new NullProgressMonitor();

        myCompJob.execute(monitor);
        assertEquals(true, job.wasExecuted());

        myCompJob.cleanup(monitor);
        assertEquals(true, job.wasCleanedUp());
    }

    /**
     * add a number of jobs. execute the composite job. check if all jobs were executed in the order
     * they were added.
     *
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     */
    @Test
    public void testInOrderExecution() throws JobFailedException, UserCanceledException {
        final LinkedList<MockJob> jobs = new LinkedList<MockJob>();
        final NullProgressMonitor monitor = new NullProgressMonitor();

        for (int i = 0; i < 20; i++) {
            jobs.addLast(new MockJob());
            myCompJob.addJob(jobs.peekLast());
        }

        myCompJob.execute(monitor);

        int executionNumber = 0;
        while (!jobs.isEmpty()) {
            final MockJob job = jobs.removeFirst();
            assertTrue("Job was executed in the wrong order!", job.getExecutionNumber() > executionNumber);
            executionNumber = job.getExecutionNumber();
        }
    }

    /**
     * test a failed job.
     *
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     */
    @Test(expected=WorkflowFailedException.class)
    public void testFailedJob() throws JobFailedException, UserCanceledException {
        final NullProgressMonitor monitor = new NullProgressMonitor();

        myCompJob.addJob(new FailingJob());
        myCompJob.execute(monitor);
    }

    /**
     * test a canceled job.
     *
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     */
    @Test(expected=WorkflowFailedException.class)
    public void testCanceledJob() throws JobFailedException, UserCanceledException {
        final NullProgressMonitor monitor = new NullProgressMonitor();

        myCompJob.addJob(new CancelingJob());
        myCompJob.execute(monitor);
    }
}
