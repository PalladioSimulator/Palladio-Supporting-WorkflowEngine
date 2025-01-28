package de.uka.ipd.sdq.workflow.mdsd.oaw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.issues.IssuesImpl;
import org.eclipse.emf.mwe.core.issues.MWEDiagnostic;
import org.eclipse.xtend.check.CheckFacade;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.ExecutionContextImpl;
import org.eclipse.xtend.typesystem.emf.EmfMetaModel;

import de.uka.ipd.sdq.errorhandling.core.SeverityAndIssue;
import de.uka.ipd.sdq.errorhandling.core.SeverityEnum;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import de.uka.ipd.sdq.workflow.mdsd.validation.ModelValidationJob;

/**
 * Execute a model validation check using a given oAW check file. The oAW check language allows
 * powerfull specification of model validation rules similar to OCL constraints.
 *
 * @author Steffen Becker
 */
public class PerformOAWCheckValidation extends ModelValidationJob {

    /** The blackboard. */
    private MDSDBlackboard blackboard;

    /** The ctx. */
    private ExecutionContextImpl ctx;

    /** The partition name. */
    private final String partitionName;

    /** The check filename. */
    private final String checkFilename;

    /** The e packages. */
    private final EPackage[] ePackages;

    /**
     * Create a new oAW check job.
     *
     * @param partitionName
     *            The blackboard model partition containing the model to validate
     * @param checkFilename
     *            The URL of the check file containing the rules for well-formed models
     * @param packages
     *            The EPackages used in the model to be checked
     */
    public PerformOAWCheckValidation(final String partitionName, final String checkFilename,
            final EPackage[] packages) {
        super(SeverityEnum.ERROR);

        this.partitionName = partitionName;
        this.checkFilename = checkFilename;
        this.ePackages = packages;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.codegen.workflow.IJob#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {

        final Issues issues = new IssuesImpl();

        final ResourceSetPartition partition = this.blackboard.getPartition(this.partitionName);
        partition.resolveAllProxies();

        for (final Resource r : partition.getResourceSet().getResources()) {
            // Check resource with oAW's check language
            CheckFacade.checkAll(this.checkFilename, this.getElementsInResource(r), this.getExecutionContext(), issues);
        }

        this.setJobResult(this.getSeverityAndIssues(issues));
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.codegen.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        return "Checking oAW constraints";
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uka.ipd.sdq.workflow.IBlackboardInteractingJob#setBlackboard(de.uka.ipd.sdq.workflow.
     * Blackboard)
     */
    @Override
    public void setBlackboard(final MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }

    /**
     * Gets the elements in resource.
     *
     * @param resource
     *            the resource
     * @return the elements in resource
     */
    private Collection<EObject> getElementsInResource(final Resource resource) {
        final TreeIterator<EObject> items = resource.getAllContents();
        final ArrayList<EObject> result = new ArrayList<EObject>();
        for (; items.hasNext();) {
            result.add(items.next());
        }

        return result;
    }

    /**
     * Gets the execution context.
     *
     * @return the execution context
     */
    private ExecutionContext getExecutionContext() {
        if (this.ctx == null) {
            this.ctx = new ExecutionContextImpl();
            final List<EPackage> l = this.getMetamodelPackages();
            for (EPackage pkg : l) {
                this.ctx.registerMetaModel(new EmfMetaModel(pkg));
            }
        }
        return this.ctx;
    }

    /**
     * Gets the severity and issues.
     *
     * @param issues
     *            the issues
     * @return the severity and issues
     */
    private List<SeverityAndIssue> getSeverityAndIssues(final Issues issues) {
        final ArrayList<SeverityAndIssue> result = new ArrayList<SeverityAndIssue>();
        for (final MWEDiagnostic issue : issues.getErrors()) {
            if (issue.getElement() instanceof EObject) {
                result.add(new SeverityAndIssue(SeverityEnum.ERROR, issue.getMessage(), (EObject) issue.getElement()));
            } else if (issue.getElement() == null) {
                result.add(new SeverityAndIssue(SeverityEnum.ERROR, issue.getMessage(), null));
            } else {
                result.add(new SeverityAndIssue(SeverityEnum.ERROR, issue.getMessage() + issue.getElement().toString(),
                        null));
            }
        }

        for (final MWEDiagnostic issue : issues.getWarnings()) {
            result.add(new SeverityAndIssue(SeverityEnum.WARNING, issue.getMessage(), (EObject) issue.getElement()));
        }
        return result;
    }

    /**
     * Gets the metamodel packages.
     *
     * @return the metamodel packages
     */
    private List<EPackage> getMetamodelPackages() {
        return Arrays.asList(this.ePackages);
    }
}
