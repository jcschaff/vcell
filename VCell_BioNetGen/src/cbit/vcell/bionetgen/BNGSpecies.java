package cbit.vcell.bionetgen;

import org.vcell.expression.IExpression;

/**
 * Insert the type's description here.
 * Creation date: (1/13/2006 5:28:44 PM)
 * @author: Jim Schaff
 */
public abstract class BNGSpecies implements org.vcell.util.Matchable {

	//
	// Storing the index of a species in the network file generated by BioNetGen. 
	// This index is required for filling out reactions listed in the network file.
	//
	private int networkFileIndex = 0;			
	private String name = null;
	private IExpression concentration = null;

/**
 * BNGSpecies constructor comment.
 */
public BNGSpecies(String argName, IExpression argConc, int argNetwkFileIndx) {
	super();
	name = argName;
	concentration = argConc;
	networkFileIndex = argNetwkFileIndx;
}


/**
 * compareEqual method comment.
 */
public boolean compareEqual(org.vcell.util.Matchable object) {
	if (this == object) {
		return (true);
	}
	if (object != null && object instanceof BNGSpecies) {
		BNGSpecies species = (BNGSpecies) object;
		//
		// check for true equality
		//
		if (!org.vcell.util.Compare.isEqual(getName(),species.getName())){
			return false;
		}
		if (!org.vcell.util.Compare.isEqualOrNull(getConcentration(),species.getConcentration())){
			return false;
		}
		return true;
	}
	return false;

}


/**
 * Insert the method's description here.
 * Creation date: (1/13/2006 5:30:41 PM)
 * @return java.lang.String
 */
public IExpression getConcentration() {
	return concentration;
}


/**
 * Insert the method's description here.
 * Creation date: (1/13/2006 5:30:41 PM)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}


/**
 * Insert the method's description here.
 * Creation date: (1/13/2006 5:30:41 PM)
 * @return java.lang.String
 */
public int getNetworkFileIndex() {
	return networkFileIndex;
}


/**
 * Insert the method's description here.
 * Creation date: (1/13/2006 5:31:44 PM)
 * @return boolean
 */
public abstract boolean isWellDefined();


/**
 * Insert the method's description here.
 * Creation date: (1/13/2006 5:31:44 PM)
 * @return boolean
 */
public abstract BNGSpecies[] parseBNGSpeciesName();


/**
 * Insert the method's description here.
 * Creation date: (1/13/2006 5:30:41 PM)
 * @param newName java.lang.String
 */
public void setConcentration(IExpression newConc) {
	concentration = newConc;
}


/**
 * Insert the method's description here.
 * Creation date: (1/13/2006 5:30:41 PM)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}


/**
 * Insert the method's description here.
 * Creation date: (1/13/2006 5:30:41 PM)
 * @param newName java.lang.String
 */
public String toString() {
	return new String(getNetworkFileIndex() + ";\t" + getName() + ";\t" + getConcentration().infix());
}
}