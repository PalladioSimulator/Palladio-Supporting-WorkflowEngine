package de.uka.ipd.sdq.workflow.tests;

import java.util.LinkedList;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;

import de.uka.ipd.sdq.workflow.SequentialJob;
import de.uka.ipd.sdq.workflow.exceptions.JobFailedException;
import de.uka.ipd.sdq.workflow.exceptions.CleanupFailedException;
import de.uka.ipd.sdq.workflow.exceptions.UserCanceledException;
import de.uka.ipd.sdq.workflow.mocks.CancelingJob;
import de.uka.ipd.sdq.workflow.mocks.FailingJob;
import de.uka.ipd.sdq.workflow.mocks.MockJob;

/**
 * The Class SequentialJobTests.
 */
public class SequentialJobTests extends TestCase {

    /** The my comp job. */
    private SequentialJob myCompJob = null;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
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
    public void testJobHandling() throws JobFailedException, UserCanceledException, CleanupFailedException {
        MockJob job = new MockJob();
        myCompJob.addJob(job);
        NullProgressMonitor monitor = new NullProgressMonitor();

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
    public void testInOrderExecution() throws JobFailedException, UserCanceledException {
        LinkedList<MockJob> jobs = new LinkedList<MockJob>();
        NullProgressMonitor monitor = new NullProgressMonitor();

        for (int i = 0; i < 20; i++) {
            jobs.addLast(new MockJob());
            myCompJob.addJob(jobs.peekLast());
        }

        myCompJob.execute(monitor);

        int executionNumber = 0;
        while (!jobs.isEmpty()) {
            MockJob job = jobs.removeFirst();
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
    public void testFailedJob() throws JobFailedException, UserCanceledException {
        try {
            NullProgressMonitor monitor = new NullProgressMonitor();

            myCompJob.addJob(new FailingJob());
            myCompJob.execute(monitor);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof JobFailedException);
            return;
        }
        Assert.fail("Expected exception not thrown");
    }

    /**
     * test a canceled job.
     * 
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     */
    public void testCanceledJob() throws JobFailedException, UserCanceledException {
        try {
            NullProgressMonitor monitor = new NullProgressMonitor();

            myCompJob.addJob(new CancelingJob());
            myCompJob.execute(monitor);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UserCanceledException);
            return;
        }
        Assert.fail("Expected exception not thrown");
    }
}
