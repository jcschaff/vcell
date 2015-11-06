/* Generated By:JJTree: Do not edit this line. ASTFloatNode.java */

package org.vcell.model.bngl;

public class ASTFloatNode extends SimpleNode {
  public double value;


public ASTFloatNode(int id) {
    super(id);
  }

  public ASTFloatNode(BNGLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(BNGLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

@Override
public String toBNGL() {
	// TODO Auto-generated method stub
	return Double.toString(value);
}
}