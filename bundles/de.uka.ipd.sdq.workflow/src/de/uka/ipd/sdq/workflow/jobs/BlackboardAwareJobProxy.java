package de.uka.ipd.sdq.workflow.jobs;

import java.util.Optional;
import java.util.function.Supplier;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;

/**
 * This is a Blackboard aware Job Proxy. The proxied job is still only retrieved once the proxy is
 * executed. This proxy class additionally implements the IBlackboardInteractingJob interface to
 * forward the current blackboard instance.
 * 
 * @author Sebastian Krach
 *
 * @param <BlackboardType> the blackboard type as described by {@link IBlackboardInteractingJob}
 */
public class BlackboardAwareJobProxy<BlackboardType extends Blackboard<?>> extends JobProxy
        implements IBlackboardInteractingJob<BlackboardType> {

    private Optional<BlackboardType> blackboard;

    public BlackboardAwareJobProxy(String name, Supplier<IJob> jobSupplier) {
        super(name, jobSupplier);
    }

    @Override
    public void setBlackboard(BlackboardType blackboard) {
        this.blackboard = Optional.of(blackboard);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected IJob supplyJob() {
        var job = super.supplyJob();
        if (job instanceof IBlackboardInteractingJob) {
            blackboard.ifPresent(bb -> ((IBlackboardInteractingJob<BlackboardType>) job).setBlackboard(bb));
        }
        return job;
    }

}
