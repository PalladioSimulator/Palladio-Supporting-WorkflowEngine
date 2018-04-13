package de.uka.ipd.sdq.workflow.mdsd.xtext.generator;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.mwe.utils.StandaloneSetup;
import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.generator.GeneratorComponent;
import org.eclipse.xtext.generator.GeneratorComponent.Outlet;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.mwe.AbstractReader;
import org.eclipse.xtext.mwe.Reader;
import org.eclipse.xtext.mwe.ResourceLoadingSlotEntry;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;

/**
 * A configuration for using the XText Generator.
 *
 * @author Joerg Henss
 */
public class XTextGeneratorConfiguration {

    /** The module. */
    private XTextGeneratorModule module;

    /** The setup. */
    private ISetup setup;

    /** The generator class. */
    private final Class<? extends IGenerator> generatorClass;

    /** The language name. */
    private final String languageName;

    /** The file extension. */
    private final String fileExtension;

    /** The output package name. */
    private final String outputPackageName;

    /** The model location. */
    private ModelLocation modelLocation;

    /** The slots. */
    private final Set<String> slots = new TreeSet<String>();

    /** The outlets. */
    private final Set<Outlet> outlets = new TreeSet<Outlet>();

    /** The reader pathes. */
    private final Set<String> readerPathes = new TreeSet<String>();

    /** The load entries. */
    private final Set<ResourceLoadingSlotEntry> loadEntries = new TreeSet<ResourceLoadingSlotEntry>();

    /** The generator. */
    private GeneratorComponent generator;

    /**
     * Create and init the StandaloneSetup for the MWE.
     */
    public void initMWEBean() {
        final StandaloneSetup standaloneSetup = new StandaloneSetup();
        standaloneSetup.addRegisterGeneratedEPackage(this.outputPackageName);
        standaloneSetup.setScanClassPath(true);
    }

    /**
     * Set the file path to the model directory.
     *
     * @param modelPath
     *            the new model file path
     */
    public void setModelFilePath(final String modelPath) {
        this.readerPathes.add(modelPath);
    }

    /**
     * Set the target directory for the generator.
     *
     * @param targetDir
     *            the new target dir
     */
    public void setTargetDir(final String targetDir) {
        final Outlet o = new Outlet();
        o.setPath(targetDir);
        this.outlets.add(o);
    }

    /**
     * Set the slot that is used for loading the model.
     *
     * @param slotName
     *            the new source slot
     */
    public void setSourceSlot(final String slotName) {
        this.slots.add(slotName);
        final ResourceLoadingSlotEntry loadEntry = new ResourceLoadingSlotEntry();
        loadEntry.setSlot(slotName);
        this.loadEntries.add(loadEntry);

    }

    /**
     * Gets the generator class.
     *
     * @return the generatorClass
     */
    public Class<? extends IGenerator> getGeneratorClass() {
        return this.generatorClass;
    }

    /**
     * Gets the language name.
     *
     * @return the languageName
     */
    public String getLanguageName() {
        return this.languageName;
    }

    /**
     * Gets the file extension.
     *
     * @return the fileExtension
     */
    public String getFileExtension() {
        return this.fileExtension;
    }

    /**
     * Constructor.
     *
     * @param generatorClass
     *            The generator class that is used
     * @param languageName
     *            The name of the language
     * @param fileExtension
     *            The file extension of the model
     * @param outputPackageName
     *            The package name used for output
     */
    public XTextGeneratorConfiguration(final Class<? extends IGenerator> generatorClass, final String languageName,
            final String fileExtension, final String outputPackageName) {
        super();
        this.generatorClass = generatorClass;
        this.languageName = languageName;
        this.fileExtension = fileExtension;
        this.outputPackageName = outputPackageName;
    }

    /**
     * Creates a MWE Reader for the model based on the path and slots.
     *
     * @return the reader
     */
    public Reader createReader() {
        if (this.readerPathes.isEmpty()) {
            throw new RuntimeException("XText Reader requires a path!");
        }

        final Reader reader = new Reader();
        this.setupReader(reader);

        for (final String path : this.readerPathes) {
            reader.addPath(path);
        }

        return reader;

    }

    /**
     * Sets the up reader.
     *
     * @param reader
     *            the new up reader
     */
    private void setupReader(final AbstractReader reader) {
        reader.addRegister(this.setup);
        for (final ResourceLoadingSlotEntry entry : this.loadEntries) {
            reader.addLoadResource(entry);
        }
    }

    /**
     * Creates and inits the XText generator.
     *
     * @return the generator component
     */
    public GeneratorComponent createGenerator() {
        this.generator = new GeneratorComponent();
        this.generator.setRegister(this.setup);
        for (final String slot : this.slots) {
            this.generator.addSlot(slot);
        }

        for (final Outlet outlet : this.outlets) {
            this.generator.addOutlet(outlet);
        }

        return this.generator;
    }

    /**
     * Initialises the GeneratorModule and GeneratorSetup.
     */
    public void initGeneratorModuleAndGeneratorSetup() {
        this.module = new XTextGeneratorModule(this.generatorClass, this.languageName, this.fileExtension);
        this.setup = new XTextGeneratorSetup(this.module);
    }

    /**
     * Create the GeneratorSupport.
     *
     * @return the x text generator support
     */
    public XTextGeneratorSupport createGeneratorSupport() {
        return new XTextGeneratorSupport(this.module);
    }

    /**
     * Gets the slots.
     *
     * @return the slots
     */
    public Set<String> getSlots() {
        return this.slots;
    }

    /**
     * Gets the blackboard model location.
     *
     * @return the blackboard model location
     */
    public ModelLocation getBlackboardModelLocation() {
        return this.modelLocation;
    }

    /**
     * Sets the blackboard model location.
     *
     * @param modelLocation
     *            the new blackboard model location
     */
    public void setBlackboardModelLocation(final ModelLocation modelLocation) {
        this.modelLocation = modelLocation;
    }

    /**
     * Creates the blackboard reader.
     *
     * @return the blackboard reader
     */
    public BlackboardReader createBlackboardReader() {
        if (this.modelLocation == null) {
            throw new RuntimeException("XText BlackboardReader requires a modelLocation!");
        }

        final BlackboardReader reader = new BlackboardReader(this.modelLocation);
        this.setupReader(reader);

        return reader;
    }

}
