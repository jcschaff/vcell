package cbit.vcell.solvers;

import cbit.vcell.math.*;
import cbit.vcell.parser.*;
import cbit.vcell.solver.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CppClassCoderContourVarContext extends CppClassCoderAbstractVarContext {
	protected CompartmentSubDomain compartmentSubDomain = null;

/**
 * VarContextCppCoder constructor comment.
 * @param name java.lang.String
 */
protected CppClassCoderContourVarContext(CppCoderVCell argCppCoderVCell,
												Equation argEquation,
												FilamentSubDomain argFilamentSubDomain,
												Simulation argSimulation, 
												String argParentClass) throws Exception
{
	super(argCppCoderVCell,argEquation,argFilamentSubDomain,argSimulation,argParentClass);
	this.compartmentSubDomain = argFilamentSubDomain.getOutsideCompartment();
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.model.Feature
 */
public CompartmentSubDomain getCompartmentSubDomain() {
	return compartmentSubDomain;
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.model.Feature
 */
public FilamentSubDomain getFilamentSubDomain() {
	return (FilamentSubDomain)getSubDomain();
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeConstructor(java.io.PrintWriter out) throws Exception {
	out.println(getClassName()+"::"+getClassName()+"(Contour *Acontour,Feature *Afeature,CString AspeciesName)");
	out.println(": "+getParentClassName()+"(Afeature,AspeciesName)");
	out.println("{");
	try {
		double value = getEquation().getInitialExpression().evaluateConstant();
		out.println("\tinitialValue = new double;");
		out.println("\t*initialValue = "+value+";");
	}catch (Exception e){
		out.println("\tinitialValue = NULL;");
	}	
	out.println();
	Variable requiredVariables[] = getRequiredVariables();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		if (var instanceof VolVariable || var instanceof FilamentVariable){
			out.println("\t" + CppClassCoder.getEscapedFieldVariableName_C(var.getName())+" = NULL;");
		}
	}		  	
	out.println("}");
}


/**
 * This method was created by a SmartGuide.
 * @param printWriter java.io.PrintWriter
 */
public void writeDeclaration(java.io.PrintWriter out) throws Exception {
	out.println("//---------------------------------------------");
	out.println("//  class " + getClassName());
	out.println("//---------------------------------------------");

	out.println("class " + getClassName() + " : public " + getParentClassName());
	out.println("{");
	out.println("public:");
	out.println("\t" + getClassName() + "(Contour *contour, Feature *feature, CString speciesName);");
	out.println("\tvirtual void resolveReferences(Simulation *sim);");

	try {
		double value = getEquation().getInitialExpression().evaluateConstant();
	}catch (Exception e){
		out.println("\tvirtual double getInitialValue(ContourElement *contourElement);");
	}
	out.println("protected:");
	out.println("\tvirtual double getContourReactionRate(ContourElement *memElement);");
	out.println("\tvirtual double getContourDiffusionRate(ContourElement *memElement);");
	out.println();
	out.println("private:");
	Variable requiredVariables[] = getRequiredVariables();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		String mangledVarName = CppClassCoder.getEscapedFieldVariableName_C(var.getName());
		if (var instanceof FilamentVariable){
			out.println("\tContourVariable *" + mangledVarName + ";");
		}else if (var instanceof VolVariable){
			out.println("\tVolumeVariable *" + mangledVarName + ";");
		}else if (var instanceof ReservedVariable){
		}else if (var instanceof Constant){
		}else if (var instanceof Function){
		}else{
			throw new Exception("unknown identifier type '"+var.getClass().getName()+"' for identifier: "+var.getName());
		}	
	}		  	
	out.println("};");
}


/**
 * This method was created by a SmartGuide.
 * @param printWriter java.io.PrintWriter
 */
public void writeImplementation(java.io.PrintWriter out) throws Exception {
	out.println("//---------------------------------------------");
	out.println("//  class " + getClassName());
	out.println("//---------------------------------------------");
	writeConstructor(out);
	out.println("");
	writeResolveReferences(out);
	out.println("");
	writeContourFunction(out,"getContourReactionRate", getEquation().getRateExpression());
	out.println("");
	writeContourFunction(out,"getContourDiffusionRate", new Expression(0.0));
	out.println("");
	try {
		double value = getEquation().getInitialExpression().evaluateConstant();
	}catch (Exception e){
		writeContourFunction(out,"getInitialValue", getEquation().getInitialExpression());
	}
	out.println("");
}
}