package de.uka.ipd.sdq.workflow.mdsd.core.emf.qvto;

import java.util.Map;

import org.eclipse.emf.common.util.URI;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;

/**
 * Configuration class of the {@link QVTOTransformationJob}. The QVTO transformation job reads a
 * QVTO script, parses it and executes it. It takes its input from a {@link MDSDBlackboard} where it
 * also stores its output later. The transformation is allowed to use configuration parameters.
 *
 * @author Steffen Becker
 */
public class QVTOTransformationJobConfiguration {

    /** The opts. */
    private Map<String, Object> opts;

    /** The inout models. */
    private ModelLocation[] inoutModels;

    /** The script file. */
    private URI scriptFile;

    /** The trace model location. */
    private ModelLocation traceLocation;

    /**
     * Gets the options.
     *
     * @return the options
     */
    public Map<String, Object> getOptions() {
        return this.opts;
    }

    /**
     * Sets the options.
     *
     * @param opts
     *            the opts
     */
    public void setOptions(final Map<String, Object> opts) {
        this.opts = opts;
    }

    /**
     * Gets the inout models.
     *
     * @return the inoutModels
     */
    public ModelLocation[] getInoutModels() {
        return this.inoutModels;
    }

    /**
     * Set the location of the models used as parameters for the QVTO transformation. The model need
     * to be located on the blackboard. They are allowed to reside only in memory but need at least
     * to have a URI. Also the order of the models in the array has to match exactly the order of
     * the parameters of the QVTO transformation. It is necessary to have an entry for all
     * parameters, i.e., in, out, and inout
     *
     * @param inoutModels
     *            A list of models used as parameter for the transformation
     */
    public void setInoutModels(final ModelLocation[] inoutModels) {
        this.inoutModels = inoutModels;
    }

    /**
     * Gets the script file uri.
     *
     * @return the scriptFile
     */
    public URI getScriptFileURI() {
        return this.scriptFile;
    }

    /**
     * Sets the script file uri.
     *
     * @param scriptFile
     *            the scriptFile to set
     */
    public void setScriptFileURI(final URI scriptFile) {
        this.scriptFile = scriptFile;
    }

    /**
     * Sets the trace model location.
     *
     * @param traceFilePath
     *            the new trace model location
     */
    public void setTraceLocation(final ModelLocation traceLocation) {
        this.traceLocation = traceLocation;
    }

    /**
     * Gets the trace model location.
     *
     * @return the trace model location
     */
    public ModelLocation getTraceLocation() {
        return this.traceLocation;
    }
}
