package cbit.vcell.opt;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import org.vcell.expression.ExpressionException;
import org.vcell.expression.ExpressionFactory;
import org.vcell.expression.IExpression;

import cbit.vcell.parser.*;
/**
 * Insert the type's description here.
 * Creation date: (3/3/00 12:09:47 AM)
 * @author: 
 */
public class Constraint {
	private IExpression exp = null;
	private ConstraintType constraintType = null;

/**
 * Constraint constructor comment.
 * @param name java.lang.String
 * @param exp cbit.vcell.parser.Expression
 */
public Constraint(ConstraintType constraintType, IExpression exp) {
	if (exp==null){
		throw new IllegalArgumentException("expression cannot be null");
	}
	if (exp.isLogical() || exp.isRelational()){
		throw new RuntimeException("constraint expression should evaluate to a real, not a boolean");
	}
	this.exp = exp;
	this.constraintType = constraintType;
}


/**
 * Insert the method's description here.
 * Creation date: (3/3/00 12:34:17 AM)
 * @return cbit.vcell.opt.ConstraintType
 */
public ConstraintType getConstraintType() {
	return constraintType;
}


/**
 * Insert the method's description here.
 * Creation date: (3/3/00 12:33:43 AM)
 * @return cbit.vcell.parser.Expression
 */
public IExpression getExpression() {
	return exp;
}


/**
 * Insert the method's description here.
 * Creation date: (3/3/00 12:33:43 AM)
 * @return cbit.vcell.parser.Expression
 */
public IExpression getLogicalExpression() {

	try {
		if (constraintType.isEquality()) {
			return ExpressionFactory.createExpression(exp.infix()+" == 0");
		} else {
			return ExpressionFactory.createExpression(exp.infix()+" <= 0");
		}
	} catch (ExpressionException e) {
		e.printStackTrace(System.out);
		throw new RuntimeException("Error in exp : "+e.getMessage());
	}
}
}