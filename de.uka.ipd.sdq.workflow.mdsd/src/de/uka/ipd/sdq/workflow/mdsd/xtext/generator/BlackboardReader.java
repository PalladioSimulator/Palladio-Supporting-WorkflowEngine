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
public class BlackboardReader extends AbstractReader
        implements IBlackboardInteractingWorkflowComponent<MDSDBlackboard> {

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
    protected void checkConfigurationInternal(final Issues issues) {
        super.checkConfigurationInternal(issues);
        if (this.modelLocation.getModelID() == null) {
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
    protected void invokeInternal(final WorkflowContext ctx, final ProgressMonitor monitor, final Issues issues) {
        final ResourceSetPartition partition = this.blackboard.getPartition(this.modelLocation.getPartitionID());
        final ResourceSet resourceSet = partition.getResourceSet();

        final URI modelURI = this.modelLocation.getModelID();
        final Resource resource = resourceSet.getResource(modelURI, true);
        int numberResources;
        do {
            numberResources = resourceSet.getResources().size();
            EcoreUtil.resolveAll(resource);
        } while (numberResources != resourceSet.getResources().size());

        this.getValidator().validate(resourceSet, this.getRegistry(), issues);
        this.addModelElementsToContext(ctx, resourceSet);

    }

    /**
     * Instantiates a new blackboard reader.
     *
     * @param modelLocation
     *            the model location
     */
    public BlackboardReader(final ModelLocation modelLocation) {
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
    public void setBlackboard(final MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }
}
