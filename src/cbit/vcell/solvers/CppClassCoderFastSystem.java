package cbit.vcell.solvers;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.*;


import cbit.vcell.field.FieldFunctionArguments;
import cbit.vcell.mapping.FastSystemAnalyzer;
import cbit.vcell.math.*;
import cbit.vcell.parser.*;
import cbit.vcell.solver.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CppClassCoderFastSystem extends CppClassCoder {
	private Simulation simulation = null;
	private SubDomain subDomain = null;
	private FastSystemAnalyzer fs_analyzer = null;
/**
 * VarContextCppCoder constructor comment.
 * @param name java.lang.String
 */
protected CppClassCoderFastSystem(CppCoderVCell cppCoderVCell, FastSystem fastSystem,
								SubDomain subDomain,Simulation argSimulation,String parentClass) throws Exception
{
	super(cppCoderVCell,parentClass+subDomain.getName(), parentClass);
	this.simulation = argSimulation;
	this.subDomain = subDomain;
	this.fs_analyzer = new FastSystemAnalyzer(fastSystem, simulation);
}
/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.model.Feature
 */
public SubDomain getCompartment() {
	return subDomain;
}
/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeConstructor(java.io.PrintWriter out) throws Exception {
	out.println(getClassName()+"::"+getClassName()+"()");
	out.println(": "+getParentClassName()+"("+fs_analyzer.getNumIndependentVariables()+","+fs_analyzer.getNumDependentVariables()+")");
	out.println("{");
	out.println("\tsetTolerance(1e-7);");
	out.println();

	int varCount=0;
	Enumeration<Variable> enum_vars = fs_analyzer.getIndependentVariables();
	while (enum_vars.hasMoreElements()){
		Variable var = enum_vars.nextElement();
		out.println("\tpVars[" + varCount + "] = " + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + " = NULL;");
		varCount++;
	}
	out.println("");
	
	varCount=0;
	enum_vars = fs_analyzer.getDependentVariables();
	while (enum_vars.hasMoreElements()){
		Variable var = (Variable)enum_vars.nextElement();
		out.println("\tpDependentVars[" + varCount + "] = " + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + " = NULL;");
		varCount++;
	}
	out.println("");
	
	Enumeration<PseudoConstant> enum_pc = fs_analyzer.getPseudoConstants();
	while (enum_pc.hasMoreElements()){
		PseudoConstant pc = enum_pc.nextElement();
		out.println("\t" + CppClassCoder.getEscapedFieldVariableName_C(pc.getName()) + " = 0.0;");
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
	out.println("\t " + getClassName() + "();");
	out.println("\tvoid resolveReferences(Simulation *sim);");
	out.println("\tvoid initVars();");
	out.println("\tvoid updateDependentVars();");
	out.println("protected:");
	out.println("\tvoid updateMatrix();");
	out.println("private:");
	out.println("\tMesh *mesh;");
	out.println("\tSimulation *simulation;");

	Enumeration<Variable> enum_vars = fs_analyzer.getIndependentVariables();
	while (enum_vars.hasMoreElements()){
		Variable var = enum_vars.nextElement();
		out.println("\tVariable *" + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + ";");
	}
	enum_vars = fs_analyzer.getDependentVariables();
	while (enum_vars.hasMoreElements()){
		Variable var = enum_vars.nextElement();
		out.println("\tVariable *" + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + ";");
	}
	out.println("");

	Enumeration<PseudoConstant> enum_pc = fs_analyzer.getPseudoConstants();
	while (enum_pc.hasMoreElements()){
		PseudoConstant pc = enum_pc.nextElement();
		out.println("\tdouble " + CppClassCoder.getEscapedFieldVariableName_C(pc.getName()) + ";");
	}
	out.println("};");
}
/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeFastFunctionDeclarations(java.io.PrintWriter out, Expression exp, String volumeIndexString) throws Exception {

	if (exp == null){
		throw new Exception("null expression");
	}	

	boolean wc_defined = false;
	Enumeration<Variable> enum1 = MathUtilities.getRequiredVariables(exp, fs_analyzer);

	while (enum1.hasMoreElements()){
		Variable var = enum1.nextElement();
		if (var instanceof ReservedVariable){
			//
			// define reserved symbols (x,y,z,t)
			//
			ReservedVariable rv = (ReservedVariable)var;
			String mangledVarName = CppClassCoder.getEscapedLocalVariableName_C(rv.getName());
			if (rv.isTIME()){
				out.println("\tdouble " + mangledVarName + " = simulation->getTime_sec();");
			}else if (rv.isX()){
				if (!wc_defined){
					out.println("\tWorldCoord wc = mesh->getVolumeWorldCoord("+volumeIndexString+");");
					wc_defined = true;
				}	
				out.println("\tdouble " + mangledVarName + " = wc.x;");
			}else if (rv.isY()){
				if (!wc_defined){
					out.println("\tWorldCoord wc = mesh->getVolumeWorldCoord("+volumeIndexString+");");
					wc_defined = true;
				}	
				out.println("\tdouble " + mangledVarName + " = wc.y;");
			}else if (rv.isZ()){
				if (!wc_defined){
					out.println("\tWorldCoord wc = mesh->getVolumeWorldCoord("+volumeIndexString+");");
					wc_defined = true;
				}	
				out.println("\tdouble " + mangledVarName + " = wc.z;");
			}		
		}		
	}	
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
	writeInitVars(out,"initVars");
	out.println("");
	writeUpdateDependentVars(out,"updateDependentVars");
	out.println("");
	writeUpdateMatrix(out,"updateMatrix");
	out.println("");
	out.println("");
}
/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeInitVars(java.io.PrintWriter out, String functionName) throws Exception {

	out.println("void "+getClassName()+"::"+functionName+"()");
	out.println("{");

	int varCount=0;
	Enumeration<Variable> enum1 = fs_analyzer.getIndependentVariables();

	while (enum1.hasMoreElements()){
		Variable var = enum1.nextElement();
		out.println("\tdouble " + CppClassCoder.getEscapedLocalVariableName_C(var.getName()) + " = " + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + "->getCurr(currIndex);");
		out.println("\tsetX(" + varCount + "," + CppClassCoder.getEscapedLocalVariableName_C(var.getName()) + ");");
		varCount++;
	}
	enum1 = fs_analyzer.getDependentVariables();
	while (enum1.hasMoreElements()){
		Variable var = (Variable)enum1.nextElement();
		out.println("\tdouble " + CppClassCoder.getEscapedLocalVariableName_C(var.getName()) + " = " + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + "->getCurr(currIndex);");
	}
	
	// Write out the reserved variables, (x,y,z,t) in case they are used in the fast invariants 
	String mangledVarXName = CppClassCoder.getEscapedLocalVariableName_C(ReservedVariable.X.getName());
	String mangledVarYName = CppClassCoder.getEscapedLocalVariableName_C(ReservedVariable.Y.getName());
	String mangledVarZName = CppClassCoder.getEscapedLocalVariableName_C(ReservedVariable.Z.getName());
	String mangledVarTName = CppClassCoder.getEscapedLocalVariableName_C(ReservedVariable.TIME.getName());

	out.println("\tdouble " +  mangledVarTName + " = simulation->getTime_sec();");
	out.println("\tWorldCoord wc = mesh->getVolumeWorldCoord(currIndex);");
	out.println("\tdouble " +  mangledVarXName + " = wc.x;");
	out.println("\tdouble " +  mangledVarYName + " = wc.y;");
	out.println("\tdouble " +  mangledVarZName + " = wc.z;");
	
	int invariantCount=0;
	Enumeration<PseudoConstant> enum2 = fs_analyzer.getPseudoConstants();
	while (enum2.hasMoreElements()){
		PseudoConstant pc = (PseudoConstant)enum2.nextElement();
		out.println("\t" + CppClassCoder.getEscapedFieldVariableName_C(pc.getName()) + " = " + simulation.substituteFunctions(pc.getPseudoExpression()).flatten().infix_C()+";");
		invariantCount++;
	}
		
	out.println("}");
	out.println("");
}
/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeResolveReferences(java.io.PrintWriter out) throws Exception {
	out.println("void "+getClassName()+"::resolveReferences(Simulation *sim)");
	out.println("{");
	out.println("\tASSERTION(sim);");
	out.println("\tthis->mesh = sim->getMesh();");
	out.println("\tthis->simulation = sim;");
	out.println("");
	Enumeration<Variable> enum1 = fs_analyzer.getIndependentVariables();
	int varCount=0;
	while (enum1.hasMoreElements()){
		Variable var = enum1.nextElement();
		out.println("\t" + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + " = sim->getVariableFromName(\"" + var.getName() + "\");");
		out.println("\tif (" + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + "==NULL){");
		out.println("\t\tthrow(\"could not resolve '" + var.getName() + "'\\n\");");
		out.println("\t}");
		out.println("\tpVars[" + varCount + "] = " + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + ";");
		out.println();
		varCount++;
	}		  	
	enum1 = fs_analyzer.getDependentVariables();
	varCount=0;
	while (enum1.hasMoreElements()){
		Variable var = enum1.nextElement();
		out.println("\t" + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + " = sim->getVariableFromName(\"" + var.getName() + "\");");
		out.println("\tif (" + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + "==NULL){");
		out.println("\t\tthrow(\"could not resolve '" + var.getName() + "'\\n\");");
		out.println("\t}");
		out.println("\tpDependentVars[" + varCount + "] = " + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + ";");
		out.println("");
		varCount++;
	}		  	
	out.println("}");
	out.println();
}
/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeUpdateDependentVars(java.io.PrintWriter out, String functionName) throws Exception {

	out.println("void "+getClassName()+"::"+functionName+"()");
	out.println("{");

	int varCount=0;
	Enumeration<Variable> enum1 = fs_analyzer.getIndependentVariables();
	while (enum1.hasMoreElements()){
		Variable var = (Variable)enum1.nextElement();
		out.println("\tdouble " + CppClassCoder.getEscapedLocalVariableName_C(var.getName()) + " = getX(" + varCount + ");");
		varCount++;
	}

	// --- Write out the reserved variables, (x,y,z,t) in case they are used in the fast invariants 
	String mangledVarXName = CppClassCoder.getEscapedLocalVariableName_C(ReservedVariable.X.getName());
	String mangledVarYName = CppClassCoder.getEscapedLocalVariableName_C(ReservedVariable.Y.getName());
	String mangledVarZName = CppClassCoder.getEscapedLocalVariableName_C(ReservedVariable.Z.getName());
	String mangledVarTName = CppClassCoder.getEscapedLocalVariableName_C(ReservedVariable.TIME.getName());

	out.println("\tdouble " +  mangledVarTName + " = simulation->getTime_sec();");
	out.println("\tWorldCoord wc = mesh->getVolumeWorldCoord(currIndex);");
	out.println("\tdouble " +  mangledVarXName + " = wc.x;");
	out.println("\tdouble " +  mangledVarYName + " = wc.y;");
	out.println("\tdouble " +  mangledVarZName + " = wc.z;");

	Enumeration<PseudoConstant> enum_pc = fs_analyzer.getPseudoConstants();
	while (enum_pc.hasMoreElements()){
		PseudoConstant pc = enum_pc.nextElement();
		out.println("\tdouble " + CppClassCoder.getEscapedLocalVariableName_C(pc.getName()) + " = " + CppClassCoder.getEscapedFieldVariableName_C(pc.getName()) + ";");
	}	

	Enumeration<Expression> enum_exp = fs_analyzer.getDependencyExps();
	Enumeration<Variable> enum_var = fs_analyzer.getDependentVariables();
	while (enum_exp.hasMoreElements()){
		Expression exp = enum_exp.nextElement();
		Variable depVar = enum_var.nextElement();
		exp.bindExpression(fs_analyzer);
		exp = MathUtilities.substituteFunctions(exp, fs_analyzer).flatten();
		out.println("\t" + CppClassCoder.getEscapedFieldVariableName_C(depVar.getName()) + "->setCurr(currIndex," + exp.infix_C() + ");");
	}
	
	out.println("}");
	out.println();
}
/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeUpdateMatrix(java.io.PrintWriter out, String functionName) throws Exception {

	out.println("void "+getClassName()+"::"+functionName+"()");
	out.println("{");

	//
	// collect all expressions into one and declare x,y,z,t if necessary
	//
	Expression expTemp = new Expression("0.0;");
	Enumeration<Expression> enum_fre = fs_analyzer.getFastRateExpressions();
	while (enum_fre.hasMoreElements()){
		Expression fre = enum_fre.nextElement();
		expTemp = Expression.add(expTemp,fre);
	}
	writeFastFunctionDeclarations(out,expTemp,"currIndex");	
	
	int varCount=0;
	Enumeration<Variable> enum1 = fs_analyzer.getIndependentVariables();
	while (enum1.hasMoreElements()){
		Variable var = enum1.nextElement();
		out.println("\tdouble " + CppClassCoder.getEscapedLocalVariableName_C(var.getName()) + " = getX(" + varCount + ");");
		varCount++;
	}

	Enumeration<PseudoConstant> enum_pc = fs_analyzer.getPseudoConstants();
	while (enum_pc.hasMoreElements()){
		PseudoConstant pc = enum_pc.nextElement();
		out.println("\tdouble " + CppClassCoder.getEscapedLocalVariableName_C(pc.getName()) + " = " + CppClassCoder.getEscapedFieldVariableName_C(pc.getName()) + ";");
	}
	
	FieldFunctionArguments[] fieldFuncArgs = simulation.getMathDescription().getFieldFunctionArguments();

	for (int i = 0; fieldFuncArgs != null && i < fieldFuncArgs.length; i ++) {
		String localvarname = CppClassCoder.getEscapedLocalFieldVariableName_C(fieldFuncArgs[i]);
		String globalvarname = CppClassCoder.getEscapedGlobalFieldVariableName_C(fieldFuncArgs[i]);
		out.println("\tdouble " + localvarname + " = " + globalvarname + "->getData()[currIndex];");	
	}
	
	int frCount=0;
	enum_fre = fs_analyzer.getFastRateExpressions();
	while (enum_fre.hasMoreElements()){
		Expression fre = (Expression)enum_fre.nextElement();
		varCount=0;
		Enumeration<Variable> enum_var = fs_analyzer.getIndependentVariables();
		while (enum_var.hasMoreElements()){
			Variable var = enum_var.nextElement();
			Expression exp = MathUtilities.substituteFunctions(fre, fs_analyzer).flatten();
			Expression differential = exp.differentiate(var.getName());
			differential.bindExpression(fs_analyzer);
			out.println("\tsetMatrix("+frCount+", "+varCount+", "+differential.flatten().infix_C()+");");
			varCount++;
		}
		Expression exp = Expression.negate(fre);
		exp = MathUtilities.substituteFunctions(exp, fs_analyzer);
		out.println("\tsetMatrix("+frCount+", "+varCount+", "+exp.flatten().infix_C()+");");
		frCount++;
		out.println();
	}
	out.println("}");
	out.println();
}
}
