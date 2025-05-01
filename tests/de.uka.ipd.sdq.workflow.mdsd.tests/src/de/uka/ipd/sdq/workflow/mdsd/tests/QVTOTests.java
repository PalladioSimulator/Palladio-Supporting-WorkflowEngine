package de.uka.ipd.sdq.workflow.mdsd.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.m2m.internal.qvt.oml.trace.EMappingContext;
import org.eclipse.m2m.internal.qvt.oml.trace.EMappingResults;
import org.eclipse.m2m.internal.qvt.oml.trace.EValue;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;
import org.eclipse.m2m.internal.qvt.oml.trace.TraceRecord;
import org.eclipse.m2m.internal.qvt.oml.trace.VarParameterValue;
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
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;
import tools.mdsd.library.standalone.initialization.StandaloneInitializerBuilder;

@SuppressWarnings("restriction")
public class QVTOTests {

    private static final String PLUGIN_ID = "de.uka.ipd.sdq.workflow.mdsd.tests";
    protected MDSDBlackboard blackboard;

    @BeforeClass
    public static void registerFactories() throws StandaloneInitializationException {
        StandaloneInitializerBuilder.builder()
            .registerProjectURI(QVTOTests.class, PLUGIN_ID)
            .build()
            .init();
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

    protected URI createURI(String relativePart) {
        return URI.createPlatformPluginURI("/" + PLUGIN_ID + "/" + relativePart, false);
    }

    @Test
    public void testQVTOTransformationJob() throws JobFailedException, UserCanceledException, IOException {
        ModelLocation inputModelLocation = new ModelLocation("inputPartitionId", createURI("models/test.ecore"));
        ModelLocation outputModelLocation = new ModelLocation("inputPartitionId", createURI("models/output.ecore"));
        URI scriptFileURI = createURI("models/test.qvto");

        final QVTOTransformationJobConfiguration config = createConfiguration(inputModelLocation, outputModelLocation,
                scriptFileURI);

        final QVTOTransformationJob job = new QVTOTransformationJob(config);

        job.setBlackboard(this.blackboard);

        job.execute(new NullProgressMonitor());

        final List<EObject> outputContents = this.blackboard.getContents(outputModelLocation);
        assertThat(outputContents, is(not(empty())));
        // check if name was set in transformation
        assertThat(((EPackage) outputContents.get(0)).getName(), is("test"));
    }

    @Test
    public void testTraceOption() throws JobFailedException, UserCanceledException {
        final ModelLocation inputModelLocation = new ModelLocation("inputPartitionId", createURI("models/test.ecore"));
        final ModelLocation outputModelLocation = new ModelLocation("inputPartitionId",
                createURI("models/output.ecore"));
        final ModelLocation traceModelLocation = new ModelLocation("trace", createURI("models/output.trace"));
        final URI scriptFileURI = createURI("models/test.qvto");

        initBlackboardWithModel(traceModelLocation, false);
        final QVTOTransformationJobConfiguration config = createConfiguration(inputModelLocation, outputModelLocation,
                scriptFileURI);
        config.setTraceLocation(traceModelLocation);

        final QVTOTransformationJob job = new QVTOTransformationJob(config);

        job.setBlackboard(this.blackboard);

        job.execute(new NullProgressMonitor());

        assertThat(this.blackboard.modelExists(traceModelLocation), is(true));
        
        final List<EObject> traceModelContent = this.blackboard.getContents(traceModelLocation);
        assertThat(traceModelContent, is(iterableWithSize(1)));
        assertThat(traceModelContent.get(0), is(instanceOf(Trace.class)));
        
        final Trace traceModel = (Trace) traceModelContent.get(0);
        assertThat(traceModel.getTraceRecords(), is(iterableWithSize(1)));
        final TraceRecord traceRecord = traceModel.getTraceRecords()
            .get(0);

        final EPackage inputPackage = (EPackage) this.blackboard.getContents(inputModelLocation)
            .get(0);
        final EObject traceContext = Optional.of(traceRecord.getContext())
            .map(EMappingContext::getContext)
            .map(VarParameterValue::getValue)
            .map(EValue::getModelElement)
            .orElse(null);
        assertThat(traceContext, is(inputPackage));

        final List<EObject> outputContent = this.blackboard.getContents(outputModelLocation);
        assertThat(outputContent, is(iterableWithSize(1)));
        final EPackage outputPackage = (EPackage) outputContent.get(0);
        final EObject traceResult = Optional.of(traceRecord.getResult())
            .map(EMappingResults::getResult)
            .map(Collection::iterator)
            .filter(Iterator::hasNext)
            .map(Iterator::next)
            .map(VarParameterValue::getValue)
            .map(EValue::getModelElement)
            .orElse(null);
        assertThat(traceResult, is(outputPackage));
    }

    protected void initBlackboardWithModel(ModelLocation modelLocation, boolean loadModel) {
        if (!this.blackboard.hasPartition(modelLocation.getPartitionID())) {
            this.blackboard.addPartition(modelLocation.getPartitionID(), new ResourceSetPartition());
        }
        final ResourceSetPartition partition = this.blackboard.getPartition(modelLocation.getPartitionID());
        if (loadModel) {
            partition.loadModel(modelLocation.getModelID());
        }
    }

    protected QVTOTransformationJobConfiguration createConfiguration(ModelLocation inputModelLocation,
            ModelLocation outputModelLocation, URI scriptFileURI) {
        final ModelLocation[] inoutLocations = new ModelLocation[] { inputModelLocation, outputModelLocation };

        initBlackboardWithModel(inputModelLocation, true);
        initBlackboardWithModel(outputModelLocation, false);

        final QVTOTransformationJobConfiguration config = new QVTOTransformationJobConfiguration();
        config.setInoutModels(inoutLocations);
        config.setScriptFileURI(scriptFileURI);
        config.setOptions(Collections.emptyMap());
        return config;
    }

}
