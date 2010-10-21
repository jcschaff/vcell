package cbit.vcell.geometry.gui;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.Hashtable;

import javax.swing.JTable;

import org.vcell.util.gui.DialogUtils;


import cbit.gui.AutoCompleteSymbolFilter;
import cbit.gui.ScopedExpression;
import cbit.vcell.client.PopupGenerator;
import cbit.vcell.client.task.AsynchClientTask;
import cbit.vcell.client.task.ClientTaskDispatcher;
import cbit.vcell.geometry.AnalyticSubVolume;
import cbit.vcell.geometry.Geometry;
import cbit.vcell.geometry.GeometrySpec;
import cbit.vcell.geometry.SubVolume;
import cbit.vcell.model.ReservedSymbol;
import cbit.vcell.parser.ASTFuncNode;
import cbit.vcell.parser.Expression;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.parser.SymbolTableEntry;
/**
 * Insert the type's description here.
 * Creation date: (2/23/01 10:52:36 PM)
 * @author: 
 */
public class GeometrySubVolumeTableModel extends javax.swing.table.AbstractTableModel implements java.beans.PropertyChangeListener {
	private final int COLUMN_NAME = 0;
	private final int COLUMN_VALUE = 1;
	private String LABELS[] = { "Name", "Value" };
	protected transient java.beans.PropertyChangeSupport propertyChange;
	private Geometry fieldGeometry = null;
	private AutoCompleteSymbolFilter autoCompleteSymbolFilter = null;
	private JTable ownerTable = null;

/**
 * ReactionSpecsTableModel constructor comment.
 */
public GeometrySubVolumeTableModel(JTable table) {
	super();
	ownerTable = table;
}


/**
 * The addPropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().addPropertyChangeListener(listener);
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}



/**
 * Insert the method's description here.
 * Creation date: (2/24/01 12:24:35 AM)
 * @return java.lang.Class
 * @param column int
 */
public Class<?> getColumnClass(int column) {
	switch (column){
		case COLUMN_NAME:{
			return SubVolume.class;
		}
		case COLUMN_VALUE:{
			return ScopedExpression.class;
		}
		default:{
			return Object.class;
		}
	}
}


/**
 * getColumnCount method comment.
 */
public int getColumnCount() {
	return LABELS.length;
}


/**
 * Insert the method's description here.
 * Creation date: (2/24/01 12:24:35 AM)
 * @return java.lang.Class
 * @param column int
 */
public String getColumnName(int column) {
	if (column<0 || column>=getColumnCount()){
		throw new RuntimeException("GeometrySubVolumeTableModel.getColumnName(), column = "+column+" out of range ["+0+","+(getColumnCount()-1)+"]");
	}
	return LABELS[column];
}


/**
 * Gets the geometry property (cbit.vcell.geometry.Geometry) value.
 * @return The geometry property value.
 * @see #setGeometry
 */
public Geometry getGeometry() {
	return fieldGeometry;
}


/**
 * Accessor for the propertyChange field.
 */
protected java.beans.PropertyChangeSupport getPropertyChange() {
	if (propertyChange == null) {
		propertyChange = new java.beans.PropertyChangeSupport(this);
	};
	return propertyChange;
}


/**
 * getRowCount method comment.
 */
public int getRowCount() {
	if (getGeometry()==null){
		return 0;
	}else{
		return getGeometry().getGeometrySpec().getNumSubVolumes();
	}
}


/**
 * getValueAt method comment.
 */
public Object getValueAt(int row, int col) {
	try {
		if (row<0 || row>=getRowCount()){
			throw new RuntimeException("GeometrySubVolumeTableModel.getValueAt(), row = "+row+" out of range ["+0+","+(getRowCount()-1)+"]");
		}
		if (col<0 || col>=getColumnCount()){
			throw new RuntimeException("GeometrySubVolumeTableModel.getValueAt(), column = "+col+" out of range ["+0+","+(getColumnCount()-1)+"]");
		}
		SubVolume subVolume = getGeometry().getGeometrySpec().getSubVolumes(row);
		switch (col){
			case COLUMN_NAME:{
				return subVolume;
			}
			case COLUMN_VALUE:{
				if (subVolume instanceof AnalyticSubVolume){
					return new ScopedExpression(((AnalyticSubVolume)subVolume).getExpression(), ReservedSymbol.X.getNameScope(), true, 
							autoCompleteSymbolFilter);
				}else{
					return null;
				}
			}
			default:{
				return null;
			}
		}
	} catch (Exception ex) {
		ex.printStackTrace(System.out);
		return null;
	}
}


/**
 * The hasListeners method was generated to support the propertyChange field.
 */
public synchronized boolean hasListeners(java.lang.String propertyName) {
	return getPropertyChange().hasListeners(propertyName);
}


/**
 * Insert the method's description here.
 * Creation date: (2/24/01 12:27:46 AM)
 * @return boolean
 * @param rowIndex int
 * @param columnIndex int
 */
public boolean isCellEditable(int rowIndex, int columnIndex) {
	if (columnIndex == COLUMN_NAME){
		return true;
	}else if (columnIndex == COLUMN_VALUE && getGeometry()!=null){
		SubVolume subVolume = getGeometry().getGeometrySpec().getSubVolumes(rowIndex);
		//
		// the "value" column is only editable if it is an expression for a AnalyticSubVolume
		//
		return (subVolume instanceof AnalyticSubVolume);
	}else{
		return false;
	}
}


/**
	 * This method gets called when a bound property is changed.
	 * @param evt A PropertyChangeEvent object describing the event source 
	 *   and the property that has changed.
	 */
public void propertyChange(java.beans.PropertyChangeEvent evt) {
	if (evt.getSource() == this && evt.getPropertyName().equals("geometry")) {
		fireTableStructureChanged();
	}
	if (evt.getSource() instanceof GeometrySpec && evt.getPropertyName().equals("subVolumes")) {
		fireTableDataChanged();
	}
	if (evt.getSource() instanceof SubVolume) {
		fireTableRowsUpdated(0,getRowCount()-1);
	}
}


/**
 * The removePropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().removePropertyChangeListener(listener);
}

/**
 * Sets the geometry property (cbit.vcell.geometry.Geometry) value.
 * @param geometry The new value for the property.
 * @see #getGeometry
 */
public void setGeometry(Geometry geometry) {
	Geometry oldValue = fieldGeometry;
	if (oldValue != null){
		oldValue.getGeometrySpec().removePropertyChangeListener(this);
		for (SubVolume sv : oldValue.getGeometrySpec().getSubVolumes()) {
			sv.removePropertyChangeListener(this);
		}
	}
	fieldGeometry = geometry;
	if (fieldGeometry != null){
		fieldGeometry.getGeometrySpec().addPropertyChangeListener(this);
		for (SubVolume sv : fieldGeometry.getGeometrySpec().getSubVolumes()) {
			sv.addPropertyChangeListener(this);
		}
		autoCompleteSymbolFilter = new AutoCompleteSymbolFilter() {
			public boolean accept(SymbolTableEntry ste) {
				int dimension = fieldGeometry.getDimension();
				if (ste.equals(ReservedSymbol.X) || dimension > 1 && ste.equals(ReservedSymbol.Y) || dimension > 2 && ste.equals(ReservedSymbol.Z)) {
					return true;
				}
				return false;
			}
			public boolean acceptFunction(String funcName) {
				return true;
			}	   
		};
	}
	firePropertyChange("geometry", oldValue, fieldGeometry);

	fireTableDataChanged();
}


public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	if (rowIndex<0 || rowIndex>=getRowCount()){
		throw new RuntimeException("GeometrySubVolumeTableModel.setValueAt(), row = "+rowIndex+" out of range ["+0+","+(getRowCount()-1)+"]");
	}
	if (columnIndex<0 || columnIndex>=getColumnCount()){
		throw new RuntimeException("GeometrySubVolumeTableModel.setValueAt(), column = "+columnIndex+" out of range ["+0+","+(getColumnCount()-1)+"]");
	}
	final SubVolume subVolume = getGeometry().getGeometrySpec().getSubVolumes(rowIndex);
	try {
		switch (columnIndex){
			case COLUMN_NAME:{
				final String newName = (String)aValue;
				subVolume.setName(newName);
				AsynchClientTask task1 = new AsynchClientTask("changing the name", AsynchClientTask.TASKTYPE_NONSWING_BLOCKING) {
					@Override
					public void run(Hashtable<String, Object> hashTable) throws Exception {
						getGeometry().precomputeAll();
					}
				};
				ClientTaskDispatcher.dispatch(ownerTable, new Hashtable<String, Object>(), new AsynchClientTask[] {task1}, false);
				break;
			}
			case COLUMN_VALUE:{
				if (subVolume instanceof AnalyticSubVolume){
					final AnalyticSubVolume analyticSubVolume = (AnalyticSubVolume)subVolume;
					if (aValue instanceof ScopedExpression){
						throw new RuntimeException("unexpected value type ScopedExpression");
					}else if (aValue instanceof String) {
						final String newExpressionString = (String)aValue;
						analyticSubVolume.setExpression(new Expression(newExpressionString));
						AsynchClientTask task1 = new AsynchClientTask("changing the expression", AsynchClientTask.TASKTYPE_NONSWING_BLOCKING) {
							@Override
							public void run(Hashtable<String, Object> hashTable) throws Exception {
								getGeometry().precomputeAll();
							}
						};
						ClientTaskDispatcher.dispatch(ownerTable, new Hashtable<String, Object>(), new AsynchClientTask[] {task1}, false);
					}
				}
				break;
			}
		}
	}catch (Exception e){
		e.printStackTrace(System.out);
		DialogUtils.showErrorDialog(ownerTable, e.getMessage(), e);
	}
}

}