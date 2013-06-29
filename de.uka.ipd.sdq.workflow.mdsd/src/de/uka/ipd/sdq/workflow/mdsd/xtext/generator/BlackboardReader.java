package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.xtext.mwe.AbstractReader;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.xtext.IBlackboardInteractingWorkflowComponent;

/**
 * The Class BlackboardReader.
 * 
 * @author Joerg Henss
 */
public class BlackboardReader extends AbstractReader implements IBlackboardInteractingWorkflowComponent<MDSDBlackboard> {

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /** The model location. */
    private final ModelLocation modelLocation;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.xtext.mwe.AbstractReader#checkConfigurationInternal(org.eclipse.emf.mwe.core.
     * issues.Issues)
     */
    @Override
    protected void checkConfigurationInternal(Issues issues) {
        super.checkConfigurationInternal(issues);
        if (modelLocation.getModelID() == null) {
            issues.addError(this, "No model URIs configured");
        }

    }

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
        ResourceSetPartition partition = blackboard.getPartition(modelLocation.getPartitionID());
        ResourceSet resourceSet = partition.getResourceSet();

        URI modelURI = modelLocation.getModelID();
        Resource resource = resourceSet.getResource(modelURI, true);
        int numberResources;
        do {
            numberResources = resourceSet.getResources().size();
            EcoreUtil.resolveAll(resource);
        } while (numberResources != resourceSet.getResources().size());

        getValidator().validate(resourceSet, getRegistry(), issues);
        addModelElementsToContext(ctx, resourceSet);

    }

    /**
     * Instantiates a new blackboard reader.
     * 
     * @param modelLocation
     *            the model location
     */
    public BlackboardReader(ModelLocation modelLocation) {
        super();
        this.modelLocation = modelLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.codegen.workflow.IBlackboardInteractingJob#setBlackbard(de.uka.ipd.sdq.codegen
     * .workflow.Blackboard)
     */
    @Override
    public void setBlackboard(MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }
}
