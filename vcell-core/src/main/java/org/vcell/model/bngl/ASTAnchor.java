/* Generated By:JJTree: Do not edit this line. ASTAnchor.java */

package org.vcell.model.bngl;

public class ASTAnchor extends SimpleNode {
	
	private String name = null;
	
	public ASTAnchor(int id) {
		super(id);
	}
	public ASTAnchor(BNGLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(BNGLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	@Override
	public String toBNGL() {
		return null;
	}
}