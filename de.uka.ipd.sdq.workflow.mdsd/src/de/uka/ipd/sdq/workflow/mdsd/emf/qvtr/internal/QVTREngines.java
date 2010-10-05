package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

import de.uka.ipd.sdq.workflow.mdsd.Activator;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.AbstractQVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngine;
import de.uka.ipd.sdq.workflow.mdsd.emf.qvtr.QVTREngineFactory;

/**
 * This class enumerates all registered {@link QVTREngine}s.
 * This class provides access to the extension point.
 * 
 * @author Thomas Schuischel
 *
 */
public class QVTREngines {

	private static final String TAG_ENGINETYPE = "engineType";
	private static final String ATT_ID ="id";
	private static final String ATT_NAME ="name";
	private static final String ATT_CLASS ="class";

	private final IConfigurationElement configElement;
	private final String id;
	private final String name;
	private QVTREngineFactory factory;

	private static QVTREngines[] cachedEngineTypes;

	private QVTREngines(IConfigurationElement configurationElement, int ordinal) {
		this.configElement = configurationElement;
		id = getAttribute(configurationElement, ATT_ID, null);
		name = getAttribute(configurationElement, ATT_NAME, id);
		getAttribute(configurationElement, ATT_CLASS, null);
	}

	private static String getAttribute (IConfigurationElement configElement, String name, String defaultValue) {
		String value = configElement.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("missing " + name + " attribute");

	}
	
	///////////////////////////////////////////////////////////////////

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public AbstractQVTREngine newQVTREngine() {
		QVTREngineFactory factory = getFactory();
		if (factory == null) {
			return null;
		}
		return factory.createEngine();
	}

	private QVTREngineFactory getFactory() {
		if(factory != null) {
			return factory;
		}
		try {
			factory = (QVTREngineFactory) configElement.createExecutableExtension(ATT_CLASS);
		}
		catch (Exception e) {
			/*logger.logError("Failed to instantiate factory: "
            + configElement.getAttribute(ATT_CLASS) + " in type: " + id
            + " in plugin: "
            + configElement.getDeclaringExtension().getNamespaceIdentifier(), e);*/
		}
		return factory;
	}


	   /**
	    * Dispose of any OS resources
	    */
	   private void dispose() {
	      if (factory == null)
	         return;
	      factory.dispose();
	      factory = null;
	   }
 
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Collects all available QVT-R engines.
	 * 
	 * @return array of {@link QVTREngine}s
	 */
	public static QVTREngines[] getAllEngines() {
		if(cachedEngineTypes != null)
			return cachedEngineTypes;

		try {
			IExtension[] extensions = Platform.getExtensionRegistry()
			.getExtensionPoint(Activator.PLUGIN_ID,"qvtrengine")
			.getExtensions();
			List<QVTREngines> found = new ArrayList<QVTREngines>();
			for (int i = 0; i < extensions.length; i++) {
				IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configElements.length; j++) {
					QVTREngines proxy = parseEngineConfiguration(configElements[j], found.size());
					if(proxy != null)
						found.add(proxy);
				}
			}
			cachedEngineTypes = (QVTREngines[]) found.toArray(new QVTREngines[found.size()]);
		} catch (InvalidRegistryObjectException e) {
			//logger.logError(msg,e);
		}
		return cachedEngineTypes;
	}
	
	//
	private static QVTREngines parseEngineConfiguration(IConfigurationElement configElement, int ordinal) {
		if (!configElement.getName().equals(TAG_ENGINETYPE))
			return null;
		try{
			return new QVTREngines(configElement,ordinal);
		}
		catch (Exception e) {
			String name = configElement.getAttribute(ATT_NAME);
			if (name == null)
				name = "[missing name attribute]";
			/*String msg = "Failed to load QVTREngine named "
				+ name
				+ " in "
				+ configElement.getDeclaringExtension().getNamespaceIdentifier();
			logger.logError(msg,e);*/
			return null;
		}
	}	

	
	private static Map<String, QVTREngines> engineTypeMap = null;

	/**
	 * Answer the favorite item type that has the specified identifier
	 * 
	 * @param id
	 *           the identifier
	 * @return the type or <code>null</code> if none
	 */
	public static QVTREngines getType(String id) {
		if (engineTypeMap == null) {
			if(cachedEngineTypes == null)
				getAllEngines();
			engineTypeMap = new HashMap<String, QVTREngines>(cachedEngineTypes.length);
			for (int i = 0; i < cachedEngineTypes.length; i++) {
				QVTREngines eachType = cachedEngineTypes[i];
				engineTypeMap.put(eachType.getId(), eachType);
			}
		}
		return engineTypeMap.get(id);
	}

	/**
	 * Dispose of any OS resources. Must be called by the plugin before shutdown.
	 */
	public static void disposeTypes() {
		if (cachedEngineTypes == null)
			return;
		for (int i = 0; i < cachedEngineTypes.length; i++)
			cachedEngineTypes[i].dispose();
		cachedEngineTypes = null;
	}
}
