package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.oaw;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.AbstractQVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTRScript;

/**
 * A OAW workflow component for QVT-R transformations. It uses registered QVT-R engines.
 * 
 * @author Thomas Schuischel
 * 
 */
public class QVTRTransformation extends AbstractWorkflowComponent {

    /** The logger. */
    private static Logger logger = Logger.getLogger(QVTRTransformation.class);

    /** The Constant COMPONENT_NAME. */
    private static final String COMPONENT_NAME = "QVTRTransformation";

    /** The debug. */
    private Boolean debug = false;

    /** The qvtr script. */
    private String qvtrScript = null;

    /** The qvtr engine type. */
    private AbstractQVTREngine qvtrEngineType = null;

    /** The resource set. */
    protected ResourceSet resourceSet;

    /** The meta models. */
    private Collection<String> metaModels = new ArrayList<String>();

    /** The trace file uri. */
    private URI traceFileURI = null;

    /** The input models. */
    private Collection<String> inputModels = new ArrayList<String>();

    /** The output models. */
    private Collection<String> outputModels = new ArrayList<String>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent#invokeInternal(org.eclipse.emf.mwe
     * .core.WorkflowContext, org.eclipse.emf.mwe.core.monitor.ProgressMonitor,
     * org.eclipse.emf.mwe.core.issues.Issues)
     */
    @Override
    protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor, Issues issues) {
        // ctx.get(getModelSlot());
        run(issues);

    }

    /**
     * Run.
     * 
     * @param issues
     *            the issues
     */
    public void run(Issues issues) {

        logger.info("Executing QVTR Transformation...");
        logger.debug("Script: " + getQvtrScript());

        this.resourceSet = new ResourceSetImpl();
        this.resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
                .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

        AbstractQVTREngine qvtrEngine = getQvtrEngineType();

        if (qvtrEngine == null) {
            issues.addError(this, "No QVT-R Engine available.");
            return;
        }

        qvtrEngine.setDebug(getDebug());

        QVTRScript qvtrScriptLocal = new QVTRScript();
        qvtrScriptLocal.setMetaModels(getMetaModelsFromString(getMetaModelURIs()));
        qvtrScriptLocal.setQVTFile(getQvtrScript());

        qvtrEngine.setWorkingDirectory(getTraceFileURI());

        qvtrEngine.addModels(getResorces(inputModels));

        qvtrEngine.addModels(getAndCreateResorces(outputModels));

        qvtrEngine.addModels(getResorces(inputModels));

        qvtrEngine.setQVTRScript(qvtrScriptLocal);

        try {
            qvtrEngine.transform();
        } catch (Throwable e) {
            issues.addError(this, "Error in mediniQVT Transformation", e);
            return;
        }

        logger.info("Transformation executed successfully");
        issues.addInfo("Transformation executed successfully");
    }

    /**
     * Sets the trace file uri.
     * 
     * @param traceFileURI
     *            the new trace file uri
     */
    public void setTraceFileURI(String traceFileURI) {
        this.traceFileURI = URI.createURI(traceFileURI);
    }

    /**
     * Gets the trace file uri.
     * 
     * @return the trace file uri
     */
    public URI getTraceFileURI() {
        return traceFileURI;
    }

    /**
     * Sets the debug.
     * 
     * @param debug
     *            the new debug
     */
    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    /**
     * Gets the debug.
     * 
     * @return the debug
     */
    public Boolean getDebug() {
        return debug;
    }

    /**
     * Sets the qvtr script.
     * 
     * @param qvtrScript
     *            the new qvtr script
     */
    public void setQvtrScript(String qvtrScript) {
        this.qvtrScript = qvtrScript;
    }

    /**
     * Gets the qvtr script.
     * 
     * @return the qvtr script
     */
    public String getQvtrScript() {
        return qvtrScript;
    }

    /**
     * Gets the qvtr engine type.
     * 
     * @return the qvtr engine type
     */
    public AbstractQVTREngine getQvtrEngineType() {
        return qvtrEngineType;
    }

    /**
     * Sets the qvtr engine type.
     * 
     * @param qvtrEngineType
     *            the new qvtr engine type
     */
    public void setQvtrEngineType(AbstractQVTREngine qvtrEngineType) {
        this.qvtrEngineType = qvtrEngineType;
    }

    /**
     * Gets the input models.
     * 
     * @return the input models
     */
    public Collection<String> getInputModels() {
        return inputModels;
    }

    /**
     * Adds the input models.
     * 
     * @param inputModels
     *            the input models
     */
    public void addInputModels(String inputModels) {
        this.inputModels.add(inputModels);
    }

    /**
     * Gets the output models.
     * 
     * @return the output models
     */
    public Collection<String> getOutputModels() {
        return outputModels;
    }

    /**
     * Sets the output models.
     * 
     * @param outputModels
     *            the new output models
     */
    public void setOutputModels(String outputModels) {
        this.outputModels.add(outputModels);
    }

    /**
     * Gets the meta model ur is.
     * 
     * @return the meta model ur is
     */
    public Collection<String> getMetaModelURIs() {
        return metaModels;
    }

    /**
     * Adds the meta model uri.
     * 
     * @param metaModel
     *            the meta model
     */
    public void addMetaModelURI(String metaModel) {
        this.metaModels.add(metaModel);
    }

    /**
     * Gets the meta models from string.
     * 
     * @param models
     *            the models
     * @return the meta models from string
     */
    protected Collection<Object> getMetaModelsFromString(Collection<String> models) {
        Collection<Object> modelResources = new ArrayList<Object>();
        for (String model : models) {
            URI uri = URI.createURI(model, false);
            EList<EObject> objects = resourceSet.getResource(uri, true).getContents();
            for (EObject o : objects) {
                if (o instanceof EPackageImpl) {
                    EPackageImpl p = (EPackageImpl) o;
                    // this.resourceSet.getPackageRegistry().put(p.getNsURI(), p);
                    modelResources.add(p);
                }
            }

        }
        return modelResources;
    }

    /**
     * Gets the resorces.
     * 
     * @param models
     *            the models
     * @return the resorces
     */
    private Collection<Resource> getResorces(Collection<String> models) {
        Collection<Resource> modelResources = new ArrayList<Resource>();
        for (String model : models) {
            URI uri = URI.createURI(model, false);
            Resource r = resourceSet.getResource(uri, true);
            modelResources.add(r);
        }
        return modelResources;
    }

    /**
     * Gets the and create resorces.
     * 
     * @param models
     *            the models
     * @return the and create resorces
     */
    private Collection<Resource> getAndCreateResorces(Collection<String> models) {
        Collection<Resource> modelResources = new ArrayList<Resource>();
        ResourceSet rSet = new ResourceSetImpl();
        for (String model : models) {
            URI uri = URI.createURI(model, false);
            Resource r = rSet.createResource(uri);
            modelResources.add(r);
        }
        return modelResources;
    }

    /**
     * Gets the component name.
     * 
     * @return the component name
     * @see org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent#getComponentName()
     */
    @Override
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.emf.mwe.core.WorkflowComponent#checkConfiguration(org.eclipse.emf.mwe.core.issues
     * .Issues)
     */
    @Override
    public void checkConfiguration(final Issues issues) {
        if (qvtrScript == null) {
            issues.addError("QTRScript not set");
        }
    }
}
