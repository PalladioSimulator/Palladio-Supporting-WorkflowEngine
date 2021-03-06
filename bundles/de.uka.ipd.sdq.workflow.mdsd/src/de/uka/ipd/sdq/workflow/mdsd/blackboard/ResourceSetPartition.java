package de.uka.ipd.sdq.workflow.mdsd.blackboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Implementation of a blackboard partition based on EMF Resource Sets. Each partition has a
 * resource set configured for the given EPackages. It can load a set of model resources.
 * Inter-Model links are resolved up to the boundaries of the underlying resource set
 *
 * @author Steffen Becker
 */
public class ResourceSetPartition {

    /** Logger of this class. */
    private final Logger logger = Logger.getLogger(ResourceSetPartition.class);

    /** Common EMF resource set for all models in this partition. */
    protected ResourceSet rs = new ResourceSetImpl();

    /**
     * Gets the resource set.
     *
     * @return Returns the internal used resource set of this blackboard partition
     */
    public ResourceSet getResourceSet() {
        return this.rs;
    }

    /**
     * Initialize the EPackages of the models to be stored in this blackboard partition.
     *
     * @param ePackages
     *            The list of EPackages which form the meta-model of the model stored in this model
     *            partition
     */
    public void initialiseResourceSetEPackages(final EPackage[] ePackages) {
        for (final EPackage ePackage : ePackages) {
            this.rs.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
        }
    }

    /**
     * Load the model with the given URI into this blackboard partition.
     *
     * @param modelURI
     *            The URI of the model to be loaded
     * @return The loaded resource
     */
    public Resource loadModel(final URI modelURI) {
        return this.rs.getResource(modelURI, true);
    }

    /**
     * Returns all top level EObjects of the given model stored under the given URI. The model has
     * to be created first.
     *
     * @param modelURI
     *            The model URI
     * @return All top level elements of the model. Empty list if none have been found.
     */
    public List<EObject> getContents(final URI modelURI) {
        final Resource r = this.rs.getResource(modelURI, false);
        if (r == null) {
            throw new IllegalArgumentException("Model with URI " + modelURI + " must be loaded first");
        }
        return r.getContents();
    }

    /**
     * Returns the first top level EObject of the given model stored under the given URI. The model
     * has to be created first and it has to have at least one top level element
     *
     * @param modelURI
     *            The model URI
     * @return The first top level element of the model
     */
    public EObject getFirstContentElement(final URI modelURI) {
        return this.getContents(modelURI).get(0);
    }

    /**
     * Load the model with the given URI into this blackboard partition.
     *
     * @param modelURI
     *            The URI of the model to be loaded
     * @return The loaded resource
     * @deprecated Use loadModel taking a URI instead
     */
    @Deprecated
    public Resource loadModel(final String modelURI) {
        Resource r;
        if (URI.createURI(modelURI).isPlatform() || modelURI.indexOf("://") >= 0) {
            r = this.loadModel(URI.createURI(modelURI));
        } else {
            r = this.loadModel(URI.createFileURI(modelURI));
        }
        return r;
        // BRG 07.12.09
        // java.util.Map<EObject,Collection<EStructuralFeature.Setting>> map =
        // EcoreUtil.CrossReferencer.find(Collections.singleton(r.getContents().get(0)));
        // EcoreUtil.resolveAll(r);

    }

    /**
     * Resolve all model proxies, i.e., load all dependent models.
     */
    public void resolveAllProxies() {
        ArrayList<Resource> currentResources = null;
        // The resolveAll() call is not recursive; thus we have to repeat
        // the thing until the resource set does not grow any more:
        do {
            // Copy list to avoid concurrent modification exceptions:
            currentResources = new ArrayList<Resource>(this.rs.getResources());
            // TODO: check if this loop can be replaced through a single call
            // to EcoreUtil.resolveAll(rs). Maybe this is even already
            // recursive,
            // i.e. we can eliminate the outer do..while loop?
            for (final Resource r : currentResources) {
                EcoreUtil.resolveAll(r);
            }
        } while (currentResources.size() != this.rs.getResources().size());
    }

    /**
     * Replaces the contents of the given model with newContents.
     *
     * @param modelID
     *            The model extent whose contents get replaced
     * @param newContents
     *            The new contents
     */
    public void setContents(final URI modelID, final List<EObject> newContents) {
        if (!this.hasModel(modelID)) {
            this.getResourceSet().createResource(modelID);
        }
        final Resource r = this.getResourceSet().getResource(modelID, false);
        if (newContents != r.getContents()) {
            r.getContents().clear();
            r.getContents().addAll(newContents);
        }
    }

    /**
     * Replaces the contents of the given model with newContents.
     *
     * @param modelID
     *            The model extent whose contents get replaced
     * @param newContents
     *            The new contents, single object
     */
    public void setContents(final URI modelID, final EObject newContents) {

        final ArrayList<EObject> list = new ArrayList<EObject>();
        list.add(newContents);
        this.setContents(modelID, list);
    }

    /**
     * Determines if the Resource referenced by the parameter contains an EMF model.
     *
     * @param modelURI
     *            URI of the Resource.
     * @return {@code true} if, and only if, an EMF model is contained.
     */
    public boolean hasModel(final URI modelURI) {
        return this.rs.getResource(modelURI, false) != null;
    }

    /**
     * Stores all resources in the resource set.
     *
     * @throws IOException
     *             If saving leads to I/O-Errors.
     */
    public void storeAllResources() throws IOException {
        this.storeAllResources(null);
    }

    /**
     * Stores all resources in the resource set.
     *
     * @param saveOptions
     *            Save options to use
     * @throws IOException
     *             IOException If saving leads to I/O-Errors.
     */
    public void storeAllResources(final Map<String, Object> saveOptions) throws IOException {
        for (final Resource r : this.rs.getResources()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Save resource " + r.getURI());
            }
            if (r.getURI().isFile() || r.getURI().isPlatformResource()) {
                r.save(saveOptions);
            }
        }
    }

    /**
     * Helper to find root objects of a specified class.
     *
     * @param <T>
     *            the generic type
     * @param targetType
     *            the target type
     * @return The list of found root elements. Empty list if none have been found.
     */
    public <T extends EObject> List<T> getElement(final EClass targetType) {
        final ArrayList<T> result = new ArrayList<T>();
        for (final Resource r : this.rs.getResources()) {
            if (this.isTargetInResource(targetType, r)) {
            	result.addAll(EcoreUtil.<T>getObjectsByType(r.getContents(), targetType));
            }
        }
        
        return result;
    }

    private boolean isTargetInResource(final EClass targetType, final Resource r) {
    	if (r != null) {
	    	for (EObject c : r.getContents()) {
	    		if (targetType.isSuperTypeOf(c.eClass())) {
	    			return true;
	    		}
	    	}
    	}
    	return false;
    }

    /**
     * Helper to determine whether root objects of a specified class exist.
     *
     * @param targetType
     *            the target type
     * @return true if elements have been found; false otherwise.
     */
    public boolean hasElement(final EClass targetType) {
        for (final Resource r : this.rs.getResources()) {
            if (this.isTargetInResource(targetType, r)) {
                return true;
            }
        }
        return false;
    }
}
