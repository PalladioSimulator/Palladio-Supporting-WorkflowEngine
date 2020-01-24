package de.uka.ipd.sdq.workflow.mdsd.tests;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvto.QVTOTransformationJob;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvto.QVTOTransformationJobConfiguration;

public class QVTOTests {

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
    public void testQVTOTransformationJob() throws JobFailedException, UserCanceledException, IOException {

        final ModelLocation[] inoutLocations = new ModelLocation[2];

        final URI inputModelURI = URI.createFileURI("models/test.ecore");
        final URI outputModelURI = URI.createFileURI("models/output.ecore");

        URI scriptFileURI;
        if (org.eclipse.core.runtime.Platform.isRunning()) {
            scriptFileURI = URI.createPlatformPluginURI("/de.uka.ipd.sdq.workflow.mdsd.tests/models/test.qvto", true);
        } else {
            scriptFileURI = URI.createFileURI("models/test.qvto");
        }

        final ResourceSetPartition inputPartition = new ResourceSetPartition();
        inputPartition.loadModel(inputModelURI);
        this.blackboard.addPartition("inputPartitionId", inputPartition);
        inoutLocations[0] = new ModelLocation("inputPartitionId", inputModelURI);

        final ResourceSetPartition outputPartition = new ResourceSetPartition();
        this.blackboard.addPartition("outputPartitionId", outputPartition);
        inoutLocations[1] = new ModelLocation("outputPartitionId", outputModelURI);

        final QVTOTransformationJobConfiguration config = new QVTOTransformationJobConfiguration();
        config.setInoutModels(inoutLocations);
        config.setScriptFileURI(scriptFileURI);
        config.setOptions(Collections.emptyMap());

        final QVTOTransformationJob job = new QVTOTransformationJob(config);

        job.setBlackboard(this.blackboard);

        final NullProgressMonitor monitor = new NullProgressMonitor();
        job.execute(monitor);

        Assert.assertNotNull(outputPartition.getFirstContentElement(outputModelURI));
        // check if name was set in transformation
        Assert.assertEquals("test", ((EPackage) outputPartition.getFirstContentElement(outputModelURI)).getName());

    }

}
