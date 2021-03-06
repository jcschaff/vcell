/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package org.vcell.sbml;

import java.io.File;
import java.io.IOException;

import cbit.vcell.solver.SolverException;

public interface SBMLSolver {
	public File solve(String filePrefix, File outDir, String sbmlText, SimSpec testSpec) throws IOException, SolverException, SbmlException;
	public String getResultsFileColumnDelimiter();
}
