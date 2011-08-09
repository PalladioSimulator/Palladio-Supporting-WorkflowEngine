package de.uka.ipd.sdq.workflow.launchconfig.extension;

import de.uka.ipd.sdq.workflow.Blackboard;
import de.uka.ipd.sdq.workflow.OrderPreservingBlackboardCompositeJob;

public abstract class AbstractExtensionJob<BlackboardType extends Blackboard<?>> extends OrderPreservingBlackboardCompositeJob<Blackboard<?>>{

}
