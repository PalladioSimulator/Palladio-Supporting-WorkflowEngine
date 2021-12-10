package de.uka.ipd.sdq.workflow.mdsd.blackboard;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

public class MDSDBlackboardTests {

    MDSDBlackboard blackboard;

    @BeforeClass
    public static void registerFactories() {
        final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("ecore", new XMIResourceFactoryImpl());
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() {
        this.blackboard = new MDSDBlackboard();
    }

    @Test
    public void testModelLoading() {

        final URI modelURI = URI.createFileURI("models/test.ecore");

        final ResourceSetPartition partition = new ResourceSetPartition();
        partition.loadModel(modelURI);
        this.blackboard.addPartition("testPartitionId", partition);

        // partition should be present
        Assert.assertTrue(this.blackboard.hasPartition("testPartitionId"));
        
        assertThat(this.blackboard.getPartitionIds(), containsInAnyOrder("testPartitionId"));

        // model should be present for modelLocation
        final ModelLocation modelLocation = new ModelLocation("testPartitionId", modelURI);
        Assert.assertTrue(this.blackboard.modelExists(modelLocation));

        // test model retrieval
        final ResourceSetPartition modelPartition = this.blackboard.getPartition("testPartitionId");
        Assert.assertTrue(modelPartition.hasModel(modelURI));
        Assert.assertNotNull(modelPartition.getFirstContentElement(modelURI));

    }

    @Test
    public void testSavePartitionToDisk() throws JobFailedException, UserCanceledException, CleanupFailedException {

        // Test on mock
        final MockResourceSetPartition partition = new MockResourceSetPartition();
        this.blackboard.addPartition("testPartitionId", partition);

        final SavePartitionToDiskJob job = new SavePartitionToDiskJob("testPartitionId");
        job.setBlackboard(this.blackboard);

        final NullProgressMonitor monitor = new NullProgressMonitor();
        job.execute(monitor);

        Assert.assertTrue(partition.wasSaved());

    }

}
