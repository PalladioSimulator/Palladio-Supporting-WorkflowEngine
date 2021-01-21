package de.uka.ipd.sdq.workflow.jobs;

import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This class allows to register a job, which will be resolved shortly before it is executed.
 * 
 * @author Sebastian Krach
 *
 */
public class JobProxy implements IJob {
    
    private final String name;
    private final Supplier<IJob> jobSupplier;
    private IJob delegate = null;

    
    public JobProxy(Supplier<IJob> jobSupplier) {
        this.name = "Unresolved Job Proxy";
        this.jobSupplier = jobSupplier;
    }
    
    public JobProxy(String name, Supplier<IJob> jobSupplier) {
        this.name = name;
        this.jobSupplier = jobSupplier;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        delegate = jobSupplier.get();
        delegate.execute(monitor);
    }

    @Override
    public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
        if (delegate == null) {
            throw new CleanupFailedException("The proxy job has not been executed before.");
        }
        delegate.cleanup(monitor);
    }

    @Override
    public String getName() {
        return delegate == null ? name : delegate.getName();
    }

}
