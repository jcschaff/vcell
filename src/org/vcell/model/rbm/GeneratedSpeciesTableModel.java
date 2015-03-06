/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package org.vcell.model.rbm;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import org.vcell.pathway.BioPaxObject;
import org.vcell.relationship.ConversionTableRow;
import org.vcell.util.gui.AutoCompleteTableModel;
import org.vcell.util.gui.EditorScrollTable;
import org.vcell.util.gui.GuiUtils;

import cbit.vcell.bionetgen.BNGSpecies;
import cbit.vcell.client.desktop.biomodel.VCellSortTableModel;
import cbit.vcell.parser.AutoCompleteSymbolFilter;
import cbit.vcell.parser.SymbolTable;

@SuppressWarnings("serial")
public class GeneratedSpeciesTableModel extends VCellSortTableModel<GeneratedSpeciesTableRow> 
	implements  PropertyChangeListener, AutoCompleteTableModel{

	public static final int colCount = 2;
	public static final int iColIndex = 0;
	public static final int iColExpression = 1;
	
	// filtering variables 
	protected static final String PROPERTY_NAME_SEARCH_TEXT = "searchText";
	protected String searchText = null;

	private BNGSpecies[] speciess;
	private ArrayList<GeneratedSpeciesTableRow> allGeneratedSpeciesList;
	
	protected transient java.beans.PropertyChangeSupport propertyChange;

	public GeneratedSpeciesTableModel(EditorScrollTable table) {
		super(table, new String[] {"Index", "Expression"});
		setMaxRowsPerPage(1000);
	}
	
	public Class<?> getColumnClass(int iCol) {
		switch (iCol){		
			case iColIndex:{
				return String.class;
			}case iColExpression:{
				return String.class;
			}
		}
	return Object.class;
	}
	
	public Object getValueAt(int iRow, int iCol) {
		GeneratedSpeciesTableRow speciesTableRow = getValueAt(iRow);
		switch(iCol) {
			case iColIndex:{
				return speciesTableRow.getIndex();
			}
			case iColExpression:{
				return speciesTableRow.getExpression();
			}
			default:{
				return null;
			}
		}
	}
	
	public boolean isCellEditable(int iRow, int iCol) {
		return false;
		}
	
	public void setValueAt(Object valueNew, int iRow, int iCol) {
		return;
	}
	
	public AutoCompleteSymbolFilter getAutoCompleteSymbolFilter(final int row, final int column) {
		return null;
	}
	
	public Set<String> getAutoCompletionWords(int row, int iCol) {
		return null;
	}
	
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		
	}
	
	public Comparator<GeneratedSpeciesTableRow> getComparator(final int col, final boolean ascending) {
		return new Comparator<GeneratedSpeciesTableRow>() {
		    public int compare(GeneratedSpeciesTableRow o1, GeneratedSpeciesTableRow o2){
		    	return 0;
		    }
		};
	}
	
	public void setSearchText(String newValue) {
		if (searchText == newValue) {
			return;
		}
		searchText = newValue;
		refreshData();
	}
	
	private void refreshData() {
		allGeneratedSpeciesList = new ArrayList<GeneratedSpeciesTableRow>();
		
		for(int i = 0; i<speciess.length; i++) {
			BNGSpecies species = speciess[i];
			GeneratedSpeciesTableRow newRow = createTableRow(species, i+1, species.toStringShort());
			allGeneratedSpeciesList.add(newRow);
		}
		// apply text search function for particular columns
		ArrayList<GeneratedSpeciesTableRow> speciesObjectList = new ArrayList<GeneratedSpeciesTableRow>();
		if (searchText == null || searchText.length() == 0) {
			speciesObjectList.addAll(allGeneratedSpeciesList);
		} else {
			String lowerCaseSearchText = searchText.toLowerCase();
			for (GeneratedSpeciesTableRow rs : allGeneratedSpeciesList){
				if (rs.getExpression().toLowerCase().contains(lowerCaseSearchText) ) {
					speciesObjectList.add(rs);
				}
			}
		}
		setData(speciesObjectList);
		GuiUtils.flexResizeTableColumns(ownerTable);
	}
	
	private GeneratedSpeciesTableRow createTableRow(BNGSpecies species, int index, String interactionLabel) {
		GeneratedSpeciesTableRow row = new GeneratedSpeciesTableRow(species);
		
		row.setIndex(index+" ");
		row.setExpression(interactionLabel);
		return row;
	}
	
	
	public void setSpecies(BNGSpecies[] newValue) {
		if (speciess == newValue) {
			return;
		}
		speciess = newValue;
		refreshData();
	}
	public ArrayList<GeneratedSpeciesTableRow> getTableRows() {
		return null;
	}

	@Override
	public String checkInputValue(String inputValue, int row, int column) {
		return null;
	}

	@Override
	public SymbolTable getSymbolTable(int row, int column) {
		return null;
	}
}
