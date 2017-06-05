/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package org.vcell.optimization;

import info.aduna.xml.XMLUtil;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.vcell.optimization.thrift.OptProblem;

import cbit.util.xml.XmlUtil;
import cbit.vcell.mapping.SimulationContext.MathMappingCallback;
import cbit.vcell.math.Function;
import cbit.vcell.math.FunctionColumnDescription;
import cbit.vcell.math.MathException;
import cbit.vcell.math.ODESolverResultSetColumnDescription;
import cbit.vcell.math.RowColumnResultSet;
import cbit.vcell.modelopt.ParameterEstimationTask;
import cbit.vcell.opt.OptSolverResultSet;
import cbit.vcell.opt.OptimizationException;
import cbit.vcell.opt.OptimizationResultSet;
import cbit.vcell.parser.Expression;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.resource.NativeLib;
import cbit.vcell.resource.ResourceUtil;
import cbit.vcell.solver.SimulationSymbolTable;
import cbit.vcell.solver.ode.ODESolverResultSet;
import cbit.vcell.xml.XmlHelper;


public class CopasiOptimizationSolver {	
	static {
		NativeLib.COPASI.load( );
	}
	private static native String solve(String optProblemXml, CopasiOptSolverCallbacks optSolverCallbacks);
	
	public static OptimizationResultSet solve(ParameterEstimationTaskSimulatorIDA parestSimulator, ParameterEstimationTask parameterEstimationTask, CopasiOptSolverCallbacks optSolverCallbacks, MathMappingCallback mathMappingCallback) 
							throws IOException, ExpressionException, OptimizationException {
		try {
			File dir = new File("C:\\temp\\ggg");
			String prefix = "testing_"+Math.abs(new Random().nextInt(10000));
			
			File sbmlFile = new File(dir,prefix+".sbml.xml");
			File dataFile = new File(dir,prefix+".csv");
			File optProblemThriftFile = new File(dir,prefix+".optprob.bin");
//			File optProblemXMLFile = new File(dir,prefix+".xml");
			File optResultFile = new File(dir,prefix+".optresult.xml");
			
			//
			// Setup Python COPASI opt problem and write to disk
			//
			OptProblem optProblem = CopasiServicePython.makeOptProblem(parameterEstimationTask, sbmlFile, dataFile);
			CopasiServicePython.writeOptProblem(optProblemThriftFile, optProblem);

			//
			// JNI (C++) input XML file
			//
			Element optProblemXML = OptXmlWriter.getCoapsiOptProblemDescriptionXML(parameterEstimationTask,mathMappingCallback);
			String inputXML = XmlUtil.xmlToString(optProblemXML);
			System.out.println(XmlUtil.beautify(inputXML));
			
			
			//
			// run Python COPASI opt problem
			//
			CopasiServicePython.runCopasiPython(optProblemThriftFile, optResultFile);
			Element copasiOptResultsXML = XmlUtil.readXML(optResultFile).getRootElement();
			String copasiOptResultsString = XmlUtil.beautify(XmlUtil.xmlToString(copasiOptResultsXML));
			OptSolverResultSet copasiOptSolverResultSet = OptXmlReader.getOptimizationResultSet(copasiOptResultsString);
			String[] copasiParameterNames = copasiOptSolverResultSet.getParameterNames();
			double[] copasiParameterVals = copasiOptSolverResultSet.getBestEstimates();
			RowColumnResultSet copasiRcResultSet = parestSimulator.getRowColumnRestultSetByBestEstimations(parameterEstimationTask, copasiParameterNames, copasiParameterVals);
			OptimizationResultSet copasiOptimizationResultSet = new OptimizationResultSet(copasiOptSolverResultSet, copasiRcResultSet);
			
			
			//
			// run JNI COPASI Solver
			//
//			org.apache.commons.io.FileUtils.write(optProblemXMLFile, XmlUtil.beautify(inputXML));
			String jniOptResultsXML = solve(inputXML, optSolverCallbacks);
			OptSolverResultSet jniOptSolverResultSet = OptXmlReader.getOptimizationResultSet(jniOptResultsXML);
			//get ode solution by best estimates
			String[] jniParameterNames = jniOptSolverResultSet.getParameterNames();
			double[] jniParameterVals = jniOptSolverResultSet.getBestEstimates();
			RowColumnResultSet jniRcResultSet = parestSimulator.getRowColumnRestultSetByBestEstimations(parameterEstimationTask, jniParameterNames, jniParameterVals);
			OptimizationResultSet jniOptimizationResultSet = new OptimizationResultSet(jniOptSolverResultSet, jniRcResultSet);

			System.out.println("-----------SOLUTION FROM PYTHON---------------\n"+XmlUtil.beautify(copasiOptResultsString));
			System.out.println("-----------SOLUTION FROM JNI------------------\n"+XmlUtil.beautify(jniOptResultsXML));

			
//			return jniOptimizationResultSet;
			return copasiOptimizationResultSet;
		} catch (Throwable e){
			e.printStackTrace(System.out);
			throw new OptimizationException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		}
	}
		
	private static ODESolverResultSet getOdeSolverResultSet(RowColumnResultSet rcResultSet, SimulationSymbolTable simSymbolTable, String[] parameterNames, double[] parameterValues){
		//
		// get simulation results - copy from RowColumnResultSet into OdeSolverResultSet
		//
		
		ODESolverResultSet odeSolverResultSet = new ODESolverResultSet();
		for (int i = 0; i < rcResultSet.getDataColumnCount(); i++){
			odeSolverResultSet.addDataColumn(new ODESolverResultSetColumnDescription(rcResultSet.getColumnDescriptions(i).getName()));
		}
		for (int i = 0; i < rcResultSet.getRowCount(); i++){
			odeSolverResultSet.addRow(rcResultSet.getRow(i));
		}
		//
		// add appropriate Function columns to result set
		//
		Function functions[] = simSymbolTable.getFunctions();
		for (int i = 0; i < functions.length; i++){
			if (SimulationSymbolTable.isFunctionSaved(functions[i])){
				Expression exp1 = new Expression(functions[i].getExpression());
				try {
					exp1 = simSymbolTable.substituteFunctions(exp1).flatten();
					//
					// substitute in place all "optimization parameter" values.
					//
					for (int j = 0; parameterNames!=null && j < parameterNames.length; j++) {
						exp1.substituteInPlace(new Expression(parameterNames[j]), new Expression(parameterValues[j]));
					}
				} catch (MathException e) {
					e.printStackTrace(System.out);
					throw new RuntimeException("Substitute function failed on function "+functions[i].getName()+" "+e.getMessage());
				} catch (ExpressionException e) {
					e.printStackTrace(System.out);
					throw new RuntimeException("Substitute function failed on function "+functions[i].getName()+" "+e.getMessage());
				}
				
				try {
					FunctionColumnDescription cd = new FunctionColumnDescription(exp1.flatten(),functions[i].getName(), null, functions[i].getName(), false);
					odeSolverResultSet.addFunctionColumn(cd);
				}catch (ExpressionException e){
					e.printStackTrace(System.out);
				}
			}
		}
		return odeSolverResultSet;
	}
	
}
