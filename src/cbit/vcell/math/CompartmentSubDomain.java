package cbit.vcell.math;
import java.util.Enumeration;

import org.vcell.util.CommentStringTokenizer;
import org.vcell.util.Compare;
import org.vcell.util.Matchable;
import cbit.vcell.parser.Expression;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CompartmentSubDomain extends SubDomain {
	public final static int NON_SPATIAL_PRIORITY = -1;
	private final static int MAX_LEVELS_OF_MEMBRANE_NESTING = 100;
	private int priority = NON_SPATIAL_PRIORITY;
	
	private BoundaryConditionType boundaryConditionTypeXp = BoundaryConditionType.getDIRICHLET();
	private BoundaryConditionType boundaryConditionTypeXm = BoundaryConditionType.getDIRICHLET();
	private BoundaryConditionType boundaryConditionTypeYp = BoundaryConditionType.getDIRICHLET();
	private BoundaryConditionType boundaryConditionTypeYm = BoundaryConditionType.getDIRICHLET();
	private BoundaryConditionType boundaryConditionTypeZp = BoundaryConditionType.getDIRICHLET();
	private BoundaryConditionType boundaryConditionTypeZm = BoundaryConditionType.getDIRICHLET();

/**
 * This method was created by a SmartGuide.
 * @param name java.lang.String
 */
public CompartmentSubDomain (String name, int argPriority) {
	super(name);
	this.priority = argPriority;
}


/**
 * This method was created by a SmartGuide.
 * @param name java.lang.String
 */
CompartmentSubDomain (String name, MathDescription mathDesc, CommentStringTokenizer tokens) throws cbit.vcell.parser.ExpressionException, MathException {
	super(name);
	read(mathDesc,tokens);
}


/**
 * This method was created in VisualAge.
 * @return boolean
 * @param object java.lang.Object
 */
public boolean compareEqual(Matchable object) {
	if (!super.compareEqual0(object)){
		return false;
	}
	CompartmentSubDomain csd = null;
	if (!(object instanceof CompartmentSubDomain)){
		return false;
	}else{
		csd = (CompartmentSubDomain)object;
	}
	//
	// compare priority
	//
	if (priority!=csd.priority){
		return false;
	}
	//
	// compare boundaryConditions
	//
	if (!Compare.isEqual(boundaryConditionTypeXp,csd.boundaryConditionTypeXp)){
		return false;
	}
	if (!Compare.isEqual(boundaryConditionTypeXm,csd.boundaryConditionTypeXm)){
		return false;
	}
	if (!Compare.isEqual(boundaryConditionTypeYp,csd.boundaryConditionTypeYp)){
		return false;
	}
	if (!Compare.isEqual(boundaryConditionTypeYm,csd.boundaryConditionTypeYm)){
		return false;
	}
	if (!Compare.isEqual(boundaryConditionTypeZp,csd.boundaryConditionTypeZp)){
		return false;
	}
	if (!Compare.isEqual(boundaryConditionTypeZm,csd.boundaryConditionTypeZm)){
		return false;
	}

	return true;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public BoundaryConditionType getBoundaryConditionXm() {
	return boundaryConditionTypeXm;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public BoundaryConditionType getBoundaryConditionXp() {
	return boundaryConditionTypeXp;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public BoundaryConditionType getBoundaryConditionYm() {
	return boundaryConditionTypeYm;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public BoundaryConditionType getBoundaryConditionYp() {
	return boundaryConditionTypeYp;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public BoundaryConditionType getBoundaryConditionZm() {
	return boundaryConditionTypeZm;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public BoundaryConditionType getBoundaryConditionZp() {
	return boundaryConditionTypeZp;
}


/**
 * This method was created by a SmartGuide.
 * @return int
 */
public int getPriority() {
	return priority;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public String getVCML(int spatialDimension) {
	StringBuffer buffer = new StringBuffer();
	buffer.append(VCML.CompartmentSubDomain+" "+getName()+" {\n");
	if (priority != -1){
		buffer.append("\t"+VCML.Priority+" "+priority+"\n");
	}	
	if (spatialDimension>=1){
		buffer.append("\t"+VCML.BoundaryXm+"\t "+boundaryConditionTypeXm.toString()+"\n");
		buffer.append("\t"+VCML.BoundaryXp+"\t "+boundaryConditionTypeXp.toString()+"\n");
	}
	if (spatialDimension>=2){
		buffer.append("\t"+VCML.BoundaryYm+"\t "+boundaryConditionTypeYm.toString()+"\n");
		buffer.append("\t"+VCML.BoundaryYp+"\t "+boundaryConditionTypeYp.toString()+"\n");
	}
	if (spatialDimension==3){
		buffer.append("\t"+VCML.BoundaryZm+"\t "+boundaryConditionTypeZm.toString()+"\n");
		buffer.append("\t"+VCML.BoundaryZp+"\t "+boundaryConditionTypeZp.toString()+"\n");
	}
	Enumeration<Equation> enum1 = getEquations();
	while (enum1.hasMoreElements()){
		Equation equ = enum1.nextElement();
		buffer.append(equ.getVCML());
	}	
	if (getFastSystem()!=null){
		buffer.append(getFastSystem().getVCML());
	}
	//Var initial conditions
	if(getVarIniConditions().size()>0)
	{
		
		for(VarIniCondition vic : getVarIniConditions()){
			buffer.append(vic.getVCML());
		}
		buffer.append("\n");
	}
	//particle initial conditions
	for (ParticleProperties pp : getParticleProperties()){
		buffer.append(pp.getVCML(spatialDimension));
		buffer.append("\n");
	}
	//Jump processes
	for (ParticleJumpProcess particleJumpProcess : getParticleJumpProcesses()){
		buffer.append(particleJumpProcess.getVCML());
		buffer.append("\n");
	}
	//Jump processes
	if(getJumpProcesses().size()>0)
	{
		for(JumpProcess jp : getJumpProcesses()){
			buffer.append(jp.getVCML());
		}
	}	
	buffer.append("}\n");
	return buffer.toString();		
}


/**
 * This method was created by a SmartGuide.
 * @param tokens java.util.StringTokenizer
 * @exception java.lang.Exception The exception description.
 */
private void read(MathDescription mathDesc, CommentStringTokenizer tokens) throws MathException, cbit.vcell.parser.ExpressionException {
	String token = null;
	token = tokens.nextToken();
	if (!token.equalsIgnoreCase(VCML.BeginBlock)){
		throw new MathFormatException("unexpected token "+token+" expecting "+VCML.BeginBlock);
	}			
	while (tokens.hasMoreTokens()){
		token = tokens.nextToken();
		if (token.equalsIgnoreCase(VCML.EndBlock)){
			break;
		}			
		if (token.equalsIgnoreCase(VCML.Handle)){
			//
			// throw away "handle information" deprecated
			//
			token = tokens.nextToken();
			//handle = Integer.valueOf(token).intValue();
			continue;
		}
		if (token.equalsIgnoreCase(VCML.Priority)){
			token = tokens.nextToken();
			priority = Integer.valueOf(token).intValue();
			continue;
		}
		if (token.equalsIgnoreCase(VCML.BoundaryXm)){
			String type = tokens.nextToken();
			boundaryConditionTypeXm = new BoundaryConditionType(type);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.BoundaryXp)){
			String type = tokens.nextToken();
			boundaryConditionTypeXp = new BoundaryConditionType(type);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.BoundaryYm)){
			String type = tokens.nextToken();
			boundaryConditionTypeYm = new BoundaryConditionType(type);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.BoundaryYp)){
			String type = tokens.nextToken();
			boundaryConditionTypeYp = new BoundaryConditionType(type);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.BoundaryZm)){
			String type = tokens.nextToken();
			boundaryConditionTypeZm = new BoundaryConditionType(type);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.BoundaryZp)){
			String type = tokens.nextToken();
			boundaryConditionTypeZp = new BoundaryConditionType(type);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.PdeEquation)){
			token = tokens.nextToken();
			boolean bSteady = false;
			if (token.equals(VCML.Steady)) {
				bSteady = true;
				token = tokens.nextToken();
			}
			Variable var = mathDesc.getVariable(token);
			if (var == null){
				throw new MathFormatException("variable "+token+" not defined");
			}	
			if (!(var instanceof VolVariable)){
				throw new MathFormatException("variable "+token+" not a VolumeVariable");
			}	
			PdeEquation pde = new PdeEquation((VolVariable)var, bSteady);
			pde.read(tokens);
			addEquation(pde);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.OdeEquation)){
			token = tokens.nextToken();
			Variable var = mathDesc.getVariable(token);
			if (var == null){
				throw new MathFormatException("variable "+token+" not defined");
			}	
			if (!(var instanceof VolVariable)){
				throw new MathFormatException("variable "+token+" not a VolumeVariable");
			}	
			OdeEquation ode = new OdeEquation((VolVariable)var,null,null);
			ode.read(tokens);
			addEquation(ode);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.VolumeRegionEquation)){
			token = tokens.nextToken();
			Variable var = mathDesc.getVariable(token);
			if (var == null){
				throw new MathFormatException("variable "+token+" not defined");
			}	
			if (!(var instanceof VolumeRegionVariable)){
				throw new MathFormatException("variable "+token+" not a VolumeRegionVariable");
			}	
			VolumeRegionEquation vre = new VolumeRegionEquation((VolumeRegionVariable)var,null);
			vre.read(tokens);
			addEquation(vre);
			continue;
		}			
		/**
		 * ParticleJumpProcess name A B {
		 *    MacroscopicRateConstant dkdkdk;
		 *    Action destroy A
		 *    Action destroy B
		 *    Action create C
		 * }
		 */
		if (token.equalsIgnoreCase(VCML.ParticleJumpProcess)){
			ParticleJumpProcess particleJumpProcess = ParticleJumpProcess.fromVCML(mathDesc, tokens);
			addParticleJumpProcess(particleJumpProcess);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.ParticleProperties)){
			ParticleProperties pp = new ParticleProperties(mathDesc, tokens);
			addParticleProperties(pp);
			continue;
		}			
		if (token.equalsIgnoreCase(VCML.FastSystem)){
			FastSystem fs = new FastSystem(mathDesc);
			fs.read(tokens);
			setFastSystem(fs);
			continue;
		}	
		//Variable initial conditions as count		
		if (token.equalsIgnoreCase(VCML.VarIniCount_Old) || token.equalsIgnoreCase(VCML.VarIniCount))
		{
			token = tokens.nextToken();
			Variable var = mathDesc.getVariable(token);
			if (var == null){
				throw new MathFormatException("variable "+token+" not defined");
			}	
			if (!(var instanceof StochVolVariable)){
				throw new MathFormatException("variable "+token+" not a Stochastic Volume Variable");
			}
			
			Expression varIniExp = MathFunctionDefinitions.fixFunctionSyntax(tokens);
			try{
				varIniExp.bindExpression(mathDesc);
			}catch(Exception ex){
				ex.printStackTrace(System.out);
				throw new MathException(ex.getMessage());
			}
			VarIniCount vic= new VarIniCount(var,varIniExp);
			addVarIniCondition(vic);
			
			continue;
		}
		//Variable inital conditions as concentration
		if (token.equalsIgnoreCase(VCML.VarIniPoissonExpectedCount))
		{
			token = tokens.nextToken();
			Variable var = mathDesc.getVariable(token);
			if (var == null){
				throw new MathFormatException("variable "+token+" not defined");
			}	
			if (!(var instanceof StochVolVariable)){
				throw new MathFormatException("variable "+token+" not a Stochastic Volume Variable");
			}
			
			Expression varIniExp = MathFunctionDefinitions.fixFunctionSyntax(tokens);
			try{
				varIniExp.bindExpression(mathDesc);
			}catch(Exception ex){
				ex.printStackTrace(System.out);
				throw new MathException(ex.getMessage());
			}
			VarIniPoissonExpectedCount vic= new VarIniPoissonExpectedCount(var,varIniExp);
			addVarIniCondition(vic);
			
			continue;
		}
		//Jump processes 
		if (token.equalsIgnoreCase(VCML.JumpProcess))
		{
			JumpProcess jump = null;
			token = tokens.nextToken();
			String name=token;
			token = tokens.nextToken();
			if(!token.equalsIgnoreCase(VCML.BeginBlock))
				throw new MathFormatException("unexpected token "+token+" expecting "+VCML.BeginBlock);
			token = tokens.nextToken();	
			if(token.equalsIgnoreCase(VCML.ProbabilityRate))
			{
				Expression probExp = MathFunctionDefinitions.fixFunctionSyntax(tokens);
				//check if probability functions contain "t", which is not allowed.
				Expression extProb = MathUtilities.substituteFunctions(probExp,mathDesc).flatten();
				String[] symbols = extProb.getSymbols();
				if(symbols != null)
				{
					for(int i=0; i<symbols.length; i++)
					{
						if(symbols[i].equals("t"))
						{
							throw new MathFormatException("Unexpected symbol \'t\'  in probability rate of jump process "+name+". Probability rate should not be a function of t.");
						}	
					}
				}
				probExp.bindExpression(mathDesc);
				jump = new JumpProcess(name,probExp);
				addJumpProcess(jump);
			}
			else {
				throw new MathFormatException("unexpected identifier "+token);
			}

			if(jump != null)
			{
				while (tokens.hasMoreTokens())
				{
					token = tokens.nextToken();
					if (token.equalsIgnoreCase(VCML.EndBlock)){
						break;
					}
					if (token.equalsIgnoreCase(VCML.Action))
					{
						token = tokens.nextToken();
						Variable var = mathDesc.getVariable(token);
						if (var == null){
							throw new MathFormatException("variable "+token+" not defined");
						}	
						if (!(var instanceof StochVolVariable)){
							throw new MathFormatException("variable "+token+" not a Stochastic Volume Variable");
						}
						String opera = tokens.nextToken();
						if (!opera.equals(Action.ACTION_INC)){
							throw new MathFormatException("expected 'INC' for action, found "+opera);
						}
						Expression exp = MathFunctionDefinitions.fixFunctionSyntax(tokens);
						try{
							exp.bindExpression(mathDesc);
						}
						catch(Exception ex){
							ex.printStackTrace(System.out);
							throw new MathException(ex.getMessage());
						}
						Action action = Action.createIncrementAction(var,exp);
						jump.addAction(action);
					}
					else throw new MathFormatException("unexpected identifier "+token);
				}
			}
			
			continue;
		}	
		throw new MathFormatException("unexpected identifier "+token);
	}	
		
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public void setBoundaryConditionXm(BoundaryConditionType bc) {
	boundaryConditionTypeXm = bc;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public void setBoundaryConditionXp(BoundaryConditionType bc) {
	if (boundaryConditionTypeXm.isPERIODIC() && !bc.isPERIODIC()) {
		throw new RuntimeException("Xm and Xp must both have periodic boundary condition");
	}
	boundaryConditionTypeXp = bc;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public void setBoundaryConditionYm(BoundaryConditionType bc) {
	boundaryConditionTypeYm = bc;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public void setBoundaryConditionYp(BoundaryConditionType bc) {
	if (boundaryConditionTypeYm.isPERIODIC() && !bc.isPERIODIC()) {
		throw new RuntimeException("Ym and Yp must both have periodic boundary condition");
	}	
	boundaryConditionTypeYp = bc;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public void setBoundaryConditionZm(BoundaryConditionType bc) {
	boundaryConditionTypeZm = bc;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public void setBoundaryConditionZp(BoundaryConditionType bc) {
	if (boundaryConditionTypeZm.isPERIODIC() && !bc.isPERIODIC()) {
		throw new RuntimeException("Zm and Zp must both have periodic boundary condition");
	}	
	boundaryConditionTypeZp = bc;
}


/**
 * Insert the method's description here.
 * Creation date: (10/12/2002 4:05:51 PM)
 */
void setPriority(int argPriority) {
	priority = argPriority;
}
}