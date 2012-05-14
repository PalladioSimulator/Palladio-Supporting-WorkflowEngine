package de.uka.ipd.sdq.workflow.mdsd.oaw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xpand2.Generator;
import org.eclipse.xpand2.output.JavaBeautifier;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.PostProcessor;
import org.eclipse.xpand2.output.XmlBeautifier;
import org.eclipse.xtend.expression.AbstractExpressionsUsingWorkflowComponent.GlobalVarDef;
import org.eclipse.xtend.typesystem.emf.EmfMetaModel;

/**
 * Job which creates, configures and runs an XPand generator.
 * 
 * @author Steffen Becker
 * @author groenda
 */
public class XpandGeneratorJob extends AbstractOAWWorkflowJobBridge<Generator> {

    /** The e packages. */
    private EPackage[] ePackages = null;

    /** The outlets. */
    private Outlet[] outlets;

    /** The expand expression. */
    private String expandExpression;

    /** The advices. */
    private List<String> advices = new ArrayList<String>();

    /** The check protected regions. */
    private boolean checkProtectedRegions;

    /** The file encoding. */
    private String fileEncoding;

    /** The beautify code. */
    private boolean beautifyCode;
    /** Definition of global variables. */
    private GlobalVarDef[] glovalVarDefs;

    /**
     * Creates a new XPand generator job.
     * 
     * @param slotContents
     *            Slots and their content.
     * @param ePackages
     *            EMF Meta-models which can be used by the generator.
     * @param outlets
     *            Outlets.
     * @param expandExpression
     *            Initial generation expression.
     * @param globalVarDefs
     *            Definitions for global Variables.
     */
    public XpandGeneratorJob(HashMap<String, Object> slotContents, EPackage[] ePackages, Outlet[] outlets,
            String expandExpression, GlobalVarDef[] globalVarDefs) {
        super(new Generator(), slotContents);

        this.ePackages = ePackages;
        this.outlets = outlets;
        this.expandExpression = expandExpression;
        this.glovalVarDefs = globalVarDefs;

        this.checkProtectedRegions = false;
        this.fileEncoding = "ISO-8859-1";
        this.beautifyCode = false;
    }

    /**
     * Creates a new XPand generator job without global variables.
     * 
     * @param slotContents
     *            Slots and their content.
     * @param ePackages
     *            EMF Meta-models which can be used by the generator.
     * @param outlets
     *            Outlets.
     * @param expandExpression
     *            Initial generation expression.
     */
    public XpandGeneratorJob(HashMap<String, Object> slotContents, EPackage[] ePackages, Outlet[] outlets,
            String expandExpression) {
        this(slotContents, ePackages, outlets, expandExpression, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.mdsd.oaw.AbstractOAWWorkflowJobBridge#setupOAWJob(org.
     * openarchitectureware.workflow.lib.AbstractWorkflowComponent2)
     */
    @Override
    protected void setupOAWJob(Generator generatorJob) {
        generatorJob.setExpand(expandExpression);
        generatorJob.setFileEncoding(fileEncoding);

        for (EPackage p : ePackages) {
            generatorJob.addMetaModel(new EmfMetaModel(p));
        }

        String prResolver = "";
        for (Outlet o : outlets) {
            generatorJob.addOutlet(o);
            prResolver += o.getPath() + ",";
        }
        prResolver = prResolver.substring(0, prResolver.length() - 1);

        if (glovalVarDefs != null) {
            for (GlobalVarDef def : glovalVarDefs) {
                generatorJob.addGlobalVarDef(def);
            }
        }

        if (this.checkProtectedRegions) {
            generatorJob.setPrSrcPaths(prResolver);
            generatorJob.setPrExcludes(".svn");
        }

        for (String advice : this.advices) {
            generatorJob.addAdvice(advice);
        }

        if (beautifyCode) {
            ArrayList<PostProcessor> beautifier = new ArrayList<PostProcessor>();
            beautifier.add(new JavaBeautifier());
            beautifier.add(new XmlBeautifier());
            generatorJob.setBeautifier(beautifier);
        }
    }

    /**
     * Gets the advices.
     * 
     * @return the advices
     */
    public List<String> getAdvices() {
        return this.advices;
    }

    /**
     * Checks if is check protected regions.
     * 
     * @return true, if is check protected regions
     */
    public boolean isCheckProtectedRegions() {
        return checkProtectedRegions;
    }

    /**
     * Sets the check protected regions.
     * 
     * @param checkProtectedRegions
     *            the new check protected regions
     */
    public void setCheckProtectedRegions(boolean checkProtectedRegions) {
        this.checkProtectedRegions = checkProtectedRegions;
    }

    /**
     * Gets the file encoding.
     * 
     * @return the file encoding
     */
    public String getFileEncoding() {
        return fileEncoding;
    }

    /**
     * Sets the file encoding.
     * 
     * @param fileEncoding
     *            the new file encoding
     */
    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    /**
     * Gets the expand expression.
     * 
     * @return the expand expression
     */
    public String getExpandExpression() {
        return expandExpression;
    }

    /**
     * Checks if is beautify code.
     * 
     * @return true, if is beautify code
     */
    public boolean isBeautifyCode() {
        return beautifyCode;
    }

    /**
     * Sets the beautify code.
     * 
     * @param beautifyCode
     *            the new beautify code
     */
    public void setBeautifyCode(boolean beautifyCode) {
        this.beautifyCode = beautifyCode;
    }
}
