package cbit.vcell.solvers;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.*;


import cbit.vcell.math.*;
import cbit.vcell.parser.*;
import cbit.vcell.solver.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CppClassCoderVolumeRegionVarContext extends CppClassCoderAbstractVarContext {
	protected MembraneSubDomain membraneSubDomainsOwned[] = new MembraneSubDomain[0];

/**
 * VarContextCppCoder constructor comment.
 * @param name java.lang.String
 */
protected CppClassCoderVolumeRegionVarContext(CppCoderVCell argCppCoderVCell,
												Equation argEquation,
												CompartmentSubDomain argVolumeSubDomain,
												Simulation argSimulation, 
												String argParentClass) throws Exception
{
	super(argCppCoderVCell,argEquation,argVolumeSubDomain,argSimulation,argParentClass);
	
	Vector<MembraneSubDomain> membraneSubDomainOwnedList = new Vector<MembraneSubDomain>();
	MembraneSubDomain membranes[] = argSimulation.getMathDescription().getMembraneSubDomains(argVolumeSubDomain);
	for (int i = 0; i < membranes.length; i++){
		//
		// determine membrane "owner" for reasons of code generation (owner compartment is that which has a greater priority ... now this is arbitrary)
		//
		CompartmentSubDomain inside = membranes[i].getInsideCompartment();
		CompartmentSubDomain outside = membranes[i].getOutsideCompartment();
		CompartmentSubDomain membraneOwner = null;
		if (inside.getPriority() > outside.getPriority()){
			membraneOwner = inside;
		}else if (inside.getPriority() < outside.getPriority()){
			membraneOwner = outside;
		}else{ // inside.getPriority() == outside.getPriority()
			throw new RuntimeException("CompartmentSubDomains '"+inside.getName()+"' and '"+outside.getName()+"' have same priority ("+inside.getPriority()+")");
		}
		if (membraneOwner == argVolumeSubDomain){
			membraneSubDomainOwnedList.add(membranes[i]);
		}
	}
	this.membraneSubDomainsOwned = (MembraneSubDomain[])org.vcell.util.BeanUtils.getArray(membraneSubDomainOwnedList,MembraneSubDomain.class);
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.model.Feature
 */
public CompartmentSubDomain getCompartmentSubDomain() {
	return (CompartmentSubDomain)getSubDomain();
}


/**
 * Insert the method's description here.
 * Creation date: (6/22/2004 3:07:51 PM)
 * @return cbit.vcell.math.Variable[]
 */
protected Variable[] getRequiredVariables() throws Exception {

	//
	// 
	//
	Variable requiredVariables[] = super.getRequiredVariables();
	if (getEquation() instanceof PdeEquation){
		for (int i = 0;membraneSubDomainsOwned!=null && i < membraneSubDomainsOwned.length; i++){
			JumpCondition jumpCondition = membraneSubDomainsOwned[i].getJumpCondition((VolVariable)getEquation().getVariable());
			Enumeration<Variable> enumJC = jumpCondition.getRequiredVariables(getSimulation());
			requiredVariables = (Variable[])org.vcell.util.BeanUtils.addElements(requiredVariables,(Variable[])org.vcell.util.BeanUtils.getArray(enumJC,Variable.class));
		}
	}
	Vector<Variable> uniqueVarList = new Vector<Variable>();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		if (var instanceof InsideVariable){
			InsideVariable insideVar = (InsideVariable)var;
			VolVariable volVar = (VolVariable)getSimulation().getVariable(insideVar.getVolVariableName());
			if (!uniqueVarList.contains(volVar)){
				uniqueVarList.addElement(volVar);
			}	
		}else if (var instanceof OutsideVariable){
			OutsideVariable outsideVar = (OutsideVariable)var;
			VolVariable volVar = (VolVariable)getSimulation().getVariable(outsideVar.getVolVariableName());
			if (!uniqueVarList.contains(volVar)){
				uniqueVarList.addElement(volVar);
			}
		}else{
			if (!uniqueVarList.contains(var)){
				uniqueVarList.addElement(var);
			}
		}
	}

	return (Variable[])org.vcell.util.BeanUtils.getArray(uniqueVarList,Variable.class);
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeConstructor(java.io.PrintWriter out) throws Exception {
	out.println(getClassName()+"::"+getClassName()+"(Feature *Afeature, string& AspeciesName)");
	out.println(": "+getParentClassName()+"(Afeature,AspeciesName)");
	out.println("{");
	try {
		Expression ic = getEquation().getInitialExpression();
		ic.bindExpression(getSimulation());
		double value = ic.evaluateConstant();
		out.println("\tinitialValue = new double;");
		out.println("\t*initialValue = "+value+";");
	}catch (Exception e){
		out.println("\tinitialValue = NULL;");
	}	
	out.println();
	Variable requiredVariables[] = getRequiredVariables();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		if (var instanceof VolVariable || var instanceof MemVariable ||
			var instanceof VolumeRegionVariable || var instanceof MembraneRegionVariable){
			out.println("\t" + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + " = NULL;");
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
	out.println("\t"+getClassName() + "(Feature *feature, string& speciesName);");
	out.println("\tvirtual void resolveReferences(Simulation *sim);");

	try {
		Expression ic = getEquation().getInitialExpression();
		ic.bindExpression(getSimulation());
		double value = ic.evaluateConstant();
	}catch (Exception e){
		out.println("\tvirtual double getInitialValue(long volumeIndex);");
	}
	out.println("\tvirtual double getUniformRate(VolumeRegion *region);");
	out.println("\tvirtual double getReactionRate(long volumeIndex);");
	out.println("\tvirtual void getFlux(MembraneElement *element,double *inFlux, double *outFlux);");
	out.println();
	out.println("\tprivate:");
	Variable requiredVariables[] = getRequiredVariables();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		String mangledVarName = CppClassCoder.getEscapedFieldVariableName_C(var.getName());
		if (var instanceof VolVariable){
			out.println("\tVolumeVariable *" + mangledVarName + ";");
		}else if (var instanceof MemVariable){
			out.println("\tMembraneVariable *" + mangledVarName + ";");
		}else if (var instanceof MembraneRegionVariable){
			out.println("\tMembraneRegionVariable *" + mangledVarName + ";");
		}else if (var instanceof VolumeRegionVariable){
			out.println("\tVolumeRegionVariable *" + mangledVarName + ";");
		}else if (var instanceof ReservedVariable){
		}else if (var instanceof Constant){
		}else if (var instanceof Function){
		}else{
			throw new Exception("unknown identifier type '" + var.getClass().getName() + "' for identifier: " + var.getName());
		}	
	}		  	
	out.println("};");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeGetFlux(java.io.PrintWriter out, String functionName) throws Exception {
	//
	// Explanation of PRIORITIES and INSIDE/OUTSIDE wrt Code Generation:
	// -----------------------------------------------------------------
	//
	// due to code generation requirements, the compartment with the higher priority must be the "inside" compartment
	// and the "inside" compartment is where the flux is defined in the C++ library.
	//
	//
	// The math description specifies "inside" and "outside" compartments locally for each membrane
	//
	//     MembraneSubDomain inside_compartment outside_compartment {
	//         ...
	//     }
	//
	// which can contradict the priority-based determination of inside-outside.
	//
	// in these cases:
	//   1) the "influx" and "outflux" expressions must be reversed, and
	//   2) the var_INSIDE and var_OUTSIDE variable definitions must be exchanged (substituted)
	//
	
	out.println("void "+getClassName()+"::"+functionName+"(MembraneElement *element,double *inFlux, double *outFlux)");
	out.println("{");
	
	if (getEquation() instanceof VolumeRegionEquation){

		//
		// if zero or one membranes, write out single inFlux/outFlux expression
		//
		boolean bFlipInsideOutside = (membraneSubDomainsOwned.length == 0 || membraneSubDomainsOwned[0].getOutsideCompartment() == getCompartmentSubDomain());
		Expression inFluxExp = ((VolumeRegionEquation)getEquation()).getMembraneRateExpression();
		Expression inFluxExp_substituted = getSimulation().substituteFunctions(inFluxExp).flatten();
		//
		// get totalExpression (composite expression to combine symbols)
		// then write out dependencies
		//
		writeMembraneFunctionDeclarations(out,"element", inFluxExp_substituted, bFlipInsideOutside, "\t");
		out.println("\t*inFlux = " + inFluxExp_substituted.infix_C() + ";");
		out.println("\t*outFlux = 0.0;");
	} else {
		throw new Exception("VolumeRegionEquation is required.");
	}
	out.println("}");
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
	writeVolumeRegionFunction(out,"getUniformRate", ((VolumeRegionEquation)getEquation()).getUniformRateExpression());
	out.println("");
	writeVolumeFunction(out,"getReactionRate", ((VolumeRegionEquation)getEquation()).getVolumeRateExpression());
	out.println("");
	writeGetFlux(out,"getFlux");
	out.println("");
	try {
		double value = getEquation().getInitialExpression().evaluateConstant();
	}catch (Exception e){
		writeVolumeFunction(out,"getInitialValue", getEquation().getInitialExpression());
	}
	out.println("");
}
}