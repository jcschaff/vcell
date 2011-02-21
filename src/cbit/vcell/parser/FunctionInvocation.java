package cbit.vcell.parser;

import cbit.vcell.parser.ASTFuncNode.FunctionType;

public class FunctionInvocation {
	private String functionName = null;
	private FunctionType functionId;
	private Expression[] arguments = null;
	private Expression functionExpression = null;
	private SymbolTableFunctionEntry symbolTableFunctionEntry = null;
	
	FunctionInvocation(ASTFuncNode funcNode){
		functionName = funcNode.getName();
		functionId = funcNode.getFunction();
		int numChildren = funcNode.jjtGetNumChildren();
		arguments = new Expression[numChildren];
		for (int i=0;i<numChildren;i++){
			arguments[i] = new Expression((SimpleNode)funcNode.jjtGetChild(i).copyTree());
		}
		functionExpression = new Expression((SimpleNode)funcNode.copyTree());
		symbolTableFunctionEntry = funcNode.getSymbolTableFunctionEntry();
	}

	public String getFunctionName() {
		return functionName;
	}

	public FunctionType getFunctionId() {
		return functionId;
	}

	public Expression[] getArguments(){
		return arguments;
	}

	public Expression getFunctionExpression() {
		return functionExpression;
	}
	
	public SymbolTableFunctionEntry getSymbolTableFunctionEntry() {
		return symbolTableFunctionEntry;
	}
	
	public boolean equals(Object obj){
		if (obj instanceof FunctionInvocation){
			FunctionInvocation fi = (FunctionInvocation)obj;
			return functionExpression.equals(fi.functionExpression);
		}
		return false;
	}

	public int hashCode(){
		return functionExpression.hashCode();
	}

}
