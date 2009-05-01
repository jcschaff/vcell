    package org.vcell.sbml.vcell;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.sbml.libsbml.ASTNode;
import org.sbml.libsbml.AssignmentRule;
import org.sbml.libsbml.InitialAssignment;
import org.sbml.libsbml.RateRule;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.libsbml;
import org.vcell.util.PropertyLoader;
import org.vcell.util.TokenMangler;

import cbit.util.xml.XmlUtil;
import cbit.vcell.parser.Expression;
import cbit.vcell.resource.ResourceUtil;
/**
 * Insert the type's description here.
 * Creation date: (4/11/2006 11:35:34 AM)
 * @author: Jim Schaff
 */
public class MathModel_SBMLExporter {
	static
	{
		ResourceUtil.loadlibSbmlLibray();
	}

/**
 * Insert the method's description here.
 * Creation date: (4/11/2006 11:38:26 AM)
 * @return org.sbml.libsbml.Model
 * @param mathModel cbit.vcell.mathmodel.MathModel
 */
public static org.sbml.libsbml.SBMLDocument getSBML(cbit.vcell.mathmodel.MathModel mathModel) throws cbit.vcell.parser.ExpressionException, java.io.IOException {

	if (mathModel.getMathDescription().isSpatial()){
		throw new RuntimeException("spatial models export to SBML not supported");
	}

	if (mathModel.getMathDescription().hasFastSystems()){
		throw new RuntimeException("math models with fast systems cannot be exported to SBML");
	}

	if (mathModel.getMathDescription().isStoch()){
		throw new RuntimeException("stochastic math models cannot be exported to SBML");
	}

	if (!mathModel.getMathDescription().isValid()){
		throw new RuntimeException("math model has an invalid Math Description, cannot export to SBML");
	}

	String dummyID = "ID_0";
	String compartmentId = "compartment";
	SBMLDocument document = new SBMLDocument();
	document.setId(mathModel.getName());
	org.sbml.libsbml.Model model = document.createModel();
	model.setId(mathModel.getName());
	org.sbml.libsbml.Compartment compartment = model.createCompartment();
	compartment.setId(compartmentId);

	cbit.vcell.math.MathDescription mathDesc = mathModel.getMathDescription();
	java.util.Enumeration enumVars = mathDesc.getVariables();
	
	while (enumVars.hasMoreElements()){
		cbit.vcell.math.Variable vcVar = (cbit.vcell.math.Variable)enumVars.nextElement();
		//
		// Variables map to species
		// Constants (that are numeric) map to parameters with value
		// Constants and Functions that are not numeric map to Parameters with assignment rules
		// ODEs map to rate rules.
		//
		if (vcVar instanceof cbit.vcell.math.VolVariable){
			//
			// skip for now, define later when defining ODEEquations.
			//
//			org.sbml.libsbml.Species species = model.createSpecies();
//			species.setId(vcVar.getName());
//			species.setCompartment(compartmentId);
		}else if (vcVar instanceof cbit.vcell.math.Constant && ((cbit.vcell.math.Constant)vcVar).getExpression().isNumeric()){
			org.sbml.libsbml.Parameter param = model.createParameter();
			param.setId(vcVar.getName());
			param.setConstant(true);
			param.setValue(vcVar.getExpression().evaluateConstant());
		}else if (vcVar instanceof cbit.vcell.math.Constant || vcVar instanceof cbit.vcell.math.Function) {
			org.sbml.libsbml.Parameter param = model.createParameter();
			param.setId(vcVar.getName());
			param.setConstant(false);
			//
			// Function or Constant with expressions - create assignment rule and add to model.
			//
			ASTNode mathNode = getFormulaFromExpression(vcVar.getExpression());
			AssignmentRule assignmentRule = model.createAssignmentRule();
			dummyID = TokenMangler.getNextEnumeratedToken(dummyID);
			assignmentRule.setId(dummyID);
			assignmentRule.setVariable(vcVar.getName());
			
			assignmentRule.setMath(mathNode);
			// Create a parameter for this function/non-numeric constant, set its value to be 'not-constant', 
			// add to model.
		}
	}
	
	cbit.vcell.math.CompartmentSubDomain subDomain = (cbit.vcell.math.CompartmentSubDomain)mathDesc.getSubDomains().nextElement();
//	System.out.println(model.toSBML());
	java.util.Enumeration enumEqu = subDomain.getEquations();
	
	while (enumEqu.hasMoreElements()){
		cbit.vcell.math.Equation equ = (cbit.vcell.math.Equation)enumEqu.nextElement();
		if (equ instanceof cbit.vcell.math.OdeEquation){
			// For ODE equations, add the ode variable as a parameter, add rate as a rate rule and init condition as an initial assignment rule.
			org.sbml.libsbml.Parameter param = model.createParameter();
			param.setId(equ.getVariable().getName());
			param.setConstant(false);
			
			// try to obtain the constant to which the init expression evaluates.
			RateRule rateRule = model.createRateRule();
			rateRule.setVariable(equ.getVariable().getName());
			rateRule.setMath(getFormulaFromExpression(equ.getRateExpression()));

			InitialAssignment initialAssignment = model.createInitialAssignment();
			dummyID = TokenMangler.getNextEnumeratedToken(dummyID);
			initialAssignment.setId(dummyID);
			initialAssignment.setMath(getFormulaFromExpression(equ.getInitialExpression()));
			initialAssignment.setSymbol(equ.getVariable().getName());
		 }else{
		 	throw new RuntimeException("equation type "+equ.getClass().getName()+" not supported");
		 }
	}

	return document;
}

/**
 * 	getFormulaFromExpression : 
 *  Expression infix strings are not handled gracefully by libSBML, esp when ligical or inequality operators are used.
 *  This method 
 *		converts the expression into MathML using ExpressionMathMLPrinter;
 *		converts that into libSBMl-readable formula using libSBML utilties.
 *		returns the new formula string.
 *  
 */
public static ASTNode getFormulaFromExpression(Expression expression) { 
	// Convert expression into MathML string
	String expMathMLStr = null;

	try {
		expMathMLStr = cbit.vcell.parser.ExpressionMathMLPrinter.getMathML(expression, false);
	} catch (java.io.IOException e) {
		e.printStackTrace(System.out);
		throw new RuntimeException("Error converting expression to MathML string :" + e.getMessage());
	} catch (cbit.vcell.parser.ExpressionException e1) {
		e1.printStackTrace(System.out);
		throw new RuntimeException("Error converting expression to MathML string :" + e1.getMessage());
	}
	
	// Use libSBMl routines to convert MathML string to MathML document and a libSBML-readable formula string

	ASTNode mathNode = libsbml.readMathMLFromString(expMathMLStr);
	return mathNode.deepCopy();
}


/**
 * Insert the method's description here.
 * Creation date: (4/11/2006 12:46:12 PM)
 */
public static void main(String[] args) {
	try {
		if (args.length!=2){
			System.out.println("Usage: MathModel_SBMLExporter inputVCMLFile outputSBMLFile");
			System.exit(0);
		}
		String inputVCMLFileName = args[0];
		String outputSBMLFileName = args[1];
		String vcml = cbit.util.xml.XmlUtil.getXMLString(inputVCMLFileName);
		cbit.vcell.mathmodel.MathModel mathModel = cbit.vcell.xml.XmlHelper.XMLToMathModel(vcml);
		org.sbml.libsbml.SBMLDocument sbmlDoc = getSBML(mathModel);
		org.sbml.libsbml.SBMLWriter sbmlWriter = new org.sbml.libsbml.SBMLWriter();
		sbmlWriter.setProgramName("Virtual Cell");
		String vcellVersion = PropertyLoader.getProperty(PropertyLoader.vcellSoftwareVersion, "unknown");
		sbmlWriter.setProgramVersion(vcellVersion);
		String sbmlString = sbmlWriter.writeToString(sbmlDoc);
		java.io.FileWriter fileWriter = new java.io.FileWriter(new java.io.File(outputSBMLFileName));
		fileWriter.write(sbmlString);
		fileWriter.flush();
	} catch (Throwable e) {
		e.printStackTrace(System.out);
	}
}
}