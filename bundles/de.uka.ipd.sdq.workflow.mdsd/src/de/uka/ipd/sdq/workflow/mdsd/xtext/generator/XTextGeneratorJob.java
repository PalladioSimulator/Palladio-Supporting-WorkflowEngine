package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.emf.mwe2.runtime.workflow.WorkflowContextImpl;
import org.eclipse.xtext.generator.GeneratorComponent;
import org.eclipse.xtext.mwe.Reader;

import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2SequentialJob;
import de.uka.ipd.sdq.workflow.mdsd.xtext.MWE2WorkflowComponentBridge;

/**
 * XTextGeneratorCompositeJob for using XText Generators in the WFE.
 *
 * @author Joerg Henss
 */
public class XTextGeneratorJob extends MWE2SequentialJob {

    /**
     * Instantiates a new x text generator composite job.
     *
     * @param config
     *            the config
     */
    public XTextGeneratorJob(final XTextGeneratorConfiguration config) {
        this(config, "XTextGenerator Job", true);
    }

    /**
     * Instantiates a new x text generator composite job.
     *
     * @param config
     *            the config
     * @param name
     *            The name of the job sequence.
     */
    public XTextGeneratorJob(final XTextGeneratorConfiguration config, final String name) {
        this(config, name, true);
    }

    /**
     * Instantiates a new x text generator composite job.
     *
     * @param config
     *            the config
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     */
    public XTextGeneratorJob(final XTextGeneratorConfiguration config, final boolean cleanUpImmediately) {
        this(config, "XTextGenerator Job", cleanUpImmediately);
    }

    /**
     * Instantiates a new x text generator composite job.
     *
     * @param config
     *            the config
     * @param cleanUpImmediately
     *            Flag if jobs should be cleaned up immediately or not.
     * @param name
     *            The name of the job sequence.
     */
    public XTextGeneratorJob(final XTextGeneratorConfiguration config, final String name,
            final boolean cleanUpImmediately) {

        super(name, cleanUpImmediately);

        // Setup all
        config.initMWEBean();
        // Create context
        final IWorkflowContext ctx = new WorkflowContextImpl();
        config.initGeneratorModuleAndGeneratorSetup();

        this.addJob(new MWE2WorkflowComponentBridge<XTextGeneratorSupport>(config.createGeneratorSupport(), ctx,
                "XTextGeneratorSupport Job"));
        this.addJob(new MWE2WorkflowComponentBridge<Reader>(config.createReader(), ctx, "MWEReader Job"));
        this.addJob(new MWE2WorkflowComponentBridge<GeneratorComponent>(config.createGenerator(), ctx,
                "XTextGenerator Job"));

    }

}
