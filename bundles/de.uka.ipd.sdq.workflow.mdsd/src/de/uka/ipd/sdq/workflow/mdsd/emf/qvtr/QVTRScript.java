package de.uka.ipd.sdq.workflow.mdsd.emf.qvtr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * This Class encapsulates a QVT-R script. It holds a QVT-R script file, its needed meta models, a
 * transformation and a direction. A QVT-R transformation always consists of a transformation name
 * and a direction to execute. The available transformations and directions of a given QVT-R script
 * file can be queried from a {@link QVTRScriptInfo}.
 *
 * To execute a QVT-R transformation the meta models used by the transformation needed to be known.
 *
 * @author Thomas Schuischel
 *
 */
public class QVTRScript {

    /**
     * Path to the QVT script file.
     */
    private String qvtScriptFile;
    /**
     * Name of the Transformation to execute.
     */
    private String transformationName;
    /**
     * Name of the direction for the transformation.
     */
    private String transformationDirection;
    /**
     * Collection of the meta models needed by the transformation.
     */
    private Collection<Object> metaModels;
    /**
     * log4j logger.
     */
    private final Logger logger = Logger.getLogger(QVTRScript.class);

    /**
     * Create a new empty QVTRScript.
     */
    public QVTRScript() {
        this.qvtScriptFile = "";
        this.transformationName = "";
        this.transformationDirection = "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.qvtScriptFile;
    }

    /**
     * Returns the transformation name.
     * 
     * @return name of the transformation
     */
    public String getTransformationName() {
        return this.transformationName;
    }

    /**
     * Returns the name of the transformation direction.
     * 
     * @return name of the direction
     */
    public String getTransformationDirection() {
        return this.transformationDirection;
    }

    /**
     * Sets the name of a transformation.
     * 
     * @param transformationName
     *            name of a transformation
     */
    public void setTransformationName(final String transformationName) {
        this.transformationName = transformationName;
    }

    /**
     * Sets the name of a transformation direction.
     * 
     * @param transformationDirection
     *            name of transformation direction
     */
    public void setTransformationDirection(final String transformationDirection) {
        this.transformationDirection = transformationDirection;
    }

    /**
     * Returns a new Reader to the QVT script file.
     * 
     * @return a {@link Reader} to the content of the QVT script file
     */
    public Reader toReader() {
        return new StringReader(this.qvtScriptFile);
    }

    /**
     * Sets the QVT script file. The script file is read into the internal buffer.
     * 
     * @param fileName
     *            path to the QVT script file
     */
    public void setQVTFile(final String fileName) {
        URI qvturl = null;
        try {
            qvturl = URI.createURI(fileName);

        } catch (final IllegalArgumentException e) {
            this.logger.error("Wrong URI format.", e);
        }
        try {
            InputStream stream = null;

            if (qvturl.scheme().startsWith("platform")) {
                stream = URIConverter.INSTANCE.createInputStream(qvturl);
            } else {
                qvturl = CommonPlugin.resolve(qvturl);
                stream = new FileInputStream(qvturl.toFileString());
            }

            final BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            final StringBuffer contentOfFile = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                contentOfFile.append(line);
                contentOfFile.append("\n");
            }
            this.qvtScriptFile = contentOfFile.toString();

        } catch (final FileNotFoundException fileNotFoundException) {
            // logger.error("QVT file not found!",fileNotFoundException);
            return;
        } catch (final IOException ioException) {
            this.logger.info("Error reading the QVT file.", ioException);
        }
    }

    /**
     * Sets the meta models needed by the transformation.
     * 
     * @param metaModels
     *            a {@link Collection} of needed meta models
     */
    public void setMetaModels(final Collection<Object> metaModels) {
        this.metaModels = metaModels;
    }

    /**
     * Returns a {@link Collection} of meta models that is currently connected to the tranformation.
     * 
     * @return {@link Collection} of current meta models
     */
    public Collection<Object> getMetaModels() {
        return this.metaModels;
    }

}
