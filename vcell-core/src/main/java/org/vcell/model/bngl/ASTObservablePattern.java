/* Generated By:JJTree: Do not edit this line. ASTObservablePattern.java */

package org.vcell.model.bngl;

public class ASTObservablePattern extends SimpleNode {
	
	private String compartment = null;
	private String lengthEqualStr = null;
	private String lengthGreaterStr = null;

  public ASTObservablePattern(int id) {
    super(id);
  }

  public ASTObservablePattern(BNGLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(BNGLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

public void setCompartment(String compartment) {
	this.compartment = compartment;
}

public String getCompartment(){
	return this.compartment;
}

@Override
public String toBNGL(){
	if (compartment!=null){
		return "@"+compartment+":"+jjtGetChild(0).toBNGL();
	}else{
		return jjtGetChild(0).toBNGL();
	}
}

public void setLengthEqual(String image) {
	lengthEqualStr = image;
}
public String getLengthEqual() {
	return lengthEqualStr;
}
public void setLengthGreater(String image) {
	lengthGreaterStr = image;
}
public String getLengthGreater() {
	return lengthGreaterStr;
}

}
