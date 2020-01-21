package de.uka.ipd.sdq.workflow.mdsd.tests;



import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.SavePartitionToDiskJob;
import de.uka.ipd.sdq.workflow.mdsd.mocks.MockResourceSetPartition;

import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;


public class MDSDBlackboardTests {
	
	MDSDBlackboard blackboard;
	
	@BeforeClass
	public static void registerFactories() {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
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
    	
    	URI modelURI = URI.createFileURI("models/test.ecore");
    	
    	ResourceSetPartition partition = new ResourceSetPartition();
    	partition.loadModel(modelURI);
		blackboard.addPartition("testPartitionId", partition);
    	
		//partition should be present
		Assert.assertTrue(blackboard.hasPartition("testPartitionId"));
		
    	//model should be present for modelLocation
		ModelLocation modelLocation = new ModelLocation("testPartitionId", modelURI);
    	Assert.assertTrue(blackboard.modelExists(modelLocation));
    	
    	//test model retrieval
    	ResourceSetPartition modelPartition = blackboard.getPartition("testPartitionId");
    	Assert.assertTrue(modelPartition.hasModel(modelURI));
    	Assert.assertNotNull(modelPartition.getFirstContentElement(modelURI));
    	
    }
    
    @Test
    public void testSavePartitionToDisk() throws JobFailedException, UserCanceledException, CleanupFailedException  {
    	
    	//Test on mock
    	MockResourceSetPartition partition = new MockResourceSetPartition();
    	blackboard.addPartition("testPartitionId", partition);
    	
		SavePartitionToDiskJob job = new SavePartitionToDiskJob("testPartitionId");
		job.setBlackboard(blackboard);
		
		final NullProgressMonitor monitor = new NullProgressMonitor();
		job.execute(monitor);
		
		Assert.assertTrue(partition.wasSaved());
		

		
    	
    }
    
    
    

}