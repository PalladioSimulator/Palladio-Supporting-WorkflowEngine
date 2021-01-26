package de.uka.ipd.sdq.workflow.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.function.Supplier;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;
import de.uka.ipd.sdq.workflow.jobs.BlackboardAwareJobProxy;
import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.JobProxy;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mocks.CancelingJob;
import de.uka.ipd.sdq.workflow.mocks.MockJob;

/**
 * The Class JobProxyTests.
 */
public class JobProxyTests {

    @Before
    public void setUp() {
        MockJob.resetExecutionNumber();
    }
    
    @Test
    public void testJobHandling() throws JobFailedException, UserCanceledException, CleanupFailedException {
        final MockJob job = new MockJob();
        var proxy = new JobProxy(() -> job);
        
        final NullProgressMonitor monitor = new NullProgressMonitor();
        proxy.execute(monitor);
        assertEquals(true, job.wasExecuted());
        proxy.cleanup(monitor);
        assertEquals(true, job.wasCleanedUp());
    }
    
    private boolean supplierExecuted = false;
    @Test
    public void testDelayedInit() throws JobFailedException, UserCanceledException, CleanupFailedException {
        final MockJob job = new MockJob();
        Supplier<IJob> trackingSupplier = () -> {
            supplierExecuted = true;
            return job;
        };
        var proxy = new JobProxy(trackingSupplier);
        
        final NullProgressMonitor monitor = new NullProgressMonitor();
        assertEquals(false, supplierExecuted);
        proxy.execute(monitor);
        assertEquals(true, supplierExecuted);
        assertEquals(true, job.wasExecuted());
    }
    
    
    @Test
    public void testBlackboardAwareVariant() throws JobFailedException, UserCanceledException {
        var bb = new Blackboard<>();
        
        final var job = new MockJob.WithBlackboard() {
            public void execute(org.eclipse.core.runtime.IProgressMonitor monitor) throws JobFailedException ,UserCanceledException {
                super.execute(monitor);
                assertEquals(bb, this.blackboard);
            };
        };
        var proxy = new BlackboardAwareJobProxy<>("test proxy", () -> job);
        
        assertNull(job.blackboard);
        proxy.setBlackboard(bb);
        assertNull(job.blackboard);
        
        final NullProgressMonitor monitor = new NullProgressMonitor();
        proxy.execute(monitor);
        
        assertEquals(true, job.wasExecuted());
    }
    
    /**
     * test a not executed job
     * @throws CleanupFailedException 
     */
    @Test(expected=CleanupFailedException.class)
    public void testNotExecutedJob() throws CleanupFailedException {
        final NullProgressMonitor monitor = new NullProgressMonitor();
        final MockJob job = new MockJob();
        var proxy = new JobProxy(() -> job);
        proxy.cleanup(monitor);
    }


    /**
     * test a canceled job.
     *
     * @throws JobFailedException
     *             the job failed exception
     * @throws UserCanceledException
     *             the user canceled exception
     */
    @Test(expected=UserCanceledException.class)
    public void testCanceledJob() throws JobFailedException, UserCanceledException {
        final NullProgressMonitor monitor = new NullProgressMonitor();
        var proxy = new JobProxy(() -> new CancelingJob());
        proxy.execute(monitor);
    }
}
