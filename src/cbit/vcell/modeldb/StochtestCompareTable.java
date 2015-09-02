/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.modeldb;
import cbit.sql.Field;
import cbit.sql.Table;
/**
 * This type was created in VisualAge.
 */
public class StochtestCompareTable extends Table {
	private static final String TABLE_NAME = "stochtestcompare";
	public static final String REF_TYPE = "REFERENCES " + TABLE_NAME + "(" + Table.id_ColumnName + ")";

	public final Field stochtestrunref1			= new Field("stochtestrunref1",			"integer",	"NOT NULL " + StochtestRunTable.REF_TYPE + " ON DELETE CASCADE");
	public final Field stochtestrunref2			= new Field("stochtestrunref2",			"integer",	"NOT NULL " + StochtestRunTable.REF_TYPE + " ON DELETE CASCADE");
	public final Field results					= new Field("results",					"varchar2(4000)",	"");
	public final Field status					= new Field("status",					"varchar2(32)",		"");

	private final Field fields[] = {stochtestrunref1,stochtestrunref2,results,status};
	
	public static final StochtestCompareTable table = new StochtestCompareTable();

/**
 * ModelTable constructor comment.
 */
private StochtestCompareTable() {
	super(TABLE_NAME);
	addFields(fields);
}
}
