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

/**Job which creates, configures and runs an XPand generator.
 * @author Steffen Becker
 * @author groenda
 */
public class XpandGeneratorJob extends AbstractOAWWorkflowJobBridge<Generator> {

	private EPackage[] ePackages = null;
	private Outlet[] outlets;
	private String expandExpression;

	private List<String> advices = new ArrayList<String>();
	private boolean checkProtectedRegions;
	private String fileEncoding;
	private boolean beautifyCode;
	/** Definition of global variables. */
	private GlobalVarDef[] glovalVarDefs;

	/**Creates a new XPand generator job.
	 * @param slotContents Slots and their content.
	 * @param ePackages EMF Meta-models which can be used by the generator.
	 * @param outlets Outlets.
	 * @param expandExpression Initial generation expression.
	 * @param globalVarDefs Definitions for global Variables.
	 */
	public XpandGeneratorJob(HashMap<String, Object> slotContents,
			EPackage[] ePackages,
			Outlet[] outlets,
			String expandExpression, GlobalVarDef[] globalVarDefs) {
		super(new Generator(),slotContents);

		this.ePackages = ePackages;
		this.outlets = outlets;
		this.expandExpression = expandExpression;
		this.glovalVarDefs = globalVarDefs;

		this.checkProtectedRegions = false;
		this.fileEncoding = "ISO-8859-1";
		this.beautifyCode = false;
	}

	/**Creates a new XPand generator job without global variables.
	 * @param slotContents Slots and their content.
	 * @param ePackages EMF Meta-models which can be used by the generator.
	 * @param outlets Outlets.
	 * @param expandExpression Initial generation expression.
	 */
	public XpandGeneratorJob(HashMap<String, Object> slotContents,
			EPackage[] ePackages,
			Outlet[] outlets,
			String expandExpression) {
		this(slotContents, ePackages, outlets, expandExpression, null);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.sdq.workflow.mdsd.oaw.AbstractOAWWorkflowJobBridge#setupOAWJob(org.openarchitectureware.workflow.lib.AbstractWorkflowComponent2)
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
		prResolver = prResolver.substring(0,prResolver.length()-1);

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

	public List<String> getAdvices() {
		return this.advices;
	}

	public boolean isCheckProtectedRegions() {
		return checkProtectedRegions;
	}

	public void setCheckProtectedRegions(boolean checkProtectedRegions) {
		this.checkProtectedRegions = checkProtectedRegions;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public String getExpandExpression() {
		return expandExpression;
	}

	public boolean isBeautifyCode() {
		return beautifyCode;
	}

	public void setBeautifyCode(boolean beautifyCode) {
		this.beautifyCode = beautifyCode;
	}
}
