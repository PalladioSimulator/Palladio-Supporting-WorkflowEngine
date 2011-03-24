package de.uka.ipd.sdq.workflow.launchconfig;

/**
 * This class is used as a constant container.
 * 
 * @author Roman Andrej
 */
public class ConstantsContainer {

	// Model filenames configuration identifiers
	public static final String RESOURCETYPEREPOSITORY_FILE = "resourceTypeFile";
	public static final String RESOURCEENVIRONMENT_FILE = "resourceEnvironmentFile";
	public static final String REPOSITORY_FILE = "repositoryFile";
	public static final String SYSTEM_FILE = "systemFile";
	public static final String ALLOCATION_FILE = "allocationFile";
	public static final String USAGE_FILE = "usageFile";
	public static final String MWREPOSITORY_FILE = "mwRepositoryFile";
	public static final String EVENT_MIDDLEWARE_REPOSITORY_FILE = "eventMiddlewareRepositoryFile";
	public static final String ACCURACY_QUALITY_ANNOTATION_FILE = "accuracyQualityAnnotationFile";
	
	// Template method value
	public static final String AOP_TEMPLATE  = "aop_templates";
	
	/*
	 * Set the file extensions which the dialog will use to filter the files it
	 * shows to the argument.
	 */
	public static final String[] RESOURCETYPE_EXTENSION = new String[] { "*.resourcetype" };
	public static final String[] RESOURCEENVIRONMENT_EXTENSION = new String[] { "*.resourceenvironment" };
	public static final String[] REPOSITORY_EXTENSION = new String[] { "*.repository" };
	public static final String[] SYSTEM_EXTENSION = new String[] { "*.system" };
	public static final String[] ALLOCATION_EXTENSION = new String[] { "*.allocation" };
	public static final String[] USAGEMODEL_EXTENSION = new String[] { "*.usagemodel" };
	public static final String[] FEATURECONFIG_EXTENSION = new String[] { "*.featureconfig" };
	public static final String[] ACCURACY_QUALITY_ANNOTATION_EXTENSION = new String[] { "*.qualityannotation" };

	// Configuration tab
	public static final String PLUGIN_ID = "outpath";
	public static final String DELETE_PLUGIN = "clear";
	
	public static final String VARIABLE_TEXT = "variable";
	public static final String MINIMUM_TEXT = "minimum";
	public static final String MAXIMUM_TEXT = "maximum";
	public static final String STEP_WIDTH_TEXT = "stepwidth";
	public static final String RUN_NO = "runNo";
	
	// Reliability Sensitivity Analysis
	public static final String SENSITIVITY_ACTIVE = "sensitivityActive";
	public static final String TEMPORARY_MODELS_PATH = "temporaryModelsPath";
	
	// Output path settings
	public static final String EAROUT_PATH = "earOutpath";
	public static final String INTERFACESOUT_PATH = "interfacesOutpath";
	public static final String CLIENTOUT_PATH = "clientOutpath";
	public static final String EJBSOUT_PATH = "ejbsOutpath";
	
	public static final String MODEL_TO_TEXT_CHOICE = "modelToTextTarget";
	public static final String MODEL_TO_TEXT_TARGET_EJB = "Model Target EJB";
	public static final String MODEL_TO_TEXT_TARGET_PROTO = "Model Target Protocom";
	
	// Simulation features
	public static final String FEATURE_CONFIG = "featureConfig";
	public static final String FEATURE_CONFIG_TARGET = "featureConfigTarget";
	public static final String FEATURE_FILE = "featureFile";
	public static final String SIMULATE_LINKING_RESOURCES = "simulateLinkingResources";
	public static final String SIMULATE_FAILURES = "simulateFailures";
	public static final String SIMULATE_ACCURACY = "simulateAccuracy";
	
	// Default values
	public static String SIMUCOM_DEFAULT_PROJECT_ID = "de.uka.ipd.sdq.codegen.simucominstance";
	/** Default URI of the feature configuration file. */
	public static final String DEFAULT_FEATURE_CONFIGURATION_FILE = "pathmap://PCM_MODELS/ConnectorConfig.featureconfig";
	/** Default URI of the quality annotation file used for accuracy analysis . */
	public static final String DEFAULT_ACCURACY_QUALITY_ANNOTATION_FILE = "";
	/** Default URI of the event middleware repository file. */
	public static final String DEFAULT_EVENT_MIDDLEWARE_FILE = "pathmap://PCM_MODELS/default_event_middleware.repository";
	//BRG
	// private static final String PCM_RESOURCETYPE_FILE_URI = "pathmap://PCM_MODELS/Palladio.resourcetype";
	/** Default URI of the middleware repository file. */
	public static final String DEFAULT_MIDDLEWARE_REPOSITORY_FILE = "pathmap://PCM_MODELS/Glassfish.repository";
	/** Default URI of the allocation model file. */
	public static final String DEFAULT_ALLOCATION_FILE = "";
	/** Default URI of the usage model file. */
	public static final String DEFAULT_USAGE_FILE = "";
}
