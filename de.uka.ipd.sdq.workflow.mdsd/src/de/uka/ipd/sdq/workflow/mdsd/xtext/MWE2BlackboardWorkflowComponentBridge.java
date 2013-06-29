package de.uka.ipd.sdq.workflow.mdsd.xtext;

import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowComponent;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

/**
 * The Class MWE2BlackboardWorkflowComponentBridge.
 * 
 * @param <T>
 *            the generic type
 */
public class MWE2BlackboardWorkflowComponentBridge<T extends IWorkflowComponent> extends MWE2WorkflowComponentBridge<T>
        implements IBlackboardInteractingWorkflowComponent<MDSDBlackboard> {

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /**
     * Instantiates a new mW e2 blackboard workflow component bridge.
     * 
     * @param job
     *            the job
     * @param ctx
     *            the ctx
     * @param name
     *            the name
     */
    public MWE2BlackboardWorkflowComponentBridge(T job, IWorkflowContext ctx, String name) {
        super(job, ctx, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uka.ipd.sdq.workflow.mdsd.xtext.IBlackboardInteractingWorkflowComponent#setBlackboard(
     * de.uka.ipd.sdq.workflow.Blackboard)
     */
    @Override
    public void setBlackboard(MDSDBlackboard blackboard) {
        this.blackboard = blackboard;

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2WorkflowComponentBridge#preExecute()
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void preExecute() {
        if (mwe2Job instanceof IBlackboardInteractingWorkflowComponent) {
            ((IBlackboardInteractingWorkflowComponent) mwe2Job).setBlackboard(blackboard);
        }

        super.preExecute();
    }

}
