package de.uka.ipd.sdq.workflow.mdsd.emf.qvto.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;

import de.uka.ipd.sdq.workflow.mdsd.Activator;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvto.QVTOResult;

/**
 * The Class QVTOExecutor.
 */
@SuppressWarnings("restriction")
public class QVTOExecutor {

    /**
     * Execute.
     *
     * @param transformationURI
     *            the transformation uri
     * @param inoutParameter
     *            the inout parameter
     * @return the qVTO result
     */
    public static QVTOResult execute(final URI transformationURI, final List<EObject>[] inoutParameter) {
        return QVTOExecutor.execute(transformationURI, new HashMap<String, Object>(), inoutParameter);
    }

    /**
     * Execute.
     *
     * @param transformationURI
     *            the transformation uri
     * @param parameters
     *            the parameters
     * @param inoutParameter
     *            the inout parameter
     * @return the qVTO result
     */
    public static QVTOResult execute(final URI transformationURI, final Map<String, Object> parameters,
            final List<EObject>[] inoutParameter) {
        // create executor for the given transformation
        final TransformationExecutor executor = new TransformationExecutor(transformationURI);

        // create the input extent with its initial contents
        final ModelExtent[] transformationParameter = new ModelExtent[inoutParameter.length];
        for (int i = 0; i < inoutParameter.length; i++) {
            transformationParameter[i] = new BasicModelExtent();
            if (inoutParameter[i] != null) {
                final ArrayList<EObject> contents = new ArrayList<EObject>();
                contents.addAll(inoutParameter[i]);
                transformationParameter[i].setContents(contents);
            }
        }

        // setup the execution environment details ->
        // configuration properties, logger, monitor object etc.
        final ExecutionContextImpl context = new ExecutionContextImpl();
        for (final Map.Entry<String, Object> parameter : parameters.entrySet()) {
            context.setConfigProperty(parameter.getKey(), parameter.getValue());
        }
        context.setLog(new QVTOLogger());

        // run the transformation assigned to the executor with the given
        // input and output and execution context -> ChangeTheWorld(in, out)
        // Remark: variable arguments count is supported
        final ExecutionDiagnostic result = executor.execute(context, transformationParameter);

        QVTOResult qvtResult = null;
        // check the result for success
        if (result.getSeverity() != Diagnostic.OK) {
            qvtResult = new QVTOResult(result);
            // turn the result diagnostic into status and send it to error log
            final IStatus status = BasicDiagnostic.toIStatus(result);
            log(status);
        } else {
            qvtResult = new QVTOResult(result, executor.getLastTrace());
            for (int i = 0; i < inoutParameter.length; i++) {
                inoutParameter[i] = transformationParameter[i].getContents();
            }
        }

        return qvtResult;
    }
    
    public static void log(IStatus status) {
    	Optional<ILog> bundleLogger = Optional.ofNullable(Activator.getDefault()).map(Plugin::getLog);
    	if (bundleLogger.isPresent()) {
    		bundleLogger.get().log(status);
    	} else {
    		new QVTOLogger().log(Level.ERROR.toInt(), status.getMessage());
    	}
    }
}
